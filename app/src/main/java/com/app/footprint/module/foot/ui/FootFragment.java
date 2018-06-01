package com.app.footprint.module.foot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.foot.bean.FootFileBean;
import com.app.footprint.module.foot.bean.FootRouteTextInfo;
import com.app.footprint.module.foot.func.tool.ShareTool;
import com.app.footprint.module.foot.func.view.FootRecordView;
import com.app.footprint.module.foot.mvp.contract.FootContract;
import com.app.footprint.module.foot.mvp.model.FootModel;
import com.app.footprint.module.foot.mvp.presenter.FootPresenter;
import com.app.footprint.module.map.ui.MapFragment;
import com.esri.core.geometry.Point;
import com.google.gson.Gson;
import com.zx.zxutils.util.ZXFragmentUtil;
import com.zx.zxutils.util.ZXSharedPrefUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.cv_foot_preview)
    CardView cvPreview;
    @BindView(R.id.fm_map_preview)
    FrameLayout fmPreview;
    @BindView(R.id.tv_title_save)
    TextView tvAction;
    @BindView(R.id.ll_route_edit)
    LinearLayout llRouteEdite;
    @BindView(R.id.et_route_name)
    EditText etRouteName;
    @BindView(R.id.et_route_detail)
    EditText etRouteDetail;
    private MapFragment mapFragment;
    private WebViewFragment webViewFragment;
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
                tvAction.setText("分享");
            }

        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                if ("分享".equals(tvAction.getText().toString())) {
                    openFootPreview(null);
                } else if ("保存".equals(tvAction.getText().toString())) {
                    mRxManager.post("commitRoute", false);
                }
                break;
            case R.id.tv_title_save://分享
                if ("分享".equals(tvAction.getText().toString())) {
                    ShareTool.doShare(getActivity());
                } else if ("保存".equals(tvAction.getText().toString())) {
                    if (etRouteName.getText().toString().length() == 0) {
                        showToast("路线名称不能为空");
                        break;
                    }

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

                    List<FootFileBean> footFiles = zxSharedPrefUtil.getList(ConstStrings.FootFiles);
                    Log.i("wangwansheng", "footFiles size is " + footFiles.size());
                    int index = 1;
                    for (FootFileBean bean : footFiles) {
                        FootRouteTextInfo.FootRouteTextInfoPointPositions footRouteTextInfoPointPositions = new FootRouteTextInfo.FootRouteTextInfoPointPositions();
                        String pointId = mSharedPrefUtil.getString("userId", "") + "-p" + index++;
                        footRouteTextInfoPointPositions.setPointId(pointId);
                        footRouteTextInfoPointPositions.setAddr(bean.getTextName());
                        footRouteTextInfoPointPositions.setDesc(bean.getDescription());
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
                                File file = new File(ConstStrings.getCachePath() + bean1.getPath());
                                FootRouteTextInfo.FootRouteFileInfo fileInfo = new FootRouteTextInfo.FootRouteFileInfo();
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

                            File file = new File(ConstStrings.getCachePath() + bean.getVedioPath());
                            FootRouteTextInfo.FootRouteFileInfo fileInfo = new FootRouteTextInfo.FootRouteFileInfo();
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
                    Log.i("wangwansheng", "FootprintInfo json str is " + jsonsStr);

                    //设置TextInfo域
                    if (footRouteTextInfoBeanList.size() > 0) {
                        FootRouteTextInfo.FootRouteTextInfoBean footRouteTextInfoBean = new FootRouteTextInfo.FootRouteTextInfoBean();
                        footRouteTextInfoBean.setTextInfo(footRouteTextInfoBeanList);
                        gson = new Gson();
                        jsonsStr = gson.toJson(footRouteTextInfoBean);
                        map.put("TextInfo", jsonsStr);
                        Log.i("wangwansheng", "TextInfo json str is " + jsonsStr);
                    }

                    //设置PathInfo域
                    List<Point> pointList = zxSharedPrefUtil.getList("record_points");
                    List<String> pointStringList = new ArrayList<>();
                    for (Point point : pointList) {
                        double x = point.getX();
                        double y = point.getY();
                        double z = point.getZ();
                        pointStringList.add(x + "," + y + "," + z);
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
                        Log.i("wangwansheng", "SaveInfo json str is " + jsonsStr);
                    }

                    if (mediaFiles.size() > 0) {
                        map.put("file", mediaFiles);
                    }

                    mPresenter.commitRoute(map);
                }
                break;
        }
    }

    /**
     * 开启预览
     *
     * @param footFileBean
     */
    private void openFootPreview(FootFileBean footFileBean) {
        if (footFileBean != null) {
            tvAction.setText("分享");
            cvPreview.setVisibility(View.VISIBLE);
            fmPreview.setVisibility(View.VISIBLE);
            if (webViewFragment == null) {
                webViewFragment = WebViewFragment.newInstance(footFileBean.getUrl());
                ZXFragmentUtil.addFragment(getChildFragmentManager(), webViewFragment, R.id.fm_map_preview);
            } else {
                webViewFragment.reload(footFileBean.getUrl());
            }
            rlTitle.setVisibility(View.VISIBLE);
            if (footFileBean.isRoute()) {
                tvTitle.setText("路径预览");
            } else {
                tvTitle.setText("足印预览");
            }
            mapFragment.showFootView(false);
        } else {
            cvPreview.setVisibility(View.GONE);
            fmPreview.setVisibility(View.GONE);
            rlTitle.setVisibility(View.GONE);
            llRouteEdite.setVisibility(View.GONE);
            mapFragment.showFootView(true);
        }
    }

    @Override
    public void onRouteCommitResult(String url) {
        FootFileBean footFileBean = new FootFileBean();
        footFileBean.setUrl(url);
        footFileBean.setRoute(true);
        llRouteEdite.setVisibility(View.GONE);
        mapFragment.clearSharedPref();
        openFootPreview(footFileBean);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mapFragment.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x01) {
            mapFragment.refreshPoints();
        } else if (resultCode == 0x02) {
            FootFileBean itemBean = (FootFileBean) data.getSerializableExtra("itemBean");
            openFootPreview(itemBean);
        }
    }
}
