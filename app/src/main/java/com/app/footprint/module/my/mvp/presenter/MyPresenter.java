package com.app.footprint.module.my.mvp.presenter;

import com.app.footprint.module.my.bean.IntegralEntity;
import com.app.footprint.module.my.bean.UserInfoEntity;
import com.app.footprint.module.my.mvp.contract.MyContract;
import com.app.footprint.module.system.bean.LoginEntity;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.Map;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyPresenter extends MyContract.Presenter {
    public void doRequestUserInfo(Map<String, String> map) {
        mModel.requestUserInfo(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<UserInfoEntity>(mView) {
                    @Override
                    protected void _onNext(UserInfoEntity userInfoEntity) {
                        mView.onRequestUserInfoResult(userInfoEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }

    public void doRequestintegralInfo(Map<String, String> map) {
        mModel.RequestintegralInfo(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<IntegralEntity>(mView) {
                    @Override
                    protected void _onNext(IntegralEntity integralEntity) {
                        mView.onRequestIntergralInfoResult(integralEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }
}