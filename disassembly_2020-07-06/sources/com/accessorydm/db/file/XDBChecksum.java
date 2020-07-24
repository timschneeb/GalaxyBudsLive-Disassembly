package com.accessorydm.db.file;

import com.samsung.android.fotaprovider.log.Log;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class XDBChecksum {
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0047 A[SYNTHETIC, Splitter:B:25:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x006c A[SYNTHETIC, Splitter:B:33:0x006c] */
    public static String getChecksum(String str) {
        String str2;
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(str);
            try {
                byte[] bArr = new byte[32768];
                MessageDigest instance = MessageDigest.getInstance("MD5");
                int i = 0;
                while (i != -1) {
                    i = fileInputStream2.read(bArr);
                    if (i > 0) {
                        instance.update(bArr, 0, i);
                    }
                }
                str2 = bytesToHex(instance.digest());
                try {
                    fileInputStream2.close();
                } catch (Exception e) {
                    Log.E(e.toString());
                }
            } catch (Exception e2) {
                e = e2;
                fileInputStream = fileInputStream2;
                try {
                    Log.E(e.toString());
                    if (fileInputStream != null) {
                    }
                    str2 = "";
                    Log.I("Checksum : " + str2);
                    return str2;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream2 = fileInputStream;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (Exception e3) {
                            Log.E(e3.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (fileInputStream2 != null) {
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            Log.E(e.toString());
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e5) {
                    Log.E(e5.toString());
                }
            }
            str2 = "";
            Log.I("Checksum : " + str2);
            return str2;
        }
        Log.I("Checksum : " + str2);
        return str2;
    }

    public static String bytesToHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        if (bArr != null) {
            int length = bArr.length;
            for (int i = 0; i < length; i++) {
                sb.append(String.format("%02x", new Object[]{Byte.valueOf(bArr[i])}));
            }
            return sb.toString();
        }
        Log.E("byte is null");
        return "";
    }
}
