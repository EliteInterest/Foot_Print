package com.app.footprint.module.foot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    RelativeLayout rlTitle;
    @BindView(R.id.cv_foot_preview)
    CardView cvPreview;
    @BindView(R.id.fm_map_preview)
    FrameLayout fmPreview;
    @BindView(R.id.tv_title_save)
    TextView tvAction;
    @BindView(R.id.ll_route_edit)
    LinearLayout llRouteEdite;
    @BindView(R.id.et_route_name)
    EditText etRouteName;
    @BindView(R.id.et_route_detail)
    EditText etRouteDetail;
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

        mRxManager.on("commitRoute", (Action1<Boolean>) aBoolean -> {
            if (aBoolean) {
                rlTitle.setVisibility(View.VISIBLE);
                llRouteEdite.setVisibility(View.VISIBLE);
                tvAction.setText("保存");
                tvTitle.setText("路线编辑");
            } else {
                rlTitle.setVisibility(View.GONE);
                llRouteEdite.setVisibility(View.GONE);
                tvAction.setText("分享");
            }

        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                if ("分享".equals(tvAction.getText().toString())) {
                    openFootPreview(null);
                } else if ("保存".equals(tvAction.getText().toString())) {
                    mRxManager.post("commitRoute", false);
                }
                break;
            case R.id.tv_title_save://分享
                if ("分享".equals(tvAction.getText().toString())) {

                } else if ("保存".equals(tvAction.getText().toString())) {
                    if (etRouteName.getText().toString().length() == 0) {
                        showToast("路线名称不能为空");
                        break;
                    }
                    String routeName = etRouteName.getText().toString();
                    String routeDetail = etRouteDetail.getText().toString();
                    List<FootFileBean> footFileBeans = mSharedPrefUtil.getList(ConstStrings.FootFiles);
                    mPresenter.commitRoute(routeName, routeDetail, footFileBeans);
                }
                break;
        }
    }

    /**
     * 开启预览
     * @param footFileBean
     */
    private void openFootPreview(FootFileBean footFileBean){
        if (footFileBean != null) {
            tvAction.setText("分享");
            cvPreview.setVisibility(View.VISIBLE);
            fmPreview.setVisibility(View.VISIBLE);
            if (webViewFragment == null) {
                webViewFragment = WebViewFragment.newInstance(footFileBean.getUrl());
                ZXFragmentUtil.addFragment(getChildFragmentManager(), webViewFragment, R.id.fm_map_preview);
            } else {
                webViewFragment.reload(footFileBean.getUrl());
            }
            rlTitle.setVisibility(View.VISIBLE);
            if (footFileBean.isRoute()) {
                tvTitle.setText("路径预览");
            } else {
                tvTitle.setText("足印预览");
            }
            mapFragment.showFootView(false);
        } else {
            cvPreview.setVisibility(View.GONE);
            fmPreview.setVisibility(View.GONE);
            rlTitle.setVisibility(View.GONE);
            llRouteEdite.setVisibility(View.GONE);
            mapFragment.showFootView(true);
        }
    }

    @Override
    public void onRouteCommitResult(String url) {
        FootFileBean footFileBean = new FootFileBean();
        footFileBean.setUrl(url);
        footFileBean.setRoute(true);
        llRouteEdite.setVisibility(View.GONE);
        mapFragment.clearSharedPref();
        openFootPreview(footFileBean);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mapFragment.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 0x01) {
            mapFragment.refreshPoints();
        } else if (resultCode == 0x02) {
            FootFileBean itemBean = (FootFileBean) data.getSerializableExtra("itemBean");
            openFootPreview(itemBean);
        }
    }
}
