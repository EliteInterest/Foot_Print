package cn.gisdata.footprint.module.my.mvp.presenter;

import cn.gisdata.footprint.module.my.bean.MyfootMarkEntity;
import cn.gisdata.footprint.module.my.mvp.contract.FootListContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.List;
import java.util.Map;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class FootListPresenter extends FootListContract.Presenter {
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