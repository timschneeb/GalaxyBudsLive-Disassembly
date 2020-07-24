package com.accessorydm.agent;

import com.accessorydm.XDMDmUtils;
import com.accessorydm.adapter.XDMCommonUtils;
import com.accessorydm.adapter.XDMDevinfAdapter;
import com.accessorydm.adapter.XDMTargetAdapter;
import com.accessorydm.db.file.XDBAESCrypt;
import com.accessorydm.db.file.XDBFumoAdp;
import com.accessorydm.eng.core.XDMBase64;
import com.accessorydm.interfaces.XDMInterface;
import com.accessorydm.ui.handler.XDMToastHandler;
import com.samsung.android.fotaagent.register.RegisterInterface;
import com.samsung.android.fotaprovider.log.Log;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;

public class XDMDebug implements XDMInterface {
    private static boolean DEBUG_WBXML_FILE = false;
    private static boolean bBooting = true;
    public static boolean bSessionRuning = false;
    private static final String bootinglogfile = "/dm_booting.log";
    private static final String bootstraplogfile = "/dm_bootstrap.log";
    public static int curFileIndex = 1;
    private static ByteArrayOutputStream logTemp = new ByteArrayOutputStream();
    private static FileOutputStream logfileStream = null;
    private static final String sessionlogfile = "/dm_session";

    static {
        if (logTemp != null) {
            try {
                Date date = new Date();
                logTemp.write((">> time : " + date.toString() + "\n").getBytes(Charset.defaultCharset()));
            } catch (Exception e) {
                Log.E(e.toString());
            }
        }
    }

    public static void xdmSetDebugWbxmlFile(boolean z) {
        DEBUG_WBXML_FILE = z;
        XDMToastHandler.xdmShowToast("WBXML_FILE : " + DEBUG_WBXML_FILE, 1);
        Log.I("WBXML_FILE : " + DEBUG_WBXML_FILE);
    }

