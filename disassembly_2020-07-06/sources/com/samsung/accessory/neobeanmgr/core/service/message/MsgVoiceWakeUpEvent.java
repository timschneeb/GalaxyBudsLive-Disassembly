package com.samsung.accessory.neobeanmgr.core.service.message;

public class MsgVoiceWakeUpEvent extends Msg {
    public byte status;

    MsgVoiceWakeUpEvent(byte[] bArr) {
        super(bArr);
        this.status = bArr[getDataStartIndex()];
    }
}
