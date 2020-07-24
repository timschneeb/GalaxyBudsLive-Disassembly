package com.samsung.accessory.neobeanmgr.common.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

public class BroadcastReceiverUtil {
    private static final String TAG = "NeoBean_BroadcastReceiverUtil";

    public static abstract class Receiver extends BroadcastReceiver {
        protected boolean registered = false;

        public abstract void setIntentFilter(IntentFilter intentFilter);
    }

    public static void register(Context context, Receiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        receiver.setIntentFilter(intentFilter);
        context.registerReceiver(receiver, intentFilter);
        receiver.registered = true;
    }

    public static void unregister(Context context, Receiver receiver) {
        try {
            if (receiver.registered) {
                context.unregisterReceiver(receiver);
                receiver.registered = false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(TAG, "unregister() : IllegalArgumentException e !!!");
        }
    }
}
