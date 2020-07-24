package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.os.EnvironmentCompat;

public class newPackageInformation {
    private static final String PRE_ID = "TWID:";
    private static final String SERIAL_NO = "ro.serialno";
    private static final String TAG_DEVICEINFO = "deviceInfo";
    private static final String TAG_SERVICECLIENT_VERSION = "serviceClientVer";
    public static newPackageInformation instance = new newPackageInformation();

    public String getPackageName(Context context) {
        return context.getPackageName();
    }

    public String getSimpleName(Context context) {
        String packageName = getPackageName(context);
        int lastIndexOf = packageName.lastIndexOf(46);
        return lastIndexOf != -1 ? packageName.substring(lastIndexOf + 1) : packageName;
    }

    public String getPackageVersion(Context context) {
        return getPackageVersion(context, getPackageName(context));
    }

    public String getPackageVersion(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        try {
            return packageManager.getPackageInfo(str, 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public static String getTWID() {
        String serialNo = getSerialNo();
        if ("".equals(serialNo)) {
            return "";
        }
        return PRE_ID + serialNo;
    }

    public static String getSerialNo() {
        if (Build.VERSION.SDK_INT >= 26) {
            return "";
        }
        return Build.SERIAL;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x000f */
    public Bundle getDeviceInfoBundle(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBundle("deviceInfo", new Bundle());
        try {
            bundle.getBundle("deviceInfo").putString(TAG_SERVICECLIENT_VERSION, getPackageVersion(context));
        } catch (Exception unused) {
        }
        return bundle;
    }
}
