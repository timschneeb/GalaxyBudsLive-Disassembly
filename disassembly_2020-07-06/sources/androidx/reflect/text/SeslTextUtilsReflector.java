package androidx.reflect.text;

import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import androidx.reflect.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslTextUtilsReflector {
    private static final Class<?> mClass = TextUtils.class;

    private SeslTextUtilsReflector() {
    }

    public static char[] semGetPrefixCharForSpan(TextPaint textPaint, CharSequence charSequence, char[] cArr) {
        Method method;
        Class<char[]> cls = char[].class;
        if (Build.VERSION.SDK_INT >= 29) {
            method = SeslBaseReflector.getDeclaredMethod(mClass, "hidden_semGetPrefixCharForSpan", (Class<?>[]) new Class[]{TextPaint.class, CharSequence.class, cls});
        } else if (Build.VERSION.SDK_INT >= 24) {
            method = SeslBaseReflector.getMethod(mClass, "semGetPrefixCharForSpan", (Class<?>[]) new Class[]{TextPaint.class, CharSequence.class, cls});
        } else {
            method = SeslBaseReflector.getMethod(mClass, "getPrefixCharForIndian", (Class<?>[]) new Class[]{TextPaint.class, CharSequence.class, cls});
        }
        if (method != null) {
            Object invoke = SeslBaseReflector.invoke((Object) null, method, textPaint, charSequence, cArr);
            if (invoke instanceof char[]) {
                return (char[]) invoke;
            }
        }
        return new char[0];
    }
}
