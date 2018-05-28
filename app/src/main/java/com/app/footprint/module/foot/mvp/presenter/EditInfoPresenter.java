package com.app.footprint.module.foot.mvp.presenter;

import com.app.footprint.module.foot.mvp.contract.EditInfoContract;
import com.app.footprint.module.my.bean.UserInfoEntity;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class EditInfoPresenter extends EditInfoContract.Presenter {


    @Override
    public void commitFile(Map<String, Object> map) {
        String FootmarkInfo = (String) map.get("FootmarkInfo");
        int uploadType = (int) map.get("uploadType");
        RequestBody requestBody = null;
        List<File> files = (List<File>) map.get("file");
        switch (uploadType) {
            case 1:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("FootmarkInfo", FootmarkInfo)
                        .build();
                break;

            case 2:
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("FootmarkInfo", FootmarkInfo);
                for (File file : files)
                    builder.addFormDataPart("FileInfo", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                builder.build();
                break;

            case 3:
                builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("FootmarkInfo", FootmarkInfo);
                for (File file : files)
                    builder.addFormDataPart("FileInfo", file.getName(), RequestBody.create(MediaType.parse("video/*"), file));
                builder.build();
                break;
        }

        mModel.commitFile(requestBody)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<String>() {
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

//    @Override
//    public void commitFile(String FootmarkInfo, List<String> files) {
//        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("FootmarkInfo", FootmarkInfo)
////                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
//                .build();
//
//        mModel.commitFile(requestBody)
//                .compose(RxHelper.bindToLifecycle(mView))
//                .subscribe(new RxSubscriber<String>() {
//                    @Override
//                    protected void _onNext(String o) {
//                        mView.onFileCommitResult(o);
//                    }
//
//                    @Override
//                    protected void _onError(String message) {
//
//                        mView.showToast(message);
//                    }
//                });
//    }
}