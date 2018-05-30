package com.app.footprint.module.my.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.base.BaseFragment;
import com.app.footprint.module.my.bean.MyFootRouteEntity;
import com.app.footprint.module.my.func.adapter.RouteListAdapter;
import com.app.footprint.module.my.mvp.contract.RouteListContract;
import com.app.footprint.module.my.mvp.model.RouteListModel;
import com.app.footprint.module.my.mvp.presenter.RouteListPresenter;
import com.zx.zxutils.util.ZXToastUtil;
import com.zx.zxutils.views.RecylerMenu.ZXRecyclerDeleteHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class RouteListFragment extends BaseFragment<RouteListPresenter, RouteListModel> implements RouteListContract.View {


    @BindView(R.id.rv_route_list)
    RecyclerView rvRouteList;
    @BindView(R.id.srl_route_refresh)
    SwipeRefreshLayout srlRouteRefresh;

    private List<MyFootRouteEntity> routeList = new ArrayList();
    private RouteListAdapter routeListAdapter;

    public static RouteListFragment newInstance() {
        RouteListFragment fragment = new RouteListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_route_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        routeListAdapter = new RouteListAdapter(routeList);
        rvRouteList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRouteList.setAdapter(routeListAdapter);
        srlRouteRefresh.setOnRefreshListener(() -> loadData());

        ZXRecyclerDeleteHelper deleteHelper = new ZXRecyclerDeleteHelper(getActivity(), rvRouteList)
                .setClickable(i -> PreviewActivity.startAction(getActivity(), false, routeList.get(i).getName(), routeList.get(i).getDetailsUrlPath()))
                .setSwipeOptionViews(R.id.tv_delete)
                .setSwipeable(R.id.ll_item_content, R.id.ll_list_menu, new ZXRecyclerDeleteHelper.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        ZXToastUtil.showToast("删除第" + position + "个");
                    }
                });
        loadData();

    }

    public void onViewClicked(View view) {

    }

    private void loadData() {
        String userId = mSharedPrefUtil.getString("userId");
        mPresenter.doRequestMyFootRouteList(ApiParamUtil.getUserDataInfo(userId));
    }

    @Override
    public void onRequestMyFootRouteListResult(List<MyFootRouteEntity> list) {
        routeList.clear();
        routeList.addAll(list);
        srlRouteRefresh.setRefreshing(false);
        routeListAdapter.notifyDataSetChanged();
    }

}
