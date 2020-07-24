package androidx.appcompat.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import androidx.appcompat.R;
import androidx.core.content.res.ResourcesCompat;

public class SeslRoundedCorner {
    public static final int ROUNDED_CORNER_ALL = 15;
    public static final int ROUNDED_CORNER_BOTTOM_LEFT = 4;
    public static final int ROUNDED_CORNER_BOTTOM_RIGHT = 8;
    public static final int ROUNDED_CORNER_NONE = 0;
    public static final int ROUNDED_CORNER_TOP_LEFT = 1;
    public static final int ROUNDED_CORNER_TOP_RIGHT = 2;
    static final String TAG = "SeslRoundedCorner";
    Drawable mBottomLeftRound;
    int mBottomLeftRoundColor;
    Drawable mBottomRightRound;
    int mBottomRightRoundColor;
    Context mContext;
    boolean mIsMutate;
    boolean mIsStrokeRoundedCorner;
    Resources mRes;
    int mRoundRadius;
    Drawable mRoundStrokeBottom;
    int mRoundStrokeHeight;
    Drawable mRoundStrokeTop;
    Rect mRoundedCornerBounds;
    int mRoundedCornerMode;
    Drawable mTopLeftRound;
    int mTopLeftRoundColor;
    Drawable mTopRightRound;
    int mTopRightRoundColor;
    int mX;
    int mY;

    public SeslRoundedCorner(Context context) {
        this(context, true);
    }

    public SeslRoundedCorner(Context context, boolean z) {
        this.mIsStrokeRoundedCorner = true;
        this.mIsMutate = false;
        this.mRoundRadius = -1;
        this.mRoundedCornerBounds = new Rect();
        this.mContext = context;
        this.mRes = context.getResources();
        this.mIsStrokeRoundedCorner = z;
        initRoundedCorner();
    }

    public SeslRoundedCorner(Context context, boolean z, boolean z2) {
        this.mIsStrokeRoundedCorner = true;
        this.mIsMutate = false;
        this.mRoundRadius = -1;
        this.mRoundedCornerBounds = new Rect();
        this.mContext = context;
        this.mRes = context.getResources();
        this.mIsStrokeRoundedCorner = z;
        this.mIsMutate = z2;
        initRoundedCorner();
    }

