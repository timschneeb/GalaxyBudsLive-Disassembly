package androidx.reflect.feature;

import android.os.Build;
import androidx.reflect.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslFloatingFeatureReflector {
    private static String mClassName;

    private SeslFloatingFeatureReflector() {
    }

    static {
        if (Build.VERSION.SDK_INT >= 29) {
            mClassName = "com.samsung.sesl.feature.SemFloatingFeature";
        } else if (Build.VERSION.SDK_INT >= 24) {
            mClassName = "com.samsung.android.feature.SemFloatingFeature";
        } else {
            mClassName = "com.samsung.android.feature.FloatingFeature";
        }
    }

    private static Object getInstance() {
        Method method = SeslBaseReflector.getMethod(mClassName, "getInstance", (Class<?>[]) new Class[0]);
        if (method == null) {
            return null;
        }
        Object invoke = SeslBaseReflector.invoke((Object) null, method, new Object[0]);
        if (invoke.getClass().getName().equals(mClassName)) {
            return invoke;
        }
        return null;
    }

    public static String getString(String str, String str2) {
        Object obj = null;
        if (Build.VERSION.SDK_INT >= 29) {
            obj = SeslBaseReflector.invoke((Object) null, SeslBaseReflector.getDeclaredMethod(mClassName, "hidden_getString", (Class<?>[]) new Class[]{String.class, String.class}), str, str2);
        } else {
            Object instance = getInstance();
            if (instance != null) {
                obj = SeslBaseReflector.invoke(instance, SeslBaseReflector.getMethod(mClassName, "getString", (Class<?>[]) new Class[]{String.class, String.class}), str, str2);
            }
        }
        return obj instanceof String ? (String) obj : str2;
    }
}
