package com.github.penfeizhou.animation.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferReader implements Reader {
    private final ByteBuffer byteBuffer;

    public void close() throws IOException {
    }

    public ByteBufferReader(ByteBuffer byteBuffer2) {
        this.byteBuffer = byteBuffer2;
        byteBuffer2.position(0);
    }

    public long skip(long j) throws IOException {
        ByteBuffer byteBuffer2 = this.byteBuffer;
        byteBuffer2.position((int) (((long) byteBuffer2.position()) + j));
        return j;
    }

    public byte peek() throws IOException {
        return this.byteBuffer.get();
    }

    public void reset() throws IOException {
        this.byteBuffer.position(0);
    }

    public int position() {
        return this.byteBuffer.position();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        this.byteBuffer.get(bArr, i, i2);
        return i2;
    }

    public int available() throws IOException {
        return this.byteBuffer.limit() - this.byteBuffer.position();
    }

    public InputStream toInputStream() throws IOException {
        return new ByteArrayInputStream(this.byteBuffer.array());
    }
}
