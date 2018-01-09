package com.joker.ocean.webutil;

import android.os.Bundle;

import com.joker.ocean.R;
import com.joker.pacific.component.BaseFragmentActivity;

/**
 * Created by joker on 17-12-5.
 */

public class TestTBSWebViewActivity extends BaseFragmentActivity{
    private TBSWebViewLayout m_tvTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_webview);
        m_tvTestView = (TBSWebViewLayout) findViewById(R.id.tv_customview);
        m_tvTestView.loadUrl("https://x5.tencent.com/tbs/sdk.html");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
