package cn.gisdata.footprint.module.system.mvp.presenter;

import cn.gisdata.footprint.module.system.bean.LoginEntity;
import cn.gisdata.footprint.module.system.mvp.contract.LoginContract;
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
                .subscribe(new RxSubscriber<LoginEntity>(mView) {
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
