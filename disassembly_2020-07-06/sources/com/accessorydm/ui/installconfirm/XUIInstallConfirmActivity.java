package com.accessorydm.ui.installconfirm;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.ViewStub;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenActivity;
import com.accessorydm.ui.fullscreen.content.BottomContentView;
import com.accessorydm.ui.fullscreen.content.MiddleContentView;
import com.accessorydm.ui.fullscreen.content.TopContentView;
import com.accessorydm.ui.installconfirm.XUIInstallConfirmContract;
import com.accessorydm.ui.installconfirm.scheduleinstall.ScheduleInstallDialog;
import com.samsung.android.fotaprovider.log.Log;

public class XUIInstallConfirmActivity extends XUIBaseFullscreenActivity implements XUIInstallConfirmContract.View {
    private BottomContentView.TwoButtons bottomContentView = null;
    private MiddleContentView.WithCaution middleContentView = null;
    /* access modifiers changed from: private */
    public XUIInstallConfirmContract.Presenter presenter;
    private TopContentView.Guide topContentView = null;

    public XUIInstallConfirmContract.Presenter getPresenter() {
        return this.presenter;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.I("");
        this.presenter = new XUIInstallConfirmPresenter(this, XUIInstallConfirmModel.getInstance());
        super.onCreate(bundle);
        this.presenter.onCreate();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.I("");
        super.onResume();
        this.presenter.onResume();
    }

    /* access modifiers changed from: protected */
    public void onUserLeaveHint() {
        Log.D("");
        this.presenter.onUserLeaveHint();
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
                XUIInstallConfirmActivity.this.presenter.doFirstButtonAction();
            }

            public void secondButtonAction() {
                XUIInstallConfirmActivity.this.presenter.doSecondButtonAction();
            }
        });
    }

    public void xuiShowScheduleInstallDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.findFragmentByTag(ScheduleInstallDialog.TAG) == null) {
            new ScheduleInstallDialog().show(fragmentManager, ScheduleInstallDialog.TAG);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.D("");
        super.onDestroy();
    }
}
