package com.joker.ocean.log;

import android.util.Log;

/**
 * Created by joker on 16-12-31.
 */

public class Logger {
    private String tag;
    public Logger(String tag){
        this.tag = tag;
    }

    public static Logger getLogger(Class classz){
        return new Logger(classz.getSimpleName());
    }

    public void i(Object obj){
        Log.i(tag, obj.toString());
    }

    public void v(Object obj){
        Log.v(tag, obj.toString());
    }

    public void d(Object obj){
        Log.d(tag, obj.toString());
    }

    public void w(Object obj){
        Log.w(tag, obj.toString());
    }

    public void e(Object obj){
        Log.e(tag, obj.toString());
    }
}
