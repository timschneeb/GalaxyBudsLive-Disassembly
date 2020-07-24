package com.samsung.android.fotaagent;

import com.samsung.android.fotaprovider.FotaProviderInitializer;

public enum FotaServiceJobId {
    INSTANCE;
    
    public int DM_SERVICE_JOB_ID;
    public int REGISTER_JOB_ID;
    public int UPDATE_JOB_ID;

    private int getPackageNameHashCode() {
        return FotaProviderInitializer.getContext().getPackageName().hashCode();
    }
}
