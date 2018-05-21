package com.app.footprint.module.system.mvp.presenter;

import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.RegisterContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class RegisterPresenter extends RegisterContract.Presenter {

    @Override
    public void doRegister(Map<String, String> map) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new JSONObject(map).toString());
        mModel.registerData(requestBody)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<LoginEntity>() {
                    @Override
                    protected void _onNext(LoginEntity loginEntity) {
                        mView.onRegisterResult(loginEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }

    public void doSendPhoneNum(Map<String, String> map) {
        mModel.sendPhoneData(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<LoginEntity>() {
                    @Override
                    protected void _onNext(LoginEntity loginEntity) {
                        mView.onSendPhoneNUmResult(loginEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }
}