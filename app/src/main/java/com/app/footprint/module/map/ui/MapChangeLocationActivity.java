package com.app.footprint.module.map.ui;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.map.func.tool.tiditu.TianDiTuLayer;
import com.app.footprint.module.map.func.tool.tiditu.TianDiTuLayerTypes;
import com.app.footprint.module.map.func.util.GpsUtil;
import com.app.footprint.module.map.mvp.contract.MapChangeLocationContract;
import com.app.footprint.module.map.mvp.model.MapChangeLocationModel;
import com.app.footprint.module.map.mvp.presenter.MapChangeLocationPresenter;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Point;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MapChangeLocationActivity extends BaseActivity<MapChangeLocationPresenter, MapChangeLocationModel> implements MapChangeLocationContract.View {


    @BindView(R.id.mvMap_basemap)
    MapView mMapView;
    private static final double DEFAULT_SCALE = 30000;

    private TianDiTuLayer tianDiTuVectorLayer;
    private Point point;

    public static void startAction(Activity activity, boolean isFinish, Point point) {
        Intent intent = new Intent(activity, MapChangeLocationActivity.class);
        intent.putExtra("point", point);
        activity.startActivityForResult(intent, 0x01);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_location;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Object ins = getLastNonConfigurationInstance();
        if (ins != null && mMapView != null) {
            mMapView.restoreState((String) ins);
        }
        point = (Point) getIntent().getSerializableExtra("point");
        initMap();
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        ArcGISRuntime.setClientId(ConstStrings.arcgisKey);
        mMapView.setEsriLogoVisible(false);
        mMapView.setMapBackground(0xffffff, 0xffffff, 1, 1);
        showLoading("正在初始化地图，请稍后...");
        mMapView.setOnStatusChangedListener(layerLoadListener);
        tianDiTuVectorLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_2000);
        mMapView.addLayer(tianDiTuVectorLayer);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_save:
                // 得到设备的分辨率
                int left = mMapView.getLeft();
                int right = mMapView.getRight();
                int top = mMapView.getTop();
                int bottom = mMapView.getBottom();
                // 计算屏幕宽度和高度
                int screenWidth = (right - left) / 2;
                int screenHeight = (bottom - top) / 2;
                // 计算屏幕中心点经纬度
                Point centerPoint = mMapView.toMapPoint(screenWidth, screenHeight);
                Intent intent = new Intent();
                intent.putExtra("changeLocation", centerPoint);
                setResult(0x01, intent);
                finish();
                break;
            default:
                break;
        }
    }

    OnStatusChangedListener layerLoadListener = new OnStatusChangedListener() {
        private static final long serialVersionUID = 1L;

        @Override
        public void onStatusChanged(Object source, STATUS status) {
            if (status == STATUS.INITIALIZED) {
                dismissLoading();
                if (point == null) {
                    Location location = GpsUtil.getLocation(MapChangeLocationActivity.this);
                    point = new Point(location.getLongitude(), location.getLatitude());
                }
                mMapView.centerAt(point, true);
                mMapView.setScale(DEFAULT_SCALE);
            } else if (STATUS.LAYER_LOADED == status) {
                dismissLoading();
                mMapView.postInvalidate();
            } else if (STATUS.INITIALIZATION_FAILED == status) {
                dismissLoading();
                showToast("地图初始化失败");
            } else if (STATUS.LAYER_LOADING_FAILED == status) {
                dismissLoading();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.unpause();
    }
}
