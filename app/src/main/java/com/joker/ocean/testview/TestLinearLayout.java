package com.joker.ocean.testview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.joker.pacific.log.Logger;

/**
 * Created by joker on 17-5-12.
 */

public class TestLinearLayout extends LinearLayout{
    private Logger mLogger = Logger.getLogger(TestLinearLayout.class);
    public TestLinearLayout(Context context) {
        super(context);
    }

    public TestLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mLogger.info("dispatchTouchEvent: --->> " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mLogger.info("onInterceptTouchEvent: --->> " + ev.getAction());
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLogger.info("onTouchEvent: --->> " + event.getAction());
        return super.onTouchEvent(event);
    }
}
