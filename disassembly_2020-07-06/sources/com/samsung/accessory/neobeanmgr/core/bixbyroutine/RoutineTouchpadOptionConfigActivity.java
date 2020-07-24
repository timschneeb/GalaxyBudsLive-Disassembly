package com.samsung.accessory.neobeanmgr.core.bixbyroutine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.module.mainmenu.TouchpadActivity;
import com.samsung.android.SDK.routine.Constants;

public class RoutineTouchpadOptionConfigActivity extends Activity implements View.OnClickListener {
    private static final String TAG = (Application.TAG_ + RoutineTouchpadOptionConfigActivity.class.getSimpleName());
    private RadioButton leftOption1;
    private RadioButton leftOption2;
    private RadioButton leftOption3;
    /* access modifiers changed from: private */
    public RadioButton preset0;
    private RadioButton preset1;
    /* access modifiers changed from: private */
    public RadioGroup radioGroupLeft;
    /* access modifiers changed from: private */
    public RadioGroup radioGroupRight;
    private RadioButton rightOption1;
    private RadioButton rightOption2;
    private RadioButton rightOption3;
    private int validState;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.validState = getIntent().getIntExtra(Constants.EXTRA_VALID_STATE, 0);
        String str = TAG;
        Log.d(str, "validState  : " + this.validState);
        int i = this.validState;
        if (i < 0) {
            RoutineUtils.showErrorDialog(this, i);
            return;
        }
        setContentView(R.layout.activity_routine_config_touchpad_option);
        getWindow().setGravity(80);
        setTitle(R.string.settings_touchpad_option_menu);
        this.preset0 = (RadioButton) findViewById(R.id.btn_preset_0);
        this.preset1 = (RadioButton) findViewById(R.id.btn_preset_1);
        this.radioGroupLeft = (RadioGroup) findViewById(R.id.routine_touchpad_option_left_group);
        this.radioGroupRight = (RadioGroup) findViewById(R.id.routine_touchpad_option_right_group);
        this.leftOption1 = (RadioButton) findViewById(R.id.left_option1);
        this.leftOption2 = (RadioButton) findViewById(R.id.left_option2);
        this.leftOption3 = (RadioButton) findViewById(R.id.left_option3);
        this.rightOption1 = (RadioButton) findViewById(R.id.right_option1);
        this.rightOption2 = (RadioButton) findViewById(R.id.right_option2);
        this.rightOption3 = (RadioButton) findViewById(R.id.right_option3);
        TextView textView = (TextView) findViewById(R.id.btn_ok);
        TextView textView2 = (TextView) findViewById(R.id.btn_cancel);
        ((LinearLayout) findViewById(R.id.preset_0_text_layout)).setOnClickListener(this);
        this.preset1.setOnClickListener(this);
        if (getResources().getConfiguration().getLayoutDirection() == 1) {
            RoutineUtils.setRTLConfigurationWithChildren(this.radioGroupLeft, 0);
            RoutineUtils.setRTLConfigurationWithChildren(this.radioGroupRight, 0);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int i;
                int i2;
                String str;
                if (RoutineTouchpadOptionConfigActivity.this.preset0.isChecked()) {
                    i = 3;
                    str = RoutineTouchpadOptionConfigActivity.this.getString(R.string.settings_touchpad_popup_txt3_left) + ", " + RoutineTouchpadOptionConfigActivity.this.getString(R.string.settings_touchpad_popup_txt3_right);
                    i2 = 3;
                } else {
                    RadioButton radioButton = (RadioButton) RoutineTouchpadOptionConfigActivity.this.radioGroupLeft.findViewById(RoutineTouchpadOptionConfigActivity.this.radioGroupLeft.getCheckedRadioButtonId());
                    RadioButton radioButton2 = (RadioButton) RoutineTouchpadOptionConfigActivity.this.radioGroupRight.findViewById(RoutineTouchpadOptionConfigActivity.this.radioGroupRight.getCheckedRadioButtonId());
                    int convertOptionTagToOptionNumber = RoutineUtils.convertOptionTagToOptionNumber((String) radioButton.getTag());
                    i2 = RoutineUtils.convertOptionTagToOptionNumber((String) radioButton2.getTag());
                    str = radioButton.getText() + ", " + radioButton2.getText();
                    i = convertOptionTagToOptionNumber;
                }
                RoutineUtils.save(RoutineTouchpadOptionConfigActivity.this, str, "" + i + i2);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RoutineTouchpadOptionConfigActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.validState >= 0) {
            setPrevParam();
        }
    }

    private void setPrevParam() {
        if (!TouchpadActivity.isReadySpotify()) {
            Log.d(TAG, "spotify is not installed");
            this.leftOption3.setVisibility(8);
            this.rightOption3.setVisibility(8);
        }
        if (Util.isInstalledPackage("com.samsung.android.bixby.agent")) {
            this.leftOption1.setText(R.string.settings_touchpad_popup_txt1_bixby);
            this.rightOption1.setText(R.string.settings_touchpad_popup_txt1_bixby);
        } else {
            Log.d(TAG, "bixby is not installed");
            this.leftOption1.setText(R.string.settings_touchpad_popup_txt1_normal);
            this.rightOption1.setText(R.string.settings_touchpad_popup_txt1_normal);
        }
        String stringExtra = getIntent().getStringExtra(Constants.EXTRA_CONFIG_PARAMS);
        if (stringExtra == null) {
            setOptionValue(2, 2);
        } else {
            setOptionValue(Character.getNumericValue(stringExtra.charAt(0)), Character.getNumericValue(stringExtra.charAt(1)));
        }
    }

    private void setOptionValue(int i, int i2) {
        String str = TAG;
        Log.d(str, "setOptionValue() leftOption : " + i + ", rightOption : " + i2);
        if (i == 3) {
            setOptionVolume();
            return;
        }
        setOptionOthers();
        if (i == 1) {
            this.leftOption1.setChecked(true);
        } else if (i == 2) {
            this.leftOption2.setChecked(true);
        } else if (i == 4) {
            this.leftOption3.setChecked(true);
        }
        if (i2 == 1) {
            this.rightOption1.setChecked(true);
        } else if (i2 == 2) {
            this.rightOption2.setChecked(true);
        } else if (i2 == 4) {
            this.rightOption3.setChecked(true);
        }
    }

    private void setOptionVolume() {
        this.preset0.setChecked(true);
        this.preset1.setChecked(false);
        UiUtil.setEnabledWithChildren(this.radioGroupLeft, false);
        UiUtil.setEnabledWithChildren(this.radioGroupRight, false);
    }

    private void setOptionOthers() {
        this.preset0.setChecked(false);
        this.preset1.setChecked(true);
        UiUtil.setEnabledWithChildren(this.radioGroupLeft, true);
        UiUtil.setEnabledWithChildren(this.radioGroupRight, true);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_preset_1) {
            setOptionOthers();
        } else if (id == R.id.preset_0_text_layout) {
            setOptionVolume();
        }
    }
}
