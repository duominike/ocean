/*
 *  Copyright (C) 2016 joker
 *  自定义垂直的seekbar
 *
 */
/*============================================================================*/
package com.joker.sponge.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by joker on 17-1-2.
 */

public class VerticalSeekBar extends View {
    private int mProgress = 0;
    private int mMax = 100;
    private int mBgColor = Color.GRAY;
    private int mProgressColor = Color.BLUE;
    private Drawable mThumbDrawable;
    private int mThumbWidth;
    private int mThumbHeight;
    // size params
    private int mHeight;
    private int mWidth;
    private int mBgWidth;
    private int mLeftMargin;
    private int mTopMargin;
    private int mRightMargin;
    private int mBottomMargin;
    private int mTotalWidth;
    private int mTotalHeight;
    // progress pos
    private float mCurrentProgressPos;
    private int mProgresStartPos;
    private int mProgresEndPos;
    // draw params
    private Paint mPaint;
    private RectF mRectF;

    private boolean mIsDraging;
    private VerticalSeekBar.OnVerticalSeekbarChangeListener m_listener;
    public VerticalSeekBar(Context context) {
        this(context, null);
    }

    public VerticalSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalSeekBar);
        if (typedArray.hasValue(R.styleable.VerticalSeekBar_max)) {
            mMax = typedArray.getInt(R.styleable.VerticalSeekBar_max, 100);
        }
        if (typedArray.hasValue(R.styleable.VerticalSeekBar_progress)) {
            mProgress = typedArray.getInt(R.styleable.VerticalSeekBar_progress, 0);
        }
        if (typedArray.hasValue(R.styleable.VerticalSeekBar_progressColor)) {
            mProgressColor = typedArray.getColor(R.styleable.VerticalSeekBar_progressColor, Color.BLUE);
        }

        if (typedArray.hasValue(R.styleable.VerticalSeekBar_backgroundColor)) {
            mBgColor = typedArray.getColor(R.styleable.VerticalSeekBar_backgroundColor, Color.GRAY);
        }

        if (typedArray.hasValue(R.styleable.VerticalSeekBar_thumbDrawable)) {
            mThumbDrawable = typedArray.getDrawable(R.styleable.VerticalSeekBar_thumbDrawable);
            mThumbWidth = mThumbDrawable.getIntrinsicWidth();
            mThumbHeight = mThumbDrawable.getIntrinsicHeight();
        }

        if(typedArray.hasValue(R.styleable.VerticalSeekBar_seekBarWidth)){
            mBgWidth = typedArray.getDimensionPixelSize(R.styleable.VerticalSeekBar_seekBarWidth, 8);
        }

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawProgress(canvas);
        drawThumb(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (width < mThumbWidth) {
            width = mThumbWidth;
            int height = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(width, height);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void drawBackground(Canvas canvas) {
        mPaint.setColor(mBgColor);
        mRectF.left = mLeftMargin + (mWidth - mBgWidth) / 2;
        mRectF.right = mRectF.left + mBgWidth;
        mRectF.top = mTopMargin - mThumbHeight / 2;
        mRectF.bottom = mHeight - mThumbHeight / 2;
        canvas.drawRect(mRectF, mPaint);
        canvas.drawCircle(mRectF.left + mBgWidth / 2, mRectF.top, mBgWidth / 2, mPaint);
        canvas.drawCircle(mRectF.left + mBgWidth / 2, mRectF.bottom, mBgWidth / 2, mPaint);

    }

    private void drawProgress(Canvas canvas) {
        prosessOutSide();
        mRectF.top = mCurrentProgressPos;
        mPaint.setColor(mProgressColor);
        canvas.drawRect(mRectF, mPaint);
        canvas.drawCircle(mRectF.left + mBgWidth / 2, mRectF.bottom, mBgWidth / 2, mPaint);
    }

    private void prosessOutSide() {
        if (mCurrentProgressPos >= mProgresStartPos) {
            mCurrentProgressPos = mProgresStartPos;
        }
        if (mCurrentProgressPos <= mProgresEndPos) {
            mCurrentProgressPos = mProgresEndPos;
        }
    }

    private void drawThumb(Canvas canvas) {
        if (mThumbDrawable != null) {
            prosessOutSide();
            canvas.save();
            Rect srcRect = new Rect(0, 0, mThumbWidth, mThumbHeight);
            int left = mLeftMargin + mWidth / 2 - mThumbWidth / 2;
            int top = (int) mCurrentProgressPos - mThumbHeight / 2;
            Rect dstRect = new Rect(left, top, left + mThumbWidth, top + mThumbHeight);
            canvas.drawBitmap(((BitmapDrawable) mThumbDrawable).getBitmap(), srcRect, dstRect, mPaint);
            canvas.restore();
        }
    }

    private void startTracking() {
        mIsDraging = true;
        if(m_listener != null){
            m_listener.onStartTrackingTouch(this);
        }
        invalidate();
    }

    private void stopTracking() {
        mIsDraging = false;
        if(m_listener != null){
            m_listener.onStopTrackingTouch(this);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_CANCEL) {
            return false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTracking();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentProgressPos = event.getY();
                prosessOutSide();
                changeProgressFromUser();
                break;
            case MotionEvent.ACTION_UP:
                stopTracking();
                break;
        }
        return true;
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    /**
     * set current progress
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        mCurrentProgressPos = getProgressPos();
        invalidate();
        if(m_listener != null){
            m_listener.onProgressChanged(this, mProgress, false);
        }
    }

    /**
     * get current progress
     * @return
     */
    public int getProgress() {
        return this.mProgress;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalHeight = h;
        mTotalWidth = w;
        if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
            mLeftMargin = ((LinearLayout.LayoutParams) getLayoutParams()).leftMargin;
            mRightMargin = ((LinearLayout.LayoutParams) getLayoutParams()).rightMargin;
            mTopMargin = ((LinearLayout.LayoutParams) getLayoutParams()).topMargin;
            mBottomMargin = ((LinearLayout.LayoutParams) getLayoutParams()).bottomMargin;
        }

        if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            mLeftMargin = ((RelativeLayout.LayoutParams) getLayoutParams()).leftMargin;
            mRightMargin = ((RelativeLayout.LayoutParams) getLayoutParams()).rightMargin;
            mTopMargin = ((RelativeLayout.LayoutParams) getLayoutParams()).topMargin;
            mBottomMargin = ((RelativeLayout.LayoutParams) getLayoutParams()).bottomMargin;
        }

        if (getLayoutParams() instanceof FrameLayout.LayoutParams) {
            mLeftMargin = ((FrameLayout.LayoutParams) getLayoutParams()).leftMargin;
            mRightMargin = ((FrameLayout.LayoutParams) getLayoutParams()).rightMargin;
            mTopMargin = ((FrameLayout.LayoutParams) getLayoutParams()).topMargin;
            mBottomMargin = ((FrameLayout.LayoutParams) getLayoutParams()).bottomMargin;
        }

        mHeight = mTotalHeight - mTopMargin - mBottomMargin;
        mWidth = mTotalWidth - mLeftMargin - mRightMargin;
        if (mThumbDrawable == null) {
            mThumbWidth = mWidth;
            mThumbHeight = mWidth;
        }
        mProgresStartPos = mHeight - mThumbHeight / 2;
        mProgresEndPos = mThumbHeight / 2;
        mCurrentProgressPos = getProgressPos();
    }

    /**
     * convert progress to current seek pos
     *
     * @return
     */
    private float getProgressPos() {
        float everyProgressPx = Math.abs(mProgresEndPos - mProgresStartPos) / mMax;
        return mThumbHeight / 2 + (mMax - mProgress) * everyProgressPx;
    }

    /**
     * change progress by user seek current pos
     */
    private void changeProgressFromUser(){
        if(mCurrentProgressPos >= mProgresStartPos){
            mProgress = 0;
        }else if(mCurrentProgressPos <= mProgresEndPos){
            mProgress = mMax;
        }
        mProgress = (int)(Math.abs((mProgresStartPos - mCurrentProgressPos)/ (mProgresStartPos - mProgresEndPos)) * mMax);
        if(m_listener != null){
            m_listener.onProgressChanged(this, mProgress, mIsDraging);
        }
        invalidate();
    }

    public void setOnVerticalSeekbarChangeListener(OnVerticalSeekbarChangeListener l){
        this.m_listener = l;
    }

    /**
     * This is an interface which when user seek we can notify user progress changed
     */
    public interface OnVerticalSeekbarChangeListener {
        void onStartTrackingTouch(VerticalSeekBar verticalSeekBar);

        void onStopTrackingTouch(VerticalSeekBar verticalSeekBar);

        void onProgressChanged(VerticalSeekBar verticalSeekBar, int i, boolean fromUser);
    }
}
