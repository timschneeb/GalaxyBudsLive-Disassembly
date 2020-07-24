package com.samsung.android.sdk.mobileservice.social;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.sdk.mobileservice.social.activity.IActivityBundlePartialResultCallback;
import com.samsung.android.sdk.mobileservice.social.activity.IActivityBundleResultCallback;
import com.samsung.android.sdk.mobileservice.social.activity.IActivityResultCallback;
import com.samsung.android.sdk.mobileservice.social.activity.IDeleteAllActivityResultCallback;
import com.samsung.android.sdk.mobileservice.social.buddy.IBuddyInfoResultCallback;
import com.samsung.android.sdk.mobileservice.social.buddy.IPublicBuddyInfoResultCallback;
import com.samsung.android.sdk.mobileservice.social.buddy.IServiceActivationResultCallback;
import com.samsung.android.sdk.mobileservice.social.buddy.IServiceDeactivationResultCallback;
import com.samsung.android.sdk.mobileservice.social.buddy.ISyncResultCallback;
import com.samsung.android.sdk.mobileservice.social.common.IBundleProgressResultCallback;
import com.samsung.android.sdk.mobileservice.social.feedback.IFeedbackBundlePartialResultCallback;
import com.samsung.android.sdk.mobileservice.social.feedback.IFeedbackBundleResultCallback;
import com.samsung.android.sdk.mobileservice.social.group.IGroupCoverImageDownloadingResultCallback;
import com.samsung.android.sdk.mobileservice.social.group.IGroupInvitationResultCallback;
import com.samsung.android.sdk.mobileservice.social.group.IGroupListResultCallback;
import com.samsung.android.sdk.mobileservice.social.group.IGroupListWithInvitationResultCallback;
import com.samsung.android.sdk.mobileservice.social.group.IGroupRequestResultCallback;
import com.samsung.android.sdk.mobileservice.social.group.IGroupResultCallback;
import com.samsung.android.sdk.mobileservice.social.group.IGroupSyncResultCallback;
import com.samsung.android.sdk.mobileservice.social.group.IMemberListResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.IContentDownloadingResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.IDownloadThumbnailResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.IShareResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.IShareResultWithFileListCallback;
import com.samsung.android.sdk.mobileservice.social.share.IShareStatusCallback;
import com.samsung.android.sdk.mobileservice.social.share.IShareSyncResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISharedItemDeletionResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISharedItemListDeletionResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISharedItemListResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISharedItemResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISharedItemUpdateResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISpaceCoverImageDownloadingResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISpaceDeletionResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISpaceListResultCallback;
import com.samsung.android.sdk.mobileservice.social.share.ISpaceResultCallback;
import java.util.ArrayList;
import java.util.List;

public interface IMobileServiceSocial extends IInterface {
    int cancelShare(String str, String str2) throws RemoteException;

    void clearSpaceUnreadCount(String str, String str2) throws RemoteException;

    void clearSpaceUnreadCountWithData(String str, Bundle bundle, String str2) throws RemoteException;

    Bundle getBuddyActivityCount(Bundle bundle) throws RemoteException;

    Bundle getBuddyActivityList(Bundle bundle) throws RemoteException;

    void getBuddyInfo(Bundle bundle, IBuddyInfoResultCallback iBuddyInfoResultCallback) throws RemoteException;

    int getCountryTypeForAgreement() throws RemoteException;

    Bundle getDeviceAuthInfoCached() throws RemoteException;

    boolean getDisclaimerAgreementForService(Bundle bundle) throws RemoteException;

    boolean getDisclaimerAgreementForSocial() throws RemoteException;

    List<Bundle> getGroupList(String str) throws RemoteException;

    Intent getIntentToDisplay(Bundle bundle) throws RemoteException;

    Bundle getNotification(Bundle bundle) throws RemoteException;

    Bundle getServiceState() throws RemoteException;

    int getShareStatus(String str, String str2) throws RemoteException;

    int isServiceActivated(int i) throws RemoteException;

    boolean isServiceRegistered(Bundle bundle) throws RemoteException;

    Bundle isSomethingNeeded(Bundle bundle) throws RemoteException;

    int pauseShare(String str, String str2) throws RemoteException;

    void requestActivity(Bundle bundle, IActivityBundlePartialResultCallback iActivityBundlePartialResultCallback) throws RemoteException;

    void requestActivityChanges(IActivityBundlePartialResultCallback iActivityBundlePartialResultCallback) throws RemoteException;

    void requestActivityContent(Bundle bundle, IActivityBundleResultCallback iActivityBundleResultCallback) throws RemoteException;

    void requestActivityContentStreamingUrl(Bundle bundle, IActivityBundleResultCallback iActivityBundleResultCallback) throws RemoteException;

    void requestActivityDeletion(Bundle bundle, IActivityResultCallback iActivityResultCallback) throws RemoteException;

    void requestActivityImageList(Bundle bundle, IBundleProgressResultCallback iBundleProgressResultCallback) throws RemoteException;

    void requestActivityList(Bundle bundle, IActivityBundlePartialResultCallback iActivityBundlePartialResultCallback) throws RemoteException;

    Bundle requestActivityPosting(Bundle bundle, PendingIntent pendingIntent, IBundleProgressResultCallback iBundleProgressResultCallback) throws RemoteException;

    void requestActivitySync(IActivityBundleResultCallback iActivityBundleResultCallback) throws RemoteException;

    int requestAllSpaceList(String str, ISpaceListResultCallback iSpaceListResultCallback) throws RemoteException;

    int requestAllSpaceListWithData(String str, Bundle bundle, ISpaceListResultCallback iSpaceListResultCallback) throws RemoteException;

    void requestBuddySync(int i, ISyncResultCallback iSyncResultCallback) throws RemoteException;

    void requestCommentCreation(Bundle bundle, IFeedbackBundleResultCallback iFeedbackBundleResultCallback) throws RemoteException;

    void requestCommentDeletion(Bundle bundle, IFeedbackBundleResultCallback iFeedbackBundleResultCallback) throws RemoteException;

    void requestCommentList(Bundle bundle, IFeedbackBundlePartialResultCallback iFeedbackBundlePartialResultCallback) throws RemoteException;

    void requestCommentUpdate(Bundle bundle, IFeedbackBundleResultCallback iFeedbackBundleResultCallback) throws RemoteException;

    Bundle requestContentsController(Bundle bundle) throws RemoteException;

    int requestDelegateAuthorityOfOwner(String str, String str2, String str3, IGroupResultCallback iGroupResultCallback) throws RemoteException;

    void requestDeleteAllActivity(IDeleteAllActivityResultCallback iDeleteAllActivityResultCallback) throws RemoteException;

    void requestEmotionMemberList(Bundle bundle, IFeedbackBundlePartialResultCallback iFeedbackBundlePartialResultCallback) throws RemoteException;

    void requestEmotionUpdate(Bundle bundle, IFeedbackBundleResultCallback iFeedbackBundleResultCallback) throws RemoteException;

    void requestFeedback(Bundle bundle, IFeedbackBundlePartialResultCallback iFeedbackBundlePartialResultCallback) throws RemoteException;

    int requestGroup(String str, String str2, IGroupResultCallback iGroupResultCallback) throws RemoteException;

    int requestGroupCreation(String str, Bundle bundle, Bundle bundle2, IGroupInvitationResultCallback iGroupInvitationResultCallback) throws RemoteException;

