package com.samsung.accessory.neobeanmgr.module.setupwizard;

import com.samsung.accessory.neobeanmgr.R;

public class NoticeEULAActivity extends NoticeActivity {
    private static final String TAG = "NeoBean_NoticeEULAActivity";

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    public /* bridge */ /* synthetic */ boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    /* access modifiers changed from: protected */
    public String getNoticeTile() {
        return getString(R.string.end_user_license_agreement);
    }

    /* access modifiers changed from: protected */
    public String getNoticeDescription() {
        return AssetString.getStringEULA();
    }
}
