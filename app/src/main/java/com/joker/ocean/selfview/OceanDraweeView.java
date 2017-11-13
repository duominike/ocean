package com.joker.ocean.selfview;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.joker.pacific.log.Logger;

/**
 * Created by joker on 17-11-7.
 */

public class OceanDraweeView extends com.facebook.drawee.view.SimpleDraweeView {
    private Logger mLogger = Logger.getLogger(getClass());
    public OceanDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public OceanDraweeView(Context context) {
        super(context);
    }

    public OceanDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OceanDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OceanDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (null == uri || uri == Uri.EMPTY || TextUtils.isEmpty(uri.toString())) {
            super.setImageURI(uri);
            return;
        }
        setNetWorkImageResources(uri);
    }

    private void setNetWorkImageResources(Uri uri) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(this.getController())
                .setAutoPlayAnimations(true)
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
                        mLogger.info("onSubmit: " + id + Log.getStackTraceString(new Throwable()));
                        super.onSubmit(id, callerContext);
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        mLogger.info("onFinalImageSet: " + id);
                        super.onFinalImageSet(id, imageInfo, animatable);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                        mLogger.info("onIntermediateImageSet: " + id);
                        super.onIntermediateImageSet(id, imageInfo);
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                        mLogger.info("onIntermediateImageFailed: " + id);
                        super.onIntermediateImageFailed(id, throwable);
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        mLogger.info("onFailure: " + id);
                        super.onFailure(id, throwable);
                    }

                    @Override
                    public void onRelease(String id) {
                        mLogger.info("onRelease: " + id);
                        super.onRelease(id);
                    }
                }).build();
        this.setController(draweeController);
    }
}
