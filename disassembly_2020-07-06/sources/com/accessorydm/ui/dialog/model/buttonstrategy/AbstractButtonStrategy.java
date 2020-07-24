package com.accessorydm.ui.dialog.model.buttonstrategy;

import android.content.Context;
import com.accessorydm.XDMDmUtils;
import com.samsung.android.fotaprovider.log.Log;

abstract class AbstractButtonStrategy implements ButtonStrategy {
    private final int id;
    private final String text;

    /* access modifiers changed from: protected */
    public abstract void doOnClick();

    AbstractButtonStrategy(String str, int i) {
        this.text = str;
        this.id = i;
    }

    public final String getText() {
        return this.text;
    }

    public final int getId() {
        return this.id;
    }

    public void onClick() {
        Log.I(getClass().getSimpleName() + " - getText(): " + getText() + ", getId(): " + getId());
        doOnClick();
    }

    static String getString(int i) {
        return getContext().getString(i);
    }

    static Context getContext() {
        return XDMDmUtils.getContext();
    }
}
