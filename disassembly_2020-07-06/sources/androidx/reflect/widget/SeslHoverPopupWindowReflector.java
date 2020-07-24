package androidx.reflect.widget;

import android.os.Build;
import androidx.reflect.SeslBaseReflector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslHoverPopupWindowReflector {
    private static String mClassName;

    private SeslHoverPopupWindowReflector() {
    }

    static {
        if (Build.VERSION.SDK_INT >= 24) {
            mClassName = "com.samsung.android.widget.SemHoverPopupWindow";
        } else {
            mClassName = "android.widget.HoverPopupWindow";
        }
    }

    public static int getField_TYPE_NONE() {
        Object obj = null;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mClassName, "hidden_TYPE_NONE", (Class<?>[]) new Class[0]);
            if (declaredMethod != null) {
                obj = SeslBaseReflector.invoke((Object) null, declaredMethod, new Object[0]);
            }
        } else {
            Field field = SeslBaseReflector.getField(mClassName, "TYPE_NONE");
            if (field != null) {
                obj = SeslBaseReflector.get((Object) null, field);
            }
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        return 0;
    }

    public static int getField_TYPE_TOOLTIP() {
        Object obj = null;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mClassName, "hidden_TYPE_TOOLTIP", (Class<?>[]) new Class[0]);
            if (declaredMethod != null) {
                obj = SeslBaseReflector.invoke((Object) null, declaredMethod, new Object[0]);
            }
        } else {
            Field field = SeslBaseReflector.getField(mClassName, "TYPE_TOOLTIP");
            if (field != null) {
                obj = SeslBaseReflector.get((Object) null, field);
            }
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        return 1;
    }

    public static int getField_TYPE_USER_CUSTOM() {
        Object obj = null;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mClassName, "hidden_TYPE_USER_CUSTOM", (Class<?>[]) new Class[0]);
            if (declaredMethod != null) {
                obj = SeslBaseReflector.invoke((Object) null, declaredMethod, new Object[0]);
            }
        } else {
            Field field = SeslBaseReflector.getField(mClassName, "TYPE_USER_CUSTOM");
            if (field != null) {
                obj = SeslBaseReflector.get((Object) null, field);
            }
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        return 3;
    }

    public static void setGravity(Object obj, int i) {
        Method method;
        if (Build.VERSION.SDK_INT >= 29) {
            method = SeslBaseReflector.getDeclaredMethod(mClassName, "hidden_setGravity", (Class<?>[]) new Class[]{Integer.TYPE});
        } else if (Build.VERSION.SDK_INT >= 24) {
            method = SeslBaseReflector.getMethod(mClassName, "setGravity", (Class<?>[]) new Class[]{Integer.TYPE});
        } else {
            method = SeslBaseReflector.getMethod(mClassName, "setPopupGravity", (Class<?>[]) new Class[]{Integer.TYPE});
        }
        if (method != null) {
            SeslBaseReflector.invoke(obj, method, Integer.valueOf(i));
        }
    }

    public static void setOffset(Object obj, int i, int i2) {
        Method method;
        if (Build.VERSION.SDK_INT >= 29) {
            method = SeslBaseReflector.getDeclaredMethod(mClassName, "hidden_setOffset", (Class<?>[]) new Class[]{Integer.TYPE, Integer.TYPE});
        } else if (Build.VERSION.SDK_INT >= 24) {
            method = SeslBaseReflector.getMethod(mClassName, "setOffset", (Class<?>[]) new Class[]{Integer.TYPE, Integer.TYPE});
        } else {
            method = SeslBaseReflector.getMethod(mClassName, "setPopupPosOffset", (Class<?>[]) new Class[]{Integer.TYPE, Integer.TYPE});
        }
        if (method != null) {
            SeslBaseReflector.invoke(obj, method, Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    public static void setHoverDetectTime(Object obj, int i) {
        Method method;
        if (Build.VERSION.SDK_INT >= 29) {
            method = SeslBaseReflector.getDeclaredMethod(mClassName, "hidden_setHoverDetectTime", (Class<?>[]) new Class[]{Integer.TYPE});
        } else {
            method = SeslBaseReflector.getMethod(mClassName, "setHoverDetectTime", (Class<?>[]) new Class[]{Integer.TYPE});
        }
        if (method != null) {
            SeslBaseReflector.invoke(obj, method, Integer.valueOf(i));
        }
    }

    public static void setHoveringPoint(Object obj, int i, int i2) {
        Method method = SeslBaseReflector.getMethod(mClassName, "setHoveringPoint", (Class<?>[]) new Class[]{Integer.TYPE, Integer.TYPE});
        if (method != null) {
            SeslBaseReflector.invoke(obj, method, Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    public static void update(Object obj) {
        Method method;
        if (Build.VERSION.SDK_INT >= 29) {
            method = SeslBaseReflector.getMethod(mClassName, "hidden_update", (Class<?>[]) new Class[0]);
        } else if (Build.VERSION.SDK_INT >= 24) {
            method = SeslBaseReflector.getMethod(mClassName, "update", (Class<?>[]) new Class[0]);
        } else {
            method = SeslBaseReflector.getMethod(mClassName, "updateHoverPopup", (Class<?>[]) new Class[0]);
        }
        if (method != null) {
            SeslBaseReflector.invoke(obj, method, new Object[0]);
        }
    }
}
