package com.samsung.accessory.neobeanmgr.core.bixby;

import android.util.Log;
import com.google.gson.JsonObject;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bixby.BixbyConstants;
import com.samsung.android.sdk.bixby2.action.ResponseCallback;

public class BixbyShowEQStatus {
    public static final String TAG = "NeoBean_BixbyShowEQStatus";
    String[] preset = {BixbyConstants.Response.NORMAL, BixbyConstants.Response.BASS_BOOST, BixbyConstants.Response.SOFT, BixbyConstants.Response.DYNAMIC, BixbyConstants.Response.CLEAR, BixbyConstants.Response.TREBLE_BOOST};

    public void executeAction(ResponseCallback responseCallback) {
        JsonObject jsonObject = new JsonObject();
        EarBudsInfo earBudsInfo = Application.getCoreService().getEarBudsInfo();
        jsonObject.addProperty("result", "success");
        jsonObject.addProperty(BixbyConstants.Response.EQUALIZER_MODE, this.preset[earBudsInfo.equalizerType]);
        Log.d(TAG, jsonObject.toString());
        responseCallback.onComplete(jsonObject.toString());
    }
}
