package com.joker.ocean.testview;

import android.databinding.DataBindingUtil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.joker.ocean.R;
import com.joker.ocean.adapter.OceanDataBindingAdapter;
import com.joker.ocean.databinding.ActivityDataBindingBinding;
import com.joker.ocean.testview.testmodel.App;
import com.joker.ocean.testview.testmodel.OceanHandlers;
import com.joker.pacific.component.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

/**
 * Created by joker on 2018/3/29.
 */

public class TestDataBindingActivity extends BaseFragmentActivity{
    private App app = new App("Ocean");
    private ActivityDataBindingBinding mbinding;
    private static int index = 0;
    private RecyclerView mRecycleView;
    private OceanDataBindingAdapter madapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        mbinding.setApp(app);
        mbinding.setHandlers(new OceanHandlers());
        mRecycleView = (RecyclerView)findViewById(R.id.recycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        madapter = new OceanDataBindingAdapter(this);
        mRecycleView.setAdapter(madapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.range(0, 50)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        madapter.getItems().add(new App("ocean-" + integer));
                    }
                });
//        Observable.interval(300, TimeUnit.MILLISECONDS)
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        app.setName("ocean"+ (index++));
//                    }
//                });
    }
}
