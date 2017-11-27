package com.joker.multimedia.videorecorder;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by joker on 17-11-23.
 */

public class OceanGLRender implements GLSurfaceView.Renderer {
    private OceanGLSurfaceView mGLSurfaceView;

    public void setGLSurfaceView(OceanGLSurfaceView glSurfaceView) {
        this.mGLSurfaceView = glSurfaceView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }
}
