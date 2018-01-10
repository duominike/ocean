package com.joker.pacific.imageload.cache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

/**
 * Created by joker on 18-1-10.
 */

public class MemCache {
    private LruCache<String, Bitmap> mLruCache;
    private final int memSize;

    public MemCache(int memSizePercent) {
        this.memSize = (int) (Math.round(memSizePercent * Runtime.getRuntime().
                maxMemory() * 0.01 / 1024));

        mLruCache = new LruCache<String, Bitmap>(memSize) {
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

    public Bitmap get(String url){
        return mLruCache.get(url);
    }

    public void put(String url, Bitmap bitmap){
        mLruCache.put(url, bitmap);
    }

    public void clearCache() {
        if (mLruCache != null) {
            mLruCache.evictAll();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }

        return bitmap.getByteCount() * bitmap.getHeight();
    }
}

