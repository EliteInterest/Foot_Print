package com.app.footprint.api.service;

import com.app.footprint.module.my.bean.UserInfoEntity;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.bean.VersionEntity;
import com.frame.zxmvp.basebean.BaseRespose;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Create By Xiangb On 2017/7/11
 * 功能：接口列表
 */
public interface ApiService {

    @GET("fsms/user/logon")
    Observable<BaseRespose<LoginEntity>> login(@QueryMap Map<String, String> map);

    @GET("fsms/user/logon")
    Observable<BaseRespose<VersionEntity>> getVersionInfo(@QueryMap Map<String, String> map);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/fsms/user/register")
//    Observable<BaseRespose<LoginEntity>> register(@QueryMap Map<String, String> map);
    Observable<BaseRespose<LoginEntity>> register(@Body RequestBody param);

    @GET("/fsms/user/register/phone/code")
    Observable<BaseRespose<LoginEntity>> sendPhoneNum(@QueryMap Map<String, String> map);

    @GET("/fsms/footprint/details/relation/info")
    Observable<BaseRespose<UserInfoEntity>> requestUserInfo(@QueryMap Map<String, String> map);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/fsms/user/headPortraits/upload")
    Observable<BaseRespose<UserInfoEntity>> uploadHead(@Body RequestBody param);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/fsms/user/info/upload")
    Observable<BaseRespose<UserInfoEntity>> uploadName(@Body RequestBody param);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/fsms/user/phone/upload")
    Observable<BaseRespose<UserInfoEntity>> uploadPhone(@Body RequestBody param);
}
