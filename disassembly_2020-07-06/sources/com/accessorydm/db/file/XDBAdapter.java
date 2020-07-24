package com.accessorydm.db.file;

import android.text.TextUtils;
import com.accessorydm.adapter.XDMTargetAdapter;
import com.accessorydm.interfaces.XDBInterface;
import com.accessorydm.interfaces.XDMDefInterface;
import com.accessorydm.interfaces.XDMInterface;
import com.accessorydm.interfaces.XFOTAInterface;
import com.accessorydm.interfaces.XTPInterface;
import com.accessorydm.tp.XTPHttpUtil;
import com.samsung.android.fotaprovider.log.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

public class XDBAdapter implements XDMDefInterface, XDMInterface, XTPInterface, XDBInterface, XFOTAInterface {
    public static int xdbFileFreeSizeCheck(long j) {
        long xdmGetAvailableStorageSize = XDMTargetAdapter.xdmGetAvailableStorageSize();
        long xdmGetTotalStorageSize = XDMTargetAdapter.xdmGetTotalStorageSize();
        Log.I(String.format(Locale.US, "Remain size : %d, Total size : %d and Required Size : %d bytes", new Object[]{Long.valueOf(xdmGetAvailableStorageSize), Long.valueOf(xdmGetTotalStorageSize), Long.valueOf(j)}));
        if (!XDMTargetAdapter.xdmGetStorageAvailable() || j > xdmGetAvailableStorageSize) {
            return 4;
        }
        Log.I("Storage >>> XDB_FS_OK...");
        return 0;
    }

