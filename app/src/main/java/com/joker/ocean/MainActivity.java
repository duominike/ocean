package com.joker.ocean;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.joker.ocean.base.BaseActivity;
import com.joker.ocean.tvdemo.TVDemoActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private TextView m_tvTestView;
    private static final String TAG = "MainActivity";
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
        m_tvTestView.setFocusable(false);
        m_tvTestView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Log.i(TAG, "has Focus");
                    m_tvTestView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.tv_customview:
                intent = new Intent(MainActivity.this, TVDemoActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "dispatchKeyEvent: ----->> " + event.getKeyCode());
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            if(m_tvTestView.hasFocus()){
                startActivity(new Intent(this, TVDemoActivity.class));
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
            Log.i(TAG, "onKeyUp: ----->> ");
            m_tvTestView.setFocusable(true);
            m_tvTestView.requestFocus();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
