package com.joker.pacific.imageload.producer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.joker.pacific.imageload.ImageLoader;
import com.joker.pacific.imageload.ImageLoaderConfig;
import com.joker.pacific.log.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by joker on 18-1-9.
 */

public class ImageLoadController {
    private Context mContext;
    private Logger mLogger = Logger.getLogger(ImageLoadController.class);
    private ImageProducerFactory producerFactory;

    public ImageLoadController(Context context, ImageLoaderConfig loaderConfig) {
        producerFactory = new ImageProducerFactory(loaderConfig);
        this.mContext = context;
    }

    public void loadImage(final ImageLoader.ImageRequest request) {
        if (request.url.startsWith("http:") || request.url.startsWith("https:")) {
            producerFactory.getProducerSequeue().produce(request)
                    .subscribe(new Observer<Bitmap>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                            mLogger.info("onSubscribe");
                            if (request != null && request.mImageListener != null) {
                                request.mImageListener.onPreLoad();
                            }
                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull Bitmap bitmap) {
                            mLogger.info("onNext:");
                            if (request != null && request.mImageListener != null) {
                                request.mImageListener.onLoadBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            mLogger.error("onError: " + Log.getStackTraceString(e));
                            if (request != null && request.mImageListener != null) {
                                request.mImageListener.onError();
                            }
                        }

                        @Override
                        public void onComplete() {
                            mLogger.info("onComplete");
                        }
                    });
        }
    }

}
