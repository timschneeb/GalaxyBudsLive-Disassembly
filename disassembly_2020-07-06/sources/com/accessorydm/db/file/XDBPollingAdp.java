package com.accessorydm.db.file;

import com.accessorydm.db.sql.XDMDbSqlQuery;
import com.samsung.android.fotaagent.polling.PollingInfo;
import com.samsung.android.fotaprovider.log.Log;

public class XDBPollingAdp {
    /* JADX WARNING: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001e  */
    public static PollingInfo xdbGetPollingInfo() {
        PollingInfo pollingInfo;
        try {
            pollingInfo = XDMDbSqlQuery.xdmDbFetchPollingRow(1);
        } catch (XDBUserDBException e) {
            Log.E(e.toString());
            e.failHandling();
            pollingInfo = null;
            if (pollingInfo != null) {
            }
        } catch (Exception e2) {
            Log.E(e2.toString());
            pollingInfo = null;
            if (pollingInfo != null) {
            }
        }
        if (pollingInfo != null) {
            return pollingInfo;
        }
        Log.W("pollingInfo is null, return default");
        return new PollingInfo();
    }

    public static void xdbSetPollingInfo(PollingInfo pollingInfo) {
        try {
            XDMDbSqlQuery.xdmDbUpdatePollingRow(1, pollingInfo);
        } catch (XDBUserDBException e) {
            Log.E(e.toString());
            e.failHandling();
        } catch (Exception e2) {
            Log.E(e2.toString());
        }
    }

    public static long xdbGetNextPollingTime() {
        try {
            return xdbGetPollingInfo().getNextPollingTime();
        } catch (Exception e) {
            Log.E(e.toString());
            return 0;
        }
    }

    public static void xdbSetNextPollingTime(long j) {
        try {
            PollingInfo xdbGetPollingInfo = xdbGetPollingInfo();
            xdbGetPollingInfo.setNextPollingTime(j);
            xdbSetPollingInfo(xdbGetPollingInfo);
        } catch (Exception e) {
            Log.E(e.toString());
        }
    }
}
