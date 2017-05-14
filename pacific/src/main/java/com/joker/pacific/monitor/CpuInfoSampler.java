package com.joker.pacific.monitor;

import com.joker.pacific.logger.Logger;
import com.joker.pacific.module.CpuInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 17-5-14.
 */

public class CpuInfoSampler extends BaseSampler {
    private Logger logger = Logger.getLogger(CpuInfoSampler.class);
    private ArrayList<CpuInfo> mCpuInfoList = new ArrayList<CpuInfo>();
    private int mPid = -1;
    private long mUserPre = 0;
    private long mSystemPre = 0;
    private long mIdlePre = 0;
    private long mIoWaitPre = 0;
    private long mTotalPre = 0;
    private long mAppCpuTimePre = 0;
    @Override
    public void start() {
        super.start();
        mUserPre = 0;
        mSystemPre = 0;
        mIdlePre = 0;
        mIoWaitPre = 0;
        mTotalPre = 0;
        mAppCpuTimePre = 0;
        mCpuInfoList.clear();
    }

    public List<CpuInfo> getCpuInfos(){
        return mCpuInfoList;
    }
    @Override
    void doSample() {
        BufferedReader cpuReader = null;
        BufferedReader pidReader = null;
        try{
            cpuReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1024);
            String cpuRate = cpuReader.readLine();
            if(null == cpuRate){
                cpuRate = "";
            }
            if(mPid < 0){
                mPid = android.os.Process.myPid();
            }
            pidReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + mPid
                +"/stat/")), 1024);
            String pidCpuRate = pidReader.readLine();
            if(null == pidCpuRate){
                pidCpuRate = "";
            }
            parseCpuRate(cpuRate, pidCpuRate);
        }catch (Throwable ex){
            logger.exception(ex);
        }finally {
            try{
                if(null != cpuReader){
                    cpuReader.close();
                }
                if(null != pidReader){
                    pidReader.close();
                }
            }catch (IOException e){
                logger.exception(new Throwable(e));
            }
        }

    }

    private void parseCpuRate(String cpuRate, String pidCpuRate){
        String [] cpuInfoArray = cpuRate.split(" ");
        if(cpuInfoArray.length < 9){
            return;
        }
        long user_time = Long.parseLong(cpuInfoArray[2]);
        long nice_time = Long.parseLong(cpuInfoArray[3]);
        long system_time = Long.parseLong(cpuInfoArray[4]);
        long idle_time = Long.parseLong(cpuInfoArray[5]);
        long iowait_time = Long.parseLong(cpuInfoArray[6]);
        long total_time = user_time + nice_time + system_time + idle_time + iowait_time +
                Long.parseLong(cpuInfoArray[7]) + Long.parseLong(cpuInfoArray[8]);

        String [] pidCpuInfos = cpuRate.split(" ");
        if(pidCpuInfos.length < 17){
            return;
        }
        long appCpu_time = Long.parseLong(pidCpuInfos[13]) + Long.parseLong(pidCpuInfos[14]) +
                Long.parseLong(pidCpuInfos[15]) + Long.parseLong(pidCpuInfos[16]);
        if(mAppCpuTimePre > 0){
            CpuInfo mci = new CpuInfo();
            long idleTime = idle_time - mIdlePre;
            long totalTime = total_time - mTotalPre;
            mci.mCpuRate = (total_time - idle_time) * 100L / total_time;
            mci.mAppRate = (appCpu_time - mAppCpuTimePre) * 100L / total_time;
            mci.mSystemRate = (system_time - mSystemPre) * 100L / total_time;
            mci.mUserRate = (user_time - mUserPre) * 100L / total_time;
            mci.mIoWait = (iowait_time - mIoWaitPre) * 100L / total_time;
            synchronized (mCpuInfoList){
                mCpuInfoList.add(mci);
            }

            mUserPre = user_time;
            mSystemPre = system_time;
            mIdlePre = idle_time;
            mIoWaitPre = iowait_time;
            mTotalPre = total_time;
            mAppCpuTimePre = appCpu_time;

        }
//        long nice_time = Long.parseLong(pidCpuInfos[3]);
//        long system_time = Long.parseLong(pidCpuInfos[4]);
//        long idle_time = Long.parseLong(pidCpuInfos[5]);
//        long iowait_time = Long.parseLong(pidCpuInfos[6]);
//        long total = user_time + nice_time + system_time + idle_time + iowait_time +
//                Long.parseLong(pidCpuInfos[7]) + Long.parseLong(pidCpuInfos[8]);

    }
}
