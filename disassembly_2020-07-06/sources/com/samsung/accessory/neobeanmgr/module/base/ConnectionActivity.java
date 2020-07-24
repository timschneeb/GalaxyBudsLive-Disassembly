package com.samsung.accessory.neobeanmgr.module.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;

public abstract class ConnectionActivity extends PermissionCheckActivity {
    private static final String TAG = "NeoBean_ConnectionActivity";
    private final BroadcastReceiver mDisconnectedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.w(ConnectionActivity.TAG, "CoreService.ACTION_DEVICE_DISCONNECTED -> finish()");
            ConnectionActivity.this.finish();
        }
    };

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        registerReceiver(this.mDisconnectedReceiver, getDisconnectedIntentFilter());
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!Application.getCoreService().isConnected()) {
            Log.w(TAG, "isConnected() == false -> finish()");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        unregisterReceiver(this.mDisconnectedReceiver);
    }

    private final IntentFilter getDisconnectedIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        return intentFilter;
    }
}
