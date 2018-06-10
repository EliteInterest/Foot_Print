package cn.gisdata.footprint.module.foot.ui;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.base.BaseFragment;
import cn.gisdata.footprint.module.foot.mvp.contract.WebViewContract;
import cn.gisdata.footprint.module.foot.mvp.model.WebViewModel;
import cn.gisdata.footprint.module.foot.mvp.presenter.WebViewPresenter;

import butterknife.BindView;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class WebViewFragment extends BaseFragment<WebViewPresenter, WebViewModel> implements WebViewContract.View {

    @BindView(R.id.web_preview)
    WebView webView;

    private String url;

    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mappreview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        url = getArguments().getString("url");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        // 设置支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 启动缓存
        webView.getSettings().setAppCacheEnabled(true);
        // 设置缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new webViewClient());
        webView.loadUrl(url);

    }

    public void reload(String url) {
        webView.loadUrl(url);
        webView.reload();
    }


    class webViewClient extends WebViewClient {

        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            //如果不需要其他对点击链接事件的处理返回true，否则返回false

            return true;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }
}
