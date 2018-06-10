package cn.gisdata.footprint.module.my.mvp.model;

import cn.gisdata.footprint.api.service.ApiService;
import cn.gisdata.footprint.module.my.bean.MyFootRouteEntity;
import com.frame.zxmvp.base.BaseModel;

import cn.gisdata.footprint.module.my.mvp.contract.RouteListContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class RouteListModel extends BaseModel implements RouteListContract.Model {


    @Override
    public Observable<List<MyFootRouteEntity>> requestMyFootRouteList(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .getMyFootRouteList(map)
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