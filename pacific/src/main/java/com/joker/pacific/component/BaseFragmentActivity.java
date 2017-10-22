package com.joker.pacific.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.joker.pacific.component.delegate.SupportFragmentDelegate;
import com.joker.pacific.component.delegate.TransactionCommitter;
import com.joker.pacific.log.Logger;

/**
 * 可以安全的commit FragmentTransation的　BaseFragmentActivity
 * Created by joker on 17-10-18.
 */

public class BaseFragmentActivity extends FragmentActivity implements TransactionCommitter{
    private final SupportFragmentDelegate mSupportFragmentDelegate = new SupportFragmentDelegate();
    private volatile boolean mIsResumed = false;
    protected Logger mLogger = Logger.getLogger(getClass());

    protected boolean safeCommit(FragmentTransaction transaction){
        return mSupportFragmentDelegate.safeCommit(this, transaction);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLogger.info("onCreate");
        mIsResumed = true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mLogger.info("onResumeFragments");
        mIsResumed = true;
        mSupportFragmentDelegate.onResumed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mLogger.info("onPause");
        mIsResumed = false;
    }

    @Override
    public boolean isCommitterResumed() {
        return mIsResumed;
    }
}
