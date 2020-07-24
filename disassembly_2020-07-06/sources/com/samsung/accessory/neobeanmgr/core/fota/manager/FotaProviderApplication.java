package com.samsung.accessory.neobeanmgr.core.fota.manager;

import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.android.fotaprovider.FotaProviderInitializer;

public class FotaProviderApplication {
    private static final String TAG = (Application.TAG_ + FotaProviderApplication.class.getSimpleName());

    public static void init(Application application) {
        Log.d(TAG, "init()");
        FotaProviderInitializer.initializeFotaProviderWithAccessoryController(application, new FotaController());
    }

    public static void terminate(Application application) {
        Log.d(TAG, "terminate()");
        FotaProviderInitializer.terminateFotaProvider(application);
    }
}
