package com.app.footprint.module.foot.ui;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.foot.bean.FootFileBean;
import com.app.footprint.module.foot.mvp.contract.FootContract;
import com.app.footprint.module.foot.mvp.model.FootModel;
import com.app.footprint.module.foot.mvp.presenter.FootPresenter;
import com.app.footprint.module.map.ui.MapFragment;
import com.zx.zxutils.util.ZXFragmentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class FootFragment extends BaseFragment<FootPresenter, FootModel> implements FootContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_title_bar)
    RelativeLayout rvTitle;
    @BindView(R.id.cv_foot_preview)
    CardView cvPreview;
    @BindView(R.id.fm_map_preview)
    FrameLayout fmPreview;
    private MapFragment mapFragment;
    private WebViewFragment webViewFragment;

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
        mapFragment = MapFragment.newInstance();
        ZXFragmentUtil.addFragment(getFragmentManager(), mapFragment, R.id.fm_map);

        if (!mSharedPrefUtil.contains(ConstStrings.FootFiles) || mSharedPrefUtil.getList(ConstStrings.FootFiles) == null) {
            List<FootFileBean> footFiles = new ArrayList<>();
            mSharedPrefUtil.putList(ConstStrings.FootFiles, footFiles);
        }

        mRxManager.on("footPreview", (Action1<FootFileBean>) footFileBean -> {
            if (footFileBean != null) {
                cvPreview.setVisibility(View.VISIBLE);
                fmPreview.setVisibility(View.VISIBLE);
                webViewFragment = WebViewFragment.newInstance(footFileBean.getUrl());
                ZXFragmentUtil.addFragment(getFragmentManager(), webViewFragment, R.id.fm_map_preview);
                rvTitle.setVisibility(View.VISIBLE);
                if (footFileBean.isRoute()) {
                    tvTitle.setText("路径预览");
                } else {
                    tvTitle.setText("足印预览");
                }
                mapFragment.showFootView(false);
            } else if (webViewFragment != null) {
                ZXFragmentUtil.removeFragment(webViewFragment);
                cvPreview.setVisibility(View.GONE);
                fmPreview.setVisibility(View.GONE);
                rvTitle.setVisibility(View.GONE);
                mapFragment.showFootView(true);
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                mRxManager.post("footPreview", null);
                break;
            case R.id.iv_title_save://分享

                break;
        }
    }
}
