package com.samsung.accessory.neobeanmgr.module.mainmenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.RingManager;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;
import com.samsung.accessory.neobeanmgr.module.home.card.CardEarbuds;

public class FindMyEarbudsActivity extends PermissionCheckActivity {
    private static final int LEFT_SIDE = 0;
    private static final int RIGHT_SIDE = 1;
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + FindMyEarbudsActivity.class.getSimpleName());
    private View mButtonMuteLeft;
    private View mButtonMuteRight;
    /* access modifiers changed from: private */
    public Context mContext;
    private ConstraintLayout mDeviceInfo;
    private ImageView mImageButton;
    private ImageView mImageMuteLeft;
    private ImageView mImageMuteRight;
    private ImageView mImageRotate;
    private ImageView mLeftEarbud;
    private TextView mNoBeepInCaseDesc;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Can't fix incorrect switch cases order */
        public void onReceive(Context context, Intent intent) {
            char c;
            String access$000 = FindMyEarbudsActivity.TAG;
            Log.d(access$000, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            switch (action.hashCode()) {
                case -2043421558:
                    if (action.equals(CoreService.ACTION_MSG_ID_STATUS_UPDATED)) {
                        c = 2;
                        break;
                    }
                case -1915507242:
                    if (action.equals(CoreService.ACTION_MUTE_EARBUD_STATUS_UPDATED)) {
                        c = 1;
                        break;
                    }
                case -1354974214:
                    if (action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) {
                        c = 3;
                        break;
                    }
                case -348576706:
                    if (action.equals(CoreService.ACTION_MSG_ID_FIND_MY_EARBUDS_STATUS_UPDATED)) {
                        c = 0;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            if (c == 0) {
                FindMyEarbudsActivity.this.initView();
            } else if (c == 1) {
                FindMyEarbudsActivity.this.initMuteView();
            } else if (c == 2) {
                FindMyEarbudsActivity.this.initEarbuds();
                FindMyEarbudsActivity.this.initMuteView();
            } else if (c == 3) {
                if (RingManager.isFinding()) {
                    Toast.makeText(Application.getContext(), FindMyEarbudsActivity.this.getString(R.string.settings_find_my_gear_disconnected_toast), 0).show();
                }
                Log.w(FindMyEarbudsActivity.TAG, "CoreService.ACTION_DEVICE_DISCONNECTED -> finish()");
                FindMyEarbudsActivity.this.finish();
            }
        }
    };
    private ImageView mRightEarbud;
    private ConstraintLayout mStartButton;
    private TextView mTextButton;
    private TextView mTextDesc;
    private TextView mTextMuteLeft;
    private TextView mTextMuteRight;
    private TextView mWarningDesc;
    private View.OnClickListener setButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (RingManager.isFinding()) {
                Log.d(FindMyEarbudsActivity.TAG, "mStartButton onClick:: RingManager.ready()");
                RingManager.ready();
                SamsungAnalyticsUtil.sendEvent(SA.Event.FIND_MY_EARBUDS_STOP, SA.Screen.FIND_MY_EARBUDS_FINDING);
                return;
            }
            Log.d(FindMyEarbudsActivity.TAG, "mStartButton onClick:: RingManager.find()");
            int check = RingManager.check();
            if (check == 0) {
                Log.d(FindMyEarbudsActivity.TAG, "onClick:: RingManager.SUCCESS");
                RingManager.find();
                SamsungAnalyticsUtil.sendEvent(SA.Event.FIND_MY_EARBUDS_START, SA.Screen.FIND_MY_EARBUDS_READY);
            } else if (check == 2) {
                Log.d(FindMyEarbudsActivity.TAG, "onClick:: RingManager.DEVICE_BOTH_WEARING");
                Toast.makeText(FindMyEarbudsActivity.this.mContext, FindMyEarbudsActivity.this.getString(R.string.settings_find_my_gear_both_wearing_toast), 0).show();
                SamsungAnalyticsUtil.sendEvent(SA.Event.FIND_MY_EARBUDS_START, SA.Screen.FIND_MY_EARBUDS_READY);
            } else if (check == 3) {
                Log.d(FindMyEarbudsActivity.TAG, "onClick:: RingManager.DEVICE_CALLING");
                Toast.makeText(FindMyEarbudsActivity.this.mContext, FindMyEarbudsActivity.this.getString(R.string.settings_find_my_gear_call_toast), 0).show();
                SamsungAnalyticsUtil.sendEvent(SA.Event.FIND_MY_EARBUDS_START, SA.Screen.FIND_MY_EARBUDS_READY);
            }
        }
    };
    private View.OnClickListener setMuteOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            String str = "a";
            switch (view.getId()) {
                case R.id.button_mute_left:
                case R.id.image_mute_left:
                    boolean z = !FindMyEarbudsActivity.this.isLeftMute();
                    if (!z) {
                        str = "b";
                    }
                    SamsungAnalyticsUtil.sendEvent(SA.Event.LEFT_MUTE, SA.Screen.FIND_MY_EARBUDS_FINDING, str);
                    if (!FindMyEarbudsActivity.this.isWearLeftDevice()) {
                        RingManager.setLeftMute(z);
                        return;
                    } else {
                        Log.d(FindMyEarbudsActivity.TAG, "onClick:: isWearLeftDevice false");
                        return;
                    }
                case R.id.button_mute_right:
                case R.id.image_mute_right:
                    boolean z2 = !FindMyEarbudsActivity.this.isRightMute();
                    if (!z2) {
                        str = "b";
                    }
                    SamsungAnalyticsUtil.sendEvent(SA.Event.RIGHT_MUTE, SA.Screen.FIND_MY_EARBUDS_FINDING, str);
                    if (!FindMyEarbudsActivity.this.isWearRightDevice()) {
                        RingManager.setRightMute(z2);
                        return;
                    } else {
                        Log.d(FindMyEarbudsActivity.TAG, "onClick:: isWearRightDevice false");
                        return;
                    }
                default:
                    return;
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        setContentView((int) R.layout.activity_find_my_earbud);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((CharSequence) getString(R.string.settings_find_my_gear));
        registerReceiver();
        init();
        initView();
        initEarbuds();
        setDeviceImage();
        registerListener();
    }

    private void init() {
        this.mImageRotate = (ImageView) findViewById(R.id.image_rotate);
        this.mImageButton = (ImageView) findViewById(R.id.image_button);
        this.mTextButton = (TextView) findViewById(R.id.text_button_name);
        this.mTextDesc = (TextView) findViewById(R.id.text_description);
        this.mImageMuteLeft = (ImageView) findViewById(R.id.image_mute_left);
        this.mImageMuteRight = (ImageView) findViewById(R.id.image_mute_right);
        this.mTextMuteLeft = (TextView) findViewById(R.id.text_mute_left);
        this.mTextMuteRight = (TextView) findViewById(R.id.text_mute_right);
        this.mWarningDesc = (TextView) findViewById(R.id.mute_description);
        this.mNoBeepInCaseDesc = (TextView) findViewById(R.id.text_no_beep_in_case);
        this.mDeviceInfo = (ConstraintLayout) findViewById(R.id.device_layout);
        this.mButtonMuteLeft = findViewById(R.id.button_mute_left);
        this.mButtonMuteRight = findViewById(R.id.button_mute_right);
        this.mStartButton = (ConstraintLayout) findViewById(R.id.start_button_layout);
        this.mLeftEarbud = (ImageView) findViewById(R.id.image_bud_left);
        this.mRightEarbud = (ImageView) findViewById(R.id.image_bud_right);
    }

    /* access modifiers changed from: private */
    public void initView() {
        if (RingManager.isFinding()) {
            setFindView();
        } else {
            setReadyView();
        }
    }

    private void setFindView() {
        this.mImageRotate.setVisibility(0);
        this.mImageButton.setBackgroundResource(R.drawable.shape_btn_find_my_earbuds_finding);
        this.mImageButton.setImageResource(R.drawable.buds_01_finding);
        this.mTextButton.setText(R.string.settings_find_my_gear_btn_stop);
        this.mTextDesc.setText(getString(R.string.settings_find_my_gear_finding_desc, new Object[]{getString(R.string.app_name)}));
        initMuteView();
        this.mImageMuteLeft.setVisibility(0);
        this.mTextMuteLeft.setVisibility(0);
        this.mImageMuteRight.setVisibility(0);
        this.mTextMuteRight.setVisibility(0);
        if (Util.isTalkBackEnabled()) {
            this.mButtonMuteLeft.setVisibility(0);
            this.mButtonMuteRight.setVisibility(0);
        } else {
            this.mButtonMuteLeft.setVisibility(8);
            this.mButtonMuteRight.setVisibility(8);
        }
        this.mWarningDesc.setVisibility(8);
        this.mNoBeepInCaseDesc.setVisibility(0);
        startAnimation();
        getWindow().addFlags(128);
    }

    private void setReadyView() {
        this.mImageRotate.setVisibility(4);
        this.mImageButton.setBackgroundResource(R.drawable.shape_btn_find_my_earbuds_ready);
        this.mImageButton.setImageResource(R.drawable.fd_settings_ic_search);
        this.mTextButton.setText(R.string.settings_find_my_gear_btn_start);
        this.mTextDesc.setText(R.string.settings_find_my_gear_ready_desc);
        this.mButtonMuteLeft.setVisibility(8);
        this.mButtonMuteRight.setVisibility(8);
        this.mImageMuteLeft.setVisibility(8);
        this.mTextMuteLeft.setVisibility(8);
        this.mImageMuteRight.setVisibility(8);
        this.mTextMuteRight.setVisibility(8);
        this.mWarningDesc.setVisibility(0);
        this.mNoBeepInCaseDesc.setVisibility(8);
        stopAnimation();
        getWindow().clearFlags(128);
    }

    /* access modifiers changed from: private */
    public void initEarbuds() {
        Log.d(TAG, "initEarbuds()");
        if (isConnectedLeftDevice()) {
            UiUtil.setAnimateAlpha(this.mLeftEarbud, 1.0f);
            ((TextView) findViewById(R.id.text_left_connection)).setText(R.string.settings_find_my_gear_connected);
        } else {
            UiUtil.setAnimateAlpha(this.mLeftEarbud, 0.4f);
            ((TextView) findViewById(R.id.text_left_connection)).setText(R.string.settings_find_my_gear_disconnected);
        }
        if (isConnectedRightDevice()) {
            UiUtil.setAnimateAlpha(this.mRightEarbud, 1.0f);
            ((TextView) findViewById(R.id.text_right_connection)).setText(R.string.settings_find_my_gear_connected);
        } else {
            UiUtil.setAnimateAlpha(this.mRightEarbud, 0.4f);
            ((TextView) findViewById(R.id.text_right_connection)).setText(R.string.settings_find_my_gear_disconnected);
        }
        String string = getString(R.string.earbud_info);
        if (isConnectedLeftDevice() && isConnectedRightDevice()) {
            string = String.format(getString(R.string.earbud_connected), new Object[]{getString(R.string.device_left)}) + ", " + String.format(getString(R.string.earbud_connected), new Object[]{getString(R.string.device_right)});
        } else if (isConnectedLeftDevice()) {
            string = String.format(getString(R.string.earbud_connected), new Object[]{getString(R.string.device_left)}) + ", " + String.format(getString(R.string.earbud_disconnected), new Object[]{getString(R.string.device_right)});
        } else if (isConnectedRightDevice()) {
            string = String.format(getString(R.string.earbud_disconnected), new Object[]{getString(R.string.device_left)}) + ", " + String.format(getString(R.string.earbud_connected), new Object[]{getString(R.string.device_right)});
        }
        this.mDeviceInfo.setContentDescription(string);
    }

    private void registerListener() {
        this.mStartButton.setOnClickListener(this.setButtonOnClickListener);
        this.mImageMuteLeft.setOnClickListener(this.setMuteOnClickListener);
        this.mImageMuteRight.setOnClickListener(this.setMuteOnClickListener);
        this.mButtonMuteLeft.setOnClickListener(this.setMuteOnClickListener);
        this.mButtonMuteRight.setOnClickListener(this.setMuteOnClickListener);
    }

    private void startAnimation() {
        RotateAnimation rotateAnimation;
        if (Util.isSystemLayoutDirectionRtl()) {
            this.mImageRotate.setScaleX(-1.0f);
            rotateAnimation = new RotateAnimation(360.0f, 0.0f, 1, 0.0f, 1, 1.0f);
        } else {
            rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 1.0f, 1, 1.0f);
        }
        rotateAnimation.setDuration(4000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        this.mImageRotate.setAnimation(rotateAnimation);
    }

    private void stopAnimation() {
        this.mImageRotate.clearAnimation();
    }

    private void setMute(int i) {
        if (i == 0) {
            this.mImageMuteLeft.setAlpha(1.0f);
            this.mImageMuteLeft.setClickable(true);
            this.mImageMuteLeft.setBackgroundResource(R.drawable.circle1_shape);
            this.mImageMuteLeft.setImageResource(R.drawable.fd_settings_ic_mute);
            this.mTextMuteLeft.setText(R.string.settings_find_my_gear_unmute);
            View view = this.mButtonMuteLeft;
            view.setContentDescription(getString(R.string.settings_find_my_gear_unmute) + " " + getString(R.string.device_left));
            return;
        }
        this.mImageMuteRight.setAlpha(1.0f);
        this.mImageMuteRight.setClickable(true);
        this.mImageMuteRight.setBackgroundResource(R.drawable.circle1_shape);
        this.mImageMuteRight.setImageResource(R.drawable.fd_settings_ic_mute);
        this.mTextMuteRight.setText(R.string.settings_find_my_gear_unmute);
        View view2 = this.mButtonMuteRight;
        view2.setContentDescription(getString(R.string.settings_find_my_gear_unmute) + " " + getString(R.string.device_right));
    }

    private void setUnMute(int i) {
        if (i == 0) {
            this.mImageMuteLeft.setAlpha(1.0f);
            this.mImageMuteLeft.setClickable(true);
            this.mImageMuteLeft.setBackgroundResource(R.drawable.circle3_shape);
            this.mImageMuteLeft.setImageResource(R.drawable.fd_settings_ic_sound);
            this.mTextMuteLeft.setText(R.string.settings_find_my_gear_mute);
            View view = this.mButtonMuteLeft;
            view.setContentDescription(getString(R.string.settings_find_my_gear_mute) + " " + getString(R.string.device_left));
            return;
        }
        this.mImageMuteRight.setAlpha(1.0f);
        this.mImageMuteRight.setClickable(true);
        this.mImageMuteRight.setBackgroundResource(R.drawable.circle3_shape);
        this.mImageMuteRight.setImageResource(R.drawable.fd_settings_ic_sound);
        this.mTextMuteRight.setText(R.string.settings_find_my_gear_mute);
        View view2 = this.mButtonMuteRight;
        view2.setContentDescription(getString(R.string.settings_find_my_gear_mute) + " " + getString(R.string.device_right));
    }

    private void setMuteDisconnect(int i) {
        if (i == 0) {
            this.mImageMuteLeft.setAlpha(0.2f);
            this.mImageMuteLeft.setClickable(false);
            this.mImageMuteLeft.setBackgroundResource(R.drawable.circle1_shape);
            this.mImageMuteLeft.setImageResource(R.drawable.fd_settings_ic_mute);
            this.mTextMuteLeft.setText(R.string.settings_find_my_gear_disconnected);
            View view = this.mButtonMuteLeft;
            view.setContentDescription(getString(R.string.settings_find_my_gear_disconnected) + " " + getString(R.string.device_left));
            return;
        }
        this.mImageMuteRight.setAlpha(0.2f);
        this.mImageMuteRight.setClickable(false);
        this.mImageMuteRight.setBackgroundResource(R.drawable.circle1_shape);
        this.mImageMuteRight.setImageResource(R.drawable.fd_settings_ic_mute);
        this.mTextMuteRight.setText(R.string.settings_find_my_gear_disconnected);
        View view2 = this.mButtonMuteRight;
        view2.setContentDescription(getString(R.string.settings_find_my_gear_disconnected) + " " + getString(R.string.device_right));
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_MSG_ID_FIND_MY_EARBUDS_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MUTE_EARBUD_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        registerReceiver(this.mReceiver, intentFilter);
    }

    /* access modifiers changed from: private */
    public void initMuteView() {
        String str = TAG;
        Log.d(str, "initMuteView() isLeftMute : " + isLeftMute() + ", isRightMute : " + isRightMute());
        setLeftMuteView(isLeftMute());
        setRightMuteView(isRightMute());
    }

    private void setLeftMuteView(boolean z) {
        if (!isConnectedLeftDevice()) {
            setMuteDisconnect(0);
        } else if (z) {
            setMute(0);
        } else {
            setUnMute(0);
        }
    }

    private void setRightMuteView(boolean z) {
        if (!isConnectedRightDevice()) {
            setMuteDisconnect(1);
        } else if (z) {
            setMute(1);
        } else {
            setUnMute(1);
        }
    }

    private boolean isConnectedLeftDevice() {
        return Application.getCoreService().getEarBudsInfo().batteryL > 0;
    }

    private boolean isConnectedRightDevice() {
        return Application.getCoreService().getEarBudsInfo().batteryR > 0;
    }

    /* access modifiers changed from: private */
    public boolean isLeftMute() {
        return Application.getCoreService().getEarBudsInfo().leftMuteStatus;
    }

    /* access modifiers changed from: private */
    public boolean isRightMute() {
        return Application.getCoreService().getEarBudsInfo().rightMuteStatus;
    }

    /* access modifiers changed from: private */
    public boolean isWearLeftDevice() {
        return Application.getCoreService().getEarBudsInfo().wearingL;
    }

    /* access modifiers changed from: private */
    public boolean isWearRightDevice() {
        return Application.getCoreService().getEarBudsInfo().wearingR;
    }

    private void setDeviceImage() {
        CardEarbuds.setDeviceImages((Integer) null, this.mLeftEarbud, this.mRightEarbud, (ImageView) null);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        SamsungAnalyticsUtil.sendPage(SA.Screen.FIND_MY_EARBUDS_READY);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        RingManager.ready();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        RingManager.ready();
        super.onDestroy();
    }

    public boolean onSupportNavigateUp() {
        SamsungAnalyticsUtil.sendEvent(SA.Event.UP_BUTTON, RingManager.isFinding() ? SA.Screen.FIND_MY_EARBUDS_FINDING : SA.Screen.FIND_MY_EARBUDS_READY);
        finish();
        return true;
    }
}
