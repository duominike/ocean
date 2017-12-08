package com.joker.ocean.webutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.joker.ocean.R;
import com.joker.pacific.component.BaseFragmentActivity;
import com.joker.pacific.log.Logger;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by joker on 17-12-5.
 */

public class TBSWebViewLayout extends RelativeLayout{
    private Logger mLogger = Logger.getLogger(TBSWebViewLayout.class);
    private BaseFragmentActivity mActivity;
    private WebView mWebView;
    private FrameLayout m_flWebviewContent = null;
    private String path;
    public TBSWebViewLayout(Context context) {
        this(context, null);
    }

    public TBSWebViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TBSWebViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity  = (BaseFragmentActivity)context;
    }

    private void init(Context context){

        this.m_flWebviewContent = (FrameLayout) findViewById(R.id.fl_webview_content);
        this.mWebView = new WebView(getContext());
        this.m_flWebviewContent.addView(this.mWebView);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.requestFocusFromTouch();
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.getSettings().setLoadsImagesAutomatically(true);
        this.mWebView.getSettings().setDatabaseEnabled(true);
        path = mActivity.getApplication().getDir("database", Context.MODE_PRIVATE).getPath();
        this.mWebView.getSettings().setDatabasePath(path);
        this.mWebView.getSettings().setDomStorageEnabled(true);
        this.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        this.mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.mWebView.getSettings().setAppCacheEnabled(true);
        this.mWebView.getSettings().setAppCacheMaxSize(8 * 1024 * 1024);
        path = mActivity.getApplication().getDir("cache", Context.MODE_PRIVATE).getPath();
        this.mWebView.getSettings().setAppCachePath(path);
        this.mWebView.getSettings().setAllowFileAccess(true);
        this.mWebView.getSettings().setGeolocationEnabled(true);
        this.mWebView.getSettings().setDomStorageEnabled(true);


        this.mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                mLogger.info("shouldOverrideUrlLoading  url = " + url);
                return super.shouldOverrideUrlLoading(webView, url);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                mLogger.info("onPageStarted");
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                mLogger.info("onPageFinished");
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                mLogger.info("onReceivedError: " + i);
                super.onReceivedError(webView, i, s, s1);
            }
        });


    }

    public void loadUrl(String url){
        if(mWebView == null){
            init(mActivity);
        }
        this.mWebView.clearCache(true);
        mWebView.loadUrl(url);
    }

}
