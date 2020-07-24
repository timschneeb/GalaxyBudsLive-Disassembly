package com.samsung.accessory.neobeanmgr.module.setupwizard;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;

public class PermissionsActivity extends PermissionCheckActivity {
    private static final String TAG = "NeoBean_PermissionsActivity";
    private StringBuffer mDescription;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_permissions);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (Build.VERSION.SDK_INT < 23) {
            findViewById(R.id.text_guide_for_legacy).setVisibility(0);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
