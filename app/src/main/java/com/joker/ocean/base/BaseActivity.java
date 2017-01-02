package com.joker.ocean.base;

import android.support.v4.app.FragmentActivity;

import com.joker.ocean.log.Logger;

/**
 * Created by joker on 16-12-31.
 */

public class BaseActivity extends FragmentActivity{
    protected Logger logger = new Logger(this.getClass().getSimpleName());
}
