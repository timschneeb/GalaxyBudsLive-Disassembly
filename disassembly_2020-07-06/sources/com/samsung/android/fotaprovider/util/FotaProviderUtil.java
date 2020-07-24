package com.samsung.android.fotaprovider.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.accessorydm.db.file.XDBLastUpdateAdp;
import com.accessorydm.db.file.XDBLastUpdateInfo;
import com.accessorydm.interfaces.XDBInterface;
import com.samsung.accessory.neobeanmgr.BuildConfig;
import com.samsung.android.fotaagent.FotaNoticeIntent;
import com.samsung.android.fotaprovider.FotaProviderInitializer;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.galaxywearable.BroadcastHelper;
import com.samsung.android.fotaprovider.util.type.DeviceType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Locale;

public class FotaProviderUtil {
    private static String getBudsPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    private static String getGearFit2PackageName() {
        return "com.samsung.android.gearfit2plugin";
    }

    private static String getGearModulesPackageName() {
        return "com.samsung.android.gear";
    }

    public static String getSingleFotaProviderPackageName() {
        return "com.sec.android.fotaprovider";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00fe, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0103, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r3.addSuppressed(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0107, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x010a, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0113, code lost:
        throw r3;
     */
    public static void copyLogToSdcard(Context context, String str) {
        String str2;
        if (context == null) {
            Log.D("cannot copy log to sdcard : called by [" + str + "]");
            return;
        }
        Log.D("copy log to sdcard : called by [" + str + "]");
        String absolutePath = context.getApplicationContext().getDir(Log.LOGFILE_PATH, 0).getAbsolutePath();
        if (isSingleFotaProvider()) {
            try {
                str2 = context.getExternalFilesDir((String) null).getAbsolutePath();
            } catch (Exception unused) {
                str2 = context.getFilesDir().getAbsolutePath();
            }
        } else {
            str2 = Environment.getDataDirectory().getAbsolutePath();
        }
        String str3 = str2 + File.separator + Log.LOGFILE_PATH + File.separator + "GearLog" + File.separator;
        File file = new File(str3);
        if (file.mkdirs()) {
            Log.D("copy log to sdcard : created " + file);
        }
        setFilePermissions(file);
        for (int i = 0; i < 2; i++) {
            File file2 = new File(absolutePath, String.format(Locale.US, Log.LOGFILE_DUMPSTATE, new Object[]{Integer.valueOf(i)}));
            File file3 = new File(str3, String.format(Locale.US, Log.LOGFILE_DUMPSTATE, new Object[]{Integer.valueOf(i)}));
            try {
                FileInputStream fileInputStream = new FileInputStream(file2);
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                FileChannel channel = fileInputStream.getChannel();
                channel.transferTo(0, channel.size(), fileOutputStream.getChannel());
                setFilePermissions(file3);
                fileOutputStream.close();
                fileInputStream.close();
            } catch (IOException | NullPointerException e) {
                Log.W(e.toString());
            } catch (Throwable th) {
                r2.addSuppressed(th);
            }
        }
    }

    public static void setFilePermissions(File file) {
        if (file.exists()) {
            if (!file.setReadable(true, false)) {
                Log.W("setPermissions() : setReadable FAIL");
            }
            if (!file.setWritable(true, false)) {
                Log.W("setPermissions() : setWritable FAIL");
            }
            if (!file.setExecutable(true, false)) {
                Log.W("setPermissions() : setWritable FAIL");
                return;
            }
            return;
        }
        Log.W("setPermissions() : file not exist");
    }

    public static DeviceType getDeviceType() {
        String packageName = FotaProviderInitializer.getContext().getPackageName();
        Log.I("Package Name - " + packageName);
        if (packageName.equals(getBudsPackageName())) {
            return DeviceType.EARBUDS;
        }
        if (packageName.contains(getGearFit2PackageName())) {
            return DeviceType.GEARFIT2;
        }
        if (packageName.contains(getGearModulesPackageName()) || isSingleFotaProvider()) {
            return DeviceType.WATCH;
        }
        Log.W("Unknown package. Set as default to watch");
        return DeviceType.WATCH;
    }

    public static boolean isSingleFotaProvider() {
        return FotaProviderInitializer.getContext().getPackageName().equals(getSingleFotaProviderPackageName());
    }

    public static String generateLogTagByPackageName(Context context, String str) {
        String str2;
        if (!isSingleFotaProvider()) {
            try {
                String[] split = context.getPackageName().split("\\.");
                str2 = split[split.length - 1];
            } catch (IndexOutOfBoundsException unused) {
                str2 = "UNKNOWN";
            }
        } else {
            str2 = "TEST";
        }
        return str + "_" + str2.toUpperCase();
    }

    public static void sendLastCheckedDate() {
        long currentTimeMillis = System.currentTimeMillis();
        Log.I("Current time : " + currentTimeMillis);
        Intent intent = new Intent(FotaNoticeIntent.INTENT_LAST_CHECKED_DATE);
        intent.putExtra(XDBInterface.XDM_SQL_DB_POLLING_TIME, currentTimeMillis);
        intent.setPackage(FotaProviderInitializer.getContext().getPackageName());
        BroadcastHelper.sendBroadcast(intent);
    }

    public static void sendIntentUpdateInProgress() {
        Log.D("");
        Intent intent = new Intent(FotaNoticeIntent.INTENT_UPDATE_IN_PROGRESS);
        intent.setPackage(FotaProviderInitializer.getContext().getPackageName());
        BroadcastHelper.sendBroadcast(intent);
    }

    public static void sendLastUpdateInfo() {
        XDBLastUpdateInfo lastUpdateInfo = XDBLastUpdateAdp.getLastUpdateInfo();
        long lastUpdateDate = lastUpdateInfo != null ? lastUpdateInfo.getLastUpdateDate() : 0;
        Log.I("send last update time: " + lastUpdateDate);
        Intent intent = new Intent(FotaNoticeIntent.INTENT_LAST_UPDATE_INFO);
        intent.putExtra(XDBInterface.XDM_SQL_DB_POLLING_TIME, lastUpdateDate);
        intent.setPackage(FotaProviderInitializer.getContext().getPackageName());
        BroadcastHelper.sendBroadcast(intent);
    }
}
