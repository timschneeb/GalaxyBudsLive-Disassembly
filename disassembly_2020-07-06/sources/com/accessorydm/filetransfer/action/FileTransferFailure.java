package com.accessorydm.filetransfer.action;

import com.accessorydm.XDMSecReceiverApiCall;
import com.accessorydm.XDMServiceManager;
import com.accessorydm.adapter.XDMInitAdapter;
import com.accessorydm.agent.fota.XFOTADl;
import com.accessorydm.db.file.XDB;
import com.accessorydm.db.file.XDBFumoAdp;
import com.accessorydm.eng.core.XDMEvent;
import com.accessorydm.eng.core.XDMMsg;
import com.accessorydm.filetransfer.XDMFileTransferManager;
import com.accessorydm.interfaces.XDMAccessoryInterface;
import com.accessorydm.interfaces.XEventInterface;
import com.accessorydm.interfaces.XFOTAInterface;
import com.accessorydm.interfaces.XUIEventInterface;
import com.accessorydm.ui.UIManager;
import com.accessorydm.ui.notification.XUINotificationManager;
import com.accessorydm.ui.notification.manager.NotificationId;
import com.accessorydm.ui.notification.manager.NotificationType;
import com.accessorydm.ui.progress.XUIProgressModel;
import com.samsung.accessory.fotaprovider.controller.RequestError;
import com.samsung.android.fotaprovider.log.Log;

public class FileTransferFailure {
    public static void changeDeviceWithReport(String str) {
        Log.I("");
        handleInstallFailure(str);
    }

