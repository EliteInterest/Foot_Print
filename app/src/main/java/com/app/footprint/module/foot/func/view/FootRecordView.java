package com.app.footprint.module.foot.func.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.footprint.R;
import com.zx.zxutils.util.ZXDialogUtil;

/**
 * Created by Xiangb on 2018/5/17.
 * 功能：
 */

public class FootRecordView extends RelativeLayout implements View.OnClickListener {

    private Context context;
    private ImageView ivModeRoute, ivModeFoot;
    private LinearLayout llModeRoute, llModeFoot;
    private LinearLayout llStartRecord;
    private ImageView ivTabCancel;
    private CardView cvRecordTab;

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

        ivModeRoute.setOnClickListener(this);
        ivModeFoot.setOnClickListener(this);
        llStartRecord.setOnClickListener(this);
        ivTabCancel.setOnClickListener(this);
        cvRecordTab.setOnClickListener(this);
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
                break;
            case R.id.iv_record_tab_cancel://录制取消
                ZXDialogUtil.showYesNoDialog(context, "提示", "是否取消路线录制？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cvRecordTab.setVisibility(GONE);
                    }
                });
                break;
            case R.id.cv_record_tab:

                break;
            default:
                break;
        }
    }
}
