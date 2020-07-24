package com.sec.android.diagmonagent.log.provider.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.accessorydm.interfaces.XDBInterface;
import com.sec.android.diagmonagent.log.provider.DiagMonConfig;
import com.sec.android.diagmonagent.log.provider.DiagMonSDK;
import com.sec.android.diagmonagent.log.provider.EventBuilder;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class DiagMonUtil {
    public static final String DESCRIPTION = "errorDesc";
    public static final String DEVICE_ID = "deviceId";
    public static final String DMA_PKG_NAME = "com.sec.android.diagmonagent";
    private static final int DMA_SUPPORT_VERSION = 600000000;
    public static final String ERROR_CODE = "errorCode";
    public static final String EXTRA_DATA = "extension";
    public static final String FILE_DESCRIPTOR = "fileDescriptor";
    public static final int LEGACY_DMA = 1;
    public static final String MEMORY = "memory";
    public static final String NETWORK_MODE = "wifiOnly";
    public static final int NEW_DMA = 2;
    public static final int NO_DMA = 0;
    public static final String RELAY_CLIENT_TYPE = "relayClientType";
    public static final String RELAY_CLIENT_VER = "relayClientVersion";
    public static final String SDK_TYPE = "sdkType";
    public static final String SDK_VERSION = "sdkVersion";
    public static final String SERVICE_AGREE = "serviceAgreeType";
    public static final String SERVICE_DEFINED_KEY = "serviceDefinedKey";
    public static final String SERVICE_ID = "serviceId";
    public static final String SERVICE_VER = "serviceVersion";
    public static final String STORAGE = "storage";
    public static final String TAG = ("DIAGMON_SDK[" + DiagMonSDK.getSDKVersion() + "]");
    public static final String TRACKING_ID = "trackingId";
    static boolean hasDMA = false;
    private static final String intentNameApp = "com.sec.android.diagmonagent.intent.REPORT_ERROR_APP";
    private static final String intentNameSystem = "com.sec.android.diagmonagent.intent.REPORT_ERROR_V2";

    public static Bundle generateSRobj(DiagMonConfig diagMonConfig) {
        Bundle bundle = new Bundle();
        bundle.putString("serviceId", diagMonConfig.getServiceId());
        bundle.putString(SERVICE_VER, getPackageVersion(diagMonConfig.getContext()));
        bundle.putString(SERVICE_AGREE, diagMonConfig.getAgreeAsString());
        bundle.putString("deviceId", diagMonConfig.getDeviceId());
        bundle.putString(TRACKING_ID, diagMonConfig.getTrackingId());
        bundle.putString(SDK_VERSION, DiagMonSDK.getSDKVersion());
        bundle.putString(SDK_TYPE, DiagMonSDK.getSDKtype());
        Log.i(TAG, "generated SR object");
        return bundle;
    }

    public static Intent makeEventobjAsIntent(Context context, DiagMonConfig diagMonConfig, EventBuilder eventBuilder) {
        Intent intent;
        JSONObject jSONObject = new JSONObject();
        if (getUid(context) == 1000) {
            intent = new Intent(intentNameSystem);
        } else {
            intent = new Intent(intentNameApp);
        }
        Bundle bundle = new Bundle();
        intent.addFlags(32);
        bundle.putBundle("DiagMon", new Bundle());
        bundle.getBundle("DiagMon").putBundle("CFailLogUpload", new Bundle());
        bundle.getBundle("DiagMon").getBundle("CFailLogUpload").putString("ServiceID", diagMonConfig.getServiceId());
        bundle.getBundle("DiagMon").getBundle("CFailLogUpload").putBundle("Ext", new Bundle());
        bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("ClientV", getPackageVersion(context));
        if (!TextUtils.isEmpty(eventBuilder.getRelayClientType())) {
            bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("RelayClient", eventBuilder.getRelayClientType());
        }
        if (!TextUtils.isEmpty(eventBuilder.getRelayClientVer())) {
            bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("RelayClientV", eventBuilder.getRelayClientVer());
        }
        bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("UiMode", "0");
        bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString(XDBInterface.XDM_SQL_LAST_UPDATE_RESULTCODE, eventBuilder.getErrorCode());
        if (!TextUtils.isEmpty(eventBuilder.getServiceDefinedKey())) {
            bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("EventID", eventBuilder.getServiceDefinedKey());
        }
        try {
            jSONObject.put("SasdkV", "6.05.025");
            jSONObject.put("SdkV", DiagMonSDK.getSDKVersion());
            jSONObject.put("TrackingID", diagMonConfig.getTrackingId());
            jSONObject.put(XDBInterface.XDM_SQL_LAST_UPDATE_DESCRIPTION, eventBuilder.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString(XDBInterface.XDM_SQL_LAST_UPDATE_DESCRIPTION, jSONObject.toString());
        if (eventBuilder.getNetworkMode()) {
            bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("WifiOnlyFeature", "1");
        } else {
            bundle.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("WifiOnlyFeature", "0");
        }
        intent.putExtra("uploadMO", bundle);
        intent.setFlags(32);
        Log.i(TAG, "EventObject is generated");
        return intent;
    }

    public static Bundle makeEventObjAsBundle(Context context, DiagMonConfig diagMonConfig, EventBuilder eventBuilder) {
        Bundle bundle = new Bundle();
        try {
            bundle.putParcelable(FILE_DESCRIPTOR, collectLogs(context, eventBuilder));
            bundle.putString("serviceId", diagMonConfig.getServiceId());
            bundle.putString(SERVICE_VER, diagMonConfig.getServiceVer());
            bundle.putString(SERVICE_DEFINED_KEY, eventBuilder.getServiceDefinedKey());
            bundle.putString(ERROR_CODE, eventBuilder.getErrorCode());
            bundle.putBoolean(NETWORK_MODE, eventBuilder.getNetworkMode());
            bundle.putString(DESCRIPTION, eventBuilder.getDescription());
            bundle.putString(RELAY_CLIENT_VER, eventBuilder.getRelayClientVer());
            bundle.putString(RELAY_CLIENT_TYPE, eventBuilder.getRelayClientType());
            bundle.putString(EXTRA_DATA, eventBuilder.getExtData());
            bundle.putString("deviceId", diagMonConfig.getDeviceId());
            bundle.putString(SERVICE_AGREE, diagMonConfig.getAgreeAsString());
            bundle.putString(SDK_VERSION, DiagMonSDK.getSDKVersion());
            bundle.putString(SDK_TYPE, DiagMonSDK.getSDKtype());
            bundle.putString(MEMORY, eventBuilder.getMemory().toString());
            bundle.putString(STORAGE, eventBuilder.getInternalStorageSize().toString());
            Log.d(TAG, "Generated EventObject");
            return bundle;
        } catch (Exception unused) {
            return null;
        }
    }

    public static int checkDMA(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.sec.android.diagmonagent", 0).versionCode < DMA_SUPPORT_VERSION ? 1 : 2;
        } catch (PackageManager.NameNotFoundException e) {
            String str = TAG;
            Log.w(str, "DiagMonAgent isn't found: " + e.getMessage());
            return 0;
        }
    }

    public static String getPackageVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                return packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
            }
            return "";
        } catch (PackageManager.NameNotFoundException unused) {
            String str = TAG;
            Log.e(str, context.getPackageName() + " is not found");
            return "";
        }
    }

    public static int getUid(Context context) {
        return context.getApplicationInfo().uid;
    }

    public static boolean isErrorLogAgreed(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "samsung_errorlog_agree", 0) == 1;
    }

    public static ParcelFileDescriptor collectLogs(Context context, EventBuilder eventBuilder) throws Exception {
        ParcelFileDescriptor parcelFileDescriptor;
        if (eventBuilder.getLogPath() == null || TextUtils.isEmpty(eventBuilder.getLogPath())) {
            Log.w(TAG, "No Log Path, You have to set LogPath to report logs");
            throw new IOException("Not found");
        }
        try {
            String valueOf = String.valueOf(System.currentTimeMillis());
            File file = new File(context.getFilesDir().getAbsolutePath() + "/zip");
            file.mkdir();
            String absolutePath = file.getAbsolutePath();
            String logPath = eventBuilder.getLogPath();
            String zip = ZipHelper.zip(logPath, absolutePath + "/" + valueOf + ".zip");
            parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = ParcelFileDescriptor.open(new File(zip), 268435456);
                eventBuilder.setZipFilePath(zip);
                Log.d(TAG, "Zipping logs is completed");
                String str = TAG;
                Log.d(str, "Zipped file size : " + String.valueOf(parcelFileDescriptor.getStatSize()));
                return parcelFileDescriptor;
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            } catch (Throwable unused) {
            }
        } catch (Exception e2) {
            Log.w(TAG, "Zipping failure");
            String str2 = TAG;
            Log.w(str2, "Exception : " + e2.getMessage());
            throw e2;
        }
        return parcelFileDescriptor;
    }

    public static void removeZipFile(String str) {
        File file = new File(str);
        if (!file.exists()) {
            String str2 = TAG;
            Log.w(str2, "File is not found : " + str);
        } else if (file.delete()) {
            String str3 = TAG;
            Log.d(str3, "Removed zipFile : " + str);
        } else {
            String str4 = TAG;
            Log.w(str4, "Coudn't removed zipFile : " + str);
        }
    }

    public static void printResultfromDMA(Bundle bundle) {
        try {
            String string = bundle.getString("serviceId");
            String string2 = bundle.getString("result");
            String string3 = bundle.getString("cause");
            if (string3 == null) {
                String str = TAG;
                Log.i(str, "Service ID : " + string + ", results : " + string2);
                return;
            }
            String str2 = TAG;
            Log.i(str2, "Service ID : " + string + ", Results : " + string2 + ", Cause : " + string3);
        } catch (NullPointerException e) {
            Log.w(TAG, e.getMessage());
        } catch (Exception e2) {
            Log.w(TAG, e2.getMessage());
        }
    }
}
