package com.app.footprint.module.foot.ui;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.foot.bean.FootFileBean;
import com.app.footprint.module.foot.bean.FootMarkTextInfo;
import com.app.footprint.module.foot.func.adapter.FootPicAdapter;
import com.app.footprint.module.foot.mvp.contract.EditInfoContract;
import com.app.footprint.module.foot.mvp.model.EditInfoModel;
import com.app.footprint.module.foot.mvp.presenter.EditInfoPresenter;
import com.app.footprint.module.map.bean.BaiduSearchBean;
import com.app.footprint.module.map.func.util.BaiduMapUtil;
import com.app.footprint.module.map.func.util.GpsUtil;
import com.app.footprint.module.map.ui.MapChangeLocationActivity;
import com.esri.core.geometry.Point;
import com.google.gson.Gson;
import com.zx.zxutils.other.ZXItemClickSupport;
import com.zx.zxutils.util.ZXSystemUtil;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class EditInfoActivity extends BaseActivity<EditInfoPresenter, EditInfoModel> implements EditInfoContract.View, FootPicAdapter.OnPicDeleteListener, ZXItemClickSupport.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_location_address)
    TextView tvLocationAddress;
    @BindView(R.id.tv_location_point)
    TextView tvLocationPoint;
    @BindView(R.id.rv_edit_picture)
    RecyclerView rvPicture;
    @BindView(R.id.vedio_edit_view)
    VideoView vedioView;
    @BindView(R.id.et_edit_text_name)
    EditText etName;
    @BindView(R.id.et_edit_remark)
    EditText etRemark;


    private Point point;

    private EditType editType;
    private FootPicAdapter picAdapter;
    private List<FootFileBean> footFiles;
    private FootFileBean itemBean;
    private FootFileBean.PicBean previewPicBean;

    private List<FootFileBean.PicBean> picChildBeans = new ArrayList<>();
    private String vedioShootPath, vedioPath;

    private String footId = "";

    public enum EditType implements Serializable {
        MapRouteText,
        MapRoutePic,
        MapRouteVedio,
        MapFootPic,
        MapFootVedio,
        MapFootText,
        MapDetail
    }

    public static void startAction(Activity activity, boolean isFinish, EditType editType) {
        Intent intent = new Intent(activity, EditInfoActivity.class);
        intent.putExtra("type", editType);
        activity.startActivityForResult(intent, 0x01);
        if (isFinish) activity.finish();
    }

    public static void startAction(Activity activity, boolean isFinish, String footRouteId) {
        Intent intent = new Intent(activity, EditInfoActivity.class);
        intent.putExtra("type", EditType.MapDetail);
        intent.putExtra("footId", footRouteId);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        editType = (EditType) getIntent().getSerializableExtra("type");
        Location location = GpsUtil.getLocation(this);
        itemBean = new FootFileBean();

        footFiles = mSharedPrefUtil.getList(ConstStrings.FootFiles);
        actionByType();
        itemBean.setStartTime(String.valueOf(System.currentTimeMillis()));

        if (point == null && location != null) {
            point = new Point(location.getLongitude(), location.getLatitude());
        }
        getAddress();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        tvLocationPoint.setText("(" + decimalFormat.format(point.getX()) + "," + decimalFormat.format(point.getY()) + ")");

    }

    /**
     * 根据来源进行操作
     */
    private void actionByType() {
        //pic
        picAdapter = new FootPicAdapter(this, picChildBeans);
        rvPicture.setLayoutManager(new GridLayoutManager(this, 4));
        rvPicture.setAdapter(picAdapter);
        picAdapter.setOnDeleteListener(this);
        ZXItemClickSupport.addTo(rvPicture).setOnItemClickListener(this);

        if (editType == EditType.MapDetail) {//地图-详情
            footId = getIntent().getStringExtra("footId");
            for (FootFileBean footFileBean : footFiles) {
                if (footId.equals(footFileBean.getId())) {
                    itemBean = footFileBean;
                    if (footFileBean.getType() == FootFileBean.Type.Camera) {
                        editType = EditType.MapRoutePic;
                        picChildBeans.addAll(footFileBean.getPicPaths());
                        picAdapter.notifyDataSetChanged();
                    } else if (footFileBean.getType() == FootFileBean.Type.Vedio) {
                        editType = EditType.MapRouteVedio;
                        playVedio(footFileBean.getVedioPath());
                    } else if (footFileBean.getType() == FootFileBean.Type.Text) {
                        editType = EditType.MapRouteText;
                        etName.setText(footFileBean.getTextName());
                    }
                    etRemark.setText(footFileBean.getDescription());
                    point = footFileBean.getMapPoint();
                    break;
                }
            }
        }

        if (editType == EditType.MapRoutePic) {//地图-轨迹-图片
            rvPicture.setVisibility(View.VISIBLE);
            vedioView.setVisibility(View.GONE);
            itemBean.setType(FootFileBean.Type.Camera);
            etRemark.setHint("照片描述");
            itemBean.setRoute(true);
            if (footId.length() == 0) {
                CameraActivity.startAction(this, false, true, 0);
            }
        } else if (editType == EditType.MapRouteVedio) {//地图-轨迹-视频
            rvPicture.setVisibility(View.GONE);
            vedioView.setVisibility(View.VISIBLE);
            itemBean.setType(FootFileBean.Type.Vedio);
            etRemark.setHint("视频描述");
            itemBean.setRoute(true);
            if (footId.length() == 0) {
                CameraActivity.startAction(this, false, true, 1);
            }
        } else if (editType == EditType.MapRouteText) {//地图-轨迹-文本
            rvPicture.setVisibility(View.GONE);
            vedioView.setVisibility(View.GONE);
            etName.setVisibility(View.VISIBLE);
            itemBean.setType(FootFileBean.Type.Text);
            etRemark.setHint("事件简介");
            itemBean.setRoute(true);
        } else if (editType == EditType.MapFootPic) {//地图-足迹-图片
            rvPicture.setVisibility(View.VISIBLE);
            vedioView.setVisibility(View.GONE);
            itemBean.setType(FootFileBean.Type.Text);
            etRemark.setHint("照片描述");
            itemBean.setRoute(false);
            if (footId.length() == 0) {
                CameraActivity.startAction(this, false, true, 0);
            }
        } else if (editType == EditType.MapFootVedio) {//地图-足迹-视频
            rvPicture.setVisibility(View.GONE);
            vedioView.setVisibility(View.VISIBLE);
            itemBean.setType(FootFileBean.Type.Vedio);
            etRemark.setHint("视频描述");
            itemBean.setRoute(false);
            if (footId.length() == 0) {
                CameraActivity.startAction(this, false, true, 1);
            }
        } else if (editType == EditType.MapFootText) {//地图-足迹-文本
            rvPicture.setVisibility(View.GONE);
            vedioView.setVisibility(View.GONE);
            etName.setVisibility(View.VISIBLE);
            itemBean.setType(FootFileBean.Type.Text);
            etRemark.setHint("事件简介");
            itemBean.setRoute(false);
        }
    }

    /**
     * 图片点击事件
     *
     * @param recyclerView
     * @param i
     * @param view
     */
    @Override
    public void onItemClicked(RecyclerView recyclerView, int i, View view) {
        if (i == picChildBeans.size()) {
            if (picChildBeans.size() >= 9) {
                showToast("已达最大图片数量！");
                return;
            }
            CameraActivity.startAction(this, false, false, 0);
        } else {
            previewPicBean = picChildBeans.get(i);
            PicPreviewActivity.startAction(this, false, previewPicBean.getPath(), previewPicBean.getRemark());
        }
    }

    /**
     * 图片删除事件事件
     *
     * @param position
     */
    @Override
    public void onDelete(int position) {
        picChildBeans.remove(position);
        picAdapter.notifyDataSetChanged();
    }

    /**
     * 上传成功
     */
    @Override
    public void onFileCommitResult(String result) {
        itemBean.setUrl(result);
        Intent intent = new Intent();
        intent.putExtra("itemBean", itemBean);
        setResult(0x02, intent);
        finish();
    }

    /**
     * 获取地理位置信息
     */
    private void getAddress() {
        BaiduMapUtil.searchPoi(point.getX(), point.getY(), new BaiduMapUtil.OnBaiduSearchListener() {
            @Override
            public void onSearchBack(BaiduSearchBean baiduSearchBean) {
                String streetName = "";
                String formatName = "";
                streetName = baiduSearchBean.getResult().getAddressComponent().getStreet();
                formatName = baiduSearchBean.getResult().getFormatted_address();
                if (streetName == null || streetName.length() == 0) {
                    streetName = formatName;
                }
                tvLocationAddress.setText(streetName);
                itemBean.setFormatAddress(formatName);
                itemBean.setStreetAddress(streetName);
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                tvLocationPoint.setText("(" + decimalFormat.format(point.getX()) + "," + decimalFormat.format(point.getY()) + ")");
                itemBean.setPoint(point.getX() + "," + point.getY() + point.getZ());
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_save, R.id.btn_location_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_save:
                if (etName.getVisibility() == View.VISIBLE && etName.getText().toString().length() == 0) {
                    showToast("请填写事件名称");
                } else {
                    saveByType();
                }
                break;
            case R.id.btn_location_change:
                MapChangeLocationActivity.startAction(this, false, point);
                break;
        }
    }

    /**
     * 根据类型进行保存
     */
    private void saveByType() {
        itemBean.setEndTime(String.valueOf(System.currentTimeMillis()));
        String pointString = point.getX() + "," + point.getY() + "," + point.getZ();
        itemBean.setPoint(pointString);
        itemBean.setDescription(etRemark.getText().toString());
        if (editType == EditType.MapRoutePic) {//地图-轨迹-图片
            itemBean.setPicPaths(picChildBeans);
            itemBean.setType(FootFileBean.Type.Camera);
            addFootBean();
        } else if (editType == EditType.MapRouteVedio) {//地图-轨迹-视频
            itemBean.setVedioPath(vedioPath);
            itemBean.setVedioShootPath(vedioShootPath);
            itemBean.setType(FootFileBean.Type.Vedio);
            addFootBean();
        } else if (editType == EditType.MapRouteText) {//地图-轨迹-文本
            itemBean.setTextName(etName.getText().toString());
            itemBean.setDescription(etRemark.getText().toString());
            itemBean.setType(FootFileBean.Type.Text);
            addFootBean();
        } else if (editType == EditType.MapFootPic) {//地图-足迹-图片
            itemBean.setPicPaths(picChildBeans);

            FootMarkTextInfo footMarkTextInfo = new FootMarkTextInfo();

            FootMarkTextInfo.FootMarkTextBean footMarkTextBean = new FootMarkTextInfo.FootMarkTextBean();
            footMarkTextBean.setUserId(mSharedPrefUtil.getString("userId", ""));
//            footMarkTextBean.setName(itemBean.getLocationName() == null ? "":itemBean.getLocationName());
//            footMarkTextBean.setDesc(itemBean.getDescription() == null ? "":itemBean.getDescription());
            footMarkTextBean.setName(itemBean.getStreetAddress());
            footMarkTextBean.setDesc(itemBean.getDescription());
            footMarkTextBean.setConsumptionTime("0");
            footMarkTextBean.setStartTime(String.valueOf(System.currentTimeMillis()));
            footMarkTextBean.setEndTime(String.valueOf(System.currentTimeMillis()));
            footMarkTextInfo.setFootprint(footMarkTextBean);

            FootMarkTextInfo.FootMarkTextBean1 footMarkTextBean1 = new FootMarkTextInfo.FootMarkTextBean1();
            footMarkTextBean1.setPointId(itemBean.getId());
            footMarkTextBean1.setLatitude(point.getY());
            footMarkTextBean1.setLongitude(point.getX());
            footMarkTextBean1.setAltitude(point.getZ());
            footMarkTextBean1.setAddr(itemBean.getStreetAddress());
            footMarkTextBean1.setDesc(itemBean.getFormatAddress());
            footMarkTextBean1.setPointType(2);
            footMarkTextInfo.setPointPosition(footMarkTextBean1);


            FootMarkTextInfo.FootMarkTextBean2 footMarkTextBean2 = new FootMarkTextInfo.FootMarkTextBean2();
            footMarkTextBean2.setTotalDesc(itemBean.getDescription());
            List<String> MediaInfos = new ArrayList<>();
            List<File> files = new ArrayList<>();

            for (FootFileBean.PicBean picBean : picChildBeans) {
                MediaInfos.add(picBean.getRemark());//test
                File file = new File(ConstStrings.getCachePath() + picBean.getPath());
                files.add(file);
            }

            footMarkTextBean2.setMediaInfo(MediaInfos);
            footMarkTextInfo.setFileInfo(footMarkTextBean2);

            Gson gson = new Gson();
            String jsonsStr = gson.toJson(footMarkTextInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("FootmarkInfo", jsonsStr);
            map.put("uploadType", 2);
            map.put("file", files);
            mPresenter.commitFile(map);
        } else if (editType == EditType.MapFootVedio) {//地图-足迹-视频
            itemBean.setVedioPath(vedioPath);
            itemBean.setVedioShootPath(vedioShootPath);

            FootMarkTextInfo footMarkTextInfo = new FootMarkTextInfo();

            FootMarkTextInfo.FootMarkTextBean footMarkTextBean = new FootMarkTextInfo.FootMarkTextBean();
            footMarkTextBean.setUserId(mSharedPrefUtil.getString("userId", ""));
//            footMarkTextBean.setName(itemBean.getLocationName() == null ? "":itemBean.getLocationName());
//            footMarkTextBean.setDesc(itemBean.getDescription() == null ? "":itemBean.getDescription());
            footMarkTextBean.setName(itemBean.getStreetAddress());
            footMarkTextBean.setDesc(itemBean.getDescription());
            footMarkTextBean.setConsumptionTime("0");
            footMarkTextBean.setStartTime(String.valueOf(itemBean.getStartTime()));
            footMarkTextBean.setEndTime(String.valueOf(itemBean.getEndTime()));
            footMarkTextInfo.setFootprint(footMarkTextBean);

            FootMarkTextInfo.FootMarkTextBean1 footMarkTextBean1 = new FootMarkTextInfo.FootMarkTextBean1();
            footMarkTextBean1.setPointId(itemBean.getId());
            footMarkTextBean1.setLatitude(point.getY());
            footMarkTextBean1.setLongitude(point.getX());
            footMarkTextBean1.setAltitude(point.getZ());
            footMarkTextBean1.setAddr(itemBean.getStreetAddress());
            footMarkTextBean1.setDesc(itemBean.getFormatAddress());
            footMarkTextBean1.setPointType(3);
            footMarkTextInfo.setPointPosition(footMarkTextBean1);


            FootMarkTextInfo.FootMarkTextBean2 footMarkTextBean2 = new FootMarkTextInfo.FootMarkTextBean2();
            footMarkTextBean2.setTotalDesc("这是图片集合的描述，MediaInfo的数量和上传的图片和视频数量相同");
            List<String> MediaInfos = new ArrayList<>();
            List<File> files = new ArrayList<>();

//            for(FootFileBean.PicBean picBean : picChildBeans)
//            {
            MediaInfos.add("图片描述");//test
            File file = new File(ConstStrings.getCachePath() + vedioPath);
            files.add(file);
//            }

            footMarkTextBean2.setMediaInfo(MediaInfos);
            footMarkTextInfo.setFileInfo(footMarkTextBean2);

            Gson gson = new Gson();
            String jsonsStr = gson.toJson(footMarkTextInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("FootmarkInfo", jsonsStr);
            map.put("uploadType", 3);
            map.put("file", files);
            mPresenter.commitFile(map);
        } else if (editType == EditType.MapFootText) {//地图-足迹-文本
            itemBean.setTextName(etName.getText().toString());
            //初始化JSON文本
            FootMarkTextInfo footMarkTextInfo = new FootMarkTextInfo();

            FootMarkTextInfo.FootMarkTextBean footMarkTextBean = new FootMarkTextInfo.FootMarkTextBean();
            footMarkTextBean.setUserId(mSharedPrefUtil.getString("userId", ""));
//            footMarkTextBean.setName(itemBean.getAddrSimple() == null ? "":itemBean.getAddrSimple());
//            footMarkTextBean.setDesc(itemBean.getDescription() == null ? "":itemBean.getDescription());
            footMarkTextBean.setName(itemBean.getStreetAddress());
            footMarkTextBean.setDesc(itemBean.getDescription());
            footMarkTextBean.setConsumptionTime("0");
            footMarkTextBean.setStartTime(String.valueOf(itemBean.getStartTime()));
            footMarkTextBean.setEndTime(String.valueOf(itemBean.getEndTime()));
            footMarkTextInfo.setFootprint(footMarkTextBean);

            FootMarkTextInfo.FootMarkTextBean1 footMarkTextBean1 = new FootMarkTextInfo.FootMarkTextBean1();
            footMarkTextBean1.setPointId(itemBean.getId());
            footMarkTextBean1.setLatitude(point.getY());
            footMarkTextBean1.setLongitude(point.getX());
            footMarkTextBean1.setAltitude(point.getZ());
            footMarkTextBean1.setAddr(itemBean.getStreetAddress());
            footMarkTextBean1.setDesc(itemBean.getFormatAddress());
            footMarkTextBean1.setPointType(1);
            footMarkTextInfo.setPointPosition(footMarkTextBean1);

            FootMarkTextInfo.FootMarkTextBean3 footMarkTextBean3 = new FootMarkTextInfo.FootMarkTextBean3();
            footMarkTextBean3.setTextName(etName.getText().toString());
            footMarkTextBean3.setTextDesc(etRemark.getText().toString());
            FootMarkTextInfo.FootMarkTextBean2 footMarkTextBean2 = new FootMarkTextInfo.FootMarkTextBean2();
            footMarkTextBean2.setTotalDesc("这是图片集合的描述，MediaInfo的数量和上传的图片和视频数量相同");
            footMarkTextBean2.setTextInfo(footMarkTextBean3);
            footMarkTextInfo.setFileInfo(footMarkTextBean2);

            Gson gson = new Gson();
            String jsonsStr = gson.toJson(footMarkTextInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("FootmarkInfo", jsonsStr);
            map.put("uploadType", 1);
            mPresenter.commitFile(map);

        }
        ZXSystemUtil.closeKeybord(this);
    }

    /**
     * 判断是否存在进行添加
     */
    private void addFootBean() {
        if (footId.length() > 0) {
            for (int i = 0; i < footFiles.size(); i++) {
                if (itemBean.getId().equals(footFiles.get(i).getId())) {
                    footFiles.set(i, itemBean);
                    break;
                }
            }
        } else {
            footFiles.add(itemBean);
        }
        mSharedPrefUtil.putList(ConstStrings.FootFiles, footFiles);
        setResult(0x01);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {//直接回退
            finish();
        } else if (resultCode == 0x01) {//位置修改
            point = (Point) data.getSerializableExtra("changeLocation");
            getAddress();
        } else if (resultCode == 0x02) {//数据返回
            if (data.hasExtra("type")) {
                if ("pic".equals(data.getStringExtra("type"))) {
                    String name = data.getStringExtra("picName");
                    String remark = data.getStringExtra("remark");
                    FootFileBean.PicBean picChildBean = new FootFileBean.PicBean(name, remark);
                    picChildBeans.add(picChildBean);
                    picAdapter.notifyDataSetChanged();
                } else if ("vedio".equals(data.getStringExtra("type"))) {
                    String name = data.getStringExtra("vedioName");
                    playVedio(name);

                    vedioShootPath = data.getStringExtra("vedioShoot");
                    vedioPath = data.getStringExtra("vedioName");
                }
            }
        } else if (resultCode == 0x03) {//图片备注修改
            if (previewPicBean != null) {
                previewPicBean.setRemark(data.getStringExtra("remark"));
            }
        }
    }

    private void playVedio(String name) {
        MediaController mediaController = new MediaController(this);
        vedioView.setMediaController(mediaController);
        mediaController.setMediaPlayer(vedioView);
        vedioView.setOnCompletionListener(mp -> vedioView.start());
        vedioView.setOnPreparedListener(mp -> vedioView.start());
        vedioView.setVideoURI(Uri.parse(ConstStrings.getCachePath() + name));
    }
}
