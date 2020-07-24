package com.sec.android.diagmonagent.log.provider;

import android.os.Bundle;
import java.util.List;

public abstract class newAbstractMasterLogProvider extends newAbstractLogProvider {
    public static final String AGREED = "agreed";
    public static final String DEVICE_ID = "deviceId";
    public static final String DEVICE_INFO = "deviceInfo";
    public static final String NONCE = "nonce";
    public static final String PUSH_REGISTERED = "pushRegistered";
    public static final String REGISTERED = "registered";
    public static final String SERVICE_NAME = "serviceName";
    public static final String SUPPORT_PUSH = "supportPush";
    public static final String TRY_REGISTERING = "tryRegistering";
    public static final String UPLOAD_WIFIONLY = "uploadWifionly";

    private void enforceAgreement() {
    }

    /* access modifiers changed from: protected */
    public abstract List<String> setAuthorityList();

    /* access modifiers changed from: protected */
    public abstract String setServiceName();

    /* access modifiers changed from: protected */
    public boolean setSupportPush() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean setUploadWiFiOnly() {
        return true;
    }

    public boolean onCreate() {
        if (!super.onCreate()) {
            return false;
        }
        data.putBundle("registered", makeBundle("registered", false));
        data.putBundle("pushRegistered", makeBundle("pushRegistered", false));
        data.putBundle("tryRegistering", makeBundle("tryRegistering", true));
        data.putBundle("nonce", makeBundle("nonce", ""));
        data.putBundle("authorityList", makeAuthorityListBundle(setAuthorityList()));
        data.putBundle("serviceName", makeBundle("serviceName", setServiceName()));
        data.putBundle("deviceId", makeBundle("deviceId", setDeviceId()));
        data.putBundle("deviceInfo", setDeviceInfo());
        data.putBundle("uploadWifionly", makeBundle("uploadWifionly", setUploadWiFiOnly()));
        data.putBundle("supportPush", makeBundle("supportPush", setSupportPush()));
        data.putBundle("logList", makeLogListBundle(setLogList()));
        data.putBundle("plainLogList", makeLogListBundle(setPlainLogList()));
        return true;
    }

    public void setConfiguration(DiagMonConfig diagMonConfig) {
        data.putBundle("authorityList", makeAuthorityListBundle(diagMonConfig.getOldConfig().getAuthorityList()));
        data.putBundle("serviceName", makeBundle("serviceName", diagMonConfig.getOldConfig().getServiceName()));
        data.putBundle("deviceId", makeBundle("deviceId", diagMonConfig.getDeviceId()));
        data.putBundle("agreed", makeBundle("agreed", diagMonConfig.getAgree()));
        data.putBundle("logList", makeLogListBundle(diagMonConfig.getOldConfig().getLogList()));
        data.putBundle("plainLogList", makeLogListBundle(setPlainLogList()));
    }

    private Bundle makeBundle(String str, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(str, z);
        return bundle;
    }

    private Bundle makeBundle(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString(str, str2);
        return bundle;
    }

    private Bundle makeAuthorityListBundle(List<String> list) {
        Bundle bundle = new Bundle();
        for (String next : list) {
            bundle.putString(next, next);
        }
        return bundle;
    }

    /* access modifiers changed from: protected */
    public String setDeviceId() {
        newPackageInformation newpackageinformation = newPackageInformation.instance;
        return newPackageInformation.getTWID();
    }

    /* access modifiers changed from: protected */
    public Bundle setDeviceInfo() {
        return newPackageInformation.instance.getDeviceInfoBundle(getContext());
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        enforceSelfOrSystem();
        if ("get".equals(str) && "registered".equals(str2)) {
            enforceAgreement();
        }
        return super.call(str, str2, bundle);
    }
}
