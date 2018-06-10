package cn.gisdata.footprint.base;

import android.os.Handler;

import com.frame.zxmvp.base.BaseModel;
import com.frame.zxmvp.base.BasePresenter;
import com.frame.zxmvp.base.RxBaseFragment;
import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.util.ZXSharedPrefUtil;
import com.zx.zxutils.util.ZXToastUtil;

/**
 * Created by Xiangb on 2017/7/6.
 * 功能：
 */

public abstract class BaseFragment<T extends BasePresenter, E extends BaseModel> extends RxBaseFragment<T, E> {

    public ZXSharedPrefUtil mSharedPrefUtil = new ZXSharedPrefUtil();
    public Handler handler = new Handler();

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void showLoading(String message, int progress) {
        ZXDialogUtil.showLoadingDialog(getActivity(), message, progress);
    }

    @Override
    public void showLoading(String message) {
        ZXDialogUtil.showLoadingDialog(getActivity(), message);
    }

    @Override
    public void dismissLoading() {
        ZXDialogUtil.dismissLoadingDialog();
    }

    @Override
    public void showToast(String message) {
        ZXToastUtil.showToast(message);
    }
}