    int requestGroupDeletion(String str, String str2, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException;

    int requestGroupInvitationAcceptance(String str, String str2, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException;

    int requestGroupInvitationRejection(String str, String str2, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException;

    int requestGroupList(String str, IGroupListResultCallback iGroupListResultCallback) throws RemoteException;

    int requestGroupMemberInvitation(String str, String str2, Bundle bundle, IGroupInvitationResultCallback iGroupInvitationResultCallback) throws RemoteException;

    int requestGroupMemberList(String str, String str2, IMemberListResultCallback iMemberListResultCallback) throws RemoteException;

    int requestGroupMemberRemoval(String str, String str2, String str3, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException;

    int requestGroupPendingInvitationCancellation(String str, String str2, String str3, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException;

    int requestGroupSync(String str, IGroupSyncResultCallback iGroupSyncResultCallback) throws RemoteException;

    int requestGroupSyncWithoutImage(String str, IGroupSyncResultCallback iGroupSyncResultCallback) throws RemoteException;

    int requestGroupUpdate(String str, String str2, Bundle bundle, IGroupResultCallback iGroupResultCallback) throws RemoteException;

    int requestGroupWithInvitationList(String str, IGroupListWithInvitationResultCallback iGroupListWithInvitationResultCallback) throws RemoteException;

    int requestInstantShare(String str, Bundle bundle, List<Bundle> list, IShareResultCallback iShareResultCallback) throws RemoteException;

    int requestLeave(String str, String str2, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException;

    void requestMyActivityPrivacy(IActivityBundleResultCallback iActivityBundleResultCallback) throws RemoteException;

    void requestMyActivityPrivacyUpdate(Bundle bundle, IActivityResultCallback iActivityResultCallback) throws RemoteException;

    int requestOriginalGroupImageDownload(String str, String str2, IGroupCoverImageDownloadingResultCallback iGroupCoverImageDownloadingResultCallback) throws RemoteException;

    int requestOriginalSharedContentWithFileListDownload(String str, String str2, String str3, List<String> list, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException;

    int requestOriginalSharedContentWithFileListDownloadWithData(String str, String str2, String str3, List<String> list, Bundle bundle, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException;

    int requestOriginalSharedContentWithItemFileListDownloadWithPath(String str, String str2, String str3, List<String> list, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle, String str4) throws RemoteException;

    int requestOriginalSharedContentWithItemFileListDownloadWithPathWithData(String str, String str2, String str3, List<String> list, Bundle bundle, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle2, String str4) throws RemoteException;

    int requestOriginalSharedContentsDownload(String str, String str2, String[] strArr, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException;

    int requestOriginalSharedContentsDownloadWithPath(String str, String str2, String[] strArr, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle, String str3) throws RemoteException;

    int requestOriginalSpaceImageDownload(String str, String str2, ISpaceCoverImageDownloadingResultCallback iSpaceCoverImageDownloadingResultCallback) throws RemoteException;

    void requestProfileImageList(Bundle bundle, IBundleProgressResultCallback iBundleProgressResultCallback) throws RemoteException;

    void requestPublicBuddyInfo(String str, IPublicBuddyInfoResultCallback iPublicBuddyInfoResultCallback) throws RemoteException;

    void requestServiceActivation(int i, IServiceActivationResultCallback iServiceActivationResultCallback) throws RemoteException;

    void requestServiceDeactivation(int i, IServiceDeactivationResultCallback iServiceDeactivationResultCallback) throws RemoteException;

    String requestShareListUpdateWithItemFileList(String str, String str2, List<Bundle> list, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException;

    String requestShareListUpdateWithItemFileListWithData(String str, String str2, List<Bundle> list, Bundle bundle, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException;

    int requestShareSync(String str, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException;

    int requestShareSyncWithData(String str, Bundle bundle, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException;

    String requestShareUpdateWithUriList(String str, String str2, String str3, Bundle bundle, IShareResultWithFileListCallback iShareResultWithFileListCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException;

    String requestShareWithFileList(String str, String str2, Bundle bundle, IShareResultWithFileListCallback iShareResultWithFileListCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException;

    String requestShareWithItemFileList(String str, String str2, List<Bundle> list, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException;

    String requestShareWithItemFileListWithData(String str, String str2, List<Bundle> list, Bundle bundle, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException;

    String requestShareWithPendingIntent(String str, String str2, List<Bundle> list, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException;

    int requestSharedItem(String str, String str2, String str3, ISharedItemResultCallback iSharedItemResultCallback) throws RemoteException;

    int requestSharedItemDeletion(String str, String str2, String str3, ISharedItemDeletionResultCallback iSharedItemDeletionResultCallback) throws RemoteException;

    int requestSharedItemList(String str, String str2, String str3, String str4, ISharedItemListResultCallback iSharedItemListResultCallback) throws RemoteException;

    int requestSharedItemListDeletion(String str, String str2, List<String> list, ISharedItemListDeletionResultCallback iSharedItemListDeletionResultCallback) throws RemoteException;

    int requestSharedItemListDeletionWithData(String str, String str2, List<String> list, Bundle bundle, ISharedItemListDeletionResultCallback iSharedItemListDeletionResultCallback) throws RemoteException;

    String requestSharedItemListUpdate(String str, String str2, List<Bundle> list, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException;

    int requestSharedItemListWithFileList(String str, String str2, String str3, String str4, ISharedItemListResultCallback iSharedItemListResultCallback) throws RemoteException;

    int requestSharedItemListWithFileListWithData(String str, String str2, String str3, String str4, Bundle bundle, ISharedItemListResultCallback iSharedItemListResultCallback) throws RemoteException;

    int requestSharedItemSync(String str, String str2, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException;

    void requestSharedItemSyncWithResolution(String str, Bundle bundle, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException;

    int requestSharedItemSyncWithResolutionWithData(String str, Bundle bundle, Bundle bundle2, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException;

    String requestSharedItemUpdate(String str, String str2, String str3, Bundle bundle, ISharedItemUpdateResultCallback iSharedItemUpdateResultCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException;

    int requestSharedItemWithGroupId(String str, String str2, String str3, String str4, ISharedItemResultCallback iSharedItemResultCallback) throws RemoteException;

    int requestSpace(String str, String str2, ISpaceResultCallback iSpaceResultCallback) throws RemoteException;

    int requestSpaceCreation(String str, String str2, Bundle bundle, ISpaceResultCallback iSpaceResultCallback) throws RemoteException;

    int requestSpaceCreationWithData(String str, String str2, Bundle bundle, Bundle bundle2, ISpaceResultCallback iSpaceResultCallback) throws RemoteException;

    int requestSpaceDeletion(String str, String str2, ISpaceDeletionResultCallback iSpaceDeletionResultCallback) throws RemoteException;

    int requestSpaceDeletionWithData(String str, String str2, Bundle bundle, ISpaceDeletionResultCallback iSpaceDeletionResultCallback) throws RemoteException;

    int requestSpaceList(String str, String str2, ISpaceListResultCallback iSpaceListResultCallback) throws RemoteException;

    int requestSpaceListSync(String str, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException;

    int requestSpaceListSyncWithData(String str, Bundle bundle, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException;

    int requestSpaceListWithData(String str, String str2, Bundle bundle, ISpaceListResultCallback iSpaceListResultCallback) throws RemoteException;

    int requestSpaceUpdate(String str, String str2, Bundle bundle, ISpaceResultCallback iSpaceResultCallback) throws RemoteException;

    int requestSpaceUpdateWithData(String str, String str2, Bundle bundle, Bundle bundle2, ISpaceResultCallback iSpaceResultCallback) throws RemoteException;

    int requestSpaceWithData(String str, String str2, Bundle bundle, ISpaceResultCallback iSpaceResultCallback) throws RemoteException;

    void requestSync(ISyncResultCallback iSyncResultCallback) throws RemoteException;

    int requestThumbnailDownload(String str, String str2, String str3, String str4, String str5, String str6, IDownloadThumbnailResultCallback iDownloadThumbnailResultCallback) throws RemoteException;

    int resumeShare(String str, String str2) throws RemoteException;

    Bundle setBuddyActivityListRead(Bundle bundle) throws RemoteException;

    boolean setDisclaimerAgreementForSocial(Bundle bundle) throws RemoteException;

    int setShareStatusListener(String str, String str2, IShareStatusCallback iShareStatusCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements IMobileServiceSocial {
        private static final String DESCRIPTOR = "com.samsung.android.sdk.mobileservice.social.IMobileServiceSocial";
        static final int TRANSACTION_cancelShare = 26;
        static final int TRANSACTION_clearSpaceUnreadCount = 29;
        static final int TRANSACTION_clearSpaceUnreadCountWithData = 95;
        static final int TRANSACTION_getBuddyActivityCount = 81;
        static final int TRANSACTION_getBuddyActivityList = 46;
        static final int TRANSACTION_getBuddyInfo = 110;
        static final int TRANSACTION_getCountryTypeForAgreement = 61;
        static final int TRANSACTION_getDeviceAuthInfoCached = 107;
        static final int TRANSACTION_getDisclaimerAgreementForService = 106;
        static final int TRANSACTION_getDisclaimerAgreementForSocial = 105;
        static final int TRANSACTION_getGroupList = 10;
        static final int TRANSACTION_getIntentToDisplay = 43;
        static final int TRANSACTION_getNotification = 59;
        static final int TRANSACTION_getServiceState = 68;
        static final int TRANSACTION_getShareStatus = 27;
        static final int TRANSACTION_isServiceActivated = 2;
        static final int TRANSACTION_isServiceRegistered = 108;
        static final int TRANSACTION_isSomethingNeeded = 67;
        static final int TRANSACTION_pauseShare = 24;
        static final int TRANSACTION_requestActivity = 51;
        static final int TRANSACTION_requestActivityChanges = 74;
        static final int TRANSACTION_requestActivityContent = 86;
        static final int TRANSACTION_requestActivityContentStreamingUrl = 84;
        static final int TRANSACTION_requestActivityDeletion = 45;
        static final int TRANSACTION_requestActivityImageList = 69;
        static final int TRANSACTION_requestActivityList = 44;
        static final int TRANSACTION_requestActivityPosting = 83;
        static final int TRANSACTION_requestActivitySync = 50;
        static final int TRANSACTION_requestAllSpaceList = 20;
        static final int TRANSACTION_requestAllSpaceListWithData = 94;
        static final int TRANSACTION_requestBuddySync = 109;
        static final int TRANSACTION_requestCommentCreation = 54;
        static final int TRANSACTION_requestCommentDeletion = 56;
        static final int TRANSACTION_requestCommentList = 53;
        static final int TRANSACTION_requestCommentUpdate = 55;
        static final int TRANSACTION_requestContentsController = 85;
        static final int TRANSACTION_requestDelegateAuthorityOfOwner = 71;
        static final int TRANSACTION_requestDeleteAllActivity = 6;
        static final int TRANSACTION_requestEmotionMemberList = 58;
        static final int TRANSACTION_requestEmotionUpdate = 57;
        static final int TRANSACTION_requestFeedback = 52;
        static final int TRANSACTION_requestGroup = 9;
        static final int TRANSACTION_requestGroupCreation = 32;
        static final int TRANSACTION_requestGroupDeletion = 33;
        static final int TRANSACTION_requestGroupInvitationAcceptance = 35;
        static final int TRANSACTION_requestGroupInvitationRejection = 36;
        static final int TRANSACTION_requestGroupList = 8;
        static final int TRANSACTION_requestGroupMemberInvitation = 34;
        static final int TRANSACTION_requestGroupMemberList = 37;
        static final int TRANSACTION_requestGroupMemberRemoval = 39;
        static final int TRANSACTION_requestGroupPendingInvitationCancellation = 41;
        static final int TRANSACTION_requestGroupSync = 7;
        static final int TRANSACTION_requestGroupSyncWithoutImage = 87;
        static final int TRANSACTION_requestGroupUpdate = 73;
        static final int TRANSACTION_requestGroupWithInvitationList = 38;
        static final int TRANSACTION_requestInstantShare = 42;
        static final int TRANSACTION_requestLeave = 40;
        static final int TRANSACTION_requestMyActivityPrivacy = 49;
        static final int TRANSACTION_requestMyActivityPrivacyUpdate = 48;
        static final int TRANSACTION_requestOriginalGroupImageDownload = 21;
        static final int TRANSACTION_requestOriginalSharedContentWithFileListDownload = 63;
        static final int TRANSACTION_requestOriginalSharedContentWithFileListDownloadWithData = 102;
        static final int TRANSACTION_requestOriginalSharedContentWithItemFileListDownloadWithPath = 80;
        static final int TRANSACTION_requestOriginalSharedContentWithItemFileListDownloadWithPathWithData = 101;
        static final int TRANSACTION_requestOriginalSharedContentsDownload = 23;
        static final int TRANSACTION_requestOriginalSharedContentsDownloadWithPath = 75;
        static final int TRANSACTION_requestOriginalSpaceImageDownload = 22;
        static final int TRANSACTION_requestProfileImageList = 70;
        static final int TRANSACTION_requestPublicBuddyInfo = 5;
        static final int TRANSACTION_requestServiceActivation = 3;
        static final int TRANSACTION_requestServiceDeactivation = 4;
        static final int TRANSACTION_requestShareListUpdateWithItemFileList = 79;
        static final int TRANSACTION_requestShareListUpdateWithItemFileListWithData = 100;
        static final int TRANSACTION_requestShareSync = 11;
        static final int TRANSACTION_requestShareSyncWithData = 88;
        static final int TRANSACTION_requestShareUpdateWithUriList = 64;
        static final int TRANSACTION_requestShareWithFileList = 62;
        static final int TRANSACTION_requestShareWithItemFileList = 77;
        static final int TRANSACTION_requestShareWithItemFileListWithData = 99;
        static final int TRANSACTION_requestShareWithPendingIntent = 12;
        static final int TRANSACTION_requestSharedItem = 13;
        static final int TRANSACTION_requestSharedItemDeletion = 14;
        static final int TRANSACTION_requestSharedItemList = 65;
        static final int TRANSACTION_requestSharedItemListDeletion = 76;
        static final int TRANSACTION_requestSharedItemListDeletionWithData = 98;
        static final int TRANSACTION_requestSharedItemListUpdate = 78;
        static final int TRANSACTION_requestSharedItemListWithFileList = 66;
        static final int TRANSACTION_requestSharedItemListWithFileListWithData = 103;
        static final int TRANSACTION_requestSharedItemSync = 31;
        static final int TRANSACTION_requestSharedItemSyncWithResolution = 82;
        static final int TRANSACTION_requestSharedItemSyncWithResolutionWithData = 97;
        static final int TRANSACTION_requestSharedItemUpdate = 60;
        static final int TRANSACTION_requestSharedItemWithGroupId = 72;
        static final int TRANSACTION_requestSpace = 15;
        static final int TRANSACTION_requestSpaceCreation = 16;
        static final int TRANSACTION_requestSpaceCreationWithData = 90;
        static final int TRANSACTION_requestSpaceDeletion = 17;
        static final int TRANSACTION_requestSpaceDeletionWithData = 91;
        static final int TRANSACTION_requestSpaceList = 18;
        static final int TRANSACTION_requestSpaceListSync = 30;
        static final int TRANSACTION_requestSpaceListSyncWithData = 96;
        static final int TRANSACTION_requestSpaceListWithData = 92;
        static final int TRANSACTION_requestSpaceUpdate = 19;
        static final int TRANSACTION_requestSpaceUpdateWithData = 93;
        static final int TRANSACTION_requestSpaceWithData = 89;
        static final int TRANSACTION_requestSync = 1;
        static final int TRANSACTION_requestThumbnailDownload = 111;
        static final int TRANSACTION_resumeShare = 25;
        static final int TRANSACTION_setBuddyActivityListRead = 47;
        static final int TRANSACTION_setDisclaimerAgreementForSocial = 104;
        static final int TRANSACTION_setShareStatusListener = 28;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMobileServiceSocial asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IMobileServiceSocial)) {
                return new Proxy(iBinder);
            }
            return (IMobileServiceSocial) queryLocalInterface;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v2, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v9, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v12, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v15, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v18, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v21, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v24, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v27, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v30, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v33, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v36, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v39, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v42, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v45, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v48, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v51, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v54, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v57, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v60, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v71, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v74, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v77, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v82, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v90, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v93, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v96, resolved type: android.app.PendingIntent} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v99, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v102, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v105, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v108, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v111, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v114, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v116, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v119, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v122, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v124, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v127, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v130, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v133, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v136, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v143, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v146, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v149, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v152, resolved type: android.os.Bundle} */
        /* JADX WARNING: type inference failed for: r4v0 */
        /* JADX WARNING: type inference failed for: r4v115 */
        /* JADX WARNING: type inference failed for: r4v123 */
        /* JADX WARNING: type inference failed for: r4v137 */
        /* JADX WARNING: type inference failed for: r4v157 */
        /* JADX WARNING: type inference failed for: r4v158 */
        /* JADX WARNING: type inference failed for: r4v159 */
        /* JADX WARNING: type inference failed for: r4v160 */
        /* JADX WARNING: type inference failed for: r4v161 */
        /* JADX WARNING: type inference failed for: r4v162 */
        /* JADX WARNING: type inference failed for: r4v163 */
        /* JADX WARNING: type inference failed for: r4v164 */
        /* JADX WARNING: type inference failed for: r4v165 */
        /* JADX WARNING: type inference failed for: r4v166 */
        /* JADX WARNING: type inference failed for: r4v167 */
        /* JADX WARNING: type inference failed for: r4v168 */
        /* JADX WARNING: type inference failed for: r4v169 */
        /* JADX WARNING: type inference failed for: r4v170 */
        /* JADX WARNING: type inference failed for: r4v171 */
        /* JADX WARNING: type inference failed for: r4v172 */
        /* JADX WARNING: type inference failed for: r4v173 */
        /* JADX WARNING: type inference failed for: r4v174 */
        /* JADX WARNING: type inference failed for: r4v175 */
        /* JADX WARNING: type inference failed for: r4v176 */
        /* JADX WARNING: type inference failed for: r4v177 */
        /* JADX WARNING: type inference failed for: r4v178 */
        /* JADX WARNING: type inference failed for: r4v179 */
        /* JADX WARNING: type inference failed for: r4v180 */
        /* JADX WARNING: type inference failed for: r4v181 */
        /* JADX WARNING: type inference failed for: r4v182 */
        /* JADX WARNING: type inference failed for: r4v183 */
        /* JADX WARNING: type inference failed for: r4v184 */
        /* JADX WARNING: type inference failed for: r4v185 */
        /* JADX WARNING: type inference failed for: r4v186 */
        /* JADX WARNING: type inference failed for: r4v187 */
        /* JADX WARNING: type inference failed for: r4v188 */
        /* JADX WARNING: type inference failed for: r4v189 */
        /* JADX WARNING: type inference failed for: r4v190 */
        /* JADX WARNING: type inference failed for: r4v191 */
        /* JADX WARNING: type inference failed for: r4v192 */
        /* JADX WARNING: type inference failed for: r4v193 */
        /* JADX WARNING: type inference failed for: r4v194 */
        /* JADX WARNING: type inference failed for: r4v195 */
        /* JADX WARNING: type inference failed for: r4v196 */
        /* JADX WARNING: type inference failed for: r4v197 */
        /* JADX WARNING: type inference failed for: r4v198 */
        /* JADX WARNING: Multi-variable type inference failed */
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = i;
            Parcel parcel3 = parcel;
            Parcel parcel4 = parcel2;
            if (i3 != 1598968902) {
                ? r4 = 0;
                switch (i3) {
                    case 1:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestSync(ISyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 2:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int isServiceActivated = isServiceActivated(parcel.readInt());
                        parcel2.writeNoException();
                        parcel4.writeInt(isServiceActivated);
                        return true;
                    case 3:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestServiceActivation(parcel.readInt(), IServiceActivationResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestServiceDeactivation(parcel.readInt(), IServiceDeactivationResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestPublicBuddyInfo(parcel.readString(), IPublicBuddyInfoResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestDeleteAllActivity(IDeleteAllActivityResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 7:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupSync = requestGroupSync(parcel.readString(), IGroupSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupSync);
                        return true;
                    case 8:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupList = requestGroupList(parcel.readString(), IGroupListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupList);
                        return true;
                    case 9:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroup = requestGroup(parcel.readString(), parcel.readString(), IGroupResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroup);
                        return true;
                    case 10:
                        parcel3.enforceInterface(DESCRIPTOR);
                        List<Bundle> groupList = getGroupList(parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeTypedList(groupList);
                        return true;
                    case 11:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestShareSync = requestShareSync(parcel.readString(), IShareSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestShareSync);
                        return true;
                    case 12:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestShareWithPendingIntent = requestShareWithPendingIntent(parcel.readString(), parcel.readString(), parcel3.createTypedArrayList(Bundle.CREATOR), IShareResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestShareWithPendingIntent);
                        return true;
                    case 13:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSharedItem = requestSharedItem(parcel.readString(), parcel.readString(), parcel.readString(), ISharedItemResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItem);
                        return true;
                    case 14:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSharedItemDeletion = requestSharedItemDeletion(parcel.readString(), parcel.readString(), parcel.readString(), ISharedItemDeletionResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemDeletion);
                        return true;
                    case 15:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSpace = requestSpace(parcel.readString(), parcel.readString(), ISpaceResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpace);
                        return true;
                    case 16:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString = parcel.readString();
                        String readString2 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSpaceCreation = requestSpaceCreation(readString, readString2, r4, ISpaceResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceCreation);
                        return true;
                    case 17:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSpaceDeletion = requestSpaceDeletion(parcel.readString(), parcel.readString(), ISpaceDeletionResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceDeletion);
                        return true;
                    case 18:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSpaceList = requestSpaceList(parcel.readString(), parcel.readString(), ISpaceListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceList);
                        return true;
                    case 19:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString3 = parcel.readString();
                        String readString4 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSpaceUpdate = requestSpaceUpdate(readString3, readString4, r4, ISpaceResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceUpdate);
                        return true;
                    case 20:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestAllSpaceList = requestAllSpaceList(parcel.readString(), ISpaceListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestAllSpaceList);
                        return true;
                    case 21:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestOriginalGroupImageDownload = requestOriginalGroupImageDownload(parcel.readString(), parcel.readString(), IGroupCoverImageDownloadingResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestOriginalGroupImageDownload);
                        return true;
                    case 22:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestOriginalSpaceImageDownload = requestOriginalSpaceImageDownload(parcel.readString(), parcel.readString(), ISpaceCoverImageDownloadingResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestOriginalSpaceImageDownload);
                        return true;
                    case 23:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestOriginalSharedContentsDownload = requestOriginalSharedContentsDownload(parcel.readString(), parcel.readString(), parcel.createStringArray(), IContentDownloadingResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeInt(requestOriginalSharedContentsDownload);
                        return true;
                    case 24:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int pauseShare = pauseShare(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(pauseShare);
                        return true;
                    case 25:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int resumeShare = resumeShare(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(resumeShare);
                        return true;
                    case 26:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int cancelShare = cancelShare(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(cancelShare);
                        return true;
                    case 27:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int shareStatus = getShareStatus(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(shareStatus);
                        return true;
                    case 28:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int shareStatusListener = setShareStatusListener(parcel.readString(), parcel.readString(), IShareStatusCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(shareStatusListener);
                        return true;
                    case 29:
                        parcel3.enforceInterface(DESCRIPTOR);
                        clearSpaceUnreadCount(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 30:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSpaceListSync = requestSpaceListSync(parcel.readString(), IShareSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceListSync);
                        return true;
                    case 31:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSharedItemSync = requestSharedItemSync(parcel.readString(), parcel.readString(), IShareSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemSync);
                        return true;
                    case 32:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString5 = parcel.readString();
                        Bundle bundle = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null;
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestGroupCreation = requestGroupCreation(readString5, bundle, r4, IGroupInvitationResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupCreation);
                        return true;
                    case 33:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupDeletion = requestGroupDeletion(parcel.readString(), parcel.readString(), IGroupRequestResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupDeletion);
                        return true;
                    case 34:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString6 = parcel.readString();
                        String readString7 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestGroupMemberInvitation = requestGroupMemberInvitation(readString6, readString7, r4, IGroupInvitationResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupMemberInvitation);
                        return true;
                    case 35:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupInvitationAcceptance = requestGroupInvitationAcceptance(parcel.readString(), parcel.readString(), IGroupRequestResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupInvitationAcceptance);
                        return true;
                    case 36:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupInvitationRejection = requestGroupInvitationRejection(parcel.readString(), parcel.readString(), IGroupRequestResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupInvitationRejection);
                        return true;
                    case 37:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupMemberList = requestGroupMemberList(parcel.readString(), parcel.readString(), IMemberListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupMemberList);
                        return true;
                    case 38:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupWithInvitationList = requestGroupWithInvitationList(parcel.readString(), IGroupListWithInvitationResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupWithInvitationList);
                        return true;
                    case 39:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupMemberRemoval = requestGroupMemberRemoval(parcel.readString(), parcel.readString(), parcel.readString(), IGroupRequestResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupMemberRemoval);
                        return true;
                    case 40:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestLeave = requestLeave(parcel.readString(), parcel.readString(), IGroupRequestResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestLeave);
                        return true;
                    case 41:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupPendingInvitationCancellation = requestGroupPendingInvitationCancellation(parcel.readString(), parcel.readString(), parcel.readString(), IGroupRequestResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupPendingInvitationCancellation);
                        return true;
                    case 42:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString8 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestInstantShare = requestInstantShare(readString8, r4, parcel3.createTypedArrayList(Bundle.CREATOR), IShareResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestInstantShare);
                        return true;
                    case 43:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        Intent intentToDisplay = getIntentToDisplay(r4);
                        parcel2.writeNoException();
                        if (intentToDisplay != null) {
                            parcel4.writeInt(1);
                            intentToDisplay.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 44:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestActivityList(r4, IActivityBundlePartialResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 45:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestActivityDeletion(r4, IActivityResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 46:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        Bundle buddyActivityList = getBuddyActivityList(r4);
                        parcel2.writeNoException();
                        if (buddyActivityList != null) {
                            parcel4.writeInt(1);
                            buddyActivityList.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 47:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        Bundle buddyActivityListRead = setBuddyActivityListRead(r4);
                        parcel2.writeNoException();
                        if (buddyActivityListRead != null) {
                            parcel4.writeInt(1);
                            buddyActivityListRead.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 48:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestMyActivityPrivacyUpdate(r4, IActivityResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 49:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestMyActivityPrivacy(IActivityBundleResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 50:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestActivitySync(IActivityBundleResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 51:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestActivity(r4, IActivityBundlePartialResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 52:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestFeedback(r4, IFeedbackBundlePartialResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 53:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestCommentList(r4, IFeedbackBundlePartialResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 54:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestCommentCreation(r4, IFeedbackBundleResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 55:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestCommentUpdate(r4, IFeedbackBundleResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 56:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestCommentDeletion(r4, IFeedbackBundleResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 57:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestEmotionUpdate(r4, IFeedbackBundleResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 58:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestEmotionMemberList(r4, IFeedbackBundlePartialResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 59:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        Bundle notification = getNotification(r4);
                        parcel2.writeNoException();
                        if (notification != null) {
                            parcel4.writeInt(1);
                            notification.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 60:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestSharedItemUpdate = requestSharedItemUpdate(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, ISharedItemUpdateResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestSharedItemUpdate);
                        return true;
                    case 61:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int countryTypeForAgreement = getCountryTypeForAgreement();
                        parcel2.writeNoException();
                        parcel4.writeInt(countryTypeForAgreement);
                        return true;
                    case 62:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestShareWithFileList = requestShareWithFileList(parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, IShareResultWithFileListCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestShareWithFileList);
                        return true;
                    case 63:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestOriginalSharedContentWithFileListDownload = requestOriginalSharedContentWithFileListDownload(parcel.readString(), parcel.readString(), parcel.readString(), parcel.createStringArrayList(), IContentDownloadingResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeInt(requestOriginalSharedContentWithFileListDownload);
                        return true;
                    case 64:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestShareUpdateWithUriList = requestShareUpdateWithUriList(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, IShareResultWithFileListCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestShareUpdateWithUriList);
                        return true;
                    case 65:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSharedItemList = requestSharedItemList(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), ISharedItemListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemList);
                        return true;
                    case 66:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSharedItemListWithFileList = requestSharedItemListWithFileList(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), ISharedItemListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemListWithFileList);
                        return true;
                    case 67:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        Bundle isSomethingNeeded = isSomethingNeeded(r4);
                        parcel2.writeNoException();
                        if (isSomethingNeeded != null) {
                            parcel4.writeInt(1);
                            isSomethingNeeded.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 68:
                        parcel3.enforceInterface(DESCRIPTOR);
                        Bundle serviceState = getServiceState();
                        parcel2.writeNoException();
                        if (serviceState != null) {
                            parcel4.writeInt(1);
                            serviceState.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 69:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestActivityImageList(r4, IBundleProgressResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 70:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestProfileImageList(r4, IBundleProgressResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 71:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestDelegateAuthorityOfOwner = requestDelegateAuthorityOfOwner(parcel.readString(), parcel.readString(), parcel.readString(), IGroupResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestDelegateAuthorityOfOwner);
                        return true;
                    case 72:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSharedItemWithGroupId = requestSharedItemWithGroupId(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), ISharedItemResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemWithGroupId);
                        return true;
                    case 73:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString9 = parcel.readString();
                        String readString10 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestGroupUpdate = requestGroupUpdate(readString9, readString10, r4, IGroupResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupUpdate);
                        return true;
                    case 74:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestActivityChanges(IActivityBundlePartialResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 75:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestOriginalSharedContentsDownloadWithPath = requestOriginalSharedContentsDownloadWithPath(parcel.readString(), parcel.readString(), parcel.createStringArray(), IContentDownloadingResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(requestOriginalSharedContentsDownloadWithPath);
                        return true;
                    case 76:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSharedItemListDeletion = requestSharedItemListDeletion(parcel.readString(), parcel.readString(), parcel.createStringArrayList(), ISharedItemListDeletionResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemListDeletion);
                        return true;
                    case 77:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestShareWithItemFileList = requestShareWithItemFileList(parcel.readString(), parcel.readString(), parcel3.createTypedArrayList(Bundle.CREATOR), IShareResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestShareWithItemFileList);
                        return true;
                    case 78:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestSharedItemListUpdate = requestSharedItemListUpdate(parcel.readString(), parcel.readString(), parcel3.createTypedArrayList(Bundle.CREATOR), IShareResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestSharedItemListUpdate);
                        return true;
                    case 79:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestShareListUpdateWithItemFileList = requestShareListUpdateWithItemFileList(parcel.readString(), parcel.readString(), parcel3.createTypedArrayList(Bundle.CREATOR), IShareResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestShareListUpdateWithItemFileList);
                        return true;
                    case 80:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestOriginalSharedContentWithItemFileListDownloadWithPath = requestOriginalSharedContentWithItemFileListDownloadWithPath(parcel.readString(), parcel.readString(), parcel.readString(), parcel.createStringArrayList(), IContentDownloadingResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(requestOriginalSharedContentWithItemFileListDownloadWithPath);
                        return true;
                    case 81:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        Bundle buddyActivityCount = getBuddyActivityCount(r4);
                        parcel2.writeNoException();
                        if (buddyActivityCount != null) {
                            parcel4.writeInt(1);
                            buddyActivityCount.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 82:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString11 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestSharedItemSyncWithResolution(readString11, r4, IShareSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 83:
                        parcel3.enforceInterface(DESCRIPTOR);
                        Bundle bundle2 = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null;
                        if (parcel.readInt() != 0) {
                            r4 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3);
                        }
                        Bundle requestActivityPosting = requestActivityPosting(bundle2, r4, IBundleProgressResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        if (requestActivityPosting != null) {
                            parcel4.writeInt(1);
                            requestActivityPosting.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 84:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestActivityContentStreamingUrl(r4, IActivityBundleResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 85:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        Bundle requestContentsController = requestContentsController(r4);
                        parcel2.writeNoException();
                        if (requestContentsController != null) {
                            parcel4.writeInt(1);
                            requestContentsController.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 86:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        requestActivityContent(r4, IActivityBundleResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 87:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestGroupSyncWithoutImage = requestGroupSyncWithoutImage(parcel.readString(), IGroupSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestGroupSyncWithoutImage);
                        return true;
                    case 88:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString12 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestShareSyncWithData = requestShareSyncWithData(readString12, r4, IShareSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestShareSyncWithData);
                        return true;
                    case 89:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString13 = parcel.readString();
                        String readString14 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSpaceWithData = requestSpaceWithData(readString13, readString14, r4, ISpaceResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceWithData);
                        return true;
                    case 90:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString15 = parcel.readString();
                        String readString16 = parcel.readString();
                        Bundle bundle3 = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null;
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSpaceCreationWithData = requestSpaceCreationWithData(readString15, readString16, bundle3, r4, ISpaceResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceCreationWithData);
                        return true;
                    case 91:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString17 = parcel.readString();
                        String readString18 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSpaceDeletionWithData = requestSpaceDeletionWithData(readString17, readString18, r4, ISpaceDeletionResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceDeletionWithData);
                        return true;
                    case 92:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString19 = parcel.readString();
                        String readString20 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSpaceListWithData = requestSpaceListWithData(readString19, readString20, r4, ISpaceListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceListWithData);
                        return true;
                    case 93:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString21 = parcel.readString();
                        String readString22 = parcel.readString();
                        Bundle bundle4 = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null;
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSpaceUpdateWithData = requestSpaceUpdateWithData(readString21, readString22, bundle4, r4, ISpaceResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceUpdateWithData);
                        return true;
                    case 94:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString23 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestAllSpaceListWithData = requestAllSpaceListWithData(readString23, r4, ISpaceListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestAllSpaceListWithData);
                        return true;
                    case 95:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString24 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        clearSpaceUnreadCountWithData(readString24, r4, parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 96:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString25 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSpaceListSyncWithData = requestSpaceListSyncWithData(readString25, r4, IShareSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSpaceListSyncWithData);
                        return true;
                    case 97:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString26 = parcel.readString();
                        Bundle bundle5 = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null;
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSharedItemSyncWithResolutionWithData = requestSharedItemSyncWithResolutionWithData(readString26, bundle5, r4, IShareSyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemSyncWithResolutionWithData);
                        return true;
                    case 98:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String readString27 = parcel.readString();
                        String readString28 = parcel.readString();
                        ArrayList<String> createStringArrayList = parcel.createStringArrayList();
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        int requestSharedItemListDeletionWithData = requestSharedItemListDeletionWithData(readString27, readString28, createStringArrayList, r4, ISharedItemListDeletionResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemListDeletionWithData);
                        return true;
                    case 99:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestShareWithItemFileListWithData = requestShareWithItemFileListWithData(parcel.readString(), parcel.readString(), parcel3.createTypedArrayList(Bundle.CREATOR), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, IShareResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestShareWithItemFileListWithData);
                        return true;
                    case 100:
                        parcel3.enforceInterface(DESCRIPTOR);
                        String requestShareListUpdateWithItemFileListWithData = requestShareListUpdateWithItemFileListWithData(parcel.readString(), parcel.readString(), parcel3.createTypedArrayList(Bundle.CREATOR), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, IShareResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeString(requestShareListUpdateWithItemFileListWithData);
                        return true;
                    case 101:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestOriginalSharedContentWithItemFileListDownloadWithPathWithData = requestOriginalSharedContentWithItemFileListDownloadWithPathWithData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.createStringArrayList(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, IContentDownloadingResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(requestOriginalSharedContentWithItemFileListDownloadWithPathWithData);
                        return true;
                    case 102:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestOriginalSharedContentWithFileListDownloadWithData = requestOriginalSharedContentWithFileListDownloadWithData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.createStringArrayList(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, IContentDownloadingResultCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel3) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null);
                        parcel2.writeNoException();
                        parcel4.writeInt(requestOriginalSharedContentWithFileListDownloadWithData);
                        return true;
                    case 103:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestSharedItemListWithFileListWithData = requestSharedItemListWithFileListWithData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel3) : null, ISharedItemListResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestSharedItemListWithFileListWithData);
                        return true;
                    case 104:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        boolean disclaimerAgreementForSocial = setDisclaimerAgreementForSocial(r4);
                        parcel2.writeNoException();
                        parcel4.writeInt(disclaimerAgreementForSocial);
                        return true;
                    case 105:
                        parcel3.enforceInterface(DESCRIPTOR);
                        boolean disclaimerAgreementForSocial2 = getDisclaimerAgreementForSocial();
                        parcel2.writeNoException();
                        parcel4.writeInt(disclaimerAgreementForSocial2);
                        return true;
                    case 106:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        boolean disclaimerAgreementForService = getDisclaimerAgreementForService(r4);
                        parcel2.writeNoException();
                        parcel4.writeInt(disclaimerAgreementForService);
                        return true;
                    case 107:
                        parcel3.enforceInterface(DESCRIPTOR);
                        Bundle deviceAuthInfoCached = getDeviceAuthInfoCached();
                        parcel2.writeNoException();
                        if (deviceAuthInfoCached != null) {
                            parcel4.writeInt(1);
                            deviceAuthInfoCached.writeToParcel(parcel4, 1);
                        } else {
                            parcel4.writeInt(0);
                        }
                        return true;
                    case 108:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        boolean isServiceRegistered = isServiceRegistered(r4);
                        parcel2.writeNoException();
                        parcel4.writeInt(isServiceRegistered);
                        return true;
                    case 109:
                        parcel3.enforceInterface(DESCRIPTOR);
                        requestBuddySync(parcel.readInt(), ISyncResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 110:
                        parcel3.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            r4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        }
                        getBuddyInfo(r4, IBuddyInfoResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 111:
                        parcel3.enforceInterface(DESCRIPTOR);
                        int requestThumbnailDownload = requestThumbnailDownload(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), IDownloadThumbnailResultCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel4.writeInt(requestThumbnailDownload);
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel4.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IMobileServiceSocial {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void requestSync(ISyncResultCallback iSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iSyncResultCallback != null ? iSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int isServiceActivated(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestServiceActivation(int i, IServiceActivationResultCallback iServiceActivationResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iServiceActivationResultCallback != null ? iServiceActivationResultCallback.asBinder() : null);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestServiceDeactivation(int i, IServiceDeactivationResultCallback iServiceDeactivationResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iServiceDeactivationResultCallback != null ? iServiceDeactivationResultCallback.asBinder() : null);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestPublicBuddyInfo(String str, IPublicBuddyInfoResultCallback iPublicBuddyInfoResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iPublicBuddyInfoResultCallback != null ? iPublicBuddyInfoResultCallback.asBinder() : null);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestDeleteAllActivity(IDeleteAllActivityResultCallback iDeleteAllActivityResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iDeleteAllActivityResultCallback != null ? iDeleteAllActivityResultCallback.asBinder() : null);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupSync(String str, IGroupSyncResultCallback iGroupSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iGroupSyncResultCallback != null ? iGroupSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupList(String str, IGroupListResultCallback iGroupListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iGroupListResultCallback != null ? iGroupListResultCallback.asBinder() : null);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroup(String str, String str2, IGroupResultCallback iGroupResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iGroupResultCallback != null ? iGroupResultCallback.asBinder() : null);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<Bundle> getGroupList(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(Bundle.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestShareSync(String str, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iShareSyncResultCallback != null ? iShareSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestShareWithPendingIntent(String str, String str2, List<Bundle> list, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedList(list);
                    obtain.writeStrongBinder(iShareResultCallback != null ? iShareResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItem(String str, String str2, String str3, ISharedItemResultCallback iSharedItemResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStrongBinder(iSharedItemResultCallback != null ? iSharedItemResultCallback.asBinder() : null);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemDeletion(String str, String str2, String str3, ISharedItemDeletionResultCallback iSharedItemDeletionResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStrongBinder(iSharedItemDeletionResultCallback != null ? iSharedItemDeletionResultCallback.asBinder() : null);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpace(String str, String str2, ISpaceResultCallback iSpaceResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iSpaceResultCallback != null ? iSpaceResultCallback.asBinder() : null);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceCreation(String str, String str2, Bundle bundle, ISpaceResultCallback iSpaceResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSpaceResultCallback != null ? iSpaceResultCallback.asBinder() : null);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceDeletion(String str, String str2, ISpaceDeletionResultCallback iSpaceDeletionResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iSpaceDeletionResultCallback != null ? iSpaceDeletionResultCallback.asBinder() : null);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceList(String str, String str2, ISpaceListResultCallback iSpaceListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iSpaceListResultCallback != null ? iSpaceListResultCallback.asBinder() : null);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceUpdate(String str, String str2, Bundle bundle, ISpaceResultCallback iSpaceResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSpaceResultCallback != null ? iSpaceResultCallback.asBinder() : null);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestAllSpaceList(String str, ISpaceListResultCallback iSpaceListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iSpaceListResultCallback != null ? iSpaceListResultCallback.asBinder() : null);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestOriginalGroupImageDownload(String str, String str2, IGroupCoverImageDownloadingResultCallback iGroupCoverImageDownloadingResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iGroupCoverImageDownloadingResultCallback != null ? iGroupCoverImageDownloadingResultCallback.asBinder() : null);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestOriginalSpaceImageDownload(String str, String str2, ISpaceCoverImageDownloadingResultCallback iSpaceCoverImageDownloadingResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iSpaceCoverImageDownloadingResultCallback != null ? iSpaceCoverImageDownloadingResultCallback.asBinder() : null);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestOriginalSharedContentsDownload(String str, String str2, String[] strArr, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStringArray(strArr);
                    obtain.writeStrongBinder(iContentDownloadingResultCallback != null ? iContentDownloadingResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int pauseShare(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int resumeShare(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int cancelShare(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getShareStatus(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setShareStatusListener(String str, String str2, IShareStatusCallback iShareStatusCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iShareStatusCallback != null ? iShareStatusCallback.asBinder() : null);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearSpaceUnreadCount(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceListSync(String str, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iShareSyncResultCallback != null ? iShareSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemSync(String str, String str2, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iShareSyncResultCallback != null ? iShareSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupCreation(String str, Bundle bundle, Bundle bundle2, IGroupInvitationResultCallback iGroupInvitationResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iGroupInvitationResultCallback != null ? iGroupInvitationResultCallback.asBinder() : null);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupDeletion(String str, String str2, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iGroupRequestResultCallback != null ? iGroupRequestResultCallback.asBinder() : null);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupMemberInvitation(String str, String str2, Bundle bundle, IGroupInvitationResultCallback iGroupInvitationResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iGroupInvitationResultCallback != null ? iGroupInvitationResultCallback.asBinder() : null);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupInvitationAcceptance(String str, String str2, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iGroupRequestResultCallback != null ? iGroupRequestResultCallback.asBinder() : null);
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupInvitationRejection(String str, String str2, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iGroupRequestResultCallback != null ? iGroupRequestResultCallback.asBinder() : null);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupMemberList(String str, String str2, IMemberListResultCallback iMemberListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iMemberListResultCallback != null ? iMemberListResultCallback.asBinder() : null);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupWithInvitationList(String str, IGroupListWithInvitationResultCallback iGroupListWithInvitationResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iGroupListWithInvitationResultCallback != null ? iGroupListWithInvitationResultCallback.asBinder() : null);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupMemberRemoval(String str, String str2, String str3, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStrongBinder(iGroupRequestResultCallback != null ? iGroupRequestResultCallback.asBinder() : null);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestLeave(String str, String str2, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iGroupRequestResultCallback != null ? iGroupRequestResultCallback.asBinder() : null);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupPendingInvitationCancellation(String str, String str2, String str3, IGroupRequestResultCallback iGroupRequestResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStrongBinder(iGroupRequestResultCallback != null ? iGroupRequestResultCallback.asBinder() : null);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestInstantShare(String str, Bundle bundle, List<Bundle> list, IShareResultCallback iShareResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeTypedList(list);
                    obtain.writeStrongBinder(iShareResultCallback != null ? iShareResultCallback.asBinder() : null);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Intent getIntentToDisplay(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestActivityList(Bundle bundle, IActivityBundlePartialResultCallback iActivityBundlePartialResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iActivityBundlePartialResultCallback != null ? iActivityBundlePartialResultCallback.asBinder() : null);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestActivityDeletion(Bundle bundle, IActivityResultCallback iActivityResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iActivityResultCallback != null ? iActivityResultCallback.asBinder() : null);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getBuddyActivityList(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle setBuddyActivityListRead(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestMyActivityPrivacyUpdate(Bundle bundle, IActivityResultCallback iActivityResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iActivityResultCallback != null ? iActivityResultCallback.asBinder() : null);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestMyActivityPrivacy(IActivityBundleResultCallback iActivityBundleResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iActivityBundleResultCallback != null ? iActivityBundleResultCallback.asBinder() : null);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestActivitySync(IActivityBundleResultCallback iActivityBundleResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iActivityBundleResultCallback != null ? iActivityBundleResultCallback.asBinder() : null);
                    this.mRemote.transact(50, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestActivity(Bundle bundle, IActivityBundlePartialResultCallback iActivityBundlePartialResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iActivityBundlePartialResultCallback != null ? iActivityBundlePartialResultCallback.asBinder() : null);
                    this.mRemote.transact(51, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestFeedback(Bundle bundle, IFeedbackBundlePartialResultCallback iFeedbackBundlePartialResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iFeedbackBundlePartialResultCallback != null ? iFeedbackBundlePartialResultCallback.asBinder() : null);
                    this.mRemote.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestCommentList(Bundle bundle, IFeedbackBundlePartialResultCallback iFeedbackBundlePartialResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iFeedbackBundlePartialResultCallback != null ? iFeedbackBundlePartialResultCallback.asBinder() : null);
                    this.mRemote.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestCommentCreation(Bundle bundle, IFeedbackBundleResultCallback iFeedbackBundleResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iFeedbackBundleResultCallback != null ? iFeedbackBundleResultCallback.asBinder() : null);
                    this.mRemote.transact(54, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestCommentUpdate(Bundle bundle, IFeedbackBundleResultCallback iFeedbackBundleResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iFeedbackBundleResultCallback != null ? iFeedbackBundleResultCallback.asBinder() : null);
                    this.mRemote.transact(55, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestCommentDeletion(Bundle bundle, IFeedbackBundleResultCallback iFeedbackBundleResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iFeedbackBundleResultCallback != null ? iFeedbackBundleResultCallback.asBinder() : null);
                    this.mRemote.transact(56, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestEmotionUpdate(Bundle bundle, IFeedbackBundleResultCallback iFeedbackBundleResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iFeedbackBundleResultCallback != null ? iFeedbackBundleResultCallback.asBinder() : null);
                    this.mRemote.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestEmotionMemberList(Bundle bundle, IFeedbackBundlePartialResultCallback iFeedbackBundlePartialResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iFeedbackBundlePartialResultCallback != null ? iFeedbackBundlePartialResultCallback.asBinder() : null);
                    this.mRemote.transact(58, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getNotification(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(59, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestSharedItemUpdate(String str, String str2, String str3, Bundle bundle, ISharedItemUpdateResultCallback iSharedItemUpdateResultCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSharedItemUpdateResultCallback != null ? iSharedItemUpdateResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(60, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getCountryTypeForAgreement() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(61, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestShareWithFileList(String str, String str2, Bundle bundle, IShareResultWithFileListCallback iShareResultWithFileListCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iShareResultWithFileListCallback != null ? iShareResultWithFileListCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(62, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestOriginalSharedContentWithFileListDownload(String str, String str2, String str3, List<String> list, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStringList(list);
                    obtain.writeStrongBinder(iContentDownloadingResultCallback != null ? iContentDownloadingResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(63, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestShareUpdateWithUriList(String str, String str2, String str3, Bundle bundle, IShareResultWithFileListCallback iShareResultWithFileListCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iShareResultWithFileListCallback != null ? iShareResultWithFileListCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(64, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemList(String str, String str2, String str3, String str4, ISharedItemListResultCallback iSharedItemListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeStrongBinder(iSharedItemListResultCallback != null ? iSharedItemListResultCallback.asBinder() : null);
                    this.mRemote.transact(65, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemListWithFileList(String str, String str2, String str3, String str4, ISharedItemListResultCallback iSharedItemListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeStrongBinder(iSharedItemListResultCallback != null ? iSharedItemListResultCallback.asBinder() : null);
                    this.mRemote.transact(66, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle isSomethingNeeded(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(67, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getServiceState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(68, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestActivityImageList(Bundle bundle, IBundleProgressResultCallback iBundleProgressResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iBundleProgressResultCallback != null ? iBundleProgressResultCallback.asBinder() : null);
                    this.mRemote.transact(69, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestProfileImageList(Bundle bundle, IBundleProgressResultCallback iBundleProgressResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iBundleProgressResultCallback != null ? iBundleProgressResultCallback.asBinder() : null);
                    this.mRemote.transact(70, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestDelegateAuthorityOfOwner(String str, String str2, String str3, IGroupResultCallback iGroupResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStrongBinder(iGroupResultCallback != null ? iGroupResultCallback.asBinder() : null);
                    this.mRemote.transact(71, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemWithGroupId(String str, String str2, String str3, String str4, ISharedItemResultCallback iSharedItemResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeStrongBinder(iSharedItemResultCallback != null ? iSharedItemResultCallback.asBinder() : null);
                    this.mRemote.transact(72, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupUpdate(String str, String str2, Bundle bundle, IGroupResultCallback iGroupResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iGroupResultCallback != null ? iGroupResultCallback.asBinder() : null);
                    this.mRemote.transact(73, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestActivityChanges(IActivityBundlePartialResultCallback iActivityBundlePartialResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iActivityBundlePartialResultCallback != null ? iActivityBundlePartialResultCallback.asBinder() : null);
                    this.mRemote.transact(74, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestOriginalSharedContentsDownloadWithPath(String str, String str2, String[] strArr, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStringArray(strArr);
                    obtain.writeStrongBinder(iContentDownloadingResultCallback != null ? iContentDownloadingResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str3);
                    this.mRemote.transact(75, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemListDeletion(String str, String str2, List<String> list, ISharedItemListDeletionResultCallback iSharedItemListDeletionResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStringList(list);
                    obtain.writeStrongBinder(iSharedItemListDeletionResultCallback != null ? iSharedItemListDeletionResultCallback.asBinder() : null);
                    this.mRemote.transact(76, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestShareWithItemFileList(String str, String str2, List<Bundle> list, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedList(list);
                    obtain.writeStrongBinder(iShareResultCallback != null ? iShareResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(77, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestSharedItemListUpdate(String str, String str2, List<Bundle> list, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedList(list);
                    obtain.writeStrongBinder(iShareResultCallback != null ? iShareResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(78, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestShareListUpdateWithItemFileList(String str, String str2, List<Bundle> list, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedList(list);
                    obtain.writeStrongBinder(iShareResultCallback != null ? iShareResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(79, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestOriginalSharedContentWithItemFileListDownloadWithPath(String str, String str2, String str3, List<String> list, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStringList(list);
                    obtain.writeStrongBinder(iContentDownloadingResultCallback != null ? iContentDownloadingResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str4);
                    this.mRemote.transact(80, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getBuddyActivityCount(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(81, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestSharedItemSyncWithResolution(String str, Bundle bundle, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iShareSyncResultCallback != null ? iShareSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(82, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle requestActivityPosting(Bundle bundle, PendingIntent pendingIntent, IBundleProgressResultCallback iBundleProgressResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    Bundle bundle2 = null;
                    obtain.writeStrongBinder(iBundleProgressResultCallback != null ? iBundleProgressResultCallback.asBinder() : null);
                    this.mRemote.transact(83, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    return bundle2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestActivityContentStreamingUrl(Bundle bundle, IActivityBundleResultCallback iActivityBundleResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iActivityBundleResultCallback != null ? iActivityBundleResultCallback.asBinder() : null);
                    this.mRemote.transact(84, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle requestContentsController(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(85, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestActivityContent(Bundle bundle, IActivityBundleResultCallback iActivityBundleResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iActivityBundleResultCallback != null ? iActivityBundleResultCallback.asBinder() : null);
                    this.mRemote.transact(86, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestGroupSyncWithoutImage(String str, IGroupSyncResultCallback iGroupSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iGroupSyncResultCallback != null ? iGroupSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(87, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestShareSyncWithData(String str, Bundle bundle, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iShareSyncResultCallback != null ? iShareSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(88, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceWithData(String str, String str2, Bundle bundle, ISpaceResultCallback iSpaceResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSpaceResultCallback != null ? iSpaceResultCallback.asBinder() : null);
                    this.mRemote.transact(89, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceCreationWithData(String str, String str2, Bundle bundle, Bundle bundle2, ISpaceResultCallback iSpaceResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSpaceResultCallback != null ? iSpaceResultCallback.asBinder() : null);
                    this.mRemote.transact(90, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceDeletionWithData(String str, String str2, Bundle bundle, ISpaceDeletionResultCallback iSpaceDeletionResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSpaceDeletionResultCallback != null ? iSpaceDeletionResultCallback.asBinder() : null);
                    this.mRemote.transact(91, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceListWithData(String str, String str2, Bundle bundle, ISpaceListResultCallback iSpaceListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSpaceListResultCallback != null ? iSpaceListResultCallback.asBinder() : null);
                    this.mRemote.transact(92, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceUpdateWithData(String str, String str2, Bundle bundle, Bundle bundle2, ISpaceResultCallback iSpaceResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSpaceResultCallback != null ? iSpaceResultCallback.asBinder() : null);
                    this.mRemote.transact(93, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestAllSpaceListWithData(String str, Bundle bundle, ISpaceListResultCallback iSpaceListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSpaceListResultCallback != null ? iSpaceListResultCallback.asBinder() : null);
                    this.mRemote.transact(94, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearSpaceUnreadCountWithData(String str, Bundle bundle, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str2);
                    this.mRemote.transact(95, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSpaceListSyncWithData(String str, Bundle bundle, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iShareSyncResultCallback != null ? iShareSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(96, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemSyncWithResolutionWithData(String str, Bundle bundle, Bundle bundle2, IShareSyncResultCallback iShareSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iShareSyncResultCallback != null ? iShareSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(97, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemListDeletionWithData(String str, String str2, List<String> list, Bundle bundle, ISharedItemListDeletionResultCallback iSharedItemListDeletionResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStringList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSharedItemListDeletionResultCallback != null ? iSharedItemListDeletionResultCallback.asBinder() : null);
                    this.mRemote.transact(98, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestShareWithItemFileListWithData(String str, String str2, List<Bundle> list, Bundle bundle, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iShareResultCallback != null ? iShareResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(99, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestShareListUpdateWithItemFileListWithData(String str, String str2, List<Bundle> list, Bundle bundle, IShareResultCallback iShareResultCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iShareResultCallback != null ? iShareResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(100, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestOriginalSharedContentWithItemFileListDownloadWithPathWithData(String str, String str2, String str3, List<String> list, Bundle bundle, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle2, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStringList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iContentDownloadingResultCallback != null ? iContentDownloadingResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str4);
                    this.mRemote.transact(101, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestOriginalSharedContentWithFileListDownloadWithData(String str, String str2, String str3, List<String> list, Bundle bundle, IContentDownloadingResultCallback iContentDownloadingResultCallback, PendingIntent pendingIntent, Bundle bundle2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStringList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iContentDownloadingResultCallback != null ? iContentDownloadingResultCallback.asBinder() : null);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(102, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSharedItemListWithFileListWithData(String str, String str2, String str3, String str4, Bundle bundle, ISharedItemListResultCallback iSharedItemListResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSharedItemListResultCallback != null ? iSharedItemListResultCallback.asBinder() : null);
                    this.mRemote.transact(103, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setDisclaimerAgreementForSocial(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(104, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getDisclaimerAgreementForSocial() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(105, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getDisclaimerAgreementForService(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(106, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getDeviceAuthInfoCached() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(107, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isServiceRegistered(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(108, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestBuddySync(int i, ISyncResultCallback iSyncResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iSyncResultCallback != null ? iSyncResultCallback.asBinder() : null);
                    this.mRemote.transact(109, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getBuddyInfo(Bundle bundle, IBuddyInfoResultCallback iBuddyInfoResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iBuddyInfoResultCallback != null ? iBuddyInfoResultCallback.asBinder() : null);
                    this.mRemote.transact(110, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestThumbnailDownload(String str, String str2, String str3, String str4, String str5, String str6, IDownloadThumbnailResultCallback iDownloadThumbnailResultCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeString(str5);
                    obtain.writeString(str6);
                    obtain.writeStrongBinder(iDownloadThumbnailResultCallback != null ? iDownloadThumbnailResultCallback.asBinder() : null);
                    this.mRemote.transact(111, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
