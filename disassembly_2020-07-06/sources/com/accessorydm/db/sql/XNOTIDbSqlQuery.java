package com.accessorydm.db.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.accessorydm.db.XDMDbManager;
import com.accessorydm.db.file.XDBNotiInfo;
import com.accessorydm.db.file.XDBUserDBException;
import com.accessorydm.interfaces.XDBInterface;
import com.samsung.android.fotaprovider.log.Log;

public class XNOTIDbSqlQuery implements XDBInterface, XNOTIDbSql {
    public static void xnotiDbSqlCreate(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            sQLiteDatabase.execSQL(XNOTIDbSql.XNOTI_DB_SQL_INFO_TABLE_CREATE);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    public static void xnotiDbSqlDrop(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS NOTIFICATION");
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    public static void xnotiDbSqlInfoInsertRow(XDBNotiInfo xDBNotiInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put("appId", Integer.valueOf(xDBNotiInfo.appId));
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_UIMODE, Integer.valueOf(xDBNotiInfo.uiMode));
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_SESSIONID, xDBNotiInfo.m_szSessionId);
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_SERVERID, xDBNotiInfo.m_szServerId);
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_OPMODE, Integer.valueOf(xDBNotiInfo.opMode));
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_JOBID, Integer.valueOf(xDBNotiInfo.jobId));
            xdmDbGetWritableDatabase.insert(XNOTIDbSql.XNOTI_DB_SQL_INFO_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
    }

    public static void xnotiDbSqlInfoDeleteRow(String str, String str2) {
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            xdmDbGetWritableDatabase.delete(XNOTIDbSql.XNOTI_DB_SQL_INFO_TABLE, str + "='" + str2 + "'", (String[]) null);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
    }

    public static void xnotiDbSqlInfoUpdateRow(long j, XDBNotiInfo xDBNotiInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put("appId", Integer.valueOf(xDBNotiInfo.appId));
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_UIMODE, Integer.valueOf(xDBNotiInfo.uiMode));
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_SESSIONID, xDBNotiInfo.m_szSessionId);
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_SERVERID, xDBNotiInfo.m_szServerId);
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_OPMODE, Integer.valueOf(xDBNotiInfo.opMode));
            contentValues.put(XNOTIDbSql.XNOTI_DB_SQL_JOBID, Integer.valueOf(xDBNotiInfo.jobId));
            xdmDbGetWritableDatabase.update(XNOTIDbSql.XNOTI_DB_SQL_INFO_TABLE, contentValues, "rowId=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0078, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0079, code lost:
        if (r3 != null) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0083, code lost:
        throw r4;
     */
    public static XDBNotiInfo xnotiDbSqlInfoFetchRow() throws XDBUserDBException {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "appId", XNOTIDbSql.XNOTI_DB_SQL_UIMODE, XNOTIDbSql.XNOTI_DB_SQL_SESSIONID, XNOTIDbSql.XNOTI_DB_SQL_SERVERID};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBNotiInfo xDBNotiInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XNOTIDbSql.XNOTI_DB_SQL_INFO_TABLE, strArr, (String) null, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return null;
            }
            if (query.getCount() > 0 && query.moveToFirst()) {
                xDBNotiInfo = new XDBNotiInfo();
                xDBNotiInfo.rowId = query.getInt(0);
                xDBNotiInfo.appId = query.getInt(1);
                xDBNotiInfo.uiMode = query.getInt(2);
                xDBNotiInfo.m_szSessionId = query.getString(3);
                xDBNotiInfo.m_szServerId = query.getString(4);
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return xDBNotiInfo;
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

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004e, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004f, code lost:
        if (r2 != null) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0059, code lost:
        throw r4;
     */
    public static boolean xnotiDbSqlInfoExistsRow() {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "appId", XNOTIDbSql.XNOTI_DB_SQL_UIMODE, XNOTIDbSql.XNOTI_DB_SQL_SESSIONID, XNOTIDbSql.XNOTI_DB_SQL_SERVERID};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XNOTIDbSql.XNOTI_DB_SQL_INFO_TABLE, strArr, (String) null, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return false;
            }
            boolean z = query.getCount() > 0;
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return z;
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
                return false;
            } finally {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            }
        } catch (Throwable th) {
            r3.addSuppressed(th);
        }
    }
}
