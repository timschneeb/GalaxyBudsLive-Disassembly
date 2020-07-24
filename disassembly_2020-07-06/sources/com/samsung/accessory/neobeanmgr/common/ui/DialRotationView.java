package com.samsung.accessory.neobeanmgr.common.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.samsung.accessory.neobeanmgr.Application;
import java.util.TreeMap;

public class DialRotationView extends View {
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + DialRotationView.class.getSimpleName());
    private float DIAL_ENABLE_TOUCH_ANGLE = 30.0f;
    private Float[] ROTATION_ANGLE = {Float.valueOf(146.0f), Float.valueOf(-170.0f), Float.valueOf(-127.0f), Float.valueOf(-54.0f), Float.valueOf(-12.0f), Float.valueOf(35.0f)};
    private TreeMap<Float, Integer> mAngleMap = new TreeMap<>();
    private int mCurrentIndex;
    private int mDataSize;
    /* access modifiers changed from: private */
    public float mDialAngle;
    private DialEventListener mDialEventListener;
    /* access modifiers changed from: private */
    public AppCompatImageView mDialView;
    private float mEndAngle;
    private TreeMap<Integer, Float> mIndexMap = new TreeMap<>();
    /* access modifiers changed from: private */
    public float mStartAngle;
    private int mViewHeight;
    private int mViewWidth;
    private RotationThread rotationThread = new RotationThread();
    private ValueAnimator valueAnimator;

    public interface DialEventListener {
        void onDialChanged(int i);
    }

    private float convertPositiveAngle(float f) {
        return f < 0.0f ? f + 360.0f : f;
    }

    public DialRotationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Float[] fArr = this.ROTATION_ANGLE;
        this.mDataSize = fArr.length;
        this.mStartAngle = convertPositiveAngle(fArr[0].floatValue());
        this.mEndAngle = convertPositiveAngle(this.ROTATION_ANGLE[this.mDataSize - 1].floatValue() - this.mStartAngle);
        setAngleFromStartAngle();
        setRTLConfiguration(false);
    }

    private void setAngleFromStartAngle() {
        for (int i = 0; i < this.mDataSize; i++) {
            Float[] fArr = this.ROTATION_ANGLE;
            fArr[i] = Float.valueOf(convertPositiveAngle(fArr[i].floatValue() - this.mStartAngle));
        }
    }

    public void setRTLConfiguration(boolean z) {
        this.mIndexMap.clear();
        this.mAngleMap.clear();
        for (int i = 0; i < this.mDataSize; i++) {
            float floatValue = this.ROTATION_ANGLE[i].floatValue();
            if (z) {
                this.mIndexMap.put(Integer.valueOf((this.mDataSize - 1) - i), Float.valueOf(floatValue));
                this.mAngleMap.put(Float.valueOf(floatValue), Integer.valueOf((this.mDataSize - 1) - i));
            } else {
                this.mIndexMap.put(Integer.valueOf(i), Float.valueOf(floatValue));
                this.mAngleMap.put(Float.valueOf(floatValue), Integer.valueOf(i));
            }
        }
        this.mDialAngle = this.mIndexMap.get(0).floatValue();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mViewWidth = i3 - i;
        this.mViewHeight = i4 - i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b0, code lost:
        if (r12 != 3) goto L_0x011d;
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        if (!isEnabled()) {
            if (motionEvent.getAction() == 1) {
                this.rotationThread.stop();
            }
            return false;
        } else if (this.mDialEventListener == null) {
            return false;
        } else {
            int i2 = this.mViewWidth / 2;
            int i3 = this.mViewHeight / 2;
            int x = ((int) motionEvent.getX()) - i2;
            int y = i3 - ((int) motionEvent.getY());
            float convertPositiveAngle = convertPositiveAngle((-this.mStartAngle) - ((float) Math.toDegrees(Math.atan2((double) y, (double) x))));
            float f = this.mEndAngle;
            if (convertPositiveAngle > f) {
                convertPositiveAngle = convertPositiveAngle - f < this.mStartAngle - 90.0f ? f : 0.0f;
            }
            Float floorKey = this.mAngleMap.floorKey(Float.valueOf(convertPositiveAngle));
            if (floorKey == null) {
                return false;
            }
            Float higherKey = this.mAngleMap.higherKey(Float.valueOf(convertPositiveAngle));
            if (higherKey == null) {
                i = this.mAngleMap.get(floorKey).intValue();
            } else if (convertPositiveAngle - floorKey.floatValue() <= higherKey.floatValue() - convertPositiveAngle) {
                i = this.mAngleMap.get(floorKey).intValue();
            } else {
                i = this.mAngleMap.get(higherKey).intValue();
            }
            int action = motionEvent.getAction() & 255;
            if (action != 0) {
                if (action != 1) {
                    if (action == 2) {
                        this.rotationThread.setTargetAngle(convertPositiveAngle);
                        if (i != this.mCurrentIndex) {
                            this.mCurrentIndex = i;
                            this.mDialEventListener.onDialChanged(this.mCurrentIndex);
                        }
                    }
                }
                this.rotationThread.stop();
                smoothRotate(this.mIndexMap.get(Integer.valueOf(this.mCurrentIndex)).floatValue());
            } else {
                float f2 = (float) x;
                float f3 = (float) y;
                float f4 = (float) i2;
                if (isOutOfCircleRange(f2, f3, f4)) {
                    return false;
                }
                if (isInsideCircleRange(f2, f3, f4 - UiUtil.DP_TO_PX(20.0f)) && !isInsideTouchAngle(convertPositiveAngle)) {
                    return false;
                }
                ValueAnimator valueAnimator2 = this.valueAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                this.rotationThread.setTargetAngle(convertPositiveAngle);
                this.rotationThread.start();
                if (i != this.mCurrentIndex) {
                    this.mCurrentIndex = i;
                    this.mDialEventListener.onDialChanged(this.mCurrentIndex);
                }
            }
            return true;
        }
    }

    private boolean isOutOfCircleRange(float f, float f2, float f3) {
        return Math.pow((double) f, 2.0d) + Math.pow((double) f2, 2.0d) > Math.pow((double) f3, 2.0d);
    }

    private boolean isInsideCircleRange(float f, float f2, float f3) {
        return Math.pow((double) f, 2.0d) + Math.pow((double) f2, 2.0d) < Math.pow((double) f3, 2.0d);
    }

    private boolean isInsideTouchAngle(float f) {
        float f2 = this.mDialAngle;
        float f3 = this.DIAL_ENABLE_TOUCH_ANGLE;
        return f < f2 + f3 && f > f2 - f3;
    }

    public void syncDialView(AppCompatImageView appCompatImageView) {
        this.mDialView = appCompatImageView;
        rotate(this.mStartAngle);
    }

    public void rotate(float f) {
        try {
            this.mDialView.setRotation(this.mStartAngle + f);
            this.mDialAngle = f;
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    public void smoothRotate(int i) {
        smoothRotate(this.mIndexMap.get(Integer.valueOf(i)).floatValue());
        this.mCurrentIndex = i;
    }

    public void smoothRotate(float f) {
        float f2 = this.mStartAngle;
        this.valueAnimator = ValueAnimator.ofFloat(new float[]{this.mDialAngle + f2, f2 + f});
        this.valueAnimator.setDuration(200);
        this.valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialRotationView.this.mDialView.setRotation(((Float) valueAnimator.getAnimatedValue()).floatValue());
                float unused = DialRotationView.this.mDialAngle = ((Float) valueAnimator.getAnimatedValue()).floatValue() - DialRotationView.this.mStartAngle;
            }
        });
        this.valueAnimator.start();
    }

    public void setPosition(int i) {
        rotate(this.mIndexMap.get(Integer.valueOf(i)).floatValue());
        this.mCurrentIndex = i;
    }

    public void setDialEventListener(DialEventListener dialEventListener) {
        this.mDialEventListener = dialEventListener;
    }

    private class RotationThread {
        /* access modifiers changed from: private */
        public Handler handler;
        /* access modifiers changed from: private */
        public Runnable rotate;
        /* access modifiers changed from: private */
        public boolean stop;
        /* access modifiers changed from: private */
        public float targetAngle;

        private RotationThread() {
            this.stop = false;
            this.handler = new Handler();
            this.rotate = new Runnable() {
                final float tick = 20.0f;

                public void run() {
                    DialRotationView.this.rotate(DialRotationView.this.mDialAngle);
                    if (Math.abs(DialRotationView.this.mDialAngle - RotationThread.this.targetAngle) < 20.0f) {
                        float unused = DialRotationView.this.mDialAngle = RotationThread.this.targetAngle;
                    } else if (DialRotationView.this.mDialAngle - RotationThread.this.targetAngle < 0.0f) {
                        float unused2 = DialRotationView.this.mDialAngle = DialRotationView.this.mDialAngle + 20.0f;
                    } else {
                        float unused3 = DialRotationView.this.mDialAngle = DialRotationView.this.mDialAngle - 20.0f;
                    }
                    if (!RotationThread.this.stop) {
                        RotationThread.this.handler.post(RotationThread.this.rotate);
                    }
                }
            };
        }

        /* access modifiers changed from: package-private */
        public void stop() {
            Log.d(DialRotationView.TAG, "RotationThread stop()");
            this.stop = true;
        }

        /* access modifiers changed from: package-private */
        public void start() {
            Log.d(DialRotationView.TAG, "RotationThread start()");
            this.stop = false;
            this.handler.post(this.rotate);
        }

        /* access modifiers changed from: package-private */
        public void setTargetAngle(float f) {
            this.targetAngle = f;
        }
    }
}
