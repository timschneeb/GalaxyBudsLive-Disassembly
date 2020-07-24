package seccompat.android.os;

import android.os.SemSystemProperties;
import seccompat.Reflection;
import seccompat.SecCompatUtil;

public class SystemProperties {
    public static int getInt(String str, int i) {
        if (SecCompatUtil.isSEPDevice()) {
            return SemSystemProperties.getInt(str, i);
        }
        return ((Integer) Reflection.callStaticMethod("android.os.SystemProperties", "getInt", str, Integer.valueOf(i))).intValue();
    }

    public static String get(String str, String str2) {
        if (SecCompatUtil.isSEPDevice()) {
            return SemSystemProperties.get(str, str2);
        }
        return (String) Reflection.callStaticMethod("android.os.SystemProperties", "get", str, str2);
    }

    public static String get(String str) {
        if (SecCompatUtil.isSEPDevice()) {
            return SemSystemProperties.get(str);
        }
        return (String) Reflection.callStaticMethod("android.os.SystemProperties", "get", str);
    }
}
