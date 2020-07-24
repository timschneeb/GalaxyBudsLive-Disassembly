package com.accessorydm.db.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.accessorydm.db.XDMDbManager;
import com.accessorydm.db.file.XDBAccessoryInfo;
import com.accessorydm.db.file.XDBUserDBException;
import com.accessorydm.interfaces.XDBInterface;
import com.samsung.android.fotaprovider.log.Log;

public class XDMAccessoryDbSqlQuery implements XDBInterface {
    static void xdmAccessoryDbSqlCreate(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_ACCESSORY_INFO_CREATE);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    static void xdmAccessoryDbSqlDrop(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS accessory");
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    public static long xdmAccessoryDbSqlInsertRow(XDBAccessoryInfo xDBAccessoryInfo) {
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("deviceid", xDBAccessoryInfo.getDeviceId());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_MODEL, xDBAccessoryInfo.getModelNumber());
            contentValues.put("cc", xDBAccessoryInfo.getSalesCode());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_FWVERSION, xDBAccessoryInfo.getFirmwareVersion());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_HWVERSION, xDBAccessoryInfo.getHardwareVersion());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_UNIQUE, xDBAccessoryInfo.getUniqueNumber());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_SERIAL, xDBAccessoryInfo.getSerialNumber());
            contentValues.put("status", Integer.valueOf(xDBAccessoryInfo.getStatus()));
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_MCC, xDBAccessoryInfo.getMCC());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_OPERATOR, xDBAccessoryInfo.getCountry());
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_ACCESSORY_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    public static void xdmAccessoryDbSqlUpdateRow(long j, XDBAccessoryInfo xDBAccessoryInfo) throws XDBUserDBException {
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("deviceid", xDBAccessoryInfo.getDeviceId());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_MODEL, xDBAccessoryInfo.getModelNumber());
            contentValues.put("cc", xDBAccessoryInfo.getSalesCode());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_FWVERSION, xDBAccessoryInfo.getFirmwareVersion());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_HWVERSION, xDBAccessoryInfo.getHardwareVersion());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_UNIQUE, xDBAccessoryInfo.getUniqueNumber());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_SERIAL, xDBAccessoryInfo.getSerialNumber());
            contentValues.put("status", Integer.valueOf(xDBAccessoryInfo.getStatus()));
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_MCC, xDBAccessoryInfo.getMCC());
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_PUSHTYPE, "");
            contentValues.put(XDBInterface.XDM_SQL_ACCESSORY_OPERATOR, xDBAccessoryInfo.getCountry());
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_ACCESSORY_TABLE, contentValues, "rowId=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    public static XDBAccessoryInfo xdmAccessoryDbSqlFetchRow() throws XDBUserDBException {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "deviceid", XDBInterface.XDM_SQL_ACCESSORY_MODEL, "cc", XDBInterface.XDM_SQL_ACCESSORY_FWVERSION, XDBInterface.XDM_SQL_ACCESSORY_HWVERSION, XDBInterface.XDM_SQL_ACCESSORY_UNIQUE, XDBInterface.XDM_SQL_ACCESSORY_SERIAL, "status", XDBInterface.XDM_SQL_ACCESSORY_MCC, XDBInterface.XDM_SQL_ACCESSORY_PUSHTYPE, XDBInterface.XDM_SQL_ACCESSORY_OPERATOR, "name"};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBAccessoryInfo xDBAccessoryInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_ACCESSORY_TABLE, strArr, (String) null, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                try {
                    Log.E("cursor is null");
                    if (query != null) {
                        query.close();
                    }
                    XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                    return null;
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th3;
                }
            } else {
                if (query.getCount() > 0 && query.moveToFirst()) {
                    xDBAccessoryInfo = new XDBAccessoryInfo();
                    xDBAccessoryInfo.setDeviceId(query.getString(1));
                    xDBAccessoryInfo.setModelNumber(query.getString(2));
                    xDBAccessoryInfo.setSalesCode(query.getString(3));
                    xDBAccessoryInfo.setFirmwareVersion(query.getString(4));
                    xDBAccessoryInfo.setHardwareVersion(query.getString(5));
                    xDBAccessoryInfo.setUniqueNumber(query.getString(6));
                    xDBAccessoryInfo.setSerialNumber(query.getString(7));
                    xDBAccessoryInfo.setStatus(query.getInt(8));
                    xDBAccessoryInfo.setMCC(query.getString(9));
                    xDBAccessoryInfo.setCountry(query.getString(11));
                }
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return xDBAccessoryInfo;
            }
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
                throw new XDBUserDBException(1);
            } catch (Throwable th4) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th4;
            }
        } catch (Throwable th5) {
            th.addSuppressed(th5);
        }
    }

    /* JADX INFO: finally extract failed */
    public static boolean xdmAccessoryDbSqlExistsRow(long j) {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "deviceid", XDBInterface.XDM_SQL_ACCESSORY_MODEL, "cc", XDBInterface.XDM_SQL_ACCESSORY_FWVERSION, XDBInterface.XDM_SQL_ACCESSORY_HWVERSION, XDBInterface.XDM_SQL_ACCESSORY_UNIQUE, XDBInterface.XDM_SQL_ACCESSORY_SERIAL, "status", XDBInterface.XDM_SQL_ACCESSORY_MCC, XDBInterface.XDM_SQL_ACCESSORY_PUSHTYPE, XDBInterface.XDM_SQL_ACCESSORY_OPERATOR, "name"};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_ACCESSORY_TABLE, strArr, "rowId=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
