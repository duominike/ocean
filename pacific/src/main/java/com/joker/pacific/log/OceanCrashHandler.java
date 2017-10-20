package com.joker.pacific.log;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 17-10-20.
 */

public class OceanCrashHandler implements Thread.UncaughtExceptionHandler{
    private Thread.UncaughtExceptionHandler mdefaultCrashHandler;
    private Context mContext;
    private Logger mLogger = Logger.getLogger(OceanCrashHandler.class);
    private List<OnCrashListener> mOnCrashListeners = new ArrayList<>();
    public interface OnCrashListener{
        void onCrash();
    }

    public void registerCrashListener(OnCrashListener listener){
        mOnCrashListeners.add(listener);
    }

    public  OceanCrashHandler(Context context){
        mdefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前实例设置为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.mContext = context;
        // todo 初始化崩溃上报信息　手机deviceId app Version等　崩溃文件路径等等
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        mLogger.info("uncaughtException");
        dumpException(e);
        e.printStackTrace();
        mLogger.exception(e);

        for (OnCrashListener listener: mOnCrashListeners){
            listener.onCrash();
        }

        if(mdefaultCrashHandler != null){ // 交给系统结束我们的程序
            mdefaultCrashHandler.uncaughtException(t, e);
        }else{
            // todo 自己结束应用程序, 并在结束前可以执行一些清理工作
        }
    }

    private void dumpException(Throwable ex){
        // todo 将异常信息写入文件　等待合适的时机上报
    }
}
