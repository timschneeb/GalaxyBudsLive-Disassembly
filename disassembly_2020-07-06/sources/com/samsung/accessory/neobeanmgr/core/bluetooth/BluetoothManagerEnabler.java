package com.samsung.accessory.neobeanmgr.core.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.util.BluetoothUtil;
import com.samsung.accessory.neobeanmgr.common.util.ResponseCallback;

public class BluetoothManagerEnabler {
    public static final String REASON_ERROR = "error";
    public static final String REASON_FAILED = "failed";
    public static final String REASON_TIMEOUT = "timeout";
    private static final String TAG = "NeoBean_BluetoothManagerEnabler";
    private static final long TIMEOUT_MILLIS = 5000;
    private ResponseCallback mCallback;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(BluetoothManagerEnabler.TAG, "onReceive()");
            BluetoothManagerEnabler.this.response((String) null);
        }
    };
    private boolean mReceiverRegistered = false;

    public BluetoothManagerEnabler(ResponseCallback responseCallback) {
        this.mCallback = responseCallback;
    }

    public void execute() {
        Log.d(TAG, "execute()");
        if (Application.getBluetoothManager().isReady()) {
            response((String) null);
            return;
        }
        registerReceiver();
        if (BluetoothUtil.getAdapter().enable()) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    BluetoothManagerEnabler.this.response(BluetoothManagerEnabler.REASON_TIMEOUT);
                }
            }, 5000);
        } else {
            response(REASON_ERROR);
        }
    }

    /* access modifiers changed from: private */
    public void response(String str) {
        unregisterReceiver();
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mCallback.onResponse(str);
    }

    private void registerReceiver() {
        Application.getContext().registerReceiver(this.mReceiver, getIntentFilter());
        this.mReceiverRegistered = true;
    }

    private void unregisterReceiver() {
        if (this.mReceiverRegistered) {
            Application.getContext().unregisterReceiver(this.mReceiver);
        }
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothManager.ACTION_READY);
        return intentFilter;
    }
}
