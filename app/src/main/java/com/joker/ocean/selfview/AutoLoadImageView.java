package com.joker.ocean.selfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.joker.ocean.R;
import com.joker.pacific.imageload.ImageLoader;

/**
 * Created by joker on 18-1-9.
 */

public class AutoLoadImageView extends AppCompatImageView{
    private Drawable mPreLoadDrawable;
    private Drawable mErrorDrawable;
    public AutoLoadImageView(Context context) {
        this(context, null);
    }

    public AutoLoadImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLoadImageView);
        if(typedArray.hasValue(R.styleable.AutoLoadImageView_errorImage)){
            mErrorDrawable = typedArray.getDrawable(R.styleable.AutoLoadImageView_errorImage);
        }

        if(typedArray.hasValue(R.styleable.AutoLoadImageView_preLoadImage)){
            mPreLoadDrawable = typedArray.getDrawable(R.styleable.AutoLoadImageView_preLoadImage);
        }

        typedArray.recycle();
    }

    public void setUrl(String url){
        ImageLoader.ImageRequest imageRequest = new ImageLoader.ImageRequest();
        imageRequest.url = url;
        imageRequest.width = getWidth();
        imageRequest.height = getHeight();
        imageRequest.mImageListener = new ImageLoader.ImageRequest.OnLoadImageListener() {
            @Override
            public void onPreLoad() {
                if(mPreLoadDrawable != null){
                    AutoLoadImageView.this.setImageDrawable(mPreLoadDrawable);
                }
            }

            @Override
            public void onLoadBitmap(Bitmap bitmap) {
                AutoLoadImageView.this.setImageBitmap(bitmap);
            }

            @Override
            public void onError() {
                if(mErrorDrawable != null){
                    AutoLoadImageView.this.setImageDrawable(mErrorDrawable);
                }
            }
        };

        ImageLoader.getInstance().setUrl(imageRequest);

    }
}
