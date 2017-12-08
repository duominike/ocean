package com.joker.ocean.testview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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
        setContentView(R.layout.activity_test_webview);
        findViewById(R.id.tv_customview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestVerticalSeekbarActiivty.this, TempActivity.class));
            }
        });

    }
}
