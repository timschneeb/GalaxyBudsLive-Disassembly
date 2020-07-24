package androidx.reflect.icu;

import android.os.Build;
import android.util.Log;
import androidx.reflect.SeslBaseReflector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.util.Locale;

public class SeslLocaleDataReflector {
    private static String mClassName = "libcore.icu.LocaleData";
    private static String mSemClassName = "com.samsung.sesl.icu.SemLocaleData";

    private SeslLocaleDataReflector() {
    }

    public static Object get(Locale locale) {
        Method method;
        if (Build.VERSION.SDK_INT >= 29) {
            method = SeslBaseReflector.getDeclaredMethod(mSemClassName, "get", (Class<?>[]) new Class[]{Locale.class});
        } else {
            method = SeslBaseReflector.getMethod(mClassName, "get", (Class<?>[]) new Class[]{Locale.class});
        }
        if (method != null) {
            Object invoke = SeslBaseReflector.invoke((Object) null, method, locale);
            if (invoke.getClass().getName().equals(mClassName)) {
                return invoke;
            }
        }
        return null;
    }

    public static char getField_zeroDigit(Object obj) {
        Object obj2 = null;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mSemClassName, "getZeroDigit", (Class<?>[]) new Class[]{SeslBaseReflector.getClass(mClassName)});
            if (declaredMethod != null) {
                obj2 = SeslBaseReflector.invoke((Object) null, declaredMethod, obj);
            }
        } else {
            Field field = SeslBaseReflector.getField(mClassName, "zeroDigit");
            if (field != null) {
                obj2 = SeslBaseReflector.get(obj, field);
            }
        }
        if (obj2 instanceof Character) {
            return ((Character) obj2).charValue();
        }
        return '0';
    }

    public static String[] getField_amPm(Object obj) {
        Object obj2 = null;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mSemClassName, "getAmPm", (Class<?>[]) new Class[]{SeslBaseReflector.getClass(mClassName)});
            if (declaredMethod != null) {
                obj2 = SeslBaseReflector.invoke((Object) null, declaredMethod, obj);
            }
        } else {
            Field field = SeslBaseReflector.getField(mClassName, "amPm");
            if (field != null) {
                obj2 = SeslBaseReflector.get(obj, field);
            }
        }
        if (obj2 instanceof String[]) {
            return (String[]) obj2;
        }
        Log.e("SeslLocaleDataReflector", "amPm failed. Use DateFormatSymbols for ampm");
        return new DateFormatSymbols().getAmPmStrings();
    }

    public static String getField_narrowAm(Object obj) {
        Object obj2 = null;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mSemClassName, "getNarrowAm", (Class<?>[]) new Class[]{SeslBaseReflector.getClass(mClassName)});
            if (declaredMethod != null) {
                obj2 = SeslBaseReflector.invoke((Object) null, declaredMethod, obj);
            }
        } else {
            Field field = SeslBaseReflector.getField(mClassName, "narrowAm");
            if (field != null) {
                obj2 = SeslBaseReflector.get(obj, field);
            }
        }
        return obj2 instanceof String ? (String) obj2 : "Am";
    }

    public static String getField_narrowPm(Object obj) {
        Object obj2 = null;
        if (Build.VERSION.SDK_INT >= 29) {
            Method declaredMethod = SeslBaseReflector.getDeclaredMethod(mSemClassName, "getNarrowPm", (Class<?>[]) new Class[]{SeslBaseReflector.getClass(mClassName)});
            if (declaredMethod != null) {
                obj2 = SeslBaseReflector.invoke((Object) null, declaredMethod, obj);
            }
        } else {
            Field field = SeslBaseReflector.getField(mClassName, "narrowPm");
            if (field != null) {
                obj2 = SeslBaseReflector.get(obj, field);
            }
        }
        return obj2 instanceof String ? (String) obj2 : "Pm";
    }
}