    public void setRoundedCorners(int i) {
        if ((i & -16) == 0) {
            this.mRoundedCornerMode = i;
            if (this.mTopLeftRound == null || this.mTopRightRound == null || this.mBottomLeftRound == null || this.mBottomRightRound == null) {
                initRoundedCorner();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Use wrong rounded corners to the param, corners = " + i);
    }

    public int getRoundedCorners() {
        return this.mRoundedCornerMode;
    }

    public void setRoundedCornerColor(int i, int i2) {
        if (this.mIsStrokeRoundedCorner) {
            Log.d(TAG, "can not change round color on stroke rounded corners");
        } else if (i == 0) {
            throw new IllegalArgumentException("There is no rounded corner on = " + this);
        } else if ((i & -16) == 0) {
            if (!(i2 == this.mTopLeftRoundColor && i2 == this.mBottomLeftRoundColor)) {
                Log.d(TAG, "change color = " + i2 + ", on =" + i + ", top color = " + this.mTopLeftRoundColor + ", bottom color = " + this.mBottomLeftRoundColor);
            }
            if (this.mTopLeftRound == null || this.mTopRightRound == null || this.mBottomLeftRound == null || this.mBottomRightRound == null) {
                initRoundedCorner();
            }
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(i2, PorterDuff.Mode.SRC_IN);
            if ((i & 1) != 0) {
                this.mTopLeftRoundColor = i2;
                this.mTopLeftRound.setColorFilter(porterDuffColorFilter);
            }
            if ((i & 2) != 0) {
                this.mTopRightRoundColor = i2;
                this.mTopRightRound.setColorFilter(porterDuffColorFilter);
            }
            if ((i & 4) != 0) {
                this.mBottomLeftRoundColor = i2;
                this.mBottomLeftRound.setColorFilter(porterDuffColorFilter);
            }
            if ((i & 8) != 0) {
                this.mBottomRightRoundColor = i2;
                this.mBottomRightRound.setColorFilter(porterDuffColorFilter);
            }
        } else {
            throw new IllegalArgumentException("Use wrong rounded corners to the param, corners = " + i);
        }
    }

    public int getRoundedCornerColor(int i) {
        if (i == 0) {
            throw new IllegalArgumentException("There is no rounded corner on = " + this);
        } else if (i != 1 && i != 2 && i != 4 && i != 8) {
            throw new IllegalArgumentException("Use multiple rounded corner as param on = " + this);
        } else if ((i & 1) != 0) {
            return this.mTopLeftRoundColor;
        } else {
            if ((i & 2) != 0) {
                return this.mTopRightRoundColor;
            }
            if ((i & 4) != 0) {
                return this.mBottomLeftRoundColor;
            }
            return this.mBottomRightRoundColor;
        }
    }

    private void initRoundedCorner() {
        this.mRoundRadius = (int) TypedValue.applyDimension(1, (float) 26, this.mRes.getDisplayMetrics());
        boolean z = !SeslMisc.isLightTheme(this.mContext);
        if (z) {
            this.mIsStrokeRoundedCorner = false;
        }
        Log.d(TAG, "initRoundedCorner, rounded corner with stroke = " + this.mIsStrokeRoundedCorner + ", dark theme = " + z + ", mutate " + this.mIsMutate);
        if (this.mIsStrokeRoundedCorner) {
            int color = this.mRes.getColor(R.color.sesl_round_and_bgcolor_light);
            this.mBottomRightRoundColor = color;
            this.mBottomLeftRoundColor = color;
            this.mTopRightRoundColor = color;
            this.mTopLeftRoundColor = color;
            this.mTopLeftRound = ResourcesCompat.getDrawable(this.mRes, R.drawable.sesl_top_left_round_stroke, this.mContext.getTheme());
            this.mTopRightRound = ResourcesCompat.getDrawable(this.mRes, R.drawable.sesl_top_right_round_stroke, this.mContext.getTheme());
            this.mBottomLeftRound = ResourcesCompat.getDrawable(this.mRes, R.drawable.sesl_bottom_left_round_stroke, this.mContext.getTheme());
            this.mBottomRightRound = ResourcesCompat.getDrawable(this.mRes, R.drawable.sesl_bottom_right_round_stroke, this.mContext.getTheme());
        } else if (this.mIsMutate) {
            int color2 = this.mRes.getColor(R.color.sesl_round_and_bgcolor_dark);
            this.mBottomRightRoundColor = color2;
            this.mBottomLeftRoundColor = color2;
            this.mTopRightRoundColor = color2;
            this.mTopLeftRoundColor = color2;
            this.mTopLeftRound = this.mRes.getDrawable(R.drawable.sesl_top_left_round).mutate();
            this.mTopRightRound = this.mRes.getDrawable(R.drawable.sesl_top_right_round).mutate();
            this.mBottomLeftRound = this.mRes.getDrawable(R.drawable.sesl_bottom_left_round).mutate();
            this.mBottomRightRound = this.mRes.getDrawable(R.drawable.sesl_bottom_right_round).mutate();
        } else {
            int color3 = this.mRes.getColor(R.color.sesl_round_and_bgcolor_dark);
            this.mBottomRightRoundColor = color3;
            this.mBottomLeftRoundColor = color3;
            this.mTopRightRoundColor = color3;
            this.mTopLeftRoundColor = color3;
            this.mTopLeftRound = this.mRes.getDrawable(R.drawable.sesl_top_left_round);
            this.mTopRightRound = this.mRes.getDrawable(R.drawable.sesl_top_right_round);
            this.mBottomLeftRound = this.mRes.getDrawable(R.drawable.sesl_bottom_left_round);
            this.mBottomRightRound = this.mRes.getDrawable(R.drawable.sesl_bottom_right_round);
        }
        Drawable drawable = ResourcesCompat.getDrawable(this.mRes, R.drawable.sesl_round_stroke, this.mContext.getTheme());
        this.mRoundStrokeBottom = drawable;
        this.mRoundStrokeTop = drawable;
        this.mRoundStrokeHeight = this.mRes.getDimensionPixelSize(R.dimen.sesl_round_stroke_height);
    }

    public int getRoundedCornerRadius() {
        return this.mRoundRadius;
    }

    private void removeRoundedCorner(int i) {
        if ((i & 1) != 0) {
            this.mTopLeftRound = null;
        }
        if ((i & 2) != 0) {
            this.mTopRightRound = null;
        }
        if ((i & 4) != 0) {
            this.mBottomLeftRound = null;
        }
        if ((i & 8) != 0) {
            this.mBottomRightRound = null;
        }
    }

    public void drawRoundedCorner(Canvas canvas) {
        canvas.getClipBounds(this.mRoundedCornerBounds);
        if ((this.mRoundedCornerMode & 1) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeTop.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top + this.mRoundStrokeHeight);
                this.mRoundStrokeTop.draw(canvas);
            }
            this.mTopLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.top + this.mRoundRadius);
            this.mTopLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 2) != 0) {
            this.mTopRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top + this.mRoundRadius);
            this.mTopRightRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 4) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeBottom.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.bottom - this.mRoundStrokeHeight, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom);
                this.mRoundStrokeBottom.draw(canvas);
            }
            this.mBottomLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.bottom - this.mRoundRadius, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.bottom);
            this.mBottomLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 8) != 0) {
            this.mBottomRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.bottom - this.mRoundRadius, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom);
            this.mBottomRightRound.draw(canvas);
        }
    }

    public void drawRoundedCorner(View view, Canvas canvas) {
        if (view.getTranslationY() != 0.0f) {
            this.mX = Math.round(view.getX());
            this.mY = Math.round(view.getY());
        } else {
            this.mX = view.getLeft();
            this.mY = view.getTop();
        }
        Rect rect = this.mRoundedCornerBounds;
        int i = this.mX;
        rect.set(i, this.mY, view.getWidth() + i, this.mY + view.getHeight());
        if ((this.mRoundedCornerMode & 1) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeTop.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top + this.mRoundStrokeHeight);
                this.mRoundStrokeTop.draw(canvas);
            }
            this.mTopLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.top + this.mRoundRadius);
            this.mTopLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 2) != 0) {
            this.mTopRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top + this.mRoundRadius);
            this.mTopRightRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 4) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeBottom.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.bottom - this.mRoundStrokeHeight, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom);
                this.mRoundStrokeBottom.draw(canvas);
            }
            this.mBottomLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.bottom - this.mRoundRadius, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.bottom);
            this.mBottomLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 8) != 0) {
            this.mBottomRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.bottom - this.mRoundRadius, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom);
            this.mBottomRightRound.draw(canvas);
        }
    }
}
