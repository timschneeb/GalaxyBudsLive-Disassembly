package com.samsung.accessory.neobeanmgr.common.util;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class ActionBuffer {
    private static final long BUFFER_MILLIS = 5000;
    private static final String TAG = "NeoBean_ActionBuffer";
    private final Runnable mActionRunnable = new Runnable() {
        public void run() {
            long unused = ActionBuffer.this.mLastActionUptimeMillis = SystemClock.uptimeMillis();
            Log.d(ActionBuffer.TAG, "mActionRunnable.run() : " + ActionBuffer.this.mLastActionUptimeMillis);
            ActionBuffer.this.mUserAction.run();
        }
    };
    private final long mBufferMillis;
    private final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public long mLastActionUptimeMillis;
    /* access modifiers changed from: private */
    public final Runnable mUserAction;

    public ActionBuffer(Runnable runnable) {
        this.mUserAction = runnable;
        this.mBufferMillis = 5000;
    }

    public ActionBuffer(Runnable runnable, long j) {
        this.mUserAction = runnable;
        this.mBufferMillis = j;
    }

    public void destroy() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    public void action() {
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = this.mLastActionUptimeMillis;
        long j2 = this.mBufferMillis;
        if (uptimeMillis - j > j2) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mActionRunnable.run();
            Log.d(TAG, "action() : action");
            return;
        }
        long j3 = uptimeMillis + (j2 - (uptimeMillis - j));
        Log.d(TAG, "action() : atUptimeMillis=" + j3 + " (now=" + SystemClock.uptimeMillis() + ")");
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mHandler.postAtTime(this.mActionRunnable, j3);
    }
}
