package com.joker.ocean.testview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.joker.ocean.R;
import com.joker.ocean.adapter.MomentsAdapter;
import com.joker.pacific.component.BaseFragmentActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Created by joker on 2018/3/29.
 */

public class TestSmartRefreshActivity extends BaseFragmentActivity{
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView mRecycleView;
    private MomentsAdapter momentsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_refresh);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartRefreshLayout);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        momentsAdapter = new MomentsAdapter(this);
        mRecycleView.setAdapter(momentsAdapter);
    }
}
