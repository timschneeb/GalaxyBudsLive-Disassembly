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
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;

public class GamingModeActivity extends ConnectionActivity {
    private static final String TAG = (Application.TAG_ + GamingModeActivity.class.getSimpleName());
    /* access modifiers changed from: private */
    public SwitchCompat gamingModeSwitch;
    /* access modifiers changed from: private */
    public EarBudsInfo mEarBudsInfo;
    /* access modifiers changed from: private */
    public TextView textOnOff;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.i(TAG, "onCreate()");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_gaming_mode);
        this.mEarBudsInfo = Application.getCoreService().getEarBudsInfo();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initGamingMode();
    }

    private void initGamingMode() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_gaming_mode_switch);
        this.textOnOff = (TextView) findViewById(R.id.textview_on_off);
        this.gamingModeSwitch = (SwitchCompat) findViewById(R.id.gaming_mode_switch);
        this.gamingModeSwitch.setChecked(this.mEarBudsInfo.adjustSoundSync);
        this.textOnOff.setText(getString(this.mEarBudsInfo.adjustSoundSync ? R.string.double_tap_on : R.string.double_tap_off));
        setColorBackground(this.mEarBudsInfo.adjustSoundSync);
        this.gamingModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GamingModeActivity gamingModeActivity;
                int i;
                GamingModeActivity.this.mEarBudsInfo.adjustSoundSync = z;
                SamsungAnalyticsUtil.setStatusString(SA.Status.GAME_MODE, GamingModeActivity.this.mEarBudsInfo.adjustSoundSync ? "1" : "0");
                Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.ADJUST_SOUND_SYNC, GamingModeActivity.this.mEarBudsInfo.adjustSoundSync ? (byte) 1 : 0));
                TextView access$100 = GamingModeActivity.this.textOnOff;
                if (GamingModeActivity.this.mEarBudsInfo.adjustSoundSync) {
                    gamingModeActivity = GamingModeActivity.this;
                    i = R.string.double_tap_on;
                } else {
                    gamingModeActivity = GamingModeActivity.this;
                    i = R.string.double_tap_off;
                }
                access$100.setText(gamingModeActivity.getString(i));
                GamingModeActivity gamingModeActivity2 = GamingModeActivity.this;
                gamingModeActivity2.setColorBackground(gamingModeActivity2.mEarBudsInfo.adjustSoundSync);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GamingModeActivity.this.gamingModeSwitch.setChecked(!GamingModeActivity.this.gamingModeSwitch.isChecked());
            }
        });
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
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onSupportNavigateUp() {
        SamsungAnalyticsUtil.sendEvent(SA.Event.UP_BUTTON, "261");
        finish();
        return super.onSupportNavigateUp();
    }

    private void updateVoiceAssistant() {
        if (Util.isTalkBackEnabled()) {
            this.gamingModeSwitch.setFocusable(false);
            this.gamingModeSwitch.setClickable(false);
            return;
        }
        this.gamingModeSwitch.setFocusable(true);
        this.gamingModeSwitch.setClickable(true);
    }
}
