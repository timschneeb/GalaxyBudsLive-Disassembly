package com.accessorydm.agent;

import android.text.TextUtils;
import com.accessorydm.adapter.XDMCommonUtils;
import com.accessorydm.adapter.XDMDevinfAdapter;
import com.accessorydm.db.file.XDB;
import com.accessorydm.db.file.XDBAgentAdp;
import com.accessorydm.db.file.XDBFumoAdp;
import com.accessorydm.db.file.XDBPostPoneAdp;
import com.accessorydm.db.file.XDBProfileAdp;
import com.accessorydm.db.file.XDBProfileInfo;
import com.accessorydm.db.file.XDBProfileListAdp;
import com.accessorydm.db.file.XDBUrlInfo;
import com.accessorydm.eng.core.XDMAccXNode;
import com.accessorydm.eng.core.XDMAuth;
import com.accessorydm.eng.core.XDMBase64;
import com.accessorydm.eng.core.XDMDDFXmlHandler;
import com.accessorydm.eng.core.XDMEncoder;
import com.accessorydm.eng.core.XDMHmacData;
import com.accessorydm.eng.core.XDMLinkedList;
import com.accessorydm.eng.core.XDMList;
import com.accessorydm.eng.core.XDMMem;
import com.accessorydm.eng.core.XDMMsg;
import com.accessorydm.eng.core.XDMOmAcl;
import com.accessorydm.eng.core.XDMOmLib;
import com.accessorydm.eng.core.XDMOmList;
import com.accessorydm.eng.core.XDMOmTree;
import com.accessorydm.eng.core.XDMOmTreeException;
import com.accessorydm.eng.core.XDMOmVfs;
import com.accessorydm.eng.core.XDMUic;
import com.accessorydm.eng.core.XDMVnode;
import com.accessorydm.eng.core.XDMWbxmlEncoder;
import com.accessorydm.eng.core.XDMWorkspace;
import com.accessorydm.eng.parser.XDMParser;
import com.accessorydm.eng.parser.XDMParserAdd;
import com.accessorydm.eng.parser.XDMParserAlert;
import com.accessorydm.eng.parser.XDMParserAtomic;
import com.accessorydm.eng.parser.XDMParserCopy;
import com.accessorydm.eng.parser.XDMParserCred;
import com.accessorydm.eng.parser.XDMParserDelete;
import com.accessorydm.eng.parser.XDMParserExec;
import com.accessorydm.eng.parser.XDMParserGet;
import com.accessorydm.eng.parser.XDMParserItem;
import com.accessorydm.eng.parser.XDMParserPcdata;
import com.accessorydm.eng.parser.XDMParserReplace;
import com.accessorydm.eng.parser.XDMParserResults;
import com.accessorydm.eng.parser.XDMParserSequence;
import com.accessorydm.eng.parser.XDMParserStatus;
import com.accessorydm.eng.parser.XDMParserSyncheader;
import com.accessorydm.interfaces.XDMDefInterface;
import com.accessorydm.interfaces.XDMInterface;
import com.accessorydm.interfaces.XEventInterface;
import com.accessorydm.interfaces.XFOTAInterface;
import com.accessorydm.interfaces.XTPInterface;
import com.accessorydm.interfaces.XUICInterface;
import com.accessorydm.tp.XTPAdapter;
import com.accessorydm.tp.XTPHttpUtil;
import com.accessorydm.tp.urlconnect.HttpNetworkInterface;
import com.samsung.accessory.neobeanmgr.core.bluetooth.BluetoothManagerEnabler;
import com.samsung.android.fotaprovider.log.Log;
import java.nio.charset.Charset;
import java.util.Calendar;

public class XDMAgent implements XDMDefInterface, XDMInterface, XEventInterface, XFOTAInterface, XTPInterface, XUICInterface {
    private static final String DEFAULT_NONCE = "SamSungNextNonce=";
    private static final int PACKAGE_SIZE_GAP = 128;
    private static XDMWorkspace g_DmWs = null;
    private static XDMAccXNode m_DmAccXNodeInfo = null;
    private static XDMAccXNode m_DmAccXNodeTndsInfo = null;
    private static boolean m_bPendingStatus = false;
    private static int m_nChangedProtocolCount = 0;
    private static int m_nConnectRetryCount = 0;
    private static int m_nDMSync = 0;
    private static String szSvcState = "";
    public XDMParserAdd m_AddCmd;
    public XDMAgentHandler m_AgentHandler;
    public XDMParserAlert m_Alert;
    public XDMParserAtomic m_Atomic;
    public XDMParserCopy m_CopyCmd;
    public XDMParserDelete m_DeleteCmd;
    public XDMParserExec m_Exec;
    public XDMParserGet m_Get;
    public XDMParserSyncheader m_Header;
    public XTPAdapter m_HttpDMAdapter;
    public XDMParserReplace m_ReplaceCmd;
    public XDMParserSequence m_Sequence;
    public XDMParserStatus m_Status;
    public boolean m_bInProgresscmd;
    public String m_szCmd;

    public XDMAgent() {
        if (this.m_HttpDMAdapter == null) {
            this.m_HttpDMAdapter = new XTPAdapter();
        }
    }

    private static void xdmAgentInitParser(XDMWorkspace xDMWorkspace, XDMParser xDMParser) {
        xDMParser.xdmParParseInit(xDMParser, xDMWorkspace);
    }

    public XDMWorkspace xdmAgentGetWorkSpace() {
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (xDMWorkspace != null) {
            return xDMWorkspace;
        }
        Log.E("dm_ws is NULL");
        return null;
    }

    public static int xdmAgentGetSyncMode() {
        if (m_nDMSync != 0) {
            Log.I("nSync = " + m_nDMSync);
        }
        return m_nDMSync;
    }

    public static void xdmAgentSetSyncMode(int i) {
        Log.I("nSync = " + i);
        m_nDMSync = i;
    }

    private boolean xdmAgentIsAccessibleNode(String str) {
        if (!TextUtils.isEmpty(XDMMem.xdmLibStrstr(str, XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH)) || !TextUtils.isEmpty(XDMMem.xdmLibStrstr(str, XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH))) {
            return false;
        }
        String xdmDDFGetMOPath = XDMDDFXmlHandler.xdmDDFGetMOPath(10);
        if (!TextUtils.isEmpty(xdmDDFGetMOPath) && XDMMem.xdmLibStrncmp(str, xdmDDFGetMOPath, xdmDDFGetMOPath.length()) == 0) {
            return false;
        }
        return true;
    }

