package com.accessorydm.ui.downloadandinstallconfirm;

import android.os.Bundle;
import android.view.ViewStub;
import com.accessorydm.ui.downloadandinstallconfirm.DownloadAndInstallConfirmContract;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenActivity;
import com.accessorydm.ui.fullscreen.content.BottomContentView;
import com.accessorydm.ui.fullscreen.content.MiddleContentView;
import com.accessorydm.ui.fullscreen.content.TopContentView;
import com.samsung.android.fotaprovider.log.Log;

public class DownloadAndInstallConfirmActivity extends XUIBaseFullscreenActivity implements DownloadAndInstallConfirmContract.View {
    private BottomContentView.TwoButtons bottomContentView = null;
    private MiddleContentView.WithCaution middleContentView = null;
    /* access modifiers changed from: private */
    public DownloadAndInstallConfirmContract.Presenter presenter;
    private TopContentView.Guide topContentView = null;

    /* access modifiers changed from: protected */
    public DownloadAndInstallConfirmContract.Presenter getPresenter() {
        return this.presenter;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.I("");
        this.presenter = new DownloadAndInstallConfirmPresenter(this, DownloadAndInstallConfirmModel.getInstance());
        super.onCreate(bundle);
        this.presenter.onCreate();
    }

    public void xuiGenerateTopContentLayout(ViewStub viewStub) {
        this.topContentView = new TopContentView.Guide(this, viewStub);
    }

    public void xuiGenerateMiddleContentLayout(ViewStub viewStub) {
        this.middleContentView = new MiddleContentView.WithCaution(this, viewStub);
    }

    public void xuiGenerateBottomLayout(ViewStub viewStub) {
        this.bottomContentView = new BottomContentView.TwoButtons(this, viewStub);
    }

    public TopContentView.Guide getTopContentView() {
        return this.topContentView;
    }

    public MiddleContentView.WithCaution getMiddleContentView() {
        return this.middleContentView;
    }

    public BottomContentView.TwoButtons getBottomContentView() {
        return this.bottomContentView;
    }

    public void xuiSetBottomButtonsClickListeners() {
        this.bottomContentView.setBottomButtonClickListeners(new BottomContentView.TwoButtons.BottomButtonAction() {
            public void firstButtonAction() {
                DownloadAndInstallConfirmActivity.this.presenter.doFirstButtonAction();
            }

            public void secondButtonAction() {
                DownloadAndInstallConfirmActivity.this.presenter.doSecondButtonAction();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.D("");
        super.onDestroy();
    }
}
