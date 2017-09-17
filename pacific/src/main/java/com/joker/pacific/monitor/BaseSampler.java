package com.joker.pacific.monitor;

import android.os.Handler;
import android.os.HandlerThread;

import com.joker.pacific.log.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by joker on 17-5-14.
 */

public abstract class BaseSampler {
    private Logger logger = Logger.getLogger(BaseSampler.this.getClass());
    private Handler mControlHandler = null;
    private int intervalTime = 500;
    private AtomicBoolean mIsSampling = new AtomicBoolean(false);

    public BaseSampler(){
        logger.info("start Sampler");
    }

    public void start(){
        if(!mIsSampling.get()){
            getControlHandler().removeCallbacks(mRunnable);
            getControlHandler().post(mRunnable);
            mIsSampling.set(true);
        }
    }

    public void stop(){
        if(mIsSampling.get()){
            getControlHandler().removeCallbacks(mRunnable);
            mIsSampling.set(false);
        }
    }

    private Handler getControlHandler(){
        if(null == mControlHandler){
            HandlerThread mHT = new HandlerThread(this.getClass().getSimpleName());
            mHT.start();
            mControlHandler = new Handler(mHT.getLooper());
        }
        return mControlHandler;
    }

    abstract void doSample();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mIsSampling.get()){
                doSample();
                getControlHandler().postDelayed(mRunnable, intervalTime);
            }
        }
    };
}
