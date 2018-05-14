package com.app.footprint.module.foot.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.module.foot.mvp.contract.MapContract;
import com.app.footprint.module.foot.mvp.model.MapModel;
import com.app.footprint.module.foot.mvp.presenter.MapPresenter;
import com.app.footprint.base.BaseFragment;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Point;
import com.esri.core.io.UserCredentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MapFragment extends BaseFragment<MapPresenter, MapModel> implements MapContract.View {


    @BindView(R.id.mvMap_basemap)
    public MapView mMapView;
    private static final double DEFAULT_SCALE = 350000;
    public static final Point DEFAULTPOINT = new Point(106.52252214551413, 29.55847182396155);//默认坐标
    private MapOnTouchListener defaultListener;

    public List<Layer> identifyLayers;//要素
    public List<String> dynamicLayerNameList = new ArrayList<>();
    public HashMap<String, List<Integer>> identifyIdNameHash = new HashMap<>();
    public HashMap<String, String> identifyLayersHash = new HashMap<>();
    public HashMap<String, UserCredentials> userCredentialsHash = new HashMap<>();

    public GraphicsLayer mMarkersGLayer = new GraphicsLayer();// 用于展示主体或任务结果注记
    public GraphicsLayer idenLayer = new GraphicsLayer();
    private boolean isShowMeasureTool = false;

    public void setIsShowMeasureTool(boolean isShowMeasureTool) {
        this.isShowMeasureTool = isShowMeasureTool;
    }


    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Object ins = getActivity().getLastNonConfigurationInstance();
        if (ins != null) {
            mMapView.restoreState((String) ins);
        }
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
        defaultListener = new MapOnTouchListener(this.getActivity(), mMapView);
        mMapView.addLayer(mMarkersGLayer);
        new Handler().postDelayed(() -> {
            try {
//                GpsUtil.location(mMapView, (MainActivity) getActivity());
                mMapView.zoomToScale(DEFAULTPOINT, DEFAULT_SCALE);
            } catch (Exception e) {
                e.printStackTrace();
                mMapView.zoomToScale(DEFAULTPOINT, DEFAULT_SCALE);
            }
        }, 500);
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    public void clearCurrentState(String name) {
        if (name.equals(idenLayer.getName())) {
            idenLayer.removeAll();
            mMapView.getCallout().hide();
        }
    }

    OnStatusChangedListener layerLoadListener = new OnStatusChangedListener() {
        private static final long serialVersionUID = 1L;

        @Override
        public void onStatusChanged(Object source, STATUS status) {
            if (status == STATUS.INITIALIZED) {
                dismissLoading();
                mMapView.zoomToScale(DEFAULTPOINT, DEFAULT_SCALE);
            } else if (STATUS.LAYER_LOADED == status) {
                dismissLoading();
                mMapView.postInvalidate();
                // 如果为动态的
                if (source instanceof ArcGISDynamicMapServiceLayer) {
                }
                if (source instanceof ArcGISFeatureLayer) {
                }
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
