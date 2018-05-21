package com.app.footprint.module.foot.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.foot.mvp.contract.CameraContract;
import com.app.footprint.module.foot.mvp.model.CameraModel;
import com.app.footprint.module.foot.mvp.presenter.CameraPresenter;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class CameraActivity extends BaseActivity<CameraPresenter, CameraModel> implements CameraContract.View {

    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, CameraActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    public void onViewClicked(View view) {

    }

}
