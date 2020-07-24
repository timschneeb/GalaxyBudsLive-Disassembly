package com.samsung.accessory.neobeanmgr.core.bixby;

import android.os.Bundle;
import android.util.Log;
import com.google.gson.JsonObject;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.bixby.BixbyConstants;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import com.samsung.android.sdk.bixby2.action.ActionHandler;
import com.samsung.android.sdk.bixby2.action.ResponseCallback;
import java.util.HashMap;
import java.util.List;

public class BixbyChangeEqualizerMode {
    public static final String TAG = "NeoBean_BixbyChangeEqualizerMode";

    /* JADX WARNING: Can't fix incorrect switch cases order */
    public void executeAction(Bundle bundle, ResponseCallback responseCallback) {
        char c;
        JsonObject jsonObject = new JsonObject();
        HashMap hashMap = (HashMap) bundle.getSerializable(ActionHandler.PARAMS);
        int i = 0;
        String str = null;
        if (hashMap != null) {
            for (String str2 : hashMap.keySet()) {
                Log.d(TAG, "key : " + str2.toString() + " value : " + ((String) ((List) hashMap.get(str2)).get(0)));
                if (str2.equals(BixbyConstants.Response.EQUALIZER_MODE)) {
                    str = (String) ((List) hashMap.get(str2)).get(0);
                }
            }
        } else {
            Log.e(TAG, "paramsMap == null");
        }
        if (str != null) {
            switch (str.hashCode()) {
                case -1955878649:
                    if (str.equals(BixbyConstants.Response.NORMAL)) {
                        c = 0;
                        break;
                    }
                case -505546721:
                    if (str.equals(BixbyConstants.Response.DYNAMIC)) {
                        c = 3;
                        break;
                    }
                case -398284401:
                    if (str.equals(BixbyConstants.Response.TREBLE_BOOST)) {
                        c = 5;
                        break;
                    }
                case 2582602:
                    if (str.equals(BixbyConstants.Response.SOFT)) {
                        c = 2;
                        break;
                    }
                case 65193517:
                    if (str.equals(BixbyConstants.Response.CLEAR)) {
                        c = 4;
                        break;
                    }
                case 561430500:
                    if (str.equals(BixbyConstants.Response.BASS_BOOST)) {
                        c = 1;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            if (c != 0) {
                i = c != 1 ? c != 2 ? c != 3 ? c != 4 ? c != 5 ? -1 : 5 : 4 : 3 : 2 : 1;
            }
            EarBudsInfo earBudsInfo = Application.getCoreService().getEarBudsInfo();
            if (earBudsInfo.equalizerType == i) {
                jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.ALREADY_SET);
            } else if (i == -1) {
                jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.NOT_SUPPORTED);
            } else {
                Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.EQUALIZER, (byte) i));
                earBudsInfo.equalizerType = i;
                SamsungAnalyticsUtil.setStatusString(SA.Status.EQUALIZER_STATUS, SamsungAnalyticsUtil.equalizerTypeToDetail(i));
                jsonObject.addProperty("result", "success");
                jsonObject.addProperty(BixbyConstants.Response.EQUALIZER_MODE, str);
            }
            Log.d(TAG, jsonObject.toString());
            responseCallback.onComplete(jsonObject.toString());
        }
    }
}
