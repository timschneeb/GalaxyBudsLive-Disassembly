package com.samsung.accessory.neobeanmgr.core.service.message;

import com.samsung.accessory.neobeanmgr.common.util.BufferBuilder;

public class MsgUpdateTime extends Msg {
    private static final String TAG = "NeoBean_MsgUpdateTime";
    public long currentTime;
    public int timeZone;

    public MsgUpdateTime(long j, int i) {
        super((byte) MsgID.UPDATE_TIME);
        this.currentTime = j;
        this.timeZone = i;
    }

    public byte[] getData() {
        BufferBuilder bufferBuilder = new BufferBuilder();
        bufferBuilder.putLong(this.currentTime);
        bufferBuilder.putInt(this.timeZone);
        return bufferBuilder.array();
    }
}
