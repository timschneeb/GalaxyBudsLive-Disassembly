package com.samsung.accessory.neobeanmgr.core.bixby;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.JsonObject;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.bixby.BixbyConstants;
import com.samsung.android.sdk.bixby2.Sbixby;
import com.samsung.android.sdk.bixby2.action.ActionHandler;
import com.samsung.android.sdk.bixby2.action.ResponseCallback;

public class BixbyActionHandler extends ActionHandler {
    public static final String TAG = "NeoBean_BixbyActionHandler";

    public static class Actions {
        static final String ACTION_CHANGE_EQUALIZER_MODE = "ChangeEqualizerMode";
        static final String ACTION_LOCK_TOUCHPAD = "LockTouchPad";
        static final String ACTION_SHOW_BATTERY_STATUS = "ShowBatteryStatus";
        static final String ACTION_SHOW_EQ_STATUS = "ShowEQStatus";
        static final String ACTION_TURN_OFF_FEATURE = "TurnOffFeature";
        static final String ACTION_TURN_ON_FEATURE = "TurnOnFeature";
    }

    public static void initialize(Context context) {
        Log.d(TAG, "initialize()");
        Sbixby.initialize(context);
        Sbixby instance = Sbixby.getInstance();
        instance.addActionHandler("ShowBatteryStatus", new BixbyActionHandler());
        instance.addActionHandler("TurnOffFeature", new BixbyActionHandler());
        instance.addActionHandler("TurnOnFeature", new BixbyActionHandler());
        instance.addActionHandler("ShowEQStatus", new BixbyActionHandler());
        instance.addActionHandler("ChangeEqualizerMode", new BixbyActionHandler());
        instance.addActionHandler("LockTouchPad", new BixbyActionHandler());
    }

    public void executeAction(Context context, String str, Bundle bundle, ResponseCallback responseCallback) {
        Log.d(TAG, "Action name : " + str);
        JsonObject jsonObject = new JsonObject();
        if (!Application.getCoreService().isConnected()) {
            jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
            jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.NOT_CONNECTED);
            responseCallback.onComplete(jsonObject.toString());
            return;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -2004243708:
                if (str.equals("TurnOffFeature")) {
                    c = 1;
                    break;
                }
                break;
            case -1722841094:
                if (str.equals("TurnOnFeature")) {
                    c = 2;
                    break;
                }
                break;
            case -1512373182:
                if (str.equals("ShowBatteryStatus")) {
                    c = 0;
                    break;
                }
                break;
            case -881224283:
                if (str.equals("ChangeEqualizerMode")) {
                    c = 4;
                    break;
                }
                break;
            case 468238143:
                if (str.equals("LockTouchPad")) {
                    c = 5;
                    break;
                }
                break;
            case 602240475:
                if (str.equals("ShowEQStatus")) {
                    c = 3;
                    break;
                }
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c != 3) {
                        if (c != 4) {
                            if (c != 5) {
                                Log.d(TAG, "not support feature");
                                JsonObject jsonObject2 = new JsonObject();
                                jsonObject2.addProperty("result", BixbyConstants.Response.FAILURE);
                                jsonObject2.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.NOT_SUPPORTED);
                                responseCallback.onComplete(jsonObject2.toString());
                            } else if (responseCallback != null) {
                                new BixbyLockTouchpad().executeAction(responseCallback);
                            }
                        } else if (responseCallback != null) {
                            new BixbyChangeEqualizerMode().executeAction(bundle, responseCallback);
                        }
                    } else if (responseCallback != null) {
                        new BixbyShowEQStatus().executeAction(responseCallback);
                    }
                } else if (responseCallback != null) {
                    new BixbyTurnOnFeature().executeAction(context, bundle, responseCallback);
                }
            } else if (responseCallback != null) {
                new BixbyTurnOffFeature().executeAction(bundle, responseCallback);
            }
        } else if (responseCallback != null) {
            new BixbyBatteryStatus().executeAction(bundle, responseCallback);
        }
    }
}
