package com.github.penfeizhou.animation.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferWriter implements Writer {
    protected ByteBuffer byteBuffer;

    public void close() {
    }

    public ByteBufferWriter() {
        reset(10240);
    }

    public void putByte(byte b) {
        this.byteBuffer.put(b);
    }

    public void putBytes(byte[] bArr) {
        this.byteBuffer.put(bArr);
    }

    public int position() {
        return this.byteBuffer.position();
    }

    public void skip(int i) {
        this.byteBuffer.position(i + position());
    }

    public byte[] toByteArray() {
        return this.byteBuffer.array();
    }

    public void reset(int i) {
        ByteBuffer byteBuffer2 = this.byteBuffer;
        if (byteBuffer2 == null || i > byteBuffer2.capacity()) {
            this.byteBuffer = ByteBuffer.allocate(i);
            this.byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        this.byteBuffer.clear();
    }
}
