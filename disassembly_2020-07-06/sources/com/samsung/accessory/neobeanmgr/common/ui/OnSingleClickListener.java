package com.samsung.accessory.neobeanmgr.common.ui;

import android.os.Handler;
import android.util.Log;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final long BLOCK_TIME = 500;
    private static final String TAG = "NeoBean_OnSingleClickListener";
    private static final Runnable UNBLOCK_RUNNABLE = new Runnable() {
        public void run() {
            Boolean unused = OnSingleClickListener.sBlocked = false;
        }
    };
    /* access modifiers changed from: private */
    public static Boolean sBlocked = false;
    private static Handler sHandler = new Handler();

    public abstract void onSingleClick(View view);

    public void onClick(View view) {
        if (sBlocked.booleanValue()) {
            Log.w(TAG, "Blocked");
            return;
        }
        sBlocked = true;
        sHandler.postDelayed(UNBLOCK_RUNNABLE, BLOCK_TIME);
        onSingleClick(view);
    }
}
