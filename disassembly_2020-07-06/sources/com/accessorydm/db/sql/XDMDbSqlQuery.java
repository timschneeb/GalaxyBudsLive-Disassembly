package com.accessorydm.db.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.CancellationSignal;
import com.accessorydm.db.XDMDbManager;
import com.accessorydm.db.file.XDBAESCrypt;
import com.accessorydm.db.file.XDBAccXNodeInfo;
import com.accessorydm.db.file.XDBAgentInfo;
import com.accessorydm.db.file.XDBFumoInfo;
import com.accessorydm.db.file.XDBPostPoneInfo;
import com.accessorydm.db.file.XDBProfileInfo;
import com.accessorydm.db.file.XDBProfileListInfo;
import com.accessorydm.db.file.XDBResyncModeInfo;
import com.accessorydm.db.file.XDBSimInfo;
import com.accessorydm.db.file.XDBUserDBException;
import com.accessorydm.interfaces.XDBInterface;
import com.samsung.android.fotaagent.polling.PollingInfo;
import com.samsung.android.fotaprovider.log.Log;

public class XDMDbSqlQuery implements XDBInterface {
    public static void xdmDbFullReset() {
        Log.I("xdmDbFullReset");
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS profile");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS network");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS profilelist");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS fumo");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS postpone");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS siminfo");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS polling");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS accxlistnode");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS resyncmode");
            xdmDbGetWritableDatabase.execSQL("DROP TABLE IF EXISTS DmAgnetInfo");
            XNOTIDbSqlQuery.xnotiDbSqlDrop(xdmDbGetWritableDatabase);
            XDMAccessoryDbSqlQuery.xdmAccessoryDbSqlDrop(xdmDbGetWritableDatabase);
            XDMRegistrationDbSqlQuery.deleteDBRegistration(xdmDbGetWritableDatabase);
            XDMLastUpdateDbSqlQuery.deleteDBLastUpdate(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
        }
        try {
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_PROFILE_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_NETWORK_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_PROFILELIST_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_FUMO_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_POSTPONE_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_SIMINFO_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_POLLING_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_ACCXLISTNODE_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_RESYNCMODE_CREATE);
            xdmDbGetWritableDatabase.execSQL(XDBInterface.DATABASE_DM_AGENT_INFO_CREATE);
            XNOTIDbSqlQuery.xnotiDbSqlCreate(xdmDbGetWritableDatabase);
            XDMAccessoryDbSqlQuery.xdmAccessoryDbSqlCreate(xdmDbGetWritableDatabase);
            XDMRegistrationDbSqlQuery.createDBRegistration(xdmDbGetWritableDatabase);
            XDMLastUpdateDbSqlQuery.createDBLastUpdate(xdmDbGetWritableDatabase);
        } catch (SQLException e2) {
            Log.E(e2.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
    }

    public static long xdmDbInsertProfileRow(XDBProfileInfo xDBProfileInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            contentValues.put("protocol", xDBProfileInfo.Protocol);
            contentValues.put("serverport", Integer.valueOf(xDBProfileInfo.ServerPort));
            contentValues.put("serverurl", XDBAESCrypt.xdbEncryptorStrBase64(xDBProfileInfo.ServerUrl, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put("serverip", xDBProfileInfo.ServerIP);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PATH, xDBProfileInfo.Path);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PROTOCOL_ORG, xDBProfileInfo.Protocol_Org);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERPORT_ORG, Integer.valueOf(xDBProfileInfo.ServerPort_Org));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERURL_ORG, XDBAESCrypt.xdbEncryptorStrBase64(xDBProfileInfo.ServerUrl_Org, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERIP_ORG, xDBProfileInfo.ServerIP_Org);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PATH_ORG, xDBProfileInfo.Path_Org);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_CHANGEDPROTOCOL, Boolean.valueOf(xDBProfileInfo.bChangedProtocol));
            contentValues.put("obextype", Integer.valueOf(xDBProfileInfo.ObexType));
            contentValues.put("authtype", Integer.valueOf(xDBProfileInfo.AuthType));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERAUTHTYPE, Integer.valueOf(xDBProfileInfo.nServerAuthType));
            contentValues.put("appid", xDBProfileInfo.AppID);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_AUTHLEVEL, xDBProfileInfo.AuthLevel);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERAUTHLEVEL, xDBProfileInfo.ServerAuthLevel);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PREFCONREF, xDBProfileInfo.PrefConRef);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_USERNAME, XDBAESCrypt.xdbEncryptor(xDBProfileInfo.UserName, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PASSWORD, XDBAESCrypt.xdbEncryptor(xDBProfileInfo.Password, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERID, XDBAESCrypt.xdbEncryptor(xDBProfileInfo.ServerID, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERPWD, XDBAESCrypt.xdbEncryptor(xDBProfileInfo.ServerPwd, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_CLIENTNONCE, xDBProfileInfo.ClientNonce);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERNONCE, xDBProfileInfo.ServerNonce);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERNONCEFORMAT, Integer.valueOf(xDBProfileInfo.ServerNonceFormat));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_CLIENTNONCEFORMAT, Integer.valueOf(xDBProfileInfo.ClientNonceFormat));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PROFILENAME, xDBProfileInfo.ProfileName);
            contentValues.put("networkconnname", xDBProfileInfo.NetworkConnName);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_NETWORKCONNINDEX, Integer.valueOf(xDBProfileInfo.nNetworkConnIndex));
            contentValues.put("magicnumber", Integer.valueOf(xDBProfileInfo.MagicNumber));
            j = xdmDbGetWritableDatabase.insert("profile", (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    public static void xdmDbUpdateProfileRow(long j, XDBProfileInfo xDBProfileInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put("protocol", xDBProfileInfo.Protocol);
            contentValues.put("serverport", Integer.valueOf(xDBProfileInfo.ServerPort));
            contentValues.put("serverurl", XDBAESCrypt.xdbEncryptorStrBase64(xDBProfileInfo.ServerUrl, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put("serverip", xDBProfileInfo.ServerIP);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PATH, xDBProfileInfo.Path);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PROTOCOL_ORG, xDBProfileInfo.Protocol_Org);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERPORT_ORG, Integer.valueOf(xDBProfileInfo.ServerPort_Org));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERURL_ORG, XDBAESCrypt.xdbEncryptorStrBase64(xDBProfileInfo.ServerUrl_Org, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERIP_ORG, xDBProfileInfo.ServerIP_Org);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PATH_ORG, xDBProfileInfo.Path_Org);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_CHANGEDPROTOCOL, Boolean.valueOf(xDBProfileInfo.bChangedProtocol));
            contentValues.put("obextype", Integer.valueOf(xDBProfileInfo.ObexType));
            contentValues.put("authtype", Integer.valueOf(xDBProfileInfo.AuthType));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERAUTHTYPE, Integer.valueOf(xDBProfileInfo.nServerAuthType));
            contentValues.put("appid", xDBProfileInfo.AppID);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_AUTHLEVEL, xDBProfileInfo.AuthLevel);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERAUTHLEVEL, xDBProfileInfo.ServerAuthLevel);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PREFCONREF, xDBProfileInfo.PrefConRef);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_USERNAME, XDBAESCrypt.xdbEncryptor(xDBProfileInfo.UserName, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PASSWORD, XDBAESCrypt.xdbEncryptor(xDBProfileInfo.Password, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERID, XDBAESCrypt.xdbEncryptor(xDBProfileInfo.ServerID, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERPWD, XDBAESCrypt.xdbEncryptor(xDBProfileInfo.ServerPwd, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_CLIENTNONCE, xDBProfileInfo.ClientNonce);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERNONCE, xDBProfileInfo.ServerNonce);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_SERVERNONCEFORMAT, Integer.valueOf(xDBProfileInfo.ServerNonceFormat));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_CLIENTNONCEFORMAT, Integer.valueOf(xDBProfileInfo.ClientNonceFormat));
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_PROFILENAME, xDBProfileInfo.ProfileName);
            contentValues.put("networkconnname", xDBProfileInfo.NetworkConnName);
            contentValues.put(XDBInterface.XDM_SQL_DB_PROFILE_NETWORKCONNINDEX, Integer.valueOf(xDBProfileInfo.nNetworkConnIndex));
            contentValues.put("magicnumber", Integer.valueOf(xDBProfileInfo.MagicNumber));
            xdmDbGetWritableDatabase.update("profile", contentValues, "rowid=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    public static XDBProfileInfo xdmDbFetchProfileRow(long j) throws XDBUserDBException {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "protocol", "serverport", "serverurl", "serverip", XDBInterface.XDM_SQL_DB_PROFILE_PATH, XDBInterface.XDM_SQL_DB_PROFILE_PROTOCOL_ORG, XDBInterface.XDM_SQL_DB_PROFILE_SERVERPORT_ORG, XDBInterface.XDM_SQL_DB_PROFILE_SERVERURL_ORG, XDBInterface.XDM_SQL_DB_PROFILE_SERVERIP_ORG, XDBInterface.XDM_SQL_DB_PROFILE_PATH_ORG, XDBInterface.XDM_SQL_DB_PROFILE_CHANGEDPROTOCOL, "obextype", "authtype", XDBInterface.XDM_SQL_DB_PROFILE_SERVERAUTHTYPE, "appid", XDBInterface.XDM_SQL_DB_PROFILE_AUTHLEVEL, XDBInterface.XDM_SQL_DB_PROFILE_SERVERAUTHLEVEL, XDBInterface.XDM_SQL_DB_PROFILE_PREFCONREF, XDBInterface.XDM_SQL_DB_PROFILE_USERNAME, XDBInterface.XDM_SQL_DB_PROFILE_PASSWORD, XDBInterface.XDM_SQL_DB_PROFILE_SERVERID, XDBInterface.XDM_SQL_DB_PROFILE_SERVERPWD, XDBInterface.XDM_SQL_DB_PROFILE_CLIENTNONCE, XDBInterface.XDM_SQL_DB_PROFILE_SERVERNONCE, XDBInterface.XDM_SQL_DB_PROFILE_SERVERNONCEFORMAT, XDBInterface.XDM_SQL_DB_PROFILE_CLIENTNONCEFORMAT, XDBInterface.XDM_SQL_DB_PROFILE_PROFILENAME, "networkconnname", XDBInterface.XDM_SQL_DB_PROFILE_NETWORKCONNINDEX, "magicnumber"};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBProfileInfo xDBProfileInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, "profile", strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
                    xDBProfileInfo = new XDBProfileInfo();
                    xDBProfileInfo.Protocol = query.getString(1);
                    xDBProfileInfo.ServerPort = query.getInt(2);
                    xDBProfileInfo.ServerUrl = XDBAESCrypt.xdbDecryptorStrBase64(query.getString(3), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBProfileInfo.ServerIP = query.getString(4);
                    xDBProfileInfo.Path = query.getString(5);
                    xDBProfileInfo.Protocol_Org = query.getString(6);
                    xDBProfileInfo.ServerPort_Org = query.getInt(7);
                    xDBProfileInfo.ServerUrl_Org = XDBAESCrypt.xdbDecryptorStrBase64(query.getString(8), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBProfileInfo.ServerIP_Org = query.getString(9);
                    xDBProfileInfo.Path_Org = query.getString(10);
                    if (query.getInt(11) != 0) {
                        xDBProfileInfo.bChangedProtocol = true;
                    } else {
                        xDBProfileInfo.bChangedProtocol = false;
                    }
                    xDBProfileInfo.ObexType = query.getInt(12);
                    xDBProfileInfo.AuthType = query.getInt(13);
                    xDBProfileInfo.nServerAuthType = query.getInt(14);
                    xDBProfileInfo.AppID = query.getString(15);
                    xDBProfileInfo.AuthLevel = query.getString(16);
                    xDBProfileInfo.ServerAuthLevel = query.getString(17);
                    xDBProfileInfo.PrefConRef = query.getString(18);
                    xDBProfileInfo.UserName = XDBAESCrypt.xdbDecryptor(query.getBlob(19), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBProfileInfo.Password = XDBAESCrypt.xdbDecryptor(query.getBlob(20), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBProfileInfo.ServerID = XDBAESCrypt.xdbDecryptor(query.getBlob(21), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBProfileInfo.ServerPwd = XDBAESCrypt.xdbDecryptor(query.getBlob(22), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBProfileInfo.ClientNonce = query.getString(23);
                    xDBProfileInfo.ServerNonce = query.getString(24);
                    xDBProfileInfo.ServerNonceFormat = query.getInt(25);
                    xDBProfileInfo.ClientNonceFormat = query.getInt(26);
                    xDBProfileInfo.ProfileName = query.getString(27);
                    xDBProfileInfo.NetworkConnName = query.getString(28);
                    xDBProfileInfo.nNetworkConnIndex = query.getInt(29);
                    xDBProfileInfo.MagicNumber = query.getInt(30);
                }
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return xDBProfileInfo;
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
    public static boolean xdmDbExistsProfileRow(long j) {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "protocol", "serverport", "serverurl", "serverip", XDBInterface.XDM_SQL_DB_PROFILE_PATH, XDBInterface.XDM_SQL_DB_PROFILE_PROTOCOL_ORG, XDBInterface.XDM_SQL_DB_PROFILE_SERVERPORT_ORG, XDBInterface.XDM_SQL_DB_PROFILE_SERVERURL_ORG, XDBInterface.XDM_SQL_DB_PROFILE_SERVERIP_ORG, XDBInterface.XDM_SQL_DB_PROFILE_PATH_ORG, XDBInterface.XDM_SQL_DB_PROFILE_CHANGEDPROTOCOL, "obextype", "authtype", XDBInterface.XDM_SQL_DB_PROFILE_SERVERAUTHTYPE, "appid", XDBInterface.XDM_SQL_DB_PROFILE_AUTHLEVEL, XDBInterface.XDM_SQL_DB_PROFILE_SERVERAUTHLEVEL, XDBInterface.XDM_SQL_DB_PROFILE_PREFCONREF, XDBInterface.XDM_SQL_DB_PROFILE_USERNAME, XDBInterface.XDM_SQL_DB_PROFILE_PASSWORD, XDBInterface.XDM_SQL_DB_PROFILE_SERVERID, XDBInterface.XDM_SQL_DB_PROFILE_SERVERPWD, XDBInterface.XDM_SQL_DB_PROFILE_CLIENTNONCE, XDBInterface.XDM_SQL_DB_PROFILE_SERVERNONCE, XDBInterface.XDM_SQL_DB_PROFILE_SERVERNONCEFORMAT, XDBInterface.XDM_SQL_DB_PROFILE_CLIENTNONCEFORMAT, XDBInterface.XDM_SQL_DB_PROFILE_PROFILENAME, "networkconnname", XDBInterface.XDM_SQL_DB_PROFILE_NETWORKCONNINDEX, "magicnumber"};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, "profile", strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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

    public static long xdmDbInsertProfileListRow(XDBProfileListInfo xDBProfileListInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
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
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_PROFILELIST_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    public static void xdmDbUpdateProfileListRow(long j, XDBProfileListInfo xDBProfileListInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
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
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_PROFILELIST_TABLE, contentValues, "rowid=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    public static XDBProfileListInfo xdmDbFetchProfileListRow(long j) throws XDBUserDBException {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "networkconnname", XDBInterface.XDM_SQL_DB_PROFILELIST_PROXYINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILEINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILENAME1, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILENAME2, XDBInterface.XDM_SQL_DB_PROFILELIST_SESSIONID, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIEVENT, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIOPMODE, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIJOBID, XDBInterface.XDM_SQL_DB_PROFILELIST_DESTORYNOTITIME, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESYNCMODE, XDBInterface.XDM_SQL_DB_PROFILELIST_DDFPARSERNODEINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_SKIPDEVDISCOVERY, "magicnumber", XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_SESSIONSAVESTATE, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_NOTIUIEVENT, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_NOTIRETRYCOUNT, "status", "appid", XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_UICTYPE, "result", XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_NUMBER, XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_TEXT, XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_LEN, "size", XDBInterface.XDM_SQL_DB_PROFILELIST_WIFIONLY, XDBInterface.XDM_SQL_DB_PROFILELIST_AUTOUPDATE, XDBInterface.XDM_SQL_DB_PROFILELIST_PUSHMESSAGE, XDBInterface.XDM_SQL_DB_PROFILELIST_SAVE_DELTAFILE_INDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_DEVICE_REGISTER};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBProfileListInfo xDBProfileListInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_PROFILELIST_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
                    xDBProfileListInfo = new XDBProfileListInfo();
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
                }
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return xDBProfileListInfo;
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
    public static boolean xdmDbExistsProfileListRow(long j) {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "networkconnname", XDBInterface.XDM_SQL_DB_PROFILELIST_PROXYINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILEINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILENAME1, XDBInterface.XDM_SQL_DB_PROFILELIST_PROFILENAME2, XDBInterface.XDM_SQL_DB_PROFILELIST_SESSIONID, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIEVENT, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIOPMODE, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIJOBID, XDBInterface.XDM_SQL_DB_PROFILELIST_DESTORYNOTITIME, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESYNCMODE, XDBInterface.XDM_SQL_DB_PROFILELIST_DDFPARSERNODEINDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_SKIPDEVDISCOVERY, "magicnumber", XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_SESSIONSAVESTATE, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_NOTIUIEVENT, XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_NOTIRETRYCOUNT, "status", "appid", XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_UICTYPE, "result", XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_NUMBER, XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_TEXT, XDBInterface.XDM_SQL_DB_PROFILELIST_UICRESULTKEEP_LEN, "size", XDBInterface.XDM_SQL_DB_PROFILELIST_WIFIONLY, XDBInterface.XDM_SQL_DB_PROFILELIST_AUTOUPDATE, XDBInterface.XDM_SQL_DB_PROFILELIST_PUSHMESSAGE, XDBInterface.XDM_SQL_DB_PROFILELIST_SAVE_DELTAFILE_INDEX, XDBInterface.XDM_SQL_DB_PROFILELIST_DEVICE_REGISTER};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_PROFILELIST_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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

    public static long xdmDbInsertFUMORow(XDBFumoInfo xDBFumoInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            contentValues.put("protocol", xDBFumoInfo.m_szProtocol);
            contentValues.put("obextype", Integer.valueOf(xDBFumoInfo.ObexType));
            contentValues.put("authtype", Integer.valueOf(xDBFumoInfo.AuthType));
            contentValues.put("serverport", Integer.valueOf(xDBFumoInfo.ServerPort));
            contentValues.put("serverurl", XDBAESCrypt.xdbEncryptorStrBase64(xDBFumoInfo.ServerUrl, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put("serverip", xDBFumoInfo.ServerIP);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADPROTOCOL, xDBFumoInfo.m_szObjectDownloadProtocol);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADURL, XDBAESCrypt.xdbEncryptorStrBase64(xDBFumoInfo.m_szObjectDownloadUrl, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADIP, xDBFumoInfo.m_szObjectDownloadIP);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADPORT, Integer.valueOf(xDBFumoInfo.nObjectDownloadPort));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYPROTOCOL, xDBFumoInfo.m_szStatusNotifyProtocol);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYURL, XDBAESCrypt.xdbEncryptorStrBase64(xDBFumoInfo.m_szStatusNotifyUrl, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYIP, xDBFumoInfo.m_szStatusNotifyIP);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYPORT, Integer.valueOf(xDBFumoInfo.nStatusNotifyPort));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_REPORTURI, xDBFumoInfo.m_szReportURI);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTSIZE, Long.valueOf(xDBFumoInfo.nObjectSize));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_FFSWRITESIZE, Integer.valueOf(xDBFumoInfo.nFFSWriteSize));
            contentValues.put("status", Integer.valueOf(xDBFumoInfo.nStatus));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNODENAME, xDBFumoInfo.m_szStatusNodeName);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_RESULTCODE, xDBFumoInfo.ResultCode);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_UPDATEMECHANISM, Integer.valueOf(xDBFumoInfo.nUpdateMechanism));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_DOWNLOADMODE, Boolean.valueOf(xDBFumoInfo.nDownloadMode));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_CORRELATOR, xDBFumoInfo.Correlator);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_CONTENTTYPE, xDBFumoInfo.m_szContentType);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_ACCEPTTYPE, xDBFumoInfo.m_szAcceptType);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_UPDATEWAIT, Boolean.valueOf(xDBFumoInfo.bUpdateWait));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_INITIATED_TYPE, Integer.valueOf(xDBFumoInfo.nInitiatedType));
            contentValues.put("description", xDBFumoInfo.szDescription);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OPTIONALUPDATE, Boolean.valueOf(xDBFumoInfo.m_bOptionalUpdate));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OPTIONALCANCEL, Boolean.valueOf(xDBFumoInfo.m_bOptionalCancel));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_DOWNLOAD_RESULTCODE, xDBFumoInfo.szDownloadResultCode);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_WIFIONLYDOWNLOAD, Boolean.valueOf(xDBFumoInfo.m_bWifiOnlyDownload));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_CHECKROOTING, Boolean.valueOf(xDBFumoInfo.m_bCheckRooting));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_CURRENT_DOWNLOADMODE, Integer.valueOf(xDBFumoInfo.nCurrentDownloadMode));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_BIGDELTA_DOWNLOAD, Boolean.valueOf(xDBFumoInfo.m_bBigDeltaDownload));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_COPY_RETRY_COUNT, Integer.valueOf(xDBFumoInfo.nCopyRetryCount));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_NETCONNTYPE, xDBFumoInfo.szNetworkConnType);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_LOWBATTERY_RETRY_COUNT, Integer.valueOf(xDBFumoInfo.nLowBatteryRetryCount));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTHASH, xDBFumoInfo.szObjectHash);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_UPDATE_FW, xDBFumoInfo.szUpdateFWVer);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_UIMODE, Integer.valueOf(xDBFumoInfo.nUiMode));
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_FUMO_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    public static XDBFumoInfo xdmDbFetchFUMORow(long j) throws XDBUserDBException {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "protocol", "obextype", "authtype", "serverport", "serverurl", "serverip", XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADPROTOCOL, XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADURL, XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADIP, XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADPORT, XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYPROTOCOL, XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYURL, XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYIP, XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYPORT, XDBInterface.XDM_SQL_DB_FUMO_REPORTURI, XDBInterface.XDM_SQL_DB_FUMO_OBJECTSIZE, XDBInterface.XDM_SQL_DB_FUMO_FFSWRITESIZE, "status", XDBInterface.XDM_SQL_DB_FUMO_STATUSNODENAME, XDBInterface.XDM_SQL_DB_FUMO_RESULTCODE, XDBInterface.XDM_SQL_DB_FUMO_UPDATEMECHANISM, XDBInterface.XDM_SQL_DB_FUMO_DOWNLOADMODE, XDBInterface.XDM_SQL_DB_FUMO_CORRELATOR, XDBInterface.XDM_SQL_DB_FUMO_CONTENTTYPE, XDBInterface.XDM_SQL_DB_FUMO_ACCEPTTYPE, XDBInterface.XDM_SQL_DB_FUMO_UPDATEWAIT, XDBInterface.XDM_SQL_DB_FUMO_INITIATED_TYPE, "description", XDBInterface.XDM_SQL_DB_FUMO_OPTIONALUPDATE, XDBInterface.XDM_SQL_DB_FUMO_OPTIONALCANCEL, XDBInterface.XDM_SQL_DB_FUMO_DOWNLOAD_RESULTCODE, XDBInterface.XDM_SQL_DB_FUMO_WIFIONLYDOWNLOAD, XDBInterface.XDM_SQL_DB_FUMO_CHECKROOTING, XDBInterface.XDM_SQL_DB_FUMO_CURRENT_DOWNLOADMODE, XDBInterface.XDM_SQL_DB_FUMO_BIGDELTA_DOWNLOAD, XDBInterface.XDM_SQL_DB_FUMO_COPY_RETRY_COUNT, XDBInterface.XDM_SQL_DB_FUMO_NETCONNTYPE, XDBInterface.XDM_SQL_DB_FUMO_LOWBATTERY_RETRY_COUNT, XDBInterface.XDM_SQL_DB_FUMO_OBJECTHASH, XDBInterface.XDM_SQL_DB_FUMO_UPDATE_FW, XDBInterface.XDM_SQL_DB_FUMO_UIMODE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBFumoInfo xDBFumoInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_FUMO_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
                    xDBFumoInfo = new XDBFumoInfo();
                    xDBFumoInfo.m_szProtocol = query.getString(1);
                    xDBFumoInfo.ObexType = query.getInt(2);
                    xDBFumoInfo.AuthType = query.getInt(3);
                    xDBFumoInfo.ServerPort = query.getInt(4);
                    xDBFumoInfo.ServerUrl = XDBAESCrypt.xdbDecryptorStrBase64(query.getString(5), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBFumoInfo.ServerIP = query.getString(6);
                    xDBFumoInfo.m_szObjectDownloadProtocol = query.getString(7);
                    xDBFumoInfo.m_szObjectDownloadUrl = XDBAESCrypt.xdbDecryptorStrBase64(query.getString(8), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBFumoInfo.m_szObjectDownloadIP = query.getString(9);
                    xDBFumoInfo.nObjectDownloadPort = query.getInt(10);
                    xDBFumoInfo.m_szStatusNotifyProtocol = query.getString(11);
                    xDBFumoInfo.m_szStatusNotifyUrl = XDBAESCrypt.xdbDecryptorStrBase64(query.getString(12), XDBAESCrypt.CRYPTO_SEED_PASSWORD);
                    xDBFumoInfo.m_szStatusNotifyIP = query.getString(13);
                    xDBFumoInfo.nStatusNotifyPort = query.getInt(14);
                    xDBFumoInfo.m_szReportURI = query.getString(15);
                    xDBFumoInfo.nObjectSize = query.getLong(16);
                    xDBFumoInfo.nFFSWriteSize = query.getInt(17);
                    xDBFumoInfo.nStatus = query.getInt(18);
                    xDBFumoInfo.m_szStatusNodeName = query.getString(19);
                    xDBFumoInfo.ResultCode = query.getString(20);
                    xDBFumoInfo.nUpdateMechanism = query.getInt(21);
                    if (query.getInt(22) == 0) {
                        xDBFumoInfo.nDownloadMode = false;
                    } else {
                        xDBFumoInfo.nDownloadMode = true;
                    }
                    xDBFumoInfo.Correlator = query.getString(23);
                    xDBFumoInfo.m_szContentType = query.getString(24);
                    xDBFumoInfo.m_szAcceptType = query.getString(25);
                    if (query.getInt(26) == 0) {
                        xDBFumoInfo.bUpdateWait = false;
                    } else {
                        xDBFumoInfo.bUpdateWait = true;
                    }
                    xDBFumoInfo.nInitiatedType = query.getInt(27);
                    xDBFumoInfo.szDescription = query.getString(28);
                    if (query.getInt(29) == 0) {
                        xDBFumoInfo.m_bOptionalUpdate = false;
                    } else {
                        xDBFumoInfo.m_bOptionalUpdate = true;
                    }
                    if (query.getInt(30) == 0) {
                        xDBFumoInfo.m_bOptionalCancel = false;
                    } else {
                        xDBFumoInfo.m_bOptionalCancel = true;
                    }
                    xDBFumoInfo.szDownloadResultCode = query.getString(31);
                    if (query.getInt(32) == 0) {
                        xDBFumoInfo.m_bWifiOnlyDownload = false;
                    } else {
                        xDBFumoInfo.m_bWifiOnlyDownload = true;
                    }
                    if (query.getInt(33) == 0) {
                        xDBFumoInfo.m_bCheckRooting = false;
                    } else {
                        xDBFumoInfo.m_bCheckRooting = true;
                    }
                    xDBFumoInfo.nCurrentDownloadMode = query.getInt(34);
                    if (query.getInt(35) == 0) {
                        xDBFumoInfo.m_bBigDeltaDownload = false;
                    } else {
                        xDBFumoInfo.m_bBigDeltaDownload = true;
                    }
                    xDBFumoInfo.nCopyRetryCount = query.getInt(36);
                    xDBFumoInfo.szNetworkConnType = query.getString(37);
                    xDBFumoInfo.nLowBatteryRetryCount = query.getInt(38);
                    xDBFumoInfo.szObjectHash = query.getString(39);
                    xDBFumoInfo.szUpdateFWVer = query.getString(40);
                    xDBFumoInfo.nUiMode = query.getInt(41);
                }
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return xDBFumoInfo;
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

    public static void xdmDbUpdateFUMORow(long j, XDBFumoInfo xDBFumoInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put("protocol", xDBFumoInfo.m_szProtocol);
            contentValues.put("obextype", Integer.valueOf(xDBFumoInfo.ObexType));
            contentValues.put("authtype", Integer.valueOf(xDBFumoInfo.AuthType));
            contentValues.put("serverport", Integer.valueOf(xDBFumoInfo.ServerPort));
            contentValues.put("serverurl", XDBAESCrypt.xdbEncryptorStrBase64(xDBFumoInfo.ServerUrl, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put("serverip", xDBFumoInfo.ServerIP);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADPROTOCOL, xDBFumoInfo.m_szObjectDownloadProtocol);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADURL, XDBAESCrypt.xdbEncryptorStrBase64(xDBFumoInfo.m_szObjectDownloadUrl, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADIP, xDBFumoInfo.m_szObjectDownloadIP);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADPORT, Integer.valueOf(xDBFumoInfo.nObjectDownloadPort));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYPROTOCOL, xDBFumoInfo.m_szStatusNotifyProtocol);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYURL, XDBAESCrypt.xdbEncryptorStrBase64(xDBFumoInfo.m_szStatusNotifyUrl, XDBAESCrypt.CRYPTO_SEED_PASSWORD));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYIP, xDBFumoInfo.m_szStatusNotifyIP);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYPORT, Integer.valueOf(xDBFumoInfo.nStatusNotifyPort));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_REPORTURI, xDBFumoInfo.m_szReportURI);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTSIZE, Long.valueOf(xDBFumoInfo.nObjectSize));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_FFSWRITESIZE, Integer.valueOf(xDBFumoInfo.nFFSWriteSize));
            contentValues.put("status", Integer.valueOf(xDBFumoInfo.nStatus));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_STATUSNODENAME, xDBFumoInfo.m_szStatusNodeName);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_RESULTCODE, xDBFumoInfo.ResultCode);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_UPDATEMECHANISM, Integer.valueOf(xDBFumoInfo.nUpdateMechanism));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_DOWNLOADMODE, Boolean.valueOf(xDBFumoInfo.nDownloadMode));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_CORRELATOR, xDBFumoInfo.Correlator);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_CONTENTTYPE, xDBFumoInfo.m_szContentType);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_ACCEPTTYPE, xDBFumoInfo.m_szAcceptType);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_UPDATEWAIT, Boolean.valueOf(xDBFumoInfo.bUpdateWait));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_INITIATED_TYPE, Integer.valueOf(xDBFumoInfo.nInitiatedType));
            contentValues.put("description", xDBFumoInfo.szDescription);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OPTIONALUPDATE, Boolean.valueOf(xDBFumoInfo.m_bOptionalUpdate));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OPTIONALCANCEL, Boolean.valueOf(xDBFumoInfo.m_bOptionalCancel));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_DOWNLOAD_RESULTCODE, xDBFumoInfo.szDownloadResultCode);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_WIFIONLYDOWNLOAD, Boolean.valueOf(xDBFumoInfo.m_bWifiOnlyDownload));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_CHECKROOTING, Boolean.valueOf(xDBFumoInfo.m_bCheckRooting));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_CURRENT_DOWNLOADMODE, Integer.valueOf(xDBFumoInfo.nCurrentDownloadMode));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_BIGDELTA_DOWNLOAD, Boolean.valueOf(xDBFumoInfo.m_bBigDeltaDownload));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_COPY_RETRY_COUNT, Integer.valueOf(xDBFumoInfo.nCopyRetryCount));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_NETCONNTYPE, xDBFumoInfo.szNetworkConnType);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_LOWBATTERY_RETRY_COUNT, Integer.valueOf(xDBFumoInfo.nLowBatteryRetryCount));
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_OBJECTHASH, xDBFumoInfo.szObjectHash);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_UPDATE_FW, xDBFumoInfo.szUpdateFWVer);
            contentValues.put(XDBInterface.XDM_SQL_DB_FUMO_UIMODE, Integer.valueOf(xDBFumoInfo.nUiMode));
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_FUMO_TABLE, contentValues, "rowid=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public static boolean xdmDbExistsFUMORow(long j) {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, "protocol", "obextype", "authtype", "serverport", "serverurl", "serverip", XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADPROTOCOL, XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADURL, XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADIP, XDBInterface.XDM_SQL_DB_FUMO_OBJECTDOWNLOADPORT, XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYPROTOCOL, XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYURL, XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYIP, XDBInterface.XDM_SQL_DB_FUMO_STATUSNOTIFYPORT, XDBInterface.XDM_SQL_DB_FUMO_REPORTURI, XDBInterface.XDM_SQL_DB_FUMO_OBJECTSIZE, XDBInterface.XDM_SQL_DB_FUMO_FFSWRITESIZE, "status", XDBInterface.XDM_SQL_DB_FUMO_STATUSNODENAME, XDBInterface.XDM_SQL_DB_FUMO_RESULTCODE, XDBInterface.XDM_SQL_DB_FUMO_UPDATEMECHANISM, XDBInterface.XDM_SQL_DB_FUMO_DOWNLOADMODE, XDBInterface.XDM_SQL_DB_FUMO_CORRELATOR, XDBInterface.XDM_SQL_DB_FUMO_CONTENTTYPE, XDBInterface.XDM_SQL_DB_FUMO_ACCEPTTYPE, XDBInterface.XDM_SQL_DB_FUMO_UPDATEWAIT, XDBInterface.XDM_SQL_DB_FUMO_INITIATED_TYPE, "description", XDBInterface.XDM_SQL_DB_FUMO_OPTIONALUPDATE, XDBInterface.XDM_SQL_DB_FUMO_OPTIONALCANCEL, XDBInterface.XDM_SQL_DB_FUMO_DOWNLOAD_RESULTCODE, XDBInterface.XDM_SQL_DB_FUMO_WIFIONLYDOWNLOAD, XDBInterface.XDM_SQL_DB_FUMO_CHECKROOTING, XDBInterface.XDM_SQL_DB_FUMO_CURRENT_DOWNLOADMODE, XDBInterface.XDM_SQL_DB_FUMO_BIGDELTA_DOWNLOAD, XDBInterface.XDM_SQL_DB_FUMO_COPY_RETRY_COUNT, XDBInterface.XDM_SQL_DB_FUMO_NETCONNTYPE, XDBInterface.XDM_SQL_DB_FUMO_LOWBATTERY_RETRY_COUNT, XDBInterface.XDM_SQL_DB_FUMO_OBJECTHASH, XDBInterface.XDM_SQL_DB_FUMO_UPDATE_FW, XDBInterface.XDM_SQL_DB_FUMO_UIMODE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_FUMO_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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

    public static long xdmDbInsertPostPoneRow(XDBPostPoneInfo xDBPostPoneInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_CURRENTTIME, 0);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_ENDTIME, Long.valueOf(xDBPostPoneInfo.getPostponeTime()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_AFTERDOWNLOADBATTERYSTATUS, false);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONETIME, 0);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONECOUNT, 0);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONEMAXCOUNT, 0);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONEDOWNLOAD, Integer.valueOf(xDBPostPoneInfo.getPostponeStatus()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_CURRENTVERSION, "");
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_AUTOINSTALL, false);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_FORCE, Integer.valueOf(xDBPostPoneInfo.getForceInstall()));
            contentValues.put(XDBInterface.XDM_SQL_DB_WIFI_POSTPONE_COUNT, Integer.valueOf(xDBPostPoneInfo.getWifiPostponeRetryCount()));
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_POSTPONE_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    public static XDBPostPoneInfo xdmDbFetchPostPoneRow(long j) throws XDBUserDBException {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_POSTPONE_CURRENTTIME, XDBInterface.XDM_SQL_DB_POSTPONE_ENDTIME, XDBInterface.XDM_SQL_DB_POSTPONE_AFTERDOWNLOADBATTERYSTATUS, XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONETIME, XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONECOUNT, XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONEMAXCOUNT, XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONEDOWNLOAD, XDBInterface.XDM_SQL_DB_POSTPONE_CURRENTVERSION, XDBInterface.XDM_SQL_DB_POSTPONE_AUTOINSTALL, XDBInterface.XDM_SQL_DB_POSTPONE_FORCE, XDBInterface.XDM_SQL_DB_WIFI_POSTPONE_COUNT};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBPostPoneInfo xDBPostPoneInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_POSTPONE_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
                    xDBPostPoneInfo = new XDBPostPoneInfo();
                    xDBPostPoneInfo.setPostponeTime(query.getLong(2));
                    xDBPostPoneInfo.setPostponeStatus(query.getInt(7));
                    xDBPostPoneInfo.setForceInstall(query.getInt(10));
                    xDBPostPoneInfo.setWifiPostponeRetryCount(query.getInt(11));
                }
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return xDBPostPoneInfo;
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

    public static void xdmDbUpdatePostPoneRow(long j, XDBPostPoneInfo xDBPostPoneInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_CURRENTTIME, 0);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_ENDTIME, Long.valueOf(xDBPostPoneInfo.getPostponeTime()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_AFTERDOWNLOADBATTERYSTATUS, false);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONETIME, 0);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONECOUNT, 0);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONEMAXCOUNT, 0);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONEDOWNLOAD, Integer.valueOf(xDBPostPoneInfo.getPostponeStatus()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_CURRENTVERSION, "");
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_AUTOINSTALL, false);
            contentValues.put(XDBInterface.XDM_SQL_DB_POSTPONE_FORCE, Integer.valueOf(xDBPostPoneInfo.getForceInstall()));
            contentValues.put(XDBInterface.XDM_SQL_DB_WIFI_POSTPONE_COUNT, Integer.valueOf(xDBPostPoneInfo.getWifiPostponeRetryCount()));
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_POSTPONE_TABLE, contentValues, "rowid=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public static boolean xdmDbExistsPostPoneRow(long j) {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_POSTPONE_CURRENTTIME, XDBInterface.XDM_SQL_DB_POSTPONE_ENDTIME, XDBInterface.XDM_SQL_DB_POSTPONE_AFTERDOWNLOADBATTERYSTATUS, XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONETIME, XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONECOUNT, XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONEMAXCOUNT, XDBInterface.XDM_SQL_DB_POSTPONE_POSTPONEDOWNLOAD, XDBInterface.XDM_SQL_DB_POSTPONE_CURRENTVERSION, XDBInterface.XDM_SQL_DB_POSTPONE_AUTOINSTALL, XDBInterface.XDM_SQL_DB_POSTPONE_FORCE, XDBInterface.XDM_SQL_DB_WIFI_POSTPONE_COUNT};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_POSTPONE_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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

    public static long xdmDbInsertSimInfoRow(XDBSimInfo xDBSimInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_SIMINFO_IMSI, xDBSimInfo.m_szIMSI);
            contentValues.put(XDBInterface.XDM_SQL_DB_SIMINFO_IMSI2, xDBSimInfo.m_szIMSI2);
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_SIMINFO_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006f, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0070, code lost:
        if (r13 != null) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x007a, code lost:
        throw r1;
     */
    public static XDBSimInfo xdmDbFetchSimInfoRow(long j) throws XDBUserDBException {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_SIMINFO_IMSI, XDBInterface.XDM_SQL_DB_SIMINFO_IMSI2};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBSimInfo xDBSimInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_SIMINFO_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return null;
            }
            if (query.getCount() > 0 && query.moveToFirst()) {
                xDBSimInfo = new XDBSimInfo();
                xDBSimInfo.m_szIMSI = query.getString(1);
                xDBSimInfo.m_szIMSI2 = query.getString(2);
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return xDBSimInfo;
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
                throw new XDBUserDBException(1);
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r14.addSuppressed(th2);
        }
    }

    public static void xdmDbUpdateSimInfoRow(long j, XDBSimInfo xDBSimInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_SIMINFO_IMSI, xDBSimInfo.m_szIMSI);
            contentValues.put(XDBInterface.XDM_SQL_DB_SIMINFO_IMSI2, xDBSimInfo.m_szIMSI2);
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_SIMINFO_TABLE, contentValues, "rowid=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0059, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005a, code lost:
        if (r13 != null) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0064, code lost:
        throw r2;
     */
    public static boolean xdmDbExistsSimInfoRow(long j) {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_SIMINFO_IMSI, XDBInterface.XDM_SQL_DB_SIMINFO_IMSI2};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_SIMINFO_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return false;
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r14.addSuppressed(th2);
        }
    }

    public static long xdmDbInsertPollingRow(PollingInfo pollingInfo) {
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_ORIGINAL_URL, pollingInfo.getOriginPreUrl());
            contentValues.put("url", pollingInfo.getPreUrl());
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_PERIODUNIT, pollingInfo.getPeriodUnit());
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_PERIOD, Integer.valueOf(pollingInfo.getPeriod()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_TIME, Integer.valueOf(pollingInfo.getTime()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_RANGE, Integer.valueOf(pollingInfo.getRange()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_CYCLEOFDEVICEHEARTBEAT, Integer.valueOf(pollingInfo.getHeartBeatPeriod()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_SERVICEURL, pollingInfo.getHeartBeatUrl());
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_NEXTPOLLINGTIME, Long.valueOf(pollingInfo.getNextPollingTime()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_NEXTREPORTTIME, Long.valueOf(pollingInfo.getNextHeartBeatTime()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_VERSIONFILENAME, pollingInfo.getVersionFileName());
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_POLLING_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    public static PollingInfo xdmDbFetchPollingRow(long j) throws XDBUserDBException {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_POLLING_ORIGINAL_URL, "url", XDBInterface.XDM_SQL_DB_POLLING_PERIODUNIT, XDBInterface.XDM_SQL_DB_POLLING_PERIOD, XDBInterface.XDM_SQL_DB_POLLING_TIME, XDBInterface.XDM_SQL_DB_POLLING_RANGE, XDBInterface.XDM_SQL_DB_POLLING_CYCLEOFDEVICEHEARTBEAT, XDBInterface.XDM_SQL_DB_POLLING_SERVICEURL, XDBInterface.XDM_SQL_DB_POLLING_NEXTPOLLINGTIME, XDBInterface.XDM_SQL_DB_POLLING_NEXTREPORTTIME, XDBInterface.XDM_SQL_DB_POLLING_VERSIONFILENAME};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        PollingInfo pollingInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_POLLING_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
                    pollingInfo = new PollingInfo();
                    pollingInfo.setOriginPreUrl(query.getString(1));
                    pollingInfo.setPreUrl(query.getString(2));
                    pollingInfo.setPeriodUnit(query.getString(3));
                    pollingInfo.setPeriod(query.getInt(4));
                    pollingInfo.setTime(query.getInt(5));
                    pollingInfo.setRange(query.getInt(6));
                    pollingInfo.setHeartBeatPeriod(query.getInt(7));
                    pollingInfo.setHeartBeatUrl(query.getString(8));
                    pollingInfo.setNextPollingTime(query.getLong(9));
                    pollingInfo.setNextHeartBeatTime(query.getLong(10));
                    pollingInfo.setVersionFileName(query.getString(11));
                }
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return pollingInfo;
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

    public static void xdmDbUpdatePollingRow(long j, PollingInfo pollingInfo) throws XDBUserDBException {
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_ORIGINAL_URL, pollingInfo.getOriginPreUrl());
            contentValues.put("url", pollingInfo.getPreUrl());
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_PERIODUNIT, pollingInfo.getPeriodUnit());
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_PERIOD, Integer.valueOf(pollingInfo.getPeriod()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_TIME, Integer.valueOf(pollingInfo.getTime()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_RANGE, Integer.valueOf(pollingInfo.getRange()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_CYCLEOFDEVICEHEARTBEAT, Integer.valueOf(pollingInfo.getHeartBeatPeriod()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_SERVICEURL, pollingInfo.getHeartBeatUrl());
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_NEXTPOLLINGTIME, Long.valueOf(pollingInfo.getNextPollingTime()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_NEXTREPORTTIME, Long.valueOf(pollingInfo.getNextHeartBeatTime()));
            contentValues.put(XDBInterface.XDM_SQL_DB_POLLING_VERSIONFILENAME, pollingInfo.getVersionFileName());
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_POLLING_TABLE, contentValues, "rowid=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public static boolean xdmDbExistsPollingRow(long j) {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_POLLING_ORIGINAL_URL, "url", XDBInterface.XDM_SQL_DB_POLLING_PERIODUNIT, XDBInterface.XDM_SQL_DB_POLLING_PERIOD, XDBInterface.XDM_SQL_DB_POLLING_TIME, XDBInterface.XDM_SQL_DB_POLLING_RANGE, XDBInterface.XDM_SQL_DB_POLLING_CYCLEOFDEVICEHEARTBEAT, XDBInterface.XDM_SQL_DB_POLLING_SERVICEURL, XDBInterface.XDM_SQL_DB_POLLING_NEXTPOLLINGTIME, XDBInterface.XDM_SQL_DB_POLLING_NEXTREPORTTIME, XDBInterface.XDM_SQL_DB_POLLING_VERSIONFILENAME};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_POLLING_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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

    public static void xdmDbInsertAccXListNodeRow(XDBAccXNodeInfo xDBAccXNodeInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_ACCXLISTNODE_ACCOUNT, xDBAccXNodeInfo.m_szAccount);
            contentValues.put(XDBInterface.XDM_SQL_DB_ACCXLISTNODE_APPADDR, xDBAccXNodeInfo.m_szAppAddr);
            contentValues.put(XDBInterface.XDM_SQL_DB_ACCXLISTNODE_APPADDRPORT, xDBAccXNodeInfo.m_szAppAddrPort);
            contentValues.put(XDBInterface.XDM_SQL_DB_ACCXLISTNODE_CLIENTAPPAUTH, xDBAccXNodeInfo.m_szClientAppAuth);
            contentValues.put(XDBInterface.XDM_SQL_DB_ACCXLISTNODE_SERVERAPPAUTH, xDBAccXNodeInfo.m_szServerAppAuth);
            contentValues.put(XDBInterface.XDM_SQL_DB_ACCXLISTNODE_TOCONREF, xDBAccXNodeInfo.m_szToConRef);
            xdmDbGetWritableDatabase.insert(XDBInterface.XDB_ACCXLISTNODE_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
    }

    /* JADX INFO: finally extract failed */
    public static boolean xdmDbExistsAccXListNodeRow(long j) {
        Throwable th;
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_ACCXLISTNODE_ACCOUNT, XDBInterface.XDM_SQL_DB_ACCXLISTNODE_APPADDR, XDBInterface.XDM_SQL_DB_ACCXLISTNODE_APPADDRPORT, XDBInterface.XDM_SQL_DB_ACCXLISTNODE_CLIENTAPPAUTH, XDBInterface.XDM_SQL_DB_ACCXLISTNODE_SERVERAPPAUTH, XDBInterface.XDM_SQL_DB_ACCXLISTNODE_TOCONREF};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_ACCXLISTNODE_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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

    public static long xdmDbInsertResyncModeRow(XDBResyncModeInfo xDBResyncModeInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_RESYNCMODE_NONCERESYNCMODE, Boolean.valueOf(xDBResyncModeInfo.nNoceResyncMode));
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_RESYNCMODE_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x006c, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x006d, code lost:
        if (r13 != null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0077, code lost:
        throw r1;
     */
    public static XDBResyncModeInfo xdmDbFetchResyncModeRow(long j) throws XDBUserDBException {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_RESYNCMODE_NONCERESYNCMODE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBResyncModeInfo xDBResyncModeInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_RESYNCMODE_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return null;
            }
            if (query.getCount() > 0 && query.moveToFirst()) {
                xDBResyncModeInfo = new XDBResyncModeInfo();
                if (query.getInt(1) == 0) {
                    xDBResyncModeInfo.nNoceResyncMode = false;
                } else {
                    xDBResyncModeInfo.nNoceResyncMode = true;
                }
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return xDBResyncModeInfo;
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
                throw new XDBUserDBException(1);
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r14.addSuppressed(th2);
        }
    }

    public static void xdmDbUpdateResyncModeRow(long j, XDBResyncModeInfo xDBResyncModeInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_RESYNCMODE_NONCERESYNCMODE, Boolean.valueOf(xDBResyncModeInfo.nNoceResyncMode));
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_RESYNCMODE_TABLE, contentValues, "rowid=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0057, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0058, code lost:
        if (r12 != null) goto L_0x005a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r12.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0062, code lost:
        throw r2;
     */
    public static boolean xdmDbExistsResyncModeRow(long j) {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_RESYNCMODE_NONCERESYNCMODE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_RESYNCMODE_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return false;
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r13.addSuppressed(th2);
        }
    }

    public static long xdmDbSqlAgentInfoInsertRow(XDBAgentInfo xDBAgentInfo) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        long j = -1;
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return -1;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_AGENT_INFO_AGENT_TYPE, Integer.valueOf(xDBAgentInfo.m_nAgentType));
            j = xdmDbGetWritableDatabase.insert(XDBInterface.XDB_DM_AGENT_INFO_TABLE, (String) null, contentValues);
        } catch (SQLException e) {
            Log.E(e.toString());
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
        XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        return j;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0066, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0067, code lost:
        if (r13 != null) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0071, code lost:
        throw r1;
     */
    public static XDBAgentInfo xdmDbSqlAgentInfoFetchRow(long j) throws XDBUserDBException {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_AGENT_INFO_AGENT_TYPE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        XDBAgentInfo xDBAgentInfo = null;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return null;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_DM_AGENT_INFO_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return null;
            }
            if (query.getCount() > 0 && query.moveToFirst()) {
                xDBAgentInfo = new XDBAgentInfo();
                xDBAgentInfo.m_nAgentType = query.getInt(1);
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return xDBAgentInfo;
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
                throw new XDBUserDBException(1);
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r14.addSuppressed(th2);
        }
    }

    public static void xdmDbSqlAgentInfoUpdateRow(long j, XDBAgentInfo xDBAgentInfo) throws XDBUserDBException {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase xdmDbGetWritableDatabase = XDMDbManager.xdmDbGetWritableDatabase();
        if (xdmDbGetWritableDatabase == null) {
            Log.E("db is null");
            return;
        }
        try {
            contentValues.put(XDBInterface.XDM_SQL_DB_AGENT_INFO_AGENT_TYPE, Integer.valueOf(xDBAgentInfo.m_nAgentType));
            xdmDbGetWritableDatabase.update(XDBInterface.XDB_DM_AGENT_INFO_TABLE, contentValues, "rowid=" + j, (String[]) null);
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
        } catch (SQLException e) {
            Log.E(e.toString());
            throw new XDBUserDBException(1);
        } catch (Throwable th) {
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetWritableDatabase);
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0057, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0058, code lost:
        if (r12 != null) goto L_0x005a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r12.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0062, code lost:
        throw r2;
     */
    public static boolean xdmDbSqlAgentInfoExistsRow(long j) {
        String[] strArr = {XDBInterface.XDM_SQL_DB_ROWID, XDBInterface.XDM_SQL_DB_AGENT_INFO_AGENT_TYPE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return false;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_DM_AGENT_INFO_TABLE, strArr, "rowid=" + j, (String[]) null, (String) null, (String) null, (String) null, (String) null);
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
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return false;
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r13.addSuppressed(th2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004a, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004b, code lost:
        if (r1 != null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0055, code lost:
        throw r3;
     */
    public static int xdmDbsqlGetFUMOStatus() {
        String[] strArr = {"status"};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        int i = 0;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return 0;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_FUMO_TABLE, strArr, "rowid=1", (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return 0;
            }
            if (query.getCount() > 0 && query.moveToFirst()) {
                i = query.getInt(0);
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            Log.I("xdbsqlGetFUMOStatus : " + i);
            return i;
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r2.addSuppressed(th2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004a, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004b, code lost:
        if (r1 != null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0055, code lost:
        throw r3;
     */
    public static int xdmdbsqlGetFumoInitType() {
        String[] strArr = {XDBInterface.XDM_SQL_DB_FUMO_INITIATED_TYPE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        int i = 0;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return 0;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_FUMO_TABLE, strArr, "rowid=1", (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return 0;
            }
            if (query.getCount() > 0 && query.moveToFirst()) {
                i = query.getInt(0);
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return i;
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r2.addSuppressed(th2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004a, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004b, code lost:
        if (r1 != null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0055, code lost:
        throw r3;
     */
    public static int xdmdbsqlGetNotiSaveState() {
        String[] strArr = {XDBInterface.XDM_SQL_DB_PROFILELIST_NOTIRESUMESTATE_SESSIONSAVESTATE};
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        int i = 0;
        if (xdmDbGetReadableDatabase == null) {
            Log.E("db is null");
            return 0;
        }
        try {
            Cursor query = xdmDbGetReadableDatabase.query(true, XDBInterface.XDB_PROFILELIST_TABLE, strArr, "rowid=1", (String[]) null, (String) null, (String) null, (String) null, (String) null);
            if (query == null) {
                Log.E("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return 0;
            }
            if (query.getCount() > 0 && query.moveToFirst()) {
                i = query.getInt(0);
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return i;
        } catch (SQLException e) {
            try {
                Log.E(e.toString());
            } catch (Throwable th) {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                throw th;
            }
        } catch (Throwable th2) {
            r2.addSuppressed(th2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004b, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004c, code lost:
        if (r14 != null) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        r14.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0056, code lost:
        throw r1;
     */
    public static boolean xdmdbsqlGetExistsTable(String str) {
        SQLiteDatabase xdmDbGetReadableDatabase = XDMDbManager.xdmDbGetReadableDatabase();
        if (xdmDbGetReadableDatabase == null) {
            Log.I("db is null");
            return false;
        }
        try {
            boolean z = true;
            SQLiteDatabase sQLiteDatabase = xdmDbGetReadableDatabase;
            Cursor query = sQLiteDatabase.query(true, "sqlite_master", new String[]{"tbl_name"}, "tbl_name = ?", new String[]{str}, (String) null, (String) null, (String) null, (String) null, (CancellationSignal) null);
            if (query == null) {
                Log.I("cursor is null");
                if (query != null) {
                    query.close();
                }
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
                return false;
            }
            if (query.getCount() <= 0) {
                z = false;
            }
            if (query != null) {
                query.close();
            }
            XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            return z;
        } catch (Exception e) {
            try {
                Log.E(e.toString());
                return false;
            } finally {
                XDMDbManager.xdmDbCloseSQLiteDatabase(xdmDbGetReadableDatabase);
            }
        } catch (Throwable th) {
            r0.addSuppressed(th);
        }
    }
}
