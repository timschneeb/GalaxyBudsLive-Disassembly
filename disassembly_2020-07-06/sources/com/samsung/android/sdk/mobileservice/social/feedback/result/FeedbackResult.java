package com.samsung.android.sdk.mobileservice.social.feedback.result;

import com.samsung.android.sdk.mobileservice.common.result.BaseResult;
import com.samsung.android.sdk.mobileservice.common.result.CommonResultStatus;

public class FeedbackResult<T> implements BaseResult<T> {
    private T mResult;
    private CommonResultStatus mStatus;

    public FeedbackResult(CommonResultStatus commonResultStatus, T t) {
        this.mStatus = commonResultStatus;
        this.mResult = t;
    }

    public CommonResultStatus getStatus() {
        return this.mStatus;
    }

    public T getResult() {
        return this.mResult;
    }
}
