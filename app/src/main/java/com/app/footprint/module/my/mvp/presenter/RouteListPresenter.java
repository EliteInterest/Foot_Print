package com.app.footprint.module.my.mvp.presenter;

import com.app.footprint.module.my.bean.MyFootRouteEntity;
import com.app.footprint.module.my.mvp.contract.RouteListContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.List;
import java.util.Map;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class RouteListPresenter extends RouteListContract.Presenter {


    @Override
    public void doRequestMyFootRouteList(Map<String, String> map) {
        mModel.requestMyFootRouteList(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<List<MyFootRouteEntity>>(mView) {
                    @Override
                    protected void _onNext(List<MyFootRouteEntity> list) {
                        mView.onRequestMyFootRouteListResult(list);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }

    @Override
    public void deleteList(Map<String, String> map) {
        mModel.deleteList(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<String>(mView) {
                    @Override
                    protected void _onNext(String s) {
                        mView.onDeleteResult();
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }
}