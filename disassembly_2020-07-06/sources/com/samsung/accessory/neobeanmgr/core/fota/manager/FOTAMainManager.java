package com.samsung.accessory.neobeanmgr.core.fota.manager;

import android.content.Intent;
import android.util.Log;
import com.samsung.accessory.fotaprovider.controller.RequestError;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.fota.util.FotaUtil;

public class FOTAMainManager {
    private static final String TAG = "NeoBean_FOTAMainManager";
    private static FOTAMainManager mFOTAMainManagerInstance;

    public static synchronized FOTAMainManager getInstance() {
        FOTAMainManager fOTAMainManager;
        synchronized (FOTAMainManager.class) {
            if (mFOTAMainManagerInstance == null) {
                mFOTAMainManagerInstance = new FOTAMainManager();
                Log.d(TAG, "mFOTAMainManagerInstance is null");
            }
            fOTAMainManager = mFOTAMainManagerInstance;
        }
        return fOTAMainManager;
    }

    public void updateFOTACopyProcessResult(String str, int i) {
        if (((str.hashCode() == -117388702 && str.equals(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT)) ? (char) 0 : 65535) == 0) {
            Log.d(TAG, "sendBroadcast - ACTION_FOTA_PROGRESS_COPY_RESULT: " + i);
            switch (i) {
                case 1:
                    sendBroadcastForFOTAResult(str, FotaUtil.FOTA_RESULT, 1);
                    return;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    Log.d(TAG, "ACTION_FOTA_PROGRESS_COPY_RESULT : fail");
                    sendBroadcastForFOTAResult(str, FotaUtil.FOTA_RESULT, 3);
                    FotaRequestController.mRequestResultCallback.onFailure(RequestError.ERROR_FILE_TRANSFER);
                    return;
                default:
                    return;
            }
        }
    }

    private void sendBroadcastForFOTAResult(String str, String str2, int i) {
        Intent intent = new Intent(str);
        intent.setPackage(Application.getContext().getPackageName());
        if (!"".equals(str2)) {
            intent.putExtra(str2, i);
        }
        Application.getContext().sendBroadcast(intent);
    }
}
