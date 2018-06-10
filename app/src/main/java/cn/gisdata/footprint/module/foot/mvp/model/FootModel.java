package cn.gisdata.footprint.module.foot.mvp.model;

import cn.gisdata.footprint.api.service.ApiService;
import com.frame.zxmvp.base.BaseModel;

import cn.gisdata.footprint.module.foot.mvp.contract.FootContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class FootModel extends BaseModel implements FootContract.Model {

    @Override
    public Observable<String> commitRoute(RequestBody requestBody) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .commitRouteFile(requestBody)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}