package cn.gisdata.footprint.module.foot.mvp.presenter;

import cn.gisdata.footprint.module.foot.bean.FootRouteTextInfo;
import cn.gisdata.footprint.module.foot.mvp.contract.FootContract;
import com.frame.zxmvp.baserx.RxHelper;
import com.frame.zxmvp.baserx.RxSubscriber;

import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class FootPresenter extends FootContract.Presenter {

    @Override
    public void commitRoute(Map<String, Object> map) {
        String FootprintInfo = map.get("FootprintInfo") == null ? "" : (String) map.get("FootprintInfo");
        String TextInfo = map.get("TextInfo") == null ? "{}" : (String) map.get("TextInfo");
        String PathInfo = map.get("PathInfo") == null ? "{}" : (String) map.get("PathInfo");
        String SaveInfo = map.get("SaveInfo") == null ? "{}" : (String) map.get("SaveInfo");
        List<FootRouteTextInfo.FootRouteFileInfo> files = (List<FootRouteTextInfo.FootRouteFileInfo>) map.get("file");
        RequestBody requestBody = null;

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("FootprintInfo", FootprintInfo);

        builder.addFormDataPart("TextInfo", TextInfo);
        builder.addFormDataPart("PathInfo", PathInfo);
        builder.addFormDataPart("SaveInfo", SaveInfo);

        if (files != null && files.size() > 0) {
            for (FootRouteTextInfo.FootRouteFileInfo file : files) {
                if (file.getFileType() == 2) {
                    builder.addFormDataPart("FileInfo", file.getMediaInfo().getName(), RequestBody.create(MediaType.parse("image/*"), file.getMediaInfo()));
                } else {
                    builder.addFormDataPart("FileInfo", file.getMediaInfo().getName(), RequestBody.create(MediaType.parse("video/*"), file.getMediaInfo()));
                }
            }
        }

        requestBody = builder.build();

        mModel.commitRoute(requestBody)
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<String>(mView, "正在上传中...") {
                    @Override
                    protected void _onNext(String o) {
                        mView.onRouteCommitResult(o);
                    }

                    @Override
                    protected void _onError(String message) {

                        mView.showToast(message);
                    }
                });
    }
}