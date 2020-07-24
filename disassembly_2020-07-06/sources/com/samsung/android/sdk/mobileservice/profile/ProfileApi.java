package com.samsung.android.sdk.mobileservice.profile;

import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import com.samsung.android.sdk.mobileservice.SeMobileServiceSession;
import com.samsung.android.sdk.mobileservice.common.CommonConstants;
import com.samsung.android.sdk.mobileservice.common.ErrorCodeConvertor;
import com.samsung.android.sdk.mobileservice.common.api.SeMobileServiceApi;
import com.samsung.android.sdk.mobileservice.common.exception.NotAuthorizedException;
import com.samsung.android.sdk.mobileservice.common.exception.NotConnectedException;
import com.samsung.android.sdk.mobileservice.common.exception.NotSupportedApiException;
import com.samsung.android.sdk.mobileservice.common.result.BooleanResult;
import com.samsung.android.sdk.mobileservice.common.result.CommonResultStatus;
import com.samsung.android.sdk.mobileservice.profile.IPrivacyUpdateResultCallback;
import com.samsung.android.sdk.mobileservice.profile.IProfileUpdateResultCallback;
import com.samsung.android.sdk.mobileservice.profile.ISyncResultCallback;
import com.samsung.android.sdk.mobileservice.profile.result.PrivacyResult;
import com.samsung.android.sdk.mobileservice.profile.result.ProfileBirthdayTypeResult;
import com.samsung.android.sdk.mobileservice.profile.result.ProfileImageUrlResult;
import com.samsung.android.sdk.mobileservice.profile.result.ProfileResult;

public class ProfileApi extends SeMobileServiceApi {
    public static final String API_NAME = "ProfileApi";
    public static final int PRIVACY_TYPE_CONTACTS = 2;
    public static final int PRIVACY_TYPE_EVERYONE = 0;
    public static final String SERVICE_NAME = "ProfileService";
    private int mConnectedProfileVersion = 4;
    private int mPrivacyType = 0;

    public interface PrivacyUpdateResultCallback {
        void onResult(BooleanResult booleanResult);
    }

    public enum ProfileBirthdayType {
        SOLAR_BIRTHDAY,
        LUNAR_BIRTHDAY,
        LEAP_BIRTHDAY,
        INVALID
    }

    public interface ProfileUpdateResultCallback {
        void onResult(BooleanResult booleanResult);
    }

    public interface SyncResultCallback {
        void onResult(ProfileResult profileResult);
    }

    public ProfileApi(SeMobileServiceSession seMobileServiceSession) throws NotConnectedException, NotAuthorizedException, NotSupportedApiException {
        super(seMobileServiceSession, "ProfileApi");
        checkAuthorized(0);
        try {
            this.mConnectedProfileVersion = getProfileService().exchangeProfileVersion(4);
        } catch (RemoteException e) {
            secureLog(e);
        }
    }

    /* access modifiers changed from: protected */
    public String[] getEssentialServiceNames() {
        return new String[]{"ProfileService"};
    }

