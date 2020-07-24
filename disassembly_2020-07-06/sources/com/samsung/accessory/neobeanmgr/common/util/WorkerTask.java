package com.samsung.accessory.neobeanmgr.common.util;

import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;

public abstract class WorkerTask implements Runnable {
    protected final String TAG = (Application.TAG_ + getClass().getSimpleName());

    public abstract void execute();

    public void run() {
        Log.d(this.TAG, "execute()");
        execute();
        Log.d(this.TAG, "execute()_end");
    }
}
