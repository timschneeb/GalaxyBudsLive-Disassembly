package com.samsung.accessory.neobeanmgr.core.bixby;

import android.os.Bundle;
import android.util.Log;
import com.google.gson.JsonObject;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bixby.BixbyConstants;
import com.samsung.android.sdk.bixby2.action.ActionHandler;
import com.samsung.android.sdk.bixby2.action.ResponseCallback;
import java.util.HashMap;
import java.util.List;

public class BixbyBatteryStatus {
    private static final int EARBUD_PLACEMENT_IN_OPEN_CASE = 3;
    public static final String TAG = "NeoBean_BixbyBatteryStatus";

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00cd, code lost:
        if (r1.batteryR > 0) goto L_0x00cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01a1, code lost:
        if (r1.batteryR > 0) goto L_0x01a5;
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00cb  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0194  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x019f  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01b1  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01b5  */
    public void executeAction(Bundle bundle, ResponseCallback responseCallback) {
        String str;
        String str2;
        String str3;
        JsonObject jsonObject = new JsonObject();
        HashMap hashMap = (HashMap) bundle.getSerializable(ActionHandler.PARAMS);
        String str4 = null;
        if (hashMap != null) {
            for (String str5 : hashMap.keySet()) {
                Log.d(TAG, "" + ((String) ((List) hashMap.get(str5)).get(0)));
                if (str5.equals(BixbyConstants.Response.SIDE)) {
                    str4 = (String) ((List) hashMap.get(str5)).get(0);
                }
            }
        } else {
            Log.e(TAG, "paramsMap == null");
        }
        Log.d(TAG, "side : " + str4);
        EarBudsInfo earBudsInfo = Application.getCoreService().getEarBudsInfo();
        String adjustValue = getAdjustValue(earBudsInfo);
        boolean equals = str4.equals(BixbyConstants.Response.BOTH);
        String str6 = BixbyConstants.Response.NOT_CONNECTED;
        if (equals) {
            jsonObject.addProperty("result", "success");
            jsonObject.addProperty(BixbyConstants.Response.ADJUST, adjustValue);
            if (adjustValue.equals("")) {
                if (earBudsInfo.batteryL > 0) {
                    str3 = Integer.toString(earBudsInfo.batteryL);
                    jsonObject.addProperty(BixbyConstants.Response.LEFT_RESULT, str3);
                    if (adjustValue.equals("")) {
                        if (earBudsInfo.batteryR > 0) {
                            adjustValue = Integer.toString(earBudsInfo.batteryR);
                        }
                        jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, str6);
                        str = TAG;
                    }
                    str6 = adjustValue;
                    jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, str6);
                    str = TAG;
                }
            } else if (earBudsInfo.batteryL > 0) {
                str3 = adjustValue;
                jsonObject.addProperty(BixbyConstants.Response.LEFT_RESULT, str3);
                if (adjustValue.equals("")) {
                }
                str6 = adjustValue;
                jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, str6);
                str = TAG;
            }
            str3 = str6;
            jsonObject.addProperty(BixbyConstants.Response.LEFT_RESULT, str3);
            if (adjustValue.equals("")) {
            }
            str6 = adjustValue;
            jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, str6);
            str = TAG;
        } else {
            boolean equals2 = str4.equals(BixbyConstants.Response.LEFT);
            str = TAG;
            if (equals2) {
                if (earBudsInfo.batteryL > 0) {
                    jsonObject.addProperty("result", "success");
                    jsonObject.addProperty(BixbyConstants.Response.ADJUST, adjustValue);
                    if (adjustValue.equals("")) {
                        adjustValue = Integer.toString(earBudsInfo.batteryL);
                    }
                    jsonObject.addProperty(BixbyConstants.Response.LEFT_RESULT, adjustValue);
                } else {
                    jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                    jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, str6);
                    jsonObject.addProperty(BixbyConstants.Response.SIDE, BixbyConstants.Response.LEFT);
                }
            } else if (str4.equals(BixbyConstants.Response.RIGHT)) {
                if (earBudsInfo.batteryR > 0) {
                    jsonObject.addProperty("result", "success");
                    jsonObject.addProperty(BixbyConstants.Response.ADJUST, adjustValue);
                    if (adjustValue.equals("")) {
                        adjustValue = Integer.toString(earBudsInfo.batteryR);
                    }
                    jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, adjustValue);
                } else {
                    jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                    jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, str6);
                    jsonObject.addProperty(BixbyConstants.Response.SIDE, BixbyConstants.Response.RIGHT);
                }
            } else if (str4.equals(BixbyConstants.Response.CRADLE)) {
                if (earBudsInfo.placementL >= 3 || earBudsInfo.placementR >= 3) {
                    jsonObject.addProperty("result", "success");
                    jsonObject.addProperty(BixbyConstants.Response.CRADLE_RESULT, Integer.toString(earBudsInfo.batteryCase));
                } else {
                    jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                    jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, str6);
                }
            } else if (str4.equals(BixbyConstants.Response.ALL)) {
                jsonObject.addProperty("result", "success");
                jsonObject.addProperty(BixbyConstants.Response.ADJUST, adjustValue);
                if (adjustValue.equals("")) {
                    if (earBudsInfo.batteryL > 0) {
                        str2 = Integer.toString(earBudsInfo.batteryL);
                        jsonObject.addProperty(BixbyConstants.Response.LEFT_RESULT, str2);
                        if (adjustValue.equals("")) {
                            if (earBudsInfo.batteryR > 0) {
                                adjustValue = Integer.toString(earBudsInfo.batteryR);
                                jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, adjustValue);
                                if (earBudsInfo.placementL >= 3 || earBudsInfo.placementR >= 3) {
                                    jsonObject.addProperty(BixbyConstants.Response.CRADLE_RESULT, Integer.toString(earBudsInfo.batteryCase));
                                } else {
                                    jsonObject.addProperty(BixbyConstants.Response.CRADLE_RESULT, str6);
                                }
                            }
                        }
                        adjustValue = str6;
                        jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, adjustValue);
                        if (earBudsInfo.placementL >= 3 || earBudsInfo.placementR >= 3) {
                        }
                    }
                } else if (earBudsInfo.batteryL > 0) {
                    str2 = adjustValue;
                    jsonObject.addProperty(BixbyConstants.Response.LEFT_RESULT, str2);
                    if (adjustValue.equals("")) {
                    }
                    adjustValue = str6;
                    jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, adjustValue);
                    if (earBudsInfo.placementL >= 3 || earBudsInfo.placementR >= 3) {
                    }
                }
                str2 = str6;
                jsonObject.addProperty(BixbyConstants.Response.LEFT_RESULT, str2);
                if (adjustValue.equals("")) {
                }
                adjustValue = str6;
                jsonObject.addProperty(BixbyConstants.Response.RIGHT_RESULT, adjustValue);
                if (earBudsInfo.placementL >= 3 || earBudsInfo.placementR >= 3) {
                }
            } else {
                jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.NOT_SUPPORTED);
            }
        }
        Log.d(str, jsonObject.toString());
        responseCallback.onComplete(jsonObject.toString());
    }

    private String getAdjustValue(EarBudsInfo earBudsInfo) {
        Log.d(TAG, "getAdjustValue() : " + earBudsInfo.batteryI);
        return earBudsInfo.batteryI == -1 ? "" : Integer.toString(earBudsInfo.batteryI);
    }
}
