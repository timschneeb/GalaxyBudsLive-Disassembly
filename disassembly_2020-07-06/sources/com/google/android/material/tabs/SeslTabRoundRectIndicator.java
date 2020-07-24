package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import com.google.android.material.R;

public class SeslTabRoundRectIndicator extends SeslAbsIndicatorView {
    private static final int SESL_TAB_ROUND_RECT_PRESS_DURATION = 50;
    private static final int SESL_TAB_ROUND_RECT_RELEASE_DURATION = 350;
    private static final float SESL_TAB_ROUND_RECT_SCALE_MINOR = 0.95f;
    private Drawable mBackground;
    /* access modifiers changed from: private */
    public AnimationSet mPressAnimationSet;

    public SeslTabRoundRectIndicator(Context context) {
        this(context, (AttributeSet) null);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Drawable drawable;
        if (isLightTheme()) {
            drawable = getContext().getDrawable(R.drawable.sesl_tablayout_subtab_indicator_background);
        } else {
            drawable = getContext().getDrawable(R.drawable.sesl_tablayout_subtab_indicator_background_dark);
        }
        this.mBackground = drawable;
        setBackground(this.mBackground);
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i != 0 && !isSelected()) {
            onHide();
        }
    }

    /* access modifiers changed from: package-private */
    public void onHide() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        startAnimation(alphaAnimation);
        setAlpha(0.0f);
    }

    /* access modifiers changed from: package-private */
    public void onShow() {
        setAlpha(1.0f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 1.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        startAnimation(alphaAnimation);
    }

    /* access modifiers changed from: package-private */
    public void startPressEffect() {
        setAlpha(1.0f);
        this.mPressAnimationSet = new AnimationSet(false);
        this.mPressAnimationSet.setStartOffset(50);
        this.mPressAnimationSet.setFillAfter(true);
        this.mPressAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                AnimationSet unused = SeslTabRoundRectIndicator.this.mPressAnimationSet = null;
            }
        });
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, SESL_TAB_ROUND_RECT_SCALE_MINOR, 1.0f, SESL_TAB_ROUND_RECT_SCALE_MINOR, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(50);
        scaleAnimation.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
        scaleAnimation.setFillAfter(true);
        this.mPressAnimationSet.addAnimation(scaleAnimation);
        if (!isSelected()) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(50);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
            this.mPressAnimationSet.addAnimation(alphaAnimation);
        }
        startAnimation(this.mPressAnimationSet);
    }

    /* access modifiers changed from: package-private */
    public void startReleaseEffect() {
        setAlpha(1.0f);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(SESL_TAB_ROUND_RECT_SCALE_MINOR, 1.0f, SESL_TAB_ROUND_RECT_SCALE_MINOR, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(350);
        scaleAnimation.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
        scaleAnimation.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        startAnimation(animationSet);
    }

    /* access modifiers changed from: package-private */
    public void startPressAndReleaseEffect() {
        setAlpha(1.0f);
        this.mPressAnimationSet = new AnimationSet(false);
        this.mPressAnimationSet.setStartOffset(50);
        this.mPressAnimationSet.setFillAfter(true);
        this.mPressAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                AnimationSet unused = SeslTabRoundRectIndicator.this.mPressAnimationSet = null;
            }
        });
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, SESL_TAB_ROUND_RECT_SCALE_MINOR, 1.0f, SESL_TAB_ROUND_RECT_SCALE_MINOR, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(50);
        scaleAnimation.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
        scaleAnimation.setFillAfter(true);
        this.mPressAnimationSet.addAnimation(scaleAnimation);
        if (!isSelected()) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(50);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
            this.mPressAnimationSet.addAnimation(alphaAnimation);
        }
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(SESL_TAB_ROUND_RECT_SCALE_MINOR, 1.0f, SESL_TAB_ROUND_RECT_SCALE_MINOR, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation2.setStartOffset(50);
        scaleAnimation2.setDuration(350);
        scaleAnimation2.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
        scaleAnimation2.setFillAfter(true);
        this.mPressAnimationSet.addAnimation(scaleAnimation);
    }

    private boolean isLightTheme() {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.isLightTheme, typedValue, true);
        if (typedValue.data != 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void onSetSelectedIndicatorColor(int i) {
        if (!(getBackground() instanceof NinePatchDrawable)) {
            if (Build.VERSION.SDK_INT >= 22) {
                getBackground().setTint(i);
            } else {
                getBackground().setColorFilter(i, PorterDuff.Mode.SRC_IN);
            }
            if (!isSelected()) {
                setHide();
            }
        }
    }
}
