package com.joker.pacific.monitor;

import android.util.Printer;

/**
 * Created by joker on 17-5-14.
 */

public class UIMonitorPrinter implements Printer{
    private final String TAG = UIMonitorPrinter.class.getSimpleName();
    private LogPrinterListener mLogPrinterListener = null;
    private long startTime = 0;
    public UIMonitorPrinter(LogPrinterListener listener){
        this.mLogPrinterListener = listener;
    }

    @Override
    public void println(String s) {
        if(startTime <= 0){
            startTime = System.currentTimeMillis();
            mLogPrinterListener.onStartLoop();
        }else{
            long endTime = System.currentTimeMillis();
            long time = System.currentTimeMillis() - startTime;
            executeTime(startTime, endTime, s, time);
            startTime = 0;
        }
    }

    private void executeTime(long startTime, long endTime, String s, long time){
        int level = 0;
        if(time > UiPerfMonitorConfig.TIME_WARNING_LEVEL_2){
            level = UiPerfMonitorConfig.TIME_WARNING_LEVEL_2;
        }else if(time > UiPerfMonitorConfig.TIME_WARNING_LEVEL_1){
            level = UiPerfMonitorConfig.TIME_WARNING_LEVEL_1;
        }
        mLogPrinterListener.onEndLoop(startTime, endTime, s, level);
    }
}
