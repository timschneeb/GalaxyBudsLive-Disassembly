package com.samsung.accessory.neobeanmgr.core.bixby;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.bixby.BixbyConstants;
import com.samsung.accessory.neobeanmgr.core.gamemode.GameModeManager;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetNoiseReduction;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import com.samsung.android.sdk.bixby2.action.ActionHandler;
import com.samsung.android.sdk.bixby2.action.ResponseCallback;
import java.util.HashMap;
import java.util.List;

public class BixbyTurnOnFeature {
    public static final String TAG = "NeoBean_BixbyTurnOnFeature";

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00ab, code lost:
        if (r6.equals(com.samsung.accessory.neobeanmgr.core.bixby.BixbyConstants.Response.GAMING_MODE) != false) goto L_0x00af;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0188  */
    public void executeAction(Context context, Bundle bundle, ResponseCallback responseCallback) {
        ResponseCallback responseCallback2;
        String str;
        ResponseCallback responseCallback3 = responseCallback;
        JsonObject jsonObject = new JsonObject();
        HashMap hashMap = (HashMap) bundle.getSerializable(ActionHandler.PARAMS);
        char c = 0;
        String str2 = null;
        if (hashMap != null) {
            for (String str3 : hashMap.keySet()) {
                Log.d(TAG, "key : " + str3.toString() + " value : " + ((String) ((List) hashMap.get(str3)).get(0)));
                if (str3.equals(BixbyConstants.Response.GALAXY_BUDS_FEATURE_NAME)) {
                    str2 = (String) ((List) hashMap.get(str3)).get(0);
                }
            }
        } else {
            Log.e(TAG, "paramsMap == null");
        }
        if (str2 != null) {
            EarBudsInfo earBudsInfo = Application.getCoreService().getEarBudsInfo();
            int hashCode = str2.hashCode();
            if (hashCode != -1658843502) {
                if (hashCode != 65974) {
                    if (hashCode == 66015 && str2.equals(BixbyConstants.Response.AOM)) {
                        c = 1;
                        if (c != 0) {
                            String str4 = TAG;
                            if (c != 1) {
                                if (c != 2) {
                                    jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                                    jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.NOT_SUPPORTED);
                                    JsonArray jsonArray = new JsonArray();
                                    jsonArray.add(BixbyConstants.Response.ANC);
                                    if (Application.getAomManager().isSupportAOM()) {
                                        jsonArray.add(BixbyConstants.Response.AOM);
                                    }
                                    if (GameModeManager.isSupportDevice()) {
                                        jsonArray.add(BixbyConstants.Response.GAMING_MODE);
                                    }
                                    jsonObject.add(BixbyConstants.Response.AVAILABLE_FEATURE, jsonArray);
                                } else if (earBudsInfo.noiseReduction) {
                                    jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                                    jsonObject.addProperty(BixbyConstants.Response.GALAXY_BUDS_FEATURE_NAME, str2);
                                    jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.ALREADY_ON);
                                } else {
                                    earBudsInfo.noiseReduction = true;
                                    Application.getCoreService().sendSppMessage(new MsgSetNoiseReduction(true));
                                    jsonObject.addProperty("result", "success");
                                    jsonObject.addProperty(BixbyConstants.Response.GALAXY_BUDS_FEATURE_NAME, str2);
                                }
                                responseCallback2 = responseCallback;
                                str = str4;
                            } else if (!Application.getAomManager().isSupportAOM()) {
                                jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                                jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.NOT_SUPPORTED);
                                JsonArray jsonArray2 = new JsonArray();
                                jsonArray2.add(BixbyConstants.Response.ANC);
                                if (GameModeManager.isSupportDevice()) {
                                    jsonArray2.add(BixbyConstants.Response.GAMING_MODE);
                                }
                                jsonObject.add(BixbyConstants.Response.AVAILABLE_FEATURE, jsonArray2);
                                Log.d(str4, jsonObject.toString());
                                responseCallback.onComplete(jsonObject.toString());
                                return;
                            } else {
                                responseCallback2 = responseCallback;
                                str = str4;
                                if (earBudsInfo.voiceWakeUp) {
                                    jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                                    jsonObject.addProperty(BixbyConstants.Response.GALAXY_BUDS_FEATURE_NAME, str2);
                                    jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.ALREADY_ON);
                                } else {
                                    Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.SET_VOICE_WAKE_UP, (byte) 1));
                                    earBudsInfo.voiceWakeUp = true;
                                    SamsungAnalyticsUtil.setStatusString(SA.Status.VOICE_WAKE_UP, earBudsInfo.voiceWakeUp ? "1" : "0");
                                    jsonObject.addProperty("result", "success");
                                    jsonObject.addProperty(BixbyConstants.Response.GALAXY_BUDS_FEATURE_NAME, str2);
                                }
                            }
                        } else {
                            str = TAG;
                            responseCallback2 = responseCallback;
                            if (!GameModeManager.isSupportDevice()) {
                                jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                                jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.NOT_SUPPORTED);
                                JsonArray jsonArray3 = new JsonArray();
                                jsonArray3.add(BixbyConstants.Response.ANC);
                                if (Application.getAomManager().isSupportAOM()) {
                                    jsonArray3.add(BixbyConstants.Response.AOM);
                                }
                                jsonObject.add(BixbyConstants.Response.AVAILABLE_FEATURE, jsonArray3);
                                Log.d(str, jsonObject.toString());
                                responseCallback2.onComplete(jsonObject.toString());
                                return;
                            } else if (earBudsInfo.adjustSoundSync) {
                                jsonObject.addProperty("result", BixbyConstants.Response.FAILURE);
                                jsonObject.addProperty(BixbyConstants.Response.GALAXY_BUDS_FEATURE_NAME, str2);
                                jsonObject.addProperty(BixbyConstants.Response.MORE_INFO, BixbyConstants.Response.ALREADY_ON);
                            } else {
                                Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.ADJUST_SOUND_SYNC, (byte) 1));
                                earBudsInfo.adjustSoundSync = true;
                                SamsungAnalyticsUtil.setStatusString(SA.Status.GAME_MODE, earBudsInfo.adjustSoundSync ? "1" : "0");
                                jsonObject.addProperty("result", "success");
                                jsonObject.addProperty(BixbyConstants.Response.GALAXY_BUDS_FEATURE_NAME, str2);
                            }
                        }
                        Log.d(str, jsonObject.toString());
                        responseCallback2.onComplete(jsonObject.toString());
                    }
                } else if (str2.equals(BixbyConstants.Response.ANC)) {
                    c = 2;
                    if (c != 0) {
                    }
                    Log.d(str, jsonObject.toString());
                    responseCallback2.onComplete(jsonObject.toString());
                }
            }
            c = 65535;
            if (c != 0) {
            }
            Log.d(str, jsonObject.toString());
            responseCallback2.onComplete(jsonObject.toString());
        }
    }
}
