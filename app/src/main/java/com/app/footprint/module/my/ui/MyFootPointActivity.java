package com.app.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.my.bean.IntegralEntity;
import com.app.footprint.module.my.bean.MyfootMarkEntity;
import com.app.footprint.module.my.bean.UserInfoEntity;
import com.app.footprint.module.my.mvp.contract.MyFootPointContract;
import com.app.footprint.module.my.mvp.model.MyFootPointModel;
import com.app.footprint.module.my.mvp.presenter.MyFootPointPresenter;
import com.app.footprint.util.DateUtil;
import com.zx.zxutils.other.ZXRecyclerAdapter.RvHolder;
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
public class MyFootPointActivity extends BaseActivity<MyFootPointPresenter, MyFootPointModel> implements MyFootPointContract.View {
    private int tab = 0;
    private String userId;
    private ZXRecycleSimpleAdapter recyclerAdapter;
    private List<MyfootMarkEntity> adapterList = new ArrayList();

    @BindView(R.id.mine_foot_point)
    TextView myFootRoute;

    @BindView(R.id.mine_foot_print)
    TextView myFootMark;

    @BindView(R.id.mine_foot_detail_content)
    ZXSwipeRecyler swipeRecyler;

    public static void startAction(Activity activity, boolean isFinish, int tab) {
        Intent intent = new Intent(activity, MyFootPointActivity.class);
        intent.putExtra("tab", tab);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_foot_point;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tab = getIntent().getIntExtra("tab", 0);
        userId = mSharedPrefUtil.getString("userId");
        myFootMark.setTextColor(getResources().getColor(R.color.dp_blue));
        recyclerAdapter = new ZXRecycleSimpleAdapter() {
            @Override
            public RecyclerView.ViewHolder onItemHolder(ViewGroup viewGroup, int i) {
                View view;
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_foot_mark, viewGroup, false);
                RvHolder holder = new RvHolder(view);
                return holder;
            }

            @Override
            public void onBindHolder(RecyclerView.ViewHolder viewHolder, int i) {
                if(RvHolder.class.isInstance(viewHolder))
                {
                    RvHolder holder = (RvHolder) viewHolder;
                    ZxRvHolder zxRvHolder = holder.getViewHolder();
                    List<MyfootMarkEntity> dataList = (List<MyfootMarkEntity>) recyclerAdapter.getDataList();
                    MyfootMarkEntity info = dataList.get(i);
                    TextView myName = zxRvHolder.getTextView(R.id.my_foot_name);
                    TextView myTime = zxRvHolder.getTextView(R.id.my_foot_time);
                    TextView myVisitCount = zxRvHolder.getTextView(R.id.my_foot_visitcount);

                    String time = info.getName() == null ? "" : info.getName();
                    myName.setText(time);
                    String startTime = DateUtil.getDateTimeFromMillis(Long.valueOf(info.getStartTime()));
                    myTime.setText(startTime);
                    int visitcount = info.getVisitVolume();
                    myVisitCount.setText(visitcount+"次访问");
                }
            }

            @Override
            public List<?> onItemList() {
                return adapterList;
            }
        };

        swipeRecyler.setSimpleAdapter(recyclerAdapter)
                .showLoadInfo(true)
                .setSRListener(new ZXSRListener<MyfootMarkEntity>() {
                    @Override
                    public void onItemClick(MyfootMarkEntity o, int i) {
                        ZXToastUtil.showToast("点击:" + o.toString());
                    }

                    @Override
                    public void onItemLongClick(MyfootMarkEntity o, int i) {
                        ZXToastUtil.showToast("长按:" + o.toString());
                    }

                    @Override
                    public void onRefresh() {
                        showLoading("加载中...");
                        String userId = mSharedPrefUtil.getString("userId");
                        mPresenter.doRequestMyFootMarkList(ApiParamUtil.getUserDataInfo(userId));
                    }

                    @Override
                    public void onLoadMore() {
                        showLoading("加载中...");
                        String userId = mSharedPrefUtil.getString("userId");
                        mPresenter.doRequestMyFootMarkList(ApiParamUtil.getUserDataInfo(userId));
                    }
                });

        showLoading("加载中...");
        mPresenter.doRequestMyFootMarkList(ApiParamUtil.getUserDataInfo(userId));
    }

    @OnClick({R.id.mine_foot_back, R.id.mine_foot_point, R.id.mine_foot_print})
    public void onViewClicked(android.view.View view) {
        switch (view.getId()) {
            case R.id.mine_foot_back:
                finish();
                break;

            case R.id.mine_foot_point:
                myFootRoute.setTextColor(getResources().getColor(R.color.dp_blue));
                myFootMark.setTextColor(getResources().getColor(R.color.black));
                break;

            case R.id.mine_foot_print:
                myFootMark.setTextColor(getResources().getColor(R.color.dp_blue));
                myFootRoute.setTextColor(getResources().getColor(R.color.black));
                showLoading("加载中...");
                mPresenter.doRequestMyFootMarkList(ApiParamUtil.getUserDataInfo(userId));
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestMyFootMarkListResult(List<MyfootMarkEntity> list) {
        dismissLoading();
        adapterList.removeAll(adapterList);
        adapterList.addAll(list);
        swipeRecyler.stopRefresh();
        swipeRecyler.notifyDataSetChanged();
        swipeRecyler.setLoadInfo(50);//total size
    }
}
