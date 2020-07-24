package seccompat.android.os;

import seccompat.Reflection;
import seccompat.SecCompatUtil;

public class UserHandle {
    public static int myUserId() {
        if (SecCompatUtil.isSEPDevice()) {
            return android.os.UserHandle.semGetMyUserId();
        }
        return ((Integer) Reflection.callStaticMethod("android.os.UserHandle", "myUserId", new Object[0])).intValue();
    }

    public static int proxyGetIdentifier(android.os.UserHandle userHandle) {
        return ((Integer) Reflection.callMethod(userHandle, "getIdentifier", new Object[0])).intValue();
    }
}
