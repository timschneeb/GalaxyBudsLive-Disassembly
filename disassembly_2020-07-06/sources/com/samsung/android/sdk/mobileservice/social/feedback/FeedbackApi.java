package com.samsung.android.sdk.mobileservice.social.feedback;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import com.samsung.android.sdk.mobileservice.SeMobileServiceSession;
import com.samsung.android.sdk.mobileservice.common.CommonConstants;
import com.samsung.android.sdk.mobileservice.common.ErrorCodeConvertor;
import com.samsung.android.sdk.mobileservice.common.api.SeMobileServiceApi;
import com.samsung.android.sdk.mobileservice.common.exception.NotAuthorizedException;
import com.samsung.android.sdk.mobileservice.common.exception.NotConnectedException;
import com.samsung.android.sdk.mobileservice.common.exception.NotSupportedApiException;
import com.samsung.android.sdk.mobileservice.common.result.CommonResultStatus;
import com.samsung.android.sdk.mobileservice.social.common.IBundleProgressResultCallback;
import com.samsung.android.sdk.mobileservice.social.feedback.EmotionMemberList;
import com.samsung.android.sdk.mobileservice.social.feedback.IFeedbackBundlePartialResultCallback;
import com.samsung.android.sdk.mobileservice.social.feedback.IFeedbackBundleResultCallback;
import com.samsung.android.sdk.mobileservice.social.feedback.result.FeedbackResult;
import com.samsung.android.sdk.mobileservice.social.group.provider.GroupContract;
import com.sec.android.diagmonagent.log.provider.utils.DiagMonUtil;
import java.util.ArrayList;
import java.util.List;

public class FeedbackApi extends SeMobileServiceApi {
    public static final String API_NAME = "FeedbackApi";

    public interface FeedbackDownloadResultCallback<T> {
        void onPartialResult(T t);

        void onResult(FeedbackResult<T> feedbackResult);
    }

    public interface FeedbackResultCallback<T> {
        void onResult(FeedbackResult<T> feedbackResult);
    }

    public interface ProfileImageListResultCallback<T> {
        void onProgress(T t);

        void onResult(FeedbackResult<List<T>> feedbackResult);
    }

    private boolean isResultSuccess(CommonResultStatus commonResultStatus) {
        return commonResultStatus == null;
    }

    public FeedbackApi(SeMobileServiceSession seMobileServiceSession) throws NotConnectedException, NotAuthorizedException, NotSupportedApiException {
        super(seMobileServiceSession, "FeedbackApi");
        checkAuthorized(0, 1);
    }

    /* access modifiers changed from: protected */
    public String[] getEssentialServiceNames() {
        return new String[]{"SocialService"};
    }

