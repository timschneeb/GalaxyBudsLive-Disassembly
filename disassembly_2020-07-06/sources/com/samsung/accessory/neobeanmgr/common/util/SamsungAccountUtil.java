package com.samsung.accessory.neobeanmgr.common.util;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.android.sdk.mobileservice.common.CommonUtils;

public class SamsungAccountUtil {
    private static final String TAG = "NeoBean_SamsungAccountSupport";

    public static boolean isSignedIn() {
        boolean z = AccountManager.get(Application.getContext()).getAccountsByType(CommonUtils.SAMSUNG_ACCOUNT_PACKAGE_NAME).length > 0;
        Log.d(TAG, "isSignedIn() : " + z);
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0021, code lost:
        r4 = com.samsung.android.sdk.mobileservice.common.CommonUtils.SAMSUNG_ACCOUNT_PACKAGE_NAME;
     */
    private static long getSamsungAccountVersion() {
        long j;
        PackageInfo packageInfo;
        PackageManager packageManager = Application.getContext().getPackageManager();
        long j2 = 0;
        String str = null;
        try {
            if (Build.VERSION.SDK_INT != 26) {
                if (Build.VERSION.SDK_INT != 28) {
                    if (Build.VERSION.SDK_INT >= 29 && (packageInfo = packageManager.getPackageInfo(str, 0)) != null) {
                        j = packageInfo.getLongVersionCode();
                        j2 = j;
                    }
                    Log.d(TAG, "getSamsungAccountVersion() : SamsungAccountPackageName=" + str);
                    Log.d(TAG, "getSamsungAccountVersion() : result=" + j2);
                    return j2;
                }
            }
            str = "com.samsung.android.mobileservice";
            PackageInfo packageInfo2 = packageManager.getPackageInfo(str, 0);
            if (packageInfo2 != null) {
                j = (long) packageInfo2.versionCode;
                j2 = j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getSamsungAccountVersion() : Exception : " + e);
        }
        Log.d(TAG, "getSamsungAccountVersion() : SamsungAccountPackageName=" + str);
        Log.d(TAG, "getSamsungAccountVersion() : result=" + j2);
        return j2;
    }

    public static boolean isSettingSupport() {
        boolean z = getSamsungAccountVersion() > 420000000;
        Log.d(TAG, "isSettingSupport() : " + z);
        return z;
    }

    public static void startSettingActivity(Activity activity) {
        Log.d(TAG, "startSettingActivity() : " + activity);
        if (isSettingSupport()) {
            try {
                activity.startActivity(new Intent("com.samsung.android.samsungaccount.action.OPEN_SASETTINGS"));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "startSettingActivity() : Exception : " + e);
            }
        }
    }
}
