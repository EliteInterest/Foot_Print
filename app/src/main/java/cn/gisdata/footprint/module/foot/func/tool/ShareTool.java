package cn.gisdata.footprint.module.foot.func.tool;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.zx.zxutils.util.ZXBitmapUtil;
import com.zx.zxutils.util.ZXSharedPrefUtil;
import com.zx.zxutils.util.ZXToastUtil;
import com.zx.zxutils.views.BottomSheet.SheetData;
import com.zx.zxutils.views.BottomSheet.ZXBottomSheet;

import java.util.HashMap;

import cn.gisdata.footprint.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by fxs on 2018/6/1.
 * 功能：分享工具
 */
public class ShareTool {

    private static ZXBottomSheet bottomSheet;

    public static void doShare(Context context, String url, String msg) {
        bottomSheet = ZXBottomSheet.initGrid(context)
                .addItem("QQ", ContextCompat.getDrawable(context, R.mipmap.share_qq))
                .addItem("QQ空间", ContextCompat.getDrawable(context, R.mipmap.share_qzone))
                .addItem("新浪微博", ContextCompat.getDrawable(context, R.mipmap.share_weibo))
                .addItem("微信好友", ContextCompat.getDrawable(context, R.mipmap.share_wechat))
                .addItem("微信朋友圈", ContextCompat.getDrawable(context, R.mipmap.share_wechat_friend))
                .addItem("微信收藏", ContextCompat.getDrawable(context, R.mipmap.share_wechat_star))
                .showCheckMark(false)
                .showCloseView(true)
                .setOnItemClickListener(new ZXBottomSheet.OnSheetItemClickListener() {
                    @Override
                    public void onSheetItemClick(SheetData sheetData, int i) {
                        switch (i) {
                            case 0:
                                showshare(context, QQ.NAME, url,msg);
                                break;
                            case 1:
                                showshare(context, QZone.NAME, url,msg);
                                break;
                            case 2:
                                showshare(context, SinaWeibo.NAME, url,msg);
                                break;
                            case 3:
                                showshare(context, Wechat.NAME, url,msg);
                                break;
                            case 4:
                                showshare(context, WechatMoments.NAME, url,msg);
                                break;
                            case 5:
                                showshare(context, WechatFavorite.NAME, url,msg);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .build()
                .show();

    }

    private static void showshare(Context context, String platForm, String url, String msg) {
        ZXSharedPrefUtil zxSharedPrefUtil = new ZXSharedPrefUtil();
        OnekeyShare oks = new OnekeyShare();
        oks.setPlatform(platForm);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("我的足迹");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(msg);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (zxSharedPrefUtil.contains("headPortraits")) {
            oks.setImageUrl(zxSharedPrefUtil.getString("headPortraits"));
        } else {
            oks.setImageData(ZXBitmapUtil.drawableToBitmap(ContextCompat.getDrawable(context, R.mipmap.ic_launcher)));
        }
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ZXToastUtil.showToast("分享成功！");
                bottomSheet.dismiss();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ZXToastUtil.showToast("分享失败！");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ZXToastUtil.showToast("取消分享！");
            }
        });
        // 启动分享GUI
        oks.show(context);
    }

}
