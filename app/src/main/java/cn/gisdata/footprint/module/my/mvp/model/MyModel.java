package cn.gisdata.footprint.module.my.mvp.model;

import cn.gisdata.footprint.api.service.ApiService;
import cn.gisdata.footprint.module.my.bean.IntegralEntity;
import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import com.frame.zxmvp.base.BaseModel;

import cn.gisdata.footprint.module.my.bean.VersionCheckEntity;
import cn.gisdata.footprint.module.my.mvp.contract.MyContract;
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

    @Override
    public Observable<VersionCheckEntity> requestVersionCheckInfo(Map<String, String> map) {
         return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .checkVersion(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}