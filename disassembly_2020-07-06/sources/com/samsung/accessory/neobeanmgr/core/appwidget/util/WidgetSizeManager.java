package com.samsung.accessory.neobeanmgr.core.appwidget.util;

import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;

public class WidgetSizeManager {
    private static final String TAG = (Application.TAG_ + WidgetSizeManager.class.getSimpleName());
    private static final float WIDGET_LANDSCAPE_HEIGHT_1 = 40.0f;
    public static final int WIDGET_LANDSCAPE_TYPE_1X1 = 3;
    public static final int WIDGET_LANDSCAPE_TYPE_2X1 = 4;
    private static final float WIDGET_LANDSCAPE_WIDTH_1 = 148.0f;
    private static final float WIDGET_LANDSCAPE_WIDTH_2 = 295.0f;
    private static final float WIDGET_PORTRAIT_HEIGHT_1 = 109.0f;
    public static final int WIDGET_PORTRAIT_TYPE_1X1 = 0;
    public static final int WIDGET_PORTRAIT_TYPE_2X1 = 1;
    private static final float WIDGET_PORTRAIT_WIDTH_1 = 73.0f;
    private static final float WIDGET_PORTRAIT_WIDTH_2 = 166.0f;
    private float mPaddingLeft;
    private float mPaddingTop;
    private float mRatio;

    public WidgetSizeManager(int i, float f, float f2) {
        float f3;
        float f4 = f / f2;
        init();
        float f5 = WIDGET_PORTRAIT_HEIGHT_1;
        if (i == 0) {
            f3 = WIDGET_PORTRAIT_WIDTH_1;
        } else if (i != 1) {
            if (i == 3) {
                f3 = WIDGET_LANDSCAPE_WIDTH_1;
            } else if (i != 4) {
                f3 = 0.0f;
                f5 = 0.0f;
            } else {
                f3 = WIDGET_LANDSCAPE_WIDTH_2;
            }
            f5 = WIDGET_LANDSCAPE_HEIGHT_1;
        } else {
            f3 = WIDGET_PORTRAIT_WIDTH_2;
        }
        if (f3 != 0.0f) {
            if (f4 > f3 / f5) {
                this.mRatio = f2 / f5;
                this.mPaddingLeft = (f - (f3 * this.mRatio)) / 2.0f;
                if (this.mPaddingLeft < 0.0f) {
                    String str = TAG;
                    Log.d(str, "mPaddingLeft < 0 : " + this.mPaddingLeft);
                    this.mPaddingLeft = 0.0f;
                }
            } else {
                this.mRatio = f / f3;
                this.mPaddingTop = (f2 - (f5 * this.mRatio)) / 2.0f;
                if (this.mPaddingTop < 0.0f) {
                    String str2 = TAG;
                    Log.d(str2, "mPaddingTop < 0 : " + this.mPaddingTop);
                    this.mPaddingTop = 0.0f;
                }
            }
            String str3 = TAG;
            Log.d(str3, "widgetType : " + i + ", width : " + f + ", height : " + f2 + ", mRatio : " + this.mRatio + ", mPaddingLeft : " + this.mPaddingLeft + ", mPaddingTop : " + this.mPaddingTop);
        }
    }

    private void init() {
        this.mRatio = 1.0f;
        this.mPaddingLeft = 0.0f;
        this.mPaddingTop = 0.0f;
    }

    public float getRatio() {
        return this.mRatio;
    }

    public float getPaddingLeft() {
        return this.mPaddingLeft;
    }

    public float getPaddingLeftPixel() {
        return WidgetUtil.DP_TO_PX(this.mPaddingLeft);
    }

    public float getPaddingTop() {
        return this.mPaddingTop;
    }

    public float getPaddingTopPixel() {
        return WidgetUtil.DP_TO_PX(this.mPaddingTop);
    }
}
