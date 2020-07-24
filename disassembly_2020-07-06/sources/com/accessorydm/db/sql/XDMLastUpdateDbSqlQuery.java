package com.accessorydm.db.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.accessorydm.db.XDMDbManager;
import com.accessorydm.db.file.XDBLastUpdateInfo;
import com.accessorydm.db.file.XDBUserDBException;
import com.accessorydm.interfaces.XDBInterface;
import com.samsung.android.fotaprovider.log.Log;

public class XDMLastUpdateDbSqlQuery implements XDBInterface {
    public static void createDBLastUpdate(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_LAST_UPDATE_INFO_CREATE);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    public static void deleteDBLastUpdate(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS LastUpdate");
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    public static long insertLastUpdateInfo(XDBLastUpdateInfo xDBLastUpdateInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_DATE, Long.valueOf(xDBLastUpdateInfo.getLastUpdateDate()));
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_VERSION, xDBLastUpdateInfo.getLastUpdateVersion());
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_DESCRIPTION, xDBLastUpdateInfo.getLastUpdateDescription());
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_RESULTCODE, xDBLastUpdateInfo.getLastUpdateResultCode());
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_DELTASIZE, Long.valueOf(xDBLastUpdateInfo.getLastUpdateDeltaSize()));
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_LAST_UPDATE_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    public static void updateLastUpdateInfo(long j, XDBLastUpdateInfo xDBLastUpdateInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_DATE, Long.valueOf(xDBLastUpdateInfo.getLastUpdateDate()));
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_VERSION, xDBLastUpdateInfo.getLastUpdateVersion());
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_DESCRIPTION, xDBLastUpdateInfo.getLastUpdateDescription());
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_RESULTCODE, xDBLastUpdateInfo.getLastUpdateResultCode());
            contentValues.put(XDBInterface.XDM_SQL_LAST_UPDATE_DELTASIZE, Long.valueOf(xDBLastUpdateInfo.getLastUpdateDeltaSize()));
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_LAST_UPDATE_TABLE, contentValues, "rowId=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0098, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0099, code lost:
        if (r2 != null) goto L_0x009b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00a3, code lost:
        throw r3;
     */
    public static XDBLastUpdateInfo getQueryLastUpdateInfo() throws XDBUserDBException {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_LAST_UPDATE_DATE, XDBInterface.XDM_SQL_LAST_UPDATE_VERSION, XDBInterface.XDM_SQL_LAST_UPDATE_DESCRIPTION, XDBInterface.XDM_SQL_LAST_UPDATE_RESULTCODE, XDBInterface.XDM_SQL_LAST_UPDATE_DELTASIZE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBLastUpdateInfo xDBLastUpdateInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_LAST_UPDATE_TABLE, strArr, (String) null, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return null;
            }
            if (query.getCount() > 0 && query.moveToFirst()) {
                xDBLastUpdateInfo = new XDBLastUpdateInfo();
                xDBLastUpdateInfo.setLastUpdateDate(query.getLong(query.getColumnIndex(XDBInterface.XDM_SQL_LAST_UPDATE_DATE)));
                xDBLastUpdateInfo.setLastUpdateVersion(query.getString(query.getColumnIndex(XDBInterface.XDM_SQL_LAST_UPDATE_VERSION)));
                xDBLastUpdateInfo.setLastUpdateDescription(query.getString(query.getColumnIndex(XDBInterface.XDM_SQL_LAST_UPDATE_DESCRIPTION)));
                xDBLastUpdateInfo.setLastUpdateResultCode(query.getString(query.getColumnIndex(XDBInterface.XDM_SQL_LAST_UPDATE_RESULTCODE)));
                xDBLastUpdateInfo.setLastUpdateDeltaSize(query.getLong(query.getColumnIndex(XDBInterface.XDM_SQL_LAST_UPDATE_DELTASIZE)));
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return xDBLastUpdateInfo;
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
                throw new XDBUserDBException(1);
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r1.addSuppressed(th2);
        }
    }

    /* JADX INFO: finally extract failed */
    public static boolean existLastUpdateInfo(long j) {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_LAST_UPDATE_DATE, XDBInterface.XDM_SQL_LAST_UPDATE_VERSION, XDBInterface.XDM_SQL_LAST_UPDATE_DESCRIPTION, XDBInterface.XDM_SQL_LAST_UPDATE_RESULTCODE, XDBInterface.XDM_SQL_LAST_UPDATE_DELTASIZE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_LAST_UPDATE_TABLE, strArr, "rowId=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                try {
                    Log.E("cursor is null");
                    if (query != null) {
                        query.close();
                    }
                    XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                    return false;
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th3;
                }
            } else {
                boolean z = query.getCount() > 0;
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return z;
            }
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return false;
            } catch (Throwable th4) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th4;
            }
        } catch (Throwable th5) {
            th.addSuppressed(th5);
        }
    }
}
