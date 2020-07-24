package com.samsung.context.sdk.samsunganalytics.internal.sender.DLC;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.samsung.android.fotaagent.push.SPPConfig;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.sec.spp.push.dlc.api.IDlcService;

public class DLCBinder {
    private static final String ACTION_DEREGI = "com.sec.spp.push.REQUEST_DEREGISTER";
    private static final String ACTION_REGI = "com.sec.spp.push.REQUEST_REGISTER";
    private static String DLC_LOG_CLASS = "com.sec.spp.push.dlc.writer.WriterService";
    private static String DLC_LOG_PACKAGE = "com.sec.spp.push";
    private static final String EXTRA_KEY_INTENTFILTER = "EXTRA_INTENTFILTER";
    private static final String EXTRA_KEY_PACKAGENAME = "EXTRA_PACKAGENAME";
    private static final String EXTRA_KEY_RESULT_CODE = "EXTRA_RESULT_CODE";
    private static final String EXTRA_KEY_STR = "EXTRA_STR";
    private static final String EXTRA_KEY_STR_ACTION = "EXTRA_STR_ACTION";
    private static final int RESULT_FAIL_Blocked_app = -7;
    private static final int RESULT_FAIL_Http_Fail = -5;
    private static final int RESULT_FAIL_Internal_DB_Error = -4;
    private static final int RESULT_FAIL_Internal_Error = -3;
    private static final int RESULT_FAIL_Invalid_Parameters = -2;
    private static final int RESULT_FAIL_Package_Not_Found = -8;
    private static final int RESULT_FAIL_Timeout = -6;
    private static final int RESULT_SUCCESS_Already_Registered = 200;
    private static final int RESULT_SUCCESS_Register_Success = 100;
    /* access modifiers changed from: private */
    public Callback callback;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public BroadcastReceiver dlcRegisterReplyReceiver;
    /* access modifiers changed from: private */
    public IDlcService dlcService;
    private ServiceConnection dlcServiceConnection;
    /* access modifiers changed from: private */
    public boolean isBindToDLC;
    /* access modifiers changed from: private */
    public boolean onRegisterRequest;
    /* access modifiers changed from: private */
    public String registerFilter;

    public DLCBinder(Context context2) {
        this.isBindToDLC = false;
        this.onRegisterRequest = false;
        this.dlcServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Debug.LogD("DLC Sender", "DLC Client ServiceConnected");
                IDlcService unused = DLCBinder.this.dlcService = IDlcService.Stub.asInterface(iBinder);
                if (DLCBinder.this.dlcRegisterReplyReceiver != null) {
                    DLCBinder.this.context.unregisterReceiver(DLCBinder.this.dlcRegisterReplyReceiver);
                    BroadcastReceiver unused2 = DLCBinder.this.dlcRegisterReplyReceiver = null;
                }
                if (DLCBinder.this.callback != null) {
                    DLCBinder.this.callback.onResult(null);
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                Debug.LogD("DLC Sender", "Client ServiceDisconnected");
                IDlcService unused = DLCBinder.this.dlcService = null;
                boolean unused2 = DLCBinder.this.isBindToDLC = false;
            }
        };
        this.context = context2;
        this.registerFilter = context2.getPackageName();
        this.registerFilter += ".REGISTER_FILTER";
    }

    public DLCBinder(Context context2, Callback callback2) {
        this(context2);
        this.callback = callback2;
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(this.registerFilter);
        if (this.dlcRegisterReplyReceiver == null) {
            this.dlcRegisterReplyReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    boolean unused = DLCBinder.this.onRegisterRequest = false;
                    if (intent == null) {
                        Debug.LogD("DLC Sender", "dlc register reply fail");
                        return;
                    }
                    String action = intent.getAction();
                    Bundle extras = intent.getExtras();
                    if (action == null || extras == null) {
                        Debug.LogD("DLC Sender", "dlc register reply fail");
                    } else if (action.equals(DLCBinder.this.registerFilter)) {
                        String string = extras.getString(DLCBinder.EXTRA_KEY_STR);
                        int i = extras.getInt(DLCBinder.EXTRA_KEY_RESULT_CODE);
                        Debug.LogD("DLC Sender", "register DLC result:" + string);
                        if (i < 0) {
                            Debug.LogD("DLC Sender", "register DLC result fail:" + string);
                            return;
                        }
                        DLCBinder.this.bindService(extras.getString(DLCBinder.EXTRA_KEY_STR_ACTION));
                    }
                }
            };
        }
        this.context.registerReceiver(this.dlcRegisterReplyReceiver, intentFilter);
    }

    public void sendRegisterRequestToDLC() {
        if (this.dlcRegisterReplyReceiver == null) {
            registerReceiver();
        }
        if (!this.onRegisterRequest) {
            Intent intent = new Intent(ACTION_REGI);
            intent.putExtra(EXTRA_KEY_PACKAGENAME, this.context.getPackageName());
            intent.putExtra(EXTRA_KEY_INTENTFILTER, this.registerFilter);
            intent.setPackage(SPPConfig.SPP_PACKAGENAME);
            this.context.sendBroadcast(intent);
            this.onRegisterRequest = true;
            Debug.LogD("DLCBinder", "send register Request");
            Debug.LogENG("send register Request:" + this.context.getPackageName());
            return;
        }
        Debug.LogD("DLCBinder", "already send register request");
    }

    /* access modifiers changed from: private */
    public void bindService(String str) {
        if (this.isBindToDLC) {
            unbindService();
        }
        try {
            Intent intent = new Intent(str);
            intent.setClassName(DLC_LOG_PACKAGE, DLC_LOG_CLASS);
            this.isBindToDLC = this.context.bindService(intent, this.dlcServiceConnection, 1);
            Debug.LogD("DLCBinder", "bind");
        } catch (Exception e) {
            Debug.LogException(getClass(), e);
        }
    }

    private void unbindService() {
        if (this.isBindToDLC) {
            try {
                Debug.LogD("DLCBinder", "unbind");
                this.context.unbindService(this.dlcServiceConnection);
                this.isBindToDLC = false;
            } catch (Exception e) {
                Debug.LogException(getClass(), e);
            }
        }
    }

    public boolean isBindToDLC() {
        return this.isBindToDLC;
    }

    public IDlcService getDlcService() {
        return this.dlcService;
    }
}
