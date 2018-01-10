package com.joker.pacific.imageload.scheduler;

import io.reactivex.Scheduler;

/**
 * Created by joker on 18-1-10.
 */

public interface SchedulerProvider {
    Scheduler offerNetWorkScheduler();
    Scheduler offerDecodeScheduler();
    Scheduler offerUIThreadScheduler();
    Scheduler offerDiskCacheScheduler();
}
