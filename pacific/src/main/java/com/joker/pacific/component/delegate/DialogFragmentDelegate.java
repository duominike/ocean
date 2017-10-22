package com.joker.pacific.component.delegate;

import android.support.v4.app.DialogFragment;

/**
 * Created by joker on 17-10-18.
 */

public class DialogFragmentDelegate {
    private volatile boolean mPendingDismiss = false;


    public boolean safeDismiss(DialogFragment fragment){
        if(fragment.isResumed()){
            fragment.dismiss();
            return false;
        }else{
            mPendingDismiss = true;
            return true;
        }
    }

    public boolean onResumed(DialogFragment fragment){
        if(mPendingDismiss){
            fragment.dismiss();
            mPendingDismiss = true;
            return true;
        }
        return false;
    }

}
