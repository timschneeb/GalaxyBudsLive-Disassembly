package com.samsung.accessory.neobeanmgr.core.bixbyroutine;

import android.os.Bundle;
import com.samsung.accessory.neobeanmgr.R;

public class RoutineAmbientConfigActivity extends RoutinePresetOnOffConfigActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setRoutineTitle(R.string.settings_ambient_sound);
        super.onCreate(bundle);
    }
}
