package com.app.footprint.module.map.ui;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

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

    private GeocodeSearch geocodeSearch;
    private Timer timer = new Timer();

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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 100, 2000);
        //刷新点集
        mRxManager.on("refreshPoint", (Action1<Boolean>) bool -> footRecordView.refreshPoints());
        //足印-预览
        mRxManager.on("footPreview", new Action1<Object>() {

            @Override
            public void call(Object o) {
                
            }
        });
    }

    /**
     * 初始化高德定位
     */
    private void initGaoDe() {
        geocodeSearch = new GeocodeSearch(getActivity());
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null) {
                    String city = "", district = "", roadName = "";
                    String address = "";
                    try {
                        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                        city = regeocodeAddress.getCity();
                        district = regeocodeAddress.getDistrict();
                        if (regeocodeAddress.getCrossroads() != null && regeocodeAddress.getCrossroads().size() > 0) {
                            roadName = regeocodeAddress.getCrossroads().get(0).getFirstRoadName().length() == 0 ? regeocodeAddress.getCrossroads().get(0).getSecondRoadName() : regeocodeAddress.getCrossroads().get(0).getFirstRoadName();
                        }
                        address = city + district + roadName;
                    } catch (Exception e) {
                        e.printStackTrace();
                        address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                    }
                    tvAddress.setText(address);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            }
        });
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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Location location = GpsUtil.getLocation(getActivity());
                if (location != null) {
                    Log.e("MapFragment", "Latitude: " + location.getLatitude() + ", Longtitude: " + location.getLongitude());
                    LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
                    // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.GPS);
                    geocodeSearch.getFromLocationAsyn(query);
                }
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
        timer.cancel();
        footRecordView.onDestory();
    }
}
