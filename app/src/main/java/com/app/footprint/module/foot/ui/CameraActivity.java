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
    private boolean isFromMap = true;
    private int cameraType = 0;

    public static void startAction(Activity activity, boolean isFinish, boolean isFromMap, int cameraType) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra("isFromMap", isFromMap);
        intent.putExtra("cameraType", cameraType);
        activity.startActivityForResult(intent, 0);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ZXStatusBarCompat.translucent(this);
        isFromMap = getIntent().getBooleanExtra("isFromMap", true);
        cameraType = getIntent().getIntExtra("cameraType", 0);
        File file = new File(ConstStrings.getCachePath());
        if (!file.exists()) {
            file.mkdirs();
        }

        //设置视频保存路径
        jCameraView.setSaveVideoPath(ConstStrings.getCachePath())
                .setCameraMode(cameraType == 0 ? ZXCameraView.BUTTON_STATE_ONLY_CAPTURE : ZXCameraView.BUTTON_STATE_ONLY_RECORDER)
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
                })
                .setTip(cameraType == 0 ? "点击拍照" : "长按录像");
        jCameraView.setLeftClickListener(() -> {
            if (isFromMap) {
                setResult(-1);
            }
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        if (isFromMap) {
            setResult(-1);
        }
        finish();
    }

    private void onDataBack(Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(0x02, intent);
        finish();
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
