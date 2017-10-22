package com.joker.pacific.component.delegate;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

/**
 * 安全的添加 或移除　fragment类
 * Created by joker on 17-10-18.
 */

public class FragmentTransactionBuilder {

    private final FragmentManager mFragmentManager;
    private final FragmentTransaction mTransaction;

    private FragmentTransactionBuilder(FragmentManager fragmentManager,
                                       FragmentTransaction transaction) {
        mFragmentManager = fragmentManager;
        mTransaction = transaction;
    }

    public static FragmentTransactionBuilder transaction(FragmentManager fragmentManager) {
        return new FragmentTransactionBuilder(fragmentManager, fragmentManager.beginTransaction());
    }

    public FragmentTransactionBuilder add(Fragment fragment, String tag)
            throws IllegalArgumentException, IllegalStateException {
        checkTag(tag);
        checkTagNotExist(mFragmentManager, tag);
        mTransaction.add(fragment, tag);
        return this;
    }

    public FragmentTransactionBuilder add(Fragment fragment, String tag, String name)
            throws IllegalArgumentException, IllegalStateException {
        checkTag(tag);
        checkTag(name);
        checkTagNotExist(mFragmentManager, tag);
        mTransaction.add(fragment, tag).addToBackStack(name);
        return this;
    }

    public FragmentTransactionBuilder add(@IdRes int id, Fragment fragment, String tag)
            throws IllegalArgumentException, IllegalStateException {
        checkId(id);
        checkTag(tag);
        checkIdNotExist(mFragmentManager, id);
        checkTagNotExist(mFragmentManager, tag);
        mTransaction.add(id, fragment, tag);
        return this;
    }

    public FragmentTransactionBuilder add(@IdRes int id, Fragment fragment, String tag, String name)
            throws IllegalArgumentException, IllegalStateException {
        checkId(id);
        checkTag(tag);
        checkTag(name);
        checkIdNotExist(mFragmentManager, id);
        checkTagNotExist(mFragmentManager, tag);
        mTransaction.add(id, fragment, tag).addToBackStack(name);
        return this;
    }

    public FragmentTransactionBuilder remove(@IdRes int id)
            throws IllegalArgumentException, IllegalStateException {
        checkId(id);
        checkIdExist(mFragmentManager, id);
        mTransaction.remove(mFragmentManager.findFragmentById(id));
        return this;
    }

    public FragmentTransactionBuilder remove(String tag)
            throws IllegalArgumentException, IllegalStateException {
        checkTag(tag);
        checkTagExist(mFragmentManager, tag);
        mTransaction.remove(mFragmentManager.findFragmentByTag(tag));
        return this;
    }

    public FragmentTransactionBuilder replace(@IdRes int id, Fragment fragment, String tag)
            throws IllegalArgumentException, IllegalStateException {
        checkId(id);
        checkTag(tag);
        checkIdExist(mFragmentManager, id);
        Fragment old = mFragmentManager.findFragmentById(id);
        if (!TextUtils.equals(old.getTag(), tag)) {
            checkTagNotExist(mFragmentManager, tag);
        }
        mTransaction.replace(id, fragment, tag);
        return this;
    }

    public FragmentTransaction build() {
        return mTransaction;
    }

    private void checkTagNotExist(FragmentManager fragmentManager, String tag)
            throws IllegalStateException {
        if (fragmentManager.findFragmentByTag(tag) != null) {
            throw new IllegalStateException("fragment with tag "
                    + tag
                    + " already exist: "
                    + fragmentManager.findFragmentByTag(tag));
        }
    }

    private void checkIdNotExist(FragmentManager fragmentManager, @IdRes int id)
            throws IllegalStateException {
        if (fragmentManager.findFragmentById(id) != null) {
            throw new IllegalStateException("fragment with id "
                    + id
                    + " already exist: "
                    + fragmentManager.findFragmentById(id));
        }
    }

    private void checkTagExist(FragmentManager fragmentManager, String tag)
            throws IllegalStateException {
        if (fragmentManager.findFragmentByTag(tag) == null) {
            throw new IllegalStateException("fragment with tag " + tag + " not exist!");
        }
    }

    private void checkIdExist(FragmentManager fragmentManager, @IdRes int id)
            throws IllegalStateException {
        if (fragmentManager.findFragmentById(id) == null) {
            throw new IllegalStateException("fragment with id " + id + " not exist!");
        }
    }

    private void checkTag(String tag) throws IllegalArgumentException {
        if (TextUtils.isEmpty(tag)) {
            throw new IllegalArgumentException("tag is empty");
        }
    }

    private void checkId(@IdRes int id) throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("id: " + id + " <= 0");
        }
    }
}
