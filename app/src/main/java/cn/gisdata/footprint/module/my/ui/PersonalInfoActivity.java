package cn.gisdata.footprint.module.my.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.api.ApiParamUtil;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.base.BaseActivity;
import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import cn.gisdata.footprint.module.my.mvp.contract.PersonalInfoContract;
import cn.gisdata.footprint.module.my.mvp.model.PersonalInfoModel;
import cn.gisdata.footprint.module.my.mvp.presenter.PersonalInfoPresenter;
import cn.gisdata.footprint.module.system.ui.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
@SuppressWarnings("ALL")
public class PersonalInfoActivity extends BaseActivity<PersonalInfoPresenter, PersonalInfoModel> implements PersonalInfoContract.View {
    private static final String TAG = "PersonalInfoActivity";

    @BindView(R.id.personal_back)
    TextView mPersonalBack;

    @BindView(R.id.person_nickName_content)
    TextView mNickName;

    @BindView(R.id.personal_username_textview)
    TextView mUserName;

    @BindView(R.id.person_phone_content)
    TextView mPhoneNum;

    @BindView(R.id.layout_nickName)
    RelativeLayout mNickNameLayout;

    @BindView(R.id.layout_account)
    RelativeLayout mUserNameLayout;

    @BindView(R.id.layout_phone)
    RelativeLayout mPhoneLayout;

    @BindView(R.id.personal_image)
    ImageView mHeadImage;

    public static void startAction(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, PersonalInfoActivity.class);
        activity.startActivity(intent);
        if (isFinish) activity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (MyFragment.bitmap != null) {
            mHeadImage.setMaxHeight(70);
            mHeadImage.setMaxWidth(70);
            Log.i(TAG, "bitmap is " + MyFragment.bitmap.toString());
            mHeadImage.setBackground(new BitmapDrawable(MyFragment.bitmap));
        }
    }

    @Override
    protected void onResume() {
        String userName = mSharedPrefUtil.getString("userName");
        String nickName = mSharedPrefUtil.getString("nickName");
        String phone = mSharedPrefUtil.getString("phone");

        if (!TextUtils.isEmpty(userName)) {
            mUserName.setText(userName);
        }

        if (!TextUtils.isEmpty(nickName)) {
            mNickName.setText(nickName);
        }

        if (!TextUtils.isEmpty(phone)) {
            mPhoneNum.setText(phone);
        }
        super.onResume();
    }

    @OnClick({R.id.personal_back, R.id.layout_nickName, R.id.layout_account, R.id.layout_phone, R.id.btn_settings_logout, R.id.layout_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;

            case R.id.layout_head:
                // 调用android自带的图库
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PersonalInfoContract.ActivityRequestCode.SHOW_MAP_DEPOT);
                break;

            case R.id.layout_nickName:
                String content = mSharedPrefUtil.getString("nickName");
                toEdit(1, "昵称", content == null ? "" : content);
                break;

            case R.id.layout_account:
                content = mSharedPrefUtil.getString("userName");
                toEdit(2, "账号", content == null ? "" : content);
                break;

            case R.id.layout_phone:
                content = mSharedPrefUtil.getString("phone");
                toEdit(3, "手机号", content == null ? "" : content);
                break;

            case R.id.btn_settings_logout:
                //lougout interface
                LoginActivity.startAction(this, true);
                break;

            default:
                break;
        }
    }

    private void toEdit(int tab, String title, String content) {
        Intent intent = new Intent(this, PersonalInfoEditActivity.class);
        intent.putExtra("tab", tab);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
//        startActivity(intent);

        this.setIntent(intent);
        PersonalInfoEditActivity.startAction(this, false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == PersonalInfoContract.ActivityRequestCode.SHOW_MAP_DEPOT
                    && resultCode == Activity.RESULT_OK)
                showPersonalHead(data);
        }

        //开始上传头像
//        mPresenter.doUploadHeadPortraits(PersonalInfoContract.ActivityRequestCode.UPLOAD_STEP_USERID,
//                ApiParamUtil.getUserDataInfo(mSharedPrefUtil.getString("userId")));

        Log.i(TAG, " ConstStrings.LOCAL_HEAD_PIC_PATH is " + ConstStrings.LOCAL_HEAD_PIC_PATH);
        if (ConstStrings.LOCAL_HEAD_PIC_PATH != null && ConstStrings.LOCAL_HEAD_PIC_PATH.length() != 0) {
            showLoading("头像上传中...");
            mPresenter.doUploadHeadPortraits(
                    ApiParamUtil.getHeadPortraitsInfo(mSharedPrefUtil.getString("userId"), ConstStrings.LOCAL_HEAD_PIC_PATH));
        }
    }

    private void showPersonalHead(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        if (picturePath.equals(""))
            return;

        ConstStrings.LOCAL_HEAD_PIC_PATH = picturePath;

        // 缩放图片, width, height 按相同比例缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        int scale = (int) (options.outWidth / (float) 90);
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(picturePath, options);

        mHeadImage.setMaxHeight(70);
        mHeadImage.setMaxWidth(70);

//        mHeadImage.setImageBitmap(bitmap);
        mHeadImage.setBackground(new BitmapDrawable(bitmap));
    }

    @Override
    public void onUploadUserHeadFileResult(UserInfoEntity userInfoEntity) {
        dismissLoading();
        Log.i(TAG, "user head file load success!");
    }
}
