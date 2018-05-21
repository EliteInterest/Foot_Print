package com.app.footprint.module.system.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.app.MyApplication;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.foot.ui.FootFragment;
import com.app.footprint.module.my.ui.MyFragment;
import com.app.footprint.module.system.bean.VersionEntity;
import com.app.footprint.module.system.mvp.contract.MainContract;
import com.app.footprint.module.system.mvp.model.MainModel;
import com.app.footprint.module.system.mvp.presenter.MainPresenter;
import com.app.footprint.module.task.ui.TaskFragment;
import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.views.TabViewPager.ZXTabViewPager;

import java.io.File;

import butterknife.BindView;


/**
 * Create By Xiangb On 2017/7/11
 * 功能：主界面
 */
public class MainActivity extends BaseActivity<MainPresenter, MainModel> implements MainContract.View {

    @BindView(R.id.tvp_main)
    ZXTabViewPager tvpMain;

    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
//        mPresenter.getVersionInfo(ApiParamUtil.getVersionUpdateDataInfo(ZXSystemUtil.getVersionName()));
        tvpMain.setManager(getSupportFragmentManager())
                .setTabLayoutGravity(ZXTabViewPager.TabGravity.GRAVITY_BOTTOM)
                .addTab(FootFragment.newInstance(), "足迹", R.drawable.foot_tab)
                .addTab(TaskFragment.newInstance(), "任务", R.mipmap.ic_launcher)
                .addTab(MyFragment.newInstance(), "我的", R.drawable.mine_tab)
                .setTitleColor(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.red))
                .setIndicatorColor(ContextCompat.getColor(this, R.color.wheat))
                .setIndicatorHeight(3)
                .setSelectOn(1)
                .setViewpagerCanScroll(false)
                .build();

        tvpMain.getTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(com.zx.zxutils.R.id.iv_item_tab).setSelected(true);
//                tab.getCustomView().findViewById(com.zx.zxutils.R.id.tv_item_tab).setSelected(true);
//                if (ZXTabViewPager.this.selectTextColor != 0) {
//                    ((TextView)tab.getCustomView().findViewById(com.zx.zxutils.R.id.tv_item_tab)).setTextColor(ZXTabViewPager.this.selectTextColor);
//                }
                String selectText = tab.getText().toString();
                if(selectText.equals("足迹"))
                {
                    tab.setIcon(R.mipmap.foot_tab_select);
                }else if(selectText.equals("我的"))
                {
                    tab.setIcon(R.mipmap.mine_tab_select);
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(com.zx.zxutils.R.id.iv_item_tab).setSelected(false);
//                tab.getCustomView().findViewById(com.zx.zxutils.R.id.tv_item_tab).setSelected(false);
//                if (ZXTabViewPager.this.normalTextColor != 0) {
//                    ((TextView)tab.getCustomView().findViewById(com.zx.zxutils.R.id.tv_item_tab)).setTextColor(ZXTabViewPager.this.normalTextColor);
//                }
                String selectText = tab.getText().toString();
                if(selectText.equals("足迹"))
                {
                    tab.setIcon(R.mipmap.foot_tab);
                }else if(selectText.equals("我的"))
                {
                    tab.setIcon(R.mipmap.mine_tab);
                }
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onVersionResult(VersionEntity versionResult) {
        if ("1".equals(versionResult.getIslatest() + "")) {
            mSharedPrefUtil.putString("isLast", versionResult.getIslatest() + "");
            boolean isForce = "1".equals(versionResult.getIsforced()) ? true : false;
            String message = isForce ? "强制升级:\n" + versionResult.getContent() : versionResult.getContent();
            if (isForce) {
                ZXDialogUtil.showInfoDialog(mContext, "升级提示", message, (dialogInterface, i) -> {
                    mPresenter.downLoadApk(versionResult);
                });
            } else {
                ZXDialogUtil.showYesNoDialog(mContext, "升级提示", message, (dialogInterface, i) -> {
                    mPresenter.downLoadApk(versionResult);
                });
            }
        } else {
            mSharedPrefUtil.putString("version", "");
            mSharedPrefUtil.putString("content", "");
            mSharedPrefUtil.putString("url", "");
        }
    }

    @Override
    public void onApkDownloadResult(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private long triggerAtTimefirst = 0;

    @Override
    public void onBackPressed() {

        long triggerAtTimeSecond = triggerAtTimefirst;
        triggerAtTimefirst = SystemClock.elapsedRealtime();
        if (triggerAtTimefirst - triggerAtTimeSecond <= 2000) {
            MyApplication.getInstance().finishAll();
        } else {
            showToast("请再点击一次, 确认退出...");
        }
    }
}
