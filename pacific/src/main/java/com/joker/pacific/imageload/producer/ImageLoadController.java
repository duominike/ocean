package com.joker.pacific.imageload.producer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.joker.pacific.imageload.ImageLoaderConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by joker on 18-1-9.
 */

public class ImageLoadController {
    private MemCacheProducer mMemCacheProducer;
    private DiskCacheCacheProducer mDiskCacheCacheProducer;
    private DecodeBitmapProducer mDecodeBitmapProducer;
    private NetWorkProducer mNetWorkProducer;
    private Executor executor;
    private Context mContext;

    public ImageLoadController(Context context, ImageLoaderConfig loaderConfig){
        this.mContext = context;
        this.executor = loaderConfig.getExecutors();
        if(executor == null){
            executor = Executors.newFixedThreadPool(5, new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    return new Thread("ImageLoadThread");
                }
            });
        }
        mNetWorkProducer  = new NetWorkProducer(executor);
        mDiskCacheCacheProducer = new DiskCacheCacheProducer(loaderConfig.getDiskCachePath(),
                loaderConfig.getDiskCacheSize(), executor);
        mDecodeBitmapProducer = new DecodeBitmapProducer(executor);
        mMemCacheProducer = new MemCacheProducer(loaderConfig.getMemCacheSize(), executor);

        mMemCacheProducer.setProducer(mDecodeBitmapProducer);
        mDecodeBitmapProducer.setProducer(mDiskCacheCacheProducer);
        mDiskCacheCacheProducer.setProducer(mNetWorkProducer);

    }

    public void loadImage(final ImageRequest request){
        if(request.url.startsWith("http:") || request.url.startsWith("https:")){
            mMemCacheProducer.produce(request)
            .subscribe(new Observer<Bitmap>() {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    if(request != null && request.mImageListener != null){
                        request.mImageListener.onPreLoad();
                    }
                }

                @Override
                public void onNext(@io.reactivex.annotations.NonNull Bitmap bitmap) {
                    if(request != null && request.mImageListener != null){
                        request.mImageListener.onLoadBitmap(bitmap);
                    }
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    if(request != null && request.mImageListener != null){
                        request.mImageListener.onError();
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

}
