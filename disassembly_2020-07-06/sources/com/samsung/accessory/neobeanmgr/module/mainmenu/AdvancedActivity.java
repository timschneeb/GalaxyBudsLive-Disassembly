package com.samsung.accessory.neobeanmgr.module.mainmenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.aom.AomManager;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;

public class AdvancedActivity extends ConnectionActivity {
    private static final String TAG = (Application.TAG_ + AdvancedActivity.class.getSimpleName());
    /* access modifiers changed from: private */
    public boolean flagSetBixby = false;
    private EarBudsInfo mEarBudsInfo;
    private ConstraintLayout mVoiceWakeUpLayout;
    /* access modifiers changed from: private */
    public SwitchCompat mVoiceWakeUpSwitch;
    private TextView onOffText;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.i(TAG, "onCreate()");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_advanced);
        this.mEarBudsInfo = Application.getCoreService().getEarBudsInfo();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initVoiceWakeUp();
        initSeamlessConnection();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        updateVoiceAssistant();
        updateVoiceWakeUp();
        this.onOffText.setText(getString(this.mEarBudsInfo.seamlessConnection ? R.string.vn_on : R.string.vn_off));
        SamsungAnalyticsUtil.sendPage("261");
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        String str = TAG;
        Log.d(str, "onActivityResult() requestCode: " + i + ", resultCode :" + i2);
        if (i == 1000 && Application.getAomManager().isSupportAOM()) {
            EarBudsInfo earBudsInfo = this.mEarBudsInfo;
            earBudsInfo.voiceWakeUp = true;
            SamsungAnalyticsUtil.setStatusString(SA.Status.VOICE_WAKE_UP, earBudsInfo.voiceWakeUp ? "1" : "0");
        }
    }

    private void initVoiceWakeUp() {
        int i = 0;
        this.flagSetBixby = false;
        this.mVoiceWakeUpLayout = (ConstraintLayout) findViewById(R.id.layout_voice_wakeup);
        ConstraintLayout constraintLayout = this.mVoiceWakeUpLayout;
        if (!Application.getAomManager().checkEnabledBixby()) {
            i = 8;
        }
        constraintLayout.setVisibility(i);
        this.mVoiceWakeUpSwitch = (SwitchCompat) findViewById(R.id.switch_voice_wakeup);
        this.mVoiceWakeUpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.VOICE_WAKE_UP_SWITCH, "261", !z ? "a" : "b");
                if (Application.getAomManager().isSupportAOM()) {
                    AdvancedActivity.this.setbixbyVoiceWakeUp(z);
                } else if (z) {
                    boolean unused = AdvancedActivity.this.flagSetBixby = true;
                    AdvancedActivity.this.executeBixby();
                    AdvancedActivity.this.mVoiceWakeUpSwitch.setChecked(false);
                } else {
                    AdvancedActivity.this.setbixbyVoiceWakeUp(false);
                }
            }
        });
        this.mVoiceWakeUpLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdvancedActivity.this.mVoiceWakeUpSwitch.setChecked(!AdvancedActivity.this.mVoiceWakeUpSwitch.isChecked());
            }
        });
    }

    private void initSeamlessConnection() {
        findViewById(R.id.layout_seamless_connection).setVisibility(this.mEarBudsInfo.extendedRevision >= 3 ? 0 : 8);
        this.onOffText = (TextView) findViewById(R.id.textview_seamless_connection_desc);
        this.onOffText.setText(getString(this.mEarBudsInfo.seamlessConnection ? R.string.vn_on : R.string.vn_off));
        findViewById(R.id.seamless_connection_content_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdvancedActivity advancedActivity = AdvancedActivity.this;
                advancedActivity.startActivity(new Intent(advancedActivity, SeamlessConnectionActivity.class));
            }
        });
    }

    private void updateVoiceAssistant() {
        if (Util.isTalkBackEnabled()) {
            this.mVoiceWakeUpSwitch.setFocusable(false);
            this.mVoiceWakeUpSwitch.setClickable(false);
            return;
        }
        this.mVoiceWakeUpSwitch.setFocusable(true);
        this.mVoiceWakeUpSwitch.setClickable(true);
    }

    private void updateVoiceWakeUp() {
        if (!Application.getAomManager().isSupportAOM()) {
            this.mVoiceWakeUpSwitch.setChecked(false);
            Application.getAomManager();
            if (AomManager.isCompleteBixbyOOB() || !Application.getAomManager().isCompleteUpdate()) {
                ((TextView) findViewById(R.id.textview_voice_wakeup_desc)).setText(R.string.settings_voice_wakeup_update_incomplete_desc);
            } else {
                ((TextView) findViewById(R.id.textview_voice_wakeup_desc)).setText(R.string.settings_voice_wakeup_oobe_incomplete_desc);
            }
        } else if (this.flagSetBixby) {
            setbixbyVoiceWakeUp(true);
            this.flagSetBixby = false;
        } else {
            this.mVoiceWakeUpSwitch.setChecked(this.mEarBudsInfo.voiceWakeUp);
            ((TextView) findViewById(R.id.textview_voice_wakeup_desc)).setText(getString(R.string.settings_voice_wakeup_desc, new Object[]{getString(R.string.hi_bixby)}));
        }
    }

    /* access modifiers changed from: private */
    public void executeBixby() {
        startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("bixbyvoice://com.samsung.android.bixby.agent/GoToFeature?featureName=AssistantHome")), 1000);
    }

    /* access modifiers changed from: private */
    public void setbixbyVoiceWakeUp(boolean z) {
        EarBudsInfo earBudsInfo = this.mEarBudsInfo;
        earBudsInfo.voiceWakeUp = z;
        SamsungAnalyticsUtil.setStatusString(SA.Status.VOICE_WAKE_UP, earBudsInfo.voiceWakeUp ? "1" : "0");
        Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.SET_VOICE_WAKE_UP, this.mEarBudsInfo.voiceWakeUp ? (byte) 1 : 0));
    }

    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
