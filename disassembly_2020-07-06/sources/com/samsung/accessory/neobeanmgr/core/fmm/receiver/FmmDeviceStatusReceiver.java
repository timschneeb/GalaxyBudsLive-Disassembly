package com.samsung.accessory.neobeanmgr.core.fmm.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.core.fmm.FmmManager;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.FmmConstants;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.RingManager;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FmmDeviceStatusReceiver {
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + FmmDeviceStatusReceiver.class.getSimpleName());
    Context mContext;
    private BroadcastReceiver mDeviceResponseReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002b  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x004b  */
        public void onReceive(Context context, Intent intent) {
            char c;
            String action = intent.getAction();
            int hashCode = action.hashCode();
            if (hashCode != -601743473) {
                if (hashCode == -213641688 && action.equals(CoreService.ACTION_MSG_ID_SET_FMM_CONFIG_RESULT)) {
                    c = 0;
                    if (c == 0) {
                        Log.d(FmmDeviceStatusReceiver.TAG, "ACTION_MSG_ID_SET_FMM_CONFIG_RESULT");
                        FmmManager.responseSetDeviceInfo(context);
                        return;
                    } else if (c == 1) {
                        Log.d(FmmDeviceStatusReceiver.TAG, "ACTION_MSG_ID_GET_FMM_CONFIG_RESP");
                        FmmManager.responseGetDeviceInfo(context, ByteBuffer.wrap(intent.getByteArrayExtra("getFmmConfig")).order(ByteOrder.LITTLE_ENDIAN));
                        return;
                    } else {
                        return;
                    }
                }
            } else if (action.equals(CoreService.ACTION_MSG_ID_GET_FMM_CONFIG_RESP)) {
                c = 1;
                if (c == 0) {
                }
            }
            c = 65535;
            if (c == 0) {
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Can't fix incorrect switch cases order */
        public void onReceive(Context context, Intent intent) {
            char c;
            String action = intent.getAction();
            switch (action.hashCode()) {
                case -1915507242:
                    if (action.equals(CoreService.ACTION_MUTE_EARBUD_STATUS_UPDATED)) {
                        c = 3;
                        break;
                    }
                case -1856324259:
                    if (action.equals(CoreService.ACTION_DEVICE_EXTENDED_STATUS_READY)) {
                        c = 0;
                        break;
                    }
                case -1354974214:
                    if (action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) {
                        c = 1;
                        break;
                    }
                case -348576706:
                    if (action.equals(CoreService.ACTION_MSG_ID_FIND_MY_EARBUDS_STATUS_UPDATED)) {
                        c = 2;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            if (c == 0 || c == 1) {
                Log.d(FmmDeviceStatusReceiver.TAG, "FMM:CONNECTION");
                FmmManager.sendAction(context, FmmConstants.Operation.CONNECTION);
            } else if (c == 2 || c == 3) {
                String stringExtra = intent.getStringExtra(RingManager.KEY_SENDER_ID);
                if (stringExtra == null || !FmmRequestReceiver.class.getName().equals(stringExtra)) {
                    FmmManager.sendAction(context, FmmConstants.Operation.RING_STATUS);
                    return;
                }
                String access$000 = FmmDeviceStatusReceiver.TAG;
                Log.d(access$000, "This intent from Fmm request. Do not send ring status. senderId : " + stringExtra);
            }
        }
    };
    private BroadcastReceiver mUnpairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String access$000 = FmmDeviceStatusReceiver.TAG;
            Log.d(access$000, "onReceive() : " + intent.getAction());
            if (intent.getAction() != null && intent.getAction().equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
                int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", Integer.MIN_VALUE);
                if (((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress().equals(UhmFwUtil.getLastLaunchDeviceId()) && intExtra == 10 && intExtra != intExtra2) {
                    FmmManager.sendAction(context, FmmConstants.Operation.REMOVE);
                }
            }
        }
    };

    public FmmDeviceStatusReceiver(Context context) {
        this.mContext = context;
        onCreate();
    }

    private void onCreate() {
        registerReceiver();
    }

    public void onDestroy() {
        unRegisterReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_DEVICE_EXTENDED_STATUS_READY);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_FIND_MY_EARBUDS_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MUTE_EARBUD_STATUS_UPDATED);
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        this.mContext.registerReceiver(this.mUnpairReceiver, intentFilter2);
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction(CoreService.ACTION_MSG_ID_SET_FMM_CONFIG_RESULT);
        intentFilter3.addAction(CoreService.ACTION_MSG_ID_GET_FMM_CONFIG_RESP);
        this.mContext.registerReceiver(this.mDeviceResponseReceiver, intentFilter3);
    }

    private void unRegisterReceiver() {
        this.mContext.unregisterReceiver(this.mReceiver);
        this.mContext.unregisterReceiver(this.mUnpairReceiver);
        this.mContext.unregisterReceiver(this.mDeviceResponseReceiver);
    }
}
