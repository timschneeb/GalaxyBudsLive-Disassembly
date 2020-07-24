package com.samsung.android.fotaprovider.util.firebase;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.samsung.accessory.neobeanmgr.BuildConfig;
import com.sec.android.fotaprovider.R;

public enum FotaFirebaseInfo {
    FOTAPROVIDER("com.sec.android.fotaprovider", R.string.google_app_id_fotaprovider, R.string.google_api_key_fotaprovider),
    GEAROPLUGIN("com.samsung.android.gearoplugin", R.string.google_app_id_gearoplugin, R.string.google_api_key_gearoplugin),
    GEARGPLUGIN("com.samsung.android.geargplugin", R.string.google_app_id_geargplugin, R.string.google_api_key_geargplugin),
    GEARPPLUGIN("com.samsung.android.gearpplugin", R.string.google_app_id_gearpplugin, R.string.google_api_key_gearpplugin),
    GEARRPLUGIN("com.samsung.android.gearrplugin", R.string.google_app_id_gearrplugin, R.string.google_api_key_gearrplugin),
    GEARNPLUGIN("com.samsung.android.gearnplugin", R.string.google_app_id_gearnplugin, R.string.google_api_key_gearnplugin),
    GEARFIT2PLUGIN("com.samsung.android.gearfit2plugin", R.string.google_app_id_gearfit2plugin, R.string.google_api_key_gearfit2plugin),
    NEOBEANMGR(BuildConfig.APPLICATION_ID, R.string.google_app_id_neobeanmgr, R.string.google_api_key_neobeanmgr);
    
    private final int currentKey;
    private final int mobilesdkAppId;
    private final String packageName;

    private FotaFirebaseInfo(String str, int i, int i2) {
        this.packageName = str;
        this.mobilesdkAppId = i;
        this.currentKey = i2;
    }

    public static void initializeFirebaseInfo(Context context) {
        FotaFirebaseInfo fotaFirebaseInfo = FOTAPROVIDER;
        for (FotaFirebaseInfo fotaFirebaseInfo2 : values()) {
            if (fotaFirebaseInfo2.packageName.equals(context.getPackageName())) {
                fotaFirebaseInfo = fotaFirebaseInfo2;
            }
        }
        FirebaseApp.initializeApp(context, new FirebaseOptions.Builder().setGcmSenderId(context.getString(R.string.gcm_defaultSenderId)).setDatabaseUrl(context.getString(R.string.firebase_database_url)).setProjectId(context.getString(R.string.project_id)).setStorageBucket(context.getString(R.string.google_storage_bucket)).setApplicationId(context.getString(fotaFirebaseInfo.mobilesdkAppId)).setApiKey(context.getString(fotaFirebaseInfo.currentKey)).build());
    }
}
