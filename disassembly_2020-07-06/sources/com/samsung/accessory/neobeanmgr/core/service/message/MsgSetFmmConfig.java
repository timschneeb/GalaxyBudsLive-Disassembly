package com.samsung.accessory.neobeanmgr.core.service.message;

import android.util.Log;
import com.samsung.accessory.neobeanmgr.common.util.BufferBuilder;

public class MsgSetFmmConfig extends Msg {
    private static final String TAG = "NeoBean_MsgSetFmmConfig";
    public BufferBuilder fmmConfigData;
    public boolean result;

    MsgSetFmmConfig(byte[] bArr) {
        super(bArr);
        this.result = getRecvDataByteBuffer().get() == 0;
        Log.d(TAG, "MSG_ID_SET_FMM_CONFIG: result:  " + this.result);
    }

    public MsgSetFmmConfig(BufferBuilder bufferBuilder) {
        super(MsgID.SET_FMM_CONFIG, false);
        this.fmmConfigData = bufferBuilder;
    }

    public byte[] getData() {
        return this.fmmConfigData.array();
    }
}
