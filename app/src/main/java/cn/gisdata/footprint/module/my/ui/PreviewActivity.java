package cn.gisdata.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zx.zxutils.util.ZXFragmentUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gisdata.footprint.R;
import cn.gisdata.footprint.base.BaseActivity;
import cn.gisdata.footprint.module.foot.func.tool.ShareTool;
import cn.gisdata.footprint.module.foot.ui.WebViewFragment;
import cn.gisdata.footprint.module.my.mvp.contract.PreviewContract;
import cn.gisdata.footprint.module.my.mvp.model.PreviewModel;
import cn.gisdata.footprint.module.my.mvp.presenter.PreviewPresenter;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class PreviewActivity extends BaseActivity<PreviewPresenter, PreviewModel> implements PreviewContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String name = "";
    private String url = "";
    private String title = "";

    public static void startAction(Activity activity, boolean isFinish, String name, String url,String title) {
        Intent intent = new Intent(activity, PreviewActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        tvTitle.setText(name);

        ZXFragmentUtil.addFragment(getSupportFragmentManager(), WebViewFragment.newInstance(url), R.id.fm_preview);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_save:
                ShareTool.doShare(this, url, name,title);
                break;
        }
    }

}
