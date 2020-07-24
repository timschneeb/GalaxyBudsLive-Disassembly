package com.accessorydm.db.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.accessorydm.db.file.XDBProfileListInfo;
import com.accessorydm.interfaces.XDBInterface;
import com.accessorydm.interfaces.XDMInterface;
import com.samsung.android.fotaprovider.log.Log;

public class XDMDbHelper extends SQLiteOpenHelper implements XDBInterface, XDMInterface, XNOTIDbSql {
    public XDMDbHelper(Context context) {
        super(context, XDBInterface.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 11);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.I("");
        xdmDbCreateAllTable(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.I("oldVersion : " + i + ", newVersion : " + i2);
        xdmDbOperateFirstUpdate(sQLiteDatabase);
        xdmDbOperateSecondUpdate(sQLiteDatabase);
        xdmDbOperateThirdUpdate(sQLiteDatabase);
        xdmDbOperateFourthUpdate(sQLiteDatabase);
        xdmDbOperateFifthUpdate(sQLiteDatabase);
        xdmDbOperateSixthUpdate(sQLiteDatabase);
        xdmDbOperateSeventhUpdate(sQLiteDatabase);
        if (i < 9) {
            xdmDbOperateEighthUpdate(sQLiteDatabase);
        }
        if (i < 10) {
            xdmDbOperateNinthUpdate(sQLiteDatabase);
        }
        if (i < 11) {
            xdmDbOperateTenUpdate(sQLiteDatabase);
        }
    }

    private void xdmDbCreateAllTable(SQLiteDatabase sQLiteDatabase) {
        Log.I("");
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_PROFILE_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_NETWORK_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_PROFILELIST_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_FUMO_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_POSTPONE_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_SIMINFO_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_POLLING_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_ACCXLISTNODE_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_RESYNCMODE_CREATE);
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_DM_AGENT_INFO_CREATE);
            XNOTIDbSqlQuery.xnotiDbSqlCreate(sQLiteDatabase);
            XDMAccessoryDbSqlQuery.xdmAccessoryDbSqlCreate(sQLiteDatabase);
            XDMRegistrationDbSqlQuery.createDBRegistration(sQLiteDatabase);
            XDMLastUpdateDbSqlQuery.createDBLastUpdate(sQLiteDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0036, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0037, code lost:
        if (r4 != null) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0041, code lost:
        throw r0;
     */
    private boolean xdmDbExistCheckColumn(SQLiteDatabase sQLiteDatabase, String str) {
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return false;
        }
        boolean z = true;
        try {
            Cursor rawQuery = sQLiteDatabase.rawQuery(str, (String[]) null);
            if (rawQuery == null) {
                Log.E("cursor is null");
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return false;
            }
            Log.I(String.valueOf(rawQuery.getColumnCount()));
            if (rawQuery.getColumnCount() > 0) {
                z = false;
            }
            if (rawQuery != null) {
                rawQuery.close();
            }
            return z;
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            r5.addSuppressed(th);
        }
    }

    private void xdmDbAddColumn(SQLiteDatabase sQLiteDatabase, String str) {
        if (sQLiteDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            sQLiteDatabase.execSQL(str);
            Log.I("Database Add Column : " + str);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    private void xdmDbOperateFirstUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbExistCheckColumn(sQLiteDatabase, "select networkconntype from fumo")) {
            Log.I("FirstUpdate exec(For NetworkConnType)");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE fumo ADD networkconntype text");
            return;
        }
        Log.I("FirstUpdate nothing(For NetworkConnType)");
    }

