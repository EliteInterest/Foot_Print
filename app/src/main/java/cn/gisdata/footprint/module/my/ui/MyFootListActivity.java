package cn.gisdata.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.base.BaseActivity;
import cn.gisdata.footprint.module.my.func.adapter.MyPagerAdapter;
import cn.gisdata.footprint.module.my.mvp.contract.MyFootListContract;
import cn.gisdata.footprint.module.my.mvp.model.MyFootListModel;
import cn.gisdata.footprint.module.my.mvp.presenter.MyFootListPresenter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyFootListActivity extends BaseActivity<MyFootListPresenter, MyFootListModel> implements MyFootListContract.View {

    @BindView(R.id.tl_foot_list)
    TabLayout tlFootList;
    @BindView(R.id.vp_foot_list)
    ViewPager vpFootList;

    private MyPagerAdapter pagerAdapter;

    public static void startAction(Activity activity, boolean isFinish, int currentItem) {
        Intent intent = new Intent(activity, MyFootListActivity.class);
        intent.putExtra("currentItem", currentItem);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_foot_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(RouteListFragment.newInstance(), "路线");
        pagerAdapter.addFragment(FootListFragment.newInstance(), "脚印");
        vpFootList.setOffscreenPageLimit(2);
        vpFootList.setAdapter(pagerAdapter);
        tlFootList.setupWithViewPager(vpFootList);
        vpFootList.setCurrentItem(getIntent().getIntExtra("currentItem", 0));
        tlFootList.setSelectedTabIndicatorHeight(5);
        tlFootList.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = tlFootList.getTabAt(i);
            tab.setCustomView(R.layout.item_foot_tab);
            if (i == 0) {
                tab.getCustomView().findViewById(R.id.iv_item_tab).setBackgroundResource(R.drawable.tab_item_route_selector);
            } else if (i == 1) {
                tab.getCustomView().findViewById(R.id.iv_item_tab).setBackgroundResource(R.drawable.tab_item_foot_selector);
            }
            ((TextView) tab.getCustomView().findViewById(R.id.tv_item_tab)).setText(pagerAdapter.getPageTitle(i));
        }
        tlFootList.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tv_item_tab).setSelected(true);
                tab.getCustomView().findViewById(R.id.iv_item_tab).setSelected(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tv_item_tab).setSelected(false);
                tab.getCustomView().findViewById(R.id.iv_item_tab).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick({R.id.mine_foot_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_foot_back:
                finish();
                break;

            default:
                break;
        }
    }
}
