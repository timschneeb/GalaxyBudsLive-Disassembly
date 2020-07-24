package com.samsung.accessory.neobeanmgr.core.fmm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.fmm.FmmManager;

public class FmmRequestReceiver extends BroadcastReceiver {
    private static final String TAG = (Application.TAG_ + FmmRequestReceiver.class.getSimpleName());

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            String stringExtra = intent.getStringExtra("operation");
            String stringExtra2 = intent.getStringExtra("uid");
            String str = TAG;
            Log.i(str, "onReceive(): " + intent.getAction() + ", operation = " + stringExtra + ", uid: " + stringExtra2);
            FmmManager.handleResponse(context, intent);
        }
    }
}
