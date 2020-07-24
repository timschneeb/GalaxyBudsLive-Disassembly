package androidx.reflect.os;

import android.os.Build;
import androidx.reflect.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslPerfManagerReflector {
    private static String mClassName;

    private SeslPerfManagerReflector() {
    }

    static {
        if (Build.VERSION.SDK_INT >= 24) {
            mClassName = "com.samsung.android.os.SemPerfManager";
        } else {
            mClassName = "android.os.DVFSHelper";
        }
    }

    public static boolean onSmoothScrollEvent(boolean z) {
        Method method = SeslBaseReflector.getMethod(mClassName, "onSmoothScrollEvent", (Class<?>[]) new Class[]{Boolean.TYPE});
        if (method == null) {
            return false;
        }
        SeslBaseReflector.invoke((Object) null, method, Boolean.valueOf(z));
        return true;
    }
}
