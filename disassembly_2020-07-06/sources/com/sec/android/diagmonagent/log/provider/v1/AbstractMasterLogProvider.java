package com.sec.android.diagmonagent.log.provider.v1;

import android.os.Bundle;
import java.util.List;

public abstract class AbstractMasterLogProvider extends AbstractLogProvider {
    public static final String AGREED = "agreed";
    public static final String DEFAULT_MO = "defaultMO";
    public static final String DEVICE_ID = "deviceId";
    public static final String DEVICE_INFO = "deviceInfo";
    public static final String NONCE = "nonce";
    public static final String PUSH_REGISTERED = "pushRegistered";
    public static final String REGISTERED = "registered";
    public static final String SERVICE_NAME = "serviceName";
    public static final String SUPPORT_PUSH = "supportPush";
    public static final String TRY_REGISTERING = "tryRegistering";
    public static final String UPLOAD_WIFIONLY = "uploadWifionly";

    /* access modifiers changed from: protected */
    public abstract boolean isAgreed();

    /* access modifiers changed from: protected */
    public abstract List<String> setAuthorityList();

    /* access modifiers changed from: protected */
    public abstract Bundle setDefaultMO();

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
        this.data.putBundle("registered", makeBundle("registered", false));
        this.data.putBundle("pushRegistered", makeBundle("pushRegistered", false));
        this.data.putBundle("tryRegistering", makeBundle("tryRegistering", true));
        this.data.putBundle("nonce", makeBundle("nonce", ""));
        this.data.putBundle("authorityList", makeAuthorityListBundle(setAuthorityList()));
        this.data.putBundle("serviceName", makeBundle("serviceName", setServiceName()));
        this.data.putBundle("deviceId", makeBundle("deviceId", setDeviceId()));
        this.data.putBundle(DEFAULT_MO, setDefaultMO());
        this.data.putBundle("deviceInfo", setDeviceInfo());
        this.data.putBundle("uploadWifionly", makeBundle("uploadWifionly", setUploadWiFiOnly()));
        this.data.putBundle("supportPush", makeBundle("supportPush", setSupportPush()));
        return true;
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
        PackageInformation packageInformation = PackageInformation.instance;
        return PackageInformation.getTWID();
    }

    /* access modifiers changed from: protected */
    public Bundle setDeviceInfo() {
        return PackageInformation.instance.getDeviceInfoBundle(getContext());
    }

    private void enforceAgreement() {
        if (!isAgreed()) {
            throw new SecurityException("Permission Denial");
        }
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        enforceSelfOrSystem();
        if (!"get".equals(str) || !"agreed".equals(str2)) {
            if ("get".equals(str) && "registered".equals(str2)) {
                enforceAgreement();
            }
            return super.call(str, str2, bundle);
        }
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("agreed", isAgreed());
        return bundle2;
    }
}
