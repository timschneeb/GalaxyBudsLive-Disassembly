package com.samsung.android.fotaprovider.log.base;

import com.samsung.android.fotaprovider.log.base.Logger;
import java.util.ArrayList;
import java.util.List;

public class LoggerCollection extends Logger.Impl implements Logger.Core, Logger {
    private List<Logger.Core> list = new ArrayList();

    static {
        LogLineInfo.excludeClass(LoggerCollection.class);
    }

    public LoggerCollection(Logger.Core... coreArr) {
        for (Logger.Core add : coreArr) {
            this.list.add(add);
        }
    }

    public void println(int i, String str) {
        for (Logger.Core println : this.list) {
            println.println(i, str);
        }
    }
}
