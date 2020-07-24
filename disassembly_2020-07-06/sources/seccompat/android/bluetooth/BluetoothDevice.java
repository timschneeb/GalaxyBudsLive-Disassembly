package seccompat.android.bluetooth;

import seccompat.Reflection;

public class BluetoothDevice {
    public static String proxyGetAliasName(android.bluetooth.BluetoothDevice bluetoothDevice) {
        try {
            return (String) Reflection.callMethod(bluetoothDevice, "getAliasName", new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String proxyGetAlias(android.bluetooth.BluetoothDevice bluetoothDevice) {
        try {
            return (String) Reflection.callMethod(bluetoothDevice, "getAlias", new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean proxySetAlias(android.bluetooth.BluetoothDevice bluetoothDevice, String str) {
        try {
            Object callMethod = Reflection.callMethod(bluetoothDevice, "semSetAlias", str);
            if (callMethod != null) {
                return ((Boolean) callMethod).booleanValue();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
