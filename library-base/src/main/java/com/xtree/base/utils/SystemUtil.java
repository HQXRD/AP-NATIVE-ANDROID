package com.xtree.base.utils;

import android.os.Build;

public class SystemUtil {
    public static String getDeviceBrand(){
        return Build.BRAND;
    }

    public static String getDeviceModel(){
        return Build.MODEL;
    }

    public static String getSystemVersion(){
        return Build.VERSION.RELEASE;
    }
}
