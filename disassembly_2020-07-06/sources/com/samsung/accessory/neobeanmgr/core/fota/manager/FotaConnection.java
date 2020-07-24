package com.samsung.accessory.neobeanmgr.core.fota.manager;

import android.util.Log;
import com.samsung.accessory.fotaprovider.AccessoryEventHandler;
import com.samsung.accessory.fotaprovider.controller.ConnectionController;
import com.samsung.accessory.fotaprovider.controller.ConsumerInfo;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.fota.manager.FotaRealConnection;

public final class FotaConnection {
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + FotaConnection.class.getSimpleName());
    /* access modifiers changed from: private */
    public ConnectionController.ConnectionResultCallback connectionResultCallback;
    private FotaRealConnection.RealConnectionCallback secondDeviceConnectionCallback = new FotaRealConnection.RealConnectionCallback() {
        public void onConnected() {
            String access$000 = FotaConnection.TAG;
            Log.d(access$000, "onConnected()" + FotaConnection.this.connectionResultCallback);
            if (FotaConnection.this.connectionResultCallback != null) {
                FotaConnection.this.connectionResultCallback.onSuccess();
                Log.d(FotaConnection.TAG, "Fota Connected : true");
                FotaConnectionController.FotaConnected = true;
            }
        }

        public void onError() {
            Log.d(FotaConnection.TAG, "onError()");
            if (FotaConnection.this.connectionResultCallback != null) {
                FotaConnection.this.connectionResultCallback.onFailure();
                FotaConnectionController.FotaConnected = false;
            }
        }
    };
    private FotaRealConnection.TransferCallback transferCallback = new FotaRealConnection.TransferCallback() {
        public void completed() {
            Log.d(FotaConnection.TAG, "TransferCallback : completed()");
            Application.getCoreService().getEarBudsFotaInfo().printFota();
            AccessoryEventHandler.getInstance().reportUpdateResult(new ConsumerInfo(Application.getCoreService().getEarBudsFotaInfo().deviceId, Application.getCoreService().getEarBudsFotaInfo().modelNumber, Application.getCoreService().getEarBudsFotaInfo().salesCode, Application.getCoreService().getEarBudsFotaInfo().firmwareVersion, Application.getCoreService().getEarBudsFotaInfo().uniqueNumber, Application.getCoreService().getEarBudsFotaInfo().serialNumber), true);
        }
    };

    public void doMyConnection() {
        Log.d(TAG, "doMyConnection()");
        new FotaRealConnection().connectRealDevice(this.secondDeviceConnectionCallback);
    }

    public void doMyConnection(ConnectionController.ConnectionResultCallback connectionResultCallback2) {
        Log.d(TAG, "doMyConnection(ConnectionController.ConnectionResultCallback callback)");
        this.connectionResultCallback = connectionResultCallback2;
        new FotaRealConnection().connectRealDevice(this.secondDeviceConnectionCallback);
    }

    public void disconnectMyConnection() {
        Log.d(TAG, "disconnectMyConnection()");
        new FotaRealConnection().disconnect();
        FotaConnectionController.FotaConnected = false;
    }

    public boolean isConnected() {
        String str = TAG;
        Log.d(str, "FotaProviderConnected() : " + FotaConnectionController.FotaConnected);
        return FotaConnectionController.FotaConnected;
    }
}
