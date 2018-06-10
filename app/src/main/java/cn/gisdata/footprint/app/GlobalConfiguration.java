package cn.gisdata.footprint.app;

import android.app.Application;
import android.content.Context;

import cn.gisdata.footprint.BuildConfig;
import cn.gisdata.footprint.api.ApiConstants;
import cn.gisdata.footprint.api.cache.CommonCache;
import cn.gisdata.footprint.api.service.ApiService;
import com.frame.zxmvp.di.module.GlobalConfigModule;
import com.frame.zxmvp.http.AppDelegate;
import com.frame.zxmvp.http.GlobalHttpHandler;
import com.frame.zxmvp.integration.ConfigModule;
import com.frame.zxmvp.integration.IRepositoryManager;
import com.zx.zxutils.util.ZXLogUtil;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.baseurl(ApiConstants.RELEASE_URL)
                //使用builder可以为框架配置一些配置信息
                .globalHttpHandler(new GlobalHttpHandler() {// 这里可以提供一个全局处理Http请求和响应结果的处理类,
                    // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                        /* 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                           重新请求token,并重新执行请求 */
                        ZXLogUtil.loge("Response:" + httpResult);
                        return response;
                    }

                    // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header以及参数加密等操作
                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                        /* 如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则直接返回request参数
                           return chain.request().newBuilder().header("token", tokenId)
                                  .build(); */
                        ZXLogUtil.loge("Request:" + request.url().toString());
                        return request;
                    }
                })
                .responseErroListener((context1, e) -> {
                    /* 用来提供处理所有错误的监听
                       rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效 */
                });
    }

    @Override
    public void registerComponents(Context context, IRepositoryManager repositoryManager) {
        repositoryManager.injectRetrofitService(ApiService.class);
        repositoryManager.injectCacheService(CommonCache.class);
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycles) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(new AppDelegate.Lifecycle() {
            @Override
            public void onCreate(Application application) {
                if (BuildConfig.LOG_DEBUG) {//Timber日志打印
                }
            }

            @Override
            public void onTerminate(Application application) {

            }
        });
    }

}
