package com.samsung.accessory.neobeanmgr.module.aboutgalaxywearable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.core.service.DeviceLogManager;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;

public class VerificationDumpLogActivity extends ConnectionActivity {
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + VerificationDeviceInfoActivity.class.getSimpleName());
    /* access modifiers changed from: private */
    public String debugMessage = "no data";
    /* access modifiers changed from: private */
    public AppCompatButton mButton;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Can't fix incorrect switch cases order */
        public void onReceive(Context context, Intent intent) {
            char c;
            String access$100 = VerificationDumpLogActivity.TAG;
            Log.d(access$100, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            switch (action.hashCode()) {
                case -970408517:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_DATA)) {
                        c = 6;
                        break;
                    }
                case -663448447:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_DATA_SIZE)) {
                        c = 2;
                        break;
                    }
                case -3491759:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_START)) {
                        c = 5;
                        break;
                    }
                case 485005290:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_SESSION_OPEN)) {
                        c = 0;
                        break;
                    }
                case 810515391:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_DATA)) {
                        c = 3;
                        break;
                    }
                case 1104647875:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_COMPLETE)) {
                        c = 4;
                        break;
                    }
                case 1314538476:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_ROLE_SWITCH)) {
                        c = 8;
                        break;
                    }
                case 1679171317:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_COMPLETE)) {
                        c = 7;
                        break;
                    }
                case 2139070520:
                    if (action.equals(DeviceLogManager.ACTION_MSG_ID_LOG_SESSION_CLOSE)) {
                        c = 1;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    String unused = VerificationDumpLogActivity.this.debugMessage = "open session";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    return;
                case 1:
                    String unused2 = VerificationDumpLogActivity.this.debugMessage = "close session";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    VerificationDumpLogActivity.this.mButton.setAlpha(1.0f);
                    VerificationDumpLogActivity.this.mButton.setEnabled(true);
                    return;
                case 2:
                    String unused3 = VerificationDumpLogActivity.this.debugMessage = "coredump data size";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    return;
                case 3:
                    String unused4 = VerificationDumpLogActivity.this.debugMessage = "core dump data";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    return;
                case 4:
                    String unused5 = VerificationDumpLogActivity.this.debugMessage = "coredump complete";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    return;
                case 5:
                    String unused6 = VerificationDumpLogActivity.this.debugMessage = "trace start";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    return;
                case 6:
                    String unused7 = VerificationDumpLogActivity.this.debugMessage = "trace data";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    return;
                case 7:
                    String unused8 = VerificationDumpLogActivity.this.debugMessage = "trace complete";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    return;
                case 8:
                    String unused9 = VerificationDumpLogActivity.this.debugMessage = "role switch";
                    ((TextView) VerificationDumpLogActivity.this.findViewById(R.id.layout_device_info_data)).setText(VerificationDumpLogActivity.this.debugMessage);
                    return;
                default:
                    return;
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_verification_dump_log);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((CharSequence) "Device Dump Log");
        registerReceiver();
        this.mButton = (AppCompatButton) findViewById(R.id.button_start);
        this.mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Application.getCoreService().getDeviceLogInfo().sendOpenSession();
                VerificationDumpLogActivity.this.mButton.setAlpha(0.4f);
                VerificationDumpLogActivity.this.mButton.setEnabled(false);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        Application.getCoreService().getDeviceLogInfo().setDeviceLogExtractionState(0);
        super.onDestroy();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_SESSION_OPEN);
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_SESSION_CLOSE);
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_DATA_SIZE);
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_DATA);
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_COREDUMP_COMPLETE);
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_START);
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_DATA);
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_COMPLETE);
        intentFilter.addAction(DeviceLogManager.ACTION_MSG_ID_LOG_TRACE_ROLE_SWITCH);
        registerReceiver(this.mReceiver, intentFilter);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
