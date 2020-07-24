package com.accessorydm.ui.downloadconfirm;

import android.os.Bundle;
import android.view.ViewStub;
import com.accessorydm.ui.downloadconfirm.XUIDownloadConfirmContract;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenActivity;
import com.accessorydm.ui.fullscreen.content.BottomContentView;
import com.accessorydm.ui.fullscreen.content.MiddleContentView;
import com.accessorydm.ui.fullscreen.content.TopContentView;
import com.samsung.android.fotaprovider.log.Log;

public class XUIDownloadConfirmActivity extends XUIBaseFullscreenActivity implements XUIDownloadConfirmContract.View {
    private BottomContentView.TwoButtons bottomContentView = null;
    private MiddleContentView.WithoutCaution middleContentView = null;
    /* access modifiers changed from: private */
    public XUIDownloadConfirmContract.Presenter presenter;
    private TopContentView.Guide topContentView = null;

    public XUIDownloadConfirmContract.Presenter getPresenter() {
        return this.presenter;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.I("");
        this.presenter = new XUIDownloadConfirmPresenter(this, XUIDownloadConfirmModel.getInstance());
        super.onCreate(bundle);
        this.presenter.onCreate();
    }

    public void xuiGenerateTopContentLayout(ViewStub viewStub) {
        this.topContentView = new TopContentView.Guide(this, viewStub);
    }

    public void xuiGenerateMiddleContentLayout(ViewStub viewStub) {
        this.middleContentView = new MiddleContentView.WithoutCaution(this, viewStub);
    }

    public void xuiGenerateBottomLayout(ViewStub viewStub) {
        this.bottomContentView = new BottomContentView.TwoButtons(this, viewStub);
    }

    public TopContentView.Guide getTopContentView() {
        return this.topContentView;
    }

    public MiddleContentView.WithoutCaution getMiddleContentView() {
        return this.middleContentView;
    }

    public BottomContentView.TwoButtons getBottomContentView() {
        return this.bottomContentView;
    }

    public void xuiSetBottomButtonsClickListeners() {
        this.bottomContentView.setBottomButtonClickListeners(new BottomContentView.TwoButtons.BottomButtonAction() {
            public void firstButtonAction() {
                XUIDownloadConfirmActivity.this.presenter.doFirstButtonAction();
            }

            public void secondButtonAction() {
                XUIDownloadConfirmActivity.this.presenter.doSecondButtonAction();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.D("");
        super.onDestroy();
    }
}
