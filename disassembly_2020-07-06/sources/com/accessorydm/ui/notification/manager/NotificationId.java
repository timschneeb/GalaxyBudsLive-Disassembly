package com.accessorydm.ui.notification.manager;

import android.content.Intent;
import android.os.IBinder;
import com.accessorydm.ui.notification.XUINotificationManager;

public enum NotificationId {
    XDM_NOTIFICATION_ID_NONE {
        /* access modifiers changed from: package-private */
        public Class<? extends NotificationTypeManagerService> getNotificationTypeManagerServiceClass() {
            return StubNotificationTypeManagerService.class;
        }
    },
    XDM_NOTIFICATION_ID_PRIMARY {
        /* access modifiers changed from: package-private */
        public Class<? extends NotificationTypeManagerService> getNotificationTypeManagerServiceClass() {
            return Common.class;
        }
    },
    XDM_NOTIFICATION_ID_SECONDARY {
        /* access modifiers changed from: package-private */
        public Class<? extends NotificationTypeManagerService> getNotificationTypeManagerServiceClass() {
            return UpdateReport.class;
        }
    };

    /* access modifiers changed from: package-private */
    public abstract Class<? extends NotificationTypeManagerService> getNotificationTypeManagerServiceClass();

    /* access modifiers changed from: package-private */
    public int getId(boolean z) {
        return (ordinal() * 2) + (z ^ true ? 1 : 0);
    }

    /* access modifiers changed from: package-private */
    public void setNotificationType(NotificationType notificationType) {
        XUINotificationManager.getInstance().xuiSetNotificationType(notificationType);
    }

    public NotificationType getNotificationType() {
        return XUINotificationManager.getInstance().xuiGetNotificationType();
    }

    /* access modifiers changed from: package-private */
    public void clearNotificationType() {
        setNotificationType(NotificationType.XUI_INDICATOR_NONE);
    }

    public static class Common extends RealNotificationTypeManagerService {
        public /* bridge */ /* synthetic */ IBinder onBind(Intent intent) {
            return super.onBind(intent);
        }

        public /* bridge */ /* synthetic */ void onCreate() {
            super.onCreate();
        }

        public /* bridge */ /* synthetic */ void onDestroy() {
            super.onDestroy();
        }

        public /* bridge */ /* synthetic */ int onStartCommand(Intent intent, int i, int i2) {
            return super.onStartCommand(intent, i, i2);
        }
    }

    public static class UpdateReport extends RealNotificationTypeManagerService {
        public /* bridge */ /* synthetic */ IBinder onBind(Intent intent) {
            return super.onBind(intent);
        }

        public /* bridge */ /* synthetic */ void onCreate() {
            super.onCreate();
        }

        public /* bridge */ /* synthetic */ void onDestroy() {
            super.onDestroy();
        }

        public /* bridge */ /* synthetic */ int onStartCommand(Intent intent, int i, int i2) {
            return super.onStartCommand(intent, i, i2);
        }
    }
}
