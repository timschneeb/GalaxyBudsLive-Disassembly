package com.samsung.accessory.neobeanmgr.core.appwidget;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WallpaperColorManager;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetConstants;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;

public class WidgetManager {
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + WidgetManager.class.getSimpleName());
    /* access modifiers changed from: private */
    public Context mContext;
    private BroadcastReceiver mWidgetReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Can't fix incorrect switch cases order */
        public void onReceive(final Context context, Intent intent) {
            char c;
            String access$000 = WidgetManager.TAG;
            Log.d(access$000, "onReceive : " + intent.getAction());
            String action = intent.getAction();
            switch (action.hashCode()) {
                case -2043421558:
                    if (action.equals(CoreService.ACTION_MSG_ID_STATUS_UPDATED)) {
                        c = 4;
                        break;
                    }
                case -1645270254:
                    if (action.equals(WidgetConstants.ACTION_WALLPAPER_CHANGED)) {
                        c = 7;
                        break;
                    }
                case -1354974214:
                    if (action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) {
                        c = 1;
                        break;
                    }
                case -1314239911:
                    if (action.equals(CoreService.ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED)) {
                        c = 6;
                        break;
                    }
                case -415576694:
                    if (action.equals(CoreService.ACTION_DEVICE_CONNECTED)) {
                        c = 0;
                        break;
                    }
                case -145626792:
                    if (action.equals(CoreService.ACTION_MSG_ID_EXTENDED_STATUS_UPDATED)) {
                        c = 2;
                        break;
                    }
                case 158859398:
                    if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                        c = 9;
                        break;
                    }
                case 1150598536:
                    if (action.equals(WidgetConstants.ACTION_SEC_WALLPAPER_CHANGED)) {
                        c = 8;
                        break;
                    }
                case 1174571750:
                    if (action.equals("android.bluetooth.device.action.ALIAS_CHANGED")) {
                        c = 3;
                        break;
                    }
                case 1936469230:
                    if (action.equals(CoreService.ACTION_MSG_ID_NOISE_REDUCTION_UPDATED)) {
                        c = 5;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                    WallpaperColorManager.initWallpaperColor(context);
                    WidgetUtil.updateWidgetProvider(context);
                    return;
                case 4:
                    WidgetUtil.updateWidgetProvider(context, WidgetBatteryProvider.class);
                    return;
                case 5:
                case 6:
                    WidgetUtil.updateWidgetProvider(context, WidgetMasterProvider.class);
                    return;
                case 7:
                case 8:
                    WallpaperColorManager.initWallpaperColor(context);
                    return;
                case 9:
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            WidgetUtil.updateWidgetProvider(context);
                        }
                    }, 1000);
                    return;
                default:
                    return;
            }
        }
    };
    private WallpaperManager.OnColorsChangedListener wallpaperColorManager;
    private WallpaperManager wallpaperManager;

    public WidgetManager(Context context) {
        this.mContext = context;
        onCreate();
    }

    public void onCreate() {
        registerReceiver();
        registerWallpaperCallback();
    }

    public void onDestroy() {
        unregisterReceiver();
        unregisterWallpaperCallback();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.ALIAS_CHANGED");
        intentFilter.addAction(CoreService.ACTION_DEVICE_CONNECTED);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_EXTENDED_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_NOISE_REDUCTION_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED);
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        if (Util.isSamsungDevice()) {
            intentFilter.addAction(WidgetConstants.ACTION_SEC_WALLPAPER_CHANGED);
        } else {
            intentFilter.addAction(WidgetConstants.ACTION_WALLPAPER_CHANGED);
        }
        this.mContext.registerReceiver(this.mWidgetReceiver, intentFilter);
    }

    private void registerWallpaperCallback() {
        if (Build.VERSION.SDK_INT >= 27) {
            this.wallpaperColorManager = new WallpaperManager.OnColorsChangedListener() {
                public void onColorsChanged(WallpaperColors wallpaperColors, int i) {
                    if (wallpaperColors != null) {
                        String access$000 = WidgetManager.TAG;
                        Log.d(access$000, "onWallpaperColorChanged : " + wallpaperColors.getPrimaryColor());
                        WallpaperColorManager.initWallpaperColor(WidgetManager.this.mContext);
                    }
                }
            };
            this.wallpaperManager = WallpaperManager.getInstance(this.mContext);
            WallpaperManager wallpaperManager2 = this.wallpaperManager;
            if (wallpaperManager2 != null) {
                wallpaperManager2.addOnColorsChangedListener(this.wallpaperColorManager, new Handler(Looper.getMainLooper()));
            }
        }
    }

    private void unregisterReceiver() {
        this.mContext.unregisterReceiver(this.mWidgetReceiver);
    }

    private void unregisterWallpaperCallback() {
        WallpaperManager.OnColorsChangedListener onColorsChangedListener;
        WallpaperManager wallpaperManager2;
        if (Build.VERSION.SDK_INT >= 27 && (onColorsChangedListener = this.wallpaperColorManager) != null && (wallpaperManager2 = this.wallpaperManager) != null) {
            wallpaperManager2.removeOnColorsChangedListener(onColorsChangedListener);
        }
    }
}
