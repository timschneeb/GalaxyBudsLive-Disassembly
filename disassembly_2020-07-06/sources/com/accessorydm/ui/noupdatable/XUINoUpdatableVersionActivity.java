package com.accessorydm.ui.noupdatable;

import android.os.Bundle;
import android.view.ViewStub;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenActivity;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenContract;
import com.accessorydm.ui.fullscreen.content.MiddleContentView;
import com.accessorydm.ui.fullscreen.content.TopContentView;
import com.accessorydm.ui.noupdatable.XUINoUpdatableVersionContract;
import com.samsung.android.fotaprovider.log.Log;

public class XUINoUpdatableVersionActivity extends XUIBaseFullscreenActivity implements XUINoUpdatableVersionContract.View {
    private MiddleContentView.WithoutCaution middleContentView = null;
    private XUINoUpdatableVersionContract.Presenter presenter = null;
    private TopContentView.Guide topContentView = null;

    public void xuiGenerateBottomLayout(ViewStub viewStub) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.I("");
        this.presenter = new XUINoUpdatableVersionPresenter(this);
        this.presenter.onCreate();
    }

    /* access modifiers changed from: protected */
    public XUIBaseFullscreenContract.Presenter getPresenter() {
        return this.presenter;
    }

    public void xuiGenerateTopContentLayout(ViewStub viewStub) {
        this.topContentView = new TopContentView.Guide(this, viewStub);
    }

    public void xuiGenerateMiddleContentLayout(ViewStub viewStub) {
        this.middleContentView = new MiddleContentView.WithoutCaution(this, viewStub);
    }

    public TopContentView.Guide getTopContentView() {
        return this.topContentView;
    }

    public MiddleContentView.WithoutCaution getMiddleContentView() {
        return this.middleContentView;
    }

    public void onDestroy() {
        Log.I("");
        super.onDestroy();
    }
}
