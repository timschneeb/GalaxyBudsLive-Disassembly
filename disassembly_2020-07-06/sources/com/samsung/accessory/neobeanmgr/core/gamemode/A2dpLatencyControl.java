package com.samsung.accessory.neobeanmgr.core.gamemode;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import seccompat.Reflection;

public class A2dpLatencyControl {
    private static final String TAG = "NeoBean_A2dpLatencyControl";

    public static boolean isSupportDevice() {
        boolean containsMethod = Reflection.containsMethod(BluetoothA2dp.class, "semSetLatency", Integer.class);
        Log.d(TAG, "isSupportDevice() : " + containsMethod);
        return containsMethod;
    }

    public static void setA2dpLatency(boolean z) {
        Log.d(TAG, "setA2dpLatency() : " + z);
        BluetoothA2dp a2dpProxy = Application.getBluetoothManager().getA2dpProxy();
        BluetoothDevice connectedDevice = Application.getCoreService().getConnectedDevice();
        if (a2dpProxy != null && connectedDevice != null) {
            seccompat.android.bluetooth.BluetoothA2dp.proxySemSetLatency(a2dpProxy, connectedDevice, z ? 1 : 0);
        }
    }
}
