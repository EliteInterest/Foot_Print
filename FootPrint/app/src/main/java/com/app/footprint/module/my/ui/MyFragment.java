package com.app.footprint.module.my.ui;

import android.os.Bundle;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.my.mvp.contract.MyContract;
import com.app.footprint.module.my.mvp.model.MyModel;
import com.app.footprint.module.my.mvp.presenter.MyPresenter;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyFragment extends BaseFragment<MyPresenter, MyModel> implements MyContract.View {


    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public void onViewClicked(View view) {

    }

}
