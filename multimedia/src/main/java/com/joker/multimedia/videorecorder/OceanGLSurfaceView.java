package com.joker.multimedia.videorecorder;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by joker on 17-11-23.
 */

public class OceanGLSurfaceView extends GLSurfaceView {
    private OceanGLRender mRenderer;

    public OceanGLSurfaceView(Context context) {
        super(context);
        initRender();
    }

    public OceanGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRender();
    }

    private void initRender() {
        mRenderer = new OceanGLRender();
        this.setEGLContextClientVersion(2);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.setRenderer(mRenderer);
        /**
         * RENDERMODE_WHEN_DIRTY 懒惰渲染
         * RENDERMODE_CONTINUOUSLY 不停渲染
         */
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        mRenderer.setGLSurfaceView(this);
    }
}
