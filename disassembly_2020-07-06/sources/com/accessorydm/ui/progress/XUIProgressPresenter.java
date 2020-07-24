package com.accessorydm.ui.progress;

import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenPresenter;
import com.accessorydm.ui.progress.XUIProgressContract;
import com.accessorydm.ui.progress.listener.XUIProgressListener;
import com.accessorydm.ui.progress.listener.XUIProgressViewListener;
import com.samsung.android.fotaprovider.log.Log;

public abstract class XUIProgressPresenter extends XUIBaseFullscreenPresenter implements XUIProgressContract.Presenter {
    private XUIProgressListener listener = null;
    private XUIProgressModel model;
    protected XUIProgressContract.View view;

    /* access modifiers changed from: protected */
    public void initializeBottomContentUI() {
    }

    XUIProgressPresenter(XUIProgressContract.View view2, XUIProgressModel xUIProgressModel) {
        this.view = view2;
        this.model = xUIProgressModel;
    }

    /* access modifiers changed from: protected */
    public XUIProgressContract.View getView() {
        return this.view;
    }

    /* access modifiers changed from: protected */
    public XUIProgressModel getModel() {
        return this.model;
    }

    /* access modifiers changed from: protected */
    public void initializeTopContentUI() {
        if (this.view.getTopContentView() != null) {
            this.view.getTopContentView().setTitle(this.model.getProgressTitle());
            this.view.getTopContentView().setProgressbarProgress(0);
            this.view.getTopContentView().setSizeText("");
        }
        if (this.model.getProgressPercent() >= 100 || this.model.getProgressPercent() < 0) {
            this.model.initializeProgressInfo();
        }
    }

    /* access modifiers changed from: protected */
    public void initializeMiddleContentUI() {
        if (this.view.getMiddleContentView() != null) {
            this.view.getMiddleContentView().setSoftwareUpdateInformation(this.model.getFirmwareVersion(), this.model.getFirmwareSize());
            this.view.getMiddleContentView().setWhatsNewText(this.model.getWhatsNewText());
        }
    }

    public void initializeListenersAfterCreatedUI() {
        this.listener = new XUIProgressViewListener(this.view);
        this.model.addProgressListener(this.listener);
    }

    public void onDestroy() {
        Log.D("");
        this.model.removeProgressListener(this.listener);
        this.listener = null;
    }
}
