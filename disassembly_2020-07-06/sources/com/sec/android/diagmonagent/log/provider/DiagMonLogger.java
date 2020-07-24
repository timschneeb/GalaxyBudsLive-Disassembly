package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import com.samsung.android.fotaagent.update.UpdateInterface;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.sec.android.diagmonagent.log.provider.DiagMonSDK;
import com.sec.android.diagmonagent.log.provider.utils.DiagMonUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.Thread;
import java.util.ArrayList;

public class DiagMonLogger implements Thread.UncaughtExceptionHandler {
    private static final String LOG_CRASH_FILE_NAME = "diagmon.log";
    private static final String LOG_EVENT_FILE_NAME = "diagmon_event.log";
    private static final String LOG_MAIN_FILE_NAME = "diagmon_main.log";
    private static final String LOG_MEMORY_FILE_NAME = "diagmon_memory.log";
    private static final String LOG_STORAGE_FILE_NAME = "diagmon_storage.log";
    private static final String LOG_THREAD_STACK_FILE_NAME = "diagmon_thread.log";
    private static DiagMonConfig diagmonConfig;
    private static EventBuilder eventBuilder;
    private final String DIRECTORY;
    private String agree;
    private Context application;
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private final String[] logcatCmd = {"logcat -b events -v threadtime -v printable -v uid -d *:v", "cat /proc/meminfo", "df"};
    private String mCrashLogFilePath;
    private String mEventLogFilePath;
    private boolean mIsAppend = false;
    private String mMainLogFilePath;
    private String mMemoryLogFilePath;
    private String mStorageLogFilePath;
    private String mThreadStackLogFilePath;
    private boolean networkMode = true;

    public DiagMonLogger(Context context, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, DiagMonConfig diagMonConfig, boolean z, String str) {
        this.application = context;
        this.DIRECTORY = context.getApplicationInfo().dataDir + "/exception/";
        this.defaultUncaughtExceptionHandler = uncaughtExceptionHandler;
        this.agree = str;
        this.networkMode = z;
        diagmonConfig = diagMonConfig;
        setConfiguration();
    }

