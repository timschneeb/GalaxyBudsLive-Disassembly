package com.samsung.android.sdk.mobileservice.social.buddy.request;

public class Fingerprint extends BuddyKey {
    private static final int FINGERPRINT = 1;

    public int getType() {
        return 1;
    }

    public Fingerprint(String str) {
        super(str);
    }
}
