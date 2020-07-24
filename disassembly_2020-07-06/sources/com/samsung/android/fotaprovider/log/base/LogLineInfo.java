package com.samsung.android.fotaprovider.log.base;

import java.util.ArrayList;
import java.util.List;

public class LogLineInfo {
    private static List<String> classNameList = new ArrayList();

    static {
        try {
            excludeClass(Class.forName("dalvik.system.VMStack"));
        } catch (ClassNotFoundException unused) {
        }
        excludeClass(Thread.class, LogLineInfo.class);
    }

    public static void excludeClass(Class<?>... clsArr) {
        for (Class<?> name : clsArr) {
            classNameList.add(name.getName());
        }
    }

    /* access modifiers changed from: protected */
    public StackTraceElement peekStack() {
        for (StackTraceElement stackTraceElement : getStackTrace()) {
            if (!getClassNameList().contains(stackTraceElement.getClassName())) {
                return stackTraceElement;
            }
        }
        return new StackTraceElement("<getStackTrace() failed>", "<getStackTrace() failed>", "<getStackTrace() failed>", -1);
    }

    /* access modifiers changed from: protected */
    public List<String> getClassNameList() {
        return classNameList;
    }

    /* access modifiers changed from: protected */
    public StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    public String makeLogLine(String str) {
        StackTraceElement peekStack = peekStack();
        return "[" + peekStack.getClassName() + "(" + peekStack.getLineNumber() + "/" + peekStack.getMethodName() + ")] " + str;
    }
}
