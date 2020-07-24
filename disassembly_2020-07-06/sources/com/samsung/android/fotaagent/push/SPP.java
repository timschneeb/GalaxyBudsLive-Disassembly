package com.samsung.android.fotaagent.push;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.galaxywearable.BroadcastHelper;

public class SPP {
    private static SPP mSPP;
    protected boolean bRegMode = true;
    protected SPPResultReceiver mResultReceiver = null;
    protected int mRetryCount = 0;
    protected SPPResult mSPPResult = null;

    public static SPP getSPP() {
        if (mSPP == null) {
            mSPP = new SPP();
        }
        return mSPP;
    }

    public SPPResultReceiver getSPPReceiver() {
        return this.mResultReceiver;
    }

    public void setSPPReceiver(SPPResultReceiver sPPResultReceiver) {
        this.mResultReceiver = sPPResultReceiver;
    }

    public void requestID(Context context) {
        Log.I("Register SPP server");
        this.bRegMode = true;
        Intent intent = new Intent(SPPConfig.PUSH_SERVICE_REQUEST);
        intent.putExtra(SPPConfig.EXTRA_REQTYPE, 1);
        intent.putExtra("appId", SPPConfig.APP_ID);
        intent.putExtra(SPPConfig.EXTRA_USERDATA, context.getPackageName());
        BroadcastHelper.sendBroadcast(intent);
    }

    public void removeID(Context context) {
        Log.I("Unregister SPP server");
        this.bRegMode = false;
        Intent intent = new Intent(SPPConfig.PUSH_SERVICE_REQUEST);
        intent.putExtra(SPPConfig.EXTRA_REQTYPE, 2);
        intent.putExtra("appId", SPPConfig.APP_ID);
        intent.putExtra(SPPConfig.EXTRA_USERDATA, context.getPackageName());
        BroadcastHelper.sendBroadcast(intent);
    }

    public void sendResult(Intent intent) {
        new AsyncTask<Intent, Void, Void>() {
            /* access modifiers changed from: protected */
            /* JADX WARNING: Removed duplicated region for block: B:15:0x0059  */
            /* JADX WARNING: Removed duplicated region for block: B:16:0x0061  */
            public Void doInBackground(Intent... intentArr) {
                Thread.currentThread().setName("AST:FOTA:SPP");
                boolean z = false;
                int intExtra = intentArr[0].getIntExtra(SPPConfig.EXTRA_STATUS, 1);
                String stringExtra = intentArr[0].getStringExtra(SPPConfig.EXTRA_REGID);
                int intExtra2 = intentArr[0].getIntExtra(SPPConfig.EXTRA_ERROR, 0);
                if (intExtra != 0 || !SPP.this.bRegMode) {
                    if (intExtra == 2 && !SPP.this.bRegMode) {
                        Log.I("SPP Push unregistration Success");
                    }
                    SPP.this.mSPPResult = new SPPResult(z);
                    if (SPP.this.mSPPResult.isSuccess()) {
                        SPP.this.mSPPResult.setPushID(stringExtra);
                    } else {
                        SPP.this.mSPPResult.setError(intExtra2);
                        SPP.this.mRetryCount++;
                    }
                    Thread.currentThread().setName("AST:FOTA");
                    return null;
                }
                if (!TextUtils.isEmpty(stringExtra)) {
                    Log.I("SPP Push registration Success");
                }
                SPP.this.mSPPResult = new SPPResult(z);
                if (SPP.this.mSPPResult.isSuccess()) {
                }
                Thread.currentThread().setName("AST:FOTA");
                return null;
                z = true;
                SPP.this.mSPPResult = new SPPResult(z);
                if (SPP.this.mSPPResult.isSuccess()) {
                }
                Thread.currentThread().setName("AST:FOTA");
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (SPP.this.mResultReceiver != null) {
                    SPP.this.mResultReceiver.onSPPResponse(SPP.this.mSPPResult);
                }
            }
        }.execute(new Intent[]{intent});
    }

    public int getRetryCount() {
        return this.mRetryCount;
    }

    public boolean needRetry() {
        if (this.mRetryCount >= 3) {
            return false;
        }
        try {
            Log.I("Wait for a while... :" + 5000);
            Thread.sleep((long) 5000);
            return true;
        } catch (InterruptedException e) {
            Log.printStackTrace(e);
            return true;
        }
    }
}
