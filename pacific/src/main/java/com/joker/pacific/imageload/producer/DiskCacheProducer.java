package com.joker.pacific.imageload.producer;

import android.util.Log;

import com.joker.pacific.imageload.CloseUtil;
import com.joker.pacific.imageload.ImageLoader;
import com.joker.pacific.imageload.cache.DiskCache;
import com.joker.pacific.log.Logger;

import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Created by joker on 18-1-9.
 */

class DiskCacheProducer implements Producer<InputStream> {
    private Producer<InputStream> inputProducer;
    private DiskCache mDiskCache;
    private Logger mLogger = Logger.getLogger(DiskCacheProducer.class);

    public DiskCacheProducer(Producer<InputStream> producer, DiskCache diskCache) {
        this.mDiskCache = diskCache;
        this.inputProducer = producer;
    }


    @Override
    public Observable<InputStream> produce(final ImageLoader.ImageRequest imgRequest) {
        Observable<InputStream> diskCache = Observable.create(new ObservableOnSubscribe<InputStream>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<InputStream> e) throws Exception {
                InputStream inputStream;
                try {
                    inputStream = mDiskCache.get(imgRequest.url);
                    if (inputStream != null) {
                        try{
                            e.onNext(inputStream);
                        }catch (Exception exception){
                            mLogger.error(Log.getStackTraceString(exception));
                        }finally {
                            CloseUtil.close(inputStream);
                        }
                    } else {
                        e.onComplete();
                    }
                } catch (Exception exception) {
                    mLogger.error("DiskCache read Error: " + Log.getStackTraceString(exception));
                    e.onComplete();
                }
            }
        });
        Observable<InputStream> writeCache = inputProducer.produce(imgRequest)
                .flatMap(new Function<InputStream, ObservableSource<InputStream>>() {
                    @Override
                    public ObservableSource<InputStream> apply(@NonNull final InputStream inputStream) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<InputStream>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<InputStream> e) throws Exception {
                                mDiskCache.putCache(imgRequest.url, inputStream);
                                InputStream cachedStream = mDiskCache.get(imgRequest.url);
                                if(cachedStream != null){
                                    e.onNext(cachedStream);
                                }
                                e.onComplete();
                            }
                        });
                    }
                });

        return diskCache.concatWith(writeCache).firstElement().toObservable();

    }


}