    private void xdmDbOperateSecondUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbExistCheckColumn(sQLiteDatabase, "select lowbatteryretrycount from fumo")) {
            Log.I("SecondUpdate exec(For LowBatteryRetryCount)");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE fumo ADD lowbatteryretrycount integer");
            return;
        }
        Log.I("SecondUpdate nothing(For LowBatteryRetryCounts)");
    }

    private void xdmDbOperateThirdUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbExistCheckColumn(sQLiteDatabase, "select objecthash from fumo")) {
            Log.I("ThirdUpdate exec(For ObjectHash)");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE fumo ADD objecthash text");
            return;
        }
        Log.I("ThirdUpdate nothing(For ObjectHash)");
    }

    private void xdmDbOperateFourthUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbExistCheckColumn(sQLiteDatabase, "select name from accessory")) {
            Log.I("FourthUpdate exec(For GearName)");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE accessory ADD name text");
            return;
        }
        Log.I("FourthUpdate nothing(For GearName)");
    }

    private void xdmDbOperateFifthUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbExistCheckColumn(sQLiteDatabase, "select postponemaxcount from postpone")) {
            Log.I("FifthUpdate exec(For PostponeMaxCnt, Force)");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE postpone ADD force integer default 0");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE postpone ADD postponemaxcount integer default " + Integer.toString(3));
            return;
        }
        Log.I("FifthUpdate nothing(For PostponeMaxCnt, Force)");
    }

    private void xdmDbOperateSixthUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbExistCheckColumn(sQLiteDatabase, "select updatefwver from fumo")) {
            Log.I("SixthUpdate exec(For Update FW)");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE fumo ADD updatefwver text");
            return;
        }
        Log.I("SixthUpdate nothing(For Update FW)");
    }

    private void xdmDbOperateSeventhUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbExistCheckColumn(sQLiteDatabase, "select wifipostponecount from postpone")) {
            Log.I("SeventhUpdatae exec(For Update postponecount)");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE postpone ADD wifipostponecount text");
            xdmDbUpdateDefaultValue(sQLiteDatabase, XDBInterface.XDB_POSTPONE_TABLE, XDBInterface.XDM_SQL_DB_WIFI_POSTPONE_COUNT, "0");
            return;
        }
        Log.I("SeventhUpdatae nothing(For Update postponecount)");
    }

    private void xdmDbOperateEighthUpdate(SQLiteDatabase sQLiteDatabase) {
        Log.I("EighthUpdate exec(Profile & Profile List Refresh)");
        xdmDbProfileUpdate(sQLiteDatabase);
        xdmDbProfileListUpdate(sQLiteDatabase);
    }

    private void xdmDbOperateNinthUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbExistCheckColumn(sQLiteDatabase, "select uimode from fumo")) {
            Log.I("NinthUpdate exec(For uimode)");
            xdmDbAddColumn(sQLiteDatabase, "ALTER TABLE fumo ADD uimode integer default 0");
            return;
        }
        Log.I("NinthUpdate nothing(For uimode)");
    }

    private void xdmDbOperateTenUpdate(SQLiteDatabase sQLiteDatabase) {
        Log.I("xdmDbOperateTenUpdate Create Last Update");
        sQLiteDatabase.execSQL(XDBInterface.DATABASE_LAST_UPDATE_INFO_CREATE);
    }

    private void xdmDbProfileUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbTableRowExistCheck(sQLiteDatabase, "profile", 3)) {
            Log.I("xdmDbExistsProfileRow=3 delete");
            xdmDbDeleteTableRow(sQLiteDatabase, "profile", 3);
        }
    }

    private void xdmDbProfileListUpdate(SQLiteDatabase sQLiteDatabase) {
        if (xdmDbTableRowExistCheck(sQLiteDatabase, XDBInterface.XDB_PROFILELIST_TABLE, 1)) {
            Log.I("Profile List Row Refresh");
            XDBProfileListInfo xdmDbGetProfileList = xdmDbGetProfileList(sQLiteDatabase, 1);
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS profilelist");
            sQLiteDatabase.execSQL(XDBInterface.DATABASE_PROFILELIST_CREATE);
            xdmDbInsertProfileListRow(sQLiteDatabase, xdmDbGetProfileList);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005e, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005f, code lost:
        if (r12 != null) goto L_0x0061;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r12.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0069, code lost:
        throw r14;
     */
    private boolean xdmDbTableRowExistCheck(SQLiteDatabase sQLiteDatabase, String str, long j) {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID};
        if (sQLiteDatabase == null) {
            Log.I("db is null");
            return false;
        }
        try {
            Cursor query = sQLiteDatabase.query(true, str, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.I("xdmDbTableRowExistCheck cursor null");
                if (query != null) {
                    query.close();
                }
                return false;
            }
            boolean z = query.getCount() > 0;
            if (query != null) {
                query.close();
            }
            Log.I("xdmDbTableRowExistCheck result : " + z);
            return z;
        } catch (SQLException e) {
            Log.E(e.toString());
            return false;
        } catch (Throwable th) {
            r13.addSuppressed(th);
        }
    }

    private static void xdmDbDeleteTableRow(SQLiteDatabase sQLiteDatabase, String str, long j) {
        if (sQLiteDatabase == null) {
            Log.I("db is null");
            return;
        }
        try {
            sQLiteDatabase.delete(str, "rowid=" + j, (String[]) null);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    private static void xdmDbInsertProfileListRow(SQLiteDatabase sQLiteDatabase, XDBProfileListInfo xDBProfileListInfo) {
        if (sQLiteDatabase == null) {
            Log.I("db is null");
            return;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("networkconnname", xDBProfileListInfo.m_szNetworkConnName);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_PROXYINDEX, Integer.valueOf(xDBProfileListInfo.nProxyIndex));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILEINDEX, Integer.valueOf(xDBProfileListInfo.Profileindex));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILENAME1, xDBProfileListInfo.ProfileName[0]);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILENAME2, xDBProfileListInfo.ProfileName[1]);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_SESSIONID, xDBProfileListInfo.m_szSessionID);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIEVENT, Integer.valueOf(xDBProfileListInfo.nNotiEvent));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIOPMODE, Integer.valueOf(xDBProfileListInfo.nNotiOpMode));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIJOBID, Integer.valueOf(xDBProfileListInfo.nNotiJobId));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_DESTORYNOTITIME, Long.valueOf(xDBProfileListInfo.nDestoryNotiTime));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESYNCMODE, Integer.valueOf(xDBProfileListInfo.nNotiReSyncMode));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_DDFPARSERNODEINDEX, Integer.valueOf(xDBProfileListInfo.nDDFParserNodeIndex));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_SKIPDEVDISCOVERY, Boolean.valueOf(xDBProfileListInfo.bSkipDevDiscovery));
            contentValues.put("magicnumber", Integer.valueOf(xDBProfileListInfo.MagicNumber));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_SESSIONSAVESTATE, Integer.valueOf(xDBProfileListInfo.NotiResumeState.nSessionSaveState));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_NOTIUIEVENT, Integer.valueOf(xDBProfileListInfo.NotiResumeState.nNotiUiEvent));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_NOTIRETRYCOUNT, Integer.valueOf(xDBProfileListInfo.NotiResumeState.nNotiRetryCount));
            contentValues.put("status", Integer.valueOf(xDBProfileListInfo.tUicResultKeep.eStatus));
            contentValues.put("appid", Integer.valueOf(xDBProfileListInfo.tUicResultKeep.appId));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_UICTYPE, Integer.valueOf(xDBProfileListInfo.tUicResultKeep.UICType));
            contentValues.put("result", Integer.valueOf(xDBProfileListInfo.tUicResultKeep.result));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_NUMBER, Integer.valueOf(xDBProfileListInfo.tUicResultKeep.number));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_TEXT, xDBProfileListInfo.tUicResultKeep.m_szText);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_LEN, Integer.valueOf(xDBProfileListInfo.tUicResultKeep.nLen));
            contentValues.put("size", Integer.valueOf(xDBProfileListInfo.tUicResultKeep.nSize));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_WIFIONLY, Boolean.valueOf(xDBProfileListInfo.bWifiOnly));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_AUTOUPDATE, Boolean.valueOf(xDBProfileListInfo.bAutoUpdate));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_PUSHMESSAGE, Boolean.valueOf(xDBProfileListInfo.bPushMessage));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_SAVE_DELTAFILE_INDEX, Integer.valueOf(xDBProfileListInfo.nSaveDeltaFileIndex));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILELIST_DEVICE_REGISTER, Integer.valueOf(xDBProfileListInfo.nDeviceRegister));
            sQLiteDatabase.insert(XDBInterface.XDB_PROFILELIST_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }

    private static XDBProfileListInfo xdmDbGetProfileList(SQLiteDatabase sQLiteDatabase, long j) {
        XDBProfileListInfo xDBProfileListInfo;
        Throwable th;
        Throwable th2;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "networkconnname", XDBInterface.XDM_SQL_DB_PROFILELIST_PROXYINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILEINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILENAME1, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILENAME2, XDBInterface.XDM_SQL_DB_PROFILELIST_SESSIONID, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIEVENT, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIOPMODE, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIJOBID, XDBInterface.XDM_SQL_DB_PROFILELIST_DESTORYNOTITIME, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESYNCMODE, XDBInterface.XDM_SQL_DB_PROFILELIST_DDFPARSERNODEINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_SKIPDEVDISCOVERY, "magicnumber", XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_SESSIONSAVESTATE, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_NOTIUIEVENT, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_NOTIRETRYCOUNT, "status", "appid", XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_UICTYPE, "result", XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_NUMBER, XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_TEXT, XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_LEN, "size", XDBInterface.XDM_SQL_DB_PROFILELIST_WIFIONLY, XDBInterface.XDM_SQL_DB_PROFILELIST_AUTOUPDATE, XDBInterface.XDM_SQL_DB_PROFILELIST_PUSHMESSAGE, XDBInterface.XDM_SQL_DB_PROFILELIST_SAVE_DELTAFILE_INDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_DEVICE_REGISTER};
        XDBProfileListInfo xDBProfileListInfo2 = null;
        if (sQLiteDatabase == null) {
            Log.I("db is null");
            return null;
        }
        try {
            Cursor query = sQLiteDatabase.query(true, XDBInterface.XDB_PROFILELIST_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                try {
                    Log.I("cursor is null");
                    if (query != null) {
                        query.close();
                    }
                    return null;
                } catch (Throwable th3) {
                    th = th3;
                    xDBProfileListInfo = null;
                    th = th;
                    try {
                        throw th;
                    } catch (SQLException e) {
                        e = e;
                        xDBProfileListInfo2 = xDBProfileListInfo;
                        Log.E(e.toString());
                        return xDBProfileListInfo2;
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                }
            } else {
                if (query.getCount() > 0 && query.moveToFirst()) {
                    xDBProfileListInfo = new XDBProfileListInfo();
                    try {
                        xDBProfileListInfo.m_szNetworkConnName = query.getString(1);
                        xDBProfileListInfo.nProxyIndex = query.getInt(2);
                        xDBProfileListInfo.Profileindex = query.getInt(3);
                        xDBProfileListInfo.ProfileName[0] = query.getString(4);
                        xDBProfileListInfo.ProfileName[1] = query.getString(5);
                        xDBProfileListInfo.m_szSessionID = query.getString(6);
                        xDBProfileListInfo.nNotiEvent = query.getInt(7);
                        xDBProfileListInfo.nNotiOpMode = query.getInt(8);
                        xDBProfileListInfo.nNotiJobId = query.getInt(9);
                        xDBProfileListInfo.nDestoryNotiTime = (long) query.getInt(10);
                        xDBProfileListInfo.nNotiReSyncMode = query.getInt(11);
                        xDBProfileListInfo.nDDFParserNodeIndex = query.getInt(12);
                        if (query.getInt(13) != 0) {
                            xDBProfileListInfo.bSkipDevDiscovery = true;
                        } else {
                            xDBProfileListInfo.bSkipDevDiscovery = false;
                        }
                        xDBProfileListInfo.MagicNumber = query.getInt(14);
                        xDBProfileListInfo.NotiResumeState.nSessionSaveState = query.getInt(15);
                        xDBProfileListInfo.NotiResumeState.nNotiUiEvent = query.getInt(16);
                        xDBProfileListInfo.NotiResumeState.nNotiRetryCount = query.getInt(17);
                        xDBProfileListInfo.tUicResultKeep.eStatus = query.getInt(18);
                        xDBProfileListInfo.tUicResultKeep.appId = query.getInt(19);
                        xDBProfileListInfo.tUicResultKeep.UICType = query.getInt(20);
                        xDBProfileListInfo.tUicResultKeep.result = query.getInt(21);
                        xDBProfileListInfo.tUicResultKeep.number = query.getInt(22);
                        xDBProfileListInfo.tUicResultKeep.m_szText = query.getString(23);
                        xDBProfileListInfo.tUicResultKeep.nLen = query.getInt(24);
                        xDBProfileListInfo.tUicResultKeep.nSize = query.getInt(25);
                        if (query.getInt(26) != 0) {
                            xDBProfileListInfo.bWifiOnly = true;
                        } else {
                            xDBProfileListInfo.bWifiOnly = false;
                        }
                        if (query.getInt(27) != 0) {
                            xDBProfileListInfo.bAutoUpdate = true;
                        } else {
                            xDBProfileListInfo.bAutoUpdate = false;
                        }
                        if (query.getInt(28) != 0) {
                            xDBProfileListInfo.bPushMessage = true;
                        } else {
                            xDBProfileListInfo.bPushMessage = false;
                        }
                        xDBProfileListInfo.nSaveDeltaFileIndex = query.getInt(29);
                        xDBProfileListInfo.nDeviceRegister = query.getInt(30);
                        xDBProfileListInfo2 = xDBProfileListInfo;
                    } catch (Throwable th5) {
                        th = th5;
                        th = th;
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
                return xDBProfileListInfo2;
            }
        } catch (SQLException e2) {
            e = e2;
        }
        throw th2;
    }

    private void xdmDbUpdateDefaultValue(SQLiteDatabase sQLiteDatabase, String str, String str2, String str3) {
        if (sQLiteDatabase == null) {
            Log.I("db is null");
            return;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(str2, str3);
            sQLiteDatabase.update(str, contentValues, "rowid=1", (String[]) null);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
    }
}
