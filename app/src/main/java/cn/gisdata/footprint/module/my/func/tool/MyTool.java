package cn.gisdata.footprint.module.my.func.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import cn.gisdata.footprint.module.my.ui.MyFragment;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

public class MyTool {
    public static void setIamge(Context context, ImageView imageView, String url,int width,int height)
    {
        OkHttpUtils.get().url(url).tag(context)
                .build()
                .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        if(height > 0)
                        imageView.setMaxHeight(height);
                        if(width > 0)
                        imageView.setMaxWidth(width);
                        imageView.setBackground(new BitmapDrawable(response));
                        MyFragment.bitmap = response;
                    }
                });
    }
}
