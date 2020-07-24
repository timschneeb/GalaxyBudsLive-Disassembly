package com.samsung.android.sdk.mobileservice.common.result;

public class IntResult implements Result {
    private int mResult;
    private CommonResultStatus mStatus;

    public IntResult(CommonResultStatus commonResultStatus, int i) {
        this.mStatus = commonResultStatus;
        this.mResult = i;
    }

    public CommonResultStatus getStatus() {
        return this.mStatus;
    }

    public int getResult() {
        return this.mResult;
    }
}
