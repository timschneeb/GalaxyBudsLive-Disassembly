package com.samsung.accessory.neobeanmgr.core.fmm;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.util.BufferBuilder;
import com.samsung.accessory.neobeanmgr.common.util.ByteUtil;
import com.samsung.accessory.neobeanmgr.core.fmm.FmmModels;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.FmmConfig;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.FmmConstants;
import com.samsung.accessory.neobeanmgr.core.service.message.Msg;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgGetFmmConfig;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetFmmConfig;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

public class FmmManager {
    public static final String INTI_VALUE = "";
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + FmmManager.class.getSimpleName());
    public static FmmConfig fmmConfig;

    public FmmManager() {
        fmmConfig = new FmmConfig();
    }

    public static void handleResponse(final Context context, final Intent intent) {
        new Handler().post(new Runnable() {
            /* JADX WARNING: Can't fix incorrect switch cases order */
            /* JADX WARNING: Code restructure failed: missing block: B:20:0x00b5, code lost:
                if (r0.equals(com.samsung.accessory.neobeanmgr.core.fmm.utils.FmmConstants.Operation.CONNECTION_CHECK) != false) goto L_0x00e5;
             */
            public void run() {
                String stringExtra = intent.getStringExtra("operation");
                String stringExtra2 = intent.getStringExtra("uid");
                char c = 0;
                boolean booleanExtra = intent.getBooleanExtra("status", false);
                String access$000 = FmmManager.TAG;
                Log.d(access$000, "operation : " + stringExtra + ", uid : " + stringExtra2 + ", status : " + booleanExtra);
                if (stringExtra != null) {
                    if (stringExtra.equals(FmmConstants.Operation.GET_DEVICE_INFO) && stringExtra2 == null) {
                        stringExtra2 = Application.getCoreService().getEarBudsInfo().address;
                    }
                    if (stringExtra2 != null) {
                        if (Application.getCoreService().getEarBudsInfo().address == null) {
                            FmmModels.FmmSimpleModel fmmSimpleModel = new FmmModels.FmmSimpleModel(FmmConstants.Action.ACTION_OPERATION_RESPONSE, stringExtra, stringExtra2);
                            fmmSimpleModel.setResultCode(1);
                            fmmSimpleModel.send(context);
                        } else if (!stringExtra2.toUpperCase().equals(Application.getCoreService().getEarBudsInfo().address.toUpperCase())) {
                            FmmModels.FmmSimpleModel fmmSimpleModel2 = new FmmModels.FmmSimpleModel(FmmConstants.Action.ACTION_OPERATION_RESPONSE, stringExtra, stringExtra2);
                            fmmSimpleModel2.setResultCode(5);
                            fmmSimpleModel2.send(context);
                        } else {
                            switch (stringExtra.hashCode()) {
                                case -2009453160:
                                    if (stringExtra.equals(FmmConstants.Operation.MUTE_L)) {
                                        c = 4;
                                        break;
                                    }
                                case -2009453154:
                                    if (stringExtra.equals(FmmConstants.Operation.MUTE_R)) {
                                        c = 5;
                                        break;
                                    }
                                case 2515504:
                                    if (stringExtra.equals("RING")) {
                                        c = 3;
                                        break;
                                    }
                                case 279402862:
                                    if (stringExtra.equals(FmmConstants.Operation.GET_DEVICE_INFO)) {
                                        c = 1;
                                        break;
                                    }
                                case 1190155386:
                                    if (stringExtra.equals("SET_DEVICE_INFO")) {
                                        c = 2;
                                        break;
                                    }
                                case 1852415178:
                                    break;
                                default:
                                    c = 65535;
                                    break;
                            }
                            if (c == 0) {
                                Log.d(FmmManager.TAG, "receive : CONNECTION_CHECK");
                                FmmModels.ConnectionCheckModel connectionCheckModel = new FmmModels.ConnectionCheckModel(stringExtra2);
                                if (!Application.getCoreService().isConnected()) {
                                    connectionCheckModel.setResultCode(1);
                                }
                                connectionCheckModel.send(context);
                            } else if (c == 1) {
                                Log.d(FmmManager.TAG, "receive : GET_DEVICE_INFO");
                                Application.getCoreService().sendSppMessage(new MsgGetFmmConfig());
                            } else if (c == 2) {
                                Log.d(FmmManager.TAG, "receive : SET_DEVICE_INFO");
                                FmmConfig fmmConfig = FmmManager.fmmConfig;
                                FmmConfig.setFmmConfig(intent);
                                if (Application.getCoreService().isConnected()) {
                                    FmmManager.sendSetFmmConfig(FmmManager.fmmConfig);
                                    return;
                                }
                                FmmModels.SetDeviceInfoModel setDeviceInfoModel = new FmmModels.SetDeviceInfoModel(stringExtra2);
                                setDeviceInfoModel.setResultCode(1);
                                setDeviceInfoModel.send(context);
                            } else if (c == 3) {
                                Log.d(FmmManager.TAG, "receive : RING");
                                FmmModels.RingCheckModel ringCheckModel = new FmmModels.RingCheckModel(stringExtra2);
                                if (booleanExtra) {
                                    ringCheckModel.startRing();
                                } else {
                                    ringCheckModel.stopRing();
                                }
                                ringCheckModel.send(context);
                            } else if (c == 4) {
                                Log.d(FmmManager.TAG, "receive : MUTE_L");
                                FmmModels.MuteModel muteModel = new FmmModels.MuteModel(stringExtra2, FmmConstants.Operation.MUTE_L);
                                muteModel.setMuteLeft(booleanExtra);
                                muteModel.send(context);
                            } else if (c == 5) {
                                Log.d(FmmManager.TAG, "receive : MUTE_R");
                                FmmModels.MuteModel muteModel2 = new FmmModels.MuteModel(stringExtra2, FmmConstants.Operation.MUTE_R);
                                muteModel2.setMuteRight(booleanExtra);
                                muteModel2.send(context);
                            }
                        }
                    }
                }
            }
        });
    }

    public static void sendAction(final Context context, final String str) {
        new Handler().post(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:17:0x0039  */
            /* JADX WARNING: Removed duplicated region for block: B:21:0x0091  */
            public void run() {
                char c;
                String str = str;
                int hashCode = str.hashCode();
                if (hashCode != -1881281404) {
                    if (hashCode != -290559266) {
                        if (hashCode == 1809891874 && str.equals(FmmConstants.Operation.RING_STATUS)) {
                            c = 2;
                            if (c != 0) {
                                Log.d(FmmManager.TAG, "FMM Operation : REMOVE");
                                new FmmModels.DeviceInfoModel(FmmConstants.Operation.REMOVE, Application.getCoreService().getEarBudsInfo().address).send(context);
                                return;
                            } else if (c == 1) {
                                boolean isConnected = Application.getCoreService().isConnected();
                                String access$000 = FmmManager.TAG;
                                Log.d(access$000, "FMM Operation : CONNECTION : " + isConnected);
                                new FmmModels.ConnectionModel(Application.getCoreService().getEarBudsInfo().address, isConnected).send(context);
                                return;
                            } else if (c == 2) {
                                Log.d(FmmManager.TAG, "FMM Operation : RING_STATUS");
                                new FmmModels.RingStatusModel(Application.getCoreService().getEarBudsInfo().address).send(context);
                                return;
                            } else {
                                return;
                            }
                        }
                    } else if (str.equals(FmmConstants.Operation.CONNECTION)) {
                        c = 1;
                        if (c != 0) {
                        }
                    }
                } else if (str.equals(FmmConstants.Operation.REMOVE)) {
                    c = 0;
                    if (c != 0) {
                    }
                }
                c = 65535;
                if (c != 0) {
                }
            }
        });
    }

    public static void responseSetDeviceInfo(Context context) {
        Log.d(TAG, "responseSetDeviceInfo");
        new FmmModels.SetDeviceInfoModel(Application.getCoreService().getEarBudsInfo().address).send(context);
    }

    public static void responseGetDeviceInfo(Context context, ByteBuffer byteBuffer) {
        Log.d(TAG, "responseGetDeviceInfo");
        sendGetFmmConfig(byteBuffer, fmmConfig);
        new FmmModels.getDeviceInfoModel(FmmConstants.Operation.GET_DEVICE_INFO, Application.getCoreService().getEarBudsInfo().address, fmmConfig).send(context);
    }

    /* access modifiers changed from: private */
    public static void sendSetFmmConfig(FmmConfig fmmConfig2) {
        int i;
        int i2;
        int i3 = Application.getCoreService().getEarBudsInfo().batteryL > 0 ? 1 : 0;
        int i4 = Application.getCoreService().getEarBudsInfo().batteryR > 0 ? 1 : 0;
        int i5 = Application.getCoreService().getEarBudsInfo().extendedRevision < 4 ? 3 : Application.getCoreService().getEarBudsInfo().fmmRevision;
        BufferBuilder bufferBuilder = new BufferBuilder();
        bufferBuilder.put((byte) i5);
        bufferBuilder.put((byte) ((i3 << 4) | i4));
        if (i3 != 0) {
            bufferBuilder.put((byte) (!"".equals(FmmConfig.left_findingSupport) ? FmmConstants.SUPPORT.equals(FmmConfig.left_findingSupport) : 255));
            bufferBuilder.put((byte) (!"".equals(FmmConfig.left_e2e) ? FmmConstants.SUPPORT.equals(FmmConfig.left_e2e) : 255));
            byte[] bArr = new byte[16];
            if (!"".equals(FmmConfig.left_secretKey)) {
                bArr = decode(FmmConfig.left_secretKey);
            } else {
                Arrays.fill(bArr, (byte) -1);
            }
            if (i5 < 3) {
                for (byte put : bArr) {
                    bufferBuilder.put(put);
                }
            } else {
                for (byte put2 : makeXOR(bArr)) {
                    bufferBuilder.put(put2);
                }
            }
            bufferBuilder.putInt(FmmConfig.left_maxN != -1 ? FmmConfig.left_maxN : 0);
            bufferBuilder.put((byte) (FmmConfig.left_region != -1 ? FmmConfig.left_region : -1));
            byte[] bytes = FmmConfig.left_fmmToken.getBytes();
            if (i5 < 2) {
                bufferBuilder.put((byte) bytes.length);
                byte[] bArr2 = new byte[26];
                System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
                for (byte put3 : bArr2) {
                    bufferBuilder.put(put3);
                }
            } else {
                byte[] bArr3 = new byte[31];
                if (!"".equals(FmmConfig.left_fmmToken)) {
                    i2 = bytes.length;
                    System.arraycopy(bytes, 0, bArr3, 0, i2);
                } else {
                    Arrays.fill(bArr3, (byte) -1);
                    i2 = 255;
                }
                bufferBuilder.put((byte) i2);
                for (byte put4 : bArr3) {
                    bufferBuilder.put(put4);
                }
                byte[] bArr4 = new byte[16];
                if (!"".equals(FmmConfig.left_iv)) {
                    bArr4 = decode(FmmConfig.left_iv);
                } else {
                    Arrays.fill(bArr4, (byte) -1);
                }
                if (i5 < 3) {
                    for (byte put5 : bArr4) {
                        bufferBuilder.put(put5);
                    }
                } else {
                    for (byte put6 : makeXOR(bArr4)) {
                        bufferBuilder.put(put6);
                    }
                }
            }
        }
        if (i4 != 0) {
            bufferBuilder.put((byte) (!"".equals(FmmConfig.right_findingSupport) ? FmmConstants.SUPPORT.equals(FmmConfig.right_findingSupport) : 255));
            bufferBuilder.put((byte) (!"".equals(FmmConfig.right_e2e) ? FmmConstants.SUPPORT.equalsIgnoreCase(FmmConfig.right_e2e) : 255));
            byte[] bArr5 = new byte[16];
            if (!"".equals(FmmConfig.right_secretKey)) {
                bArr5 = decode(FmmConfig.right_secretKey);
            } else {
                Arrays.fill(bArr5, (byte) -1);
            }
            if (i5 < 3) {
                for (byte put7 : bArr5) {
                    bufferBuilder.put(put7);
                }
            } else {
                for (byte put8 : makeXOR(bArr5)) {
                    bufferBuilder.put(put8);
                }
            }
            bufferBuilder.putInt(FmmConfig.right_maxN != -1 ? FmmConfig.right_maxN : 0);
            bufferBuilder.put((byte) (FmmConfig.right_region != -1 ? FmmConfig.right_region : -1));
            byte[] bytes2 = FmmConfig.right_fmmToken.getBytes();
            if (i5 < 2) {
                bufferBuilder.put((byte) bytes2.length);
                byte[] bArr6 = new byte[26];
                System.arraycopy(bytes2, 0, bArr6, 0, bytes2.length);
                for (byte put9 : bArr6) {
                    bufferBuilder.put(put9);
                }
            } else {
                byte[] bArr7 = new byte[31];
                if (!"".equals(FmmConfig.right_fmmToken)) {
                    i = bytes2.length;
                    System.arraycopy(bytes2, 0, bArr7, 0, i);
                } else {
                    Arrays.fill(bArr7, (byte) -1);
                    i = 255;
                }
                bufferBuilder.put((byte) i);
                for (byte put10 : bArr7) {
                    bufferBuilder.put(put10);
                }
                byte[] bArr8 = new byte[16];
                if (!"".equals(FmmConfig.right_iv)) {
                    bArr8 = decode(FmmConfig.right_iv);
                } else {
                    Arrays.fill(bArr8, (byte) -1);
                }
                if (i5 < 3) {
                    for (byte put11 : bArr8) {
                        bufferBuilder.put(put11);
                    }
                } else {
                    for (byte put12 : makeXOR(bArr8)) {
                        bufferBuilder.put(put12);
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        byte[] array = bufferBuilder.array();
        int length = array.length;
        for (int i6 = 0; i6 < length; i6++) {
            sb.append(String.format("%02X ", new Object[]{Byte.valueOf(array[i6])}));
        }
        Log.d(TAG, "SET_FMM_CONFIG : " + sb.toString());
        Application.getCoreService().sendSppMessage(new MsgSetFmmConfig(bufferBuilder));
    }

    public static void sendGetFmmConfig(ByteBuffer byteBuffer, FmmConfig fmmConfig2) {
        ByteBuffer byteBuffer2 = byteBuffer;
        StringBuilder sb = new StringBuilder();
        byte[] array = byteBuffer.array();
        int length = array.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", new Object[]{Byte.valueOf(array[i])}));
        }
        String str = TAG;
        Log.d(str, "GET_FMM_CONFIG : " + sb.toString());
        FmmConfig.revision = byteBuffer.get();
        byte b = byteBuffer.get();
        boolean z = ByteUtil.valueOfLeft(b) == 1;
        boolean z2 = ByteUtil.valueOfRight(b) == 1;
        String str2 = FmmConstants.SUPPORT;
        String str3 = "";
        if (z) {
            byte b2 = byteBuffer.get();
            if (b2 != -1) {
                FmmConfig.left_findingSupport = b2 == 1 ? str2 : FmmConstants.NOT_SUPPORT;
            } else {
                FmmConfig.left_findingSupport = str3;
            }
            byte b3 = byteBuffer.get();
            if (b3 != -1) {
                FmmConfig.left_e2e = b3 == 1 ? str2 : FmmConstants.NOT_SUPPORT;
            } else {
                FmmConfig.left_e2e = str3;
            }
            byte[] bArr = new byte[16];
            byteBuffer2.get(bArr);
            StringBuilder sb2 = new StringBuilder();
            int length2 = bArr.length;
            for (int i2 = 0; i2 < length2; i2++) {
                sb2.append(String.format("%02X ", new Object[]{Byte.valueOf(bArr[i2])}));
            }
            String str4 = TAG;
            Log.d(str4, "GET_FMM_CONFIG left_secret_key : " + sb2.toString());
            if (FmmConfig.revision < 3) {
                FmmConfig.left_secretKey = isSupportedValue(bArr) ? encode(bArr) : str3;
            } else {
                byte[] makeXOR = makeXOR(bArr);
                FmmConfig.left_secretKey = isSupportedValue(makeXOR) ? encode(makeXOR) : str3;
            }
            int i3 = byteBuffer.getInt();
            if (i3 == 0) {
                i3 = -1;
            }
            FmmConfig.left_maxN = i3;
            FmmConfig.left_region = byteBuffer.get();
            byte b4 = byteBuffer.get();
            if (FmmConfig.revision < 2) {
                byte[] bArr2 = new byte[26];
                byteBuffer2.get(bArr2);
                FmmConfig.left_fmmToken = new String(bArr2, 0, Math.max(b4, 0));
            } else {
                byte[] bArr3 = new byte[31];
                byteBuffer2.get(bArr3);
                FmmConfig.left_fmmToken = new String(bArr3, 0, Math.max(b4, 0));
                byte[] bArr4 = new byte[16];
                byteBuffer2.get(bArr4);
                if (FmmConfig.revision < 3) {
                    FmmConfig.left_iv = isSupportedValue(bArr4) ? encode(bArr4) : str3;
                } else {
                    byte[] makeXOR2 = makeXOR(bArr4);
                    FmmConfig.left_iv = isSupportedValue(makeXOR2) ? encode(makeXOR2) : str3;
                }
            }
            byte[] bArr5 = new byte[11];
            byteBuffer2.get(bArr5);
            FmmConfig.left_sn = isSupportedValue(bArr5) ? new String(bArr5) : str3;
        }
        if (z2) {
            byte b5 = byteBuffer.get();
            if (b5 != -1) {
                FmmConfig.right_findingSupport = b5 == 1 ? str2 : FmmConstants.NOT_SUPPORT;
            } else {
                FmmConfig.right_findingSupport = str3;
            }
            byte b6 = byteBuffer.get();
            if (b6 != -1) {
                if (b6 != 1) {
                    str2 = FmmConstants.NOT_SUPPORT;
                }
                FmmConfig.right_e2e = str2;
            } else {
                FmmConfig.right_e2e = str3;
            }
            byte[] bArr6 = new byte[16];
            byteBuffer2.get(bArr6);
            if (FmmConfig.revision < 3) {
                FmmConfig.right_secretKey = isSupportedValue(bArr6) ? encode(bArr6) : str3;
            } else {
                byte[] makeXOR3 = makeXOR(bArr6);
                FmmConfig.right_secretKey = isSupportedValue(makeXOR3) ? encode(makeXOR3) : str3;
            }
            int i4 = byteBuffer.getInt();
            if (i4 == 0) {
                i4 = -1;
            }
            FmmConfig.right_maxN = i4;
            FmmConfig.right_region = byteBuffer.get();
            byte b7 = byteBuffer.get();
            if (FmmConfig.revision < 2) {
                byte[] bArr7 = new byte[26];
                byteBuffer2.get(bArr7);
                FmmConfig.right_fmmToken = new String(bArr7, 0, Math.max(b7, 0));
            } else {
                byte[] bArr8 = new byte[31];
                byteBuffer2.get(bArr8);
                FmmConfig.right_fmmToken = new String(bArr8, 0, Math.max(b7, 0));
                byte[] bArr9 = new byte[16];
                byteBuffer2.get(bArr9);
                if (FmmConfig.revision < 3) {
                    FmmConfig.right_iv = isSupportedValue(bArr9) ? encode(bArr9) : str3;
                } else {
                    byte[] makeXOR4 = makeXOR(bArr9);
                    FmmConfig.right_iv = isSupportedValue(makeXOR4) ? encode(makeXOR4) : str3;
                }
            }
            byte[] bArr10 = new byte[11];
            byteBuffer2.get(bArr10);
            if (isSupportedValue(bArr10)) {
                str3 = new String(bArr10);
            }
            FmmConfig.right_sn = str3;
        }
        FmmConfig.printFmmConfing();
    }

    private static byte[] decode(String str) {
        byte[] bArr;
        if (Build.VERSION.SDK_INT >= 26) {
            bArr = Base64.getDecoder().decode(str.getBytes());
        } else {
            bArr = android.util.Base64.decode(str.getBytes(), 0);
        }
        String str2 = TAG;
        Log.d(str2, "decode size : " + bArr.length);
        String str3 = TAG;
        Log.d(str3, "decode data : " + new String(bArr));
        return bArr;
    }

    private static String encode(byte[] bArr) {
        String str;
        if (Build.VERSION.SDK_INT >= 26) {
            str = Base64.getEncoder().encodeToString(bArr);
        } else {
            str = android.util.Base64.encodeToString(bArr, 0);
        }
        String str2 = TAG;
        Log.d(str2, "encode data : " + str);
        return str;
    }

    private static boolean isSupportedValue(byte[] bArr) {
        for (byte b : bArr) {
            if (b != -1) {
                return true;
            }
        }
        return false;
    }

    private static byte[] makeXOR(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = (byte) (bArr[i] ^ Msg.SOM);
        }
        return bArr2;
    }
}
