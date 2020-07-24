package com.accessorydm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import com.accessorydm.agent.XDMAgent;
import com.accessorydm.agent.XDMTask;
import com.accessorydm.agent.XDMUITask;
import com.accessorydm.db.file.XDBFactoryBootstrap;
import com.accessorydm.interfaces.XCommonInterface;
import com.accessorydm.ui.UIManager;
import com.accessorydm.ui.dialog.XUIDialog;
import com.accessorydm.ui.dialog.XUIDialogActivity;
import com.accessorydm.ui.progress.XUIProgressActivity;
import com.samsung.android.fotaprovider.FotaProviderInitializer;
import com.samsung.android.fotaprovider.log.Log;

public enum XDMDmUtils {
    INSTANCE;
    
    public boolean XDM_ROAMING_CHECK;
    public boolean XDM_VALIDATION_CHECK;
    public XDMTask g_Task;
    public XDMUITask g_UITask;
    private int g_nResumeStatus;
    private PowerManager.WakeLock m_WakeLock;
    private WifiManager.WifiLock m_WifiLock;
    private int m_nWaitWifiConnectMode;

    public static XDMDmUtils getInstance() {
        return INSTANCE;
    }

    public void xdmTaskInit() {
        if (this.g_Task == null) {
            this.g_Task = new XDMTask();
        }
        if (this.g_UITask == null) {
            this.g_UITask = new XDMUITask();
        }
    }

    public void xdmCallUiDialogActivity(int i) {
        if (i == XUIDialog.NONE.ordinal()) {
            Log.W("wrong dialog id");
            return;
        }
        Log.I("UI_id:" + XUIDialog.valueOf(i));
        UIManager.getInstance().finishAllActivitiesExcept(XUIDialogActivity.class.getName());
        startActivityWithCommonFlag(new Intent(String.valueOf(i), (Uri) null, getContext(), XUIDialogActivity.class));
    }

    public void callActivity(Class<?> cls) {
        Log.I("");
        startActivityWithCommonFlag(new Intent(getContext(), cls));
        UIManager.getInstance().finishAllActivitiesExcept(cls.getName());
    }

    public void xdmCallUiDownloadProgressActivity() {
        Log.I("");
        Intent intent = new Intent(getContext(), XUIProgressActivity.class);
        intent.putExtra("progressMode", 1);
        startActivityWithCommonFlag(intent);
        UIManager.getInstance().finishAllActivitiesExcept(XUIProgressActivity.class.getName());
    }

    public void xdmCallUiCopyProgressActivity() {
        Log.I("");
        Intent intent = new Intent(FotaProviderInitializer.getContext(), XUIProgressActivity.class);
        intent.putExtra("progressMode", 2);
        startActivityWithCommonFlag(intent);
        UIManager.getInstance().finishAllActivitiesExcept(XUIProgressActivity.class.getName());
    }

    private void startActivityWithCommonFlag(Intent intent) {
        intent.setFlags(872415232);
        getContext().startActivity(intent);
    }

    public void xdmRegisterFactoryBootstrap() {
        XDMAgent.xdmAgentSaveBootstrapDateToFFS(XDBFactoryBootstrap.xdbFBGetFactoryBootstrapData(1));
    }

    public void xdmWakeLockAcquire(String str) {
        Log.I("");
        try {
            if (this.m_WakeLock == null) {
                Log.I("m_WakeLock is acquire!!");
                PowerManager powerManager = (PowerManager) xdmGetServiceManager("power");
                if (powerManager == null) {
                    Log.E("PowerManager is null!!");
                    return;
                }
                this.m_WakeLock = powerManager.newWakeLock(1, str);
                this.m_WakeLock.setReferenceCounted(false);
                this.m_WakeLock.acquire(XCommonInterface.WAKE_LOCK_TIMEOUT);
            }
        } catch (Exception e) {
            Log.E(e.toString());
        }
    }

    public void xdmWakeLockRelease() {
        Log.I("");
        try {
            if (this.m_WakeLock != null) {
                Log.I("m_WakeLock is release!!");
                this.m_WakeLock.release();
                this.m_WakeLock = null;
            }
        } catch (Exception e) {
            Log.E(e.toString());
        }
    }

    public void xdmWifiLockAcquire(String str) {
        if (this.m_WifiLock == null) {
            WifiManager wifiManager = (WifiManager) getInstance().xdmGetServiceManager("wifi");
            if (wifiManager == null) {
                Log.E("WifiManager is null!!");
                return;
            }
            this.m_WifiLock = wifiManager.createWifiLock(str);
            this.m_WifiLock.setReferenceCounted(false);
            this.m_WifiLock.acquire();
        }
    }

    public void xdmWifiLockRelease() {
        Log.I("");
        try {
            if (this.m_WifiLock != null) {
                Log.I("m_WifiLock is release!!");
                this.m_WifiLock.release();
                this.m_WifiLock = null;
            }
        } catch (Exception e) {
            Log.E(e.toString());
        }
    }

    public Object xdmGetServiceManager(String str) {
        Object obj = null;
        try {
            obj = getContext().getSystemService(str);
            if (obj == null) {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.E(e.toString());
                    }
                    Log.I(str + " is null, retry...");
                    obj = getContext().getSystemService(str);
                    if (obj != null) {
                        break;
                    }
                }
            }
        } catch (Exception e2) {
            Log.E(e2.toString());
        }
        return obj;
    }

    public int xdmGetResumeStatus() {
        return this.g_nResumeStatus;
    }

    public void xdmSetResumeStatus(int i) {
        this.g_nResumeStatus = i;
    }

    public int xdmGetWaitWifiConnectMode() {
        return this.m_nWaitWifiConnectMode;
    }

    public void xdmSetWaitWifiConnectMode(int i) {
        Log.I("WaitWifiConnectMode = " + i);
        this.m_nWaitWifiConnectMode = i;
    }

    public String xdmGetAccessorydmPath() {
        return getContext().getApplicationInfo().dataDir + "/";
    }

    public static Context getContext() {
        return FotaProviderInitializer.getContext();
    }
}
