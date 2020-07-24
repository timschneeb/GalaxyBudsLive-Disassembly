package com.samsung.context.sdk.samsunganalytics.internal.sender.DLC;

import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.sender.BaseLogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.util.Map;
import java.util.Queue;

public class DLCLogSender extends BaseLogSender {
    private DLCBinder binder;

    public DLCLogSender(Context context, Configuration configuration) {
        super(context, configuration);
        this.binder = new DLCBinder(context, new Callback<Void, Void>() {
            public Void onResult(Void voidR) {
                DLCLogSender.this.sendAll();
                return null;
            }
        });
        this.binder.sendRegisterRequestToDLC();
    }

    /* access modifiers changed from: private */
    public void sendAll() {
        Queue<SimpleLog> queue = this.manager.get();
        while (!queue.isEmpty()) {
            this.executor.execute(new SendLogTask(this.binder, this.configuration, queue.poll(), (AsyncTaskCallback) null));
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, String> setCommonParamToLog(Map<String, String> map) {
        Map<String, String> commonParamToLog = super.setCommonParamToLog(map);
        commonParamToLog.remove("do");
        commonParamToLog.remove("dm");
        commonParamToLog.remove("v");
        return commonParamToLog;
    }

    public int send(Map<String, String> map) {
        insert(map);
        if (!this.binder.isBindToDLC()) {
            this.binder.sendRegisterRequestToDLC();
            return 0;
        } else if (this.binder.getDlcService() == null) {
            return 0;
        } else {
            sendAll();
            return 0;
        }
    }

    public int sendSync(Map<String, String> map) {
        Debug.LogD("DLCLogSender", "not support sync api");
        send(map);
        return -100;
    }
}
