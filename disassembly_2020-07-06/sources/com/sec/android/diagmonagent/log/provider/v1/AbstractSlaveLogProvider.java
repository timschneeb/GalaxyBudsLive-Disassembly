package com.sec.android.diagmonagent.log.provider.v1;

import android.os.Bundle;

public abstract class AbstractSlaveLogProvider extends AbstractLogProvider {
    public boolean onCreate() {
        if (!super.onCreate()) {
            return false;
        }
        this.data.putBundle("authorityList", Bundle.EMPTY);
        return true;
    }
}
