package com.app.footprint.module.system.mvp.contract;

import com.app.footprint.module.map.bean.MapUrlBean;
import com.app.footprint.module.system.bean.LoginEntity;
import com.frame.zxmvp.base.BasePresenter;
import com.frame.zxmvp.base.IModel;
import com.frame.zxmvp.base.IView;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by admin on 2017/2/28.
 */
public interface WelcomeContract {
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<LoginEntity> loginAppData(Map<String, String> map);

        Observable<List<MapUrlBean>> mapUrlData();
    }

    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void onLoginResult(LoginEntity loginEntity);

        void onMapUrlResult(List<MapUrlBean> mapUrlBeans);
    }

    //方法
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void doLogin(Map<String, String> map);

        public abstract void getMapUrl();
    }
}
