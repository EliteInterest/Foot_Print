package com.app.footprint.module.system.mvp.presenter;

import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.LoginContract;
import com.frame.zxmvp.basebean.BaseRespose;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.Map;

import rx.Observable;

/**
 * Created by admin on 2017/2/28.
 */
public class LoginPresenter extends LoginContract.Presenter {

    public void doLogin(Map<String, String> map) {
<<<<<<< Updated upstream
        mModel.loginData(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<LoginEntity>(mView) {
=======
        mView.onLoginStart();
        Observable<LoginEntity> Observable1=  mModel.loginData(map);
        Observable1.compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<LoginEntity>() {
>>>>>>> Stashed changes
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
