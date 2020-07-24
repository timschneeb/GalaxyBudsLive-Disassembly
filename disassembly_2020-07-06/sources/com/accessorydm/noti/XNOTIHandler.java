package com.accessorydm.noti;

import android.text.TextUtils;
import com.accessorydm.db.file.XDB;
import com.accessorydm.db.file.XDBAdapter;
import com.accessorydm.db.file.XDBProfileAdp;
import com.accessorydm.db.file.XDBProfileInfo;
import com.accessorydm.db.file.XDBProfileListAdp;
import com.accessorydm.db.file.XDBResyncModeAdp;
import com.accessorydm.eng.core.XDMAuth;
import com.accessorydm.eng.core.XDMMem;
import com.accessorydm.interfaces.XDMDefInterface;
import com.accessorydm.interfaces.XDMInterface;
import com.accessorydm.interfaces.XNOTIInterface;
import com.samsung.android.fotaprovider.log.Log;
import java.nio.charset.Charset;

public class XNOTIHandler implements XDMDefInterface, XDMInterface, XNOTIInterface {
    public static XNOTI[] g_tNotiMsg;

    public XNOTIHandler() {
        g_tNotiMsg = new XNOTI[3];
        for (int i = 0; i < 3; i++) {
            g_tNotiMsg[i] = new XNOTI();
        }
    }

    private static void xnotiPushHdleMessageFree(XNOTIMessage xNOTIMessage) {
        if (xNOTIMessage != null) {
            xNOTIMessage.pData = null;
            xNOTIMessage.pBody = null;
        }
    }

    public XNOTIMessage xnotiPushHdleMessageCopy(Object obj) {
        XNOTIMessage xNOTIMessage = (XNOTIMessage) obj;
        XNOTIMessage xNOTIMessage2 = new XNOTIMessage();
        if (xNOTIMessage.pData != null) {
            xNOTIMessage2.pData = xNOTIMessage.pData;
            xNOTIMessage2.dataSize = xNOTIMessage.dataSize;
        } else if (xNOTIMessage.pBody != null) {
            xNOTIMessage2.pData = new byte[xNOTIMessage.bodySize];
            xNOTIMessage2.pBody = xNOTIMessage.pBody;
        }
        xNOTIMessage2.bodySize = xNOTIMessage.bodySize;
        xNOTIMessage2.mime_type = xNOTIMessage.mime_type;
        xNOTIMessage2.push_type = xNOTIMessage.push_type;
        xNOTIMessage2.push_status = xNOTIMessage.push_status;
        xNOTIMessage2.appId = xNOTIMessage.appId;
        return xNOTIMessage2;
    }

    private static int xnotiPushHdleWSPHeader(byte[] bArr, int i) {
        int i2;
        if ((bArr[1] & 255) != 6 || (i2 = bArr[2] + 3) >= i) {
            return -1;
        }
        return i2;
    }

    private static void xnotiPushHdleInitNotiMsg(XNOTI xnoti, int i) {
        xnoti.appId = i;
    }

    private void xnotiPushHdleFreeNotiMsg(XNOTI xnoti) {
        if (xnoti == null) {
            Log.E("pNotiMsg is NULL");
        } else if (xnoti.triggerHeader != null) {
            xnoti.triggerHeader.m_szServerID = null;
            xnoti.triggerHeader = null;
        }
    }

    private static int xnotiPushHdleCompareNotiDigest(byte[] bArr, int i, XNOTI xnoti, int i2) {
        if (xnoti.triggerHeader == null) {
            Log.E("triggerHeader is NULL");
            return -1;
        } else if (TextUtils.isEmpty(xnoti.triggerHeader.m_szServerID)) {
            Log.E("pServerID is NULL");
            return -1;
        } else {
            int i3 = i - 16;
            byte[] bArr2 = new byte[i3];
            System.arraycopy(bArr, 0, bArr2, 0, i3);
            String xdbGetNotiDigest = XDBAdapter.xdbGetNotiDigest(i2, xnoti.triggerHeader.m_szServerID, 3, bArr2, i3);
            if (TextUtils.isEmpty(xdbGetNotiDigest)) {
                Log.E("pDigest is NULL ");
                return -1;
            } else if (!xdbGetNotiDigest.equals(new String(xnoti.digestdata, Charset.defaultCharset()))) {
                Log.I("Compare Fail");
                return -1;
            } else {
                Log.I("Compare Success");
                return 0;
            }
        }
    }

