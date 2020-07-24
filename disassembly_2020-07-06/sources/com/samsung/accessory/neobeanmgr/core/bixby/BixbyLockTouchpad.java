package com.samsung.accessory.neobeanmgr.core.bixby;

import android.util.Log;
import com.google.gson.JsonObject;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.bixby.BixbyConstants;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLockTouchpad;
import com.samsung.android.sdk.bixby2.action.ResponseCallback;

public class BixbyLockTouchpad {
    public static final String TAG = "NeoBean_BixbyLockTouchpad";

    public void executeAction(ResponseCallback responseCallback) {
        JsonObject jsonObject = new JsonObject();
        if (Application.getCoreService().getEarBudsInfo().touchpadLocked) {
            jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
            jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.ALREADY_LOCKED);
        } else {
            Application.getCoreService().getEarBudsInfo().touchpadLocked = true;
            Application.getCoreService().sendSppMessage(new MsgLockTouchpad(true));
            jsonObject.addProperty("result", "success");
        }
        Log.d(TAG, jsonObject.toString());
        responseCallback.onComplete(jsonObject.toString());
    }
}
