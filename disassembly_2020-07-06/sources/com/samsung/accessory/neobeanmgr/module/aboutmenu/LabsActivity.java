package com.samsung.accessory.neobeanmgr.module.aboutmenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.OnSingleClickListener;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.gamemode.GameModeManager;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;
import com.samsung.accessory.neobeanmgr.module.mainmenu.GamingModeActivity;

public class LabsActivity extends ConnectionActivity {
    private static final String TAG = (Application.TAG_ + LabsActivity.class.getSimpleName());
    private SwitchCompat doubleTapSideSwitch;
    private View gamingModeLayout;
    /* access modifiers changed from: private */
    public SwitchCompat gamingModeSwitch;
    /* access modifiers changed from: private */
    public EarBudsInfo mEarBudsInfo;
    /* access modifiers changed from: private */
    public SwitchCompat relievePressureAmbientSwitch;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.i(TAG, "onCreate()");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_labs);
        this.mEarBudsInfo = Application.getCoreService().getEarBudsInfo();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initGameModeCard();
        initRelievePressureWithAmbientSound();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        SamsungAnalyticsUtil.sendPage(SA.Screen.LABS);
        updateUI();
        updateVoiceAssistant();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void initGameModeCard() {
        this.gamingModeLayout = findViewById(R.id.layout_gaming_mode);
        this.gamingModeLayout.setVisibility(GameModeManager.isSupportDevice() ? 0 : 8);
        this.gamingModeSwitch = (SwitchCompat) findViewById(R.id.switch_gaming_mode);
        this.gamingModeSwitch.setChecked(this.mEarBudsInfo.adjustSoundSync);
        findViewById(R.id.layout_game_mode_switch).setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.GAME_MODE_DETAIL, SA.Screen.LABS);
                LabsActivity labsActivity = LabsActivity.this;
                labsActivity.startActivity(new Intent(labsActivity, GamingModeActivity.class));
            }
        });
        this.gamingModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.ADJUST_SOUND_SYNC, z ? (byte) 1 : 0));
                LabsActivity.this.mEarBudsInfo.adjustSoundSync = z;
                SamsungAnalyticsUtil.sendEvent(SA.Event.GAME_MODE, SA.Screen.LABS, LabsActivity.this.mEarBudsInfo.adjustSoundSync ? "b" : "a");
                SamsungAnalyticsUtil.setStatusString(SA.Status.GAME_MODE, LabsActivity.this.mEarBudsInfo.adjustSoundSync ? "1" : "0");
            }
        });
        findViewById(R.id.switch_layout_gaming_mode).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LabsActivity.this.gamingModeSwitch.setChecked(!LabsActivity.this.gamingModeSwitch.isChecked());
            }
        });
    }

    private void initRelievePressureWithAmbientSound() {
        findViewById(R.id.layout_relieve_pressure_ambient_sound).setVisibility(this.mEarBudsInfo.extendedRevision < 5 ? 8 : 0);
        this.relievePressureAmbientSwitch = (SwitchCompat) findViewById(R.id.switch_relieve_pressure_ambient_sound);
        this.relievePressureAmbientSwitch.setChecked(this.mEarBudsInfo.passThrough);
        this.relievePressureAmbientSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                LabsActivity.this.mEarBudsInfo.passThrough = z;
                Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.PASS_THROUGH, LabsActivity.this.mEarBudsInfo.passThrough ? (byte) 1 : 0));
            }
        });
        findViewById(R.id.layout_relieve_pressure_ambient_sound_switch).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LabsActivity.this.relievePressureAmbientSwitch.setChecked(!LabsActivity.this.relievePressureAmbientSwitch.isChecked());
            }
        });
    }

    private void updateUI() {
        initGameModeCard();
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

    public boolean onSupportNavigateUp() {
        SamsungAnalyticsUtil.sendEvent(SA.Event.UP_BUTTON, SA.Screen.LABS);
        finish();
        return super.onSupportNavigateUp();
    }
}
