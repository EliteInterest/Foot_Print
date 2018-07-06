package cn.gisdata.footprint.api.service;

import cn.gisdata.footprint.module.map.bean.BaiduLatlngBean;
import cn.gisdata.footprint.module.map.bean.BaiduSearchBean;
import cn.gisdata.footprint.module.map.bean.MapUrlBean;
import cn.gisdata.footprint.module.my.bean.IntegralEntity;
import cn.gisdata.footprint.module.my.bean.MyFootRouteEntity;
import cn.gisdata.footprint.module.my.bean.MyfootMarkEntity;
import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import cn.gisdata.footprint.module.my.bean.VersionCheckEntity;
import cn.gisdata.footprint.module.system.bean.LoginEntity;
import cn.gisdata.footprint.module.system.bean.VersionEntity;
import com.frame.zxmvp.basebean.BaseRespose;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Create By fxs On 2017/7/11
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
    Observable<BaseRespose<UserInfoEntity>> uploadHeadUseId(@Body RequestBody param);

//    @Multipart
//    @POST("/fsms/user/headPortraits/upload")
//    Observable<BaseRespose<UserInfoEntity>> uploadHeadFile(@Part RequestBody userId, @Part MultipartBody.Part file);

    @POST("/fsms/user/headPortraits/upload")
    Observable<BaseRespose<UserInfoEntity>> uploadHeadFile(@Body RequestBody userId);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/fsms/user/info/upload")
    Observable<BaseRespose<UserInfoEntity>> uploadName(@Body RequestBody param);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/fsms/user/phone/upload")
    Observable<BaseRespose<UserInfoEntity>> uploadPhone(@Body RequestBody param);

    @GET("/fsms/integral/details/info")
    Observable<BaseRespose<IntegralEntity>> getIntegralDetail(@QueryMap Map<String, String> map);

    @GET("/fsms/footprint/footmark/list/info")
    Observable<BaseRespose<List<MyfootMarkEntity>>> getMyFootMarkList(@QueryMap Map<String, String> map);

    @GET("/fsms/footprint/route/list/info")
    Observable<BaseRespose<List<MyFootRouteEntity>>> getMyFootRouteList(@QueryMap Map<String, String> map);

    @POST("/fsms/footprint/footmark/info/upload")
    Observable<BaseRespose<String>> commitFile(@Body RequestBody userId);

    @POST("/fsms/footprint/route/info/upload")
    Observable<BaseRespose<String>> commitRouteFile(@Body RequestBody userId);

    @GET("http://api.map.baidu.com/geocoder/v2/")
    Observable<BaiduSearchBean> queryBaiduSearch(@QueryMap Map<String, String> map);

    @GET("http://api.map.baidu.com/geoconv/v1/")
    Observable<BaiduLatlngBean> queryBaiduLatlng(@QueryMap Map<String, String> map);

    @GET("fsms/config/map/info")
    Observable<BaseRespose<List<MapUrlBean>>> getMapUrl();

    @GET("fsms/footprint/delete/info")
    Observable<BaseRespose<String>> deleteFoot(@QueryMap Map<String, String> map);

    @GET("fsms/system/apk/version/check")
    Observable<BaseRespose<VersionCheckEntity>> checkVersion(@QueryMap Map<String, String> map);

}
