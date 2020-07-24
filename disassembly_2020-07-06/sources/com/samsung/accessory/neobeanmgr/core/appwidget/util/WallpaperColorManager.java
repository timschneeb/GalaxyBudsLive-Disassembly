package com.samsung.accessory.neobeanmgr.core.appwidget.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.permission.PermissionManager;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.SettingsGlobalCompat;

public class WallpaperColorManager {
    private static final String KEY_NEED_DARK_FONT = "need_dark_font";
    private static final int NOT_SUPPORTED = -1;
    private static final String TAG = (Application.TAG_ + WallpaperColorManager.class.getSimpleName());
    private static final int USER_ID = 0;
    private static boolean isWhiteWallpaper;
    private static WallpaperColorManager sWallpaperColorManager;

    public static synchronized WallpaperColorManager getInstance(Context context) {
        WallpaperColorManager wallpaperColorManager;
        synchronized (WallpaperColorManager.class) {
            if (sWallpaperColorManager == null) {
                sWallpaperColorManager = new WallpaperColorManager(context);
            }
            wallpaperColorManager = sWallpaperColorManager;
        }
        return wallpaperColorManager;
    }

    public WallpaperColorManager(Context context) {
        initWallpaperColor(context);
    }

    public static synchronized void initWallpaperColor(Context context) {
        synchronized (WallpaperColorManager.class) {
            boolean isWhiteWallPaper = isWhiteWallPaper(context);
            if (isWhiteWallpaper != isWhiteWallPaper) {
                isWhiteWallpaper = isWhiteWallPaper;
                WidgetUtil.updateWidgetProvider(context);
            }
            String str = TAG;
            Log.d(str, "initWallpaperColor isWhiteWallpaper : " + isWhiteWallpaper);
        }
    }

    public boolean isWhiteWallpaper() {
        return isWhiteWallpaper;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002b A[SYNTHETIC, Splitter:B:11:0x002b] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0020  */
    private static boolean isWhiteWallPaper(Context context) {
        int i;
        boolean z = false;
        if (!isWallpaperSupported(context)) {
            try {
                i = SettingsGlobalCompat.System.getIntForUser(context.getContentResolver(), KEY_NEED_DARK_FONT, -1, 0);
            } catch (Throwable th) {
                Log.d(TAG, th.toString());
            }
            if (i == -1) {
                i = Settings.System.getInt(context.getContentResolver(), KEY_NEED_DARK_FONT, -1);
            }
            if (i != -1) {
                try {
                    if (isPermissionDenied(context)) {
                        Log.d(TAG, "permission denied");
                        return false;
                    }
                    Bitmap bitmap = ((BitmapDrawable) WallpaperManager.getInstance(context).getDrawable()).getBitmap();
                    float[] colorHSV = getColorHSV(bitmap, new Rect[]{new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight())});
                    if (colorHSV != null && ((double) colorHSV[1]) < 0.3d && ((double) colorHSV[2]) > 0.88d) {
                        z = true;
                    }
                    String str = TAG;
                    Log.d(str, "KEY_NEED_DARK_FONT is not supported - isWhiteWallpaper : " + z);
                    return z;
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            } else if (i == 1) {
                return true;
            } else {
                return false;
            }
        }
        i = -1;
        if (i == -1) {
        }
        if (i != -1) {
        }
    }

    private static boolean isPermissionDenied(Context context) {
        return Build.VERSION.SDK_INT >= 27 && !PermissionManager.isPermissionGranted(context, "android.permission.READ_EXTERNAL_STORAGE");
    }

    private static boolean isWallpaperSupported(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        try {
            return WallpaperManager.getInstance(context).isWallpaperSupported();
        } catch (Exception e) {
            String str = TAG;
            Log.e(str, "WallpaperManager is not supported:" + e.toString());
            return false;
        }
    }

    private static float[] getColorHSV(Bitmap bitmap, Rect[] rectArr) {
        Rect[] rectArr2 = rectArr;
        float[] fArr = new float[3];
        float[] fArr2 = new float[3];
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int i = (int) ((width > height ? (float) height : (float) width) / 100.0f);
            if (i <= 0) {
                i = 1;
            }
            float f = 0.0f;
            int i2 = 0;
            float f2 = 0.0f;
            float f3 = 0.0f;
            for (int i3 = 0; i3 < rectArr2.length; i3++) {
                int i4 = rectArr2[i3].left;
                int i5 = rectArr2[i3].right;
                int i6 = rectArr2[i3].top;
                int i7 = rectArr2[i3].bottom;
                while (i4 < i5) {
                    int i8 = i2;
                    float f4 = f;
                    for (int i9 = i6; i9 < i7; i9 += i) {
                        Color.colorToHSV(bitmap.getPixel(i4, i9), fArr);
                        f4 += fArr[0];
                        f2 += fArr[1];
                        f3 += fArr[2];
                        i8++;
                    }
                    Bitmap bitmap2 = bitmap;
                    i4 += i;
                    f = f4;
                    i2 = i8;
                }
                Bitmap bitmap3 = bitmap;
            }
            float f5 = (float) i2;
            fArr2[0] = f / f5;
            fArr2[1] = f2 / f5;
            fArr2[2] = f3 / f5;
            return fArr2;
        } catch (Exception e) {
            String str = TAG;
            Log.e(str, "Exception = " + e);
            return null;
        }
    }
}
