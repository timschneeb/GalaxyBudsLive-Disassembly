package com.sec.android.diagmonagent.log.provider.utils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.sec.android.diagmonagent.log.provider.DiagMonConfig;
import com.sec.android.diagmonagent.log.provider.EventBuilder;
import java.io.File;

public class Validator {
    public static boolean validateLegacyConfig(DiagMonConfig diagMonConfig) {
        if (diagMonConfig == null) {
            Log.w(DiagMonUtil.TAG, "DiagMonConfiguration has to be set");
            return true;
        } else if (diagMonConfig.getServiceId() == null || diagMonConfig.getServiceId().isEmpty()) {
            Log.w(DiagMonUtil.TAG, "Service ID has to be set");
            return true;
        } else if (diagMonConfig.getAgree()) {
            return false;
        } else {
            Log.w(DiagMonUtil.TAG, "You have to agree to terms and conditions");
            return true;
        }
    }

    public static boolean validateSrObj(Context context, Bundle bundle) {
        if (bundle.getString("serviceId") == null || bundle.getString("serviceId").isEmpty()) {
            Log.w(DiagMonUtil.TAG, "Service ID has to be set");
            return true;
        } else if (bundle.getString(DiagMonUtil.SERVICE_AGREE) == null || bundle.getString(DiagMonUtil.SERVICE_AGREE).isEmpty()) {
            Log.w(DiagMonUtil.TAG, "You have to agree to terms and conditions");
            return true;
        } else {
            String string = bundle.getString(DiagMonUtil.SERVICE_AGREE);
            if (string.equals("S")) {
                String str = DiagMonUtil.TAG;
                Log.d(str, "Agreement value : " + string);
            } else if (string.equals("D")) {
                String str2 = DiagMonUtil.TAG;
                Log.d(str2, "Agreement value : " + string);
                if (bundle.getString("deviceId") != null && !bundle.getString("deviceId").isEmpty()) {
                    Log.w(DiagMonUtil.TAG, "You can't use setDeviceId API if you used setAgree as Diagnostic agreement");
                    return true;
                }
            } else {
                String str3 = DiagMonUtil.TAG;
                Log.w(str3, "Undefined agreement : " + string);
                return true;
            }
            if (bundle.getString(DiagMonUtil.SERVICE_VER) == null || bundle.getString(DiagMonUtil.SERVICE_VER).isEmpty()) {
                Log.w(DiagMonUtil.TAG, "No service version");
                return true;
            } else if (bundle.getString(DiagMonUtil.SDK_VERSION).isEmpty()) {
                Log.w(DiagMonUtil.TAG, "No SDK version");
                return true;
            } else if (!bundle.getString(DiagMonUtil.SDK_TYPE).isEmpty()) {
                return false;
            } else {
                Log.w(DiagMonUtil.TAG, "No SDK type");
                return true;
            }
        }
    }

    public static boolean isValidLegacyEventBuilder(EventBuilder eventBuilder) {
        if (!TextUtils.isEmpty(eventBuilder.getErrorCode())) {
            return false;
        }
        Log.w(DiagMonUtil.TAG, "No Result code - you have to set");
        return true;
    }

    public static boolean validateErObj(Context context, Bundle bundle, Bundle bundle2) {
        if (bundle.getString("serviceId") == null || bundle.getString("serviceId").isEmpty()) {
            Log.w(DiagMonUtil.TAG, "Service ID has to be set");
            return true;
        } else if (bundle.getString(DiagMonUtil.SERVICE_VER) == null || bundle.getString(DiagMonUtil.SERVICE_VER).isEmpty()) {
            Log.w(DiagMonUtil.TAG, "No Service version");
            return true;
        } else if (bundle.getString(DiagMonUtil.SDK_VERSION).isEmpty()) {
            Log.w(DiagMonUtil.TAG, "No SDK version");
            return true;
        } else if (bundle.getString(DiagMonUtil.SDK_TYPE).isEmpty()) {
            Log.w(DiagMonUtil.TAG, "No SDK type");
            return true;
        } else if (bundle.getString(DiagMonUtil.SERVICE_AGREE) == null || bundle.getString(DiagMonUtil.SERVICE_AGREE).isEmpty()) {
            Log.w(DiagMonUtil.TAG, "You have to agree to terms and conditions");
            return true;
        } else {
            String string = bundle.getString(DiagMonUtil.SERVICE_AGREE);
            if (string.equals("S")) {
                String str = DiagMonUtil.TAG;
                Log.d(str, "Agreement : " + string);
                return false;
            } else if (string.equals("D")) {
                String str2 = DiagMonUtil.TAG;
                Log.d(str2, "Agreement : " + string);
                return false;
            } else {
                String str3 = DiagMonUtil.TAG;
                Log.w(str3, "Undefined agreement : " + string);
                return true;
            }
        }
    }

    public static boolean isValidLogPath(String str) {
        File file = new File(str);
        if (!file.isDirectory() || file.listFiles().length < 1) {
            return true;
        }
        return false;
    }
}
