package com.samsung.android.fotaagent;

import android.content.Intent;
import androidx.core.app.SafeJobIntentService;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.XDMSecReceiverApiCall;
import com.accessorydm.db.file.AccessoryInfoAdapter;
import com.accessorydm.db.file.XDBFumoAdp;
import com.accessorydm.db.file.XDBPollingAdp;
import com.accessorydm.db.file.XDBRegistrationAdp;
import com.samsung.accessory.fotaprovider.AccessoryController;
import com.samsung.accessory.fotaprovider.controller.ConsumerInfo;
import com.samsung.accessory.fotaprovider.controller.RequestController;
import com.samsung.accessory.fotaprovider.controller.RequestError;
import com.samsung.android.fotaagent.network.NetConnect;
import com.samsung.android.fotaagent.network.action.DeviceRegistrationAction;
import com.samsung.android.fotaagent.network.action.NetworkResult;
import com.samsung.android.fotaagent.network.action.PushRegistrationAction;
import com.samsung.android.fotaagent.polling.Polling;
import com.samsung.android.fotaagent.push.FCM;
import com.samsung.android.fotaagent.push.FCMResult;
import com.samsung.android.fotaagent.push.SPP;
import com.samsung.android.fotaagent.push.SPPResult;
import com.samsung.android.fotaagent.push.SPPResultReceiver;
import com.samsung.android.fotaagent.register.RegisterInterface;
import com.samsung.android.fotaagent.register.RegisterState;
import com.samsung.android.fotaagent.register.RegisterType;
import com.samsung.android.fotaagent.ui.DialogActivity;
import com.samsung.android.fotaprovider.FotaProviderInitializer;
import com.samsung.android.fotaprovider.appstate.FotaProviderState;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.GeneralUtil;
import com.samsung.android.fotaprovider.util.OperatorUtil;
import com.samsung.android.fotaprovider.util.type.DeviceType;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FotaRegisterJobIntentService extends SafeJobIntentService {
    /* access modifiers changed from: private */
    public int mRetryCount = 0;

    static /* synthetic */ int access$008(FotaRegisterJobIntentService fotaRegisterJobIntentService) {
        int i = fotaRegisterJobIntentService.mRetryCount;
        fotaRegisterJobIntentService.mRetryCount = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void onHandleWork(Intent intent) {
        RegisterType registerType = (RegisterType) intent.getSerializableExtra(RegisterInterface.REGISTER_TYPE);
        RegisterState registerState = (RegisterState) intent.getSerializableExtra(RegisterInterface.REGISTER_STATE);
        if (registerState == null) {
            sendNextState(registerType, RegisterState.CHECK_NEXT_STATE);
        } else {
            handleState(registerType, registerState, intent);
        }
    }

    /* access modifiers changed from: private */
    public void sendNextState(RegisterType registerType, RegisterState registerState) {
        sendNextState(registerType, registerState, 0);
    }

    private void sendNextState(RegisterType registerType, RegisterState registerState, int i) {
        Class<FotaRegisterJobIntentService> cls = FotaRegisterJobIntentService.class;
        Intent intent = new Intent(FotaProviderInitializer.getContext(), cls);
        intent.putExtra(RegisterInterface.REGISTER_TYPE, registerType);
        intent.putExtra(RegisterInterface.REGISTER_STATE, registerState);
        intent.putExtra(RegisterInterface.REGISTER_ERROR, i);
        intent.addFlags(32);
        enqueueWork(FotaProviderInitializer.getContext(), (Class) cls, FotaServiceJobId.INSTANCE.REGISTER_JOB_ID, intent);
    }

    private void sendRegisterPushIdState(RegisterType registerType, String str, String str2) {
        Class<FotaRegisterJobIntentService> cls = FotaRegisterJobIntentService.class;
        Intent intent = new Intent(FotaProviderInitializer.getContext(), cls);
        intent.putExtra(RegisterInterface.REGISTER_TYPE, registerType);
        intent.putExtra(RegisterInterface.REGISTER_STATE, RegisterState.REGISTERING_PUSH_ID);
        intent.putExtra(RegisterInterface.REGISTER_FCM_ID, str);
        intent.putExtra(RegisterInterface.REGISTER_SPP_ID, str2);
        intent.addFlags(32);
        enqueueWork(FotaProviderInitializer.getContext(), (Class) cls, FotaServiceJobId.INSTANCE.REGISTER_JOB_ID, intent);
    }

    private void handleState(RegisterType registerType, RegisterState registerState, Intent intent) {
        switch (registerState) {
            case CHECK_NEXT_STATE:
                Log.I("Register State: Check condition to decide next state");
                checkNextState(registerType);
                return;
            case CONNECTING_CONSUMER:
                Log.I("Register State: Connecting to consumer for initialization");
                initDevice(registerType);
                return;
            case CONSUMER_CONNECTION_FAILED:
                Log.I("Register State: Fail to connect consumer");
                sendNextState(registerType, RegisterState.REGISTRATION_COMPLETE);
                if (isUiMode(registerType)) {
                    DialogActivity.getUIHandler().showDialog(10);
                    return;
                }
                return;
            case REGISTERING_DEVICE:
                Log.I("Register State: Registering device");
                if (isUiMode(registerType)) {
                    DialogActivity.getUIHandler().showDialog(20);
                }
                lambda$handleState$0$FotaRegisterJobIntentService(registerType);
                return;
            case REGISTERING_DEVICE_WITH_DELAY:
                Log.I("Register State: Registering device after setup wizard");
                Executors.newSingleThreadScheduledExecutor().schedule(new Runnable(registerType) {
                    private final /* synthetic */ RegisterType f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        FotaRegisterJobIntentService.this.lambda$handleState$0$FotaRegisterJobIntentService(this.f$1);
                    }
                }, RegisterInterface.DELAY_PERIOD_FOR_BACKGROUND_REGISTER, TimeUnit.MILLISECONDS);
                return;
            case DEVICE_REGISTRATION_FAILED:
                Log.I("Register State: Fail to register device");
                XDBRegistrationAdp.setDeviceRegistrationStatus(0);
                sendNextState(registerType, RegisterState.REGISTRATION_COMPLETE);
                if (isUiMode(registerType)) {
                    DialogActivity.getUIHandler().showDialog(30, intent.getIntExtra(RegisterInterface.REGISTER_ERROR, 0));
                    return;
                }
                return;
            case DEVICE_REGISTRATION_SUCCESS:
                Log.I("Register State: Success to register device");
                registeredDevice(registerType);
                if (isUiMode(registerType)) {
                    DialogActivity.getUIHandler().showDialog(40);
                    return;
                }
                return;
            case REGISTERING_POLLING:
                Log.I("Register State: Registering polling");
                registerPolling(registerType);
                return;
            case REGISTERING_PUSH:
                Log.I("Register State: Registering push");
                registerPush(registerType);
                return;
            case REGISTERING_PUSH_ID:
                Log.I("Register State: Registering push id");
                registerPushID(registerType, intent.getStringExtra(RegisterInterface.REGISTER_FCM_ID), intent.getStringExtra(RegisterInterface.REGISTER_SPP_ID));
                return;
            case PUSH_REGISTRATION_FAILED:
                Log.I("Register State: Fail to register push");
                sendNextState(registerType, RegisterState.REGISTRATION_COMPLETE);
                return;
            case REGISTRATION_COMPLETE:
                Log.I("Register State: Finish registration");
                stopSelf();
                return;
            default:
                return;
        }
    }

    private void checkNextState(final RegisterType registerType) {
        int deviceRegistrationStatus = XDBRegistrationAdp.getDeviceRegistrationStatus();
        if (deviceRegistrationStatus != 0) {
            if (deviceRegistrationStatus == 1) {
                Log.I("registered. go to next step");
                if (AnonymousClass3.$SwitchMap$com$samsung$android$fotaagent$register$RegisterType[registerType.ordinal()] != 3) {
                    sendNextState(registerType, RegisterState.REGISTERING_POLLING);
                } else {
                    sendNextState(registerType, RegisterState.REGISTERING_PUSH);
                }
            } else if (deviceRegistrationStatus != 2) {
                Log.W("no more status to check");
                sendNextState(registerType, RegisterState.REGISTRATION_COMPLETE);
            } else {
                Log.I("in registering...");
                int i = AnonymousClass3.$SwitchMap$com$samsung$android$fotaagent$register$RegisterType[registerType.ordinal()];
                if (i != 1) {
                    if (i != 2) {
                        sendNextState(registerType, RegisterState.REGISTERING_DEVICE);
                    } else {
                        sendNextState(registerType, RegisterState.REGISTERING_DEVICE_WITH_DELAY);
                    }
                } else if (DialogActivity.getUIHandler() == null) {
                    Intent intent = new Intent(this, DialogActivity.class);
                    intent.addFlags(335544352);
                    startActivity(intent);
                    final Timer timer = new Timer("T:FotaRegisterJobIntentService");
                    timer.schedule(new TimerTask() {
                        public void run() {
                            FotaRegisterJobIntentService.access$008(FotaRegisterJobIntentService.this);
                            if (DialogActivity.getUIHandler() != null) {
                                timer.cancel();
                                FotaRegisterJobIntentService.this.sendNextState(registerType, RegisterState.REGISTERING_DEVICE);
                            } else if (FotaRegisterJobIntentService.this.mRetryCount > 5) {
                                timer.cancel();
                                FotaRegisterJobIntentService.this.sendNextState(RegisterType.BACKGROUND, RegisterState.REGISTERING_DEVICE);
                                Log.W("Fail to start dialog activity");
                            } else {
                                Log.I("Waiting for start dialog activity...(" + FotaRegisterJobIntentService.this.mRetryCount + ")");
                            }
                        }
                    }, 500, 500);
                }
            }
        } else if (XDBFumoAdp.xdbGetFUMOStatus() == 0) {
            Log.I("need to register");
            sendNextState(registerType, RegisterState.CONNECTING_CONSUMER);
        } else {
            Log.I("do not need to register, abnormal case, reset all data");
            FotaProviderState.resetDataAndStopAlarms(FotaProviderInitializer.getContext());
            sendNextState(registerType, RegisterState.REGISTRATION_COMPLETE);
        }
    }

    /* renamed from: com.samsung.android.fotaagent.FotaRegisterJobIntentService$3  reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$fotaagent$register$RegisterType = new int[RegisterType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(30:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|(3:35|36|38)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(31:0|(2:1|2)|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|(3:35|36|38)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(34:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|38) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0051 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0067 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0072 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x007d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0089 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0095 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00a1 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00ad */
        static {
            try {
                $SwitchMap$com$samsung$android$fotaagent$register$RegisterType[RegisterType.FOREGROUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$samsung$android$fotaagent$register$RegisterType[RegisterType.BACKGROUND_WITH_DELAY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$samsung$android$fotaagent$register$RegisterType[RegisterType.PUSH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState = new int[RegisterState.values().length];
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.CHECK_NEXT_STATE.ordinal()] = 1;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.CONNECTING_CONSUMER.ordinal()] = 2;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.CONSUMER_CONNECTION_FAILED.ordinal()] = 3;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.REGISTERING_DEVICE.ordinal()] = 4;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.REGISTERING_DEVICE_WITH_DELAY.ordinal()] = 5;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.DEVICE_REGISTRATION_FAILED.ordinal()] = 6;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.DEVICE_REGISTRATION_SUCCESS.ordinal()] = 7;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.REGISTERING_POLLING.ordinal()] = 8;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.REGISTERING_PUSH.ordinal()] = 9;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.REGISTERING_PUSH_ID.ordinal()] = 10;
            $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.PUSH_REGISTRATION_FAILED.ordinal()] = 11;
            try {
                $SwitchMap$com$samsung$android$fotaagent$register$RegisterState[RegisterState.REGISTRATION_COMPLETE.ordinal()] = 12;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private void initDevice(final RegisterType registerType) {
        DeviceType.get().setDefaultSettings(this);
        if (!AccessoryController.getInstance().getConnectionController().isConnected()) {
            Log.W("Device connection is not ready");
            sendNextState(registerType, RegisterState.CONSUMER_CONNECTION_FAILED);
        } else if (AccessoryController.getInstance().getRequestController().isInProgress()) {
            Log.W("Accessory is in progress");
        } else {
            AccessoryController.getInstance().getRequestController().initializeDeviceInfo(new RequestController.RequestCallback.Result() {
                public void onSuccessAction(ConsumerInfo consumerInfo) {
                    Log.I("initializeDeviceInfo : succeeded by " + registerType);
                    new AccessoryInfoAdapter().updateAccessoryDB(consumerInfo.getAccessoryInfo());
                    DeviceType.reloadDeviceType();
                    XDBRegistrationAdp.setDeviceRegistrationStatus(2);
                    FotaRegisterJobIntentService.this.sendNextState(registerType, RegisterState.CHECK_NEXT_STATE);
                }

                public void onFailure(RequestError requestError) {
                    Log.I("initializeDeviceInfo : failed by " + registerType);
                    FotaRegisterJobIntentService.this.sendNextState(registerType, RegisterState.CONSUMER_CONNECTION_FAILED);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: registerDevice */
    public void lambda$handleState$0$FotaRegisterJobIntentService(RegisterType registerType) {
        Log.I("registerType: " + registerType);
        if (FotaProviderState.isDeviceRegisteredDB()) {
            Log.W("duplicated request to register, so skip and keep going previous request");
            return;
        }
        XDMDmUtils.getInstance().xdmRegisterFactoryBootstrap();
        NetworkResult execute = new NetConnect().execute(this, new DeviceRegistrationAction());
        if (execute.isSuccess()) {
            Log.I("Receive result: success in DeviceRegistrationAction by " + registerType);
            XDBRegistrationAdp.setDeviceRegistrationStatus(1);
            sendNextState(registerType, RegisterState.DEVICE_REGISTRATION_SUCCESS);
            return;
        }
        Log.W("Receive result: fail in DeviceRegistrationAction by " + registerType);
        sendNextState(registerType, RegisterState.DEVICE_REGISTRATION_FAILED, execute.getErrorType());
    }

    private void registeredDevice(RegisterType registerType) {
        requestInit(registerType);
        sendNextState(registerType, RegisterState.REGISTERING_POLLING);
    }

    private void requestInit(RegisterType registerType) {
        Log.I("");
        XDMSecReceiverApiCall.getInstance().xdmDeviceRegistration(registerType == RegisterType.FOREGROUND ? 1 : 2);
    }

    private void registerPolling(RegisterType registerType) {
        if (DeviceType.get().isPollingSupported()) {
            if (!Polling.isPassedPollingTime()) {
                Log.I("Register polling time");
                Polling.calculateNextPollingTime();
                Polling.startPollingTimer(this);
            }
        } else if (XDBPollingAdp.xdbGetNextPollingTime() != 0) {
            Log.I("Unregister polling time, change polling time to zero");
            XDBPollingAdp.xdbSetNextPollingTime(0);
            Polling.stopPollingTimer(this);
        }
        sendNextState(registerType, RegisterState.REGISTERING_PUSH);
    }

    private void registerPush(RegisterType registerType) {
        if (OperatorUtil.isSPP()) {
            registerSPP(registerType);
        } else if (!GeneralUtil.isGSFPackagedInstalled(getApplicationContext())) {
            Log.W("GSF package is not installed. cannot support FCM");
        } else {
            registerFCM(registerType);
        }
    }

    private void registerSPP(RegisterType registerType) {
        SPP spp = SPP.getSPP();
        spp.setSPPReceiver(new SPPResultReceiver(registerType) {
            private final /* synthetic */ RegisterType f$1;

            {
                this.f$1 = r2;
            }

            public final void onSPPResponse(SPPResult sPPResult) {
                FotaRegisterJobIntentService.this.lambda$registerSPP$1$FotaRegisterJobIntentService(this.f$1, sPPResult);
            }
        });
        spp.requestID(this);
    }

    public /* synthetic */ void lambda$registerSPP$1$FotaRegisterJobIntentService(RegisterType registerType, SPPResult sPPResult) {
        if (sPPResult != null) {
            try {
                if (sPPResult.isSuccess()) {
                    Log.I("Receive result: success in SPP requestID");
                    Log.H("spp id" + sPPResult.getPushID());
                    sendRegisterPushIdState(registerType, "", sPPResult.getPushID());
                    return;
                }
                Log.W("Receive result: fail in SPP error: " + sPPResult.getError());
            } finally {
                SPP.getSPP().setSPPReceiver((SPPResultReceiver) null);
            }
        }
        sendNextState(registerType, RegisterState.PUSH_REGISTRATION_FAILED);
        SPP.getSPP().setSPPReceiver((SPPResultReceiver) null);
    }

    private void registerFCM(RegisterType registerType) {
        FCMResult registrationIDByBackground = FCM.instance.getRegistrationIDByBackground(FotaProviderInitializer.getContext());
        if (registrationIDByBackground != null) {
            if (registrationIDByBackground.isSuccess()) {
                Log.I("Receive result: success in FCM requestID");
                Log.H("fcm id:" + registrationIDByBackground.getPushID());
                sendRegisterPushIdState(registerType, registrationIDByBackground.getPushID(), "");
                return;
            }
            registrationIDByBackground.setNextRetry();
            Log.W("Receive result: fail in FCM error: " + registrationIDByBackground.getErrorMsg());
        }
        sendNextState(registerType, RegisterState.PUSH_REGISTRATION_FAILED);
    }

    private void registerPushID(RegisterType registerType, String str, String str2) {
        NetworkResult execute = new NetConnect().execute(this, new PushRegistrationAction(str, str2));
        if (execute.isSuccess()) {
            if (OperatorUtil.isSPP()) {
                Log.I("Receive result: success SPP in PushRegistrationAction by " + registerType);
                XDBRegistrationAdp.setPushRegistrationStatus(1);
            } else {
                Log.I("Receive result: success FCM in PushRegistrationAction by " + registerType);
                XDBRegistrationAdp.setPushRegistrationStatus(2);
            }
            sendNextState(registerType, RegisterState.REGISTRATION_COMPLETE);
            return;
        }
        if (execute.getErrorType() == 440) {
            FotaProviderState.resetDataAndStopAlarms(this);
        }
        Log.I("Receive result: fail in PushRegistrationAction by " + registerType);
        sendNextState(registerType, RegisterState.PUSH_REGISTRATION_FAILED);
    }

    private boolean isUiMode(RegisterType registerType) {
        if (registerType != RegisterType.FOREGROUND) {
            return false;
        }
        if (DialogActivity.getUIHandler() != null) {
            return true;
        }
        Log.W("Ui is not available to show dialog activity");
        return false;
    }
}
