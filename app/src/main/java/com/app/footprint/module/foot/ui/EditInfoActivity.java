package com.app.footprint.module.foot.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.foot.mvp.contract.EditInfoContract;
import com.app.footprint.module.foot.mvp.model.EditInfoModel;
import com.app.footprint.module.foot.mvp.presenter.EditInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class EditInfoActivity extends BaseActivity<EditInfoPresenter, EditInfoModel> implements EditInfoContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_location_address)
    TextView tvLocationAddress;

    public static void startAction(Activity activity, boolean isFinish, Bundle bundle) {
        Intent intent = new Intent(activity, EditInfoActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_save, R.id.btn_location_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_title_save:

                break;
            case R.id.btn_location_change:

                break;
        }
    }
}
