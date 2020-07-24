package com.samsung.accessory.neobeanmgr.core.bixbyroutine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.pm.PackageInfoCompat;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.android.SDK.routine.Constants;
import com.samsung.context.sdk.samsunganalytics.ErrorType;

public class RoutineUtils {
    private static final String TAG = (Application.TAG_ + RoutineUtils.class.getSimpleName());

    public static void showErrorDialog(final Activity activity, int i) {
        String str = TAG;
        Log.d(str, "showErrorDialog() :: activity : " + activity + ", validState : " + i);
        activity.requestWindowFeature(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.action_error_title);
        switch (i) {
            case -105:
                builder.setMessage(R.string.action_error_permission_denied);
                break;
            case -104:
                builder.setMessage(R.string.action_error_not_supported_gaming_mode);
                break;
            case -103:
                builder.setMessage(R.string.action_error_during_call);
                break;
            case -101:
                builder.setMessage(activity.getString(R.string.action_error_spp_connection_fail_title, new Object[]{activity.getString(R.string.app_name)}));
                break;
            case ErrorType.ERROR_UNKNOWN /*-100*/:
                builder.setMessage(R.string.action_error_oobe_not_completed);
                break;
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                activity.finish();
            }
        });
        builder.show();
    }

    public static void save(Activity activity, String str, String str2) {
        String str3 = TAG;
        Log.d(str3, "save lebel : " + str + ", param : " + str2);
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_CONFIG_LABEL_PARAMS, str);
        intent.putExtra(Constants.EXTRA_CONFIG_PARAMS, str2);
        activity.setResult(-1, intent);
        activity.finish();
    }

    public static void setRTLConfigurationWithChildren(View view, int i) {
        view.setLayoutDirection(i);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
                setRTLConfigurationWithChildren(viewGroup.getChildAt(i2), i);
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    public static int convertOptionTagToOptionNumber(String str) {
        char c;
        switch (str.hashCode()) {
            case -1998723398:
                if (str.equals("spotify")) {
                    c = 3;
                    break;
                }
            case -1699885912:
                if (str.equals("ambient_sound")) {
                    c = 1;
                    break;
                }
            case -810883302:
                if (str.equals("volume")) {
                    c = 2;
                    break;
                }
            case 93751592:
                if (str.equals("bixby")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            return 1;
        }
        if (c == 1) {
            return 2;
        }
        if (c != 2) {
            return c != 3 ? 1 : 4;
        }
        return 3;
    }

    public static void sendRecommendBroadcast(Context context, String str) {
        if (isSupportedErrorCard(context)) {
            if (!Preferences.getBoolean("preference_routine.routine_recommend_broadcast_" + str, false, Preferences.MODE_MANAGER)) {
                String str2 = TAG;
                Log.d(str2, "sendRecommendBroadcast() : " + str);
                Preferences.putBoolean("preference_routine.routine_recommend_broadcast_" + str, true, Preferences.MODE_MANAGER);
                Bundle bundle = new Bundle();
                bundle.putString("recommend_tag", str);
                ComponentName componentName = new ComponentName("com.samsung.android.app.routines", "com.samsung.android.app.routines.core.service.RoutineBroadcastReceiver");
                Intent intent = new Intent();
                intent.setComponent(componentName);
                intent.setAction("com.samsung.android.app.routines.intent.ACTION_REQUEST_RECOMMEND_FORCED_REGISTRATION");
                intent.putExtras(bundle);
                Application.getContext().sendBroadcast(intent);
            }
        }
    }

    public static void initialize(Context context) {
        Log.d(TAG, "initialize()");
        changeComponentEnableState(new ComponentName(context, RoutineActionProvider.class), isSupportedErrorCard(context));
    }

    public static boolean isSupportedErrorCard(Context context) {
        try {
            long longVersionCode = PackageInfoCompat.getLongVersionCode(context.getPackageManager().getPackageInfo("com.samsung.android.app.routines", 128));
            boolean z = longVersionCode >= 250800000;
            String str = TAG;
            Log.d(str, "isSupportedErrorCard() versionCode : " + longVersionCode + " isSupported : " + z);
            return z;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d(TAG, "routine package is null");
            return false;
        }
    }

    private static void changeComponentEnableState(ComponentName componentName, boolean z) {
        String str = TAG;
        Log.d(str, "changeComponentEnableState :: " + z);
        Application.getContext().getPackageManager().setComponentEnabledSetting(componentName, z ? 1 : 2, 1);
    }
}
