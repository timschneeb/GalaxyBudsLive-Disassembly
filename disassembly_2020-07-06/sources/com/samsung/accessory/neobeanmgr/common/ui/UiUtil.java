package com.samsung.accessory.neobeanmgr.common.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import java.text.SimpleDateFormat;

public class UiUtil {
    private static final String TAG = "NeoBean_UiUtil";
    static final int TAG_ANIMATE_ALPHA_KEY = "setAnimateAlpha".hashCode();

    public static int caleRtlIndex(int i, int i2) {
        return (i2 - 1) - i;
    }

    public static void setEnabledWithChildren(View view, boolean z) {
        view.setEnabled(z);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setEnabledWithChildren(viewGroup.getChildAt(i), z);
            }
        }
    }

    public static void setAnimateAlpha(View view, float f) {
        if (view.getTag(TAG_ANIMATE_ALPHA_KEY) == null || ((Float) view.getTag(TAG_ANIMATE_ALPHA_KEY)).floatValue() != f) {
            view.setTag(TAG_ANIMATE_ALPHA_KEY, Float.valueOf(f));
            view.animate().setDuration(300).alpha(f).start();
        }
    }

    public static void setAnimateAlphaDirectly(View view, float f) {
        view.clearAnimation();
        view.setTag(TAG_ANIMATE_ALPHA_KEY, Float.valueOf(f));
        view.setAlpha(f);
    }

    public static void setAnimateAlpha(View view, float f, boolean z) {
        if (z) {
            setAnimateAlphaDirectly(view, f);
        } else {
            setAnimateAlpha(view, f);
        }
    }

    public static float DP_TO_PX(float f) {
        return TypedValue.applyDimension(1, f, Application.getContext().getResources().getDisplayMetrics());
    }

    public static float SP_TO_PX(float f) {
        return TypedValue.applyDimension(2, f, Application.getContext().getResources().getDisplayMetrics());
    }

    public static boolean isLayoutRtl(View view) {
        return view.getLayoutDirection() == 1;
    }

    public static boolean isSystemRtl() {
        return Application.getContext().getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public static int rtlCompatIndex(int i, int i2) {
        return isSystemRtl() ? caleRtlIndex(i, i2) : i;
    }

    public static void setDimColorFilter(ImageView imageView) {
        imageView.setColorFilter(-9276814, PorterDuff.Mode.SRC_IN);
    }

    public static void awakeScrollbarWidthChildView(View view) {
        if (view instanceof CanAwakeNestedScrollView) {
            ((CanAwakeNestedScrollView) view).awakenScrollBar();
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                awakeScrollbarWidthChildView(viewGroup.getChildAt(i));
            }
        }
    }

    public static StringBuilder getAllTextWithChildView(View view) {
        StringBuilder sb = new StringBuilder();
        if (view instanceof TextView) {
            sb.append(((TextView) view).getText());
            sb.append("\n");
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                sb.append(getAllTextWithChildView(viewGroup.getChildAt(i)));
            }
        }
        return sb;
    }

    public static boolean isOnUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void startWebBrowser(Activity activity, String str) {
        Log.e(TAG, "startWebBrowser() : " + str);
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        } catch (ActivityNotFoundException unused) {
            Log.e(TAG, "startWebBrowser() : ActivityNotFoundException");
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage((int) R.string.unable_to_start_browser);
            builder.setPositiveButton((CharSequence) activity.getString(R.string.ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }

    public static String MillisToString(long j) {
        return String.valueOf(j) + " (" + new SimpleDateFormat().format(Long.valueOf(j)) + ")";
    }
}
