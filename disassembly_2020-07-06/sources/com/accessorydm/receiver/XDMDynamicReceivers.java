package com.accessorydm.receiver;

import android.content.Context;
import android.content.IntentFilter;
import android.util.ArrayMap;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.dmstarter.XDMInitExecutor;
import com.accessorydm.dmstarter.XDMSessionStarter;
import com.accessorydm.interfaces.XCommonInterface;
import com.accessorydm.resume.XDMResumeStarter;
import com.samsung.android.fotaprovider.FotaProviderInitializer;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.NetworkUtil;

public class XDMDynamicReceivers {
    private static final XDMDynamicReceivers instance = new XDMDynamicReceivers();
    private ReceiverState receiverState = ReceiverState.UNREGISTERED;
    private final ArrayMap<Class<ReceiverWithIntentFilter>, ReceiverWithIntentFilter> receivers = new ArrayMap<>();

    enum ReceiverState {
        UNREGISTERED,
        PARTIALLY_REGISTERED,
        REGISTERED
    }

    private XDMDynamicReceivers() {
    }

    public static XDMDynamicReceivers getInstance() {
        return instance;
    }

    public synchronized void xdmRegisterReceiver() {
        Log.I("xdmRegisterReceiver");
        this.receiverState = ReceiverState.REGISTERED;
        for (Class cls : getClass().getDeclaredClasses()) {
            if (cls.isAnnotationPresent(ReceiverToBeRegistered.class)) {
                if (!ReceiverWithIntentFilter.class.isAssignableFrom(cls)) {
                    Log.W("Annotation attached to unsupported class: " + cls);
                } else if (this.receivers.get(cls) != null) {
                    Log.W("already registered: " + cls);
                } else {
                    try {
                        ReceiverWithIntentFilter receiverWithIntentFilter = (ReceiverWithIntentFilter) cls.newInstance();
                        getContext().registerReceiver(receiverWithIntentFilter, receiverWithIntentFilter.getIntentFilter());
                        this.receivers.put(cls, receiverWithIntentFilter);
                    } catch (Exception e) {
                        Log.E(e.getMessage());
                        this.receiverState = ReceiverState.PARTIALLY_REGISTERED;
                    }
                }
            }
        }
    }

    public synchronized void xdmUnregisterReceiver() {
        Log.I("xdmUnregisterReceiver");
        this.receiverState = ReceiverState.UNREGISTERED;
        for (ReceiverWithIntentFilter next : this.receivers.values()) {
            if (next == null) {
                Log.W("receiver is null");
            } else {
                getContext().unregisterReceiver(next);
            }
        }
        this.receivers.clear();
    }

    public synchronized boolean xdmIsRegisterReceiver() {
        Log.I("xdmIsRegisterReceiver: " + this.receiverState);
        return this.receiverState == ReceiverState.REGISTERED;
    }

    @ReceiverToBeRegistered
    static class ConnectivityChangeReceiver extends ReceiverWithIntentFilter {
        ConnectivityChangeReceiver() {
        }

        /* access modifiers changed from: package-private */
        public void doWork() {
            if (!NetworkUtil.isAnyNetworkConnected(XDMDynamicReceivers.getContext())) {
                return;
            }
            if (!XDMInitExecutor.getInstance().isDmInitializedSuccessfully()) {
                Log.I("----------- NetworkChange DM_INIT ok");
                XDMSessionStarter.forInitiateType(XCommonInterface.INIT_TYPE.INIT_TYPE_PULL).dmInitExecute();
            } else if (XDMDmUtils.getInstance().xdmGetResumeStatus() != 0) {
                Log.I("Run Intent ResumeStatus Operation");
                XDMResumeStarter.INTENT_RESUME.resumeExecute();
            } else if (XDMDmUtils.getInstance().xdmGetWaitWifiConnectMode() != 0) {
                Log.I("Run Intent WifiConnectMode Operation");
                XDMResumeStarter.INTENT_RESUME.resumeExecute();
            }
        }

        /* access modifiers changed from: package-private */
        public IntentFilter getIntentFilter() {
            return new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        }
    }

    @ReceiverToBeRegistered
    static class WifiOnlyReceiver extends ReceiverWithIntentFilter {
        WifiOnlyReceiver() {
        }

        /* access modifiers changed from: package-private */
        public void doWork() {
            if (XCommonInterface.XCOMMON_INTENT_WIFIONLY_FROM_FC.equals(getAction())) {
                int intExtra = getIntent().getIntExtra("changed_data", 0);
                Log.I("sec.fotaprovider.intent.WIFIONLY: wifi only = " + intExtra);
                if (intExtra != 1) {
                    if (!XDMInitExecutor.getInstance().isDmInitializedSuccessfully()) {
                        Log.E("DM Not Init. return");
                    } else if (XDMDmUtils.getInstance().xdmGetResumeStatus() != 0) {
                        Log.I("Run Resume Operation, Resume call Wi-Fi only Changed");
                        XDMResumeStarter.INTENT_RESUME.resumeExecute();
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public IntentFilter getIntentFilter() {
            return new IntentFilter(XCommonInterface.XCOMMON_INTENT_WIFIONLY_FROM_FC);
        }
    }

    /* access modifiers changed from: private */
    public static Context getContext() {
        return FotaProviderInitializer.getContext();
    }
}
