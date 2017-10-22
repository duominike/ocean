package com.joker.pacific.component.delegate;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by joker on 17-10-18.
 */

public final class ResourceDelegate {
    private static Context sContext;
    private ResourceDelegate(Context context){
        this.sContext = context;
    }

    public static synchronized void install(Context context){
        if(sContext == null){
            sContext = context;
        }
    }


    public static Drawable getDrawable(int id){
        return sContext.getResources().getDrawable(id);
    }

    public static String getString(int id){
        return sContext.getResources().getString(id);
    }

    public static int getColor(int id){
        return sContext.getResources().getColor(id);
    }

    public static int getDimensionPixelOffset(int id){
        return sContext.getResources().getDimensionPixelOffset(id);
    }

    public static float getDimension(int id){
        return sContext.getResources().getDimension(id);
    }

    public static int getDimensionPixelSize(int id){
        return sContext.getResources().getDimensionPixelSize(id);
    }

}
