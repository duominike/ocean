package com.joker.pacific.util;

import android.os.Environment;

import java.io.File;

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

    public static String getSDCardDataFolder(String more, String file) {
        String result = null;
        if (hasSDCard()) {
            File sdcardFile = Environment.getExternalStorageDirectory();
            if (sdcardFile != null) {
                result = sdcardFile.getAbsolutePath() + packageName;
            }
        }
        if (result != null) {
            if (more != null) {
                result += more;
            }
            if (createFolderRecursive(result)) {
                if (file != null) {
                    result += "/";
                    result += file;
                }
            } else {
                result = null;
            }
        }
        return result;
    }

    public static boolean createFolderRecursive(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            return true;
        }
        if (file.isFile()) {
            return false;
        }
        String fileParent = file.getParent();
        if (fileParent != null) {
            if (!createFolderRecursive(fileParent)) {
                return false;
            }
        }
        return file.mkdir();
    }


}
