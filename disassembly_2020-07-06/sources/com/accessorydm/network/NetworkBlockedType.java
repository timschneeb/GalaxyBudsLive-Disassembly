package com.accessorydm.network;

import com.accessorydm.eng.core.XDMEvent;
import com.accessorydm.interfaces.XUIEventInterface;
import com.accessorydm.ui.XUIAdapter;
import com.accessorydm.ui.handler.XDMToastHandler;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.OperatorUtil;

public enum NetworkBlockedType {
    NO_BLOCKING {
        public boolean isBlocked() {
            return false;
        }
    },
    ROAMING {
        public void networkOperation(ShowUiType showUiType) {
            Log.I("1-1 ROAMING : " + showUiType);
            XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.DM_UIEVENT.XUI_DM_ROAMING_WIFI_DISCONNECTED);
        }
    },
    WIFI_DISCONNECTED {
        public void networkOperation(ShowUiType showUiType) {
            Log.I("1-2 WIFI_DISCONNECTED : " + showUiType);
            int i = AnonymousClass5.$SwitchMap$com$accessorydm$network$NetworkBlockedType$ShowUiType[showUiType.ordinal()];
            if (i == 1) {
                XDMToastHandler.xdmShowToast(OperatorUtil.replaceToWLAN(XUIAdapter.xuiAdpGetStrNetworkDisable()), 0);
            } else if (i == 2) {
                XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.DL_UIEVENT.XUI_DL_DOWNLOAD_FAILED_WIFI_DISCONNECTED);
            }
        }
    },
    NETWORK_DISCONNECTED {
        public void networkOperation(ShowUiType showUiType) {
            Log.I("1-3 NETWORK_DISCONNECTED : " + showUiType);
            int i = AnonymousClass5.$SwitchMap$com$accessorydm$network$NetworkBlockedType$ShowUiType[showUiType.ordinal()];
            if (i == 1) {
                XDMToastHandler.xdmShowToast(OperatorUtil.replaceToWLAN(XUIAdapter.xuiAdpGetStrNetworkDisable()), 0);
            } else if (i == 2) {
                XDMEvent.XDMSetEvent((Object) null, XUIEventInterface.DL_UIEVENT.XUI_DL_DOWNLOAD_FAILED_NETWORK_DISCONNECTED);
            }
        }
    };

    public enum ShowUiType {
        GENERAL_NETWORK_UI_BLOCK,
        DOWNLOAD_NETWORK_UI_BLOCK
    }

    public boolean isBlocked() {
        return true;
    }

    public void networkOperation(ShowUiType showUiType) {
    }

    /* renamed from: com.accessorydm.network.NetworkBlockedType$5  reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$accessorydm$network$NetworkBlockedType$ShowUiType = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            $SwitchMap$com$accessorydm$network$NetworkBlockedType$ShowUiType = new int[ShowUiType.values().length];
            $SwitchMap$com$accessorydm$network$NetworkBlockedType$ShowUiType[ShowUiType.GENERAL_NETWORK_UI_BLOCK.ordinal()] = 1;
            $SwitchMap$com$accessorydm$network$NetworkBlockedType$ShowUiType[ShowUiType.DOWNLOAD_NETWORK_UI_BLOCK.ordinal()] = 2;
        }
    }
}
