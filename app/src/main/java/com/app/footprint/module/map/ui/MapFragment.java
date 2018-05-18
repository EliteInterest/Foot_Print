package com.app.footprint.module.map.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.foot.func.view.FootRecordView;
import com.app.footprint.module.map.func.tool.tiditu.TianDiTuLayer;
import com.app.footprint.module.map.func.tool.tiditu.TianDiTuLayerTypes;
import com.app.footprint.module.map.func.util.GpsUtil;
import com.app.footprint.module.map.mvp.contract.MapContract;
import com.app.footprint.module.map.mvp.model.MapModel;
import com.app.footprint.module.map.mvp.presenter.MapPresenter;
import com.app.footprint.module.system.ui.MainActivity;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Point;
import com.esri.core.io.UserCredentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MapFragment extends BaseFragment<MapPresenter, MapModel> implements MapContract.View {


    @BindView(R.id.mvMap_basemap)
    MapView mMapView;
    @BindView(R.id.tv_map_address)
    TextView tvAddress;
    @BindView(R.id.iv_map_layer)
    ImageView ivLayer;
    @BindView(R.id.foot_record_view)
    FootRecordView footRecordView;
    private static final double DEFAULT_SCALE = 350000;
    private static final Point DEFAULTPOINT = new Point(106.52252214551413, 29.55847182396155);//默认坐标
    private MapOnTouchListener defaultListener;

    private TianDiTuLayer tianDiTuVectorLayer, tianDiTuImageLayer;
    private List<Layer> identifyLayers;//要素
    private List<String> dynamicLayerNameList = new ArrayList<>();
    private HashMap<String, List<Integer>> identifyIdNameHash = new HashMap<>();
    private HashMap<String, String> identifyLayersHash = new HashMap<>();
    private HashMap<String, UserCredentials> userCredentialsHash = new HashMap<>();

    private GraphicsLayer mMarkersGLayer = new GraphicsLayer();// 用于展示主体或任务结果注记
    private GraphicsLayer idenLayer = new GraphicsLayer();

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;


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
        if (ins != null && mMapView != null) {
            mMapView.restoreState((String) ins);
        }
        initMap();
        initGaoDe();
        footRecordView.setMapView(mMapView);
    }

    /**
     * 初始化高德定位
     */
    private void initGaoDe() {
        mLocationOption = new AMapLocationClientOption();
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        tvAddress.setText(aMapLocation.getAddress());
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
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
//        mMapView.addLayer(mMarkersGLayer);
        tianDiTuVectorLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_2000);
        tianDiTuImageLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_IMAGE_2000);
        mMapView.addLayer(tianDiTuVectorLayer);
        mMapView.addLayer(tianDiTuImageLayer);
        tianDiTuImageLayer.setVisible(false);
        new Handler().postDelayed(() -> {
            try {
                GpsUtil.location(mMapView, (MainActivity) getActivity());
            } catch (Exception e) {
                e.printStackTrace();
                mMapView.zoomToScale(DEFAULTPOINT, DEFAULT_SCALE);
            }
        }, 500);
    }

    @OnClick({R.id.iv_map_layer, R.id.iv_map_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_map_layer:
                if (tianDiTuVectorLayer.isVisible()) {
                    tianDiTuVectorLayer.setVisible(false);
                    tianDiTuImageLayer.setVisible(true);
                    ivLayer.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.map_mode_img));
                } else {
                    tianDiTuVectorLayer.setVisible(true);
                    tianDiTuImageLayer.setVisible(false);
                    ivLayer.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.map_mode_vector));
                }
                break;
            case R.id.iv_map_location:
                try {
                    GpsUtil.location(mMapView, (MainActivity) getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                    mMapView.zoomToScale(DEFAULTPOINT, DEFAULT_SCALE);
                }
                break;
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
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        footRecordView.onDestory();
    }
}
