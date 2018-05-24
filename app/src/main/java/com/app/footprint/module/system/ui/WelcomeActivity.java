package com.app.footprint.module.system.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.WelcomeContract;
import com.app.footprint.module.system.mvp.model.WelcomeModel;
import com.app.footprint.module.system.mvp.presenter.WelcomePresenter;
import com.zx.zxutils.util.ZXFileUtil;
import com.zx.zxutils.util.ZXPermissionUtil;
import com.zx.zxutils.util.ZXStringUtil;
import com.zx.zxutils.util.ZXSystemUtil;


/**
 * Create By Xiangb On 2017/7/11
 * 功能：欢迎页
 */
public class WelcomeActivity extends BaseActivity<WelcomePresenter, WelcomeModel> implements WelcomeContract.View {
    private static final String TAG = "WelcomeActivity";
    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ConstStrings.LOCAL_PATH = ZXSystemUtil.getSDCardPath();
        ConstStrings.INI_PATH = getFilesDir().getPath();
        ZXFileUtil.deleteFiles(ConstStrings.getApkPath());
        ZXFileUtil.deleteFiles(ConstStrings.getCachePath());
        ZXFileUtil.deleteFiles(ConstStrings.getOnlinePath());
        if (!ZXPermissionUtil.checkPermissionsByArray(permissions)) {
            ZXPermissionUtil.requestPermissionsByArray(this);
        } else {
            loginIn();
        }
    }

    /**
     * 登录
     */
    private void loginIn() {
        String userName = mSharedPrefUtil.getString("userName");
        String userPwd = mSharedPrefUtil.getString("userPwd");
        if (!ZXStringUtil.isEmpty(userName) && !ZXStringUtil.isEmpty(userPwd)) {
            ConstStrings.code = mSharedPrefUtil.getString("code");
            ConstStrings.e = mSharedPrefUtil.getString("e");
            ConstStrings.usename = mSharedPrefUtil.getString("userName");
            showLoading("正在登陆中...");
            mPresenter.doLogin(ApiParamUtil.getLoginDataInfo(userName, userPwd));
        } else {
            this.setTheme(R.style.MyAppTheme1);
            LoginActivity.startAction(this, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!ZXPermissionUtil.checkPermissionsByArray(permissions)) {
            showToast("当前应用缺乏必要权限");
            finish();
        } else {
            loginIn();
        }
    }

    @Override
    public void onLoginResult(LoginEntity loginEntity) {
        dismissLoading();
        this.setTheme(R.style.MyAppTheme1);
        if (loginEntity == null) {
            LoginActivity.startAction(this, true);
        } else {
            Log.i(TAG,"loginEntity.getUserName() is " +loginEntity.getUserName());
            mSharedPrefUtil.putString("userName", loginEntity.getUserName());
            mSharedPrefUtil.putString("userId", loginEntity.getUserId());
            mSharedPrefUtil.putString("headPortraits", loginEntity.getHeadPortraits());
            mSharedPrefUtil.putString("nickName", loginEntity.getNickname());
            mSharedPrefUtil.putString("phone", loginEntity.getPhone());
            goToMain();
        }
    }

    /**
     * 根据登录状态判断是否直接进入主界面
     */
    private void goToMain() {
        handler.post(() -> {
            MainActivity.startAction(this, true);
        });
    }

    @Override
    public void showLoading(String message, int progress) {

    }
}
