package com.app.footprint.api.service;

import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.bean.VersionEntity;
import com.frame.zxmvp.basebean.BaseRespose;

import java.util.Map;

import retrofit2.http.GET;
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

    @POST("/fsms/user/register")
    Observable<BaseRespose<LoginEntity>> register(@QueryMap Map<String, String> map);

    @GET("fsms/user/register/phone/code")
    Observable<BaseRespose<LoginEntity>> sendPhoneNum(@QueryMap Map<String, String> map);

}
