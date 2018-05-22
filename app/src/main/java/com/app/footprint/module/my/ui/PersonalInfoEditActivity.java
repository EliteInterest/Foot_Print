package com.app.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.my.mvp.contract.PersonalInfoEditContract;
import com.app.footprint.module.my.mvp.model.PersonalInfoEditModel;
import com.app.footprint.module.my.mvp.presenter.PersonalInfoEditPresenter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class PersonalInfoEditActivity extends BaseActivity<PersonalInfoEditPresenter, PersonalInfoEditModel> implements PersonalInfoEditContract.View {
    private static final String TAG = "PersonalInfoEditActivity";
    private int tab = 0;
    private String title = "";
    private String content ="";

    @BindView(R.id.layout_phone_edit)
    RelativeLayout mPhoneEditLayout;

    @BindView(R.id.personal_edit_back)
    TextView mBack;

    @BindView(R.id.personal_edit_title)
    TextView mTitle;

    @BindView(R.id.personal_edit)
    EditText mEdit;

    @BindView(R.id.user_edit_phone_checknum)
    EditText mEditCheckNum;

    @BindView(R.id.btn_user_edit_send_phone)
    Button mSendButton;

    @BindView(R.id.btn_user_edit_commit_logout)
    AppCompatButton mCommitButton;

    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, PersonalInfoEditActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        tab = getIntent().getIntExtra("tab", 1);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_info_edit;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (tab == 3)
        {
            mPhoneEditLayout.setVisibility(View.VISIBLE);
        }

        mTitle.setText(title);

        if(!TextUtils.isEmpty(content))
        {
            mEdit.setText(content);
        }
    }

    @OnClick({R.id.personal_edit_back, R.id.btn_user_edit_send_phone, R.id.btn_user_edit_commit_logout})
    public void onViewClicked(android.view.View view) {
        switch (view.getId()) {
            case R.id.personal_edit_back:
                finish();
                break;

            case R.id.btn_user_edit_send_phone:

                break;

            case R.id.btn_user_edit_commit_logout:
                break;
        }
    }

}
