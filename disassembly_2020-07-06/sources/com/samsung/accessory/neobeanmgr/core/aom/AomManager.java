package com.samsung.accessory.neobeanmgr.core.aom;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import seccompat.com.samsung.android.feature.CscFeature;
import seccompat.com.samsung.android.feature.FloatingFeature;

public class AomManager {
    private static final String ACTION_AOM_STATE_CHANGED = "samsung.intent.action.AOM_STATE_CHANGED";
    private static final String ACTION_AOM_WAKEUP_COMMAND = "samsung.intent.action.WAKEUP_COMMAND";
    private static final String CLASSNAME_BIXBY_WAKEUP = "com.samsung.android.bixby.wakeup.ExternalWakeupService";
    private static final String EXTRA_AOM_STATE = "samsung.android.voicewakeup.extra.AOM_STATE";
    private static final String EXTRA_VOICEWAKEUP_LOCALE = "samsung.android.voicewakeup.extra.LOCALE";
    private static final String PACKAGENAME_BIXBY_AGENT = "com.samsung.android.bixby.agent";
    private static final String PACKAGENAME_BIXBY_WAKEUP = "com.samsung.android.bixby.wakeup";
    private static final String TAG = "NeoBean_AomManager";
    private static final String URI_BIXBY_LOCALE = "content://com.samsung.android.bixby.agent.common.settings.public/bixby_locale";
    private static final String URI_CHECK_BIXBY_OOB = "content://com.samsung.android.bixby.agent.common.settings.public/bixby_provision_completed";
    private String[] bixbyLanguage = {"de-DE", "en-GB", "en-US", "es-ES", "fr-FR", "it-IT", "ko-KR", "pt-BR", "zh-CN"};
    /* access modifiers changed from: private */
    public final ContentObserver bixbyLocaleCheckObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            super.onChange(z);
            Log.d(AomManager.TAG, "bixbyLocaleCheckObserver");
            if (!Application.getCoreService().isConnected()) {
                Log.d(AomManager.TAG, "not connected");
            } else {
                AomManager.this.sendSPPMessageBixbyLanguage();
            }
        }
    };
    /* access modifiers changed from: private */
    public CoreService coreService;
    private final BroadcastReceiver mConnectedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(AomManager.TAG, "onReceive() : " + intent.getAction());
            if (intent.getAction() != null) {
                String action = intent.getAction();
                char c = 65535;
                int hashCode = action.hashCode();
                if (hashCode != -1354974214) {
                    if (hashCode == -415576694 && action.equals(CoreService.ACTION_DEVICE_CONNECTED)) {
                        c = 0;
                    }
                } else if (action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) {
                    c = 1;
                }
                if (c == 0) {
                    try {
                        AomManager.this.mContext.getContentResolver().unregisterContentObserver(AomManager.this.bixbyLocaleCheckObserver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        AomManager.this.mContext.getContentResolver().registerContentObserver(Uri.parse(AomManager.URI_BIXBY_LOCALE), false, AomManager.this.bixbyLocaleCheckObserver);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    AomManager.this.sendSPPMessageBixbyLanguage();
                } else if (c == 1) {
                    try {
                        AomManager.this.mContext.getContentResolver().unregisterContentObserver(AomManager.this.bixbyLocaleCheckObserver);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    if (AomManager.this.mWakeupListeningStatus) {
                        AomManager.this.setBixbyMic(false);
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    private final BroadcastReceiver mPackageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(AomManager.TAG, "onReceive() : " + intent.getAction());
            if (intent.getAction() != null) {
                String schemeSpecificPart = intent.getData() != null ? intent.getData().getSchemeSpecificPart() : "";
                Log.w(AomManager.TAG, "onReceive() packageName : " + schemeSpecificPart);
                String action = intent.getAction();
                char c = 65535;
                switch (action.hashCode()) {
                    case -810471698:
                        if (action.equals("android.intent.action.PACKAGE_REPLACED")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 267468725:
                        if (action.equals("android.intent.action.PACKAGE_DATA_CLEARED")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 525384130:
                        if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1544582882:
                        if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                            c = 0;
                            break;
                        }
                        break;
                }
                if (c == 0 || c == 1) {
                    if (schemeSpecificPart.equals("com.samsung.android.bixby.agent") && Application.getCoreService().isConnected()) {
                        try {
                            AomManager.this.mContext.getContentResolver().unregisterContentObserver(AomManager.this.bixbyLocaleCheckObserver);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            AomManager.this.mContext.getContentResolver().registerContentObserver(Uri.parse(AomManager.URI_BIXBY_LOCALE), false, AomManager.this.bixbyLocaleCheckObserver);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                } else if ((c == 2 || c == 3) && schemeSpecificPart.equals("com.samsung.android.bixby.agent")) {
                    Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.SET_VOICE_WAKE_UP, (byte) 0));
                    AomManager.this.coreService.getEarBudsInfo().voiceWakeUp = false;
                    SamsungAnalyticsUtil.setStatusString(SA.Status.VOICE_WAKE_UP, AomManager.this.coreService.getEarBudsInfo().voiceWakeUp ? "1" : "0");
                }
            }
        }
    };
    private BroadcastReceiver mUnpairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(AomManager.TAG, "onReceive() : " + intent.getAction());
            if (intent.getAction() != null && intent.getAction().equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
                int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", Integer.MIN_VALUE);
                if (!((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress().equals(UhmFwUtil.getLastLaunchDeviceId())) {
                    return;
                }
                if (intExtra == 10 && intExtra != intExtra2) {
                    AomManager.this.setAomEnable(false);
                } else if (intExtra == 12 && intExtra != intExtra2 && Preferences.getBoolean(PreferenceKey.SETUP_WIZARD_DONE, false, UhmFwUtil.getLastLaunchDeviceId())) {
                    AomManager.this.setAomEnable(true);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mWakeupListeningStatus = false;
    private String mbixbyLocale = "";

    public AomManager(Context context) {
        this.mContext = context;
        this.coreService = Application.getCoreService();
        onCreate();
    }

    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_DEVICE_CONNECTED);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        this.mContext.registerReceiver(this.mConnectedReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        intentFilter2.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter2.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter2.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter2.addDataScheme("package");
        this.mContext.registerReceiver(this.mPackageReceiver, intentFilter2);
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        this.mContext.registerReceiver(this.mUnpairReceiver, intentFilter3);
    }

    public void destroy() {
        this.mContext.unregisterReceiver(this.mConnectedReceiver);
        this.mContext.unregisterReceiver(this.mPackageReceiver);
        this.mContext.unregisterReceiver(this.mUnpairReceiver);
    }

    /* access modifiers changed from: private */
    public void sendSPPMessageBixbyLanguage() {
        Cursor query = this.mContext.getContentResolver().query(Uri.parse(URI_BIXBY_LOCALE), (String[]) null, (String) null, (String[]) null, (String) null);
        if (query != null && query.moveToNext()) {
            int i = 0;
            String string = query.getString(0);
            while (true) {
                String[] strArr = this.bixbyLanguage;
                if (i >= strArr.length) {
                    i = -1;
                    break;
                } else if (strArr[i].equals(string)) {
                    this.mbixbyLocale = this.bixbyLanguage[i];
                    break;
                } else {
                    i++;
                }
            }
            Log.d(TAG, "sendSPPMessageBixbyLanguage : " + this.mbixbyLocale);
            int i2 = i + 1;
            this.coreService.sendSppMessage(new MsgSimple(MsgID.VOICE_WAKE_UP_LANGUAGE, (byte) i2));
            this.coreService.getEarBudsInfo().voiceWakeUpLanguage = i2;
        }
        Util.safeClose(query);
    }

    public void setAomEnable(boolean z) {
        Log.d(TAG, "setAomEnable : " + z);
        this.mContext.getPackageManager().setComponentEnabledSetting(new ComponentName(this.mContext, AomReceiver.class), z ? 1 : 2, 1);
    }

    public void startBixby() {
        Log.d(TAG, "startBixby()");
        if (!isSupportAOM()) {
            Log.d(TAG, "startBixby : not support!");
        } else if (this.coreService.getConnectedDevice() != null) {
            try {
                Intent intent = new Intent(ACTION_AOM_WAKEUP_COMMAND);
                intent.putExtra("android.bluetooth.device.extra.DEVICE", this.coreService.getConnectedDevice());
                intent.setComponent(new ComponentName(PACKAGENAME_BIXBY_WAKEUP, CLASSNAME_BIXBY_WAKEUP));
                this.mContext.startService(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "bixby is disable!");
            }
        }
    }

    public void setBixbyMic(boolean z) {
        Log.d(TAG, "setBixbyMic : " + z);
        if (!isSupportAOM()) {
            Log.d(TAG, "setBixbyMic : not support!");
            return;
        }
        try {
            Intent intent = new Intent(ACTION_AOM_STATE_CHANGED);
            intent.putExtra("android.bluetooth.device.extra.DEVICE", this.coreService.getConnectedDevice());
            intent.putExtra(EXTRA_VOICEWAKEUP_LOCALE, this.mbixbyLocale);
            intent.putExtra(EXTRA_AOM_STATE, z);
            intent.setComponent(new ComponentName(PACKAGENAME_BIXBY_WAKEUP, CLASSNAME_BIXBY_WAKEUP));
            this.mContext.startService(intent);
            this.mWakeupListeningStatus = z;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "bixby is disable!");
        }
    }

    public static boolean isCompleteBixbyOOB() {
        Cursor query = Application.getContext().getContentResolver().query(Uri.parse(URI_CHECK_BIXBY_OOB), (String[]) null, (String) null, (String[]) null, (String) null);
        boolean z = false;
        if (query != null && query.moveToNext()) {
            z = "true".equalsIgnoreCase(query.getString(0));
        }
        if (query != null) {
            query.close();
        }
        return z;
    }

    public boolean isSupportAOM() {
        boolean checkEnabledBixby = checkEnabledBixby();
        boolean checkBixbyAgentSupported = checkBixbyAgentSupported();
        boolean checkBixbyWakeupSupported = checkBixbyWakeupSupported();
        boolean isCompleteBixbyOOB = isCompleteBixbyOOB();
        Log.d(TAG, "isSupportAOM() : " + checkEnabledBixby + " " + checkBixbyAgentSupported + " " + checkBixbyWakeupSupported + " " + isCompleteBixbyOOB);
        return checkEnabledBixby && checkBixbyAgentSupported && checkBixbyWakeupSupported && isCompleteBixbyOOB;
    }

    public boolean isCompleteUpdate() {
        return checkEnabledBixby() && checkBixbyAgentSupported() && checkBixbyWakeupSupported();
    }

    public boolean checkEnabledBixby() {
        boolean z;
        if (new Intent("android.intent.action.VIEW", Uri.parse("bixbyvoice://com.samsung.android.bixby.agent/GoToFeature?featureName=AssistantHome")).resolveActivity(Application.getContext().getPackageManager()) != null) {
            z = true;
        } else {
            Log.d(TAG, "bixby not support");
            z = false;
        }
        if (!Util.isSamsungDevice() || !FloatingFeature.getInstance().getSupportBixby("SEC_FLOATING_FEATURE_COMMON_SUPPORT_BIXBY") || CscFeature.getInstance().getBoolean("CscFeature_Common_DisableBixby") || !z) {
            return false;
        }
        return true;
    }

    private boolean checkBixbyAgentSupported() {
        boolean z;
        Cursor cursor;
        try {
            cursor = Application.getContext().getContentResolver().query(Uri.parse(URI_BIXBY_LOCALE), (String[]) null, (String) null, (String[]) null, (String) null);
            z = true;
        } catch (Exception e) {
            e.printStackTrace();
            z = false;
            cursor = null;
        }
        if (cursor != null) {
            cursor.close();
        }
        return z;
    }

    private boolean checkBixbyWakeupSupported() {
        Intent intent = new Intent(ACTION_AOM_WAKEUP_COMMAND);
        intent.setClassName(PACKAGENAME_BIXBY_WAKEUP, CLASSNAME_BIXBY_WAKEUP);
        if (Application.getContext().getPackageManager().queryIntentServices(intent, 0).size() > 0) {
            return true;
        }
        return false;
    }
}
