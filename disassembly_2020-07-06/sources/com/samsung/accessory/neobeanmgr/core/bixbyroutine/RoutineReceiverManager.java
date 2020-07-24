package com.samsung.accessory.neobeanmgr.core.bixbyroutine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;

public class RoutineReceiverManager {
    private static final String ACTION_GOS_GAME_RESUME_PAUSE = "com.samsung.android.game.gos.ACTION_GAME_RESUME_PAUSE";
    private static final String EXTRA_GOS_PKG_NAME = "pkgName";
    private static final String EXTRA_GOS_TYPE = "type";
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + RoutineReceiverManager.class.getSimpleName());
    private static final String VALUE_GOS_PAUSE = "GAME_PAUSED";
    private Context context;
    private final BroadcastReceiver mGosReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String access$000 = RoutineReceiverManager.TAG;
            Log.d(access$000, "mGosReceiver onReceive() : " + intent.getAction());
            if (Application.getCoreService().isConnected() && RoutineReceiverManager.ACTION_GOS_GAME_RESUME_PAUSE.equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("type");
                String stringExtra2 = intent.getStringExtra(RoutineReceiverManager.EXTRA_GOS_PKG_NAME);
                String access$0002 = RoutineReceiverManager.TAG;
                Log.d(access$0002, "onReceive() : type=" + stringExtra + ", pkgName=" + stringExtra2);
                if (stringExtra != null && RoutineReceiverManager.VALUE_GOS_PAUSE.equals(stringExtra)) {
                    RoutineUtils.sendRecommendBroadcast(context, RoutineConstants.RECOMMEND_DRIVE_TAG_GAMING);
                }
            }
        }
    };
    private final BroadcastReceiver mRoutinePackageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Uri data = intent.getData();
            if (data != null) {
                String schemeSpecificPart = data.getSchemeSpecificPart();
                if ("com.samsung.android.app.routines".equals(schemeSpecificPart)) {
                    String access$000 = RoutineReceiverManager.TAG;
                    Log.d(access$000, "mRoutinePackageReceiver onReceive() Package name : " + schemeSpecificPart + ", Action : " + intent.getAction());
                    RoutineUtils.initialize(context);
                }
            }
        }
    };

    public RoutineReceiverManager(Context context2) {
        this.context = context2;
        onCreate();
    }

    public void onCreate() {
        Log.d(TAG, "onCreate()");
        RoutineUtils.initialize(this.context);
        registerReceiver();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        unRegisterReceiver();
    }

    private void registerReceiver() {
        registerRoutinePackageReceiver();
        registerGosReceiver();
    }

    private void unRegisterReceiver() {
        this.context.unregisterReceiver(this.mRoutinePackageReceiver);
        this.context.unregisterReceiver(this.mGosReceiver);
    }

    private void registerRoutinePackageReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addDataScheme("package");
        this.context.registerReceiver(this.mRoutinePackageReceiver, intentFilter);
    }

    private void registerGosReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GOS_GAME_RESUME_PAUSE);
        this.context.registerReceiver(this.mGosReceiver, intentFilter, "android.permission.WRITE_SECURE_SETTINGS", (Handler) null);
    }
}
