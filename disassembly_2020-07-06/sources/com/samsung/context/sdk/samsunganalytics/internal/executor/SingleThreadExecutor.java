package com.samsung.context.sdk.samsunganalytics.internal.executor;

import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class SingleThreadExecutor implements Executor {
    private static ExecutorService executorService;
    private static SingleThreadExecutor singleThreadExecutor;

    public SingleThreadExecutor() {
        executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setPriority(1);
                thread.setDaemon(true);
                Debug.LogD("newThread on Executor");
                return thread;
            }
        });
    }

    public void execute(final AsyncTaskClient asyncTaskClient) {
        executorService.submit(new Runnable() {
            public void run() {
                asyncTaskClient.run();
                asyncTaskClient.onFinish();
            }
        });
    }

    public static Executor getInstance() {
        if (singleThreadExecutor == null) {
            singleThreadExecutor = new SingleThreadExecutor();
        }
        return singleThreadExecutor;
    }
}
