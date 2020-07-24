package com.google.android.material.resources;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

public class MaterialAttributes {
    public static TypedValue resolveAttributeOrThrow(View view, int i) {
        Context context = view.getContext();
        TypedValue resolveAttribute = resolveAttribute(context, i);
        if (resolveAttribute != null) {
            return resolveAttribute;
        }
        throw new IllegalArgumentException(String.format("The %1$s view requires a value for the %2$s attribute to be set in your app theme. You can either set the attribute in your theme or update your theme to inherit from Theme.MaterialComponents (or a descendant).", new Object[]{view.getClass().getCanonicalName(), context.getResources().getResourceName(i)}));
    }

    public static TypedValue resolveAttribute(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue;
        }
        return null;
    }
}
