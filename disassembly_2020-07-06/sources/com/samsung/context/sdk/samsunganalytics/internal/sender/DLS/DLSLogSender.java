package com.samsung.context.sdk.samsunganalytics.internal.sender.DLS;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Constants;
import com.samsung.context.sdk.samsunganalytics.internal.policy.GetPolicyClient;
import com.samsung.context.sdk.samsunganalytics.internal.policy.PolicyUtils;
import com.samsung.context.sdk.samsunganalytics.internal.sender.BaseLogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.LogType;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DLSLogSender extends BaseLogSender {
    public static final int DB_SELECT_LIMIT = 200;

    public DLSLogSender(Context context, Configuration configuration) {
        super(context, configuration);
    }

    private int getNetworkType() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return -4;
        }
        return activeNetworkInfo.getType();
    }

    private int checkAvailableLogging(int i) {
        if (i == -4) {
            Debug.LogD("DLS Sender", "Network unavailable.");
            return -4;
        } else if (PolicyUtils.isPolicyExpired(this.context)) {
            Debug.LogD("DLS Sender", "policy expired. request policy");
            return -6;
        } else if (this.configuration.getRestrictedNetworkType() != i) {
            return 0;
        } else {
            Debug.LogD("DLS Sender", "Network unavailable by restrict option:" + i);
            return -4;
        }
    }

    private void sendSum(int i, LogType logType, Queue<SimpleLog> queue, int i2, AsyncTaskCallback asyncTaskCallback) {
        PolicyUtils.useQuota(this.context, i, i2);
        this.executor.execute(new DLSAPIClient(logType, queue, this.configuration.getTrackingId(), this.configuration.getNetworkTimeoutInMilliSeconds(), asyncTaskCallback));
    }

    private int flushBufferedLogs(int i, LogType logType, Queue<SimpleLog> queue, AsyncTaskCallback asyncTaskCallback) {
        ArrayList arrayList = new ArrayList();
        Iterator it = queue.iterator();
        while (true) {
            int i2 = 0;
            if (!it.hasNext()) {
                return 0;
            }
            LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
            int remainingQuota = PolicyUtils.getRemainingQuota(this.context, i);
            if (51200 <= remainingQuota) {
                remainingQuota = Constants.MAXIMUM_LOG_LENGTH;
            }
            while (it.hasNext()) {
                SimpleLog simpleLog = (SimpleLog) it.next();
                if (simpleLog.getType() == logType) {
                    if (simpleLog.getData().getBytes().length + i2 > remainingQuota) {
                        break;
                    }
                    i2 += simpleLog.getData().getBytes().length;
                    linkedBlockingQueue.add(simpleLog);
                    it.remove();
                    arrayList.add(simpleLog.getId());
                    if (queue.isEmpty()) {
                        this.manager.remove((List<String>) arrayList);
                        queue = this.manager.get(200);
                        it = queue.iterator();
                    }
                }
            }
            if (linkedBlockingQueue.isEmpty()) {
                return -1;
            }
            this.manager.remove((List<String>) arrayList);
            sendSum(i, logType, linkedBlockingQueue, i2, asyncTaskCallback);
            Debug.LogD("DLSLogSender", "send packet : num(" + linkedBlockingQueue.size() + ") size(" + i2 + ")");
        }
    }

    private int sendOne(int i, SimpleLog simpleLog, AsyncTaskCallback asyncTaskCallback, boolean z) {
        if (simpleLog == null) {
            return -100;
        }
        int length = simpleLog.getData().getBytes().length;
        int hasQuota = PolicyUtils.hasQuota(this.context, i, length);
        if (hasQuota != 0) {
            return hasQuota;
        }
        PolicyUtils.useQuota(this.context, i, length);
        DLSAPIClient dLSAPIClient = new DLSAPIClient(simpleLog, this.configuration.getTrackingId(), this.configuration.getNetworkTimeoutInMilliSeconds(), asyncTaskCallback);
        if (z) {
            Debug.LogENG("sync send");
            dLSAPIClient.run();
            return dLSAPIClient.onFinish();
        }
        this.executor.execute(dLSAPIClient);
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x006b A[LOOP:0: B:12:0x006b->B:15:0x007b, LOOP_START, PHI: r2 
      PHI: (r2v3 int) = (r2v1 int), (r2v6 int) binds: [B:10:0x005e, B:15:0x007b] A[DONT_GENERATE, DONT_INLINE]] */
    public int send(Map<String, String> map) {
        final int networkType = getNetworkType();
        int checkAvailableLogging = checkAvailableLogging(networkType);
        if (checkAvailableLogging != 0) {
            insert(map);
            if (checkAvailableLogging == -6) {
                PolicyUtils.getPolicy(this.context, this.configuration, this.executor, this.deviceInfo);
                this.manager.delete();
            }
            return checkAvailableLogging;
        }
        AnonymousClass1 r1 = new AsyncTaskCallback() {
            public void onSuccess(int i, String str, String str2, String str3) {
            }

            public void onFail(int i, String str, String str2, String str3) {
                DLSLogSender.this.manager.insert(Long.valueOf(str).longValue(), str2, str3.equals(LogType.DEVICE.getAbbrev()) ? LogType.DEVICE : LogType.UIX);
                PolicyUtils.useQuota(DLSLogSender.this.context, networkType, str2.getBytes().length * -1);
            }
        };
        int sendOne = sendOne(networkType, new SimpleLog(Long.valueOf(map.get("ts")).longValue(), makeBodyString(setCommonParamToLog(map)), getLogType(map)), r1, false);
        if (sendOne == -1) {
            return sendOne;
        }
        Queue<SimpleLog> queue = this.manager.get(200);
        if (this.manager.isEnabledDatabaseBuffering()) {
            flushBufferedLogs(networkType, LogType.UIX, queue, r1);
            flushBufferedLogs(networkType, LogType.DEVICE, queue, r1);
        } else {
            while (!queue.isEmpty() && (sendOne = sendOne(networkType, queue.poll(), r1, false)) != -1) {
            }
        }
        return sendOne;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0033, code lost:
        if (r1 != 0) goto L_0x0035;
     */
    public int sendSync(Map<String, String> map) {
        int networkType = getNetworkType();
        int checkAvailableLogging = checkAvailableLogging(networkType);
        if (checkAvailableLogging != 0) {
            if (checkAvailableLogging == -6) {
                GetPolicyClient makeGetPolicyClient = PolicyUtils.makeGetPolicyClient(this.context, this.configuration, this.deviceInfo, (Callback) null);
                makeGetPolicyClient.run();
                checkAvailableLogging = makeGetPolicyClient.onFinish();
                Debug.LogENG("get policy sync " + checkAvailableLogging);
            }
            return checkAvailableLogging;
        }
        return sendOne(networkType, new SimpleLog(Long.valueOf(map.get("ts")).longValue(), makeBodyString(setCommonParamToLog(map)), getLogType(map)), (AsyncTaskCallback) null, true);
    }
}
