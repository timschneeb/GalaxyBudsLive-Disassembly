package com.samsung.accessory.neobeanmgr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.os.EnvironmentCompat;
import com.samsung.accessory.neobeanmgr.core.aom.AomManager;
import com.samsung.accessory.neobeanmgr.core.appwidget.WidgetManager;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.bixby.BixbyActionHandler;
import com.samsung.accessory.neobeanmgr.core.bluetooth.BluetoothManager;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.RingManager;
import com.samsung.accessory.neobeanmgr.core.fota.manager.FotaProviderApplication;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationCoreService;
import com.samsung.accessory.neobeanmgr.core.service.BudsLogManager;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.MainService;
import com.samsung.accessory.neobeanmgr.core.uhmdb.UhmDatabase;
import oreocompat.AppNotificationChannels;
import seccompat.android.os.SystemProperties;

public class Application extends android.app.Application {
    public static final boolean DEBUG_MODE = (SystemProperties.getInt("ro.debuggable", 0) == 1);
    public static final String DEVICE_NAME = "Galaxy Buds Live";
    public static final String DEVICE_NAME_COMPAT = "Galaxy Bean";
    public static final boolean EMULATOR_MODE;
    public static final String PERMISSION_SIGNATURE = "com.samsung.accessory.neobeanmgr.permission.SIGNATURE";
    private static final String TAG = "NeoBean_Application";
    public static final String TAG_ = "NeoBean_";
    private static AomManager sAomManager;
    private static BluetoothManager sBluetoothManager;
    private static Context sContext;
    private static CoreService sCoreService;
    /* access modifiers changed from: private */
    public static MainService sMainService;
    private static NotificationCoreService sNotificationCoreService;
    private static UhmDatabase sUhmDatabase;
    private static WidgetManager sWidgetManager;
    private final ServiceConnection mMainServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MainService unused = Application.sMainService = ((MainService.Binder) iBinder).getService();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            MainService unused = Application.sMainService = null;
        }
    };

    static {
        boolean z = false;
        if (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith(EnvironmentCompat.MEDIA_UNKNOWN) || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || ((Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || "google_sdk".equals(Build.PRODUCT))) {
            z = true;
        }
        EMULATOR_MODE = z;
    }

    public void onCreate() {
        Log.d(TAG, "onCreate()");
        sContext = this;
        super.onCreate();
        sBluetoothManager = new BluetoothManager(this);
        sCoreService = new CoreService(this);
        sNotificationCoreService = new NotificationCoreService(this);
        sUhmDatabase = new UhmDatabase(this);
        sWidgetManager = new WidgetManager(this);
        sAomManager = new AomManager(this);
        RingManager.registerReceiver(this);
        AppNotificationChannels.register(this);
        MainService.startService();
        bindMainService();
        SamsungAnalyticsUtil.init(this);
        BixbyActionHandler.initialize(this);
        BudsLogManager.getInstance();
        FotaProviderApplication.init(this);
        Log.d(TAG, "onCreate()_end");
    }

    public void onTerminate() {
        Log.d(TAG, "onTerminate()");
        sCoreService.onDestroy();
        sBluetoothManager.destroy();
        sNotificationCoreService.onDestroy();
        sUhmDatabase.destroy();
        sWidgetManager.onDestroy();
        sAomManager.destroy();
        RingManager.unregisterReceiver(this);
        unbindMainService();
        BudsLogManager.getInstance().destroy();
        FotaProviderApplication.terminate(this);
        super.onTerminate();
    }

    public void onConfigurationChanged(Configuration configuration) {
        Log.d(TAG, "onConfigurationChanged()");
        super.onConfigurationChanged(configuration);
    }

    public void onLowMemory() {
        Log.d(TAG, "onLowMemory()");
        super.onLowMemory();
    }

    public void onTrimMemory(int i) {
        Log.d(TAG, "onTrimMemory() : level=" + i);
        super.onTrimMemory(i);
    }

    public static Context getContext() {
        return sContext;
    }

    public static BluetoothManager getBluetoothManager() {
        return sBluetoothManager;
    }

    public static CoreService getCoreService() {
        return sCoreService;
    }

    public static NotificationCoreService getNotificationCoreService() {
        return sNotificationCoreService;
    }

    public static MainService getMainService() {
        return sMainService;
    }

    public static UhmDatabase getUhmDatabase() {
        return sUhmDatabase;
    }

    public static AomManager getAomManager() {
        return sAomManager;
    }

    private void bindMainService() {
        Log.d(TAG, "bindMainService()");
        bindService(new Intent(this, MainService.class), this.mMainServiceConnection, 65);
    }

    private void unbindMainService() {
        Log.d(TAG, "unbindMainService()");
        unbindService(this.mMainServiceConnection);
    }
}
