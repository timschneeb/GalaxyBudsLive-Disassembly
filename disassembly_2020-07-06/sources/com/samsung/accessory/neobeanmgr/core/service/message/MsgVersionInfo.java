package com.samsung.accessory.neobeanmgr.core.service.message;

import android.util.Log;
import com.accessorydm.interfaces.XDMInterface;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.FmmConstants;
import com.samsung.accessory.neobeanmgr.core.service.BudsLogManager;
import java.nio.ByteBuffer;

public class MsgVersionInfo extends Msg {
    private static final String TAG = "NeoBean_MsgVersionInfo";
    static final String[] str_SWMonth = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
    static final String[] str_SWRelVer = {"G", "H", "I", "J", "K", "L", "M", FmmConstants.NOT_SUPPORT, "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", FmmConstants.SUPPORT, "Z"};
    static final String[] str_SWVer = {"E", "U"};
    static final String[] str_SWYear = {"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", FmmConstants.SUPPORT, "Z"};
    public String Left_HW_version;
    public String Left_SW_version;
    public String Left_Touch_FW_Version;
    public String Right_HW_version;
    public String Right_SW_version;
    public String Right_Touch_FW_Version;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MsgVersionInfo(byte[] bArr) {
        super(bArr);
        ByteBuffer recvDataByteBuffer = getRecvDataByteBuffer();
        byte b = recvDataByteBuffer.get();
        this.Left_HW_version = "rev" + String.format("%X", new Object[]{Integer.valueOf((b & 240) >> 4)}) + XDMInterface.XDM_BASE_PATH + String.format("%X", new Object[]{Integer.valueOf(b & 15)});
        byte b2 = recvDataByteBuffer.get();
        this.Right_HW_version = "rev" + String.format("%X", new Object[]{Integer.valueOf((b2 & 240) >> 4)}) + XDMInterface.XDM_BASE_PATH + String.format("%X", new Object[]{Integer.valueOf(b2 & 15)});
        StringBuilder sb = new StringBuilder();
        byte b3 = recvDataByteBuffer.get();
        sb.append("R180XX");
        byte b4 = b3 & 1;
        String str = "E";
        sb.append(b4 == 0 ? str : "U");
        sb.append("0A");
        Log.d(TAG, "DEBUG = ENG : USER = " + b4);
        Log.d(TAG, "DEBUG = BASE : DM : ETC = " + ((b3 & 240) >> 4));
        byte b5 = recvDataByteBuffer.get();
        byte b6 = recvDataByteBuffer.get();
        String format = b6 <= 15 ? String.format("%X", new Object[]{Byte.valueOf(b6)}) : str_SWRelVer[b6 - 16];
        sb.append(str_SWYear[(b5 & 240) >> 4]);
        sb.append(str_SWMonth[b5 & 15]);
        sb.append(format);
        this.Left_SW_version = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        byte b7 = recvDataByteBuffer.get();
        sb2.append("R180XX");
        byte b8 = b7 & 1;
        sb2.append(b8 != 0 ? "U" : str);
        sb2.append("0A");
        Log.d(TAG, "DEBUG = ENG : USER = " + b8);
        Log.d(TAG, "DEBUG = BASE : DM : ETC = " + ((b7 & 240) >> 4));
        byte b9 = recvDataByteBuffer.get();
        byte b10 = recvDataByteBuffer.get();
        String format2 = b10 <= 15 ? String.format("%X", new Object[]{Byte.valueOf(b10)}) : str_SWRelVer[b10 - 16];
        sb2.append(str_SWYear[(b9 & 240) >> 4]);
        sb2.append(str_SWMonth[b9 & 15]);
        sb2.append(format2);
        this.Right_SW_version = sb2.toString();
        this.Left_Touch_FW_Version = String.format("%x", new Object[]{Byte.valueOf(recvDataByteBuffer.get())});
        this.Right_Touch_FW_Version = String.format("%x", new Object[]{Byte.valueOf(recvDataByteBuffer.get())});
        String str2 = "\nLEFT [HW version] :" + this.Left_HW_version + "\nRIGHT [HW version] :" + this.Right_HW_version + "\nLEFT [SW version] :" + this.Left_SW_version + "\nRIGHT [SW version] :" + this.Right_SW_version + "\nLEFT [TOUCH FW version] :" + this.Left_Touch_FW_Version + "\nRIGHT [TOUCH FW version] :" + this.Right_Touch_FW_Version;
        Log.d(TAG, str2);
        BudsLogManager.sendLog(6, str2);
    }
}
