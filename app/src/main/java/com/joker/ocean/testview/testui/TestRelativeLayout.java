package com.joker.ocean.testview.testui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.joker.pacific.log.Logger;


/**
 * Created by joker on 17-5-12.
 */

public class TestRelativeLayout extends RelativeLayout{
    private Logger mLogger = Logger.getLogger(TestRelativeLayout.class);
    public TestRelativeLayout(Context context) {
        super(context);
    }

    public TestRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLogger.info("onTouchEvent: --->> " + event.getAction());
        return super.onTouchEvent(event);
    }
}
