package com.joker.ocean.pic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by joker on 17-11-13.
 */

public class PicHelper {
    public static Bitmap decodeRes(Context context, int rsId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(context.getResources(), rsId, options);
    }

    public static Bitmap convertDrawableToBitmap(Drawable drawable){
        BitmapDrawable bd = (BitmapDrawable)drawable;
        return bd.getBitmap();
    }

    public static Drawable convertBitMapToDrawable(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    public static Bitmap createNewBitmapByDrawable(Drawable drawable){
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }
}

