package com.joker.pacific.component;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

import com.joker.pacific.component.delegate.DialogFragmentDelegate;
import com.joker.pacific.component.delegate.SupportFragmentDelegate;
import com.joker.pacific.component.delegate.TransactionCommitter;
import com.joker.pacific.log.Logger;

/**
 * 可以安全的dissmiss和commit FragmentTransation的　DialogFragment
 * Created by joker on 17-10-18.
 */

public class BaseDialogFragment extends DialogFragment implements TransactionCommitter{
    private final DialogFragmentDelegate mDialogFragmentDelegate = new DialogFragmentDelegate();
    private final SupportFragmentDelegate mSupportFragmentDelegate = new SupportFragmentDelegate();
    protected Logger mLogger = Logger.getLogger(getClass());

    protected boolean safeDismiss(){
        return mDialogFragmentDelegate.safeDismiss(this);
    }

    protected boolean safeCommit(FragmentTransaction transaction){
        return mSupportFragmentDelegate.safeCommit(this, transaction);
    }


    @Override
    public void onResume() {
        super.onResume();
        mLogger.info("onResume");
        mDialogFragmentDelegate.onResumed(this);
        mSupportFragmentDelegate.onResumed();
    }

    @Override
    public boolean isCommitterResumed() {
        return isResumed();
    }
}
