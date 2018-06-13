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
import cn.gisdata.footprint.module.foot.mvp.contract.EditInfoContract;
import cn.gisdata.footprint.module.foot.mvp.model.EditInfoModel;
import cn.gisdata.footprint.module.foot.mvp.presenter.EditInfoPresenter;
import cn.gisdata.footprint.module.my.bean.MyfootMarkEntity;
import cn.gisdata.footprint.module.my.func.adapter.FootListAdapter;
import cn.gisdata.footprint.module.my.mvp.contract.FootListContract;
import cn.gisdata.footprint.module.my.mvp.model.FootListModel;
import cn.gisdata.footprint.module.my.mvp.presenter.FootListPresenter;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class DraftFootListFragment extends BaseFragment<EditInfoPresenter, EditInfoModel> implements EditInfoContract.View {


    @BindView(R.id.rv_foot_list)
    RecyclerView rvFootList;
    @BindView(R.id.srl_foot_refresh)
    SwipeRefreshLayout srlFootRefresh;

    private List<MyfootMarkEntity> footList = new ArrayList();
    private int deletePosition = 0;
    private FootListAdapter footListAdapter;

    public static DraftFootListFragment newInstance() {
        DraftFootListFragment fragment = new DraftFootListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_foot_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //定义脚印列表，根据自己定义的类型填充
        footListAdapter = new FootListAdapter(footList,getActivity());
        rvFootList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFootList.setAdapter(footListAdapter);

        ZXRecyclerDeleteHelper deleteHelper = new ZXRecyclerDeleteHelper(getActivity(), rvFootList)
//                .setClickable(i -> PreviewActivity.startAction(getActivity(), false, footList.get(i).getName(), footList.get(i).getDetailsUrlPath()))
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
    public void onFileCommitResult(String result) {

    }
}
