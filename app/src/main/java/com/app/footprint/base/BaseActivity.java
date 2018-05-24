package com.app.footprint.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import com.app.footprint.app.MyApplication;
import com.frame.zxmvp.base.BaseModel;
import com.frame.zxmvp.base.BasePresenter;
import com.frame.zxmvp.base.RxBaseActivity;
import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.util.ZXSharedPrefUtil;
import com.zx.zxutils.util.ZXToastUtil;

/**
 * Created by Xiangb on 2017/6/29.
 * 功能：
 */

public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends RxBaseActivity<T, E> {
    public ZXSharedPrefUtil mSharedPrefUtil = new ZXSharedPrefUtil();
    public Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
    }

    @Override
    public void showToast(String message) {
        ZXToastUtil.showToast(message);
    }

    @Override
    public void showLoading(String message, int progress) {
        ZXDialogUtil.showLoadingDialog(this, message, progress);
    }

    @Override
    public void showLoading(String message) {
        ZXDialogUtil.showLoadingDialog(this, message);
    }

    @Override
    public void dismissLoading() {
        ZXDialogUtil.dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        MyApplication.getInstance().remove(this.getClass());
        super.onDestroy();
    }
}
