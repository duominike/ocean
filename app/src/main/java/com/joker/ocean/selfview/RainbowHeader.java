package com.joker.ocean.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.joker.ocean.R;
import com.joker.pacific.log.Logger;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * Created by joker on 18-1-14.
 */

public class RainbowHeader extends FrameLayout implements RefreshHeader {
    private Drawable mRaindow;
    private Paint mPaint;
    private Logger logger = Logger.getLogger(RainbowHeader.class);
    private int state = State.NONE;
    private static final class State{
        public static final int NONE = 0;
        public static final int PULL_DOWN = 1;
        public static final int RELEASSING_BEFORE_ANIM = 2;
        public static final int RELEASSING_ANIM_ING = 3;
        public static final int RELEASSING_AFTER_ANIM = 4;
    }
    public RainbowHeader(Context context) {
        super(context);
        init(context);
    }

    public RainbowHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RainbowHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mRaindow = context.getResources().getDrawable(R.drawable.rainbow_ic);
        mPaint = new Paint();
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        logger.info("onPullingDown percent: %.2f; offset: %d; headerHeight: %d; extendHeight: %d",
                percent, offset, headerHeight, extendHeight);
        state = State.PULL_DOWN;
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
        logger.info("onReleasing percent: %.2f; offset: %d; headerHeight: %d; extendHeight: %d",
                percent, offset, headerHeight, extendHeight);
    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
        logger.info("onRefreshReleased");

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.FixedFront;
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
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        logger.info("onFinish: " + success);
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
