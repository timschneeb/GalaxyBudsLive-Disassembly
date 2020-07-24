package com.accessorydm.ui.checkingforupdate;

import android.os.Bundle;
import android.view.ViewStub;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenActivity;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenContract;
import com.samsung.android.fotaprovider.FotaProviderInitializer;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.type.DeviceType;
import com.sec.android.fotaprovider.R;

public class XUICheckingForUpdateActivity extends XUIBaseFullscreenActivity {
    /* access modifiers changed from: protected */
    public XUIBaseFullscreenContract.Presenter getPresenter() {
        return null;
    }

    public void xuiGenerateBottomLayout(ViewStub viewStub) {
    }

    public void xuiGenerateMiddleContentLayout(ViewStub viewStub) {
    }

    public void xuiGenerateTopContentLayout(ViewStub viewStub) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.I("");
        setContentView(R.layout.checking_for_update_activity);
        if (getActionBar() != null) {
            getActionBar().setTitle(FotaProviderInitializer.getContext().getString(DeviceType.get().getTextType().getTitleId()));
        }
    }
}
