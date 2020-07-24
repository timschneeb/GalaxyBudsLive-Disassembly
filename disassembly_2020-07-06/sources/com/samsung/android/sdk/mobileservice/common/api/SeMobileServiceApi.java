package com.samsung.android.sdk.mobileservice.common.api;

import android.content.Context;
import android.text.TextUtils;
import com.samsung.android.sdk.mobileservice.SeMobileServiceSession;
import com.samsung.android.sdk.mobileservice.SeMobileServiceSessionImpl;
import com.samsung.android.sdk.mobileservice.auth.IMobileServiceAuth;
import com.samsung.android.sdk.mobileservice.common.exception.NotAuthorizedException;
import com.samsung.android.sdk.mobileservice.common.exception.NotConnectedException;
import com.samsung.android.sdk.mobileservice.common.exception.NotSupportedApiException;
import com.samsung.android.sdk.mobileservice.place.IMobileServicePlace;
import com.samsung.android.sdk.mobileservice.profile.IMobileServiceProfile;
import com.samsung.android.sdk.mobileservice.social.IMobileServiceSocial;
import com.samsung.android.sdk.mobileservice.util.SdkLog;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public abstract class SeMobileServiceApi {
    private final String mApiName;
    private final String mReference;
    private final SeMobileServiceSessionImpl mSessionInstance;

    /* access modifiers changed from: protected */
    public abstract String[] getEssentialServiceNames();

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0041, code lost:
        if (r0 == null) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004d, code lost:
        if (r0.getClassName().startsWith(com.samsung.android.sdk.mobileservice.BuildConfig.APPLICATION_ID) == false) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0057, code lost:
        throw new com.samsung.android.sdk.mobileservice.common.exception.NotAuthorizedException("Allowed using SeMobileServiceApi in mobile service SDK only.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0039, code lost:
        if (r0.hasNext() == false) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x003b, code lost:
        r0 = (java.lang.StackTraceElement) r0.next();
     */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0058 A[EDGE_INSN: B:37:0x0058->B:15:0x0058 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x001c  */
    protected SeMobileServiceApi(SeMobileServiceSession seMobileServiceSession, String str) throws NotConnectedException, NotAuthorizedException, NotSupportedApiException {
        debugLog(str);
        Iterator it = Arrays.asList(Thread.currentThread().getStackTrace()).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            StackTraceElement stackTraceElement = (StackTraceElement) it.next();
            if (stackTraceElement == null || TextUtils.equals(stackTraceElement.getClassName(), SeMobileServiceApi.class.getName())) {
            }
            if (!it.hasNext()) {
            }
        }
        if (seMobileServiceSession instanceof SeMobileServiceSessionImpl) {
            SeMobileServiceSessionImpl seMobileServiceSessionImpl = (SeMobileServiceSessionImpl) seMobileServiceSession;
            this.mSessionInstance = seMobileServiceSessionImpl;
            this.mApiName = str;
            this.mReference = SdkLog.getReference(this.mSessionInstance);
            String[] essentialServiceNames = getEssentialServiceNames();
            int length = essentialServiceNames.length;
            int i = 0;
            while (i < length) {
                String str2 = essentialServiceNames[i];
                if (seMobileServiceSessionImpl.isAddedService(str2)) {
                    i++;
                } else {
                    SdkLog.d(str, "Not added service " + SdkLog.getReference(seMobileServiceSession));
                    throw new NotAuthorizedException(str2 + " is not added service. Before new this api class, you must add this service name on session!");
                }
            }
            if (!seMobileServiceSessionImpl.isSessionConnected()) {
                SdkLog.d(str, "Session is not connected " + SdkLog.getReference(seMobileServiceSession));
                throw new NotConnectedException("Session is not connected! After connection callback called, new this api class!");
            } else if (!seMobileServiceSessionImpl.isSupportedApi(str)) {
                SdkLog.d(str, "Api component is not supported. " + SdkLog.getReference(seMobileServiceSession));
                throw new NotSupportedApiException(str + " is not supported");
            }
        } else {
            SdkLog.d(str, "Session is invalid " + SdkLog.getReference(seMobileServiceSession));
            throw new NotConnectedException("Session is not connected! After connection callback called, new this api class!");
        }
    }

    /* access modifiers changed from: protected */
    public void checkAuthorized(int... iArr) throws NotAuthorizedException {
        HashSet hashSet = new HashSet();
        for (int valueOf : iArr) {
            hashSet.add(Integer.valueOf(valueOf));
        }
        int authorized = this.mSessionInstance.getAuthorized();
        if (hashSet.contains(Integer.valueOf(authorized))) {
            if (authorized == 0) {
                debugLog("Account not authorized ");
                throw new NotAuthorizedException("Account is not authorized! you need sign-in");
            } else if (authorized == 1) {
                debugLog("Device not authorized ");
                throw new NotAuthorizedException("Device is not authorized! you need to authorize device");
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isSupportedSemsAgentVersion(int i) {
        return this.mSessionInstance.isSupportedSemsAgentVersion(i);
    }

    /* access modifiers changed from: protected */
    public boolean isSupportedSaAgentVersion(int i) {
        return this.mSessionInstance.isSupportedSaAgentVersion(i);
    }

    /* access modifiers changed from: protected */
    public long getSemsAgentVersion() {
        return this.mSessionInstance.getSamsungExperienceServiceAgentVersion();
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.mSessionInstance.getContext();
    }

    /* access modifiers changed from: protected */
    public String getAppId() {
        return this.mSessionInstance.getAppId();
    }

    /* access modifiers changed from: protected */
    public String getReference() {
        return this.mReference;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return this.mApiName;
    }

    /* access modifiers changed from: protected */
    public IMobileServiceAuth getAuthService() throws NotConnectedException {
        return this.mSessionInstance.getAuthService();
    }

    /* access modifiers changed from: protected */
    public IMobileServiceSocial getSocialService() throws NotConnectedException {
        return this.mSessionInstance.getSocialService();
    }

    /* access modifiers changed from: protected */
    public IMobileServiceProfile getProfileService() throws NotConnectedException {
        return this.mSessionInstance.getProfileService();
    }

    /* access modifiers changed from: protected */
    public IMobileServicePlace getPlaceService() throws NotConnectedException {
        return this.mSessionInstance.getPlaceService();
    }

    /* access modifiers changed from: protected */
    public void debugLog(String str) {
        String str2 = this.mApiName;
        SdkLog.d(str2, str + " " + this.mReference);
    }

    /* access modifiers changed from: protected */
    public void secureLog(Exception exc) {
        SdkLog.s(exc);
    }

    /* access modifiers changed from: protected */
    public void secureLog(String str, String... strArr) {
        SdkLog.s(this.mApiName, str, strArr);
    }

    /* access modifiers changed from: protected */
    public void verboseLog(String str) {
        String str2 = this.mApiName;
        SdkLog.v(str2, str + " " + this.mReference);
    }
}