    public static boolean xdmIsDebugWbxmlFile() {
        return DEBUG_WBXML_FILE;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0024 A[SYNTHETIC, Splitter:B:16:0x0024] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0033 A[SYNTHETIC, Splitter:B:21:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    public static void xdmWriteFile(String str, byte[] bArr) {
        DataOutputStream dataOutputStream = null;
        try {
            DataOutputStream dataOutputStream2 = new DataOutputStream(new FileOutputStream(str));
            try {
                dataOutputStream2.write(bArr);
                try {
                    dataOutputStream2.close();
                } catch (Exception e) {
                    Log.E(e.toString());
                }
            } catch (Exception e2) {
                e = e2;
                dataOutputStream = dataOutputStream2;
                try {
                    Log.I(e.toString());
                    if (dataOutputStream == null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Exception e3) {
                            Log.E(e3.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                dataOutputStream = dataOutputStream2;
                if (dataOutputStream != null) {
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            Log.I(e.toString());
            if (dataOutputStream == null) {
                dataOutputStream.close();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00af A[SYNTHETIC, Splitter:B:31:0x00af] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00be A[SYNTHETIC, Splitter:B:36:0x00be] */
    /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    public static void xdmSaveBootStrapLog(String str, String str2) {
        if (XDMDmUtils.getContext() != null) {
            File dir = XDMDmUtils.getContext().getDir(Log.LOGFILE_PATH, 0);
            String path = dir != null ? dir.getPath() : "";
            if (str != null && str2 != null) {
                File file = new File(path + bootstraplogfile);
                if (!file.exists() || file.length() <= RegisterInterface.DELAY_PERIOD_FOR_BACKGROUND_REGISTER || file.delete()) {
                    FileOutputStream fileOutputStream = null;
                    try {
                        StringBuilder sb = new StringBuilder();
                        FileOutputStream fileOutputStream2 = new FileOutputStream(path + bootstraplogfile, true);
                        try {
                            String xdmBase64Encode = XDMBase64.xdmBase64Encode(XDBAESCrypt.xdbEncryptor(str + str2));
                            sb.append("\t");
                            sb.append(xdmBase64Encode);
                            sb.append("\n");
                            fileOutputStream2.write(sb.toString().getBytes(Charset.defaultCharset()));
                        } catch (Exception e) {
                            e = e;
                            fileOutputStream = fileOutputStream2;
                            try {
                                Log.E(e.toString());
                                if (fileOutputStream == null) {
                                    fileOutputStream.close();
                                    return;
                                }
                                return;
                            } catch (Throwable th) {
                                th = th;
                                fileOutputStream2 = fileOutputStream;
                                if (fileOutputStream2 != null) {
                                    try {
                                        fileOutputStream2.close();
                                    } catch (Exception e2) {
                                        Log.E(e2.toString());
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (fileOutputStream2 != null) {
                            }
                            throw th;
                        }
                        try {
                            fileOutputStream2.close();
                        } catch (Exception e3) {
                            Log.E(e3.toString());
                        }
                    } catch (Exception e4) {
                        e = e4;
                        Log.E(e.toString());
                        if (fileOutputStream == null) {
                        }
                    }
                }
            }
        }
    }

    private static void xdmSaveDevInfoLog() {
        if (logfileStream != null) {
            StringBuilder sb = new StringBuilder();
            Date date = new Date();
            bSessionRuning = false;
            sb.append(">>>> time :");
            sb.append(date.toString());
            sb.append("\n");
            sb.append("//////////////// Device infomation\n");
            sb.append("Release Version : ");
            sb.append(XDMDevinfAdapter.xdmDevAdpGetAppVersion());
            sb.append("\n");
            sb.append("Model : ");
            sb.append(XDMDevinfAdapter.xdmDevAdpGetModel());
            sb.append("\n");
            sb.append("CSC : ");
            sb.append(XDMDevinfAdapter.xdmDevAdpGetSalesCode());
            sb.append("\n");
            String xdmDevAdpGetDeviceID = XDMDevinfAdapter.xdmDevAdpGetDeviceID();
            String xdmBase64Encode = XDMBase64.xdmBase64Encode(XDBAESCrypt.xdbEncryptor("DeviceID :" + xdmDevAdpGetDeviceID));
            sb.append("\t");
            sb.append(xdmBase64Encode);
            sb.append("\n");
            sb.append("Kb /data/data: ");
            sb.append(XDMTargetAdapter.xdmGetAvailableStorageSize() / 1024);
            sb.append("Kb\n");
            sb.append("////////////////\n\n");
            try {
                logfileStream.write(sb.toString().getBytes(Charset.defaultCharset()));
                try {
                    logfileStream.close();
                } catch (IOException e) {
                    Log.E(e.toString());
                }
            } catch (Exception e2) {
                Log.E(e2.toString());
                logfileStream.close();
            } catch (Throwable th) {
                try {
                    logfileStream.close();
                } catch (IOException e3) {
                    Log.E(e3.toString());
                }
                throw th;
            }
            bSessionRuning = true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00dd A[SYNTHETIC, Splitter:B:40:0x00dd] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ec A[SYNTHETIC, Splitter:B:45:0x00ec] */
    public static void xdmSetBooting(boolean z) {
        if (bBooting && !z && XDMDmUtils.getContext() != null) {
            FileOutputStream fileOutputStream = null;
            try {
                File dir = XDMDmUtils.getContext().getDir(Log.LOGFILE_PATH, 0);
                if (dir == null || !dir.exists()) {
                    Log.I("logmaindir is null");
                } else {
                    String path = dir.getPath();
                    File file = new File(path + bootinglogfile);
                    if (!file.exists() || file.delete()) {
                        if (logTemp != null) {
                            FileOutputStream fileOutputStream2 = new FileOutputStream(path + bootinglogfile, true);
                            try {
                                logTemp.write(String.format("Release Version : %s%n", new Object[]{XDMDevinfAdapter.xdmDevAdpGetAppVersion()}).getBytes(Charset.defaultCharset()));
                                fileOutputStream2.write(logTemp.toByteArray());
                                logTemp.close();
                                fileOutputStream2.close();
                                fileOutputStream = fileOutputStream2;
                            } catch (Exception e) {
                                e = e;
                                fileOutputStream = fileOutputStream2;
                                try {
                                    Log.E(e.toString());
                                    if (fileOutputStream != null) {
                                        fileOutputStream.close();
                                    }
                                    bBooting = z;
                                } catch (Throwable th) {
                                    th = th;
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                        } catch (Exception e2) {
                                            Log.E(e2.toString());
                                        }
                                    }
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                fileOutputStream = fileOutputStream2;
                                if (fileOutputStream != null) {
                                }
                                throw th;
                            }
                        }
                        if (bSessionRuning) {
                            Locale locale = Locale.US;
                            logfileStream = new FileOutputStream(String.format(locale, "%s%d.log", new Object[]{path + sessionlogfile, Integer.valueOf(curFileIndex)}), true);
                        }
                    } else {
                        return;
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e3) {
                        Log.E(e3.toString());
                    }
                }
            } catch (Exception e4) {
                e = e4;
                Log.E(e.toString());
                if (fileOutputStream != null) {
                }
                bBooting = z;
            }
        }
        bBooting = z;
    }

    public static void xdmSetSessionRuning(boolean z) {
        File dir;
        File dir2;
        if (XDBFumoAdp.xdbGetFUMOStatus() == 0 || !z) {
            if (bSessionRuning && z) {
                if (!(XDMDmUtils.getContext() == null || (dir2 = XDMDmUtils.getContext().getDir(Log.LOGFILE_PATH, 0)) == null || !dir2.exists())) {
                    Locale locale = Locale.US;
                    String format = String.format(locale, "%s%d.log", new Object[]{dir2.getPath() + sessionlogfile, Integer.valueOf(curFileIndex)});
                    try {
                        File file = new File(format);
                        if (file.exists()) {
                            if (logfileStream != null) {
                                logfileStream.close();
                                logfileStream = null;
                            }
                            if (file.delete()) {
                                logfileStream = new FileOutputStream(format, true);
                            } else {
                                return;
                            }
                        }
                    } catch (Exception e) {
                        Log.E(e.toString());
                    }
                }
                xdmSaveDevInfoLog();
            } else if (!bSessionRuning && z) {
                if (!(XDMDmUtils.getContext() == null || (dir = XDMDmUtils.getContext().getDir(Log.LOGFILE_PATH, 0)) == null || !dir.exists())) {
                    Locale locale2 = Locale.US;
                    String format2 = String.format(locale2, "%s%d.log", new Object[]{dir.getPath() + sessionlogfile, Integer.valueOf(curFileIndex)});
                    try {
                        File file2 = new File(format2);
                        if (!file2.exists() || file2.delete()) {
                            logfileStream = new FileOutputStream(format2, true);
                        } else {
                            return;
                        }
                    } catch (Exception e2) {
                        Log.E(e2.toString());
                    }
                }
                xdmSaveDevInfoLog();
            } else if (bSessionRuning && !z) {
                try {
                    if (logfileStream != null) {
                        logfileStream.close();
                        logfileStream = null;
                    }
                } catch (Exception e3) {
                    Log.E(e3.toString());
                }
                int i = curFileIndex;
                if (i >= 3) {
                    curFileIndex = 1;
                } else {
                    curFileIndex = i + 1;
                }
            }
            bSessionRuning = z;
            if (XDMDmUtils.getContext() != null) {
                XDMCommonUtils.xdmSavelogflag();
            }
        }
    }
}
