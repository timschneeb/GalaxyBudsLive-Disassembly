package com.samsung.context.sdk.samsunganalytics.internal.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import com.accessorydm.tp.urlconnect.HttpNetworkInterface;
import com.samsung.context.sdk.samsunganalytics.AnalyticsException;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.executor.SingleThreadExecutor;
import com.samsung.context.sdk.samsunganalytics.internal.sender.LogType;
import com.samsung.context.sdk.samsunganalytics.internal.setting.BuildClient;
import java.util.Map;

public class Utils {
    private static BroadcastReceiver br;

    public static boolean isEngBin() {
        return Build.TYPE.equals("eng");
    }

    public static void throwException(String str) {
        if (!isEngBin()) {
            Debug.LogE(str);
            return;
        }
        throw new AnalyticsException(str);
    }

    public static long getDaysAgo(int i) {
        return Long.valueOf(System.currentTimeMillis()).longValue() - (((long) i) * 86400000);
    }

    public static boolean compareDays(int i, Long l) {
        return Long.valueOf(System.currentTimeMillis()).longValue() > l.longValue() + (((long) i) * 86400000);
    }

    public static boolean compareHours(int i, Long l) {
        return Long.valueOf(System.currentTimeMillis()).longValue() > l.longValue() + (((long) i) * 3600000);
    }

    public static String getDebugMessage(Map<String, String> map) {
        String str;
        String str2 = "";
        if (map.get("t").equals("pv")) {
            str2 = "page: " + map.get("pn");
            str = "detail: " + map.get("pd") + "  value: " + map.get("pv");
        } else if (map.get("t").equals("ev")) {
            str2 = "event: " + map.get(HttpNetworkInterface.XTP_HTTP_LANGUAGE);
            str = "detail: " + map.get("ed") + "  value: " + map.get("ev");
        } else if (map.get("t").equals("st")) {
            str2 = "status";
            str = map.get("sti");
        } else {
            str = str2;
        }
        return str2 + "\n" + str;
    }

    public static LogType getTypeForServer(String str) {
        return "dl".equals(str) ? LogType.DEVICE : LogType.UIX;
    }

    public static boolean isDiagnosticAgree(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "samsung_errorlog_agree", 0) == 1;
    }

    public static void sendSettings(Context context, Configuration configuration) {
        SingleThreadExecutor.getInstance().execute(new BuildClient(context, configuration));
    }

    public static void registerReceiver(Context context, final Configuration configuration) {
        Debug.LogENG("register BR ");
        if (br == null) {
            br = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("receive BR ");
                    sb.append(intent != null ? intent.getAction() : "null");
                    Debug.LogENG(sb.toString());
                    if (intent != null && "android.intent.action.ACTION_POWER_CONNECTED".equals(intent.getAction())) {
                        Utils.sendSettings(context, configuration);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            context.registerReceiver(br, intentFilter);
            return;
        }
        Debug.LogENG("BR is already registered");
    }
}
