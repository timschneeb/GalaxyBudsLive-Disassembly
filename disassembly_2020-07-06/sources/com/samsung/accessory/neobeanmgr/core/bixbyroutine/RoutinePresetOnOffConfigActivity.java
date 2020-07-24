package com.samsung.accessory.neobeanmgr.core.bixbyroutine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import com.accessorydm.interfaces.XDMInterface;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.android.SDK.routine.Constants;

public class RoutinePresetOnOffConfigActivity extends Activity {
    private static final String TAG = (Application.TAG_ + RoutinePresetOnOffConfigActivity.class.getSimpleName());
    private RadioButton presetOff;
    /* access modifiers changed from: private */
    public RadioButton presetOn;
    private int title;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int intExtra = getIntent().getIntExtra(Constants.EXTRA_VALID_STATE, 0);
        String str = TAG;
        Log.d(str, "validState  : " + intExtra);
        if (intExtra < 0) {
            RoutineUtils.showErrorDialog(this, intExtra);
            return;
        }
        setContentView(R.layout.activity_routine_config_onoff);
        getWindow().setGravity(80);
        setTitle(this.title);
        this.presetOn = (RadioButton) findViewById(R.id.btn_on);
        this.presetOff = (RadioButton) findViewById(R.id.btn_off);
        setPrevParam();
        ((TextView) findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RoutinePresetOnOffConfigActivity routinePresetOnOffConfigActivity;
                int i;
                if (RoutinePresetOnOffConfigActivity.this.presetOn.isChecked()) {
                    routinePresetOnOffConfigActivity = RoutinePresetOnOffConfigActivity.this;
                    i = R.string.routine_on;
                } else {
                    routinePresetOnOffConfigActivity = RoutinePresetOnOffConfigActivity.this;
                    i = R.string.routine_off;
                }
                RoutineUtils.save(RoutinePresetOnOffConfigActivity.this, routinePresetOnOffConfigActivity.getString(i), RoutinePresetOnOffConfigActivity.this.presetOn.isChecked() ? "true" : XDMInterface.XDM_DEVDETAIL_DEFAULT_LRGOBJ_SUPPORT);
            }
        });
        ((TextView) findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RoutinePresetOnOffConfigActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setRoutineTitle(int i) {
        this.title = i;
    }

    private void setPrevParam() {
        String stringExtra = getIntent().getStringExtra(Constants.EXTRA_CONFIG_PARAMS);
        if (stringExtra != null) {
            char c = 65535;
            int hashCode = stringExtra.hashCode();
            if (hashCode != 3569038) {
                if (hashCode == 97196323 && stringExtra.equals(XDMInterface.XDM_DEVDETAIL_DEFAULT_LRGOBJ_SUPPORT)) {
                    c = 1;
                }
            } else if (stringExtra.equals("true")) {
                c = 0;
            }
            if (c == 0) {
                this.presetOn.setChecked(true);
            } else if (c == 1) {
                this.presetOff.setChecked(true);
            }
        }
    }
}
