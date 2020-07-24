package com.samsung.accessory.neobeanmgr.core.service.message;

import java.nio.ByteBuffer;

public class MsgFotaResult extends Msg {
    public static final int EARBUD_STATUS_SUCCESS = 0;
    private static final String TAG = "NeoBean_MsgFotaResult";
    public int mErrorCode;
    public int mResult;

    public byte[] getData() {
        return new byte[]{1};
    }

    public MsgFotaResult(byte[] bArr) {
        super(bArr);
        ByteBuffer recvDataByteBuffer = getRecvDataByteBuffer();
        this.mResult = recvDataByteBuffer.get();
        this.mErrorCode = recvDataByteBuffer.get();
    }

    public MsgFotaResult() {
        super(MsgID.FOTA_RESULT, true);
    }
}
