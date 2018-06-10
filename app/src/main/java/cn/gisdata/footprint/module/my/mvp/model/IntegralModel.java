package cn.gisdata.footprint.module.my.mvp.model;

import cn.gisdata.footprint.api.service.ApiService;
import cn.gisdata.footprint.module.my.bean.IntegralEntity;
import com.frame.zxmvp.base.BaseModel;

import cn.gisdata.footprint.module.my.mvp.contract.IntegralContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class IntegralModel extends BaseModel implements IntegralContract.Model {

    @Override
    public Observable<IntegralEntity> RequestintegralInfo(Map<String, String> map)
    {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .getIntegralDetail(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}