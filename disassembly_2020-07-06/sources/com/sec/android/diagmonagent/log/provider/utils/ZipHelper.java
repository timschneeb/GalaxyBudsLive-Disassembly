package com.sec.android.diagmonagent.log.provider.utils;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {
    private static final int BUFFER_SIZE = 2048;
    private static final int COMPRESSION_LEVEL = 8;

    /* JADX WARNING: Removed duplicated region for block: B:26:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0056  */
    public static String zip(String str, String str2) throws Exception {
        BufferedOutputStream bufferedOutputStream;
        FileOutputStream fileOutputStream;
        ZipOutputStream zipOutputStream;
        File file = new File(str);
        if (file.isFile() || file.isDirectory()) {
            ZipOutputStream zipOutputStream2 = null;
            try {
                fileOutputStream = new FileOutputStream(str2);
                try {
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    try {
                        zipOutputStream = new ZipOutputStream(bufferedOutputStream);
                    } catch (Throwable th) {
                        th = th;
                        if (zipOutputStream2 != null) {
                            zipOutputStream2.close();
                        }
                        if (bufferedOutputStream != null) {
                            bufferedOutputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        throw th;
                    }
                    try {
                        zipOutputStream.setLevel(8);
                        zipEntry(file, str, zipOutputStream);
                        zipOutputStream.finish();
                        zipOutputStream.close();
                        bufferedOutputStream.close();
                        fileOutputStream.close();
                        return str2;
                    } catch (Throwable th2) {
                        th = th2;
                        zipOutputStream2 = zipOutputStream;
                        if (zipOutputStream2 != null) {
                        }
                        if (bufferedOutputStream != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bufferedOutputStream = null;
                    if (zipOutputStream2 != null) {
                    }
                    if (bufferedOutputStream != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream = null;
                bufferedOutputStream = null;
                if (zipOutputStream2 != null) {
                }
                if (bufferedOutputStream != null) {
                }
                if (fileOutputStream != null) {
                }
                throw th;
            }
        } else {
            throw new Exception("not found");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x007a  */
    private static void zipEntry(File file, String str, ZipOutputStream zipOutputStream) throws Exception {
        if (!file.isDirectory()) {
            BufferedInputStream bufferedInputStream = null;
            try {
                String path = file.getPath();
                Log.d(DiagMonUtil.TAG, path);
                StringTokenizer stringTokenizer = new StringTokenizer(path, "/");
                int countTokens = stringTokenizer.countTokens();
                String obj = stringTokenizer.toString();
                while (countTokens != 0) {
                    countTokens--;
                    obj = stringTokenizer.nextToken();
                }
                BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(file));
                try {
                    ZipEntry zipEntry = new ZipEntry(obj);
                    zipEntry.setTime(file.lastModified());
                    zipOutputStream.putNextEntry(zipEntry);
                    byte[] bArr = new byte[2048];
                    while (true) {
                        int read = bufferedInputStream2.read(bArr, 0, 2048);
                        if (read != -1) {
                            zipOutputStream.write(bArr, 0, read);
                        } else {
                            zipOutputStream.closeEntry();
                            bufferedInputStream2.close();
                            return;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedInputStream = bufferedInputStream2;
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (bufferedInputStream != null) {
                }
                throw th;
            }
        } else if (!file.getName().equalsIgnoreCase(".metadata")) {
            File[] listFiles = file.listFiles();
            for (File zipEntry2 : listFiles) {
                zipEntry(zipEntry2, str, zipOutputStream);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0059  */
    public static void unzip(String str, String str2, boolean z) throws Exception {
        ZipInputStream zipInputStream;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                zipInputStream = new ZipInputStream(fileInputStream);
                while (true) {
                    try {
                        ZipEntry nextEntry = zipInputStream.getNextEntry();
                        if (nextEntry != null) {
                            String name = nextEntry.getName();
                            if (z) {
                                name = name.toLowerCase();
                            }
                            File file = new File(str2, name);
                            if (nextEntry.isDirectory()) {
                                new File(file.getAbsolutePath()).mkdirs();
                            } else {
                                new File(file.getParent()).mkdirs();
                                unzipEntry(zipInputStream, file);
                            }
                        } else {
                            zipInputStream.close();
                            fileInputStream.close();
                            return;
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (zipInputStream != null) {
                            zipInputStream.close();
                        }
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                zipInputStream = null;
                if (zipInputStream != null) {
                }
                if (fileInputStream != null) {
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            zipInputStream = null;
            fileInputStream = null;
            if (zipInputStream != null) {
            }
            if (fileInputStream != null) {
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0020  */
    protected static File unzipEntry(ZipInputStream zipInputStream, File file) throws Exception {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            try {
                byte[] bArr = new byte[2048];
                while (true) {
                    int read = zipInputStream.read(bArr);
                    if (read != -1) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileOutputStream.close();
                        return file;
                    }
                }
            } catch (Throwable th) {
                th = th;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream = null;
            if (fileOutputStream != null) {
            }
            throw th;
        }
    }
}
