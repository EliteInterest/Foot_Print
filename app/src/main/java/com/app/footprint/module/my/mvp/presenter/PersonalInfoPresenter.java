package com.app.footprint.module.my.mvp.presenter;

import android.util.Log;

import com.app.footprint.module.my.bean.UserInfoEntity;
import com.app.footprint.module.my.mvp.contract.PersonalInfoContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class PersonalInfoPresenter extends PersonalInfoContract.Presenter {

    @Override
    public void doUploadHeadPortraits(Map<String, String> map) {
//        if (type == PersonalInfoContract.ActivityRequestCode.UPLOAD_STEP_USERID) {
//        Map<String, String> map1 = new HashMap<>();
//        map1.put("userId", map.get("userId"));
//            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
//                    new JSONObject(map1).toString());
//        Log.i("wangwansheng","userId is " + new JSONObject(map1).toString());

        String filename = map.get("file");
//        Log.i("wangwansheng","file is " +filename);
        File file = new File(filename);
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part part = MultipartBody.Part.createFormData("file",file.getName(),requestFile);
//
//        String UserId = map.get("userId");
//        Log.i("wangwansheng","userId is " + UserId);
//        RequestBody userId =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), UserId);

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", map.get("userId"))
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();


            mModel.uploadHeadPortraits(requestBody)
                    .compose(RxHelper.bindToLifecycle(mView))
                    .subscribe(new RxSubscriber<UserInfoEntity>() {
                        @Override
                        protected void _onNext(UserInfoEntity userInfoEntity) {
                            mView.onUploadUserHeadFileResult(userInfoEntity);
                        }

                        @Override
                        protected void _onError(String message) {
                            mView.showToast(message);
                        }
                    });
//        } else if(type  == PersonalInfoContract.ActivityRequestCode.UPLOAD_STEP_FILE){
//            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
//                    new JSONObject(map).toString());
//
//            mModel.uploadHeadPortraits(requestBody)
//                    .compose(RxHelper.bindToLifecycle(mView))
//                    .subscribe(new RxSubscriber<UserInfoEntity>() {
//                        @Override
//                        protected void _onNext(UserInfoEntity userInfoEntity) {
//                            mView.onUploadUserHeadFileResult(userInfoEntity);
//                        }
//
//                        @Override
//                        protected void _onError(String message) {
//                            mView.showToast(message);
//                        }
//                    });
//        }
    }
}