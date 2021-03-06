package cn.gisdata.footprint.module.map.func.util;

import android.content.pm.ApplicationInfo;

import com.frame.zxmvp.baserx.RxSchedulers;
import com.frame.zxmvp.baserx.RxSubscriber;
import com.zx.zxutils.ZXApp;
import com.zx.zxutils.util.ZXSystemUtil;

import java.util.HashMap;
import java.util.Map;

import cn.gisdata.footprint.api.service.ApiService;
import cn.gisdata.footprint.app.MyApplication;
import cn.gisdata.footprint.module.map.bean.BaiduLatlngBean;
import cn.gisdata.footprint.module.map.bean.BaiduSearchBean;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by fxs on 2018/5/30.
 * 功能：
 */
public class BaiduMapUtil {

//    private static String ak = "WUSFaSubKbTnqf5tylC7awzDhZ5sKjNi";
    private static String ak = "5OBCNS1Or059Fns48ooP3OhQ44zI9q2Q";


    public static String getStaticBitmapPath(double longitude, double latitude) {
        String point = longitude + "," + latitude;
        String bitmapPath = "http://api.map.baidu.com/staticimage/v2?" +
                "ak=" + ak +
                "&mcode=" + getMcode() +
                "&center=" + point +
                "&width=400" +
                "&height=200" +
                "&zoom=14" +
                "&copyright=1" +
                "&coordtype=wgs84ll" +
                "&markers=" + point;
        return bitmapPath;
    }

    public static String getStaticBitmapPath(double startLongitude, double startLatitude, double endLongitude, double endLatitude) {
        String centerPoint = (startLongitude + endLongitude) / 2 + "," + (startLatitude + endLatitude) / 2;
        String startPoint = startLongitude + "," + startLatitude;
        String endPoint = endLongitude + "," + endLatitude;
        String bitmapPath = "http://api.map.baidu.com/staticimage/v2?" +
                "ak=" + ak +
                "&mcode=" + getMcode() +
                "&center=" + centerPoint +
                "&width=400" +
                "&height=200" +
                "&zoom=14" +
                "&copyright=1" +
                "&coordtype=wgs84ll" +
                "&markers=" + startPoint + "|" + endPoint;
        return bitmapPath;
    }

    public static void searchPoi(double longitude, double latitude, OnBaiduSearchListener baiduSearchListener) {
        Map<String, String> latlngMap = new HashMap<>();
        latlngMap.put("ak", ak);
        latlngMap.put("coords", longitude + "," + latitude);
        latlngMap.put("from", "1");
        latlngMap.put("to", "5");
        latlngMap.put("mcode", getMcode());

        Observable<BaiduLatlngBean> changeLatlng = MyApplication.appComponent
                .repositoryManager()
                .obtainRetrofitService(ApiService.class)
                .queryBaiduLatlng(latlngMap)
                .compose(RxSchedulers.io_main());

        changeLatlng.flatMap(new Func1<BaiduLatlngBean, Observable<BaiduSearchBean>>() {
            @Override
            public Observable<BaiduSearchBean> call(BaiduLatlngBean baiduLatlngBean) {
                if (baiduLatlngBean.getStatus() == 0 && baiduLatlngBean.getResult().size() > 0) {
                    Map<String, String> searchMap = new HashMap<>();
                    searchMap.put("ak", ak);
                    searchMap.put("location", baiduLatlngBean.getResult().get(0).getY() + "," + baiduLatlngBean.getResult().get(0).getX());
                    searchMap.put("output", "json");
                    searchMap.put("mcode", getMcode());
                    return MyApplication.appComponent
                            .repositoryManager()
                            .obtainRetrofitService(ApiService.class)
                            .queryBaiduSearch(searchMap)
                            .compose(RxSchedulers.io_main());
                } else {
                    return null;
                }
            }
        }).subscribe(new RxSubscriber<BaiduSearchBean>() {
            @Override
            protected void _onNext(BaiduSearchBean baiduSearchBean) {
                if (baiduSearchBean.getStatus() == 0) {
                    baiduSearchListener.onSearchBack(baiduSearchBean);
                } else {
//                    ZXToastUtil.showToast("请求位置信息失败");
                    baiduSearchListener.onSearchError();
                }
            }

            @Override
            protected void _onError(String message) {
//                ZXToastUtil.showToast("请求位置信息失败");
                baiduSearchListener.onSearchError();
            }
        });

    }


    public interface OnBaiduSearchListener {
        void onSearchBack(BaiduSearchBean baiduSearchBean);

        void onSearchError();
    }

    public static String getMcode() {
        boolean isDebug = false;
        try {
            ApplicationInfo info = ZXApp.getContext().getApplicationInfo();
            isDebug = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return (isDebug ? "75:05:81:E9:7D:22:22:90:3C:1B:AD:D3:72:26:55:0C:A6:80:64:5E" : "7C:84:99:A0:B0:6F:A9:35:31:BF:C4:CE:45:67:7F:EB:99:01:FE:55") + ";" + ZXSystemUtil.getPackageName();
        return (isDebug ? "75:05:81:E9:7D:22:22:90:3C:1B:AD:D3:72:26:55:0C:A6:80:64:5E" : "68:85:38:19:38:BA:53:91:2F:98:B0:81:63:4D:B4:4C:9E:49:8E:4C") + ";" + ZXSystemUtil.getPackageName();
    }

}
