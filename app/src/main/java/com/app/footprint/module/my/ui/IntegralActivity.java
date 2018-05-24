package com.app.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.my.bean.IntegralEntity;
import com.app.footprint.module.my.mvp.contract.IntegralContract;
import com.app.footprint.module.my.mvp.model.IntegralModel;
import com.app.footprint.module.my.mvp.presenter.IntegralPresenter;
import com.zx.zxutils.other.ZXRecyclerAdapter.RvHolder;
import com.zx.zxutils.other.ZXRecyclerAdapter.ZXRecycleAdapter;
import com.zx.zxutils.other.ZXRecyclerAdapter.ZXRecycleSimpleAdapter;
import com.zx.zxutils.other.ZXRecyclerAdapter.ZxRvHolder;
import com.zx.zxutils.util.ZXToastUtil;
import com.zx.zxutils.views.SwipeRecylerView.ZXSRListener;
import com.zx.zxutils.views.SwipeRecylerView.ZXSwipeRecyler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class IntegralActivity extends BaseActivity<IntegralPresenter, IntegralModel> implements IntegralContract.View {
    private ZXRecycleSimpleAdapter recyclerAdapter;
    private List<IntegralEntity.DetailsBean> adapterList = new ArrayList();

    @BindView(R.id.mine_integral_detail_content)
    ZXSwipeRecyler swipeRecyler;

    @BindView(R.id.mine_integral_content)
    TextView mineIntegalCount;

    @BindView(R.id.RouteCount)
    TextView mRouteCount;

    @BindView(R.id.FootmarkCount)
    TextView mFootmarkCount;

    @BindView(R.id.IntegralCount)
    TextView mIntegral;

    @BindView(R.id.VisitVolumel)
    TextView mVisitVolume;


    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, IntegralActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        recyclerAdapter = new ZXRecycleSimpleAdapter() {
            @Override
            public RecyclerView.ViewHolder onItemHolder(ViewGroup viewGroup, int i) {
                View view;
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_integral, viewGroup, false);
                RvHolder holder = new RvHolder(view);
                return holder;
            }

            @Override
            public void onBindHolder(RecyclerView.ViewHolder viewHolder, int i) {
                if(RvHolder.class.isInstance(viewHolder))
                {
                    RvHolder holder = (RvHolder) viewHolder;
                    ZxRvHolder zxRvHolder = holder.getViewHolder();
                    List<IntegralEntity.DetailsBean> dataList = (List<IntegralEntity.DetailsBean>) recyclerAdapter.getDataList();
                    IntegralEntity.DetailsBean info = dataList.get(i);
                    TextView loginTime = zxRvHolder.getTextView(R.id.item_integral_time);
                    TextView loginCount = zxRvHolder.getTextView(R.id.item_integral_count);

                    String time = info.getRecodeTime() == null ? "" : info.getRecodeTime();
                    int count = info.getIntegral();

                    loginTime.setText(time);
                    loginCount.setText("+" + count);
                }
            }

            @Override
            public List<?> onItemList() {
                return adapterList;
            }

//            @Override
//            public int onCreateViewLayoutID(int i) {
//                return R.layout.item_integral;
//            }


//            @Override
//            public void onBindHolder(ZxRvHolder zxRvHolder, Object o, int i) {
//                IntegralEntity.DetailsBean info = (IntegralEntity.DetailsBean) o;
//                TextView loginTime = zxRvHolder.getTextView(R.id.item_integral_time);
//                TextView loginCount = zxRvHolder.getTextView(R.id.item_integral_count);
//
//                String time = info.getRecodeTime() == null ? "" : info.getRecodeTime();
//                int count = info.getIntegral();
//
//                loginTime.setText(time);
//                loginCount.setText("+" + count);
//            }
        };

        swipeRecyler.setSimpleAdapter(recyclerAdapter)
                .showLoadInfo(true)
                .setSRListener(new ZXSRListener<IntegralEntity.DetailsBean>() {
                    @Override
                    public void onItemClick(IntegralEntity.DetailsBean o, int i) {
                        ZXToastUtil.showToast("点击:" + o.toString());
                    }

                    @Override
                    public void onItemLongClick(IntegralEntity.DetailsBean o, int i) {
                        ZXToastUtil.showToast("长按:" + o.toString());
                    }

                    @Override
                    public void onRefresh() {
                        showLoading("正在获取积分");
                        String userId = mSharedPrefUtil.getString("userId");
                        mPresenter.doRequestintegralInfo(ApiParamUtil.getUserDataInfo(userId));
                    }

                    @Override
                    public void onLoadMore() {
                        showLoading("正在获取积分");
                        String userId = mSharedPrefUtil.getString("userId");
                        mPresenter.doRequestintegralInfo(ApiParamUtil.getUserDataInfo(userId));
                    }
                });


        showLoading("正在获取积分");
        String userId = mSharedPrefUtil.getString("userId");
        mPresenter.doRequestintegralInfo(ApiParamUtil.getUserDataInfo(userId));
    }

    @OnClick(R.id.mine_integral_back)
    public void onViewClicked(android.view.View view) {
        switch (view.getId()) {
            case R.id.mine_integral_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestIntergralInfoResult(IntegralEntity integralEntity) {
        dismissLoading();
        showLoading("获取成功");

        int count = integralEntity.getRoute().getCount();
        mRouteCount.setText("+" + count);

        int foot = integralEntity.getFootmark().getCount();
        mFootmarkCount.setText("+" + foot);

        int visit = integralEntity.getVisit().getCount();
        mVisitVolume.setText("+" + visit);

        int login = integralEntity.getLogin().getCount();
        mIntegral.setText("+" + login);
        mineIntegalCount.setText(String.valueOf(login));

        adapterList.addAll(integralEntity.getDetailsInfo());
        swipeRecyler.stopRefresh();
        swipeRecyler.notifyDataSetChanged();
        ;
        swipeRecyler.setLoadInfo(50);//total size
    }
}
