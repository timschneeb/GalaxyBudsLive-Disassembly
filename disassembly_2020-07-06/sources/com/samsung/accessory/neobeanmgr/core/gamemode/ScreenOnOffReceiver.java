package com.samsung.accessory.neobeanmgr.core.gamemode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import java.util.HashSet;
import java.util.Set;

public class ScreenOnOffReceiver extends BroadcastReceiver {
    private static final String TAG = "NeoBean_ScreenOnOffReceiver";
    private Context mContext;
    private final BroadcastReceiver mDynamicReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(ScreenOnOffReceiver.TAG, "mDynamicReceiver.onReceive() : " + intent.getAction());
            Log.d(ScreenOnOffReceiver.TAG, "isInUserUse() : " + Util.isInUserUse());
            for (BroadcastReceiver onReceive : ScreenOnOffReceiver.this.mListenReceivers) {
                onReceive.onReceive(context, intent);
            }
        }
    };
    /* access modifiers changed from: private */
    public Set<BroadcastReceiver> mListenReceivers;

    public ScreenOnOffReceiver() {
    }

    public ScreenOnOffReceiver(Context context) {
        this.mContext = context;
        this.mListenReceivers = new HashSet();
        this.mContext.registerReceiver(this.mDynamicReceiver, getIntentFilter());
    }

    public void destroy() {
        this.mContext.unregisterReceiver(this.mDynamicReceiver);
        this.mListenReceivers.clear();
        this.mContext = null;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() : " + intent.getAction());
    }

    private static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        return intentFilter;
    }

    public void addReceiver(BroadcastReceiver broadcastReceiver) {
        this.mListenReceivers.add(broadcastReceiver);
    }

    public void removeReceiver(BroadcastReceiver broadcastReceiver) {
        this.mListenReceivers.remove(broadcastReceiver);
    }
}
