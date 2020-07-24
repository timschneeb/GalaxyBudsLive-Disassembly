package com.samsung.android.fotaprovider.util.galaxywearable;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import com.samsung.android.fotaprovider.FotaProviderInitializer;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.FotaProviderUtil;

public class Settings {
    /* access modifiers changed from: private */
    public static Uri contentUri = generateContentUri();

    public interface GearPluginEventQuery {
        public static final String[] COLUMNS = {"keyField", "keyValue"};
        public static final int KEY_FIELD = 0;
        public static final int KEY_VALUE = 1;
    }

    private static Uri generateContentUri() {
        String packageName = FotaProviderInitializer.getContext().getPackageName();
        if (FotaProviderUtil.getSingleFotaProviderPackageName().equals(packageName)) {
            packageName = "com.samsung.android.gear2plugin";
        } else if ("com.samsung.android.gearfit2plugin".equals(packageName)) {
            packageName = "com.samsung.android.gearfit2plugin.gearfit2FT";
        }
        return Uri.parse("content://" + packageName + "/settings");
    }

    public static class System {
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0068, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0069, code lost:
            if (r7 != null) goto L_0x006b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            r7.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0073, code lost:
            throw r1;
         */
        public static int getInt(ContentResolver contentResolver, String str, int i) {
            String[] strArr = {str};
            String str2 = null;
            try {
                Uri access$000 = Settings.contentUri;
                Cursor query = contentResolver.query(access$000, (String[]) null, GearPluginEventQuery.COLUMNS[0] + "=?", strArr, (String) null);
                Log.D("succeeded to find content uri : " + Settings.contentUri.toString());
                if (query == null || query.getCount() <= 0) {
                    Log.W("Field Entry not present in the DB !!");
                } else {
                    query.moveToFirst();
                    str2 = query.getString(query.getColumnIndex(GearPluginEventQuery.COLUMNS[1]));
                }
                if (query != null) {
                    query.close();
                }
            } catch (Exception unused) {
                Log.D("failed to find content uri : " + Settings.contentUri.toString());
            } catch (Throwable th) {
                r0.addSuppressed(th);
            }
            if (str2 == null) {
                return i;
            }
            try {
                return Integer.parseInt(str2);
            } catch (NumberFormatException unused2) {
                return i;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0069, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x006a, code lost:
            if (r8 != null) goto L_0x006c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            r8.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0074, code lost:
            throw r1;
         */
        public static int getInt(ContentResolver contentResolver, String str) throws Settings.SettingNotFoundException {
            String[] strArr = {str};
            String str2 = null;
            try {
                Uri access$000 = Settings.contentUri;
                Cursor query = contentResolver.query(access$000, (String[]) null, GearPluginEventQuery.COLUMNS[0] + "=?", strArr, (String) null);
                Log.D("succeeded to find content uri : " + Settings.contentUri.toString());
                if (query == null || query.getCount() <= 0) {
                    Log.W("Field Entry not present in the DB !!");
                } else {
                    query.moveToFirst();
                    str2 = query.getString(query.getColumnIndex(GearPluginEventQuery.COLUMNS[1]));
                }
                if (query != null) {
                    query.close();
                }
            } catch (Exception unused) {
                Log.D("failed to find content uri : " + Settings.contentUri.toString());
            } catch (Throwable th) {
                r0.addSuppressed(th);
            }
            if (str2 != null) {
                try {
                    return Integer.parseInt(str2);
                } catch (NumberFormatException unused2) {
                    throw new Settings.SettingNotFoundException("No Setting with " + str);
                }
            } else {
                throw new Settings.SettingNotFoundException("No Setting with " + str);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:17:0x009f, code lost:
            r11 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x00a0, code lost:
            if (r2 != null) goto L_0x00a2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r2.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x00aa, code lost:
            throw r11;
         */
        public static void putInt(ContentResolver contentResolver, String str, int i) {
            String[] strArr = {str};
            try {
                Uri access$000 = Settings.contentUri;
                Cursor query = contentResolver.query(access$000, (String[]) null, GearPluginEventQuery.COLUMNS[0] + "=?", strArr, (String) null);
                Log.D("succeeded to find content uri : " + Settings.contentUri.toString());
                ContentValues contentValues = new ContentValues();
                if (query == null || query.getCount() <= 0) {
                    contentValues.put(GearPluginEventQuery.COLUMNS[0], str);
                    contentValues.put(GearPluginEventQuery.COLUMNS[1], Integer.toString(i));
                    contentResolver.insert(Settings.contentUri, contentValues);
                } else {
                    contentValues.put(GearPluginEventQuery.COLUMNS[1], Integer.toString(i));
                    contentResolver.update(Settings.contentUri, contentValues, GearPluginEventQuery.COLUMNS[0] + "=?", strArr);
                }
                contentResolver.notifyChange(getUriFor(str), (ContentObserver) null);
                if (query != null) {
                    query.close();
                }
            } catch (Exception unused) {
                Log.D("failed to find content uri : " + Settings.contentUri.toString());
            } catch (Throwable th) {
                r10.addSuppressed(th);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0068, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0069, code lost:
            if (r7 != null) goto L_0x006b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            r7.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0073, code lost:
            throw r1;
         */
        public static String getString(ContentResolver contentResolver, String str, String str2) {
            String[] strArr = {str};
            String str3 = null;
            try {
                Uri access$000 = Settings.contentUri;
                Cursor query = contentResolver.query(access$000, (String[]) null, GearPluginEventQuery.COLUMNS[0] + "=?", strArr, (String) null);
                Log.D("succeeded to find content uri : " + Settings.contentUri.toString());
                if (query == null || query.getCount() <= 0) {
                    Log.W("Field Entry not present in the DB !!");
                } else {
                    query.moveToFirst();
                    str3 = query.getString(query.getColumnIndex(GearPluginEventQuery.COLUMNS[1]));
                }
                if (query != null) {
                    query.close();
                }
            } catch (Exception unused) {
                Log.D("failed to find content uri : " + Settings.contentUri.toString());
            } catch (Throwable th) {
                r0.addSuppressed(th);
            }
            return str3 != null ? str3 : str2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0068, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0069, code lost:
            if (r7 != null) goto L_0x006b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            r7.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0073, code lost:
            throw r1;
         */
        public static String getString(ContentResolver contentResolver, String str) {
            String[] strArr = {str};
            String str2 = null;
            try {
                Uri access$000 = Settings.contentUri;
                Cursor query = contentResolver.query(access$000, (String[]) null, GearPluginEventQuery.COLUMNS[0] + "=?", strArr, (String) null);
                Log.D("succeeded to find content uri : " + Settings.contentUri.toString());
                if (query == null || query.getCount() <= 0) {
                    Log.W("Field Entry not present in the DB !!");
                } else {
                    query.moveToFirst();
                    str2 = query.getString(query.getColumnIndex(GearPluginEventQuery.COLUMNS[1]));
                }
                if (query != null) {
                    query.close();
                }
            } catch (Exception unused) {
                Log.D("failed to find content uri : " + Settings.contentUri.toString());
            } catch (Throwable th) {
                r0.addSuppressed(th);
            }
            return str2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0097, code lost:
            r11 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0098, code lost:
            if (r2 != null) goto L_0x009a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r2.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x00a2, code lost:
            throw r11;
         */
        public static void putString(ContentResolver contentResolver, String str, String str2) {
            String[] strArr = {str};
            try {
                Uri access$000 = Settings.contentUri;
                Cursor query = contentResolver.query(access$000, (String[]) null, GearPluginEventQuery.COLUMNS[0] + "=?", strArr, (String) null);
                Log.D("succeeded to find content uri : " + Settings.contentUri.toString());
                ContentValues contentValues = new ContentValues();
                if (query == null || query.getCount() <= 0) {
                    contentValues.put(GearPluginEventQuery.COLUMNS[0], str);
                    contentValues.put(GearPluginEventQuery.COLUMNS[1], str2);
                    contentResolver.insert(Settings.contentUri, contentValues);
                } else {
                    contentValues.put(GearPluginEventQuery.COLUMNS[1], str2);
                    contentResolver.update(Settings.contentUri, contentValues, GearPluginEventQuery.COLUMNS[0] + "=?", strArr);
                }
                contentResolver.notifyChange(getUriFor(str), (ContentObserver) null);
                if (query != null) {
                    query.close();
                }
            } catch (Exception unused) {
                Log.D("failed to find content uri : " + Settings.contentUri.toString());
            } catch (Throwable th) {
                r10.addSuppressed(th);
            }
        }

        private static Uri getUriFor(String str) {
            return Uri.withAppendedPath(Settings.contentUri, str);
        }
    }
}