    private static byte[] xnotiPushHdleParsingSyncNotiDigest(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = new byte[16];
        for (int i = 0; i < 16; i++) {
            bArr2[i] = bArr[i];
        }
        return bArr2;
    }

    private static int xnotiPushHdleParsingSyncNotiHeader(byte[] bArr, XNOTI xnoti) {
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        xnoti.triggerHeader.version = ((bArr2[0] << 8) | (bArr2[1] & 192)) >> 6;
        int i = (bArr2[1] & 48) >> 4;
        if (i == 0) {
            xnoti.triggerHeader.uiMode = 1;
        } else if (i == 1) {
            xnoti.triggerHeader.uiMode = 2;
        } else if (i != 2) {
            xnoti.triggerHeader.uiMode = 4;
        } else {
            xnoti.triggerHeader.uiMode = 3;
        }
        int i2 = (bArr2[1] & 8) >> 3;
        xnoti.triggerHeader.initiator = i2 > 0 ? 1 : 0;
        xnoti.triggerHeader.future = bArr2[4] | (bArr2[1] & 7) | bArr2[2] | bArr2[3];
        xnoti.triggerHeader.m_szSessionID = XDMMem.xdmLibToHexString(bArr2, 5, 2);
        xnoti.triggerHeader.lenServerID = bArr2[7];
        byte[] bArr3 = new byte[xnoti.triggerHeader.lenServerID];
        for (int i3 = 0; i3 < xnoti.triggerHeader.lenServerID; i3++) {
            bArr3[i3] = bArr[8 + i3];
        }
        xnoti.triggerHeader.m_szServerID = new String(bArr3, Charset.defaultCharset());
        int i4 = 8 + xnoti.triggerHeader.lenServerID;
        Log.H("ServerID=" + xnoti.triggerHeader.m_szServerID);
        Log.H("SessionID=" + xnoti.triggerHeader.m_szSessionID);
        Log.I("UI Mode=" + xnoti.triggerHeader.uiMode);
        return i4;
    }

    private static void xnotiPushHdleParsingSyncNotiBody(byte[] bArr, XNOTI xnoti) {
        Log.I("");
        if (bArr != null) {
            try {
                xnoti.triggerBody.opmode = bArr[0] >> 4;
                xnoti.triggerBody.pushjobId = (bArr[8] >> 4) | (bArr[0] & 15) | bArr[1] | bArr[2] | bArr[3] | bArr[4] | bArr[5] | bArr[6] | bArr[7];
                Log.H("opmode=" + xnoti.triggerBody.opmode);
                Log.H("pushjobId=" + xnoti.triggerBody.pushjobId);
            } catch (Exception e) {
                Log.E(e.toString());
            }
        }
    }

    private static int xnotiPushHdleParsingSyncNoti(byte[] bArr, int i, int i2) {
        Log.I("");
        XNOTI xnoti = g_tNotiMsg[i2];
        xnoti.digestdata = xnotiPushHdleParsingSyncNotiDigest(bArr);
        byte[] bArr2 = new byte[(bArr.length - 16)];
        for (int i3 = 0; i3 < bArr.length - 16; i3++) {
            bArr2[i3] = bArr[i3 + 16];
        }
        int xnotiPushHdleParsingSyncNotiHeader = xnotiPushHdleParsingSyncNotiHeader(bArr2, xnoti);
        Log.H("bodySize[" + i + "], notiHeaderLen[" + xnotiPushHdleParsingSyncNotiHeader + "] DigestLength[" + 16 + "]");
        if (xnotiPushHdleParsingSyncNotiHeader == 0) {
            Log.E("notiHeaderLen is 0");
            return -1;
        } else if (xnotiPushHdleCompareNotiDigest(bArr, i, xnoti, i2) != 0 && xnotiReSyncCompare(bArr, i, xnoti) != 0) {
            return -1;
        } else {
            int i4 = xnotiPushHdleParsingSyncNotiHeader + 16;
            if (i4 < i) {
                int length = bArr.length - i4;
                byte[] bArr3 = new byte[length];
                for (int i5 = 0; i5 < length; i5++) {
                    bArr3[i5] = bArr[i4 + i5];
                }
                xnotiPushHdleParsingSyncNotiBody(bArr3, xnoti);
            }
            return 0;
        }
    }

