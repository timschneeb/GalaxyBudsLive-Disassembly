package com.accessorydm.ui.notification.manager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.ui.notification.XUINotificationManager;
import com.samsung.android.fotaprovider.FotaProviderInitializer;
import com.samsung.android.fotaprovider.log.Log;

public class NotificationTypeManager {
    public static void notify(NotificationType notificationType) {
        Intent intent = new Intent(getContext(), notificationType.getNotificationStrategy().getNotificationId().getNotificationTypeManagerServiceClass());
        intent.putExtra("OPERATION_KEY", "OPERATION_NOTIFY");
        intent.putExtra(NotificationTypeManagerService.NOTIFICATION_TYPE_KEY, notificationType);
        startService(intent);
    }

    public static void cancel(NotificationType notificationType) {
        NotificationId notificationId = notificationType.getNotificationStrategy().getNotificationId();
        if (notificationType == NotificationType.XUI_INDICATOR_NONE) {
            return;
        }
        if (notificationType.getNotificationStrategy().isForegroundService()) {
            Log.I("Foreground Notification cancel - current : " + notificationId + "(2) : " + notificationType);
            Intent intent = new Intent(getContext(), notificationId.getNotificationTypeManagerServiceClass());
            intent.putExtra("OPERATION_KEY", "OPERATION_CANCEL");
            intent.putExtra(NotificationTypeManagerService.NOTIFICATION_TYPE_KEY, notificationType);
            startService(intent);
            return;
        }
        cancelBackgroundNotification(notificationId);
    }

    public static void cancel(NotificationId notificationId) {
        NotificationType notificationType = notificationId.getNotificationType();
        if (notificationType != NotificationType.XUI_INDICATOR_NONE && notificationId == notificationType.getNotificationStrategy().getNotificationId()) {
            if (notificationType.getNotificationStrategy().isForegroundService()) {
                Log.I("Foreground Notification cancel - current : " + notificationId + "(2) : " + notificationType);
                Intent intent = new Intent(getContext(), notificationId.getNotificationTypeManagerServiceClass());
                intent.putExtra("OPERATION_KEY", "OPERATION_CANCEL");
                intent.putExtra(NotificationTypeManagerService.NOTIFICATION_ID_KEY, notificationId);
                startService(intent);
                return;
            }
            cancelBackgroundNotification(notificationId);
        }
    }

    private static void cancelBackgroundNotification(NotificationId notificationId) {
        NotificationType notificationType = notificationId.getNotificationType();
        Log.I("Background Notification cancel - current : " + notificationId + "(3) : " + notificationType);
        getNotificationManager().cancel(notificationId.getId(false));
        XUINotificationManager.getInstance().xuiSetNotificationType(NotificationType.XUI_INDICATOR_NONE);
    }

    public static void cancelAll() {
        Log.I("");
        for (NotificationId notificationId : NotificationId.values()) {
            if (notificationId != NotificationId.XDM_NOTIFICATION_ID_NONE) {
                cancel(notificationId);
            }
        }
    }

    private static Context getContext() {
        return FotaProviderInitializer.getContext();
    }

    private static NotificationManager getNotificationManager() {
        return (NotificationManager) XDMDmUtils.getInstance().xdmGetServiceManager("notification");
    }

    private static void startService(Intent intent) {
        if (Build.VERSION.SDK_INT < 26) {
            getContext().startService(intent);
        } else {
            getContext().startForegroundService(intent);
        }
    }
}
