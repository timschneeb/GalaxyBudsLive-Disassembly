package com.github.penfeizhou.animation.executor;

import android.os.HandlerThread;
import android.os.Looper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class FrameDecoderExecutor {
    private static int sPoolNumber = 4;
    private AtomicInteger counter;
    private ArrayList<Looper> mLooperGroup;

    private FrameDecoderExecutor() {
        this.mLooperGroup = new ArrayList<>();
        this.counter = new AtomicInteger(0);
    }

    static class Inner {
        static final FrameDecoderExecutor sInstance = new FrameDecoderExecutor();

        Inner() {
        }
    }

    public void setPoolSize(int i) {
        sPoolNumber = i;
    }

    public static FrameDecoderExecutor getInstance() {
        return Inner.sInstance;
    }

    public Looper getLooper(int i) {
        int i2 = i % sPoolNumber;
        if (i2 < this.mLooperGroup.size()) {
            return this.mLooperGroup.get(i2);
        }
        HandlerThread handlerThread = new HandlerThread("FrameDecoderExecutor-" + i2);
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        if (looper != null) {
            this.mLooperGroup.add(looper);
        } else {
            Iterator<Looper> it = this.mLooperGroup.iterator();
            while (it.hasNext()) {
                Looper next = it.next();
                if (next != null) {
                    return next;
                }
            }
        }
        return looper;
    }

    public int generateTaskId() {
        return this.counter.getAndIncrement();
    }
}
