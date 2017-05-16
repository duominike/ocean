package com.joker.pacific.monitor;

import android.os.Looper;

import com.joker.pacific.module.CpuInfo;

import java.io.File;

/**
 * Created by joker on 17-5-14.
 */

public class UiPerfMonitor implements UiPerfMonitorConfig, LogPrinterListener{
    private static UiPerfMonitor sInstance;
    private UIMonitorPrinter mPrinter;
    private CpuLogWriteThread mThread;
    private CpuInfoSampler mSampler;
    private static  String logFilePath;
    private static  String logFileName;

    private static void inStall(String logFilePathSet, String fileNameSet){
        logFilePath = logFilePathSet;
        File logpath = new File(logFilePath);
        if(!logpath.exists()){
            logpath.mkdir();
        }
        logFileName = fileNameSet;
    }

    public synchronized static UiPerfMonitor getInstance(){
        if(null == sInstance){
            sInstance = new UiPerfMonitor(logFilePath, logFileName);
        }
        return sInstance;
    }

    private UiPerfMonitor(String logFilePath, String fileName){
        mSampler = new CpuInfoSampler();
        mThread = new CpuLogWriteThread(logFilePath, fileName);
        mPrinter = new UIMonitorPrinter(this);
    }

    public void startMonitor(){
        Looper.getMainLooper().setMessageLogging(mPrinter);
    }

    public void stopMonitor(){
        Looper.getMainLooper().setMessageLogging(null);
        mSampler.stop();
    }

    @Override
    public void onStartLoop() {
        mSampler.start();
    }

    @Override
    public void onEndLoop(long startTime, long endTime, String logInfo, int level) {
        mSampler.stop();
        switch (level){
            case UiPerfMonitorConfig.TIME_WARNING_LEVEL_1:
                if(mSampler.getCpuInfos().size() > 0){
                    StringBuffer sb = new StringBuffer("startTime:");
                    sb.append(startTime);
                    sb.append("endTime:");
                    sb.append(endTime);
                    sb.append("handleTime");
                    sb.append(endTime- startTime);
                    for(CpuInfo info: mSampler.getCpuInfos()){
                        sb.append("\r\n");
                        sb.append(info.toString());
                    }
                    mThread.saveLog(sb.toString());

                }
                break;
            case UiPerfMonitorConfig.TIME_WARNING_LEVEL_2:
                break;
        }
    }
}
