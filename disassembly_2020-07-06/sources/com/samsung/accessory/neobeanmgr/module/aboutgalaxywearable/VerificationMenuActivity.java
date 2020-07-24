package com.samsung.accessory.neobeanmgr.module.aboutgalaxywearable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;

public class VerificationMenuActivity extends ConnectionActivity {
    private static final String TAG = (Application.TAG_ + VerificationMenuActivity.class.getSimpleName());

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_verification_menu);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((CharSequence) "Verification");
        findViewById(R.id.layout_debug).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VerificationMenuActivity verificationMenuActivity = VerificationMenuActivity.this;
                verificationMenuActivity.startActivity(new Intent(verificationMenuActivity, VerificationDeviceInfoActivity.class));
            }
        });
        findViewById(R.id.layout_dumplog).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VerificationMenuActivity verificationMenuActivity = VerificationMenuActivity.this;
                verificationMenuActivity.startActivity(new Intent(verificationMenuActivity, VerificationDumpLogActivity.class));
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
