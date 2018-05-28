package com.app.footprint.module.foot.mvp.presenter;

import com.app.footprint.module.foot.mvp.contract.EditInfoContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class EditInfoPresenter extends EditInfoContract.Presenter {


//    @Override
//    public void commitFile(Map<String, String> map) {
////
////    }

    @Override
    public void commitFile(String FootmarkInfo, List<String> files) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("FootmarkInfo", FootmarkInfo)
//                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        mModel.commitFile(requestBody)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<String>(mView) {
                    @Override
                    protected void _onNext(String o) {
                        mView.onFileCommitResult(o);
                    }

                    @Override
                    protected void _onError(String message) {

                        mView.showToast(message);
                    }
                });
    }
}