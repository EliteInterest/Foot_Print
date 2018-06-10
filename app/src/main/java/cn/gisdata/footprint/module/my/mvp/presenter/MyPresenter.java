package cn.gisdata.footprint.module.my.mvp.presenter;

import cn.gisdata.footprint.module.my.bean.IntegralEntity;
import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import cn.gisdata.footprint.module.my.mvp.contract.MyContract;
import cn.gisdata.footprint.module.system.bean.LoginEntity;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.Map;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyPresenter extends MyContract.Presenter {
    public void doRequestUserInfo(Map<String, String> map) {
        mModel.requestUserInfo(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<UserInfoEntity>(mView) {
                    @Override
                    protected void _onNext(UserInfoEntity userInfoEntity) {
                        mView.onRequestUserInfoResult(userInfoEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }
}