    private boolean xdmAgentIsPermanentNode(XDMOmTree xDMOmTree, String str) {
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, str);
        return xdmOmGetNodeProp != null && xdmOmGetNodeProp.scope == 1;
    }

    private static int xdmAgentInit() {
        g_DmWs = new XDMWorkspace();
        if (g_DmWs == null) {
            return -1;
        }
        m_DmAccXNodeInfo = new XDMAccXNode();
        return 0;
    }

    public static void xdmAgentClose() {
        XDMWorkspace xDMWorkspace = g_DmWs;
        Log.I("inDMSync = " + m_nDMSync);
        if (m_nDMSync > 0) {
            if (xDMWorkspace != null) {
                if (m_bPendingStatus) {
                    Log.I("Pending Status don't save");
                    XDMOmLib.xdmOmVfsEnd(xDMWorkspace.om.vfs);
                } else {
                    Log.I("workspace save");
                    XDMOmLib.xdmOmEnd(xDMWorkspace.om);
                }
                xDMWorkspace.xdmFreeWorkSpace();
                g_DmWs = null;
            }
            xdmAgentSetSyncMode(0);
        }
    }

    private static int xdmAgentParsingWbxml(byte[] bArr) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        xDMWorkspace.nextMsg = false;
        xDMWorkspace.endOfMsg = false;
        XDMParser xDMParser = new XDMParser(bArr);
        xdmAgentInitParser(xDMWorkspace, xDMParser);
        if (xDMParser.xdmParParse() != 0) {
            return -2;
        }
        return 0;
    }

    private int xdmAgentVerifyServerAuth(XDMParserSyncheader xDMParserSyncheader) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMParserCred xDMParserCred = xDMParserSyncheader.cred;
        XDMHmacData xDMHmacData = xDMWorkspace.recvHmacData;
        Log.I("");
        if (TextUtils.isEmpty(xDMWorkspace.m_szServerID)) {
            Log.H("ServerID is null");
            if (xDMWorkspace.serverAuthState == -7 || xDMWorkspace.serverAuthState == -9) {
                return -1;
            }
            return -9;
        }
        if (xDMWorkspace.serverCredType == 2) {
            if (xDMHmacData == null) {
                Log.H("HMAC is null");
                return -9;
            }
            Log.H("algorithm : " + xDMHmacData.m_szHmacAlgorithm);
            Log.H("digest : " + xDMHmacData.m_szHmacDigest);
            if (TextUtils.isEmpty(xDMHmacData.m_szHmacAlgorithm) || TextUtils.isEmpty(xDMHmacData.m_szHmacUserName) || TextUtils.isEmpty(xDMHmacData.m_szHmacDigest)) {
                Log.H("Any of MAC data is empty");
                return -9;
            } else if ("MD5".compareTo(xDMHmacData.m_szHmacAlgorithm) != 0) {
                Log.H("State No Credential");
                return -9;
            } else {
                Log.H("credtype:" + xDMWorkspace.serverCredType + ", nextNonce:" + new String(xDMWorkspace.serverNextNonce, Charset.defaultCharset()));
                StringBuilder sb = new StringBuilder();
                sb.append("httpContentLength:");
                sb.append(xDMHmacData.httpContentLength);
                Log.H(sb.toString());
                String xdmAuthMakeDigest = XDMAuth.xdmAuthMakeDigest(xDMWorkspace.serverCredType, xDMWorkspace.m_szServerID, xDMWorkspace.m_szServerPW, xDMWorkspace.serverNextNonce, xDMWorkspace.serverNextNonce.length, xDMWorkspace.buf.toByteArray(), xDMHmacData.httpContentLength);
                if (TextUtils.isEmpty(xdmAuthMakeDigest)) {
                    Log.H("key is null");
                    return -1;
                } else if (xdmAuthMakeDigest.compareTo(xDMHmacData.m_szHmacDigest) != 0) {
                    Log.H("key and pHMAC.m_szHmacDigest not equal");
                    return -1;
                }
            }
        } else if (xDMParserCred.meta == null) {
            return -9;
        } else {
            if (XDMAuth.xdmAuthCredString2Type(xDMParserCred.meta.m_szType) != xDMWorkspace.serverCredType) {
                Log.H("server auth type is mismatch");
                return -1;
            } else if (XDMInterface.CRED_TYPE_MD5.compareTo(xDMParserCred.meta.m_szType) == 0) {
                Log.H("CRED_TYPE_MD5 ws.serverCredType : " + xDMWorkspace.serverCredType);
                String xdmAuthMakeDigest2 = XDMAuth.xdmAuthMakeDigest(xDMWorkspace.serverCredType, xDMWorkspace.m_szServerID, xDMWorkspace.m_szServerPW, xDMWorkspace.serverNextNonce, xDMWorkspace.serverNextNonce.length, (byte[]) null, 0);
                if (TextUtils.isEmpty(xdmAuthMakeDigest2)) {
                    Log.H("key is null");
                    return -1;
                }
                Log.I("CRED_TYPE_MD5 key.compareTo(cred.data) != 0 key= " + xdmAuthMakeDigest2 + " cred.data= " + xDMParserCred.m_szData);
                if (xdmAuthMakeDigest2.compareTo(xDMParserCred.m_szData) != 0) {
                    Log.H("key.compareTo(cred.data) != 0 key= " + xdmAuthMakeDigest2 + " cred.data= " + xDMParserCred.m_szData);
                    Log.I("key and cred.data not equal");
                    return -1;
                }
            } else if (XDMInterface.CRED_TYPE_BASIC.compareTo(xDMParserCred.meta.m_szType) == 0) {
                String xdmAuthMakeDigest3 = XDMAuth.xdmAuthMakeDigest(xDMWorkspace.serverCredType, xDMWorkspace.m_szServerID, xDMWorkspace.m_szServerPW, "".getBytes(Charset.defaultCharset()), 0, (byte[]) null, 0);
                if (TextUtils.isEmpty(xdmAuthMakeDigest3)) {
                    Log.H("key is null");
                    return -1;
                } else if (xdmAuthMakeDigest3.compareTo(xDMParserCred.m_szData) != 0) {
                    Log.H("key.compareTo(cred.data) != 0 key= " + xdmAuthMakeDigest3 + " cred.data= " + xDMParserCred.m_szData);
                    Log.I("key and cred.data not equal");
                    return -1;
                }
            }
        }
        return 1;
    }

    public int xdmAgentSendPackage() {
        int i;
        int i2;
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (xDMWorkspace.credType == 0 || xDMWorkspace.credType == 1) {
            try {
                i = this.m_HttpDMAdapter.xtpAdpSetHttpObj(xDMWorkspace.m_szTargetURI, "", "", HttpNetworkInterface.XTP_HTTP_METHOD_POST, 0);
                if (i == 2) {
                    m_bPendingStatus = true;
                } else {
                    if (m_bPendingStatus) {
                        m_bPendingStatus = false;
                    }
                    try {
                        i2 = this.m_HttpDMAdapter.xtpSendPackage(xDMWorkspace.buf.toByteArray(), xDMWorkspace.buf.size(), 0);
                    } catch (Exception e) {
                        Log.E(e.toString());
                        i2 = -3;
                    }
                    if (i == 0) {
                        XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DM_CONTINUE, (Object) null, (Object) null);
                    } else if (i == -2) {
                        XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DM_CONNECTFAIL, (Object) null, (Object) null);
                    } else {
                        XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DM_SENDFAIL, (Object) null, (Object) null);
                    }
                }
            } catch (NullPointerException e2) {
                Log.E(e2.toString());
                XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DM_SENDFAIL, (Object) null, (Object) null);
                return -3;
            }
        } else {
            Log.H("1: " + xDMWorkspace.m_szUserName);
            Log.H("1: " + xDMWorkspace.m_szClientPW);
            int xdmWbxEncGetBufferSize = XDMWbxmlEncoder.xdmWbxEncGetBufferSize();
            int i3 = xDMWorkspace.credType;
            String str = xDMWorkspace.m_szUserName;
            String str2 = xDMWorkspace.m_szClientPW;
            byte[] bArr = xDMWorkspace.nextNonce;
            String xdmAuthMakeDigest = XDMAuth.xdmAuthMakeDigest(i3, str, str2, bArr, xDMWorkspace.nextNonce.length, xDMWorkspace.buf.toByteArray(), (long) xdmWbxEncGetBufferSize);
            try {
                i = this.m_HttpDMAdapter.xtpAdpSetHttpObj(xDMWorkspace.m_szTargetURI, "algorithm=MD5, username=\"" + xDMWorkspace.m_szUserName + "\", mac=" + xdmAuthMakeDigest, "", HttpNetworkInterface.XTP_HTTP_METHOD_POST, 0);
                if (i == 2) {
                    m_bPendingStatus = true;
                } else {
                    if (m_bPendingStatus) {
                        m_bPendingStatus = false;
                    }
                    try {
                        i = this.m_HttpDMAdapter.xtpSendPackage(xDMWorkspace.buf.toByteArray(), XDMEncoder.xdmEncGetBufferSize(xDMWorkspace.e), 0);
                    } catch (Exception e3) {
                        Log.E(e3.toString());
                        i = -3;
                    }
                    if (i == 0) {
                        XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DM_CONTINUE, (Object) null, (Object) null);
                    } else if (i == -2) {
                        XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DM_CONNECTFAIL, (Object) null, (Object) null);
                    } else {
                        XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DM_SENDFAIL, (Object) null, (Object) null);
                    }
                }
            } catch (NullPointerException e4) {
                Log.E(e4.toString());
                XDMMsg.xdmSendMessage(XEventInterface.XEVENT.XEVENT_DM_SENDFAIL, (Object) null, (Object) null);
                return -3;
            }
        }
        return i;
    }

    private String xdmAgentLibMakeSessionID() {
        Calendar instance = Calendar.getInstance();
        String format = String.format("%x%x", new Object[]{Integer.valueOf(String.valueOf(instance.get(12))), Integer.valueOf(instance.get(13))});
        Log.H("Make sessionid =" + format);
        return format;
    }

    public int xdmAgentStartSession() {
        Log.I("");
        if (xdmAgentInit() != 0) {
            return -1;
        }
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (XDBProfileListAdp.xdbGetNotiEvent() <= 0 || m_bPendingStatus) {
            xDMWorkspace.m_szSessionID = xdmAgentLibMakeSessionID();
        } else {
            xDMWorkspace.m_szSessionID = XDBProfileListAdp.xdbGetNotiSessionID();
        }
        try {
            if (xdmAgentMakeNode() != 0) {
                return -1;
            }
            return 0;
        } catch (XDMOmTreeException e) {
            Log.E(e.toString());
            Log.E("OmTree Delete");
            XDMOmLib.xdmOmVfsEnd(xDMWorkspace.om.vfs);
            XDMOmLib.xdmOmVfsDeleteStdobj(xDMWorkspace.om.vfs);
            XDMOmVfs.xdmOmVfsDeleteOmFile();
            return -1;
        } catch (Exception e2) {
            Log.E(e2.toString());
            return -1;
        }
    }

    private int xdmAgentMakeNode() throws XDMOmTreeException {
        XDMOmTree xDMOmTree = g_DmWs.om;
        if (XDMOmLib.xdmOmInit(xDMOmTree) != 0) {
            return -1;
        }
        XDMOmLib.xdmOmSetServerId(xDMOmTree, "*");
        xdmAgentMakeSyncMLNode();
        xdmAgentMakeDevInfoNode();
        xdmAgentMakeDevDetailNode();
        xdmAgentMakeFwUpdateNode();
        return 0;
    }

    public int xdmAgentCreatePackage() {
        int i;
        int i2;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMEncoder xDMEncoder = xDMWorkspace.e;
        if (xDMWorkspace.dmState == XDMInterface.XDMSyncMLState.XDM_STATE_INIT) {
            i = xdmAgentLoadWorkSpace();
            if (XDBFumoAdp.xdbGetFUMOInitiatedType() != 0) {
                xDMWorkspace.dmState = XDMInterface.XDMSyncMLState.XDM_STATE_GENERIC_ALERT;
            } else {
                xDMWorkspace.dmState = XDMInterface.XDMSyncMLState.XDM_STATE_CLIENT_INIT_MGMT;
            }
        } else {
            i = (xDMWorkspace.dmState == XDMInterface.XDMSyncMLState.XDM_STATE_GENERIC_ALERT_REPORT || xDMWorkspace.dmState == XDMInterface.XDMSyncMLState.XDM_STATE_ABORT_ALERT) ? xdmAgentLoadWorkSpace() : 0;
        }
        if (i != 0) {
            Log.E("xdmAgentCreatePackage failed");
            return -1;
        }
        xDMWorkspace.buf.reset();
        xDMEncoder.xdmEncInit(xDMWorkspace.buf);
        xDMEncoder.xdmEncStartSyncml(0, 106, XDMInterface.WBXML_STRING_TABLE_1_2, 29);
        XDMWorkspace xdmAgentBuildCmdSyncHeader = XDMBuildCmd.xdmAgentBuildCmdSyncHeader(xDMWorkspace);
        xDMEncoder.xdmEncAddSyncHeader(xdmAgentBuildCmdSyncHeader.syncHeader);
        xDMEncoder.xdmEncStartSyncbody();
        int i3 = AnonymousClass1.$SwitchMap$com$accessorydm$interfaces$XDMInterface$XDMSyncMLState[xdmAgentBuildCmdSyncHeader.dmState.ordinal()];
        if (i3 == 1) {
            Log.I("XDM_STATE_CLIENT_INIT_MGMT");
            xdmAgentClientInitPackage(xDMEncoder);
        } else if (i3 == 2) {
            Log.I("XDM_STATE_PROCESSING");
            xdmAgentMgmtPackage(xDMEncoder);
        } else if (i3 == 3) {
            Log.I("XDM_STATE_GENERIC_ALERT");
            int xdmAgentClientInitPackage = xdmAgentClientInitPackage(xDMEncoder);
            if (xdmAgentClientInitPackage != 0) {
                if (xdmAgentClientInitPackage == -3) {
                    xdmAgentBuildCmdSyncHeader.endOfMsg = false;
                } else {
                    Log.E("failed(%d)" + xdmAgentClientInitPackage);
                }
                return -1;
            }
            i = xdmAgentCreatePackageGenericAlert(xDMEncoder, XDMInterface.ALERT_GENERIC);
            if (i != 0) {
                if (i == -3) {
                    xdmAgentBuildCmdSyncHeader.endOfMsg = false;
                } else {
                    Log.E("failed(%d)" + i);
                }
                return -1;
            }
            xdmAgentBuildCmdSyncHeader.endOfMsg = true;
        } else if (i3 == 4) {
            Log.I("XDM_STATE_GENERIC_ALERT_REPORT");
            int xdmAgentClientInitPackage2 = xdmAgentClientInitPackage(xDMEncoder);
            if (xdmAgentClientInitPackage2 != 0) {
                if (xdmAgentClientInitPackage2 == -3) {
                    xdmAgentBuildCmdSyncHeader.endOfMsg = false;
                } else {
                    Log.E("failed(%d)" + xdmAgentClientInitPackage2);
                }
                return -1;
            }
            i = xdmAgentCreatePackageReportGenericAlert(xDMEncoder, XDMInterface.ALERT_GENERIC);
            if (i != 0) {
                if (i == -3) {
                    xdmAgentBuildCmdSyncHeader.endOfMsg = false;
                } else {
                    Log.E("failed(%d)" + i);
                }
                return -1;
            }
            xdmAgentBuildCmdSyncHeader.endOfMsg = true;
        } else if (i3 == 5) {
            Log.I("XDM_STATE_ABORT_ALERT");
            if (XDBProfileListAdp.xdbGetNotiEvent() > 0) {
                i2 = xdmAgentCreatePackageAlert(xDMEncoder, XDMInterface.ALERT_SERVER_INITIATED_MGMT);
            } else {
                i2 = xdmAgentCreatePackageAlert(xDMEncoder, XDMInterface.ALERT_CLIENT_INITIATED_MGMT);
            }
            if (i2 != 0) {
                if (i2 == -3) {
                    xdmAgentBuildCmdSyncHeader.endOfMsg = false;
                } else {
                    Log.E("failed(%d)" + i2);
                }
                return -1;
            }
            i = xdmAgentCreatePackageAlert(xDMEncoder, XDMInterface.ALERT_SESSION_ABORT);
            if (i != 0) {
                if (i == -3) {
                    xdmAgentBuildCmdSyncHeader.endOfMsg = false;
                } else {
                    Log.E("failed(%d)" + i);
                }
                return -1;
            }
            xdmAgentBuildCmdSyncHeader.endOfMsg = true;
        }
        if (xdmAgentBuildCmdSyncHeader.dataBuffered || xdmAgentBuildCmdSyncHeader.sendRemain) {
            xdmAgentBuildCmdSyncHeader.endOfMsg = false;
        }
        xDMEncoder.xdmEncEndSyncbody(xdmAgentBuildCmdSyncHeader.endOfMsg);
        xDMEncoder.xdmEncEndSyncml();
        return i;
    }

    /* renamed from: com.accessorydm.agent.XDMAgent$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$accessorydm$interfaces$XDMInterface$XDMSyncMLState = new int[XDMInterface.XDMSyncMLState.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            $SwitchMap$com$accessorydm$interfaces$XDMInterface$XDMSyncMLState[XDMInterface.XDMSyncMLState.XDM_STATE_CLIENT_INIT_MGMT.ordinal()] = 1;
            $SwitchMap$com$accessorydm$interfaces$XDMInterface$XDMSyncMLState[XDMInterface.XDMSyncMLState.XDM_STATE_PROCESSING.ordinal()] = 2;
            $SwitchMap$com$accessorydm$interfaces$XDMInterface$XDMSyncMLState[XDMInterface.XDMSyncMLState.XDM_STATE_GENERIC_ALERT.ordinal()] = 3;
            $SwitchMap$com$accessorydm$interfaces$XDMInterface$XDMSyncMLState[XDMInterface.XDMSyncMLState.XDM_STATE_GENERIC_ALERT_REPORT.ordinal()] = 4;
            $SwitchMap$com$accessorydm$interfaces$XDMInterface$XDMSyncMLState[XDMInterface.XDMSyncMLState.XDM_STATE_ABORT_ALERT.ordinal()] = 5;
        }
    }

    private int xdmAgentLoadWorkSpace() {
        XDMOmTree xDMOmTree;
        String concat;
        XDMVnode xdmOmGetNodeProp;
        char[] cArr;
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (xDMWorkspace == null || (xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat)) == null) {
            return -1;
        }
        char[] cArr2 = new char[xdmOmGetNodeProp.size];
        XDMOmLib.xdmOmRead((xDMOmTree = xDMWorkspace.om), (concat = m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH)), 0, cArr2, xdmOmGetNodeProp.size);
        xDMWorkspace.m_szUserName = String.valueOf(cArr2);
        String concat2 = m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
        XDMVnode xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat2);
        if (xdmOmGetNodeProp2 == null) {
            return -1;
        }
        char[] cArr3 = new char[xdmOmGetNodeProp2.size];
        XDMOmLib.xdmOmRead(xDMOmTree, concat2, 0, cArr3, xdmOmGetNodeProp2.size);
        xDMWorkspace.m_szClientPW = String.valueOf(cArr3);
        String concat3 = m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHTYPE_PATH);
        XDMVnode xdmOmGetNodeProp3 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat3);
        if (xdmOmGetNodeProp3 == null) {
            return -1;
        }
        char[] cArr4 = new char[xdmOmGetNodeProp3.size];
        XDMOmLib.xdmOmRead(xDMOmTree, concat3, 0, cArr4, xdmOmGetNodeProp3.size);
        int xdbGetNotiReSyncMode = XDBProfileListAdp.xdbGetNotiReSyncMode();
        if (xdbGetNotiReSyncMode == 1) {
            xDMWorkspace.credType = XDMAuth.xdmAuthAAuthtring2Type(XDMInterface.AUTH_TYPE_DIGEST);
        } else {
            xDMWorkspace.credType = XDMAuth.xdmAuthAAuthtring2Type(String.valueOf(cArr4));
        }
        String concat4 = m_DmAccXNodeInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHTYPE_PATH);
        XDMVnode xdmOmGetNodeProp4 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat4);
        if (xdmOmGetNodeProp4 == null) {
            return -1;
        }
        char[] cArr5 = new char[xdmOmGetNodeProp4.size];
        XDMOmLib.xdmOmRead(xDMOmTree, concat4, 0, cArr5, xdmOmGetNodeProp4.size);
        if (xdbGetNotiReSyncMode == 1) {
            xDMWorkspace.serverCredType = XDMAuth.xdmAuthAAuthtring2Type(XDMInterface.AUTH_TYPE_DIGEST);
        } else {
            xDMWorkspace.serverCredType = XDMAuth.xdmAuthAAuthtring2Type(String.valueOf(cArr5));
            if (xDMWorkspace.serverCredType == -1) {
                xDMWorkspace.serverCredType = xDMWorkspace.credType;
            }
        }
        String concat5 = m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_SERVERID_PATH);
        XDMVnode xdmOmGetNodeProp5 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat5);
        if (xdmOmGetNodeProp5 == null) {
            return -1;
        }
        char[] cArr6 = new char[xdmOmGetNodeProp5.size];
        XDMOmLib.xdmOmRead(xDMOmTree, concat5, 0, cArr6, xdmOmGetNodeProp5.size);
        xDMWorkspace.m_szServerID = String.valueOf(cArr6);
        String concat6 = m_DmAccXNodeInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
        XDMVnode xdmOmGetNodeProp6 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat6);
        if (xdmOmGetNodeProp6 == null) {
            return -1;
        }
        char[] cArr7 = new char[xdmOmGetNodeProp6.size];
        XDMOmLib.xdmOmRead(xDMOmTree, concat6, 0, cArr7, xdmOmGetNodeProp6.size);
        xDMWorkspace.m_szServerPW = String.valueOf(cArr7);
        char[] cArr8 = null;
        if (xdbGetNotiReSyncMode == 1) {
            xDMWorkspace.nextNonce[0] = 0;
            xDMWorkspace.nextNonce[1] = 0;
            xDMWorkspace.nextNonce[2] = 0;
            xDMWorkspace.nextNonce[3] = 0;
        } else {
            String concat7 = m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
            XDMVnode xdmOmGetNodeProp7 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat7);
            if (xdmOmGetNodeProp7 != null) {
                if (xdmOmGetNodeProp7.size > 0) {
                    cArr = new char[xdmOmGetNodeProp7.size];
                    XDMOmLib.xdmOmRead(xDMOmTree, concat7, 0, cArr, xdmOmGetNodeProp7.size);
                } else {
                    cArr = null;
                }
                if (cArr != null) {
                    if (xdmOmGetNodeProp7.format != 1) {
                        for (int i = 0; i < xdmOmGetNodeProp7.size; i++) {
                            xDMWorkspace.nextNonce[i] = (byte) cArr[i];
                        }
                        Log.I("node->size = " + xdmOmGetNodeProp7.size);
                    } else {
                        byte[] xdmBase64Decode = XDMBase64.xdmBase64Decode(new String(cArr));
                        xDMWorkspace.nextNonce = new byte[xdmBase64Decode.length];
                        System.arraycopy(xdmBase64Decode, 0, xDMWorkspace.nextNonce, 0, xdmBase64Decode.length);
                    }
                }
            }
            return -1;
        }
        String concat8 = m_DmAccXNodeInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
        Log.H("Server szAccBuf) :" + concat8);
        XDMVnode xdmOmGetNodeProp8 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat8);
        if (xdmOmGetNodeProp8 != null) {
            if (xdmOmGetNodeProp8.size > 0) {
                cArr8 = new char[xdmOmGetNodeProp8.size];
                XDMOmLib.xdmOmRead(xDMOmTree, concat8, 0, cArr8, xdmOmGetNodeProp8.size);
            }
            if (cArr8 != null) {
                if (xdmOmGetNodeProp8.format != 1) {
                    for (int i2 = 0; i2 < xdmOmGetNodeProp8.size; i2++) {
                        xDMWorkspace.serverNextNonce[i2] = (byte) cArr8[i2];
                    }
                } else {
                    Log.H("Server Next Noncenew String(buf) :" + new String(cArr8));
                    byte[] xdmBase64Decode2 = XDMBase64.xdmBase64Decode(new String(cArr8));
                    xDMWorkspace.serverNextNonce = new byte[xdmBase64Decode2.length];
                    System.arraycopy(xdmBase64Decode2, 0, xDMWorkspace.serverNextNonce, 0, xdmBase64Decode2.length);
                }
            }
            String concat9 = m_DmAccXNodeInfo.m_szAppAddr.concat("/Addr");
            XDMVnode xdmOmGetNodeProp9 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat9);
            if (xdmOmGetNodeProp9 == null) {
                return -1;
            }
            char[] cArr9 = new char[xdmOmGetNodeProp9.size];
            XDMOmLib.xdmOmRead(xDMOmTree, concat9, 0, cArr9, xdmOmGetNodeProp9.size);
            xDMWorkspace.m_szHostname = String.valueOf(cArr9);
            String concat10 = m_DmAccXNodeInfo.m_szAppAddrPort.concat("/PortNbr");
            XDMVnode xdmOmGetNodeProp10 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, concat10);
            if (xdmOmGetNodeProp10 == null) {
                return -1;
            }
            char[] cArr10 = new char[xdmOmGetNodeProp10.size];
            XDMOmLib.xdmOmRead(xDMOmTree, concat10, 0, cArr10, xdmOmGetNodeProp10.size);
            try {
                xDMWorkspace.port = Integer.valueOf(String.valueOf(cArr10)).intValue();
            } catch (NumberFormatException e) {
                Log.E(e.toString());
            }
            XDMVnode xdmOmGetNodeProp11 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_DEVID_PATH);
            if (xdmOmGetNodeProp11 == null) {
                return -1;
            }
            char[] cArr11 = new char[xdmOmGetNodeProp11.size];
            XDMOmLib.xdmOmRead(xDMOmTree, XDMInterface.XDM_DEVINFO_DEVID_PATH, 0, cArr11, xdmOmGetNodeProp11.size);
            xDMWorkspace.m_szSourceURI = String.valueOf(cArr11);
            return 0;
        }
        return -1;
    }

    private void xdmAgentMgmtPackage(XDMEncoder xDMEncoder) {
        int xdmAgentCreatePackageAlert;
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (!xDMWorkspace.dataBuffered || (xdmAgentCreatePackageAlert = xdmAgentCreatePackageAlert(xDMEncoder, XDMInterface.ALERT_NEXT_MESSAGE)) == 0) {
            int xdmAgentCreatePackageStatus = xdmAgentCreatePackageStatus(xDMEncoder);
            if (xdmAgentCreatePackageStatus == 0) {
                int xdmAgentCreatePackageResults = xdmAgentCreatePackageResults(xDMEncoder);
                if (xdmAgentCreatePackageResults == 0) {
                    xDMWorkspace.endOfMsg = true;
                } else if (xdmAgentCreatePackageResults == -3) {
                    xDMWorkspace.endOfMsg = false;
                } else {
                    Log.E("failed = " + xdmAgentCreatePackageResults);
                }
            } else if (xdmAgentCreatePackageStatus == -3) {
                xDMWorkspace.endOfMsg = false;
            } else {
                Log.E("failed = " + xdmAgentCreatePackageStatus);
            }
        } else if (xdmAgentCreatePackageAlert == -3) {
            xDMWorkspace.endOfMsg = false;
        } else {
            Log.E("failed = " + xdmAgentCreatePackageAlert);
        }
    }

    private void xdmAgentMakeSyncMLNode() throws XDMOmTreeException {
        XDMOmTree xDMOmTree = g_DmWs.om;
        try {
            XDBProfileInfo xdbGetProfileInfo = XDBProfileAdp.xdbGetProfileInfo();
            xdmAgentMakeDefaultAcl(xDMOmTree, XDMInterface.XDM_BASE_PATH, 9, 1);
            xdm_SET_OM_PATH(xDMOmTree, XDMInterface.XDM_SYNCML_PATH, 27, 1);
            xdm_SET_OM_PATH(xDMOmTree, XDMInterface.XDM_BASE_PATH, 27, 1);
            xdm_SET_OM_PATH(xDMOmTree, XDMInterface.XDM_ACCOUNT_PATH, 27, 1);
            xdm_SET_OM_PATH(xDMOmTree, XDMInterface.XDM_SYNCML_CON_PATH, 27, 1);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szAccount, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPID_PATH), "w7", 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_SERVERID_PATH), xdbGetProfileInfo.ServerID, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_NAME_PATH), xdbGetProfileInfo.ProfileName, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_PREFCONREF_PATH), xdbGetProfileInfo.PrefConRef, 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_TOCONREF_PATH), 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szToConRef, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szToConRef.concat("/ConRef"), XDMInterface.XDM_DEFAULT_CONREF, 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPADDR_PATH), 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szAppAddr, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAppAddr.concat("/Addr"), xdbGetProfileInfo.ServerUrl, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAppAddr.concat("/AddrType"), "URI", 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szAppAddr.concat(XDMInterface.XDM_APPADDR_PORT_PATH), 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szAppAddrPort, 27, 2);
            String valueOf = String.valueOf(xdbGetProfileInfo.ServerPort);
            Log.H("ServerPort = " + valueOf);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAppAddrPort.concat("/PortNbr"), valueOf, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_AAUTHPREF_PATH), XDMAuth.xdmAuthCredType2String(xdbGetProfileInfo.AuthType), 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPAUTH_PATH), 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szClientAppAuth, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHLEVEL_PATH), xdbGetProfileInfo.AuthLevel, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHTYPE_PATH), XDMAuth.xdmAuthAAuthType2String(xdbGetProfileInfo.AuthType), 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH), xdbGetProfileInfo.UserName, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH), xdbGetProfileInfo.Password, 27, 2);
            String xdmAgentCheckNonce = xdmAgentCheckNonce(xdbGetProfileInfo.ClientNonce);
            xdmAgentSetOMAccB64(xDMOmTree, m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH), xdmAgentCheckNonce, 27, 2);
            xdm_SET_OM_PATH(xDMOmTree, m_DmAccXNodeInfo.m_szServerAppAuth, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHLEVEL_PATH), xdbGetProfileInfo.ServerAuthLevel, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHTYPE_PATH), XDMAuth.xdmAuthAAuthType2String(xdbGetProfileInfo.nServerAuthType), 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH), xdbGetProfileInfo.ServerID, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH), xdbGetProfileInfo.ServerPwd, 27, 2);
            String xdmAgentCheckNonce2 = xdmAgentCheckNonce(xdbGetProfileInfo.ServerNonce);
            xdmAgentSetOMAccB64(xDMOmTree, m_DmAccXNodeInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH), xdmAgentCheckNonce2, 27, 2);
            xdmAgentSetOMAccStr(xDMOmTree, m_DmAccXNodeInfo.m_szAccount.concat("/Ext"), " ", 27, 2);
            String xdmDDFGetMOPath = XDMDDFXmlHandler.xdmDDFGetMOPath(10);
            if (!TextUtils.isEmpty(xdmDDFGetMOPath)) {
                xdm_SET_OM_PATH(xDMOmTree, xdmDDFGetMOPath, 27, 1);
            }
        } catch (Exception e) {
            Log.E("XDMOmTreeException : " + e.toString());
            throw new XDMOmTreeException();
        }
    }

    private String xdmAgentCheckNonce(String str) {
        return TextUtils.isEmpty(str) ? XDMBase64.xdmBase64Encode(DEFAULT_NONCE) : str;
    }

    private static void xdmAgentReMakeFwUpdateNode(XDMOmTree xDMOmTree, String str) throws XDMOmTreeException {
        XDMOmTree xDMOmTree2 = xDMOmTree;
        char[] cArr = new char[str.length()];
        String str2 = str;
        while (true) {
            XDMOmLib.xdmOmMakeParentPath(str2, cArr);
            str2 = XDMMem.xdmLibCharToString(cArr);
            if (TextUtils.isEmpty(str2)) {
                Log.I("szTmpbuf null!");
                break;
            }
            Log.I(str2);
            if (!str2.contains("/Update") && !str2.contains("/DownloadAndUpdate") && !str2.contains("/Download") && !str2.contains("/Ext")) {
                if (XDMOmLib.xdmOmGetNodeProp(xDMOmTree, str2) == null) {
                    XDMOmLib.xdmOmProcessCmdImplicitAdd(xDMOmTree, str2, 24, 1);
                }
            }
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        String str3 = str2;
        xdmAgentSetOMAccStr(xDMOmTree, str3 + XFOTAInterface.XFUMO_PKGNAME_PATH, " ", 25, 2);
        xdmAgentSetOMAccStr(xDMOmTree, str3 + XFOTAInterface.XFUMO_PKGVERSION_PATH, " ", 25, 2);
        String str4 = str3 + "/Download";
        xdm_SET_OM_PATH(xDMOmTree, str4, 13, 2);
        xdmAgentSetOMAccStr(xDMOmTree, str4.concat(XFOTAInterface.XFUMO_PKGURL_PATH), " ", 25, 2);
        String str5 = str3 + "/Update";
        xdm_SET_OM_PATH(xDMOmTree, str5, 29, 2);
        xdmAgentSetOMAccBin(xDMOmTree, str5.concat(XFOTAInterface.XFUMO_PKGDATA_PATH), "", 0, 16, 2);
        String str6 = str3 + "/DownloadAndUpdate";
        xdm_SET_OM_PATH(xDMOmTree, str6, 29, 2);
        xdmAgentSetOMAccStr(xDMOmTree, str6.concat(XFOTAInterface.XFUMO_PKGURL_PATH), " ", 25, 2);
        xdmAgentSetOMAccStr(xDMOmTree, str3 + XFOTAInterface.XFUMO_STATE_PATH, String.valueOf(XDBFumoAdp.xdbGetFUMOStatus()), 8, 2);
        String str7 = str3 + "/Ext";
        xdm_SET_OM_PATH(xDMOmTree, str7, 25, 2);
        xdmAgentSetOMAccStr(xDMOmTree, str7.concat(XFOTAInterface.XFUMO_SVCSTATE), " ", 25, 2);
        xdmAgentSetOMAccStr(xDMOmTree, str7.concat(XFOTAInterface.XFUMO_DOWNLOADCONNTYPE_PATH), " ", 25, 2);
        xdmAgentSetOMAccStr(xDMOmTree, str7.concat(XFOTAInterface.XFUMO_ROOTINGCHECK_PATH), " ", 25, 2);
        String concat = str7.concat(XFOTAInterface.XFUMO_POSTPONE_PATH);
        xdmAgentSetOMAccStr(xDMOmTree, concat, " ", 25, 2);
        String concat2 = concat.concat(XFOTAInterface.XFUMO_FORCE_PATH);
        xdmAgentSetOMAccStr(xDMOmTree, concat2, " ", 25, 2);
        Log.I("pFUMONode:" + concat2);
    }

    private void xdmAgentMakeDevInfoNode() throws XDMOmTreeException {
        XDMOmTree xDMOmTree = g_DmWs.om;
        xdm_SET_OM_PATH(xDMOmTree, XDMInterface.XDM_DEVINFO_PATH, 11, 1);
        xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_DEVID_PATH, XDMDevinfAdapter.xdmDevAdpGetFullDeviceID(), 8, 1);
        xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_MAN_PATH, XDMDevinfAdapter.xdmDevAdpGetManufacturer(), 11, 1);
        xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_MOD_PATH, XDMDevinfAdapter.xdmDevAdpGetModel(), 11, 1);
        xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_DMV_PATH, "1.2", 11, 1);
        xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_LANG_PATH, XDMDevinfAdapter.xdmDevAdpGetLanguage(), 11, 1);
        xdm_SET_OM_PATH(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_PATH, 10, 1);
        String xdmDevAdpGetTelephonyMcc = XDMDevinfAdapter.xdmDevAdpGetTelephonyMcc();
        if (!TextUtils.isEmpty(xdmDevAdpGetTelephonyMcc)) {
            xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_TELEPHONYMCC_PATH, xdmDevAdpGetTelephonyMcc, 11, 1);
        }
        String xdmDevAdpGetTelephonyMnc = XDMDevinfAdapter.xdmDevAdpGetTelephonyMnc();
        if (!TextUtils.isEmpty(xdmDevAdpGetTelephonyMnc)) {
            xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_TELEPHONYMNC_PATH, xdmDevAdpGetTelephonyMnc, 11, 1);
        }
        String xdmDevAdpGetAppVersion = XDMDevinfAdapter.xdmDevAdpGetAppVersion();
        if (!TextUtils.isEmpty(xdmDevAdpGetAppVersion)) {
            xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_FOTACLIENTVER_PATH, xdmDevAdpGetAppVersion, 11, 1);
            xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_DMCLIENTVER_PATH, xdmDevAdpGetAppVersion, 11, 1);
        }
        xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_NETWORKCONNTYPE_PATH, xdmAgentGetDevNetworkConnType(), 11, 1);
    }

    private void xdmAgentMakeDevDetailNode() throws XDMOmTreeException {
        XDMOmTree xDMOmTree = g_DmWs.om;
        xdm_SET_OM_PATH(xDMOmTree, XDMInterface.XDM_DEVDETAIL_PATH, 8, 1);
        xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVDETAIL_FWV_PATH, XDMDevinfAdapter.xdmDevAdpGetFirmwareVersion(), 8, 1);
        xdmAgentSetOMAccStr(xDMOmTree, XDMInterface.XDM_DEVDETAIL_LRGOBJ_PATH, XDMInterface.XDM_DEVDETAIL_DEFAULT_LRGOBJ_SUPPORT, 8, 1);
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVDETAIL_LRGOBJ_PATH);
        if (xdmOmGetNodeProp != null) {
            xdmOmGetNodeProp.format = 3;
            xdmOmGetNodeProp.type = null;
        }
        xdm_SET_OM_PATH(xDMOmTree, XDMInterface.XDM_DEVDETAIL_EXT_PATH, 10, 1);
    }

    private void xdmAgentMakeFwUpdateNode() throws XDMOmTreeException {
        XDMOmTree xDMOmTree = g_DmWs.om;
        Log.I("xdmAgentMakeFwUpdateNode Initialize");
        xdm_SET_OM_PATH(xDMOmTree, XFOTAInterface.XFUMO_PATH, 25, 1);
        for (int i = 0; i < 1; i++) {
            String concat = XFOTAInterface.XFUMO_PATH.concat(XDMInterface.FUMO_X_NODE_COMMON);
            Log.I("pFUMOPackageNode :".concat(concat));
            xdm_SET_OM_PATH(xDMOmTree, concat, 25, 1);
            xdmAgentSetOMAccStr(xDMOmTree, concat.concat(XFOTAInterface.XFUMO_PKGNAME_PATH), XFOTAInterface.XDL_DEFAULT_PKGNAME, 25, 2);
            xdmAgentSetOMAccStr(xDMOmTree, concat.concat(XFOTAInterface.XFUMO_PKGVERSION_PATH), "1.0", 25, 2);
            String concat2 = concat.concat("/Download");
            xdm_SET_OM_PATH(xDMOmTree, concat2, 29, 2);
            xdmAgentSetOMAccStr(xDMOmTree, concat2.concat(XFOTAInterface.XFUMO_PKGURL_PATH), " ", 25, 2);
            String concat3 = concat.concat("/Update");
            xdm_SET_OM_PATH(xDMOmTree, concat3, 29, 2);
            xdmAgentSetOMAccBin(xDMOmTree, concat3.concat(XFOTAInterface.XFUMO_PKGDATA_PATH), "", 0, 16, 2);
            String concat4 = concat.concat("/DownloadAndUpdate");
            xdm_SET_OM_PATH(xDMOmTree, concat4, 29, 2);
            xdmAgentSetOMAccStr(xDMOmTree, concat4.concat(XFOTAInterface.XFUMO_PKGURL_PATH), " ", 25, 2);
            String concat5 = concat.concat(XFOTAInterface.XFUMO_STATE_PATH);
            String valueOf = String.valueOf(XDBFumoAdp.xdbGetFUMOStatus());
            Log.I(valueOf);
            xdmAgentSetOMAccStr(xDMOmTree, concat5, valueOf, 8, 2);
            String concat6 = concat.concat("/Ext");
            xdm_SET_OM_PATH(xDMOmTree, concat6, 25, 2);
            xdmAgentSetOMAccStr(xDMOmTree, concat6.concat(XFOTAInterface.XFUMO_SVCSTATE), " ", 25, 2);
            xdmAgentSetOMAccStr(xDMOmTree, concat6.concat(XFOTAInterface.XFUMO_DOWNLOADCONNTYPE_PATH), " ", 25, 2);
            xdmAgentSetOMAccStr(xDMOmTree, concat6.concat(XFOTAInterface.XFUMO_ROOTINGCHECK_PATH), " ", 25, 2);
            String concat7 = concat6.concat(XFOTAInterface.XFUMO_POSTPONE_PATH);
            xdmAgentSetOMAccStr(xDMOmTree, concat7, " ", 25, 2);
            xdmAgentSetOMAccStr(xDMOmTree, concat7.concat(XFOTAInterface.XFUMO_FORCE_PATH), " ", 25, 2);
        }
    }

    private static void xdm_SET_OM_PATH(XDMOmTree xDMOmTree, String str, int i, int i2) throws XDMOmTreeException {
        try {
            if (XDMOmLib.xdmOmGetNodeProp(xDMOmTree, str) == null) {
                XDMOmLib.xdmOmWrite(xDMOmTree, str, 0, 0, "", 0);
                xdmAgentMakeDefaultAcl(xDMOmTree, str, i, i2);
            }
        } catch (Exception e) {
            Log.E("XDMOmTreeException : " + e.toString());
            throw new XDMOmTreeException();
        }
    }

    public static void xdmAgentMakeDefaultAcl(XDMOmTree xDMOmTree, String str, int i, int i2) {
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, str);
        if (xdmOmGetNodeProp != null) {
            if (i != 0) {
                ((XDMOmAcl) xdmOmGetNodeProp.acl.data).ac = i;
            } else {
                Log.I("ACL is XDM_OMACL_NONE");
            }
            xdmOmGetNodeProp.scope = i2;
            return;
        }
        Log.I("Not Exist");
    }

    private static void xdmAgentSetOMAccStr(Object obj, String str, String str2, int i, int i2) throws XDMOmTreeException {
        XDMOmTree xDMOmTree = (XDMOmTree) obj;
        try {
            XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, str);
            if (TextUtils.isEmpty(str2)) {
                str2 = "";
            }
            if (xdmOmGetNodeProp == null) {
                xdmAgentSetOM(str, str2);
                xdmAgentMakeDefaultAcl(xDMOmTree, str, i, i2);
                return;
            }
            char[] cArr = new char[xdmOmGetNodeProp.size];
            XDMOmLib.xdmOmRead(xDMOmTree, str, 0, cArr, xdmOmGetNodeProp.size);
            String valueOf = String.valueOf(cArr);
            if (xdmOmGetNodeProp.size != str2.length()) {
                xdmAgentSetOM(str, str2);
            } else if (valueOf.compareTo(str2) != 0) {
                xdmAgentSetOM(str, str2);
            }
        } catch (Exception e) {
            Log.E("XDMOmTreeException : " + e.toString());
            throw new XDMOmTreeException();
        }
    }

    private void xdmAgentSetOMAccB64(Object obj, String str, String str2, int i, int i2) {
        XDMOmTree xDMOmTree = (XDMOmTree) obj;
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, str);
        if (xdmOmGetNodeProp == null) {
            xdmAgentSetOMB64(str, str2);
            xdmAgentMakeDefaultAcl(xDMOmTree, str, i, i2);
            return;
        }
        char[] cArr = new char[xdmOmGetNodeProp.size];
        XDMOmLib.xdmOmRead(xDMOmTree, str, 0, cArr, xdmOmGetNodeProp.size);
        String valueOf = String.valueOf(cArr);
        if (str2.length() != xdmOmGetNodeProp.size) {
            xdmAgentSetOMB64(str, str2);
        } else if (valueOf.compareTo(str2) != 0) {
            xdmAgentSetOMB64(str, str2);
        }
    }

    private static void xdmAgentSetOMAccBin(Object obj, String str, String str2, int i, int i2, int i3) {
        XDMOmTree xDMOmTree = (XDMOmTree) obj;
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, str);
        if (xdmOmGetNodeProp == null) {
            xdmAgentSetOMBin(str, str2, i);
            xdmAgentMakeDefaultAcl(xDMOmTree, str, i2, i3);
            return;
        }
        char[] cArr = new char[xdmOmGetNodeProp.size];
        XDMOmLib.xdmOmRead(xDMOmTree, str, 0, cArr, xdmOmGetNodeProp.size);
        String valueOf = String.valueOf(cArr);
        if (i != xdmOmGetNodeProp.size) {
            xdmAgentSetOMBin(str, str2, i);
        } else if (valueOf.compareTo(str2) != 0) {
            xdmAgentSetOMBin(str, str2, i);
        }
    }

    private static void xdmAgentSetOMBin(String str, String str2, int i) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmLib.xdmOmWrite(xDMWorkspace.om, str, i, 0, str2, i);
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMWorkspace.om, str);
        if (xdmOmGetNodeProp != null) {
            if (xdmOmGetNodeProp.type != null) {
                XDMOmLib.xdmOmVfsDeleteMimeList(xdmOmGetNodeProp.type);
            }
            xdmOmGetNodeProp.type = null;
            xdmOmGetNodeProp.format = 2;
        }
    }

    private void xdmAgentSetOMB64(String str, Object obj) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (obj == null) {
            Log.E("data is NULL");
            return;
        }
        int length = String.valueOf(obj).getBytes(Charset.defaultCharset()).length;
        if (length <= 0) {
            XDMOmLib.xdmOmDeleteImplicit(xDMWorkspace.om, str, true);
            Log.I("The [" + str + "] node is 0 length");
        }
        XDMOmLib.xdmOmWrite(xDMWorkspace.om, str, length, 0, obj, length);
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMWorkspace.om, str);
        if (xdmOmGetNodeProp != null) {
            if (xdmOmGetNodeProp.type != null) {
                XDMOmLib.xdmOmVfsDeleteMimeList(xdmOmGetNodeProp.type);
            }
            XDMOmList xDMOmList = new XDMOmList();
            xDMOmList.data = XDMInterface.MIMETYPE_TEXT_PLAIN;
            xDMOmList.next = null;
            xdmOmGetNodeProp.type = xDMOmList;
            xdmOmGetNodeProp.format = 1;
        }
    }

    private static void xdmAgentSetOM(String str, Object obj) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (obj != null) {
            int length = String.valueOf(obj).getBytes(Charset.defaultCharset()).length;
            if (length <= 0) {
                XDMOmLib.xdmOmDeleteImplicit(xDMWorkspace.om, str, true);
            }
            XDMOmLib.xdmOmWrite(xDMWorkspace.om, str, length, 0, obj, length);
            XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMWorkspace.om, str);
            if (xdmOmGetNodeProp != null) {
                if (xdmOmGetNodeProp.type != null) {
                    XDMOmLib.xdmOmVfsDeleteMimeList(xdmOmGetNodeProp.type);
                }
                XDMOmList xDMOmList = new XDMOmList();
                xDMOmList.data = XDMInterface.MIMETYPE_TEXT_PLAIN;
                xDMOmList.next = null;
                xdmOmGetNodeProp.type = xDMOmList;
                xdmOmGetNodeProp.format = 4;
            }
        }
    }

    private int xdmAgentClientInitPackage(XDMEncoder xDMEncoder) {
        int i;
        XDMWorkspace xDMWorkspace = g_DmWs;
        Log.I("");
        int xdmAgentCreatePackageStatus = xdmAgentCreatePackageStatus(xDMEncoder);
        if (xdmAgentCreatePackageStatus != 0) {
            if (xdmAgentCreatePackageStatus == -3) {
                xDMWorkspace.endOfMsg = false;
            } else {
                Log.E("failed(" + xdmAgentCreatePackageStatus + ")");
            }
            return -1;
        }
        int xdmAgentCreatePackageResults = xdmAgentCreatePackageResults(xDMEncoder);
        if (xdmAgentCreatePackageResults != 0) {
            if (xdmAgentCreatePackageResults == -3) {
                xDMWorkspace.endOfMsg = false;
            } else {
                Log.E("failed(" + xdmAgentCreatePackageResults + ")");
            }
            return -1;
        }
        if (XDBProfileListAdp.xdbGetNotiEvent() > 0) {
            i = xdmAgentCreatePackageAlert(xDMEncoder, XDMInterface.ALERT_SERVER_INITIATED_MGMT);
        } else {
            i = xdmAgentCreatePackageAlert(xDMEncoder, XDMInterface.ALERT_CLIENT_INITIATED_MGMT);
        }
        if (i != 0) {
            if (i == -3) {
                xDMWorkspace.endOfMsg = false;
            } else {
                Log.E("failed(" + i + ")");
            }
            return -1;
        }
        int xdmAgentCreatePackageDevInfo = xdmAgentCreatePackageDevInfo(xDMEncoder);
        if (xdmAgentCreatePackageDevInfo != 0) {
            if (xdmAgentCreatePackageDevInfo == -3) {
                xDMWorkspace.endOfMsg = false;
            } else {
                Log.E("failed(" + xdmAgentCreatePackageDevInfo + ")");
            }
            return -1;
        }
        xDMWorkspace.endOfMsg = true;
        return 0;
    }

    private int xdmAgentCreatePackageStatus(XDMEncoder xDMEncoder) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMLinkedList.xdmListSetCurrentObj(xDMWorkspace.statusList, 0);
        for (XDMParserStatus xDMParserStatus = (XDMParserStatus) XDMLinkedList.xdmListGetNextObj(xDMWorkspace.statusList); xDMParserStatus != null; xDMParserStatus = (XDMParserStatus) XDMLinkedList.xdmListGetNextObj(xDMWorkspace.statusList)) {
            xDMEncoder.xdmEncAddStatus(xDMParserStatus);
            XDMLinkedList.xdmListRemoveObjAtFirst(xDMWorkspace.statusList);
            XDMHandleCmd.xdmAgentDataStDeleteStatus(xDMParserStatus);
        }
        XDMLinkedList.xdmListClearLinkedList(xDMWorkspace.statusList);
        return 0;
    }

    private int xdmAgentCreatePackageResults(XDMEncoder xDMEncoder) {
        XDMParserResults xDMParserResults;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        XDMLinkedList.xdmListSetCurrentObj(xDMWorkspace.resultsList, 0);
        XDMParserResults xDMParserResults2 = (XDMParserResults) XDMLinkedList.xdmListGetNextObj(xDMWorkspace.resultsList);
        if (xDMParserResults2 != null || xDMWorkspace.dmState == XDMInterface.XDMSyncMLState.XDM_STATE_CLIENT_INIT_MGMT || !xDMWorkspace.nextMsg) {
            while (xDMParserResults2 != null) {
                XDMParserItem xDMParserItem = (XDMParserItem) xDMParserResults2.itemlist.item;
                int intValue = (xDMParserItem.meta == null || xDMParserItem.meta.size <= 0) ? 0 : Integer.valueOf(xDMParserItem.meta.size).intValue();
                XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem.m_szSource);
                int i = intValue + 128;
                int xdmEncGetBufferSize = xDMWorkspace.maxMsgSize - XDMEncoder.xdmEncGetBufferSize(xDMEncoder);
                if (xdmEncGetBufferSize < 128) {
                    return -3;
                }
                if (intValue <= 0 || xdmEncGetBufferSize >= i) {
                    intValue = 0;
                }
                if (intValue > 0) {
                    char[] cArr = new char[intValue];
                    if (xDMParserItem.meta == null || TextUtils.isEmpty(xDMParserItem.meta.m_szType) || XDMInterface.SYNCML_MIME_TYPE_TNDS_XML.compareTo(xDMParserItem.meta.m_szType) != 0) {
                        XDMOmLib.xdmOmRead(xDMOmTree, xDMParserItem.m_szSource, 0, cArr, intValue);
                        if (xdmOmGetNodeProp == null || xdmOmGetNodeProp.format != 2) {
                            xDMParserItem.data = XDMHandleCmd.xdmAgentDataStString2Pcdata(cArr);
                        } else {
                            xDMParserItem.data = new XDMParserPcdata();
                            xDMParserItem.data.type = 1;
                            xDMParserItem.data.data = new char[intValue];
                            System.arraycopy(cArr, 0, xDMParserItem.data.data, 0, intValue);
                            xDMParserItem.data.size = intValue;
                        }
                    } else {
                        byte[] bArr = new byte[intValue];
                        XDB.xdbReadFile(XDB.xdbGetFileIdTNDS(), 0, intValue, bArr);
                        xDMParserItem.data = XDMHandleCmd.xdmAgentDataStString2Pcdata(new String(bArr, Charset.defaultCharset()).toCharArray());
                        xDMWorkspace.sendPos = 0;
                        xDMEncoder.xdmEncAddResults(xDMParserResults2);
                        xDMParserResults = (XDMParserResults) XDMLinkedList.xdmListGetNextObj(xDMWorkspace.resultsList);
                        XDMLinkedList.xdmListRemoveObjAtFirst(xDMWorkspace.resultsList);
                        XDMHandleCmd.xdmAgentDataStDeleteResults(xDMParserResults2);
                        xDMParserResults2 = xDMParserResults;
                    }
                }
                xDMWorkspace.sendPos = 0;
                xDMEncoder.xdmEncAddResults(xDMParserResults2);
                xDMParserResults = (XDMParserResults) XDMLinkedList.xdmListGetNextObj(xDMWorkspace.resultsList);
                XDMLinkedList.xdmListRemoveObjAtFirst(xDMWorkspace.resultsList);
                XDMHandleCmd.xdmAgentDataStDeleteResults(xDMParserResults2);
                xDMParserResults2 = xDMParserResults;
            }
            XDMLinkedList.xdmListClearLinkedList(xDMWorkspace.resultsList);
            return 0;
        }
        XDMLinkedList.xdmListClearLinkedList(xDMWorkspace.resultsList);
        return 0;
    }

    private int xdmAgentCreatePackageAlert(XDMEncoder xDMEncoder, String str) {
        XDMParserAlert xdmAgentBuildCmdAlert = XDMBuildCmd.xdmAgentBuildCmdAlert(g_DmWs, str);
        xDMEncoder.xdmEncAddAlert(xdmAgentBuildCmdAlert);
        XDMHandleCmd.xdmAgentDataStDeleteAlert(xdmAgentBuildCmdAlert);
        return 0;
    }

    private int xdmAgentCreatePackageDevInfo(XDMEncoder xDMEncoder) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        XDMLinkedList xdmListCreateLinkedList = XDMLinkedList.xdmListCreateLinkedList();
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_LANG_PATH);
        if (xdmOmGetNodeProp != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_LANG_PATH, xdmOmGetNodeProp.size);
        }
        XDMVnode xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_DMV_PATH);
        if (xdmOmGetNodeProp2 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_DMV_PATH, xdmOmGetNodeProp2.size);
        }
        XDMVnode xdmOmGetNodeProp3 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_MOD_PATH);
        if (xdmOmGetNodeProp3 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_MOD_PATH, xdmOmGetNodeProp3.size);
        }
        XDMVnode xdmOmGetNodeProp4 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_MAN_PATH);
        if (xdmOmGetNodeProp4 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_MAN_PATH, xdmOmGetNodeProp4.size);
        }
        XDMVnode xdmOmGetNodeProp5 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_DEVID_PATH);
        if (xdmOmGetNodeProp5 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_DEVID_PATH, xdmOmGetNodeProp5.size);
        }
        XDMVnode xdmOmGetNodeProp6 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_TELEPHONYMCC_PATH);
        if (xdmOmGetNodeProp6 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_EXT_TELEPHONYMCC_PATH, xdmOmGetNodeProp6.size);
        }
        XDMVnode xdmOmGetNodeProp7 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_TELEPHONYMNC_PATH);
        if (xdmOmGetNodeProp7 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_EXT_TELEPHONYMNC_PATH, xdmOmGetNodeProp7.size);
        }
        XDMVnode xdmOmGetNodeProp8 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_FOTACLIENTVER_PATH);
        if (xdmOmGetNodeProp8 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_EXT_FOTACLIENTVER_PATH, xdmOmGetNodeProp8.size);
        }
        XDMVnode xdmOmGetNodeProp9 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_DMCLIENTVER_PATH);
        if (xdmOmGetNodeProp9 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_EXT_DMCLIENTVER_PATH, xdmOmGetNodeProp9.size);
        }
        XDMVnode xdmOmGetNodeProp10 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMInterface.XDM_DEVINFO_EXT_NETWORKCONNTYPE_PATH);
        if (xdmOmGetNodeProp10 != null) {
            xdmAgent_MAKE_REP_ITEM(xDMOmTree, xdmListCreateLinkedList, XDMInterface.XDM_DEVINFO_EXT_NETWORKCONNTYPE_PATH, xdmOmGetNodeProp10.size);
        }
        XDMParserReplace xdmAgentBuildCmdReplace = XDMBuildCmd.xdmAgentBuildCmdReplace(xDMWorkspace, xdmListCreateLinkedList);
        XDMLinkedList.xdmListFreeLinkedList(xdmListCreateLinkedList);
        xDMEncoder.xdmEncAddReplace(xdmAgentBuildCmdReplace);
        XDMHandleCmd.xdmAgentDataStDeleteReplace(xdmAgentBuildCmdReplace);
        return 0;
    }

    private void xdmAgent_MAKE_REP_ITEM(XDMOmTree xDMOmTree, XDMLinkedList xDMLinkedList, String str, int i) {
        char[] cArr = new char[i];
        if (XDMOmLib.xdmOmRead(xDMOmTree, str, 0, cArr, i) < 0) {
            Log.I("xdmOmRead failed");
        }
        XDMParserItem xDMParserItem = new XDMParserItem();
        xDMParserItem.m_szSource = str;
        xDMParserItem.data = XDMHandleCmd.xdmAgentDataStString2Pcdata(cArr);
        XDMLinkedList.xdmListAddObjAtLast(xDMLinkedList, xDMParserItem);
    }

    public static void xdmAgentSaveBootstrapDateToFFS(XDBProfileInfo xDBProfileInfo) {
        Log.H("ServerID[" + xDBProfileInfo.ServerID + "]");
        int xdbSetActiveProfileIndexByServerID = XDB.xdbSetActiveProfileIndexByServerID(xDBProfileInfo.ServerID);
        XDBProfileAdp.xdbSetProfileInfo(xDBProfileInfo);
        XDBProfileListAdp.xdbSetProfileName(xdbSetActiveProfileIndexByServerID, xDBProfileInfo.ProfileName);
    }

    private int xdmAgentCreatePackageGenericAlert(XDMEncoder xDMEncoder, String str) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        Log.I("");
        XDMParserAlert xdmAgentBuildCmdGenericAlert = XDMBuildCmd.xdmAgentBuildCmdGenericAlert(xDMWorkspace, str);
        xDMEncoder.xdmEncAddAlert(xdmAgentBuildCmdGenericAlert);
        XDMHandleCmd.xdmAgentDataStDeleteAlert(xdmAgentBuildCmdGenericAlert);
        return 0;
    }

    private int xdmAgentCreatePackageReportGenericAlert(XDMEncoder xDMEncoder, String str) {
        XDMParserAlert xdmAgentBuildCmdGenericAlertReport = XDMBuildCmd.xdmAgentBuildCmdGenericAlertReport(g_DmWs, str);
        xDMEncoder.xdmEncAddAlert(xdmAgentBuildCmdGenericAlertReport);
        XDMHandleCmd.xdmAgentDataStDeleteAlert(xdmAgentBuildCmdGenericAlertReport);
        return 0;
    }

    private boolean xdmAgentVefifyAtomicCmd(XDMAgent xDMAgent) {
        if ("Atomic_Start".compareTo(xDMAgent.m_szCmd) == 0 || HttpNetworkInterface.XTP_HTTP_METHOD_GET.compareTo(xDMAgent.m_szCmd) == 0) {
            return false;
        }
        Log.I("");
        return true;
    }

    private int xdmAgentCmdAtomicBlock(XDMParserAtomic xDMParserAtomic, XDMLinkedList xDMLinkedList) throws XDMOmTreeException {
        XDMParserStatus xDMParserStatus;
        XDMParserStatus xdmAgentBuildCmdStatus;
        XDMParserStatus xdmAgentBuildCmdStatus2;
        XDMParserStatus xdmAgentBuildCmdStatus3;
        XDMParserStatus xdmAgentBuildCmdStatus4;
        XDMParserStatus xdmAgentBuildCmdStatus5;
        XDMParserStatus xdmAgentBuildCmdStatus6;
        XDMWorkspace xDMWorkspace = g_DmWs;
        xDMWorkspace.tmpItem = null;
        XDMLinkedList.xdmListSetCurrentObj(xDMLinkedList, 0);
        XDMAgent xDMAgent = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
        boolean z = true;
        while (xDMAgent != null) {
            if (!xdmAgentVefifyAtomicCmd(xDMAgent)) {
                z = false;
            }
            xDMAgent = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
        }
        if (z) {
            xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAtomic.cmdid, XDMInterface.CMD_ATOMIC, (String) null, (String) null, "200");
            xDMWorkspace.atomicStep = XDMInterface.XDMAtomicStep.XDM_ATOMIC_NONE;
        } else {
            xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAtomic.cmdid, XDMInterface.CMD_ATOMIC, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_FAILED);
            xDMWorkspace.atomicStep = XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_ROLLBACK;
        }
        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
        XDMLinkedList.xdmListSetCurrentObj(xDMLinkedList, 0);
        XDMAgent xDMAgent2 = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
        int i = 1;
        while (xDMAgent2 != null) {
            if (!xDMWorkspace.atomicFlag) {
                if (XDMInterface.CMD_GET.compareTo(xDMAgent2.m_szCmd) == 0) {
                    if (xdmAgentCmdGet(xDMAgent2.m_Get, true) != 0) {
                        Log.E("get failed");
                        return -1;
                    }
                } else if (XDMInterface.CMD_EXEC.compareTo(xDMAgent2.m_szCmd) == 0) {
                    int xdmAgentCmdExec = xdmAgentCmdExec(xDMAgent2.m_Exec);
                    if (XDMInterface.STATUS_ATOMIC_FAILED.compareTo(xDMParserStatus.m_szData) == 0) {
                        xDMWorkspace.atomicFlag = true;
                    }
                    if (xdmAgentCmdExec != 0) {
                        Log.E("exec failed");
                        return -1;
                    }
                } else if (XDMInterface.CMD_ADD.compareTo(xDMAgent2.m_szCmd) == 0) {
                    int xdmAgentCmdAdd = xdmAgentCmdAdd(xDMAgent2.m_AddCmd, true, xDMParserStatus);
                    if (XDMInterface.STATUS_ATOMIC_FAILED.compareTo(xDMParserStatus.m_szData) == 0) {
                        xDMWorkspace.atomicFlag = true;
                    }
                    if (xdmAgentCmdAdd != 0) {
                        Log.E("Add failed");
                        return -1;
                    }
                } else if (XDMInterface.CMD_DELETE.compareTo(xDMAgent2.m_szCmd) == 0) {
                    int xdmAgentCmdDelete = xdmAgentCmdDelete(xDMAgent2.m_DeleteCmd, true, xDMParserStatus);
                    if (XDMInterface.STATUS_ATOMIC_FAILED.compareTo(xDMParserStatus.m_szData) == 0) {
                        xDMWorkspace.atomicFlag = true;
                    }
                    if (xdmAgentCmdDelete != 0) {
                        Log.E("Delete failed");
                        return -1;
                    }
                } else if (XDMInterface.CMD_REPLACE.compareTo(xDMAgent2.m_szCmd) == 0) {
                    int xdmAgentCmdReplace = xdmAgentCmdReplace(xDMAgent2.m_ReplaceCmd, true, xDMParserStatus);
                    if (XDMInterface.STATUS_ATOMIC_FAILED.compareTo(xDMParserStatus.m_szData) == 0) {
                        xDMWorkspace.atomicFlag = true;
                    }
                    if (xdmAgentCmdReplace != 0) {
                        Log.E("Replace failed");
                        return -1;
                    }
                } else if (XDMInterface.CMD_COPY.compareTo(xDMAgent2.m_szCmd) == 0) {
                    int xdmAgentCmdCopy = xdmAgentCmdCopy(xDMAgent2.m_CopyCmd, true, xDMParserStatus);
                    if (XDMInterface.STATUS_ATOMIC_FAILED.compareTo(xDMParserStatus.m_szData) == 0) {
                        xDMWorkspace.atomicFlag = true;
                    }
                    if (xdmAgentCmdCopy != 0) {
                        Log.E("Copy failed");
                        return -1;
                    }
                } else if ("Atomic_Start".compareTo(xDMAgent2.m_szCmd) == 0) {
                    xDMWorkspace.atomicFlag = true;
                    XDMParserAtomic xDMParserAtomic2 = xDMAgent2.m_Atomic;
                    xdmAgentCmdAtomicBlock(xDMParserAtomic2, xDMParserAtomic2.itemlist);
                } else {
                    Log.E("unknown command");
                }
                i++;
            } else if (XDMInterface.CMD_GET.compareTo(xDMAgent2.m_szCmd) == 0) {
                XDMParserItem xDMParserItem = (XDMParserItem) xDMAgent2.m_Get.itemlist.item;
                if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    xdmAgentBuildCmdStatus6 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_Get.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xdmAgentBuildCmdStatus6 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_Get.cmdid, XDMInterface.CMD_GET, (String) null, (String) null, "404");
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
            } else if (XDMInterface.CMD_EXEC.compareTo(xDMAgent2.m_szCmd) == 0) {
                XDMParserItem xDMParserItem2 = (XDMParserItem) xDMAgent2.m_Exec.itemlist.item;
                if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                    xdmAgentBuildCmdStatus5 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_Exec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xdmAgentBuildCmdStatus5 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_Exec.cmdid, XDMInterface.CMD_EXEC, (String) null, (String) null, "404");
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
            } else if (XDMInterface.CMD_ADD.compareTo(xDMAgent2.m_szCmd) == 0) {
                XDMParserItem xDMParserItem3 = (XDMParserItem) xDMAgent2.m_AddCmd.itemlist.item;
                if (!TextUtils.isEmpty(xDMParserItem3.m_szTarget)) {
                    xdmAgentBuildCmdStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_AddCmd.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem3.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xdmAgentBuildCmdStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_AddCmd.cmdid, XDMInterface.CMD_ADD, (String) null, (String) null, "404");
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
            } else if (XDMInterface.CMD_DELETE.compareTo(xDMAgent2.m_szCmd) == 0) {
                XDMParserItem xDMParserItem4 = (XDMParserItem) xDMAgent2.m_DeleteCmd.itemlist.item;
                if (!TextUtils.isEmpty(xDMParserItem4.m_szTarget)) {
                    xdmAgentBuildCmdStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_DeleteCmd.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem4.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xdmAgentBuildCmdStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_DeleteCmd.cmdid, XDMInterface.CMD_DELETE, (String) null, (String) null, "404");
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
            } else if (XDMInterface.CMD_REPLACE.compareTo(xDMAgent2.m_szCmd) == 0) {
                XDMParserItem xDMParserItem5 = (XDMParserItem) xDMAgent2.m_ReplaceCmd.itemlist.item;
                if (!TextUtils.isEmpty(xDMParserItem5.m_szTarget)) {
                    xdmAgentBuildCmdStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_ReplaceCmd.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem5.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xdmAgentBuildCmdStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_ReplaceCmd.cmdid, XDMInterface.CMD_REPLACE, (String) null, (String) null, "404");
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
            } else if (XDMInterface.CMD_COPY.compareTo(xDMAgent2.m_szCmd) == 0) {
                XDMParserItem xDMParserItem6 = (XDMParserItem) xDMAgent2.m_CopyCmd.itemlist.item;
                if (!TextUtils.isEmpty(xDMParserItem6.m_szTarget)) {
                    xdmAgentBuildCmdStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_CopyCmd.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem6.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xdmAgentBuildCmdStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMAgent2.m_CopyCmd.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, "404");
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
            } else {
                Log.I("unknown command");
            }
            xDMAgent2 = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
        }
        XDMLinkedList.xdmListClearLinkedList(xDMParserAtomic.itemlist);
        return i;
    }

    private int xdmAgentCmdGet(XDMParserGet xDMParserGet, boolean z) {
        String str;
        XDMParserStatus xDMParserStatus;
        XDMParserStatus xDMParserStatus2;
        XDMParserStatus xDMParserStatus3;
        XDMParserGet xDMParserGet2 = xDMParserGet;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        int i = 100;
        String[] strArr = new String[100];
        boolean xdmAgentCmdUicAlert = xdmAgentCmdUicAlert();
        XDMList xDMList = xDMParserGet2.itemlist;
        while (xDMList != null) {
            XDMParserItem xDMParserItem = (XDMParserItem) xDMList.item;
            if (xDMWorkspace.serverAuthState != 1) {
                if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, xDMWorkspace.m_szStatusReturnCode);
                } else {
                    xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, (String) null, xDMWorkspace.m_szStatusReturnCode);
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus3);
                xDMList = xDMList.next;
            } else if (TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, (String) null, "404"));
                xDMList = xDMList.next;
            } else if (!xdmAgentCmdUicAlert) {
                if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                }
                if (xDMParserStatus2 != null) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus2);
                }
                xDMList = xDMList.next;
            } else if (z) {
                if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, xDMParserItem.m_szTarget, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                }
                if (xDMParserStatus != null) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
                }
                xDMList = xDMList.next;
            } else if (!TextUtils.isEmpty(XDMMem.xdmLibStrstr(xDMParserItem.m_szTarget, "?"))) {
                xdmAgentCmdProp(XDMInterface.CMD_GET, xDMParserItem, xDMParserGet2);
                xDMList = xDMList.next;
            } else {
                XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem.m_szTarget);
                if (xdmOmGetNodeProp == null) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, "404"));
                    xDMList = xDMList.next;
                } else if (!xdmAgentIsAccessibleNode(xDMParserItem.m_szTarget)) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, "405"));
                    xDMList = xDMList.next;
                } else if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp, 8)) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
                    xDMList = xDMList.next;
                } else {
                    if (xdmOmGetNodeProp.vaddr >= 0 || xdmOmGetNodeProp.size > 0) {
                        XDMVnode xDMVnode = xdmOmGetNodeProp;
                        if (xDMVnode.size > xDMWorkspace.serverMaxObjSize) {
                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, "413"));
                            xDMList = xDMList.next;
                        } else {
                            int i2 = xDMVnode.size;
                            String xdmOmGetFormatString = XDMOmList.xdmOmGetFormatString(xDMVnode.format);
                            char[] cArr = new char[xDMVnode.size];
                            if (cArr.length == 0) {
                                XDMParserStatus xdmAgentBuildCmdStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                                if (xdmAgentBuildCmdStatus != null) {
                                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xdmAgentBuildCmdStatus);
                                }
                                xDMList = xDMList.next;
                            } else {
                                char[] cArr2 = cArr;
                                int i3 = i2;
                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, "200"));
                                String valueOf = (xDMVnode.type == null || xDMVnode.type.data == null) ? null : String.valueOf(xDMVnode.type.data);
                                XDMOmLib.xdmOmRead(xDMOmTree, xDMParserItem.m_szTarget, 0, cArr2, xDMVnode.size);
                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet2.cmdid, xDMParserItem.m_szTarget, xdmOmGetFormatString, valueOf, i3, cArr2));
                                Log.H("item.target = " + xDMParserItem.m_szTarget);
                                Log.H("item.data = " + new String(cArr2));
                            }
                        }
                    } else {
                        XDMVnode xDMVnode2 = xdmOmGetNodeProp;
                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet2.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem.m_szTarget, "200"));
                        int xdmOmGetChild = XDMOmLib.xdmOmGetChild(xDMOmTree, xDMParserItem.m_szTarget, strArr, i);
                        String xdmOmGetFormatString2 = XDMOmList.xdmOmGetFormatString(xDMVnode2.format);
                        String valueOf2 = (xDMVnode2.type == null || xDMVnode2.type.data == null) ? null : String.valueOf(xDMVnode2.type.data);
                        if (xdmOmGetChild > 0) {
                            str = strArr[0];
                            for (int i4 = 1; i4 < xdmOmGetChild; i4++) {
                                str = str.concat("/").concat(strArr[i4]);
                            }
                        } else {
                            str = "";
                        }
                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet2.cmdid, xDMParserItem.m_szTarget, xdmOmGetFormatString2, valueOf2, 0, str.toCharArray()));
                    }
                    xDMList = xDMList.next;
                    i = 100;
                }
            }
        }
        return 0;
    }

    private boolean xdmAgentCmdUicAlert() {
        XDMParserStatus xDMParserStatus;
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (xDMWorkspace.uicAlert != null) {
            if (xDMWorkspace.uicFlag == XUICInterface.XUICFlag.UIC_TRUE || xDMWorkspace.uicFlag == XUICInterface.XUICFlag.UIC_NONE) {
                xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMWorkspace.uicAlert.cmdid, XDMInterface.CMD_ALERT, (String) null, (String) null, "200");
                if (xDMWorkspace.uicData != null) {
                    xDMParserStatus.itemlist = xDMWorkspace.uicData;
                }
            } else if (xDMWorkspace.uicFlag == XUICInterface.XUICFlag.UIC_FALSE) {
                xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMWorkspace.uicAlert.cmdid, XDMInterface.CMD_ALERT, (String) null, (String) null, XDMInterface.STATUS_NOT_MODIFIED);
                if (xDMWorkspace.uicData != null) {
                    xDMParserStatus.itemlist = xDMWorkspace.uicData;
                }
            } else if (xDMWorkspace.uicFlag == XUICInterface.XUICFlag.UIC_CANCELED) {
                xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMWorkspace.uicAlert.cmdid, XDMInterface.CMD_ALERT, (String) null, (String) null, XDMInterface.STATUS_OPERATION_CANCELLED);
                if (xDMWorkspace.uicData != null) {
                    xDMParserStatus.itemlist = xDMWorkspace.uicData;
                }
            } else {
                xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMWorkspace.uicAlert.cmdid, XDMInterface.CMD_ALERT, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                if (xDMWorkspace.uicData != null) {
                    xDMParserStatus.itemlist = xDMWorkspace.uicData;
                }
            }
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
            XDMHandleCmd.xdmAgentDataStDeleteAlert(xDMWorkspace.uicAlert);
            xDMWorkspace.uicData = null;
            xDMWorkspace.uicAlert = null;
        }
        return xDMWorkspace.uicFlag == XUICInterface.XUICFlag.UIC_TRUE || xDMWorkspace.uicFlag == XUICInterface.XUICFlag.UIC_NONE;
    }

    private int xdmAgentCmdProp(String str, XDMParserItem xDMParserItem, Object obj) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        String str2 = xDMParserItem.m_szTarget;
        if (XDMInterface.CMD_GET.compareTo(str) == 0) {
            XDMParserGet xDMParserGet = (XDMParserGet) obj;
            if (TextUtils.isEmpty(str2)) {
                Log.I("ptr is null");
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, str, (String) null, xDMParserItem.m_szTarget, "405"));
                return 0;
            }
            Log.I("ptr = " + str2);
            char[] cArr = new char[str2.length()];
            String xdmLibStrsplit = XDMMem.xdmLibStrsplit(str2.toCharArray(), '?', cArr);
            if (TextUtils.isEmpty(xdmLibStrsplit)) {
                Log.I("ptr is null");
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, str, (String) null, xDMParserItem.m_szTarget, "405"));
                return 0;
            }
            Log.I("ptr = " + xdmLibStrsplit);
            char[] cArr2 = new char[xdmLibStrsplit.length()];
            String xdmLibStrsplit2 = XDMMem.xdmLibStrsplit(xdmLibStrsplit.toCharArray(), '=', cArr2);
            if (TextUtils.isEmpty(xdmLibStrsplit2)) {
                Log.I("ptr is null");
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, str, (String) null, xDMParserItem.m_szTarget, "405"));
                return 0;
            }
            Log.I("ptr = " + xdmLibStrsplit2);
            return xdmAgentCmdPropGet(xDMWorkspace, xDMParserItem, xdmLibStrsplit2, cArr2, cArr, obj);
        } else if (XDMInterface.CMD_REPLACE.compareTo(str) == 0) {
            XDMParserReplace xDMParserReplace = (XDMParserReplace) obj;
            if (TextUtils.isEmpty(str2)) {
                Log.I("ptr is null");
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, str, (String) null, xDMParserItem.m_szTarget, "405"));
                return 0;
            }
            Log.I("ptr = " + str2);
            char[] cArr3 = new char[str2.length()];
            String xdmLibStrsplit3 = XDMMem.xdmLibStrsplit(str2.toCharArray(), '?', cArr3);
            if (TextUtils.isEmpty(xdmLibStrsplit3)) {
                Log.I("ptr is null");
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, str, (String) null, xDMParserItem.m_szTarget, "405"));
                return 0;
            }
            Log.I("ptr = " + xdmLibStrsplit3);
            char[] cArr4 = new char[xdmLibStrsplit3.length()];
            String xdmLibStrsplit4 = XDMMem.xdmLibStrsplit(xdmLibStrsplit3.toCharArray(), '=', cArr4);
            Log.I(String.valueOf(cArr4) + ":" + String.valueOf(xdmLibStrsplit4));
            if (TextUtils.isEmpty(xdmLibStrsplit4)) {
                Log.I("ptr is null");
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, str, (String) null, xDMParserItem.m_szTarget, "405"));
                return 0;
            }
            Log.I("ptr = " + xdmLibStrsplit4);
            return xdmAgentCmdPropReplace(xDMWorkspace, xDMParserItem, xdmLibStrsplit4, cArr3, obj);
        } else {
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, ((XDMParserGet) obj).cmdid, str, (String) null, xDMParserItem.m_szTarget, "405"));
            return 0;
        }
    }

    private int xdmAgentCmdPropGet(XDMWorkspace xDMWorkspace, XDMParserItem xDMParserItem, String str, char[] cArr, char[] cArr2, Object obj) {
        XDMWorkspace xDMWorkspace2 = xDMWorkspace;
        XDMParserItem xDMParserItem2 = xDMParserItem;
        String str2 = str;
        XDMOmTree xDMOmTree = xDMWorkspace2.om;
        XDMParserGet xDMParserGet = (XDMParserGet) obj;
        int xdbGetFileIdTNDS = XDB.xdbGetFileIdTNDS();
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMMem.xdmLibCharToString(cArr2));
        if (xdmOmGetNodeProp == null) {
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "404"));
            return 0;
        }
        String xdmLibCharToString = XDMMem.xdmLibCharToString(cArr);
        if (xdmLibCharToString == null || "list".compareTo(xdmLibCharToString) != 0) {
            if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp, 8)) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
                return 0;
            }
            char[] cArr3 = null;
            if ("ACL".compareTo(str2) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "200"));
                String xdmAgentGetAclStr = xdmAgentGetAclStr(xdmOmGetNodeProp.acl, xDMParserItem2);
                if (!TextUtils.isEmpty(xdmAgentGetAclStr)) {
                    cArr3 = xdmAgentGetAclStr.toCharArray();
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet.cmdid, xDMParserItem2.m_szTarget, "chr", XDMInterface.MIMETYPE_TEXT_PLAIN, 0, cArr3));
                return 0;
            }
            if ("Format".compareTo(str2) == 0) {
                String xdmOmGetFormatString = XDMOmList.xdmOmGetFormatString(xdmOmGetNodeProp.format);
                if (!TextUtils.isEmpty(xdmOmGetFormatString)) {
                    cArr3 = xdmOmGetFormatString.toCharArray();
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "200"));
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet.cmdid, xDMParserItem2.m_szTarget, "chr", XDMInterface.MIMETYPE_TEXT_PLAIN, 0, cArr3));
            } else if ("Type".compareTo(str2) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "200"));
                if (xdmOmGetNodeProp.type == null || xdmOmGetNodeProp.type.data == null) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet.cmdid, xDMParserItem2.m_szTarget, "", (String) null, 0, (char[]) null));
                } else {
                    String valueOf = String.valueOf(xdmOmGetNodeProp.type.data);
                    if (!TextUtils.isEmpty(valueOf)) {
                        cArr3 = valueOf.toCharArray();
                    }
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet.cmdid, xDMParserItem2.m_szTarget, "chr", valueOf, 0, cArr3));
                }
            } else if ("Size".compareTo(str2) == 0) {
                if (xdmOmGetNodeProp.vaddr < 0 || xdmOmGetNodeProp.size <= 0) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "406"));
                } else {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "200"));
                    String valueOf2 = String.valueOf(xdmOmGetNodeProp.size - 1);
                    if (!TextUtils.isEmpty(valueOf2)) {
                        cArr3 = valueOf2.toCharArray();
                    }
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet.cmdid, xDMParserItem2.m_szTarget, "chr", XDMInterface.MIMETYPE_TEXT_PLAIN, 0, cArr3));
                }
            } else if ("Name".compareTo(str2) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "200"));
                String str3 = xdmOmGetNodeProp.m_szName;
                if (!TextUtils.isEmpty(str3)) {
                    cArr3 = str3.toCharArray();
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet.cmdid, xDMParserItem2.m_szTarget, "chr", XDMInterface.MIMETYPE_TEXT_PLAIN, 0, cArr3));
            } else if ("Title".compareTo(str2) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "200"));
                String str4 = xdmOmGetNodeProp.title;
                if (!TextUtils.isEmpty(str4)) {
                    cArr3 = str4.toCharArray();
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet.cmdid, xDMParserItem2.m_szTarget, "chr", XDMInterface.MIMETYPE_TEXT_PLAIN, 0, cArr3));
            } else {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "405"));
                return 0;
            }
            return 0;
        } else if (!xdmAgentIsAccessibleNode(xDMParserItem2.m_szTarget)) {
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "405"));
            return 0;
        } else if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp, 8)) {
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
            return 0;
        } else {
            if ("Struct".compareTo(str2) == 0) {
                xdmAgentCmdPropGetStruct(xDMParserGet, xdmOmGetNodeProp, false);
            } else if ("StructData".compareTo(str2) == 0) {
                xdmAgentCmdPropGetStruct(xDMParserGet, xdmOmGetNodeProp, true);
            } else if (str2.contains("TNDS") && !xdmAgentCmdPropGetTnds(xDMParserGet, xDMOmTree, xdmOmGetNodeProp, str2)) {
                XDB.xdbDeleteFile(xdbGetFileIdTNDS);
                XDMLinkedList.xdmListAddObjAtLast(g_DmWs.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(g_DmWs, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "404"));
                return 0;
            }
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserGet.cmdid, XDMInterface.CMD_GET, (String) null, xDMParserItem2.m_szTarget, "200"));
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x005c A[LOOP:0: B:20:0x005a->B:21:0x005c, LOOP_END] */
    private void xdmAgentCmdPropGetStruct(XDMParserGet xDMParserGet, XDMVnode xDMVnode, boolean z) {
        char[] cArr;
        int i;
        String str;
        String str2;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        if (xDMVnode != null) {
            String xdmAgentGetPathFromNode = xdmAgentGetPathFromNode(xDMWorkspace.om, xDMVnode);
            if (!z) {
                str2 = XDMOmList.xdmOmGetFormatString(xDMVnode.format);
            } else if (xDMVnode.vaddr < 0 || xDMVnode.size <= 0) {
                str2 = XDMOmList.xdmOmGetFormatString(xDMVnode.format);
            } else {
                str = xDMVnode.format > 0 ? XDMOmList.xdmOmGetFormatString(xDMVnode.format) : "";
                int i2 = xDMVnode.size;
                char[] cArr2 = new char[i2];
                XDMOmLib.xdmOmRead(xDMOmTree, xdmAgentGetPathFromNode, 0, cArr2, i2);
                cArr = cArr2;
                i = xDMVnode.size;
                if (xdmAgentIsAccessibleNode(xDMVnode.m_szName)) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.resultsList, XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet.cmdid, xdmAgentGetPathFromNode, str, "", i, cArr));
                }
                for (XDMVnode xDMVnode2 = xDMVnode.childlist; xDMVnode2 != null; xDMVnode2 = xDMVnode2.next) {
                    xdmAgentCmdPropGetStruct(xDMParserGet, xDMVnode2, z);
                }
            }
            cArr = null;
            i = 0;
            str = str2;
            if (xdmAgentIsAccessibleNode(xDMVnode.m_szName)) {
            }
            while (xDMVnode2 != null) {
            }
        }
    }

    private String xdmAgentGetPathFromNode(XDMOmTree xDMOmTree, XDMVnode xDMVnode) {
        String[] strArr = new String[10];
        XDMVnode xdmOmVfsGetParent = XDMOmLib.xdmOmVfsGetParent(xDMOmTree.vfs, xDMOmTree.vfs.root, xDMVnode);
        int i = 0;
        while (xdmOmVfsGetParent != null && xdmOmVfsGetParent != xDMOmTree.vfs.root) {
            strArr[i] = xdmOmVfsGetParent.m_szName;
            i++;
            xdmOmVfsGetParent = XDMOmLib.xdmOmVfsGetParent(xDMOmTree.vfs, xDMOmTree.vfs.root, xdmOmVfsGetParent);
        }
        if (xdmOmVfsGetParent == null) {
            return XDMInterface.XDM_BASE_PATH;
        }
        String str = "./";
        for (int i2 = i - 1; i2 >= 0; i2--) {
            str = str.concat(strArr[i2]).concat("/");
        }
        return (str.compareTo("/") == 0 && str.compareTo("./") == 0) ? str : str.concat(xDMVnode.m_szName);
    }

    private int xdmAgentCmdPropReplace(XDMWorkspace xDMWorkspace, XDMParserItem xDMParserItem, String str, char[] cArr, Object obj) {
        XDMWorkspace xDMWorkspace2 = xDMWorkspace;
        XDMParserItem xDMParserItem2 = xDMParserItem;
        String str2 = str;
        XDMOmTree xDMOmTree = xDMWorkspace2.om;
        XDMParserReplace xDMParserReplace = (XDMParserReplace) obj;
        String xdmLibCharToString = XDMMem.xdmLibCharToString(cArr);
        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xdmLibCharToString);
        if (xdmOmGetNodeProp == null) {
            Log.I("!node");
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "404"));
            return 0;
        } else if (xdmAgentIsPermanentNode(xDMOmTree, xdmLibCharToString)) {
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "405"));
            return 0;
        } else if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp, 16)) {
            Log.I("!XDM_OMACL_REPLACE");
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
            return 0;
        } else {
            if ("ACL".compareTo(str) == 0) {
                Log.I("ACL");
                if (xdmOmGetNodeProp.format == 6 || XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp.ptParentNode, 16)) {
                    String xdmAgentDataStGetString = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem2.data);
                    if (TextUtils.isEmpty(xdmAgentDataStGetString)) {
                        xdmAgentDataStGetString = String.valueOf(xDMParserItem2.data.data);
                    }
                    XDMOmList xdmAgentMakeAcl = xdmAgentMakeAcl((XDMOmList) null, xdmAgentDataStGetString);
                    XDMOmList.xdmOmDeleteAclList(xdmOmGetNodeProp.acl);
                    xdmOmGetNodeProp.acl = xdmAgentMakeAcl;
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "200"));
                } else {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
                    Log.I("STATUS_COMMAND_NOT_ALLOWED=" + String.valueOf(cArr));
                    return 0;
                }
            } else if ("Format".compareTo(str) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "405"));
            } else if ("Type".compareTo(str) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "405"));
            } else if ("Size".compareTo(str) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "405"));
            } else if ("Name".compareTo(str) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "405"));
            } else if ("Title".compareTo(str) == 0) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "200"));
                xdmOmGetNodeProp.title = null;
                xdmOmGetNodeProp.title = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem2.data);
                if (!(!TextUtils.isEmpty(xdmOmGetNodeProp.title) || xDMParserItem2.data == null || xDMParserItem2.data.data == null)) {
                    xdmOmGetNodeProp.title = String.valueOf(xDMParserItem2.data.data);
                }
            } else {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace2.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem2.m_szTarget, "405"));
            }
            return 0;
        }
    }

    private void xdmAgentMakeTndsSubTree(XDMOmTree xDMOmTree, XDMVnode xDMVnode, int i, String str) {
        XDMOmAcl xDMOmAcl;
        int i2;
        String str2;
        boolean z;
        new XDMOmList();
        if (xDMVnode != null) {
            int xdbGetFileIdTNDS = XDB.xdbGetFileIdTNDS();
            String str3 = XDMDDFXmlHandler.g_szDmXmlTagString[4];
            if (!TextUtils.isEmpty(str)) {
                String str4 = XDMDDFXmlHandler.g_szDmXmlTagString[8];
                Log.I("szTag : " + str4);
                String concat = str3.concat(str4);
                Log.I("szPath : " + str);
                String concat2 = concat.concat(str);
                String str5 = XDMDDFXmlHandler.g_szDmXmlTagString[9];
                Log.I("szTag" + str5);
                str3 = concat2.concat(str5);
            }
            if (!TextUtils.isEmpty(xDMVnode.m_szName)) {
                String concat3 = str3.concat(XDMDDFXmlHandler.g_szDmXmlTagString[6]).concat(xDMVnode.m_szName);
                Log.I("node.name : " + xDMVnode.m_szName);
                str3 = concat3.concat(XDMDDFXmlHandler.g_szDmXmlTagString[7]);
            }
            String xdmOmGetFormatString = XDMOmList.xdmOmGetFormatString(xDMVnode.format);
            if (!(TextUtils.isEmpty(xdmOmGetFormatString) && xDMVnode.acl == null && xDMVnode.type == null)) {
                String concat4 = str3.concat(XDMDDFXmlHandler.g_szDmXmlTagString[12]);
                if ((i & 2) == 2 && !TextUtils.isEmpty(xdmOmGetFormatString)) {
                    concat4 = concat4.concat(XDMDDFXmlHandler.g_szDmXmlTagString[16]).concat("<").concat(xdmOmGetFormatString).concat("/>").concat(XDMDDFXmlHandler.g_szDmXmlTagString[17]);
                }
                if ((i & 4) == 4 && xDMVnode.type != null) {
                    String str6 = (String) xDMVnode.type.data;
                    if (!TextUtils.isEmpty(str6)) {
                        concat4 = concat4.concat(XDMDDFXmlHandler.g_szDmXmlTagString[18]).concat("<MIME>").concat(str6).concat("</MIME>").concat(XDMDDFXmlHandler.g_szDmXmlTagString[19]);
                    }
                }
                if (!((i & 1) != 1 || xDMVnode.acl == null || (xDMOmAcl = (XDMOmAcl) xDMVnode.acl.data) == null || (i2 = xDMOmAcl.ac) == 0)) {
                    String concat5 = concat4.concat(XDMDDFXmlHandler.g_szDmXmlTagString[14]);
                    if ((i2 & 1) == 1) {
                        str2 = concat5.concat("Add=*");
                        z = true;
                    } else {
                        str2 = concat5;
                        z = false;
                    }
                    if ((i2 & 2) == 2) {
                        if (z) {
                            str2 = str2.concat("&amp;Delete=*");
                        } else {
                            str2 = str2.concat("Delete=*");
                            z = true;
                        }
                    }
                    if ((i2 & 4) == 4) {
                        if (z) {
                            str2 = str2.concat("&amp;Exec=*");
                        } else {
                            str2 = str2.concat("Exec=*");
                            z = true;
                        }
                    }
                    if ((i2 & 8) == 8) {
                        if (z) {
                            str2 = str2.concat("&amp;Get=*");
                        } else {
                            str2 = str2.concat("Get=*");
                            z = true;
                        }
                    }
                    if ((i2 & 16) == 16) {
                        if (z) {
                            str2 = str2.concat("&amp;Replace=*");
                        } else {
                            str2 = str2.concat("Replace=*");
                        }
                    }
                    concat4 = str2.concat(XDMDDFXmlHandler.g_szDmXmlTagString[15]);
                }
                str3 = concat4.concat(XDMDDFXmlHandler.g_szDmXmlTagString[13]);
            }
            if ((i & 8) != 8) {
                XDB.xdbAppendFile(xdbGetFileIdTNDS, str3.getBytes(Charset.defaultCharset()));
            } else if (xDMVnode.size > 0) {
                XDB.xdbAppendFile(xdbGetFileIdTNDS, str3.concat(XDMDDFXmlHandler.g_szDmXmlTagString[10]).getBytes(Charset.defaultCharset()));
                String xdmAgentGetPathFromNode = xdmAgentGetPathFromNode(xDMOmTree, xDMVnode);
                char[] cArr = new char[xDMVnode.size];
                int xdmOmRead = XDMOmLib.xdmOmRead(xDMOmTree, xdmAgentGetPathFromNode, 0, cArr, xDMVnode.size);
                String valueOf = String.valueOf(cArr);
                if (xdmOmRead > 0) {
                    XDB.xdbAppendFile(xdbGetFileIdTNDS, valueOf.getBytes(Charset.defaultCharset()));
                }
                XDB.xdbAppendFile(xdbGetFileIdTNDS, XDMDDFXmlHandler.g_szDmXmlTagString[11].getBytes(Charset.defaultCharset()));
            } else {
                XDB.xdbAppendFile(xdbGetFileIdTNDS, str3.getBytes(Charset.defaultCharset()));
            }
            for (XDMVnode xDMVnode2 = xDMVnode.childlist; xDMVnode2 != null; xDMVnode2 = xDMVnode2.next) {
                xdmAgentMakeTndsSubTree(xDMOmTree, xDMVnode2, i, (String) null);
            }
            XDB.xdbAppendFile(xdbGetFileIdTNDS, XDMDDFXmlHandler.g_szDmXmlTagString[5].getBytes(Charset.defaultCharset()));
        }
    }

    private boolean xdmAgentCmdPropGetTnds(XDMParserGet xDMParserGet, XDMOmTree xDMOmTree, XDMVnode xDMVnode, String str) {
        int i;
        XDMParserResults xDMParserResults;
        int i2;
        XDMParserGet xDMParserGet2 = xDMParserGet;
        XDMOmTree xDMOmTree2 = xDMOmTree;
        XDMVnode xDMVnode2 = xDMVnode;
        String str2 = str;
        XDMWorkspace xDMWorkspace = g_DmWs;
        Log.I("");
        if (xDMVnode2 == null || xDMVnode2.childlist == null) {
            return false;
        }
        int xdbGetFileIdTNDS = XDB.xdbGetFileIdTNDS();
        String[] split = str2.split("\\+");
        String str3 = split[0];
        Log.I("token : " + str3);
        String str4 = str3;
        if (split.length > 1) {
            String str5 = str4;
            i = 0;
            for (int i3 = 1; i3 < split.length; i3++) {
                if ("ACL".compareTo(str5) == 0) {
                    i2 = i | 1;
                } else if ("Format".compareTo(str5) == 0) {
                    i2 = i | 2;
                } else if ("Type".compareTo(str5) == 0) {
                    i2 = i | 4;
                } else if ("Value".compareTo(str5) == 0) {
                    i2 = i | 8;
                } else {
                    str5 = split[i3];
                }
                i = i2;
                str5 = split[i3];
            }
        } else {
            String[] split2 = str2.split("-");
            if (split2 != null) {
                String str6 = split2[0];
                Log.I("token : " + str6);
                if ("ACL".compareTo(str6) == 0) {
                    i = 14;
                } else if ("Format".compareTo(str6) == 0) {
                    i = 13;
                } else if ("Type".compareTo(str6) == 0) {
                    i = 11;
                } else if ("Value".compareTo(str6) == 0) {
                    i = 7;
                }
            }
            i = 15;
        }
        XDB.xdbDeleteFile(xdbGetFileIdTNDS);
        XDB.xdbAppendFile(xdbGetFileIdTNDS, "".concat(XDMDDFXmlHandler.g_szDmXmlTagString[34]).concat(XDMDDFXmlHandler.g_szDmXmlTagString[0]).concat(XDMDDFXmlHandler.g_szDmXmlTagString[2]).concat("1.2").concat(XDMDDFXmlHandler.g_szDmXmlTagString[3]).getBytes(Charset.defaultCharset()));
        String xdmAgentGetPathFromNode = xdmAgentGetPathFromNode(xDMOmTree2, xDMVnode2);
        char[] cArr = new char[xdmAgentGetPathFromNode.length()];
        XDMOmLib.xdmOmMakeParentPath(xdmAgentGetPathFromNode, cArr);
        Log.I("tempPath : " + XDMMem.xdmLibCharToString(cArr));
        xdmAgentMakeTndsSubTree(xDMOmTree2, xDMVnode2, i, XDMMem.xdmLibCharToString(cArr));
        XDB.xdbAppendFile(xdbGetFileIdTNDS, XDMDDFXmlHandler.g_szDmXmlTagString[1].getBytes(Charset.defaultCharset()));
        XDB.xdbAppendFile(xdbGetFileIdTNDS, XDMDDFXmlHandler.g_szDmXmlTagString[35].getBytes(Charset.defaultCharset()));
        int xdbGetFileSize = (int) XDB.xdbGetFileSize(xdbGetFileIdTNDS);
        String str7 = new String((byte[]) XDB.xdbReadFile(xdbGetFileIdTNDS, 0, xdbGetFileSize), Charset.defaultCharset());
        String xdmAgentGetPathFromNode2 = xdmAgentGetPathFromNode(xDMOmTree2, xDMVnode2);
        String xdmOmGetFormatString = XDMOmList.xdmOmGetFormatString(8);
        Log.H("name : " + xdmAgentGetPathFromNode2);
        if (TextUtils.isEmpty(str7)) {
            Log.E("_____ TNDSResults File Read Error!");
            xDMParserResults = XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet2.cmdid, xdmAgentGetPathFromNode2, xdmOmGetFormatString, XDMInterface.SYNCML_MIME_TYPE_TNDS_XML, xdbGetFileSize, (char[]) null);
        } else {
            xDMParserResults = XDMBuildCmd.xdmAgentBuildCmdDetailResults(xDMWorkspace, xDMParserGet2.cmdid, xdmAgentGetPathFromNode2, xdmOmGetFormatString, XDMInterface.SYNCML_MIME_TYPE_TNDS_XML, xdbGetFileSize, str7.toCharArray());
        }
        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.resultsList, xDMParserResults);
        return true;
    }

    private String xdmAgentGetAclStr(XDMOmList xDMOmList, XDMParserItem xDMParserItem) {
        String str = "\u0000";
        String[] strArr = {str, str, str, str, str};
        if (xDMOmList == null) {
            return null;
        }
        if (xDMParserItem.meta == null) {
            xDMParserItem.meta = null;
        } else if (TextUtils.isEmpty(xDMParserItem.meta.m_szFormat)) {
            xDMParserItem.meta.m_szFormat = null;
        } else {
            Log.I("item->meta !NULL");
        }
        while (xDMOmList != null) {
            XDMOmAcl xDMOmAcl = (XDMOmAcl) xDMOmList.data;
            if ((xDMOmAcl.ac & 1) > 0) {
                if (strArr[0].charAt(0) == 0) {
                    strArr[0] = xDMOmAcl.m_szServerid;
                } else {
                    strArr[0] = strArr[0].concat(xDMOmAcl.m_szServerid);
                }
            }
            if ((xDMOmAcl.ac & 2) > 0) {
                if (strArr[1].charAt(0) == 0) {
                    strArr[1] = xDMOmAcl.m_szServerid;
                } else {
                    strArr[1] = strArr[1].concat(xDMOmAcl.m_szServerid);
                }
            }
            if ((xDMOmAcl.ac & 4) > 0) {
                if (strArr[2].charAt(0) == 0) {
                    strArr[2] = xDMOmAcl.m_szServerid;
                } else {
                    strArr[2] = strArr[2].concat(xDMOmAcl.m_szServerid);
                }
            }
            if ((xDMOmAcl.ac & 8) > 0) {
                if (strArr[3].charAt(0) == 0) {
                    strArr[3] = xDMOmAcl.m_szServerid;
                } else {
                    strArr[3] = strArr[3].concat(xDMOmAcl.m_szServerid);
                }
            }
            if ((xDMOmAcl.ac & 16) > 0) {
                if (strArr[4].charAt(0) == 0) {
                    strArr[4] = xDMOmAcl.m_szServerid;
                } else {
                    strArr[4] = strArr[4].concat(xDMOmAcl.m_szServerid);
                }
            }
            xDMOmList = xDMOmList.next;
        }
        for (int i = 0; i < 5; i++) {
            if (i == 0 && strArr[i].charAt(0) != 0) {
                if (str.charAt(0) != 0) {
                    if (xDMParserItem.meta == null) {
                        str = str.concat("&");
                    } else if (TextUtils.isEmpty(xDMParserItem.meta.m_szFormat)) {
                        str = str.concat("&");
                    } else if ("xml".compareTo(xDMParserItem.meta.m_szFormat) == 0) {
                        str = str.concat("&amp;");
                    } else {
                        str = str.concat("&");
                    }
                }
                if (str.charAt(0) != 0) {
                    str = str.concat("Add=");
                } else {
                    str = "Add=";
                }
            }
            if (i == 1 && strArr[i].charAt(0) != 0) {
                if (str.charAt(0) != 0) {
                    if (xDMParserItem.meta == null) {
                        str = str.concat("&");
                    } else if (TextUtils.isEmpty(xDMParserItem.meta.m_szFormat)) {
                        str = str.concat("&");
                    } else if ("xml".compareTo(xDMParserItem.meta.m_szFormat) == 0) {
                        str = str.concat("&amp;");
                    } else {
                        str = str.concat("&");
                    }
                }
                if (str.charAt(0) != 0) {
                    str = str.concat("Delete=");
                } else {
                    str = "Delete=";
                }
            }
            if (i == 2 && strArr[i].charAt(0) != 0) {
                if (str.charAt(0) != 0) {
                    if (xDMParserItem.meta == null) {
                        str = str.concat("&");
                    } else if (TextUtils.isEmpty(xDMParserItem.meta.m_szFormat)) {
                        str = str.concat("&");
                    } else if ("xml".compareTo(xDMParserItem.meta.m_szFormat) == 0) {
                        str = str.concat("&amp;");
                    } else {
                        str = str.concat("&");
                    }
                }
                if (str.charAt(0) != 0) {
                    str = str.concat("Exec=");
                } else {
                    str = "Exec=";
                }
            }
            if (i == 3 && strArr[i].charAt(0) != 0) {
                if (str.charAt(0) != 0) {
                    if (xDMParserItem.meta == null) {
                        str = str.concat("&");
                    } else if (TextUtils.isEmpty(xDMParserItem.meta.m_szFormat)) {
                        str = str.concat("&");
                    } else if ("xml".compareTo(xDMParserItem.meta.m_szFormat) == 0) {
                        str = str.concat("&amp;");
                    } else {
                        str = str.concat("&");
                    }
                }
                if (str.charAt(0) != 0) {
                    str = str.concat("Get=");
                } else {
                    str = "Get=";
                }
            }
            if (i == 4 && strArr[i].charAt(0) != 0) {
                if (str.charAt(0) != 0) {
                    if (xDMParserItem.meta == null) {
                        str = str.concat("&");
                    } else if (TextUtils.isEmpty(xDMParserItem.meta.m_szFormat)) {
                        str = str.concat("&");
                    } else if ("xml".compareTo(xDMParserItem.meta.m_szFormat) == 0) {
                        str = str.concat("&amp;");
                    } else {
                        str = str.concat("&");
                    }
                }
                if (str.charAt(0) != 0) {
                    str = str.concat("Replace=");
                } else {
                    str = "Replace=";
                }
            }
            if (strArr[i].charAt(0) != 0) {
                str = str.concat(strArr[i]);
            }
        }
        return str;
    }

    private XDMOmList xdmAgentMakeAcl(XDMOmList xDMOmList, String str) {
        char[] cArr = new char[str.length()];
        String xdmLibStrsplit = XDMMem.xdmLibStrsplit(str.toCharArray(), '&', cArr);
        String str2 = null;
        while (true) {
            if (TextUtils.isEmpty(xdmLibStrsplit)) {
                cArr[cArr.length - 1] = 0;
            }
            String xdmLibCharToString = XDMMem.xdmLibCharToString(cArr);
            if (xdmLibCharToString != null) {
                char[] cArr2 = new char[xdmLibCharToString.length()];
                xdmLibCharToString = XDMMem.xdmLibStrsplit(xdmLibCharToString.toCharArray(), '=', cArr2);
                str2 = XDMMem.xdmLibCharToString(cArr2);
            }
            if (!TextUtils.isEmpty(xdmLibCharToString)) {
                if (XDMInterface.CMD_ADD.compareTo(str2) == 0) {
                    xDMOmList = xdmAgentAppendAclItem(xDMOmList, xdmLibCharToString, 1);
                } else if (XDMInterface.CMD_DELETE.compareTo(str2) == 0) {
                    xDMOmList = xdmAgentAppendAclItem(xDMOmList, xdmLibCharToString, 2);
                } else if (XDMInterface.CMD_REPLACE.compareTo(str2) == 0) {
                    xDMOmList = xdmAgentAppendAclItem(xDMOmList, xdmLibCharToString, 16);
                } else if (XDMInterface.CMD_GET.compareTo(str2) == 0) {
                    xDMOmList = xdmAgentAppendAclItem(xDMOmList, xdmLibCharToString, 8);
                } else if (XDMInterface.CMD_EXEC.compareTo(str2) == 0) {
                    xDMOmList = xdmAgentAppendAclItem(xDMOmList, xdmLibCharToString, 4);
                }
            }
            if (TextUtils.isEmpty(xdmLibStrsplit)) {
                return xDMOmList;
            }
            if (xdmLibStrsplit.charAt(0) == 'a' && xdmLibStrsplit.charAt(1) == 'm' && xdmLibStrsplit.charAt(2) == 'p' && xdmLibStrsplit.charAt(3) == ';') {
                xdmLibStrsplit = xdmLibStrsplit.substring(4);
            }
            cArr = new char[(xdmLibStrsplit.length() + 1)];
            xdmLibStrsplit = XDMMem.xdmLibStrsplit(xdmLibStrsplit.toCharArray(), '&', cArr);
        }
    }

    private XDMOmList xdmAgentAppendAclItem(XDMOmList xDMOmList, String str, int i) {
        char[] cArr = new char[(str.length() + 1)];
        if (!str.contains("*")) {
            str = XDMMem.xdmLibStrsplit(str.toCharArray(), '+', cArr);
        } else {
            cArr[0] = '*';
            cArr[1] = 0;
        }
        char[] cArr2 = cArr;
        boolean z = false;
        while (!TextUtils.isEmpty(str)) {
            boolean z2 = z;
            XDMOmList xDMOmList2 = xDMOmList;
            while (xDMOmList2 != null && xDMOmList2.data != null) {
                XDMOmAcl xDMOmAcl = (XDMOmAcl) xDMOmList2.data;
                if (xDMOmAcl.m_szServerid.compareTo(String.valueOf(cArr2)) == 0) {
                    xDMOmAcl.ac |= i;
                    z2 = true;
                }
                xDMOmList2 = xDMOmList2.next;
            }
            if (!z2) {
                char[] cArr3 = new char[40];
                XDMOmAcl xDMOmAcl2 = new XDMOmAcl();
                if (!String.valueOf(cArr2).contains("*")) {
                    String.valueOf(cArr2).getChars(0, 39, cArr3, 0);
                } else {
                    cArr3[0] = '*';
                    cArr3[1] = 0;
                }
                xDMOmAcl2.m_szServerid = XDMMem.xdmLibCharToString(cArr3);
                xDMOmAcl2.ac |= i;
                XDMOmList xDMOmList3 = new XDMOmList();
                xDMOmList3.data = xDMOmAcl2;
                xDMOmList3.next = null;
                xDMOmList = XDMOmLib.xdmOmVfsAppendList(xDMOmList, xDMOmList3);
            }
            cArr2 = new char[str.length()];
            str = XDMMem.xdmLibStrsplit(str.toCharArray(), '+', cArr2);
            z = z2;
        }
        return xDMOmList;
    }

    public int xdmAgentStartMgmtSession() {
        int i;
        int i2;
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (xDMWorkspace == null) {
            Log.E("Parsing package failed Abort session " + 0);
            return -1;
        }
        if (xDMWorkspace.procState == XDMInterface.XDMProcessingState.XDM_PROC_NONE) {
            xDMWorkspace.numAction = 0;
            int xdmAgentParsingWbxml = xdmAgentParsingWbxml(xDMWorkspace.buf.toByteArray());
            if (xdmAgentParsingWbxml != 0) {
                Log.E("Parsing package failed Abort session" + xdmAgentParsingWbxml);
                return -1;
            }
        }
        try {
            int xdmAgentHandleCmd = xdmAgentHandleCmd();
            xDMWorkspace.uicFlag = XUICInterface.XUICFlag.UIC_TRUE;
            if (xdmAgentHandleCmd == -4) {
                Log.I("Handling Paused  Processing UIC Command");
                return xdmAgentHandleCmd;
            } else if (xdmAgentHandleCmd == 0) {
                if (xDMWorkspace.dmState != XDMInterface.XDMSyncMLState.XDM_STATE_FINISH) {
                    xDMWorkspace.msgID++;
                }
                if (xDMWorkspace.authState == 1 && xDMWorkspace.serverAuthState == 1) {
                    xDMWorkspace.dmState = XDMInterface.XDMSyncMLState.XDM_STATE_PROCESSING;
                    xDMWorkspace.authCount = 0;
                    Log.I("total action commands = " + xDMWorkspace.numAction);
                    if (xDMWorkspace.numAction != 0 || !xDMWorkspace.isFinal) {
                        int xdmAgentCreatePackage = xdmAgentCreatePackage();
                        if (xdmAgentCreatePackage < 0) {
                            Log.E("xdmAgentCreatePackage failed " + xdmAgentCreatePackage);
                            return -1;
                        }
                        if (XTPAdapter.g_HttpObj[0].nHttpConnection == 1) {
                            try {
                                i2 = this.m_HttpDMAdapter.xtpAdpOpen(0);
                            } catch (Exception e) {
                                Log.E(e.toString());
                                i2 = -2;
                            }
                            if (i2 != 0) {
                                Log.E("XTP_RET_CONNECTION_FAIL");
                                return -2;
                            }
                        }
                        return xdmAgentSendPackage();
                    }
                    XDBProfileListAdp.xdbClearUicResultKeepFlag();
                    int xdbGetFUMOUpdateMechanism = XDBFumoAdp.xdbGetFUMOUpdateMechanism();
                    int xdbGetFUMOStatus = XDBFumoAdp.xdbGetFUMOStatus();
                    Log.I("nStatus :" + xdbGetFUMOStatus);
                    if (xdbGetFUMOUpdateMechanism == 2 && xdbGetFUMOStatus == 10) {
                        Log.I("Now Download Start");
                        return 5;
                    } else if (xdbGetFUMOUpdateMechanism == 1 && xdbGetFUMOStatus == 40) {
                        Log.I("OMA-DM Download Completed");
                        XDBFumoAdp.xdbSetFUMOStatus(50);
                        return 8;
                    } else if (xdbGetFUMOUpdateMechanism == 3 && xdbGetFUMOStatus == 10) {
                        Log.I("XDM_RET_EXEC_ALTERNATIVE_DOWNLOAD Start");
                        return 6;
                    } else if (xdbGetFUMOUpdateMechanism == 3 && (xdbGetFUMOStatus == 50 || xdbGetFUMOStatus == 251)) {
                        Log.I("Now Update Start");
                        return 7;
                    } else {
                        XDBProfileListAdp.xdbSetNotiReSyncMode(0);
                        XDBProfileListAdp.xdbClearUicResultKeepFlag();
                        return 3;
                    }
                } else {
                    xDMWorkspace.authCount++;
                    if (xDMWorkspace.authCount >= 3) {
                        xDMWorkspace.authCount = 0;
                        xDMWorkspace.serverAuthState = -8;
                        Log.E("Authentication Failed Abort");
                        XDBFumoAdp.xdbSetFUMOInitiatedType(0);
                        return -5;
                    }
                    if (xDMWorkspace.authState == 0) {
                        xDMWorkspace.dmState = XDMInterface.XDMSyncMLState.XDM_STATE_PROCESSING;
                    } else {
                        Log.H("Authentication Retry...ws->dmState = " + xDMWorkspace.dmState);
                        if (!(xDMWorkspace.dmState == XDMInterface.XDMSyncMLState.XDM_STATE_CLIENT_INIT_MGMT || xDMWorkspace.dmState == XDMInterface.XDMSyncMLState.XDM_STATE_GENERIC_ALERT || xDMWorkspace.dmState == XDMInterface.XDMSyncMLState.XDM_STATE_GENERIC_ALERT_REPORT)) {
                            xDMWorkspace.dmState = XDMInterface.XDMSyncMLState.XDM_STATE_PROCESSING;
                        }
                    }
                    if (xdmAgentCreatePackage() != 0) {
                        Log.E(BluetoothManagerEnabler.REASON_FAILED);
                        return -1;
                    }
                    if (XTPAdapter.g_HttpObj[0].nHttpConnection == 1) {
                        try {
                            i = this.m_HttpDMAdapter.xtpAdpOpen(0);
                        } catch (Exception e2) {
                            Log.E(e2.toString());
                            i = -2;
                        }
                        if (i != 0) {
                            return -2;
                        }
                    }
                    return xdmAgentSendPackage();
                }
            } else if (xdmAgentHandleCmd != 1) {
                Log.E("Handling Commands failed Abort session " + xdmAgentHandleCmd);
                return -1;
            } else {
                Log.I("XDM_RET_ALERT_SESSION_ABORT");
                XDBFumoAdp.xdbSetFUMOStatus(0);
                XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
                return xdmAgentHandleCmd;
            }
        } catch (XDMOmTreeException e3) {
            Log.E(e3.toString());
            Log.E("OmTree Delete");
            XDMOmLib.xdmOmVfsEnd(xDMWorkspace.om.vfs);
            XDMOmVfs.xdmOmVfsDeleteOmFile();
            return -1;
        } catch (Exception e4) {
            Log.E(e4.toString());
            return -1;
        }
    }

    public int xdmAgentStartGeneralAlert() {
        int i;
        XDMWorkspace xDMWorkspace = g_DmWs;
        Log.I("");
        if (xdmAgentInit() != 0) {
            return -1;
        }
        if (XDBProfileListAdp.xdbGetNotiEvent() > 0) {
            g_DmWs.m_szSessionID = XDBProfileListAdp.xdbGetNotiSessionID();
        } else {
            g_DmWs.m_szSessionID = xdmAgentLibMakeSessionID();
        }
        try {
            if (xdmAgentMakeNode() != 0) {
                return -1;
            }
        } catch (XDMOmTreeException e) {
            Log.E(e.toString());
            Log.E("OmTree Delete");
            XDMOmLib.xdmOmVfsEnd(xDMWorkspace.om.vfs);
            XDMOmLib.xdmOmVfsDeleteStdobj(xDMWorkspace.om.vfs);
            XDMOmVfs.xdmOmVfsDeleteOmFile();
        } catch (Exception e2) {
            Log.E(e2.toString());
        }
        int xdbGetDmAgentType = XDBAgentAdp.xdbGetDmAgentType();
        Log.I("nAgentType : " + xdbGetDmAgentType);
        if (xdbGetDmAgentType == 1) {
            XDMOmTree xDMOmTree = g_DmWs.om;
            String xdbGetFUMOStatusNode = XDBFumoAdp.xdbGetFUMOStatusNode();
            if (!TextUtils.isEmpty(xdbGetFUMOStatusNode)) {
                String concat = XFOTAInterface.XFUMO_PATH.concat("/").concat(xdbGetFUMOStatusNode).concat(XFOTAInterface.XFUMO_STATE_PATH);
                String valueOf = String.valueOf(XDBFumoAdp.xdbGetFUMOStatus());
                Log.I("node[" + concat + "] value[" + valueOf + "]");
                try {
                    xdmAgentSetOMAccStr(xDMOmTree, concat, valueOf, 8, 2);
                } catch (XDMOmTreeException e3) {
                    Log.E(e3.toString());
                    Log.E("OmTree Delete");
                    XDMOmLib.xdmOmVfsEnd(xDMWorkspace.om.vfs);
                    XDMOmLib.xdmOmVfsDeleteStdobj(xDMWorkspace.om.vfs);
                    XDMOmVfs.xdmOmVfsDeleteOmFile();
                } catch (Exception e4) {
                    Log.E(e4.toString());
                }
            }
        }
        g_DmWs.dmState = XDMInterface.XDMSyncMLState.XDM_STATE_GENERIC_ALERT_REPORT;
        if (xdmAgentCreatePackage() != 0) {
            return -1;
        }
        try {
            i = this.m_HttpDMAdapter.xtpAdpOpen(0);
        } catch (Exception e5) {
            Log.E(e5.toString());
            i = -2;
        }
        if (i != 0) {
            return -2;
        }
        XTPAdapter.xtpAdpSetIsConnected(true);
        return xdmAgentSendPackage();
    }

    private int xdmAgentHandleCmd() throws XDMOmTreeException {
        XDMAgent xDMAgent;
        XDMLinkedList xDMLinkedList;
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (xDMWorkspace.procState == XDMInterface.XDMProcessingState.XDM_PROC_NONE) {
            xDMWorkspace.procStep = 0;
            xDMWorkspace.cmdID = 1;
        }
        boolean z = false;
        while (xDMWorkspace.procStep != 3) {
            if (!xDMWorkspace.IsSequenceProcessing) {
                xDMLinkedList = xDMWorkspace.list;
                XDMLinkedList.xdmListSetCurrentObj(xDMLinkedList, 0);
                xDMAgent = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
            } else {
                xDMLinkedList = xDMWorkspace.sequenceList;
                xDMAgent = (XDMAgent) XDMLinkedList.xdmListGetObj(xDMLinkedList, 0);
                if (xDMAgent != null) {
                    xdmAgentCmdSequenceBlock(xDMWorkspace.sequenceList);
                } else {
                    xDMWorkspace.IsSequenceProcessing = false;
                }
                if (!xDMWorkspace.IsSequenceProcessing) {
                    xDMLinkedList = xDMWorkspace.list;
                    XDMLinkedList.xdmListSetCurrentObj(xDMLinkedList, 0);
                    XDMAgent xDMAgent2 = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
                    xDMAgent = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
                    XDMLinkedList.xdmListRemoveObjAtFirst(xDMLinkedList);
                }
            }
            if (xDMAgent == null) {
                if (xDMWorkspace.uicAlert == null) {
                    break;
                }
                xdmAgentCmdUicAlert();
            }
            while (xDMAgent != null) {
                if (XDMInterface.CMD_GET.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_EXEC.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_ALERT.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_ADD.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_REPLACE.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_COPY.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_DELETE.compareTo(xDMAgent.m_szCmd) == 0 || "Atomic_Start".compareTo(xDMAgent.m_szCmd) == 0 || "Sequence_Start".compareTo(xDMAgent.m_szCmd) == 0) {
                    xDMWorkspace.numAction++;
                }
                Log.I(xDMAgent.m_szCmd);
                if (xDMAgent.m_Atomic != null) {
                    xDMWorkspace.inAtomicCmd = true;
                    z = true;
                } else if (xDMAgent.m_Sequence != null) {
                    xDMWorkspace.inSequenceCmd = true;
                }
                int xdmAgentVerifyCmd = xdmAgentVerifyCmd(xDMAgent, z, (XDMParserStatus) null);
                if (!xDMWorkspace.IsSequenceProcessing) {
                    xDMAgent = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
                    XDMLinkedList.xdmListRemoveObjAtFirst(xDMLinkedList);
                }
                if (xdmAgentVerifyCmd == -4) {
                    Log.I("XDM_RET_PAUSED_BECAUSE_UIC_COMMAND");
                    return -4;
                }
                xDMWorkspace.atomicFlag = false;
                xDMWorkspace.inAtomicCmd = false;
                xDMWorkspace.inSequenceCmd = false;
                if (xdmAgentVerifyCmd == 5 || xdmAgentVerifyCmd == 4) {
                    xDMWorkspace.procStep = 3;
                    xDMWorkspace.numAction = 0;
                    return xdmAgentVerifyCmd;
                } else if (xdmAgentVerifyCmd == 1) {
                    return xdmAgentVerifyCmd;
                } else {
                    if (xdmAgentVerifyCmd != 0) {
                        Log.E("Processing failed");
                        return -1;
                    }
                }
            }
            XDMLinkedList.xdmListClearLinkedList(xDMLinkedList);
        }
        XDMLinkedList.xdmListClearLinkedList(xDMWorkspace.list);
        return 0;
    }

    private int xdmAgentVerifyCmd(XDMAgent xDMAgent, boolean z, XDMParserStatus xDMParserStatus) throws XDMOmTreeException {
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (XDMInterface.CMD_SYNCHDR.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdSyncHeader(xDMAgent.m_Header);
        }
        if (XDMInterface.CMD_STATUS.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdStatus(xDMAgent.m_Status);
        }
        if (XDMInterface.CMD_GET.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdGet(xDMAgent.m_Get, z);
        }
        if (XDMInterface.CMD_EXEC.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdExec(xDMAgent.m_Exec);
        }
        if (XDMInterface.CMD_ALERT.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdAlert(xDMAgent.m_Alert, z);
        }
        if (XDMInterface.CMD_ADD.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdAdd(xDMAgent.m_AddCmd, z, xDMParserStatus);
        }
        if (XDMInterface.CMD_REPLACE.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdReplace(xDMAgent.m_ReplaceCmd, z, xDMParserStatus);
        }
        if (XDMInterface.CMD_COPY.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdCopy(xDMAgent.m_CopyCmd, z, xDMParserStatus);
        }
        if (XDMInterface.CMD_DELETE.compareTo(xDMAgent.m_szCmd) == 0) {
            return xdmAgentCmdDelete(xDMAgent.m_DeleteCmd, z, xDMParserStatus);
        }
        if ("Atomic_Start".compareTo(xDMAgent.m_szCmd) == 0) {
            xDMWorkspace.inAtomicCmd = true;
            xDMWorkspace.atomicFlag = false;
            try {
                XDMOmLib.xdmOmVfsSaveFs(xDMWorkspace.om.vfs);
            } catch (Exception e) {
                Log.E(e.toString());
            }
            int xdmAgentCmdAtomic = xdmAgentCmdAtomic(xDMAgent.m_Atomic);
            if (xDMWorkspace.atomicFlag) {
                xDMWorkspace.om = null;
                xDMWorkspace.om = new XDMOmTree();
                XDMOmLib.xdmOmVfsInit(xDMWorkspace.om.vfs);
            }
            xDMWorkspace.inAtomicCmd = false;
            return xdmAgentCmdAtomic;
        } else if ("Sequence_Start".compareTo(xDMAgent.m_szCmd) == 0) {
            xDMWorkspace.inSequenceCmd = true;
            int xdmAgentCmdSequence = xdmAgentCmdSequence(xDMAgent.m_Sequence);
            if (xdmAgentCmdSequence == -4) {
                return xdmAgentCmdSequence;
            }
            xDMWorkspace.inSequenceCmd = false;
            return xdmAgentCmdSequence;
        } else {
            Log.E("Unknown Command" + xDMAgent.m_szCmd);
            return -6;
        }
    }

    private int xdmAgentCmdSequenceBlock(XDMLinkedList xDMLinkedList) throws XDMOmTreeException {
        int i;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMLinkedList.xdmListSetCurrentObj(xDMLinkedList, 0);
        XDMAgent xDMAgent = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
        if (xDMAgent != null) {
            xDMWorkspace.IsSequenceProcessing = true;
        }
        int i2 = 0;
        while (xDMAgent != null) {
            if (XDMInterface.CMD_GET.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_EXEC.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_ALERT.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_ADD.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_REPLACE.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_COPY.compareTo(xDMAgent.m_szCmd) == 0 || XDMInterface.CMD_DELETE.compareTo(xDMAgent.m_szCmd) == 0) {
                xDMWorkspace.numAction++;
            }
            if ("Atomic_Start".compareTo(xDMAgent.m_szCmd) == 0) {
                Log.I("Atomic_Start");
                if (xDMWorkspace.inAtomicCmd) {
                    i = xdmAgentCmdAtomic(xDMAgent.m_Atomic);
                } else {
                    i = xdmAgentVerifyCmd(xDMAgent, true, (XDMParserStatus) null);
                }
            } else if ("Sequence_Start".compareTo(xDMAgent.m_szCmd) == 0) {
                Log.I("Sequence_Start");
                if (xDMWorkspace.inSequenceCmd) {
                    i = xdmAgentCmdSequence(xDMAgent.m_Sequence);
                } else {
                    i = xdmAgentVerifyCmd(xDMAgent, false, (XDMParserStatus) null);
                }
            } else {
                i = xdmAgentVerifyCmd(xDMAgent, false, (XDMParserStatus) null);
            }
            i2 = i;
            xDMAgent = (XDMAgent) XDMLinkedList.xdmListGetNextObj(xDMLinkedList);
            XDMLinkedList.xdmListRemoveObjAtFirst(xDMLinkedList);
            if (i2 == -4) {
                xDMWorkspace.sequenceList = xDMLinkedList;
                return -4;
            } else if (i2 == 1) {
                return i2;
            } else {
                if (i2 != 0) {
                    Log.E("Processing failed");
                    return -1;
                }
            }
        }
        XDMLinkedList.xdmListClearLinkedList(xDMLinkedList);
        xDMWorkspace.IsSequenceProcessing = false;
        return i2;
    }

    private int xdmAgentCmdSyncHeader(XDMParserSyncheader xDMParserSyncheader) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        xDMWorkspace.m_szMsgRef = String.valueOf(xDMParserSyncheader.msgid);
        if (!TextUtils.isEmpty(xDMParserSyncheader.m_szRespUri)) {
            xDMWorkspace.m_szTargetURI = xDMParserSyncheader.m_szRespUri;
        }
        if (xDMParserSyncheader.meta != null) {
            if (xDMParserSyncheader.meta.maxobjsize > 0) {
                xDMWorkspace.serverMaxObjSize = xDMParserSyncheader.meta.maxobjsize;
                if (xDMWorkspace.serverMaxObjSize <= 0) {
                    xDMWorkspace.serverMaxObjSize = 1048576;
                } else if (xDMWorkspace.serverMaxObjSize > 1048576) {
                    xDMWorkspace.serverMaxObjSize = 1048576;
                }
            } else {
                xDMWorkspace.serverMaxObjSize = 1048576;
            }
            if (xDMParserSyncheader.meta.maxmsgsize > 0) {
                xDMWorkspace.serverMaxMsgSize = xDMParserSyncheader.meta.maxmsgsize;
            } else {
                xDMWorkspace.serverMaxMsgSize = 5120;
            }
        } else {
            xDMWorkspace.serverMaxObjSize = 1048576;
            xDMWorkspace.serverMaxMsgSize = 5120;
        }
        if (xDMWorkspace.serverAuthState != 1) {
            if (xDMWorkspace.serverCredType == 2) {
                xDMWorkspace.serverAuthState = xdmAgentVerifyServerAuth(xDMParserSyncheader);
            } else if (xDMParserSyncheader.cred != null) {
                xDMWorkspace.serverAuthState = xdmAgentVerifyServerAuth(xDMParserSyncheader);
            } else {
                Log.H("Not Used Server Authentication");
                if (xDMWorkspace.serverAuthState == -7 || xDMWorkspace.serverAuthState == -9) {
                    xDMWorkspace.serverAuthState = -1;
                } else {
                    xDMWorkspace.serverAuthState = -9;
                }
            }
        }
        if (xDMWorkspace.serverAuthState == 1) {
            if (xDMWorkspace.serverCredType == 2) {
                xDMWorkspace.m_szStatusReturnCode = "200";
            } else {
                xDMWorkspace.m_szStatusReturnCode = XDMInterface.STATUS_AUTHENTICATIONACCEPTED;
            }
            XDBProfileAdp.xdbSetServerAuthType(xDMWorkspace.serverCredType);
        } else if (xDMWorkspace.serverAuthState == -9) {
            xDMWorkspace.m_szStatusReturnCode = "407";
        } else {
            xDMWorkspace.m_szStatusReturnCode = "401";
        }
        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, 0, XDMInterface.CMD_SYNCHDR, xDMWorkspace.m_szHostname, xDMWorkspace.m_szSourceURI, xDMWorkspace.m_szStatusReturnCode));
        return 0;
    }

    private int xdmAgentCmdStatus(XDMParserStatus xDMParserStatus) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        if ("401".compareTo(xDMParserStatus.m_szData) == 0) {
            xDMWorkspace.authState = -7;
            Log.E("Client invalid credential(401)");
            if (XDMInterface.CMD_SYNCHDR.compareTo(xDMParserStatus.m_szCmd) == 0 && xDMParserStatus.chal == null) {
                Log.E("SyncHdr Status 401. and No Chal");
                xDMWorkspace.authCount = 3;
            }
        } else if (XDMInterface.STATUS_AUTHENTICATIONACCEPTED.compareTo(xDMParserStatus.m_szData) == 0) {
            xDMWorkspace.authState = 1;
        } else if ("200".compareTo(xDMParserStatus.m_szData) == 0 && XDMInterface.CMD_SYNCHDR.compareTo(xDMParserStatus.m_szCmd) == 0) {
            if (xDMWorkspace.credType != 2) {
                xDMWorkspace.authState = 1;
            } else if (xDMParserStatus.chal != null) {
                xDMWorkspace.authState = 1;
            }
            Log.I("Client Authorization Accepted (Catch 200)");
        } else if (XDMInterface.STATUS_ACCEPTED_AND_BUFFERED.compareTo(xDMParserStatus.m_szData) == 0) {
            Log.I("received Status 'buffered' cmd " + xDMParserStatus.m_szCmdRef);
            if (xDMWorkspace.tempResults != null) {
                XDMParserResults xDMParserResults = new XDMParserResults();
                XDMHandleCmd.xdmAgentDataStDuplResults(xDMParserResults, xDMWorkspace.tempResults);
                XDMLinkedList.xdmListAddObjAtFirst(xDMWorkspace.resultsList, xDMParserResults);
            } else {
                Log.E("can't find cached results can't send multi-messaged");
            }
        }
        if (xDMParserStatus.chal != null) {
            if (XDMInterface.CRED_TYPE_MD5.compareTo(xDMParserStatus.chal.m_szType) == 0) {
                xDMWorkspace.credType = 1;
                XDBProfileAdp.xdbSetAuthType(xDMWorkspace.credType);
                if ("b64".compareTo(xDMParserStatus.chal.m_szFormat) == 0) {
                    xdmAgentSetOMB64(m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH), xDMParserStatus.chal.m_szNextNonce);
                    byte[] xdmBase64Decode = XDMBase64.xdmBase64Decode(xDMParserStatus.chal.m_szNextNonce);
                    xDMWorkspace.nextNonce = new byte[xdmBase64Decode.length];
                    System.arraycopy(xdmBase64Decode, 0, xDMWorkspace.nextNonce, 0, xdmBase64Decode.length);
                    String str = new String(xDMWorkspace.nextNonce, Charset.defaultCharset());
                    Log.H("receive nextNonce:" + xDMParserStatus.chal.m_szNextNonce + "B64 decode String(ws.nextNonce):" + str);
                } else {
                    Log.I("!B64");
                    xdmAgentSetOM(m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH), xDMParserStatus.chal.m_szNextNonce);
                    xDMWorkspace.nextNonce = xDMParserStatus.chal.m_szNextNonce.getBytes(Charset.defaultCharset());
                }
                XDBProfileAdp.xdbSetClientNonce(xDMParserStatus.chal.m_szNextNonce);
                xdmAgentSetOM(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_AAUTHPREF_PATH), xDMParserStatus.chal.m_szType);
            } else if (XDMInterface.CRED_TYPE_HMAC.compareTo(xDMParserStatus.chal.m_szType) == 0) {
                xDMWorkspace.credType = 2;
                XDBProfileAdp.xdbSetAuthType(xDMWorkspace.credType);
                if ("b64".compareTo(xDMParserStatus.chal.m_szFormat) == 0) {
                    xdmAgentSetOMB64(m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH), xDMParserStatus.chal.m_szNextNonce);
                    byte[] xdmBase64Decode2 = XDMBase64.xdmBase64Decode(xDMParserStatus.chal.m_szNextNonce);
                    xDMWorkspace.nextNonce = new byte[xdmBase64Decode2.length];
                    System.arraycopy(xdmBase64Decode2, 0, xDMWorkspace.nextNonce, 0, xdmBase64Decode2.length);
                    String str2 = new String(xDMWorkspace.nextNonce, Charset.defaultCharset());
                    Log.H("B64 decode nextNonce" + str2);
                } else {
                    xdmAgentSetOM(m_DmAccXNodeInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH), xDMParserStatus.chal.m_szNextNonce);
                    xDMWorkspace.nextNonce = xDMParserStatus.chal.m_szNextNonce.getBytes(Charset.defaultCharset());
                }
                XDBProfileAdp.xdbSetClientNonce(xDMParserStatus.chal.m_szNextNonce);
                xdmAgentSetOM(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_AAUTHPREF_PATH), xDMParserStatus.chal.m_szType);
            } else if (XDMInterface.CRED_TYPE_BASIC.compareTo(xDMParserStatus.chal.m_szType) == 0) {
                xDMWorkspace.credType = 0;
                XDBProfileAdp.xdbSetAuthType(xDMWorkspace.credType);
                xdmAgentSetOM(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_AAUTHPREF_PATH), xDMParserStatus.chal.m_szType);
            }
        }
        return 0;
    }

    private void xdmAgentCmdExecFumo(XDMWorkspace xDMWorkspace, XDMParserExec xDMParserExec, XDMParserItem xDMParserItem) {
        char c = xDMWorkspace.nUpdateMechanism;
        if (c == 0) {
            XDBFumoAdp.xdbSetFUMOStatus(0);
            XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
            this.m_Status = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_ACCEPTED_FOR_PROCESSING);
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, this.m_Status);
            Log.I("Mechanism is XDM_FOTA_MECHANISM_NONE");
        } else if (c == 1) {
            XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
            XDBFumoAdp.xdbSetFUMOStatus(0);
            this.m_Status = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, "406");
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, this.m_Status);
        } else if (c == 2) {
            XDBFumoAdp.xdbSetFUMOStatus(10);
            XDBFumoAdp.xdbSetFUMOUpdateReportURI(xDMParserItem.m_szTarget);
            Log.I("Mechanism is XDM_FOTA_MECHANISM_ALTERNATIVE");
            if (xDMParserExec != null) {
                this.m_Status = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_ACCEPTED_FOR_PROCESSING);
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, this.m_Status);
            }
        } else if (c != 3) {
            XDBFumoAdp.xdbSetFUMOStatus(0);
            XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
            this.m_Status = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, "500");
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, this.m_Status);
            Log.I("Mechanism is");
        } else {
            XDBFumoAdp.xdbSetFUMOUpdateReportURI(xDMParserItem.m_szTarget);
            int xdbGetFUMOStatus = XDBFumoAdp.xdbGetFUMOStatus();
            Log.I("nStatus" + xdbGetFUMOStatus);
            if (xdbGetFUMOStatus == 251) {
                XDBFumoAdp.xdbSetFUMOStatus(50);
            } else {
                XDBFumoAdp.xdbSetFUMOStatus(10);
            }
            Log.I("Mechanism is XDM_FOTA_MECHANISM_ALTERNATIVE_DOWNLOAD");
            this.m_Status = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_ACCEPTED_FOR_PROCESSING);
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, this.m_Status);
        }
    }

    private int xdmAgentCmdExec(XDMParserExec xDMParserExec) {
        XDMParserStatus xDMParserStatus;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        XDMList xDMList = xDMParserExec.itemlist;
        while (xDMList != null) {
            XDMParserItem xDMParserItem = (XDMParserItem) xDMList.item;
            if (xDMWorkspace.serverAuthState != 1) {
                XDBFumoAdp.xdbSetFUMOStatus(0);
                XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
                if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, xDMWorkspace.m_szStatusReturnCode);
                } else {
                    xDMParserStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, (String) null, xDMWorkspace.m_szStatusReturnCode);
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus);
                xDMList = xDMList.next;
            } else if (TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                XDBFumoAdp.xdbSetFUMOStatus(0);
                XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, (String) null, "404"));
                xDMList = xDMList.next;
            } else {
                XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem.m_szTarget);
                if (xdmOmGetNodeProp == null) {
                    XDBFumoAdp.xdbSetFUMOStatus(0);
                    XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, "404"));
                    xDMList = xDMList.next;
                } else if (!xdmAgentIsAccessibleNode(xDMParserItem.m_szTarget)) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, "405"));
                    xDMList = xDMList.next;
                } else if (xdmAgentIsPermanentNode(xDMOmTree, xDMParserItem.m_szTarget)) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, "405"));
                    xDMList = xDMList.next;
                } else if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp, 4)) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
                    xDMList = xDMList.next;
                } else if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    Log.I(xDMParserItem.m_szTarget);
                    if (!XDMOmLib.xdmOmCheckAclCurrentNode(xDMOmTree, xDMParserItem.m_szTarget, 4)) {
                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
                        xDMList = xDMList.next;
                    } else if (xDMParserItem.m_szTarget.contains("/DownloadAndUpdate") || xDMParserItem.m_szTarget.contains("/Download") || xDMParserItem.m_szTarget.contains("/Update")) {
                        if (xDMParserItem.m_szTarget.contains("/Update") || xDMParserItem.m_szTarget.contains("/DownloadAndUpdate") || xDMParserItem.m_szTarget.contains("/Download")) {
                            XDBAgentAdp.xdbSetDmAgentType(1);
                        } else {
                            XDBAgentAdp.xdbSetDmAgentType(0);
                        }
                        if (!TextUtils.isEmpty(xDMParserExec.m_szCorrelator)) {
                            XDBFumoAdp.xdbSetFUMOCorrelator(xDMParserExec.m_szCorrelator);
                        }
                        if (xDMParserItem.data == null || xDMParserItem.data.data == null) {
                            XDBFumoAdp.xdbSetFUMOOptionalUpdate(false);
                        } else if ("O".equals(String.valueOf(xDMParserItem.data.data)) || "o".equals(String.valueOf(xDMParserItem.data.data))) {
                            XDBFumoAdp.xdbSetFUMOOptionalUpdate(true);
                        } else {
                            XDBFumoAdp.xdbSetFUMOOptionalUpdate(false);
                        }
                        int xdbGetDmAgentType = XDBAgentAdp.xdbGetDmAgentType();
                        if (xdbGetDmAgentType == 1) {
                            XDBFumoAdp.xdbSetUpdateFWVer("");
                            xdmAgentCmdExecFumo(xDMWorkspace, xDMParserExec, xDMParserItem);
                        } else {
                            Log.I(String.valueOf(xdbGetDmAgentType));
                        }
                        xDMList = xDMList.next;
                    } else {
                        Log.I("Node is not existed");
                        XDBFumoAdp.xdbSetFUMOStatus(0);
                        XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
                        XDMLinkedList.xdmListAddObjAtLast(g_DmWs.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, xDMParserItem.m_szTarget, "406"));
                        xDMList = xDMList.next;
                    }
                } else {
                    Log.I("Error item->target->pLocURI is NULL");
                    XDBFumoAdp.xdbSetFUMOUpdateMechanism(0);
                    XDBFumoAdp.xdbSetFUMOStatus(0);
                    XDMLinkedList.xdmListAddObjAtLast(g_DmWs.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserExec.cmdid, XDMInterface.CMD_EXEC, (String) null, (String) null, "403"));
                    xDMList = xDMList.next;
                }
            }
        }
        return 0;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v22, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v5, resolved type: com.accessorydm.eng.parser.XDMParserItem} */
    /* JADX WARNING: Multi-variable type inference failed */
    private int xdmAgentCmdAlert(XDMParserAlert xDMParserAlert, boolean z) {
        XDMWorkspace xDMWorkspace = g_DmWs;
        xDMWorkspace.procState = XDMInterface.XDMProcessingState.XDM_PROC_ALERT;
        if (!TextUtils.isEmpty(xDMParserAlert.m_szData)) {
            Log.I("Code " + xDMParserAlert.m_szData);
            int i = 0;
            if (xDMWorkspace.serverAuthState != 1) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAlert.cmdid, XDMInterface.CMD_ALERT, (String) null, (String) null, xDMWorkspace.m_szStatusReturnCode));
            } else if (z) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAlert.cmdid, XDMInterface.CMD_ALERT, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED));
            } else if (XDMInterface.ALERT_NEXT_MESSAGE.compareTo(xDMParserAlert.m_szData) == 0) {
                xDMWorkspace.nextMsg = true;
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAlert.cmdid, XDMInterface.CMD_ALERT, (String) null, (String) null, "200"));
            } else if (XDMInterface.ALERT_DISPLAY.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_CONTINUE_OR_ABORT.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_TEXT_INPUT.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_SINGLE_CHOICE.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_MULTIPLE_CHOICE.compareTo(xDMParserAlert.m_szData) == 0) {
                String str = null;
                if (xDMWorkspace.uicOption != null) {
                    XDMUic.xdmUicFreeUicOption(xDMWorkspace.uicOption);
                    xDMWorkspace.uicOption = null;
                }
                if (xDMWorkspace.uicOption == null) {
                    xDMWorkspace.uicOption = XDMUic.xdmUicCreateUicOption();
                }
                xDMWorkspace.uicOption.UICType = XDMUic.xdmUicGetUicType(xDMParserAlert.m_szData);
                XDMList xDMList = xDMParserAlert.itemlist;
                XDMParserItem xDMParserItem = (XDMParserItem) xDMList.item;
                if (xDMParserItem.data != null) {
                    str = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem.data);
                    Log.H("str = " + str);
                } else {
                    Log.I("str = NULL");
                }
                if (TextUtils.isEmpty(str)) {
                    if (XDMInterface.ALERT_DISPLAY.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_CONTINUE_OR_ABORT.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_TEXT_INPUT.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_SINGLE_CHOICE.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_MULTIPLE_CHOICE.compareTo(xDMParserAlert.m_szData) == 0) {
                        str = XDMInterface.DEFAULT_DISPLAY_UIC_OPTION;
                    } else if (!(xDMParserItem.data == null || xDMParserItem.data.data == null)) {
                        str = String.valueOf(xDMParserItem.data.data);
                    }
                }
                String xdmUicOptionProcess = XDMUic.xdmUicOptionProcess(str, xDMWorkspace.uicOption);
                XDMList xDMList2 = xDMList.next;
                if (xDMList2 != null) {
                    xDMParserItem = xDMList2.item;
                }
                if (xDMParserItem.data != null) {
                    xdmUicOptionProcess = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem.data);
                }
                if (!(!TextUtils.isEmpty(xdmUicOptionProcess) || xDMParserItem.data == null || xDMParserItem.data.data == null)) {
                    xdmUicOptionProcess = String.valueOf(xDMParserItem.data.data);
                }
                if (!TextUtils.isEmpty(xdmUicOptionProcess)) {
                    xDMWorkspace.uicOption.text = XDMList.xdmListAppendStrText(xDMWorkspace.uicOption.text, xdmUicOptionProcess);
                }
                if (XDMInterface.ALERT_SINGLE_CHOICE.compareTo(xDMParserAlert.m_szData) == 0 || XDMInterface.ALERT_MULTIPLE_CHOICE.compareTo(xDMParserAlert.m_szData) == 0) {
                    if (xDMList2 != null) {
                        for (XDMList xDMList3 = xDMList2.next; xDMList3 != null; xDMList3 = xDMList3.next) {
                            XDMParserItem xDMParserItem2 = (XDMParserItem) xDMList3.item;
                            if (xDMParserItem2.data != null) {
                                xdmUicOptionProcess = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem2.data);
                            }
                            if (!(!TextUtils.isEmpty(xdmUicOptionProcess) || xDMParserItem2.data == null || xDMParserItem2.data.data == null)) {
                                xdmUicOptionProcess = String.valueOf(xDMParserItem2.data.data);
                            }
                            if (!TextUtils.isEmpty(xdmUicOptionProcess)) {
                                xDMWorkspace.uicOption.uicMenuList[i] = xdmUicOptionProcess;
                                i++;
                            }
                        }
                    }
                    xDMWorkspace.uicOption.uicMenuNumbers = i;
                }
                xDMWorkspace.uicOption.appId = xDMWorkspace.appId;
                if (xDMWorkspace.uicAlert != null) {
                    XDMHandleCmd.xdmAgentDataStDeleteAlert(xDMWorkspace.uicAlert);
                }
                xDMWorkspace.uicAlert = new XDMParserAlert();
                XDMHandleCmd.xdmAgentDataStDuplAlert(xDMWorkspace.uicAlert, xDMParserAlert);
                return -4;
            } else if (XDMInterface.ALERT_SESSION_ABORT.compareTo(xDMParserAlert.m_szData) == 0) {
                xDMWorkspace.sessionAbort = 1;
                return 1;
            }
            xDMWorkspace.procState = XDMInterface.XDMProcessingState.XDM_PROC_NONE;
            return 0;
        }
        Log.I("alert->data is NULL");
        return -1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:138:0x0338  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0342  */
    /* JADX WARNING: Removed duplicated region for block: B:150:0x0366  */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x036b  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x0376  */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x0385  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x03f2  */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0458  */
    private int xdmAgentCmdAdd(XDMParserAdd xDMParserAdd, boolean z, XDMParserStatus xDMParserStatus) throws XDMOmTreeException {
        int i;
        String str;
        String str2;
        int i2;
        char[] cArr;
        boolean z2;
        int i3;
        String str3;
        String str4;
        int i4;
        XDMParserItem xDMParserItem;
        int i5;
        int i6;
        boolean z3;
        XDMParserStatus xDMParserStatus2;
        XDMList xDMList;
        String str5;
        XDMList xDMList2;
        XDMList xDMList3;
        int i7;
        String str6;
        String str7;
        String str8;
        int i8;
        int i9;
        XDMParserStatus xDMParserStatus3;
        XDMParserStatus xDMParserStatus4;
        XDMList xDMList4;
        XDMParserStatus xDMParserStatus5;
        XDMAgent xDMAgent = this;
        XDMParserAdd xDMParserAdd2 = xDMParserAdd;
        XDMParserStatus xDMParserStatus6 = xDMParserStatus;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        char[] cArr2 = new char[256];
        boolean xdmAgentCmdUicAlert = xdmAgentCmdUicAlert();
        int xdbGetFileIdTNDS = XDB.xdbGetFileIdTNDS();
        String str9 = null;
        XDMList xDMList5 = xDMParserAdd2.itemlist;
        String str10 = null;
        int i10 = 0;
        while (xDMList5 != null) {
            XDMParserItem xDMParserItem2 = (XDMParserItem) xDMList5.item;
            if (xDMParserItem2.meta == null) {
                if (xDMParserAdd2.meta != null) {
                    xDMParserItem2.meta = xDMParserAdd2.meta;
                }
            } else if (xDMParserAdd2.meta != null) {
                if (TextUtils.isEmpty(xDMParserItem2.meta.m_szType) && !TextUtils.isEmpty(xDMParserAdd2.meta.m_szType)) {
                    xDMParserItem2.meta.m_szType = xDMParserAdd2.meta.m_szType;
                }
                if (TextUtils.isEmpty(xDMParserItem2.meta.m_szFormat) && !TextUtils.isEmpty(xDMParserAdd2.meta.m_szFormat)) {
                    xDMParserItem2.meta.m_szFormat = xDMParserAdd2.meta.m_szFormat;
                }
            }
            if (xDMWorkspace.serverAuthState != 1) {
                if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                    xDMList4 = xDMList5;
                    xDMParserStatus5 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, xDMWorkspace.m_szStatusReturnCode);
                } else {
                    xDMList4 = xDMList5;
                    xDMParserStatus5 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, (String) null, xDMWorkspace.m_szStatusReturnCode);
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus5);
                xDMList5 = xDMList4.next;
            } else {
                XDMList xDMList6 = xDMList5;
                if (!xdmAgentCmdUicAlert) {
                    if (xDMParserItem2.moredata > 0) {
                        xDMWorkspace.dataBuffered = true;
                    }
                    if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                        xDMParserStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                    } else {
                        xDMParserStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                    }
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus4);
                    xDMList5 = xDMList6.next;
                } else if (!z || xDMWorkspace.atomicStep == XDMInterface.XDMAtomicStep.XDM_ATOMIC_NONE) {
                    if (TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                        int i11 = xDMParserAdd2.cmdid;
                        String str11 = XDMInterface.STATUS_ATOMIC_FAILED;
                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, i11, XDMInterface.CMD_ADD, (String) null, (String) null, "403"));
                        xDMList3 = xDMList6.next;
                        if (z && xDMParserStatus6 != null) {
                            xDMParserStatus6.m_szData = str11;
                        }
                    } else {
                        String str12 = XDMInterface.STATUS_ATOMIC_FAILED;
                        Log.I(xDMParserItem2.m_szTarget);
                        if (xdmAgentGetSyncMode() == 3) {
                            XDMOmLib.xdmOmMakeParentPath(xDMParserItem2.m_szTarget, cArr2);
                            xdmAgentSetXNodePath(XDMMem.xdmLibCharToString(cArr2), xDMParserItem2.m_szTarget, false);
                        } else if (XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem2.m_szTarget) != null && xDMParserItem2.moredata == 0 && !xDMWorkspace.dataBuffered) {
                            String xdmDDFGetMOPath = XDMDDFXmlHandler.xdmDDFGetMOPath(10);
                            if (TextUtils.isEmpty(xdmDDFGetMOPath)) {
                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ALREADY_EXISTS));
                                xDMList3 = xDMList6.next;
                                if (z && xDMParserStatus6 != null) {
                                    xDMParserStatus6.m_szData = str12;
                                }
                            } else if (xDMParserItem2.m_szTarget.substring(0, xDMParserItem2.m_szTarget.length()).compareTo(xdmDDFGetMOPath) != 0) {
                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ALREADY_EXISTS));
                                xDMList3 = xDMList6.next;
                                if (z && xDMParserStatus6 != null) {
                                    xDMParserStatus6.m_szData = str12;
                                }
                            } else {
                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ALREADY_EXISTS));
                                xDMList3 = xDMList6.next;
                                Log.I("node already Existed[418]");
                                if (z && xDMParserStatus6 != null) {
                                    xDMParserStatus6.m_szData = str12;
                                }
                            }
                        }
                        XDMOmLib.xdmOmMakeParentPath(xDMParserItem2.m_szTarget, cArr2);
                        String xdmLibCharToString = XDMMem.xdmLibCharToString(cArr2);
                        XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xdmLibCharToString);
                        if (xdmOmGetNodeProp == null) {
                            if (!XDMOmLib.xdmOmProcessCmdImplicitAdd(xDMOmTree, xdmLibCharToString, 27, 1)) {
                                if (xDMParserItem2.moredata > 0) {
                                    xDMWorkspace.dataBuffered = true;
                                }
                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "500"));
                                xDMList3 = xDMList6.next;
                                Log.E("Node depth is over 15  Command failed 500");
                                if (z && xDMParserStatus6 != null) {
                                    xDMParserStatus6.m_szData = str12;
                                }
                            } else {
                                xDMList3 = xDMList6;
                            }
                        } else if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp, 1)) {
                            if (xDMParserItem2.moredata > 0) {
                                xDMWorkspace.dataBuffered = true;
                            }
                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
                            xDMList3 = xDMList6.next;
                            if (z && xDMParserStatus6 != null) {
                                xDMParserStatus6.m_szData = str12;
                            }
                        } else {
                            if (xDMParserItem2.meta == null || TextUtils.isEmpty(xDMParserItem2.meta.m_szFormat) || "node".compareTo(xDMParserItem2.meta.m_szFormat) != 0) {
                                if (xDMParserItem2.meta != null) {
                                    if (!(xDMParserItem2.data == null || xDMParserItem2.data.data == null)) {
                                        str10 = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem2.data);
                                        if (TextUtils.isEmpty(str10)) {
                                            i9 = xDMParserItem2.data.size;
                                            str10 = String.valueOf(xDMParserItem2.data.data);
                                            if (xDMParserItem2.meta.size <= 0) {
                                                xDMWorkspace.dataTotalSize = xDMParserItem2.meta.size;
                                                xDMWorkspace.prevBufPos = 0;
                                            } else if (xDMWorkspace.prevBufPos == 0) {
                                                if (xDMParserItem2.data == null) {
                                                    xDMWorkspace.dataTotalSize = i9;
                                                } else if (xDMParserItem2.data.size > 0) {
                                                    xDMWorkspace.dataTotalSize = xDMParserItem2.data.size;
                                                } else {
                                                    xDMWorkspace.dataTotalSize = i9;
                                                }
                                            }
                                            String str13 = TextUtils.isEmpty(xDMParserItem2.meta.m_szType) ? xDMParserItem2.meta.m_szType : null;
                                            if (TextUtils.isEmpty(xDMParserItem2.meta.m_szFormat)) {
                                                i = i9;
                                                i2 = XDMOmList.xdmOmGetFormatFromString(xDMParserItem2.meta.m_szFormat);
                                                str = str13;
                                                str2 = str10;
                                            } else {
                                                i = i9;
                                                str = str13;
                                                str2 = str10;
                                                i2 = 12;
                                            }
                                        } else {
                                            i10 = str10.length();
                                        }
                                    }
                                    i9 = i10;
                                    if (xDMParserItem2.meta.size <= 0) {
                                    }
                                    if (TextUtils.isEmpty(xDMParserItem2.meta.m_szType)) {
                                    }
                                    if (TextUtils.isEmpty(xDMParserItem2.meta.m_szFormat)) {
                                    }
                                } else if (xDMParserItem2.data == null || xDMParserItem2.data.data == null) {
                                    xDMWorkspace.dataTotalSize = 0;
                                    i2 = 12;
                                } else {
                                    String xdmAgentDataStGetString = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem2.data);
                                    if (TextUtils.isEmpty(xdmAgentDataStGetString)) {
                                        i8 = xDMParserItem2.data.size;
                                        str2 = xDMParserItem2.data.data != null ? String.valueOf(xDMParserItem2.data.data) : null;
                                    } else {
                                        str2 = xdmAgentDataStGetString;
                                        i8 = xdmAgentDataStGetString.length();
                                    }
                                    if (!xDMWorkspace.dataBuffered) {
                                        xDMWorkspace.dataTotalSize = i8;
                                    }
                                    i = i8;
                                    i2 = 12;
                                    str = null;
                                }
                                int i12 = i2;
                                if (xDMWorkspace.nTNDSFlag && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                                    if (str.equals(XDMInterface.SYNCML_MIME_TYPE_TNDS_XML)) {
                                        XDB.xdbAppendFile(xdbGetFileIdTNDS, str2.getBytes(Charset.defaultCharset()));
                                        if (xDMParserItem2.moredata == 0) {
                                            xDMWorkspace.prevBufPos = 0;
                                            xDMWorkspace.dataBuffered = false;
                                            xDMWorkspace.dataTotalSize = 0;
                                            xDMWorkspace.nTNDSFlag = false;
                                            if (XDMDDFXmlHandler.xdmDDFCreateTNDSNodeFromFile(xdbGetFileIdTNDS, xDMOmTree) > 0) {
                                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "200"));
                                                xDMList5 = xDMList6.next;
                                            } else {
                                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "500"));
                                                xDMList5 = xDMList6.next;
                                            }
                                        } else {
                                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED));
                                            xDMList5 = xDMList6.next;
                                        }
                                        i10 = i;
                                        str9 = null;
                                        str10 = null;
                                    } else if (str.equals(XDMInterface.SYNCML_MIME_TYPE_TNDS_WBXML)) {
                                        Log.I("### SYNCML_MIME_TYPE_TNDS_WBXML ###");
                                        XDB.xdbAppendFile(xdbGetFileIdTNDS, str2.getBytes(Charset.defaultCharset()));
                                        if (xDMParserItem2.moredata == 0) {
                                            xDMWorkspace.prevBufPos = 0;
                                            xDMWorkspace.dataBuffered = false;
                                            xDMWorkspace.dataTotalSize = 0;
                                            xDMWorkspace.nTNDSFlag = false;
                                            int xdbGetFileSize = (int) XDB.xdbGetFileSize(xdbGetFileIdTNDS);
                                            byte[] bArr = new byte[xdbGetFileSize];
                                            XDB.xdbReadFile(xdbGetFileIdTNDS, 0, xdbGetFileSize, bArr);
                                            String str14 = new String(bArr, Charset.defaultCharset());
                                            String xdmTndsWbxmlParse = XDMDDFXmlHandler.xdmTndsWbxmlParse(str14, str14.length());
                                            int xdmDDFCreateTNDSNode = XDMDDFXmlHandler.xdmDDFCreateTNDSNode(xdmTndsWbxmlParse, !TextUtils.isEmpty(xdmTndsWbxmlParse) ? xdmTndsWbxmlParse.length() : 0, xDMOmTree);
                                            XDMDDFXmlHandler.xdmTndsParseFinish();
                                            if (xdmDDFCreateTNDSNode > 0) {
                                                str7 = str2;
                                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "200"));
                                                xDMList3 = xDMList6.next;
                                            } else {
                                                str7 = str2;
                                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "500"));
                                                xDMList3 = xDMList6.next;
                                            }
                                        } else {
                                            str7 = str2;
                                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED));
                                            xDMList3 = xDMList6.next;
                                        }
                                        i10 = i;
                                        str10 = str7;
                                    }
                                }
                                String str15 = str2;
                                if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str15)) {
                                    z2 = xdmAgentCmdUicAlert;
                                    i3 = i;
                                    cArr = cArr2;
                                    str3 = str15;
                                } else {
                                    if (XDMInterface.SYNCML_MIME_TYPE_TNDS_XML.compareTo(str) == 0) {
                                        if (xDMParserItem2.moredata > 0) {
                                            i7 = i;
                                            xDMWorkspace.prevBufPos += i7;
                                            xDMWorkspace.dataBuffered = true;
                                            if (!xDMWorkspace.nTNDSFlag) {
                                                xDMWorkspace.nTNDSFlag = true;
                                                XDB.xdbDeleteFile(xdbGetFileIdTNDS);
                                            }
                                            String str16 = str15;
                                            XDB.xdbAppendFile(xdbGetFileIdTNDS, str16.getBytes(Charset.defaultCharset()));
                                            cArr = cArr2;
                                            str6 = str16;
                                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED));
                                            xDMList2 = xDMList6.next;
                                        } else {
                                            i7 = i;
                                            cArr = cArr2;
                                            str6 = str15;
                                            if (XDMDDFXmlHandler.xdmDDFCreateTNDSNode(str6, i7, xDMOmTree) > 0) {
                                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "200"));
                                                xDMList2 = xDMList6.next;
                                                xDMAgent.xdmAgentGetAccountFromOM(xDMOmTree);
                                            } else {
                                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "500"));
                                                XDMList xDMList7 = xDMList6.next;
                                                Log.E("Fail");
                                                return -1;
                                            }
                                        }
                                        str10 = str6;
                                        i10 = i7;
                                    } else {
                                        int i13 = i;
                                        cArr = cArr2;
                                        str3 = str15;
                                        if (XDMInterface.SYNCML_MIME_TYPE_TNDS_WBXML.compareTo(str) == 0) {
                                            Log.I("### SYNCML_MIME_TYPE_TNDS_WBXML ###\n");
                                            if (xDMParserItem2.moredata > 0) {
                                                xDMWorkspace.prevBufPos += i13;
                                                xDMWorkspace.dataBuffered = true;
                                                if (!xDMWorkspace.nTNDSFlag) {
                                                    xDMWorkspace.nTNDSFlag = true;
                                                    XDB.xdbDeleteFile(xdbGetFileIdTNDS);
                                                }
                                                XDB.xdbAppendFile(xdbGetFileIdTNDS, str3.getBytes(Charset.defaultCharset()));
                                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED));
                                                xDMList3 = xDMList6.next;
                                                str10 = str3;
                                                cArr2 = cArr;
                                                i10 = i13;
                                            } else {
                                                int i14 = i13;
                                                String xdmTndsWbxmlParse2 = XDMDDFXmlHandler.xdmTndsWbxmlParse(str3, i14);
                                                int xdmDDFCreateTNDSNode2 = XDMDDFXmlHandler.xdmDDFCreateTNDSNode(xdmTndsWbxmlParse2, !TextUtils.isEmpty(xdmTndsWbxmlParse2) ? xdmTndsWbxmlParse2.length() : 0, xDMOmTree);
                                                XDMDDFXmlHandler.xdmTndsParseFinish();
                                                if (xdmDDFCreateTNDSNode2 > 0) {
                                                    boolean z4 = xdmAgentCmdUicAlert;
                                                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "200"));
                                                    xDMList2 = xDMList6.next;
                                                    if (xdmAgentGetSyncMode() != 3) {
                                                        xDMAgent.xdmAgentGetAccountFromOM(xDMOmTree);
                                                    }
                                                    str10 = str3;
                                                    i10 = i14;
                                                    xdmAgentCmdUicAlert = z4;
                                                } else {
                                                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, "500"));
                                                    XDMList xDMList8 = xDMList6.next;
                                                    Log.E("xdmAgentBuildCmdStatus : Warning!!!. Fail");
                                                    return -1;
                                                }
                                            }
                                        } else {
                                            z2 = xdmAgentCmdUicAlert;
                                            i3 = i13;
                                        }
                                    }
                                    cArr2 = cArr;
                                }
                                if (xDMWorkspace.dataTotalSize == 0) {
                                    int i15 = i12;
                                    str4 = str;
                                    i4 = xdbGetFileIdTNDS;
                                    xDMParserItem = xDMParserItem2;
                                    i6 = XDMOmLib.xdmOmWrite(xDMOmTree, xDMParserItem2.m_szTarget, xDMWorkspace.dataTotalSize, 0, str3, i3);
                                    xDMAgent.xdmAgentSetAclDynamicFUMONode(xDMOmTree, xDMParserItem.m_szTarget);
                                    Log.I("ADD (NO DATA)");
                                    XDMVnode xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem.m_szTarget);
                                    int i16 = i15;
                                    if (!(xdmOmGetNodeProp2 == null || i16 == 12)) {
                                        xdmOmGetNodeProp2.format = i16;
                                    }
                                    i5 = i16;
                                    z3 = true;
                                } else {
                                    i4 = xdbGetFileIdTNDS;
                                    int i17 = i12;
                                    str4 = str;
                                    xDMParserItem = xDMParserItem2;
                                    if (xDMWorkspace.prevBufPos == 0) {
                                        if ((XDMInterface.XDM_OMA_REPLACE.compareTo(xDMParserItem.m_szTarget) != 0 ? XDMOmVfs.xdmOmVfsGetFreeVaddr(xDMWorkspace.om.vfs, xDMWorkspace.dataTotalSize) : 0) < 0) {
                                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_DEVICE_FULL));
                                            Log.I("ADD STATUS_DEVICE_FULL");
                                            xDMList = xDMList6.next;
                                            if (xDMParserItem.moredata > 0) {
                                                xDMWorkspace.dataBuffered = true;
                                            }
                                            if (z && xDMParserStatus6 != null) {
                                                xDMParserStatus6.m_szData = str12;
                                            }
                                            str5 = str3;
                                            xdbGetFileIdTNDS = i4;
                                            cArr2 = cArr;
                                            str9 = null;
                                            i10 = i3;
                                            xdmAgentCmdUicAlert = z2;
                                        }
                                    }
                                    z3 = true;
                                    String str17 = str3;
                                    i5 = i17;
                                    i6 = XDMOmLib.xdmOmWrite(xDMOmTree, xDMParserItem.m_szTarget, xDMWorkspace.dataTotalSize, xDMWorkspace.prevBufPos, str17, i3);
                                }
                                if (i6 < 0) {
                                    Log.E(String.valueOf(i6));
                                    if (xDMParserItem.moredata > 0) {
                                        xDMWorkspace.dataBuffered = z3;
                                    }
                                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem.m_szTarget, "500"));
                                    xDMWorkspace.dataBuffered = false;
                                    xDMList = xDMList6.next;
                                    if (z && xDMParserStatus6 != null) {
                                        xDMParserStatus6.m_szData = str12;
                                    }
                                    str9 = null;
                                    str5 = null;
                                    xDMAgent = this;
                                } else {
                                    XDMVnode xdmOmGetNodeProp3 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem.m_szTarget);
                                    if (xdmOmGetNodeProp3 != null) {
                                        if (!TextUtils.isEmpty(str4)) {
                                            if (xdmOmGetNodeProp3.type != null) {
                                                XDMOmLib.xdmOmVfsDeleteMimeList(xdmOmGetNodeProp3.type);
                                            }
                                            xdmOmGetNodeProp3.type = new XDMOmList();
                                            xdmOmGetNodeProp3.type.data = str4;
                                            str9 = null;
                                            xdmOmGetNodeProp3.type.next = null;
                                        } else {
                                            str9 = null;
                                        }
                                        if (i5 != 12) {
                                            xdmOmGetNodeProp3.format = i5;
                                        }
                                    } else {
                                        str9 = null;
                                    }
                                    if (xDMParserItem.moredata == 0) {
                                        xDMWorkspace.prevBufPos = 0;
                                        xDMWorkspace.dataBuffered = false;
                                        xDMWorkspace.dataTotalSize = 0;
                                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem.m_szTarget, "200");
                                    } else {
                                        xDMWorkspace.prevBufPos += i3;
                                        xDMWorkspace.dataBuffered = z3;
                                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED);
                                    }
                                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus2);
                                    xDMList = xDMList6.next;
                                    xDMAgent = this;
                                    str5 = str9;
                                }
                                xdbGetFileIdTNDS = i4;
                                cArr2 = cArr;
                                i10 = i3;
                                xdmAgentCmdUicAlert = z2;
                            } else {
                                xDMWorkspace.dataTotalSize = 0;
                                i2 = 6;
                                if (!TextUtils.isEmpty(xDMParserItem2.meta.m_szType)) {
                                    str = xDMParserItem2.meta.m_szType;
                                    str8 = null;
                                    i = 0;
                                    int i122 = i2;
                                    if (str.equals(XDMInterface.SYNCML_MIME_TYPE_TNDS_XML)) {
                                    }
                                }
                            }
                            str8 = null;
                            str = null;
                            i = 0;
                            int i1222 = i2;
                            if (str.equals(XDMInterface.SYNCML_MIME_TYPE_TNDS_XML)) {
                            }
                        }
                    }
                    str9 = null;
                } else {
                    if (xDMWorkspace.tmpItem != null) {
                        if (xDMWorkspace.tmpItem.equals(xDMParserItem2)) {
                            if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                                xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ALREADY_EXISTS);
                            } else {
                                xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, (String) null, XDMInterface.STATUS_ALREADY_EXISTS);
                            }
                            xDMWorkspace.atomicStep = XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_NOT_EXEC;
                            xDMWorkspace.tmpItem = str9;
                        } else if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                            xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                        } else {
                            xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                        }
                    } else if (xDMWorkspace.atomicStep == XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_NOT_EXEC) {
                        if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                            xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                        } else {
                            xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                        }
                    } else if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                    } else {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserAdd2.cmdid, XDMInterface.CMD_ADD, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                    }
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus3);
                    xDMList5 = xDMList6.next;
                }
            }
        }
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:66:0x01e4, code lost:
        if (com.accessorydm.interfaces.XDMInterface.AUTH_TYPE_DIGEST.compareTo(java.lang.String.valueOf(r8)) == 0) goto L_0x01c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x02ae, code lost:
        if (com.accessorydm.interfaces.XDMInterface.AUTH_TYPE_DIGEST.compareTo(java.lang.String.valueOf(r5)) == 0) goto L_0x028c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x02c1  */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x02c4  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x02de  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x02e1  */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x02fb  */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x0300  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01fc  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0218  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x021b  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0239  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x023e  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x025c  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x025f  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0279  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x027c  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x028e  */
    private int xdmAgentGetAccountFromOM(XDMOmTree xDMOmTree) throws XDMOmTreeException {
        int i;
        XDMVnode xdmOmGetNodeProp;
        int i2;
        int i3;
        char[] cArr;
        int i4;
        XDMVnode xdmOmGetNodeProp2;
        int i5;
        XDMOmTree xDMOmTree2 = xDMOmTree;
        XDBProfileInfo xDBProfileInfo = new XDBProfileInfo();
        if (TextUtils.isEmpty(m_DmAccXNodeTndsInfo.m_szAccount)) {
            xdmAgentClose();
            return -1;
        } else if (TextUtils.isEmpty(m_DmAccXNodeTndsInfo.m_szAppAddr)) {
            xdmAgentClose();
            return -1;
        } else if (TextUtils.isEmpty(m_DmAccXNodeTndsInfo.m_szAppAddrPort)) {
            xdmAgentClose();
            return -1;
        } else if (TextUtils.isEmpty(m_DmAccXNodeTndsInfo.m_szClientAppAuth)) {
            xdmAgentClose();
            return -1;
        } else if (TextUtils.isEmpty(m_DmAccXNodeTndsInfo.m_szServerAppAuth)) {
            xdmAgentClose();
            return -1;
        } else if (TextUtils.isEmpty(m_DmAccXNodeTndsInfo.m_szToConRef)) {
            xdmAgentClose();
            return -1;
        } else {
            String concat = m_DmAccXNodeTndsInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPID_PATH);
            XDMVnode xdmOmGetNodeProp3 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat);
            int i6 = xdmOmGetNodeProp3 != null ? xdmOmGetNodeProp3.size : 0;
            char[] cArr2 = new char[i6];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat, 0, cArr2, i6);
            xDBProfileInfo.AppID = String.valueOf(cArr2);
            String concat2 = m_DmAccXNodeTndsInfo.m_szAccount.concat(XDMInterface.XDM_ACC_SERVERID_PATH);
            XDMVnode xdmOmGetNodeProp4 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat2);
            int i7 = xdmOmGetNodeProp4 != null ? xdmOmGetNodeProp4.size : 0;
            char[] cArr3 = new char[i7];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat2, 0, cArr3, i7);
            xDBProfileInfo.ServerID = String.valueOf(cArr3);
            Log.H("get DM informations from OM...ServerId : " + String.valueOf(cArr3));
            String concat3 = m_DmAccXNodeTndsInfo.m_szAccount.concat(XDMInterface.XDM_ACC_NAME_PATH);
            XDMVnode xdmOmGetNodeProp5 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat3);
            if (xdmOmGetNodeProp5 != null) {
                char[] cArr4 = new char[xdmOmGetNodeProp5.size];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat3, 0, cArr4, xdmOmGetNodeProp5.size);
                xDBProfileInfo.ProfileName = String.valueOf(cArr4);
            } else {
                xDBProfileInfo.ProfileName = xDBProfileInfo.ServerID;
                xdmAgentSetOMAccStr(xDMOmTree2, concat3, xDBProfileInfo.ProfileName, 27, 2);
            }
            String concat4 = m_DmAccXNodeTndsInfo.m_szAccount.concat(XDMInterface.XDM_ACC_PREFCONREF_PATH);
            XDMVnode xdmOmGetNodeProp6 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat4);
            int i8 = xdmOmGetNodeProp6 != null ? xdmOmGetNodeProp6.size : 0;
            char[] cArr5 = new char[i8];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat4, 0, cArr5, i8);
            xDBProfileInfo.PrefConRef = String.valueOf(cArr5);
            String concat5 = m_DmAccXNodeTndsInfo.m_szAppAddr.concat("/Addr");
            XDMVnode xdmOmGetNodeProp7 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat5);
            int i9 = xdmOmGetNodeProp7 != null ? xdmOmGetNodeProp7.size : 0;
            char[] cArr6 = new char[i9];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat5, 0, cArr6, i9);
            String valueOf = String.valueOf(cArr6);
            Log.H("get DM informations from OM...AddURI : " + valueOf);
            String concat6 = m_DmAccXNodeTndsInfo.m_szAppAddrPort.concat("/PortNbr");
            XDMVnode xdmOmGetNodeProp8 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat6);
            int i10 = xdmOmGetNodeProp8 != null ? xdmOmGetNodeProp8.size : 0;
            XDMOmLib.xdmOmRead(xDMOmTree2, concat6, 0, new char[i10], i10);
            xDBProfileInfo.ServerUrl = valueOf;
            XDBUrlInfo xtpURLParser = XTPHttpUtil.xtpURLParser(xDBProfileInfo.ServerUrl);
            xDBProfileInfo.ServerUrl = xtpURLParser.pURL;
            xDBProfileInfo.ServerIP = xtpURLParser.pAddress;
            xDBProfileInfo.Path = xtpURLParser.pPath;
            xDBProfileInfo.ServerPort = xtpURLParser.nPort;
            xDBProfileInfo.Protocol = xtpURLParser.pProtocol;
            xDBProfileInfo.ServerUrl_Org = xDBProfileInfo.ServerUrl;
            xDBProfileInfo.ServerIP_Org = xDBProfileInfo.ServerIP;
            xDBProfileInfo.Path_Org = xDBProfileInfo.Path;
            xDBProfileInfo.Protocol_Org = xDBProfileInfo.Protocol;
            xDBProfileInfo.ServerPort_Org = xDBProfileInfo.ServerPort;
            xDBProfileInfo.bChangedProtocol = false;
            String concat7 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHLEVEL_PATH);
            XDMVnode xdmOmGetNodeProp9 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat7);
            int i11 = xdmOmGetNodeProp9 != null ? xdmOmGetNodeProp9.size : 0;
            char[] cArr7 = new char[i11];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat7, 0, cArr7, i11);
            xDBProfileInfo.AuthLevel = String.valueOf(cArr7);
            String concat8 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHTYPE_PATH);
            XDMVnode xdmOmGetNodeProp10 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat8);
            int i12 = xdmOmGetNodeProp10 != null ? xdmOmGetNodeProp10.size : 0;
            char[] cArr8 = new char[i12];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat8, 0, cArr8, i12);
            if (XDMInterface.AUTH_TYPE_DIGEST.compareTo(String.valueOf(cArr8)) != 0) {
                if (XDMInterface.AUTH_TYPE_BASIC.compareTo(String.valueOf(cArr8)) != 0) {
                    if (XDMInterface.AUTH_TYPE_HMAC.compareTo(String.valueOf(cArr8)) == 0) {
                        i = 2;
                        xDBProfileInfo.AuthType = i;
                        String concat9 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH);
                        XDMVnode xdmOmGetNodeProp11 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat9);
                        int i13 = xdmOmGetNodeProp11 != null ? xdmOmGetNodeProp11.size : 0;
                        char[] cArr9 = new char[i13];
                        XDMOmLib.xdmOmRead(xDMOmTree2, concat9, 0, cArr9, i13);
                        xDBProfileInfo.UserName = String.valueOf(cArr9);
                        String concat10 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
                        XDMVnode xdmOmGetNodeProp12 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat10);
                        int i14 = xdmOmGetNodeProp12 != null ? xdmOmGetNodeProp12.size : 0;
                        char[] cArr10 = new char[i14];
                        XDMOmLib.xdmOmRead(xDMOmTree2, concat10, 0, cArr10, i14);
                        xDBProfileInfo.Password = String.valueOf(cArr10);
                        String concat11 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
                        xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat11);
                        int i15 = 12;
                        if (xdmOmGetNodeProp != null) {
                            i3 = xdmOmGetNodeProp.size;
                            i2 = xdmOmGetNodeProp.format;
                        } else {
                            i3 = 0;
                            i2 = 12;
                        }
                        char[] cArr11 = new char[i3];
                        XDMOmLib.xdmOmRead(xDMOmTree2, concat11, 0, cArr11, i3);
                        xDBProfileInfo.ClientNonce = String.valueOf(cArr11);
                        xDBProfileInfo.ClientNonceFormat = i2;
                        String concat12 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHLEVEL_PATH);
                        XDMVnode xdmOmGetNodeProp13 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat12);
                        int i16 = xdmOmGetNodeProp13 != null ? xdmOmGetNodeProp13.size : 0;
                        char[] cArr12 = new char[i16];
                        XDMOmLib.xdmOmRead(xDMOmTree2, concat12, 0, cArr12, i16);
                        xDBProfileInfo.ServerAuthLevel = String.valueOf(cArr12);
                        String concat13 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHTYPE_PATH);
                        XDMVnode xdmOmGetNodeProp14 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat13);
                        int i17 = xdmOmGetNodeProp14 != null ? xdmOmGetNodeProp14.size : 0;
                        cArr = new char[i17];
                        XDMOmLib.xdmOmRead(xDMOmTree2, concat13, 0, cArr, i17);
                        if (XDMInterface.AUTH_TYPE_DIGEST.compareTo(String.valueOf(cArr)) != 0) {
                            if (XDMInterface.AUTH_TYPE_BASIC.compareTo(String.valueOf(cArr)) != 0) {
                                if (XDMInterface.AUTH_TYPE_HMAC.compareTo(String.valueOf(cArr)) == 0) {
                                    i4 = 2;
                                    xDBProfileInfo.nServerAuthType = i4;
                                    String concat14 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH);
                                    XDMVnode xdmOmGetNodeProp15 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat14);
                                    int i18 = xdmOmGetNodeProp15 != null ? xdmOmGetNodeProp15.size : 0;
                                    char[] cArr13 = new char[i18];
                                    XDMOmLib.xdmOmRead(xDMOmTree2, concat14, 0, cArr13, i18);
                                    xDBProfileInfo.ServerID = String.valueOf(cArr13);
                                    String concat15 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
                                    XDMVnode xdmOmGetNodeProp16 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat15);
                                    int i19 = xdmOmGetNodeProp16 != null ? xdmOmGetNodeProp16.size : 0;
                                    char[] cArr14 = new char[i19];
                                    XDMOmLib.xdmOmRead(xDMOmTree2, concat15, 0, cArr14, i19);
                                    xDBProfileInfo.ServerPwd = String.valueOf(cArr14);
                                    String concat16 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
                                    xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat16);
                                    if (xdmOmGetNodeProp2 != null) {
                                        i5 = xdmOmGetNodeProp2.size;
                                        i15 = xdmOmGetNodeProp2.format;
                                    } else {
                                        i5 = 0;
                                    }
                                    char[] cArr15 = new char[i5];
                                    XDMOmLib.xdmOmRead(xDMOmTree2, concat16, 0, cArr15, i5);
                                    xDBProfileInfo.ServerNonce = String.valueOf(cArr15);
                                    xDBProfileInfo.ServerNonceFormat = i15;
                                    XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, m_DmAccXNodeTndsInfo.m_szAccount.concat("/Ext"));
                                    int xdbSetActiveProfileIndexByServerID = XDB.xdbSetActiveProfileIndexByServerID(xDBProfileInfo.ServerID);
                                    XDBProfileAdp.xdbSetProfileInfo(xDBProfileInfo);
                                    XDBProfileListAdp.xdbSetProfileName(xdbSetActiveProfileIndexByServerID, xDBProfileInfo.ProfileName);
                                    m_DmAccXNodeTndsInfo = null;
                                    return 0;
                                }
                            }
                            i4 = 0;
                            xDBProfileInfo.nServerAuthType = i4;
                            String concat142 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH);
                            XDMVnode xdmOmGetNodeProp152 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat142);
                            if (xdmOmGetNodeProp152 != null) {
                            }
                            char[] cArr132 = new char[i18];
                            XDMOmLib.xdmOmRead(xDMOmTree2, concat142, 0, cArr132, i18);
                            xDBProfileInfo.ServerID = String.valueOf(cArr132);
                            String concat152 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
                            XDMVnode xdmOmGetNodeProp162 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat152);
                            if (xdmOmGetNodeProp162 != null) {
                            }
                            char[] cArr142 = new char[i19];
                            XDMOmLib.xdmOmRead(xDMOmTree2, concat152, 0, cArr142, i19);
                            xDBProfileInfo.ServerPwd = String.valueOf(cArr142);
                            String concat162 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
                            xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat162);
                            if (xdmOmGetNodeProp2 != null) {
                            }
                            char[] cArr152 = new char[i5];
                            XDMOmLib.xdmOmRead(xDMOmTree2, concat162, 0, cArr152, i5);
                            xDBProfileInfo.ServerNonce = String.valueOf(cArr152);
                            xDBProfileInfo.ServerNonceFormat = i15;
                            XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, m_DmAccXNodeTndsInfo.m_szAccount.concat("/Ext"));
                            int xdbSetActiveProfileIndexByServerID2 = XDB.xdbSetActiveProfileIndexByServerID(xDBProfileInfo.ServerID);
                            XDBProfileAdp.xdbSetProfileInfo(xDBProfileInfo);
                            XDBProfileListAdp.xdbSetProfileName(xdbSetActiveProfileIndexByServerID2, xDBProfileInfo.ProfileName);
                            m_DmAccXNodeTndsInfo = null;
                            return 0;
                        }
                        i4 = 1;
                        xDBProfileInfo.nServerAuthType = i4;
                        String concat1422 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH);
                        XDMVnode xdmOmGetNodeProp1522 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat1422);
                        if (xdmOmGetNodeProp1522 != null) {
                        }
                        char[] cArr1322 = new char[i18];
                        XDMOmLib.xdmOmRead(xDMOmTree2, concat1422, 0, cArr1322, i18);
                        xDBProfileInfo.ServerID = String.valueOf(cArr1322);
                        String concat1522 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
                        XDMVnode xdmOmGetNodeProp1622 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat1522);
                        if (xdmOmGetNodeProp1622 != null) {
                        }
                        char[] cArr1422 = new char[i19];
                        XDMOmLib.xdmOmRead(xDMOmTree2, concat1522, 0, cArr1422, i19);
                        xDBProfileInfo.ServerPwd = String.valueOf(cArr1422);
                        String concat1622 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
                        xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat1622);
                        if (xdmOmGetNodeProp2 != null) {
                        }
                        char[] cArr1522 = new char[i5];
                        XDMOmLib.xdmOmRead(xDMOmTree2, concat1622, 0, cArr1522, i5);
                        xDBProfileInfo.ServerNonce = String.valueOf(cArr1522);
                        xDBProfileInfo.ServerNonceFormat = i15;
                        XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, m_DmAccXNodeTndsInfo.m_szAccount.concat("/Ext"));
                        int xdbSetActiveProfileIndexByServerID22 = XDB.xdbSetActiveProfileIndexByServerID(xDBProfileInfo.ServerID);
                        XDBProfileAdp.xdbSetProfileInfo(xDBProfileInfo);
                        XDBProfileListAdp.xdbSetProfileName(xdbSetActiveProfileIndexByServerID22, xDBProfileInfo.ProfileName);
                        m_DmAccXNodeTndsInfo = null;
                        return 0;
                    }
                }
                i = 0;
                xDBProfileInfo.AuthType = i;
                String concat92 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH);
                XDMVnode xdmOmGetNodeProp112 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat92);
                if (xdmOmGetNodeProp112 != null) {
                }
                char[] cArr92 = new char[i13];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat92, 0, cArr92, i13);
                xDBProfileInfo.UserName = String.valueOf(cArr92);
                String concat102 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
                XDMVnode xdmOmGetNodeProp122 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat102);
                if (xdmOmGetNodeProp122 != null) {
                }
                char[] cArr102 = new char[i14];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat102, 0, cArr102, i14);
                xDBProfileInfo.Password = String.valueOf(cArr102);
                String concat112 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
                xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat112);
                int i152 = 12;
                if (xdmOmGetNodeProp != null) {
                }
                char[] cArr112 = new char[i3];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat112, 0, cArr112, i3);
                xDBProfileInfo.ClientNonce = String.valueOf(cArr112);
                xDBProfileInfo.ClientNonceFormat = i2;
                String concat122 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHLEVEL_PATH);
                XDMVnode xdmOmGetNodeProp132 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat122);
                if (xdmOmGetNodeProp132 != null) {
                }
                char[] cArr122 = new char[i16];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat122, 0, cArr122, i16);
                xDBProfileInfo.ServerAuthLevel = String.valueOf(cArr122);
                String concat132 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHTYPE_PATH);
                XDMVnode xdmOmGetNodeProp142 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat132);
                if (xdmOmGetNodeProp142 != null) {
                }
                cArr = new char[i17];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat132, 0, cArr, i17);
                if (XDMInterface.AUTH_TYPE_DIGEST.compareTo(String.valueOf(cArr)) != 0) {
                }
                i4 = 1;
                xDBProfileInfo.nServerAuthType = i4;
                String concat14222 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH);
                XDMVnode xdmOmGetNodeProp15222 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat14222);
                if (xdmOmGetNodeProp15222 != null) {
                }
                char[] cArr13222 = new char[i18];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat14222, 0, cArr13222, i18);
                xDBProfileInfo.ServerID = String.valueOf(cArr13222);
                String concat15222 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
                XDMVnode xdmOmGetNodeProp16222 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat15222);
                if (xdmOmGetNodeProp16222 != null) {
                }
                char[] cArr14222 = new char[i19];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat15222, 0, cArr14222, i19);
                xDBProfileInfo.ServerPwd = String.valueOf(cArr14222);
                String concat16222 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
                xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat16222);
                if (xdmOmGetNodeProp2 != null) {
                }
                char[] cArr15222 = new char[i5];
                XDMOmLib.xdmOmRead(xDMOmTree2, concat16222, 0, cArr15222, i5);
                xDBProfileInfo.ServerNonce = String.valueOf(cArr15222);
                xDBProfileInfo.ServerNonceFormat = i152;
                XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, m_DmAccXNodeTndsInfo.m_szAccount.concat("/Ext"));
                int xdbSetActiveProfileIndexByServerID222 = XDB.xdbSetActiveProfileIndexByServerID(xDBProfileInfo.ServerID);
                XDBProfileAdp.xdbSetProfileInfo(xDBProfileInfo);
                XDBProfileListAdp.xdbSetProfileName(xdbSetActiveProfileIndexByServerID222, xDBProfileInfo.ProfileName);
                m_DmAccXNodeTndsInfo = null;
                return 0;
            }
            i = 1;
            xDBProfileInfo.AuthType = i;
            String concat922 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH);
            XDMVnode xdmOmGetNodeProp1122 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat922);
            if (xdmOmGetNodeProp1122 != null) {
            }
            char[] cArr922 = new char[i13];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat922, 0, cArr922, i13);
            xDBProfileInfo.UserName = String.valueOf(cArr922);
            String concat1022 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
            XDMVnode xdmOmGetNodeProp1222 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat1022);
            if (xdmOmGetNodeProp1222 != null) {
            }
            char[] cArr1022 = new char[i14];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat1022, 0, cArr1022, i14);
            xDBProfileInfo.Password = String.valueOf(cArr1022);
            String concat1122 = m_DmAccXNodeTndsInfo.m_szClientAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
            xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat1122);
            int i1522 = 12;
            if (xdmOmGetNodeProp != null) {
            }
            char[] cArr1122 = new char[i3];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat1122, 0, cArr1122, i3);
            xDBProfileInfo.ClientNonce = String.valueOf(cArr1122);
            xDBProfileInfo.ClientNonceFormat = i2;
            String concat1222 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHLEVEL_PATH);
            XDMVnode xdmOmGetNodeProp1322 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat1222);
            if (xdmOmGetNodeProp1322 != null) {
            }
            char[] cArr1222 = new char[i16];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat1222, 0, cArr1222, i16);
            xDBProfileInfo.ServerAuthLevel = String.valueOf(cArr1222);
            String concat1322 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHTYPE_PATH);
            XDMVnode xdmOmGetNodeProp1422 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat1322);
            if (xdmOmGetNodeProp1422 != null) {
            }
            cArr = new char[i17];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat1322, 0, cArr, i17);
            if (XDMInterface.AUTH_TYPE_DIGEST.compareTo(String.valueOf(cArr)) != 0) {
            }
            i4 = 1;
            xDBProfileInfo.nServerAuthType = i4;
            String concat142222 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHNAME_PATH);
            XDMVnode xdmOmGetNodeProp152222 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat142222);
            if (xdmOmGetNodeProp152222 != null) {
            }
            char[] cArr132222 = new char[i18];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat142222, 0, cArr132222, i18);
            xDBProfileInfo.ServerID = String.valueOf(cArr132222);
            String concat152222 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHSECRET_PATH);
            XDMVnode xdmOmGetNodeProp162222 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat152222);
            if (xdmOmGetNodeProp162222 != null) {
            }
            char[] cArr142222 = new char[i19];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat152222, 0, cArr142222, i19);
            xDBProfileInfo.ServerPwd = String.valueOf(cArr142222);
            String concat162222 = m_DmAccXNodeTndsInfo.m_szServerAppAuth.concat(XDMInterface.XDM_APPAUTH_AAUTHDATA_PATH);
            xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, concat162222);
            if (xdmOmGetNodeProp2 != null) {
            }
            char[] cArr152222 = new char[i5];
            XDMOmLib.xdmOmRead(xDMOmTree2, concat162222, 0, cArr152222, i5);
            xDBProfileInfo.ServerNonce = String.valueOf(cArr152222);
            xDBProfileInfo.ServerNonceFormat = i1522;
            XDMOmLib.xdmOmGetNodeProp(xDMOmTree2, m_DmAccXNodeTndsInfo.m_szAccount.concat("/Ext"));
            int xdbSetActiveProfileIndexByServerID2222 = XDB.xdbSetActiveProfileIndexByServerID(xDBProfileInfo.ServerID);
            XDBProfileAdp.xdbSetProfileInfo(xDBProfileInfo);
            XDBProfileListAdp.xdbSetProfileName(xdbSetActiveProfileIndexByServerID2222, xDBProfileInfo.ProfileName);
            m_DmAccXNodeTndsInfo = null;
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:157:0x035e  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x03ef  */
    /* JADX WARNING: Removed duplicated region for block: B:201:0x04e0  */
    /* JADX WARNING: Removed duplicated region for block: B:202:0x04e7  */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x04ef  */
    /* JADX WARNING: Removed duplicated region for block: B:205:0x051e  */
    /* JADX WARNING: Removed duplicated region for block: B:208:0x052e  */
    /* JADX WARNING: Removed duplicated region for block: B:209:0x0530  */
    /* JADX WARNING: Removed duplicated region for block: B:212:0x053a  */
    /* JADX WARNING: Removed duplicated region for block: B:217:0x0553  */
    /* JADX WARNING: Removed duplicated region for block: B:241:0x05cd  */
    private int xdmAgentCmdReplace(XDMParserReplace xDMParserReplace, boolean z, XDMParserStatus xDMParserStatus) {
        int i;
        String str;
        String str2;
        int i2;
        boolean z2;
        int i3;
        String str3;
        int i4;
        int i5;
        XDMParserItem xDMParserItem;
        int i6;
        XDMVnode xDMVnode;
        int i7;
        XDMParserStatus xDMParserStatus2;
        XDMList xDMList;
        int i8;
        int i9;
        String str4;
        int i10;
        String str5;
        XDMList xDMList2;
        String str6;
        int i11;
        String str7;
        String str8;
        XDMParserStatus xDMParserStatus3;
        XDMParserStatus xDMParserStatus4;
        XDMAgent xDMAgent = this;
        XDMParserReplace xDMParserReplace2 = xDMParserReplace;
        XDMParserStatus xDMParserStatus5 = xDMParserStatus;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        boolean xdmAgentCmdUicAlert = xdmAgentCmdUicAlert();
        int xdbGetFileIdTNDS = XDB.xdbGetFileIdTNDS();
        XDMParserItem xDMParserItem2 = null;
        int i12 = 0;
        XDMList xDMList3 = xDMParserReplace2.itemlist;
        String str9 = null;
        String str10 = null;
        int i13 = 0;
        int i14 = 12;
        while (xDMList3 != null) {
            XDMParserItem xDMParserItem3 = (XDMParserItem) xDMList3.item;
            if (xDMParserItem3.meta == null) {
                if (xDMParserReplace2.meta != null) {
                    xDMParserItem3.meta = xDMParserReplace2.meta;
                }
            } else if (xDMParserReplace2.meta != null) {
                if (TextUtils.isEmpty(xDMParserItem3.meta.m_szType) && !TextUtils.isEmpty(xDMParserReplace2.meta.m_szType)) {
                    xDMParserItem3.meta.m_szType = xDMParserReplace2.meta.m_szType;
                }
                if (TextUtils.isEmpty(xDMParserItem3.meta.m_szFormat) && !TextUtils.isEmpty(xDMParserReplace2.meta.m_szFormat)) {
                    xDMParserItem3.meta.m_szFormat = xDMParserReplace2.meta.m_szFormat;
                }
            }
            if (xDMWorkspace.serverAuthState != 1) {
                if (!TextUtils.isEmpty(xDMParserItem3.m_szTarget)) {
                    xDMParserStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, xDMWorkspace.m_szStatusReturnCode);
                } else {
                    xDMParserStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, (String) null, xDMWorkspace.m_szStatusReturnCode);
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus4);
                xDMList3 = xDMList3.next;
            } else if (z && xDMWorkspace.atomicStep != XDMInterface.XDMAtomicStep.XDM_ATOMIC_NONE) {
                if (xDMWorkspace.tmpItem != null) {
                    if (xDMWorkspace.tmpItem.equals(xDMParserItem3)) {
                        if (!TextUtils.isEmpty(xDMParserItem3.m_szTarget)) {
                            xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, "404");
                        } else {
                            xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, (String) null, "404");
                        }
                        xDMWorkspace.atomicStep = XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_NOT_EXEC;
                        xDMWorkspace.tmpItem = xDMParserItem2;
                    } else if (!TextUtils.isEmpty(xDMParserItem3.m_szTarget)) {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                    } else {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                    }
                } else if (xDMWorkspace.atomicStep == XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_NOT_EXEC) {
                    if (!TextUtils.isEmpty(xDMParserItem3.m_szTarget)) {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                    } else {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                    }
                } else if (!TextUtils.isEmpty(xDMParserItem3.m_szTarget)) {
                    xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                } else {
                    xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                }
                if (xDMParserStatus3 != null) {
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus3);
                }
                xDMList3 = xDMList3.next;
            } else if (TextUtils.isEmpty(xDMParserItem3.m_szTarget)) {
                XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, (String) null, "404");
                xDMList3 = xDMList3.next;
            } else if (xDMParserItem3.m_szTarget.contains("?")) {
                xDMAgent.xdmAgentCmdProp(XDMInterface.CMD_REPLACE, xDMParserItem3, xDMParserReplace2);
                xDMList3 = xDMList3.next;
            } else {
                XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem3.m_szTarget);
                if (!xdmAgentCmdUicAlert) {
                    int i15 = xDMParserReplace2.cmdid;
                    String str11 = xDMParserItem3.m_szTarget;
                    String str12 = XDMInterface.STATUS_ATOMIC_FAILED;
                    XDMParserStatus xdmAgentBuildCmdStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, i15, XDMInterface.CMD_REPLACE, (String) null, str11, XDMInterface.STATUS_NOT_EXECUTED);
                    if (z && xDMParserStatus5 != null) {
                        xDMParserStatus5.m_szData = str12;
                    }
                    if (xdmAgentBuildCmdStatus != null) {
                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xdmAgentBuildCmdStatus);
                    }
                    xDMList3 = xDMList3.next;
                } else {
                    String str13 = XDMInterface.STATUS_ATOMIC_FAILED;
                    if (xdmOmGetNodeProp == null) {
                        Log.I("node == null(not exist)");
                        if (xDMParserItem3.m_szTarget.contains(XDMInterface.XDM_OMA_ALTERNATIVE) || xDMParserItem3.m_szTarget.contains(XDMInterface.XDM_OMA_ALTERNATIVE_2) || xDMParserItem3.m_szTarget.contains(XDMInterface.XDM_OMA_REPLACE) || xDMParserItem3.m_szTarget.contains("/Ext")) {
                            try {
                                xdmAgentReMakeFwUpdateNode(xDMOmTree, xDMParserItem3.m_szTarget);
                            } catch (XDMOmTreeException e) {
                                Log.E(e.toString());
                                Log.E("OmTree Delete");
                                XDMOmLib.xdmOmVfsEnd(xDMWorkspace.om.vfs);
                                XDMOmLib.xdmOmVfsDeleteStdobj(xDMWorkspace.om.vfs);
                                XDMOmVfs.xdmOmVfsDeleteOmFile();
                                return -1;
                            } catch (Exception e2) {
                                Log.E(e2.toString());
                                return -1;
                            }
                        } else {
                            XDMParserStatus xdmAgentBuildCmdStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, "404");
                            if (z && xDMParserStatus5 != null) {
                                xDMParserStatus5.m_szData = str13;
                            }
                            if (xdmAgentBuildCmdStatus2 != null) {
                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xdmAgentBuildCmdStatus2);
                            }
                            xDMList3 = xDMList3.next;
                        }
                    } else if (xDMAgent.xdmAgentIsPermanentNode(xDMOmTree, xDMParserItem3.m_szTarget)) {
                        Log.I("Fail");
                        XDMParserStatus xdmAgentBuildCmdStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, "405");
                        if (z && xDMParserStatus5 != null) {
                            xDMParserStatus5.m_szData = str13;
                        }
                        if (xdmAgentBuildCmdStatus3 != null) {
                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xdmAgentBuildCmdStatus3);
                        }
                        xDMList3 = xDMList3.next;
                    } else if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp, 16)) {
                        Log.I("xdmOmCheckAcl Fail");
                        XDMParserStatus xdmAgentBuildCmdStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED);
                        if (z && xDMParserStatus5 != null) {
                            xDMParserStatus5.m_szData = str13;
                        }
                        if (xdmAgentBuildCmdStatus4 != null) {
                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xdmAgentBuildCmdStatus4);
                        }
                        xDMList3 = xDMList3.next;
                    }
                    Log.I("else");
                    Log.H(xDMParserItem3.m_szTarget);
                    if (xDMParserItem3.meta != null) {
                        if (!TextUtils.isEmpty(xDMParserItem3.meta.m_szType)) {
                            str9 = xDMParserItem3.meta.m_szType;
                        }
                        if (!TextUtils.isEmpty(xDMParserItem3.meta.m_szFormat)) {
                            i14 = XDMOmList.xdmOmGetFormatFromString(xDMParserItem3.meta.m_szFormat);
                        }
                        if (xDMParserItem3.meta.size > 0) {
                            xDMWorkspace.dataTotalSize = xDMParserItem3.meta.size;
                            xDMWorkspace.prevBufPos = i12;
                        } else if (xDMWorkspace.prevBufPos == 0) {
                            if (xDMParserItem3.data != null) {
                                xDMWorkspace.dataTotalSize = xDMParserItem3.data.size;
                            } else {
                                xDMWorkspace.dataTotalSize = i12;
                            }
                        }
                        if (xDMParserItem3.data == null || xDMParserItem3.data.data == null) {
                            xDMWorkspace.dataTotalSize = i12;
                            Log.I("REPLACE ( no item->data)");
                            str = str9;
                            i = i14;
                            i2 = 0;
                            str2 = null;
                            if (!xDMWorkspace.nTNDSFlag) {
                                Log.I("REPLACE ws.nTNDSFlag = true");
                                if (TextUtils.isEmpty(str2)) {
                                    str6 = "";
                                } else {
                                    str6 = str2;
                                }
                                XDB.xdbAppendFile(xdbGetFileIdTNDS, str6.getBytes(Charset.defaultCharset()));
                                if (xDMParserItem3.moredata == 0) {
                                    xDMWorkspace.prevBufPos = 0;
                                    xDMWorkspace.dataBuffered = false;
                                    xDMWorkspace.dataTotalSize = 0;
                                    xDMWorkspace.nTNDSFlag = false;
                                    int xdmOmDeleteImplicit = XDMOmLib.xdmOmDeleteImplicit(xDMOmTree, xDMParserItem3.m_szTarget, true);
                                    if (xdmOmDeleteImplicit >= 0) {
                                        xdmOmDeleteImplicit = XDMDDFXmlHandler.xdmDDFCreateTNDSNodeFromFile(xdbGetFileIdTNDS, xDMOmTree);
                                    }
                                    if (xdmOmDeleteImplicit > 0) {
                                        i11 = i2;
                                        str7 = str;
                                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, "200"));
                                        xDMList2 = xDMList3.next;
                                    } else {
                                        i11 = i2;
                                        str7 = str;
                                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, "500"));
                                        xDMList2 = xDMList3.next;
                                    }
                                } else {
                                    i11 = i2;
                                    str7 = str;
                                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED));
                                    xDMList2 = xDMList3.next;
                                }
                                str10 = str6;
                                str9 = str7;
                                i14 = i;
                                i13 = i11;
                            } else {
                                int i16 = i2;
                                String str14 = str;
                                if (TextUtils.isEmpty(str14)) {
                                    z2 = xdmAgentCmdUicAlert;
                                    i3 = i16;
                                    str3 = str2;
                                    if (!xDMParserItem3.m_szTarget.contains("/Ext")) {
                                    }
                                    if (i4 <= 0) {
                                    }
                                    if (TextUtils.isEmpty(str3)) {
                                    }
                                    if (i6 >= 0) {
                                    }
                                    if (xDMParserStatus2 != null) {
                                    }
                                    xDMList = xDMList3.next;
                                    xDMAgent = this;
                                    i8 = i3;
                                    xdbGetFileIdTNDS = i5;
                                    i9 = i7;
                                    str4 = str14;
                                } else if (XDMInterface.SYNCML_MIME_TYPE_TNDS_XML.compareTo(str14) != 0) {
                                    z2 = xdmAgentCmdUicAlert;
                                    i3 = i16;
                                    str3 = str2;
                                    if (XDMInterface.SYNCML_MIME_TYPE_TNDS_WBXML.compareTo(str14) == 0) {
                                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, "406"));
                                        XDMList xDMList4 = xDMList3.next;
                                        Log.E("Not Support TNDS with WBXML Type.\n");
                                        return -1;
                                    }
                                    if (!xDMParserItem3.m_szTarget.contains("/Ext")) {
                                        i4 = xDMAgent.xdmAgentVerifyFotaOption(xDMWorkspace, xDMParserItem3.m_szTarget, str3);
                                    } else {
                                        i4 = xDMAgent.xdmAgentVerifyUpdateMechanism(xDMWorkspace, xDMParserItem3.m_szTarget, str3);
                                    }
                                    if (i4 <= 0) {
                                        Log.H(xDMParserItem3.m_szTarget);
                                        Log.I(String.valueOf(i3));
                                        Log.I(String.valueOf(xDMWorkspace.dataTotalSize));
                                        xDMVnode = xdmOmGetNodeProp;
                                        i5 = xdbGetFileIdTNDS;
                                        xDMParserItem = xDMParserItem3;
                                        i6 = XDMOmLib.xdmOmWrite(xDMOmTree, xDMParserItem3.m_szTarget, xDMWorkspace.dataTotalSize, xDMWorkspace.prevBufPos, str3, i3);
                                    } else {
                                        xDMVnode = xdmOmGetNodeProp;
                                        i5 = xdbGetFileIdTNDS;
                                        xDMParserItem = xDMParserItem3;
                                        Log.H(xDMParserItem.m_szTarget);
                                        i6 = -1;
                                    }
                                    if (TextUtils.isEmpty(str3)) {
                                        str3 = null;
                                    } else if (xDMVnode != null) {
                                        xDMVnode.vaddr = -1;
                                        xDMVnode.size = 0;
                                    }
                                    if (i6 >= 0) {
                                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem.m_szTarget, "500");
                                        if (z && xDMParserStatus5 != null) {
                                            xDMParserStatus5.m_szData = str13;
                                        }
                                        i7 = i;
                                        xDMParserItem2 = null;
                                    } else {
                                        XDMVnode xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem.m_szTarget);
                                        if (xdmOmGetNodeProp2 != null) {
                                            if (!TextUtils.isEmpty(str14)) {
                                                if (xdmOmGetNodeProp2.type != null) {
                                                    XDMOmLib.xdmOmVfsDeleteMimeList(xdmOmGetNodeProp2.type);
                                                }
                                                XDMOmList xDMOmList = new XDMOmList();
                                                xDMOmList.data = str14;
                                                xDMParserItem2 = null;
                                                xDMOmList.next = null;
                                                xdmOmGetNodeProp2.type = xDMOmList;
                                            } else {
                                                xDMParserItem2 = null;
                                            }
                                            i10 = i;
                                            if (i10 > 0) {
                                                xdmOmGetNodeProp2.format = i10;
                                            }
                                        } else {
                                            i10 = i;
                                            xDMParserItem2 = null;
                                        }
                                        if (xDMParserItem.moredata == 0) {
                                            xDMWorkspace.prevBufPos = 0;
                                            xDMWorkspace.dataBuffered = false;
                                            xDMWorkspace.dataTotalSize = 0;
                                            if (xDMWorkspace.nUpdateMechanism == 1) {
                                                XDBFumoAdp.xdbSetFUMOStatus(40);
                                            }
                                            i7 = i10;
                                            xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem.m_szTarget, "200");
                                        } else {
                                            i7 = i10;
                                            xDMWorkspace.prevBufPos += i3;
                                            xDMWorkspace.dataBuffered = true;
                                            if (xDMWorkspace.nUpdateMechanism == 1) {
                                                XDBFumoAdp.xdbSetFUMOStatus(30);
                                            }
                                            xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED);
                                        }
                                    }
                                    if (xDMParserStatus2 != null) {
                                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus2);
                                    }
                                    xDMList = xDMList3.next;
                                    xDMAgent = this;
                                    i8 = i3;
                                    xdbGetFileIdTNDS = i5;
                                    i9 = i7;
                                    str4 = str14;
                                } else if (xDMParserItem3.moredata > 0) {
                                    int i17 = i16;
                                    xDMWorkspace.prevBufPos += i17;
                                    xDMWorkspace.dataBuffered = true;
                                    if (!xDMWorkspace.nTNDSFlag) {
                                        xDMWorkspace.nTNDSFlag = true;
                                        XDB.xdbDeleteFile(xdbGetFileIdTNDS);
                                    }
                                    if (TextUtils.isEmpty(str2)) {
                                        str5 = "";
                                    } else {
                                        str5 = str2;
                                    }
                                    XDB.xdbAppendFile(xdbGetFileIdTNDS, str5.getBytes(Charset.defaultCharset()));
                                    str10 = str5;
                                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED));
                                    xDMList2 = xDMList3.next;
                                    i13 = i17;
                                    str9 = str14;
                                    i14 = i;
                                } else {
                                    int i18 = i16;
                                    if (XDMOmLib.xdmOmDeleteImplicit(xDMOmTree, xDMParserItem3.m_szTarget, true) < 0 || XDMDDFXmlHandler.xdmDDFCreateTNDSNode(str2, i18, xDMOmTree) <= 0) {
                                        XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, "500"));
                                        XDMList xDMList5 = xDMList3.next;
                                        Log.E("Delete Fail.\n");
                                        return -1;
                                    }
                                    z2 = xdmAgentCmdUicAlert;
                                    str3 = str2;
                                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserReplace2.cmdid, XDMInterface.CMD_REPLACE, (String) null, xDMParserItem3.m_szTarget, "200"));
                                    xDMList = xDMList3.next;
                                    i8 = i18;
                                    str4 = str14;
                                    i9 = i;
                                    xDMParserItem2 = null;
                                }
                                i12 = 0;
                                boolean z3 = z2;
                                str10 = str3;
                                xdmAgentCmdUicAlert = z3;
                            }
                            xDMParserItem2 = null;
                            i12 = 0;
                        } else {
                            String xdmAgentDataStGetString = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem3.data);
                            if (TextUtils.isEmpty(xdmAgentDataStGetString)) {
                                i2 = xDMParserItem3.data.size;
                                str8 = String.valueOf(xDMParserItem3.data.data);
                            } else {
                                str2 = xdmAgentDataStGetString;
                                i2 = xdmAgentDataStGetString.length();
                                str = str9;
                                i = i14;
                                if (!xDMWorkspace.nTNDSFlag) {
                                }
                                xDMParserItem2 = null;
                                i12 = 0;
                            }
                        }
                    } else if (xDMParserItem3.data == null || xDMParserItem3.data.data == null) {
                        i2 = i13;
                        str = str9;
                        str2 = str10;
                        i = i14;
                        if (!xDMWorkspace.nTNDSFlag) {
                        }
                        xDMParserItem2 = null;
                        i12 = 0;
                    } else {
                        String xdmAgentDataStGetString2 = XDMHandleCmd.xdmAgentDataStGetString(xDMParserItem3.data);
                        if (TextUtils.isEmpty(xdmAgentDataStGetString2)) {
                            i2 = xDMParserItem3.data.size;
                            str8 = String.valueOf(xDMParserItem3.data.data);
                        } else {
                            str8 = xdmAgentDataStGetString2;
                            i2 = xdmAgentDataStGetString2.length();
                        }
                        if (!xDMWorkspace.dataBuffered) {
                            xDMWorkspace.dataTotalSize = i2;
                        }
                    }
                    str2 = str8;
                    str = str9;
                    i = i14;
                    if (!xDMWorkspace.nTNDSFlag) {
                    }
                    xDMParserItem2 = null;
                    i12 = 0;
                }
                xDMParserItem2 = null;
            }
        }
        return 0;
    }

    private int xdmAgentVerifyUpdateMechanism(XDMWorkspace xDMWorkspace, String str, String str2) {
        if (str.contains(XDMInterface.XDM_OMA_REPLACE)) {
            xDMWorkspace.nUpdateMechanism = 1;
            XDBFumoAdp.xdbSetFUMOStatus(10);
            XDBFumoAdp.xdbSetFUMOUpdateMechanism(xDMWorkspace.nUpdateMechanism);
        } else if (str.contains(XDMInterface.XDM_OMA_ALTERNATIVE)) {
            if (TextUtils.isEmpty(str2) || str2.length() > 256) {
                Log.I("D/L Mechanism  Object URL MISMATCH");
                return 0;
            } else if (!XDBFumoAdp.xdbSetFUMOServerUrl(str2)) {
                Log.I("wrong URL");
                return 0;
            } else {
                xDMWorkspace.nUpdateMechanism = 2;
                xDMWorkspace.m_szDownloadURI = str2;
                XDBFumoAdp.xdbSetFUMOUpdateMechanism(xDMWorkspace.nUpdateMechanism);
            }
        } else if (str.contains(XDMInterface.XDM_OMA_ALTERNATIVE_2)) {
            if (TextUtils.isEmpty(str2) || str2.length() > 256) {
                Log.I("D/L Mechanism  Object URL MISMATCH");
                return 0;
            } else if (!XDBFumoAdp.xdbSetFUMOServerUrl(str2)) {
                Log.I("wrong URL");
                return 0;
            } else {
                xDMWorkspace.nUpdateMechanism = 3;
                xDMWorkspace.m_szDownloadURI = str2;
                XDBFumoAdp.xdbSetFUMOUpdateMechanism(xDMWorkspace.nUpdateMechanism);
            }
        }
        return 1;
    }

    private int xdmAgentVerifyFotaOption(XDMWorkspace xDMWorkspace, String str, String str2) {
        Log.I("szPath : " + str + ", szData : " + str2);
        if (str.endsWith(XFOTAInterface.XFUMO_SVCSTATE)) {
            if (TextUtils.isEmpty(str2)) {
                return -1;
            }
            xDMWorkspace.m_szSvcState = str2;
            xdmAgentSetSvcState(str2);
            Log.I("SVCSTATE : " + xDMWorkspace.m_szSvcState);
        } else if (str.endsWith(XFOTAInterface.XFUMO_DOWNLOADCONNTYPE_PATH)) {
            if (TextUtils.isEmpty(str2)) {
                XDBFumoAdp.xdbSetFUMOWifiOnlyDownload(false);
                return -1;
            } else if (str2.endsWith(XFOTAInterface.XFUMO_DOWNLOAD_TYPE_WIFI)) {
                XDBFumoAdp.xdbSetFUMOWifiOnlyDownload(true);
            } else {
                XDBFumoAdp.xdbSetFUMOWifiOnlyDownload(false);
            }
        } else if (str.endsWith(XFOTAInterface.XFUMO_ROOTINGCHECK_PATH)) {
            Log.I("RootingCheckPath doesn't support");
        } else if (str.endsWith(XFOTAInterface.XFUMO_POSTPONE_PATH)) {
            Log.I("PostponeCountPath doesn't support");
        } else if (str.endsWith(XFOTAInterface.XFUMO_FORCE_PATH)) {
            try {
                XDBPostPoneAdp.xdbSetForceInstall(Integer.valueOf(str2).intValue());
            } catch (Exception e) {
                Log.E(e.toString());
                Log.E("Postpone Force - Set default value");
                XDBPostPoneAdp.xdbSetForceInstall(0);
                return -1;
            }
        }
        return 1;
    }

    private int xdmAgentCmdCopy(XDMParserCopy xDMParserCopy, boolean z, XDMParserStatus xDMParserStatus) {
        XDMParserStatus xDMParserStatus2;
        int i;
        char[] cArr;
        XDMParserStatus xDMParserStatus3;
        XDMParserItem xDMParserItem;
        XDMParserStatus xDMParserStatus4;
        XDMParserStatus xDMParserStatus5;
        XDMParserCopy xDMParserCopy2 = xDMParserCopy;
        XDMParserStatus xDMParserStatus6 = xDMParserStatus;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        char[] cArr2 = new char[80];
        boolean xdmAgentCmdUicAlert = xdmAgentCmdUicAlert();
        XDMList xDMList = xDMParserCopy2.itemlist;
        while (xDMList != null) {
            XDMParserItem xDMParserItem2 = (XDMParserItem) xDMList.item;
            if (xDMWorkspace.serverAuthState != 1) {
                if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                    xDMParserStatus5 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, xDMWorkspace.m_szStatusReturnCode);
                } else {
                    xDMParserStatus5 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, xDMWorkspace.m_szStatusReturnCode);
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus5);
                xDMList = xDMList.next;
            } else if (!xdmAgentCmdUicAlert) {
                if (xDMParserItem2.moredata > 0) {
                    xDMWorkspace.dataBuffered = true;
                }
                if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                    xDMParserStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                } else {
                    xDMParserStatus4 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus4);
                xDMList = xDMList.next;
            } else if (z && xDMWorkspace.atomicStep != XDMInterface.XDMAtomicStep.XDM_ATOMIC_NONE) {
                if (xDMWorkspace.tmpItem != null) {
                    if (xDMWorkspace.tmpItem.equals(xDMParserItem2)) {
                        if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                            int i2 = xDMParserCopy2.cmdid;
                            String str = xDMParserItem2.m_szTarget;
                            xDMParserItem = null;
                            xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, i2, XDMInterface.CMD_COPY, (String) null, str, XDMInterface.STATUS_ALREADY_EXISTS);
                        } else {
                            xDMParserItem = null;
                            xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, XDMInterface.STATUS_ALREADY_EXISTS);
                        }
                        xDMWorkspace.atomicStep = XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_NOT_EXEC;
                        xDMWorkspace.tmpItem = xDMParserItem;
                    } else if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                    } else {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                    }
                } else if (xDMWorkspace.atomicStep == XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_NOT_EXEC) {
                    if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                    } else {
                        xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                    }
                } else if (!TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                    xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                } else {
                    xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus3);
                xDMList = xDMList.next;
            } else if (TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, "404"));
                xDMList = xDMList.next;
            } else if (TextUtils.isEmpty(xDMParserItem2.m_szTarget)) {
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, (String) null, "404"));
                xDMList = xDMList.next;
            } else {
                XDMVnode xdmOmGetNodeProp = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem2.m_szSource);
                if (xdmOmGetNodeProp == null) {
                    int i3 = xDMParserCopy2.cmdid;
                    String str2 = xDMParserItem2.m_szSource;
                    String str3 = XDMInterface.STATUS_ATOMIC_FAILED;
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, i3, XDMInterface.CMD_COPY, (String) null, str2, XDMInterface.STATUS_NOT_EXECUTED));
                    xDMList = xDMList.next;
                    if (z && xDMParserStatus6 != null) {
                        xDMParserStatus6.m_szData = str3;
                    }
                } else {
                    int i4 = xdmOmGetNodeProp.size;
                    char[] cArr3 = new char[i4];
                    XDMOmVfs.xdmOmVfsGetData(xDMOmTree.vfs, xdmOmGetNodeProp, cArr3);
                    int i5 = xdmOmGetNodeProp.format;
                    if (!(xdmOmGetNodeProp.type == null || xdmOmGetNodeProp.type.data == null)) {
                        String.valueOf(xdmOmGetNodeProp.type.data);
                    }
                    int i6 = xdmOmGetNodeProp.format;
                    XDMVnode xdmOmGetNodeProp2 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem2.m_szTarget);
                    if (xdmOmGetNodeProp2 == null || xDMParserItem2.moredata != 0 || xDMWorkspace.dataBuffered) {
                        String str4 = XDMInterface.STATUS_ATOMIC_FAILED;
                        XDMOmLib.xdmOmMakeParentPath(xDMParserItem2.m_szTarget, cArr2);
                        XDMVnode xdmOmGetNodeProp3 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, XDMMem.xdmLibCharToString(cArr2));
                        if (xdmOmGetNodeProp3 == null) {
                            if (xDMParserItem2.moredata > 0) {
                                xDMWorkspace.dataBuffered = true;
                            }
                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, "500"));
                            xDMList = xDMList.next;
                            if (z && xDMParserStatus6 != null) {
                                xDMParserStatus6.m_szData = str4;
                            }
                        } else if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xdmOmGetNodeProp3, 1)) {
                            if (xDMParserItem2.moredata > 0) {
                                xDMWorkspace.dataBuffered = true;
                            }
                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED));
                            xDMList = xDMList.next;
                        } else {
                            xDMWorkspace.dataTotalSize = i4;
                            int i7 = i4;
                            int i8 = i6;
                            if (XDMOmLib.xdmOmWrite(xDMOmTree, xDMParserItem2.m_szTarget, xDMWorkspace.dataTotalSize, 0, String.valueOf(cArr3), i7) < 0) {
                                if (xDMParserItem2.moredata > 0) {
                                    xDMWorkspace.dataBuffered = true;
                                }
                                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, "500"));
                                xDMWorkspace.dataBuffered = false;
                                xDMList = xDMList.next;
                                if (z && xDMParserStatus6 != null) {
                                    xDMParserStatus6.m_szData = str4;
                                }
                            } else {
                                XDMVnode xdmOmGetNodeProp4 = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem2.m_szTarget);
                                if (xdmOmGetNodeProp4 != null) {
                                    if (!TextUtils.isEmpty((CharSequence) null)) {
                                        if (xdmOmGetNodeProp4.type != null) {
                                            XDMOmLib.xdmOmVfsDeleteMimeList(xdmOmGetNodeProp4.type);
                                        }
                                        xdmOmGetNodeProp4.type = new XDMOmList();
                                        xdmOmGetNodeProp4.type.data = null;
                                        xdmOmGetNodeProp4.type.next = null;
                                    }
                                    xdmOmGetNodeProp4.format = i8;
                                }
                                if (xDMParserItem2.moredata == 0) {
                                    xDMWorkspace.prevBufPos = 0;
                                    xDMWorkspace.dataBuffered = false;
                                    xDMWorkspace.dataTotalSize = 0;
                                    xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, "200");
                                } else {
                                    xDMWorkspace.prevBufPos += i7;
                                    xDMWorkspace.dataBuffered = true;
                                    xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED);
                                }
                            }
                        }
                    } else {
                        int i9 = xdmOmGetNodeProp2.size;
                        if (i9 < i4) {
                            cArr = new char[i4];
                            i = i4;
                        } else {
                            i = i9;
                            cArr = new char[i9];
                        }
                        if (xdmOmGetNodeProp2.format == 6) {
                            int i10 = xDMParserCopy2.cmdid;
                            String str5 = xDMParserItem2.m_szSource;
                            String str6 = XDMInterface.STATUS_ATOMIC_FAILED;
                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, i10, XDMInterface.CMD_COPY, (String) null, str5, XDMInterface.STATUS_NOT_EXECUTED));
                            xDMList = xDMList.next;
                            if (z && xDMParserStatus6 != null) {
                                xDMParserStatus6.m_szData = str6;
                            }
                        } else {
                            System.arraycopy(cArr3, 0, cArr, 0, i4);
                            XDMOmVfs.xdmOmVfsSetData(xDMOmTree.vfs, xdmOmGetNodeProp2, new String(cArr), i4);
                            if (xDMParserItem2.moredata == 0) {
                                xDMWorkspace.prevBufPos = 0;
                                xDMWorkspace.dataBuffered = false;
                                xDMWorkspace.dataTotalSize = 0;
                                xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, "200");
                            } else {
                                xDMWorkspace.prevBufPos += i;
                                xDMWorkspace.dataBuffered = true;
                                xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserCopy2.cmdid, XDMInterface.CMD_COPY, (String) null, xDMParserItem2.m_szTarget, XDMInterface.STATUS_ACCEPTED_AND_BUFFERED);
                            }
                        }
                    }
                    XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus2);
                    xDMList = xDMList.next;
                }
            }
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:82:0x01f8  */
    private int xdmAgentCmdDelete(XDMParserDelete xDMParserDelete, boolean z, XDMParserStatus xDMParserStatus) {
        XDMParserStatus xDMParserStatus2;
        XDMParserStatus xdmAgentBuildCmdStatus;
        XDMParserStatus xdmAgentBuildCmdStatus2;
        XDMParserStatus xDMParserStatus3;
        XDMParserDelete xDMParserDelete2 = xDMParserDelete;
        XDMParserStatus xDMParserStatus4 = xDMParserStatus;
        XDMWorkspace xDMWorkspace = g_DmWs;
        XDMOmTree xDMOmTree = xDMWorkspace.om;
        boolean xdmAgentCmdUicAlert = xdmAgentCmdUicAlert();
        XDMList xDMList = xDMParserDelete2.itemlist;
        XDMVnode xDMVnode = null;
        while (xDMList != null) {
            XDMParserItem xDMParserItem = (XDMParserItem) xDMList.item;
            if (xDMWorkspace.serverAuthState != 1) {
                if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, xDMWorkspace.m_szStatusReturnCode);
                } else {
                    xDMParserStatus3 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, (String) null, xDMWorkspace.m_szStatusReturnCode);
                }
                XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus3);
                xDMList = xDMList.next;
            } else {
                if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    xDMVnode = XDMOmLib.xdmOmGetNodeProp(xDMOmTree, xDMParserItem.m_szTarget);
                }
                if (!xdmAgentCmdUicAlert) {
                    if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                    } else {
                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                    }
                    if (z && xDMParserStatus4 != null) {
                        xDMParserStatus4.m_szData = XDMInterface.STATUS_ATOMIC_FAILED;
                    }
                } else if (!z || xDMWorkspace.atomicStep == XDMInterface.XDMAtomicStep.XDM_ATOMIC_NONE) {
                    if (xDMVnode == null) {
                        if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                            xdmAgentBuildCmdStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, "404");
                        } else {
                            xdmAgentBuildCmdStatus = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, (String) null, "404");
                        }
                        if (z && xDMParserStatus4 != null) {
                            xDMParserStatus4.m_szData = XDMInterface.STATUS_ATOMIC_FAILED;
                        }
                    } else {
                        if (xdmAgentIsPermanentNode(xDMOmTree, xDMParserItem.m_szTarget)) {
                            xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, "405");
                            if (z && xDMParserStatus4 != null) {
                                xDMParserStatus4.m_szData = XDMInterface.STATUS_ATOMIC_FAILED;
                            }
                        } else if (!XDMOmLib.xdmOmCheckAcl(xDMOmTree, xDMVnode, 2)) {
                            xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_PERMISSION_DENIED);
                            if (z && xDMParserStatus4 != null) {
                                xDMParserStatus4.m_szData = XDMInterface.STATUS_ATOMIC_FAILED;
                            }
                        } else if (xDMVnode == xDMOmTree.vfs.root) {
                            xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, "405");
                            if (z && xDMParserStatus4 != null) {
                                xDMParserStatus4.m_szData = XDMInterface.STATUS_ATOMIC_FAILED;
                            }
                        } else {
                            Log.H(xDMParserItem.m_szTarget);
                            if (XDMOmLib.xdmOmDelete(xDMOmTree, xDMParserItem.m_szTarget, true) < 0) {
                                xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, "405");
                                if (z && xDMParserStatus4 != null) {
                                    xDMParserStatus4.m_szData = XDMInterface.STATUS_ATOMIC_FAILED;
                                }
                            } else {
                                xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, "200");
                            }
                        }
                        if (xDMParserStatus2 != null) {
                            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, xDMParserStatus2);
                        }
                        xDMList = xDMList.next;
                    }
                } else if (xDMWorkspace.tmpItem != null) {
                    if (xDMWorkspace.tmpItem.equals(xDMParserItem)) {
                        if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                            xdmAgentBuildCmdStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, "404");
                        } else {
                            xdmAgentBuildCmdStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, (String) null, "404");
                        }
                        xDMWorkspace.atomicStep = XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_NOT_EXEC;
                        xDMWorkspace.tmpItem = null;
                    } else if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                    } else {
                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                    }
                } else if (xDMWorkspace.atomicStep == XDMInterface.XDMAtomicStep.XDM_ATOMIC_STEP_NOT_EXEC) {
                    if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_NOT_EXECUTED);
                    } else {
                        xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, (String) null, XDMInterface.STATUS_NOT_EXECUTED);
                    }
                } else if (!TextUtils.isEmpty(xDMParserItem.m_szTarget)) {
                    xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, xDMParserItem.m_szTarget, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                } else {
                    xDMParserStatus2 = XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserDelete2.cmdid, XDMInterface.CMD_DELETE, (String) null, (String) null, XDMInterface.STATUS_ATOMIC_ROLL_BACK_OK);
                }
                if (xDMParserStatus2 != null) {
                }
                xDMList = xDMList.next;
            }
        }
        return 0;
    }

    private int xdmAgentCmdAtomic(XDMParserAtomic xDMParserAtomic) throws XDMOmTreeException {
        int xdmAgentCmdAtomicBlock;
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (xDMParserAtomic == null || xDMParserAtomic.itemlist == null || (xdmAgentCmdAtomicBlock = xdmAgentCmdAtomicBlock(xDMParserAtomic, xDMParserAtomic.itemlist)) < 0) {
            return -1;
        }
        xDMWorkspace.numAction += xdmAgentCmdAtomicBlock;
        return 0;
    }

    private int xdmAgentCmdSequence(XDMParserSequence xDMParserSequence) throws XDMOmTreeException {
        XDMWorkspace xDMWorkspace = g_DmWs;
        if (xDMParserSequence == null) {
            return -1;
        }
        if (!xDMWorkspace.IsSequenceProcessing) {
            XDMLinkedList.xdmListAddObjAtLast(xDMWorkspace.statusList, XDMBuildCmd.xdmAgentBuildCmdStatus(xDMWorkspace, xDMParserSequence.cmdid, XDMInterface.CMD_SEQUENCE, (String) null, (String) null, "200"));
        }
        if (xDMParserSequence.itemlist == null) {
            return -1;
        }
        int xdmAgentCmdSequenceBlock = xdmAgentCmdSequenceBlock(xDMParserSequence.itemlist);
        if (xdmAgentCmdSequenceBlock == -4) {
            return -4;
        }
        if (xdmAgentCmdSequenceBlock < 0) {
            return -1;
        }
        return xdmAgentCmdSequenceBlock;
    }

    public static void xdmAgentSetXNodePath(String str, String str2, boolean z) {
        Log.I("target[" + str2 + "]parent[" + str + "]");
        if (xdmAgentGetSyncMode() != 3) {
            m_DmAccXNodeTndsInfo = new XDMAccXNode();
            if (XDMInterface.XDM_BASE_PATH.compareTo(str) == 0 || XDMInterface.XDM_ACCOUNT_PATH.compareTo(str) == 0) {
                XDMAccXNode xDMAccXNode = m_DmAccXNodeTndsInfo;
                xDMAccXNode.m_szAccount = str;
                xDMAccXNode.m_szAccount = xDMAccXNode.m_szAccount.concat("/");
                XDMAccXNode xDMAccXNode2 = m_DmAccXNodeTndsInfo;
                xDMAccXNode2.m_szAccount = xDMAccXNode2.m_szAccount.concat(str2);
            }
            if (str.compareTo(m_DmAccXNodeTndsInfo.m_szAccount.concat(XDMInterface.XDM_ACC_TOCONREF_PATH)) == 0) {
                XDMAccXNode xDMAccXNode3 = m_DmAccXNodeTndsInfo;
                xDMAccXNode3.m_szToConRef = str;
                xDMAccXNode3.m_szToConRef = xDMAccXNode3.m_szToConRef.concat("/");
                XDMAccXNode xDMAccXNode4 = m_DmAccXNodeTndsInfo;
                xDMAccXNode4.m_szToConRef = xDMAccXNode4.m_szToConRef.concat(str2);
            }
            if (str.compareTo(m_DmAccXNodeTndsInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPADDR_PATH)) == 0) {
                XDMAccXNode xDMAccXNode5 = m_DmAccXNodeTndsInfo;
                xDMAccXNode5.m_szAppAddr = str;
                xDMAccXNode5.m_szAppAddr = xDMAccXNode5.m_szAppAddr.concat("/");
                XDMAccXNode xDMAccXNode6 = m_DmAccXNodeTndsInfo;
                xDMAccXNode6.m_szAppAddr = xDMAccXNode6.m_szAppAddr.concat(str2);
            }
            if (str.compareTo(m_DmAccXNodeTndsInfo.m_szAppAddr.concat(XDMInterface.XDM_APPADDR_PORT_PATH)) == 0) {
                XDMAccXNode xDMAccXNode7 = m_DmAccXNodeTndsInfo;
                xDMAccXNode7.m_szAppAddrPort = str;
                xDMAccXNode7.m_szAppAddrPort = xDMAccXNode7.m_szAppAddrPort.concat("/");
                XDMAccXNode xDMAccXNode8 = m_DmAccXNodeTndsInfo;
                xDMAccXNode8.m_szAppAddrPort = xDMAccXNode8.m_szAppAddrPort.concat(str2);
            }
            if (str.compareTo(m_DmAccXNodeTndsInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPAUTH_PATH)) != 0) {
                return;
            }
            if (str2.compareTo("ClientSide") == 0) {
                XDMAccXNode xDMAccXNode9 = m_DmAccXNodeTndsInfo;
                xDMAccXNode9.m_szClientAppAuth = str;
                xDMAccXNode9.m_szClientAppAuth = xDMAccXNode9.m_szClientAppAuth.concat("/");
                XDMAccXNode xDMAccXNode10 = m_DmAccXNodeTndsInfo;
                xDMAccXNode10.m_szClientAppAuth = xDMAccXNode10.m_szClientAppAuth.concat(str2);
                return;
            }
            XDMAccXNode xDMAccXNode11 = m_DmAccXNodeTndsInfo;
            xDMAccXNode11.m_szServerAppAuth = str;
            xDMAccXNode11.m_szServerAppAuth = xDMAccXNode11.m_szServerAppAuth.concat("/");
            XDMAccXNode xDMAccXNode12 = m_DmAccXNodeTndsInfo;
            xDMAccXNode12.m_szServerAppAuth = xDMAccXNode12.m_szServerAppAuth.concat(str2);
        } else if (z) {
            if (XDMInterface.XDM_BASE_PATH.compareTo(str) == 0 || XDMInterface.XDM_ACCOUNT_PATH.compareTo(str) == 0) {
                XDMAccXNode xDMAccXNode13 = m_DmAccXNodeInfo;
                xDMAccXNode13.m_szAccount = str;
                xDMAccXNode13.m_szAccount = xDMAccXNode13.m_szAccount.concat("/");
                XDMAccXNode xDMAccXNode14 = m_DmAccXNodeInfo;
                xDMAccXNode14.m_szAccount = xDMAccXNode14.m_szAccount.concat(str2);
            }
            if (str.compareTo(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_TOCONREF_PATH)) == 0) {
                XDMAccXNode xDMAccXNode15 = m_DmAccXNodeInfo;
                xDMAccXNode15.m_szToConRef = str;
                xDMAccXNode15.m_szToConRef = xDMAccXNode15.m_szToConRef.concat("/");
                XDMAccXNode xDMAccXNode16 = m_DmAccXNodeInfo;
                xDMAccXNode16.m_szToConRef = xDMAccXNode16.m_szToConRef.concat(str2);
            }
            if (str.compareTo(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPADDR_PATH)) == 0) {
                XDMAccXNode xDMAccXNode17 = m_DmAccXNodeInfo;
                xDMAccXNode17.m_szAppAddr = str;
                xDMAccXNode17.m_szAppAddr = xDMAccXNode17.m_szAppAddr.concat("/");
                XDMAccXNode xDMAccXNode18 = m_DmAccXNodeInfo;
                xDMAccXNode18.m_szAppAddr = xDMAccXNode18.m_szAppAddr.concat(str2);
            }
            if (str.compareTo(m_DmAccXNodeInfo.m_szAppAddr.concat(XDMInterface.XDM_APPADDR_PORT_PATH)) == 0) {
                XDMAccXNode xDMAccXNode19 = m_DmAccXNodeInfo;
                xDMAccXNode19.m_szAppAddrPort = str;
                xDMAccXNode19.m_szAppAddrPort = xDMAccXNode19.m_szAppAddrPort.concat("/");
                XDMAccXNode xDMAccXNode20 = m_DmAccXNodeInfo;
                xDMAccXNode20.m_szAppAddrPort = xDMAccXNode20.m_szAppAddrPort.concat(str2);
            }
            if (str.compareTo(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPAUTH_PATH)) != 0) {
                return;
            }
            if ("ClientSide".compareTo(str2) == 0) {
                XDMAccXNode xDMAccXNode21 = m_DmAccXNodeInfo;
                xDMAccXNode21.m_szClientAppAuth = str;
                xDMAccXNode21.m_szClientAppAuth = xDMAccXNode21.m_szClientAppAuth.concat("/");
                XDMAccXNode xDMAccXNode22 = m_DmAccXNodeInfo;
                xDMAccXNode22.m_szClientAppAuth = xDMAccXNode22.m_szClientAppAuth.concat(str2);
                return;
            }
            XDMAccXNode xDMAccXNode23 = m_DmAccXNodeInfo;
            xDMAccXNode23.m_szServerAppAuth = str;
            xDMAccXNode23.m_szServerAppAuth = xDMAccXNode23.m_szServerAppAuth.concat("/");
            XDMAccXNode xDMAccXNode24 = m_DmAccXNodeInfo;
            xDMAccXNode24.m_szServerAppAuth = xDMAccXNode24.m_szServerAppAuth.concat(str2);
        } else {
            if (XDMInterface.XDM_BASE_PATH.compareTo(str) == 0 || XDMInterface.XDM_ACCOUNT_PATH.compareTo(str) == 0) {
                m_DmAccXNodeInfo.m_szAccount = str2;
            }
            if (str.compareTo(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_TOCONREF_PATH)) == 0) {
                m_DmAccXNodeInfo.m_szToConRef = str2;
            }
            if (str.compareTo(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPADDR_PATH)) == 0) {
                m_DmAccXNodeInfo.m_szAppAddr = str2;
            }
            if (str.compareTo(m_DmAccXNodeInfo.m_szAppAddr.concat(XDMInterface.XDM_APPADDR_PORT_PATH)) == 0) {
                m_DmAccXNodeInfo.m_szAppAddrPort = str2;
            }
            if (str.compareTo(m_DmAccXNodeInfo.m_szAccount.concat(XDMInterface.XDM_ACC_APPAUTH_PATH)) != 0) {
                return;
            }
            if ("ClientSide".compareTo(str2) == 0) {
                m_DmAccXNodeInfo.m_szClientAppAuth = str2;
            } else {
                m_DmAccXNodeInfo.m_szServerAppAuth = str2;
            }
        }
    }

    private void xdmAgentSetAclDynamicFUMONode(XDMOmTree xDMOmTree, String str) {
        Log.I("target path[" + str + "]");
        if (str.contains(XFOTAInterface.XFUMO_PKGNAME_PATH)) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 24, 2);
        } else if (str.contains(XFOTAInterface.XFUMO_PKGVERSION_PATH)) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 24, 2);
        } else if (str.contains(XFOTAInterface.XFUMO_PKGURL_PATH)) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 25, 2);
        } else if (str.contains("/DownloadAndUpdate")) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 13, 2);
        } else if (str.contains("/Update")) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 12, 2);
        } else if (str.contains(XFOTAInterface.XFUMO_PKGDATA_PATH)) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 16, 2);
        } else if (str.contains("/Download")) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 13, 2);
        } else if (str.contains(XFOTAInterface.XFUMO_STATE_PATH)) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 8, 2);
        } else if (str.contains("/Ext")) {
            xdmAgentMakeDefaultAcl(xDMOmTree, str, 8, 2);
        }
    }

    public void xdmAgentTpClose(int i) {
        this.m_HttpDMAdapter.xtpAdpClose(i);
    }

    public int xdmAgentTpInit(int i) {
        return this.m_HttpDMAdapter.xtpAdpInit(i);
    }

    public void xdmAgentTpCloseNetwork(int i) {
        this.m_HttpDMAdapter.xtpAdpCloseNetWork(i);
    }

    public boolean xdmAgentTpCheckRetry() {
        Log.I("DM ConnectRetryCount " + m_nConnectRetryCount);
        if (XDMDevinfAdapter.xdmBlocksDueToRoamingNetwork()) {
            m_nConnectRetryCount = 3;
        }
        int i = m_nConnectRetryCount;
        if (i >= 3) {
            m_nConnectRetryCount = 0;
            return false;
        }
        m_nConnectRetryCount = i + 1;
        return true;
    }

    public static boolean xdmAgentCheckChangeProtocolCount() {
        m_nChangedProtocolCount++;
        Log.I("ChangeProtocolCount " + m_nChangedProtocolCount);
        if (m_nChangedProtocolCount < 5) {
            return true;
        }
        m_nChangedProtocolCount = 0;
        return false;
    }

    public static void xdmAgentResetChangeProtocolCount() {
        m_nChangedProtocolCount = 0;
    }

    public static void xdmAgentTpSetRetryCount(int i) {
        m_nConnectRetryCount = i;
    }

    public static boolean xdmAgentGetPendingStatus() {
        return m_bPendingStatus;
    }

    public static String xdmAgentGetDefaultLocuri() {
        return XFOTAInterface.XFUMO_PATH.concat("/DownloadAndUpdate");
    }

    private static String xdmAgentGetDevNetworkConnType() {
        String xdmGetUsingBearer = XDMCommonUtils.xdmGetUsingBearer();
        String xdbGetFUMODownloadConnType = XDBFumoAdp.xdbGetFUMODownloadConnType();
        int xdbGetDmAgentType = XDBAgentAdp.xdbGetDmAgentType();
        Log.I("nAgentType = " + xdbGetDmAgentType);
        return xdbGetDmAgentType == 0 ? xdmGetUsingBearer : xdbGetFUMODownloadConnType;
    }

    public static String xdmAgentGetSvcState() {
        return szSvcState;
    }

    public static void xdmAgentSetSvcState(String str) {
        Log.I("SvcState : " + str);
        szSvcState = str;
    }
}
