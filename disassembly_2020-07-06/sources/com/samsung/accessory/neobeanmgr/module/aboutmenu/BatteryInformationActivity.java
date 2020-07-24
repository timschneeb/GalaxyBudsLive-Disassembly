package com.samsung.accessory.neobeanmgr.module.aboutmenu;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;

public class BatteryInformationActivity extends PermissionCheckActivity {
    private static final String TAG = "NeoBean_BatteryInformationActivity";
    private Context mContext;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        setContentView((int) R.layout.activity_battery_information);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((CharSequence) this.mContext.getString(R.string.about_earbuds_battery_information));
        ((TextView) findViewById(R.id.earbuds_battery_rated)).setText(getString(R.string.earbuds_battery_rated, new Object[]{5, Float.valueOf(0.2f)}));
        ((TextView) findViewById(R.id.earbuds_battery_capacity)).setText(getString(R.string.earbuds_battery_capacity, new Object[]{60}));
        ((TextView) findViewById(R.id.charging_case_rated)).setText(getString(R.string.case_battery_rated, new Object[]{5, Float.valueOf(0.6f)}));
        ((TextView) findViewById(R.id.charging_case_capacity)).setText(getString(R.string.case_battery_capacity, new Object[]{472}));
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
