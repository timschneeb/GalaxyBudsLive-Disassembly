package com.accessorydm.ui.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.db.file.XDBFumoAdp;
import com.accessorydm.interfaces.XUIEventInterface;
import com.accessorydm.ui.checkingforupdate.XUICheckingForUpdateActivity;
import com.accessorydm.ui.dialog.XUIDialog;
import com.accessorydm.ui.installconfirm.XUIInstallConfirmActivity;
import com.accessorydm.ui.noupdatable.XUINoUpdatableVersionActivity;
import com.accessorydm.ui.progress.XUIProgressModel;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.type.DeviceType;

public class XDMServiceHandler {
    private static final XDMServiceHandler dmEventHandler = new XDMServiceHandler();
    private final Handler dmHandler;

    private XDMServiceHandler() {
        HandlerThread handlerThread = new HandlerThread("XDMServiceHandler");
        handlerThread.setDaemon(true);
        handlerThread.start();
        this.dmHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            public final boolean handleMessage(Message message) {
                return XDMServiceHandler.this.lambda$new$0$XDMServiceHandler(message);
            }
        });
    }

    public /* synthetic */ boolean lambda$new$0$XDMServiceHandler(Message message) {
        if (message.obj instanceof XUIEventInterface.DM_UIEVENT) {
            xdmDMHandlerMessage(((XUIEventInterface.DM_UIEVENT) message.obj).ordinal());
            return true;
        } else if (message.obj instanceof XUIEventInterface.DL_UIEVENT) {
            xdmDLHandlerMessage(((XUIEventInterface.DL_UIEVENT) message.obj).ordinal());
            return true;
        } else if (!(message.obj instanceof XUIDialog)) {
            return true;
        } else {
            xdmDialogHandlerMessage(((XUIDialog) message.obj).ordinal());
            return true;
        }
    }

    private void xdmDialogHandlerMessage(int i) {
        Log.I("");
        try {
            XUIDialog valueOf = XUIDialog.valueOf(i);
            Log.I("xdmDialogHandlerMessage : " + valueOf + "(" + valueOf.ordinal() + ")");
            XDMDmUtils.getInstance().xdmCallUiDialogActivity(valueOf.ordinal());
            this.dmHandler.removeMessages(valueOf.ordinal());
        } catch (Exception e) {
            Log.E(e.toString());
        }
    }

    private void xdmDLHandlerMessage(int i) {
        Log.I("");
        try {
            XUIEventInterface.DL_UIEVENT valueOf = XUIEventInterface.DL_UIEVENT.valueOf(i);
            Log.I("xdmDLHandlerMessage : " + valueOf + "(" + valueOf.ordinal() + ")");
            int i2 = AnonymousClass1.$SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DL_UIEVENT[valueOf.ordinal()];
            if (i2 == 1) {
                XDMDmUtils.getInstance().callActivity(DeviceType.get().getDownloadConfirmActivity());
            } else if (i2 == 2) {
                int xdbGetFUMOStatus = XDBFumoAdp.xdbGetFUMOStatus();
                if (xdbGetFUMOStatus == 30 || xdbGetFUMOStatus == 200) {
                    Log.I("in download progress, so show activity");
                    XDMDmUtils.getInstance().xdmCallUiDownloadProgressActivity();
                } else {
                    Log.W("not in download progress, not show activity");
                }
            } else if (i2 != 3) {
                if (i2 == 4) {
                    XDMDmUtils.getInstance().callActivity(XUIInstallConfirmActivity.class);
                    XUIProgressModel.getInstance().initializeProgress();
                }
            } else if (XDBFumoAdp.xdbGetFUMOStatus() != 250) {
                Log.W("not in copy progress, not show activity");
            } else {
                Log.I("in copy progress, so show activity");
                XDMDmUtils.getInstance().xdmCallUiCopyProgressActivity();
            }
            this.dmHandler.removeMessages(valueOf.ordinal());
        } catch (Exception e) {
            Log.E(e.toString());
        }
    }

    private void xdmDMHandlerMessage(int i) {
        Log.I("");
        try {
            XUIEventInterface.DM_UIEVENT valueOf = XUIEventInterface.DM_UIEVENT.valueOf(i);
            Log.I("xdmDMHandlerMessage : " + valueOf + "(" + valueOf.ordinal() + ")");
            int i2 = AnonymousClass1.$SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DM_UIEVENT[valueOf.ordinal()];
            if (i2 == 1) {
                XDMDmUtils.getInstance().callActivity(XUICheckingForUpdateActivity.class);
            } else if (i2 == 2) {
                XDMDmUtils.getInstance().callActivity(XUINoUpdatableVersionActivity.class);
            }
            this.dmHandler.removeMessages(valueOf.ordinal());
        } catch (Exception e) {
            Log.E(e.toString());
        }
    }

    /* renamed from: com.accessorydm.ui.handler.XDMServiceHandler$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DL_UIEVENT = new int[XUIEventInterface.DL_UIEVENT.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DM_UIEVENT = new int[XUIEventInterface.DM_UIEVENT.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(15:0|(2:1|2)|3|5|6|7|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|5|6|7|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0032 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x003c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0047 */
        static {
            try {
                $SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DM_UIEVENT[XUIEventInterface.DM_UIEVENT.XUI_DM_CHECKING_FOR_UPDATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DM_UIEVENT[XUIEventInterface.DM_UIEVENT.XUI_DM_NO_UPDATABLE_VERSION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DL_UIEVENT[XUIEventInterface.DL_UIEVENT.XUI_DL_DOWNLOAD_START_CONFIRM.ordinal()] = 1;
            $SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DL_UIEVENT[XUIEventInterface.DL_UIEVENT.XUI_DL_DOWNLOAD_IN_PROGRESS.ordinal()] = 2;
            $SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DL_UIEVENT[XUIEventInterface.DL_UIEVENT.XUI_DL_COPY_IN_PROGRESS.ordinal()] = 3;
            try {
                $SwitchMap$com$accessorydm$interfaces$XUIEventInterface$DL_UIEVENT[XUIEventInterface.DL_UIEVENT.XUI_DL_UPDATE_CONFIRM.ordinal()] = 4;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static void xdmSendMessageDmHandler(Object obj) {
        Handler handler = dmEventHandler.dmHandler;
        if (handler != null) {
            Message obtainMessage = handler.obtainMessage();
            obtainMessage.obj = obj;
            dmEventHandler.dmHandler.sendMessage(obtainMessage);
            return;
        }
        Log.I("m_hDmHandler is null!!");
    }
}
