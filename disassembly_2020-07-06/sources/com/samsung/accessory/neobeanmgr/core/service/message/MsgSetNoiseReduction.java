package com.samsung.accessory.neobeanmgr.core.service.message;

import android.content.Intent;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;

public class MsgSetNoiseReduction extends Msg {
    private boolean noiseReduction;

    public MsgSetNoiseReduction(boolean z) {
        super((byte) MsgID.SET_NOISE_REDUCTION);
        this.noiseReduction = z;
        SamsungAnalyticsUtil.setStatusString(SA.Status.ANC, z ? "1" : "0");
        Util.sendPermissionBroadcast(Application.getContext(), new Intent(CoreService.ACTION_MSG_ID_NOISE_REDUCTION_UPDATED));
    }

    public byte[] getData() {
        return new byte[]{this.noiseReduction};
    }
}
