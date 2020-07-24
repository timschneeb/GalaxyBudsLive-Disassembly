package com.samsung.accessory.neobeanmgr.module.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.samsung.accessory.neobeanmgr.common.util.Util;

public abstract class OrientationPolicyActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientationByPolicy(this);
    }

    public static void setRequestedOrientationByPolicy(Activity activity) {
        if (Util.isTablet()) {
            activity.setRequestedOrientation(2);
        } else {
            activity.setRequestedOrientation(1);
        }
    }
}
