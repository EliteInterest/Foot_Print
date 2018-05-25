package com.app.footprint.module.my.mvp.presenter;

import com.app.footprint.module.my.bean.MyfootMarkEntity;
import com.app.footprint.module.my.mvp.contract.MyFootPointContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.List;
import java.util.Map;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyFootPointPresenter extends MyFootPointContract.Presenter {

    @Override
    public void doRequestMyFootMarkList(Map<String, String> map) {
        mModel.requestMyFootMarkList(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<List<MyfootMarkEntity>>(mView) {
                    @Override
                    protected void _onNext(List<MyfootMarkEntity> list) {
                        mView.onRequestMyFootMarkListResult(list);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }
}