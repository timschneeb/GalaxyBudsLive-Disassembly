package com.google.android.material.dialog;

import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.R;

class InsetDialogOnTouchListener implements View.OnTouchListener {
    private final AlertDialog dialog;
    private final int leftInset;
    private final int topInset;

    InsetDialogOnTouchListener(AlertDialog alertDialog, int i, int i2) {
        this.dialog = alertDialog;
        this.leftInset = i;
        this.topInset = i2;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        View findViewById = view.findViewById(R.id.parentPanel);
        int left = this.leftInset + findViewById.getLeft();
        int width = findViewById.getWidth() + left;
        int top = this.topInset + findViewById.getTop();
        if (new RectF((float) left, (float) top, (float) width, (float) (findViewById.getHeight() + top)).contains(motionEvent.getX(), motionEvent.getY())) {
            return false;
        }
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(4);
        view.performClick();
        if (Build.VERSION.SDK_INT >= 28) {
            return this.dialog.onTouchEvent(obtain);
        }
        this.dialog.onBackPressed();
        return true;
    }
}
