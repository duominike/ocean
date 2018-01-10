package com.joker.pacific.log;

import android.util.Log;

/**
 * Created by joker on 17-5-18.
 */

public class Logger {
    private String tag;

    private Logger(String tag) {
        this.tag = tag;
    }

    public static Logger getLogger(Class logClass) {
       return new Logger(logClass.getSimpleName());
    }

    public void info(String logInfo) {
        Log.i(tag, logInfo);
    }

    public void debug(String logInfo) {
        Log.d(tag,logInfo);
    }

    public void error(String logInfo) {
        Log.e(tag,logInfo);
    }

    public void warn(String logInfo) {
        Log.w(tag, logInfo);
    }

    public void exception(Throwable e) {
        Log.e(tag, Log.getStackTraceString(e));
    }

    public void error(String format, Object... args) {
        Log.e(tag, String.format(format, args));
    }

    public void info(String format, Object... args) {
        Log.i(tag, String.format(format, args));
    }

    public void debug(String format, Object... args) {
        Log.d(tag, String.format(format, args));
    }

    public void warn(String format, Object... args) {
        Log.w(tag, String.format(format, args));
    }


}
