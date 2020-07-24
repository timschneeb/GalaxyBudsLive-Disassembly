package com.samsung.accessory.neobeanmgr.common.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import java.util.ArrayList;
import java.util.HashSet;

public class PermissionManager {
    public static final int ALL_DENIED = -2;
    public static final int ALL_GRANTED = 1;
    public static final String[] ALL_PERMISSION_LIST = {"android.permission.READ_PHONE_STATE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.READ_CONTACTS", "android.permission.READ_CALENDAR", "android.permission.READ_SMS", "android.permission.READ_CALL_LOG"};
    public static final int DENIED = -1;
    public static final int GRANTED = 0;
    public static final int PEMISSION_REQUEST_CODE = 100;
    public static final int PERMISSION_READ_CALENDAR = 1;
    public static final int PERMISSION_READ_CONTACTS = 0;
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 4;
    public static final int PERMISSION_READ_PHONE_STATE = 2;
    public static final int PERMISSION_READ_SMS = 3;
    private static final String TAG = "NeoBean_PermissionManager";
    public static PermissionManager instance = null;

    public interface DialogListener {
        void onRequestDismissAction();

        void onRequestNegativeAction();

        void onRequestPositiveAction();
    }

    private PermissionManager() {
    }

    public static boolean isPermissionGranted(Context context, String str) {
        return ContextCompat.checkSelfPermission(context, str) == 0;
    }

    public static boolean isBasicPermissionGranted(Context context, String[] strArr) {
        for (String checkSelfPermission : strArr) {
            if (ContextCompat.checkSelfPermission(context, checkSelfPermission) == -1) {
                Log.d(TAG, "No permission!");
                return false;
            }
        }
        for (String permissionNeverAskAgain : strArr) {
            setPermissionNeverAskAgain(permissionNeverAskAgain, false);
        }
        return true;
    }

    public static PermissionManager getInstance() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    public void onDestroy() {
        instance = null;
    }

    public static boolean isSystemDialogEnable(Context context, String[] strArr) {
        Log.v(TAG, "isSystemDialogEnable()");
        for (int i = 0; i < strArr.length; i++) {
            if (!isPermissionGranted(context, strArr[i]) && !getPermissionNeverAskAgain(strArr[i])) {
                return true;
            }
        }
        return false;
    }

    public static void setPermissionNeverAskAgain(String str, boolean z) {
        Log.d(TAG, "setPermissionNaverAskAgain val = " + z);
        Preferences.putBoolean(str, Boolean.valueOf(z));
    }

    public static boolean getPermissionNeverAskAgain(String str) {
        return Preferences.getBoolean(str, false);
    }

    public static AlertDialog showPermissionSettingsDialog(Activity activity, String str, String[] strArr, final DialogListener dialogListener) {
        HashSet hashSet = new HashSet(strArr.length);
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager != null) {
            for (String str2 : strArr) {
                if (!isPermissionGranted(activity.getApplicationContext(), str2)) {
                    try {
                        PermissionGroupInfo permissionGroupInfo = packageManager.getPermissionGroupInfo(packageManager.getPermissionInfo(str2, 128).group, 128);
                        hashSet.add(new PermissionItem(permissionGroupInfo.loadIcon(packageManager), permissionGroupInfo.loadLabel(packageManager).toString()));
                    } catch (Exception e) {
                        Log.w(TAG, "Permission label fetch", e);
                    }
                }
            }
            if (hashSet.size() > 0) {
                PermissionListAdapter permissionListAdapter = new PermissionListAdapter(activity, new ArrayList(hashSet));
                View inflate = ((LayoutInflater) activity.getSystemService("layout_inflater")).inflate(R.layout.dialog_permission_list, (ViewGroup) null);
                ((ListView) inflate.findViewById(R.id.permission_list)).setAdapter(permissionListAdapter);
                ((TextView) inflate.findViewById(R.id.popup_message_textview)).setText(getSpannableMessage(activity.getString(R.string.warning_message_for_Runtime_permission, new Object[]{str}), str));
                final AlertDialog create = new AlertDialog.Builder(activity).setView(inflate).setCancelable(false).create();
                ((TextView) inflate.findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Log.d(PermissionManager.TAG, "Clicked cancel on permission dialog");
                        dialogListener.onRequestNegativeAction();
                    }
                });
                ((TextView) inflate.findViewById(R.id.settings)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        create.dismiss();
                        dialogListener.onRequestPositiveAction();
                    }
                });
                if (create != null) {
                    create.show();
                    return create;
                }
            }
        }
        return null;
    }

    private static SpannableString getSpannableMessage(String str, String str2) {
        SpannableString spannableString = new SpannableString(str);
        int indexOf = spannableString.toString().indexOf(str2.toString(), 0);
        spannableString.setSpan(new StyleSpan(1), indexOf, str2.length() + indexOf, 0);
        return spannableString;
    }
}
