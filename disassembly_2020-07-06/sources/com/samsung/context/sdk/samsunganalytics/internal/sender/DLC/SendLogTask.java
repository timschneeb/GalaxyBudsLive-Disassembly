package com.samsung.context.sdk.samsunganalytics.internal.sender.DLC;

import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;

public class SendLogTask implements AsyncTaskClient {
    private DLCBinder binder;
    private AsyncTaskCallback callback;
    private Configuration configuration;
    private int result = -1;
    private SimpleLog simpleLog;

    public SendLogTask(DLCBinder dLCBinder, Configuration configuration2, SimpleLog simpleLog2, AsyncTaskCallback asyncTaskCallback) {
        this.binder = dLCBinder;
        this.configuration = configuration2;
        this.simpleLog = simpleLog2;
        this.callback = asyncTaskCallback;
    }

    public void run() {
        try {
            this.result = this.binder.getDlcService().requestSend(this.simpleLog.getType().getAbbrev(), this.configuration.getTrackingId().substring(0, 3), this.simpleLog.getTimestamp(), this.simpleLog.getId(), "0", "", "6.05.025", this.simpleLog.getData());
            Debug.LogENG("send to DLC : " + this.simpleLog.getData());
        } catch (Exception e) {
            Debug.LogException(getClass(), e);
        }
    }

    public int onFinish() {
        if (this.result == 0) {
            Debug.LogD("DLC Sender", "send result success : " + this.result);
            return 1;
        }
        Debug.LogD("DLC Sender", "send result fail : " + this.result);
        return -7;
    }
}
