package com.app.footprint.module.system.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.app.MyApplication;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.LoginContract;
import com.app.footprint.module.system.mvp.model.LoginModel;
import com.app.footprint.module.system.mvp.presenter.LoginPresenter;
import com.app.footprint.base.BaseActivity;
import com.zx.zxutils.util.ZXMD5Util;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_dologin)
    Button btnDologin;
    private String userName, userPwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public void onLoginResult(LoginEntity loginEntity) {
        showToast("登录成功");
        ConstStrings.code = loginEntity.getCode();
        ConstStrings.e = ZXMD5Util.getMD5(ZXMD5Util.getMD5(userPwd)
                + loginEntity.getSalt());
        ConstStrings.usename = loginEntity.getName();
        ConstStrings.adrApikey = loginEntity.getAdrApikey();
        mSharedPrefUtil.putString("userName", ConstStrings.usename);
        mSharedPrefUtil.putString("userPwd", userPwd);
        mSharedPrefUtil.putString("code", ConstStrings.code);
        mSharedPrefUtil.putString("e", ConstStrings.e);
        mSharedPrefUtil.putString("adrApikey", ConstStrings.adrApikey);
        MainActivity.startAction(this, false);
    }

    @OnClick(R.id.btn_dologin)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_dologin:
                //TODO 要删除
                MainActivity.startAction(this, false);

//                userName = etUsername.getText().toString();
//                userPwd = etPassword.getText().toString();
//                if (TextUtils.isEmpty(userName)) {
//                    showToast("请输入用户名");
//                    return;
//                }
//                if (TextUtils.isEmpty(userPwd)) {
//                    showToast("请输入用户密码");
//                    return;
//                }
//                mPresenter.doLogin(ApiParamUtil.getLoginDataInfo(userName, userPwd));
                break;

            default:
                break;
        }

    }

    private long triggerAtTimefirst = 0;

    @Override
    public void onBackPressed() {

        long triggerAtTimeSecond = triggerAtTimefirst;
        triggerAtTimefirst = SystemClock.elapsedRealtime();
        if (triggerAtTimefirst - triggerAtTimeSecond <= 2000) {
            MyApplication.getInstance().finishAll();
        } else {
            showToast("请再点击一次, 确认退出...");
        }
    }
}
