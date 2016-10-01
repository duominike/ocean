package com.handmark.pulltorefresh.library.internal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Utils {

	static final String LOG_TAG = "PullToRefresh";

	public static void warnDeprecation(String depreacted, String replacement) {
		Log.w(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
	}

    /**
     * 判断手势是否在 View里面
     * @param ev
     * @param size
     * @return true 在view里面,false 不再view里面
     */
	public static boolean isInView(MotionEvent ev, int[] size) {
		if (ev.getRawX() > size[0] && ev.getRawX() < size[2] && ev.getRawY() > size[1] && ev.getRawY() < size[3]) {
			return true;
		}
		return false;

	}

    /**
     * 计算view在屏幕内的绝对坐标 返回数组:注意是绝对坐标,包括顶部的提示栏
     *
     * @param v
     * @return
     */
	public static int[] calculateViewSize(View v) {
		int[] location = new int[2];
		v.getLocationInWindow(location);
		int[] size = { location[0], location[1], 0, 0 };
		size[2] = v.getWidth() + location[0];
		size[3] = v.getHeight() + location[1];
		return size;
	}

    /**
     * 判断手势是否在 View里面
     * @param ev
     * @param view
     * @return true 在view里面,false 不再view里面
     */
	public static boolean isInView(MotionEvent ev, View v) {
		return isInView(ev, calculateViewSize(v));
	}

    public static boolean isInWindow(Context context, View v){
        int nWindowWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int nWindowHeight =((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        int[] location = new int[2];
        v.getLocationInWindow(location);
        return (location[0] >= 0 && location[0] + v.getWidth() <= nWindowWidth);
    }

}
