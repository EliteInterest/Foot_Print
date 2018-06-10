package cn.gisdata.footprint.module.system.mvp.presenter;

import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.module.map.bean.MapUrlBean;
import cn.gisdata.footprint.module.system.bean.LoginEntity;
import cn.gisdata.footprint.module.system.mvp.contract.WelcomeContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;
import com.zx.zxutils.util.ZXSharedPrefUtil;

import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2017/2/28.
 */
public class WelcomePresenter extends WelcomeContract.Presenter {

    @Override
    public void doLogin(Map<String, String> map) {
        mModel.loginAppData(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<LoginEntity>(mView) {
                    @Override
                    protected void _onNext(LoginEntity loginEntity) {
//                        ConstStrings.code = loginEntity.getCode();
//                        ConstStrings.adrApikey = loginEntity.getAdrApikey();
                        ZXSharedPrefUtil zxSharedPrefUtil = new ZXSharedPrefUtil();
                        zxSharedPrefUtil.putString("code", ConstStrings.code);
//                        zxSharedPrefUtil.putString("adrApikey", ConstStrings.adrApikey);
                        mView.onLoginResult(loginEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                        mView.onLoginResult(null);
                    }
                });
    }

    @Override
    public void getMapUrl() {
        mModel.mapUrlData()
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<List<MapUrlBean>>(mView) {
                    @Override
                    protected void _onNext(List<MapUrlBean> mapUrlBeans) {
                        mView.onMapUrlResult(mapUrlBeans);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.onMapUrlResult(null);
                    }
                });
    }

}