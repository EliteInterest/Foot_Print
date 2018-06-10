package cn.gisdata.footprint.module.foot.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.base.BaseActivity;
import cn.gisdata.footprint.module.foot.mvp.contract.PicPreviewContract;
import cn.gisdata.footprint.module.foot.mvp.model.PicPreviewModel;
import cn.gisdata.footprint.module.foot.mvp.presenter.PicPreviewPresenter;
import com.zx.zxutils.util.ZXImageLoaderUtil;
import com.zx.zxutils.views.ZXPhotoView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class PicPreviewActivity extends BaseActivity<PicPreviewPresenter, PicPreviewModel> implements PicPreviewContract.View {

    @BindView(R.id.photo_view)
    ZXPhotoView zxPhotoView;
    @BindView(R.id.et_photo_remark)
    EditText etRemark;

    private String path;
    private String remark;

    public static void startAction(Activity activity, boolean isFinish, String path, String remark) {
        Intent intent = new Intent(activity, PicPreviewActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("remark", remark);
        activity.startActivityForResult(intent, 0);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pic_preview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        path = getIntent().getStringExtra("path");
        remark = getIntent().getStringExtra("remark");

        ZXImageLoaderUtil.display(zxPhotoView, new File(ConstStrings.getCachePath() + path));
        etRemark.setText(remark);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_save:
                Intent intent = new Intent();
                intent.putExtra("remark", etRemark.getText().toString());
                setResult(0x03, intent);
                finish();
                break;

            default:
                break;
        }
    }

}
