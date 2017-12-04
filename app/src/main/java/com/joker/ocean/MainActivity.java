package com.joker.ocean;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.joker.ocean.base.BaseActivity;
import com.joker.ocean.selfview.OceanDraweeView;
import com.joker.ocean.testview.TestOpenSLESAudioActivity;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private OceanDraweeView m_tvTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_tvTestView = (OceanDraweeView) findViewById(R.id.tv_customview);
        m_tvTestView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, TestOpenSLESAudioActivity.class));
    }
}
