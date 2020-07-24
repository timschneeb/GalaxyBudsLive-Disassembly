package com.samsung.accessory.neobeanmgr.core.gamemode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GameModeReceiver extends BroadcastReceiver {
    private static final String TAG = "NeoBean_GameModeReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() : " + intent.getAction());
    }
}
