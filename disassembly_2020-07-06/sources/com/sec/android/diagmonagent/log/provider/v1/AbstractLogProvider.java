package com.sec.android.diagmonagent.log.provider.v1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import com.accessorydm.interfaces.XDMInterface;
import com.samsung.context.sdk.samsunganalytics.BuildConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractLogProvider extends ContentProvider {
    public static final String AUTHORITY_LIST = "authorityList";
    public static final String DIAGMON_PREFERENCES = "diagmon_preferences";
    public static final String DIAGMON_SUPPORT_V1_VERSION_CODE = "diagmonSupportV1VersionCode";
    public static final String DIAGMON_SUPPORT_V1_VERSION_NAME = "diagmonSupportV1VersionName";
    public static final String LOG_LIST = "logList";
    public static final String OPERATION_NOT_SUPPORTED = "Operation not supported";
    public static final String PERMISSION_DENIAL = "Permission Denial";
    public static final String PLAIN_LOG_LIST = "plainLogList";
    protected Bundle data;

    /* access modifiers changed from: protected */
    public abstract String getAuthority();

    /* access modifiers changed from: protected */
    public abstract List<String> setLogList();

    /* access modifiers changed from: protected */
    public List<String> setPlainLogList() {
        return Arrays.asList(new String[0]);
    }

    public boolean onCreate() {
        this.data = new Bundle();
        this.data.putBundle("logList", makeLogListBundle(setLogList()));
        this.data.putBundle("plainLogList", makeLogListBundle(setPlainLogList()));
        this.data.putBundle("diagmonSupportV1VersionName", getDiagmonSupportV1VersionNameBundle());
        this.data.putBundle("diagmonSupportV1VersionCode", getDiagmonSupportV1VersionCodeBundle());
        return true;
    }

    private Bundle getDiagmonSupportV1VersionNameBundle() {
        Bundle bundle = new Bundle();
        try {
            Object obj = BuildConfig.class.getDeclaredField("VERSION_NAME").get((Object) null);
            if (obj instanceof String) {
                bundle.putString("diagmonSupportV1VersionName", String.class.cast(obj));
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
        }
        return bundle;
    }

    private Bundle getDiagmonSupportV1VersionCodeBundle() {
        Bundle bundle = new Bundle();
        try {
            bundle.putInt("diagmonSupportV1VersionCode", BuildConfig.class.getDeclaredField("VERSION_CODE").getInt((Object) null));
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
        }
        return bundle;
    }

    private Bundle makeLogListBundle(List<String> list) {
        Bundle bundle = new Bundle();
        for (String next : list) {
            try {
                next = new File(next).getCanonicalPath();
            } catch (IOException unused) {
            }
            bundle.putParcelable(next, new Uri.Builder().scheme("content").authority(getAuthority()).path(next).build());
        }
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void enforceSelfOrSystem() {
        if (Binder.getCallingUid() != 1000 && Process.myPid() != Binder.getCallingPid()) {
            throw new SecurityException("Permission Denial");
        }
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        enforceSelfOrSystem();
        if ("clear".equals(str)) {
            return clear();
        }
        if ("set".equals(str)) {
            return set(str2, bundle);
        }
        if ("get".equals(str) && !contains(str2) && this.data.getBundle(str2) != null) {
            return this.data.getBundle(str2);
        }
        if ("get".equals(str)) {
            return get(str2);
        }
        return super.call(str, str2, bundle);
    }

    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        enforceSelfOrSystem();
        String path = uri.getPath();
        if (this.data.getBundle("logList") == null || this.data.getBundle("plainLogList") == null) {
            throw new RuntimeException("Data is corrupted");
        } else if (this.data.getBundle("logList").containsKey(path) || this.data.getBundle("plainLogList").containsKey(path)) {
            return openParcelFileDescriptor(path);
        } else {
            throw new FileNotFoundException();
        }
    }

    /* access modifiers changed from: protected */
    public ParcelFileDescriptor openParcelFileDescriptor(String str) throws FileNotFoundException {
        return ParcelFileDescriptor.open(new File(str), 268435456);
    }

    public int delete(Uri uri, String str, String[] strArr) {
        throw new RuntimeException("Operation not supported");
    }

    public String getType(Uri uri) {
        enforceSelfOrSystem();
        return XDMInterface.MIMETYPE_TEXT_PLAIN;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new RuntimeException("Operation not supported");
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        throw new RuntimeException("Operation not supported");
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new RuntimeException("Operation not supported");
    }

    /* access modifiers changed from: protected */
    public SharedPreferences getDiagMonSharedPreferences() {
        return getContext().getSharedPreferences("diagmon_preferences", 0);
    }

    /* access modifiers changed from: protected */
    public Bundle clear() {
        SharedPreferences.Editor edit = getDiagMonSharedPreferences().edit();
        edit.clear();
        edit.apply();
        return Bundle.EMPTY;
    }

    /* access modifiers changed from: protected */
    public Bundle set(String str, Bundle bundle) {
        SharedPreferences.Editor edit = getDiagMonSharedPreferences().edit();
        Object obj = bundle.get(str);
        if (obj instanceof Boolean) {
            edit.putBoolean(str, ((Boolean) obj).booleanValue());
        }
        if (obj instanceof Float) {
            edit.putFloat(str, ((Float) obj).floatValue());
        }
        if (obj instanceof Integer) {
            edit.putInt(str, ((Integer) obj).intValue());
        }
        if (obj instanceof Long) {
            edit.putLong(str, ((Long) obj).longValue());
        }
        if (obj instanceof String) {
            edit.putString(str, (String) obj);
        }
        edit.apply();
        return Bundle.EMPTY;
    }

    /* access modifiers changed from: protected */
    public boolean contains(String str) {
        return getDiagMonSharedPreferences().contains(str);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0019 */
    public Bundle get(String str) {
        SharedPreferences diagMonSharedPreferences = getDiagMonSharedPreferences();
        Bundle bundle = new Bundle();
        try {
            bundle.putBoolean(str, diagMonSharedPreferences.getBoolean(str, false));
        } catch (ClassCastException unused) {
        }
        bundle.putFloat(str, diagMonSharedPreferences.getFloat(str, 0.0f));
        try {
            bundle.putInt(str, diagMonSharedPreferences.getInt(str, 0));
        } catch (ClassCastException unused2) {
        }
        try {
            bundle.putLong(str, diagMonSharedPreferences.getLong(str, 0));
        } catch (ClassCastException unused3) {
        }
        try {
            bundle.putString(str, diagMonSharedPreferences.getString(str, (String) null));
        } catch (ClassCastException unused4) {
        }
        return bundle;
    }
}
