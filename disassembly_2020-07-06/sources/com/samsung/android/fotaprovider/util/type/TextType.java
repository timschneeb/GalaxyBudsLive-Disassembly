package com.samsung.android.fotaprovider.util.type;

import com.sec.android.fotaprovider.R;

public enum TextType {
    WATCH {
        public int getCopyRetryTitleId() {
            return -1;
        }

        public int getTitleId() {
            return R.string.STR_ACCESSORY_UPDATE_TITLE_WATCH;
        }

        public int getConnectingMessageId() {
            return R.string.STR_ACCESSORY_CONNECTING_WATCH;
        }

        public int getConnectionFailedMessageId() {
            return R.string.STR_ACCESSORY_CONNECTION_FAILED_WATCH;
        }

        public int getInstallConfirmTitleId() {
            return R.string.STR_ACCESSORY_INSTALL_CONFIRM_TITLE_WATCH;
        }

        public int getForceInstallConfirmTitleId() {
            return R.string.STR_ACCESSORY_FORCE_INSTALL_CONFIRM_TITLE_WATCH;
        }

        public int getInstallConfirmCountdownTextId() {
            return R.string.STR_ACCESSORY_FORCE_INSTALL_CONFIRM_WATCH;
        }

        public int getInstallConfirmNotificationPostponeScheduleInstallTextId() {
            return R.string.STR_NOTIFICATION_INSTALL_CONFIRM_POSTPONE_SCHEDULE_INSTALL_WATCH;
        }

        public int getCopyFailedMessageId() {
            return R.string.STR_ACCESSORY_COPY_FAILED_WATCH;
        }

        public int getCopyRetryLaterMessageId() {
            return R.string.STR_ACCESSORY_COPY_RETRY_LATER_WATCH;
        }

        public int getCopyRetryMessageId() {
            return R.string.STR_ACCESSORY_COPY_RETRY_WATCH;
        }

        public int getCopyRetryPositiveButtonId() {
            return R.string.STR_BTN_OK;
        }

        public int getDownloadAccessoryLowMemoryMessageId() {
            return R.string.STR_ACCESSORY_LOW_MEMORY_DOWNLOAD_WATCH;
        }

        public int getCopyAccessoryLowMemoryMessageId() {
            return R.string.STR_ACCESSORY_LOW_MEMORY_COPY_WATCH;
        }

        public int getInstallAccessoryLowMemoryMessageId() {
            return R.string.STR_ACCESSORY_LOW_MEMORY_INSTALL_WATCH;
        }

        public int getCopyAccessoryLowBatteryMessageId() {
            return R.string.STR_ACCESSORY_LOW_BATTERY_WATCH;
        }

        public int getAccessoryModifiedTitleId() {
            return R.string.STR_ACCESSORY_MODIFIED_TITLE_WATCH;
        }

        public int getCautionMainDescriptionId(boolean z) {
            return R.string.STR_ACCESSORY_CAUTION_WATCH;
        }

        public int getCautionSettingsTextId() {
            return R.string.STR_ACCESSORY_CAUTION_SETTINGS_MAY_CHANGE_WATCH;
        }

        public int getCautionBackupTextId(boolean z) {
            if (z) {
                return -1;
            }
            return R.string.STR_ACCESSORY_CAUTION_BACKUP_WATCH;
        }

        public int getPolicyBlockedMessageId() {
            return R.string.STR_SYSTEMPOLICY_BLOCK_WATCH;
        }
    },
    EARBUDS {
        public int getCautionBackupTextId(boolean z) {
            return -1;
        }

        public int getForceInstallConfirmTitleId() {
            return -1;
        }

        public int getInstallConfirmCountdownTextId() {
            return -1;
        }

        public int getInstallConfirmNotificationPostponeScheduleInstallTextId() {
            return -1;
        }

        public int getInstallConfirmTitleId() {
            return -1;
        }

        public int getTitleId() {
            return R.string.STR_ACCESSORY_UPDATE_TITLE_EARBUDS;
        }

        public int getConnectingMessageId() {
            return R.string.STR_ACCESSORY_CONNECTING_EARBUDS;
        }

        public int getConnectionFailedMessageId() {
            return R.string.STR_ACCESSORY_CONNECTION_FAILED_EARBUDS;
        }

        public int getCopyFailedMessageId() {
            return R.string.STR_ACCESSORY_COPY_FAILED_EARBUDS;
        }

        public int getCopyRetryLaterMessageId() {
            return R.string.STR_ACCESSORY_COPY_RETRY_LATER_EARBUDS;
        }

        public int getCopyRetryTitleId() {
            return R.string.STR_ACCESSORY_COPY_RETRY_EARBUDS_TITLE;
        }

        public int getCopyRetryMessageId() {
            return R.string.STR_ACCESSORY_COPY_RETRY_EARBUDS;
        }

        public int getCopyRetryPositiveButtonId() {
            return R.string.STR_BTN_CONTINUE;
        }

        public int getDownloadAccessoryLowMemoryMessageId() {
            return R.string.STR_ACCESSORY_LOW_MEMORY_DOWNLOAD_PHONE;
        }

        public int getCopyAccessoryLowMemoryMessageId() {
            return R.string.STR_ACCESSORY_LOW_MEMORY_DOWNLOAD_PHONE;
        }

        public int getInstallAccessoryLowMemoryMessageId() {
            return R.string.STR_ACCESSORY_LOW_MEMORY_DOWNLOAD_PHONE;
        }

        public int getCopyAccessoryLowBatteryMessageId() {
            return R.string.STR_ACCESSORY_LOW_BATTERY_EARBUDS;
        }

        public int getAccessoryModifiedTitleId() {
            return R.string.STR_ACCESSORY_UPDATE_TITLE_EARBUDS;
        }

        public int getCautionMainDescriptionId(boolean z) {
            return R.string.STR_ACCESSORY_CAUTION_EARBUDS;
        }

        public int getCautionSettingsTextId() {
            return R.string.STR_ACCESSORY_CAUTION_SETTINGS_MAY_CHANGE_EARBUDS;
        }

        public int getPolicyBlockedMessageId() {
            return R.string.STR_ACCESSORY_UPDATE_FAILED_TRY_LATER;
        }
    };
    
    public static final int INVALID_ID = -1;

    public abstract int getAccessoryModifiedTitleId();

    public abstract int getCautionBackupTextId(boolean z);

    public abstract int getCautionMainDescriptionId(boolean z);

    public abstract int getCautionSettingsTextId();

    public abstract int getConnectingMessageId();

    public abstract int getConnectionFailedMessageId();

    public abstract int getCopyAccessoryLowBatteryMessageId();

    public abstract int getCopyAccessoryLowMemoryMessageId();

    public abstract int getCopyFailedMessageId();

    public abstract int getCopyRetryLaterMessageId();

    public abstract int getCopyRetryMessageId();

    public abstract int getCopyRetryPositiveButtonId();

    public abstract int getCopyRetryTitleId();

    public abstract int getDownloadAccessoryLowMemoryMessageId();

    public abstract int getForceInstallConfirmTitleId();

    public abstract int getInstallAccessoryLowMemoryMessageId();

    public abstract int getInstallConfirmCountdownTextId();

    public abstract int getInstallConfirmNotificationPostponeScheduleInstallTextId();

    public abstract int getInstallConfirmTitleId();

    public abstract int getPolicyBlockedMessageId();

    public abstract int getTitleId();
}
