package com.joker.pacific.imageload.producer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.joker.pacific.imageload.CloseUtil;
import com.joker.pacific.imageload.ImageLoader;
import com.joker.pacific.log.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by joker on 18-1-9.
 */

class DecodeBitmapProducer implements Producer<Bitmap> {
    private Producer<InputStream> inputProducer;
    private Logger mLogger = Logger.getLogger(DecodeBitmapProducer.class);
    private int nTargetDensity;

    public DecodeBitmapProducer(@NonNull Producer<InputStream> srcProducer, int targetDensity) {
        this.inputProducer = srcProducer;
        this.nTargetDensity = targetDensity;
    }


    @Override
    public Observable<Bitmap> produce(final ImageLoader.ImageRequest imgRequest) {
        return inputProducer.produce(imgRequest)
                .flatMap(new Function<InputStream, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(@NonNull final InputStream inputStream) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                                Bitmap bitmap = decodeBitmap(imgRequest, inputStream);
                                if (bitmap != null) {
                                    e.onNext(bitmap);
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    private Bitmap decodeBitmap(ImageLoader.ImageRequest imageRequest, InputStream inputStream) {
        mLogger.info("decodeBitmap");
        if (imageRequest.mOptions != null) {
            try {
                return BitmapFactory.decodeStream(inputStream, null, imageRequest.mOptions);
            } catch (Exception e) {
                mLogger.error("decode by inputRequest Options Error: " + Log.getStackTraceString(e));
            }
        }
        if(!(inputStream instanceof FileInputStream)){
            return null;
        }

        FileInputStream fileInputStream = (FileInputStream) inputStream;
        try {
            Field field = fileInputStream.getClass().getDeclaredField("path");
            field.setAccessible(true);
            String filePath = field.get(fileInputStream).toString();
            final BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;
            try{
                 BitmapFactory.decodeStream(fileInputStream, null, options);
            }catch (Exception e){
                return null;
            }finally {
                CloseUtil.close(fileInputStream);
            }

            if (imageRequest.width == 0 || imageRequest.height == 0) {
                options.inJustDecodeBounds = false;
                fileInputStream = new FileInputStream(filePath);
                try{
                    return  BitmapFactory.decodeStream(fileInputStream, null, options);
                }catch (Exception e){
                    return null;
                }finally {
                    CloseUtil.close(fileInputStream);
                }
            }

            options.inSampleSize = calculateInSampleSize(options, imageRequest.width, imageRequest.height);

            double xSScale = ((double) options.outWidth) / ((double) imageRequest.width);
            double ySScale = ((double) options.outHeight) / ((double) imageRequest.height);

            double startScale = xSScale > ySScale ? xSScale : ySScale;

            options.inScaled = true;
            options.inDensity = (int) (nTargetDensity * startScale);
            options.inTargetDensity = nTargetDensity;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            fileInputStream = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(fileInputStream, null, options);
        } catch (Exception e) {
            mLogger.error("decodeBitmap: " + Log.getStackTraceString(e));

        }finally {
            CloseUtil.close(inputStream);
        }
        return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
