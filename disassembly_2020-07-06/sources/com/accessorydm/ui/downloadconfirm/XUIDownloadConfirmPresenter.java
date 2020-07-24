package com.accessorydm.ui.downloadconfirm;

import com.accessorydm.ui.downloadconfirm.XUIDownloadConfirmContract;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenPresenter;
import com.accessorydm.ui.notification.XUINotificationManager;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.galaxywearable.SALogUtil;

public class XUIDownloadConfirmPresenter extends XUIBaseFullscreenPresenter implements XUIDownloadConfirmContract.Presenter {
    private XUIDownloadConfirmModel model;
    private XUIDownloadConfirmContract.View view;

    XUIDownloadConfirmPresenter(XUIDownloadConfirmContract.View view2, XUIDownloadConfirmModel xUIDownloadConfirmModel) {
        this.view = view2;
        this.model = xUIDownloadConfirmModel;
    }

    /* access modifiers changed from: protected */
    public XUIDownloadConfirmContract.View getView() {
        return this.view;
    }

    /* access modifiers changed from: protected */
    public XUIDownloadConfirmModel getModel() {
        return this.model;
    }

    public void onCreate() {
        Log.I("");
        super.onCreate();
    }

    /* access modifiers changed from: protected */
    public void initializeTopContentUI() {
        if (this.view.getTopContentView() != null) {
            this.view.getTopContentView().setTitle(this.model.getGuideTitle());
            this.view.getTopContentView().setText(this.model.getGuideText());
        }
    }

    /* access modifiers changed from: protected */
    public void initializeMiddleContentUI() {
        if (this.view.getMiddleContentView() != null) {
            this.view.getMiddleContentView().setSoftwareUpdateInformation(this.model.getFirmwareVersion(), this.model.getFirmwareSize());
            this.view.getMiddleContentView().setWhatsNewText(this.model.getWhatsNewText());
        }
    }

    /* access modifiers changed from: protected */
    public void initializeBottomContentUI() {
        if (this.view.getBottomContentView() != null) {
            this.view.getBottomContentView().setFirstButtonText(this.model.getFirstButtonText());
            this.view.getBottomContentView().setSecondButtonText(this.model.getSecondButtonText());
        }
    }

    public void initializeListenersAfterCreatedUI() {
        this.view.xuiSetBottomButtonsClickListeners();
    }

    public void doFirstButtonAction() {
        if (checkPreconditionBeforeButtonBehavior()) {
            cancelDownload();
        }
    }

    public void doSecondButtonAction() {
        if (checkPreconditionBeforeButtonBehavior()) {
            download();
        }
    }

    private boolean checkPreconditionBeforeButtonBehavior() {
        if (!this.model.isEnabledBottomButtons()) {
            Log.I("already clicked and disabled buttons, not allowed duplication");
            return false;
        }
        disableButtonAfterClicked();
        XUINotificationManager.getInstance().xuiRemoveNotification(XUINotificationManager.getInstance().xuiGetNotificationType());
        return true;
    }

    private void disableButtonAfterClicked() {
        this.model.disableBottomButtons();
        this.view.getBottomContentView().disableButtons();
    }

    private void cancelDownload() {
        Log.I("");
        SALogUtil.loggingDownloadLaterButton();
        this.model.cancel();
        this.view.finish();
    }

    private void download() {
        Log.I("");
        SALogUtil.loggingDownloadNowButton();
        this.model.startDownload();
        this.view.finish();
    }

    /* access modifiers changed from: protected */
    public void actionForUpButton() {
        Log.D("");
        SALogUtil.loggingDownloadUpButton();
        this.view.xuiOnBackPressed();
    }

    public void onDestroy() {
        Log.D("");
        this.model.enableBottomButtons();
    }
}
