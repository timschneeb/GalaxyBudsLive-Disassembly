package androidx.appcompat.widget;

import android.content.Context;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.reflect.os.SeslPerfManagerReflector;
import java.lang.reflect.Array;

public class SeslOverScroller {
    private static final int DEFAULT_DURATION = 250;
    private static final int FLING_MODE = 1;
    private static final int SCROLL_MODE = 0;
    /* access modifiers changed from: private */
    public static long mIntervalTime;
    private final float FRAME_LATENCY_LIMIT;
    private final boolean mFlywheel;
    private Interpolator mInterpolator;
    private int mMode;
    private final SplineOverScroller mScrollerX;
    private final SplineOverScroller mScrollerY;

    public SeslOverScroller(Context context) {
        this(context, (Interpolator) null);
    }

    public SeslOverScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public SeslOverScroller(Context context, Interpolator interpolator, boolean z) {
        this.FRAME_LATENCY_LIMIT = 16.66f;
        this.mInterpolator = interpolator == null ? new ViscousFluidInterpolator() : interpolator;
        this.mFlywheel = z;
        this.mScrollerX = new SplineOverScroller(context);
        this.mScrollerY = new SplineOverScroller(context);
        if (!SeslPerfManagerReflector.onSmoothScrollEvent(false)) {
            setSmoothScrollEnabled(false);
            Log.e("SeslOverScroller", "does NOT support Smoothscroll booster thus Smoothscroll's disabled");
        }
    }

    public SeslOverScroller(Context context, Interpolator interpolator, float f, float f2) {
        this(context, interpolator, true);
    }

    public SeslOverScroller(Context context, Interpolator interpolator, float f, float f2, boolean z) {
        this(context, interpolator, z);
    }

