package com.accessorydm.ui.downloadandinstallconfirm;

import com.accessorydm.ui.downloadandinstallconfirm.DownloadAndInstallConfirmContract;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenContract;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenModel;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenPresenter;
import com.accessorydm.ui.notification.XUINotificationManager;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.galaxywearable.SALogUtil;

public class DownloadAndInstallConfirmPresenter extends XUIBaseFullscreenPresenter implements DownloadAndInstallConfirmContract.Presenter {
    private DownloadAndInstallConfirmModel model;
    private DownloadAndInstallConfirmContract.View view;

    DownloadAndInstallConfirmPresenter(DownloadAndInstallConfirmContract.View view2, DownloadAndInstallConfirmModel downloadAndInstallConfirmModel) {
        this.view = view2;
        this.model = downloadAndInstallConfirmModel;
    }

    /* access modifiers changed from: protected */
    public XUIBaseFullscreenContract.View getView() {
        return this.view;
    }

    /* access modifiers changed from: protected */
    public XUIBaseFullscreenModel getModel() {
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
            this.view.getMiddleContentView().setWhatsNewText(this.model.getWhatsNewText());
            this.view.getMiddleContentView().setSoftwareUpdateInformation(this.model.getFirmwareVersion(), this.model.getFirmwareSize());
            this.view.getMiddleContentView().setCautionText(this.model.getCautionText());
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

    public void onDestroy() {
        Log.D("");
        this.model.enableBottomButtons();
    }

    public void doFirstButtonAction() {
        if (checkPreconditionBeforeButtonBehavior()) {
            cancel();
        }
    }

    public void doSecondButtonAction() {
        if (checkPreconditionBeforeButtonBehavior()) {
            download();
        }
    }

    private void cancel() {
        Log.I("");
        SALogUtil.loggingDownloadLaterButton();
        this.model.cancel();
        this.view.finish();
    }

    private void download() {
        Log.I("");
        SALogUtil.loggingDownloadNowButton();
        this.model.startDownloadAndInstall();
        this.view.finish();
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

    /* access modifiers changed from: protected */
    public void actionForUpButton() {
        Log.D("");
        SALogUtil.loggingDownloadUpButton();
        this.view.xuiOnBackPressed();
    }
}
