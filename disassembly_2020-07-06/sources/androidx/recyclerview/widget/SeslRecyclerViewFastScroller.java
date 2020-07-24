package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.IntProperty;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import androidx.core.math.MathUtils;
import androidx.recyclerview.R;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.reflect.view.SeslHapticFeedbackConstantsReflector;
import androidx.reflect.view.SeslViewGroupReflector;
import androidx.reflect.view.SeslViewReflector;

class SeslRecyclerViewFastScroller {
    private static Property<View, Integer> BOTTOM = new IntProperty<View>("bottom") {
        public void setValue(View view, int i) {
            view.setBottom(i);
        }

        public Integer get(View view) {
            return Integer.valueOf(view.getBottom());
        }
    };
    private static final int DURATION_CROSS_FADE = 0;
    private static final int DURATION_FADE_IN = 167;
    private static final int DURATION_FADE_OUT = 167;
    private static final int DURATION_RESIZE = 100;
    public static final int EFFECT_STATE_CLOSE = 0;
    public static final int EFFECT_STATE_OPEN = 1;
    private static final long FADE_TIMEOUT = 2500;
    private static final int FASTSCROLL_VIBRATE_INDEX = 26;
    private static Property<View, Integer> LEFT = new IntProperty<View>("left") {
        public void setValue(View view, int i) {
            view.setLeft(i);
        }

        public Integer get(View view) {
            return Integer.valueOf(view.getLeft());
        }
    };
    private static final int MIN_PAGES = 1;
    private static final int OVERLAY_ABOVE_THUMB = 2;
    private static final int OVERLAY_AT_THUMB = 1;
    private static final int OVERLAY_FLOATING = 0;
    private static final int PREVIEW_LEFT = 0;
    private static final int PREVIEW_RIGHT = 1;
    private static Property<View, Integer> RIGHT = new IntProperty<View>("right") {
        public void setValue(View view, int i) {
            view.setRight(i);
        }

        public Integer get(View view) {
            return Integer.valueOf(view.getRight());
        }
    };
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_NONE = 0;
    private static final int STATE_VISIBLE = 1;
    private static final String TAG = "SeslFastScroller";
    private static final long TAP_TIMEOUT = ((long) ViewConfiguration.getTapTimeout());
    private static final int THUMB_POSITION_INSIDE = 1;
    private static final int THUMB_POSITION_MIDPOINT = 0;
    private static Property<View, Integer> TOP = new IntProperty<View>("top") {
        public void setValue(View view, int i) {
            view.setTop(i);
        }

        public Integer get(View view) {
            return Integer.valueOf(view.getTop());
        }
    };
    private int mAdditionalBottomPadding;
    private float mAdditionalTouchArea = 0.0f;
    private boolean mAlwaysShow;
    private int mColorPrimary = -1;
    private final Rect mContainerRect = new Rect();
    private Context mContext;
    private int mCurrentSection = -1;
    private AnimatorSet mDecorAnimation;
    private final Runnable mDeferHide = new Runnable() {
        public void run() {
            SeslRecyclerViewFastScroller.this.setState(0);
        }
    };
    private int mEffectState = 0;
    private boolean mEnabled;
    private int mFirstVisibleItem;
    private int mHeaderCount;
    private float mInitialTouchY;
    private boolean mLayoutFromRight;
    private RecyclerView.Adapter mListAdapter;
    private boolean mLongList;
    private boolean mMatchDragPosition;
    private int mOldChildCount;
    private int mOldItemCount;
    private float mOldThumbPosition = -1.0f;
    private int mOrientation;
    private final ViewGroupOverlay mOverlay;
    private int mOverlayPosition;
    private long mPendingDrag = -1;
    private AnimatorSet mPreviewAnimation;
    private final View mPreviewImage;
    private int mPreviewMarginEnd;
    private int mPreviewMinHeight;
    private int mPreviewMinWidth;
    private int mPreviewPadding;
    private final int[] mPreviewResId = new int[2];
    private final TextView mPrimaryText;
    private final RecyclerView mRecyclerView;
    private int mScaledTouchSlop;
    private int mScrollBarStyle;
    private boolean mScrollCompleted;
    private float mScrollY = 0.0f;
    private int mScrollbarPosition = -1;
    private final TextView mSecondaryText;
    private SectionIndexer mSectionIndexer;
    private Object[] mSections;
    private boolean mShowingPreview;
    /* access modifiers changed from: private */
    public boolean mShowingPrimary;
    private int mState;
    private final Animator.AnimatorListener mSwitchPrimaryListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animator) {
            SeslRecyclerViewFastScroller seslRecyclerViewFastScroller = SeslRecyclerViewFastScroller.this;
            boolean unused = seslRecyclerViewFastScroller.mShowingPrimary = !seslRecyclerViewFastScroller.mShowingPrimary;
        }
    };
    private final Rect mTempBounds = new Rect();
    private final Rect mTempMargins = new Rect();
    private int mTextAppearance;
    private ColorStateList mTextColor;
    private float mTextSize;
    private Drawable mThumbDrawable;
    private final ImageView mThumbImage;
    private int mThumbMarginEnd;
    private int mThumbMinHeight;
    private int mThumbMinWidth;
    private float mThumbOffset;
    private int mThumbPosition;
    private float mThumbRange;
    private Drawable mTrackDrawable;
    private final ImageView mTrackImage;
    private int mTrackPadding;
    private boolean mUpdatingLayout;
    private int mVibrateIndex;
    private int mWidth;

    public SeslRecyclerViewFastScroller(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        this.mOldItemCount = recyclerView.getAdapter().getItemCount();
        this.mOldChildCount = recyclerView.getChildCount();
        this.mContext = recyclerView.getContext();
        this.mScaledTouchSlop = ViewConfiguration.get(this.mContext).getScaledTouchSlop();
        this.mScrollBarStyle = recyclerView.getScrollBarStyle();
        this.mScrollCompleted = true;
        this.mState = 1;
        this.mMatchDragPosition = this.mContext.getApplicationInfo().targetSdkVersion >= 11;
        this.mTrackImage = new ImageView(this.mContext);
        this.mTrackImage.setScaleType(ImageView.ScaleType.FIT_XY);
        this.mThumbImage = new ImageView(this.mContext);
        this.mThumbImage.setScaleType(ImageView.ScaleType.FIT_XY);
        this.mPreviewImage = new View(this.mContext);
        this.mPreviewImage.setAlpha(0.0f);
        this.mPrimaryText = createPreviewTextView(this.mContext);
        this.mSecondaryText = createPreviewTextView(this.mContext);
        TypedArray obtainStyledAttributes = this.mContext.getTheme().obtainStyledAttributes((AttributeSet) null, R.styleable.FastScroll, 0, R.style.Widget_RecyclerView_FastScroll);
        this.mOverlayPosition = obtainStyledAttributes.getInt(R.styleable.FastScroll_position, 0);
        this.mPreviewResId[0] = obtainStyledAttributes.getResourceId(R.styleable.FastScroll_backgroundLeft, 0);
        this.mPreviewResId[1] = obtainStyledAttributes.getResourceId(R.styleable.FastScroll_backgroundRight, 0);
        this.mThumbDrawable = obtainStyledAttributes.getDrawable(R.styleable.FastScroll_thumbDrawable);
        this.mTrackDrawable = obtainStyledAttributes.getDrawable(R.styleable.FastScroll_trackDrawable);
        this.mTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.FastScroll_android_textAppearance, 0);
        this.mTextColor = obtainStyledAttributes.getColorStateList(R.styleable.FastScroll_android_textColor);
        this.mTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.FastScroll_android_textSize, 0);
        this.mPreviewMinWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.FastScroll_android_minWidth, 0);
        this.mPreviewMinHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.FastScroll_android_minHeight, 0);
        this.mThumbMinWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.FastScroll_thumbMinWidth, 0);
        this.mThumbMinHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.FastScroll_thumbMinHeight, 0);
        this.mPreviewPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.FastScroll_android_padding, 0);
        this.mThumbPosition = obtainStyledAttributes.getInt(R.styleable.FastScroll_thumbPosition, 0);
        obtainStyledAttributes.recycle();
        updateAppearance();
        ViewGroupOverlay overlay = recyclerView.getOverlay();
        this.mOverlay = overlay;
        overlay.add(this.mTrackImage);
        overlay.add(this.mThumbImage);
        overlay.add(this.mPreviewImage);
        overlay.add(this.mPrimaryText);
        overlay.add(this.mSecondaryText);
        Resources resources = this.mContext.getResources();
        this.mPreviewMarginEnd = resources.getDimensionPixelOffset(R.dimen.sesl_fast_scroll_preview_margin_end);
        this.mThumbMarginEnd = resources.getDimensionPixelOffset(R.dimen.sesl_fast_scroll_thumb_margin_end);
        this.mAdditionalTouchArea = resources.getDimension(R.dimen.sesl_fast_scroll_additional_touch_area);
        this.mTrackPadding = resources.getDimensionPixelOffset(R.dimen.sesl_fast_scroller_track_padding);
        this.mAdditionalBottomPadding = resources.getDimensionPixelOffset(R.dimen.sesl_fast_scroller_additional_bottom_padding);
        TextView textView = this.mPrimaryText;
        int i = this.mPreviewPadding;
        textView.setPadding(i, 0, i, 0);
        TextView textView2 = this.mSecondaryText;
        int i2 = this.mPreviewPadding;
        textView2.setPadding(i2, 0, i2, 0);
        getSectionsFromIndexer();
        updateLongList(this.mOldChildCount, this.mOldItemCount);
        setScrollbarPosition(recyclerView.getVerticalScrollbarPosition());
        postAutoHide();
        this.mVibrateIndex = SeslHapticFeedbackConstantsReflector.semGetVibrationIndex(26);
    }

    private void updateAppearance() {
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        this.mColorPrimary = this.mContext.getResources().getColor(typedValue.resourceId);
        this.mTrackImage.setImageDrawable(this.mTrackDrawable);
        Drawable drawable = this.mTrackDrawable;
        int max = drawable != null ? Math.max(0, drawable.getIntrinsicWidth()) : 0;
        Drawable drawable2 = this.mThumbDrawable;
        if (drawable2 != null) {
            drawable2.setTint(this.mColorPrimary);
        }
        this.mThumbImage.setImageDrawable(this.mThumbDrawable);
        this.mThumbImage.setMinimumWidth(this.mThumbMinWidth);
        this.mThumbImage.setMinimumHeight(this.mThumbMinHeight);
        Drawable drawable3 = this.mThumbDrawable;
        if (drawable3 != null) {
            max = Math.max(max, drawable3.getIntrinsicWidth());
        }
        this.mWidth = Math.max(max, this.mThumbMinWidth);
        this.mPreviewImage.setMinimumWidth(this.mPreviewMinWidth);
        this.mPreviewImage.setMinimumHeight(this.mPreviewMinHeight);
        int i = this.mTextAppearance;
        if (i != 0) {
            this.mPrimaryText.setTextAppearance(this.mContext, i);
            this.mSecondaryText.setTextAppearance(this.mContext, this.mTextAppearance);
        }
        ColorStateList colorStateList = this.mTextColor;
        if (colorStateList != null) {
            this.mPrimaryText.setTextColor(colorStateList);
            this.mSecondaryText.setTextColor(this.mTextColor);
        }
        float f = this.mTextSize;
        if (f > 0.0f) {
            this.mPrimaryText.setTextSize(0, f);
            this.mSecondaryText.setTextSize(0, this.mTextSize);
        }
        int max2 = Math.max(0, this.mPreviewMinHeight);
        this.mPrimaryText.setMinimumWidth(this.mPreviewMinWidth);
        this.mPrimaryText.setMinimumHeight(max2);
        this.mPrimaryText.setIncludeFontPadding(false);
        this.mSecondaryText.setMinimumWidth(this.mPreviewMinWidth);
        this.mSecondaryText.setMinimumHeight(max2);
        this.mSecondaryText.setIncludeFontPadding(false);
        this.mOrientation = this.mContext.getResources().getConfiguration().orientation;
        refreshDrawablePressedState();
    }

    public void remove() {
        this.mOverlay.remove(this.mTrackImage);
        this.mOverlay.remove(this.mThumbImage);
        this.mOverlay.remove(this.mPreviewImage);
        this.mOverlay.remove(this.mPrimaryText);
        this.mOverlay.remove(this.mSecondaryText);
    }

    public void setEnabled(boolean z) {
        Log.d(TAG, "setEnabled() enabled = " + z);
        if (this.mEnabled != z) {
            this.mEnabled = z;
            onStateDependencyChanged(true);
        }
    }

    public boolean isEnabled() {
        if (this.mEnabled && !this.mLongList) {
            this.mLongList = canScrollList(1) || canScrollList(-1);
        }
        if (!this.mEnabled) {
            return false;
        }
        if (this.mLongList || this.mAlwaysShow) {
            return true;
        }
        return false;
    }

    public void setAlwaysShow(boolean z) {
        if (this.mAlwaysShow != z) {
            this.mAlwaysShow = z;
            onStateDependencyChanged(false);
        }
    }

    public boolean isAlwaysShowEnabled() {
        return this.mAlwaysShow;
    }

    private void onStateDependencyChanged(boolean z) {
        if (!isEnabled()) {
            stop();
        } else if (isAlwaysShowEnabled()) {
            setState(1);
        } else if (this.mState == 1) {
            postAutoHide();
        } else if (z) {
            setState(1);
            postAutoHide();
        }
        SeslViewGroupReflector.resolvePadding(this.mRecyclerView);
    }

    public void setScrollBarStyle(int i) {
        if (this.mScrollBarStyle != i) {
            this.mScrollBarStyle = i;
            updateLayout();
        }
    }

    public void stop() {
        setState(0);
    }

    public void setScrollbarPosition(int i) {
        boolean z = true;
        if (i == 0) {
            i = this.mRecyclerView.mLayout.getLayoutDirection() == 1 ? 1 : 2;
        }
        if (this.mScrollbarPosition != i) {
            this.mScrollbarPosition = i;
            if (i == 1) {
                z = false;
            }
            this.mLayoutFromRight = z;
            this.mPreviewImage.setBackgroundResource(this.mPreviewResId[this.mLayoutFromRight]);
            this.mPreviewImage.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
            this.mPreviewImage.getBackground().setTint(this.mColorPrimary);
            updateLayout();
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    /* access modifiers changed from: package-private */
    public int getEffectState() {
        return this.mEffectState;
    }

    /* access modifiers changed from: package-private */
    public float getScrollY() {
        return this.mScrollY;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        updateLayout();
    }

    public void onItemCountChanged(int i, int i2) {
        if (this.mOldItemCount != i2 || this.mOldChildCount != i) {
            this.mOldItemCount = i2;
            this.mOldChildCount = i;
            if ((i2 - i > 0) && this.mState != 2) {
                setThumbPos(getPosFromItemCount(this.mRecyclerView.findFirstVisibleItemPosition(), i, i2));
            }
            updateLongList(i, i2);
        }
    }

    private void updateLongList(int i, int i2) {
        boolean z = i > 0 && (canScrollList(1) || canScrollList(-1));
        if (this.mLongList != z) {
            this.mLongList = z;
            onStateDependencyChanged(true);
        }
    }

    private TextView createPreviewTextView(Context context) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        textView.setGravity(17);
        textView.setAlpha(0.0f);
        textView.setLayoutDirection(this.mRecyclerView.getLayoutDirection());
        return textView;
    }

    public void updateLayout() {
        if (!this.mUpdatingLayout) {
            this.mUpdatingLayout = true;
            updateContainerRect();
            layoutThumb();
            layoutTrack();
            updateOffsetAndRange();
            this.mUpdatingLayout = false;
            Rect rect = this.mTempBounds;
            measurePreview(this.mPrimaryText, rect);
            applyLayout(this.mPrimaryText, rect);
            measurePreview(this.mSecondaryText, rect);
            applyLayout(this.mSecondaryText, rect);
            rect.left -= this.mPreviewImage.getPaddingLeft();
            rect.top -= this.mPreviewImage.getPaddingTop();
            rect.right += this.mPreviewImage.getPaddingRight();
            rect.bottom += this.mPreviewImage.getPaddingBottom();
            applyLayout(this.mPreviewImage, rect);
        }
    }

    private void applyLayout(View view, Rect rect) {
        view.layout(rect.left, rect.top, rect.right, rect.bottom);
        view.setPivotX(this.mLayoutFromRight ? (float) (rect.right - rect.left) : 0.0f);
    }

    private void measurePreview(View view, Rect rect) {
        Rect rect2 = this.mTempMargins;
        rect2.left = this.mPreviewImage.getPaddingLeft();
        rect2.top = this.mPreviewImage.getPaddingTop();
        rect2.right = this.mPreviewImage.getPaddingRight();
        rect2.bottom = this.mPreviewImage.getPaddingBottom();
        if (this.mOverlayPosition == 0) {
            measureFloating(view, rect2, rect);
        } else {
            measureViewToSide(view, this.mThumbImage, rect2, rect);
        }
    }

    private void measureViewToSide(View view, View view2, Rect rect, Rect rect2) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        if (this.mLayoutFromRight) {
            if (view2 == null) {
                i5 = this.mThumbMarginEnd;
            } else {
                i5 = this.mPreviewMarginEnd;
            }
            i2 = i5;
            i = 0;
        } else {
            if (view2 == null) {
                i = this.mThumbMarginEnd;
            } else {
                i = this.mPreviewMarginEnd;
            }
            i2 = 0;
        }
        Rect rect3 = this.mContainerRect;
        int width = rect3.width();
        if (view2 != null) {
            if (this.mLayoutFromRight) {
                width = view2.getLeft();
            } else {
                width -= view2.getRight();
            }
        }
        int max = Math.max(0, rect3.height());
        int max2 = Math.max(0, (width - i) - i2);
        view.measure(View.MeasureSpec.makeMeasureSpec(max2, Integer.MIN_VALUE), SeslViewReflector.SeslMeasureSpecReflector.makeSafeMeasureSpec(View.MeasureSpec.getSize(max), 0));
        int min = Math.min(max2, view.getMeasuredWidth());
        if (this.mLayoutFromRight) {
            i4 = (view2 == null ? rect3.right : view2.getLeft()) - i2;
            i3 = i4 - min;
        } else {
            i3 = i + (view2 == null ? rect3.left : view2.getRight());
            i4 = i3 + min;
        }
        rect2.set(i3, 0, i4, view.getMeasuredHeight() + 0);
    }

    private void measureFloating(View view, Rect rect, Rect rect2) {
        int i;
        int i2;
        int i3;
        if (rect == null) {
            i = 0;
            i3 = 0;
            i2 = 0;
        } else {
            i3 = rect.left;
            i2 = rect.top;
            i = rect.right;
        }
        Rect rect3 = this.mContainerRect;
        int width = rect3.width();
        view.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, (width - i3) - i), Integer.MIN_VALUE), SeslViewReflector.SeslMeasureSpecReflector.makeSafeMeasureSpec(View.MeasureSpec.getSize(Math.max(0, rect3.height())), 0));
        int height = rect3.height();
        int measuredWidth = view.getMeasuredWidth();
        int i4 = (height / 10) + i2 + rect3.top;
        int i5 = ((width - measuredWidth) / 2) + rect3.left;
        rect2.set(i5, i4, measuredWidth + i5, view.getMeasuredHeight() + i4);
    }

    private void updateContainerRect() {
        RecyclerView recyclerView = this.mRecyclerView;
        SeslViewGroupReflector.resolvePadding(recyclerView);
        Rect rect = this.mContainerRect;
        rect.left = 0;
        rect.top = 0;
        rect.right = recyclerView.getWidth();
        rect.bottom = recyclerView.getHeight();
        int i = this.mScrollBarStyle;
        if (i == 16777216 || i == 0) {
            rect.left += recyclerView.getPaddingLeft();
            rect.top += recyclerView.getPaddingTop();
            rect.right -= recyclerView.getPaddingRight();
            rect.bottom -= recyclerView.getPaddingBottom();
            if (i == 16777216) {
                int width = getWidth();
                if (this.mScrollbarPosition == 2) {
                    rect.right += width;
                } else {
                    rect.left -= width;
                }
            }
        }
    }

    private void layoutThumb() {
        Rect rect = this.mTempBounds;
        measureViewToSide(this.mThumbImage, (View) null, (Rect) null, rect);
        applyLayout(this.mThumbImage, rect);
    }

    private void layoutTrack() {
        int i;
        int i2;
        ImageView imageView = this.mTrackImage;
        ImageView imageView2 = this.mThumbImage;
        Rect rect = this.mContainerRect;
        imageView.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, rect.width()), Integer.MIN_VALUE), SeslViewReflector.SeslMeasureSpecReflector.makeSafeMeasureSpec(View.MeasureSpec.getSize(Math.max(0, rect.height())), 0));
        if (this.mThumbPosition == 1) {
            i = rect.top + this.mTrackPadding;
            i2 = (rect.bottom - this.mTrackPadding) - this.mAdditionalBottomPadding;
        } else {
            int height = imageView2.getHeight() / 2;
            i2 = ((rect.bottom - height) - this.mTrackPadding) - this.mAdditionalBottomPadding;
            i = rect.top + height + this.mTrackPadding;
        }
        int measuredWidth = imageView.getMeasuredWidth();
        int left = imageView2.getLeft() + ((imageView2.getWidth() - measuredWidth) / 2);
        imageView.layout(left, i, measuredWidth + left, i2);
    }

    private void updateOffsetAndRange() {
        float f;
        float f2;
        ImageView imageView = this.mTrackImage;
        ImageView imageView2 = this.mThumbImage;
        if (this.mThumbPosition == 1) {
            float height = ((float) imageView2.getHeight()) / 2.0f;
            f = ((float) imageView.getTop()) + height;
            f2 = ((float) imageView.getBottom()) - height;
        } else {
            f = (float) imageView.getTop();
            f2 = (float) imageView.getBottom();
        }
        this.mThumbOffset = f;
        this.mThumbRange = f2 - f;
    }

    /* access modifiers changed from: private */
    public void setState(int i) {
        this.mRecyclerView.removeCallbacks(this.mDeferHide);
        if (this.mAlwaysShow && i == 0) {
            i = 1;
        }
        if (i != this.mState) {
            if (i == 0) {
                transitionToHidden();
            } else if (i == 1) {
                transitionToVisible();
            } else if (i == 2) {
                transitionPreviewLayout(this.mCurrentSection);
            }
            this.mState = i;
            refreshDrawablePressedState();
        }
    }

    private void refreshDrawablePressedState() {
        boolean z = this.mState == 2;
        this.mThumbImage.setPressed(z);
        this.mTrackImage.setPressed(z);
    }

    private void transitionToHidden() {
        int i;
        Log.d(TAG, "transitionToHidden() mState = " + this.mState);
        this.mShowingPreview = false;
        this.mCurrentSection = -1;
        AnimatorSet animatorSet = this.mDecorAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            i = 167;
        } else {
            i = 0;
        }
        Animator duration = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration((long) i);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{duration});
        this.mDecorAnimation.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
        this.mDecorAnimation.start();
    }

    private void transitionToVisible() {
        Log.d(TAG, "transitionToVisible()");
        AnimatorSet animatorSet = this.mDecorAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        Animator duration = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage).setDuration(167);
        Animator duration2 = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration(167);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{duration, duration2});
        this.mDecorAnimation.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
        this.mShowingPreview = false;
        this.mDecorAnimation.start();
    }

    private void transitionToDragging() {
        Log.d(TAG, "transitionToDragging()");
        AnimatorSet animatorSet = this.mDecorAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        Animator duration = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage).setDuration(167);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{duration});
        this.mDecorAnimation.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
        this.mDecorAnimation.start();
        this.mShowingPreview = true;
    }

    private void postAutoHide() {
        this.mRecyclerView.removeCallbacks(this.mDeferHide);
        this.mRecyclerView.postDelayed(this.mDeferHide, FADE_TIMEOUT);
    }

    public boolean canScrollList(int i) {
        int childCount = this.mRecyclerView.getChildCount();
        if (childCount == 0) {
            return false;
        }
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        Rect rect = this.mRecyclerView.mListPadding;
        if (i > 0) {
            int bottom = this.mRecyclerView.getChildAt(childCount - 1).getBottom();
            if (findFirstVisibleItemPosition + childCount < this.mRecyclerView.getAdapter().getItemCount() || bottom > this.mRecyclerView.getHeight() - rect.bottom) {
                return true;
            }
            return false;
        }
        int top = this.mRecyclerView.getChildAt(0).getTop();
        if (findFirstVisibleItemPosition > 0 || top < rect.top) {
            return true;
        }
        return false;
    }

    public void onScroll(int i, int i2, int i3) {
        if (!isEnabled()) {
            setState(0);
            return;
        }
        if ((canScrollList(1) || canScrollList(-1)) && this.mState != 2) {
            float f = this.mOldThumbPosition;
            if (f != -1.0f) {
                setThumbPos(f);
                this.mOldThumbPosition = -1.0f;
            } else {
                setThumbPos(getPosFromItemCount(i, i2, i3));
            }
        }
        this.mScrollCompleted = true;
        if (this.mFirstVisibleItem != i) {
            this.mFirstVisibleItem = i;
            if (this.mState != 2) {
                setState(1);
                postAutoHide();
            }
        }
    }

    private void getSectionsFromIndexer() {
        this.mSectionIndexer = null;
        RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
        if (adapter instanceof SectionIndexer) {
            this.mListAdapter = adapter;
            this.mSectionIndexer = (SectionIndexer) adapter;
            this.mSections = this.mSectionIndexer.getSections();
            return;
        }
        this.mListAdapter = adapter;
        this.mSections = null;
    }

    public void onSectionsChanged() {
        this.mListAdapter = null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005a A[LOOP:1: B:20:0x0050->B:23:0x005a, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0069  */
    private void scrollTo(float f) {
        int i;
        int i2;
        int i3;
        int i4;
        float f2;
        this.mScrollCompleted = false;
        int itemCount = this.mRecyclerView.getAdapter().getItemCount();
        Object[] objArr = this.mSections;
        if (objArr == null) {
            i = 0;
        } else {
            i = objArr.length;
        }
        if (objArr == null || i <= 0) {
            i3 = MathUtils.constrain((int) (((float) itemCount) * f), 0, itemCount - 1);
            i2 = -1;
        } else {
            float f3 = (float) i;
            int i5 = i - 1;
            int constrain = MathUtils.constrain((int) (f * f3), 0, i5);
            int positionForSection = this.mSectionIndexer.getPositionForSection(constrain);
            int i6 = constrain + 1;
            int positionForSection2 = constrain < i5 ? this.mSectionIndexer.getPositionForSection(i6) : itemCount;
            int i7 = constrain;
            int i8 = positionForSection;
            if (positionForSection2 == positionForSection) {
                while (true) {
                    if (i7 > 0) {
                        i7--;
                        i8 = this.mSectionIndexer.getPositionForSection(i7);
                        if (i8 == positionForSection) {
                            if (i7 == 0) {
                                i7 = constrain;
                                i2 = 0;
                                break;
                            }
                        } else {
                            break;
                        }
                    } else {
                        i2 = constrain;
                        i7 = i2;
                        break;
                    }
                }
                i4 = i6 + 1;
                while (i4 < i && this.mSectionIndexer.getPositionForSection(i4) == positionForSection2) {
                    i4++;
                    i6++;
                }
                f2 = ((float) i7) / f3;
                float f4 = ((float) i6) / f3;
                float f5 = itemCount != 0 ? Float.MAX_VALUE : 0.125f / ((float) itemCount);
                if (i7 != constrain || f - f2 >= f5) {
                    i8 += (int) ((((float) (positionForSection2 - i8)) * (f - f2)) / (f4 - f2));
                }
                i3 = MathUtils.constrain(i8, 0, itemCount - 1);
            }
            i2 = i7;
            i4 = i6 + 1;
            while (i4 < i && this.mSectionIndexer.getPositionForSection(i4) == positionForSection2) {
            }
            f2 = ((float) i7) / f3;
            float f42 = ((float) i6) / f3;
            if (itemCount != 0) {
            }
            i8 += (int) ((((float) (positionForSection2 - i8)) * (f - f2)) / (f42 - f2));
            i3 = MathUtils.constrain(i8, 0, itemCount - 1);
        }
        if (this.mRecyclerView.mLayout instanceof LinearLayoutManager) {
            ((LinearLayoutManager) this.mRecyclerView.mLayout).scrollToPositionWithOffset(i3 + this.mHeaderCount, 0);
        } else {
            ((StaggeredGridLayoutManager) this.mRecyclerView.mLayout).seslScrollToPositionWithOffset(i3 + this.mHeaderCount, 0);
        }
        onScroll(this.mRecyclerView.findFirstVisibleItemPosition(), this.mRecyclerView.getChildCount(), this.mRecyclerView.getAdapter().getItemCount());
        if (this.mCurrentSection != i2) {
            this.mRecyclerView.performHapticFeedback(this.mVibrateIndex);
        }
        this.mCurrentSection = i2;
        boolean transitionPreviewLayout = transitionPreviewLayout(i2);
        Log.d(TAG, "scrollTo() called transitionPreviewLayout() sectionIndex =" + i2 + ", position = " + f);
        if (!this.mShowingPreview && transitionPreviewLayout) {
            transitionToDragging();
        } else if (this.mShowingPreview && !transitionPreviewLayout) {
            transitionToVisible();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0009, code lost:
        r13 = r0[r13];
     */
    private boolean transitionPreviewLayout(int i) {
        TextView textView;
        TextView textView2;
        Object obj;
        Object[] objArr = this.mSections;
        String obj2 = (objArr == null || i < 0 || i >= objArr.length || obj == null) ? null : obj.toString();
        Rect rect = this.mTempBounds;
        View view = this.mPreviewImage;
        if (this.mShowingPrimary) {
            textView2 = this.mPrimaryText;
            textView = this.mSecondaryText;
        } else {
            textView2 = this.mSecondaryText;
            textView = this.mPrimaryText;
        }
        textView.setText(obj2);
        measurePreview(textView, rect);
        applyLayout(textView, rect);
        int i2 = this.mState;
        if (i2 == 1) {
            textView2.setText("");
        } else if (i2 == 2 && textView.getText() == textView2.getText()) {
            return !TextUtils.isEmpty(obj2);
        }
        AnimatorSet animatorSet = this.mPreviewAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        Animator duration = animateAlpha(textView, 1.0f).setDuration(0);
        Animator duration2 = animateAlpha(textView2, 0.0f).setDuration(0);
        duration2.addListener(this.mSwitchPrimaryListener);
        rect.left -= view.getPaddingLeft();
        rect.top -= view.getPaddingTop();
        rect.right += view.getPaddingRight();
        rect.bottom += view.getPaddingBottom();
        Animator animateBounds = animateBounds(view, rect);
        animateBounds.setDuration(100);
        this.mPreviewAnimation = new AnimatorSet();
        AnimatorSet.Builder with = this.mPreviewAnimation.play(duration2).with(duration);
        with.with(animateBounds);
        int width = (view.getWidth() - view.getPaddingLeft()) - view.getPaddingRight();
        int width2 = textView.getWidth();
        if (width2 > width) {
            textView.setScaleX(((float) width) / ((float) width2));
            with.with(animateScaleX(textView, 1.0f).setDuration(100));
        } else {
            textView.setScaleX(1.0f);
        }
        int width3 = textView2.getWidth();
        if (width3 > width2) {
            with.with(animateScaleX(textView2, ((float) width2) / ((float) width3)).setDuration(100));
        }
        this.mPreviewAnimation.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
        this.mPreviewAnimation.start();
        return !TextUtils.isEmpty(obj2);
    }

    private void setThumbPos(float f) {
        Rect rect = this.mContainerRect;
        int i = rect.top;
        int i2 = rect.bottom;
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        float f2 = (f * this.mThumbRange) + this.mThumbOffset;
        ImageView imageView = this.mThumbImage;
        imageView.setTranslationY(f2 - (((float) imageView.getHeight()) / 2.0f));
        View view = this.mPreviewImage;
        float height = ((float) view.getHeight()) / 2.0f;
        float constrain = MathUtils.constrain(f2, ((float) i) + height, ((float) i2) - height) - height;
        view.setTranslationY(constrain);
        this.mPrimaryText.setTranslationY(constrain);
        this.mSecondaryText.setTranslationY(constrain);
    }

    private float getPosFromMotionEvent(float f) {
        float f2 = this.mThumbRange;
        if (f2 <= 0.0f) {
            return 0.0f;
        }
        return MathUtils.constrain((f - this.mThumbOffset) / f2, 0.0f, 1.0f);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
        r4 = r9.mSections;
     */
    private float getPosFromItemCount(int i, int i2, int i3) {
        View childAt;
        int i4;
        int i5;
        int i6;
        int i7;
        Object[] objArr;
        SectionIndexer sectionIndexer = this.mSectionIndexer;
        if (sectionIndexer == null || this.mListAdapter == null) {
            getSectionsFromIndexer();
        }
        float f = 0.0f;
        if (i2 == 0 || i3 == 0) {
            return 0.0f;
        }
        boolean z = (sectionIndexer == null || objArr == null || objArr.length <= 0) ? false : true;
        if (z && this.mMatchDragPosition) {
            int i8 = this.mHeaderCount;
            int i9 = i - i8;
            if (i9 < 0) {
                return 0.0f;
            }
            int i10 = i3 - i8;
            View childAt2 = this.mRecyclerView.getChildAt(0);
            float paddingTop = (childAt2 == null || childAt2.getHeight() == 0) ? 0.0f : ((float) (this.mRecyclerView.getPaddingTop() - childAt2.getTop())) / ((float) childAt2.getHeight());
            int sectionForPosition = sectionIndexer.getSectionForPosition(i9);
            int positionForSection = sectionIndexer.getPositionForSection(sectionForPosition);
            int length = this.mSections.length;
            if (sectionForPosition < length - 1) {
                int i11 = sectionForPosition + 1;
                if (i11 < length) {
                    i7 = sectionIndexer.getPositionForSection(i11);
                } else {
                    i7 = i10 - 1;
                }
                i4 = i7 - positionForSection;
            } else {
                i4 = i10 - positionForSection;
            }
            if (i4 != 0) {
                f = ((((float) i9) + paddingTop) - ((float) positionForSection)) / ((float) i4);
            }
            float f2 = (((float) sectionForPosition) + f) / ((float) length);
            if (i9 <= 0 || i9 + i2 != i10) {
                return f2;
            }
            View childAt3 = this.mRecyclerView.getChildAt(i2 - 1);
            int paddingBottom = this.mRecyclerView.getPaddingBottom();
            if (this.mRecyclerView.getClipToPadding()) {
                i5 = childAt3.getHeight();
                i6 = (this.mRecyclerView.getHeight() - paddingBottom) - childAt3.getTop();
            } else {
                i5 = childAt3.getHeight() + paddingBottom;
                i6 = this.mRecyclerView.getHeight() - childAt3.getTop();
            }
            return (i6 <= 0 || i5 <= 0) ? f2 : f2 + ((1.0f - f2) * (((float) i6) / ((float) i5)));
        } else if (i2 != i3) {
            return ((float) i) / ((float) (i3 - i2));
        } else {
            if (!(this.mRecyclerView.mLayout instanceof StaggeredGridLayoutManager) || i == 0 || (childAt = this.mRecyclerView.getChildAt(0)) == null || !((StaggeredGridLayoutManager.LayoutParams) childAt.getLayoutParams()).isFullSpan()) {
                return 0.0f;
            }
            return 1.0f;
        }
    }

    private void cancelFling() {
        MotionEvent obtain = MotionEvent.obtain(0, 0, 3, 0.0f, 0.0f, 0);
        this.mRecyclerView.onTouchEvent(obtain);
        obtain.recycle();
    }

    private void cancelPendingDrag() {
        this.mPendingDrag = -1;
    }

    private void startPendingDrag() {
        this.mPendingDrag = SystemClock.uptimeMillis() + TAP_TIMEOUT;
    }

    private void beginDrag() {
        Log.d(TAG, "beginDrag() !!!");
        this.mPendingDrag = -1;
        if (this.mListAdapter == null) {
            getSectionsFromIndexer();
        }
        this.mRecyclerView.requestDisallowInterceptTouchEvent(true);
        cancelFling();
        setState(2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
        if (r0 != 3) goto L_0x0098;
     */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (!isPointInside(motionEvent.getX(), motionEvent.getY())) {
                        cancelPendingDrag();
                    } else {
                        long j = this.mPendingDrag;
                        if (j >= 0 && j <= SystemClock.uptimeMillis()) {
                            beginDrag();
                            float posFromMotionEvent = getPosFromMotionEvent(this.mInitialTouchY);
                            this.mOldThumbPosition = posFromMotionEvent;
                            scrollTo(posFromMotionEvent);
                            Log.d(TAG, "onInterceptTouchEvent() ACTION_MOVE pendingdrag open()");
                            return onTouchEvent(motionEvent);
                        }
                    }
                }
            }
            cancelPendingDrag();
        } else {
            Log.d(TAG, "onInterceptTouchEvent() ACTION_DOWN ev.getY() = " + motionEvent.getY());
            if (isPointInside(motionEvent.getX(), motionEvent.getY())) {
                this.mRecyclerView.performHapticFeedback(this.mVibrateIndex);
                if (!this.mRecyclerView.isInScrollingContainer()) {
                    return true;
                }
                this.mInitialTouchY = motionEvent.getY();
                startPendingDrag();
            }
        }
        return false;
    }

    public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if ((actionMasked == 9 || actionMasked == 7) && this.mState == 0 && isPointInside(motionEvent.getX(), motionEvent.getY())) {
            setState(1);
            postAutoHide();
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Rect rect = this.mContainerRect;
        int i = rect.top;
        int i2 = rect.bottom;
        ImageView imageView = this.mTrackImage;
        float top = (float) imageView.getTop();
        float bottom = (float) imageView.getBottom();
        this.mScrollY = motionEvent.getY();
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                if (this.mPendingDrag >= 0) {
                    beginDrag();
                    float posFromMotionEvent = getPosFromMotionEvent(motionEvent.getY());
                    this.mOldThumbPosition = posFromMotionEvent;
                    setThumbPos(posFromMotionEvent);
                    scrollTo(posFromMotionEvent);
                    this.mEffectState = 1;
                    Log.d(TAG, "onTouchEvent() ACTION_UP.. open() called with posY " + motionEvent.getY());
                }
                if (this.mState == 2) {
                    this.mRecyclerView.requestDisallowInterceptTouchEvent(false);
                    setState(1);
                    postAutoHide();
                    this.mEffectState = 0;
                    this.mScrollY = 0.0f;
                    return true;
                }
            } else if (actionMasked == 2) {
                Log.d(TAG, "onTouchEvent() ACTION_MOVE.. mState= " + this.mState + ", mInitialTouchY=" + this.mInitialTouchY);
                if (this.mPendingDrag >= 0 && Math.abs(motionEvent.getY() - this.mInitialTouchY) > ((float) this.mScaledTouchSlop)) {
                    beginDrag();
                    float f = this.mScrollY;
                    float f2 = (float) i;
                    if (f > f2 && f < ((float) i2)) {
                        Log.d(TAG, "onTouchEvent() ACTION_MOVE 1 mScrollY=" + this.mScrollY + ", min=" + top + ", max=" + bottom);
                        float f3 = this.mScrollY;
                        float f4 = f2 + top;
                        if (f3 < f4) {
                            this.mScrollY = f4;
                        } else if (f3 > bottom) {
                            this.mScrollY = bottom;
                        }
                        this.mEffectState = 1;
                    }
                }
                if (this.mState == 2) {
                    float posFromMotionEvent2 = getPosFromMotionEvent(motionEvent.getY());
                    this.mOldThumbPosition = posFromMotionEvent2;
                    setThumbPos(posFromMotionEvent2);
                    if (this.mScrollCompleted) {
                        scrollTo(posFromMotionEvent2);
                    }
                    float f5 = this.mScrollY;
                    float f6 = (float) i;
                    if (f5 > f6 && f5 < ((float) i2)) {
                        Log.d(TAG, "onTouchEvent() ACTION_MOVE 2 mScrollY=" + this.mScrollY + ", min=" + top + ", max=" + bottom);
                        float f7 = this.mScrollY;
                        float f8 = f6 + top;
                        if (f7 < f8) {
                            this.mScrollY = f8;
                        } else if (f7 > bottom) {
                            this.mScrollY = bottom;
                        }
                        this.mEffectState = 1;
                    }
                    return true;
                }
            } else if (actionMasked == 3) {
                cancelPendingDrag();
                if (this.mState == 2) {
                    setState(0);
                }
                this.mEffectState = 0;
                this.mScrollY = 0.0f;
            }
        } else if (isPointInside(motionEvent.getX(), motionEvent.getY()) && !this.mRecyclerView.isInScrollingContainer()) {
            beginDrag();
            this.mEffectState = 1;
            Log.d(TAG, "onTouchEvent() ACTION_DOWN.. open() called with posY " + motionEvent.getY());
            return true;
        }
        return false;
    }

    private boolean isPointInside(float f, float f2) {
        return isPointInsideX(f) && isPointInsideY(f2) && this.mState != 0;
    }

    private boolean isPointInsideX(float f) {
        if (this.mLayoutFromRight) {
            if (f >= ((float) this.mThumbImage.getLeft()) - this.mAdditionalTouchArea) {
                return true;
            }
            return false;
        } else if (f <= ((float) this.mThumbImage.getRight()) + this.mAdditionalTouchArea) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPointInsideY(float f) {
        float translationY = this.mThumbImage.getTranslationY();
        return f >= ((float) this.mThumbImage.getTop()) + translationY && f <= ((float) this.mThumbImage.getBottom()) + translationY;
    }

    private static Animator groupAnimatorOfFloat(Property<View, Float> property, float f, View... viewArr) {
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet.Builder builder = null;
        for (int length = viewArr.length - 1; length >= 0; length--) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(viewArr[length], property, new float[]{f});
            if (builder == null) {
                builder = animatorSet.play(ofFloat);
            } else {
                builder.with(ofFloat);
            }
        }
        return animatorSet;
    }

    private static Animator animateScaleX(View view, float f) {
        return ObjectAnimator.ofFloat(view, View.SCALE_X, new float[]{f});
    }

    private static Animator animateAlpha(View view, float f) {
        return ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{f});
    }

    private static Animator animateBounds(View view, Rect rect) {
        return ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofInt(LEFT, new int[]{rect.left}), PropertyValuesHolder.ofInt(TOP, new int[]{rect.top}), PropertyValuesHolder.ofInt(RIGHT, new int[]{rect.right}), PropertyValuesHolder.ofInt(BOTTOM, new int[]{rect.bottom})});
    }
}
