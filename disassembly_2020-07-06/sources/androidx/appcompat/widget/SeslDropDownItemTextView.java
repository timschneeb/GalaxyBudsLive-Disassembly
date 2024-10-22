package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.R;
import androidx.appcompat.util.SeslMisc;

public class SeslDropDownItemTextView extends SeslCheckedTextView {
    private static final String TAG = "SeslDropDownItemTextView";

    public SeslDropDownItemTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SeslDropDownItemTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842884);
    }

    public SeslDropDownItemTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setChecked(boolean z) {
        int i;
        super.setChecked(z);
        setTypeface(Typeface.create("sec-roboto-light", z ? 1 : 0));
        if (z && getCurrentTextColor() == -65281) {
            Log.w(TAG, "SeslDropDownItemTextView text color reload!!");
            boolean isLightTheme = SeslMisc.isLightTheme(getContext());
            Context context = getContext();
            if (isLightTheme) {
                i = R.color.sesl_spinner_dropdown_text_color_light;
            } else {
                i = R.color.sesl_spinner_dropdown_text_color_dark;
            }
            if (context != null) {
                ColorStateList colorStateList = context.getResources().getColorStateList(i, context.getTheme());
                if (colorStateList != null) {
                    setTextColor(colorStateList);
                } else {
                    Log.w(TAG, "Didn't set SeslDropDownItemTextView text color!!");
                }
            }
        }
    }
}
