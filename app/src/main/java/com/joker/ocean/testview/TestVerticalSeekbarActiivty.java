package com.joker.ocean.testview;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.joker.ocean.R;
import com.joker.ocean.base.BaseActivity;
import com.joker.sponge.customview.VerticalSeekBar;

/**
 * Created by joker on 16-12-31.
 */

public class TestVerticalSeekbarActiivty extends BaseActivity {
    private VerticalSeekBar mVerticalSeekbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_vertical_seekbar_layout);
        mVerticalSeekbar = (VerticalSeekBar) findViewById(R.id.verticalSeekbar);
        mVerticalSeekbar.setMax(100);
        mVerticalSeekbar.setProgress(30);
        mVerticalSeekbar.setOnVerticalSeekbarChangeListener(new VerticalSeekBar.OnVerticalSeekbarChangeListener(){
            @Override
            public void onProgressChanged(VerticalSeekBar seekBar, int i, boolean b) {
                logger.info("onProgressChanged:   " + i);
            }

            @Override
            public void onStartTrackingTouch(VerticalSeekBar seekBar) {
                logger.info("onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar seekBar) {
                logger.info("onStopTrackingTouch");
            }
        });
    }
}
