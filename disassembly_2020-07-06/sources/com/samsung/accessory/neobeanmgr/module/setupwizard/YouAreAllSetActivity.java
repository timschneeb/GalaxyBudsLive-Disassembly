package com.samsung.accessory.neobeanmgr.module.setupwizard;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.penfeizhou.animation.loader.AssetStreamLoader;
import com.github.penfeizhou.animation.webp.WebPDrawable;
import com.samsung.accessory.fotaprovider.FotaProviderEventHandler;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.ui.Interpolators;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationUtil;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;
import com.samsung.android.fotaagent.update.UpdateInterface;

public class YouAreAllSetActivity extends PermissionCheckActivity {
    private static final String TAG = "NeoBean_YouAreAllSetActivity";
    private Handler mHandler = new Handler();
    private ImageView mImageSuccessAnimation;
    /* access modifiers changed from: private */
    public ImageView mImageSuccessDotEffect;
    /* access modifiers changed from: private */
    public TextView mTextTitle;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate()");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_you_are_all_set);
        this.mImageSuccessDotEffect = (ImageView) findViewById(R.id.image_success_vi_dot_effect);
        this.mImageSuccessAnimation = (ImageView) findViewById(R.id.image_success_vi_animation);
        this.mTextTitle = (TextView) findViewById(R.id.text_title);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                YouAreAllSetActivity.this.startVIAnimation();
            }
        }, 800);
        new Handler().post(new Runnable() {
            public void run() {
                NotificationUtil.initSettingDefaultApps();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        this.mHandler.removeCallbacksAndMessages((Object) null);
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void startVIAnimation() {
        this.mImageSuccessAnimation.setImageDrawable(new WebPDrawable(new AssetStreamLoader(this, "success_vi.webp")));
        AnimationSet animationSet = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, UiUtil.DP_TO_PX(20.0f), 0.0f);
        translateAnimation.setStartOffset(1500);
        translateAnimation.setDuration(300);
        translateAnimation.setInterpolator(Interpolators.SineOut60Interpolator());
        translateAnimation.setFillAfter(true);
        animationSet.addAnimation(translateAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setStartOffset(1500);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                YouAreAllSetActivity.this.mTextTitle.setVisibility(0);
            }
        });
        animationSet.addAnimation(alphaAnimation);
        this.mTextTitle.startAnimation(animationSet);
        AnimationSet animationSet2 = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setStartOffset(1800);
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(Interpolators.SineOut60Interpolator());
        scaleAnimation.setFillAfter(true);
        animationSet2.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation2.setStartOffset(1800);
        alphaAnimation2.setDuration(200);
        alphaAnimation2.setInterpolator(new LinearInterpolator());
        alphaAnimation2.setFillAfter(true);
        alphaAnimation2.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                YouAreAllSetActivity.this.mImageSuccessDotEffect.setVisibility(0);
            }
        });
        animationSet2.addAnimation(alphaAnimation2);
        AlphaAnimation alphaAnimation3 = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation3.setStartOffset(2300);
        alphaAnimation3.setDuration(200);
        alphaAnimation3.setInterpolator(new LinearInterpolator());
        alphaAnimation3.setFillAfter(true);
        alphaAnimation3.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                YouAreAllSetActivity.this.mImageSuccessDotEffect.setVisibility(4);
            }
        });
        animationSet2.addAnimation(alphaAnimation3);
        this.mImageSuccessDotEffect.startAnimation(animationSet2);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                YouAreAllSetActivity.this.finishSetupWizard();
            }
        }, UpdateInterface.HOLDING_AFTER_BT_CONNECTED);
    }

    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
    }

    /* access modifiers changed from: private */
    public void finishSetupWizard() {
        Log.d(TAG, "finishSetupWizard()");
        this.mHandler.removeCallbacksAndMessages((Object) null);
        Preferences.putBoolean(PreferenceKey.SETUP_WIZARD_DONE, true);
        Application.getAomManager().setAomEnable(true);
        Log.d(TAG, "send setupwizard complete broadcast");
        FotaProviderEventHandler.setupWizardCompleted(Application.getContext());
        setResult(-1);
        finish();
    }
}
