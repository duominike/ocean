package com.joker.pacific.component;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.joker.pacific.component.delegate.SupportFragmentDelegate;
import com.joker.pacific.component.delegate.TransactionCommitter;
import com.joker.pacific.log.Logger;

/**
 * 可以安全的commit FragmentTransation的　Fragment
 * Created by joker on 17-10-18.
 */

public class BaseFragment extends Fragment implements TransactionCommitter {
    private final SupportFragmentDelegate mSupportFragmentDelegate = new SupportFragmentDelegate();
    protected Logger mLogger = Logger.getLogger(getClass());
    @Override
    public void onResume() {
        super.onResume();
        mSupportFragmentDelegate.onResumed();
    }

    protected boolean safeCommit(FragmentTransaction transaction){
        return mSupportFragmentDelegate.safeCommit(this, transaction);
    }


    @Override
    public boolean isCommitterResumed() {
        return isResumed();
    }
}
