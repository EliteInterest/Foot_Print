package cn.gisdata.footprint.module.my.mvp.contract;

import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import com.frame.zxmvp.base.BasePresenter;
import com.frame.zxmvp.base.IView;
import com.frame.zxmvp.base.IModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public interface PersonalInfoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void onUploadUserHeadFileResult(UserInfoEntity userInfoEntity);
    }

    //Model层定义接口,外部只需关心Model返回的数据,  无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<UserInfoEntity> uploadHeadPortraits(RequestBody requestBody);
//        public Observable<UserInfoEntity> uploadHeadPortraits(RequestBody userId, MultipartBody.Part file);
    }

    //方法
    abstract class Presenter extends BasePresenter<View, Model> {
//        public abstract void doUploadHeadPortraits(int type,Map<String, String> map);
        public abstract void doUploadHeadPortraits(Map<String, String> map);
    }

    public interface ActivityRequestCode { int SHOW_MAP_DEPOT = 1;
        int UPLOAD_STEP_USERID = 0;
        int UPLOAD_STEP_FILE = 1;
    }
}
