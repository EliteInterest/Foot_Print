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

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.foot.bean.FootFileBean;
import com.app.footprint.module.foot.func.adapter.FootPicAdapter;
import com.app.footprint.module.foot.mvp.contract.EditInfoContract;
import com.app.footprint.module.foot.mvp.model.EditInfoModel;
import com.app.footprint.module.foot.mvp.presenter.EditInfoPresenter;
import com.app.footprint.module.map.func.util.GpsUtil;
import com.app.footprint.module.map.ui.MapChangeLocationActivity;
import com.esri.core.geometry.Point;
import com.zx.zxutils.other.ZXItemClickSupport;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    private GeocodeSearch geocodeSearch;

    private EditType editType;
    private FootPicAdapter picAdapter;
    private List<FootFileBean> footFiles;
    private FootFileBean.PicBean previewPicBean;

    private List<FootFileBean.PicBean> picChildBeans = new ArrayList<>();
    private String vedioShootPath, vedioPath;

    public enum EditType implements Serializable {
        MapRoutePic,
        MapRouteVedio,
        MapRouteText,
        MapFootPic,
        MapFootVedio,
        MapFootText,
        MapDetail
    }

    public static void startAction(Activity activity, boolean isFinish, EditType editType) {
        Intent intent = new Intent(activity, EditInfoActivity.class);
        intent.putExtra("type", editType);
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
        initGaoDe();
        if (location != null) {
            point = new Point(location.getLongitude(), location.getLatitude());
            getAddress();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        tvLocationPoint.setText("(" + decimalFormat.format(point.getX()) + "," + decimalFormat.format(point.getY()) + ")");

        footFiles = mSharedPrefUtil.getList(ConstStrings.FootFiles);
//        if (footFileBean != null && footFileBean.getPictureBean() != null && footFileBean.getPictureBean().getPaths() != null) {
//            picChildBeans.addAll(footFileBean.getPictureBean().getPaths());
//        }
        actionByType();

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

        //media


        if (editType == EditType.MapRoutePic) {//地图-轨迹-图片
            rvPicture.setVisibility(View.VISIBLE);
            vedioView.setVisibility(View.GONE);
            etRemark.setHint("照片描述");
            CameraActivity.startAction(this, false, true, 0);
        } else if (editType == EditType.MapRouteVedio) {//地图-轨迹-视频
            rvPicture.setVisibility(View.GONE);
            vedioView.setVisibility(View.VISIBLE);
            etRemark.setHint("视频描述");
            CameraActivity.startAction(this, false, true, 1);
        } else if (editType == EditType.MapRouteText) {//地图-轨迹-文本
            rvPicture.setVisibility(View.GONE);
            vedioView.setVisibility(View.GONE);
            etName.setVisibility(View.VISIBLE);
            etRemark.setHint("事件简介");
        } else if (editType == EditType.MapFootPic) {//地图-足迹-图片
            rvPicture.setVisibility(View.VISIBLE);
            vedioView.setVisibility(View.GONE);
            etRemark.setHint("照片描述");
            CameraActivity.startAction(this, false, true, 0);
        } else if (editType == EditType.MapFootVedio) {//地图-足迹-视频
            rvPicture.setVisibility(View.GONE);
            vedioView.setVisibility(View.VISIBLE);
            etRemark.setHint("视频描述");
            CameraActivity.startAction(this, false, true, 1);
        } else if (editType == EditType.MapFootText) {//地图-足迹-文本
            rvPicture.setVisibility(View.GONE);
            vedioView.setVisibility(View.GONE);
            etName.setVisibility(View.VISIBLE);
            etRemark.setHint("事件简介");
        } else if (editType == EditType.MapDetail) {//地图-详情

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
     * 初始化高德定位
     */
    private void initGaoDe() {
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null) {
                    String address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                    tvLocationAddress.setText(address);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }


    /**
     * 获取地理位置信息
     */
    private void getAddress() {
        LatLonPoint latLonPoint = new LatLonPoint(point.getY(), point.getX());
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.GPS);
        geocodeSearch.getFromLocationAsyn(query);
    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_save, R.id.btn_location_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_title_save:
                saveByType();
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
        String pointString = point.getX() + "," + point.getY();
        if (editType == EditType.MapRoutePic) {//地图-轨迹-图片
            //TODO 要放到上传成功的回调中
            FootFileBean footFile = new FootFileBean(pointString, etRemark.getText().toString(), picChildBeans);
            footFiles.add(footFile);
            mSharedPrefUtil.putList("footFileBean", footFiles);
        } else if (editType == EditType.MapRouteVedio) {//地图-轨迹-视频
            //TODO 要放到上传成功的回调中
            FootFileBean footFile = new FootFileBean(pointString, etRemark.getText().toString(), vedioShootPath, vedioPath);
            footFiles.add(footFile);
            mSharedPrefUtil.putList("footFileBean", footFiles);
        } else if (editType == EditType.MapRouteText) {//地图-轨迹-文本
            //TODO 要放到上传成功的回调中
            FootFileBean footFile = new FootFileBean(pointString, etRemark.getText().toString(), etName.getText().toString());
            footFiles.add(footFile);
            mSharedPrefUtil.putList("footFileBean", footFiles);
        } else if (editType == EditType.MapFootPic) {//地图-足迹-图片
            //TODO 直接提交
        } else if (editType == EditType.MapFootVedio) {//地图-足迹-视频
            //TODO 直接提交
        } else if (editType == EditType.MapFootText) {//地图-足迹-文本
            //TODO 直接提交
        } else if (editType == EditType.MapDetail) {//地图-详情

        }
        mRxManager.post("refreshPoint", true);
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
                    MediaController mediaController = new MediaController(this);
                    vedioView.setMediaController(mediaController);
                    mediaController.setMediaPlayer(vedioView);
                    vedioView.setOnCompletionListener(mp -> vedioView.start());
                    vedioView.setOnPreparedListener(mp -> vedioView.start());
                    vedioView.setVideoURI(Uri.parse(ConstStrings.getCachePath() + name));

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
}
