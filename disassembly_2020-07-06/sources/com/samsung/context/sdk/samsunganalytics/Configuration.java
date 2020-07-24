package com.samsung.context.sdk.samsunganalytics;

public class Configuration {
    private int auidType = -1;
    private DBOpenHelper dbOpenHelper;
    private String deviceId;
    private boolean enableAutoDeviceId = false;
    private boolean enableFastReady = false;
    private boolean enableUseInAppLogging = false;
    private boolean isAlwaysRunningApp = false;
    private int networkTimeoutInMilliSeconds = 0;
    private String overrideIp;
    private int queueSize = 0;
    private int restrictedNetworkType = -1;
    private String trackingId;
    private boolean useAnonymizeIp = false;
    private UserAgreement userAgreement;
    private String userId;
    private String version;

    public String getTrackingId() {
        return this.trackingId;
    }

    public Configuration setTrackingId(String str) {
        this.trackingId = str;
        return this;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public Configuration setDeviceId(String str) {
        this.deviceId = str;
        return this;
    }

    public Configuration enableAutoDeviceId() {
        this.enableAutoDeviceId = true;
        return this;
    }

    public Configuration disableAutoDeviceId() {
        this.enableAutoDeviceId = false;
        return this;
    }

    public Configuration enableUseInAppLogging(UserAgreement userAgreement2) {
        setUserAgreement(userAgreement2);
        this.enableUseInAppLogging = true;
        return this;
    }

    public boolean isEnableUseInAppLogging() {
        return this.enableUseInAppLogging;
    }

    public boolean isEnableAutoDeviceId() {
        return this.enableAutoDeviceId;
    }

    @Deprecated
    public String getUserId() {
        return this.userId;
    }

    @Deprecated
    public Configuration setUserId(String str) {
        this.userId = str;
        return this;
    }

    public boolean isUseAnonymizeIp() {
        return this.useAnonymizeIp;
    }

    @Deprecated
    public Configuration setUseAnonymizeIp(boolean z) {
        this.useAnonymizeIp = z;
        return this;
    }

    public UserAgreement getUserAgreement() {
        return this.userAgreement;
    }

    public Configuration setUserAgreement(UserAgreement userAgreement2) {
        this.userAgreement = userAgreement2;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public Configuration setVersion(String str) {
        this.version = str;
        return this;
    }

    public String getOverrideIp() {
        return this.overrideIp;
    }

    @Deprecated
    public Configuration setOverrideIp(String str) {
        this.overrideIp = str;
        return this;
    }

    public boolean isAlwaysRunningApp() {
        return this.isAlwaysRunningApp;
    }

    public Configuration setAlwaysRunningApp(boolean z) {
        this.isAlwaysRunningApp = z;
        return this;
    }

    public boolean isEnableFastReady() {
        return this.enableFastReady;
    }

    public Configuration enableFastReady(boolean z) {
        this.enableFastReady = z;
        return this;
    }

    public int getNetworkTimeoutInMilliSeconds() {
        return this.networkTimeoutInMilliSeconds;
    }

    public Configuration setNetworkTimeoutInMilliSeconds(int i) {
        this.networkTimeoutInMilliSeconds = i;
        return this;
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    public Configuration setQueueSize(int i) {
        this.queueSize = i;
        return this;
    }

    public Configuration setDbOpenHelper(DBOpenHelper dBOpenHelper) {
        this.dbOpenHelper = dBOpenHelper;
        return this;
    }

    public DBOpenHelper getDbOpenHelper() {
        return this.dbOpenHelper;
    }

    public int getAuidType() {
        return this.auidType;
    }

    public void setAuidType(int i) {
        this.auidType = i;
    }

    public int getRestrictedNetworkType() {
        return this.restrictedNetworkType;
    }

    /* access modifiers changed from: protected */
    public void setRestrictedNetworkType(int i) {
        this.restrictedNetworkType = i;
    }
}