    public static void changeDeviceWithoutReport() {
        Log.I("");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_ACCESSORY_DIFFERENT_DEVICE, (Object) null, (Object) null);
        XDMFileTransferManager.resetDevice();
    }

    static void handleAccessoryConnectionFailure(RequestError requestError) {
        Log.I("");
        if (XDBFumoAdp.xdbGetFUMOStatus() == 200) {
            XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DL_USER_CANCEL_DOWNLOAD, (Object) null, (Object) null);
        }
        if (requestError != null) {
            Log.I("Check error state : " + requestError);
            int i = AnonymousClass1.$SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[requestError.ordinal()];
            if (i == 1) {
                XDMSecReceiverApiCall.getInstance().setSysScopeScanned(true);
                XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_SYSSCOPE_SCANNING);
                XDMServiceManager.getInstance().xdmStopService();
                return;
            } else if (i == 2) {
                XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_SYSSCOPE_MODIFIED);
                XDMServiceManager.getInstance().xdmStopService();
                return;
            } else if (i == 3) {
                handleInstallFailure(XFOTAInterface.XFOTA_GENERIC_BLOCKED_MDM_UPDATE_FAILED);
                return;
            }
        }
        XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_CONNECTION_FAILED);
    }

    static void handleInstallFailure(String str) {
        int xdbGetFUMOStatus = XDBFumoAdp.xdbGetFUMOStatus();
        Log.I("FUMO status: " + xdbGetFUMOStatus + ", failReason: " + str);
        if (xdbGetFUMOStatus == 0) {
            Log.I("Do not report because FUMO status is none");
        } else if (xdbGetFUMOStatus == 30 || xdbGetFUMOStatus == 200) {
            XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DL_DEVICE_FAIL_DOWNLOAD, str, (Object) null);
        } else {
            XDMInitAdapter.xdmAccessoryUpdateResultSetAndReport(str);
        }
        UIManager.getInstance().finishAllActivities();
        XDMFileTransferManager.resetDevice();
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != 51544) {
            if (hashCode != 51663) {
                if (hashCode == 51696 && str.equals(XFOTAInterface.XFOTA_GENERIC_BLOCKED_MDM_UPDATE_FAILED)) {
                    c = 1;
                }
            } else if (str.equals(XFOTAInterface.XFOTA_GENERIC_ROOTING_UPDATE_FAILED)) {
                c = 0;
            }
        } else if (str.equals("415")) {
            c = 2;
        }
        if (c == 0) {
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_SYSSCOPE_MODIFIED);
        } else if (c == 1) {
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_BLOCKED_BY_POLICY_FAILED);
        } else if (c != 2) {
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_INSTALL_FAILED);
        }
    }

    /* renamed from: com.accessorydm.filetransfer.action.FileTransferFailure$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$accessorydm$interfaces$XDMAccessoryInterface$XDMAccessoryCheckState = new int[XDMAccessoryInterface.XDMAccessoryCheckState.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError = new int[RequestError.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        static {
            try {
                $SwitchMap$com$accessorydm$interfaces$XDMAccessoryInterface$XDMAccessoryCheckState[XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_DONWLOAD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$accessorydm$interfaces$XDMAccessoryInterface$XDMAccessoryCheckState[XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_COPY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$accessorydm$interfaces$XDMAccessoryInterface$XDMAccessoryCheckState[XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_INSTALL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_CONSUMER_SCANNING.ordinal()] = 1;
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_CONSUMER_MODIFIED.ordinal()] = 2;
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_CONSUMER_MDM_BLOCKED.ordinal()] = 3;
        }
    }

    static void handleLowMemory(XDMAccessoryInterface.XDMAccessoryCheckState xDMAccessoryCheckState) {
        Log.I("");
        int i = AnonymousClass1.$SwitchMap$com$accessorydm$interfaces$XDMAccessoryInterface$XDMAccessoryCheckState[xDMAccessoryCheckState.ordinal()];
        if (i == 1) {
            XDB.xdbAdpDeltaAllClear();
            XDMInitAdapter.xdmAccessoryUpdateResultSetAndReport(XFOTAInterface.XFOTA_GENERIC_SAP_FAILED_OUT_MEMORY);
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_LOW_MEMORY_DOWNLOAD_WATCH);
            XDMFileTransferManager.resetDevice();
        } else if (i == 2) {
            XDB.xdbAdpDeltaAllClear();
            XDMInitAdapter.xdmAccessoryUpdateResultSetAndReport(XFOTAInterface.XFOTA_GENERIC_SAP_FAILED_OUT_MEMORY);
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_LOW_MEMORY_COPY_WATCH);
            XDMFileTransferManager.resetDevice();
        } else if (i == 3) {
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_LOW_MEMORY_INSTALL_WATCH);
        }
    }

    static void handleLowBattery(XDMAccessoryInterface.XDMAccessoryCheckState xDMAccessoryCheckState) {
        Log.I("");
        int i = AnonymousClass1.$SwitchMap$com$accessorydm$interfaces$XDMAccessoryInterface$XDMAccessoryCheckState[xDMAccessoryCheckState.ordinal()];
        if (i == 2) {
            XFOTADl.xfotaCopySetDrawingPercentage(false);
            if (XDBFumoAdp.xdbGetFUMOInitiatedType() == 2) {
                XDBFumoAdp.xdbSetFUMOLowBatteryRetryCount(XDBFumoAdp.xdbGetFUMOLowBatteryRetryCount() + 1);
            }
            if (XDBFumoAdp.xdbGetFUMOLowBatteryRetryCount() >= 3) {
                XUIProgressModel.getInstance().initializeProgress();
                XDBFumoAdp.xdbSetFUMOLowBatteryRetryCount(0);
                XDB.xdbAdpDeltaAllClear();
                XUINotificationManager.getInstance().xuiRemoveNotification(NotificationId.XDM_NOTIFICATION_ID_PRIMARY);
                XDMInitAdapter.xdmAccessoryUpdateResultSetAndReport(XFOTAInterface.XFOTA_GENERIC_SAP_COPY_FAILED);
                XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_COPY_FAILED);
                XDMFileTransferManager.resetDevice();
                return;
            }
            XUIProgressModel.getInstance().initializeProgress();
            XDBFumoAdp.xdbSetFUMOStatus(250);
            XUINotificationManager.getInstance().xuiSetIndicator(NotificationType.XUI_INDICATOR_COPY_FAILED);
            if (XDBFumoAdp.xdbGetUiMode() == 1) {
                XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_LOW_BATTERY_WATCH);
            }
        } else if (i == 3) {
            XUINotificationManager.getInstance().xuiSetIndicator(NotificationType.XUI_INDICATOR_FOTA_UPDATE);
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.ACCESSORY_UIEVENT.XUI_DM_ACCESSORY_LOW_BATTERY_WATCH);
        }
    }
}
