package com.samsung.accessory.neobeanmgr.module.base;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.permission.PermissionManager;
import com.samsung.accessory.neobeanmgr.common.util.Util;

public abstract class PermissionCheckActivity extends OrientationPolicyActivity {
    private static String TAG = (Application.TAG_ + PermissionCheckActivity.class.getSimpleName());
    /* access modifiers changed from: private */
    public AlertDialog dialog;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i(TAG, "onCreate");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        if (!PermissionManager.isBasicPermissionGranted(this, PermissionManager.ALL_PERMISSION_LIST)) {
            chkPermission();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        AlertDialog alertDialog = this.dialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void chkPermission() {
        if (Util.getSDKVer() > 22) {
            boolean isSystemDialogEnable = PermissionManager.isSystemDialogEnable(this, PermissionManager.ALL_PERMISSION_LIST);
            String str = TAG;
            Log.d(str, "isSystemDialogEnable : " + isSystemDialogEnable);
            if (isSystemDialogEnable) {
                Log.d(TAG, "show System Dialog");
                requestPermissions(PermissionManager.ALL_PERMISSION_LIST, 0);
                return;
            }
            Log.d(TAG, "show Custom Dialog");
            AlertDialog alertDialog = this.dialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                this.dialog = PermissionManager.showPermissionSettingsDialog(this, getString(R.string.app_name), PermissionManager.ALL_PERMISSION_LIST, new PermissionManager.DialogListener() {
                    public void onRequestDismissAction() {
                    }

                    public void onRequestPositiveAction() {
                        PermissionCheckActivity.this.startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + PermissionCheckActivity.this.getPackageName())));
                    }

                    public void onRequestNegativeAction() {
                        if (PermissionCheckActivity.this.dialog != null) {
                            PermissionCheckActivity.this.dialog.dismiss();
                        }
                        PermissionCheckActivity.this.finishAffinity();
                    }
                });
            }
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Log.d(TAG, "onRequestPermissionsResult()");
        if (Util.getSDKVer() <= 22) {
            return;
        }
        if (strArr == null || strArr.length <= 0) {
            Log.e(TAG, "wrong permission list");
            return;
        }
        char c = 1;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] == -1) {
                String str = TAG;
                Log.v(str, "permission = " + strArr[i2] + " , " + iArr[i2] + " , " + shouldShowRequestPermissionRationale(strArr[i2]));
                if (!shouldShowRequestPermissionRationale(strArr[i2])) {
                    PermissionManager.setPermissionNeverAskAgain(strArr[i2], true);
                }
                c = 65535;
            }
        }
        if (c == 65534 || c == 65535) {
            finishAffinity();
        } else if (c != 0 && c == 1) {
            if (Application.getNotificationCoreService() != null) {
                Application.getNotificationCoreService().registerMissedCallObserver();
                Log.d(TAG, "register MissedCallObserver for O");
                return;
            }
            Log.d(TAG, "no remote service 2");
        }
    }
}
