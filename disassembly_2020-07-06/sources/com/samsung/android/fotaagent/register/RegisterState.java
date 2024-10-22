package com.samsung.android.fotaagent.register;

public enum RegisterState {
    CHECK_NEXT_STATE,
    CONNECTING_CONSUMER,
    CONSUMER_CONNECTION_FAILED,
    REGISTERING_DEVICE,
    REGISTERING_DEVICE_WITH_DELAY,
    DEVICE_REGISTRATION_FAILED,
    DEVICE_REGISTRATION_SUCCESS,
    REGISTERING_POLLING,
    REGISTERING_PUSH,
    REGISTERING_PUSH_ID,
    PUSH_REGISTRATION_FAILED,
    REGISTRATION_COMPLETE
}
