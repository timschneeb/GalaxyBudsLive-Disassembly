package com.samsung.accessory.neobeanmgr.core.appwidget.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.common.ui.SingleToast;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.util.BluetoothUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLockTouchpad;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetNoiseReduction;
import com.samsung.accessory.neobeanmgr.module.LaunchActivity;

public class WidgetUtil {
    private static final int EARBUD_PLACEMENT_IN_OPEN_CASE = 3;

    public static int makeAlphaColor(int i, int i2) {
        return (i & ViewCompat.MEASURED_SIZE_MASK) | (i2 << 24);
    }

    public static void updateWidgetProvider(Context context) {
        sendPermissionBroadcast(context, new Intent("android.appwidget.action.APPWIDGET_UPDATE"));
    }

    public static void updateWidgetProvider(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendPermissionBroadcast(context, intent);
    }

    public static String getDeviceAliasName(Context context) {
        String str;
        String lastLaunchDeviceId = UhmFwUtil.getLastLaunchDeviceId();
        if (lastLaunchDeviceId != null) {
            str = BluetoothUtil.getAliasName(lastLaunchDeviceId);
            if (str == null) {
                str = Application.getUhmDatabase().getDeviceName(lastLaunchDeviceId);
            }
        } else {
            str = null;
        }
        return str == null ? context.getString(R.string.app_name) : str;
    }

    public static void sendPermissionBroadcast(Context context, Intent intent) {
        context.sendBroadcast(intent, "com.samsung.accessory.neobeanmgr.permission.SIGNATURE");
    }

    public static boolean isDeviceDarkMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & 48) == 32;
    }

    public static boolean isConnected(Context context) {
        return Application.getCoreService().isConnected();
    }

    public static boolean isConnectedLeftDevice(Context context) {
        return isConnected(context) && Application.getCoreService().getEarBudsInfo().batteryL > 0;
    }

    public static boolean isConnectedRightDevice(Context context) {
        return isConnected(context) && Application.getCoreService().getEarBudsInfo().batteryR > 0;
    }

    public static boolean isConnectedCradle(Context context) {
        CoreService coreService = Application.getCoreService();
        boolean isExtendedStatusReady = coreService.isExtendedStatusReady();
        boolean z = coreService.getEarBudsInfo().placementL >= 3 || coreService.getEarBudsInfo().placementR >= 3;
        if (!isConnected(context) || !isExtendedStatusReady || !z) {
            return false;
        }
        return true;
    }

    public static boolean isCommonBattery(Context context) {
        return isConnected(context) && Application.getCoreService().getEarBudsInfo().batteryI > 0;
    }

    public static int getBatteryGaugeLeft(Context context) {
        return Application.getCoreService().getEarBudsInfo().batteryL;
    }

    public static int getBatteryGaugeRight(Context context) {
        return Application.getCoreService().getEarBudsInfo().batteryR;
    }

    public static int getBatteryGaugeCommon(Context context) {
        return Application.getCoreService().getEarBudsInfo().batteryI;
    }

    public static int getBatteryGaugeCradle(Context context) {
        return Application.getCoreService().getEarBudsInfo().batteryCase;
    }

    public static void setNoiseReduction(Context context, boolean z) {
        Application.getCoreService().getEarBudsInfo().noiseReduction = z;
        Application.getCoreService().sendSppMessage(new MsgSetNoiseReduction(z));
    }

    public static void setTouchpadLock(Context context, boolean z) {
        Application.getCoreService().getEarBudsInfo().touchpadLocked = z;
        Application.getCoreService().sendSppMessage(new MsgLockTouchpad(z));
    }

    public static void showSingleToast(Context context, int i) {
        SingleToast.show(context, context.getResources().getString(i), 0);
    }

    public static boolean getNoiseReductionEnabled(Context context) {
        return Application.getCoreService().getEarBudsInfo().noiseReduction;
    }

    public static boolean getTouchpadLockEnabled(Context context) {
        return Application.getCoreService().getEarBudsInfo().touchpadLocked;
    }

    public static boolean isWhiteWallpaper(Context context) {
        return WallpaperColorManager.getInstance(context).isWhiteWallpaper();
    }

    public static int getWallpaperColor(Context context) {
        return isWhiteWallpaper(context) ? -1 : -16777216;
    }

    public static int getWidgetBgColor(Context context, WidgetInfo widgetInfo) {
        if (!isDeviceDarkMode(context) || !widgetInfo.darkmode) {
            return widgetInfo.color;
        }
        return -16777216;
    }

    public static int getWidgetColor(Context context, WidgetInfo widgetInfo) {
        if (widgetInfo.alpha >= 50) {
            return getWallpaperColor(context);
        }
        return getWidgetBgColor(context, widgetInfo);
    }

    public static void startActivity(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("deviceid", UhmFwUtil.getLastLaunchDeviceId());
        Intent intent = new Intent(context, LaunchActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(268468224);
        context.startActivity(intent);
    }

    public static float DP_TO_PX(float f) {
        return UiUtil.DP_TO_PX(f);
    }
}
