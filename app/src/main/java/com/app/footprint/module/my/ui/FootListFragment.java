package com.app.footprint.module.my.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.my.bean.MyfootMarkEntity;
import com.app.footprint.module.my.func.adapter.FootListAdapter;
import com.app.footprint.module.my.mvp.contract.FootListContract;
import com.app.footprint.module.my.mvp.model.FootListModel;
import com.app.footprint.module.my.mvp.presenter.FootListPresenter;
import com.zx.zxutils.views.RecylerMenu.ZXRecyclerDeleteHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class FootListFragment extends BaseFragment<FootListPresenter, FootListModel> implements FootListContract.View {


    @BindView(R.id.rv_foot_list)
    RecyclerView rvFootList;
    @BindView(R.id.srl_foot_refresh)
    SwipeRefreshLayout srlFootRefresh;

    private List<MyfootMarkEntity> footList = new ArrayList();
    private int deletePosition = 0;
    private FootListAdapter footListAdapter;

    public static FootListFragment newInstance() {
        FootListFragment fragment = new FootListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_foot_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        footListAdapter = new FootListAdapter(footList);
        rvFootList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFootList.setAdapter(footListAdapter);
        srlFootRefresh.setOnRefreshListener(() -> loadData());

        ZXRecyclerDeleteHelper deleteHelper = new ZXRecyclerDeleteHelper(getActivity(), rvFootList)
                .setClickable(i -> PreviewActivity.startAction(getActivity(), false, footList.get(i).getName(), footList.get(i).getDetailsUrlPath()))
                .setSwipeOptionViews(R.id.tv_delete)
                .setSwipeable(R.id.ll_item_content, R.id.ll_list_menu, new ZXRecyclerDeleteHelper.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        deletePosition = position;
                        mPresenter.deleteList(ApiParamUtil.getDeleteMap(footList.get(position).getId()));
                    }
                });
        loadData();
    }

    public void onViewClicked(View view) {
    }

    private void loadData() {
        String userId = mSharedPrefUtil.getString("userId");
        mPresenter.doRequestMyFootMarkList(ApiParamUtil.getUserDataInfo(userId));
    }

    @Override
    public void onRequestMyFootMarkListResult(List<MyfootMarkEntity> list) {
        dismissLoading();
        footList.clear();
        footList.addAll(list);
        srlFootRefresh.setRefreshing(false);
        footListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteResult() {
        footList.remove(deletePosition);
        footListAdapter.notifyItemRemoved(deletePosition);
        footListAdapter.notifyItemRangeChanged(deletePosition, footList.size());
    }
}