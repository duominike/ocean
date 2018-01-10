package com.joker.pacific.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.joker.pacific.imageload.producer.ImageLoadController;

public class ImageLoader {
    private ImageLoaderConfig.Builder mBuilder;
    private ImageLoadController mImageLoadController;

    private ImageLoader() {
        mBuilder = new ImageLoaderConfig.Builder();
        mBuilder.memCacheSize(ImageLoaderConfig.DEFAULT_MEM_CACHESIZE);
        mBuilder.diskCacheSize(ImageLoaderConfig.DEFAULT_DISK_CACHE_SIZE);
    }

    private static class Holder {
        private static final ImageLoader sInstance = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return Holder.sInstance;
    }

    public static void init(Context context) {
        Holder.sInstance.mBuilder.diskCachePath(Environment.getExternalStorageDirectory() + "/ocean/ImageCache");
        Holder.sInstance.mBuilder.targetDensity(context.getResources().getDisplayMetrics().densityDpi);
        ImageLoaderConfig imageLoaderConfig = Holder.sInstance.mBuilder.build();
        init(context, imageLoaderConfig);

    }

    public static void init(Context context, ImageLoaderConfig config) {

        Holder.sInstance.mBuilder.diskCacheSize(config.getDiskCacheSize() == 0 ?
                ImageLoaderConfig.DEFAULT_DISK_CACHE_SIZE : config.getDiskCacheSize());
        Holder.sInstance.mBuilder.memCacheSize(config.getMemCacheSize() == 0 ?
                ImageLoaderConfig.DEFAULT_MEM_CACHESIZE : config.getMemCacheSize());
        Holder.sInstance.mBuilder.targetDensity(config.getTargetDensity() == 0 ?
                context.getResources().getDisplayMetrics().densityDpi : config.getTargetDensity());
        if(!TextUtils.isEmpty(config.getDiskCachePath())){
            Holder.sInstance.mBuilder.diskCachePath(config.getDiskCachePath());
        }
        ImageLoaderConfig fixedConfig = Holder.sInstance.mBuilder.build();
        Holder.sInstance.initByConfig(context, fixedConfig);
    }

    private void initByConfig(Context context, ImageLoaderConfig loaderConfig) {
        mImageLoadController = new ImageLoadController(context, loaderConfig);
    }

    public void setUrl(ImageRequest imageRequest) {
        if (mImageLoadController == null) {
            throw new IllegalStateException("ImageLoader not init finish!");
        }
        mImageLoadController.loadImage(imageRequest);
    }


    /**
     * Created by joker on 18-1-9.
     */

    public static class ImageRequest {
        public String url;
        public int width;
        public int height;
        public BitmapFactory.Options mOptions;
        public Bitmap bitmap;
        // todo 增加回调监听
        public OnLoadImageListener mImageListener;

        public interface OnLoadImageListener {
            void onPreLoad();

            void onLoadBitmap(Bitmap bitmap);

            void onError();
        }
    }
}