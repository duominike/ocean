package com.joker.ocean.pic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import static com.joker.ocean.pic.PicColorMatrixOperImpl.RGB.BLUE;
import static com.joker.ocean.pic.PicColorMatrixOperImpl.RGB.GREEEN;
import static com.joker.ocean.pic.PicColorMatrixOperImpl.RGB.RED;

/**
 * Created by joker on 17-11-13.
 */

public class PicColorMatrixOperImpl {
    private static float[] srcMatrix = {
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0,
    };

    public static class RGB {
        public static final int RED = 0;
        public static final int GREEEN = 1;
        public static final int BLUE = 2;
    }

    public static void setRotate(int rgb, int value, ColorMatrix colorMatrix) {
        colorMatrix.setRotate(rgb, value);
    }

    public static void setSaturation(int value, ColorMatrix colorMatrix) {
        colorMatrix.setSaturation(value);
    }

    public static void setRotate(int rScale, int gScale, int bScale, int value, ColorMatrix colorMatrix) {
        colorMatrix.setScale(rScale, gScale, bScale, value);
    }

    public static void resetMatrix() {
        for (int i = 0; i < srcMatrix.length; i++) {
            srcMatrix[i] = 0;
        }
        srcMatrix[0] = 1;
        srcMatrix[6] = 1;
        srcMatrix[12] = 1;
        srcMatrix[18] = 1;

    }

