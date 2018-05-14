package com.app.footprint.module.task.ui;

import android.os.Bundle;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.task.mvp.contract.TaskContract;
import com.app.footprint.module.task.mvp.model.TaskModel;
import com.app.footprint.module.task.mvp.presenter.TaskPresenter;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class TaskFragment extends BaseFragment<TaskPresenter, TaskModel> implements TaskContract.View {


    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public void onViewClicked(View view) {

    }

}
