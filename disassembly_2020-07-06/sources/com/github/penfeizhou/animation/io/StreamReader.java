package com.github.penfeizhou.animation.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamReader extends FilterInputStream implements Reader {
    private int position;

    public InputStream toInputStream() throws IOException {
        return this;
    }

    public StreamReader(InputStream inputStream) {
        super(inputStream);
        try {
            inputStream.reset();
        } catch (IOException unused) {
        }
    }

    public byte peek() throws IOException {
        byte read = (byte) read();
        this.position++;
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = super.read(bArr, i, i2);
        this.position += Math.max(0, read);
        return read;
    }

    public synchronized void reset() throws IOException {
        super.reset();
        this.position = 0;
    }

    public long skip(long j) throws IOException {
        long skip = super.skip(j);
        this.position = (int) (((long) this.position) + skip);
        return skip;
    }

    public int position() {
        return this.position;
    }
}
