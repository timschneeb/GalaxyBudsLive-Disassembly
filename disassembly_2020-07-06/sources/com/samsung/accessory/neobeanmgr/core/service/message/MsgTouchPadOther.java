package com.samsung.accessory.neobeanmgr.core.service.message;

public class MsgTouchPadOther extends Msg {
    public int touchpadOtherOptionValue;

    public byte[] getData() {
        return null;
    }

    public MsgTouchPadOther(byte[] bArr) {
        super(bArr);
        this.touchpadOtherOptionValue = bArr[getDataStartIndex()];
    }

    public MsgTouchPadOther(int i, boolean z) {
        super((byte) MsgID.TOUCHPAD_OTHER_OPTION);
    }
}
