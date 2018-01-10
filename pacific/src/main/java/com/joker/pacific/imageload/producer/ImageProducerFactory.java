package com.joker.pacific.imageload.producer;

import android.graphics.Bitmap;

import com.joker.pacific.imageload.ImageLoaderConfig;
import com.joker.pacific.imageload.cache.DiskCache;
import com.joker.pacific.imageload.cache.MemCache;
import com.joker.pacific.imageload.scheduler.SchedulerProvider;
import com.joker.pacific.imageload.scheduler.SchedulerProviderImpl;

/**
 * Created by joker on 18-1-10.
 */

public class ImageProducerFactory {
    private Producer<Bitmap> newWorkProducerSequence;
    private MemCache memCache;
    private DiskCache diskCache;
    private SchedulerProvider mSchedulerProvider;
    private int nTargetDenstity;
    public ImageProducerFactory(ImageLoaderConfig imageLoaderConfig) {
        this.memCache = new MemCache(imageLoaderConfig.getMemCacheSize());
        this.diskCache = new DiskCache(imageLoaderConfig.getDiskCachePath(),
                imageLoaderConfig.getDiskCacheSize());
        this.nTargetDenstity = imageLoaderConfig.getTargetDensity();
        this.mSchedulerProvider = new SchedulerProviderImpl();
    }

    public Producer<Bitmap> getProducerSequeue() {
        if (null == newWorkProducerSequence) {
            NetWorkProducer netWorkProducer = new NetWorkProducer();

            DiskCacheProducer diskCacheProducer = new DiskCacheProducer(netWorkProducer,
                    diskCache);

            DecodeBitmapProducer decodeBitmapProducer = new DecodeBitmapProducer(
                    diskCacheProducer, nTargetDenstity);

            MemCacheProducer memCacheProducer = new MemCacheProducer(decodeBitmapProducer, memCache);
            newWorkProducerSequence = new SequenceProducer(memCacheProducer,
                    mSchedulerProvider.offerNetWorkScheduler(),
                    mSchedulerProvider.offerUIThreadScheduler());
        }
        return newWorkProducerSequence;
    }
}
