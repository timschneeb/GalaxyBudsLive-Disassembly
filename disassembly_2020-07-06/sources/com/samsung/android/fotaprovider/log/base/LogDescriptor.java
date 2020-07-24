package com.samsung.android.fotaprovider.log.base;

import com.samsung.android.fotaprovider.log.Log;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public abstract class LogDescriptor {
    public static final LogDescriptor NULL = new LogDescriptor() {
        public void println(String str) {
        }

        public void shift() {
        }
    };

    public void onBefore() {
    }

    public abstract void println(String str);

    public abstract void shift();

    protected LogDescriptor() {
    }

    public static abstract class Stream extends LogDescriptor {
        public abstract OutputStream getOutputStream() throws FileNotFoundException;

        /* access modifiers changed from: protected */
        public abstract long size();

        public boolean isLogExceeds(long j) {
            return size() > j;
        }

        public void println(String str) {
            BufferedWriter bufferedWriter;
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(getOutputStream(), Charset.defaultCharset()));
                try {
                    bufferedWriter.write(str);
                    try {
                        bufferedWriter.close();
                        return;
                    } catch (IOException e) {
                        e = e;
                    }
                    Log.printStackTrace(e);
                } catch (IOException e2) {
                    Log.printStackTrace(e2);
                    try {
                        bufferedWriter.close();
                    } catch (IOException e3) {
                        e = e3;
                    }
                }
            } catch (FileNotFoundException e4) {
                Log.printStackTrace(e4);
            } catch (RuntimeException e5) {
                Log.printStackTrace(e5);
            } catch (Throwable th) {
                try {
                    bufferedWriter.close();
                } catch (IOException e6) {
                    Log.printStackTrace(e6);
                }
                throw th;
            }
        }
    }

    public static class Limit extends LogDescriptor {
        private long limit;
        private Stream logSteramDescriptor;

        public Limit(Stream stream, long j) {
            this.logSteramDescriptor = stream;
            this.limit = j;
        }

        public void shift() {
            this.logSteramDescriptor.shift();
        }

        public void println(String str) {
            this.logSteramDescriptor.println(str);
        }

        public void onBefore() {
            if (this.logSteramDescriptor.isLogExceeds(this.limit)) {
                shift();
            }
        }
    }
}
