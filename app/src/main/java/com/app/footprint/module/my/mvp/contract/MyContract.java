package com.app.footprint.module.my.mvp.contract;

import com.app.footprint.module.my.bean.IntegralEntity;
import com.app.footprint.module.my.bean.UserInfoEntity;
import com.frame.zxmvp.base.BasePresenter;
import com.frame.zxmvp.base.IModel;
import com.frame.zxmvp.base.IView;

import java.util.Map;

import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public interface MyContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void onRequestUserInfoResult(UserInfoEntity userInfoEntity);
        void onRequestIntergralInfoResult(IntegralEntity integralEntity);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<UserInfoEntity> requestUserInfo(Map<String, String> map);
        Observable<IntegralEntity> RequestintegralInfo(Map<String, String> map);
    }

    //方法
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void doRequestUserInfo(Map<String, String> map);
        public abstract void doRequestintegralInfo(Map<String, String> map);
    }
}
