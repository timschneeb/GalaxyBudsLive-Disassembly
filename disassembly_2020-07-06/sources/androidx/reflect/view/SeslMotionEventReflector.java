package androidx.reflect.view;

import android.view.MotionEvent;
import androidx.reflect.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslMotionEventReflector {
    private static final Class<?> mClass = MotionEvent.class;

    private SeslMotionEventReflector() {
    }

    public static int getPointerIdBits(MotionEvent motionEvent) {
        Method method = SeslBaseReflector.getMethod(mClass, "getPointerIdBits", (Class<?>[]) new Class[0]);
        if (method != null) {
            Object invoke = SeslBaseReflector.invoke(motionEvent, method, new Object[0]);
            if (invoke instanceof Integer) {
                return ((Integer) invoke).intValue();
            }
        }
        return 0;
    }
}
