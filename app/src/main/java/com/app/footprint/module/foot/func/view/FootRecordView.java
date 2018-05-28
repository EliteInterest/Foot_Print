package com.app.footprint.module.foot.func.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.module.foot.bean.FootFileBean;
import com.app.footprint.module.foot.func.tool.FootUtil;
import com.app.footprint.module.foot.ui.EditInfoActivity;
import com.app.footprint.module.map.func.util.GpsUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.util.ZXFileUtil;
import com.zx.zxutils.util.ZXSharedPrefUtil;
import com.zx.zxutils.util.ZXTimeUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Xiangb on 2018/5/17.
 * 功能：
 */

public class FootRecordView extends RelativeLayout {

    @BindView(R.id.ll_record_mode_route)
    LinearLayout llModeRoute;
    @BindView(R.id.ll_record_mode_foot)
    LinearLayout llModeFoot;
    @BindView(R.id.tv_record_tab_date)
    TextView tvTabDate;//日期
    @BindView(R.id.tv_record_tab_time)
    TextView tvTabCountTime;//时长
    @BindView(R.id.tv_record_tab_mileage)
    TextView tvTabCountSize;//距离
    @BindView(R.id.cv_record_tab)
    CardView cvRecordTab;

    private Context context;

    private Timer timeTimer, routeTimer;//计时器
    private ZXSharedPrefUtil zxSharedPrefUtil;

    private MapView mapView;
    public GraphicsLayer routeLayer;//路线
    private List<Graphic> mGraphicList = new ArrayList<>();
    private List<Point> mPoints = new ArrayList<>();
    private SimpleLineSymbol lineSymbol;
    private SimpleMarkerSymbol markerSymbol;

    private int countTime = 0;//计时
    private double countSize = 0;//里程
    private long lastCurrentTime = 0;

    private static final String RECORD_START_TIME = "record_start_time";
    private static final String RECORD_POINTS = "record_points";
    private static final String RECORD_STATUS = "record_status";

    public FootRecordView(Context context) {
        super(context);
        init(context);
    }


