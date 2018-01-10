package com.joker.pacific.util;

import android.os.Environment;

/**
 * Created by joker on 18-1-9.
 */

public class FilePathHelper {
    public static final String packageName = "/ocean";
    public static boolean hasSDCard() {
        try{
            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



}
