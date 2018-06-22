package cn.gisdata.footprint.module.map.ui;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Point;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gisdata.footprint.R;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.base.BaseFragment;
import cn.gisdata.footprint.module.foot.func.view.FootRecordView;
import cn.gisdata.footprint.module.map.bean.BaiduSearchBean;
import cn.gisdata.footprint.module.map.bean.MapUrlBean;
import cn.gisdata.footprint.module.map.func.tool.tiditu.TianDiTuLayer;
import cn.gisdata.footprint.module.map.func.tool.tiditu.TianDiTuLayerTypes;
import cn.gisdata.footprint.module.map.func.util.BaiduMapUtil;
import cn.gisdata.footprint.module.map.func.util.GpsUtil;
import cn.gisdata.footprint.module.map.func.view.OnlineTileLayer;
import cn.gisdata.footprint.module.map.mvp.contract.MapContract;
import cn.gisdata.footprint.module.map.mvp.model.MapModel;
import cn.gisdata.footprint.module.map.mvp.presenter.MapPresenter;
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
    @BindView(R.id.foot_record_view)
    FootRecordView footRecordView;
    @BindView(R.id.rl_title_map)
    RelativeLayout rlTitle;
    private static final double DEFAULT_SCALE = 350000;
    private static final Point DEFAULTPOINT = new Point(106.52252214551413, 29.55847182396155);//默认坐标
    private MapOnTouchListener defaultListener;

    private TianDiTuLayer tianDiTuVectorLayer, tianDiTuImageLayer;
    private TianDiTuLayer tianDiTuVectorLabelLayer, tianDiTuImageLabelLayer;
    private OnlineTileLayer vectorLayer, imageLayer, imageLabelLayer;
    private GraphicsLayer idenLayer = new GraphicsLayer();

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
        footRecordView.setMapView(mMapView);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 100, 1000 * 10);
        mRxManager.on("destory", (Action1<Boolean>) aBoolean -> {
            if (footRecordView != null) {
                footRecordView.onDestory();
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

        tianDiTuVectorLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_2000);
        tianDiTuImageLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_IMAGE_2000);
        tianDiTuVectorLabelLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_ANNOTATION_CHINESE_2000);
        tianDiTuImageLabelLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_IMAGE_ANNOTATION_CHINESE_2000);
        mMapView.addLayer(tianDiTuVectorLayer);
        mMapView.addLayer(tianDiTuImageLayer);
        mMapView.addLayer(tianDiTuVectorLabelLayer);
        mMapView.addLayer(tianDiTuImageLabelLayer);
        List<MapUrlBean> mapUrlBeans = mSharedPrefUtil.getList("mapUrl");
        if (mapUrlBeans != null && mapUrlBeans.size() != 0) {
            for (MapUrlBean bean : mapUrlBeans) {
                if (bean.getType() == 1) {//矢量
                    vectorLayer = new OnlineTileLayer(getActivity(), bean.getMapUrl(),"vector_layer");
                } else if (bean.getType() == 2) {
                    imageLayer = new OnlineTileLayer(getActivity(), bean.getMapUrl(),"image_layer");
                    imageLabelLayer = new OnlineTileLayer(getActivity(), bean.getLabelUrl(), "image_lable_layer");
                }
            }
        }
        mMapView.addLayer(vectorLayer);
        mMapView.addLayer(imageLayer);
        mMapView.addLayer(imageLabelLayer);
        imageLayer.setVisible(false);
        imageLabelLayer.setVisible(false);
        tianDiTuImageLayer.setVisible(false);
        tianDiTuImageLabelLayer.setVisible(false);
//        new Handler().postDelayed(() -> {
//            try {
//                GpsUtil.location(mMapView, getActivity());
//            } catch (Exception e) {
//                e.printStackTrace();
//                mMapView.zoomToScale(DEFAULTPOINT, DEFAULT_SCALE);
//            }
//        }, 500);
    }

    @OnClick({R.id.iv_map_layer_vector, R.id.iv_map_layer_img, R.id.iv_map_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_map_layer_vector:
                vectorLayer.setVisible(true);
                imageLayer.setVisible(false);
                imageLabelLayer.setVisible(false);
                tianDiTuVectorLayer.setVisible(true);
                tianDiTuVectorLabelLayer.setVisible(true);
                tianDiTuImageLayer.setVisible(false);
                tianDiTuImageLabelLayer.setVisible(false);
                break;
            case R.id.iv_map_layer_img:
                vectorLayer.setVisible(false);
                imageLayer.setVisible(true);
                imageLabelLayer.setVisible(true);
                tianDiTuVectorLayer.setVisible(false);
                tianDiTuVectorLabelLayer.setVisible(false);
                tianDiTuImageLayer.setVisible(true);
                tianDiTuImageLabelLayer.setVisible(true);
                break;
            case R.id.iv_map_location:
                try {
                    GpsUtil.location(mMapView, getActivity());
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

    public void refreshPoints() {
        footRecordView.refreshPoints();
    }

    OnStatusChangedListener layerLoadListener = new OnStatusChangedListener() {
        private static final long serialVersionUID = 1L;

        @Override
        public void onStatusChanged(Object source, STATUS status) {
            if (status == STATUS.INITIALIZED) {
                dismissLoading();
                try {
                    GpsUtil.location(mMapView, getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                    mMapView.zoomToScale(DEFAULTPOINT, DEFAULT_SCALE);
                }
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
                    BaiduMapUtil.searchPoi(location.getLongitude(), location.getLatitude(), new BaiduMapUtil.OnBaiduSearchListener() {
                                @Override
                                public void onSearchBack(BaiduSearchBean baiduSearchBean) {
                                    tvAddress.setText(baiduSearchBean.getResult().getFormatted_address());
                                }

                                @Override
                                public void onSearchError() {

                                }
                            });
                }
            }
        }
    };


    public void showFootView(boolean show) {
        if (show) {
            footRecordView.setVisibility(View.VISIBLE);
            footRecordView.routeLayer.setVisible(true);
            rlTitle.setVisibility(View.VISIBLE);
        } else {
            footRecordView.setVisibility(View.GONE);
            footRecordView.routeLayer.setVisible(false);
            rlTitle.setVisibility(View.GONE);
        }
    }

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
        if (footRecordView != null) {
            footRecordView.onDestory();
        }
    }

    public FootRecordView getFootRecordView() {
        return footRecordView;
    }

    public void resetMap(boolean deleteFile) {
        footRecordView.closeRoute(deleteFile);
    }
}
