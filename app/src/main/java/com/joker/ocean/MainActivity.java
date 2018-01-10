package com.joker.ocean;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.joker.ocean.base.BaseActivity;
import com.joker.ocean.selfview.AutoLoadImageView;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private AutoLoadImageView mAutoLoadImageView;
    private String[] urls = {
            "http://upcdn.mpres.51vv.com/image/50d914d494e488e767402cf2c22132f0.jpg",
            "http://upcdn.mpres.51vv.com/image/bc06a7a3cfd40163f8f396c0fec5f294.png",
            "http://upcdn.mpres.51vv.com/image/3331d969cf46f40903d3d795ec0de662.jpg",
            "http://upcdn.mpres.51vv.com/image/bc06a7a3cfd40163f8f396c0fec5f294.png",
    };
    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoLoadImageView = (AutoLoadImageView) findViewById(R.id.test);
        mAutoLoadImageView.setUrl(urls[0]);

        mAutoLoadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoLoadImageView.setUrl(urls[(++index) % 4]);
            }
        });
    }

    @Override
    public void onClick(View v) {
//        startActivity(new Intent(this, TestTBSWebViewActivity.class));
    }
}
