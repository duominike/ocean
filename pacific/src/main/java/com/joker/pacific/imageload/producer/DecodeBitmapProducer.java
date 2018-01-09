package com.joker.pacific.imageload.producer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.joker.pacific.imageload.CloseUtil;

import java.io.InputStream;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 18-1-9.
 */

public class DecodeBitmapProducer implements Producer<Bitmap> {
    private Producer mProducer;
    private Executor mExecutor;

    public DecodeBitmapProducer(Executor executor) {
        this.mExecutor = executor;
    }

    @Override
    public void setProducer(Producer producer) {
        this.mProducer = producer;
    }


    @Override
    public Observable<Bitmap> produce(final ImageRequest imgRequest) {
        if(mProducer == null){
            return null;
        }
        return ((Observable<InputStream>) mProducer.produce(imgRequest))
                .map(new Function<InputStream, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull InputStream inputStream) throws Exception {
                        return decodeBitMap(imgRequest, inputStream);
                    }
                }).subscribeOn(Schedulers.from(mExecutor));
    }

    private Bitmap decodeBitMap(ImageRequest imageRequest, InputStream inputStream){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = true;
        if(inputStream != null){
            BitmapFactory.decodeStream(inputStream, null , options);
            boolean scaledByWidth = (options.outWidth / imageRequest.width) < (
                    options.outHeight / imageRequest.height);

            options.inScaled = true;
            options.inDensity = scaledByWidth ? options.outWidth : options.outHeight;
            options.inTargetDensity = scaledByWidth ? imageRequest.width : imageRequest.height;
            options.inJustDecodeBounds = false;
            try{
                return BitmapFactory.decodeStream(inputStream, null, options);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                CloseUtil.close(inputStream);
            }
        }
        return null;
    }
}
