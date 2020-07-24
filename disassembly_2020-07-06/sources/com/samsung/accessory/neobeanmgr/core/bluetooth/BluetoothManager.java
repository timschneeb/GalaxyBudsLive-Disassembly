package com.samsung.accessory.neobeanmgr.core.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.common.util.BluetoothUtil;
import com.samsung.accessory.neobeanmgr.common.util.BroadcastReceiverUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class BluetoothManager {
    public static final String ACTION_READY = "com.samsung.accessory.neobeanmgr.core.bluetooth.BluetoothManager.ACTION_READY";
    public static final String ACTION_STOPPED = "com.samsung.accessory.neobeanmgr.core.bluetooth.BluetoothManager.ACTION_STOPPED";
    private static final String TAG = "NeoBean_BluetoothManager";
    private final InstanceLogger<BluetoothA2dp> mBluetoothA2dp = new InstanceLogger<>();
    private final InstanceLogger<BluetoothHeadset> mBluetoothHeadset = new InstanceLogger<>();
    /* access modifiers changed from: private */
    public final Context mContext;
    private final BluetoothProfile.ServiceListener mListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            synchronized (BluetoothManager.this) {
                Log.d(BluetoothManager.TAG, "onServiceConnected() : " + BluetoothUtil.profileToString(i) + ", adapter : " + BluetoothUtil.isAdapterOn());
                if (i == 1) {
                    BluetoothManager.this.setHeadsetProxy((BluetoothHeadset) bluetoothProfile);
                } else if (i == 2) {
                    BluetoothManager.this.setA2dpProxy((BluetoothA2dp) bluetoothProfile);
                }
                if (!(BluetoothManager.this.mReady || BluetoothManager.this.getHeadsetProxy() == null || BluetoothManager.this.getA2dpProxy() == null)) {
                    BluetoothManager.this.setReady(true);
                    Util.sendPermissionBroadcast(BluetoothManager.this.mContext, new Intent(BluetoothManager.ACTION_READY));
                }
            }
        }

        public void onServiceDisconnected(int i) {
            synchronized (BluetoothManager.this) {
                Log.d(BluetoothManager.TAG, "onServiceDisconnected() : " + BluetoothUtil.profileToString(i) + ", adapter : " + BluetoothUtil.isAdapterOn());
                if (i == 1) {
                    BluetoothManager.this.closeHeadsetProfile();
                    if (BluetoothUtil.isAdapterOn()) {
                        BluetoothManager.this.bindHeadsetProfile();
                    }
                } else if (i == 2) {
                    BluetoothManager.this.closeA2dpProfile();
                    if (BluetoothUtil.isAdapterOn()) {
                        BluetoothManager.this.bindA2dpProfile();
                    }
                }
                if (BluetoothManager.this.mReady && (BluetoothManager.this.getHeadsetProxy() == null || BluetoothManager.this.getA2dpProxy() == null)) {
                    BluetoothManager.this.setReady(false);
                    Util.sendPermissionBroadcast(BluetoothManager.this.mContext, new Intent(BluetoothManager.ACTION_STOPPED));
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mReady;
    private final BroadcastReceiverUtil.Receiver mReceiver = new BroadcastReceiverUtil.Receiver() {
        public void setIntentFilter(IntentFilter intentFilter) {
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        }

        public void onReceive(Context context, Intent intent) {
            Log.d(BluetoothManager.TAG, "onReceive() : " + Util.getAction(intent));
            String action = Util.getAction(intent);
            if (((action.hashCode() == -1530327060 && action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) ? (char) 0 : 65535) == 0) {
                int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", -1);
                Log.d(BluetoothManager.TAG, "BluetoothAdapter.ACTION_STATE_CHANGED : " + BluetoothUtil.adapterStateToString(intExtra) + " (from " + BluetoothUtil.adapterStateToString(intExtra2) + ")");
                if (intExtra == 10) {
                    BluetoothManager.this.closeHeadsetProfile();
                    BluetoothManager.this.closeA2dpProfile();
                } else if (intExtra == 12) {
                    BluetoothManager.this.bindHeadsetProfile();
                    BluetoothManager.this.bindA2dpProfile();
                }
            }
        }
    };

    public BluetoothManager(Context context) {
        this.mContext = context;
        this.mReady = false;
        setHeadsetProxy((BluetoothHeadset) null);
        setA2dpProxy((BluetoothA2dp) null);
        BroadcastReceiverUtil.register(this.mContext, this.mReceiver);
        if (BluetoothUtil.isAdapterOn()) {
            bindHeadsetProfile();
            bindA2dpProfile();
        }
    }

    public void destroy() {
        Log.d(TAG, "destroy()");
        BroadcastReceiverUtil.unregister(this.mContext, this.mReceiver);
        closeHeadsetProfile();
        closeA2dpProfile();
    }

    /* access modifiers changed from: private */
    public void bindHeadsetProfile() {
        Log.d(TAG, "bindHeadsetProfile() : " + this.mBluetoothHeadset);
        if (getHeadsetProxy() != null) {
            Log.w(TAG, "bindHeadsetProfile() : getHeadsetProxy() != null");
        } else if (BluetoothUtil.getAdapter() != null) {
            BluetoothUtil.getAdapter().getProfileProxy(this.mContext, this.mListener, 1);
        }
    }

    /* access modifiers changed from: private */
    public void bindA2dpProfile() {
        Log.d(TAG, "bindA2dpProfile() : " + this.mBluetoothA2dp);
        if (getA2dpProxy() != null) {
            Log.w(TAG, "bindA2dpProfile() : getA2dpProxy() != null");
        } else if (BluetoothUtil.getAdapter() != null) {
            BluetoothUtil.getAdapter().getProfileProxy(this.mContext, this.mListener, 2);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void closeHeadsetProfile() {
        Log.d(TAG, "closeHeadsetProfile() : " + this.mBluetoothHeadset);
        if (getHeadsetProxy() != null) {
            if (BluetoothUtil.getAdapter() != null) {
                BluetoothUtil.getAdapter().closeProfileProxy(1, getHeadsetProxy());
            }
            setHeadsetProxy((BluetoothHeadset) null);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void closeA2dpProfile() {
        Log.d(TAG, "closeA2dpProfile() : " + this.mBluetoothA2dp);
        if (getA2dpProxy() != null) {
            if (BluetoothUtil.getAdapter() != null) {
                BluetoothUtil.getAdapter().closeProfileProxy(2, getA2dpProxy());
            }
            setA2dpProxy((BluetoothA2dp) null);
        }
    }

    public void rebindProfiles() {
        Log.d(TAG, "rebindProfiles() : " + this.mReady + " (" + this.mBluetoothHeadset + " / " + this.mBluetoothA2dp + ")");
        if (BluetoothUtil.isAdapterOn()) {
            bindHeadsetProfile();
            bindA2dpProfile();
            return;
        }
        Log.e(TAG, "rebindProfiles() : BluetoothUtil.isAdapterOn() == false");
    }

    /* access modifiers changed from: private */
    public synchronized void setHeadsetProxy(BluetoothHeadset bluetoothHeadset) {
        this.mBluetoothHeadset.set(bluetoothHeadset);
        Log.i(TAG, "mBluetoothHeadset = " + this.mBluetoothHeadset);
    }

    public synchronized BluetoothHeadset getHeadsetProxy() {
        return this.mBluetoothHeadset.get();
    }

    /* access modifiers changed from: private */
    public synchronized void setA2dpProxy(BluetoothA2dp bluetoothA2dp) {
        this.mBluetoothA2dp.set(bluetoothA2dp);
        Log.i(TAG, "mBluetoothA2dp = " + this.mBluetoothA2dp);
    }

    public synchronized BluetoothA2dp getA2dpProxy() {
        return this.mBluetoothA2dp.get();
    }

    public synchronized boolean isReady() {
        Log.d(TAG, "isReady() : " + this.mReady + " (" + this.mBluetoothHeadset + " / " + this.mBluetoothA2dp + ")");
        return this.mReady;
    }

    /* access modifiers changed from: private */
    public synchronized void setReady(boolean z) {
        Log.d(TAG, "setReady() : " + z + " (" + this.mBluetoothHeadset + " / " + this.mBluetoothA2dp + ")");
        this.mReady = z;
    }

    public synchronized int getHeadsetState(BluetoothDevice bluetoothDevice) {
        if (getHeadsetProxy() == null) {
            return 0;
        }
        return getHeadsetProxy().getConnectionState(bluetoothDevice);
    }

    public synchronized int getA2dpState(BluetoothDevice bluetoothDevice) {
        if (getA2dpProxy() == null) {
            return 0;
        }
        return getA2dpProxy().getConnectionState(bluetoothDevice);
    }

    public boolean isProfileConnecting(BluetoothDevice bluetoothDevice) {
        return isHeadsetConnecting(bluetoothDevice) || isA2dpConnecting(bluetoothDevice);
    }

    public boolean isHeadsetConnecting(BluetoothDevice bluetoothDevice) {
        return BluetoothUtil.isConnecting(getHeadsetState(bluetoothDevice));
    }

    public boolean isA2dpConnecting(BluetoothDevice bluetoothDevice) {
        return BluetoothUtil.isConnecting(getA2dpState(bluetoothDevice));
    }

    public boolean isHeadsetDisconnecting(BluetoothDevice bluetoothDevice) {
        return BluetoothUtil.isDisconnecting(getHeadsetState(bluetoothDevice));
    }

    public boolean isA2dpDisconnecting(BluetoothDevice bluetoothDevice) {
        return BluetoothUtil.isDisconnecting(getA2dpState(bluetoothDevice));
    }

    public synchronized boolean connectA2dp(BluetoothDevice bluetoothDevice) {
        if (getA2dpProxy() == null) {
            Log.e(TAG, "connectA2dp() : mBluetoothA2dp == null !!!");
            return false;
        }
        return seccompat.android.bluetooth.BluetoothA2dp.proxyConnect(getA2dpProxy(), bluetoothDevice);
    }

    public synchronized boolean disconnectA2dp(BluetoothDevice bluetoothDevice) {
        if (getA2dpProxy() == null) {
            Log.e(TAG, "disconnectA2dp() : mBluetoothA2dp == null !!!");
            return false;
        }
        return seccompat.android.bluetooth.BluetoothA2dp.proxyDisconnect(getA2dpProxy(), bluetoothDevice);
    }

    public synchronized boolean connectHeadset(BluetoothDevice bluetoothDevice) {
        if (getHeadsetProxy() == null) {
            Log.e(TAG, "connectHeadset() : mBluetoothHeadset == null !!!");
            return false;
        }
        return seccompat.android.bluetooth.BluetoothHeadset.proxyConnect(getHeadsetProxy(), bluetoothDevice);
    }

    public synchronized boolean disconnectHeadset(BluetoothDevice bluetoothDevice) {
        if (getHeadsetProxy() == null) {
            Log.e(TAG, "disconnectHeadset() : disconnectHeadset == null !!!");
            return false;
        }
        return seccompat.android.bluetooth.BluetoothHeadset.proxyDisconnect(getHeadsetProxy(), bluetoothDevice);
    }

    static class InstanceLogger<T> {
        private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.US);
        private T mInstance;
        private long mUpdatedMillis;

        InstanceLogger() {
        }

        public T get() {
            return this.mInstance;
        }

        public void set(T t) {
            this.mInstance = t;
            this.mUpdatedMillis = System.currentTimeMillis();
        }

        public synchronized String toString() {
            return Util.toSimpleString(this.mInstance) + " [" + DATE_FORMAT.format(Long.valueOf(this.mUpdatedMillis)) + "]";
        }
    }
}
