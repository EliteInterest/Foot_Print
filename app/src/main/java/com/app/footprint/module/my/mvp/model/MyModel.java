package com.app.footprint.module.my.mvp.model;

import com.app.footprint.api.service.ApiService;
import com.app.footprint.module.my.bean.UserInfoEntity;
import com.frame.zxmvp.base.BaseModel;

import com.app.footprint.module.my.mvp.contract.MyContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyModel extends BaseModel implements MyContract.Model {

    @Override
    public Observable<UserInfoEntity> requestUserInfo(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .requestUserInfo(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}