    public FootRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FootRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_map_foot_record, this, true);
        ButterKnife.bind(this);

        lastCurrentTime = System.currentTimeMillis();
        zxSharedPrefUtil = new ZXSharedPrefUtil();
        tvTabDate.setText(ZXTimeUtil.getTime(lastCurrentTime) + " " + ZXTimeUtil.dateToWeek(lastCurrentTime).replace("周", "星期"));

    }

    /**
     * 设置mapview
     *
     * @param mapView
     */
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        if (routeLayer != null && mapView != null) {
            mapView.removeLayer(routeLayer);
        }
        routeLayer = new GraphicsLayer();
        lineSymbol = new SimpleLineSymbol(Color.RED, 2, SimpleLineSymbol.STYLE.SOLID);
        markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 8, SimpleMarkerSymbol.STYLE.CIRCLE);
        routeLayer.setName("footRouteLayer");
        mapView.addLayer(routeLayer);

        if (zxSharedPrefUtil.getBool(RECORD_STATUS)
                && zxSharedPrefUtil.getLong(RECORD_START_TIME) > 0) {//之前处于绘制状态
            ZXDialogUtil.showYesNoDialog(context, "提示", "检测到之前处于绘制状态，是否继续之前的绘制？", "继续", "放弃",
                    (dialog, which) -> {//继续
                        cvRecordTab.setVisibility(VISIBLE);
                        startTimer(true);
                    },
                    (dialog, which) -> {//放弃
                        clearSharedPref();
                    });
        }

        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                Layer[] layers = mapView.getLayers();
                if (layers != null && layers.length > 0) {
                    for (Layer layer : layers) {
                        if (layer instanceof GraphicsLayer && "footRouteLayer".equals(layer.getName())) {
                            int[] graphicIds = ((GraphicsLayer) layer).getGraphicIDs(x, y, 20, 1);
                            for (int graphicId : graphicIds) {
                                Graphic graphic = ((GraphicsLayer) layer).getGraphic(graphicId);
                                if (graphic.getAttributes() != null && graphic.getAttributes().containsKey("footId")) {
                                    String footId = (String) graphic.getAttributes().get("footId");
                                    EditInfoActivity.startAction((Activity) context, false, footId);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 清除sharepref
     */
    private void clearSharedPref() {
        zxSharedPrefUtil.remove(RECORD_POINTS);
        zxSharedPrefUtil.remove(RECORD_START_TIME);
        zxSharedPrefUtil.putBool(RECORD_STATUS, false);
        zxSharedPrefUtil.putList(ConstStrings.FootFiles, new ArrayList<>());
        ZXFileUtil.deleteFiles(ConstStrings.getCachePath());
    }

    @OnClick({R.id.iv_record_foot_text, R.id.iv_record_mode_route, R.id.iv_record_mode_foot, R.id.ll_record_foot_start, R.id.iv_record_tab_cancel,
            R.id.cv_record_tab, R.id.iv_record_tab_camera, R.id.iv_record_tab_vedio, R.id.iv_record_tab_text, R.id.iv_record_foot_camera, R.id.iv_record_foot_vedio})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_record_mode_route://路径
                llModeRoute.setVisibility(GONE);
                llModeFoot.setVisibility(VISIBLE);
                break;
            case R.id.iv_record_mode_foot://足迹
                llModeRoute.setVisibility(VISIBLE);
                llModeFoot.setVisibility(GONE);
                break;
            case R.id.ll_record_foot_start://开始录制
                clearSharedPref();
                cvRecordTab.setVisibility(VISIBLE);
                startTimer(false);
                break;
            case R.id.iv_record_tab_cancel://录制取消
                ZXDialogUtil.showYesNoDialog(context, "提示", "是否取消路线录制？", (dialog, which) -> {
                    cvRecordTab.setVisibility(GONE);
                    timeTimer.cancel();
                    mPoints.clear();
                    mGraphicList.clear();
                    routeLayer.removeAll();
                    clearSharedPref();
                });
                break;
            case R.id.cv_record_tab:

                break;
            case R.id.iv_record_tab_camera://轨迹-相机
                EditInfoActivity.startAction((Activity) context, false, EditInfoActivity.EditType.MapRoutePic);
                break;
            case R.id.iv_record_tab_vedio://轨迹-录像
                EditInfoActivity.startAction((Activity) context, false, EditInfoActivity.EditType.MapRouteVedio);
                break;
            case R.id.iv_record_tab_text://轨迹-文本
                EditInfoActivity.startAction((Activity) context, false, EditInfoActivity.EditType.MapRouteText);
                break;
            case R.id.iv_record_foot_camera://足迹-相机
                EditInfoActivity.startAction((Activity) context, false, EditInfoActivity.EditType.MapFootPic);
                break;
            case R.id.iv_record_foot_vedio://足迹-录像
                EditInfoActivity.startAction((Activity) context, false, EditInfoActivity.EditType.MapFootVedio);
                break;
            case R.id.iv_record_foot_text://足迹-文本
                EditInfoActivity.startAction((Activity) context, false, EditInfoActivity.EditType.MapFootText);
                break;
            default:
                break;
        }
    }

    /**
     * 开启定时器
     *
     * @param resumeRoute 继续绘制
     */
    private void startTimer(boolean resumeRoute) {
        zxSharedPrefUtil.putBool(RECORD_STATUS, true);
        if (resumeRoute) {
            lastCurrentTime = zxSharedPrefUtil.getLong(RECORD_START_TIME);
            long nowCurrentTime = System.currentTimeMillis();
            tvTabDate.setText(ZXTimeUtil.getTime(lastCurrentTime) + " " + ZXTimeUtil.dateToWeek(lastCurrentTime).replace("周", "星期"));
            countTime = (int) ((nowCurrentTime - lastCurrentTime) / 1000);
            mPoints = zxSharedPrefUtil.getList(RECORD_POINTS);
            for (int i = 0; i < mPoints.size() - 1; i++) {
                Line line = new Line();
                line.setStart(mPoints.get(i));
                line.setEnd(mPoints.get(i + 1));
                Polyline polyline = new Polyline();
                polyline.addSegment(line, true);
                Graphic lineGraphic = new Graphic(polyline, lineSymbol);
                mGraphicList.add(lineGraphic);
                routeLayer.addGraphic(lineGraphic);
                double length = GeometryEngine.geodesicLength(polyline, mapView.getSpatialReference(), null);
                countSize += length / 1000;
            }
            DecimalFormat df = new DecimalFormat("#.00");
            tvTabCountSize.setText(df.format(countSize).startsWith(".") ? "0" + df.format(countSize) : df.format(countSize));
            refreshPoints();
        } else {
            countTime = 0;
            countSize = 0;
            mPoints.clear();
            mGraphicList.clear();
            tvTabCountSize.setText("0.00");
        }
        //开启定时器
        timeTimer = new Timer();
        timeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 100, 1000);
        //开启路线绘制
        routeTimer = new Timer();
        routeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 100, 1000 * 10);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//计时器
                countTime++;
                String hh = countTime / 3600 > 9 ? countTime / 3600 + "" : "0" + countTime / 3600;
                String mm = (countTime % 3600) / 60 > 9 ? (countTime % 3600) / 60 + "" : "0" + (countTime % 3600) / 60;
                String ss = (countTime % 3600) % 60 > 9 ? (countTime % 3600) % 60 + "" : "0" + (countTime % 3600) % 60;
                tvTabCountTime.setText(hh + ":" + mm + ":" + ss);
            } else if (msg.what == 1) {//路径绘制
                Location location = GpsUtil.getLocation(context);
                if (mapView != null && routeLayer != null && location != null) {
                    Point point = new Point(location.getLongitude(), location.getLatitude());
                    if (mPoints.size() > 1) {
                        Line line = new Line();
                        line.setStart(mPoints.get(mPoints.size() - 1));
                        line.setEnd(point);
                        Polyline polyline = new Polyline();
                        polyline.addSegment(line, true);
                        // 计算距离
                        double length = GeometryEngine.geodesicLength(polyline, mapView.getSpatialReference(), null);
                        if (length > 0) {//产生了移动才加入
                            Graphic lineGraphic = new Graphic(polyline, lineSymbol);
                            mGraphicList.add(lineGraphic);
                            routeLayer.addGraphic(lineGraphic);
                            mPoints.add(point);
                            countSize += length / 1000;
                            DecimalFormat df = new DecimalFormat("#.00");
                            tvTabCountSize.setText(df.format(countSize).startsWith(".") ? "0" + df.format(countSize) : df.format(countSize));
                        }
                    } else {
                        mPoints.add(point);
                    }
                }
            }
            //信息保存
            zxSharedPrefUtil.putLong(RECORD_START_TIME, lastCurrentTime);//保存开始时间
            zxSharedPrefUtil.putList(RECORD_POINTS, mPoints);//保存点集
        }
    };

    public void onDestory() {
        timeTimer.cancel();
        routeTimer.cancel();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    /**
     * 地图绘制点集合
     */
    public void refreshPoints() {
        List<FootFileBean> footFiles = zxSharedPrefUtil.getList(ConstStrings.FootFiles);
        if (footFiles == null || footFiles.size() == 0) {
            return;
        }
        int[] graphicIds = routeLayer.getGraphicIDs();
        for (int graphicId : graphicIds) {
            if (routeLayer.getGraphic(graphicId).getAttributes() != null && routeLayer.getGraphic(graphicId).getAttributes().containsKey("footId")) {
                routeLayer.removeGraphic(graphicId);
            }
        }
        for (FootFileBean footFile : footFiles) {
            Drawable drawable = FootUtil.drawTextToDrawable(context, R.mipmap.foot_pop_bg, footFile.getLocationName());
            if (drawable == null) {
                continue;
            }
            PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);
            pictureMarkerSymbol.setOffsetY(20);
            Point point = footFile.getMapPoint();
            if (point != null) {
                Map<String, Object> attr = new HashMap<>();
                attr.put("footId", footFile.getId());
                Graphic graphic = new Graphic(point, pictureMarkerSymbol, attr);
                routeLayer.addGraphic(graphic);
            }
        }
    }

}