    public <T extends ContentId> int requestFeedback(T t, final FeedbackDownloadResultCallback<Feedback<T>> feedbackDownloadResultCallback) {
        debugLog("requestFeedback");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("contentId", t.toBundle());
            getSocialService().requestFeedback(bundle, new IFeedbackBundlePartialResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestFeedback onSuccess ");
                    FeedbackDownloadResultCallback feedbackDownloadResultCallback = feedbackDownloadResultCallback;
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), FeedbackApi.this.bundleToFeedback(bundle)));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestFeedback onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), null));
                    }
                }

                public void onPartialResult(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestFeedback onPartialResult ");
                    FeedbackDownloadResultCallback feedbackDownloadResultCallback = feedbackDownloadResultCallback;
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onPartialResult(FeedbackApi.this.bundleToFeedback(bundle));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public <T extends ContentId> int requestFeedbackForContentIdList(List<T> list, final FeedbackDownloadResultCallback<List<Feedback<T>>> feedbackDownloadResultCallback) {
        debugLog("requestFeedbackForContentIdList");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            ArrayList arrayList = new ArrayList();
            for (T bundle : list) {
                arrayList.add(bundle.toBundle());
            }
            Bundle bundle2 = new Bundle();
            bundle2.putParcelableArrayList("contentIds", arrayList);
            getSocialService().requestFeedback(bundle2, new IFeedbackBundlePartialResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestFeedbackForContentIdList onSuccess ");
                    FeedbackDownloadResultCallback feedbackDownloadResultCallback = feedbackDownloadResultCallback;
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), FeedbackApi.this.bundleToFeedbackList(bundle, new ArrayList())));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestFeedbackForContentIdList onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), null));
                    }
                }

                public void onPartialResult(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestFeedbackForContentIdList onPartialResult ");
                    FeedbackDownloadResultCallback feedbackDownloadResultCallback = feedbackDownloadResultCallback;
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onPartialResult(FeedbackApi.this.bundleToFeedbackList(bundle, new ArrayList()));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public <T extends ContentId> int requestCommentCreation(T t, String str, final FeedbackResultCallback<String> feedbackResultCallback) {
        debugLog("requestCommentCreation : contentId =[" + t + "]");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("contentId", t.toBundle());
            bundle.putString("comment", str);
            getSocialService().requestCommentCreation(bundle, new IFeedbackBundleResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestCommentCreation onSuccess ");
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), bundle.getString("commentId")));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestCommentCreation onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), null));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public <T extends ContentId> int requestMyCommentUpdate(T t, String str, String str2, final FeedbackResultCallback<Boolean> feedbackResultCallback) {
        debugLog("requestMyCommentUpdate : contentId=[" + t + "]");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("contentId", t.toBundle());
            bundle.putString("commentId", str);
            bundle.putString("comment", str2);
            getSocialService().requestCommentUpdate(bundle, new IFeedbackBundleResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestMyCommentUpdate onSuccess ");
                    FeedbackResultCallback feedbackResultCallback = feedbackResultCallback;
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), true));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestMyCommentUpdate onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), false));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public <T extends ContentId> int requestCommentDeletion(T t, String str, final FeedbackResultCallback<Boolean> feedbackResultCallback) {
        debugLog("requestCommentDeletion : contentId=[" + t + "] commentId=[" + str + "] ");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("contentId", t.toBundle());
            bundle.putString("commentId", str);
            getSocialService().requestCommentDeletion(bundle, new IFeedbackBundleResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestCommentDeletion onSuccess ");
                    FeedbackResultCallback feedbackResultCallback = feedbackResultCallback;
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), true));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestCommentDeletion onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), false));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public <T extends ContentId> int requestCommentList(T t, int i, FeedbackDownloadResultCallback<CommentList> feedbackDownloadResultCallback) {
        debugLog("requestCommentList : contentId=[" + t + "] ");
        return requestCommentList(t, i, (String) null, feedbackDownloadResultCallback);
    }

    public <T extends ContentId> int requestCommentList(T t, int i, String str, final FeedbackDownloadResultCallback<CommentList> feedbackDownloadResultCallback) {
        debugLog("requestCommentList : contentId=[" + t + "] limit=[" + i + "] nextCommentId=[" + str + "] ");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("contentId", t.toBundle());
            bundle.putInt("limit", i);
            if (!TextUtils.isEmpty(str)) {
                bundle.putString("nextCommentId", str);
            }
            getSocialService().requestCommentList(bundle, new IFeedbackBundlePartialResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestCommentList onSuccess ");
                    FeedbackDownloadResultCallback feedbackDownloadResultCallback = feedbackDownloadResultCallback;
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), FeedbackApi.this.bundleToCommentList(bundle)));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestCommentList onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), null));
                    }
                }

                public void onPartialResult(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestCommentList onPartialResult ");
                    FeedbackDownloadResultCallback feedbackDownloadResultCallback = feedbackDownloadResultCallback;
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onPartialResult(FeedbackApi.this.bundleToCommentList(bundle));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public <T extends ContentId> int requestEmotionUpdate(T t, int i, FeedbackResultCallback<Emotion> feedbackResultCallback) {
        debugLog("requestEmotionUpdate : contentId=[" + t + "] emotionType=[" + i + "] ");
        return requestEmotionUpdate(t, (String) null, i, feedbackResultCallback);
    }

    public <T extends ContentId> int requestEmotionUpdate(T t, String str, int i, final FeedbackResultCallback<Emotion> feedbackResultCallback) {
        debugLog("requestEmotionUpdate : contentId=[" + t + "] commentId=[" + str + "] ");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("contentId", t.toBundle());
            if (!TextUtils.isEmpty(str)) {
                bundle.putString("commentId", str);
            }
            bundle.putBoolean("cancelAction", false);
            bundle.putInt("emotionType", i);
            getSocialService().requestEmotionUpdate(bundle, new IFeedbackBundleResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestEmotionUpdate onSuccess ");
                    FeedbackResultCallback feedbackResultCallback = feedbackResultCallback;
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), FeedbackApi.this.bundleToEmotion(bundle)));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestEmotionUpdate onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), null));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public <T extends ContentId> int requestEmotionCancellation(T t, int i, FeedbackResultCallback<Emotion> feedbackResultCallback) {
        debugLog("requestEmotionCancellation : contentId=[" + t + "] previousEmotionType=[" + i + "] ");
        return requestEmotionCancellation(t, (String) null, i, feedbackResultCallback);
    }

    public <T extends ContentId> int requestEmotionCancellation(T t, String str, int i, final FeedbackResultCallback<Emotion> feedbackResultCallback) {
        debugLog("requestEmotionCancellation : contentId=[" + t + "] commentId=[" + str + "] previousEmotionType=[" + i + "] ");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("contentId", t.toBundle());
            if (!TextUtils.isEmpty(str)) {
                bundle.putString("commentId", str);
            }
            bundle.putBoolean("cancelAction", true);
            bundle.putInt("emotionType", i);
            getSocialService().requestEmotionUpdate(bundle, new IFeedbackBundleResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestEmotionCancellation onSuccess ");
                    if (feedbackResultCallback != null) {
                        bundle.getInt("count", -1);
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), FeedbackApi.this.bundleToEmotion(bundle)));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestEmotionCancellation onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackResultCallback != null) {
                        feedbackResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), null));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public <T extends ContentId> int requestEmotionMemberList(T t, int i, FeedbackDownloadResultCallback<EmotionMemberList<T>> feedbackDownloadResultCallback) {
        debugLog("requestEmotionMemberList : contentId=[" + t + "] ");
        return requestEmotionMemberList(t, (String) null, i, (String) null, feedbackDownloadResultCallback);
    }

    public <T extends ContentId> int requestEmotionMemberList(T t, int i, String str, FeedbackDownloadResultCallback<EmotionMemberList<T>> feedbackDownloadResultCallback) {
        debugLog("requestEmotionMemberList : contentId=[" + t + "] nextMemberGuid=[" + str + "] ");
        return requestEmotionMemberList(t, (String) null, i, str, feedbackDownloadResultCallback);
    }

    public <T extends ContentId> int requestEmotionMemberList(T t, String str, int i, FeedbackDownloadResultCallback<EmotionMemberList<T>> feedbackDownloadResultCallback) {
        debugLog("requestEmotionMemberList : contentId=[" + t + "] commentId=[" + str + "] ");
        return requestEmotionMemberList(t, str, i, (String) null, feedbackDownloadResultCallback);
    }

    public <T extends ContentId> int requestEmotionMemberList(final T t, final String str, int i, String str2, final FeedbackDownloadResultCallback<EmotionMemberList<T>> feedbackDownloadResultCallback) {
        debugLog("requestEmotionMemberList : contentId=[" + t + "] commentId=[" + str + "] ");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("contentId", t.toBundle());
            bundle.putInt("limit", i);
            if (!TextUtils.isEmpty(str)) {
                bundle.putString("commentId", str);
            }
            if (!TextUtils.isEmpty(str2)) {
                bundle.putString("nextMemberGuid", str2);
            }
            getSocialService().requestEmotionMemberList(bundle, new IFeedbackBundlePartialResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestEmotionMemberList onSuccess ");
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), FeedbackApi.this.bundleToActivityEmotionMemberList(t, str, bundle)));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestEmotionMemberList onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), null));
                    }
                }

                public void onPartialResult(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestEmotionMemberList onPartialResult ");
                    FeedbackDownloadResultCallback feedbackDownloadResultCallback = feedbackDownloadResultCallback;
                    if (feedbackDownloadResultCallback != null) {
                        feedbackDownloadResultCallback.onPartialResult(FeedbackApi.this.bundleToActivityEmotionMemberList(t, str, bundle));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            secureLog(e2);
            return -1;
        }
    }

    public FeedbackResult<List<Notification<?>>> getNotificationList(int i, boolean z) {
        debugLog("getNotificationList");
        FeedbackResult<List<Notification<?>>> errorFeedbackResult = getErrorFeedbackResult(CommonConstants.SupportedApiMinVersion.VERSION_10_0);
        if (errorFeedbackResult != null) {
            return errorFeedbackResult;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("contentIdType", i);
            bundle.putBoolean("requestClear", z);
            Bundle notification = getSocialService().getNotification(bundle);
            CommonResultStatus bundleToResult = bundleToResult(notification);
            if (isResultSuccess(bundleToResult)) {
                debugLog("getNotificationList success ");
                return new FeedbackResult<>(new CommonResultStatus(1), bundleToNotificationList(notification));
            }
            debugLog("getNotificationList fail");
            return new FeedbackResult<>(bundleToResult, null);
        } catch (NotConnectedException e) {
            secureLog(e);
            return new FeedbackResult<>(new CommonResultStatus(-8), null);
        } catch (Exception e2) {
            secureLog(e2);
            return new FeedbackResult<>(new CommonResultStatus(-1), null);
        }
    }

    public int requestProfileImageList(List<String> list, final ProfileImageListResultCallback<UserProfile> profileImageListResultCallback) {
        debugLog("requestProfileImageList");
        if (!isSupportedSemsAgentVersion(CommonConstants.SupportedApiMinVersion.VERSION_10_0)) {
            return -7;
        }
        try {
            ArrayList arrayList = new ArrayList();
            for (String next : list) {
                if (!TextUtils.isEmpty(next)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("guid", next);
                    arrayList.add(bundle);
                }
            }
            Bundle bundle2 = new Bundle();
            bundle2.putParcelableArrayList("guids", arrayList);
            getSocialService().requestProfileImageList(bundle2, new IBundleProgressResultCallback.Stub() {
                public void onSuccess(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestProfileImageList onSuccess ");
                    ProfileImageListResultCallback profileImageListResultCallback = profileImageListResultCallback;
                    if (profileImageListResultCallback != null) {
                        profileImageListResultCallback.onResult(new FeedbackResult(new CommonResultStatus(1), FeedbackApi.this.bundleToProfileImageList(bundle)));
                    }
                }

                public void onFailure(long j, String str) throws RemoteException {
                    FeedbackApi feedbackApi = FeedbackApi.this;
                    feedbackApi.debugLog("requestProfileImageList onFailure : code=[" + j + "], message=[" + str + "] ");
                    if (profileImageListResultCallback != null) {
                        profileImageListResultCallback.onResult(new FeedbackResult(new CommonResultStatus(ErrorCodeConvertor.convertErrorcode(j), str, Long.toString(j)), null));
                    }
                }

                public void onProgress(Bundle bundle) throws RemoteException {
                    FeedbackApi.this.debugLog("requestProfileImageList onProgress ");
                    ProfileImageListResultCallback profileImageListResultCallback = profileImageListResultCallback;
                    if (profileImageListResultCallback != null) {
                        profileImageListResultCallback.onProgress(FeedbackApi.this.bundleToProfileImage(bundle));
                    }
                }
            });
            return 1;
        } catch (NotConnectedException e) {
            debugLog("requestProfileImageList fail");
            secureLog(e);
            return -8;
        } catch (Exception e2) {
            debugLog("requestProfileImageList fail");
            secureLog(e2);
            return -1;
        }
    }

    private CommonResultStatus bundleToResult(Bundle bundle) {
        if (bundle == null) {
            debugLog("bundle is null : bundleToResult");
            return null;
        }
        long j = bundle.getLong(DiagMonUtil.ERROR_CODE, -1);
        if (j == -1) {
            debugLog("not error : bundleToResult");
            return null;
        }
        String string = bundle.getString("errorMessage", (String) null);
        int convertErrorcode = ErrorCodeConvertor.convertErrorcode(j);
        return new CommonResultStatus(convertErrorcode, string, Long.toString((long) convertErrorcode));
    }

    private ContentId bundleToContentId(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (bundle.getInt("contentIdType", -1) == 1) {
            return new ActivityContentId(bundle);
        }
        debugLog("bundleToContentId is error" + bundle.getInt("contentIdType", -1));
        return null;
    }

    /* access modifiers changed from: private */
    public Feedback<?> bundleToFeedback(Bundle bundle) {
        if (bundle == null) {
            debugLog("bundle is null : bundleToActivityFeedback");
            return null;
        }
        ArrayList<Bundle> parcelableArrayList = bundle.getParcelableArrayList("comments");
        ArrayList arrayList = new ArrayList();
        if (parcelableArrayList != null) {
            for (Bundle bundleToComment : parcelableArrayList) {
                arrayList.add(bundleToComment(bundleToComment));
            }
        }
        ArrayList<Bundle> parcelableArrayList2 = bundle.getParcelableArrayList("emotions");
        ArrayList arrayList2 = new ArrayList();
        if (parcelableArrayList2 != null) {
            for (Bundle bundleToEmotion : parcelableArrayList2) {
                arrayList2.add(bundleToEmotion(bundleToEmotion));
            }
        }
        ContentId bundleToContentId = bundleToContentId(bundle);
        int i = bundle.getInt("myEmotionType", -1);
        return new Feedback<>(bundleToContentId, new EmotionList(i, arrayList2), bundle.getInt("commentCount"), new CommentList(bundle.getString("nextCommentId"), arrayList));
    }

    /* access modifiers changed from: private */
    public <T extends ContentId> List<Feedback<T>> bundleToFeedbackList(Bundle bundle, List<Feedback<T>> list) {
        if (bundle == null) {
            debugLog("bundle is null : bundleToActivityFeedback");
            return null;
        }
        ArrayList<Bundle> parcelableArrayList = bundle.getParcelableArrayList("activities");
        if (parcelableArrayList != null) {
            for (Bundle bundleToFeedback : parcelableArrayList) {
                list.add(bundleToFeedback(bundleToFeedback));
            }
        }
        debugLog("bundleToFeedbackList size : " + list.size());
        return list;
    }

    /* access modifiers changed from: private */
    public CommentList bundleToCommentList(Bundle bundle) {
        if (bundle == null) {
            debugLog("bundle is null : bundleToCommentList");
            return null;
        }
        ArrayList<Bundle> parcelableArrayList = bundle.getParcelableArrayList("comments");
        ArrayList arrayList = new ArrayList();
        if (parcelableArrayList != null) {
            for (Bundle bundleToComment : parcelableArrayList) {
                arrayList.add(bundleToComment(bundleToComment));
            }
        }
        String string = bundle.getString("nextCommentId", (String) null);
        debugLog("bundleToCommentList size : " + arrayList.size());
        return new CommentList(string, arrayList);
    }

    private Comment bundleToComment(Bundle bundle) {
        Uri uri = null;
        if (bundle == null) {
            debugLog("bundle is null : bundleToComment");
            return null;
        }
        ArrayList<Bundle> parcelableArrayList = bundle.getParcelableArrayList("emotions");
        ArrayList arrayList = new ArrayList();
        if (parcelableArrayList != null) {
            for (Bundle bundleToEmotion : parcelableArrayList) {
                arrayList.add(bundleToEmotion(bundleToEmotion));
            }
        }
        String string = bundle.getString("commentId");
        String string2 = bundle.getString("comment");
        String string3 = bundle.getString(GroupContract.Group.LEADER_ID);
        String string4 = bundle.getString("ownerName", (String) null);
        long j = bundle.getLong(GroupContract.Group.CREATED_TIME, 0);
        int i = bundle.getInt("myEmotionType", -1);
        String string5 = bundle.getString("profileImageContentUri", (String) null);
        if (!TextUtils.isEmpty(string5)) {
            uri = Uri.parse(string5);
        }
        return new Comment(string, string2, new UserProfile(string3, string4, uri), j, new EmotionList(i, arrayList));
    }

    /* access modifiers changed from: private */
    public Emotion bundleToEmotion(Bundle bundle) {
        if (bundle != null) {
            return new Emotion(bundle.getInt("emotionType", 0), bundle.getInt("count", 0));
        }
        debugLog("bundle is null : bundleToEmotion");
        return null;
    }

    /* access modifiers changed from: private */
    public <T extends ContentId> EmotionMemberList<T> bundleToActivityEmotionMemberList(T t, String str, Bundle bundle) {
        if (bundle == null) {
            debugLog("bundle is null : bundleToActivityEmotionMemberList");
            return null;
        }
        String string = bundle.getString("nextMemberGuid");
        ArrayList arrayList = new ArrayList();
        ArrayList<Bundle> parcelableArrayList = bundle.getParcelableArrayList("members");
        if (parcelableArrayList != null) {
            for (Bundle bundle2 : parcelableArrayList) {
                String string2 = bundle2.getString("memberName", (String) null);
                String string3 = bundle2.getString("memberId");
                long j = bundle2.getLong("updateTime", 0);
                int i = bundle2.getInt("emotionType", 0);
                String string4 = bundle2.getString("profileImageContentUri", (String) null);
                arrayList.add(new EmotionMemberList.EmotionMember(new UserProfile(string3, string2, !TextUtils.isEmpty(string4) ? Uri.parse(string4) : null), j, i));
            }
        }
        debugLog("bundleToActivityEmotionMemberList size : " + arrayList.size());
        return new EmotionMemberList<>(t, str, string, arrayList);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v7, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v9, resolved type: android.net.Uri} */
    /* JADX WARNING: Multi-variable type inference failed */
    private List<Notification<?>> bundleToNotificationList(Bundle bundle) {
        Bundle bundle2 = bundle;
        String str = null;
        if (bundle2 == null) {
            debugLog("bundle is null : bundleToNotificationList");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList<Bundle> parcelableArrayList = bundle2.getParcelableArrayList("notifications");
        if (parcelableArrayList != null) {
            for (Bundle bundle3 : parcelableArrayList) {
                String string = bundle3.getString("notificationId");
                String string2 = bundle3.getString("senderGuid");
                String string3 = bundle3.getString("senderName", str);
                ContentId bundleToContentId = bundleToContentId(bundle3);
                String string4 = bundle3.getString("commentId", str);
                String string5 = bundle3.getString("comment", str);
                int i = bundle3.getInt("emotionType", -1);
                int i2 = Notification.FEEDBACK_TYPE_COMMENT;
                if (bundle3.containsKey("emotionType")) {
                    i2 = Notification.FEEDBACK_TYPE_EMOTION;
                }
                long j = bundle3.getLong("timestamp", 0);
                String string6 = bundle3.getString("profileImageContentUri", str);
                UserProfile userProfile = new UserProfile(string2, string3, !TextUtils.isEmpty(string6) ? Uri.parse(string6) : str);
                Notification notification = r6;
                Notification notification2 = new Notification(bundleToContentId, string, i2, userProfile, string4, string5, i, j);
                arrayList.add(notification);
                str = null;
            }
        }
        debugLog("bundleToNotificationList size : " + arrayList.size());
        return arrayList;
    }

    /* access modifiers changed from: private */
    public List<UserProfile> bundleToProfileImageList(Bundle bundle) {
        if (bundle == null) {
            debugLog("bundle is null : bundleToProfileImageList");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList<Bundle> parcelableArrayList = bundle.getParcelableArrayList("images");
        if (parcelableArrayList != null) {
            for (Bundle bundleToProfileImage : parcelableArrayList) {
                arrayList.add(bundleToProfileImage(bundleToProfileImage));
            }
        }
        debugLog("bundleToImageList size : " + arrayList.size());
        return arrayList;
    }

    /* access modifiers changed from: private */
    public UserProfile bundleToProfileImage(Bundle bundle) {
        Uri uri = null;
        if (bundle == null) {
            debugLog("bundle is null : bundleToProfileImage");
            return null;
        }
        String string = bundle.getString("guid");
        String string2 = bundle.getString("profileImageContentUri", (String) null);
        if (!TextUtils.isEmpty(string2)) {
            uri = Uri.parse(string2);
        }
        return new UserProfile(string, uri);
    }

    private <T> FeedbackResult<T> getErrorFeedbackResult(int i) {
        if (!isSupportedSemsAgentVersion(i)) {
            return new FeedbackResult<>(new CommonResultStatus(-7), null);
        }
        return null;
    }
}
