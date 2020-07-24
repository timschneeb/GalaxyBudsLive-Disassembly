package com.samsung.context.sdk.samsunganalytics.internal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.DBOpenHelper;
import com.samsung.context.sdk.samsunganalytics.LogBuilders;
import com.samsung.context.sdk.samsunganalytics.UserAgreement;
import com.samsung.context.sdk.samsunganalytics.internal.connection.Directory;
import com.samsung.context.sdk.samsunganalytics.internal.connection.Domain;
import com.samsung.context.sdk.samsunganalytics.internal.device.DeviceInfo;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.executor.SingleThreadExecutor;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Constants;
import com.samsung.context.sdk.samsunganalytics.internal.policy.PolicyUtils;
import com.samsung.context.sdk.samsunganalytics.internal.sender.DMA.DMALogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.Sender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.Manager;
import com.samsung.context.sdk.samsunganalytics.internal.setting.RegisterClient;
import com.samsung.context.sdk.samsunganalytics.internal.terms.RegisterTask;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;

public class Tracker {
    public static final int AUID_TYPE_DMA = 4;
    public static final int AUID_TYPE_FROM_CF = 0;
    public static final int AUID_TYPE_INAPP = 2;
    public static final int AUID_TYPE_MAKE_SDK = 1;
    public static final int AUID_TYPE_UNKNOWN = -1;
    public static final int DEVICE_ID_BIT_NUM = 128;
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;
    /* access modifiers changed from: private */
    public Application application;
    /* access modifiers changed from: private */
    public Configuration configuration;
    private boolean isEnableAutoActivityTracking = false;

    public Tracker(Application application2, Configuration configuration2) {
        String str;
        this.application = application2;
        this.configuration = configuration2;
        final Context applicationContext = application2.getApplicationContext();
        if (!TextUtils.isEmpty(configuration2.getDeviceId())) {
            this.configuration.setAuidType(2);
        } else if (!loadDeviceId() && configuration2.isEnableAutoDeviceId() && (configuration2.isEnableUseInAppLogging() || PolicyUtils.getSenderType() == 1)) {
            setDeviceId(generateRandomDeviceId(), 1);
        }
        if (PolicyUtils.getSenderType() == 0) {
            getPolicy();
        }
        if (!configuration2.isEnableUseInAppLogging()) {
            this.configuration.setUserAgreement(new UserAgreement() {
                public boolean isAgreement() {
                    return Utils.isDiagnosticAgree(applicationContext);
                }
            });
        }
        if (isUserAgreement()) {
            if (configuration2.isEnableFastReady()) {
                Sender.get(application2, PolicyUtils.getSenderType(), configuration2);
            }
            if (PolicyUtils.getSenderType() == 3) {
                SharedPreferences preferences = Preferences.getPreferences(applicationContext);
                try {
                    str = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    Debug.LogException(getClass(), e);
                    str = "";
                }
                boolean z = preferences.getBoolean(Preferences.PREFS_KEY_SEND_COMMON_SUCCESS, false);
                String string = preferences.getString(Preferences.PREFS_KEY_APP_VERSION, "None");
                Long valueOf = Long.valueOf(preferences.getLong(Preferences.PREFS_KEY_SEND_COMMON_TIME, 0));
                Debug.LogD("AppVersion = " + str + ", prefAppVerison = " + string + ", beforeSendCommonTime = " + valueOf + ", success = " + z);
                if (!str.equals(string) || ((z && Utils.compareDays(7, valueOf)) || (!z && Utils.compareHours(6, valueOf)))) {
                    Debug.LogD("send Common!!");
                    preferences.edit().putString(Preferences.PREFS_KEY_APP_VERSION, str).putLong(Preferences.PREFS_KEY_SEND_COMMON_TIME, System.currentTimeMillis()).apply();
                    ((DMALogSender) Sender.get(application2, 3, configuration2)).sendCommon();
                }
            }
        }
        Utils.sendSettings(applicationContext, configuration2);
        sendPreviousUserAgreementState();
        Debug.LogD("Tracker", "Tracker start:6.05.025 , senderType : " + PolicyUtils.getSenderType());
    }