    public static Bitmap handleImageEffect(Bitmap src, float hue, float saturation, float lum) {
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(RED, hue);
        hueMatrix.setRotate(GREEEN, hue);
        hueMatrix.setRotate(BLUE, hue);

        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        ColorMatrix scaleMatrix = new ColorMatrix();
        scaleMatrix.setScale(lum, lum, lum, 1);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.postConcat(hueMatrix);
        colorMatrix.postConcat(saturationMatrix);
        colorMatrix.postConcat(scaleMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmap;
    }

    /**
     * 灰度
     * 0.33f, 0.59f, 0.11f, 0, 0,
     * 0.33f, 0.59f, 0.11f, 0, 0,
     * 0.33f, 0.59f, 0.11f, 0,
     * 0, 0, 0, 1, 0,
     */
    public static Bitmap handleGrayEffect(Bitmap src) {
        resetMatrix();
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        srcMatrix[0] = 0.33f;
        srcMatrix[1] = 0.59f;
        srcMatrix[2] = 0.11f;
        srcMatrix[5] = 0.33f;
        srcMatrix[6] = 0.59f;
        srcMatrix[7] = 0.11f;
        srcMatrix[10] = 0.33f;
        srcMatrix[11] = 0.59f;
        srcMatrix[12] = 0.11f;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(srcMatrix);
        Canvas canvas = new Canvas(src);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(result, 0, 0, paint);
        return result;
    }

    /**
     * 反转
     * -1, 0, 0, 1, 1,
     * 0, -1, 0, 1, 1,
     * 0, 0, -1, 1, 1,
     * 0, 0, 0, 1, 0,
     */
    public static Bitmap handleReversalEffect(Bitmap src) {
        resetMatrix();
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        srcMatrix[0] = -1;
        srcMatrix[3] = 1;
        srcMatrix[4] = 1;

        srcMatrix[6] = -1;
        srcMatrix[8] = 1;
        srcMatrix[9] = 1;

        srcMatrix[12] = -1;
        srcMatrix[13] = 1;
        srcMatrix[14] = 1;

        srcMatrix[18] = 1;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(srcMatrix);
        Canvas canvas = new Canvas(src);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(result, 0, 0, paint);
        return result;
    }

    /**
     * 去色
     * 0.15f, 0.15f 0.15f, 0 , -1,
     * 0.15f, 0.15f 0.15f, 0 , -1,
     * 0.15f, 0.15f 0.15f, 0 , -1,
     * 0, 0, 0, 1, 0,
     */
    public static Bitmap handleColorEffect(Bitmap src) {
        resetMatrix();
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        srcMatrix[0] = 0.15f;
        srcMatrix[1] = 0.15f;
        srcMatrix[2] = 0.15f;
        srcMatrix[5] = 0.15f;
        srcMatrix[6] = 0.15f;
        srcMatrix[7] = 0.15f;
        srcMatrix[10] = 0.15f;
        srcMatrix[11] = 0.15f;
        srcMatrix[12] = 0.15f;

        srcMatrix[18] = 1;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(srcMatrix);
        Canvas canvas = new Canvas(src);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(result, 0, 0, paint);
        return result;
    }

    /**
     * 去色
     * 1.438f, -0.122f, -0.016f, 0 , -0.03f,
     * -0.062f, 1.378f, -0.016f, 0 , 0.05f,
     * -0.062f, -0.122f, 1.483f, 0 , -0.02f,
     * 0, 0, 0, 1, 0,
     */
    public static Bitmap handleHighlumEffect(Bitmap src) {
        resetMatrix();
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        srcMatrix[0] = 1.438f;
        srcMatrix[1] = -0.122f;
        srcMatrix[2] = -0.016f;
        srcMatrix[4] = -0.03f;

        srcMatrix[5] = -0.062f;
        srcMatrix[6] = 1.378f;
        srcMatrix[7] = -0.016f;
        srcMatrix[8] = 0.05f;

        srcMatrix[10] = -0.062f;
        srcMatrix[11] = -0.122f;
        srcMatrix[12] = 1.483f;

        srcMatrix[14] = -0.02f;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(srcMatrix);
        Canvas canvas = new Canvas(src);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(result, 0, 0, paint);
        return result;
    }


    public static Bitmap handleNegative(Bitmap src) {
        int[] oldPixel = new int[src.getWidth() * src.getHeight()];
        int[] newPixel = new int[src.getWidth() * src.getHeight()];
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        src.getPixels(oldPixel, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        int color;
        int r, g, b, a;
        for (int i = 0; i < src.getWidth() * src.getHeight(); i++) {
            color = oldPixel[i];
            r = Color.red(color);
            g = Color.red(color);
            b = Color.red(color);
            a = Color.red(color);
            negativeEffect(r, g, b, a);
            newPixel[i] = Color.argb(r, g, b, a);
        }
        result.setPixels(newPixel, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        return result;
    }

    private static void negativeEffect(int r, int g, int b, int a) {
        r = 255 - r;
        g = 255 - g;
        b = 255 - b;
        if (r > 255) {
            r = 255;
        } else if (r < 0) {
            r = 0;
        }

        if (g > 255) {
            g = 255;
        } else if (r < 0) {
            g = 0;
        }

        if (b > 255) {
            b = 255;
        } else if (r < 0) {
            b = 0;
        }
    }


    public static Bitmap handleOldPic(Bitmap src) {
        int[] oldPixel = new int[src.getWidth() * src.getHeight()];
        int[] newPixel = new int[src.getWidth() * src.getHeight()];
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        src.getPixels(oldPixel, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        int color;
        int r, g, b, a;
        for (int i = 0; i < src.getWidth() * src.getHeight(); i++) {
            color = oldPixel[i];
            r = Color.red(color);
            g = Color.red(color);
            b = Color.red(color);
            a = Color.red(color);
            oldEffectEffect(r, g, b, a);
            newPixel[i] = Color.argb(r, g, b, a);
        }
        result.setPixels(newPixel, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        return result;
    }

    private static void oldEffectEffect(int r, int g, int b, int a) {
        r = (int)(0.393 * r + 0.769 * g + 0.189 * b);
        r = (int)(0.349 * r + 0.686 * g + 0.168 * b);
        r = (int)(0.272 * r + 0.534 * g + 0.1131 * b);
        if (r > 255) {
            r = 255;
        } else if (r < 0) {
            r = 0;
        }

        if (g > 255) {
            g = 255;
        } else if (r < 0) {
            g = 0;
        }

        if (b > 255) {
            b = 255;
        } else if (r < 0) {
            b = 0;
        }
    }


}
