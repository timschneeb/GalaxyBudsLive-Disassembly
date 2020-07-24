package com.accessorydm.ui.notification.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.db.file.XDBFumoAdp;
import com.accessorydm.db.file.XDBPostPoneAdp;
import com.accessorydm.ui.installconfirm.InstallCountdown;
import com.accessorydm.ui.notification.manager.NotificationBuilderStrategy;
import com.accessorydm.ui.progress.XUIProgressModel;
import com.samsung.android.fotaprovider.FotaProviderInitializer;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.type.DeviceType;
import com.sec.android.fotaprovider.R;
import java.util.Date;

public enum NotificationType {
    XUI_INDICATOR_NONE(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_CONNECTING, NotificationStrategy.COMMON_BACKGROUND) {
        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return "";
        }
    },
    XUI_INDICATOR_SYNC_DM(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_CONNECTING, NotificationStrategy.COMMON_FOREGROUND) {
        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_DM_CHECKING_UPDATE);
        }
    },
    XUI_INDICATOR_UPDATE_BACK_KEY_POSTPONE(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_POSTPONE, NotificationStrategy.COMMON_BACKGROUND) {
        public Object applyVariantTo(Object obj) {
            if (Build.VERSION.SDK_INT >= 26) {
                return obj;
            }
            NotificationCompat.Builder builder = (NotificationCompat.Builder) obj;
            builder.setPriority(2);
            return builder;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_NOTIFICATION_INSTALL_CONFIRM);
        }
    },
    XUI_INDICATOR_UPDATE_SCHEDULE_INSTALL(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_POSTPONE, NotificationStrategy.COMMON_BACKGROUND) {
        public Object applyVariantTo(Object obj) {
            if (Build.VERSION.SDK_INT >= 26) {
                return obj;
            }
            NotificationCompat.Builder builder = (NotificationCompat.Builder) obj;
            builder.setPriority(2);
            return builder;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            Date date = new Date(XDBPostPoneAdp.xdbGetPostponeTime());
            String format = DateFormat.getLongDateFormat(NotificationType.getContext()).format(date);
            String format2 = DateFormat.getTimeFormat(NotificationType.getContext()).format(date);
            return String.format(NotificationType.getContext().getString(R.string.STR_NOTIFICATION_INSTALL_CONFIRM_SCHEDULED), new Object[]{format2, format});
        }
    },
    XUI_INDICATOR_UPDATE_POSTPONE_SCHEDULE_INSTALL(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_POSTPONE, NotificationStrategy.COMMON_BACKGROUND) {
        public Object applyVariantTo(Object obj) {
            if (Build.VERSION.SDK_INT >= 26) {
                return obj;
            }
            NotificationCompat.Builder builder = (NotificationCompat.Builder) obj;
            builder.setPriority(2);
            return builder;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(DeviceType.get().getTextType().getInstallConfirmNotificationPostponeScheduleInstallTextId());
        }
    },
    XUI_INDICATOR_FOTA_UPDATE(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_COMPLETION, NotificationStrategy.COMMON_BACKGROUND) {
        public Object applyVariantTo(Object obj) {
            if (Build.VERSION.SDK_INT >= 26) {
                return obj;
            }
            NotificationCompat.Builder builder = (NotificationCompat.Builder) obj;
            builder.setPriority(2);
            return builder;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_NOTIFICATION_INSTALL_CONFIRM);
        }
    },
    XUI_INDICATOR_FOTA_UPDATE_COUNTDOWN(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_COMPLETION, NotificationStrategy.COMMON_FOREGROUND) {
        public Object applyVariantTo(Object obj) {
            if (Build.VERSION.SDK_INT >= 26) {
                Notification.Builder builder = (Notification.Builder) obj;
                builder.setOnlyAlertOnce(true);
                return builder;
            }
            NotificationCompat.Builder builder2 = (NotificationCompat.Builder) obj;
            builder2.setOnlyAlertOnce(true);
            return builder2;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return String.format(NotificationType.getContext().getString(DeviceType.get().getTextType().getInstallConfirmCountdownTextId()), new Object[]{Integer.valueOf(InstallCountdown.getInstance().getRemainingTime())});
        }
    },
    XUI_INDICATOR_DOWNLOAD_RETRY_CONFIRM(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_COMPLETION, NotificationStrategy.COMMON_BACKGROUND) {
        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_ACCESSORY_DOWNLOAD_FAILED);
        }
    },
    XUI_INDICATOR_DOWNLOAD_FAILED_NETWORK_DISCONNECTED(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_COMPLETION, NotificationStrategy.COMMON_BACKGROUND) {
        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_ACCESSORY_DOWNLOAD_FAILED);
        }
    },
    XUI_INDICATOR_DOWNLOAD_FAILED_WIFI_DISCONNECTED(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_COMPLETION, NotificationStrategy.COMMON_BACKGROUND) {
        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_ACCESSORY_DOWNLOAD_FAILED);
        }
    },
    XUI_INDICATOR_DOWNLOAD_PROGRESS(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_CONNECTING, NotificationStrategy.COMMON_FOREGROUND) {
        private static final int MAX_PROGRESS = 100;

        public Object applyVariantTo(Object obj) {
            int progressPercent = XUIProgressModel.getInstance().getProgressPercent();
            String progressSizeText = XUIProgressModel.getInstance().getProgressSizeText();
            if (Build.VERSION.SDK_INT >= 26) {
                Notification.Builder builder = (Notification.Builder) obj;
                builder.setOnlyAlertOnce(true);
                builder.setProgress(100, progressPercent, false).setSubText(progressSizeText);
                return builder;
            }
            NotificationCompat.Builder builder2 = (NotificationCompat.Builder) obj;
            builder2.setOnlyAlertOnce(true);
            builder2.setProgress(100, progressPercent, false).setSubText(progressSizeText);
            return builder2;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_ACCESSORY_DOWNLOAD_PROGRESS);
        }
    },
    XUI_INDICATOR_COPY_PROGRESS(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_CONNECTING, NotificationStrategy.COMMON_FOREGROUND) {
        private static final int MAX_PROGRESS = 100;

        public Object applyVariantTo(Object obj) {
            int progressPercent = XUIProgressModel.getInstance().getProgressPercent();
            String progressSizeText = XUIProgressModel.getInstance().getProgressSizeText();
            if (Build.VERSION.SDK_INT >= 26) {
                Notification.Builder builder = (Notification.Builder) obj;
                builder.setOnlyAlertOnce(true);
                builder.setProgress(100, progressPercent, false).setSubText(progressSizeText);
                return builder;
            }
            NotificationCompat.Builder builder2 = (NotificationCompat.Builder) obj;
            builder2.setOnlyAlertOnce(true);
            builder2.setProgress(100, progressPercent, false).setSubText(progressSizeText);
            return builder2;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_ACCESSORY_COPY_PROGRESS);
        }
    },
    XUI_INDICATOR_COPY_FAILED(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_COMPLETION, NotificationStrategy.COMMON_BACKGROUND) {
        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_NOTIFICATION_COPY_FAILED);
        }
    },
    XUI_INDICATOR_DOWNLOAD_START_CONFIRM(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_COMPLETION, NotificationStrategy.COMMON_BACKGROUND) {
        public Object applyVariantTo(Object obj) {
            if (Build.VERSION.SDK_INT >= 26) {
                return obj;
            }
            NotificationCompat.Builder builder = (NotificationCompat.Builder) obj;
            builder.setPriority(2);
            return builder;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            return NotificationType.getContext().getString(R.string.STR_NOTIFICATION_DOWNLOAD_CONFIRM);
        }
    },
    XUI_INDICATOR_UPDATE_RESULT(NotificationBuilderStrategy.BigText.DO_NOTHING, NotificationBuilderStrategy.SmallIcon.FOTA_COMPLETION, NotificationStrategy.UPDATE_REPORT) {
        /* access modifiers changed from: package-private */
        public CharSequence getContentText() {
            if ("200".equals(XDBFumoAdp.xdbGetFUMOResultCode())) {
                return NotificationType.getContext().getString(R.string.STR_ACCESSORY_UPDATE_SUCCESS);
            }
            return NotificationType.getContext().getString(R.string.STR_ACCESSORY_UPDATE_FAILED_TRY_LATER);
        }
    };
    
    private static final long NOW = 0;
    private final NotificationBuilderStrategy.BigText bigTextStrategy;
    private final NotificationStrategy notificationStrategy;
    private final NotificationBuilderStrategy.SmallIcon smallIconStrategy;

    public Object applyVariantTo(Object obj) {
        return obj;
    }

    /* access modifiers changed from: package-private */
    public abstract CharSequence getContentText();

    private NotificationType(NotificationBuilderStrategy.BigText bigText, NotificationBuilderStrategy.SmallIcon smallIcon, NotificationStrategy notificationStrategy2) {
        this.bigTextStrategy = bigText;
        this.smallIconStrategy = smallIcon;
        this.notificationStrategy = notificationStrategy2;
    }

    public Object xdmGetBuilder() {
        return applyVariantTo(xdmDoGetBuilder());
    }

    private Object xdmDoGetBuilder() {
        Log.I("");
        if (Build.VERSION.SDK_INT >= 26) {
            xdmCreateNotificationChannel();
            Notification.Builder builder = new Notification.Builder(getContext(), NotificationTypeManagerService.NOTIFICATION_CHANNELID);
            builder.setContentTitle(xdmGetNotiContentTitle());
            builder.setContentText(getContentText());
            builder.setColor(getContext().getColor(R.color.notification_color));
            builder.setStyle((Notification.Style) getBigTextStyle());
            builder.setSmallIcon(getSmallIcon());
            builder.setOnlyAlertOnce(true);
            builder.setWhen(0);
            return builder;
        }
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(getContext());
        builder2.setContentTitle(xdmGetNotiContentTitle());
        builder2.setContentText(getContentText());
        builder2.setColor(ContextCompat.getColor(getContext(), R.color.notification_color));
        builder2.setStyle((NotificationCompat.Style) getBigTextStyle());
        builder2.setSmallIcon(getSmallIcon());
        builder2.setOnlyAlertOnce(true);
        builder2.setWhen(0);
        return builder2;
    }

    private void xdmCreateNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) XDMDmUtils.getInstance().xdmGetServiceManager("notification");
        if (notificationManager.getNotificationChannel(NotificationTypeManagerService.NOTIFICATION_CHANNELID) == null) {
            Log.I("NotificationChannel is not exist.");
            NotificationChannel notificationChannel = new NotificationChannel(NotificationTypeManagerService.NOTIFICATION_CHANNELID, getContext().getString(DeviceType.get().getTextType().getTitleId()), 2);
            notificationChannel.setLockscreenVisibility(1);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public CharSequence xdmGetNotiContentTitle() {
        return getContext().getString(DeviceType.get().getTextType().getTitleId());
    }

    private Object getBigTextStyle() {
        return this.bigTextStrategy.getBigTextStyle(this);
    }

    private int getSmallIcon() {
        return this.smallIconStrategy.getSmallIcon();
    }

    public NotificationStrategy getNotificationStrategy() {
        return this.notificationStrategy;
    }

    public boolean isSet() {
        return this == getNotificationStrategy().getNotificationId().getNotificationType();
    }

    /* access modifiers changed from: private */
    public static Context getContext() {
        return FotaProviderInitializer.getContext();
    }
}