    public int requestSync(final SyncResultCallback syncResultCallback) {
        debugLog("requestSync ");
        try {
            getProfileService().requestSync(new ISyncResultCallback.Stub() {
                public void onSuccess(Profile profile) throws RemoteException {
                    ProfileApi.this.debugLog("requestSync onSuccess ");
                    SyncResultCallback syncResultCallback = syncResultCallback;
                    if (syncResultCallback != null) {
                        syncResultCallback.onResult(new ProfileResult(new CommonResultStatus(1), profile));
                    }
                }

                public void onFailure(String str, String str2) throws RemoteException {
                    ProfileApi profileApi = ProfileApi.this;
                    profileApi.debugLog("requestSync onFailure : code=[" + str + "], message=[" + str2 + "] ");
                    if (syncResultCallback != null) {
                        syncResultCallback.onResult(new ProfileResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(str), str2, str), (Profile) null));
                    }
                }
            });
            return 1;
        } catch (RemoteException | NotConnectedException | NullPointerException e) {
            secureLog(e);
            return -1;
        } catch (OutOfMemoryError unused) {
            verboseLog("OutOfMemoryError occurred");
            return -1;
        }
    }

    public ProfileResult getProfile() {
        debugLog("getProfile ");
        try {
            return new ProfileResult(new CommonResultStatus(1), getProfileService().getProfile());
        } catch (RemoteException | NotConnectedException | NullPointerException e) {
            secureLog(e);
            return new ProfileResult(new CommonResultStatus(-1, "RemoteException", "RemoteException"), (Profile) null);
        } catch (OutOfMemoryError unused) {
            verboseLog("OutOfMemoryError");
            return new ProfileResult(new CommonResultStatus(-1, "OutOfMemoryError", "OutOfMemoryError"), (Profile) null);
        }
    }

    public int requestProfileUpdate(Profile profile, final ProfileUpdateResultCallback profileUpdateResultCallback) {
        debugLog("requestProfileUpdate ");
        try {
            profile.setConnectedProfileVersion(this.mConnectedProfileVersion);
            getProfileService().requestProfileUpdate(profile, new IProfileUpdateResultCallback.Stub() {
                public void onResult() throws RemoteException {
                    ProfileApi.this.debugLog("requestProfileUpdate onResult ");
                    ProfileUpdateResultCallback profileUpdateResultCallback = profileUpdateResultCallback;
                    if (profileUpdateResultCallback != null) {
                        profileUpdateResultCallback.onResult(new BooleanResult(new CommonResultStatus(1), true));
                    }
                }

                public void onFailure(String str, String str2) throws RemoteException {
                    ProfileApi profileApi = ProfileApi.this;
                    profileApi.debugLog("requestProfileUpdate onFailure : code=[" + str + "], message=[" + str2 + "] ");
                    if (profileUpdateResultCallback != null) {
                        profileUpdateResultCallback.onResult(new BooleanResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(str), str2, str), false));
                    }
                }
            });
            return 1;
        } catch (TransactionTooLargeException e) {
            verboseLog("TransactionTooLargeException occurred");
            secureLog(e);
            return -1;
        } catch (RemoteException | NotConnectedException | NullPointerException e2) {
            secureLog(e2);
            return -1;
        } catch (OutOfMemoryError unused) {
            verboseLog("OutOfMemoryError occurred");
            return -1;
        }
    }

    public PrivacyResult getPrivacy() {
        debugLog("getPrivacy ");
        try {
            if (getProfileService() == null) {
                debugLog("profile service is null ");
                return new PrivacyResult(new CommonResultStatus(5), (Privacy) null);
            }
            return new PrivacyResult(new CommonResultStatus(1), new Privacy(getProfileService().getPrivacy()));
        } catch (RemoteException | NotConnectedException | NullPointerException e) {
            secureLog(e);
            return new PrivacyResult(new CommonResultStatus(-1), (Privacy) null);
        } catch (OutOfMemoryError unused) {
            verboseLog("OutOfMemoryError occurred");
            return new PrivacyResult(new CommonResultStatus(-1), (Privacy) null);
        }
    }

    public int requestPrivacyUpdate(Privacy privacy, final PrivacyUpdateResultCallback privacyUpdateResultCallback) {
        debugLog("requestPrivacyUpdate ");
        try {
            getProfileService().requestPrivacyUpdate(privacy.read(), new IPrivacyUpdateResultCallback.Stub() {
                public void onResult() throws RemoteException {
                    ProfileApi.this.debugLog("requestPrivacyUpdate onResult ");
                    PrivacyUpdateResultCallback privacyUpdateResultCallback = privacyUpdateResultCallback;
                    if (privacyUpdateResultCallback != null) {
                        privacyUpdateResultCallback.onResult(new BooleanResult(new CommonResultStatus(1), true));
                    }
                }

                public void onFailure(String str, String str2) throws RemoteException {
                    ProfileApi profileApi = ProfileApi.this;
                    profileApi.debugLog("requestPrivacyUpdate onFailure : code=[" + str + "], message=[" + str2 + "] ");
                    if (privacyUpdateResultCallback != null) {
                        privacyUpdateResultCallback.onResult(new BooleanResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(str), str2, str), false));
                    }
                }
            });
            return 1;
        } catch (RemoteException | NotConnectedException | NullPointerException e) {
            secureLog(e);
            return -1;
        } catch (OutOfMemoryError unused) {
            verboseLog("OutOfMemoryError occurred");
            return -1;
        }
    }

    public ProfileImageUrlResult getProfileImageUrl() {
        debugLog("getProfileImageUrl");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_6)) {
            return new ProfileImageUrlResult(new CommonResultStatus(200), "");
        }
        try {
            return new ProfileImageUrlResult(new CommonResultStatus(1), getProfileService().getProfileImageUrl());
        } catch (RemoteException | NotConnectedException | NullPointerException e) {
            secureLog(e);
            return new ProfileImageUrlResult(new CommonResultStatus(-1, "RemoteException", "RemoteException"), "");
        } catch (OutOfMemoryError unused) {
            verboseLog("OutOfMemoryError occurred");
            return new ProfileImageUrlResult(new CommonResultStatus(-1, "OutOfMemoryError", "OutOfMemoryError"), "");
        }
    }

    public ProfileBirthdayTypeResult getProfileBirthdayType() {
        debugLog("getProfileBirthdayType ");
        if (!isSupportedSaAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_11_0)) {
            return new ProfileBirthdayTypeResult(new CommonResultStatus(200), "");
        }
        try {
            return new ProfileBirthdayTypeResult(new CommonResultStatus(1), getProfileService().getProfileBirthdayType());
        } catch (RemoteException | NotConnectedException | NullPointerException e) {
            secureLog(e);
            return new ProfileBirthdayTypeResult(new CommonResultStatus(-1, e.getMessage(), e.getClass().getSimpleName()), "");
        } catch (OutOfMemoryError unused) {
            verboseLog("OutOfMemoryError occurred");
            return new ProfileBirthdayTypeResult(new CommonResultStatus(-1, "OutOfMemoryError", "OutOfMemoryError"), "");
        }
    }

    public void requestProfileBirthdayTypeUpdate(ProfileBirthdayType profileBirthdayType, final ProfileUpdateResultCallback profileUpdateResultCallback) {
        debugLog("requestProfileBirthdayTypeUpdate");
        if (!isSupportedSaAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_11_0)) {
            profileUpdateResultCallback.onResult(new BooleanResult(new CommonResultStatus(200), false));
        }
        try {
            getProfileService().requestProfileBirthdayTypeUpdate(convertProfileBirthdayTypeToString(profileBirthdayType), new IProfileUpdateResultCallback.Stub() {
                public void onResult() throws RemoteException {
                    ProfileApi.this.debugLog("requestProfileBirthdayTypeUpdate onResult ");
                    ProfileUpdateResultCallback profileUpdateResultCallback = profileUpdateResultCallback;
                    if (profileUpdateResultCallback != null) {
                        profileUpdateResultCallback.onResult(new BooleanResult(new CommonResultStatus(1), true));
                    }
                }

                public void onFailure(String str, String str2) throws RemoteException {
                    ProfileApi profileApi = ProfileApi.this;
                    profileApi.debugLog("requestProfileBirthdayTypeUpdate onFailure : code=[" + str + "], message=[" + str2 + "] ");
                    if (profileUpdateResultCallback != null) {
                        profileUpdateResultCallback.onResult(new BooleanResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(str), str2, str), false));
                    }
                }
            });
        } catch (RemoteException | NotConnectedException | NullPointerException e) {
            secureLog(e);
        } catch (OutOfMemoryError unused) {
            verboseLog("OutOfMemoryError occurred");
        }
    }

    /* renamed from: com.samsung.android.sdk.mobileservice.profile.ProfileApi$5  reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$sdk$mobileservice$profile$ProfileApi$ProfileBirthdayType = new int[ProfileBirthdayType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            $SwitchMap$com$samsung$android$sdk$mobileservice$profile$ProfileApi$ProfileBirthdayType[ProfileBirthdayType.SOLAR_BIRTHDAY.ordinal()] = 1;
            $SwitchMap$com$samsung$android$sdk$mobileservice$profile$ProfileApi$ProfileBirthdayType[ProfileBirthdayType.LUNAR_BIRTHDAY.ordinal()] = 2;
            try {
                $SwitchMap$com$samsung$android$sdk$mobileservice$profile$ProfileApi$ProfileBirthdayType[ProfileBirthdayType.LEAP_BIRTHDAY.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private String convertProfileBirthdayTypeToString(ProfileBirthdayType profileBirthdayType) {
        int i = AnonymousClass5.$SwitchMap$com$samsung$android$sdk$mobileservice$profile$ProfileApi$ProfileBirthdayType[profileBirthdayType.ordinal()];
        if (i == 1) {
            return "0";
        }
        if (i != 2) {
            return i != 3 ? "" : "2";
        }
        return "1";
    }
}
