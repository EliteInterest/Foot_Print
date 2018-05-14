package com.app.footprint.api.service;

import com.app.footprint.api.ApiConstants;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.bean.VersionEntity;
import com.frame.zxmvp.basebean.BaseRespose;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Create By Xiangb On 2017/7/11
 * 功能：接口列表
 */
public interface ApiService {

    @GET(ApiConstants.SERVER_URL)
    Observable<BaseRespose<LoginEntity>> login(@QueryMap Map<String, String> map);


    @GET(ApiConstants.SERVER_URL)
    Observable<BaseRespose<VersionEntity>> getVersionInfo(@QueryMap Map<String, String> map);

}
