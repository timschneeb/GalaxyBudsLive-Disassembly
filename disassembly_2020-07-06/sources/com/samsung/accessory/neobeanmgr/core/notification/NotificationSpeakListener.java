package com.samsung.accessory.neobeanmgr.core.notification;

public interface NotificationSpeakListener {
    void VoiceNotificationSpeakCompleted(int i);

    void VoiceNotificationSpeakStarted(int i, String str);
}
