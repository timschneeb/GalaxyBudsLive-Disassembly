package com.samsung.accessory.neobeanmgr.module.mainmenu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;

public class SeamlessConnectionActivity extends ConnectionActivity {
    public static final byte SC_PARAM_OFF = 1;
    public static final byte SC_PARAM_ON = 0;
    private static final String TAG = (Application.TAG_ + SeamlessConnectionActivity.class.getSimpleName());
    /* access modifiers changed from: private */
    public EarBudsInfo mEarBudsInfo;
    /* access modifiers changed from: private */
    public SwitchCompat seamlessConnectionSwitch;
    /* access modifiers changed from: private */
    public TextView textOnOff;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.i(TAG, "onCreate()");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_seamless_connection);
        this.mEarBudsInfo = Application.getCoreService().getEarBudsInfo();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initSeamlessConnection();
    }

    private void initSeamlessConnection() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_seamless_earbud_connection_switch);
        this.textOnOff = (TextView) findViewById(R.id.textview_on_off_switch);
        this.seamlessConnectionSwitch = (SwitchCompat) findViewById(R.id.seamless_connection_switch);
        this.seamlessConnectionSwitch.setChecked(this.mEarBudsInfo.seamlessConnection);
        this.textOnOff.setText(getString(this.mEarBudsInfo.seamlessConnection ? R.string.vn_on : R.string.vn_off));
        setColorBackground(this.mEarBudsInfo.seamlessConnection);
        this.seamlessConnectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SeamlessConnectionActivity seamlessConnectionActivity;
                int i;
                SeamlessConnectionActivity.this.mEarBudsInfo.seamlessConnection = z;
                Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.SET_SEAMLESS_CONNECTION, SeamlessConnectionActivity.this.mEarBudsInfo.seamlessConnection ^ true ? (byte) 1 : 0));
                TextView access$100 = SeamlessConnectionActivity.this.textOnOff;
                if (SeamlessConnectionActivity.this.mEarBudsInfo.seamlessConnection) {
                    seamlessConnectionActivity = SeamlessConnectionActivity.this;
                    i = R.string.vn_on;
                } else {
                    seamlessConnectionActivity = SeamlessConnectionActivity.this;
                    i = R.string.vn_off;
                }
                access$100.setText(seamlessConnectionActivity.getString(i));
                SeamlessConnectionActivity seamlessConnectionActivity2 = SeamlessConnectionActivity.this;
                seamlessConnectionActivity2.setColorBackground(seamlessConnectionActivity2.mEarBudsInfo.seamlessConnection);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SeamlessConnectionActivity.this.seamlessConnectionSwitch.setChecked(!SeamlessConnectionActivity.this.seamlessConnectionSwitch.isChecked());
            }
        });
        if (Util.isJapanModel()) {
            ((TextView) findViewById(R.id.text_seamless_connection_desc)).setText(R.string.settings_seamless_connection_desc_jpn);
        }
    }

    /* access modifiers changed from: private */
    public void setColorBackground(boolean z) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.layout_switch);
        if (z) {
            frameLayout.setBackgroundColor(getResources().getColor(R.color.master_switch_background));
        } else {
            frameLayout.setBackgroundColor(getResources().getColor(R.color.contents_background));
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        updateVoiceAssistant();
        Preferences.putBoolean(PreferenceKey.SEAMLESS_CONNECTION_CARD_SHOW_AGAIN, false, Preferences.MODE_MANAGER);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void updateVoiceAssistant() {
        if (Util.isTalkBackEnabled()) {
            this.seamlessConnectionSwitch.setFocusable(false);
            this.seamlessConnectionSwitch.setClickable(false);
            return;
        }
        this.seamlessConnectionSwitch.setFocusable(true);
        this.seamlessConnectionSwitch.setClickable(true);
    }
}
