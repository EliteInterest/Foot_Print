package com.app.footprint.module.foot.ui;

import android.os.Bundle;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.foot.mvp.contract.FootContract;
import com.app.footprint.module.foot.mvp.model.FootModel;
import com.app.footprint.module.foot.mvp.presenter.FootPresenter;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class FootFragment extends BaseFragment<FootPresenter, FootModel> implements FootContract.View {


    public static FootFragment newInstance() {
        FootFragment fragment = new FootFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_foot;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public void onViewClicked(View view) {

    }

}
