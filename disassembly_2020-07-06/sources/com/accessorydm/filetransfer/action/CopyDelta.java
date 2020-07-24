package com.accessorydm.filetransfer.action;

import com.accessorydm.XDMDmUtils;
import com.accessorydm.adapter.XDMInitAdapter;
import com.accessorydm.agent.fota.XFOTADl;
import com.accessorydm.db.file.XDB;
import com.accessorydm.db.file.XDBFumoAdp;
import com.accessorydm.eng.core.XDMEvent;
import com.accessorydm.eng.core.XDMMsg;
import com.accessorydm.interfaces.XDMAccessoryInterface;
import com.accessorydm.interfaces.XFOTAInterface;
import com.accessorydm.interfaces.XUIEventInterface;
import com.accessorydm.postpone.PostponeManager;
import com.accessorydm.ui.progress.XUIProgressModel;
import com.samsung.accessory.fotaprovider.AccessoryController;
import com.samsung.accessory.fotaprovider.controller.ConsumerInfo;
import com.samsung.accessory.fotaprovider.controller.RequestController;
import com.samsung.accessory.fotaprovider.controller.RequestError;
import com.samsung.android.fotaprovider.log.Log;
import java.io.File;
import java.util.Locale;

public final class CopyDelta extends FileTransferAction {
    /* access modifiers changed from: package-private */
    public boolean checkPrecondition() {
        XDMDmUtils.getInstance().xdmSetWaitWifiConnectMode(0);
        PostponeManager.cancelPostpone();
        XFOTADl.xfotaCopySetDrawingPercentage(true);
        XDBFumoAdp.xdbSetFUMOStatus(250);
        XDMMsg.xdmSendUIMessage(XUIEventInterface.DL_UIEVENT.XUI_DL_COPY_IN_PROGRESS, Long.valueOf(XDBFumoAdp.xdbGetObjectSizeFUMO()), (Object) null);
        if (!AccessoryController.getInstance().getConnectionController().isConnected()) {
            Log.W("Device connection is not ready");
            failedCopyProcess();
            return false;
        } else if (!AccessoryController.getInstance().getRequestController().isInProgress()) {
            return true;
        } else {
            Log.W("Accessory is in progress");
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void controlAccessory() {
        Log.I("");
        File file = new File(XDB.xdbFileGetNameFromCallerID(XDB.xdbGetFileIdFirmwareData()));
        Log.I(file.getName());
        if (!file.exists()) {
            Log.W("file is not existed");
            failedCopyProcess();
            return;
        }
        AccessoryController.getInstance().getRequestController().sendPackage(file.getPath(), new RequestController.RequestCallback.Result() {
            public void onSuccessAction(ConsumerInfo consumerInfo) {
                Log.I("copyDelta succeeded");
                CopyDelta.this.completeCopyProcess();
            }

            public void onFailure(RequestError requestError) {
                Log.W("copyDelta failed");
                if (requestError != null) {
                    Log.W("error: " + requestError);
                    if (requestError == RequestError.ERROR_LOW_MEMORY) {
                        Log.W("Low Memory by Socket");
                        FileTransferFailure.handleLowMemory(XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_COPY);
                        return;
                    }
                }
                CopyDelta.this.failedCopyProcess();
            }
        }, new RequestController.RequestCallback.FileTransfer() {
            public void onFileTransferStart() {
                Log.I("Copying Progress Reset");
                CopyDelta.this.updateCopyProgress(0);
            }

            public void onFileProgress(int i) {
                Log.I(String.format(Locale.US, "Copying Percentage:%d%%", new Object[]{Integer.valueOf(i)}));
                CopyDelta.this.updateCopyProgress(i);
            }
        });
        Log.I("Start to transfer delta package!");
    }

    /* access modifiers changed from: private */
    public void updateCopyProgress(int i) {
        XUIProgressModel.getInstance().updateProgressInfoForCopy(i);
    }

    /* access modifiers changed from: private */
    public void completeCopyProcess() {
        XFOTADl.xfotaCopySetDrawingPercentage(false);
        XDBFumoAdp.xdbSetFUMOCopyRetryCount(0);
        XDB.xdbAdpDeltaAllClear();
        XDBFumoAdp.xdbSetFUMOStatus(251);
        XUIProgressModel.getInstance().initializeProgress();
        XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.DL_UIEVENT.XUI_DL_UPDATE_CONFIRM);
    }

    /* access modifiers changed from: private */
    public void failedCopyProcess() {
        Log.I("");
        int xdbGetFUMOCopyRetryCount = XDBFumoAdp.xdbGetFUMOCopyRetryCount() + 1;
        XFOTADl.xfotaCopySetDrawingPercentage(false);
        if (xdbGetFUMOCopyRetryCount >= 3) {
            XUIProgressModel.getInstance().initializeProgress();
            XDBFumoAdp.xdbSetFUMOCopyRetryCount(0);
            XDMInitAdapter.xdmAccessoryUpdateResultSetAndReport(XFOTAInterface.XFOTA_GENERIC_SAP_COPY_FAILED);
            XDB.xdbAdpDeltaAllClear();
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_COPY_FAILED);
            return;
        }
        XUIProgressModel.getInstance().initializeProgress();
        XDBFumoAdp.xdbSetFUMOCopyRetryCount(xdbGetFUMOCopyRetryCount);
        XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_COPY_RETRY_LATER);
    }
}
