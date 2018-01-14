package com.joker.ocean;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joker.ocean.adapter.MomentsAdapter;
import com.joker.ocean.base.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView mRecycleView;
    private MomentsAdapter momentsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartRefreshLayout);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        momentsAdapter = new MomentsAdapter(this);
        mRecycleView.setAdapter(momentsAdapter);
    }

    @Override
    public void onClick(View v) {
    }
}
