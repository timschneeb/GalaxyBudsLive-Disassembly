package com.samsung.accessory.neobeanmgr.core.service.message;

public class MsgSimple extends Msg {
    public byte byteData;

    public MsgSimple(byte b, byte b2) {
        super(b);
        this.byteData = b2;
    }

    public MsgSimple(byte b, boolean z, byte b2) {
        super(b, z);
        this.byteData = b2;
    }

    public byte[] getData() {
        return new byte[]{this.byteData};
    }
}