    private void getPolicy() {
        SharedPreferences preferences = Preferences.getPreferences(this.application);
        Domain.DLS.setDomain(preferences.getString(Constants.KEY_DLS_DOMAIN, ""));
        Directory.DLS_DIR.setDirectory(preferences.getString(Constants.KEY_DLS_URI, ""));
        Directory.DLS_DIR_BAT.setDirectory(preferences.getString(Constants.KEY_DLS_URI_BAT, ""));
        if (PolicyUtils.isPolicyExpired(this.application.getApplicationContext())) {
            PolicyUtils.getPolicy(this.application, this.configuration, SingleThreadExecutor.getInstance(), new DeviceInfo(this.application), new Callback<Void, Boolean>() {
                public Void onResult(Boolean bool) {
                    if (!bool.booleanValue()) {
                        return null;
                    }
                    DBOpenHelper dbOpenHelper = Tracker.this.configuration.getDbOpenHelper();
                    if (dbOpenHelper == null) {
                        Manager.getInstance(Tracker.this.application.getApplicationContext(), Tracker.this.configuration).enableDatabaseBuffering(Tracker.this.application.getApplicationContext());
                        return null;
                    }
                    Manager.getInstance(Tracker.this.application.getApplicationContext(), Tracker.this.configuration).enableDatabaseBuffering(dbOpenHelper);
                    return null;
                }
            });
        }
    }

    public void enableAutoActivityTracking() {
        this.application.registerActivityLifecycleCallbacks(makeActivityLifecycleCallbacks());
    }