    private XNOTI xnotiPushHdleNotiMsgHandler(XNOTIMessage xNOTIMessage) {
        if (xNOTIMessage == null) {
            Log.E("pPushMsg is NULL");
            return null;
        }
        int i = xNOTIMessage.appId;
        xnotiPushHdleInitNotiMsg(g_tNotiMsg[i], i);
        if (xNOTIMessage.pData != null) {
            if (xNOTIMessage.pData[0] != 0) {
                Log.I("header and body");
                int i2 = xNOTIMessage.dataSize;
                int xnotiPushHdleWSPHeader = xnotiPushHdleWSPHeader(xNOTIMessage.pData, i2);
                if (xnotiPushHdleWSPHeader != -1) {
                    int i3 = i2 - xnotiPushHdleWSPHeader;
                    byte[] bArr = new byte[i3];
                    for (int i4 = 0; i4 < i3; i4++) {
                        bArr[i4] = xNOTIMessage.pData[xnotiPushHdleWSPHeader + i4];
                    }
                    xNOTIMessage.pBody = null;
                    xNOTIMessage.bodySize = i3;
                    xNOTIMessage.pBody = bArr;
                    xNOTIMessage.pData = null;
                }
            }
        }
        if (xnotiPushHdleParsingSyncNoti(xNOTIMessage.pBody, xNOTIMessage.bodySize, i) == 0) {
            return g_tNotiMsg[i];
        }
        xnotiPushHdleFreeNotiMsg(g_tNotiMsg[i]);
        return null;
    }

    public XNOTI xnotiPushHdleMsgHandler(XNOTIMessage xNOTIMessage) {
        XNOTI xnoti = new XNOTI();
        Log.I("");
        int i = xNOTIMessage.push_type;
        if (i == 1) {
            xNOTIMessage.appId = 0;
            xNOTIMessage.mime_type = 1;
            Log.I("XNOTI_MIME_CONTENT_TYPE_DM ");
            xnoti = xnotiPushHdleNotiMsgHandler(xNOTIMessage);
        } else if (i != 3) {
            Log.E("Not Support Push Type");
        } else {
            xNOTIMessage.appId = 0;
            xNOTIMessage.mime_type = 1;
            Log.I("XNOTI_MIME_CONTENT_TYPE_DM ");
        }
        xnotiPushHdleMessageFree(xNOTIMessage);
        return xnoti;
    }

