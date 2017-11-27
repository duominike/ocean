package com.joker.ocean.testview;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.joker.multimedia.videorecorder.OceanGLSurfaceView;
import com.joker.ocean.R;
import com.joker.pacific.component.BaseFragmentActivity;

/**
 * Created by joker on 17-10-25.
 */

public class TempActivity extends BaseFragmentActivity {
    private OceanGLSurfaceView mGLSurfaceView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        mGLSurfaceView = (OceanGLSurfaceView)findViewById(R.id.tv_customview);
        if(!supportGLEs20(this)){
            Toast.makeText(this, "不支持GLES", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    private boolean supportGLEs20(Activity activity){
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(
                Context.ACTIVITY_SERVICE);
        return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
    }
}
