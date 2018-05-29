package com.app.footprint.module.foot.mvp.contract;

import com.app.footprint.module.foot.bean.FootFileBean;
import com.frame.zxmvp.base.BasePresenter;
import com.frame.zxmvp.base.IModel;
import com.frame.zxmvp.base.IView;

import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public interface FootContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void onRouteCommitResult(String url);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<String> commitRoute(Map<String, String> map);
    }

    //方法
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void commitRoute(String name, String detail, List<FootFileBean> footFileBeans);
    }
}