    /* JADX WARNING: Removed duplicated region for block: B:67:0x0185  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x018c A[LOOP:3: B:69:0x018a->B:70:0x018c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01a6  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01cb  */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x021a A[LOOP:8: B:94:0x0214->B:96:0x021a, LOOP_END] */
    public static XNOTIWapPush xnotiPushHdlrParsingWSPHeader(byte[] bArr, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        byte[] bArr2 = bArr;
        if (bArr2 == null || bArr2.length == 0) {
            Log.E("pData is NULL");
            return null;
        }
        byte b = bArr2[1] & 255;
        if (b != 6) {
            Log.E("Not Support PDU Type=" + b);
            return null;
        }
        XNOTIWapPush xNOTIWapPush = new XNOTIWapPush();
        xNOTIWapPush.tWapPushInfo.nHeaderLen = bArr2[2];
        int i6 = (bArr2[3] & 255) == 31 ? 5 : 3;
        if ((bArr2[i6] & 255) < 32 || (bArr2[i6] & 255) >= 128) {
            Log.H("Content Value:" + String.valueOf(bArr2[i6]));
            Log.H("Mask Value:" + String.valueOf(bArr2[3] & Byte.MAX_VALUE));
            if ((bArr2[i6] & Byte.MAX_VALUE) == 54) {
                xNOTIWapPush.tWapPushInfo.nContentType = bArr2[i6] & Byte.MAX_VALUE;
                Log.H("Content Type: XNOTI_MIMETYPE_WAP_CONNECTIVITY_WBXML");
            } else if ((bArr2[i6] & Byte.MAX_VALUE) == 53) {
                xNOTIWapPush.tWapPushInfo.nContentType = bArr2[i6] & Byte.MAX_VALUE;
                Log.H("Content Type: XNOTI_MIMETYPE_WAP_CONNECTIVITY_XML");
            } else if ((bArr2[i6] & Byte.MAX_VALUE) == 68) {
                xNOTIWapPush.tWapPushInfo.nContentType = bArr2[i6] & Byte.MAX_VALUE;
                Log.H("Content Type: XNOTI_MIMETYPE_SYNCML_DM_NOTI");
            } else if ((bArr2[i6] & Byte.MAX_VALUE) == 66) {
                xNOTIWapPush.tWapPushInfo.nContentType = bArr2[i6] & Byte.MAX_VALUE;
                Log.H("Content Type: XNOTI_MIMETYPE_SYNCML_DM_WBXML");
            } else {
                Log.E("Not Support Content Type");
                return null;
            }
            i2 = i6 + 1;
        } else {
            int i7 = i6;
            int i8 = 0;
            while (bArr2[i7] != 0) {
                i8++;
                i7++;
            }
            byte[] bArr3 = new byte[i8];
            int i9 = 0;
            while (true) {
                byte b2 = bArr2[i6];
                if (b2 == 0) {
                    break;
                }
                bArr3[i9] = b2;
                i9++;
                i6++;
            }
            String str = new String(bArr3, Charset.defaultCharset());
            if (str.equals(XNOTIInterface.XNOTI_MIMETYPE_STR_SYNCML_DM_NOTI)) {
                xNOTIWapPush.tWapPushInfo.nContentType = 54;
            } else if (str.equals(XNOTIInterface.XNOTI_MIMETYPE_STR_WAP_CONNECTIVITY_XML)) {
                xNOTIWapPush.tWapPushInfo.nContentType = 53;
            } else if (str.equals(XNOTIInterface.XNOTI_MIMETYPE_STR_SYNCML_DM_NOTI)) {
                xNOTIWapPush.tWapPushInfo.nContentType = 68;
            } else if (str.equals("application/vnd.syncml.dm+wbxml")) {
                xNOTIWapPush.tWapPushInfo.nContentType = 66;
            } else {
                Log.E("Not Support Content Type");
                return null;
            }
            i2 = i7 + 1;
        }
        if (bArr2[i2] == 0) {
            i2++;
        }
        byte[] bArr4 = new byte[3];
        for (int i10 = 0; i10 < 3; i10++) {
            bArr4[i10] = bArr2[i2 + i10];
        }
        String str2 = new String(bArr4, Charset.defaultCharset());
        if ((bArr2[i2] & 255) == 145) {
            i5 = i2 + 1;
            xNOTIWapPush.tWapPushInfo.nSEC = bArr2[i5] & 255;
        } else {
            if (str2.equals(XNOTIInterface.XNOTI_WAP_PUSH_STR_SEC)) {
                i5 = i2 + 3 + 1;
                xNOTIWapPush.tWapPushInfo.nSEC = (bArr2[i5] & 15) | 128;
            }
            if (bArr2[i2] == 0) {
                i2++;
            }
            byte[] bArr5 = new byte[3];
            for (i3 = 0; i3 < 3; i3++) {
                bArr5[i3] = bArr2[i2 + i3];
            }
            String str3 = new String(bArr5, Charset.defaultCharset());
            if ((bArr2[i2] & 255) != 146) {
                int i11 = i2 + 1;
                int i12 = i11;
                int i13 = 0;
                while (true) {
                    int i14 = i12 + 1;
                    if (bArr2[i12] == 0) {
                        break;
                    }
                    i13++;
                    i12 = i14;
                }
                byte[] bArr6 = new byte[i13];
                int i15 = 0;
                while (true) {
                    int i16 = i11 + 1;
                    byte b3 = bArr2[i11];
                    if (b3 == 0) {
                        break;
                    }
                    bArr6[i15] = b3;
                    i15++;
                    i11 = i16;
                }
                xNOTIWapPush.tWapPushInfo.nMACLen = bArr6.length;
                xNOTIWapPush.tWapPushInfo.szMAC = bArr6;
            } else if (str3.equals(XNOTIInterface.XNOTI_WAP_PUSH_STR_MAC)) {
                int i17 = i2 + 3 + 1;
                int i18 = i17;
                int i19 = 0;
                while (true) {
                    int i20 = i18 + 1;
                    if (bArr2[i18] == 0) {
                        break;
                    }
                    i19++;
                    i18 = i20;
                }
                byte[] bArr7 = new byte[i19];
                int i21 = 0;
                while (true) {
                    int i22 = i17 + 1;
                    byte b4 = bArr2[i17];
                    if (b4 == 0) {
                        break;
                    }
                    bArr7[i21] = b4;
                    i21++;
                    i17 = i22;
                }
                xNOTIWapPush.tWapPushInfo.nMACLen = bArr7.length;
                xNOTIWapPush.tWapPushInfo.szMAC = bArr7;
            }
            xNOTIWapPush.tWapPushInfo.nBodyLen = ((i - 2) - xNOTIWapPush.tWapPushInfo.nHeaderLen) - 1;
            xNOTIWapPush.pBody = new byte[xNOTIWapPush.tWapPushInfo.nBodyLen];
            int i23 = xNOTIWapPush.tWapPushInfo.nHeaderLen + 2 + 1;
            i4 = 0;
            while (i4 < xNOTIWapPush.tWapPushInfo.nBodyLen) {
                xNOTIWapPush.pBody[i4] = bArr2[i23];
                i4++;
                i23++;
            }
            return xNOTIWapPush;
        }
        i2 = i5 + 1;
        if (bArr2[i2] == 0) {
        }
        byte[] bArr52 = new byte[3];
        while (i3 < 3) {
        }
        String str32 = new String(bArr52, Charset.defaultCharset());
        if ((bArr2[i2] & 255) != 146) {
        }
        xNOTIWapPush.tWapPushInfo.nBodyLen = ((i - 2) - xNOTIWapPush.tWapPushInfo.nHeaderLen) - 1;
        xNOTIWapPush.pBody = new byte[xNOTIWapPush.tWapPushInfo.nBodyLen];
        int i232 = xNOTIWapPush.tWapPushInfo.nHeaderLen + 2 + 1;
        i4 = 0;
        while (i4 < xNOTIWapPush.tWapPushInfo.nBodyLen) {
        }
        return xNOTIWapPush;
    }