    private void setConfiguration() {
        if (DiagMonSDK.isEnableDefaultConfiguration()) {
            DiagMonSDK.DiagMonHelper.setConfiguration(diagmonConfig);
        }
        this.mMainLogFilePath = this.DIRECTORY + LOG_MAIN_FILE_NAME;
        this.mCrashLogFilePath = this.DIRECTORY + LOG_CRASH_FILE_NAME;
        this.mEventLogFilePath = this.DIRECTORY + LOG_EVENT_FILE_NAME;
        this.mThreadStackLogFilePath = this.DIRECTORY + LOG_THREAD_STACK_FILE_NAME;
        this.mMemoryLogFilePath = this.DIRECTORY + LOG_MEMORY_FILE_NAME;
        this.mStorageLogFilePath = this.DIRECTORY + LOG_STORAGE_FILE_NAME;
        Log.d(DiagMonUtil.TAG, "Diagmon Logger Init");
        String str = DiagMonUtil.TAG;
        Log.d(str, "MAIN_LOG_PATH : " + this.mMainLogFilePath);
        String str2 = DiagMonUtil.TAG;
        Log.d(str2, "CRASH_LOG_PATH : " + this.mCrashLogFilePath);
        String str3 = DiagMonUtil.TAG;
        Log.d(str3, "EVENT_LOG_PATH : " + this.mEventLogFilePath);
        String str4 = DiagMonUtil.TAG;
        Log.d(str4, "THREAD_STACK_LOG_PATH : " + this.mThreadStackLogFilePath);
        String str5 = DiagMonUtil.TAG;
        Log.d(str5, "MEMORY_LOG_PATH : " + this.mMemoryLogFilePath);
        String str6 = DiagMonUtil.TAG;
        Log.d(str6, "STORAGE_LOG_PATH : " + this.mStorageLogFilePath);
        if (DiagMonUtil.checkDMA(this.application) == 1) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.DIRECTORY + "/" + LOG_CRASH_FILE_NAME);
            eventBuilder = new EventBuilder(this.application).setNetworkMode(this.networkMode).setErrorCode("fatal exception");
        } else if (DiagMonUtil.checkDMA(this.application) == 2) {
            eventBuilder = new EventBuilder(this.application).setLogPath(this.DIRECTORY).setNetworkMode(this.networkMode).setErrorCode("fatal exception");
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x00e4 */
    public void uncaughtException(Thread thread, Throwable th) {
        if (this.application == null) {
            Log.w(DiagMonUtil.TAG, "There is no your context!");
            return;
        }
        if (diagmonConfig.getAgree()) {
            String str = DiagMonUtil.TAG;
            Log.d(str, "Agreement for ueHandler : " + diagmonConfig.getAgree());
            String str2 = DiagMonUtil.TAG;
            Log.d(str2, "Agreement for ueHandler : " + diagmonConfig.getAgreeAsString());
            makeLogFile(makeFile(this.DIRECTORY, LOG_CRASH_FILE_NAME), th, (String) null);
            makeLogFile(makeFile(this.DIRECTORY, LOG_EVENT_FILE_NAME), th, getLogFromBuffer(this.application, this.logcatCmd[0]));
            makeLogFile(makeFile(this.DIRECTORY, LOG_THREAD_STACK_FILE_NAME), th, getLogByThreads());
            makeLogFile(makeFile(this.DIRECTORY, LOG_MEMORY_FILE_NAME), th, getLogFromBuffer(this.application, this.logcatCmd[1]));
            makeLogFile(makeFile(this.DIRECTORY, LOG_STORAGE_FILE_NAME), th, getLogFromBuffer(this.application, this.logcatCmd[2]));
            if (DiagMonUtil.checkDMA(this.application) == 1) {
                eventBuilder.setLogPath(this.DIRECTORY);
            }
            eventReport();
        } else {
            String str3 = DiagMonUtil.TAG;
            Log.d(str3, "not agreed : " + diagmonConfig.getAgreeAsString());
        }
        removeLogs();
        synchronized (this) {
            wait(UpdateInterface.HOLDING_AFTER_BT_CONNECTED);
        }
        this.defaultUncaughtExceptionHandler.uncaughtException(thread, th);
    }

    private void eventReport() {
        DiagMonSDK.DiagMonHelper.eventReport(this.application.getApplicationContext(), diagmonConfig, eventBuilder);
    }

    private void removeLogs() {
        File file = new File(this.DIRECTORY);
        if (!file.exists()) {
            Log.d(DiagMonUtil.TAG, "The directory doesn't exist.");
            return;
        }
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                removeLogs();
            } else {
                file2.delete();
            }
        }
    }

    private String getLogByThreads() {
        String str = "";
        for (Thread next : Thread.getAllStackTraces().keySet()) {
            StackTraceElement[] stackTrace = next.getStackTrace();
            if (stackTrace.length < 1) {
                Log.d(DiagMonUtil.TAG, "no StackTraceElement");
            } else {
                String str2 = str + "Thread ID : " + next.getId() + ", Thread's name : " + next.getName() + "\n";
                for (StackTraceElement stackTraceElement : stackTrace) {
                    str2 = str2 + "\t at " + stackTraceElement.toString() + "\n";
                }
                str = str2 + "\n";
            }
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        return str + "No data";
    }

    private File makeDir(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private File makeFile(String str, String str2) {
        if (!makeDir(str).isDirectory()) {
            return null;
        }
        File file = new File(str + "/" + str2);
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            Debug.LogENG(e.getLocalizedMessage());
            return file;
        }
    }

    private String getLogFromBuffer(Context context, String str) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder("=========================================\nService version   : " + packageInfo.versionName + "\nDiagMonSA SDK version : " + "6.05.025" + "\n=========================================\n");
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(str).getInputStream()));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                    sb.append("\n");
                }
            } catch (IOException unused) {
                Log.e(DiagMonUtil.TAG, "IOException occurred during getCrashLog");
            }
            return sb.toString();
        } catch (PackageManager.NameNotFoundException unused2) {
            Log.e(DiagMonUtil.TAG, "NameNotFoundException occurred during getAddtionalLog");
            return "";
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0030, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0035, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r4.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0039, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x003c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0045, code lost:
        throw r4;
     */
    private void makeLogFile(File file, Throwable th, String str) {
        if (file == null || !file.exists() || th == null) {
            Log.d(DiagMonUtil.TAG, "Failed to write log into file");
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, this.mIsAppend);
            PrintStream printStream = new PrintStream(fileOutputStream);
            this.mIsAppend = true;
            if (TextUtils.isEmpty(str)) {
                th.printStackTrace(printStream);
            } else {
                printStream.println(str);
            }
            printStream.close();
            fileOutputStream.close();
        } catch (IOException unused) {
            Log.e(DiagMonUtil.TAG, "IOException occurred during writeLogFile");
        } catch (Throwable th2) {
            r3.addSuppressed(th2);
        }
    }
}
