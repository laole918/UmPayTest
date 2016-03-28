package com.laole918.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by laole918 on 2016/3/28 0028.
 */
public class DeviceUtils {

    private static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 取出IMEI
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        if(tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 取出MSISDN，很可能为空
     * @param context
     * @return
     */
    public static String getLine1Number(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        if(tm != null) {
            return tm.getLine1Number();
        }
        return null;
    }

    /**
     * 取出ICCID
     * @param context
     * @return
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        if(tm != null) {
            return tm.getSimSerialNumber();
        }
        return null;
    }

    /**
     * 取出IMSI
     * @param context
     * @return
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        if(tm != null) {
            return tm.getSubscriberId();
        }
        return null;
    }
}
