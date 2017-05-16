package com.joker.ocean.testview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.joker.ocean.log.Logger;

/**
 * Created by joker on 17-5-12.
 */

public class TestTextView extends android.support.v7.widget.AppCompatTextView{
    private Logger mLogger = Logger.getLogger(TestTextView.class);
    public TestTextView(Context context) {
        super(context);
    }

    public TestTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLogger.i("onTouchEvent: " + event.getAction());
//        if(event.getAction() == MotionEvent.ACTION_DOWN){
//            return false;
//        }
        return super.onTouchEvent(event);
    }
}
