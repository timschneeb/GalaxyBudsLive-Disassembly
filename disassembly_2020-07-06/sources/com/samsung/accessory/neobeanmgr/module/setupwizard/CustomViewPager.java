package com.samsung.accessory.neobeanmgr.module.setupwizard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import androidx.viewpager.widget.ViewPager;
import com.samsung.accessory.neobeanmgr.common.ui.Interpolators;
import java.lang.reflect.Field;

public class CustomViewPager extends ViewPager {
    private static final String TAG = "NeoBean_CustomViewPager";
    private Scroller mScroller = null;

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public CustomViewPager(Context context) {
        super(context);
        init();
    }

    public CustomViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        Log.d(TAG, "init()");
        try {
            Field declaredField = ViewPager.class.getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            this.mScroller = new CustomScroller(getContext(), Interpolators.SineInOut80Interpolator());
            declaredField.set(this, this.mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class CustomScroller extends Scroller {
        private final int DURATION = 500;

        public CustomScroller(Context context) {
            super(context);
        }

        public CustomScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public CustomScroller(Context context, Interpolator interpolator, boolean z) {
            super(context, interpolator, z);
        }

        public void startScroll(int i, int i2, int i3, int i4, int i5) {
            super.startScroll(i, i2, i3, i4, 500);
        }

        public void startScroll(int i, int i2, int i3, int i4) {
            super.startScroll(i, i2, i3, i4, 500);
        }
    }
}
