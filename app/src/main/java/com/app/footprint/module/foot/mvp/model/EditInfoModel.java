package com.app.footprint.module.foot.mvp.model;

import com.app.footprint.api.service.ApiService;
import com.frame.zxmvp.base.BaseModel;

import com.app.footprint.module.foot.mvp.contract.EditInfoContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class EditInfoModel extends BaseModel implements EditInfoContract.Model {

    @Override
    public Observable<String> commitFile(RequestBody requestBody) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .commitFile(requestBody)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}