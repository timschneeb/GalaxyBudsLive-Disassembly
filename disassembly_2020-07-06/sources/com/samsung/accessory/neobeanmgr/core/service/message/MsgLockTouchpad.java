package com.samsung.accessory.neobeanmgr.core.service.message;

import android.content.Intent;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;

public class MsgLockTouchpad extends Msg {
    private boolean lockTouchpad;

    public MsgLockTouchpad(boolean z) {
        super((byte) MsgID.LOCK_TOUCHPAD);
        this.lockTouchpad = z;
        SamsungAnalyticsUtil.setStatusString(SA.Status.LOCK_TOUCHPAD, z ? "1" : "0");
        Util.sendPermissionBroadcast(Application.getContext(), new Intent(CoreService.ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED));
    }

    public byte[] getData() {
        return new byte[]{this.lockTouchpad};
    }
}
