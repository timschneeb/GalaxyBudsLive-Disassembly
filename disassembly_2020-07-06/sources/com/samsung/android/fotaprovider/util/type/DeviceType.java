package com.samsung.android.fotaprovider.util.type;

import android.content.Context;
import com.accessorydm.ui.downloadandinstallconfirm.DownloadAndInstallConfirmActivity;
import com.accessorydm.ui.downloadconfirm.XUIDownloadConfirmActivity;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.FotaProviderUtil;
import com.samsung.android.fotaprovider.util.galaxywearable.Settings;

public enum DeviceType {
    WATCH {
        public long getWaitingMillisForUpdateResult() {
            return 90000;
        }

        public boolean isWifiOnlySettings(Context context) {
            return false;
        }

        public NotificationIconType getNotificationIconType() {
            return NotificationIconType.WATCH;
        }
    },
    GEARFIT2 {
        public boolean isWifiOnlySettings(Context context) {
            return false;
        }

        public NotificationIconType getNotificationIconType() {
            return NotificationIconType.FIT2;
        }
    },
    EARBUDS {
        public boolean isSupportWifiOnlyFlagByServer() {
            return false;
        }

        public boolean isWifiAutoDownloadSettings(Context context) {
            return false;
        }

        public boolean shouldShowUpdateConfirmUI() {
            return false;
        }

        public TextType getTextType() {
            return TextType.EARBUDS;
        }

        public NotificationIconType getNotificationIconType() {
            return NotificationIconType.EARBUDS;
        }

        public Class getDownloadConfirmActivity() {
            return DownloadAndInstallConfirmActivity.class;
        }
    };
    
    public static final int INVALID_ID = -1;
    private static DeviceType deviceType;
    private String FOTA_PROVIDER_NETWORK_SETTINGS_STATE;
    int FOTA_PROVIDER_STATE_DEFAULT;
    int FOTA_PROVIDER_STATE_OFF;
    int FOTA_PROVIDER_STATE_ON;
    private String FOTA_PROVIDER_WIFI_AUTO_DOWNLOAD_SETTINGS_STATE;

    public abstract NotificationIconType getNotificationIconType();

    public long getWaitingMillisForUpdateResult() {
        return 15000;
    }

    public boolean isPollingSupported() {
        return true;
    }

    public boolean isSupportWifiOnlyFlagByServer() {
        return true;
    }

    public boolean shouldShowUpdateConfirmUI() {
        return true;
    }

    static {
        deviceType = FotaProviderUtil.getDeviceType();
    }

    public static DeviceType get() {
        return deviceType;
    }

    public static void reloadDeviceType() {
        deviceType = FotaProviderUtil.getDeviceType();
        Log.I("reload deviceType: " + deviceType);
    }

    public TextType getTextType() {
        return TextType.WATCH;
    }

    public boolean isWifiAutoDownloadSettings(Context context) {
        return context != null && Settings.System.getInt(context.getContentResolver(), this.FOTA_PROVIDER_WIFI_AUTO_DOWNLOAD_SETTINGS_STATE, this.FOTA_PROVIDER_STATE_DEFAULT) == this.FOTA_PROVIDER_STATE_ON;
    }

    public boolean isWifiOnlySettings(Context context) {
        return context != null && Settings.System.getInt(context.getContentResolver(), this.FOTA_PROVIDER_NETWORK_SETTINGS_STATE, this.FOTA_PROVIDER_STATE_DEFAULT) == this.FOTA_PROVIDER_STATE_ON;
    }

    public void setDefaultSettings(Context context) {
        if (context != null) {
            if (Settings.System.getInt(context.getContentResolver(), this.FOTA_PROVIDER_WIFI_AUTO_DOWNLOAD_SETTINGS_STATE, this.FOTA_PROVIDER_STATE_DEFAULT) == this.FOTA_PROVIDER_STATE_DEFAULT) {
                Settings.System.putInt(context.getContentResolver(), this.FOTA_PROVIDER_WIFI_AUTO_DOWNLOAD_SETTINGS_STATE, this.FOTA_PROVIDER_STATE_ON);
            }
            if (Settings.System.getInt(context.getContentResolver(), this.FOTA_PROVIDER_NETWORK_SETTINGS_STATE, this.FOTA_PROVIDER_STATE_DEFAULT) == this.FOTA_PROVIDER_STATE_DEFAULT) {
                Settings.System.putInt(context.getContentResolver(), this.FOTA_PROVIDER_NETWORK_SETTINGS_STATE, this.FOTA_PROVIDER_STATE_OFF);
            }
        }
    }

    public Class getDownloadConfirmActivity() {
        return XUIDownloadConfirmActivity.class;
    }
}
