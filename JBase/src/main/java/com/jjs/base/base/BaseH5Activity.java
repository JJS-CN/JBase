package com.jjs.base.base;

import android.content.Intent;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 说明：详细：http://blog.csdn.net/carson_ho/article/details/52693322
 * 点击不启动系统浏览器、返回不直接关闭activity、加载https、销毁处理、自动回调 标题Title 和 加载进度Progress
 * Created by aa on 2017/10/30.
 */

public abstract class BaseH5Activity extends BaseActivity {
    WebView mWebView;

    /**
     * 页面标题回调
     */
    protected abstract void _onReceivedTitle(WebView view, String title);

    /**
     * 页面加载进度回调
     */
    protected abstract void _onProgressChanged(WebView view, int newProgress);

    /**
     * 初始化webView
     *
     * @param webview 控件
     * @param url     加载地址
     */
    public void init(final WebView webview, String url) {
        mWebView = webview;

        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //默认是不加载https连接的，进行处理
                handler.proceed();
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });

        //设置WebChromeClient类
        mWebView.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里：" + title);
                _onReceivedTitle(view, title);
            }

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println("加载进度，直接是百分比：" + newProgress);
                _onProgressChanged(view, newProgress);
            }
        });

        //开始加载页面
        mWebView.loadUrl(url);
    }


    @Override
    protected void onActivityResult(int requestCode, Intent data) {

    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView != null) {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                mWebView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            }
        }
        return false;
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
