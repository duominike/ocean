package com.joker.pacific.imageload.producer;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;

import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * Created by joker on 18-1-9.
 */

public class MemCacheProducer implements Producer<Bitmap> {
    private Producer mProducer;
    private int memSize;
    private Executor mExecutor;
    private LruCache<String, Bitmap> mLruCache;
    public MemCacheProducer(int memSize, Executor executor) {
        this.memSize = (int)(Math.round(memSize * Runtime.getRuntime().maxMemory() * 0.01 / 1024));
        this.mExecutor = executor;
        mLruCache = new LruCache<String, Bitmap>(memSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;

            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
    }

    @Override
    public void setProducer(Producer producer) {
        this.mProducer = producer;
    }

    @Override
    public Observable<Bitmap> produce(final ImageRequest imgRequest) {
        Observable<Bitmap> result = null;
        Bitmap bitmap = null;
        if(mLruCache != null){
            bitmap = mLruCache.get(imgRequest.url);
        }

        if(bitmap == null){
            result = (Observable<Bitmap>)mProducer.produce(imgRequest);
            result.filter(new Predicate<Bitmap>() {
                @Override
                public boolean test(@NonNull Bitmap bitmap) throws Exception {
                    if(!TextUtils.isEmpty(imgRequest.url) && bitmap != null){
                        mLruCache.put(imgRequest.url, bitmap);
                        return true;
                    }
                    return false;
                }
            }).observeOn(AndroidSchedulers.mainThread());
        }else{
            result = Observable.just(bitmap);
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private int getBitmapSize(Bitmap bitmap){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            return bitmap.getAllocationByteCount();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){
            return bitmap.getByteCount();
        }

        return bitmap.getByteCount() * bitmap.getHeight();
    }

    public void clearCache(){
        if(mLruCache != null){
            mLruCache.evictAll();
        }
    }
}
