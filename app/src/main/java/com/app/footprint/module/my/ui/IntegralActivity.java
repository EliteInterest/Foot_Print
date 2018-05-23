package com.app.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.my.bean.IntegralEntity;
import com.app.footprint.module.my.mvp.contract.IntegralContract;
import com.app.footprint.module.my.mvp.model.IntegralModel;
import com.app.footprint.module.my.mvp.presenter.IntegralPresenter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class IntegralActivity extends BaseActivity<IntegralPresenter, IntegralModel> implements IntegralContract.View {

    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, IntegralActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        showLoading("正在获取积分");
        String userId = mSharedPrefUtil.getString("userId");
        mPresenter.doRequestintegralInfo(ApiParamUtil.getUserDataInfo(userId));
    }

    @OnClick(R.id.mine_integral_back)
    public void onViewClicked(android.view.View view) {
        switch (view.getId())
        {
            case R.id.mine_integral_back:
                finish();
                break;

                default:
                    break;
        }
    }

    @Override
    public void onRequestIntergralInfoResult(IntegralEntity integralEntity) {
        dismissLoading();
        showLoading("获取成功");
    }
}
