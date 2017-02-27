package com.joker.ocean;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joker.ocean.base.BaseActivity;
import com.joker.ocean.testview.TestJsbridgeActivity;
import com.joker.ocean.testview.TestVerticalSeekbarActiivty;
import com.joker.ocean.testview.TextCommentListActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private TextView m_tvTestView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setUp();
    }

    private void initView(){
        m_tvTestView = (TextView) findViewById(R.id.tv_customview);
    }

    private void setUp(){
        m_tvTestView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.tv_customview:
                intent = new Intent(MainActivity.this, TextCommentListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
