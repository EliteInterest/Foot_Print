package cn.gisdata.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.api.ApiParamUtil;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.base.BaseActivity;
import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import cn.gisdata.footprint.module.my.mvp.contract.PersonalInfoEditContract;
import cn.gisdata.footprint.module.my.mvp.model.PersonalInfoEditModel;
import cn.gisdata.footprint.module.my.mvp.presenter.PersonalInfoEditPresenter;
import cn.gisdata.footprint.module.system.bean.LoginEntity;

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
    private String content = "";

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
        Intent intent = activity.getIntent();
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
        if (tab == 3) {
            mPhoneEditLayout.setVisibility(View.VISIBLE);
        }

        mTitle.setText(title);

        if (!TextUtils.isEmpty(content)) {
            mEdit.setText(content);
            mEdit.setSelection(content.length());
        }
    }

    @OnClick({R.id.personal_edit_back, R.id.btn_user_edit_send_phone, R.id.btn_user_edit_commit_logout})
    public void onViewClicked(android.view.View view) {
        switch (view.getId()) {
            case R.id.personal_edit_back:
                finish();
                break;

            case R.id.btn_user_edit_send_phone:
                //输入验证码
                String phoneNumber = mEdit.getText().toString();
                if (TextUtils.isEmpty(phoneNumber) && !ConstStrings.isNumeric(phoneNumber)) {
                    showToast("请输入正确的电话号码！");
                    return;
                }

                if (phoneNumber.equals(mSharedPrefUtil.getString("phone", ""))) {
                    showToast("设置的电话号码与目前的一样，请输入其他电话号码！");
                    return;
                }

                mPresenter.doSendPhoneNum(ApiParamUtil.getPhoneDataInfo(phoneNumber));
                break;

            case R.id.btn_user_edit_commit_logout://提交按钮，用来更新昵称、用户名或手机号
                phoneNumber = mEdit.getText().toString();
                String checkNum = mEditCheckNum.getText().toString();
                if (TextUtils.isEmpty(phoneNumber) || ((mPhoneEditLayout.getVisibility() == View.VISIBLE) && TextUtils.isEmpty(checkNum))) {
                    showToast("请输入更新的数据！");
                    return;
                }
                if (tab == 1) {//更新昵称
                    mPresenter.doUploadName(ApiParamUtil.getUserNickNameIfo(mSharedPrefUtil.getString("userId"), phoneNumber));
                } else if (tab == 2) {//更新用户名
                    mPresenter.doUploadName(ApiParamUtil.getUserNameInfo(mSharedPrefUtil.getString("userId"), phoneNumber));
                } else {//更新手机号
                    mPresenter.doUploadPhone(ApiParamUtil.getUserPhoneInfo(mSharedPrefUtil.getString("userId"), phoneNumber, checkNum));
                }
                break;
        }
    }

    @Override
    public void onUploadResult(UserInfoEntity userInfoEntity) {
        showToast("更新成功！");
        if (tab == 1)
            mSharedPrefUtil.putString("nickName", mEdit.getText().toString());
        else if (tab == 2)
            mSharedPrefUtil.putString("userName", mEdit.getText().toString());
        else {
            Log.e("fxs", "update phone to: " + mEdit.getText().toString());
            mSharedPrefUtil.putString("phone", mEdit.getText().toString());
        }
        finish();
    }

    @Override
    public void onSendPhoneNUmResult(LoginEntity loginResult) {
        showToast("发送成功！");
    }
}
