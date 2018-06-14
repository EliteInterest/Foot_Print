package cn.gisdata.footprint.module.foot.func.tool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.zx.zxutils.util.ZXBitmapUtil;
import com.zx.zxutils.util.ZXFileUtil;
import com.zx.zxutils.util.ZXSharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.module.foot.bean.FootFileBean;

/**
 * Created by fxs on 2018/5/24.
 * 功能：
 */

public class FootUtil {

    public static Drawable drawTextToDrawable(Context context, int resourceId, String mText) {
        try {
            Resources resources = context.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.WHITE);
            // text size in pixels
            if (13 * scale * mText.length() > bitmap.getWidth()) {
                paint.setTextSize(bitmap.getWidth() / mText.length() - 1);
            } else {
                paint.setTextSize((int) (13 * scale));
            }
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() / 2 - bounds.width() / 2);
            int y = 25;

            canvas.drawText(mText, x, y * scale, paint);

            bitmap.setHasAlpha(true);
            return ZXBitmapUtil.bitmapToDrawable(bitmap);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public static void clearFootCache() {
        ZXSharedPrefUtil zxSharedPrefUtil = new ZXSharedPrefUtil();
        List<FootFileBean> footFileBeans = zxSharedPrefUtil.getList(ConstStrings.FootFiles);
        if (footFileBeans != null && footFileBeans.size() > 0) {
            for (FootFileBean file : footFileBeans) {
                ZXFileUtil.deleteFiles(ConstStrings.getCachePath() + file.getVedioShootPath());
                ZXFileUtil.deleteFiles(ConstStrings.getCachePath() + file.getVedioPath());
                if (file.getPicPaths() != null && file.getPicPaths().size() > 0) {
                    for (FootFileBean.PicBean picBean : file.getPicPaths()) {
                        ZXFileUtil.deleteFiles(ConstStrings.getCachePath() + picBean.getPath());
                    }
                }
            }
        }
        zxSharedPrefUtil.remove(ConstStrings.RECORD_POINTS);
        zxSharedPrefUtil.remove(ConstStrings.RECORD_START_TIME);
        zxSharedPrefUtil.putBool(ConstStrings.RECORD_STATUS, false);
        zxSharedPrefUtil.putList(ConstStrings.FootFiles, new ArrayList<>());
    }

    public static void deleteFootFile(List<String> filePaths) {
        for (String path : filePaths) {
            ZXFileUtil.deleteFiles(path);
        }
    }

}
