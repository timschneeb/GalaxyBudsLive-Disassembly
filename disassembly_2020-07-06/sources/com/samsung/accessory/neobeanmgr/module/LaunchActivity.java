package com.samsung.accessory.neobeanmgr.module;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.common.util.BluetoothUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.module.home.HomeActivity;
import com.samsung.accessory.neobeanmgr.module.setupwizard.TermsAndConditionsActivity;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "NeoBean_LaunchActivity";
    private static String sDeviceId;
    private static Integer sLaunchMode;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        int intValue;
        Log.d(TAG, "onCreate() : versionCode=2020070651");
        super.onCreate(bundle);
        printIntentExtras();
        int intExtra = getIntent().getIntExtra(UhmFwUtil.EXTRA_LAUNCH_DATA_LAUNCH_MODE, -1);
        sLaunchMode = intExtra != -1 ? Integer.valueOf(intExtra) : null;
        sDeviceId = getIntent().getStringExtra("deviceid");
        if (Util.isEmulator()) {
            sDeviceId = "00:00:00:00:00:00";
        }
        UhmFwUtil.setLastLaunchDeviceId(sDeviceId);
        Log.i(TAG, "last_launch_device_id=" + BluetoothUtil.privateAddress(getLaunchDeviceId()));
        Application.getCoreService().disconnectOtherDevice(sDeviceId);
        if (UhmFwUtil.getLastLaunchDeviceId() == null) {
            UhmFwUtil.startNewDeviceActivity(this, false);
        } else if (!Preferences.getBoolean(PreferenceKey.SETUP_WIZARD_DONE, false)) {
            startActivity(new Intent(this, TermsAndConditionsActivity.class));
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            if (getLaunchMode() != null && ((intValue = getLaunchMode().intValue()) == 1002 || intValue == 1003 || intValue == 1006 || intValue == 1009)) {
                intent.putExtra(HomeActivity.EXTRA_AUTO_CONNECT, true);
            }
            startActivity(intent);
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    private static String getLaunchDeviceId() {
        return sDeviceId;
    }

    private static Integer getLaunchMode() {
        return sLaunchMode;
    }

    private void printIntentExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.i(TAG, "printIntentExtras() : bundle == null");
            return;
        }
        Log.i(TAG, "printIntentExtras() :");
        for (String str : extras.keySet()) {
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 192752574) {
                if (hashCode != 831630091) {
                    if (hashCode == 1109192177 && str.equals("deviceid")) {
                        c = 0;
                    }
                } else if (str.equals("device_address")) {
                    c = 1;
                }
            } else if (str.equals(UhmFwUtil.EXTRA_LAUNCH_DATA_BT_ADDRESS)) {
                c = 2;
            }
            if (c == 0 || c == 1 || c == 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("    ");
                sb.append(str);
                sb.append(" = ");
                sb.append(BluetoothUtil.privateAddress(extras.get(str) != null ? extras.get(str).toString() : null));
                Log.i(TAG, sb.toString());
            } else {
                Log.i(TAG, "    " + str + " = " + extras.get(str));
            }
        }
    }
}
