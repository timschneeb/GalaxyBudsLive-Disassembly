package com.accessorydm.filetransfer.action;

import com.accessorydm.XDMSecReceiverApiCall;
import com.accessorydm.adapter.XDMInitAdapter;
import com.accessorydm.db.file.AccessoryInfoAdapter;
import com.accessorydm.db.file.XDBFumoAdp;
import com.accessorydm.eng.core.XDMMsg;
import com.accessorydm.filetransfer.XDMFileTransferManager;
import com.accessorydm.interfaces.XDMAccessoryInterface;
import com.accessorydm.interfaces.XEventInterface;
import com.accessorydm.interfaces.XFOTAInterface;
import com.samsung.accessory.fotaprovider.AccessoryController;
import com.samsung.accessory.fotaprovider.AccessoryState;
import com.samsung.accessory.fotaprovider.controller.ConsumerInfo;
import com.samsung.accessory.fotaprovider.controller.RequestError;
import com.samsung.android.fotaprovider.appstate.FotaProviderState;
import com.samsung.android.fotaprovider.log.Log;

enum CheckDeviceInfoActionSelector {
    CHECK_DEVICE {
        /* access modifiers changed from: package-private */
        public void onSuccessAction(ConsumerInfo consumerInfo) {
            if (consumerInfo == null || !isCorrectDevice(consumerInfo)) {
                Log.I("Check fail");
                XDMSecReceiverApiCall.getInstance().xdmAccessoryCheckDeviceCallback(0);
                return;
            }
            new AccessoryInfoAdapter().updateAccessoryDB(consumerInfo.getAccessoryInfo());
            XDMSecReceiverApiCall.getInstance().xdmAccessoryCheckDeviceCallback(1);
        }

        /* access modifiers changed from: package-private */
        public void onFailureAction(RequestError requestError) {
            if (requestError != null) {
                int i = AnonymousClass7.$SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[requestError.ordinal()];
                if (i == 1) {
                    Log.E("Socket error is different device. reset data and re-register!!");
                    FotaProviderState.blockActionDuringChangedDeviceProcess();
                    XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_ACCESSORY_DIFFERENT_DEVICE, (Object) null, (Object) null);
                    return;
                } else if (i == 2 || i == 3 || i == 4 || i == 5) {
                    XDMSecReceiverApiCall.getInstance().xdmAccessoryCheckDeviceCallback(0);
                }
            }
            FileTransferFailure.handleAccessoryConnectionFailure(requestError);
        }
    },
    DOWNLOAD {
        /* access modifiers changed from: package-private */
        public void onSuccessAction(ConsumerInfo consumerInfo) {
            if (consumerInfo != null) {
                new AccessoryInfoAdapter().updateAccessoryDB(consumerInfo.getAccessoryInfo());
                if (!AccessoryController.getInstance().getAccessoryUtil().isAvailableFreeSpaceForDownload(XDBFumoAdp.xdbGetObjectSizeFUMO())) {
                    Log.I("Low Memory");
                    FileTransferFailure.handleLowMemory(XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_DONWLOAD);
                    return;
                }
                XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DL_CONNECT, (Object) null, (Object) null);
            }
        }

        /* access modifiers changed from: package-private */
        public void onFailureAction(RequestError requestError) {
            if (!checkBlockedSocketErrorState(requestError)) {
                FileTransferFailure.handleAccessoryConnectionFailure(requestError);
            }
        }
    },
    COPY {
        /* access modifiers changed from: package-private */
        public void onSuccessAction(ConsumerInfo consumerInfo) {
            if (consumerInfo != null) {
                new AccessoryInfoAdapter().updateAccessoryDB(consumerInfo.getAccessoryInfo());
                if (!AccessoryController.getInstance().getAccessoryUtil().isAvailableFreeSpaceForCopy(XDBFumoAdp.xdbGetObjectSizeFUMO())) {
                    Log.I("Low Memory");
                    FileTransferFailure.handleLowMemory(XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_COPY);
                } else if (!AccessoryController.getInstance().getAccessoryUtil().isAvailableBatteryLevel()) {
                    Log.I("Low Battery");
                    FileTransferFailure.handleLowBattery(XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_COPY);
                } else {
                    XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_ACCESSORY_COPY, (Object) null, (Object) null);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void onFailureAction(RequestError requestError) {
            if (!checkBlockedSocketErrorState(requestError)) {
                FileTransferFailure.handleAccessoryConnectionFailure(requestError);
            }
        }
    },
    UPDATE_READY {
        /* access modifiers changed from: package-private */
        public void onSuccessAction(ConsumerInfo consumerInfo) {
            if (consumerInfo != null) {
                String firmwareVersion = new AccessoryInfoAdapter().getFirmwareVersion();
                new AccessoryInfoAdapter().updateAccessoryDB(consumerInfo.getAccessoryInfo());
                if (consumerInfo.getStatus() != AccessoryState.READY_TO_UPDATE.getValue()) {
                    FileTransferFailure.handleInstallFailure("414");
                    return;
                }
                Log.I("Check Fw version DB: " + firmwareVersion + ", result: " + consumerInfo.getFirmwareVersion());
                if (!AccessoryController.getInstance().getAccessoryUtil().isAvailableFreeSpaceForInstall(XDBFumoAdp.xdbGetObjectSizeFUMO())) {
                    Log.I("Low Memory");
                    FileTransferFailure.handleLowMemory(XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_INSTALL);
                } else if (!AccessoryController.getInstance().getAccessoryUtil().isAvailableBatteryLevel()) {
                    Log.I("Low Battery");
                    FileTransferFailure.handleLowBattery(XDMAccessoryInterface.XDMAccessoryCheckState.XDM_ACCESSORY_CHECK_INSTALL);
                } else if (!firmwareVersion.equals(consumerInfo.getFirmwareVersion())) {
                    FileTransferFailure.handleInstallFailure("403");
                } else {
                    XDMFileTransferManager.installPackage();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void onFailureAction(RequestError requestError) {
            if (!checkBlockedSocketErrorState(requestError)) {
                FileTransferFailure.handleAccessoryConnectionFailure(requestError);
            }
        }
    },
    CONSUMER_STATUS {
        /* access modifiers changed from: package-private */
        public void onSuccessAction(ConsumerInfo consumerInfo) {
            if (consumerInfo != null) {
                switch (AnonymousClass7.$SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState[AccessoryState.getStateByValue(consumerInfo.getStatus()).ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        XDMInitAdapter.xdmAccessoryUpdateResultSetAndReport(XFOTAInterface.XFOTA_GENERIC_UPDATE_FAILED);
                        consumerInfo.setStatus(AccessoryState.INIT.getValue());
                        break;
                    case 6:
                        FileTransferFailure.handleAccessoryConnectionFailure((RequestError) null);
                        break;
                }
                new AccessoryInfoAdapter().updateAccessoryDB(consumerInfo.getAccessoryInfo());
            }
        }

        /* access modifiers changed from: package-private */
        public void onFailureAction(RequestError requestError) {
            checkBlockedSocketErrorState(requestError);
        }
    },
    UPDATE_REPORT {
        /* access modifiers changed from: package-private */
        public void onSuccessAction(ConsumerInfo consumerInfo) {
            XDMInitAdapter.xdmAccessoryUpdateResultReport();
        }

        /* access modifiers changed from: package-private */
        public void onFailureAction(RequestError requestError) {
            Log.I("Check error state : " + requestError);
            FileTransferFailure.handleAccessoryConnectionFailure(requestError);
        }
    };

    /* access modifiers changed from: package-private */
    public abstract void onFailureAction(RequestError requestError);

    /* access modifiers changed from: package-private */
    public abstract void onSuccessAction(ConsumerInfo consumerInfo);

    /* renamed from: com.accessorydm.filetransfer.action.CheckDeviceInfoActionSelector$7  reason: invalid class name */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState = null;
        static final /* synthetic */ int[] $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|(2:21|22)|23|25|26|27|28|29|30|31|32|33|34|35|36|38) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|(2:17|18)|19|(2:21|22)|23|25|26|27|28|29|30|31|32|33|34|35|36|38) */
        /* JADX WARNING: Can't wrap try/catch for region: R(30:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|(2:21|22)|23|25|26|27|28|29|30|31|32|33|34|35|36|38) */
        /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x005e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0068 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0072 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x007c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0086 */
        static {
            $SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState = new int[AccessoryState.values().length];
            try {
                $SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState[AccessoryState.INIT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState[AccessoryState.COPY_FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState[AccessoryState.COPY_IN_PROGRESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState[AccessoryState.READY_TO_UPDATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState[AccessoryState.UPDATE_TO_REPORTING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$samsung$accessory$fotaprovider$AccessoryState[AccessoryState.UPDATE_IN_PROGRESS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError = new int[RequestError.values().length];
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_DIFFERENT_DEVICE.ordinal()] = 1;
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_CONNECT.ordinal()] = 2;
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_CONNECTION_LOST.ordinal()] = 3;
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_CONSUMER_SCANNING.ordinal()] = 4;
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_CONSUMER_MODIFIED.ordinal()] = 5;
            $SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[RequestError.ERROR_CONSUMER_MDM_BLOCKED.ordinal()] = 6;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isCorrectDevice(ConsumerInfo consumerInfo) {
        AccessoryInfoAdapter accessoryInfoAdapter = new AccessoryInfoAdapter();
        return accessoryInfoAdapter.getDeviceId().equals(consumerInfo.getDeviceId()) && accessoryInfoAdapter.getModelNumber().equals(consumerInfo.getModelNumber()) && accessoryInfoAdapter.getSalesCode().equals(consumerInfo.getSalesCode());
    }

    /* access modifiers changed from: package-private */
    public boolean checkBlockedSocketErrorState(RequestError requestError) {
        if (requestError == null) {
            Log.W("requestError == null");
            return false;
        }
        int i = AnonymousClass7.$SwitchMap$com$samsung$accessory$fotaprovider$controller$RequestError[requestError.ordinal()];
        if (i == 1) {
            Log.I("Socket error is different device. send result watch change");
            FileTransferFailure.handleInstallFailure("415");
            return true;
        } else if (i == 5) {
            Log.I("Socket error is rooting device. send result watch rooting");
            FileTransferFailure.handleInstallFailure(XFOTAInterface.XFOTA_GENERIC_ROOTING_UPDATE_FAILED);
            return true;
        } else if (i != 6) {
            return false;
        } else {
            Log.I("Socket error is mdm blocked. send result watch mdm blocked");
            FileTransferFailure.handleInstallFailure(XFOTAInterface.XFOTA_GENERIC_BLOCKED_MDM_UPDATE_FAILED);
            return true;
        }
    }

    private static CheckDeviceInfoActionSelector actionByFumoStatus() {
        int xdbGetFUMOStatus = XDBFumoAdp.xdbGetFUMOStatus();
        if (xdbGetFUMOStatus == 0) {
            return CHECK_DEVICE;
        }
        if (xdbGetFUMOStatus != 40) {
            if (xdbGetFUMOStatus != 50) {
                if (xdbGetFUMOStatus == 60) {
                    return CONSUMER_STATUS;
                }
                if (xdbGetFUMOStatus == 65 || xdbGetFUMOStatus == 80 || xdbGetFUMOStatus == 100) {
                    return UPDATE_REPORT;
                }
                if (xdbGetFUMOStatus == 200) {
                    return DOWNLOAD;
                }
                if (xdbGetFUMOStatus != 220) {
                    if (xdbGetFUMOStatus != 250) {
                        if (xdbGetFUMOStatus != 251) {
                            return null;
                        }
                    }
                }
            }
            return UPDATE_READY;
        }
        return COPY;
    }

    public static void nextAction(Object obj) {
        CheckDeviceInfoActionSelector actionByFumoStatus = actionByFumoStatus();
        if (actionByFumoStatus != null) {
            Log.I("CheckDeviceInfoActionSelector : " + actionByFumoStatus);
            if (obj instanceof ConsumerInfo) {
                actionByFumoStatus.onSuccessAction((ConsumerInfo) obj);
            } else if (obj instanceof RequestError) {
                actionByFumoStatus.onFailureAction((RequestError) obj);
            } else {
                Log.W("unsupported result : " + obj);
            }
        }
    }
}
