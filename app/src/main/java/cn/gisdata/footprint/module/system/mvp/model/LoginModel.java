package cn.gisdata.footprint.module.system.mvp.model;

import cn.gisdata.footprint.api.service.ApiService;
import cn.gisdata.footprint.module.system.bean.LoginEntity;
import cn.gisdata.footprint.module.system.mvp.contract.LoginContract;
import com.frame.zxmvp.base.BaseModel;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSchedulers;

import java.util.Map;

import rx.Observable;

/**
 * Created by admin on 2017/2/28.
 */
public class LoginModel extends BaseModel implements LoginContract.Model {


    @Override
    public Observable<LoginEntity> loginData(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .login(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}
