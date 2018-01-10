package com.joker.pacific.imageload.scheduler;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 18-1-10.
 */

public class SchedulerProviderImpl implements SchedulerProvider {
    private Scheduler netWorkScheduler;
//    private Scheduler decodeScheduler;
//    private Scheduler diskScheduler;
    public SchedulerProviderImpl(){
        netWorkScheduler = Schedulers.from(Executors.newFixedThreadPool(5));
//        decodeScheduler = Schedulers.from(Executors.newFixedThreadPool(2));
//        diskScheduler = Schedulers.from(Executors.newFixedThreadPool(2));
    }
    @Override
    public Scheduler offerNetWorkScheduler() {
        return netWorkScheduler;
    }

    @Override
    public Scheduler offerDecodeScheduler() {
        return netWorkScheduler;
    }

    @Override
    public Scheduler offerUIThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler offerDiskCacheScheduler() {
        return netWorkScheduler;
    }
}
