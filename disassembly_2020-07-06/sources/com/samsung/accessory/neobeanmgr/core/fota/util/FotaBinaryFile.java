package com.samsung.accessory.neobeanmgr.core.fota.util;

import android.util.Log;
import com.samsung.accessory.neobeanmgr.common.util.BufferBuilder;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgFotaSession;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FotaBinaryFile implements MsgFotaSession.FotaBinaryFileGetData {
    private static final int MAGIC_NUMBER = -889271554;
    private static final String TAG = "NeoBean_FotaBinaryFile";
    private long file_crc32 = 0;
    private final File mBinaryFile;
    private final List<Entry> mEntryList = new ArrayList();

    public FotaBinaryFile(File file) {
        this.mBinaryFile = file;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:21:0x009b=Splitter:B:21:0x009b, B:35:0x00bf=Splitter:B:35:0x00bf} */
    public boolean open() {
        BufferedInputStream bufferedInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream2;
        RandomAccessFile randomAccessFile;
        if (!this.mBinaryFile.exists()) {
            Log.e(TAG, "open() : mBinaryFile.exists() == false");
            return false;
        }
        this.mEntryList.clear();
        try {
            fileInputStream = new FileInputStream(this.mBinaryFile);
            try {
                bufferedInputStream = new BufferedInputStream(fileInputStream);
            } catch (IOException e) {
                e = e;
                bufferedInputStream2 = null;
                try {
                    e.printStackTrace();
                    safeClose(bufferedInputStream2);
                    safeClose(fileInputStream);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    bufferedInputStream = bufferedInputStream2;
                    safeClose(bufferedInputStream);
                    safeClose(fileInputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedInputStream = null;
                safeClose(bufferedInputStream);
                safeClose(fileInputStream);
                throw th;
            }
            try {
                if (((int) read4Byte(bufferedInputStream)) == MAGIC_NUMBER) {
                    read4Byte(bufferedInputStream);
                    int read4Byte = (int) read4Byte(bufferedInputStream);
                    for (int i = 0; i < read4Byte; i++) {
                        int read4Byte2 = (int) read4Byte(bufferedInputStream);
                        int read4Byte3 = (int) read4Byte(bufferedInputStream);
                        long read4Byte4 = read4Byte(bufferedInputStream);
                        long read4Byte5 = read4Byte(bufferedInputStream);
                        Entry entry = r12;
                        List<Entry> list = this.mEntryList;
                        Entry entry2 = new Entry(read4Byte2, read4Byte3, read4Byte4, read4Byte5);
                        list.add(entry);
                        Log.d(TAG, String.format("id=%d, crc=%x, offset=%d, size=%d", new Object[]{Integer.valueOf(read4Byte2), Integer.valueOf(read4Byte3), Long.valueOf(read4Byte4), Long.valueOf(read4Byte5)}));
                    }
                    byte[] bArr = new byte[4];
                    try {
                        randomAccessFile = getRandomAccessFile();
                        try {
                            randomAccessFile.seek(randomAccessFile.length() - 4);
                            randomAccessFile.read(bArr, 0, 4);
                        } catch (IOException e2) {
                            e = e2;
                            try {
                                e.printStackTrace();
                                safeClose(randomAccessFile);
                                this.file_crc32 = bufferToLong(bArr);
                                safeClose(bufferedInputStream);
                                safeClose(fileInputStream);
                                Log.d(TAG, "open() : return true");
                                return true;
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        }
                    } catch (IOException e3) {
                        e = e3;
                        randomAccessFile = null;
                        e.printStackTrace();
                        safeClose(randomAccessFile);
                        this.file_crc32 = bufferToLong(bArr);
                        safeClose(bufferedInputStream);
                        safeClose(fileInputStream);
                        Log.d(TAG, "open() : return true");
                        return true;
                    } catch (Throwable th4) {
                        th = th4;
                        randomAccessFile = null;
                        safeClose(randomAccessFile);
                        throw th;
                    }
                    safeClose(randomAccessFile);
                    this.file_crc32 = bufferToLong(bArr);
                    safeClose(bufferedInputStream);
                    safeClose(fileInputStream);
                    Log.d(TAG, "open() : return true");
                    return true;
                }
                throw new IOException("wrong MAGIC_NUMBER");
            } catch (IOException e4) {
                e = e4;
                bufferedInputStream2 = bufferedInputStream;
            } catch (Throwable th5) {
                th = th5;
                safeClose(bufferedInputStream);
                safeClose(fileInputStream);
                throw th;
            }
        } catch (IOException e5) {
            e = e5;
            bufferedInputStream2 = null;
            fileInputStream = null;
            e.printStackTrace();
            safeClose(bufferedInputStream2);
            safeClose(fileInputStream);
            return false;
        } catch (Throwable th6) {
            th = th6;
            fileInputStream = null;
            bufferedInputStream = null;
            safeClose(bufferedInputStream);
            safeClose(fileInputStream);
            throw th;
        }
    }

    public List<Entry> getEntryList() {
        return this.mEntryList;
    }

    public long getFile_crc32() {
        Log.d(TAG, "getFile_crc32 :" + this.file_crc32);
        return this.file_crc32;
    }

    public RandomAccessFile getRandomAccessFile() throws FileNotFoundException {
        return new RandomAccessFile(this.mBinaryFile, "r");
    }

    public static class Entry {
        public final int crc32;
        public final int id;
        public final long offset;
        public final long size;

        Entry(int i, int i2, long j, long j2) {
            this.id = i;
            this.crc32 = i2;
            this.offset = j;
            this.size = j2;
        }
    }

    private long read4Byte(BufferedInputStream bufferedInputStream) throws IOException {
        byte[] bArr = new byte[4];
        if (bufferedInputStream.read(bArr) != -1) {
            return bufferToLong(bArr);
        }
        throw new IOException();
    }

    private long bufferToLong(byte[] bArr) {
        return ((((long) bArr[3]) & 255) << 24) | ((((long) bArr[2]) & 255) << 16) | ((((long) bArr[1]) & 255) << 8) | (255 & ((long) bArr[0]));
    }

    private void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getDataForMsgFotaSession() {
        BufferBuilder bufferBuilder = new BufferBuilder();
        List<Entry> entryList = getEntryList();
        bufferBuilder.putInt((int) getFile_crc32());
        bufferBuilder.put((byte) entryList.size());
        for (Entry next : entryList) {
            bufferBuilder.put((byte) next.id);
            bufferBuilder.putInt((int) next.size);
            bufferBuilder.putInt(next.crc32);
        }
        return bufferBuilder.array();
    }
}
