package com.app.footprint.module.my.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.my.bean.UserInfoEntity;
import com.app.footprint.module.my.mvp.contract.MyContract;
import com.app.footprint.module.my.mvp.model.MyModel;
import com.app.footprint.module.my.mvp.presenter.MyPresenter;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyFragment extends BaseFragment<MyPresenter, MyModel> implements MyContract.View {
    private static final String TAG = "MyFragment";
    @BindView(R.id.layout_head)
    RelativeLayout mHeadLayout;

    @BindView(R.id.personal_nickname_textview)
    TextView mNickName;

    @BindView(R.id.personal_username_textview)
    TextView mUserName;

    @BindView(R.id.RouteCount)
    TextView mRouteCount;

    @BindView(R.id.FootmarkCount)
    TextView mFootmarkCount;

    @BindView(R.id.Integral)
    TextView mIntegral;

    @BindView(R.id.VisitVolume)
    TextView mVisitVolume;

    @BindView(R.id.layout_settings)
    RelativeLayout mSettingsLayout;

    @BindView(R.id.layout_contact)
    RelativeLayout mContactLayout;


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
        String userName = mSharedPrefUtil.getString("userName");
        String nickName = mSharedPrefUtil.getString("nickName");
        Log.i(TAG,"username is " +userName);
//        String phone = mSharedPrefUtil.getString("phone");
//        String headPortraits = mSharedPrefUtil.getString("headPortraits");
        if (!TextUtils.isEmpty(userName)) {
            mUserName.setText(userName);
        }

        if (!TextUtils.isEmpty(nickName)) {
            mNickName.setText(nickName);
        }

        doUserData();
    }

    private void doUserData() {
        String userId = mSharedPrefUtil.getString("userId");
        mPresenter.doRequestUserInfo(ApiParamUtil.getUserDataInfo(userId));
    }

    @OnClick({R.id.layout_head, R.id.layout_settings, R.id.layout_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                PersonalInfoActivity.startAction(getActivity(), false);
                break;

            case R.id.layout_settings:
                break;

            case R.id.layout_contact:
                break;


            default:
                break;
        }
    }

    @Override
    public void onRequestUserInfoResult(UserInfoEntity userInfoEntity) {
        int RouteCount = userInfoEntity.getRouteCount();
        int FootmarkCount = userInfoEntity.getFootmarkCount();
        int Integral = userInfoEntity.getIntegral();
        int VisitVolume = userInfoEntity.getVisitVolume();

        mRouteCount.setText(String.valueOf(RouteCount));
        mFootmarkCount.setText(String.valueOf(FootmarkCount));
        mIntegral.setText(String.valueOf(Integral));
        mVisitVolume.setText(String.valueOf(VisitVolume));
    }
}
