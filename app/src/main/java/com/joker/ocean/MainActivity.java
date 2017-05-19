package com.joker.ocean;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joker.ocean.base.BaseActivity;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private TextView m_tvTestView;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_tvTestView = (TextView)findViewById(R.id.tv_customview);
        m_tvTestView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    }

}
