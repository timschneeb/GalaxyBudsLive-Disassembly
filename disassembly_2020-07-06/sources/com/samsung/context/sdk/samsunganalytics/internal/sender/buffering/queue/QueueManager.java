package com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.queue;

import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueManager {
    private static final int DEFAULT_QUEUE_SIZE = 25;
    private static final int MAXIMUM_QUEUE_SIZE = 100;
    protected LinkedBlockingQueue<SimpleLog> logQueue;

    public QueueManager() {
        this.logQueue = new LinkedBlockingQueue<>(25);
    }

    public QueueManager(int i) {
        if (i < 25) {
            this.logQueue = new LinkedBlockingQueue<>(25);
        } else if (i > 100) {
            this.logQueue = new LinkedBlockingQueue<>(100);
        } else {
            this.logQueue = new LinkedBlockingQueue<>(i);
        }
    }

    public void insert(SimpleLog simpleLog) {
        if (!this.logQueue.offer(simpleLog)) {
            Debug.LogD("QueueManager", "queue size over. remove oldest log");
            this.logQueue.poll();
            this.logQueue.offer(simpleLog);
        }
    }

    public Queue<SimpleLog> getAll() {
        return this.logQueue;
    }

    public long getDataSize() {
        return (long) this.logQueue.size();
    }

    public boolean isEmpty() {
        return this.logQueue.isEmpty();
    }
}
