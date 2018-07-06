package cn.gisdata.footprint.module.my.mvp.presenter;

import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.Map;

import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import cn.gisdata.footprint.module.my.bean.VersionCheckEntity;
import cn.gisdata.footprint.module.my.mvp.contract.MyContract;


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
//                        mView.showToast(message);
                    }
                });
    }

    @Override
    public void doRequestVersionCheck(Map<String, String> map) {
        mModel.requestVersionCheckInfo(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<VersionCheckEntity>(mView) {
                    @Override
                    protected void _onNext(VersionCheckEntity versonCheckEntity) {
                        mView.onVersionCheckResult(versonCheckEntity);
                    }

                    @Override
                    protected void _onError(String message) {
//                        mView.showToast(message);
                    }
                });
    }
}