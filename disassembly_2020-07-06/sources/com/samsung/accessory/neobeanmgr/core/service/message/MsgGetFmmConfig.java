package com.samsung.accessory.neobeanmgr.core.service.message;

public class MsgGetFmmConfig extends Msg {
    private static final String TAG = "NeoBean_MsgGetFmmConfig";
    public byte[] getFmmConfig;

    public MsgGetFmmConfig(byte[] bArr) {
        super(bArr);
        this.getFmmConfig = getRecvData();
    }

    public MsgGetFmmConfig() {
        super((byte) MsgID.GET_FMM_CONFIG);
    }
}
