package com.samsung.android.sdk.mobileservice;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import com.samsung.android.sdk.mobileservice.common.CommonConstants;
import com.samsung.android.sdk.mobileservice.common.CommonUtils;
import com.samsung.android.sdk.mobileservice.util.SdkLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeMobileService {
    private static final String APP_ID_DAILY_BOARD = "wbu28c1241";
    private static final String APP_ID_GALLERY = "22n6hzkam0";
    private static final String APP_ID_SES_CALENDAR = "ses_calendar";
    private static final String APP_ID_SOCIAL_APP = "504k7c7fnz";
    private static final int COMMON_COLUMN_INDEX_LATEST_VERSION = 0;
    private static final String MOBILE_SERVICE_PROVIDER_PATH_COMMON = "common";
    private static final String MOBILE_SERVICE_PROVIDER_PATH_SERVICE_FEATURE = "feature";
    private static final String MOBILE_SERVICE_PROVIDER_PATH_SERVICE_STATUS = "status";
    private static final String MOBILE_SERVICE_PROVIDER_PATH_SOCIALFEATURE = "socialfeature";
    private static final String MOBILE_SERVICE_PROVIDER_URI = "com.samsung.android.mobileservice.provider.MobileServiceCapabilityProvider";
    private static final String SAMSUNG_ACCOUNT_PROVIDER_URI = "com.samsung.android.samsungaccount.mobileservice.SamsungAccountCapabilityProvider";
    public static final String SOCIAL_FEATURE_ACTIVITY_FEEDBACK_SERVICE = "activity_feedback_service";
    public static final String SOCIAL_FEATURE_ACTIVITY_HASH_CONTENTS = "activity_hash_contents";
    public static final String SOCIAL_FEATURE_BUDDY_CERTIFICATE_SHARING = "buddy_certificate_sharing";
    public static final String SOCIAL_FEATURE_FAMILY_GROUP_SHARING = "family_group_sharing";
    public static final String SOCIAL_FEATURE_GLOBAL_GROUP_SHARING = "global_group_sharing";
    public static final String SOCIAL_FEATURE_INSTANT_SHARING = "instant_sharing";
    public static final String SOCIAL_FEATURE_LOCAL_GROUP_SHARING = "local_group_sharing";
    public static final String SOCIAL_FEATURE_SOCIAL_ACTIVITY_SERVICE = "social_activity_service";
    private static final String TAG = "SeMobileService";

    public static int getSdkVersionCode() {
        SdkLog.d(TAG, "SDK Version Code = 1150100000");
        return BuildConfig.VERSION_CODE;
    }

    public static String getSdkVersionName() {
        SdkLog.d(TAG, "SDK Version Name = 11.5.01");
        return BuildConfig.VERSION_NAME;
    }

    public static int getAgentVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.samsung.android.mobileservice", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            SdkLog.s(e);
            return -1;
        }
    }

    public static boolean isAgentInstalled(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.samsung.android.mobileservice", 1);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            SdkLog.d(TAG, "Agent not installed");
            return false;
        }
    }

    public static int getSaAgentVersion(Context context) {
        if (!CommonUtils.isStandAloneSamsungAccountSupported(context)) {
            return -1;
        }
        try {
            return context.getPackageManager().getPackageInfo(CommonUtils.SAMSUNG_ACCOUNT_PACKAGE_NAME, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            SdkLog.s(e);
            return -1;
        }
    }

    public static boolean isSaAgentInstalled(Context context) {
        if (CommonUtils.isStandAloneSamsungAccountSupported(context)) {
            try {
                context.getPackageManager().getPackageInfo(CommonUtils.SAMSUNG_ACCOUNT_PACKAGE_NAME, 1);
                return true;
            } catch (PackageManager.NameNotFoundException unused) {
                SdkLog.d(TAG, "Samsung Account Agent not installed");
            }
        }
        return false;
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [java.util.Map<java.lang.String, java.util.List<java.lang.String>>, android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r7v1, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x008c, code lost:
        if (r7 != null) goto L_0x009d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009b, code lost:
        if (r7 == 0) goto L_0x00a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x009d, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00a4, code lost:
        if (com.samsung.android.sdk.mobileservice.common.CommonUtils.isStandAloneSamsungAccountSupported(r8) == false) goto L_0x00a9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a6, code lost:
        mergeFeatureListOfSaAgent(r8, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00a9, code lost:
        return r0;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    public static Map<String, List<String>> getFeatureList(Context context) {
        HashMap hashMap = new HashMap();
        ContentResolver contentResolver = context.getContentResolver();
        ? r7 = 0;
        if (!CommonUtils.isAgentSupportMinVersion(CommonConstants.SupportedApiMinVersion.VERSION_4_1, context)) {
            return r7;
        }
        try {
            Cursor query = contentResolver.query(new Uri.Builder().scheme("content").encodedAuthority(MOBILE_SERVICE_PROVIDER_URI).appendEncodedPath(MOBILE_SERVICE_PROVIDER_PATH_SERVICE_FEATURE).build(), (String[]) null, (String) null, (String[]) null, (String) null);
            if (query != null) {
                if (query.getCount() >= 1) {
                    query.moveToFirst();
                    do {
                        String string = query.getString(0);
                        String string2 = query.getString(1);
                        SdkLog.d(TAG, "feature service:" + string + ",api:" + string2);
                        if (hashMap.containsKey(string)) {
                            ((List) hashMap.get(string)).add(string2);
                        } else {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(string2);
                            hashMap.put(string, arrayList);
                        }
                    } while (query.moveToNext());
                    r7 = query;
                }
            }
            if (query != null) {
                query.close();
            }
            return hashMap;
        } catch (Exception e) {
            SdkLog.s(e);
        } catch (Throwable th) {
            if (r7 != 0) {
                r7.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0084, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0085, code lost:
        if (r6 != null) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x008f, code lost:
        throw r0;
     */
    private static void mergeFeatureListOfSaAgent(Context context, Map<String, List<String>> map) {
        try {
            Cursor query = context.getContentResolver().query(new Uri.Builder().scheme("content").encodedAuthority(SAMSUNG_ACCOUNT_PROVIDER_URI).appendEncodedPath(MOBILE_SERVICE_PROVIDER_PATH_SERVICE_FEATURE).build(), (String[]) null, (String) null, (String[]) null, (String) null);
            if (query != null) {
                if (query.getCount() >= 1) {
                    query.moveToFirst();
                    do {
                        String string = query.getString(0);
                        String string2 = query.getString(1);
                        SdkLog.d(TAG, "feature service:" + string + ",api:" + string2);
                        if (map.containsKey(string)) {
                            map.get(string).add(string2);
                        } else {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(string2);
                            map.put(string, arrayList);
                        }
                    } while (query.moveToNext());
                    if (query != null) {
                        query.close();
                        return;
                    }
                    return;
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e) {
            SdkLog.s(e);
        } catch (Throwable th) {
            r7.addSuppressed(th);
        }
    }

    /* JADX WARNING: type inference failed for: r8v0, types: [java.util.Map<java.lang.String, java.lang.Integer>, android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r8v1, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r8v2, types: [android.database.Cursor] */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0094, code lost:
        if (r8 == 0) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x009d, code lost:
        if (r8 == 0) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x009f, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00a6, code lost:
        if (com.samsung.android.sdk.mobileservice.common.CommonUtils.isStandAloneSamsungAccountSupported(r9) == false) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00a8, code lost:
        mergeApiStatusListOfSaAgent(r9, r0, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ab, code lost:
        r9 = r10.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b3, code lost:
        if (r9.hasNext() == false) goto L_0x00ca;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b5, code lost:
        r10 = (java.lang.String) r9.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00bf, code lost:
        if (r0.containsKey(r10) != false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00c1, code lost:
        r0.put(r10, -1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00ca, code lost:
        return r0;
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    public static Map<String, Integer> getApiStatusList(Context context, String[] strArr) {
        HashMap hashMap = new HashMap();
        String valueOf = String.valueOf(BuildConfig.VERSION_CODE);
        List asList = Arrays.asList(strArr);
        ContentResolver contentResolver = context.getContentResolver();
        ? r8 = 0;
        if (!CommonUtils.isAgentSupportMinVersion(CommonConstants.SupportedApiMinVersion.VERSION_4_1, context)) {
            return r8;
        }
        try {
            r8 = contentResolver.query(new Uri.Builder().scheme("content").encodedAuthority(MOBILE_SERVICE_PROVIDER_URI).appendEncodedPath("status").build(), (String[]) null, valueOf, (String[]) null, (String) null);
            if (r8 != 0 && r8.getCount() > 0) {
                r8.moveToFirst();
                do {
                    String string = r8.getString(0);
                    int intValue = Integer.valueOf(r8.getString(1)).intValue();
                    if (intValue < -4) {
                        intValue = -99;
                    }
                    SdkLog.d(TAG, "api:" + string + ",status:" + intValue);
                    if (asList.contains(string)) {
                        hashMap.put(string, Integer.valueOf(intValue));
                    }
                } while (r8.moveToNext());
            }
        } catch (Exception e) {
            SdkLog.s(e);
        } catch (Throwable th) {
            if (r8 != 0) {
                r8.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0083, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0084, code lost:
        if (r7 != null) goto L_0x0086;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x008e, code lost:
        throw r9;
     */
    private static void mergeApiStatusListOfSaAgent(Context context, Map<String, Integer> map, List<String> list) {
        try {
            Cursor query = context.getContentResolver().query(new Uri.Builder().scheme("content").encodedAuthority(SAMSUNG_ACCOUNT_PROVIDER_URI).appendEncodedPath("status").build(), (String[]) null, String.valueOf(BuildConfig.VERSION_CODE), (String[]) null, (String) null);
            if (query != null) {
                if (query.getCount() > 0) {
                    query.moveToFirst();
                    do {
                        String string = query.getString(0);
                        int intValue = Integer.valueOf(query.getString(1)).intValue();
                        if (intValue < -4) {
                            intValue = -99;
                        }
                        SdkLog.d(TAG, "api:" + string + ",status:" + intValue);
                        if (list.contains(string)) {
                            map.put(string, Integer.valueOf(intValue));
                        }
                    } while (query.moveToNext());
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e) {
            SdkLog.s(e);
        } catch (Throwable th) {
            r8.addSuppressed(th);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x006a, code lost:
        if (r7 == null) goto L_0x0078;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0073, code lost:
        if (r7 == null) goto L_0x0078;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0075, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0078, code lost:
        return r0;
     */
    public static Set<String> getSupportedSocialFeatureList(Context context, String str) {
        int agentVersion = getAgentVersion(context);
        if (agentVersion < 1010000000) {
            return getSupportedSocialFeatureListLegacy(agentVersion, str);
        }
        HashSet hashSet = new HashSet();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(new Uri.Builder().scheme("content").encodedAuthority(MOBILE_SERVICE_PROVIDER_URI).appendEncodedPath(MOBILE_SERVICE_PROVIDER_PATH_SOCIALFEATURE).build(), (String[]) null, str, (String[]) null, (String) null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String string = cursor.getString(0);
                    hashSet.add(string);
                    SdkLog.d(TAG, "feature: " + string);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            SdkLog.s(e);
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private static Set<String> getSupportedSocialFeatureListLegacy(int i, String str) {
        HashSet hashSet = new HashSet();
        if (str.equals(APP_ID_GALLERY)) {
            if (i >= 410000000) {
                if (i < 1000000000) {
                    hashSet.add(SOCIAL_FEATURE_GLOBAL_GROUP_SHARING);
                    hashSet.add(SOCIAL_FEATURE_FAMILY_GROUP_SHARING);
                } else {
                    hashSet.add(SOCIAL_FEATURE_GLOBAL_GROUP_SHARING);
                    hashSet.add(SOCIAL_FEATURE_LOCAL_GROUP_SHARING);
                    hashSet.add(SOCIAL_FEATURE_FAMILY_GROUP_SHARING);
                }
            }
        } else if (str.equals(APP_ID_DAILY_BOARD)) {
            if (i >= 420000000) {
                if (i < 1000000000) {
                    hashSet.add(SOCIAL_FEATURE_GLOBAL_GROUP_SHARING);
                    hashSet.add(SOCIAL_FEATURE_FAMILY_GROUP_SHARING);
                } else {
                    hashSet.add(SOCIAL_FEATURE_GLOBAL_GROUP_SHARING);
                    hashSet.add(SOCIAL_FEATURE_LOCAL_GROUP_SHARING);
                    hashSet.add(SOCIAL_FEATURE_FAMILY_GROUP_SHARING);
                }
            }
        } else if (str.equals(APP_ID_SOCIAL_APP)) {
            if (i >= 1000000000) {
                hashSet.add(SOCIAL_FEATURE_ACTIVITY_FEEDBACK_SERVICE);
                hashSet.add(SOCIAL_FEATURE_SOCIAL_ACTIVITY_SERVICE);
            }
        } else if (!str.equals(APP_ID_SES_CALENDAR)) {
            SdkLog.d(TAG, "appId :" + str + " is not support in getSupportedSocialFeatureListLegacy versionCode : " + i);
        } else if (i >= 1000600000) {
            hashSet.add(SOCIAL_FEATURE_FAMILY_GROUP_SHARING);
        }
        return hashSet;
    }

    public static int getAgentLatestVersionInGalaxyApps(Context context) {
        List<Object> commonListFromAgentProvider = getCommonListFromAgentProvider(context);
        if (!CommonUtils.isAgentSupportMinVersion(CommonConstants.SupportedApiMinVersion.VERSION_4_1, context) || commonListFromAgentProvider == null) {
            return -1;
        }
        try {
            if (commonListFromAgentProvider.size() > 0) {
                return Integer.valueOf((String) commonListFromAgentProvider.get(0)).intValue();
            }
            return -1;
        } catch (Exception unused) {
            SdkLog.d(TAG, "Exception during get agent latest version");
            return -1;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x005f, code lost:
        if (r7 == null) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0061, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0064, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0056, code lost:
        if (r7 != null) goto L_0x0061;
     */
    private static List<Object> getCommonListFromAgentProvider(Context context) {
        ArrayList arrayList = new ArrayList();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(new Uri.Builder().scheme("content").encodedAuthority(MOBILE_SERVICE_PROVIDER_URI).appendEncodedPath(MOBILE_SERVICE_PROVIDER_PATH_COMMON).build(), (String[]) null, (String) null, (String[]) null, (String) null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                String string = cursor.getString(0);
                SdkLog.d(TAG, "latestVersionInGalaxyApps:" + string);
                arrayList.add(string);
            }
        } catch (Exception e) {
            SdkLog.s(e);
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static int getSaAgentLatestVersionInGalaxyApps(Context context) {
        List<Object> commonListFromSaAgentProvider = getCommonListFromSaAgentProvider(context);
        if (!CommonUtils.isSaAgentSupportMinVersion(CommonConstants.SupportedApiMinVersion.VERSION_11_0, context) || commonListFromSaAgentProvider == null) {
            return -1;
        }
        try {
            if (commonListFromSaAgentProvider.size() > 0) {
                return Integer.valueOf((String) commonListFromSaAgentProvider.get(0)).intValue();
            }
            return -1;
        } catch (Exception unused) {
            SdkLog.d(TAG, "Exception during get samsung account agent latest version");
            return -1;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0058, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0059, code lost:
        if (r7 != null) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0063, code lost:
        throw r2;
     */
    private static List<Object> getCommonListFromSaAgentProvider(Context context) {
        ArrayList arrayList = new ArrayList();
        try {
            Cursor query = context.getContentResolver().query(new Uri.Builder().scheme("content").encodedAuthority(SAMSUNG_ACCOUNT_PROVIDER_URI).appendEncodedPath(MOBILE_SERVICE_PROVIDER_PATH_COMMON).build(), (String[]) null, (String) null, (String[]) null, (String) null);
            if (query != null) {
                if (query.getCount() > 0) {
                    query.moveToFirst();
                    String string = query.getString(0);
                    SdkLog.d(TAG, "latestVersionInGalaxyApps:" + string);
                    arrayList.add(string);
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e) {
            SdkLog.s(e);
        } catch (Throwable th) {
            r1.addSuppressed(th);
        }
        return arrayList;
    }
}
