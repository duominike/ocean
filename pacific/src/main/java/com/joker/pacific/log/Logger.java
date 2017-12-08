package com.joker.pacific.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by joker on 17-5-18.
 */

public class Logger {
    private com.orhanobut.logger.Logger mLogger;
    private String tag;
    private Logger(String tag){
        this.tag = tag;
    }

    public static  Logger getLogger(Class logClass){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount()         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(logClass.getSimpleName())   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        return new Logger(logClass.getSimpleName());
    }

    public void info(String logInfo){
        com.orhanobut.logger.Logger.i(logInfo);
    }

    public void debug(String logInfo){
        com.orhanobut.logger.Logger.d(logInfo);
    }

    public void error(String logInfo){
        com.orhanobut.logger.Logger.e(logInfo);
    }

    public void warn(String logInfo){
        com.orhanobut.logger.Logger.w(tag, logInfo);
    }

    public void exception(Throwable e){
        com.orhanobut.logger.Logger.e(e, "exception");
    }

}
