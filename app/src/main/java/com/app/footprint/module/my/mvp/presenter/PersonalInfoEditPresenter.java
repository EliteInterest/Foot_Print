package com.app.footprint.module.my.mvp.presenter;

import com.app.footprint.module.my.bean.UserInfoEntity;
import com.app.footprint.module.my.mvp.contract.PersonalInfoEditContract;
import com.app.footprint.module.my.mvp.contract.PersonalInfoEditContract.Presenter;
import com.app.footprint.module.system.bean.LoginEntity;
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
public class PersonalInfoEditPresenter extends Presenter {

    @Override
    public void doUploadName(Map<String, String> map) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new JSONObject(map).toString());

        mModel.uploadName(requestBody)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<UserInfoEntity>() {
                    @Override
                    protected void _onNext(UserInfoEntity userInfoEntity) {
                        mView.onUploadResult(userInfoEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }

    @Override
    public void doUploadHeadPortraits(Map<String, String> map) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new JSONObject(map).toString());

        mModel.uploadHeadPortraits(requestBody)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<UserInfoEntity>() {
                    @Override
                    protected void _onNext(UserInfoEntity userInfoEntity) {
                        mView.onUploadResult(userInfoEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }

    @Override
    public void doUploadPhone(Map<String, String> map) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new JSONObject(map).toString());

        mModel.uploadPhone(requestBody)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<UserInfoEntity>() {
                    @Override
                    protected void _onNext(UserInfoEntity userInfoEntity) {
                        mView.onUploadResult(userInfoEntity);
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