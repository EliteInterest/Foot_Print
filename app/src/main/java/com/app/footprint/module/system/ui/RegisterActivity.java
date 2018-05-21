package com.app.footprint.module.system.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.RegisterContract;
import com.app.footprint.module.system.mvp.model.RegisterModel;
import com.app.footprint.module.system.mvp.presenter.RegisterPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.BindsInstance;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter, RegisterModel> implements RegisterContract.View {

    @BindView(R.id.et_username)
    EditText etUserName;

    @BindView(R.id.et_nickname)
    EditText etNickName;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_phonechecknum)
    EditText etEtPhoneCheck;

    @BindView(R.id.btn_register)
    Button btnRegister;

    @BindView(R.id.btn_dosendPhone)
    Button btnSendPhone;

    @BindView(R.id.btn_cancel)
    Button btnRegisterCancel;

    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_register, R.id.btn_cancel, R.id.btn_dosendPhone})
    public void onViewClicked(android.view.View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                String Name = etUserName.getEditableText().toString();
                if (TextUtils.isEmpty(Name)) {
                    showToast("用戶名为空，请输入！");
                    return;
                }

                String Password = etPassword.getEditableText().toString();
                if (TextUtils.isEmpty(Password)) {
                    showToast("用戶名密码为空，请输入！");
                    return;
                }

                String NickName = etNickName.getEditableText().toString();
                if (TextUtils.isEmpty(NickName)) {
                    showToast("用戶名昵称为空，请输入！");
                    return;
                }

                String Phone = etPhone.getEditableText().toString();
                if (TextUtils.isEmpty(Phone)) {
                    showToast("手机号为空，请输入！");
                    return;
                }

                String PhoneCheck = etEtPhoneCheck.getEditableText().toString();
                if (TextUtils.isEmpty(PhoneCheck)) {
                    showToast("验证码为空，请输入！");
                    return;
                }
                mPresenter.doRegister(ApiParamUtil.getRegisterDataInfo(Name, Password, NickName, Phone, PhoneCheck));
                break;

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_dosendPhone:
                //发送短信业务
                String phoneNum = etPhone.getEditableText().toString();
                if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11 || !ConstStrings.isNumeric(phoneNum)) {
                    showToast("请输入正确手机号！");
                    return;
                } else {
                    mPresenter.doSendPhoneNum(ApiParamUtil.getPhoneDataInfo(phoneNum));
                }
                break;
        }
    }

    @Override
    public void onSendPhoneNUmResult(LoginEntity loginResult) {
        showToast("手机号已发送");
    }

    @Override
    public void onRegisterResult(LoginEntity loginResult) {
        showToast("注册成功");
        mSharedPrefUtil.putString("userName",etUserName.getText().toString());
        mSharedPrefUtil.putString("userPwd", etPassword.getText().toString());
        mSharedPrefUtil.putBool("registered", true);
        finish();
    }
}
