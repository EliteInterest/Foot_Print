package cn.gisdata.footprint.module.foot.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.base.BaseFragment;
import cn.gisdata.footprint.module.foot.bean.FootFileBean;
import cn.gisdata.footprint.module.foot.bean.FootRouteTextInfo;
import cn.gisdata.footprint.module.foot.func.view.FootRecordView;
import cn.gisdata.footprint.module.foot.mvp.contract.FootContract;
import cn.gisdata.footprint.module.foot.mvp.model.FootModel;
import cn.gisdata.footprint.module.foot.mvp.presenter.FootPresenter;
import cn.gisdata.footprint.module.map.func.util.BaiduMapUtil;
import cn.gisdata.footprint.module.map.func.util.GpsUtil;
import cn.gisdata.footprint.module.map.ui.MapFragment;
import cn.gisdata.footprint.module.my.ui.PreviewActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.esri.core.geometry.Point;
import com.google.gson.Gson;
import com.zx.zxutils.util.ZXFragmentUtil;
import com.zx.zxutils.util.ZXSharedPrefUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class FootFragment extends BaseFragment<FootPresenter, FootModel> implements FootContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_title_bar)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_title_save)
    TextView tvAction;
    @BindView(R.id.ll_route_edit)
    LinearLayout llRouteEdite;
    @BindView(R.id.et_route_name)
    EditText etRouteName;
    @BindView(R.id.et_route_detail)
    EditText etRouteDetail;
    public static MapFragment mapFragment;
    private ZXSharedPrefUtil zxSharedPrefUtil;

    public static FootFragment newInstance() {
        FootFragment fragment = new FootFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_foot;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mapFragment = MapFragment.newInstance();
        ZXFragmentUtil.addFragment(getFragmentManager(), mapFragment, R.id.fm_map);
        zxSharedPrefUtil = new ZXSharedPrefUtil();

        if (!mSharedPrefUtil.contains(ConstStrings.FootFiles) || mSharedPrefUtil.getList(ConstStrings.FootFiles) == null) {
            List<FootFileBean> footFiles = new ArrayList<>();
            mSharedPrefUtil.putList(ConstStrings.FootFiles, footFiles);
        }

        mRxManager.on("commitRoute", (Action1<Boolean>) aBoolean -> {
            if (aBoolean) {
                rlTitle.setVisibility(View.VISIBLE);
                llRouteEdite.setVisibility(View.VISIBLE);
                tvAction.setText("保存");
                tvTitle.setText("路线编辑");
            } else {
                rlTitle.setVisibility(View.GONE);
                llRouteEdite.setVisibility(View.GONE);
                mapFragment.showFootView(true);

            }

        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                mRxManager.post("commitRoute", false);
                break;
            case R.id.tv_title_save://分享
                if (etRouteName.getText().toString().length() == 0) {
                    showToast("路线名称不能为空");
                    break;
                }

                showLoading("正在上传...");

                FootRecordView footRecordView = mapFragment.getFootRecordView();
                //upload file
                FootRouteTextInfo footRouteTextInfo = new FootRouteTextInfo();
                FootRouteTextInfo.FootRouteTextInfoFootprint footRouteTextInfoFootprint = new FootRouteTextInfo.FootRouteTextInfoFootprint();
                footRouteTextInfoFootprint.setUserId(mSharedPrefUtil.getString("userId", ""));
                footRouteTextInfoFootprint.setName(etRouteName.getText().toString());
                footRouteTextInfoFootprint.setDesc(etRouteDetail.getText().toString());
                footRouteTextInfoFootprint.setMileage(Float.valueOf(zxSharedPrefUtil.getString("Mileage")));
                footRouteTextInfoFootprint.setConsumptionTime(zxSharedPrefUtil.getString("ConsumptionTime"));
                footRouteTextInfoFootprint.setStartTime(String.valueOf(zxSharedPrefUtil.getLong("record_start_time")));
                footRouteTextInfoFootprint.setEndTime(String.valueOf(System.currentTimeMillis()));
                footRouteTextInfo.setFootprint(footRouteTextInfoFootprint);

                List<FootRouteTextInfo.FootRouteTextInfoPointPositions> footRouteTextInfoPointPositionsList = new ArrayList<>();
                List<FootRouteTextInfo.FootRouteTextInfoBeanDetail> footRouteTextInfoBeanList = new ArrayList<>();
                List<FootRouteTextInfo.FootRouteSaveInfoDetail> footRouteSaveInfoList = new ArrayList<>();
                List<FootRouteTextInfo.FootRouteFileInfo> mediaFiles = new ArrayList<>();

                //first file is screenshot
                Location location = GpsUtil.getLocation(getActivity());
                String imageName = "screenshot" + System.currentTimeMillis() + ".png";
                //get the url of the map image
                String imgUrl = BaiduMapUtil.getStaticBitmapPath(location.getLongitude(), location.getLatitude());
                Glide.with(getActivity()).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int result = -2;
                        result = savePhotoToSDCard(resource, ConstStrings.getCachePath(), imageName);
                        switch (result) {
                            case 0:
                                File file = new File(ConstStrings.getCachePath() + imageName);
                                FootRouteTextInfo.FootRouteFileInfo fileInfo = new FootRouteTextInfo.FootRouteFileInfo();
                                fileInfo.setMediaFile(file);
                                fileInfo.setFileType(2);
                                mediaFiles.add(fileInfo);

                                List<FootFileBean> footFiles = zxSharedPrefUtil.getList(ConstStrings.FootFiles);
                                int index = 1;
                                for (FootFileBean bean : footFiles) {
                                    FootRouteTextInfo.FootRouteTextInfoPointPositions footRouteTextInfoPointPositions = new FootRouteTextInfo.FootRouteTextInfoPointPositions();
//                    String pointId = mSharedPrefUtil.getString("userId", "") + "-p" + index++;
                                    String pointId = UUID.randomUUID().toString().substring(0, 32) + "-p" + index++;
                                    footRouteTextInfoPointPositions.setPointId(pointId);
                                    footRouteTextInfoPointPositions.setAddr(bean.getFormatAddress());
                                    footRouteTextInfoPointPositions.setDesc(bean.getStreetAddress());
                                    String[] point = bean.getPoint().split(",");
                                    footRouteTextInfoPointPositions.setLongitude(Double.valueOf(point[0]));
                                    footRouteTextInfoPointPositions.setLatitude(Double.valueOf(point[1]));
                                    footRouteTextInfoPointPositions.setAltitude(Double.valueOf(point[2]));
                                    FootFileBean.Type type = bean.getType();
                                    int uploadType = 1;
                                    if (type == FootFileBean.Type.Text) {
                                        uploadType = 1;
                                        //文本
                                        FootRouteTextInfo.FootRouteTextInfoBeanDetail footRouteTextInfoBeanDetail = new FootRouteTextInfo.FootRouteTextInfoBeanDetail();
                                        footRouteTextInfoBeanDetail.setPointId(pointId);
                                        footRouteTextInfoBeanDetail.setTextName(bean.getTextName().toString());
                                        footRouteTextInfoBeanDetail.setTextDesc(bean.getDescription().toString());
                                        footRouteTextInfoBeanList.add(footRouteTextInfoBeanDetail);
                                    } else if (type == FootFileBean.Type.Camera) {
                                        uploadType = 2;
                                        //照片
                                        List<FootFileBean.PicBean> picBeanList = bean.getPicPaths();
                                        for (FootFileBean.PicBean bean1 : picBeanList) {
                                            //增加路径个点的说明
                                            FootRouteTextInfo.FootRouteSaveInfoDetail footRouteSaveInfoDetail = new FootRouteTextInfo.FootRouteSaveInfoDetail();
                                            footRouteSaveInfoDetail.setPointId(pointId);
                                            footRouteSaveInfoDetail.setDesc(bean1.getRemark());
                                            footRouteSaveInfoList.add(footRouteSaveInfoDetail);
                                            //增加file路径
                                            file = new File(ConstStrings.getCachePath() + bean1.getPath());
                                            fileInfo = new FootRouteTextInfo.FootRouteFileInfo();
                                            fileInfo.setMediaFile(file);
                                            fileInfo.setFileType(2);
                                            mediaFiles.add(fileInfo);
                                        }
                                    } else if (type == FootFileBean.Type.Vedio) {
                                        uploadType = 3;
                                        FootRouteTextInfo.FootRouteSaveInfoDetail footRouteSaveInfoDetail = new FootRouteTextInfo.FootRouteSaveInfoDetail();
                                        footRouteSaveInfoDetail.setPointId(pointId);
                                        footRouteSaveInfoDetail.setDesc(bean.getDescription());
                                        footRouteSaveInfoList.add(footRouteSaveInfoDetail);

                                        file = new File(ConstStrings.getCachePath() + bean.getVedioPath());
                                        fileInfo = new FootRouteTextInfo.FootRouteFileInfo();
                                        fileInfo.setMediaFile(file);
                                        fileInfo.setFileType(3);
                                        mediaFiles.add(fileInfo);
                                    }
                                    footRouteTextInfoPointPositions.setPointType(uploadType);
                                    footRouteTextInfoPointPositionsList.add(footRouteTextInfoPointPositions);
                                }

                                Map<String, Object> map = new HashMap<>();
                                //设置FootprintInfo域
                                footRouteTextInfo.setPointPositions(footRouteTextInfoPointPositionsList);
                                Gson gson = new Gson();
                                String jsonsStr = gson.toJson(footRouteTextInfo);
                                map.put("FootprintInfo", jsonsStr);

                                //设置TextInfo域
                                if (footRouteTextInfoBeanList.size() > 0) {
                                    FootRouteTextInfo.FootRouteTextInfoBean footRouteTextInfoBean = new FootRouteTextInfo.FootRouteTextInfoBean();
                                    footRouteTextInfoBean.setTextInfo(footRouteTextInfoBeanList);
                                    gson = new Gson();
                                    jsonsStr = gson.toJson(footRouteTextInfoBean);
                                    map.put("TextInfo", jsonsStr);
                                }

                                //设置PathInfo域
                                List<Point> pointList = zxSharedPrefUtil.getList("record_points");
                                List<List<Double>> pointStringList = new ArrayList<>();
                                for (Point point : pointList) {
                                    double x = point.getX();
                                    double y = point.getY();
                                    double z = point.getZ();
                                    List<Double> doubles = new ArrayList<>();
                                    doubles.add(x);
                                    doubles.add(y);
                                    doubles.add(z);
                                    pointStringList.add(doubles);
                                }

                                String pointJson = new Gson().toJson(pointStringList.toArray());
                                map.put("PathInfo", pointJson);

                                //设置SaveInfo域
                                if (footRouteSaveInfoList.size() > 0) {
                                    FootRouteTextInfo.FootRouteSaveInfo footRouteSaveInfo = new FootRouteTextInfo.FootRouteSaveInfo();
                                    footRouteSaveInfo.setTotalDesc("");
                                    footRouteSaveInfo.setMediaInfo(footRouteSaveInfoList);
                                    gson = new Gson();
                                    jsonsStr = gson.toJson(footRouteSaveInfo);
                                    map.put("SaveInfo", jsonsStr);
                                }

                                if (mediaFiles.size() > 0) {
                                    map.put("file", mediaFiles);
                                }

                                mPresenter.commitRoute(map);
                                return;

                            case -1:
                                dismissLoading();
                                showToast("获取当前位置图片出错，请重试！");
                                return;

                            case 1:
                                dismissLoading();
                                showToast("当前SD卡不可用，请检测设备!");
                                return;

                            default:
                                break;
                        }
                    }
                });
                break;
        }

    }

    @Override
    public void onRouteCommitResult(String url) {
        dismissLoading();
        llRouteEdite.setVisibility(View.GONE);
        mapFragment.clearSharedPref();
        PreviewActivity.startAction(getActivity(), false, "路径预览", url);
        mapFragment.resetMap();
        rlTitle.setVisibility(View.GONE);
        llRouteEdite.setVisibility(View.GONE);
        etRouteName.setText("");
        etRouteDetail.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mapFragment.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x01) {
            mapFragment.refreshPoints();
        } else if (resultCode == 0x02) {
            FootFileBean itemBean = (FootFileBean) data.getSerializableExtra("itemBean");
            PreviewActivity.startAction(getActivity(), false, "足迹预览", itemBean.getUrl());
        }
    }

    public static int savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
                return -1;
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
                return -1;
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
            return 0;
        }
        return 1;
    }

    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
