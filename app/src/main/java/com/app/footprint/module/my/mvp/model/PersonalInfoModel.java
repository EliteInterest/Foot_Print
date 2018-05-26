package com.app.footprint.module.my.mvp.model;

import com.app.footprint.api.service.ApiService;
import com.app.footprint.module.my.bean.UserInfoEntity;
import com.app.footprint.module.system.bean.LoginEntity;
import com.frame.zxmvp.base.BaseModel;

import com.app.footprint.module.my.mvp.contract.PersonalInfoContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class PersonalInfoModel extends BaseModel implements PersonalInfoContract.Model {
//    @Override
//    public Observable<UserInfoEntity> uploadHeadPortraits(RequestBody userId, MultipartBody.Part file) {
//        return mRepositoryManager.obtainRetrofitService(ApiService.class)
//                .uploadHeadFile(userId,file)
//                .compose(RxHelper.handleResult())
//                .compose(RxSchedulers.io_main());
//    }

    @Override
    public Observable<UserInfoEntity> uploadHeadPortraits(RequestBody requestBody) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .uploadHeadFile(requestBody)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}