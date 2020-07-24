package com.samsung.context.sdk.samsunganalytics.internal.connection;

import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;

public enum Domain {
    ;
    
    String domain;

    static {
        String str = "https://stg-api.di.atlas.samsung.com";
        REGISTRATION = new Domain("REGISTRATION", 0, Utils.isEngBin() ? str : "https://regi.di.atlas.samsung.com");
        if (!Utils.isEngBin()) {
            str = "https://dc.di.atlas.samsung.com";
        }
        POLICY = new Domain("POLICY", 1, str);
        DLS = new Domain("DLS", 2, "");
        $VALUES = new Domain[]{REGISTRATION, POLICY, DLS};
    }

    private Domain(String str) {
        this.domain = str;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String str) {
        this.domain = str;
    }
}
