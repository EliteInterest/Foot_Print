package cn.gisdata.footprint.module.system.ui;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.api.ApiParamUtil;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.base.BaseActivity;
import cn.gisdata.footprint.module.map.bean.BaiduSearchBean;
import cn.gisdata.footprint.module.map.bean.MapUrlBean;
import cn.gisdata.footprint.module.map.func.util.BaiduMapUtil;
import cn.gisdata.footprint.module.map.func.util.GpsUtil;
import cn.gisdata.footprint.module.system.bean.LoginEntity;
import cn.gisdata.footprint.module.system.mvp.contract.WelcomeContract;
import cn.gisdata.footprint.module.system.mvp.model.WelcomeModel;
import cn.gisdata.footprint.module.system.mvp.presenter.WelcomePresenter;
import cn.gisdata.footprint.util.DateUtil;
import cn.gisdata.footprint.util.VersionUtil;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.zx.zxutils.http.common.util.LogUtil;
import com.zx.zxutils.util.ZXFileUtil;
import com.zx.zxutils.util.ZXPermissionUtil;
import com.zx.zxutils.util.ZXStringUtil;
import com.zx.zxutils.util.ZXSystemUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Create By fxs On 2017/7/11
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
        ZXFileUtil.deleteFiles(ConstStrings.getOnlinePath());
        if (!ZXPermissionUtil.checkPermissionsByArray(permissions)) {
            ZXPermissionUtil.requestPermissionsByArray(this);
        } else {
            mPresenter.getMapUrl();
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
            mPresenter.getMapUrl();
//            loginIn();
        }
    }

    @Override
    public void onLoginResult(LoginEntity loginEntity) {
        dismissLoading();
        this.setTheme(R.style.MyAppTheme1);
        if (loginEntity == null) {
            LoginActivity.startAction(this, true);
        } else {
            Log.i(TAG, "loginEntity.getUserName() is " + loginEntity.getUserName());
            mSharedPrefUtil.putString("userName", loginEntity.getUserName());
            mSharedPrefUtil.putString("userId", loginEntity.getUserId());
            mSharedPrefUtil.putString("headPortraits", loginEntity.getHeadPortraits());
            mSharedPrefUtil.putString("nickName", loginEntity.getNickname());
            mSharedPrefUtil.putString("phone", loginEntity.getPhone());
            goToMain();
        }
    }

    @Override
    public void onMapUrlResult(List<MapUrlBean> mapUrlBeans) {
        if (mapUrlBeans != null) {
            mSharedPrefUtil.putList("mapUrl", mapUrlBeans);
        }
        loginIn();
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
