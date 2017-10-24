package com.joker.ocean;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joker.ocean.base.BaseActivity;
import com.joker.ocean.rxjava.rxjava2.Rxjava2Combination;
import com.joker.ocean.rxjava.rxjava2.Rxjava2FilterOperation;
import com.joker.ocean.rxjava.rxjava2.Rxjava2Test;
import com.joker.ocean.rxjava.rxjava2.Rxjava2TestFlow;
import com.joker.ocean.rxjava.rxjava2.RxjavaMapOperation;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private TextView m_tvTestView;
    private static final String TAG = "MainActivity";
    private Rxjava2Test mRxjava2Test;
    private Rxjava2FilterOperation mFilterOperation;
    private RxjavaMapOperation mRxjavaMapOperation;
    private Rxjava2Combination mRxjava2Combination;
    private Rxjava2TestFlow mRxjava2TestFlow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_tvTestView = (TextView)findViewById(R.id.tv_customview);
        m_tvTestView.setOnClickListener(this);
        mRxjava2Test = new Rxjava2Test();
        mRxjava2Test.init();
        mFilterOperation = new Rxjava2FilterOperation();
        mRxjava2TestFlow = new Rxjava2TestFlow();
        mRxjava2Combination = new Rxjava2Combination();
        mRxjavaMapOperation = new RxjavaMapOperation();
    }

    @Override
    public void onClick(View v) {
//        mRxjava2TestFlow.testError();
//        mRxjava2TestFlow.testDrop();
        mRxjava2TestFlow.testLatest();
    }


}
