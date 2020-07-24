package com.samsung.accessory.neobeanmgr.core.service;

import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.accessorydm.eng.core.XDMWbxml;
import com.accessorydm.interfaces.XDBInterface;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.uhm.BaseContentProvider;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.common.util.BluetoothUtil;
import com.samsung.accessory.neobeanmgr.common.util.BroadcastReceiverUtil;
import com.samsung.accessory.neobeanmgr.common.util.ByteUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.common.util.WorkerHandler;
import com.samsung.accessory.neobeanmgr.common.util.WorkerTask;
import com.samsung.accessory.neobeanmgr.core.EarBudsFotaInfo;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.bluetooth.BluetoothManager;
import com.samsung.accessory.neobeanmgr.core.fmm.receiver.FmmDeviceStatusReceiver;
import com.samsung.accessory.neobeanmgr.core.fota.util.FotaUtil;
import com.samsung.accessory.neobeanmgr.core.gamemode.GameModeManager;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationUtil;
import com.samsung.accessory.neobeanmgr.core.service.SppConnectionManager;
import com.samsung.accessory.neobeanmgr.core.service.message.Msg;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgCheckTheFitOfEarbudsResult;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgDebugData;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgDebugSKU;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgDebugSerialNumber;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgExtendedStatusUpdated;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgFotaDeviceInfoSwVersion;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgFotaEmergency;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgGetFmmConfig;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgManagerInfo;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgMuteEarbudStatusUpdated;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgNoiseReductionModeUpdated;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgReset;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetFmmConfig;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetInBandRingtone;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetTouchpadOption;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgStatusUpdated;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgTouchPadOther;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgTouchUpdated;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgUpdateTime;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgVersionInfo;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgVoiceWakeUpListeningStatus;
import com.samsung.accessory.neobeanmgr.module.mainmenu.TouchpadActivity;
import com.samsung.android.fotaagent.update.UpdateInterface;
import com.samsung.context.sdk.samsunganalytics.ErrorType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class CoreService implements GameModeManager.SupportService {
    public static final String ACTION_DEVICE_CONNECTED = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_DEVICE_CONNECTED";
    public static final String ACTION_DEVICE_CONNECTING = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_DEVICE_CONNECTING";
    public static final String ACTION_DEVICE_DISCONNECTED = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_DEVICE_DISCONNECTED";
    public static final String ACTION_DEVICE_DISCONNECTING = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_DEVICE_DISCONNECTING";
    public static final String ACTION_DEVICE_EXTENDED_STATUS_READY = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_DEVICE_EXTENDED_STATUS_READY";
    public static final String ACTION_MSG_FOTA_CHECK_UPDATE = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_FOTA_CHECK_UPDATE";
    public static final String ACTION_MSG_ID_CALL_STATE = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_CALL_STATE";
    public static final String ACTION_MSG_ID_CHECK_THE_FIT_OF_EARBUDS_RESULT = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_CHECK_THE_FIT_OF_EARBUDS_RESULT";
    public static final String ACTION_MSG_ID_DEBUG_GET_ALL_DATA = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_DEBUG_GET_ALL_DATA";
    public static final String ACTION_MSG_ID_DEBUG_SERIAL_NUMBER = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_DEBUG_SERIAL_NUMBER";
    public static final String ACTION_MSG_ID_EQUALIZER_TYPE_UPDATED = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_EQUALIZER_TYPE_UPDATED";
    public static final String ACTION_MSG_ID_EXTENDED_STATUS_UPDATED = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_EXTENDED_STATUS_UPDATED";
    public static final String ACTION_MSG_ID_FIND_MY_EARBUDS_STATUS_UPDATED = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_FIND_MY_EARBUDS_STATUS_UPDATED";
    public static final String ACTION_MSG_ID_FIND_MY_EARBUDS_STOP = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_FIND_MY_EARBUDS_STOP";
    public static final String ACTION_MSG_ID_FOTA_EMERGENCY = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_FOTA_EMERGENCY";
    public static final String ACTION_MSG_ID_GET_FMM_CONFIG_RESP = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_GET_FMM_CONFIG_RESP";
    public static final String ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED = "com.samsung.accessory.popcornmgr.core.service.CoreService.ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED";
    public static final String ACTION_MSG_ID_NOISE_REDUCTION_UPDATED = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_NOISE_REDUCTION_UPDATED";
    public static final String ACTION_MSG_ID_RESET = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_RESET";
    public static final String ACTION_MSG_ID_SCO_STATE_UPDATED = "com.samsung.accessory.popcornmgr.core.service.CoreService.ACTION_MSG_ID_SCO_STATE_UPDATED";
    public static final String ACTION_MSG_ID_SET_FMM_CONFIG_RESULT = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_SET_FMM_CONFIG_RESULT";
    public static final String ACTION_MSG_ID_STATUS_UPDATED = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_STATUS_UPDATED";
    public static final String ACTION_MSG_ID_VOICE_WAKE_UP_EVENT = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_VOICE_WAKE_UP_EVENT";
    public static final String ACTION_MUTE_EARBUD_STATUS_UPDATED = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MUTE_EARBUD_STATUS_UPDATED";
    private static final String TAG = "NeoBean_CoreService";
    final String FOTA_BADGECOUNT = ("com.sec.android.fotaprovider.FOTA_BADGECOUNT_" + Application.getContext().getPackageName());
    final String FOTA_CHECKED_DATE_UPDATE = ("com.sec.android.fotaprovider.FOTA_CHECKED_DATE_UPDATE_" + Application.getContext().getPackageName());
    final String LAST_UPDATE_INFO = ("com.sec.android.fotaprovider.LAST_UPDATE_INFO_" + Application.getContext().getPackageName());
    final String UPDATE_IN_PROGRESS = ("com.sec.android.fotaprovider.UPDATE_IN_PROGRESS_" + Application.getContext().getPackageName());
    private BroadcastReceiverUtil.Receiver mBluetoothReceiver = new BroadcastReceiverUtil.Receiver() {
        public void setIntentFilter(IntentFilter intentFilter) {
            intentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
            intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        }

        public void onReceive(Context context, final Intent intent) {
            Log.d(CoreService.TAG, "onReceive() : " + intent.getAction());
            CoreService.this.mWorker.post(new WorkerTask() {
                /* JADX WARNING: Removed duplicated region for block: B:17:0x004f  */
                /* JADX WARNING: Removed duplicated region for block: B:32:0x0111  */
                public void execute() {
                    char c;
                    if (!Application.getBluetoothManager().isReady()) {
                        Log.w(this.TAG, "onReceive() : BluetoothManager().isReady() == false");
                        return;
                    }
                    String action = intent.getAction();
                    int hashCode = action.hashCode();
                    if (hashCode != 545516589) {
                        if (hashCode == 1244161670 && action.equals("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED")) {
                            c = 1;
                            if (c == 0) {
                                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                                int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
                                int intExtra2 = intent.getIntExtra("android.bluetooth.profile.extra.PREVIOUS_STATE", -1);
                                String str = this.TAG;
                                Log.d(str, "BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()) + " " + bluetoothDevice.getName());
                                String str2 = this.TAG;
                                Log.d(str2, "BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED : " + BluetoothUtil.stateToString(intExtra) + " (from " + BluetoothUtil.stateToString(intExtra2) + ")");
                                if (!CoreService.isPluginDevice(bluetoothDevice)) {
                                    Log.d(this.TAG, "galaxy buds+ name is not match");
                                    return;
                                } else if (intExtra != 0) {
                                    if (intExtra == 1) {
                                        CoreService.this.onHeadsetConnecting(bluetoothDevice);
                                        return;
                                    } else if (intExtra == 2) {
                                        CoreService.this.onHeadsetConnected(bluetoothDevice);
                                        return;
                                    } else {
                                        return;
                                    }
                                } else if (CoreService.this.mDeviceLogManager.getIsDeviceLogExtrationWorking() || FotaUtil.getFOTAProcessIsRunning()) {
                                    String str3 = this.TAG;
                                    Log.w(str3, "BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED : " + ": skipped");
                                    return;
                                } else {
                                    CoreService.this.checkHeadsetA2dpDisconnected(bluetoothDevice);
                                    return;
                                }
                            } else if (c == 1) {
                                BluetoothDevice bluetoothDevice2 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                                int intExtra3 = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
                                int intExtra4 = intent.getIntExtra("android.bluetooth.profile.extra.PREVIOUS_STATE", -1);
                                String str4 = this.TAG;
                                Log.d(str4, "BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED : " + BluetoothUtil.privateAddress(bluetoothDevice2.getAddress()) + " " + bluetoothDevice2.getName());
                                String str5 = this.TAG;
                                Log.d(str5, "BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED : " + BluetoothUtil.stateToString(intExtra3) + " (from " + BluetoothUtil.stateToString(intExtra4) + ")");
                                if (!CoreService.isPluginDevice(bluetoothDevice2)) {
                                    Log.d(this.TAG, "galaxy buds+ name is not match");
                                    return;
                                } else if (intExtra3 != 0) {
                                    if (intExtra3 == 1) {
                                        CoreService.this.onA2dpConnecting(bluetoothDevice2);
                                        return;
                                    } else if (intExtra3 == 2) {
                                        CoreService.this.onA2dpConnected(bluetoothDevice2);
                                        return;
                                    } else {
                                        return;
                                    }
                                } else if (CoreService.this.mDeviceLogManager.getIsDeviceLogExtrationWorking() || FotaUtil.getFOTAProcessIsRunning()) {
                                    String str6 = this.TAG;
                                    Log.w(str6, "BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED : " + "skipped");
                                    return;
                                } else {
                                    CoreService.this.checkHeadsetA2dpDisconnected(bluetoothDevice2);
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                    } else if (action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                        c = 0;
                        if (c == 0) {
                        }
                    }
                    c = 65535;
                    if (c == 0) {
                    }
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public BluetoothDevice mConnectedDevice = null;
    private int mConnectionState = 0;
    /* access modifiers changed from: private */
    public Context mContext;
    private boolean mCurrentCoupledStatus = false;
    /* access modifiers changed from: private */
    public DeviceLogManager mDeviceLogManager = null;
    private EarBudsInfo mEarBudsInfo = new EarBudsInfo();
    private EarBudsUsageReporter mEarBudsUsageReporter = null;
    private boolean mEmulatingConnected = false;
    /* access modifiers changed from: private */
    public boolean mExtendedStatusReady = false;
    private FotaTransferManager mFOTATransferManager;
    private FmmDeviceStatusReceiver mFmmDeviceStatusReceiver = null;
    private EarBudsFotaInfo mFotaInfo = new EarBudsFotaInfo();
    private BroadcastReceiverUtil.Receiver mFotaReceiver = new BroadcastReceiverUtil.Receiver() {
        public void setIntentFilter(IntentFilter intentFilter) {
            intentFilter.addAction(CoreService.this.FOTA_CHECKED_DATE_UPDATE);
            intentFilter.addAction(CoreService.this.UPDATE_IN_PROGRESS);
            intentFilter.addAction(CoreService.this.FOTA_BADGECOUNT);
            intentFilter.addAction(CoreService.this.LAST_UPDATE_INFO);
        }

        public void onReceive(Context context, final Intent intent) {
            Log.d(CoreService.TAG, "onReceive() : " + intent.getAction());
            CoreService.this.mWorker.post(new WorkerTask() {
                public void execute() {
                    if (CoreService.this.FOTA_CHECKED_DATE_UPDATE.equals(intent.getAction())) {
                        Log.d(this.TAG, "FOTA_CHECKED_DATE_UPDATE");
                        long longExtra = intent.getLongExtra(XDBInterface.XDM_SQL_DB_POLLING_TIME, -1);
                        String str = this.TAG;
                        Log.d(str, "FOTA_CHECKED_DATE_UPDATE : time :" + longExtra);
                        FotaUtil.setLastSWVersionCheckTime(longExtra);
                    } else if (CoreService.this.UPDATE_IN_PROGRESS.equals(intent.getAction())) {
                        Log.d(this.TAG, "UPDATE_IN_PROGRESS");
                    } else if (CoreService.this.FOTA_BADGECOUNT.equals(intent.getAction())) {
                        Log.d(this.TAG, "FOTA_BADGECOUNT");
                        int intExtra = intent.getIntExtra("badge_count", -1);
                        String str2 = this.TAG;
                        Log.d(str2, "FOTA_BADGECOUNT : badge_count : " + intExtra);
                        boolean z = true;
                        if (intExtra != 1) {
                            z = false;
                        }
                        FotaUtil.setCheckFotaUpdate(z);
                    } else if (CoreService.this.LAST_UPDATE_INFO.equals(intent.getAction())) {
                        Log.d(this.TAG, "LAST_UPDATE_INFO");
                    }
                }
            });
        }
    };
    private GameModeManager mGameModeManager = null;
    private BroadcastReceiverUtil.Receiver mReceiver = new BroadcastReceiverUtil.Receiver() {
        public void setIntentFilter(IntentFilter intentFilter) {
            intentFilter.addAction(BluetoothManager.ACTION_READY);
            intentFilter.addAction(BluetoothManager.ACTION_STOPPED);
        }

        public void onReceive(Context context, final Intent intent) {
            Log.d(CoreService.TAG, "onReceive() : " + intent.getAction());
            CoreService.this.mWorker.post(new WorkerTask() {
                /* JADX WARNING: Removed duplicated region for block: B:12:0x002d  */
                /* JADX WARNING: Removed duplicated region for block: B:14:0x003f  */
                public void execute() {
                    char c;
                    String action = intent.getAction();
                    int hashCode = action.hashCode();
                    if (hashCode != 851625861) {
                        if (hashCode == 1912349019 && action.equals(BluetoothManager.ACTION_READY)) {
                            c = 0;
                            if (c == 0) {
                                Log.d(this.TAG, "BluetoothManager.ACTION_READY");
                                CoreService.this.onBluetoothManagerReady();
                                return;
                            } else if (c == 1) {
                                Log.d(this.TAG, "BluetoothManager.ACTION_STOPPED");
                                CoreService.this.onBluetoothManagerStop();
                                return;
                            } else {
                                return;
                            }
                        }
                    } else if (action.equals(BluetoothManager.ACTION_STOPPED)) {
                        c = 1;
                        if (c == 0) {
                        }
                    }
                    c = 65535;
                    if (c == 0) {
                    }
                }
            });
        }
    };
    private ResponseTimerHandler mResponseTimerHandler;
    private BroadcastReceiverUtil.Receiver mScoUpdateBroadcastReceiver = new BroadcastReceiverUtil.Receiver() {
        public void setIntentFilter(IntentFilter intentFilter) {
            intentFilter.addAction("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED");
        }

        public void onReceive(Context context, final Intent intent) {
            CoreService.this.mWorker.post(new WorkerTask() {
                public void execute() {
                    String action = intent.getAction();
                    if (((action.hashCode() == -1435586571 && action.equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) ? (char) 0 : 65535) == 0) {
                        int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                        String str = this.TAG;
                        Log.d(str, "ACTION_AUDIO_STATE_CHANGED  currentState : " + intExtra);
                        if (intExtra == 10 || intExtra == 12) {
                            Util.sendPermissionBroadcast(CoreService.this.mContext, new Intent(CoreService.ACTION_MSG_ID_SCO_STATE_UPDATED));
                        }
                    }
                }
            });
        }
    };
    private final SppConnectionManager.Callback mSppConnectionCallback = new SppConnectionManager.Callback() {
        private final String TAG = "NeoBean_CoreService_SppCallback";

        public void onMessage(BluetoothDevice bluetoothDevice, Msg msg) {
            CoreService.this.onSppMessage(msg);
        }

        public void onConnectionStateChanged(final BluetoothDevice bluetoothDevice, final int i) {
            Log.d("NeoBean_CoreService_SppCallback", "onConnectionStateChanged() : " + BluetoothUtil.stateToString(i));
            CoreService.this.mWorker.post(new WorkerTask() {
                public void execute() {
                    String str = this.TAG;
                    Log.d(str, "onConnectionStateChanged() : " + BluetoothUtil.stateToString(i));
                    if (i == 2) {
                        BluetoothManager bluetoothManager = Application.getBluetoothManager();
                        synchronized (bluetoothManager) {
                            if (bluetoothManager.isReady() && !bluetoothManager.isHeadsetConnecting(bluetoothDevice) && !bluetoothManager.isA2dpConnecting(bluetoothDevice)) {
                                Log.w(this.TAG, "onConnected() : force profile connect");
                                bluetoothManager.connectHeadset(bluetoothDevice);
                                bluetoothManager.connectA2dp(bluetoothDevice);
                            }
                        }
                    }
                    CoreService.this.updateConnectionState(bluetoothDevice, i);
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public SppConnectionManager mSppConnectionManager = null;
    private Set<OnSppMessageListener> mSppMessageListener = new HashSet();
    /* access modifiers changed from: private */
    public WorkerHandler mWorker;

    public interface OnSppMessageListener {
        void onSppMessage(Msg msg);
    }

    public CoreService(Context context) {
        this.mContext = context;
        onCreate();
    }

    private void onCreate() {
        Log.d(TAG, "onCreate()");
        this.mWorker = WorkerHandler.createWorkerHandler("core_service_worker@" + this);
        this.mResponseTimerHandler = new ResponseTimerHandler();
        this.mSppConnectionManager = new SppConnectionManager(this.mSppConnectionCallback);
        this.mFOTATransferManager = new FotaTransferManager(this);
        this.mFmmDeviceStatusReceiver = new FmmDeviceStatusReceiver(this.mContext);
        BroadcastReceiverUtil.register(this.mContext, this.mReceiver);
        BroadcastReceiverUtil.register(this.mContext, this.mScoUpdateBroadcastReceiver);
        BroadcastReceiverUtil.register(this.mContext, this.mFotaReceiver);
        this.mDeviceLogManager = new DeviceLogManager(this);
        this.mGameModeManager = new GameModeManager(this);
        this.mEarBudsUsageReporter = new EarBudsUsageReporter(this);
        if (Application.getBluetoothManager().isReady()) {
            this.mWorker.post(new WorkerTask() {
                public void execute() {
                    CoreService.this.onBluetoothManagerReady();
                }
            });
        }
        Log.d(TAG, "onCreate()_end");
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        this.mWorker.quit();
        this.mResponseTimerHandler.removeCallbacksAndMessages((Object) null);
        BroadcastReceiverUtil.unregister(this.mContext, this.mBluetoothReceiver);
        BroadcastReceiverUtil.unregister(this.mContext, this.mReceiver);
        BroadcastReceiverUtil.unregister(this.mContext, this.mScoUpdateBroadcastReceiver);
        BroadcastReceiverUtil.unregister(this.mContext, this.mFotaReceiver);
        unregisterSppMessageListener((OnSppMessageListener) null);
        this.mFOTATransferManager.destroy();
        this.mSppConnectionManager.destroy();
        this.mDeviceLogManager.destroy();
        this.mGameModeManager.destroy();
        this.mEarBudsUsageReporter.destroy();
        this.mFmmDeviceStatusReceiver.onDestroy();
        Log.d(TAG, "onDestroy()_end");
    }

    /* access modifiers changed from: private */
    public void onBluetoothManagerReady() {
        Log.d(TAG, "onBluetoothManagerReady()");
        BroadcastReceiverUtil.register(this.mContext, this.mBluetoothReceiver);
        BluetoothDevice bondedDevice = BluetoothUtil.getBondedDevice(getLastLaunchDeviceAddress());
        if (bondedDevice == null) {
            return;
        }
        if ((Application.getBluetoothManager().getHeadsetState(bondedDevice) == 2 || Application.getBluetoothManager().getA2dpState(bondedDevice) == 2) && this.mSppConnectionManager.getConnectionState() == 0) {
            connectSppByProfile(bondedDevice);
        }
    }

    /* access modifiers changed from: private */
    public void onBluetoothManagerStop() {
        Log.d(TAG, "onBluetoothManagerStop()");
        BroadcastReceiverUtil.unregister(this.mContext, this.mBluetoothReceiver);
    }

    private void onConnecting(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "onConnecting()... : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_DEVICE_CONNECTING));
    }

    public void emulateConnected() {
        Log.d(TAG, "emulateConnected()");
        this.mEmulatingConnected = true;
        EarBudsInfo earBudsInfo = this.mEarBudsInfo;
        earBudsInfo.address = "00:00:00:00:00:00";
        earBudsInfo.coupled = true;
        earBudsInfo.batteryL = 90;
        earBudsInfo.batteryR = 90;
        earBudsInfo.wearingL = true;
        earBudsInfo.wearingR = true;
        Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_DEVICE_CONNECTED));
    }

    private void onConnected(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "onConnected() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        Preferences.putBoolean(PreferenceKey.HOME_DISCONNECTED_BY_USER, false);
        this.mCurrentCoupledStatus = false;
        synchronized (this) {
            this.mConnectedDevice = bluetoothDevice;
            this.mEarBudsInfo.address = bluetoothDevice.getAddress();
            EarBudsFotaInfo earBudsFotaInfo = this.mFotaInfo;
            earBudsFotaInfo.deviceId = "TWID:" + this.mEarBudsInfo.address.replace(":", "");
        }
        Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_DEVICE_CONNECTED));
        long currentTimeMillis = System.currentTimeMillis();
        sendSppMessage(new MsgUpdateTime(currentTimeMillis, TimeZone.getDefault().getOffset(currentTimeMillis)));
        sendSppMessage(new MsgDebugData());
        sendSppMessage(new MsgDebugSKU());
        this.mResponseTimerHandler.retry = 0;
        sendRequestSerialNumber();
    }

    private void emulateDisconnected() {
        Log.d(TAG, "emulateDisconnected()");
        this.mEmulatingConnected = false;
        Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_DEVICE_DISCONNECTED));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: java.lang.String} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    public synchronized void onSppMessage(Msg msg) {
        Log.d(TAG, "onSppMessage() : " + ByteUtil.toHexString(msg.id));
        boolean z = true;
        int i = 0;
        switch (msg.id) {
            case -118:
                Log.d(TAG, "MsgID.SET_IN_BAND_RINGTONE");
                MsgSetInBandRingtone msgSetInBandRingtone = (MsgSetInBandRingtone) msg;
                if (msgSetInBandRingtone.status != 1) {
                    if (msgSetInBandRingtone.status == 0) {
                        NotificationUtil.setInBandRingtone(false);
                        break;
                    }
                } else {
                    NotificationUtil.setInBandRingtone(true);
                    break;
                }
                break;
            case -111:
                Log.d(TAG, "MsgID.TOUCH_UPDATED");
                EarBudsInfo earBudsInfo = this.mEarBudsInfo;
                if (((MsgTouchUpdated) msg).status != 1) {
                    z = false;
                }
                earBudsInfo.touchpadLocked = z;
                SamsungAnalyticsUtil.setStatusString(SA.Status.LOCK_TOUCHPAD, this.mEarBudsInfo.touchpadLocked ? "1" : "0");
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED));
                break;
            case -109:
                Log.d(TAG, "MsgID.TOUCHPAD_OTHER_OPTION");
                MsgTouchPadOther msgTouchPadOther = (MsgTouchPadOther) msg;
                if (msgTouchPadOther.touchpadOtherOptionValue != 4 || !TouchpadActivity.isReadySpotify()) {
                    if (msgTouchPadOther.touchpadOtherOptionValue > 4) {
                        String str = "none";
                        String str2 = "none";
                        String string = Preferences.getString(msgTouchPadOther.touchpadOtherOptionValue == 5 ? PreferenceKey.LEFT_OTHER_OPTION_PACKAGE_NAME : PreferenceKey.RIGHT_OTHER_OPTION_PACKAGE_NAME, "", UhmFwUtil.getLastLaunchDeviceId());
                        if (!string.equals("")) {
                            ArrayList<HashMap<String, String>> checkApp2App = checkApp2App();
                            if (checkApp2App.size() > 0) {
                                while (true) {
                                    if (i < checkApp2App.size()) {
                                        if (string.equals((String) checkApp2App.get(i).get(BaseContentProvider.PACKAGE_NAME))) {
                                            str = (String) checkApp2App.get(i).get("menu_name");
                                            str2 = checkApp2App.get(i).get("description");
                                        } else {
                                            i++;
                                        }
                                    }
                                }
                                Intent intent = new Intent(Util.SEND_PUI_EVENT);
                                intent.setPackage(string);
                                intent.putExtra("menu_name", str);
                                intent.putExtra("description", str2);
                                this.mContext.sendBroadcast(intent);
                                SamsungAnalyticsUtil.sendEvent(SA.Event.TAP_AND_HOLD_OTHERS_APPS, (String) null, SamsungAnalyticsUtil.touchPadTapAndHoldOthersPkgNameToDetail(string));
                                break;
                            }
                        } else {
                            this.mEarBudsInfo.touchpadOptionLeft = 2;
                            this.mEarBudsInfo.touchpadOptionRight = 2;
                            Application.getCoreService().sendSppMessage(new MsgSetTouchpadOption((byte) this.mEarBudsInfo.touchpadOptionLeft, (byte) this.mEarBudsInfo.touchpadOptionRight));
                            break;
                        }
                    }
                } else {
                    Intent intent2 = new Intent("com.spotify.music.features.spoton.ACTION_PLAY_SPOTIFY");
                    intent2.setClassName(Util.SPOTIFY, "com.spotify.music.features.spoton.receiver.SpotOnReceiver");
                    intent2.putExtra("com.spotify.music.features.spoton.extras.CLIENT_ID", "1ba94f7ca71e428085112fd877ea8c14");
                    intent2.putExtra("com.spotify.music.features.spoton.extras.PENDING_INTENT", PendingIntent.getBroadcast(this.mContext, 0, intent2, 0));
                    this.mContext.sendBroadcast(intent2);
                    SamsungAnalyticsUtil.sendEvent(SA.Event.TAP_AND_HOLD_OTHERS_APPS, (String) null, "a");
                    break;
                }
                break;
            case -102:
                Log.d(TAG, "MsgID.VOICE_WAKE_UP_EVENT");
                sendSppMessage(new MsgSimple(MsgID.VOICE_WAKE_UP_EVENT, true, (byte) 0));
                Application.getAomManager().startBixby();
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_VOICE_WAKE_UP_EVENT));
                break;
            case -101:
                Log.d(TAG, "MsgID.NOISE_REDUCTION_MODE_UPDATE");
                this.mEarBudsInfo.noiseReduction = ((MsgNoiseReductionModeUpdated) msg).noiseReduction;
                SamsungAnalyticsUtil.setStatusString(SA.Status.ANC, this.mEarBudsInfo.noiseReduction ? "1" : "0");
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_NOISE_REDUCTION_UPDATED));
                break;
            case ErrorType.ERROR_UNKNOWN /*-100*/:
                Log.d(TAG, "MsgID.VOICE_WAKE_UP_LISTENING_STATUS");
                Application.getAomManager().setBixbyMic(((MsgVoiceWakeUpListeningStatus) msg).voiceWakeUpListeningStatus);
                break;
            case -98:
                Log.d(TAG, "MsgID.CHECK_THE_FIT_OF_EARBUDS_REUSLT");
                MsgCheckTheFitOfEarbudsResult msgCheckTheFitOfEarbudsResult = (MsgCheckTheFitOfEarbudsResult) msg;
                this.mEarBudsInfo.leftCheckTheFitResult = msgCheckTheFitOfEarbudsResult.leftResult;
                this.mEarBudsInfo.rightCheckTheFitResult = msgCheckTheFitOfEarbudsResult.rightResult;
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_CHECK_THE_FIT_OF_EARBUDS_RESULT));
                break;
            case -95:
                Log.d(TAG, "MsgID.FIND_MY_EARBUDS_STOP");
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_FIND_MY_EARBUDS_STOP));
                break;
            case -93:
                Log.d(TAG, "MsgID.MUTE_EARBUD_STATUS_UPDATED");
                MsgMuteEarbudStatusUpdated msgMuteEarbudStatusUpdated = (MsgMuteEarbudStatusUpdated) msg;
                this.mEarBudsInfo.leftMuteStatus = msgMuteEarbudStatusUpdated.leftStatus;
                this.mEarBudsInfo.rightMuteStatus = msgMuteEarbudStatusUpdated.rightStatus;
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MUTE_EARBUD_STATUS_UPDATED));
                break;
            case -91:
                Log.d(TAG, "MsgID.VOICE_NOTI_STOP");
                Application.getNotificationCoreService().getNotificationTTSCore().stopTTS(false);
                break;
            case -84:
                Log.d(TAG, "MsgID.SET_FMM_CONFIG");
                this.mEarBudsInfo.resetOfSetFmmConfig = ((MsgSetFmmConfig) msg).result;
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_SET_FMM_CONFIG_RESULT));
                break;
            case -83:
                Log.d(TAG, "MsgID.GET_FMM_CONFIG");
                Intent intent3 = new Intent(ACTION_MSG_ID_GET_FMM_CONFIG_RESP);
                intent3.putExtra("getFmmConfig", ((MsgGetFmmConfig) msg).getFmmConfig);
                Util.sendPermissionBroadcast(this.mContext, intent3);
                break;
            case -76:
                Log.d(TAG, "MsgID.FOTA_DEVICE_INFO_SW_VERSION");
                BudsLogManager.sendLog(6, ((MsgFotaDeviceInfoSwVersion) msg).version);
                break;
            case -70:
                Log.d(TAG, "MsgID.FOTA_EMERGENCY");
                Log.d(TAG, "Reason : " + ((MsgFotaEmergency) msg).mReason);
                FotaUtil.setEmergencyFOTAIsRunning(true);
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_FOTA_EMERGENCY));
                sendSppMessage(new MsgFotaEmergency(MsgID.FOTA_EMERGENCY, true, (byte) 0));
                break;
            case 34:
                Log.d(TAG, "MsgID.DEBUG_SKU");
                MsgDebugSKU msgDebugSKU = (MsgDebugSKU) msg;
                this.mEarBudsInfo.sku_left = msgDebugSKU.LeftSKU;
                this.mEarBudsInfo.sku_right = msgDebugSKU.RightSKU;
                if (this.mEarBudsInfo.sku_left != null || this.mEarBudsInfo.sku_right != null) {
                    if (this.mEarBudsInfo.sku_left != null || this.mEarBudsInfo.sku_right == null) {
                        if (this.mEarBudsInfo.sku_left != null && this.mEarBudsInfo.sku_right == null) {
                            this.mFotaInfo.salesCode = this.mEarBudsInfo.sku_left.substring(this.mEarBudsInfo.sku_left.length() - 3, this.mEarBudsInfo.sku_left.length());
                            break;
                        } else {
                            this.mFotaInfo.salesCode = this.mEarBudsInfo.sku_right.substring(this.mEarBudsInfo.sku_right.length() - 3, this.mEarBudsInfo.sku_right.length());
                            break;
                        }
                    } else {
                        this.mFotaInfo.salesCode = this.mEarBudsInfo.sku_right.substring(this.mEarBudsInfo.sku_right.length() - 3, this.mEarBudsInfo.sku_right.length());
                        break;
                    }
                } else {
                    this.mFotaInfo.salesCode = "";
                    break;
                }
                break;
            case 38:
                Log.d(TAG, "MsgID.DEBUG_ALL_DATA");
                this.mEarBudsInfo.debugInfo = ((MsgDebugData) msg).debugdata;
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_DEBUG_GET_ALL_DATA));
                break;
            case 41:
                Log.d(TAG, "MsgID.DEBUG_SERIAL_NUMBER");
                if (this.mResponseTimerHandler != null) {
                    this.mResponseTimerHandler.removeCallbacksAndMessages((Object) null);
                }
                this.mResponseTimerHandler.retry = 0;
                MsgDebugSerialNumber msgDebugSerialNumber = (MsgDebugSerialNumber) msg;
                this.mEarBudsInfo.serialNumber_left = msgDebugSerialNumber.SerialNumberLeft;
                this.mEarBudsInfo.serialNumber_right = msgDebugSerialNumber.SerialNumberRight;
                if (this.mEarBudsInfo.serialNumber_left == null && this.mEarBudsInfo.serialNumber_right == null) {
                    this.mFotaInfo.serialNumber = "";
                } else if (this.mEarBudsInfo.serialNumber_left == null && this.mEarBudsInfo.serialNumber_right != null) {
                    this.mFotaInfo.serialNumber = this.mEarBudsInfo.serialNumber_right;
                } else if (this.mEarBudsInfo.serialNumber_left == null || this.mEarBudsInfo.serialNumber_right != null) {
                    this.mFotaInfo.serialNumber = this.mEarBudsInfo.serialNumber_right;
                } else {
                    this.mFotaInfo.serialNumber = this.mEarBudsInfo.serialNumber_left;
                }
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_DEBUG_SERIAL_NUMBER));
                break;
            case 80:
                Log.d(TAG, "MsgID.RESET");
                MsgReset msgReset = (MsgReset) msg;
                this.mEarBudsInfo.resultOfReset = msgReset.result;
                if (msgReset.result) {
                    onResetManager();
                }
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_RESET));
                break;
            case 96:
                Log.d(TAG, "MsgID.MSG_ID_STATUS_UPDATED");
                sendSppMessage(new MsgSimple(MsgID.STATUS_UPDATED, true, (byte) 0));
                ((MsgStatusUpdated) msg).applyTo(this.mEarBudsInfo);
                if (this.mCurrentCoupledStatus != this.mEarBudsInfo.coupled) {
                    this.mResponseTimerHandler.retry = 0;
                    sendRequestSerialNumber();
                }
                this.mCurrentCoupledStatus = this.mEarBudsInfo.coupled;
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_STATUS_UPDATED));
                break;
            case 97:
                Log.d(TAG, "MsgID.MSG_ID_EXTENDED_STATUS_UPDATED");
                sendSppMessage(new MsgSimple(MsgID.EXTENDED_STATUS_UPDATED, true, (byte) 0));
                sendSppMessage(new MsgManagerInfo());
                ((MsgExtendedStatusUpdated) msg).applyTo(this.mEarBudsInfo);
                SamsungAnalyticsUtil.setStatusString(SA.Status.LOCK_TOUCHPAD, this.mEarBudsInfo.touchpadLocked ? "1" : "0");
                SamsungAnalyticsUtil.setStatusString(SA.Status.TOUCH_AND_HOLD_LEFT, SamsungAnalyticsUtil.touchPadOptionToDetail(this.mEarBudsInfo.touchpadOptionLeft));
                SamsungAnalyticsUtil.setStatusString(SA.Status.TOUCH_AND_HOLD_RIGHT, SamsungAnalyticsUtil.touchPadOptionToDetail(this.mEarBudsInfo.touchpadOptionRight));
                SamsungAnalyticsUtil.setStatusString(SA.Status.GAME_MODE, this.mEarBudsInfo.adjustSoundSync ? "1" : "0");
                SamsungAnalyticsUtil.setStatusString(SA.Status.EQUALIZER_STATUS, SamsungAnalyticsUtil.equalizerTypeToDetail(this.mEarBudsInfo.equalizerType));
                SamsungAnalyticsUtil.setStatusString(SA.Status.USE_AMBIENT_SOUND_DURING_CALLS, this.mEarBudsInfo.sideToneStatus ? "1" : "0");
                SamsungAnalyticsUtil.setStatusString(SA.Status.DOUBLE_TAP_SIDE, this.mEarBudsInfo.outsideDoubleTap ? "1" : "0");
                SamsungAnalyticsUtil.setStatusString(SA.Status.EXTRA_HIGH_VOLUME_AMBIENT, this.mEarBudsInfo.extraHighAmbient ? "1" : "0");
                SamsungAnalyticsUtil.setStatusString(SA.Status.ANC, this.mEarBudsInfo.noiseReduction ? "1" : "0");
                SamsungAnalyticsUtil.setStatusString(SA.Status.VOICE_WAKE_UP, this.mEarBudsInfo.voiceWakeUp ? "1" : "0");
                Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_MSG_ID_EXTENDED_STATUS_UPDATED));
                this.mWorker.post(new WorkerTask() {
                    public void execute() {
                        if (!CoreService.this.mExtendedStatusReady) {
                            boolean unused = CoreService.this.mExtendedStatusReady = true;
                            CoreService.this.onExtendedStatusReady();
                        }
                    }
                });
                break;
            case 99:
                Log.d(TAG, "SppMessage.MSG_ID_VERSION_INFO");
                MsgVersionInfo msgVersionInfo = (MsgVersionInfo) msg;
                sendSppMessage(new MsgSimple(MsgID.VERSION_INFO, true, (byte) 0));
                if (msgVersionInfo.Left_SW_version.equals(msgVersionInfo.Right_SW_version) && FotaUtil.getEmergencyFOTAIsRunning()) {
                    FotaUtil.setEmergencyFOTAIsRunning(false);
                    break;
                }
        }
        for (OnSppMessageListener onSppMessage : this.mSppMessageListener) {
            onSppMessage.onSppMessage(msg);
        }
    }

    /* access modifiers changed from: private */
    public void onExtendedStatusReady() {
        Log.d(TAG, "onExtendedStatusReady()");
        Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_DEVICE_EXTENDED_STATUS_READY));
    }

    private void onDisconnecting(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "onDisconnecting()... : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_DEVICE_DISCONNECTING));
    }

    private void onDisconnected(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "onDisconnected() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        this.mConnectedDevice = null;
        this.mExtendedStatusReady = false;
        Util.sendPermissionBroadcast(this.mContext, new Intent(ACTION_DEVICE_DISCONNECTED));
    }

    /* access modifiers changed from: private */
    public void onHeadsetConnecting(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "onHeadsetConnecting() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        if (Util.equalsIgnoreCase(bluetoothDevice.getAddress(), getLastLaunchDeviceAddress()) && getConnectionState() == 0) {
            updateConnectionState(bluetoothDevice, 1);
        }
    }

    /* access modifiers changed from: private */
    public void onHeadsetConnected(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "onHeadsetConnected() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        if (Util.equalsIgnoreCase(bluetoothDevice.getAddress(), getLastLaunchDeviceAddress()) && this.mSppConnectionManager.getConnectionState() == 0) {
            connectSppByProfile(bluetoothDevice);
        }
    }

    /* access modifiers changed from: private */
    public void onA2dpConnecting(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "onA2dpConnecting() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        if (Util.equalsIgnoreCase(bluetoothDevice.getAddress(), getLastLaunchDeviceAddress()) && getConnectionState() == 0) {
            updateConnectionState(bluetoothDevice, 1);
        }
    }

    /* access modifiers changed from: private */
    public void onA2dpConnected(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "onA2dpConnected() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        if (Util.equalsIgnoreCase(bluetoothDevice.getAddress(), getLastLaunchDeviceAddress()) && this.mSppConnectionManager.getConnectionState() == 0) {
            connectSppByProfile(bluetoothDevice);
        }
    }

    private void onResetManager() {
        Log.d(TAG, "onResetManager");
        disconnectDevice();
    }

    public void connectToDevice() {
        Log.d(TAG, "connectToDevice() : " + BluetoothUtil.privateAddress(getLastLaunchDeviceAddress()));
        disconnectOtherDevice(getLastLaunchDeviceAddress());
        this.mWorker.post(new WorkerTask() {
            public void execute() {
                BluetoothDevice bondedDevice = BluetoothUtil.getBondedDevice(CoreService.this.getLastLaunchDeviceAddress());
                if (bondedDevice == null) {
                    Log.e(this.TAG, "TaskConnectToDevice() : device == null !!!");
                    return;
                }
                BluetoothManager bluetoothManager = Application.getBluetoothManager();
                synchronized (bluetoothManager) {
                    if (bluetoothManager.isReady()) {
                        bluetoothManager.connectHeadset(bondedDevice);
                        bluetoothManager.connectA2dp(bondedDevice);
                        if ((bluetoothManager.getHeadsetState(bondedDevice) == 2 || bluetoothManager.getA2dpState(bondedDevice) == 2) && !BluetoothUtil.isConnecting(CoreService.this.mSppConnectionManager.getConnectionState())) {
                            CoreService.this.mSppConnectionManager.connect(bondedDevice);
                        }
                    } else {
                        Log.e(this.TAG, "TaskConnectToDevice() : BluetoothManager.isReady() == false !!!");
                    }
                }
            }
        });
    }

    public void disconnectSpp() {
        Log.d(TAG, "disconnectSpp() : " + BluetoothUtil.privateAddress(this.mConnectedDevice));
        this.mSppConnectionManager.disconnect();
    }

    public void disconnectOtherDevice(String str) {
        if (isConnected() && !isConnected(str)) {
            Log.w(TAG, "disconnectOtherDevice() : " + BluetoothUtil.deviceToString(this.mConnectedDevice));
            disconnectSpp();
        }
    }

    public void disconnectDevice() {
        Log.d(TAG, "disconnectDevice() : " + BluetoothUtil.privateAddress(this.mConnectedDevice));
        if (Util.isEmulator()) {
            emulateDisconnected();
        } else {
            this.mWorker.post(new WorkerTask() {
                public void execute() {
                    BluetoothManager bluetoothManager = Application.getBluetoothManager();
                    synchronized (bluetoothManager) {
                        if (bluetoothManager.isReady()) {
                            bluetoothManager.disconnectHeadset(CoreService.this.mConnectedDevice);
                            bluetoothManager.disconnectA2dp(CoreService.this.mConnectedDevice);
                            CoreService.this.disconnectSpp();
                        } else {
                            Log.e(this.TAG, "TaskDisconnectToDevice() : BluetoothManager.isReady() == false !!!");
                        }
                    }
                }
            });
        }
    }

    public EarBudsInfo getEarBudsInfo() {
        return this.mEarBudsInfo;
    }

    public EarBudsFotaInfo getEarBudsFotaInfo() {
        return this.mFotaInfo;
    }

    public boolean isConnected() {
        if (Util.isEmulator()) {
            return this.mEmulatingConnected;
        }
        return this.mConnectedDevice != null;
    }

    public boolean isConnected(String str) {
        BluetoothDevice bluetoothDevice = this.mConnectedDevice;
        return bluetoothDevice != null && Util.equalsIgnoreCase(bluetoothDevice.getAddress(), str);
    }

    public BluetoothDevice getConnectedDevice() {
        return this.mConnectedDevice;
    }

    public int getConnectionState() {
        if (!Util.isEmulator() || !this.mEmulatingConnected) {
            return this.mConnectionState;
        }
        return 2;
    }

    public boolean isExtendedStatusReady() {
        return this.mExtendedStatusReady;
    }

    public void sendSppMessage(Msg msg) {
        this.mSppConnectionManager.sendMessage(msg);
    }

    public DeviceLogManager getDeviceLogInfo() {
        return this.mDeviceLogManager;
    }

    /* access modifiers changed from: private */
    public String getLastLaunchDeviceAddress() {
        return UhmFwUtil.getLastLaunchDeviceId();
    }

    /* access modifiers changed from: private */
    public void checkHeadsetA2dpDisconnected(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "checkHeadsetA2dpDisconnected()");
        if (!isCurrentDevice(bluetoothDevice) || Application.getBluetoothManager().getHeadsetState(bluetoothDevice) != 0 || Application.getBluetoothManager().getA2dpState(bluetoothDevice) != 0) {
            return;
        }
        if (this.mSppConnectionManager.getConnectionState() != 0) {
            Log.d(TAG, "Disconnect SPP by profiles : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
            disconnectSpp();
        } else if (getConnectionState() != 0) {
            updateConnectionState(bluetoothDevice, 0);
        }
    }

    private void connectSppByProfile(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "connectSppByProfile() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()));
        this.mSppConnectionManager.connect(bluetoothDevice);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0079, code lost:
        return;
     */
    public void updateConnectionState(BluetoothDevice bluetoothDevice, int i) {
        Log.d(TAG, "updateConnectionState()");
        BluetoothManager bluetoothManager = Application.getBluetoothManager();
        synchronized (bluetoothManager) {
            int headsetState = bluetoothManager.getHeadsetState(bluetoothDevice);
            int a2dpState = bluetoothManager.getA2dpState(bluetoothDevice);
            Log.d(TAG, "TaskUpdateConnectionState() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()) + " " + BluetoothUtil.stateToString(this.mConnectionState) + " (spp=" + BluetoothUtil.stateToString(i) + ", hfp=" + BluetoothUtil.stateToString(headsetState) + ", a2dp=" + BluetoothUtil.stateToString(a2dpState) + ")");
            if (!((i == 3 && this.mConnectionState == 0) || (i == 1 && this.mConnectionState == 2))) {
                setConnectionState(bluetoothDevice, i);
            }
        }
    }

    private void setConnectionState(BluetoothDevice bluetoothDevice, int i) {
        if (i != this.mConnectionState) {
            Log.i(TAG, "setConnectionState() : " + BluetoothUtil.privateAddress(bluetoothDevice.getAddress()) + " " + BluetoothUtil.stateToString(i) + " (from " + BluetoothUtil.stateToString(this.mConnectionState) + ")");
            this.mConnectionState = i;
            if (i == 0) {
                onDisconnected(bluetoothDevice);
            } else if (i == 1) {
                onConnecting(bluetoothDevice);
            } else if (i == 2) {
                onConnected(bluetoothDevice);
            } else if (i == 3) {
                onDisconnecting(bluetoothDevice);
            }
        }
    }

    private boolean isCurrentDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return false;
        }
        String address = bluetoothDevice.getAddress();
        BluetoothDevice bluetoothDevice2 = this.mConnectedDevice;
        return Util.equalsIgnoreCase(address, bluetoothDevice2 != null ? bluetoothDevice2.getAddress() : getLastLaunchDeviceAddress());
    }

    /* access modifiers changed from: private */
    public static boolean isPluginDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null || bluetoothDevice.getName() == null) {
            return false;
        }
        return bluetoothDevice.getName().contains("Galaxy Buds Live ") || bluetoothDevice.getName().contains("Galaxy Bean ");
    }

    public void registerSppMessageListener(OnSppMessageListener onSppMessageListener) {
        this.mSppMessageListener.add(onSppMessageListener);
    }

    public void unregisterSppMessageListener(OnSppMessageListener onSppMessageListener) {
        if (onSppMessageListener == null) {
            this.mSppMessageListener.clear();
        } else {
            this.mSppMessageListener.remove(onSppMessageListener);
        }
    }

    public void startFotaInstall(String str) {
        Log.d(TAG, "startFotaInstall : " + str);
        Log.d(TAG, "mEarBudsInfo.deviceSWVer : " + Application.getCoreService().getEarBudsInfo().deviceSWVer);
        Log.d(TAG, "FOTA start");
        FotaTransferManager fotaTransferManager = this.mFOTATransferManager;
        if (fotaTransferManager != null) {
            fotaTransferManager.startFota(str);
            FotaUtil.setFOTAProcessIsRunning(true);
        }
    }

    public int getLatestFOTAProgress() {
        return this.mFOTATransferManager.getLatestFOTAProgress();
    }

    /* access modifiers changed from: private */
    public void sendRequestSerialNumber() {
        sendSppMessage(new MsgDebugSerialNumber());
        ResponseTimerHandler responseTimerHandler = this.mResponseTimerHandler;
        if (responseTimerHandler != null) {
            responseTimerHandler.sendMessage(41);
        }
    }

    private final class ResponseTimerHandler extends Handler {
        private final int RETRY_COUNT;
        public int retry;

        private ResponseTimerHandler() {
            this.RETRY_COUNT = 3;
        }

        public void sendMessage(int i) {
            removeCallbacksAndMessages((Object) null);
            sendMessageDelayed(Message.obtain(this, i), UpdateInterface.HOLDING_AFTER_BT_CONNECTED);
        }

        public void handleMessage(Message message) {
            if (message.what == 41) {
                this.retry++;
                Log.d(CoreService.TAG, "handleMessage MsgID.MSG_ID_DEBUG_SERIAL_NUMBER : retry " + this.retry);
                if (this.retry < 3) {
                    CoreService.this.sendRequestSerialNumber();
                }
            }
        }
    }

    public ArrayList<HashMap<String, String>> checkApp2App() {
        List<ResolveInfo> queryBroadcastReceivers = this.mContext.getPackageManager().queryBroadcastReceivers(new Intent(Util.SEND_PUI_EVENT), XDMWbxml.WBXML_EXT_0);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Log.d(TAG, "receivers.size: " + queryBroadcastReceivers.size());
        for (ResolveInfo resolveInfo : queryBroadcastReceivers) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            Bundle bundle = activityInfo.metaData;
            if (!(activityInfo == null || bundle == null)) {
                String str = activityInfo.packageName;
                String string = bundle.getString("menu_name");
                String string2 = bundle.getString("description");
                if (!(string == null || string2 == null)) {
                    Log.d(TAG, "menuName : " + string);
                    Log.d(TAG, "description :" + string2);
                    HashMap hashMap = new HashMap();
                    hashMap.put(BaseContentProvider.PACKAGE_NAME, str);
                    hashMap.put("menu_name", string);
                    hashMap.put("description", string2);
                    arrayList.add(hashMap);
                }
            }
        }
        return arrayList;
    }
}
