package com.app.footprint.module.foot.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.app.footprint.R;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.base.BaseActivity;
import com.app.footprint.module.foot.bean.FootFileBean;
import com.app.footprint.module.foot.mvp.contract.CameraContract;
import com.app.footprint.module.foot.mvp.model.CameraModel;
import com.app.footprint.module.foot.mvp.presenter.CameraPresenter;
import com.zx.zxutils.util.ZXBitmapUtil;
import com.zx.zxutils.util.ZXFileUtil;
import com.zx.zxutils.views.CameraView.ZXCameraView;
import com.zx.zxutils.views.CameraView.listener.CameraListener;
import com.zx.zxutils.views.ZXStatusBarCompat;

import java.io.File;

import butterknife.BindView;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class CameraActivity extends BaseActivity<CameraPresenter, CameraModel> implements CameraContract.View {

    @BindView(R.id.jcameraview)
    ZXCameraView jCameraView;
    @BindView(R.id.et_camera_remark)
    EditText etRemark;

    private FootFileBean footFileBean;
    public static final int MAPCODE = 1;
    public static final int COMMITCODE = 2;
    private int requestCode = MAPCODE;

    public static void startAction(Activity activity, boolean isFinish, int requestCode) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra("requestCode", requestCode);
        activity.startActivityForResult(intent, requestCode);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ZXStatusBarCompat.translucent(this);
        requestCode = getIntent().getIntExtra("requestCode", requestCode);
        if (mSharedPrefUtil.contains("footFileBean")) {
            footFileBean = mSharedPrefUtil.getObject("footFileBean");
        } else {
            footFileBean = new FootFileBean();
            mSharedPrefUtil.putObject("footFileBean", footFileBean);
        }

        File file = new File(ConstStrings.getCachePath());
        if (!file.exists()) {
            file.mkdirs();
        }

        //设置视频保存路径
        jCameraView.setSaveVideoPath(ConstStrings.getCachePath())
                .setCameraMode(ZXCameraView.BUTTON_STATE_BOTH)
                .setMaxVedioDuration(30)
                .setMediaQuality(ZXCameraView.MEDIA_QUALITY_MIDDLE)
                .setJCameraLisenter(new CameraListener() {
                    @Override
                    public void onCaptureCommit(Bitmap bitmap) {
                        String picName = "pic" + System.currentTimeMillis() + ".jpg";
                        ZXBitmapUtil.bitmapToFile(bitmap, new File(ConstStrings.getCachePath() + picName));
                        //获取图片bitmap
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "pic");
                        bundle.putString("picName", picName);
                        bundle.putString("remark", etRemark.getText().toString());
                        onDataBack(bundle);
                    }

                    @Override
                    public void onRecordCommit(String path, Bitmap bitmap) {
                        String vedioName = "vedio" + System.currentTimeMillis() + ".mp4";
                        ZXFileUtil.rename(path, vedioName);
                        String vedioShootName = "vedioShoot" + System.currentTimeMillis() + ".jpg";
                        ZXBitmapUtil.bitmapToFile(bitmap, new File(ConstStrings.getCachePath() + vedioShootName));
                        //获取视频路径
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "vedio");
                        bundle.putString("vedioName", vedioName);
                        bundle.putString("vedioShoot", vedioShootName);
                        onDataBack(bundle);
                    }

                    @Override
                    public void onActionSuccess(CameraType cameraType) {
                        if (cameraType == CameraType.Picture) {
                            etRemark.setText("");
                            etRemark.setVisibility(View.VISIBLE);
                        } else {
                            etRemark.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ErrorType errorType) {

                    }
                });
    }

    private void onDataBack(Bundle bundle) {
        if (requestCode == MAPCODE) {
            EditInfoActivity.startAction(this, true,bundle);
        } else if (requestCode == COMMITCODE) {
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(100, intent);
            finish();
        }
    }

    public void onViewClicked(View view) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }
}
