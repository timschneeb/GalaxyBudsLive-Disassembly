package androidx.appcompat.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.core.widget.SeslEdgeEffect;
import androidx.reflect.widget.SeslAbsListViewReflector;
import androidx.reflect.widget.SeslAdapterViewReflector;
import androidx.reflect.widget.SeslTextViewReflector;
import java.lang.ref.WeakReference;

class AlertController {
    ListAdapter mAdapter;
    private int mAlertDialogLayout;
    private final View.OnClickListener mButtonHandler = new View.OnClickListener() {
        public void onClick(View view) {
            Message message;
            if (view == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
                message = Message.obtain(AlertController.this.mButtonPositiveMessage);
            } else if (view != AlertController.this.mButtonNegative || AlertController.this.mButtonNegativeMessage == null) {
                message = (view != AlertController.this.mButtonNeutral || AlertController.this.mButtonNeutralMessage == null) ? null : Message.obtain(AlertController.this.mButtonNeutralMessage);
            } else {
                message = Message.obtain(AlertController.this.mButtonNegativeMessage);
            }
            if (message != null) {
                message.sendToTarget();
            }
            AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialog).sendToTarget();
        }
    };
    private final int mButtonIconDimen;
    Button mButtonNegative;
    private Drawable mButtonNegativeIcon;
    Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    Button mButtonNeutral;
    private Drawable mButtonNeutralIcon;
    Message mButtonNeutralMessage;
    private CharSequence mButtonNeutralText;
    private int mButtonPanelLayoutHint = 0;
    private int mButtonPanelSideLayout;
    Button mButtonPositive;
    private Drawable mButtonPositiveIcon;
    Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    int mCheckedItem = -1;
    /* access modifiers changed from: private */
    public final Context mContext;
    private View mCustomTitleView;
    final AppCompatDialog mDialog;
    Handler mHandler;
    private Drawable mIcon;
    private int mIconId = 0;
    private ImageView mIconView;
    int mLastOrientation;
    int mListItemLayout;
    int mListLayout;
    ListView mListView;
    private CharSequence mMessage;
    private TextView mMessageView;
    int mMultiChoiceItemLayout;
    NestedScrollView mScrollView;
    private boolean mShowTitle;
    int mSingleChoiceItemLayout;
    private CharSequence mTitle;
    private TextView mTitleView;
    private View mView;
    private int mViewLayoutResId;
    private int mViewSpacingBottom;
    private int mViewSpacingLeft;
    private int mViewSpacingRight;
    private boolean mViewSpacingSpecified = false;
    private int mViewSpacingTop;
    private final Window mWindow;

    private static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<>(dialogInterface);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == -3 || i == -2 || i == -1) {
                ((DialogInterface.OnClickListener) message.obj).onClick((DialogInterface) this.mDialog.get(), message.what);
            } else if (i == 1) {
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    private static boolean shouldCenterSingleButton(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.alertDialogCenterButtons, typedValue, true);
        if (typedValue.data != 0) {
            return true;
        }
        return false;
    }

    public AlertController(Context context, AppCompatDialog appCompatDialog, Window window) {
        this.mContext = context;
        this.mDialog = appCompatDialog;
        this.mWindow = window;
        this.mHandler = new ButtonHandler(appCompatDialog);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes((AttributeSet) null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
        this.mAlertDialogLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_android_layout, 0);
        this.mButtonPanelSideLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);
        this.mListLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_listLayout, 0);
        this.mMultiChoiceItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
        this.mSingleChoiceItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
        this.mListItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
        this.mShowTitle = obtainStyledAttributes.getBoolean(R.styleable.AlertDialog_showTitle, true);
        this.mButtonIconDimen = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AlertDialog_buttonIconDimen, 0);
        obtainStyledAttributes.recycle();
        appCompatDialog.supportRequestWindowFeature(1);
    }

    static boolean canTextInput(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (canTextInput(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    public void installContent() {
        this.mDialog.setContentView(selectContentView());
        setupView();
    }

    private int selectContentView() {
        int i = this.mButtonPanelSideLayout;
        if (i == 0) {
            return this.mAlertDialogLayout;
        }
        if (this.mButtonPanelLayoutHint == 1) {
            return i;
        }
        return this.mAlertDialogLayout;
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void setCustomTitle(View view) {
        this.mCustomTitleView = view;
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
        TextView textView = this.mMessageView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void setView(int i) {
        this.mView = null;
        this.mViewLayoutResId = i;
        this.mViewSpacingSpecified = false;
    }

    public void setView(View view) {
        this.mView = view;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = false;
    }

    public void setView(View view, int i, int i2, int i3, int i4) {
        this.mView = view;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = true;
        this.mViewSpacingLeft = i;
        this.mViewSpacingTop = i2;
        this.mViewSpacingRight = i3;
        this.mViewSpacingBottom = i4;
    }

    public void setButtonPanelLayoutHint(int i) {
        this.mButtonPanelLayoutHint = i;
    }

    public void setButton(int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message, Drawable drawable) {
        if (message == null && onClickListener != null) {
            message = this.mHandler.obtainMessage(i, onClickListener);
        }
        if (i == -3) {
            this.mButtonNeutralText = charSequence;
            this.mButtonNeutralMessage = message;
            this.mButtonNeutralIcon = drawable;
        } else if (i == -2) {
            this.mButtonNegativeText = charSequence;
            this.mButtonNegativeMessage = message;
            this.mButtonNegativeIcon = drawable;
        } else if (i == -1) {
            this.mButtonPositiveText = charSequence;
            this.mButtonPositiveMessage = message;
            this.mButtonPositiveIcon = drawable;
        } else {
            throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void setIcon(int i) {
        this.mIcon = null;
        this.mIconId = i;
        ImageView imageView = this.mIconView;
        if (imageView == null) {
            return;
        }
        if (i != 0) {
            imageView.setVisibility(0);
            this.mIconView.setImageResource(this.mIconId);
            return;
        }
        imageView.setVisibility(8);
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
        this.mIconId = 0;
        ImageView imageView = this.mIconView;
        if (imageView == null) {
            return;
        }
        if (drawable != null) {
            imageView.setVisibility(0);
            this.mIconView.setImageDrawable(drawable);
            return;
        }
        imageView.setVisibility(8);
    }

    public int getIconAttributeResId(int i) {
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView getListView() {
        return this.mListView;
    }

    public Button getButton(int i) {
        if (i == -3) {
            return this.mButtonNeutral;
        }
        if (i == -2) {
            return this.mButtonNegative;
        }
        if (i != -1) {
            return null;
        }
        return this.mButtonPositive;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.mScrollView;
        return nestedScrollView != null && nestedScrollView.executeKeyEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.mScrollView;
        return nestedScrollView != null && nestedScrollView.executeKeyEvent(keyEvent);
    }

    private ViewGroup resolvePanel(View view, View view2) {
        if (view == null) {
            if (view2 instanceof ViewStub) {
                view2 = ((ViewStub) view2).inflate();
            }
            return (ViewGroup) view2;
        }
        if (view2 != null) {
            ViewParent parent = view2.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view2);
            }
        }
        if (view instanceof ViewStub) {
            view = ((ViewStub) view).inflate();
        }
        return (ViewGroup) view;
    }

    /* access modifiers changed from: private */
    public void setupPaddings() {
        View findViewById = this.mWindow.findViewById(R.id.parentPanel);
        View findViewById2 = findViewById.findViewById(R.id.title_template);
        View findViewById3 = findViewById.findViewById(R.id.scrollView);
        View findViewById4 = findViewById.findViewById(R.id.buttonPanel);
        Resources resources = this.mContext.getResources();
        ViewGroup viewGroup = (ViewGroup) findViewById.findViewById(R.id.customPanel);
        View findViewById5 = findViewById.findViewById(R.id.topPanel);
        View findViewById6 = findViewById.findViewById(R.id.contentPanel);
        boolean z = true;
        boolean z2 = (viewGroup == null || viewGroup.getVisibility() == 8) ? false : true;
        boolean z3 = (findViewById5 == null || findViewById5.getVisibility() == 8) ? false : true;
        boolean z4 = (findViewById6 == null || findViewById6.getVisibility() == 8) ? false : true;
        View view = this.mCustomTitleView;
        if (view == null || view.getVisibility() == 8) {
            z = false;
        }
        if ((!z2 || z3 || z4) && !z) {
            findViewById.setPadding(0, resources.getDimensionPixelSize(R.dimen.sesl_dialog_title_padding_top), 0, 0);
        } else {
            findViewById.setPadding(0, 0, 0, 0);
        }
        if (findViewById2 != null) {
            if (!z2 || !z3 || z4) {
                findViewById2.setPadding(resources.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0, resources.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), resources.getDimensionPixelSize(R.dimen.sesl_dialog_title_padding_bottom));
            } else {
                findViewById2.setPadding(resources.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0, resources.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0);
            }
        }
        if (findViewById3 != null) {
            findViewById3.setPadding(resources.getDimensionPixelSize(R.dimen.sesl_dialog_body_text_scroll_padding_start), 0, resources.getDimensionPixelSize(R.dimen.sesl_dialog_body_text_scroll_padding_end), resources.getDimensionPixelSize(R.dimen.sesl_dialog_body_text_padding_bottom));
        }
        if (findViewById4 != null) {
            findViewById4.setPadding(resources.getDimensionPixelSize(R.dimen.sesl_dialog_button_bar_padding_horizontal), 0, resources.getDimensionPixelSize(R.dimen.sesl_dialog_button_bar_padding_horizontal), resources.getDimensionPixelSize(R.dimen.sesl_dialog_button_bar_padding_bottom));
        }
    }

    private void setupView() {
        ListAdapter listAdapter;
        NestedScrollView nestedScrollView;
        final View findViewById = this.mWindow.findViewById(R.id.parentPanel);
        findViewById.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                view.post(new Runnable() {
                    public void run() {
                        if (AlertController.this.mContext.getResources().getConfiguration().orientation != AlertController.this.mLastOrientation) {
                            AlertController.this.setupPaddings();
                            findViewById.requestLayout();
                        }
                        AlertController.this.mLastOrientation = AlertController.this.mContext.getResources().getConfiguration().orientation;
                    }
                });
            }
        });
        View findViewById2 = findViewById.findViewById(R.id.topPanel);
        View findViewById3 = findViewById.findViewById(R.id.contentPanel);
        View findViewById4 = findViewById.findViewById(R.id.buttonPanel);
        ViewGroup viewGroup = (ViewGroup) findViewById.findViewById(R.id.customPanel);
        setupCustomContent(viewGroup);
        View findViewById5 = viewGroup.findViewById(R.id.topPanel);
        View findViewById6 = viewGroup.findViewById(R.id.contentPanel);
        View findViewById7 = viewGroup.findViewById(R.id.buttonPanel);
        ViewGroup resolvePanel = resolvePanel(findViewById5, findViewById2);
        ViewGroup resolvePanel2 = resolvePanel(findViewById6, findViewById3);
        ViewGroup resolvePanel3 = resolvePanel(findViewById7, findViewById4);
        setupContent(resolvePanel2);
        setupButtons(resolvePanel3);
        setupTitle(resolvePanel);
        boolean z = (viewGroup == null || viewGroup.getVisibility() == 8) ? false : true;
        boolean z2 = (resolvePanel == null || resolvePanel.getVisibility() == 8) ? false : true;
        boolean z3 = (resolvePanel3 == null || resolvePanel3.getVisibility() == 8) ? false : true;
        boolean z4 = (findViewById2 == null || findViewById2.getVisibility() == 8) ? false : true;
        boolean z5 = (findViewById3 == null || findViewById3.getVisibility() == 8) ? false : true;
        View view = this.mCustomTitleView;
        boolean z6 = (view == null || view.getVisibility() == 8) ? false : true;
        if ((z && !z4 && !z5) || z6) {
            adjustParentPanelPadding(findViewById);
        }
        if (z && z4 && !z5) {
            adjustTopPanelPadding(findViewById);
        }
        adjustButtonsPadding();
        if (z2 && (nestedScrollView = this.mScrollView) != null) {
            nestedScrollView.setClipToPadding(true);
        }
        ListView listView = this.mListView;
        if (listView instanceof RecycleListView) {
            ((RecycleListView) listView).setHasDecor(z2, z3);
        }
        if (!z) {
            View view2 = this.mListView;
            if (view2 == null) {
                view2 = this.mScrollView;
            }
            if (view2 != null) {
                setScrollIndicators(resolvePanel2, view2, (z3 ? (char) 2 : 0) | z2 ? 1 : 0, 3);
            }
        }
        ListView listView2 = this.mListView;
        if (listView2 != null && (listAdapter = this.mAdapter) != null) {
            listView2.setAdapter(listAdapter);
            SeslAdapterViewReflector.semSetBottomColor(listView2, 0);
            int i = this.mCheckedItem;
            if (i > -1) {
                listView2.setItemChecked(i, true);
                listView2.setSelectionFromTop(i, this.mContext.getResources().getDimensionPixelSize(R.dimen.sesl_select_dialog_padding_top));
            }
        }
    }

    private void setScrollIndicators(ViewGroup viewGroup, View view, int i, int i2) {
        final View findViewById = this.mWindow.findViewById(R.id.scrollIndicatorUp);
        View findViewById2 = this.mWindow.findViewById(R.id.scrollIndicatorDown);
        if (Build.VERSION.SDK_INT >= 23) {
            ViewCompat.setScrollIndicators(view, i, i2);
            if (findViewById != null) {
                viewGroup.removeView(findViewById);
            }
            if (findViewById2 != null) {
                viewGroup.removeView(findViewById2);
                return;
            }
            return;
        }
        final View view2 = null;
        if (findViewById != null && (i & 1) == 0) {
            viewGroup.removeView(findViewById);
            findViewById = null;
        }
        if (findViewById2 == null || (i & 2) != 0) {
            view2 = findViewById2;
        } else {
            viewGroup.removeView(findViewById2);
        }
        if (findViewById != null || view2 != null) {
            if (this.mMessage != null) {
                this.mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    public void onScrollChange(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4) {
                        AlertController.manageScrollIndicators(nestedScrollView, findViewById, view2);
                    }
                });
                this.mScrollView.post(new Runnable() {
                    public void run() {
                        AlertController.manageScrollIndicators(AlertController.this.mScrollView, findViewById, view2);
                    }
                });
                return;
            }
            ListView listView = this.mListView;
            if (listView != null) {
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                    }

                    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                        AlertController.manageScrollIndicators(absListView, findViewById, view2);
                    }
                });
                this.mListView.post(new Runnable() {
                    public void run() {
                        AlertController.manageScrollIndicators(AlertController.this.mListView, findViewById, view2);
                    }
                });
                return;
            }
            if (findViewById != null) {
                viewGroup.removeView(findViewById);
            }
            if (view2 != null) {
                viewGroup.removeView(view2);
            }
        }
    }

    private void setupCustomContent(ViewGroup viewGroup) {
        View view = this.mView;
        boolean z = false;
        if (view == null) {
            view = this.mViewLayoutResId != 0 ? LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, viewGroup, false) : null;
        }
        if (view != null) {
            z = true;
        }
        if (!z || !canTextInput(view)) {
            this.mWindow.setFlags(131072, 131072);
        }
        if (z) {
            FrameLayout frameLayout = (FrameLayout) this.mWindow.findViewById(R.id.custom);
            frameLayout.addView(view, new ViewGroup.LayoutParams(-1, -1));
            if (this.mViewSpacingSpecified) {
                frameLayout.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            }
            if (this.mListView == null) {
                return;
            }
            if (viewGroup.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) viewGroup.getLayoutParams()).weight = 0.0f;
            } else {
                ((LinearLayoutCompat.LayoutParams) viewGroup.getLayoutParams()).weight = 0.0f;
            }
        } else {
            viewGroup.setVisibility(8);
        }
    }

    private void adjustButtonsPadding() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.sesl_dialog_button_text_size);
        if (this.mButtonPositive.getVisibility() != 8) {
            this.mButtonPositive.setTextSize(0, (float) dimensionPixelSize);
            checkMaxFontScale(this.mButtonPositive, dimensionPixelSize);
        }
        if (this.mButtonNegative.getVisibility() != 8) {
            this.mButtonNegative.setTextSize(0, (float) dimensionPixelSize);
            checkMaxFontScale(this.mButtonNegative, dimensionPixelSize);
        }
        if (this.mButtonNeutral.getVisibility() != 8) {
            this.mButtonNeutral.setTextSize(0, (float) dimensionPixelSize);
            checkMaxFontScale(this.mButtonNeutral, dimensionPixelSize);
        }
    }

    private void adjustParentPanelPadding(View view) {
        view.setPadding(0, 0, 0, 0);
    }

    private void adjustTopPanelPadding(View view) {
        View findViewById = view.findViewById(R.id.title_template);
        Resources resources = this.mContext.getResources();
        findViewById.setPadding(resources.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0, resources.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0);
    }

    private void setupTitle(ViewGroup viewGroup) {
        if (this.mCustomTitleView != null) {
            viewGroup.addView(this.mCustomTitleView, 0, new ViewGroup.LayoutParams(-1, -2));
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            return;
        }
        this.mIconView = (ImageView) this.mWindow.findViewById(16908294);
        if (!(!TextUtils.isEmpty(this.mTitle)) || !this.mShowTitle) {
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            viewGroup.setVisibility(8);
            return;
        }
        this.mTitleView = (TextView) this.mWindow.findViewById(R.id.alertTitle);
        this.mTitleView.setText(this.mTitle);
        checkMaxFontScale(this.mTitleView, this.mContext.getResources().getDimensionPixelSize(R.dimen.sesl_dialog_title_text_size));
        int i = this.mIconId;
        if (i != 0) {
            this.mIconView.setImageResource(i);
            return;
        }
        Drawable drawable = this.mIcon;
        if (drawable != null) {
            this.mIconView.setImageDrawable(drawable);
            return;
        }
        this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
        this.mIconView.setVisibility(8);
    }

    private void setupContent(ViewGroup viewGroup) {
        this.mScrollView = (NestedScrollView) this.mWindow.findViewById(R.id.scrollView);
        this.mScrollView.setFocusable(false);
        this.mScrollView.setNestedScrollingEnabled(false);
        this.mMessageView = (TextView) viewGroup.findViewById(16908299);
        TextView textView = this.mMessageView;
        if (textView != null) {
            CharSequence charSequence = this.mMessage;
            if (charSequence != null) {
                textView.setText(charSequence);
                return;
            }
            textView.setVisibility(8);
            this.mScrollView.removeView(this.mMessageView);
            if (this.mListView != null) {
                ViewGroup viewGroup2 = (ViewGroup) this.mScrollView.getParent();
                int indexOfChild = viewGroup2.indexOfChild(this.mScrollView);
                viewGroup2.removeViewAt(indexOfChild);
                viewGroup2.addView(this.mListView, indexOfChild, new ViewGroup.LayoutParams(-1, -1));
                return;
            }
            viewGroup.setVisibility(8);
        }
    }

    static void manageScrollIndicators(View view, View view2, View view3) {
        int i = 0;
        if (view2 != null) {
            view2.setVisibility(view.canScrollVertically(-1) ? 0 : 4);
        }
        if (view3 != null) {
            if (!view.canScrollVertically(1)) {
                i = 4;
            }
            view3.setVisibility(i);
        }
    }

    private void setupButtons(ViewGroup viewGroup) {
        boolean z;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        boolean z2 = true;
        boolean z3 = contentResolver != null && Settings.System.getInt(contentResolver, "show_button_background", 0) == 1;
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(16842801, typedValue, true);
        int i = -1;
        if (typedValue.resourceId > 0) {
            i = this.mContext.getResources().getColor(typedValue.resourceId);
        }
        this.mButtonPositive = (Button) viewGroup.findViewById(R.id.button1);
        this.mButtonPositive.setOnClickListener(this.mButtonHandler);
        if (Build.VERSION.SDK_INT > 26) {
            if (typedValue.resourceId > 0) {
                SeslTextViewReflector.semSetButtonShapeEnabled(this.mButtonPositive, z3, i);
            } else {
                SeslTextViewReflector.semSetButtonShapeEnabled(this.mButtonPositive, z3);
            }
        } else if (z3) {
            this.mButtonPositive.setBackgroundResource(R.drawable.sesl_dialog_show_button_background);
        }
        if (!TextUtils.isEmpty(this.mButtonPositiveText) || this.mButtonPositiveIcon != null) {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            Drawable drawable = this.mButtonPositiveIcon;
            if (drawable != null) {
                int i2 = this.mButtonIconDimen;
                drawable.setBounds(0, 0, i2, i2);
                this.mButtonPositive.setCompoundDrawables(this.mButtonPositiveIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            }
            this.mButtonPositive.setVisibility(0);
            z = true;
        } else {
            this.mButtonPositive.setVisibility(8);
            z = false;
        }
        this.mButtonNegative = (Button) viewGroup.findViewById(R.id.button2);
        this.mButtonNegative.setOnClickListener(this.mButtonHandler);
        if (Build.VERSION.SDK_INT > 26) {
            if (typedValue.resourceId > 0) {
                SeslTextViewReflector.semSetButtonShapeEnabled(this.mButtonNegative, z3, i);
            } else {
                SeslTextViewReflector.semSetButtonShapeEnabled(this.mButtonNegative, z3);
            }
        } else if (z3) {
            this.mButtonNegative.setBackgroundResource(R.drawable.sesl_dialog_show_button_background);
        }
        if (!TextUtils.isEmpty(this.mButtonNegativeText) || this.mButtonNegativeIcon != null) {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            Drawable drawable2 = this.mButtonNegativeIcon;
            if (drawable2 != null) {
                int i3 = this.mButtonIconDimen;
                drawable2.setBounds(0, 0, i3, i3);
                this.mButtonNegative.setCompoundDrawables(this.mButtonNegativeIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            }
            this.mButtonNegative.setVisibility(0);
            z |= true;
        } else {
            this.mButtonNegative.setVisibility(8);
        }
        this.mButtonNeutral = (Button) viewGroup.findViewById(R.id.button3);
        this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
        if (Build.VERSION.SDK_INT > 26) {
            if (typedValue.resourceId > 0) {
                SeslTextViewReflector.semSetButtonShapeEnabled(this.mButtonNeutral, z3, i);
            } else {
                SeslTextViewReflector.semSetButtonShapeEnabled(this.mButtonNeutral, z3);
            }
        } else if (z3) {
            this.mButtonNeutral.setBackgroundResource(R.drawable.sesl_dialog_show_button_background);
        }
        if (!TextUtils.isEmpty(this.mButtonNeutralText) || this.mButtonNeutralIcon != null) {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            Drawable drawable3 = this.mButtonPositiveIcon;
            if (drawable3 != null) {
                int i4 = this.mButtonIconDimen;
                drawable3.setBounds(0, 0, i4, i4);
                this.mButtonPositive.setCompoundDrawables(this.mButtonPositiveIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            }
            this.mButtonNeutral.setVisibility(0);
            z |= true;
        } else {
            this.mButtonNeutral.setVisibility(8);
        }
        if (shouldCenterSingleButton(this.mContext)) {
            if (z) {
                centerButton(this.mButtonPositive);
            } else if (z) {
                centerButton(this.mButtonNegative);
            } else if (z) {
                centerButton(this.mButtonNeutral);
            }
        }
        if (!(z)) {
            viewGroup.setVisibility(8);
        }
        boolean z4 = this.mButtonNeutral.getVisibility() == 0;
        boolean z5 = this.mButtonPositive.getVisibility() == 0;
        if (this.mButtonNegative.getVisibility() != 0) {
            z2 = false;
        }
        View findViewById = this.mWindow.findViewById(R.id.sem_divider2);
        if (findViewById != null && ((z4 && z5) || (z4 && z2))) {
            findViewById.setVisibility(0);
        }
        View findViewById2 = this.mWindow.findViewById(R.id.sem_divider1);
        if (findViewById2 != null && z5 && z2) {
            findViewById2.setVisibility(0);
        }
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams(layoutParams);
    }

    public static class RecycleListView extends ListView {
        private final int mPaddingBottomNoButtons;
        private final int mPaddingTopNoTitle;

        public RecycleListView(Context context) {
            this(context, (AttributeSet) null);
        }

        public RecycleListView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RecycleListView);
            this.mPaddingBottomNoButtons = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.RecycleListView_paddingBottomNoButtons, -1);
            this.mPaddingTopNoTitle = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.RecycleListView_paddingTopNoTitle, -1);
        }

        public void setHasDecor(boolean z, boolean z2) {
            if (!z2 || !z) {
                setPadding(getPaddingLeft(), z ? getPaddingTop() : this.mPaddingTopNoTitle, getPaddingRight(), z2 ? getPaddingBottom() : this.mPaddingBottomNoButtons);
            }
        }

        public void setOverScrollMode(int i) {
            if (i == 2) {
                SeslAbsListViewReflector.setField_mEdgeGlowTop(this, (EdgeEffect) null);
                SeslAbsListViewReflector.setField_mEdgeGlowBottom(this, (EdgeEffect) null);
            } else if (SeslAbsListViewReflector.getField_mEdgeGlowTop(this) == null) {
                Context context = getContext();
                SeslEdgeEffect seslEdgeEffect = new SeslEdgeEffect(context);
                SeslEdgeEffect seslEdgeEffect2 = new SeslEdgeEffect(context);
                seslEdgeEffect.setSeslHostView(this);
                seslEdgeEffect2.setSeslHostView(this);
                SeslAbsListViewReflector.setField_mEdgeGlowTop(this, seslEdgeEffect);
                SeslAbsListViewReflector.setField_mEdgeGlowBottom(this, seslEdgeEffect2);
            }
            super.setOverScrollMode(i);
        }
    }

    private void checkMaxFontScale(TextView textView, int i) {
        float f = this.mContext.getResources().getConfiguration().fontScale;
        if (f > 1.3f) {
            textView.setTextSize(0, (((float) i) / f) * 1.3f);
        }
    }

    public static class AlertParams {
        public ListAdapter mAdapter;
        public boolean mCancelable;
        public int mCheckedItem = -1;
        public boolean[] mCheckedItems;
        public final Context mContext;
        public Cursor mCursor;
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        public Drawable mIcon;
        public int mIconAttrId = 0;
        public int mIconId = 0;
        public final LayoutInflater mInflater;
        public String mIsCheckedColumn;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public CharSequence[] mItems;
        public String mLabelColumn;
        public CharSequence mMessage;
        public Drawable mNegativeButtonIcon;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public Drawable mNeutralButtonIcon;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public DialogInterface.OnClickListener mOnClickListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public Drawable mPositiveButtonIcon;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public boolean mRecycleOnMeasure = true;
        public CharSequence mTitle;
        public View mView;
        public int mViewLayoutResId;
        public int mViewSpacingBottom;
        public int mViewSpacingLeft;
        public int mViewSpacingRight;
        public boolean mViewSpacingSpecified = false;
        public int mViewSpacingTop;

        public interface OnPrepareListViewListener {
            void onPrepareListView(ListView listView);
        }

        public AlertParams(Context context) {
            this.mContext = context;
            this.mCancelable = true;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public void apply(AlertController alertController) {
            View view = this.mCustomTitleView;
            if (view != null) {
                alertController.setCustomTitle(view);
            } else {
                CharSequence charSequence = this.mTitle;
                if (charSequence != null) {
                    alertController.setTitle(charSequence);
                }
                Drawable drawable = this.mIcon;
                if (drawable != null) {
                    alertController.setIcon(drawable);
                }
                int i = this.mIconId;
                if (i != 0) {
                    alertController.setIcon(i);
                }
                int i2 = this.mIconAttrId;
                if (i2 != 0) {
                    alertController.setIcon(alertController.getIconAttributeResId(i2));
                }
            }
            CharSequence charSequence2 = this.mMessage;
            if (charSequence2 != null) {
                alertController.setMessage(charSequence2);
            }
            if (!(this.mPositiveButtonText == null && this.mPositiveButtonIcon == null)) {
                alertController.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, (Message) null, this.mPositiveButtonIcon);
            }
            if (!(this.mNegativeButtonText == null && this.mNegativeButtonIcon == null)) {
                alertController.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, (Message) null, this.mNegativeButtonIcon);
            }
            if (!(this.mNeutralButtonText == null && this.mNeutralButtonIcon == null)) {
                alertController.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, (Message) null, this.mNeutralButtonIcon);
            }
            if (!(this.mItems == null && this.mCursor == null && this.mAdapter == null)) {
                createListView(alertController);
            }
            View view2 = this.mView;
            if (view2 == null) {
                int i3 = this.mViewLayoutResId;
                if (i3 != 0) {
                    alertController.setView(i3);
                }
            } else if (this.mViewSpacingSpecified) {
                alertController.setView(view2, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            } else {
                alertController.setView(view2);
            }
        }

        /* JADX WARNING: type inference failed for: r9v0, types: [android.widget.ListAdapter] */
        /* JADX WARNING: type inference failed for: r9v3 */
        /* JADX WARNING: type inference failed for: r9v4 */
        /* JADX WARNING: type inference failed for: r2v6, types: [android.widget.SimpleCursorAdapter] */
        /* JADX WARNING: type inference failed for: r1v19, types: [androidx.appcompat.app.AlertController$AlertParams$2] */
        /* JADX WARNING: type inference failed for: r1v20, types: [androidx.appcompat.app.AlertController$AlertParams$1] */
        /* JADX WARNING: Multi-variable type inference failed */
        private void createListView(final AlertController alertController) {
            ? r9;
            int i;
            final RecycleListView recycleListView = (RecycleListView) this.mInflater.inflate(alertController.mListLayout, (ViewGroup) null);
            if (this.mIsMultiChoice) {
                Cursor cursor = this.mCursor;
                if (cursor == null) {
                    final RecycleListView recycleListView2 = recycleListView;
                    r9 = new ArrayAdapter<CharSequence>(this.mContext, alertController.mMultiChoiceItemLayout, 16908308, this.mItems) {
                        public View getView(int i, View view, ViewGroup viewGroup) {
                            View view2 = super.getView(i, view, viewGroup);
                            if (AlertParams.this.mCheckedItems != null && AlertParams.this.mCheckedItems[i]) {
                                recycleListView2.setItemChecked(i, true);
                            }
                            return view2;
                        }
                    };
                } else {
                    final RecycleListView recycleListView3 = recycleListView;
                    final AlertController alertController2 = alertController;
                    r9 = new CursorAdapter(this.mContext, cursor, false) {
                        private final int mIsCheckedIndex;
                        private final int mLabelIndex;

                        {
                            Cursor cursor = getCursor();
                            this.mLabelIndex = cursor.getColumnIndexOrThrow(AlertParams.this.mLabelColumn);
                            this.mIsCheckedIndex = cursor.getColumnIndexOrThrow(AlertParams.this.mIsCheckedColumn);
                        }

                        public void bindView(View view, Context context, Cursor cursor) {
                            ((CheckedTextView) view.findViewById(16908308)).setText(cursor.getString(this.mLabelIndex));
                            RecycleListView recycleListView = recycleListView3;
                            int position = cursor.getPosition();
                            boolean z = true;
                            if (cursor.getInt(this.mIsCheckedIndex) != 1) {
                                z = false;
                            }
                            recycleListView.setItemChecked(position, z);
                        }

                        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                            return AlertParams.this.mInflater.inflate(alertController2.mMultiChoiceItemLayout, viewGroup, false);
                        }
                    };
                }
            } else {
                if (this.mIsSingleChoice) {
                    i = alertController.mSingleChoiceItemLayout;
                } else {
                    i = alertController.mListItemLayout;
                }
                int i2 = i;
                Cursor cursor2 = this.mCursor;
                if (cursor2 != null) {
                    r9 = new SimpleCursorAdapter(this.mContext, i2, cursor2, new String[]{this.mLabelColumn}, new int[]{16908308});
                } else {
                    ListAdapter listAdapter = this.mAdapter;
                    r9 = listAdapter;
                    if (listAdapter == null) {
                        r9 = new CheckedItemAdapter(this.mContext, i2, 16908308, this.mItems);
                    }
                }
            }
            OnPrepareListViewListener onPrepareListViewListener = this.mOnPrepareListViewListener;
            if (onPrepareListViewListener != null) {
                onPrepareListViewListener.onPrepareListView(recycleListView);
            }
            alertController.mAdapter = r9;
            alertController.mCheckedItem = this.mCheckedItem;
            if (this.mOnClickListener != null) {
                recycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        AlertParams.this.mOnClickListener.onClick(alertController.mDialog, i);
                        if (!AlertParams.this.mIsSingleChoice) {
                            alertController.mDialog.dismiss();
                        }
                    }
                });
            } else if (this.mOnCheckboxClickListener != null) {
                recycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        if (AlertParams.this.mCheckedItems != null) {
                            AlertParams.this.mCheckedItems[i] = recycleListView.isItemChecked(i);
                        }
                        AlertParams.this.mOnCheckboxClickListener.onClick(alertController.mDialog, i, recycleListView.isItemChecked(i));
                    }
                });
            }
            AdapterView.OnItemSelectedListener onItemSelectedListener = this.mOnItemSelectedListener;
            if (onItemSelectedListener != null) {
                recycleListView.setOnItemSelectedListener(onItemSelectedListener);
            }
            if (this.mIsSingleChoice) {
                recycleListView.setChoiceMode(1);
            } else if (this.mIsMultiChoice) {
                recycleListView.setChoiceMode(2);
            }
            alertController.mListView = recycleListView;
        }
    }

    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
        public long getItemId(int i) {
            return (long) i;
        }

        public boolean hasStableIds() {
            return true;
        }

        public CheckedItemAdapter(Context context, int i, int i2, CharSequence[] charSequenceArr) {
            super(context, i, i2, charSequenceArr);
        }
    }

    public static class ButtonBarLayout extends LinearLayout {
        public ButtonBarLayout(Context context, AttributeSet attributeSet, int i, int i2) {
            super(context, attributeSet, i, i2);
        }

        public ButtonBarLayout(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
        }

        public ButtonBarLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public ButtonBarLayout(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int childCount = getChildCount();
            int size = (View.MeasureSpec.getSize(i) - getPaddingRight()) - getPaddingLeft();
            int size2 = (View.MeasureSpec.getSize(i2) - getPaddingTop()) - getPaddingBottom();
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < childCount; i6++) {
                View childAt = getChildAt(i6);
                if (childAt.getVisibility() != 8 && (childAt instanceof Button)) {
                    i4 += childAt.getMeasuredWidth();
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE));
                    i5 += childAt.getMeasuredWidth();
                    i3++;
                }
            }
            boolean z = true;
            int i7 = i3 - 1;
            int i8 = i7 > 0 ? (int) (((float) i7) * getContext().getResources().getDisplayMetrics().density) : 0;
            if (i4 < i5 || size <= i5 + i8) {
                if (getOrientation() != 1) {
                    setOrientation(1);
                    for (int i9 = 0; i9 < childCount; i9++) {
                        View childAt2 = getChildAt(i9);
                        if (childAt2.getVisibility() != 8 && !(childAt2 instanceof Button)) {
                            childAt2.setVisibility(8);
                        }
                    }
                    setGravity(17);
                }
            } else if (getOrientation() != 0) {
                setOrientation(0);
                boolean z2 = findViewById(R.id.button1).getVisibility() == 0;
                boolean z3 = findViewById(R.id.button2).getVisibility() == 0;
                if (findViewById(R.id.button3).getVisibility() != 0) {
                    z = false;
                }
                View findViewById = findViewById(R.id.sem_divider1);
                View findViewById2 = findViewById(R.id.sem_divider2);
                if (findViewById2 != null && ((z && z2) || (z && z3))) {
                    findViewById2.setVisibility(0);
                }
                if (findViewById != null && z2 && z3) {
                    findViewById.setVisibility(0);
                }
            }
            super.onMeasure(i, i2);
        }
    }
}
