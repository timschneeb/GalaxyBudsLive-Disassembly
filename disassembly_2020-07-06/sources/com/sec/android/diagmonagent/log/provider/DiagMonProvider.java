package com.sec.android.diagmonagent.log.provider;

import android.util.Log;
import com.sec.android.diagmonagent.log.provider.utils.DiagMonUtil;
import java.util.ArrayList;
import java.util.List;

public class DiagMonProvider extends newAbstractMasterLogProvider {
    public static String AUTHORITY = "";

    /* access modifiers changed from: protected */
    public String setDeviceId() {
        return "";
    }

    /* access modifiers changed from: protected */
    public String setServiceName() {
        return "";
    }

    public boolean onCreate() {
        return super.onCreate();
    }

    public void setConfiguration(DiagMonConfig diagMonConfig) {
        AUTHORITY = "com.sec.android.log." + diagMonConfig.getServiceId();
        super.setConfiguration(diagMonConfig);
        Log.d(DiagMonUtil.TAG, "LogProvider is set");
    }

    /* access modifiers changed from: protected */
    public List<String> setAuthorityList() {
        return new ArrayList();
    }

    /* access modifiers changed from: protected */
    public List<String> setLogList() {
        return new ArrayList();
    }

    /* access modifiers changed from: protected */
    public List<String> setPlainLogList() {
        return new ArrayList();
    }

    /* access modifiers changed from: protected */
    public String getAuthority() {
        return AUTHORITY;
    }
}
