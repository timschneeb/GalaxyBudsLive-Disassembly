package com.accessorydm.ui.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.ui.notification.manager.NotificationId;
import com.accessorydm.ui.notification.manager.NotificationType;
import com.accessorydm.ui.notification.manager.NotificationTypeManager;
import com.accessorydm.ui.notification.manager.NotificationTypeManagerService;
import com.accessorydm.ui.progress.XUIProgressModel;
import com.accessorydm.ui.progress.listener.XUIProgressNotificationListener;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.type.DeviceType;

public class XUINotificationManager {
    private static XUINotificationManager instance = new XUINotificationManager();
    private static NotificationType m_BackupNotificationType = NotificationType.XUI_INDICATOR_NONE;

    private XUINotificationManager() {
        XUIProgressModel.getInstance().addProgressListener(new XUIProgressNotificationListener());
    }

    public static XUINotificationManager getInstance() {
        return instance;
    }

    public void xuiSetIndicator(NotificationType notificationType) {
        Log.I("xuiSetIndicator : " + notificationType);
        NotificationTypeManager.notify(notificationType);
        if (isContinuousUpdateBy(notificationType)) {
            NotificationTypeManager.cancel(NotificationType.XUI_INDICATOR_UPDATE_RESULT);
        }
    }

    private boolean isContinuousUpdateBy(NotificationType notificationType) {
        return notificationType == NotificationType.XUI_INDICATOR_DOWNLOAD_START_CONFIRM || notificationType == NotificationType.XUI_INDICATOR_DOWNLOAD_PROGRESS || notificationType == NotificationType.XUI_INDICATOR_COPY_PROGRESS;
    }

    public void xuiRemoveAllNotification() {
        Log.I("");
        NotificationTypeManager.cancelAll();
    }

    public void xuiRemoveNotification(NotificationType notificationType) {
        Log.I("xuiRemoveNotification notificationType : " + notificationType);
        NotificationTypeManager.cancel(notificationType);
    }

    public void xuiRemoveNotification(NotificationId notificationId) {
        Log.I("xuiRemoveNotification notificationId : " + notificationId);
        NotificationTypeManager.cancel(notificationId.getNotificationType());
    }

    public void xuiUpdateNotificationChannel() {
        NotificationChannel notificationChannel = getNotificationManager().getNotificationChannel(NotificationTypeManagerService.NOTIFICATION_CHANNELID);
        if (notificationChannel != null) {
            Log.I("NotificationChannel Name is modified - " + notificationChannel.getName());
            notificationChannel.setName(XDMDmUtils.getContext().getString(DeviceType.get().getTextType().getTitleId()));
            getNotificationManager().createNotificationChannel(notificationChannel);
            return;
        }
        Log.I("NotificationChannel is null");
    }

    public void xuiSetNotificationType(NotificationType notificationType) {
        m_BackupNotificationType = notificationType;
        Log.I("set m_BackupNotificationType : " + m_BackupNotificationType);
    }

    public NotificationType xuiGetNotificationType() {
        Log.I("get m_BackupNotificationType : " + m_BackupNotificationType);
        return m_BackupNotificationType;
    }

    public void xuiReNotifyWithBackupNotification() {
        Log.I("ReNotify: " + m_BackupNotificationType);
        if (m_BackupNotificationType == NotificationType.XUI_INDICATOR_NONE || m_BackupNotificationType == NotificationType.XUI_INDICATOR_UPDATE_RESULT) {
            xuiRemoveNotification(NotificationType.XUI_INDICATOR_UPDATE_RESULT);
        } else {
            xuiSetIndicator(m_BackupNotificationType);
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) XDMDmUtils.getInstance().xdmGetServiceManager("notification");
    }
}
