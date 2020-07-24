package com.samsung.accessory.neobeanmgr.core.aom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.module.LaunchActivity;
import com.samsung.accessory.neobeanmgr.module.mainmenu.AdvancedActivity;

public class AomReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (Preferences.getBoolean(PreferenceKey.SETUP_WIZARD_DONE, false)) {
            try {
                if (!intent.getExtras().getString("packageName").equals(context.getPackageName())) {
                    return;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (Application.getCoreService().isConnected()) {
                Intent intent2 = new Intent(context, AdvancedActivity.class);
                intent2.addFlags(335577088);
                context.startActivity(intent2);
                return;
            }
            Intent intent3 = new Intent(context, LaunchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(UhmFwUtil.EXTRA_LAUNCH_DATA_LAUNCH_MODE, 1009);
            bundle.putString("deviceid", UhmFwUtil.getLastLaunchDeviceId());
            intent3.putExtras(bundle);
            intent3.addFlags(268435456);
            context.startActivity(intent3);
        }
    }
}
