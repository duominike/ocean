package com.joker.ocean;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.joker.pacific.component.delegate.ResourceDelegate;
import com.joker.pacific.log.OceanCrashHandler;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by joker on 17-10-18.
 */

public class OceanApplication extends MultiDexApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        Fresco.initialize(this);
        LeakCanary.install(this);
        ResourceDelegate.install(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(new OceanCrashHandler(getApplicationContext()));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
