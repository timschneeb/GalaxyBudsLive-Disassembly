package com.samsung.context.sdk.samsunganalytics.internal.sender.DMA;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.sec.android.diagmonagent.sa.IDMAInterface;

public class DMABinder {
    private Context context;
    /* access modifiers changed from: private */
    public IDMAInterface dmaInterface;
    private boolean isBind = false;
    /* access modifiers changed from: private */
    public boolean isTokenFail = false;
    private ServiceConnection serviceConnection;

    public DMABinder(Context context2, final Callback<Void, String> callback) {
        this.context = context2;
        this.serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                try {
                    IDMAInterface unused = DMABinder.this.dmaInterface = IDMAInterface.Stub.asInterface(iBinder);
                    String checkToken = DMABinder.this.dmaInterface.checkToken();
                    if (checkToken == null) {
                        DMABinder.this.unBind();
                        boolean unused2 = DMABinder.this.isTokenFail = true;
                        Debug.LogD("DMABinder", "Token failed");
                        return;
                    }
                    boolean unused3 = DMABinder.this.isTokenFail = false;
                    callback.onResult(checkToken);
                    Debug.LogD("DMABinder", "DMA connected");
                } catch (Exception e) {
                    DMABinder.this.unBind();
                    boolean unused4 = DMABinder.this.isTokenFail = true;
                    Debug.LogException(e.getClass(), e);
                    e.printStackTrace();
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                IDMAInterface unused = DMABinder.this.dmaInterface = null;
            }
        };
    }

    public boolean bind() {
        if (!this.isBind && !this.isTokenFail) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.sec.android.diagmonagent", "com.sec.android.diagmonagent.sa.receiver.SALogReceiverService");
                this.isBind = this.context.bindService(intent, this.serviceConnection, 1);
                Debug.LogD("DMABinder", "bind " + this.isBind);
            } catch (Exception e) {
                Debug.LogException(e.getClass(), e);
            }
        }
        return this.isTokenFail;
    }

    public boolean isTokenfail() {
        return this.isTokenFail;
    }

    public void unBind() {
        if (this.dmaInterface != null && this.isBind) {
            try {
                this.context.unbindService(this.serviceConnection);
                this.isBind = false;
                Debug.LogD("DMABinder", "unbind");
            } catch (Exception e) {
                Debug.LogException(e.getClass(), e);
            }
        }
    }

    public IDMAInterface getDmaInterface() {
        return this.dmaInterface;
    }

    public boolean isBind() {
        return this.isBind;
    }
}
