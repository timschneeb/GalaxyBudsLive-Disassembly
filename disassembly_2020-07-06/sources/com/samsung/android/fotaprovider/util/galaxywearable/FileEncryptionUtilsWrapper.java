package com.samsung.android.fotaprovider.util.galaxywearable;

import android.content.Context;
import com.samsung.android.fotaprovider.log.Log;

public class FileEncryptionUtilsWrapper {
    public static boolean isUserUnlocked(Context context) {
        try {
            boolean booleanValue = ((Boolean) Class.forName("com.samsung.android.app.watchmanager.plugin.libfactory.util.FileEncryptionUtils").getDeclaredMethod("isUserUnlocked", new Class[]{Context.class}).invoke((Object) null, new Object[]{context})).booleanValue();
            Log.I("FBE is unlocked: " + booleanValue);
            return booleanValue;
        } catch (Exception unused) {
            Log.I("FBE is not supported");
            return true;
        }
    }
}
