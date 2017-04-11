package com.joker.ocean.tvdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.joker.ocean.R;
import com.joker.ocean.base.BaseActivity;
import com.joker.ocean.tvdemo.model.DemoModle;
import com.joker.ocean.tvdemo.model.FocusItemModle;
import com.joker.ocean.tvdemo.view.FocusAdapter;
import com.joker.ocean.tvdemo.view.FocusView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 17-3-26.
 */

public class TVDemoActivity extends BaseActivity implements FocusView.OnItemClickListener<FocusItemModle<DemoModle>> {



    @SuppressWarnings("rawtypes")
    private List<DemoModle> mTvLists =new ArrayList<DemoModle>();


    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian_tv_ui);
        FocusView view = (FocusView) findViewById(R.id.focus_ui);
        view.setOnItemClickListener(this);
        view.setBackgroundColor(getResources().getColor(R.color.theme_color));
        view.setGap(5);
        view.setVisibleItems(6, 5);
        view.setOrientation(FocusView.OrientationType.Horizontal);
        view.setAnimation(R.anim.scale_small, R.anim.scale_big);
        getData();
        FocusAdapter adapter = new FocusAdapter(mTvLists, getBaseContext());
        view.setAdapter(adapter);


    }


    /**
     * initData
     */
    private void getData() {
        for (int i = 0; i < 30; i++) {
            mTvLists.add(new DemoModle(R.drawable.ic_launcher, "text" + i ));
        }

    }

    @Override
    public void onItemClick(FocusView mFocusView, View focusView,
                            FocusItemModle<DemoModle> focusItem, int Postion, int row, int col,
                            long id) {
        Toast.makeText(this," row: "+ row + ", col: " + col,Toast.LENGTH_SHORT).show();

    }

}