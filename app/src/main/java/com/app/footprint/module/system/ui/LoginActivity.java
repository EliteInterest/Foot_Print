package com.app.footprint.module.system.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.footprint.R;
import com.app.footprint.api.ApiParamUtil;
import com.app.footprint.app.ConstStrings;
import com.app.footprint.app.MyApplication;
import com.app.footprint.module.system.bean.LoginEntity;
import com.app.footprint.module.system.mvp.contract.LoginContract;
import com.app.footprint.module.system.mvp.model.LoginModel;
import com.app.footprint.module.system.mvp.presenter.LoginPresenter;
import com.app.footprint.base.BaseActivity;
import com.bumptech.glide.load.resource.bitmap.BitmapDecoder;
import com.zx.zxutils.util.ZXMD5Util;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_dologin)
    Button btnDologin;
    @BindView(R.id.btn_doregister)
    Button getBtnDoRegidter;
    @BindView(R.id.login_layout_bg)
    ImageView imgLoginBg;

    @BindView(R.id.login_layout)
    RelativeLayout loginLayout;

    private String userName, userPwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void initView(Bundle savedInstanceState) {
//        Drawable bg =  loginLayout.getBackground();
//        Bitmap bitmap = ((BitmapDrawable)loginLayout.getBackground()).getBitmap();
//        blur(bitmap,loginLayout);
//        Bitmap bitmap  = fastblur(this,((BitmapDrawable)bg).getBitmap(),20);
//        loginLayout.setBackground(new BitmapDrawable(bitmap));

//        Bitmap bitmap = ((BitmapDrawable)imgLoginBg.getBackground()).getBitmap();
//        Bitmap bitmap = imgLoginBg.getDrawingCache();
//        blur(bitmap,imgLoginBg);

    }

    @Override
    protected void onResume() {
        String userName = mSharedPrefUtil.getString("userName");
        String userPwd = mSharedPrefUtil.getString("userPwd");
        boolean registered = mSharedPrefUtil.getBool("registered", false);

        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)) {
            etUsername.setText(userName);
            etPassword.setText(userPwd);
        }

        if(registered)
        {
            btnDologin.performClick();
        }

        super.onResume();
//        new Handler().postDelayed(() -> {
//            Drawable bg =  loginLayout.getBackground();
//            Bitmap bitmap = ((BitmapDrawable)loginLayout.getBackground()).getBitmap();
//            blur(bitmap,loginLayout);
//        }, 3000);
    }

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public void onLoginResult(LoginEntity loginEntity) {
        Log.i(TAG, "onLoginStart...");
        dismissLoading();
        showToast("登录成功");
//        ConstStrings.code = loginEntity.getCode();
//        ConstStrings.e = ZXMD5Util.getMD5(ZXMD5Util.getMD5(userPwd)
//                + loginEntity.getSalt());
//        List<LoginEntity.RowsBean> data = loginEntity.getData();
//        ConstStrings.adrApikey = loginEntity.getAdrApikey();
        mSharedPrefUtil.putString("userName", userName);
        mSharedPrefUtil.putString("userPwd", userPwd);
//        mSharedPrefUtil.putString("code", ConstStrings.code);
//        mSharedPrefUtil.putString("e", ConstStrings.e);
//        mSharedPrefUtil.putString("adrApikey", ConstStrings.adrApikey);
        MainActivity.startAction(this, true);
    }

    @Override
    public void onLoginStart() {
        Log.i(TAG, "onLoginStart...");
        showLoading("正在登陆中...");
    }

    @OnClick({R.id.btn_dologin, R.id.btn_doregister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_dologin:
                //TODO 要删除
//                MainActivity.startAction(this, false);

                userName = etUsername.getText().toString();
                userPwd = etPassword.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    showToast("请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(userPwd)) {
                    showToast("请输入用户密码");
                    return;
                }
                mPresenter.doLogin(ApiParamUtil.getLoginDataInfo(userName, userPwd));
                break;

            case R.id.btn_doregister:
                RegisterActivity.startAction(this, false);
                break;
            default:
                break;
        }

    }

    private long triggerAtTimefirst = 0;

    @Override
    public void onBackPressed() {

        long triggerAtTimeSecond = triggerAtTimefirst;
        triggerAtTimefirst = SystemClock.elapsedRealtime();
        if (triggerAtTimefirst - triggerAtTimeSecond <= 2000) {
            MyApplication.getInstance().finishAll();
        } else {
            showToast("请再点击一次, 确认退出...");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate...");
        mSharedPrefUtil.putBool("registered", false);
        super.onCreate(savedInstanceState);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();

        float radius = 20;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
                (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(this);

        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());

        blur.setInput(overlayAlloc);

        blur.setRadius(radius);

        blur.forEach(overlayAlloc);

        overlayAlloc.copyTo(overlay);

//        view.setImageBitmap(overlay);
        view.setBackground(new BitmapDrawable(
                getResources(), overlay));

        rs.destroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs,
                    sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs,
                    input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
                    Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        } else {

            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1) {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int temp = 256 * divsum;
            int dv[] = new int[temp];
            for (i = 0; i < temp; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                            | (dv[gsum] << 8) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return (bitmap);
        }
    }
}
