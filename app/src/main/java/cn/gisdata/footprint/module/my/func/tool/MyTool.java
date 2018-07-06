package cn.gisdata.footprint.module.my.func.tool;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;

import cn.gisdata.footprint.module.my.ui.MyFragment;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyTool {
    public static void setIamge(Context context, ImageView imageView, String url, int width, int height) {
        OkHttpUtils.get().url(url).tag(context)
                .build()
                .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        if (height > 0)
                            imageView.setMaxHeight(height);
                        if (width > 0)
                            imageView.setMaxWidth(width);
                        imageView.setBackground(new BitmapDrawable(response));
                        MyFragment.bitmap = response;
                    }
                });
    }


    public static File getFileFromServer(String serverUrl, String path, String apkName, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            File apkFile = new File(path, apkName);

            FileOutputStream fos = new FileOutputStream(apkFile);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return apkFile;
        } else {
            return null;
        }
    }
}
