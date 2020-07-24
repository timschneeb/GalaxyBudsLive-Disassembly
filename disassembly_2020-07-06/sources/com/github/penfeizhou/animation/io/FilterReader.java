package com.github.penfeizhou.animation.io;

import java.io.IOException;
import java.io.InputStream;

public class FilterReader implements Reader {
    protected Reader reader;

    public FilterReader(Reader reader2) {
        this.reader = reader2;
    }

    public long skip(long j) throws IOException {
        return this.reader.skip(j);
    }

    public byte peek() throws IOException {
        return this.reader.peek();
    }

    public void reset() throws IOException {
        this.reader.reset();
    }

    public int position() {
        return this.reader.position();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        return this.reader.read(bArr, i, i2);
    }

    public int available() throws IOException {
        return this.reader.available();
    }

    public void close() throws IOException {
        this.reader.close();
    }

    public InputStream toInputStream() throws IOException {
        reset();
        return this.reader.toInputStream();
    }
}
