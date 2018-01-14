package com.joker.ocean.selfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.joker.ocean.R;
import com.joker.pacific.log.Logger;

import static com.joker.ocean.selfview.RainbowAnimView.State.RELEASING_AFTER_ANIM;
import static com.joker.ocean.selfview.RainbowAnimView.State.RELEASING_BEFORE_ANIM;

/**
 * Created by joker on 2018/1/14.
 */

public class RainbowAnimView extends View {
    private Logger logger = Logger.getLogger(RainbowAnimView.class);

    public static final class State {
        public static final int NONE = 0;
        public static final int PULL_DOWN = 1;
        public static final int RELEASING_BEFORE_ANIM = 2;
        public static final int RELEASING_ANIM_ING = 4;
        public static final int RELEASING_AFTER_ANIM = 3;
    }

    private int state = RainbowAnimView.State.NONE;
    private int py;
    private int px;
    private float rotateAngle = 0.00f;
    private int mRanibowWidth = 0;
    private int mRainbowHeight = 0;
    private Animation animation;
    private Drawable mRainbow;
    private Bitmap mRainbowForDraw;
    private Paint mPaint;
    private static final  int  INVALIDATE = 100;
    private static final int PER_FRAME_ROTATE_ANGLE = 30;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == INVALIDATE){
                invalidate();
                if(state == State.RELEASING_ANIM_ING){
                    rotateAngle = (rotateAngle - PER_FRAME_ROTATE_ANGLE) % 360;
                    mHandler.sendEmptyMessageDelayed(INVALIDATE, 90);
                }
            }

        }
    };

    public RainbowAnimView(Context context) {
        this(context, null);
    }

    public RainbowAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainbowAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RainbowAnimView);
        mRanibowWidth = typedArray.getDimensionPixelSize(R.styleable.RainbowAnimView_rainbowWidth,
                context.getResources().getDimensionPixelOffset(R.dimen.rainbow_width));
        mRainbowHeight = typedArray.getDimensionPixelSize(R.styleable.RainbowAnimView_rainbowWidth,
                context.getResources().getDimensionPixelOffset(R.dimen.rainbow_height));
        px = typedArray.getDimensionPixelSize(R.styleable.RainbowAnimView_rainbowLeftMargin,
                context.getResources().getDimensionPixelOffset(R.dimen.rainbow_left_margin));
        mRainbow = typedArray.getDrawable(R.styleable.RainbowAnimView_rainbow);
        drawToBitmap();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animation = AnimationUtils.loadAnimation(context, R.anim.rainbow_rotate);
        animation.setInterpolator(new LinearInterpolator());
        typedArray.recycle();
    }

    private void drawToBitmap() {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;         // 取 drawable 的颜色格式
        mRainbowForDraw = Bitmap.createBitmap(mRanibowWidth, mRainbowHeight, config);     // 建立对应 bitmap
        Canvas canvas = new Canvas(mRainbowForDraw);         // 建立对应 bitmap 的画布
        mRainbow.setBounds(0, 0, mRanibowWidth, mRainbowHeight);
        mRainbow.draw(canvas);      // 把 drawable 内容画到画布中
    }


    public void setState(int state) {
        if (this.state == RELEASING_AFTER_ANIM && state == RELEASING_BEFORE_ANIM) {
            return;
        }
        this.state = state;
        invalidate();
    }

    public void refreshPosition(float percent, int offset) {
        py = offset;
        rotateAngle = (360 * percent * 0.01f * 100) % 360;
        invalidate();
    }

    public void startAnimation() {
        invalidate();
        state = State.RELEASING_ANIM_ING;
        rotateAngle = 360 - rotateAngle;
        rotateAngle = (rotateAngle - PER_FRAME_ROTATE_ANGLE) % 360;
        mHandler.sendEmptyMessage(INVALIDATE);
    }

    public void endAnimation() {
        mHandler.removeMessages(INVALIDATE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state >= State.PULL_DOWN && state <= State.RELEASING_ANIM_ING) {
            logger.info("onDraw: px: %d; py: %d; rotateAngle: %.2f", px, py, rotateAngle);
            canvas.save();
            canvas.translate(px, py);
            canvas.rotate(rotateAngle);
            canvas.drawBitmap(mRainbowForDraw, -mRanibowWidth / 2, -mRainbowHeight / 2, mPaint);
            canvas.restore();
        }
    }
}
