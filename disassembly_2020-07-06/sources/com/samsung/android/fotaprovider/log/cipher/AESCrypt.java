package com.samsung.android.fotaprovider.log.cipher;

import android.util.Base64;
import com.accessorydm.tp.urlconnect.HttpNetworkInterface;
import com.samsung.android.fotaprovider.log.Log;
import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt {
    private static final String CRYPTO_KEY_ALGORITHM = "AES";

    public static String encrypt(String str) {
        try {
            return new String(Base64.encode(getEncryptResult(str.getBytes(HttpNetworkInterface.XTP_HTTP_UTF8)), 2), HttpNetworkInterface.XTP_HTTP_UTF8);
        } catch (Exception e) {
            Log.E("Exception : " + e.toString());
            return "";
        }
    }

    private static byte[] getEncryptResult(byte[] bArr) throws Exception {
        Cipher instance = Cipher.getInstance(CRYPTO_KEY_ALGORITHM);
        instance.init(1, new SecretKeySpec(mealyMachine(5932, 16).getBytes(HttpNetworkInterface.XTP_HTTP_UTF8), CRYPTO_KEY_ALGORITHM));
        return instance.doFinal(bArr);
    }

    private static String mealyMachine(int i, int i2) {
        int i3 = i2;
        byte[] bArr = new byte[i3];
        int[][] iArr = {new int[]{11, 0}, new int[]{0, 4}, new int[]{8, 15}, new int[]{11, 2}, new int[]{0, 3}, new int[]{9, 0}, new int[]{15, 0}, new int[]{0, 0}, new int[]{5, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{1, 6}, new int[]{0, 0}, new int[]{3, 13}, new int[]{0, 0}, new int[]{2, 13}};
        char[][] cArr = {new char[]{'s', '3'}, new char[]{'v', 'n'}, new char[]{'1', '9'}, new char[]{'m', '0'}, new char[]{'e', 'c'}, new char[]{'3', 'B'}, new char[]{'7', 'N'}, new char[]{'k', '2'}, new char[]{'2', 'C'}, new char[]{'a', 'C'}, new char[]{'J', '2'}, new char[]{'y', 'l'}, new char[]{'8', 'd'}, new char[]{'1', '0'}, new char[]{'A', '^'}, new char[]{'7', '0'}};
        int i4 = 0;
        int i5 = i;
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = i5 & 1;
            i5 >>= 1;
            bArr[i6] = (byte) cArr[i4][i7];
            i4 = iArr[i4][i7];
        }
        try {
            return new String(bArr, HttpNetworkInterface.XTP_HTTP_UTF8);
        } catch (UnsupportedEncodingException unused) {
            return "";
        }
    }
}
