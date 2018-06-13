package cn.gisdata.footprint.module.my.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zx.zxutils.views.RecylerMenu.ZXRecyclerDeleteHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gisdata.footprint.R;
import cn.gisdata.footprint.api.ApiParamUtil;
import cn.gisdata.footprint.base.BaseFragment;
import cn.gisdata.footprint.module.foot.mvp.contract.FootContract;
import cn.gisdata.footprint.module.foot.mvp.model.FootModel;
import cn.gisdata.footprint.module.foot.mvp.presenter.FootPresenter;
import cn.gisdata.footprint.module.my.bean.MyFootRouteEntity;
import cn.gisdata.footprint.module.my.func.adapter.RouteListAdapter;
import cn.gisdata.footprint.module.my.mvp.contract.RouteListContract;
import cn.gisdata.footprint.module.my.mvp.model.RouteListModel;
import cn.gisdata.footprint.module.my.mvp.presenter.RouteListPresenter;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class DraftRouteListFragment extends BaseFragment<FootPresenter, FootModel> implements FootContract.View {


    @BindView(R.id.rv_route_list)
    RecyclerView rvRouteList;
    @BindView(R.id.srl_route_refresh)
    SwipeRefreshLayout srlRouteRefresh;

    private List<MyFootRouteEntity> routeList = new ArrayList();
    private int deletePosition = 0;
    private RouteListAdapter routeListAdapter;

    public static DraftRouteListFragment newInstance() {
        DraftRouteListFragment fragment = new DraftRouteListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_route_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        //获取路线列表，根据自己定义的类型填充
        routeListAdapter = new RouteListAdapter(routeList,getActivity());
        rvRouteList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRouteList.setAdapter(routeListAdapter);

        ZXRecyclerDeleteHelper deleteHelper = new ZXRecyclerDeleteHelper(getActivity(), rvRouteList)
//                .setClickable(i -> PreviewActivity.startAction(getActivity(), false, routeList.get(i).getName(), routeList.get(i).getDetailsUrlPath()))
                .setSwipeOptionViews(R.id.tv_delete)
                .setSwipeable(R.id.ll_item_content, R.id.ll_list_menu, new ZXRecyclerDeleteHelper.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        deletePosition = position;
                    }
                });
    }

    public void onViewClicked(View view) {

    }

    @Override
    public void onRouteCommitResult(String url) {

    }
}
