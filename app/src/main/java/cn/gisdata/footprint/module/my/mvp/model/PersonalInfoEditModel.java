package cn.gisdata.footprint.module.my.mvp.model;

import cn.gisdata.footprint.api.service.ApiService;
import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import cn.gisdata.footprint.module.system.bean.LoginEntity;
import com.frame.zxmvp.base.BaseModel;

import cn.gisdata.footprint.module.my.mvp.contract.PersonalInfoEditContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class PersonalInfoEditModel extends BaseModel implements PersonalInfoEditContract.Model {

    @Override
    public Observable<UserInfoEntity> uploadName(RequestBody requestBody) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .uploadName(requestBody)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<UserInfoEntity> uploadPhone(RequestBody requestBody) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .uploadPhone(requestBody)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<LoginEntity> sendPhoneData(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .sendPhoneNum(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}