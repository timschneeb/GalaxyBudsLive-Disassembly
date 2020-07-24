package androidx.reflect.media;

import android.media.RingtoneManager;
import android.os.Build;
import androidx.reflect.SeslBaseReflector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslRingtoneManagerReflector {
    private static final Class<?> mClass = RingtoneManager.class;

    private SeslRingtoneManagerReflector() {
    }

    public static String getField_EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS() {
        Object obj = null;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mClass, "hidden_EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS", (Class<?>[]) new Class[0]);
            if (declaredMethod != null) {
                obj = SeslBaseReflector.invoke((Object) null, declaredMethod, new Object[0]);
            }
        } else {
            Field field = SeslBaseReflector.getField(mClass, "EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS");
            if (field != null) {
                obj = SeslBaseReflector.get((Object) null, field);
            }
        }
        return obj instanceof String ? (String) obj : "android.intent.extra.ringtone.SHOW_DEFAULT";
    }
}
