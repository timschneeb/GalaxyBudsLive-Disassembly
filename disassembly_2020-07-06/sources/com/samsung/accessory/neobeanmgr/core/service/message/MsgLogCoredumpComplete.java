package com.samsung.accessory.neobeanmgr.core.service.message;

import android.util.Log;

public class MsgLogCoredumpComplete extends Msg {
    private static final String TAG = "NeoBean_MsgLogCoredumpComplete";
    public int resCode;

    MsgLogCoredumpComplete(byte[] bArr) {
        super(bArr);
        this.resCode = getRecvDataByteBuffer().get();
        Log.d(TAG, "MSG_ID_LOG_COREDUMP_COMPLETE: resCode:  " + this.resCode);
    }

    public MsgLogCoredumpComplete() {
        super((byte) MsgID.LOG_COREDUMP_COMPLETE);
    }
}
