package com.accessorydm.adapter;

import android.text.TextUtils;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.db.file.AccessoryInfoAdapter;
import com.accessorydm.interfaces.XDMDefInterface;
import com.accessorydm.interfaces.XDMInterface;
import com.accessorydm.interfaces.XFOTAInterface;
import com.accessorydm.tp.urlconnect.HttpNetworkInterface;
import com.samsung.accessory.fotaprovider.AccessoryController;
import com.samsung.android.fotaprovider.deviceinfo.ProviderInfo;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.NetworkUtil;
import java.io.BufferedReader;
import java.io.FileReader;

public class XDMDevinfAdapter implements XDMDefInterface, XDMInterface, XFOTAInterface {
    private static final String DEFAULT_DEVID_VALUE = "Default";
    private static final String DEFAULT_NULL_DEVID_VALUE = "000000000000000";
    private static final String DEFAULT_NULL_DEVID_VALUE2 = "B0000000";

    public static String xdmDevAdpGetManufacturer() {
        return "Samsung";
    }

    public static String xdmDevAdpGetDeviceID() {
        String deviceId = new AccessoryInfoAdapter().getDeviceId();
        if (deviceId.contains("TWID:") || deviceId.contains("MEID:") || deviceId.contains("IMEI:")) {
            return deviceId.substring(5);
        }
        return "";
    }

    public static String xdmDevAdpGetFullDeviceID() {
        String deviceId = new AccessoryInfoAdapter().getDeviceId();
        Log.H(deviceId);
        return deviceId;
    }

    public static String xdmDevAdpGetModel() {
        return new AccessoryInfoAdapter().getModelNumber();
    }

    public static String xdmDevAdpGetLanguage() {
        String xdmGetTargetLanguage = XDMTargetAdapter.xdmGetTargetLanguage();
        return TextUtils.isEmpty(xdmGetTargetLanguage) ? XDMInterface.XDM_DEVINFO_DEFAULT_LANG : xdmGetTargetLanguage;
    }

    public static String xdmDevAdpGetTelephonyMcc() {
        if (XDMFeature.XDM_FEATURE_WIFI_ONLY_MODEL) {
            return "";
        }
        String networkMCC = new ProviderInfo().getNetworkMCC();
        Log.H("Network MCC: " + networkMCC);
        return networkMCC;
    }

    public static String xdmDevAdpGetTelephonyMnc() {
        if (XDMFeature.XDM_FEATURE_WIFI_ONLY_MODEL) {
            return "";
        }
        String networkMNC = new ProviderInfo().getNetworkMNC();
        Log.H("Network MNC: " + networkMNC);
        return networkMNC;
    }

    public static String xdmDevAdpGetAppVersion() {
        return new ProviderInfo().getAppVersion();
    }

    public static String xdmDevAdpGetFirmwareVersion() {
        return new AccessoryInfoAdapter().getFirmwareVersion();
    }

    public static String xdmDevAdpGetSalesCode() {
        return new ProviderInfo().getSalesCode();
    }

    public static String xdmDevAdpGetHttpUserAgent() {
        return String.format("%s %s %s", new Object[]{xdmDevAdpGetManufacturer(), xdmDevAdpGetModel(), HttpNetworkInterface.XTP_HTTP_DM_USER_AGENT});
    }

    public static boolean xdmDevAdpBatteryLifeCheck() {
        int xdmGetIntFromFile = xdmGetIntFromFile("/sys/class/power_supply/battery/capacity");
        int minimumBatteryLevel = AccessoryController.getInstance().getAccessoryUtil().getMinimumBatteryLevel();
        Log.I("battery level [" + xdmGetIntFromFile + "], Minimum Level [" + minimumBatteryLevel + "]\n");
        return xdmGetIntFromFile >= minimumBatteryLevel;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0053 A[SYNTHETIC, Splitter:B:22:0x0053] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0059 A[SYNTHETIC, Splitter:B:26:0x0059] */
    private static int xdmGetIntFromFile(String str) {
        int i = 0;
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(str), 4096);
            try {
                String readLine = bufferedReader2.readLine();
                if (readLine != null && readLine.length() > 0) {
                    i = Integer.parseInt(readLine);
                }
            } catch (Exception e) {
                BufferedReader bufferedReader3 = bufferedReader2;
                e = e;
                bufferedReader = bufferedReader3;
                try {
                    Log.E(e.toString());
                    Log.E("Can't open " + str);
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    return i;
                } catch (Throwable th) {
                    th = th;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Exception e2) {
                            Log.E(e2.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                }
                throw th;
            }
            try {
                bufferedReader2.close();
            } catch (Exception e3) {
                Log.E(e3.toString());
            }
        } catch (Exception e4) {
            e = e4;
            Log.E(e.toString());
            Log.E("Can't open " + str);
            if (bufferedReader != null) {
            }
            return i;
        }
        return i;
    }

    public static boolean xdmDevAdpVerifyDevID() {
        String xdmDevAdpGetDeviceID = xdmDevAdpGetDeviceID();
        return !TextUtils.isEmpty(xdmDevAdpGetDeviceID) && !DEFAULT_NULL_DEVID_VALUE.equals(xdmDevAdpGetDeviceID) && !DEFAULT_NULL_DEVID_VALUE2.equals(xdmDevAdpGetDeviceID) && !DEFAULT_DEVID_VALUE.equals(xdmDevAdpGetDeviceID);
    }

    public static boolean xdmBlocksDueToRoamingNetwork() {
        if (!XDMDmUtils.getInstance().XDM_ROAMING_CHECK || !NetworkUtil.isRoamingNetworkConnected(XDMDmUtils.getContext()) || NetworkUtil.isWiFiNetworkConnected(XDMDmUtils.getContext())) {
            return false;
        }
        Log.I("Roaming & WiFi-Disconnected. return true");
        return true;
    }
}