    public static boolean xdbFileExist(String str) {
        try {
            File file = new File(str);
            if (!file.exists() || !file.canRead()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.E(e.toString());
            return false;
        }
    }

    public int xdbFileExists(String str) {
        try {
            File file = new File(str);
            if (!file.exists() || !file.canRead()) {
                return -1;
            }
            return 0;
        } catch (Exception e) {
            Log.E(e.toString());
            return -1;
        }
    }

    public static boolean xdbFolderExist(String str) {
        try {
            if (new File(str).isDirectory()) {
                return true;
            }
            Log.I("Folder is not Exist!!");
            return false;
        } catch (Exception e) {
            Log.E(e.toString());
            return false;
        }
    }

    public static boolean xdbFolderCreate(String str) {
        try {
            File file = new File(str);
            if (file.isDirectory()) {
                return true;
            }
            if (!file.mkdirs()) {
                return false;
            }
            Log.H("make [" + str + "] folder");
            return true;
        } catch (Exception e) {
            Log.E(e.toString());
            return false;
        }
    }

    public long xdbFileGetSize(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                return file.length();
            }
            Log.E("file is not exist : " + str);
            return 0;
        } catch (Exception e) {
            Log.E(e.toString());
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0032 A[SYNTHETIC, Splitter:B:21:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0042 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0046 A[SYNTHETIC, Splitter:B:30:0x0046] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    public boolean xdbFileRead(String str, byte[] bArr, int i, int i2) {
        int i3;
        if (i2 <= 0) {
            return false;
        }
        DataInputStream dataInputStream = null;
        try {
            DataInputStream dataInputStream2 = new DataInputStream(new FileInputStream(str));
            try {
                i3 = dataInputStream2.read(bArr, i, i2);
                try {
                    dataInputStream2.close();
                } catch (Exception e) {
                    Log.E(e.toString());
                }
            } catch (Exception e2) {
                e = e2;
                dataInputStream = dataInputStream2;
                try {
                    Log.E(e.toString());
                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (Exception e3) {
                            Log.E(e3.toString());
                        }
                    }
                    i3 = 0;
                    if (i3 != -1) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (Exception e4) {
                            Log.E(e4.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                dataInputStream = dataInputStream2;
                if (dataInputStream != null) {
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            Log.E(e.toString());
            if (dataInputStream != null) {
            }
            i3 = 0;
            if (i3 != -1) {
            }
        }
        if (i3 != -1) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0033 A[SYNTHETIC, Splitter:B:18:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0042 A[SYNTHETIC, Splitter:B:24:0x0042] */
    public static boolean xdbFileWrite(String str, int i, Object obj) {
        byte[] bArr = (byte[]) obj;
        DataOutputStream dataOutputStream = null;
        try {
            DataOutputStream dataOutputStream2 = new DataOutputStream(new FileOutputStream(str));
            try {
                dataOutputStream2.write(bArr, 0, i);
                try {
                    dataOutputStream2.close();
                    return true;
                } catch (Exception e) {
                    Log.E(e.toString());
                    return true;
                }
            } catch (Exception e2) {
                e = e2;
                dataOutputStream = dataOutputStream2;
                try {
                    Log.E(e.toString());
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Exception e3) {
                            Log.E(e3.toString());
                        }
                    }
                    return false;
                } catch (Throwable th) {
                    th = th;
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Exception e4) {
                            Log.E(e4.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                dataOutputStream = dataOutputStream2;
                if (dataOutputStream != null) {
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            Log.E(e.toString());
            if (dataOutputStream != null) {
            }
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0032 A[SYNTHETIC, Splitter:B:19:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0041 A[SYNTHETIC, Splitter:B:25:0x0041] */
    public static boolean xdbFileWrite(String str, Object obj) {
        ObjectOutputStream objectOutputStream = null;
        try {
            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new FileOutputStream(str));
            try {
                objectOutputStream2.reset();
                objectOutputStream2.writeObject(obj);
                try {
                    objectOutputStream2.close();
                    return true;
                } catch (Exception e) {
                    Log.E(e.toString());
                    return true;
                }
            } catch (Exception e2) {
                e = e2;
                objectOutputStream = objectOutputStream2;
                try {
                    Log.E(e.toString());
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (Exception e3) {
                            Log.E(e3.toString());
                        }
                    }
                    return false;
                } catch (Throwable th) {
                    th = th;
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (Exception e4) {
                            Log.E(e4.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                objectOutputStream = objectOutputStream2;
                if (objectOutputStream != null) {
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            Log.E(e.toString());
            if (objectOutputStream != null) {
            }
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x002f A[SYNTHETIC, Splitter:B:19:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x003e A[SYNTHETIC, Splitter:B:25:0x003e] */
    public boolean xdbFileCreateWrite(String str, byte[] bArr) {
        DataOutputStream dataOutputStream = null;
        try {
            DataOutputStream dataOutputStream2 = new DataOutputStream(new FileOutputStream(str));
            try {
                dataOutputStream2.write(bArr);
                try {
                    dataOutputStream2.close();
                    return true;
                } catch (Exception e) {
                    Log.E(e.toString());
                    return true;
                }
            } catch (Exception e2) {
                e = e2;
                dataOutputStream = dataOutputStream2;
                try {
                    Log.E(e.toString());
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Exception e3) {
                            Log.E(e3.toString());
                        }
                    }
                    return false;
                } catch (Throwable th) {
                    th = th;
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Exception e4) {
                            Log.E(e4.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                dataOutputStream = dataOutputStream2;
                if (dataOutputStream != null) {
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            Log.E(e.toString());
            if (dataOutputStream != null) {
            }
            return false;
        }
    }

    public boolean xdbFileDeltaCreateWrite(FileOutputStream fileOutputStream, byte[] bArr, int i) {
        Log.I("");
        try {
            fileOutputStream.write(bArr, 0, i);
            fileOutputStream.flush();
            fileOutputStream.getFD().sync();
            return true;
        } catch (Exception e) {
            Log.E(e.toString());
            return false;
        }
    }

    public static boolean xdbFileDelete(String str) {
        try {
            File file = new File(str);
            if (!file.exists() || file.delete()) {
                Log.I("xdbFileDelete true");
                return true;
            }
            Log.I("xdbFileDelete false");
            return false;
        } catch (Exception e) {
            Log.E(e.toString());
            return false;
        }
    }

    public int xdbFileRemove(String str) {
        try {
            File file = new File(str);
            if (!file.exists() || file.delete()) {
                return 0;
            }
            return -1;
        } catch (Exception e) {
            Log.E(e.toString());
            return -1;
        }
    }

    public static int xdbGetConnectType(int i) {
        if (i == 0) {
            String xdbGetServerProtocol = XDBProfileAdp.xdbGetServerProtocol();
            if (!TextUtils.isEmpty(xdbGetServerProtocol)) {
                return XTPHttpUtil.xtpHttpExchangeProtocolType(xdbGetServerProtocol);
            }
            return 2;
        } else if (i != 1) {
            return 2;
        } else {
            String xdbGetFUMOProtocol = XDBFumoAdp.xdbGetFUMOProtocol();
            if (TextUtils.isEmpty(xdbGetFUMOProtocol)) {
                return 2;
            }
            Log.H(String.format("Protool [%s]", new Object[]{xdbGetFUMOProtocol}));
            return XTPHttpUtil.xtpHttpExchangeProtocolType(xdbGetFUMOProtocol);
        }
    }

    public static String xdbGetNotiDigest(int i, String str, int i2, byte[] bArr, int i3) {
        if (TextUtils.isEmpty(str)) {
            Log.E("pServerID is NULL");
            return null;
        } else if (i == 0) {
            return XDB.xdbGetNotiDigest(str, i2, bArr, i3);
        } else {
            Log.E("Not Support Application :" + i);
            return null;
        }
    }

    public static String xdbCheckOMADDURL(String str) {
        if (str.indexOf(38) <= 0) {
            return str;
        }
        return str.replaceAll("&amp;", "&");
    }
}
