package com.app.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.app.footprint.R;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.my.mvp.contract.ContactContract;
import com.app.footprint.module.my.mvp.model.ContactModel;
import com.app.footprint.module.my.mvp.presenter.ContactPresenter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class ContactActivity extends BaseActivity<ContactPresenter, ContactModel> implements ContactContract.View {

    @BindView(R.id.btn_submission)
    Button mCommitButton;


    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, ContactActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick(R.id.contact_back)
    public void onViewClicked(android.view.View view) {
        switch (view.getId())
        {
            case R.id.contact_back:
                finish();
                break;

                default:
                    break;
        }
    }

}
