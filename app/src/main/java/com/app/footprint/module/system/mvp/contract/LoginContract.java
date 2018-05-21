package com.app.footprint.module.system.mvp.contract;

import com.app.footprint.module.system.bean.LoginEntity;
import com.frame.zxmvp.base.BasePresenter;
import com.frame.zxmvp.base.IModel;
import com.frame.zxmvp.base.IView;

import java.util.Map;

import rx.Observable;


/**
 * Created by admin on 2017/2/28.
 */

public interface LoginContract {

    interface Model extends IModel {
        Observable<LoginEntity> loginData(Map<String, String> map);

    }

    interface View extends IView {
        void onLoginResult(LoginEntity loginResult);
        void onLoginStart();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void doLogin(Map<String, String> map);
    }

}
