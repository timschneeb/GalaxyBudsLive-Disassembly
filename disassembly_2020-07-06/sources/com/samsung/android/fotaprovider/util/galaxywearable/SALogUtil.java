package com.samsung.android.fotaprovider.util.galaxywearable;

import android.app.Application;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.context.sdk.samsunganalytics.LogBuilders;
import com.samsung.context.sdk.samsunganalytics.SamsungAnalytics;

public class SALogUtil {
    private static final String SA_FOTA_DOWNLOAD_LATER_EVENT = "7003";
    private static final String SA_FOTA_DOWNLOAD_NOW_EVENT = "7004";
    private static final String SA_FOTA_DOWNLOAD_SCREEN_ID = "701";
    private static final String SA_FOTA_INSTALL_LATER_EVENT = "7005";
    private static final String SA_FOTA_INSTALL_NOW_EVENT = "7006";
    private static final String SA_FOTA_INSTALL_SCREEN_ID = "702";
    private static final String SA_FOTA_SCHEDULE_INSTALL_EVENT = "7007";
    private static final String SA_FOTA_UP_BUTTON_EVENT = "1000";
    private static final String SA_OPLUGIN_UI_VER = "2.8.1";
    private static final String SA_TRACKING_ID = "703-399-564897";

    public static void setConfiguration(Application application) {
    }

    public static void loggingDownloadUpButton() {
        sendLog(SA_FOTA_DOWNLOAD_SCREEN_ID, "1000");
    }

    public static void loggingDownloadLaterButton() {
        sendLog(SA_FOTA_DOWNLOAD_SCREEN_ID, "7003");
    }

    public static void loggingDownloadNowButton() {
        sendLog(SA_FOTA_DOWNLOAD_SCREEN_ID, SA_FOTA_DOWNLOAD_NOW_EVENT);
    }

    public static void loggingInstallUpButton() {
        sendLog(SA_FOTA_INSTALL_SCREEN_ID, "1000");
    }

    public static void loggingInstallLaterButton() {
        sendLog(SA_FOTA_INSTALL_SCREEN_ID, SA_FOTA_INSTALL_LATER_EVENT);
    }

    public static void loggingScheduleInstallButton() {
        sendLog(SA_FOTA_INSTALL_SCREEN_ID, SA_FOTA_SCHEDULE_INSTALL_EVENT);
    }

    public static void loggingInstallNowButton() {
        sendLog(SA_FOTA_INSTALL_SCREEN_ID, SA_FOTA_INSTALL_NOW_EVENT);
    }

    private static void sendLog(String str, String str2) {
        Log.D("screenID = " + str + " / event = " + str2);
        try {
            SamsungAnalytics.getInstance().sendLog(((LogBuilders.EventBuilder) new LogBuilders.EventBuilder().setScreenView(str)).setEventName(str2).build());
        } catch (Exception e) {
            Log.E("error: " + e);
            e.printStackTrace();
        }
    }
}
