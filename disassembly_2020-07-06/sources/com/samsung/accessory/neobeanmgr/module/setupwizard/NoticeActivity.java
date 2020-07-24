package com.samsung.accessory.neobeanmgr.module.setupwizard;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;

abstract class NoticeActivity extends PermissionCheckActivity {
    private static final String TAG = "NeoBean_NoticeActivity";

    /* access modifiers changed from: protected */
    public abstract String getNoticeDescription();

    /* access modifiers changed from: protected */
    public abstract String getNoticeTile();

    /* access modifiers changed from: protected */
    public abstract String getTAG();

    NoticeActivity() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.d(getTAG(), "onCreate()");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_notice);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((CharSequence) getNoticeTile());
        ((TextView) findViewById(R.id.text_description)).setText(getNoticeDescription());
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