    private static int xnotiReSyncCompare(byte[] bArr, int i, XNOTI xnoti) {
        byte[] bArr2 = {0, 0, 0, 0};
        if (!XDBResyncModeAdp.xdbGetNonceResync()) {
            Log.I("nonce resync SKIP!!!");
            return 0;
        } else if (xnoti == null || xnoti.triggerHeader == null || TextUtils.isEmpty(xnoti.triggerHeader.m_szServerID)) {
            return -1;
        } else {
            XDB.xdbSetActiveProfileIndexByServerID(xnoti.triggerHeader.m_szServerID);
            XDBProfileInfo xdbGetProfileInfo = XDBProfileAdp.xdbGetProfileInfo();
            if (xdbGetProfileInfo == null) {
                Log.E("DM profileInfo is NULL ");
                return -1;
            }
            String str = xdbGetProfileInfo.ServerID;
            String xdmAuthMakeDigest = XDMAuth.xdmAuthMakeDigest(3, str, xdbGetProfileInfo.ServerPwd, bArr2, bArr2.length, bArr, (long) (i - 16));
            if (TextUtils.isEmpty(xdmAuthMakeDigest)) {
                Log.E("Digest is NULL ");
                return -1;
            }
            Log.H(xdmAuthMakeDigest);
            if (xdmAuthMakeDigest.compareTo(new String(xnoti.digestdata, Charset.defaultCharset())) != 0) {
                Log.E("Fail");
                return -1;
            }
            Log.I("xnotiReSyncCompare Success");
            if (XDBProfileListAdp.xdbSetNotiReSyncMode(1).booleanValue()) {
                return 0;
            }
            Log.E("Fail");
            return -1;
        }
    }
}
