package cn.gisdata.footprint.module.system.mvp.presenter;

import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.module.system.bean.VersionEntity;
import cn.gisdata.footprint.module.system.mvp.contract.MainContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;
import com.frame.zxmvp.http.download.DownInfo;
import com.frame.zxmvp.http.download.listener.DownloadOnNextListener;
import com.frame.zxmvp.http.download.manager.HttpDownManager;
import com.zx.zxutils.util.ZXDialogUtil;

import java.io.File;
import java.util.Map;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MainPresenter extends MainContract.Presenter {

    @Override
    public void getVersionInfo(Map<String, String> map) {
        mModel.versionData(map)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<VersionEntity>() {
                    @Override
                    protected void _onNext(VersionEntity versionEntity) {
                        mView.onVersionResult(versionEntity);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }


    @Override
    public void downLoadApk(VersionEntity versionResult) {
        DownInfo downInfo = new DownInfo(versionResult.getUrl());
        downInfo.setSavePath(ConstStrings.getApkPath() + versionResult.getSourcename());
        downInfo.setListener(new DownloadOnNextListener() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onStart() {
                ZXDialogUtil.showLoadingDialog(mContext, "正在下载中，请稍后...", 0);
            }

            @Override
            public void onComplete(File file) {
                mView.onApkDownloadResult(file);
                ZXDialogUtil.dismissLoadingDialog();
            }

            @Override
            public void updateProgress(int progress) {
                ZXDialogUtil.showLoadingDialog(mContext, "正在下载中，请稍后...", progress);
            }
        });
        HttpDownManager.getInstance().startDown(downInfo);
    }
}