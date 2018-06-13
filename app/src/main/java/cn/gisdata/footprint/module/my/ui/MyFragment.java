package cn.gisdata.footprint.module.my.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.api.ApiParamUtil;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.base.BaseFragment;
import cn.gisdata.footprint.module.foot.func.view.FootRecordView;
import cn.gisdata.footprint.module.foot.ui.FootFragment;
import cn.gisdata.footprint.module.my.bean.UserInfoEntity;
import cn.gisdata.footprint.module.my.mvp.contract.MyContract;
import cn.gisdata.footprint.module.my.mvp.model.MyModel;
import cn.gisdata.footprint.module.my.mvp.presenter.MyPresenter;
import cn.gisdata.footprint.module.my.func.tool.MyTool;
import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.util.ZXFileUtil;
import com.zx.zxutils.views.BottomSheet.SheetData;
import com.zx.zxutils.views.BottomSheet.ZXBottomSheet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class MyFragment extends BaseFragment<MyPresenter, MyModel> implements MyContract.View {
    private static final String TAG = "MyFragment";
    public static Bitmap bitmap = null;
    @BindView(R.id.layout_head)
    RelativeLayout mHeadLayout;

    @BindView(R.id.personal_nickname_textview)
    TextView mNickName;

    @BindView(R.id.personal_username_textview)
    TextView mUserName;

    @BindView(R.id.RouteCount)
    TextView mRouteCount;

    @BindView(R.id.FootmarkCount)
    TextView mFootmarkCount;

    @BindView(R.id.Integral)
    TextView mIntegral;

    @BindView(R.id.VisitVolume)
    TextView mVisitVolume;

    @BindView(R.id.layout_settings)
    RelativeLayout mSettingsLayout;

    @BindView(R.id.layout_contact)
    RelativeLayout mContactLayout;

    @BindView(R.id.person_account_content)
    TextView mSettingsView;

    @BindView(R.id.personal_image)
    ImageView mHeadImage;


    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        refreshUI();
    }

    @Override
    public void onResume() {
        doUserData();
        refreshUI();
        super.onResume();
    }

    private void refreshUI() {
        String userName = mSharedPrefUtil.getString("userName");
        String nickName = mSharedPrefUtil.getString("nickName");
        int footPeriod = mSharedPrefUtil.getInt("footPeriod", 0);
        String headUrl = mSharedPrefUtil.getString("headPortraits");
        if(headUrl!=null && !TextUtils.isEmpty(headUrl))
            MyTool.setIamge(getActivity(),mHeadImage,headUrl,70,70);
        if(bitmap != null)
        {
            Log.i(TAG,"bitmap is not NULL!");
        }
        else
            Log.i(TAG,"bitmap is NULL!");

        Log.i(TAG, "username is " + userName);
        if (!TextUtils.isEmpty(userName)) {
            mUserName.setText(userName);
        }

        if (!TextUtils.isEmpty(nickName)) {
            mNickName.setText(nickName);
        }

        String period = "10秒";
        if (footPeriod != 0) {
            int merchant = footPeriod / 60;
            int remainder = footPeriod % 60;
            if (merchant > 0) {
                period = String.valueOf(merchant);
                period += "分钟";
            } else {
                period = String.valueOf(remainder);
                period += "秒";
            }
        }

        mSettingsView.setText(period);
    }

    private void doUserData() {
        String userId = mSharedPrefUtil.getString("userId");
        mPresenter.doRequestUserInfo(ApiParamUtil.getUserDataInfo(userId));
    }

    @OnClick({R.id.layout_head, R.id.layout_settings, R.id.layout_contact,
                R.id.RouteCount_layout,R.id.FootmarkCount_layout,R.id.Integral_layout,
                R.id.VisitVolume_layout,R.id.layout_delete_cache,R.id.layout_draft_box})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                PersonalInfoActivity.startAction(getActivity(), false);
                break;

            case R.id.layout_settings:
                ZXBottomSheet.initList(getActivity())
                        .addItem("10秒")
                        .addItem("30秒")
                        .addItem("1分钟")
                        .addItem("2分钟")
                        .addItem("3分钟")
                        .setOnItemClickListener(new ZXBottomSheet.OnSheetItemClickListener() {
                            @Override
                            public void onSheetItemClick(SheetData sheetData, int i) {
                                String name = sheetData.getName();
                                mSettingsView.setText(name);

                                switch (i) {
                                    case 0:
                                        mSharedPrefUtil.putInt("footPeriod", 10);
                                        break;
                                    case 1:
                                        mSharedPrefUtil.putInt("footPeriod", 30);
                                        break;
                                    case 2:
                                        mSharedPrefUtil.putInt("footPeriod", 60);
                                        break;
                                    case 3:
                                        mSharedPrefUtil.putInt("footPeriod", 120);
                                        break;
                                    case 4:
                                        mSharedPrefUtil.putInt("footPeriod", 180);
                                        break;
                                    default:
                                        mSharedPrefUtil.putInt("footPeriod", 10);
                                        break;
                                }
                            }
                        })
                        .showCheckMark(true)
                        .setCheckIndex(0)
                        .showCloseView(false)
                        .build()
                        .show();
                break;

            case R.id.layout_contact:
                ContactActivity.startAction(getActivity(), false);
                break;

            case R.id.RouteCount_layout://路线
                MyFootListActivity.startAction(getActivity(),false,0);
                break;

            case R.id.FootmarkCount_layout://脚印
                MyFootListActivity.startAction(getActivity(),false,1);
                break;

            case R.id.Integral_layout://积分
                IntegralActivity.startAction(getActivity(),false);
                break;

            case R.id.VisitVolume_layout://访问
                break;

            case R.id.layout_delete_cache:
                final boolean recordStatus = mSharedPrefUtil.getBool("record_status", false);
                ZXDialogUtil.showYesNoDialog(getActivity(), "提示", "是否清除缓存？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(recordStatus)
                        {
                            //recording... so first stop it!
                            if(FootFragment.mapFragment!=null)
                            {
                                FootRecordView footRecordView = FootFragment.mapFragment.getFootRecordView();
                                if(footRecordView!=null)
                                {
                                    footRecordView.closeRoute();
                                }
                            }
                        }else{
                            mSharedPrefUtil.remove("record_points");
                            mSharedPrefUtil.remove("record_start_time");
                            mSharedPrefUtil.putList(ConstStrings.FootFiles, new ArrayList<>());
                            ZXFileUtil.deleteFiles(ConstStrings.getCachePath());
                        }
                        dialog.dismiss();
                        showToast("缓存清除成功");
                    }
                });
                break;

            case R.id.layout_draft_box:
                MyDraftActivity.startAction(getActivity(),false,0);
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestUserInfoResult(UserInfoEntity userInfoEntity) {
        int RouteCount = userInfoEntity.getRouteCount();
        int FootmarkCount = userInfoEntity.getFootmarkCount();
        int Integral = userInfoEntity.getIntegral();
        int VisitVolume = userInfoEntity.getVisitVolume();

        mRouteCount.setText(String.valueOf(RouteCount));
        mFootmarkCount.setText(String.valueOf(FootmarkCount));
        mIntegral.setText(String.valueOf(Integral));
        mVisitVolume.setText(String.valueOf(VisitVolume));
    }
}