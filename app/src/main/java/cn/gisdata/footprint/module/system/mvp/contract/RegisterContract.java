package cn.gisdata.footprint.module.system.mvp.contract;

import cn.gisdata.footprint.module.system.bean.LoginEntity;
import com.frame.zxmvp.base.BasePresenter;
import com.frame.zxmvp.base.IModel;
import com.frame.zxmvp.base.IView;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public interface RegisterContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void onSendPhoneNUmResult(LoginEntity loginResult);
        void onRegisterResult(LoginEntity loginResult);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        public Observable<LoginEntity> sendPhoneData(Map<String, String> map);
        public Observable<LoginEntity> registerData(RequestBody param);
    }

    //方法
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void doRegister(Map<String, String> map);
        public abstract void doSendPhoneNum(Map<String, String> map);
    }
}
