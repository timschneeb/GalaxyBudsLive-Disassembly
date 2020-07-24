package com.samsung.accessory.neobeanmgr.core;

import android.util.Log;
import com.samsung.accessory.fotaprovider.AccessoryState;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.FmmConstants;

public class EarBudsFotaInfo {
    private static final String TAG = "NeoBean_EarBudsFotaInfo";
    public String deviceId;
    public String firmwareVersion;
    public int isFotaDM;
    public String modelNumber = FmmConstants.MODEL_NUMBER;
    public String salesCode;
    public String serialNumber;
    public AccessoryState state;
    public String uniqueNumber = "";

    public void printFota() {
        Log.d(TAG, " : isFotaDM : " + this.isFotaDM);
        Log.d(TAG, " : deviceId : " + this.deviceId);
        Log.d(TAG, " : modelNumber : " + this.modelNumber);
        Log.d(TAG, " : salesCode : " + this.salesCode);
        Log.d(TAG, " : firmwareVersion : " + this.firmwareVersion);
        Log.d(TAG, " : uniqueNumber : " + this.uniqueNumber);
        Log.d(TAG, " : serialNumber : " + this.serialNumber);
    }
}
