package com.app.footprint.module.foot.func.tool;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.app.footprint.R;
import com.zx.zxutils.views.BottomSheet.SheetData;
import com.zx.zxutils.views.BottomSheet.ZXBottomSheet;

/**
 * Created by Xiangb on 2018/6/1.
 * 功能：分享工具
 */
public class ShareTool {

    public static void doShare(Context context) {
        ZXBottomSheet.initGrid(context)
                .addItem("QQ", ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                .addItem("QQ空间", ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                .addItem("新浪微博", ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                .addItem("微信好友", ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                .addItem("微信朋友圈", ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                .addItem("微信收藏", ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                .showCheckMark(false)
                .showCloseView(true)
                .setOnItemClickListener(new ZXBottomSheet.OnSheetItemClickListener() {
                    @Override
                    public void onSheetItemClick(SheetData sheetData, int i) {
                        switch (i) {
                            case 0:

                                break;
                            case 1:

                                break;
                            case 2:

                                break;
                            case 3:

                                break;
                            case 4:

                                break;
                            case 5:

                                break;
                            default:
                                break;
                        }
                    }
                })
                .build()
                .show();
    }

}
