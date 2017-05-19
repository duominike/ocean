package com.joker.pacific.log;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by joker on 17-5-18.
 */

public class Logger {
    private String tag;
    private Logger(String tag){
        this.tag = tag;
    }

    public static  Logger getLogger(Class logClass){
        return new Logger(logClass.getSimpleName());
    }

    public void info(String logInfo){
        Log.i(tag, logInfo);
    }

    public void debug(String logInfo){
        Log.i(tag, logInfo);
    }

    public void error(String logInfo){
        Log.i(tag, logInfo);
    }

    public void warn(String logInfo){
        Log.i(tag, logInfo);
    }

    public void exception(Throwable e){
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        writer.print(e);
        Log.i(tag, stringWriter.toString());
    }

}
