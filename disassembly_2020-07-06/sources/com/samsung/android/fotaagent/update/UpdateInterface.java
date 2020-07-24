package com.samsung.android.fotaagent.update;

import com.samsung.android.fotaprovider.util.type.DeviceType;

public interface UpdateInterface {
    public static final String EXTRA_PUSH_MSG = "msg";
    public static final long HOLDING_AFTER_BT_CONNECTED = 3000;
    public static final long HOLDING_DO_POLLING = 1000;
    public static final long HOLDING_RECEIVE_UPDATE_RESULT = DeviceType.get().getWaitingMillisForUpdateResult();
    public static final String PUSH_TYPE_DM = "DM";
    public static final String UPDATE_STATE = "update_state";
}
