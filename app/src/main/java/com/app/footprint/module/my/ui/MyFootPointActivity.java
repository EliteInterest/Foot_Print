package com.app.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.my.mvp.contract.MyFootPointContract;
import com.app.footprint.module.my.mvp.model.MyFootPointModel;
import com.app.footprint.module.my.mvp.presenter.MyFootPointPresenter;

import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyFootPointActivity extends BaseActivity<MyFootPointPresenter, MyFootPointModel> implements MyFootPointContract.View {
    private int tab = 0;

    public static void startAction(Activity activity, boolean isFinish, int tab) {
        Intent intent = new Intent(activity, MyFootPointActivity.class);
        intent.putExtra("tab", tab);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_foot_point;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
         tab = getIntent().getIntExtra("tab",0);
    }

    @OnClick(R.id.mine_foot_back)
    public void onViewClicked(android.view.View view) {
        switch (view.getId())
        {
            case R.id.mine_foot_back:
                finish();
                break;
        }
    }

}
