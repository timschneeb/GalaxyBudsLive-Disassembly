package com.samsung.accessory.neobeanmgr.core.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.samsung.accessory.fotaprovider.AccessoryEventHandler;
import com.samsung.accessory.fotaprovider.controller.ConsumerInfo;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.util.WaitTimer;
import com.samsung.accessory.neobeanmgr.core.fota.manager.FOTAMainManager;
import com.samsung.accessory.neobeanmgr.core.fota.manager.FotaRequestController;
import com.samsung.accessory.neobeanmgr.core.fota.util.FotaBinaryFile;
import com.samsung.accessory.neobeanmgr.core.fota.util.FotaUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.Msg;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgFotaControl;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgFotaDownloadData;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgFotaResult;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgFotaSession;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgFotaUpdated;
import com.samsung.android.fotaagent.register.RegisterInterface;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class FotaTransferManager {
    private static final int FOTA_NO_RESPONSE_TIMEOUT = 20000;
    private static final int FOTA_TIMEOUT = 600000;
    private static final String TAG = "NeoBean_FotaTransferManager";
    /* access modifiers changed from: private */
    public int MTU_SIZE;
    /* access modifiers changed from: private */
    public FotaBinaryFile mBinaryFile;
    private CoreService mCoreService;
    /* access modifiers changed from: private */
    public int mCurEntryId;
    /* access modifiers changed from: private */
    public int mCurFOTAProgress;
    private Timer mFOTAResponseWaitingTimer = new Timer();
    /* access modifiers changed from: private */
    public WaitTimer mFotaTimer;
    /* access modifiers changed from: private */
    public long mLastEntryOffset;
    /* access modifiers changed from: private */
    public boolean mLastFragment;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0046  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x007f  */
        public void onReceive(Context context, Intent intent) {
            char c;
            Log.d(FotaTransferManager.TAG, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            int hashCode = action.hashCode();
            if (hashCode != -1856324259) {
                if (hashCode == -1354974214 && action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) {
                    c = 1;
                    if (c == 0) {
                        FotaUtil.sendFotaBroadcast(true);
                        return;
                    } else if (c == 1) {
                        Log.d(FotaTransferManager.TAG, "checkFota status");
                        Log.d(FotaTransferManager.TAG, "FotaUtil.getFOTAProcessIsRunning() : " + FotaUtil.getFOTAProcessIsRunning());
                        if (FotaUtil.getFOTAProcessIsRunning()) {
                            FOTAMainManager.getInstance().updateFOTACopyProcessResult(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT, 3);
                            FotaTransferManager.this.initFOTAStatus();
                        }
                        FotaUtil.sendFotaBroadcast(false);
                        return;
                    } else {
                        return;
                    }
                }
            } else if (action.equals(CoreService.ACTION_DEVICE_EXTENDED_STATUS_READY)) {
                c = 0;
                if (c == 0) {
                }
            }
            c = 65535;
            if (c == 0) {
            }
        }
    };
    CoreService.OnSppMessageListener mSppListener = new CoreService.OnSppMessageListener() {
        public void onSppMessage(Msg msg) {
            StringBuilder sb = new StringBuilder();
            sb.append("Msg : ");
            int i = 0;
            sb.append(String.format("%02X ", new Object[]{Byte.valueOf(msg.id)}));
            Log.d(FotaTransferManager.TAG, sb.toString());
            switch (msg.id) {
                case -71:
                    Log.i(FotaTransferManager.TAG, "== MSG_ID_FOTA_RESULT ==");
                    Application.getCoreService().sendSppMessage(new MsgFotaResult());
                    if (((MsgFotaResult) msg).mResult == 0) {
                        Log.d(FotaTransferManager.TAG, "====FOTA UPDATE SUCCESS====");
                        AccessoryEventHandler.getInstance().reportUpdateResult(new ConsumerInfo(Application.getCoreService().getEarBudsFotaInfo().deviceId, Application.getCoreService().getEarBudsFotaInfo().modelNumber, Application.getCoreService().getEarBudsFotaInfo().salesCode, Application.getCoreService().getEarBudsFotaInfo().firmwareVersion, Application.getCoreService().getEarBudsFotaInfo().uniqueNumber, Application.getCoreService().getEarBudsFotaInfo().serialNumber), true);
                        Log.d(FotaTransferManager.TAG, "send report update result : true");
                        return;
                    }
                    return;
                case -69:
                    Log.i(FotaTransferManager.TAG, "== RECV : MSG_ID_FOTA_OPEN ==");
                    FotaTransferManager.this.mFotaTimer.remove(-69);
                    MsgFotaSession msgFotaSession = (MsgFotaSession) msg;
                    Log.i(FotaTransferManager.TAG, "MSG_ID_FOTA_OPEN : mErrorCode=[success=0, fail=1] " + msgFotaSession.mErrorCode);
                    if (msgFotaSession.mErrorCode != 0) {
                        FOTAMainManager.getInstance().updateFOTACopyProcessResult(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT, 3);
                        return;
                    } else {
                        FotaTransferManager.this.mFotaTimer.start(-68, RegisterInterface.DELAY_PERIOD_FOR_BACKGROUND_REGISTER);
                        return;
                    }
                case -68:
                    Log.i(FotaTransferManager.TAG, "== RECV : MSG_ID_FOTA_CONTROL ==");
                    MsgFotaControl msgFotaControl = (MsgFotaControl) msg;
                    Log.d(FotaTransferManager.TAG, "ControlID = " + msgFotaControl.mControlID);
                    int i2 = msgFotaControl.mControlID;
                    if (i2 == 0) {
                        FotaTransferManager.this.mFotaTimer.remove(-68);
                        int unused = FotaTransferManager.this.MTU_SIZE = msgFotaControl.mMtuSize;
                        Log.i(FotaTransferManager.TAG, "-- CONTROL_ID_SEND_MTU : " + FotaTransferManager.this.MTU_SIZE + " --");
                        Application.getCoreService().sendSppMessage(new MsgFotaControl(msgFotaControl.mControlID, msgFotaControl.mMtuSize));
                        return;
                    } else if (i2 == 1) {
                        Log.i(FotaTransferManager.TAG, "-- CONTROL_ID_READY_TO_DOWNLOAD --" + msgFotaControl.mId);
                        Application.getCoreService().sendSppMessage(new MsgFotaControl(msgFotaControl.mControlID, msgFotaControl.mId));
                        int unused2 = FotaTransferManager.this.mCurEntryId = msgFotaControl.mId;
                        return;
                    } else {
                        return;
                    }
                case -67:
                    Log.i(FotaTransferManager.TAG, "== RECV : MSG_ID_FOTA_DOWNLOAD_DATA ==");
                    MsgFotaDownloadData msgFotaDownloadData = (MsgFotaDownloadData) msg;
                    while (i < msgFotaDownloadData.mReqeustPacketNum) {
                        MsgFotaDownloadData msgFotaDownloadData2 = new MsgFotaDownloadData(FotaTransferManager.this.mBinaryFile, FotaTransferManager.this.mCurEntryId, msgFotaDownloadData.mReceivedOffset + ((long) (FotaTransferManager.this.MTU_SIZE * i)), FotaTransferManager.this.MTU_SIZE, true);
                        boolean unused3 = FotaTransferManager.this.mLastFragment = msgFotaDownloadData2.isLastFragment();
                        long unused4 = FotaTransferManager.this.mLastEntryOffset = msgFotaDownloadData2.getOffset();
                        Log.d(FotaTransferManager.TAG, "mLastFragment : " + FotaTransferManager.this.mLastFragment);
                        Log.d(FotaTransferManager.TAG, "mLastEntryOffset : " + FotaTransferManager.this.mLastEntryOffset);
                        Application.getCoreService().sendSppMessage(msgFotaDownloadData2);
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!FotaTransferManager.this.mLastFragment) {
                            i++;
                        } else {
                            return;
                        }
                    }
                    return;
                case -66:
                    Log.i(FotaTransferManager.TAG, "== MSG_ID_FOTA_UPDATE ==");
                    MsgFotaUpdated msgFotaUpdated = (MsgFotaUpdated) msg;
                    int i3 = msgFotaUpdated.mUpdateId;
                    if (i3 == 0) {
                        Log.e(FotaTransferManager.TAG, " -- MsgFotaUpdated.UPDATE_ID_PERCENT : " + msgFotaUpdated.mPercent + "% --");
                        if (FotaUtil.getFOTAProcessIsRunning()) {
                            short s = (short) msgFotaUpdated.mPercent;
                            int unused5 = FotaTransferManager.this.mCurFOTAProgress = s;
                            if (FotaRequestController.mRequestFileTransferCallback != null) {
                                FotaRequestController.mRequestFileTransferCallback.onFileProgress(s);
                            }
                            Log.d(FotaTransferManager.TAG, "[ACTION_FOTA_PROGRESS_COPYING] percent : " + s + "%");
                            return;
                        }
                        return;
                    } else if (i3 == 1) {
                        Log.i(FotaTransferManager.TAG, "-- MsgFotaUpdated.UPDATE_ID_STATE_CHANGED : " + msgFotaUpdated.mState + ", " + msgFotaUpdated.mErrorCode + " --");
                        Application.getCoreService().sendSppMessage(new MsgFotaUpdated());
                        if (msgFotaUpdated.mState == 0) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FOTAMainManager.getInstance().updateFOTACopyProcessResult(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT, 1);
                                }
                            }, 1000);
                            FotaTransferManager.this.initFOTAStatus();
                            Log.d(FotaTransferManager.TAG, "====FOTA SEND SUCCESS====");
                            FotaRequestController.mRequestResultCallback.onSuccess(new ConsumerInfo(Application.getCoreService().getEarBudsFotaInfo().deviceId, Application.getCoreService().getEarBudsFotaInfo().modelNumber, Application.getCoreService().getEarBudsFotaInfo().salesCode, Application.getCoreService().getEarBudsFotaInfo().firmwareVersion, Application.getCoreService().getEarBudsFotaInfo().uniqueNumber, Application.getCoreService().getEarBudsFotaInfo().serialNumber));
                            return;
                        }
                        Log.d(FotaTransferManager.TAG, "fota_download_control>> close Download. download fail reason : " + msgFotaUpdated.mErrorCode);
                        FOTAMainManager.getInstance().updateFOTACopyProcessResult(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT, 3);
                        FotaTransferManager.this.initFOTAStatus();
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private Handler mTimeOutHandler = new Handler() {
        public void handleMessage(Message message) {
            Log.d(FotaTransferManager.TAG, "read Message :" + Integer.toHexString(message.what & 255));
            Log.d(FotaTransferManager.TAG, "RECEIVE offset : " + message.arg1);
            if (message.what == -69) {
                FOTAMainManager.getInstance().updateFOTACopyProcessResult(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT, 3);
                FotaTransferManager.this.initFOTAStatus();
            }
        }
    };
    /* access modifiers changed from: private */
    public TimerTask mTimerTask;

    public FotaTransferManager(CoreService coreService) {
        this.mCoreService = coreService;
        this.mCoreService.registerSppMessageListener(this.mSppListener);
        this.mFotaTimer = new WaitTimer(this.mTimeOutHandler);
        this.mFotaTimer.reset();
        Application.getContext().registerReceiver(this.mReceiver, getIntentFilter());
    }

    public void startFota(String str) {
        Log.d(TAG, "startFota() : " + str);
        this.mBinaryFile = new FotaBinaryFile(new File(str));
        if (!this.mBinaryFile.open()) {
            FOTAMainManager.getInstance().updateFOTACopyProcessResult(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT, 3);
            return;
        }
        this.MTU_SIZE = 0;
        this.mCurEntryId = 0;
        this.mLastFragment = false;
        this.mLastEntryOffset = 0;
        Application.getCoreService().sendSppMessage(new MsgFotaSession((MsgFotaSession.FotaBinaryFileGetData) this.mBinaryFile));
        this.mFotaTimer.start(-69, RegisterInterface.DELAY_PERIOD_FOR_BACKGROUND_REGISTER);
        runFOTAResponseWaitingTimer();
    }

    public int getLatestFOTAProgress() {
        Log.d(TAG, "getLatestFOTAProgress() - mCurFOTAProgress: " + this.mCurFOTAProgress + "%");
        return this.mCurFOTAProgress;
    }

    public void destroy() {
        Log.d(TAG, "destroy()");
        this.mCoreService.unregisterSppMessageListener(this.mSppListener);
        Application.getContext().unregisterReceiver(this.mReceiver);
    }

    public void killFOTAProcess() {
        Log.d(TAG, "killFOTAProcess()");
        FOTAMainManager.getInstance().updateFOTACopyProcessResult(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT, 3);
        initFOTAStatus();
    }

    private void checkFOTAExceptionalStatus() {
        if (isFOTAEnable()) {
            Log.d(TAG, "exceptional case - kill previous FOTA process!!");
            killFOTAProcess();
        }
    }

    public boolean isFOTAEnable() {
        boolean fOTAProcessIsRunning = FotaUtil.getFOTAProcessIsRunning();
        Log.d(TAG, "isFOTAEnable(): " + fOTAProcessIsRunning);
        return fOTAProcessIsRunning;
    }

    private void runFOTAResponseWaitingTimer() {
        TimerTask timerTask = this.mTimerTask;
        if (timerTask != null) {
            timerTask.cancel();
            this.mTimerTask = null;
            Log.d(TAG, "mFOTAResponseWaitingTimer is canceled!!");
        }
        this.mTimerTask = FOTAUpdateResponseTimeOut();
        this.mFOTAResponseWaitingTimer.schedule(this.mTimerTask, 600000);
        Log.d(TAG, "runFOTAResponseWaitingTimer");
    }

    private TimerTask FOTAUpdateResponseTimeOut() {
        return new TimerTask() {
            public void run() {
                Log.i(FotaTransferManager.TAG, "FOTAUpdateResponseTimeOut");
                TimerTask unused = FotaTransferManager.this.mTimerTask = null;
                FotaTransferManager.this.killFOTAProcess();
            }
        };
    }

    private void killFOTAResponseWaitingTimer() {
        TimerTask timerTask = this.mTimerTask;
        if (timerTask != null) {
            timerTask.cancel();
            this.mTimerTask = null;
            Log.i(TAG, "killFOTAResponseWaitingTimer");
        }
        WaitTimer waitTimer = this.mFotaTimer;
        if (waitTimer != null) {
            waitTimer.reset();
        }
    }

    /* access modifiers changed from: private */
    public void initFOTAStatus() {
        Log.d(TAG, "initFOTAStatus()");
        killFOTAResponseWaitingTimer();
        FotaUtil.setFOTAProcessIsRunning(false);
        FotaUtil.setEmergencyFOTAIsRunning(false);
        this.mCurFOTAProgress = 0;
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_DEVICE_EXTENDED_STATUS_READY);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        return intentFilter;
    }
}
