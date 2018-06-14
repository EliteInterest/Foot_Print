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
import cn.gisdata.footprint.module.foot.func.tool.FootUtil;
import cn.gisdata.footprint.module.foot.mvp.contract.EditInfoContract;
import cn.gisdata.footprint.module.foot.mvp.model.EditInfoModel;
import cn.gisdata.footprint.module.foot.mvp.presenter.EditInfoPresenter;
import cn.gisdata.footprint.module.map.bean.BaiduSearchBean;
import cn.gisdata.footprint.module.map.func.util.BaiduMapUtil;
import cn.gisdata.footprint.module.my.func.adapter.DraftListAdapter;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class DraftFootListFragment extends BaseFragment<EditInfoPresenter, EditInfoModel> implements EditInfoContract.View {


    @BindView(R.id.rv_foot_list)
    RecyclerView rvFootList;
    @BindView(R.id.srl_foot_refresh)
    SwipeRefreshLayout srlFootRefresh;

    private List<DraftFootBean> draftFootBeans;
    private List<DraftFootBean> footDraft = new ArrayList<>();

    private int uploadPosition = 0;
    private DraftListAdapter footDraftAdapter;

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

        draftFootBeans = mSharedPrefUtil.getList(ConstStrings.DraftFootList);
        if (draftFootBeans != null && draftFootBeans.size() > 0) {
            for (DraftFootBean bean : draftFootBeans) {
                if (bean.getFootType() == 0) {
                    footDraft.add(bean);
                }
            }
        }

        srlFootRefresh.setEnabled(false);

        //定义脚印列表，根据自己定义的类型填充
        footDraftAdapter = new DraftListAdapter(footDraft, getActivity());
        rvFootList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFootList.setAdapter(footDraftAdapter);

        ZXRecyclerDeleteHelper deleteHelper = new ZXRecyclerDeleteHelper(getActivity(), rvFootList)
//                .setClickable(i -> PreviewActivity.startAction(getActivity(), false, footList.get(i).getName(), footList.get(i).getDetailsUrlPath()))
                .setSwipeOptionViews(R.id.tv_delete)
                .setSwipeable(R.id.ll_item_content, R.id.ll_list_menu, (viewID, position) -> deleteDraft(position));

        footDraftAdapter.setUplaodListener(new DraftListAdapter.OnUplaodListener() {
            @Override
            public void onUpload(int position) {
                ZXDialogUtil.showYesNoDialog(getActivity(), "提示", "是否重新上传?", (dialog, which) -> {
                    uploadPosition = position;
                    DraftFootBean draftFootBean = footDraft.get(position);
                    String pointString = draftFootBean.getPoint();
                    if (!TextUtils.isEmpty(pointString)) {
                        String[] points = pointString.split(",");
                        if (points != null & points.length > 1) {
                            try {
                                getAddress(Double.valueOf(points[0]), Double.valueOf(points[1]), draftFootBean);
                            } catch (Exception e) {
                                e.printStackTrace();
                                uploadInfo(draftFootBean);
                            }
                            return;
                        }
                    }
                    uploadInfo(draftFootBean);
                });
            }
        });
    }

    /**
     * 获取地理位置信息
     */
    private void getAddress(double x, double y, DraftFootBean draftFootBean) {
        BaiduMapUtil.searchPoi(x, y, new BaiduMapUtil.OnBaiduSearchListener() {
            @Override
            public void onSearchBack(BaiduSearchBean baiduSearchBean) {
                String streetName = "";
                String formatName = "";
                streetName = baiduSearchBean.getResult().getAddressComponent().getStreet();
                formatName = baiduSearchBean.getResult().getFormatted_address();
                if (streetName == null || streetName.length() == 0) {
                    streetName = formatName;
                }
                draftFootBean.setMarkInfoJson(draftFootBean.getMarkInfoJson()
                        .replaceAll("streetUnKnow", streetName)
                        .replaceAll("formatUnKnow", formatName));
                uploadInfo(draftFootBean);
            }

            @Override
            public void onSearchError() {
                uploadInfo(draftFootBean);
            }
        });
    }

    private void uploadInfo(DraftFootBean draftFootBean) {
        draftFootBean.setMarkInfoJson(draftFootBean.getMarkInfoJson()
                .replaceAll("streetUnKnow", "未知位置")
                .replaceAll("formatUnKnow", "未知位置"));
        Map<String, Object> map = new HashMap<>();
        map.put("FootmarkInfo", draftFootBean.getMarkInfoJson());
        map.put("uploadType", draftFootBean.getUploadType());
        List<File> files = new ArrayList<>();
        if (draftFootBean.getFilePaths() != null && draftFootBean.getFilePaths().size() > 0) {
            for (String path : draftFootBean.getFilePaths()) {
                File file = new File(path);
                if (file.exists()) {
                    files.add(file);
                }
            }
        }
        if (files.size() > 0) {
            map.put("file", files);
        }
        mPresenter.commitFile(map);
        showLoading("正在上传中...");
    }

    private void deleteDraft(int position) {
        footDraftAdapter.notifyItemRemoved(position);
        DraftFootBean draftFootBean = footDraft.get(position);
        FootUtil.deleteFootFile(draftFootBean.getFilePaths());
        draftFootBeans.remove(draftFootBean);
        mSharedPrefUtil.putList(ConstStrings.DraftFootList, draftFootBeans);
        footDraft.remove(position);

    }

    public void onViewClicked(View view) {
    }


    @Override
    public void onFileCommitResult(String result) {
        dismissLoading();
        showToast("上传成功！");
        deleteDraft(uploadPosition);
    }

    @Override
    public void onFileCommitError() {
        dismissLoading();
        showToast("上传失败，请重试或删除后重新添加");
    }
}
