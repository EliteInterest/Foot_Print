package cn.gisdata.footprint.module.system.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.app.MyApplication;
import cn.gisdata.footprint.base.BaseActivity;
import cn.gisdata.footprint.module.foot.ui.FootFragment;
import cn.gisdata.footprint.module.my.ui.MyFragment;
import cn.gisdata.footprint.module.system.bean.VersionEntity;
import cn.gisdata.footprint.module.system.mvp.contract.MainContract;
import cn.gisdata.footprint.module.system.mvp.model.MainModel;
import cn.gisdata.footprint.module.system.mvp.presenter.MainPresenter;
import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.util.ZXSystemUtil;
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

    private FootFragment footFragment;

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
        footFragment = FootFragment.newInstance();
        tvpMain.setManager(getSupportFragmentManager())
                .setTabLayoutGravity(ZXTabViewPager.TabGravity.GRAVITY_BOTTOM)
                .addTab(footFragment, "足迹", R.drawable.foot_tab)
//                .addTab(TaskFragment.newInstance(), "任务", R.mipmap.ic_launcher)
                .addTab(MyFragment.newInstance(), "个人中心", R.drawable.mine_tab)
                .setTitleColor(ContextCompat.getColor(this, R.color.gray_8f), ContextCompat.getColor(this, R.color.colorPrimary))
                .setIndicatorColor(ContextCompat.getColor(this, R.color.white))
                .setIndicatorHeight(3)
                .setSelectOn(1)
                .setViewpagerCanScroll(false)
                .build();
        tvpMain.getTabLayout().setMinimumHeight(ZXSystemUtil.dp2px(50));
        tvpMain.getTabLayout().setBackgroundColor(ContextCompat.getColor(this, R.color.whitesmoke));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        footFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        long triggerAtTimeSecond = triggerAtTimefirst;
        triggerAtTimefirst = SystemClock.elapsedRealtime();
        if (triggerAtTimefirst - triggerAtTimeSecond <= 2000) {
            mRxManager.post("destory", true);
            MyApplication.getInstance().finishAll();
        } else {
            showToast("请再点击一次, 确认退出...");
        }
    }
}
