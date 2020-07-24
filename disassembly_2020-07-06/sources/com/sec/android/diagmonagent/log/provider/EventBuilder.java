package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.sec.android.diagmonagent.log.provider.utils.DiagMonUtil;
import com.sec.android.diagmonagent.log.provider.utils.Validator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class EventBuilder {
    private Context mContext;
    private String mDescription = "";
    private String mErrorCode = "";
    private JSONObject mExtData;
    public boolean mIsCalledNetworkMode;
    private String mLogPath = "";
    private boolean mNetworkMode;
    private String mRelayClientType = "";
    private String mRelayClientVer = "";
    private String mServiceDefinedKey = "";
    private String mZipFile = "";
    private oldEventBuilder oldIb;

    public EventBuilder(Context context) {
        this.mContext = context;
        this.mNetworkMode = true;
        this.mIsCalledNetworkMode = false;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldIb = new oldEventBuilder(context);
        }
    }

    public JSONObject getMemory() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("VM", getVmMemory());
            jSONObject.put("NATIVE", getNativeMemory());
            Log.d(DiagMonUtil.TAG, jSONObject.toString());
        } catch (JSONException unused) {
        }
        return jSONObject;
    }

    private JSONObject getVmMemory() throws JSONException {
        Runtime runtime = Runtime.getRuntime();
        long j = runtime.totalMemory() >> 20;
        long freeMemory = runtime.freeMemory() >> 20;
        long maxMemory = runtime.maxMemory() >> 20;
        String str = DiagMonUtil.TAG;
        Log.d(str, "[VM] TotalMemory : " + j + " FreeMemory : " + freeMemory + " maxMemory : " + maxMemory);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("TOTAL", j);
            jSONObject.put("FREE", freeMemory);
            jSONObject.put("MAX", maxMemory);
        } catch (JSONException e) {
            Log.w(DiagMonUtil.TAG, e.getMessage());
        }
        return jSONObject;
    }

    private JSONObject getNativeMemory() {
        long nativeHeapFreeSize = Debug.getNativeHeapFreeSize() >> 20;
        long nativeHeapSize = Debug.getNativeHeapSize() >> 20;
        long nativeHeapAllocatedSize = Debug.getNativeHeapAllocatedSize() >> 20;
        String str = DiagMonUtil.TAG;
        Log.d(str, "[NativeHeap] nativeHeapSize : " + nativeHeapSize + " nativeHeapFree : " + nativeHeapFreeSize + " nativeHeapAllocatedSize : " + nativeHeapAllocatedSize);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("HEAP_SIZE", nativeHeapSize);
            jSONObject.put("HEAP_FREE", nativeHeapFreeSize);
            jSONObject.put("HEAD_ALLOC", nativeHeapAllocatedSize);
        } catch (JSONException e) {
            Log.w(DiagMonUtil.TAG, e.getMessage());
        }
        return jSONObject;
    }

    public JSONObject getInternalStorageSize() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("TOTAL", getTotalInternalStorageSize() >> 20);
            jSONObject.put("FREE", getAvailableInternalStorageSize() >> 20);
        } catch (JSONException e) {
            Log.w(DiagMonUtil.TAG, e.getMessage());
        }
        return jSONObject;
    }

    private static long getTotalInternalStorageSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return statFs.getBlockCountLong() * statFs.getBlockSizeLong();
    }

    public static long getAvailableInternalStorageSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
    }

    public String getZipPath() {
        return this.mZipFile;
    }

    public void setZipFilePath(String str) {
        this.mZipFile = str;
    }

    public oldEventBuilder getOldEventBuilder() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldIb;
        }
        return null;
    }

    public String getLogPath() {
        return this.mLogPath;
    }

    public EventBuilder setLogPath(String str) {
        try {
            if (isConfigured()) {
                return this;
            }
            this.mLogPath = str;
            if (DiagMonUtil.checkDMA(this.mContext) == 1 && this.mLogPath != null) {
                if (!Validator.isValidLogPath(this.mLogPath)) {
                    DiagMonSDK.getConfiguration().getOldConfig().setLogList(makeLogList(this.mLogPath));
                    DiagMonSDK.getElp().setConfiguration(DiagMonSDK.getConfiguration());
                }
            }
            return this;
        } catch (Exception | NullPointerException unused) {
        }
    }

    public EventBuilder setServiceDefinedKey(String str) {
        if (isConfigured()) {
            return this;
        }
        this.mServiceDefinedKey = str;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldIb.setEventId(str);
        }
        return this;
    }

    public String getServiceDefinedKey() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldIb.getEventId();
        }
        return this.mServiceDefinedKey;
    }

    public EventBuilder setErrorCode(String str) {
        if (isConfigured()) {
            return this;
        }
        this.mErrorCode = str;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldIb.setResultCode(str);
        }
        return this;
    }

    public String getErrorCode() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldIb.getResultCode();
        }
        return this.mErrorCode;
    }

    public EventBuilder setNetworkMode(boolean z) {
        if (isConfigured()) {
            return this;
        }
        this.mIsCalledNetworkMode = true;
        this.mNetworkMode = z;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldIb.setWifiOnly(z);
        }
        return this;
    }

    public boolean getNetworkMode() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldIb.getWifiOnly();
        }
        return this.mNetworkMode;
    }

    public EventBuilder setDescription(String str) {
        if (isConfigured()) {
            return this;
        }
        this.mDescription = str;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldIb.setDescription(str);
        }
        return this;
    }

    public String getDescription() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldIb.getDescription();
        }
        return this.mDescription;
    }

    public EventBuilder setRelayClientVer(String str) {
        if (isConfigured()) {
            return this;
        }
        this.mRelayClientVer = str;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldIb.setRelayClientVer(str);
        }
        return this;
    }

    public String getRelayClientVer() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldIb.getRelayClientVer();
        }
        return this.mRelayClientVer;
    }

    public EventBuilder setRelayClientType(String str) {
        if (isConfigured()) {
            return this;
        }
        this.mRelayClientType = str;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldIb.setRelayClient(str);
        }
        return this;
    }

    public String getRelayClientType() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldIb.getRelayClient();
        }
        return this.mRelayClientType;
    }

    public EventBuilder setExtData(JSONObject jSONObject) {
        if (isConfigured()) {
            return this;
        }
        this.mExtData = jSONObject;
        return this;
    }

    public String getExtData() {
        JSONObject jSONObject = this.mExtData;
        if (jSONObject == null) {
            return "";
        }
        return jSONObject.toString();
    }

    public List<String> makeLogList(String str) {
        ArrayList arrayList = new ArrayList();
        for (File file : new File(str).listFiles()) {
            arrayList.add(file.getPath());
            Log.d(DiagMonUtil.TAG, "found file : " + file.getPath());
        }
        return arrayList;
    }

    class oldEventBuilder {
        private Context mContext;
        private String mDescription = "";
        private String mEventId = "";
        private String mRelayClient = "";
        private String mRelayVer = "";
        private String mResultCode = "";
        private boolean mUiMode = true;
        private boolean mWifiOnly = true;

        public oldEventBuilder(Context context) {
            this.mContext = context;
        }

        public void setResultCode(String str) {
            this.mResultCode = str;
        }

        public String getResultCode() {
            return this.mResultCode;
        }

        public void setUiMode(boolean z) {
            this.mUiMode = z;
        }

        public boolean getUiMode() {
            return this.mUiMode;
        }

        public void setWifiOnly(boolean z) {
            this.mWifiOnly = z;
        }

        public boolean getWifiOnly() {
            return this.mWifiOnly;
        }

        public void setEventId(String str) {
            if (str.contains("/")) {
                String str2 = DiagMonUtil.TAG;
                Log.w(str2, "delimiter is included : " + str);
                return;
            }
            this.mEventId = str;
        }

        public String getEventId() {
            return this.mEventId;
        }

        public void setDescription(String str) {
            this.mDescription = str;
        }

        public String getDescription() {
            return this.mDescription;
        }

        public void setRelayClient(String str) {
            this.mRelayClient = str;
        }

        public String getRelayClient() {
            return this.mRelayClient;
        }

        public void setRelayClientVer(String str) {
            this.mRelayVer = str;
        }

        public String getRelayClientVer() {
            return this.mRelayVer;
        }
    }

    private boolean isConfigured() {
        return DiagMonSDK.getConfiguration() == null;
    }
}
