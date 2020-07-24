package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;

public class SeslTabDotLineIndicator extends SeslAbsIndicatorView {
    private static final float CIRCLE_INTERVAL = 2.5f;
    private static final float DIAMETER_SIZE = 2.5f;
    private static final int SESL_DOT_LINE_SCALE_DIFF = 5;
    private int mDiameter;
    private int mInterval;
    private Paint mPaint;
    private float mScaleFrom;
    private final float mScaleFromDiff;
    private int mWidth;

    public SeslTabDotLineIndicator(Context context) {
        this(context, (AttributeSet) null);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mInterval = 2;
        this.mDiameter = 1;
        this.mDiameter = (int) TypedValue.applyDimension(1, 2.5f, context.getResources().getDisplayMetrics());
        this.mInterval = (int) TypedValue.applyDimension(1, 2.5f, context.getResources().getDisplayMetrics());
        this.mPaint = new Paint();
        this.mPaint.setFlags(1);
        this.mScaleFromDiff = TypedValue.applyDimension(1, 5.0f, context.getResources().getDisplayMetrics());
    }

    /* access modifiers changed from: package-private */
    public void onHide() {
        setAlpha(0.0f);
    }

    /* access modifiers changed from: package-private */
    public void onShow() {
        startReleaseEffect();
    }

    /* access modifiers changed from: package-private */
    public void startPressEffect() {
        setAlpha(1.0f);
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public void startReleaseEffect() {
        setAlpha(1.0f);
    }

    /* access modifiers changed from: package-private */
    public void startPressAndReleaseEffect() {
        setAlpha(1.0f);
        invalidate();
    }

    private void updateDotLineScaleFrom() {
        if (this.mWidth != getWidth() || this.mWidth == 0) {
            this.mWidth = getWidth();
            int i = this.mWidth;
            if (i <= 0) {
                this.mScaleFrom = 0.9f;
            } else {
                this.mScaleFrom = (((float) i) - this.mScaleFromDiff) / ((float) i);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onSetSelectedIndicatorColor(int i) {
        this.mPaint.setColor(i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        super.onDraw(canvas);
        updateDotLineScaleFrom();
        if ((isPressed() || isSelected()) && (getBackground() instanceof ColorDrawable)) {
            int width = (getWidth() - getPaddingStart()) - getPaddingEnd();
            int height = getHeight();
            int i3 = this.mDiameter;
            int i4 = ((width - i3) / (this.mInterval + i3)) + 1;
            int i5 = i4 - 1;
            int paddingStart = ((int) ((((float) i3) / 2.0f) + 0.5f)) + getPaddingStart();
            int i6 = this.mDiameter;
            int i7 = (width - i6) - ((this.mInterval + i6) * i5);
            if (i6 % 2 != 0) {
                i7--;
            }
            if (i5 > 0) {
                i = i7 / i5;
                i2 = i7 % i5;
            } else {
                i2 = 0;
                i = 0;
            }
            int i8 = 0;
            for (int i9 = 0; i9 < i4; i9++) {
                canvas.drawCircle((float) (paddingStart + i8), (float) (height / 2), ((float) this.mDiameter) / 2.0f, this.mPaint);
                i8 += this.mDiameter + this.mInterval + i;
                if (i9 < i2) {
                    i8++;
                }
            }
        }
    }
}
