package com.joker.pacific.monitor;

import android.os.Handler;
import android.os.HandlerThread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;

/**
 * Created by joker on 17-5-14.
 */

public class CpuLogWriteThread implements UiPerfMonitorConfig {
    private Handler mWriterHandler = null;
    private final Object FILE_LOCK = new Object();
    private final SimpleDateFormat FILE_NAME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
    private String filePath;
    private String fileName;
    public CpuLogWriteThread(String filePath, String fileName){
        this.fileName = fileName;
        this.filePath = filePath;
    }
    public void saveLog(final String logInfo){
        synchronized (FILE_LOCK){
            saveLogToLocal(logInfo);
        }
    }

    private void saveLogToLocal(String info){
        long time = System.currentTimeMillis();
        File logFIle = new File(filePath + "/"+ fileName +"-" + FILE_NAME_FORMATTER.format(time) +".log");
        StringBuffer stringBuffer = new StringBuffer("/***********************************************/\r\n");
        stringBuffer.append(TIME_FORMATTER.format(time));
        stringBuffer.append("\r\n/*******************************************************/\r\n");
        stringBuffer.append(info+"\r\n");
        if(logFIle.exists()){
            writeLogToSampeFile(logFIle.getPath(), stringBuffer.toString());
        }else{
            BufferedWriter writer = null;
            try{
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(logFIle.getPath(), true),
                        "UTF-8");
                writer = new BufferedWriter(out);
                writer.write(stringBuffer.toString());
                writer.flush();
                writer.close();
                writer = null;
            }catch (Throwable ex){

            }finally {
                try{
                    if(null != writer){
                        writer.close();
                        writer = null;
                    }
                }catch (Exception e){

                }
            }
        }
    }

    private void writeLogToSampeFile(String filePath, String log){
        RandomAccessFile file = null;
        try{
            file = new RandomAccessFile(filePath, "rw");
            long  fileLength = file.length();
            file.seek(fileLength);
            file.writeBytes(log);
        }catch (IOException e){
            if(file != null){
                try{
                    file.close();
                    file = null;
                }catch (IOException ex){

                }
            }
        }
    }

    public void sendToServer(){
        getControlHandler().post(new Runnable() {
            @Override
            public void run() {
                // todo 上传服务器
            }
        });
    }

    private Handler getControlHandler(){
        if(null == mWriterHandler){
           HandlerThread handlerThread = new HandlerThread(CpuLogWriteThread.class.getSimpleName());
            handlerThread.start();
            mWriterHandler = new Handler(handlerThread.getLooper());
        }
        return mWriterHandler;
    }



}
