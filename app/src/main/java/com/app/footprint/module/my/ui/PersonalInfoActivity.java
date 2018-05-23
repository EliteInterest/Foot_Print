package com.app.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.my.mvp.contract.PersonalInfoContract;
import com.app.footprint.module.my.mvp.model.PersonalInfoModel;
import com.app.footprint.module.my.mvp.presenter.PersonalInfoPresenter;
import com.app.footprint.module.system.ui.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class PersonalInfoActivity extends BaseActivity<PersonalInfoPresenter, PersonalInfoModel> implements PersonalInfoContract.View {

    @BindView(R.id.personal_back)
    TextView mPersonalBack;

    @BindView(R.id.person_nickName_content)
    TextView mNickName;

    @BindView(R.id.personal_username_textview)
    TextView mUserName;

    @BindView(R.id.person_phone_content)
    TextView mPhoneNum;

    @BindView(R.id.layout_nickName)
    RelativeLayout mNickNameLayout;

    @BindView(R.id.layout_account)
    RelativeLayout mUserNameLayout;

    @BindView(R.id.layout_phone)
    RelativeLayout mPhoneLayout;

    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, PersonalInfoActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String userName = mSharedPrefUtil.getString("userName");
        String nickName = mSharedPrefUtil.getString("nickName");
        String phone = mSharedPrefUtil.getString("phone");

        if (!TextUtils.isEmpty(userName)) {
            mUserName.setText(userName);
        }

        if (!TextUtils.isEmpty(nickName)) {
            mNickName.setText(nickName);
        }

        if (!TextUtils.isEmpty(phone)) {
            mPhoneNum.setText(phone);
        }
    }

    @OnClick({R.id.personal_back, R.id.layout_nickName, R.id.layout_account, R.id.layout_phone, R.id.btn_settings_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;

            case R.id.layout_nickName:
                String content = mSharedPrefUtil.getString("nickName");
                toEdit(1, "昵称", content == null ? "" : content);
                break;

            case R.id.layout_account:
                content = mSharedPrefUtil.getString("userName");
                toEdit(2, "账号", content == null ? "" : content);
                break;

            case R.id.layout_phone:
                content = mSharedPrefUtil.getString("phone");
                toEdit(3, "手机号", content == null ? "" : content);
                break;

            case R.id.btn_settings_logout:
                //lougout interface
                LoginActivity.startAction(this, true);
                break;

            default:
                break;
        }
    }

    private void toEdit(int tab, String title, String content) {
        Intent intent = new Intent(this, PersonalInfoEditActivity.class);
        intent.putExtra("tab", tab);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        startActivity(intent);
    }

}
