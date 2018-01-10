package com.joker.pacific.imageload.producer;

import android.graphics.Bitmap;

import com.joker.pacific.imageload.ImageLoader;
import com.joker.pacific.imageload.cache.MemCache;
import com.joker.pacific.log.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * Created by joker on 18-1-9.
 */

class MemCacheProducer implements Producer<Bitmap> {
    private Producer<Bitmap> inputProducer;
    private Logger mLogger  = Logger.getLogger(MemCacheProducer.class);
    private MemCache memCache;
    public MemCacheProducer(@NonNull Producer<Bitmap> producer,
                            @NonNull MemCache srcMemCache) {
        this.inputProducer = producer;
        this.memCache = srcMemCache;
    }

    @Override
    public Observable<Bitmap> produce(final ImageLoader.ImageRequest imgRequest) {
        final Observable<Bitmap> memObservable = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                Bitmap bitmap = memCache.get(imgRequest.url);
                if( bitmap != null){
                    mLogger.info("memCache hit");
                    e.onNext(bitmap);
                }else{
                    e.onComplete();
                }
            }
        });

        Observable<Bitmap> inputObservable = inputProducer.produce(imgRequest).filter(new Predicate<Bitmap>() {
            @Override
            public boolean test(@NonNull Bitmap bitmap) throws Exception {
                mLogger.info("cache bitmap to memCache");
                memCache.put(imgRequest.url, bitmap);
                return true;
            }
        });
        return memObservable.concatWith(inputObservable).
                firstElement().
                toObservable();
    }
}
