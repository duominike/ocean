package com.joker.pacific.imageload;

import android.content.Context;

import com.joker.pacific.imageload.producer.ImageLoadController;
import com.joker.pacific.util.FilePathHelper;

public class ImageLoader {
    private ImageLoaderConfig.Builder mBuilder;
    private static final String defaultDir = "/ImageCache";
    private ImageLoadController mImageLoadController;
    private ImageLoader() {
        mBuilder = new ImageLoaderConfig.Builder();
        mBuilder.diskCacheSize(ImageLoaderConfig.DEFAULT_DISK_CACHE_SIZE);
        mBuilder.diskCacheSize(ImageLoaderConfig.DEFAULT_MEM_CACHESIZE);
    }

    private static class Holder {
        private static final ImageLoader sInstance = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return Holder.sInstance;
    }

    public static void init(Context context) {
        Holder.sInstance.mBuilder.diskCachePath(FilePathHelper.getSDCardDataFolder(defaultDir, null));
        ImageLoaderConfig imageLoaderConfig = Holder.sInstance.mBuilder.build();

    }

    public static void init(Context context, ImageLoaderConfig config) {
        Holder.sInstance.initByConfig(context, config);
    }

    private void initByConfig(Context context, ImageLoaderConfig loaderConfig){
        mImageLoadController = new ImageLoadController(context, loaderConfig);
    }





}