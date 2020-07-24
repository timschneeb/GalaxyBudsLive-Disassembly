package androidx.reflect.widget;

import android.os.Build;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import androidx.reflect.SeslBaseReflector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslAbsListViewReflector {
    private static final Class<?> mClass = AbsListView.class;

    private SeslAbsListViewReflector() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0032 A[RETURN] */
    public static EdgeEffect getField_mEdgeGlowTop(AbsListView absListView) {
        Object obj;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mClass, "hidden_mEdgeGlowTop", (Class<?>[]) new Class[0]);
            if (declaredMethod != null) {
                obj = SeslBaseReflector.invoke(absListView, declaredMethod, new Object[0]);
                if (obj instanceof EdgeEffect) {
                    return (EdgeEffect) obj;
                }
                return null;
            }
        } else {
            Field declaredField = SeslBaseReflector.getDeclaredField(mClass, "mEdgeGlowTop");
            if (declaredField != null) {
                obj = SeslBaseReflector.get(absListView, declaredField);
                if (obj instanceof EdgeEffect) {
                }
            }
        }
        obj = null;
        if (obj instanceof EdgeEffect) {
        }
    }

    public static void setField_mEdgeGlowTop(AbsListView absListView, EdgeEffect edgeEffect) {
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mClass, "hidden_mEdgeGlowTop", (Class<?>[]) new Class[]{EdgeEffect.class});
            if (declaredMethod != null) {
                SeslBaseReflector.invoke(absListView, declaredMethod, edgeEffect);
                return;
            }
            return;
        }
        Field declaredField = SeslBaseReflector.getDeclaredField(mClass, "mEdgeGlowTop");
        if (declaredField != null) {
            SeslBaseReflector.set(absListView, declaredField, edgeEffect);
        }
    }

    public static void setField_mEdgeGlowBottom(AbsListView absListView, EdgeEffect edgeEffect) {
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mClass, "hidden_mEdgeGlowBottom", (Class<?>[]) new Class[]{EdgeEffect.class});
            if (declaredMethod != null) {
                SeslBaseReflector.invoke(absListView, declaredMethod, edgeEffect);
                return;
            }
            return;
        }
        Field declaredField = SeslBaseReflector.getDeclaredField(mClass, "mEdgeGlowBottom");
        if (declaredField != null) {
            SeslBaseReflector.set(absListView, declaredField, edgeEffect);
        }
    }
}
