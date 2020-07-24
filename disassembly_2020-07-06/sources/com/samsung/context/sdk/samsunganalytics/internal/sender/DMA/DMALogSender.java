package com.samsung.context.sdk.samsunganalytics.internal.sender.DMA;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.policy.PolicyUtils;
import com.samsung.context.sdk.samsunganalytics.internal.sender.BaseLogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class DMALogSender extends BaseLogSender {
    private static final int TYPE_COMMON = 1;
    private static final int TYPE_LOG = 2;
    private DMABinder dmaBinder;
    private int dmaStatus = 0;
    private boolean isReset = false;

    public DMALogSender(Context context, Configuration configuration) {
        super(context, configuration);
        if (PolicyUtils.getSenderType() == 2) {
            this.dmaBinder = new DMABinder(context, new Callback<Void, String>() {
                public Void onResult(String str) {
                    DMALogSender.this.sendCommon();
                    DMALogSender.this.sendAll();
                    return null;
                }
            });
            this.dmaBinder.bind();
        }
    }

    public int send(Map<String, String> map) {
        if (PolicyUtils.getSenderType() == 3) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("tcType", Integer.valueOf(this.configuration.isEnableUseInAppLogging() ? 1 : 0));
            contentValues.put("tid", this.configuration.getTrackingId());
            contentValues.put("logType", getLogType(map).getAbbrev());
            contentValues.put("timeStamp", Long.valueOf(map.get("ts")));
            contentValues.put("body", makeBodyString(setCommonParamToLog(map)));
            this.executor.execute(new SendLogTaskV2(this.context, 2, contentValues));
            return 0;
        } else if (this.dmaBinder.isTokenfail()) {
            return -8;
        } else {
            int i = this.dmaStatus;
            if (i != 0) {
                return i;
            }
            insert(map);
            if (!this.dmaBinder.isBind()) {
                this.dmaBinder.bind();
            } else if (this.dmaBinder.getDmaInterface() != null) {
                sendAll();
                if (this.isReset) {
                    sendCommon();
                    this.isReset = false;
                }
            }
            return this.dmaStatus;
        }
    }

    public int sendSync(Map<String, String> map) {
        return send(map);
    }

    /* access modifiers changed from: private */
    public void sendAll() {
        if (PolicyUtils.getSenderType() == 2 && this.dmaStatus == 0) {
            Queue<SimpleLog> queue = this.manager.get();
            while (!queue.isEmpty()) {
                this.executor.execute(new SendLogTask(this.dmaBinder.getDmaInterface(), this.configuration, queue.poll()));
            }
        }
    }

    public void sendCommon() {
        boolean isEnableUseInAppLogging = this.configuration.isEnableUseInAppLogging();
        String trackingId = this.configuration.getTrackingId();
        Delimiter delimiter = new Delimiter();
        HashMap hashMap = new HashMap();
        hashMap.put("av", this.deviceInfo.getAppVersionName());
        hashMap.put("uv", this.configuration.getVersion());
        hashMap.put("v", "6.05.025");
        String makeDelimiterString = delimiter.makeDelimiterString(hashMap, Delimiter.Depth.ONE_DEPTH);
        String str = null;
        HashMap hashMap2 = new HashMap();
        if (!TextUtils.isEmpty(this.configuration.getDeviceId())) {
            hashMap2.put("auid", this.configuration.getDeviceId());
            hashMap2.put("at", String.valueOf(this.configuration.getAuidType()));
            str = delimiter.makeDelimiterString(hashMap2, Delimiter.Depth.ONE_DEPTH);
        }
        if (PolicyUtils.getSenderType() == 3) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("tcType", Integer.valueOf(isEnableUseInAppLogging));
            contentValues.put("tid", trackingId);
            contentValues.put("data", makeDelimiterString);
            contentValues.put("did", str);
            this.executor.execute(new SendLogTaskV2(this.context, 1, contentValues));
            return;
        }
        try {
            this.dmaStatus = this.dmaBinder.getDmaInterface().sendCommon(isEnableUseInAppLogging ? 1 : 0, trackingId, makeDelimiterString, str);
        } catch (Exception e) {
            Debug.LogException(e.getClass(), e);
            this.dmaStatus = -9;
        }
    }

    public void reset() {
        this.isReset = true;
    }
}