    /* access modifiers changed from: package-private */
    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            interpolator = new ViscousFluidInterpolator();
        }
        this.mInterpolator = interpolator;
    }

    public final void setFriction(float f) {
        this.mScrollerX.setFriction(f);
        this.mScrollerY.setFriction(f);
    }

    public final boolean isFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public final void forceFinished(boolean z) {
        boolean unused = this.mScrollerX.mFinished = this.mScrollerY.mFinished = z;
    }

    public final int getCurrX() {
        return this.mScrollerX.mCurrentPosition;
    }

    public final int getCurrY() {
        return this.mScrollerY.mCurrentPosition;
    }

    public float getCurrVelocity() {
        return (float) Math.hypot((double) this.mScrollerX.mCurrVelocity, (double) this.mScrollerY.mCurrVelocity);
    }

    public final int getStartX() {
        return this.mScrollerX.mStart;
    }

    public final int getStartY() {
        return this.mScrollerY.mStart;
    }

    public final int getFinalX() {
        return this.mScrollerX.mFinal;
    }

    public final int getFinalY() {
        return this.mScrollerY.mFinal;
    }

    @Deprecated
    public final int getDuration() {
        return Math.max(this.mScrollerX.mDuration, this.mScrollerY.mDuration);
    }

    @Deprecated
    public void extendDuration(int i) {
        this.mScrollerX.extendDuration(i);
        this.mScrollerY.extendDuration(i);
    }

    @Deprecated
    public void setFinalX(int i) {
        this.mScrollerX.setFinalPosition(i);
    }

    @Deprecated
    public void setFinalY(int i) {
        this.mScrollerY.setFinalPosition(i);
    }

    public boolean computeScrollOffset() {
        if (isFinished()) {
            return false;
        }
        int i = this.mMode;
        if (i == 0) {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() - this.mScrollerX.mStartTime;
            int access$500 = this.mScrollerX.mDuration;
            if (currentAnimationTimeMillis < ((long) access$500)) {
                float interpolation = this.mInterpolator.getInterpolation(((float) currentAnimationTimeMillis) / ((float) access$500));
                this.mScrollerX.updateScroll(interpolation);
                this.mScrollerY.updateScroll(interpolation);
            } else {
                abortAnimation();
            }
        } else if (i == 1) {
            if (!this.mScrollerX.mFinished && !this.mScrollerX.update() && !this.mScrollerX.continueWhenFinished()) {
                this.mScrollerX.finish();
            }
            if (!this.mScrollerY.mFinished && !this.mScrollerY.update() && !this.mScrollerY.continueWhenFinished()) {
                this.mScrollerY.finish();
            }
        }
        return true;
    }

    public void startScroll(int i, int i2, int i3, int i4) {
        startScroll(i, i2, i3, i4, 250);
    }

    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        this.mMode = 0;
        this.mScrollerX.startScroll(i, i3, i5);
        this.mScrollerY.startScroll(i2, i4, i5);
    }

    public boolean springBack(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mMode = 1;
        boolean springback = this.mScrollerX.springback(i, i3, i4);
        boolean springback2 = this.mScrollerY.springback(i2, i5, i6);
        if (springback || springback2) {
            return true;
        }
        return false;
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        fling(i, i2, i3, i4, i5, i6, i7, i8, 0, 0);
    }

    /* access modifiers changed from: protected */
    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        fling(i, i2, i3, i4, i5, i6, i7, i8, 0, 0, z);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z, boolean z2, float f) {
        fling(i, i2, i3, i4, i5, i6, i7, i8, 0, 0, z, z2, f);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        int i11;
        int i12;
        int i13;
        int i14;
        if (!this.mFlywheel || isFinished()) {
            i14 = i3;
        } else {
            float access$200 = this.mScrollerX.mCurrVelocity;
            float access$2002 = this.mScrollerY.mCurrVelocity;
            i14 = i3;
            float f = (float) i14;
            if (Math.signum(f) == Math.signum(access$200)) {
                i13 = i4;
                float f2 = (float) i13;
                if (Math.signum(f2) == Math.signum(access$2002)) {
                    i12 = (int) (f2 + access$2002);
                    i11 = (int) (f + access$200);
                    this.mMode = 1;
                    this.mScrollerX.fling(i, i11, i5, i6, i9);
                    this.mScrollerY.fling(i2, i12, i7, i8, i10);
                }
                i12 = i13;
                i11 = i14;
                this.mMode = 1;
                this.mScrollerX.fling(i, i11, i5, i6, i9);
                this.mScrollerY.fling(i2, i12, i7, i8, i10);
            }
        }
        i13 = i4;
        i12 = i13;
        i11 = i14;
        this.mMode = 1;
        this.mScrollerX.fling(i, i11, i5, i6, i9);
        this.mScrollerY.fling(i2, i12, i7, i8, i10);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, boolean z) {
        int i11;
        int i12;
        int i13;
        int i14;
        if (!this.mFlywheel || isFinished() || z) {
            i14 = i3;
        } else {
            float access$200 = this.mScrollerX.mCurrVelocity;
            float access$2002 = this.mScrollerY.mCurrVelocity;
            i14 = i3;
            float f = (float) i14;
            if (Math.signum(f) == Math.signum(access$200)) {
                i13 = i4;
                float f2 = (float) i13;
                if (Math.signum(f2) == Math.signum(access$2002)) {
                    i12 = (int) (f2 + access$2002);
                    i11 = (int) (f + access$200);
                    this.mMode = 1;
                    this.mScrollerX.fling(i, i11, i5, i6, i9);
                    this.mScrollerY.fling(i2, i12, i7, i8, i10);
                }
                i12 = i13;
                i11 = i14;
                this.mMode = 1;
                this.mScrollerX.fling(i, i11, i5, i6, i9);
                this.mScrollerY.fling(i2, i12, i7, i8, i10);
            }
        }
        i13 = i4;
        i12 = i13;
        i11 = i14;
        this.mMode = 1;
        this.mScrollerX.fling(i, i11, i5, i6, i9);
        this.mScrollerY.fling(i2, i12, i7, i8, i10);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0058  */
    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, boolean z, boolean z2, float f) {
        int i11;
        int i12;
        int i13;
        if (!this.mFlywheel || isFinished() || z) {
            i12 = i3;
        } else {
            float access$200 = this.mScrollerX.mCurrVelocity;
            float access$2002 = this.mScrollerY.mCurrVelocity;
            i12 = i3;
            float f2 = (float) i12;
            if (Math.signum(f2) == Math.signum(access$200)) {
                i13 = i4;
                float f3 = (float) i13;
                if (Math.signum(f3) == Math.signum(access$2002)) {
                    i12 = (int) (f2 + access$200);
                    i11 = (int) (f3 + access$2002);
                    if (z2) {
                        float f4 = 0.0f;
                        if (f >= 0.0f) {
                            f4 = f > 16.66f ? 16.66f : f;
                        }
                        mIntervalTime = (long) f4;
                    } else {
                        mIntervalTime = 0;
                    }
                    this.mMode = 1;
                    this.mScrollerX.fling(i, i12, i5, i6, i9);
                    this.mScrollerY.fling(i2, i11, i7, i8, i10);
                }
                i11 = i13;
                if (z2) {
                }
                this.mMode = 1;
                this.mScrollerX.fling(i, i12, i5, i6, i9);
                this.mScrollerY.fling(i2, i11, i7, i8, i10);
            }
        }
        i13 = i4;
        i11 = i13;
        if (z2) {
        }
        this.mMode = 1;
        this.mScrollerX.fling(i, i12, i5, i6, i9);
        this.mScrollerY.fling(i2, i11, i7, i8, i10);
    }

    public void notifyHorizontalEdgeReached(int i, int i2, int i3) {
        this.mScrollerX.notifyEdgeReached(i, i2, i3);
    }

    public void notifyVerticalEdgeReached(int i, int i2, int i3) {
        this.mScrollerY.notifyEdgeReached(i, i2, i3);
    }

    public boolean isOverScrolled() {
        return (!this.mScrollerX.mFinished && this.mScrollerX.mState != 0) || (!this.mScrollerY.mFinished && this.mScrollerY.mState != 0);
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    public int timePassed() {
        return (int) (AnimationUtils.currentAnimationTimeMillis() - Math.min(this.mScrollerX.mStartTime, this.mScrollerY.mStartTime));
    }

    public boolean isScrollingInDirection(float f, float f2) {
        return !isFinished() && Math.signum(f) == Math.signum((float) (this.mScrollerX.mFinal - this.mScrollerX.mStart)) && Math.signum(f2) == Math.signum((float) (this.mScrollerY.mFinal - this.mScrollerY.mStart));
    }

    public void setSmoothScrollEnabled(boolean z) {
        this.mScrollerX.setMode(z ? 1 : 0);
        this.mScrollerY.setMode(z);
    }

    static class SplineOverScroller {
        private static final int BALLISTIC = 2;
        private static final int CUBIC = 1;
        private static float DECELERATION_RATE = ((float) (Math.log(0.78d) / Math.log(0.9d)));
        private static final int DEFAULT_MODE = 1;
        private static final float DISTANCE_M1 = 3.0f;
        private static final float DISTANCE_M2 = 1.5f;
        private static final float DURATION_M1 = 3.0f;
        private static final float DURATION_M2 = 1.8f;
        private static final float END_TENSION = 1.0f;
        private static final float GRAVITY = 2000.0f;
        private static final long HIGHER_TIME_GAP_COMPENSATION = 1;
        private static final long HIGHER_TIME_GAP_MARGIN = 1;
        private static float INFLEXION = INFLEXIONS[1];
        private static final float[] INFLEXIONS = {0.35f, 0.22f};
        private static final long LOWER_TIME_GAP_COMPENSATION = 1;
        private static final long LOWER_TIME_GAP_MARGIN = 1;
        private static final int MARGIN_COMPENSATION_STARTING_COUNT = 30;
        private static final int NB_SAMPLES = 100;
        public static final int ORIGINAL_MODE = 0;
        public static final int SMOOTH_MODE = 1;
        private static final int SPLINE = 0;
        private static float[] SPLINE_POSITION = SPLINE_POSITIONS[1];
        private static final float[][] SPLINE_POSITIONS;
        private static float[] SPLINE_TIME = SPLINE_TIMES[1];
        private static final float[][] SPLINE_TIMES;
        private static final float START_TENSION = 0.5f;
        private static boolean sEnableSmoothFling = true;
        private static boolean sRegulateCurrentTimeInterval = true;
        /* access modifiers changed from: private */
        public float mCurrVelocity;
        /* access modifiers changed from: private */
        public int mCurrentPosition;
        private float mDeceleration;
        /* access modifiers changed from: private */
        public int mDuration;
        /* access modifiers changed from: private */
        public int mFinal;
        /* access modifiers changed from: private */
        public boolean mFinished = true;
        private float mFlingFriction = ViewConfiguration.getScrollFriction();
        private boolean mIsDVFSBoosting = false;
        private int mMaximumVelocity;
        private int mOver;
        private float mPhysicalCoeff;
        private long mPrevTime = 0;
        private long mPrevTimeGap = 0;
        private int mSplineDistance;
        private int mSplineDuration;
        /* access modifiers changed from: private */
        public int mStart;
        /* access modifiers changed from: private */
        public long mStartTime;
        /* access modifiers changed from: private */
        public int mState = 0;
        private int mUpdateCount = 0;
        private int mVelocity;

        private static float getDeceleration(int i) {
            if (i > 0) {
                return -2000.0f;
            }
            return GRAVITY;
        }

        static {
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            Class<float> cls = float.class;
            SPLINE_POSITIONS = (float[][]) Array.newInstance(cls, new int[]{2, 101});
            SPLINE_TIMES = (float[][]) Array.newInstance(cls, new int[]{2, 101});
            int i = 0;
            for (int i2 = 2; i < i2; i2 = 2) {
                float[] fArr = INFLEXIONS;
                float f9 = fArr[i] * START_TENSION;
                float f10 = 1.0f - ((1.0f - fArr[i]) * 1.0f);
                float f11 = 0.0f;
                float f12 = 0.0f;
                for (int i3 = 0; i3 < 100; i3++) {
                    float f13 = ((float) i3) / 100.0f;
                    float f14 = 1.0f;
                    while (true) {
                        f = ((f14 - f11) / 2.0f) + f11;
                        f2 = 1.0f - f;
                        f3 = f * 3.0f * f2;
                        f4 = f * f * f;
                        float f15 = (((f2 * f9) + (f * f10)) * f3) + f4;
                        if (((double) Math.abs(f15 - f13)) < 1.0E-5d) {
                            break;
                        } else if (f15 > f13) {
                            f14 = f;
                        } else {
                            f11 = f;
                        }
                    }
                    SPLINE_POSITIONS[i][i3] = (f3 * ((f2 * START_TENSION) + f)) + f4;
                    float f16 = 1.0f;
                    while (true) {
                        f5 = ((f16 - f12) / 2.0f) + f12;
                        f6 = 1.0f - f5;
                        f7 = f5 * 3.0f * f6;
                        f8 = f5 * f5 * f5;
                        float f17 = (((f6 * START_TENSION) + f5) * f7) + f8;
                        if (((double) Math.abs(f17 - f13)) < 1.0E-5d) {
                            break;
                        } else if (f17 > f13) {
                            f16 = f5;
                        } else {
                            f12 = f5;
                        }
                    }
                    SPLINE_TIMES[i][i3] = (f7 * ((f6 * f9) + (f5 * f10))) + f8;
                }
                float[] fArr2 = SPLINE_POSITIONS[i];
                SPLINE_TIMES[i][100] = 1.0f;
                fArr2[100] = 1.0f;
                i++;
            }
        }

        public void setMode(int i) {
            if (i >= 0 && i <= 1) {
                if (i == 0) {
                    sEnableSmoothFling = false;
                    sRegulateCurrentTimeInterval = false;
                } else {
                    sEnableSmoothFling = true;
                    sRegulateCurrentTimeInterval = true;
                }
                INFLEXION = INFLEXIONS[i];
                SPLINE_POSITION = SPLINE_POSITIONS[i];
                SPLINE_TIME = SPLINE_TIMES[i];
            }
        }

        /* access modifiers changed from: package-private */
        public void setFriction(float f) {
            this.mFlingFriction = f;
        }

        SplineOverScroller(Context context) {
            this.mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
            if (sEnableSmoothFling) {
                this.mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
            }
        }

        /* access modifiers changed from: package-private */
        public void updateScroll(float f) {
            int i = this.mStart;
            this.mCurrentPosition = i + Math.round(f * ((float) (this.mFinal - i)));
        }

        private void adjustDuration(int i, int i2, int i3) {
            float abs = Math.abs(((float) (i3 - i)) / ((float) (i2 - i)));
            int i4 = (int) (abs * 100.0f);
            if (i4 < 100) {
                float f = ((float) i4) / 100.0f;
                int i5 = i4 + 1;
                float[] fArr = SPLINE_TIME;
                float f2 = fArr[i4];
                this.mDuration = (int) (((float) this.mDuration) * (f2 + (((abs - f) / ((((float) i5) / 100.0f) - f)) * (fArr[i5] - f2))));
            }
        }

        /* access modifiers changed from: package-private */
        public void startScroll(int i, int i2, int i3) {
            this.mFinished = false;
            this.mStart = i;
            this.mCurrentPosition = i;
            this.mFinal = i + i2;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = i3;
            this.mDeceleration = 0.0f;
            this.mVelocity = 0;
        }

        /* access modifiers changed from: package-private */
        public void finish() {
            if (this.mIsDVFSBoosting) {
                SeslPerfManagerReflector.onSmoothScrollEvent(false);
                this.mIsDVFSBoosting = false;
            }
            this.mCurrentPosition = this.mFinal;
            this.mFinished = true;
        }

        /* access modifiers changed from: package-private */
        public void setFinalPosition(int i) {
            this.mFinal = i;
            this.mFinished = false;
        }

        /* access modifiers changed from: package-private */
        public void extendDuration(int i) {
            this.mDuration = ((int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime)) + i;
            this.mFinished = false;
        }

        /* access modifiers changed from: package-private */
        public boolean springback(int i, int i2, int i3) {
            this.mFinished = true;
            this.mFinal = i;
            this.mStart = i;
            this.mCurrentPosition = i;
            this.mVelocity = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 0;
            if (i < i2) {
                startSpringback(i, i2, 0);
            } else if (i > i3) {
                startSpringback(i, i3, 0);
            }
            return !this.mFinished;
        }

        private void startSpringback(int i, int i2, int i3) {
            this.mFinished = false;
            this.mState = 1;
            this.mStart = i;
            this.mCurrentPosition = i;
            this.mFinal = i2;
            int i4 = i - i2;
            this.mDeceleration = getDeceleration(i4);
            this.mVelocity = -i4;
            this.mOver = Math.abs(i4);
            this.mDuration = (int) (Math.sqrt((((double) i4) * -2.0d) / ((double) this.mDeceleration)) * 1000.0d);
        }

        /* access modifiers changed from: package-private */
        public void fling(int i, int i2, int i3, int i4, int i5) {
            this.mOver = i5;
            this.mFinished = false;
            this.mVelocity = i2;
            float f = (float) i2;
            this.mCurrVelocity = f;
            this.mSplineDuration = 0;
            this.mDuration = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis() - SeslOverScroller.mIntervalTime;
            this.mStart = i;
            this.mCurrentPosition = i;
            if (i > i4 || i < i3) {
                startAfterEdge(i, i3, i4, i2);
                return;
            }
            this.mState = 0;
            double d = 0.0d;
            if (i2 != 0) {
                int splineFlingDuration = getSplineFlingDuration(i2);
                this.mSplineDuration = splineFlingDuration;
                this.mDuration = splineFlingDuration;
                d = getSplineFlingDistance(i2);
                if (sEnableSmoothFling && !this.mIsDVFSBoosting && (i2 >= 800 || i2 <= -800)) {
                    SeslPerfManagerReflector.onSmoothScrollEvent(true);
                    this.mIsDVFSBoosting = true;
                }
            }
            this.mSplineDistance = (int) (d * ((double) Math.signum(f)));
            this.mFinal = i + this.mSplineDistance;
            int i6 = this.mFinal;
            if (i6 < i3) {
                adjustDuration(this.mStart, i6, i3);
                this.mFinal = i3;
            }
            int i7 = this.mFinal;
            if (i7 > i4) {
                adjustDuration(this.mStart, i7, i4);
                this.mFinal = i4;
            }
            if (sRegulateCurrentTimeInterval) {
                this.mUpdateCount = 0;
            }
        }

        private double getSplineDeceleration(int i) {
            return Math.log((double) ((INFLEXION * ((float) Math.abs(i))) / (this.mFlingFriction * this.mPhysicalCoeff)));
        }

        private double getSplineFlingDistance(int i) {
            double d;
            double exp;
            double splineDeceleration = getSplineDeceleration(i);
            float f = DECELERATION_RATE;
            double d2 = ((double) f) - 1.0d;
            if (sEnableSmoothFling) {
                int abs = (int) ((((float) Math.abs(i)) / ((float) this.mMaximumVelocity)) * 100.0f);
                if (abs > 100) {
                    abs = 100;
                }
                d = ((double) (((1.0f - SPLINE_POSITION[abs]) * 3.0f) + DISTANCE_M2)) * ((double) this.mFlingFriction) * ((double) this.mPhysicalCoeff);
                exp = Math.exp((((double) DECELERATION_RATE) / d2) * splineDeceleration);
            } else {
                d = (double) (this.mFlingFriction * this.mPhysicalCoeff);
                exp = Math.exp((((double) f) / d2) * splineDeceleration);
            }
            return d * exp;
        }

        private int getSplineFlingDuration(int i) {
            double splineDeceleration = getSplineDeceleration(i);
            double d = ((double) DECELERATION_RATE) - 1.0d;
            if (!sEnableSmoothFling) {
                return (int) (Math.exp(splineDeceleration / d) * 1000.0d);
            }
            int abs = (int) ((((float) Math.abs(i)) / ((float) this.mMaximumVelocity)) * 100.0f);
            if (abs > 100) {
                abs = 100;
            }
            return (int) (((double) (((1.0f - SPLINE_POSITION[abs]) * 3.0f) + DURATION_M2)) * 1000.0d * Math.exp(splineDeceleration / d));
        }

        private void fitOnBounceCurve(int i, int i2, int i3) {
            float f = this.mDeceleration;
            float f2 = ((float) (-i3)) / f;
            float f3 = (float) i3;
            float sqrt = (float) Math.sqrt((((double) ((((f3 * f3) / 2.0f) / Math.abs(f)) + ((float) Math.abs(i2 - i)))) * 2.0d) / ((double) Math.abs(this.mDeceleration)));
            this.mStartTime -= (long) ((int) ((sqrt - f2) * 1000.0f));
            this.mStart = i2;
            this.mCurrentPosition = i2;
            this.mVelocity = (int) ((-this.mDeceleration) * sqrt);
        }

        private void startBounceAfterEdge(int i, int i2, int i3) {
            this.mDeceleration = getDeceleration(i3 == 0 ? i - i2 : i3);
            fitOnBounceCurve(i, i2, i3);
            onEdgeReached();
        }

        private void startAfterEdge(int i, int i2, int i3, int i4) {
            int i5 = i;
            int i6 = i3;
            int i7 = i4;
            boolean z = true;
            int i8 = i2;
            if (i5 <= i8 || i5 >= i6) {
                boolean z2 = i5 > i6;
                int i9 = z2 ? i6 : i8;
                int i10 = i5 - i9;
                if (i10 * i7 < 0) {
                    z = false;
                }
                if (z) {
                    startBounceAfterEdge(i, i9, i7);
                } else if (getSplineFlingDistance(i7) > ((double) Math.abs(i10))) {
                    fling(i, i4, z2 ? i8 : i5, z2 ? i5 : i6, this.mOver);
                } else {
                    startSpringback(i, i9, i7);
                }
            } else {
                Log.e("OverScroller", "startAfterEdge called from a valid position");
                this.mFinished = true;
            }
        }

        /* access modifiers changed from: package-private */
        public void notifyEdgeReached(int i, int i2, int i3) {
            if (this.mState == 0) {
                this.mOver = i3;
                this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                startAfterEdge(i, i2, i2, (int) this.mCurrVelocity);
            }
        }

        private void onEdgeReached() {
            int i = this.mVelocity;
            float f = ((float) i) * ((float) i);
            float abs = f / (Math.abs(this.mDeceleration) * 2.0f);
            float signum = Math.signum((float) this.mVelocity);
            int i2 = this.mOver;
            if (abs > ((float) i2)) {
                this.mDeceleration = ((-signum) * f) / (((float) i2) * 2.0f);
                abs = (float) i2;
            }
            this.mOver = (int) abs;
            this.mState = 2;
            int i3 = this.mStart;
            if (this.mVelocity <= 0) {
                abs = -abs;
            }
            this.mFinal = i3 + ((int) abs);
            this.mDuration = -((int) ((((float) this.mVelocity) * 1000.0f) / this.mDeceleration));
            if (sRegulateCurrentTimeInterval) {
                this.mUpdateCount = 0;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean continueWhenFinished() {
            int i = this.mState;
            if (i != 0) {
                if (i == 1) {
                    return false;
                }
                if (i == 2) {
                    this.mStartTime += (long) this.mDuration;
                    startSpringback(this.mFinal, this.mStart, 0);
                }
            } else if (this.mDuration >= this.mSplineDuration) {
                return false;
            } else {
                int i2 = this.mFinal;
                this.mStart = i2;
                this.mCurrentPosition = i2;
                this.mVelocity = (int) this.mCurrVelocity;
                this.mDeceleration = getDeceleration(this.mVelocity);
                this.mStartTime += (long) this.mDuration;
                onEdgeReached();
            }
            update();
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean update() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
            if (sRegulateCurrentTimeInterval && this.mState == 0) {
                if (SeslOverScroller.mIntervalTime == 0 && this.mUpdateCount > 0) {
                    currentAnimationTimeMillis = (this.mPrevTime + currentAnimationTimeMillis) / 2;
                }
                if (this.mUpdateCount > 30) {
                    long j = this.mPrevTime;
                    long j2 = currentAnimationTimeMillis - j;
                    long j3 = this.mPrevTimeGap;
                    if (j2 > j3 + 1) {
                        currentAnimationTimeMillis = j + j3 + 1;
                    } else if (j2 < j3 - 1) {
                        currentAnimationTimeMillis = (j + j3) - 1;
                    }
                }
                if (currentAnimationTimeMillis < 0) {
                    currentAnimationTimeMillis = 0;
                }
                this.mPrevTimeGap = currentAnimationTimeMillis - this.mPrevTime;
                this.mPrevTime = currentAnimationTimeMillis;
                this.mUpdateCount++;
            }
            if (currentAnimationTimeMillis != 0) {
                int i = this.mDuration;
                if (currentAnimationTimeMillis > ((long) i)) {
                    return false;
                }
                double d = 0.0d;
                int i2 = this.mState;
                if (i2 == 0) {
                    float f = ((float) currentAnimationTimeMillis) / ((float) this.mSplineDuration);
                    int i3 = (int) (f * 100.0f);
                    float f2 = 1.0f;
                    float f3 = 0.0f;
                    if (i3 < 100) {
                        float f4 = ((float) i3) / 100.0f;
                        int i4 = i3 + 1;
                        float[] fArr = SPLINE_POSITION;
                        float f5 = fArr[i3];
                        f3 = (fArr[i4] - f5) / ((((float) i4) / 100.0f) - f4);
                        f2 = f5 + ((f - f4) * f3);
                    }
                    int i5 = this.mSplineDistance;
                    this.mCurrVelocity = ((f3 * ((float) i5)) / ((float) this.mSplineDuration)) * 1000.0f;
                    d = (double) (f2 * ((float) i5));
                } else if (i2 == 1) {
                    float f6 = ((float) currentAnimationTimeMillis) / ((float) i);
                    float f7 = f6 * f6;
                    float signum = Math.signum((float) this.mVelocity);
                    int i6 = this.mOver;
                    d = (double) (((float) i6) * signum * ((3.0f * f7) - ((2.0f * f6) * f7)));
                    this.mCurrVelocity = signum * ((float) i6) * 6.0f * ((-f6) + f7);
                } else if (i2 == 2) {
                    float f8 = ((float) currentAnimationTimeMillis) / 1000.0f;
                    int i7 = this.mVelocity;
                    float f9 = this.mDeceleration;
                    this.mCurrVelocity = ((float) i7) + (f9 * f8);
                    d = (double) ((((float) i7) * f8) + (((f9 * f8) * f8) / 2.0f));
                }
                this.mCurrentPosition = this.mStart + ((int) Math.round(d));
                return true;
            } else if (this.mDuration > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    static class ViscousFluidInterpolator implements Interpolator {
        private static final float VISCOUS_FLUID_NORMALIZE = (1.0f / viscousFluid(1.0f));
        private static final float VISCOUS_FLUID_OFFSET = (1.0f - (VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0f)));
        private static final float VISCOUS_FLUID_SCALE = 8.0f;

        ViscousFluidInterpolator() {
        }

        private static float viscousFluid(float f) {
            float f2 = f * VISCOUS_FLUID_SCALE;
            if (f2 < 1.0f) {
                return f2 - (1.0f - ((float) Math.exp((double) (-f2))));
            }
            return ((1.0f - ((float) Math.exp((double) (1.0f - f2)))) * 0.63212055f) + 0.36787945f;
        }

        public float getInterpolation(float f) {
            float viscousFluid = VISCOUS_FLUID_NORMALIZE * viscousFluid(f);
            return viscousFluid > 0.0f ? viscousFluid + VISCOUS_FLUID_OFFSET : viscousFluid;
        }
    }
}
