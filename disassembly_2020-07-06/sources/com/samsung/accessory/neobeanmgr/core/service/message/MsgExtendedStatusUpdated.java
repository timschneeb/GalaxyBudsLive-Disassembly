package com.samsung.accessory.neobeanmgr.core.service.message;

import android.util.Log;
import com.samsung.accessory.neobeanmgr.common.util.ByteUtil;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import java.nio.ByteBuffer;

public class MsgExtendedStatusUpdated extends Msg {
    public static final int DEVICE_COLOR_BLACK = 2;
    public static final int DEVICE_COLOR_PINK = 4;
    public static final int DEVICE_COLOR_WHITE = 0;
    public static final int DEVICE_COLOR_YELLOW = 3;
    private static final String TAG = "NeoBean_MsgExtendedStatusUpdated";
    public static final int TYPE_KERNEL = 0;
    public static final int TYPE_OPEN = 1;
    public boolean adjustSoundSync;
    public int batteryCase;
    public int batteryLeft;
    public int batteryRight;
    public int colorL;
    public int colorR;
    public boolean coupled;
    public int deviceColor;
    public byte earType;
    public byte equalizer;
    public int equalizerType;
    public boolean extraHighAmbient;
    public int fmmRevision;
    public boolean noiseReduction;
    public boolean outsideDoubleTap;
    public boolean passThrough;
    public int placementL;
    public int placementR;
    public byte primaryEarbud;
    public byte revision;
    public boolean seamlessConnection;
    public boolean sideToneStatus;
    public boolean touchpadConfig;
    public int touchpadOptionLeft;
    public int touchpadOptionRight;
    public boolean voiceWakeUp;
    public int voiceWakeUpLanguage;
    public boolean wearingL;
    public boolean wearingR;

    MsgExtendedStatusUpdated(byte[] bArr) {
        super(bArr);
        ByteBuffer recvDataByteBuffer = getRecvDataByteBuffer();
        this.revision = recvDataByteBuffer.get();
        this.earType = recvDataByteBuffer.get();
        this.batteryLeft = recvDataByteBuffer.get();
        this.batteryRight = recvDataByteBuffer.get();
        boolean z = false;
        this.coupled = recvDataByteBuffer.get() == 1;
        this.primaryEarbud = recvDataByteBuffer.get();
        byte b = recvDataByteBuffer.get();
        this.placementL = ByteUtil.valueOfLeft(b);
        this.placementR = ByteUtil.valueOfRight(b);
        this.wearingL = this.placementL == 1;
        this.wearingR = this.placementR == 1;
        this.batteryCase = recvDataByteBuffer.get();
        this.adjustSoundSync = recvDataByteBuffer.get() == 1;
        this.equalizerType = recvDataByteBuffer.get();
        this.touchpadConfig = recvDataByteBuffer.get() == 1;
        byte b2 = recvDataByteBuffer.get();
        this.touchpadOptionLeft = ByteUtil.valueOfLeft(b2);
        this.touchpadOptionRight = ByteUtil.valueOfRight(b2);
        if (this.revision < 1) {
            this.outsideDoubleTap = recvDataByteBuffer.get() == 1;
        }
        this.noiseReduction = recvDataByteBuffer.get() == 1;
        this.voiceWakeUp = recvDataByteBuffer.get() == 1;
        short s = recvDataByteBuffer.getShort();
        this.deviceColor = s != recvDataByteBuffer.getShort() ? 0 : s;
        this.voiceWakeUpLanguage = recvDataByteBuffer.get();
        if (this.revision >= 3) {
            this.seamlessConnection = recvDataByteBuffer.get() == 0;
        }
        if (this.revision >= 4) {
            this.fmmRevision = recvDataByteBuffer.get();
        }
        if (this.revision >= 5) {
            this.passThrough = recvDataByteBuffer.get() == 1 ? true : z;
        }
        Log.d(TAG, "revision=" + this.revision + ", batteryLeft=" + this.batteryLeft + ", batteryRight=" + this.batteryRight + ", batteryCase=" + this.batteryCase + ", adjustSoundSync=" + this.adjustSoundSync);
    }

    public void applyTo(EarBudsInfo earBudsInfo) {
        earBudsInfo.batteryL = this.batteryLeft;
        earBudsInfo.batteryR = this.batteryRight;
        earBudsInfo.batteryCase = this.batteryCase;
        earBudsInfo.coupled = this.coupled;
        earBudsInfo.wearingL = this.wearingL;
        earBudsInfo.wearingR = this.wearingR;
        earBudsInfo.placementL = this.placementL;
        earBudsInfo.placementR = this.placementR;
        earBudsInfo.adjustSoundSync = this.adjustSoundSync;
        earBudsInfo.equalizerType = this.equalizerType;
        earBudsInfo.touchpadLocked = this.touchpadConfig;
        earBudsInfo.touchpadOptionLeft = this.touchpadOptionLeft;
        earBudsInfo.touchpadOptionRight = this.touchpadOptionRight;
        earBudsInfo.colorL = this.colorL;
        earBudsInfo.colorR = this.colorR;
        earBudsInfo.outsideDoubleTap = this.outsideDoubleTap;
        earBudsInfo.extendedRevision = this.revision;
        earBudsInfo.deviceColor = this.deviceColor;
        earBudsInfo.sideToneStatus = this.sideToneStatus;
        earBudsInfo.extraHighAmbient = this.extraHighAmbient;
        earBudsInfo.voiceWakeUp = this.voiceWakeUp;
        earBudsInfo.noiseReduction = this.noiseReduction;
        earBudsInfo.voiceWakeUpLanguage = this.voiceWakeUpLanguage;
        earBudsInfo.seamlessConnection = this.seamlessConnection;
        earBudsInfo.fmmRevision = this.fmmRevision;
        earBudsInfo.passThrough = this.passThrough;
        earBudsInfo.calcBatteryIntegrated();
    }
}
