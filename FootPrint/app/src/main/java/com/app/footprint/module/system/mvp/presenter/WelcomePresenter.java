package com.app.footprint.module.system.mvp.presenter;

import com.app.footprint.app.ConstStrings;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.WelcomeContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;
import com.zx.zxutils.util.ZXSharedPrefUtil;

import java.util.Map;


/**
 * Created by admin on 2017/2/28.
 */
public class WelcomePresenter extends WelcomeContract.Presenter {

    @Override
    public void doLogin(Map<String, String> map) {
        mModel.loginAppData(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<LoginEntity>(mView) {
                    @Override
                    protected void _onNext(LoginEntity loginEntity) {
                        ConstStrings.code = loginEntity.getCode();
                        ConstStrings.adrApikey = loginEntity.getAdrApikey();
                        ZXSharedPrefUtil zxSharedPrefUtil = new ZXSharedPrefUtil("maincity");
                        zxSharedPrefUtil.putString("code", ConstStrings.code);
                        zxSharedPrefUtil.putString("adrApikey", ConstStrings.adrApikey);
                        mView.onLoginResult(loginEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                        mView.onLoginResult(null);
                    }
                });
    }

}