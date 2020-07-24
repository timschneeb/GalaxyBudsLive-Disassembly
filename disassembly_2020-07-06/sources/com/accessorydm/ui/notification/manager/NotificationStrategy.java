package com.accessorydm.ui.notification.manager;

public interface NotificationStrategy {
    public static final NotificationStrategy COMMON_BACKGROUND = new NotificationStrategy() {
        public boolean isForegroundService() {
            return false;
        }

        public NotificationId getNotificationId() {
            return NotificationId.XDM_NOTIFICATION_ID_PRIMARY;
        }
    };
    public static final NotificationStrategy COMMON_FOREGROUND = new NotificationStrategy() {
        public boolean isForegroundService() {
            return true;
        }

        public NotificationId getNotificationId() {
            return NotificationId.XDM_NOTIFICATION_ID_PRIMARY;
        }
    };
    public static final NotificationStrategy UPDATE_REPORT = new NotificationStrategy() {
        public boolean isForegroundService() {
            return false;
        }

        public NotificationId getNotificationId() {
            return NotificationId.XDM_NOTIFICATION_ID_SECONDARY;
        }
    };

    NotificationId getNotificationId();

    boolean isForegroundService();
}
