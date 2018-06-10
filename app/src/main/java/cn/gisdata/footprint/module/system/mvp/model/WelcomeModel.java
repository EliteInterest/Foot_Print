package cn.gisdata.footprint.module.system.mvp.model;

import cn.gisdata.footprint.api.service.ApiService;
import cn.gisdata.footprint.module.map.bean.MapUrlBean;
import cn.gisdata.footprint.module.system.bean.LoginEntity;
import cn.gisdata.footprint.module.system.mvp.contract.WelcomeContract;
import com.frame.zxmvp.base.BaseModel;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.List;
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

    @Override
    public Observable<List<MapUrlBean>> mapUrlData() {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .getMapUrl()
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}