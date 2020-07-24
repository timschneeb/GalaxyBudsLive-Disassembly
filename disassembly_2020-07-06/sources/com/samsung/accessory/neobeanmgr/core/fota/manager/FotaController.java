package com.samsung.accessory.neobeanmgr.core.fota.manager;

import android.util.Log;
import com.samsung.accessory.fotaprovider.AccessoryController;
import com.samsung.accessory.neobeanmgr.Application;

public class FotaController extends AccessoryController {
    private static final String TAG = (Application.TAG_ + FotaController.class.getSimpleName());

    public FotaController() {
        Log.d(TAG, "FotaController()");
        this.connectionController = new FotaConnectionController();
        this.requestController = new FotaRequestController();
        this.accessoryUtil = new FotaServerUtil();
    }
}
