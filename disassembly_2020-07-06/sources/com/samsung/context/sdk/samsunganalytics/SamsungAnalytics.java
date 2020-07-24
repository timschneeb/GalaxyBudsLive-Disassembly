package com.samsung.context.sdk.samsunganalytics;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import com.samsung.context.sdk.samsunganalytics.internal.Tracker;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Validation;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.util.Map;
import java.util.Set;

public class SamsungAnalytics {
    public static final String SDK_VERSION = "6.05.025";
    private static SamsungAnalytics instance;
    private Tracker tracker = null;

    private SamsungAnalytics(Application application, Configuration configuration) {
        if (!Validation.isValidConfig(application, configuration)) {
            return;
        }
        if (configuration.isEnableUseInAppLogging() || Validation.isLoggingEnableDevice(application)) {
            this.tracker = new Tracker(application, configuration);
        }
    }

    private static SamsungAnalytics getInstanceAndConfig(Application application, Configuration configuration) {
        SamsungAnalytics samsungAnalytics = instance;
        if (samsungAnalytics == null || samsungAnalytics.tracker == null) {
            synchronized (SamsungAnalytics.class) {
                instance = new SamsungAnalytics(application, configuration);
            }
        }
        return instance;
    }

    public static void setConfiguration(Application application, Configuration configuration) {
        getInstanceAndConfig(application, configuration);
    }

    public static Configuration getConfiguration() {
        Tracker tracker2;
        SamsungAnalytics samsungAnalytics = instance;
        if (samsungAnalytics == null || (tracker2 = samsungAnalytics.tracker) == null) {
            return null;
        }
        return tracker2.getConfiguration();
    }

    public static SamsungAnalytics getInstance() {
        if (instance == null) {
            Utils.throwException("call after setConfiguration() method");
            if (!Utils.isEngBin()) {
                return getInstanceAndConfig((Application) null, (Configuration) null);
            }
        }
        return instance;
    }

    public SamsungAnalytics enableAutoActivityTracking() {
        try {
            this.tracker.enableAutoActivityTracking();
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
        }
        return this;
    }

    public void disableAutoActivityTracking() {
        try {
            this.tracker.disableAutoActivityTracking();
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
        }
    }

    public void restrictNetworkType(int i) {
        try {
            this.tracker.getConfiguration().setRestrictedNetworkType(i);
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
        }
    }

    public boolean isEnableAutoActivityTracking() {
        try {
            return this.tracker.isEnableAutoActivityTracking();
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
            return false;
        }
    }

    public int sendLog(Map<String, String> map) {
        try {
            return this.tracker.sendLog(map, false);
        } catch (NullPointerException unused) {
            return -100;
        }
    }

    public int sendLogSync(Map<String, String> map) {
        try {
            return this.tracker.sendLog(map, true);
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
            return -100;
        }
    }

    public void registerSettingPref(Map<String, Set<String>> map) {
        try {
            this.tracker.registerSettingPref(map);
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
        }
    }

    public void deleteLogData() {
        try {
            this.tracker.changeUserAgreementState(false);
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004e, code lost:
        if (r4 != null) goto L_0x0050;
     */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0058 A[Catch:{ NullPointerException -> 0x006b }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x005f A[Catch:{ NullPointerException -> 0x006b }] */
    public String getDeviceId(Context context) {
        Cursor cursor;
        try {
            if (Build.TYPE.equals("eng") && Build.VERSION.SDK_INT >= 29 && this.tracker.getConfiguration().isEnableAutoDeviceId() && !this.tracker.getConfiguration().isEnableUseInAppLogging()) {
                try {
                    cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.log.diagmonagent.sa/deviceid"), (String[]) null, (Bundle) null, (CancellationSignal) null);
                    if (cursor != null) {
                        try {
                            if (cursor.moveToNext()) {
                                String string = cursor.getString(0);
                                if (cursor != null) {
                                    cursor.close();
                                }
                                return string;
                            }
                        } catch (Exception unused) {
                            if (cursor != null) {
                            }
                            return this.tracker.getConfiguration().getDeviceId();
                        } catch (Throwable th) {
                            th = th;
                            if (cursor != null) {
                            }
                            throw th;
                        }
                    }
                } catch (Exception unused2) {
                    cursor = null;
                    if (cursor != null) {
                        cursor.close();
                    }
                    return this.tracker.getConfiguration().getDeviceId();
                } catch (Throwable th2) {
                    th = th2;
                    cursor = null;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            return this.tracker.getConfiguration().getDeviceId();
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
            return null;
        }
    }
}
