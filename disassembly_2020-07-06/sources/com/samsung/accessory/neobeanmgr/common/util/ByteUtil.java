package com.samsung.accessory.neobeanmgr.common.util;

public class ByteUtil {
    public static final String HEX_FORMAT = "%02X";

    public static byte[] fromInt(int i) {
        int i2 = i >> 8;
        int i3 = i2 >> 8;
        return new byte[]{(byte) i, (byte) i2, (byte) i3, (byte) (i3 >> 8)};
    }

    public static byte[] fromShort(int i) {
        return new byte[]{(byte) i, (byte) (i >> 8)};
    }

    public static long intToU32(int i) {
        return ((long) i) & 4294967295L;
    }

    public static int toU8(byte b) {
        return b & 255;
    }

    public static byte valueOfLeft(byte b) {
        return (byte) ((b & 240) >> 4);
    }

    public static byte valueOfRight(byte b) {
        return (byte) (b & 15);
    }

    public static int toInt(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            i += (bArr[i2] & 255) << (i2 * 8);
        }
        return i;
    }

    public static String toHexString(byte b) {
        return String.format(HEX_FORMAT, new Object[]{Byte.valueOf(b)});
    }

    public static String toLogString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        if (bArr.length > 40) {
            for (int i = 0; i < 20; i++) {
                sb.append(String.format("%02X ", new Object[]{Byte.valueOf(bArr[i])}));
            }
            sb.append(".. .. .. ");
            for (int length = bArr.length - 20; length < bArr.length; length++) {
                sb.append(String.format("%02X ", new Object[]{Byte.valueOf(bArr[length])}));
            }
        } else {
            int length2 = bArr.length;
            for (int i2 = 0; i2 < length2; i2++) {
                sb.append(String.format("%02X ", new Object[]{Byte.valueOf(bArr[i2])}));
            }
        }
        return sb.toString();
    }
}
