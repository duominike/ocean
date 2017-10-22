package com.joker.pacific.component.delegate;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 17-10-18.
 */

public class SupportFragmentDelegate {
    private List<FragmentTransaction> mPendingFragmentTransactions = new ArrayList<FragmentTransaction>();

    public synchronized boolean safeCommit(@NonNull TransactionCommitter committer,
                                           FragmentTransaction transaction) {
        if (committer.isCommitterResumed()) {
            transaction.commit();
            return false;
        } else {
            mPendingFragmentTransactions.add(transaction);
            return true;
        }
    }

    public synchronized boolean onResumed(){
        if(!mPendingFragmentTransactions.isEmpty()){
            for(FragmentTransaction transaction: mPendingFragmentTransactions){
                transaction.commit();
            }
            mPendingFragmentTransactions.clear();
            return true;
        }
        return false;
    }
}
