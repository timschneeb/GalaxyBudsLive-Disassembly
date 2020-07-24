package com.samsung.accessory.neobeanmgr.core.bixbyroutine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.android.SDK.routine.Constants;
import com.samsung.android.sdk.mobileservice.social.buddy.provider.BuddyContract;

public class RoutineEqualizerConfigActivity extends Activity {
    private static final String TAG = (Application.TAG_ + RoutineEqualizerConfigActivity.class.getSimpleName());
    private RadioButton preset0;
    private RadioButton preset1;
    private RadioButton preset2;
    private RadioButton preset3;
    private RadioButton preset4;
    private RadioButton preset5;
    /* access modifiers changed from: private */
    public RadioGroup radioGroup;

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
        setContentView(R.layout.activity_routine_config_equalizer);
        getWindow().setGravity(80);
        setTitle(R.string.equalizer);
        this.preset0 = (RadioButton) findViewById(R.id.btn_preset_0);
        this.preset1 = (RadioButton) findViewById(R.id.btn_preset_1);
        this.preset2 = (RadioButton) findViewById(R.id.btn_preset_2);
        this.preset3 = (RadioButton) findViewById(R.id.btn_preset_3);
        this.preset4 = (RadioButton) findViewById(R.id.btn_preset_4);
        this.preset5 = (RadioButton) findViewById(R.id.btn_preset_5);
        this.radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        setPrevParam();
        ((TextView) findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RadioButton radioButton = (RadioButton) RoutineEqualizerConfigActivity.this.radioGroup.findViewById(RoutineEqualizerConfigActivity.this.radioGroup.getCheckedRadioButtonId());
                RoutineUtils.save(RoutineEqualizerConfigActivity.this, radioButton.getText().toString(), String.valueOf(RoutineEqualizerConfigActivity.this.radioGroup.indexOfChild(radioButton)));
            }
        });
        ((TextView) findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RoutineEqualizerConfigActivity.this.finish();
            }
        });
    }

    private void setPrevParam() {
        String stringExtra = getIntent().getStringExtra(Constants.EXTRA_CONFIG_PARAMS);
        if (stringExtra != null) {
            char c = 65535;
            switch (stringExtra.hashCode()) {
                case 48:
                    if (stringExtra.equals("0")) {
                        c = 0;
                        break;
                    }
                    break;
                case 49:
                    if (stringExtra.equals("1")) {
                        c = 1;
                        break;
                    }
                    break;
                case 50:
                    if (stringExtra.equals("2")) {
                        c = 2;
                        break;
                    }
                    break;
                case 51:
                    if (stringExtra.equals("3")) {
                        c = 3;
                        break;
                    }
                    break;
                case 52:
                    if (stringExtra.equals(BuddyContract.Email.Type.MOBILE)) {
                        c = 4;
                        break;
                    }
                    break;
                case 53:
                    if (stringExtra.equals("5")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                this.preset0.setChecked(true);
            } else if (c == 1) {
                this.preset1.setChecked(true);
            } else if (c == 2) {
                this.preset2.setChecked(true);
            } else if (c == 3) {
                this.preset3.setChecked(true);
            } else if (c == 4) {
                this.preset4.setChecked(true);
            } else if (c == 5) {
                this.preset5.setChecked(true);
            }
        }
    }
}