    public void disableAutoActivityTracking() {
        Application.ActivityLifecycleCallbacks activityLifecycleCallbacks2 = this.activityLifecycleCallbacks;
        if (activityLifecycleCallbacks2 != null) {
            this.application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks2);
        }
    }

    public boolean isEnableAutoActivityTracking() {
        return this.isEnableAutoActivityTracking;
    }

    private Application.ActivityLifecycleCallbacks makeActivityLifecycleCallbacks() {
        Application.ActivityLifecycleCallbacks activityLifecycleCallbacks2 = this.activityLifecycleCallbacks;
        if (activityLifecycleCallbacks2 != null) {
            return activityLifecycleCallbacks2;
        }
        this.activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            public void onActivityDestroyed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivityStarted(Activity activity) {
                Tracker.this.sendLog(((LogBuilders.ScreenViewBuilder) new LogBuilders.ScreenViewBuilder().setScreenView(activity.getComponentName().getShortClassName())).build(), false);
            }
        };
        return this.activityLifecycleCallbacks;
    }

    public int sendLog(Map<String, String> map, boolean z) {
        if (!isUserAgreement()) {
            Debug.LogD("user do not agree");
            return -2;
        } else if (map == null || map.isEmpty()) {
            Debug.LogD("Failure to send Logs : No data");
            return -3;
        } else if (!checkDeviceId()) {
            return -5;
        } else {
            if (map.get("t").equals("pp") && !isSendProperty()) {
                return -9;
            }
            if (z) {
                return Sender.get(this.application, PolicyUtils.getSenderType(), this.configuration).sendSync(map);
            }
            return Sender.get(this.application, PolicyUtils.getSenderType(), this.configuration).send(map);
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    private void setDeviceId(String str, int i) {
        Preferences.getPreferences(this.application.getApplicationContext()).edit().putString("deviceId", str).putInt(Preferences.PREFS_KEY_DID_TYPE, i).apply();
        this.configuration.setAuidType(i);
        this.configuration.setDeviceId(str);
    }

    private boolean loadDeviceId() {
        SharedPreferences preferences = Preferences.getPreferences(this.application);
        String string = preferences.getString("deviceId", "");
        int i = preferences.getInt(Preferences.PREFS_KEY_DID_TYPE, -1);
        if (TextUtils.isEmpty(string) || string.length() != 32 || i == -1) {
            return false;
        }
        this.configuration.setAuidType(i);
        this.configuration.setDeviceId(string);
        return true;
    }

    private String generateRandomDeviceId() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bArr = new byte[16];
        StringBuilder sb = new StringBuilder(32);
        int i = 0;
        while (i < 32) {
            secureRandom.nextBytes(bArr);
            try {
                sb.append("0123456789abcdefghijklmjopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt((int) (Math.abs(new BigInteger(bArr).longValue()) % ((long) 62))));
                i++;
            } catch (Exception e) {
                Debug.LogException(getClass(), e);
                return null;
            }
        }
        return sb.toString();
    }

    public void registerSettingPref(Map<String, Set<String>> map) {
        Context applicationContext = this.application.getApplicationContext();
        SingleThreadExecutor.getInstance().execute(new RegisterClient(applicationContext, map));
        Utils.sendSettings(applicationContext, this.configuration);
    }

    private boolean isUserAgreement() {
        return this.configuration.getUserAgreement().isAgreement();
    }

    private boolean checkDeviceId() {
        if (PolicyUtils.getSenderType() >= 2 || !TextUtils.isEmpty(this.configuration.getDeviceId())) {
            return true;
        }
        Debug.LogD("did is empty");
        return false;
    }

    private boolean isSendProperty() {
        if (!Utils.compareDays(1, Long.valueOf(Preferences.getPreferences(this.application).getLong(Preferences.PROPERTY_SENT_DATE, 0)))) {
            Debug.LogD("do not send property < 1day");
            return false;
        }
        Preferences.getPreferences(this.application).edit().putLong(Preferences.PROPERTY_SENT_DATE, System.currentTimeMillis()).apply();
        return true;
    }

    public void changeUserAgreementState(boolean z) {
        if (!z && this.configuration.isEnableUseInAppLogging()) {
            if (PolicyUtils.getSenderType() >= 2) {
                Intent intent = new Intent();
                intent.setPackage("com.sec.android.diagmonagent");
                intent.setAction("com.sec.android.diagmonagent.sa.terms.DELETE_APP_DATA");
                intent.putExtra("tid", this.configuration.getTrackingId());
                intent.putExtra("agree", z);
                this.application.sendBroadcast(intent);
                if (PolicyUtils.getSenderType() == 2) {
                    ((DMALogSender) Sender.get(this.application, 2, this.configuration)).reset();
                }
            }
            sendUserAgreementState();
            if (this.configuration.getAuidType() == 1) {
                setDeviceId(generateRandomDeviceId(), 1);
            }
        }
    }

    private void sendPreviousUserAgreementState() {
        final SharedPreferences sharedPreferences = this.application.getSharedPreferences(Preferences.TERMS_PREF_NAME, 0);
        for (Map.Entry next : sharedPreferences.getAll().entrySet()) {
            final String str = (String) next.getKey();
            SingleThreadExecutor.getInstance().execute(new RegisterTask(this.configuration.getTrackingId(), str, ((Long) next.getValue()).longValue(), new AsyncTaskCallback() {
                public void onFail(int i, String str, String str2, String str3) {
                }

                public void onSuccess(int i, String str, String str2, String str3) {
                    sharedPreferences.edit().remove(str).apply();
                }
            }));
        }
    }

    private void sendUserAgreementState() {
        sendPreviousUserAgreementState();
        final long currentTimeMillis = System.currentTimeMillis();
        final String deviceId = this.configuration.getDeviceId();
        SingleThreadExecutor.getInstance().execute(new RegisterTask(this.configuration.getTrackingId(), deviceId, currentTimeMillis, new AsyncTaskCallback() {
            public void onSuccess(int i, String str, String str2, String str3) {
            }

            public void onFail(int i, String str, String str2, String str3) {
                Tracker.this.application.getSharedPreferences(Preferences.TERMS_PREF_NAME, 0).edit().putLong(deviceId, currentTimeMillis).apply();
            }
        }));
    }
}
