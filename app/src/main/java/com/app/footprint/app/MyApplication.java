package com.app.footprint.app;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.frame.zxmvp.baseapp.BaseApplication;
import com.frame.zxmvp.di.component.AppComponent;
import com.mob.MobSDK;
import com.zx.zxutils.ZXApp;
import com.zx.zxutils.util.ZXSharedPrefUtil;

import java.util.ArrayList;

public class MyApplication extends BaseApplication {


    private static Context sContext;
    private static MyApplication instance;
    public static ZXSharedPrefUtil mSharedPrefUtil;
    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MobSDK.init(this);
        ZXApp.init(this, true);
        MyApplication.sContext = getApplicationContext();
        mSharedPrefUtil = new ZXSharedPrefUtil();
        appComponent = getAppComponent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
//        AndFixUtil.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    // 单例模式中获取唯一的ExitApplication实例
    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;

    }

    private ArrayList<Activity> activityList = new ArrayList<Activity>();

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
//		App.getInstance().destroyMap();
        System.exit(0);
    }


    //销毁某个activity实例
    public void remove(Class<? extends Activity> t) {
        for (Activity activity : activityList) {
            if (activity.getClass() == t) {
                activity.finish();
            }
        }
    }

    // 遍历所有Activity并finish

    public void finishAll() {
        for (Activity activity : activityList) {
            activity.finish();
        }
//		App.getInstance().destroyMap();
    }

    public boolean haveActivity(Class<? extends Activity> t) {
        for (Activity activity : activityList) {
            if (activity.getClass() == t) {
                return true;
            }
        }
        return false;
    }

    public void clearActivityList() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    public ArrayList<Activity> getActivityList() {
        return activityList;
    }

}
