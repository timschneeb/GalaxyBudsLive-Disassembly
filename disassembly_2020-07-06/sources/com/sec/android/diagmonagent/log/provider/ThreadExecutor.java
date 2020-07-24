package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.sec.android.diagmonagent.log.provider.utils.DiagMonUtil;
import com.sec.android.diagmonagent.log.provider.utils.Validator;
import java.util.concurrent.TimeUnit;

/* compiled from: DiagMonSDK */
class ThreadExecutor implements Runnable {
    private static long MIN_WAITING_TIME = TimeUnit.HOURS.toMillis(6);
    private static String PREF_DIAGMON_CHECK = "diagmon_check";
    private static String PREF_DIAGMON_NAME = "diagmon_pref";
    private static String PREF_DIAGMON_TIMESTAMP = "diagmon_timestamp";
    private DiagMonProvider elp;
    private DiagMonConfig mConfig;
    private Bundle srObj;

    public void run() {
        DiagMonSDK.toggleConfigurationStatus(true);
        int checkDMA = DiagMonUtil.checkDMA(this.mConfig.getContext());
        if (checkDMA == 0) {
            Log.w(DiagMonUtil.TAG, "Not installed DMA");
            Log.w(DiagMonUtil.TAG, "SetConfiguration is aborted");
        } else if (checkDMA != 1) {
            if (checkDMA != 2) {
                Log.w(DiagMonUtil.TAG, "Exceptional case");
                Log.w(DiagMonUtil.TAG, "SetConfiguration is aborted");
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis <= getPrefDiagmonTimestamp(this.mConfig.getContext()) + MIN_WAITING_TIME) {
                Log.d(DiagMonUtil.TAG, "it is not time to request for SR");
            } else if (checkAuthority(this.mConfig.getContext(), this.mConfig.getServiceId())) {
                setPrefDiagmonTimestamp(this.mConfig.getContext(), currentTimeMillis);
                this.srObj = new Bundle();
                this.srObj = DiagMonUtil.generateSRobj(this.mConfig);
                DiagMonSDK.setSRObj(this.srObj);
                DiagMonSDK.sendSRObj(this.srObj);
            } else {
                Log.w(DiagMonUtil.TAG, "Authority check got failed");
            }
        } else if (Validator.validateLegacyConfig(this.mConfig)) {
            Log.w(DiagMonUtil.TAG, "Invalid DiagMonConfiguration");
            Log.w(DiagMonUtil.TAG, "SetConfiguration is aborted");
            this.mConfig = null;
        } else {
            this.elp = new DiagMonProvider();
            this.elp.setConfiguration(this.mConfig);
            Log.i(DiagMonUtil.TAG, "Valid DiagMonConfiguration");
        }
    }

    public ThreadExecutor(DiagMonConfig diagMonConfig) {
        this.mConfig = diagMonConfig;
    }

    private static void setPrefDiagmonTimestamp(Context context, long j) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREF_DIAGMON_NAME, 0).edit();
        edit.putLong(PREF_DIAGMON_TIMESTAMP, j);
        edit.apply();
    }

    private static long getPrefDiagmonTimestamp(Context context) {
        return context.getSharedPreferences(PREF_DIAGMON_NAME, 0).getLong(PREF_DIAGMON_TIMESTAMP, 0);
    }

    private static boolean checkAuthority(Context context, String str) {
        try {
            if (DiagMonUtil.checkDMA(context) != 2) {
                return true;
            }
            try {
                Bundle bundle = new Bundle();
                bundle.putString("serviceId", str);
                context.getContentResolver().call(Uri.parse("content://com.sec.android.log.diagmonagent/"), "request_deviceid", "request_deviceid", bundle);
                return true;
            } catch (Exception | IllegalArgumentException unused) {
                return false;
            }
        } catch (Exception | NullPointerException unused2) {
            return false;
        }
    }
}
