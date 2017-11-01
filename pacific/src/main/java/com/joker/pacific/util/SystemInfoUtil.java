package com.joker.pacific.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by joker on 17-10-31.
 */

public class SystemInfoUtil {
    private static String MID = null;
    private static final String FATAL_VALUE = "00000000";
    public static String getVersionName(Context context) {
        String appVer = "";
        PackageInfo packageInfo = null;
        synchronized (SystemInfoUtil.class) {
            try {
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (packageInfo != null) {
            appVer = packageInfo.versionName;
        }
        return appVer;
    }

    public static int getVersionCode(Context context) {
        int appVer = -1;
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appVer = packageInfo.versionCode;
        return appVer;
    }

    public static String getMobileModel() {
        if (Build.SERIAL != null) {
            return Build.MODEL.replaceAll(" ", "");
        }
        return FATAL_VALUE;
    }

    public static String getOSVersion() {
        if (Build.SERIAL != null) {
            return Build.VERSION.RELEASE;
        }
        return FATAL_VALUE;
    }

    public static String getMid(Context context) {
        if (MID != null) {
            return MID;
        }
        try {
            String deviceID = getDeviceId(context);
            String model = getMobileModel();
            model = model.replace("+", "");
            String serialNumber = getSerialNumber();
            MID = Md5.getMd5(deviceID + model + serialNumber);
        } catch (Exception e) {
            e.printStackTrace();
            MID = "00000000000000000000000000000000";
        }
        return MID;
    }

    private static String getDeviceId(Context context) {
        String deviceId = null;
        try {
            if (hasPermission(context, "android.permission.READ_PHONE_STATE")) {
                deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (deviceId != null) {
            return deviceId;
        }
        return FATAL_VALUE;
    }

    public static boolean hasPermission(Context context, String name){
        PackageManager pm = context.getPackageManager();
        if (pm == null){
            return false;
        }
        if (context.getPackageName() == null){
            return false;
        }
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission(name, context.getPackageName());
    }

    public static String getSerialNumber() {
        if (Build.SERIAL != null) {
            return Build.SERIAL;
        }
        return FATAL_VALUE;
    }
}
