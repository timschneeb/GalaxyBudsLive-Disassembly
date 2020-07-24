package com.samsung.accessory.neobeanmgr.core.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.accessorydm.interfaces.XCommonInterface;
import com.accessorydm.interfaces.XDBInterface;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.common.util.WaitTimer;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.Msg;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogCoredumpComplete;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogCoredumpData;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogCoredumpDataSize;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogRoleSwitch;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogSessionClose;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogSessionOpen;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogTraceComplete;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogTraceData;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLogTraceStart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class DeviceLogManager {
    private static final String ACTION_DEVICELOG_ISSUETRACKER_REQUEST = "com.samsung.android.issuetracker.budslogrequest";
    private static final String ACTION_DEVICELOG_ISSUETRACKER_RESPONSE = "com.samsung.accessory.hearablemgr.budslogresult";
    private static final String ACTION_DEVICELOG_SM_REQUEST = "com.samsung.android.gearlog_sm_request";
    private static final String ACTION_DEVICELOG_SM_RESPONSE = "com.samsung.android.gearlog_sm_response";
    public static final String ACTION_MSG_ID_LOG_COREDUMP_COMPLETE = "com.samsung.accessory.neobeanmgr.core.service.CoreService.MSG_ID_LOG_COREDUMP_COMPLETE";
    public static final String ACTION_MSG_ID_LOG_COREDUMP_DATA = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_LOG_COREDUMP_DATA";
    public static final String ACTION_MSG_ID_LOG_COREDUMP_DATA_SIZE = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_LOG_COREDUMP_DATA_SIZE";
    public static final String ACTION_MSG_ID_LOG_SESSION_CLOSE = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_LOG_SESSION_CLOSE";
    public static final String ACTION_MSG_ID_LOG_SESSION_OPEN = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_LOG_SESSION_OPEN";
    public static final String ACTION_MSG_ID_LOG_TRACE_COMPLETE = "com.samsung.accessory.neobeanmgr.core.service.CoreService.MSG_ID_LOG_TRACE_COMPLETE";
    public static final String ACTION_MSG_ID_LOG_TRACE_DATA = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_LOG_TRACE_DATA";
    public static final String ACTION_MSG_ID_LOG_TRACE_ROLE_SWITCH = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_LOG_TRACE_ROLE_SWITCH";
    public static final String ACTION_MSG_ID_LOG_TRACE_START = "com.samsung.accessory.neobeanmgr.core.service.CoreService.ACTION_MSG_ID_LOG_TRACE_START";
    private static final String DEVICE_NAME = "Galaxy Buds Live".replace("Galaxy ", "").replaceAll(" ", "_");
    private static final int ISSUE_TRACKER = 1;
    private static final int NO_CORE_DUMP_DONE_TIMEOUT = 90000;
    private static final int NO_RESPONSE_TIMEOUT = 1000;
    private static final int NO_ROLE_SWITCH_TIMEOUT = 10000;
    private static final int NO_TRACE_DONE_TIMEOUT = 5000;
    private static final int OPEN_SESSION_NO_RESPONSE_TIMEOUT = 8000;
    private static final String RESULT_FAIL = "fail";
    private static final String RESULT_SUCCESS = "success";
    private static final int SAMSUNG_MEMBERS = 2;
    private static final int STATE_NONE = 0;
    private static final int STATE_WORKING = 1;
    private static final String TAG = "NeoBean_DeviceLogMgr";
    private static final String TYPE_COREDUMP = "_coreDump_";
    /* access modifiers changed from: private */
    public byte[] buf;
    /* access modifiers changed from: private */
    public boolean isCompletedRoleSwitch = false;
    /* access modifiers changed from: private */
    public boolean isCoupledDevice = false;
    /* access modifiers changed from: private */
    public boolean isRequestedByApp;
    /* access modifiers changed from: private */
    public String mCoreDumpStartTime;
    /* access modifiers changed from: private */
    public CoreService mCoreService;
    private final BroadcastReceiver mDeviceLogRequestReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (DeviceLogManager.ACTION_DEVICELOG_ISSUETRACKER_REQUEST.equals(intent.getAction())) {
                Log.d(DeviceLogManager.TAG, "ACTION_DEVICELOG_ISSUETRACKER_REQUEST");
                int unused = DeviceLogManager.this.mRequester = 1;
                if (DeviceLogManager.this.mCoreService.isConnected()) {
                    boolean unused2 = DeviceLogManager.this.isRequestedByApp = true;
                    DeviceLogManager.this.sendOpenSession();
                    return;
                }
                DeviceLogManager deviceLogManager = DeviceLogManager.this;
                deviceLogManager.sendResult(false, (String) null, deviceLogManager.mRequester);
                DeviceLogManager.this.mResponseTimer.reset();
            }
        }
    };
    private final BroadcastReceiver mDeviceLogSamsungMembersReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (DeviceLogManager.ACTION_DEVICELOG_SM_REQUEST.equals(intent.getAction())) {
                Log.d(DeviceLogManager.TAG, "ACTION_DEVICELOG_SM_REQUEST");
                int unused = DeviceLogManager.this.mRequester = 2;
                if (DeviceLogManager.this.mCoreService.isConnected()) {
                    boolean unused2 = DeviceLogManager.this.isRequestedByApp = true;
                    DeviceLogManager.this.sendOpenSession();
                    return;
                }
                DeviceLogManager.this.sendResultToSamsungMembers(0);
                DeviceLogManager.this.mResponseTimer.reset();
            }
        }
    };
    /* access modifiers changed from: private */
    public int mDeviceLogState = 0;
    /* access modifiers changed from: private */
    public String mDeviceType = null;
    private ArrayList<Integer> mOffsetList = new ArrayList<>();
    /* access modifiers changed from: private */
    public int mPartialMaxSize = 0;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(DeviceLogManager.TAG, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            if (((action.hashCode() == -1354974214 && action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) ? (char) 0 : 65535) == 0) {
                if (DeviceLogManager.this.buf != null && DeviceLogManager.this.buf.length > 0) {
                    DeviceLogManager deviceLogManager = DeviceLogManager.this;
                    boolean unused = deviceLogManager.writeFile(deviceLogManager.buf, DeviceLogManager.TYPE_COREDUMP, DeviceLogManager.this.mCoreDumpStartTime, DeviceLogManager.this.mDeviceType);
                    byte[] unused2 = DeviceLogManager.this.buf = new byte[0];
                }
                if (DeviceLogManager.this.isRequestedByApp) {
                    DeviceLogManager deviceLogManager2 = DeviceLogManager.this;
                    deviceLogManager2.sendResult(true, deviceLogManager2.targetPath, DeviceLogManager.this.mRequester);
                    boolean unused3 = DeviceLogManager.this.isRequestedByApp = false;
                }
                boolean unused4 = DeviceLogManager.this.isCompletedRoleSwitch = false;
                int unused5 = DeviceLogManager.this.mDeviceLogState = 0;
                DeviceLogManager.this.mResponseTimer.reset();
            }
        }
    };
    /* access modifiers changed from: private */
    public int mRequester;
    /* access modifiers changed from: private */
    public WaitTimer mResponseTimer;
    CoreService.OnSppMessageListener mSppListener = new CoreService.OnSppMessageListener() {
        public void onSppMessage(Msg msg) {
            switch (msg.id) {
                case 49:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_COREDUMP_DATA_SIZE.");
                    MsgLogCoredumpDataSize msgLogCoredumpDataSize = (MsgLogCoredumpDataSize) msg;
                    DeviceLogManager.this.makeOffsetList(msgLogCoredumpDataSize.partialDataCnt, msgLogCoredumpDataSize.partialDataMaxSize);
                    int unused = DeviceLogManager.this.mPartialMaxSize = msgLogCoredumpDataSize.partialDataMaxSize;
                    int unused2 = DeviceLogManager.this.mTotalSize = msgLogCoredumpDataSize.totalDataSize;
                    if (msgLogCoredumpDataSize.totalDataSize > 0) {
                        byte[] unused3 = DeviceLogManager.this.buf = new byte[msgLogCoredumpDataSize.totalDataSize];
                        Application.getCoreService().sendSppMessage(new MsgLogCoredumpData(0, msgLogCoredumpDataSize.totalDataSize));
                        if (!DeviceLogManager.this.isCompletedRoleSwitch || DeviceLogManager.this.mCoreDumpStartTime == null) {
                            DeviceLogManager deviceLogManager = DeviceLogManager.this;
                            String unused4 = deviceLogManager.mCoreDumpStartTime = deviceLogManager.getDumpTime();
                        }
                        DeviceLogManager.this.mResponseTimer.start(56, 90000);
                    } else if (!DeviceLogManager.this.isCoupledDevice) {
                        DeviceLogManager.this.sendCloseSession();
                    } else if (DeviceLogManager.this.isCompletedRoleSwitch) {
                        DeviceLogManager.this.sendCloseSession();
                    } else {
                        Application.getCoreService().sendSppMessage(new MsgLogRoleSwitch());
                        DeviceLogManager.this.mResponseTimer.start(55, 10000);
                    }
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_DATA));
                    return;
                case 50:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_COREDUMP_DATA.");
                    MsgLogCoredumpData msgLogCoredumpData = (MsgLogCoredumpData) msg;
                    DeviceLogManager.this.updateOffsetList(msgLogCoredumpData.partialDataOffset);
                    System.arraycopy(msgLogCoredumpData.rawData, 0, DeviceLogManager.this.buf, msgLogCoredumpData.partialDataOffset, msgLogCoredumpData.partialDataSize);
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_DATA_SIZE));
                    return;
                case 51:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_COREDUMP_COMPLETE.");
                    if (!DeviceLogManager.this.isCoupledDevice) {
                        DeviceLogManager.this.sendCloseSession();
                    } else if (DeviceLogManager.this.isCompletedRoleSwitch) {
                        DeviceLogManager.this.sendCloseSession();
                    } else {
                        Application.getCoreService().sendSppMessage(new MsgLogRoleSwitch());
                        DeviceLogManager.this.mResponseTimer.start(55, 10000);
                    }
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_COMPLETE));
                    return;
                case 52:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_TRACE_START.");
                    MsgLogTraceStart msgLogTraceStart = (MsgLogTraceStart) msg;
                    byte[] unused5 = DeviceLogManager.this.traceBuf = new byte[msgLogTraceStart.dataSize];
                    DeviceLogManager.this.makeOffsetList(msgLogTraceStart.traceCount, msgLogTraceStart.partialDataMaxSize);
                    int unused6 = DeviceLogManager.this.mPartialMaxSize = msgLogTraceStart.partialDataMaxSize;
                    int unused7 = DeviceLogManager.this.mTotalSize = msgLogTraceStart.dataSize;
                    String unused8 = DeviceLogManager.this.mDeviceType = msgLogTraceStart.deviceType > 0 ? "L" : "R";
                    boolean unused9 = DeviceLogManager.this.isCoupledDevice = msgLogTraceStart.coupled;
                    Application.getCoreService().sendSppMessage(new MsgLogTraceData(0, msgLogTraceStart.dataSize));
                    if (!DeviceLogManager.this.isCompletedRoleSwitch) {
                        DeviceLogManager deviceLogManager2 = DeviceLogManager.this;
                        String unused10 = deviceLogManager2.mTraceStartTime = deviceLogManager2.getDumpTime();
                    }
                    DeviceLogManager.this.mResponseTimer.start(57, XCommonInterface.WAKE_LOCK_TIMEOUT);
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_START));
                    return;
                case 53:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_TRACE_DATA.");
                    MsgLogTraceData msgLogTraceData = (MsgLogTraceData) msg;
                    DeviceLogManager.this.updateOffsetList(msgLogTraceData.partialDataOffset);
                    System.arraycopy(msgLogTraceData.rawData, 0, DeviceLogManager.this.traceBuf, msgLogTraceData.partialDataOffset, msgLogTraceData.partialDataSize);
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_DATA));
                    return;
                case 54:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_TRACE_COMPLETE.");
                    Application.getCoreService().sendSppMessage(new MsgLogCoredumpDataSize());
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_COMPLETE));
                    return;
                case 55:
                    MsgLogRoleSwitch msgLogRoleSwitch = (MsgLogRoleSwitch) msg;
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_TRACE_ROLE_SWITCH." + msgLogRoleSwitch.resCode);
                    DeviceLogManager.this.mResponseTimer.remove(55);
                    boolean unused11 = DeviceLogManager.this.isCompletedRoleSwitch = true;
                    if (msgLogRoleSwitch.resCode) {
                        Application.getCoreService().sendSppMessage(new MsgLogTraceStart((byte) 0));
                    } else {
                        DeviceLogManager.this.sendCloseSession();
                    }
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_ROLE_SWITCH));
                    return;
                case 56:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_COREDUMP_DATA_DONE.");
                    DeviceLogManager.this.mResponseTimer.remove(56);
                    DeviceLogManager.this.onGetCoredumpDoneMsg();
                    return;
                case 57:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_TRACE_DATA_DONE.");
                    DeviceLogManager.this.mResponseTimer.remove(57);
                    DeviceLogManager.this.onGetTraceDoneMsg();
                    return;
                case 58:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_SESSION_OPEN.");
                    boolean unused12 = DeviceLogManager.this.isCompletedRoleSwitch = false;
                    if (((MsgLogSessionOpen) msg).resCode) {
                        int unused13 = DeviceLogManager.this.mDeviceLogState = 1;
                        Application.getCoreService().sendSppMessage(new MsgLogTraceStart((byte) 0));
                        DeviceLogManager.this.mResponseTimer.remove(58);
                    } else {
                        int unused14 = DeviceLogManager.this.mDeviceLogState = 0;
                        if (DeviceLogManager.this.isRequestedByApp) {
                            DeviceLogManager deviceLogManager3 = DeviceLogManager.this;
                            deviceLogManager3.sendResult(false, (String) null, deviceLogManager3.mRequester);
                        }
                        boolean unused15 = DeviceLogManager.this.isRequestedByApp = false;
                        DeviceLogManager.this.mResponseTimer.reset();
                    }
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_SESSION_OPEN));
                    return;
                case 59:
                    Log.d(DeviceLogManager.TAG, "MsgID.LOG_SESSION_CLOSE.");
                    int unused16 = DeviceLogManager.this.mDeviceLogState = 0;
                    if (((MsgLogSessionClose) msg).resCode && DeviceLogManager.this.isRequestedByApp) {
                        DeviceLogManager deviceLogManager4 = DeviceLogManager.this;
                        deviceLogManager4.sendResult(true, deviceLogManager4.targetPath, DeviceLogManager.this.mRequester);
                    }
                    boolean unused17 = DeviceLogManager.this.isRequestedByApp = false;
                    DeviceLogManager.this.mResponseTimer.reset();
                    Util.sendPermissionBroadcast(Application.getContext(), new Intent(DeviceLogManager.ACTION_MSG_ID_LOG_SESSION_CLOSE));
                    return;
                default:
                    return;
            }
        }
    };
    private Handler mTimeHandler = new Handler() {
        public void handleMessage(Message message) {
            Log.d(DeviceLogManager.TAG, "read Message :" + Integer.toHexString(message.what & 255));
            switch (message.what) {
                case 55:
                case 58:
                    DeviceLogManager.this.sendCloseSession();
                    return;
                case 56:
                    DeviceLogManager.this.onGetCoredumpDoneMsg();
                    return;
                case 57:
                    DeviceLogManager.this.onGetTraceDoneMsg();
                    return;
                case 59:
                    if (DeviceLogManager.this.isRequestedByApp) {
                        if (DeviceLogManager.this.isCompletedRoleSwitch) {
                            DeviceLogManager deviceLogManager = DeviceLogManager.this;
                            deviceLogManager.sendResult(true, deviceLogManager.targetPath, DeviceLogManager.this.mRequester);
                        } else {
                            DeviceLogManager deviceLogManager2 = DeviceLogManager.this;
                            deviceLogManager2.sendResult(false, (String) null, deviceLogManager2.mRequester);
                        }
                        boolean unused = DeviceLogManager.this.isRequestedByApp = false;
                    }
                    boolean unused2 = DeviceLogManager.this.isCompletedRoleSwitch = false;
                    int unused3 = DeviceLogManager.this.mDeviceLogState = 0;
                    DeviceLogManager.this.mResponseTimer.reset();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public int mTotalSize = 0;
    /* access modifiers changed from: private */
    public String mTraceStartTime = null;
    /* access modifiers changed from: private */
    public String targetPath;
    /* access modifiers changed from: private */
    public byte[] traceBuf;

    DeviceLogManager(CoreService coreService) {
        Log.d(TAG, "DeviceLogManager() : " + coreService);
        this.mCoreService = coreService;
        this.mCoreService.registerSppMessageListener(this.mSppListener);
        this.mResponseTimer = new WaitTimer(this.mTimeHandler);
        this.mResponseTimer.reset();
        RegisterReceivers();
        this.mDeviceLogState = 0;
    }

    public void destroy() {
        Log.d(TAG, "destroy()");
        this.mCoreService.unregisterSppMessageListener(this.mSppListener);
        Application.getContext().unregisterReceiver(this.mDeviceLogRequestReceiver);
        Application.getContext().unregisterReceiver(this.mReceiver);
        Application.getContext().unregisterReceiver(this.mDeviceLogSamsungMembersReceiver);
        this.mDeviceLogState = 0;
    }

    public boolean getIsDeviceLogExtrationWorking() {
        Log.d(TAG, "getIsDeviceLogExtrationWorking : " + this.mDeviceLogState);
        return this.mDeviceLogState == 1;
    }

    public void setDeviceLogExtractionState(int i) {
        Log.d(TAG, "setDeviceLogExtractionState : " + i);
        this.mDeviceLogState = i;
    }

    public void sendOpenSession() {
        this.mCoreDumpStartTime = null;
        this.mTraceStartTime = null;
        Application.getCoreService().sendSppMessage(new MsgLogSessionOpen());
        this.mResponseTimer.start(58, 8000);
    }

    /* access modifiers changed from: private */
    public void sendCloseSession() {
        Application.getCoreService().sendSppMessage(new MsgLogSessionClose());
        this.mResponseTimer.start(59, 1000);
    }

    /* access modifiers changed from: private */
    public String getDumpTime() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Long.valueOf(System.currentTimeMillis()));
    }

    private boolean makeDir() {
        String supportedStorageByCondtion = BudsLogManager.getInstance().getSupportedStorageByCondtion();
        this.targetPath = supportedStorageByCondtion + "/log/GearLog/Buds/";
        File file = new File(this.targetPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        file.setReadable(true, false);
        file.setWritable(true, false);
        file.setExecutable(true, false);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean writeFile(byte[] bArr, String str, String str2, String str3) {
        FileOutputStream fileOutputStream;
        Log.d(TAG, "writeFile");
        if (!makeDir()) {
            Log.w(TAG, "failed make directory");
        }
        File file = new File(this.targetPath + File.separator + DEVICE_NAME + str + str3 + "_" + str2 + ".dat");
        try {
            if (!file.createNewFile()) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.setReadable(true, false);
        file.setWritable(true, false);
        file.setExecutable(true, false);
        try {
            fileOutputStream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            fileOutputStream = null;
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.write(bArr);
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void makeOffsetList(int i, int i2) {
        this.mOffsetList.clear();
        for (int i3 = 0; i3 < i; i3++) {
            this.mOffsetList.add(i3, Integer.valueOf(i3 * i2));
        }
    }

    /* access modifiers changed from: private */
    public void updateOffsetList(int i) {
        if (!this.mOffsetList.isEmpty()) {
            this.mOffsetList.remove(Integer.valueOf(i));
        }
    }

    private int getRemainOffset() {
        if (!this.mOffsetList.isEmpty()) {
            Iterator<Integer> it = this.mOffsetList.iterator();
            if (it.hasNext()) {
                return it.next().intValue();
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public void onGetCoredumpDoneMsg() {
        int remainOffset = getRemainOffset();
        if (remainOffset >= 0) {
            int i = this.mPartialMaxSize;
            int i2 = remainOffset + i;
            int i3 = this.mTotalSize;
            if (i2 > i3) {
                i = i3 - remainOffset;
            }
            Application.getCoreService().sendSppMessage(new MsgLogCoredumpData(remainOffset, i));
            return;
        }
        Application.getCoreService().sendSppMessage(new MsgLogCoredumpComplete());
        writeFile(this.buf, TYPE_COREDUMP, this.mCoreDumpStartTime, this.mDeviceType);
        this.buf = new byte[0];
    }

    /* access modifiers changed from: private */
    public void onGetTraceDoneMsg() {
        int remainOffset = getRemainOffset();
        if (remainOffset >= 0) {
            int i = this.mPartialMaxSize;
            int i2 = remainOffset + i;
            int i3 = this.mTotalSize;
            if (i2 > i3) {
                i = i3 - remainOffset;
            }
            Application.getCoreService().sendSppMessage(new MsgLogTraceData(remainOffset, i));
            return;
        }
        Application.getCoreService().sendSppMessage(new MsgLogTraceComplete());
        writeFile(this.traceBuf, "_traceLog_", this.mTraceStartTime, this.mDeviceType);
    }

    /* access modifiers changed from: private */
    public void sendResult(boolean z, String str, int i) {
        if (i == 1) {
            sendResultToIssueTracker(z ? "success" : "fail", str);
        } else if (i == 2) {
            sendResultToSamsungMembers(z ? 1 : 0);
        } else {
            Log.d(TAG, "sendResult no requester");
        }
    }

    private void sendResultToIssueTracker(String str, String str2) {
        Log.d(TAG, "sendResultToIssueTracker result: " + str + ", path: " + str2);
        Intent intent = new Intent();
        intent.putExtra("result", str);
        intent.putExtra("filepath", str2);
        intent.putExtra(XDBInterface.XDM_SQL_ACCESSORY_MODEL, "Galaxy Buds Live");
        intent.setAction(ACTION_DEVICELOG_ISSUETRACKER_RESPONSE);
        Application.getContext().sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    public void sendResultToSamsungMembers(int i) {
        Log.d(TAG, "sendResultToSamsungMembers result: " + i);
        Intent intent = new Intent();
        intent.putExtra("success", Integer.toString(i));
        intent.setAction(ACTION_DEVICELOG_SM_RESPONSE);
        if (Util.isSamsungDevice()) {
            Application.getContext().sendBroadcast(intent, "android.permission.DUMP");
        } else {
            Application.getContext().sendBroadcast(intent, "com.samsung.android.permission.GEAR_DUMP");
        }
    }

    private void RegisterReceivers() {
        Application.getContext().registerReceiver(this.mDeviceLogRequestReceiver, getIssueTrackerIntentFilter(), ACTION_DEVICELOG_ISSUETRACKER_REQUEST, (Handler) null);
        Application.getContext().registerReceiver(this.mReceiver, getIntentFilter());
        Application.getContext().registerReceiver(this.mDeviceLogSamsungMembersReceiver, getSamsungMembersIntentFilter());
    }

    private IntentFilter getIssueTrackerIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DEVICELOG_ISSUETRACKER_REQUEST);
        Application.getContext().registerReceiver(this.mDeviceLogRequestReceiver, intentFilter, ACTION_DEVICELOG_ISSUETRACKER_REQUEST, (Handler) null);
        return intentFilter;
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        return intentFilter;
    }

    private IntentFilter getSamsungMembersIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DEVICELOG_SM_REQUEST);
        return intentFilter;
    }
}
