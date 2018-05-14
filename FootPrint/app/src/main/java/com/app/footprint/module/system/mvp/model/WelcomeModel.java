package com.app.footprint.module.system.mvp.model;

import com.app.footprint.api.service.ApiService;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.WelcomeContract;
import com.frame.zxmvp.base.BaseModel;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import rx.Observable;

/**
 * Created by admin on 2017/2/28.
 */
public class WelcomeModel extends BaseModel implements WelcomeContract.Model {


    @Override
    public Observable<LoginEntity> loginAppData(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .login(map)
                .compose(RxHelper.<LoginEntity>handleResult())
                .compose(RxSchedulers.<LoginEntity>io_main());
    }
}