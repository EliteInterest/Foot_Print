package com.app.footprint.module.system.mvp.model;

import com.app.footprint.api.service.ApiService;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.RegisterContract;
import com.frame.zxmvp.base.BaseModel;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class RegisterModel extends BaseModel implements RegisterContract.Model {

    @Override
    public Observable<LoginEntity> sendPhoneData(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .sendPhoneNum(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<LoginEntity> registerData(RequestBody param) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .register(param)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}