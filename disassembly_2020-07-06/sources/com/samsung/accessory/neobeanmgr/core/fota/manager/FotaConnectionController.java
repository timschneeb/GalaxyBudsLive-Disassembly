package com.samsung.accessory.neobeanmgr.core.fota.manager;

import android.util.Log;
import com.samsung.accessory.fotaprovider.controller.ConnectionController;
import com.samsung.accessory.neobeanmgr.Application;

public final class FotaConnectionController extends ConnectionController {
    public static boolean FotaConnected = false;
    private static final String TAG = (Application.TAG_ + FotaConnectionController.class.getSimpleName());

    public void makeConnection() {
        new FotaConnection().doMyConnection();
        Log.d(TAG, "makeConnection");
    }

    public void makeConnection(ConnectionController.ConnectionResultCallback connectionResultCallback) {
        new FotaConnection().doMyConnection(connectionResultCallback);
        Log.d(TAG, "makeConnection(ConnectionController.ConnectionResultCallback connectionResultCallback)");
    }

    public void releaseConnection() {
        new FotaConnection().disconnectMyConnection();
        Log.d(TAG, "releaseConnection()");
    }

    public boolean isConnected() {
        Log.d(TAG, "isConnected()");
        return new FotaConnection().isConnected();
    }
}
