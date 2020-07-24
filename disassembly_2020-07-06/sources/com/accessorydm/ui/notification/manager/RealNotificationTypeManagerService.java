package com.accessorydm.ui.notification.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.XDMServiceManager;
import com.accessorydm.ui.notification.XUINotificationManager;
import com.samsung.android.fotaprovider.log.Log;

abstract class RealNotificationTypeManagerService extends Service implements NotificationTypeManagerService {
    private Intent intent;

    public IBinder onBind(Intent intent2) {
        return null;
    }

    RealNotificationTypeManagerService() {
    }

    public void onCreate() {
        super.onCreate();
        Log.I(getClass().getSimpleName());
        XDMServiceManager.getInstance().xdmNotifyObserver(1);
    }

    public int onStartCommand(Intent intent2, int i, int i2) {
        this.intent = intent2;
        String stringExtra = intent2.getStringExtra("OPERATION_KEY");
        if (stringExtra == null) {
            Log.W(getClass().getSimpleName() + " - operation should not be null");
            return 2;
        }
        char c = 65535;
        int hashCode = stringExtra.hashCode();
        if (hashCode != -291369870) {
            if (hashCode == 36664641 && stringExtra.equals("OPERATION_NOTIFY")) {
                c = 0;
            }
        } else if (stringExtra.equals("OPERATION_CANCEL")) {
            c = 1;
        }
        if (c == 0) {
            notifyNotification();
        } else if (c != 1) {
            Log.W(getClass().getSimpleName() + " - unexpected operation: " + stringExtra);
        } else {
            cancelNotification();
        }
        return 2;
    }

    private void notifyNotification() {
        NotificationType notificationType = (NotificationType) this.intent.getSerializableExtra(NotificationTypeManagerService.NOTIFICATION_TYPE_KEY);
        if (notificationType == null) {
            Log.W(getClass().getSimpleName() + " - Neither notificationType nor taskId should be null");
            return;
        }
        notify(notificationType);
    }

    private void notify(NotificationType notificationType) {
        Log.I(getClass().getSimpleName() + " - NotificationType: " + notificationType + "[" + notificationType.xdmGetNotiContentTitle() + ", " + notificationType.getContentText() + "]");
        NotificationStrategy notificationStrategy = notificationType.getNotificationStrategy();
        boolean isForegroundService = notificationStrategy.isForegroundService();
        NotificationId notificationId = notificationStrategy.getNotificationId();
        notificationId.setNotificationType(notificationType);
        Notification notificationFromType = NotificationCommon.getNotificationFromType(notificationType);
        if (notificationId == NotificationId.XDM_NOTIFICATION_ID_NONE) {
            Log.W(getClass().getSimpleName() + " - Do not use " + notificationId + ", which is just a placeholder");
            return;
        }
        int id = notificationId.getId(isForegroundService);
        if (isForegroundService) {
            getNotificationManager().cancel(notificationId.getId(!isForegroundService));
            Log.I(getClass().getSimpleName() + " - current : " + notificationId + "(2), startForeground: " + notificationType);
            startForeground(id, notificationFromType);
            return;
        }
        callStopForeground(notificationId.getId(!isForegroundService));
        Log.I(getClass().getSimpleName() + " - current : " + notificationId + "(3), notify: " + notificationType);
        getNotificationManager().notify(id, notificationFromType);
    }

    private void cancelNotification() {
        NotificationType notificationType = (NotificationType) this.intent.getSerializableExtra(NotificationTypeManagerService.NOTIFICATION_TYPE_KEY);
        NotificationId notificationId = (NotificationId) this.intent.getSerializableExtra(NotificationTypeManagerService.NOTIFICATION_ID_KEY);
        if (notificationType == null && notificationId == null) {
            Log.W(getClass().getSimpleName() + " - One of notificationType and notificationId should not be null");
            return;
        }
        if (notificationType != null) {
            notificationId = cancelNotificationBy(notificationType);
        } else {
            cancelNotificationBy(notificationId);
        }
        notificationId.clearNotificationType();
    }

    private NotificationId cancelNotificationBy(NotificationType notificationType) {
        Log.I(getClass().getSimpleName() + " - NotificationType: " + notificationType);
        NotificationId notificationId = notificationType.getNotificationStrategy().getNotificationId();
        if (!notificationType.isSet()) {
            Log.W(getClass().getSimpleName() + " - existing notificationType should be the same as that to be cancelled - existing[" + notificationId + ":" + notificationId.getNotificationType() + "], to be cancelled[" + notificationType + "]");
            return NotificationId.XDM_NOTIFICATION_ID_NONE;
        }
        cancelNotificationBy(notificationId);
        return notificationId;
    }

    private void cancelNotificationBy(NotificationId notificationId) {
        if (notificationId == NotificationId.XDM_NOTIFICATION_ID_NONE) {
            Log.W(getClass().getSimpleName() + " - Do not use " + notificationId + ", which is just a placeholder");
            return;
        }
        NotificationType notificationType = notificationId.getNotificationType();
        if (notificationType == NotificationType.XUI_INDICATOR_NONE) {
            Log.I(getClass().getSimpleName() + " - No notifications posted in " + notificationType + ": do nothing");
            return;
        }
        stopForeground(true);
        XUINotificationManager.getInstance().xuiSetNotificationType(NotificationType.XUI_INDICATOR_NONE);
        Log.I("Foreground Notification cancel");
    }

    private void callStopForeground(int i) {
        startForeground(i, NotificationCommon.getNotificationFromType(NotificationType.XUI_INDICATOR_SYNC_DM));
        stopForeground(true);
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) XDMDmUtils.getInstance().xdmGetServiceManager("notification");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.I("");
    }
}
