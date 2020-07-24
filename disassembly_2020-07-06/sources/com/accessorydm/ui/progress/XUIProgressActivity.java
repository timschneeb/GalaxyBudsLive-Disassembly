package com.accessorydm.ui.progress;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenActivity;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenContract;
import com.accessorydm.ui.fullscreen.content.MiddleContentView;
import com.accessorydm.ui.fullscreen.content.TopContentView;
import com.accessorydm.ui.progress.XUIProgressContract;
import com.samsung.android.fotaprovider.log.Log;

public class XUIProgressActivity extends XUIBaseFullscreenActivity implements XUIProgressContract.View {
    private MiddleContentView.WithoutCaution middleContentView = null;
    private XUIProgressPresenter presenter;
    private TopContentView.Progress topContentView = null;

    public void xuiGenerateBottomLayout(ViewStub viewStub) {
    }

    /* access modifiers changed from: protected */
    public XUIBaseFullscreenContract.Presenter getPresenter() {
        return this.presenter;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.I("");
        xuiCreatePresenter(getIntent().getIntExtra("progressMode", 0));
        super.onCreate(bundle);
        this.presenter.onCreate();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        Log.I("");
        xuiCreatePresenter(intent.getIntExtra("progressMode", 0));
        super.onNewIntent(intent);
        this.presenter.onCreate();
    }

    private void xuiCreatePresenter(int i) {
        if (i == 1) {
            Log.I("Current progress: Download");
            this.presenter = new XUIDownloadProgressPresenter(this, XUIProgressModel.getInstance());
        } else if (i != 2) {
            Log.W("Current progress: Not available");
            finish();
        } else {
            Log.I("Current progress: Copy");
            this.presenter = new XUICopyProgressPresenter(this, XUIProgressModel.getInstance());
        }
    }

    public void xuiGenerateTopContentLayout(ViewStub viewStub) {
        this.topContentView = new TopContentView.Progress(this, viewStub);
    }

    public void xuiGenerateMiddleContentLayout(ViewStub viewStub) {
        this.middleContentView = new MiddleContentView.WithoutCaution(this, viewStub);
    }

    public TopContentView.Progress getTopContentView() {
        return this.topContentView;
    }

    public MiddleContentView.WithoutCaution getMiddleContentView() {
        return this.middleContentView;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.D("");
        super.onDestroy();
    }
}
