package com.app.footprint.module.system.mvp.presenter;

import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.LoginContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.Map;

/**
 * Created by admin on 2017/2/28.
 */
public class LoginPresenter extends LoginContract.Presenter {

    public void doLogin(Map<String, String> map) {
        mModel.loginData(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<LoginEntity>() {
                    @Override
                    protected void _onNext(LoginEntity loginEntity) {
                        mView.onLoginResult(loginEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }
}
