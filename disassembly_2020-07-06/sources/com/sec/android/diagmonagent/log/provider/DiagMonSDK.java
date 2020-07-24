package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.samsung.context.sdk.samsunganalytics.BuildConfig;
import com.sec.android.diagmonagent.log.provider.utils.DiagMonUtil;
import com.sec.android.diagmonagent.log.provider.utils.Validator;
import java.lang.Thread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiagMonSDK {
    private static final String SDK_TYPE_VALUE = "S";
    private static DiagMonProvider elp;
    private static DiagMonSDK instance;
    /* access modifiers changed from: private */
    public static boolean isEnableDefaultConfig = false;
    private static boolean isEnableUncaughtExceptionLogging = false;
    /* access modifiers changed from: private */
    public static DiagMonConfig mConfig = null;
    private static Thread.UncaughtExceptionHandler originUncaughtExceptionHandler;
    /* access modifiers changed from: private */
    public static Bundle srObj;
    private static final Uri uri = Uri.parse("content://com.sec.android.log.diagmonagent/");

    public static String getSDKtype() {
        return SDK_TYPE_VALUE;
    }

    public static DiagMonSDK setConfiguration(DiagMonConfig diagMonConfig) {
        try {
            synchronized (DiagMonSDK.class) {
                Log.i(DiagMonUtil.TAG, "SetConfiguration");
                mConfig = diagMonConfig;
                if (mConfig == null) {
                    Log.w(DiagMonUtil.TAG, "DiagMonConfiguration is null");
                    return null;
                }
                srObj = new Bundle();
                srObj = DiagMonUtil.generateSRobj(mConfig);
                setSRObj(srObj);
                toggleConfigurationStatus(true);
                if (isEnableDefaultConfig) {
                    Log.w(DiagMonUtil.TAG, "You can't use setConfiguration with enableDefaultConfiguration");
                    return null;
                }
                ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
                newSingleThreadExecutor.submit(new ThreadExecutor(mConfig));
                newSingleThreadExecutor.shutdown();
            }
        } catch (Exception e) {
            String str = DiagMonUtil.TAG;
            Log.e(str, "failed to setConfiguration" + e);
        }
        return instance;
    }

    public static boolean customEventReport(Context context, EventBuilder eventBuilder) {
        Log.i(DiagMonUtil.TAG, "Request CustomEventReport");
        boolean z = false;
        try {
            if (mConfig == null) {
                Log.w(DiagMonUtil.TAG, "You first have to create DiagMonConfiguration");
                Log.w(DiagMonUtil.TAG, "CustomEventReport is aborted");
                return false;
            } else if (isEnableDefaultConfig) {
                Log.w(DiagMonUtil.TAG, "You can't use customEventReport with enableDefaultConfiguration");
                return false;
            } else {
                if (eventBuilder.getLogPath() != null) {
                    if (!Validator.isValidLogPath(eventBuilder.getLogPath())) {
                        if (!eventBuilder.mIsCalledNetworkMode && mConfig.isEnabledDefaultNetwork()) {
                            Log.d(DiagMonUtil.TAG, "NetworkMode is applied as DefaultNetwork");
                            eventBuilder.setNetworkMode(mConfig.getDefaultNetworkMode());
                        }
                        int checkDMA = DiagMonUtil.checkDMA(mConfig.getContext());
                        if (checkDMA == 0) {
                            Log.w(DiagMonUtil.TAG, "Not installed DMA");
                        } else if (checkDMA == 1) {
                            z = eventReportViaBR(context, eventBuilder);
                        } else if (checkDMA != 2) {
                            Log.w(DiagMonUtil.TAG, "Exceptional case");
                            Log.w(DiagMonUtil.TAG, "SetConfiguration is aborted");
                        } else {
                            z = eventReportViaCP(context, eventBuilder);
                        }
                        if (!z) {
                            Log.w(DiagMonUtil.TAG, "CustomEventReport is aborted");
                        }
                        return z;
                    }
                }
                Log.w(DiagMonUtil.TAG, "You have to properly set LogPath");
                return false;
            }
        } catch (Exception e) {
            String str = DiagMonUtil.TAG;
            Log.e(str, "failed to customEventReport" + e);
        }
    }

    /* access modifiers changed from: private */
    public static boolean eventReportViaCP(Context context, EventBuilder eventBuilder) {
        try {
            new Bundle();
            Bundle makeEventObjAsBundle = DiagMonUtil.makeEventObjAsBundle(context, mConfig, eventBuilder);
            if (makeEventObjAsBundle == null) {
                Log.w(DiagMonUtil.TAG, "No EventObject");
                return false;
            } else if (mConfig == null) {
                Log.w(DiagMonUtil.TAG, "No Configuration");
                Log.w(DiagMonUtil.TAG, "You have to set DiagMonConfiguration");
                return false;
            } else if (Validator.validateSrObj(mConfig.getContext(), srObj)) {
                Log.w(DiagMonUtil.TAG, "Invalid SR object");
                return false;
            } else if (Validator.validateErObj(mConfig.getContext(), makeEventObjAsBundle, srObj)) {
                Log.w(DiagMonUtil.TAG, "Invalid ER object");
                return false;
            } else {
                Log.d(DiagMonUtil.TAG, "Valid SR, ER object");
                Log.i(DiagMonUtil.TAG, "Report your logs");
                String str = DiagMonUtil.TAG;
                Log.i(str, "networkMode : " + eventBuilder.getNetworkMode());
                DiagMonUtil.printResultfromDMA(context.getContentResolver().call(uri, "event_report", "eventReport", makeEventObjAsBundle));
                String zipPath = eventBuilder.getZipPath();
                if (zipPath.isEmpty()) {
                    return true;
                }
                DiagMonUtil.removeZipFile(zipPath);
                return true;
            }
        } catch (Exception | NullPointerException unused) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public static boolean eventReportViaBR(Context context, EventBuilder eventBuilder) {
        try {
            if (Validator.validateLegacyConfig(mConfig)) {
                Log.w(DiagMonUtil.TAG, "Invalid DiagMonConfiguration");
                return false;
            } else if (Validator.isValidLegacyEventBuilder(eventBuilder)) {
                Log.w(DiagMonUtil.TAG, "Invalid EventBuilder");
                return false;
            } else {
                Log.d(DiagMonUtil.TAG, "Valid EventBuilder");
                context.sendBroadcast(DiagMonUtil.makeEventobjAsIntent(context, mConfig, eventBuilder));
                Log.i(DiagMonUtil.TAG, "Report your logs");
                return true;
            }
        } catch (Exception | NullPointerException unused) {
            return false;
        }
    }

    protected static class DiagMonHelper {
        protected DiagMonHelper() {
        }

        public static void setConfiguration(DiagMonConfig diagMonConfig) {
            synchronized (DiagMonSDK.class) {
                Log.i(DiagMonUtil.TAG, "SetConfiguration");
                DiagMonConfig unused = DiagMonSDK.mConfig = diagMonConfig;
                if (diagMonConfig == null) {
                    Log.w(DiagMonUtil.TAG, "DiagMonConfiguration is null");
                    return;
                }
                Bundle unused2 = DiagMonSDK.srObj = new Bundle();
                Bundle unused3 = DiagMonSDK.srObj = DiagMonUtil.generateSRobj(DiagMonSDK.mConfig);
                DiagMonSDK.setSRObj(DiagMonSDK.srObj);
                ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
                newSingleThreadExecutor.submit(new ThreadExecutor(DiagMonSDK.mConfig));
                newSingleThreadExecutor.shutdown();
            }
        }

        public static void eventReport(Context context, DiagMonConfig diagMonConfig, EventBuilder eventBuilder) {
            DiagMonConfig unused = DiagMonSDK.mConfig = diagMonConfig;
            if (DiagMonUtil.checkDMA(context) == 0) {
                Log.w(DiagMonUtil.TAG, "not installed");
            } else if (DiagMonUtil.checkDMA(context) == 1) {
                Log.d(DiagMonUtil.TAG, "LEGACY DMA");
                setConfiguration(diagMonConfig);
                boolean unused2 = DiagMonSDK.eventReportViaBR(context, eventBuilder);
            } else if (DiagMonUtil.checkDMA(context) == 2) {
                Log.d(DiagMonUtil.TAG, "NEW DMA");
                Bundle unused3 = DiagMonSDK.srObj = new Bundle();
                Bundle unused4 = DiagMonSDK.srObj = DiagMonUtil.generateSRobj(DiagMonSDK.mConfig);
                if (DiagMonSDK.isEnableDefaultConfig) {
                    DiagMonSDK.sendSRObj(DiagMonSDK.srObj);
                }
                boolean unused5 = DiagMonSDK.eventReportViaCP(context, eventBuilder);
            } else {
                Log.d(DiagMonUtil.TAG, "Wrong Status");
            }
        }
    }

    public static boolean sendSRObj(Bundle bundle) {
        try {
            if (Validator.validateSrObj(mConfig.getContext(), bundle)) {
                Log.w(DiagMonUtil.TAG, "Invalid SR object");
                mConfig = null;
                return false;
            }
            Log.i(DiagMonUtil.TAG, "Valid SR object");
            Log.i(DiagMonUtil.TAG, "Request Service Registration");
            DiagMonUtil.printResultfromDMA(mConfig.getContext().getContentResolver().call(uri, "register_service", "registration", bundle));
            return true;
        } catch (Exception | NullPointerException unused) {
            return false;
        }
    }

    public static void initDB(Context context) {
        try {
            if (Build.TYPE.equals("eng")) {
                DiagMonUtil.printResultfromDMA(context.getContentResolver().call(uri, "init_db", "init_db", new Bundle()));
                return;
            }
            Log.w(DiagMonUtil.TAG, "You can use this API on ENG");
        } catch (Exception | NullPointerException unused) {
        }
    }

    public static DiagMonProvider getElp() {
        try {
            return elp;
        } catch (Exception | NullPointerException unused) {
            return null;
        }
    }

    public static DiagMonConfig getConfiguration() {
        try {
            return mConfig;
        } catch (Exception | NullPointerException unused) {
            return null;
        }
    }

    private static void setConfig(DiagMonConfig diagMonConfig) {
        mConfig = diagMonConfig;
    }

    public static String getSDKVersion() {
        try {
            return String.valueOf(BuildConfig.VERSION_CODE);
        } catch (Exception | NullPointerException unused) {
            return "";
        }
    }

    public static void enableUncaughtExceptionLogging(Context context) {
        String str;
        boolean z;
        boolean defaultNetworkMode;
        try {
            if (isEnableUncaughtExceptionLogging) {
                Log.w(DiagMonUtil.TAG, "UncaughtExceptionLogging is already enabled");
            } else if (mConfig == null) {
                Log.w(DiagMonUtil.TAG, "UncaughtExceptionLogging Can't be enabled because Configuration is null");
            } else if (!mConfig.isCustomConfiguration() || !isEnableDefaultConfig) {
                String str2 = "D";
                if (isEnableDefaultConfig) {
                    defaultNetworkMode = mConfig.getDefaultNetworkMode();
                } else if (mConfig.isCustomConfiguration()) {
                    defaultNetworkMode = mConfig.isEnabledDefaultNetwork() ? mConfig.getDefaultNetworkMode() : true;
                    str2 = mConfig.getAgreeAsString();
                } else {
                    Log.i(DiagMonUtil.TAG, "value for uncaughtException will be default");
                    str = str2;
                    z = true;
                    isEnableUncaughtExceptionLogging = true;
                    originUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
                    Thread.setDefaultUncaughtExceptionHandler(new DiagMonLogger(context, originUncaughtExceptionHandler, mConfig, z, str));
                }
                z = defaultNetworkMode;
                str = str2;
                isEnableUncaughtExceptionLogging = true;
                originUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
                Thread.setDefaultUncaughtExceptionHandler(new DiagMonLogger(context, originUncaughtExceptionHandler, mConfig, z, str));
            } else {
                Log.w(DiagMonUtil.TAG, "UncaughtException Logging and SetConfiguration can't be used at the same time");
            }
        } catch (Exception e) {
            String str3 = DiagMonUtil.TAG;
            Log.e(str3, "failed to enableUncaughtExceptionLogging" + e);
        }
    }

    public static void disableUncaughtExceptionLogging() {
        try {
            toggleConfigurationStatus(true);
            if (originUncaughtExceptionHandler != null) {
                isEnableUncaughtExceptionLogging = false;
                Thread.setDefaultUncaughtExceptionHandler(originUncaughtExceptionHandler);
            }
        } catch (Exception | NullPointerException unused) {
        }
    }

    public boolean isEnableUncaughtExceptionLogging() {
        try {
            return isEnableUncaughtExceptionLogging;
        } catch (Exception | NullPointerException unused) {
            return false;
        }
    }

    public static void setDefaultConfiguration(Context context, String str) {
        try {
            if (mConfig == null) {
                mConfig = new DiagMonConfig(context).setServiceId(str).setAgree("D");
                toggleConfigurationStatus(false);
            } else if (mConfig.isCustomConfiguration()) {
                Log.w(DiagMonUtil.TAG, "setDefaultConfiguration can't be used because CustomLogging is using");
            } else {
                Log.w(DiagMonUtil.TAG, "setDefaultConfiguration is already set");
            }
        } catch (Exception | NullPointerException unused) {
        }
    }

    public static void disableDefaultConfiguration() {
        try {
            toggleConfigurationStatus(true);
        } catch (Exception | NullPointerException unused) {
        }
    }

    public static boolean isEnableDefaultConfiguration() {
        try {
            return isEnableDefaultConfig;
        } catch (Exception | NullPointerException unused) {
            return false;
        }
    }

    protected static void toggleConfigurationStatus(boolean z) {
        DiagMonConfig diagMonConfig = mConfig;
        if (diagMonConfig == null) {
            Log.w(DiagMonUtil.TAG, "can't handle toggleConfigurationStatus");
        } else if (z) {
            isEnableDefaultConfig = false;
            diagMonConfig.setCustomConfigStatus(true);
            Log.d(DiagMonUtil.TAG, "Status is chaged to CustomLogging");
        } else {
            isEnableDefaultConfig = true;
            diagMonConfig.setCustomConfigStatus(false);
            Log.d(DiagMonUtil.TAG, "Status is chaged to UncaughtException");
        }
    }

    public static String getDeviceId(Context context, String str) {
        if (DiagMonUtil.checkDMA(context) == 2) {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("serviceId", str);
                Bundle call = context.getContentResolver().call(Uri.parse("content://com.sec.android.log.diagmonagent/"), "request_deviceid", "request_deviceid", bundle);
                if (call.getString("result") == null) {
                    Log.i(DiagMonUtil.TAG, "Can't find deviceId from DMA");
                    return "";
                }
                DiagMonUtil.printResultfromDMA(call);
                return call.getString("result");
            } catch (Exception | IllegalArgumentException unused) {
                return "";
            }
        } else {
            Log.w(DiagMonUtil.TAG, "It is not supported because DiagMonAgent is an old version");
            return "";
        }
    }

    protected static void setSRObj(Bundle bundle) {
        srObj = bundle;
    }
}
