package cn.gisdata.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gisdata.footprint.R;
import cn.gisdata.footprint.api.ApiParamUtil;
import cn.gisdata.footprint.base.BaseActivity;
import cn.gisdata.footprint.module.my.bean.IntegralEntity;
import cn.gisdata.footprint.module.my.mvp.contract.IntegralContract;
import cn.gisdata.footprint.module.my.mvp.model.IntegralModel;
import cn.gisdata.footprint.module.my.mvp.presenter.IntegralPresenter;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class IntegralActivity extends BaseActivity<IntegralPresenter, IntegralModel> implements IntegralContract.View {
    private RecyclerView.Adapter recyclerAdapter;
    private List<IntegralEntity.DetailsBean> adapterList = new ArrayList();

    @BindView(R.id.mine_integral_detail_content)
    RecyclerView recyclerView;

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

    @BindView(R.id.TVRoute)
    TextView tvRoute;

    @BindView(R.id.TVFootprint)
    TextView tvFootprint;

    @BindView(R.id.TVVisit)
    TextView tvVisit;

    @BindView(R.id.TVLogin)
    TextView tvLogin;

    private MyAdapter myAdapter;

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

        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

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
        tvRoute.setText("路线 " + String.valueOf(count));

        int foot = integralEntity.getFootmark().getCount();
        mFootmarkCount.setText("+" + (foot * 10));
        tvFootprint.setText("脚印 " + String.valueOf(foot * 10));

        int visit = integralEntity.getVisit().getCount();
        mVisitVolume.setText("+" + visit);
        tvVisit.setText("访问 " + String.valueOf(visit));

        int login = integralEntity.getLogin().getCount();
        mIntegral.setText("+" + login);
        tvLogin.setText("登录 " + String.valueOf(login));

        mineIntegalCount.setText(String.valueOf(login + count + foot * 10 + visit));//总积分

        adapterList.clear();
        adapterList.addAll(integralEntity.getDetailsInfo());
        myAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_integral, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            IntegralEntity.DetailsBean info = adapterList.get(position);

            String time = info.getRecodeTime() == null ? "" : info.getRecodeTime();
            int count = info.getIntegral();

            holder.loginTime.setText(time);
            holder.loginCount.setText("+" + count);
        }

        @Override
        public int getItemCount() {
            return adapterList.size();
        }

    };

    class MyHolder extends RecyclerView.ViewHolder{


        public TextView loginTime;
        public TextView loginCount;

        public MyHolder(View itemView) {
            super(itemView);
            loginTime = itemView.findViewById(R.id.item_integral_time);
            loginCount = itemView.findViewById(R.id.item_integral_count);
        }
    }
}
