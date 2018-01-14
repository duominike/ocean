package com.joker.ocean.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.joker.pacific.log.Logger;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by joker on 18-1-14.
 */

public class RainbowHeader extends FrameLayout implements RefreshHeader {
    private Logger logger = Logger.getLogger(RainbowHeader.class);
    private RainbowAnimView mRainbowAnimView;
    public RainbowHeader(Context context) {
        super(context);
    }

    public RainbowHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RainbowHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        logger.info("onPullingDown percent: %.2f; offset: %d; headerHeight: %d; extendHeight: %d",
                percent, offset, headerHeight, extendHeight);
        if(mRainbowAnimView == null){
           findRainbowAnimView();
        }
        if(mRainbowAnimView.getVisibility() != View.VISIBLE){
            mRainbowAnimView.setVisibility(View.VISIBLE);
        }
        mRainbowAnimView.setState(RainbowAnimView.State.PULL_DOWN);
        mRainbowAnimView.refreshPosition(percent, offset);
    }

    private void findRainbowAnimView(){
        ViewGroup parent = (ViewGroup) getParent();
        mRainbowAnimView = null;
        while (!(parent instanceof FrameLayout)){
            parent = (ViewGroup)parent.getParent();
        }

        for(int i = parent.getChildCount() -1; i >= 0; i--){
            if(parent.getChildAt(i) instanceof RainbowAnimView){
                mRainbowAnimView = (RainbowAnimView)parent.getChildAt(i);
                break;
            }
        }
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
        logger.info("onReleasing percent: %.2f; offset: %d; headerHeight: %d; extendHeight: %d",
                percent, offset, headerHeight, extendHeight);
        mRainbowAnimView.setState(RainbowAnimView.State.RELEASING_BEFORE_ANIM);
        mRainbowAnimView.refreshPosition(percent, offset);
        if(offset == 0 && percent == 0.00f){
            mRainbowAnimView.setState(RainbowAnimView.State.NONE);
            mRainbowAnimView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
        logger.info("onRefreshReleased");
        mRainbowAnimView.setState(RainbowAnimView.State.RELEASING_BEFORE_ANIM);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {
        logger.info("onStartAnimator");
        mRainbowAnimView.setState(RainbowAnimView.State.RELEASING_ANIM_ING);
        mRainbowAnimView.startAnimation();

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        logger.info("onFinish: " + success);
        mRainbowAnimView.endAnimation();
        mRainbowAnimView.setState(RainbowAnimView.State.RELEASING_AFTER_ANIM);
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
