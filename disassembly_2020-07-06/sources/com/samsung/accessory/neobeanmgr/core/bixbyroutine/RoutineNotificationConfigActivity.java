package com.samsung.accessory.neobeanmgr.core.bixbyroutine;

import android.os.Bundle;
import com.samsung.accessory.neobeanmgr.R;

public class RoutineNotificationConfigActivity extends RoutinePresetOnOffConfigActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setRoutineTitle(R.string.read_notifications_aloud);
        super.onCreate(bundle);
    }
}
