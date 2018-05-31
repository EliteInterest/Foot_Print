package com.app.footprint.module.my.mvp.model;

import com.app.footprint.api.service.ApiService;
import com.app.footprint.module.my.bean.MyfootMarkEntity;
import com.app.footprint.module.my.mvp.contract.FootListContract;
import com.frame.zxmvp.base.BaseModel;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class FootListModel extends BaseModel implements FootListContract.Model {

    @Override
    public Observable<List<MyfootMarkEntity>> requestMyFootMarkList(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .getMyFootMarkList(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<String> deleteList(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .deleteFoot(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}