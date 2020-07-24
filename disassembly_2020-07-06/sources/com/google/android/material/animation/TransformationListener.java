package com.google.android.material.animation;

import android.view.View;

public interface TransformationListener<T extends View> {
    void onScaleChanged(T t);

    void onTranslationChanged(T t);
}
