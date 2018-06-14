package cn.gisdata.footprint.module.my.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.views.RecylerMenu.ZXRecyclerDeleteHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gisdata.footprint.R;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.base.BaseFragment;
import cn.gisdata.footprint.module.foot.bean.DraftFootBean;
import cn.gisdata.footprint.module.foot.bean.FootRouteTextInfo;
import cn.gisdata.footprint.module.foot.func.tool.FootUtil;
import cn.gisdata.footprint.module.foot.mvp.contract.FootContract;
import cn.gisdata.footprint.module.foot.mvp.model.FootModel;
import cn.gisdata.footprint.module.foot.mvp.presenter.FootPresenter;
import cn.gisdata.footprint.module.my.func.adapter.DraftListAdapter;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class DraftRouteListFragment extends BaseFragment<FootPresenter, FootModel> implements FootContract.View {


    @BindView(R.id.rv_route_list)
    RecyclerView rvRouteList;
    @BindView(R.id.srl_route_refresh)
    SwipeRefreshLayout srlRouteRefresh;

    private List<DraftFootBean> draftFootBeans;
    private List<DraftFootBean> routeDraft = new ArrayList<>();

    private int uploadPosition = 0;
    private DraftListAdapter draftListAdapter;

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

        draftFootBeans = mSharedPrefUtil.getList(ConstStrings.DraftFootList);
        if (draftFootBeans != null && draftFootBeans.size() > 0) {
            for (DraftFootBean bean : draftFootBeans) {
                if (bean.getFootType() == 1) {
                    routeDraft.add(bean);
                }
            }
        }
        srlRouteRefresh.setEnabled(false);

        //获取路线列表，根据自己定义的类型填充
        draftListAdapter = new DraftListAdapter(routeDraft, getActivity());
        rvRouteList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRouteList.setAdapter(draftListAdapter);

        ZXRecyclerDeleteHelper deleteHelper = new ZXRecyclerDeleteHelper(getActivity(), rvRouteList)
//                .setClickable(i -> PreviewActivity.startAction(getActivity(), false, routeList.get(i).getName(), routeList.get(i).getDetailsUrlPath()))
                .setSwipeOptionViews(R.id.tv_delete)
                .setSwipeable(R.id.ll_item_content, R.id.ll_list_menu, (viewID, position) -> deleteDraft(position));

        draftListAdapter.setUplaodListener(new DraftListAdapter.OnUplaodListener() {
            @Override
            public void onUpload(int position) {
                ZXDialogUtil.showYesNoDialog(getActivity(), "提示", "是否重新上传?", (dialog, which) -> {
                    uploadPosition = position;
                    DraftFootBean bean = routeDraft.get(position);
                    Map<String, Object> map = new HashMap<>();
                    map.put("FootprintInfo", bean.getRouteInfoJson());
                    if (!TextUtils.isEmpty(bean.getTextInfoJson())) {
                        map.put("TextInfo", bean.getTextInfoJson());
                    }
                    map.put("PathInfo", bean.getPathInfoJson());
                    if (!TextUtils.isEmpty(bean.getSaveInfoJson())) {
                        map.put("SaveInfo", bean.getSaveInfoJson());
                    }
                    List<FootRouteTextInfo.FootRouteFileInfo> mediaFiles = new ArrayList<>();
                    if (bean.getFilePathBeans() != null && bean.getFilePathBeans().size() > 0) {
                        for (DraftFootBean.FilePathBean fileBean : bean.getFilePathBeans()) {
                            FootRouteTextInfo.FootRouteFileInfo fileInfo = new FootRouteTextInfo.FootRouteFileInfo();
                            File file = new File(fileBean.getPath());
                            if (file.exists()) {
                                fileInfo.setMediaFile(new File(fileBean.getPath()));
                                fileInfo.setFileType(fileBean.getType());
                                mediaFiles.add(fileInfo);
                            }
                        }
                    }
                    if (mediaFiles.size() > 0) {
                        map.put("file", mediaFiles);
                    }
                    mPresenter.commitRoute(map);
                    showLoading("正在上传中...");
                });
            }
        });
    }

    private void deleteDraft(int position) {
        draftListAdapter.notifyItemRemoved(position);
        DraftFootBean draftFootBean = routeDraft.get(position);
        List<String> paths = new ArrayList<>();
        if (draftFootBean.getFilePathBeans() != null && draftFootBean.getFilePathBeans().size() > 0) {
            for (DraftFootBean.FilePathBean fileBean : draftFootBean.getFilePathBeans()) {
                paths.add(fileBean.getPath());
            }
        }
        FootUtil.deleteFootFile(paths);
        draftFootBeans.remove(draftFootBean);
        mSharedPrefUtil.putList(ConstStrings.DraftFootList, draftFootBeans);
        routeDraft.remove(position);
    }

    public void onViewClicked(View view) {

    }

    @Override
    public void onRouteCommitResult(String url) {
        dismissLoading();
        showToast("上传成功！");
        deleteDraft(uploadPosition);
    }

    @Override
    public void onRouteCommitError()
    {
        dismissLoading();
        showToast("上传失败，请重试或删除后重新添加");
    }
}
