package com.samsung.accessory.neobeanmgr.module.setupwizard;

import com.samsung.accessory.neobeanmgr.R;

public class NoticePrivacyPolicyActivity extends NoticeActivity {
    private static final String TAG = "NeoBean_NoticePrivacyPolicyActivity";

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    public /* bridge */ /* synthetic */ boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    /* access modifiers changed from: protected */
    public String getNoticeTile() {
        return getString(R.string.privacy_policy);
    }

    /* access modifiers changed from: protected */
    public String getNoticeDescription() {
        return PrivacyNotice.getString();
    }
}
