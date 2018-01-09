package com.joker.pacific.imageload.producer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by joker on 18-1-9.
 */

public class ImageRequest {
    public String url;
    public int width;
    public int height;
    public BitmapFactory.Options mOptions;
    // todo 增加回调监听
    public OnLoadImageListener mImageListener;
    public interface OnLoadImageListener{
        void onPreLoad();
        void onLoadBitmap(Bitmap bitmap);
        void onError();
    }
}
