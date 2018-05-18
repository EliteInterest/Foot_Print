package com.app.footprint.module.foot.func.view;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.module.map.func.util.GpsUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.util.ZXSharedPrefUtil;
import com.zx.zxutils.util.ZXTimeUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Xiangb on 2018/5/17.
 * 功能：
 */

public class FootRecordView extends RelativeLayout implements View.OnClickListener {

    private Context context;
    private ImageView ivModeRoute, ivModeFoot;//轨迹和足迹切换按钮
    private LinearLayout llModeRoute, llModeFoot;//轨迹足迹界面
    private LinearLayout llStartRecord;//开始录制
    private ImageView ivTabCancel;//取消录制
    private CardView cvRecordTab;//录制界面
    private TextView tvTabDate, tvTabCountTime, tvTabCountSize;//日期  时长  距离

    private Timer timeTimer, routeTimer;//计时器
    private ZXSharedPrefUtil zxSharedPrefUtil;

    private MapView mapView;
    private GraphicsLayer routeLayer;//路线
    private List<Graphic> mGraphicList = new ArrayList<>();
    private List<Point> mPoints = new ArrayList<>();
    private SimpleLineSymbol lineSymbol;

    private int countTime = 0;//计时
    private double countSize = 0;//里程
    private long lastCurrentTime = 0;

    private static final String RECORD_START_TIME = "record_start_time";
    private static final String RECORD_POINTS = "record_points";
    private static final String RECORD_STATUS = "record_status";
    private static final String RECORD_COUNT_SIZE = "record_count_size";

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
        ivModeRoute = findViewById(R.id.iv_record_mode_route);
        ivModeFoot = findViewById(R.id.iv_record_mode_foot);
        llModeRoute = findViewById(R.id.ll_record_mode_route);
        llModeFoot = findViewById(R.id.ll_record_mode_foot);
        llStartRecord = findViewById(R.id.ll_record_foot_start);
        ivTabCancel = findViewById(R.id.iv_record_tab_cancel);
        cvRecordTab = findViewById(R.id.cv_record_tab);
        tvTabDate = findViewById(R.id.tv_record_tab_date);
        tvTabCountTime = findViewById(R.id.tv_record_tab_time);
        tvTabCountSize = findViewById(R.id.tv_record_tab_mileage);

        ivModeRoute.setOnClickListener(this);
        ivModeFoot.setOnClickListener(this);
        llStartRecord.setOnClickListener(this);
        ivTabCancel.setOnClickListener(this);
        cvRecordTab.setOnClickListener(this);

        lastCurrentTime = System.currentTimeMillis();
        zxSharedPrefUtil = new ZXSharedPrefUtil();
        tvTabDate.setText(ZXTimeUtil.getTime(lastCurrentTime) + " " + ZXTimeUtil.dateToWeek(lastCurrentTime));
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
        mapView.addLayer(routeLayer);

        if (zxSharedPrefUtil.getBool(RECORD_STATUS)
                && zxSharedPrefUtil.getLong(RECORD_START_TIME) > 0
                && zxSharedPrefUtil.getList(RECORD_POINTS).size() > 1
                && zxSharedPrefUtil.getFloat(RECORD_COUNT_SIZE) > 0) {//之前处于绘制状态
            ZXDialogUtil.showYesNoDialog(context, "提示", "检测到之前处于绘制状态，是否继续之前的绘制？", "继续", "放弃",
                    (dialog, which) -> {//继续
                        cvRecordTab.setVisibility(VISIBLE);
                        startTimer(true);
                    },
                    (dialog, which) -> {//放弃
                        zxSharedPrefUtil.remove(RECORD_POINTS);
                        zxSharedPrefUtil.remove(RECORD_START_TIME);
                        zxSharedPrefUtil.putBool(RECORD_STATUS, false);
                    });
        }
    }

    @Override
    public void onClick(View v) {
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
                    zxSharedPrefUtil.putBool(RECORD_STATUS, false);
                });
                break;
            case R.id.cv_record_tab:

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
            tvTabDate.setText(ZXTimeUtil.getTime(lastCurrentTime) + " " + ZXTimeUtil.dateToWeek(lastCurrentTime));
            countTime = (int) ((nowCurrentTime - lastCurrentTime) / 1000);
            countSize = zxSharedPrefUtil.getFloat(RECORD_COUNT_SIZE);
            mPoints = zxSharedPrefUtil.getList(RECORD_POINTS);
            for (int i = 0; i > mPoints.size() - 1; i++) {
                Line line = new Line();
                line.setStart(mPoints.get(i));
                line.setEnd(mPoints.get(i + 1));
                Polyline polyline = new Polyline();
                polyline.addSegment(line, true);
                Graphic lineGraphic = new Graphic(polyline, lineSymbol);
                mGraphicList.add(lineGraphic);
                routeLayer.addGraphic(lineGraphic);
            }
        } else {
            countTime = 0;
            countSize = 0;
            mPoints.clear();
            mGraphicList.clear();
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
                        if (length > 3) {//但单次移动超过3米才添加进去
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
            if (mGraphicList.size() > 0) {
                zxSharedPrefUtil.putLong(RECORD_START_TIME, lastCurrentTime);//保存开始时间
                zxSharedPrefUtil.putList(RECORD_POINTS, mPoints);//保存点集
                zxSharedPrefUtil.putFloat(RECORD_COUNT_SIZE, (float) countSize);
            }
        }
    };

    public void onDestory() {
        timeTimer.cancel();
        routeTimer.cancel();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }
}
