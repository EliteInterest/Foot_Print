package cn.gisdata.footprint.module.system.mvp.model;

import cn.gisdata.footprint.api.service.ApiService;
import cn.gisdata.footprint.module.system.bean.VersionEntity;
import cn.gisdata.footprint.module.system.mvp.contract.MainContract;
import com.frame.zxmvp.base.BaseModel;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MainModel extends BaseModel implements MainContract.Model {

    @Override
    public Observable<VersionEntity> versionData(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .getVersionInfo(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}