package com.samsung.accessory.neobeanmgr.common.ui;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.SwitchCompat;

public class RecycleAnimationSwitchCompat extends SwitchCompat {
    public RecycleAnimationSwitchCompat(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setChecked(boolean z) {
        layout(getLeft(), getTop(), getRight(), getBottom());
        super.setChecked(z);
    }
}
