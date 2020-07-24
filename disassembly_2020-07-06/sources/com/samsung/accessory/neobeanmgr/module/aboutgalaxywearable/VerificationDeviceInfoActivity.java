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
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgDebugData;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgDebugSerialNumber;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;

public class VerificationDeviceInfoActivity extends ConnectionActivity {
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + VerificationDeviceInfoActivity.class.getSimpleName());
    private String debugMessage = "no data";
    /* access modifiers changed from: private */
    public AppCompatButton mButton;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String access$100 = VerificationDeviceInfoActivity.TAG;
            Log.d(access$100, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            if (((action.hashCode() == -46786983 && action.equals(CoreService.ACTION_MSG_ID_DEBUG_GET_ALL_DATA)) ? (char) 0 : 65535) == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("[Left Serial Number] :");
                sb.append(Application.getCoreService().getEarBudsInfo().serialNumber_left);
                sb.append("\n");
                sb.append("[Right Serial Number] :");
                sb.append(Application.getCoreService().getEarBudsInfo().serialNumber_right);
                ((TextView) VerificationDeviceInfoActivity.this.findViewById(R.id.layout_device_serial_number)).setText(sb);
                ((TextView) VerificationDeviceInfoActivity.this.findViewById(R.id.layout_device_info_data)).setText(Application.getCoreService().getEarBudsInfo().debugInfo);
                VerificationDeviceInfoActivity.this.mButton.setAlpha(1.0f);
                VerificationDeviceInfoActivity.this.mButton.setEnabled(true);
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_verification_device_info);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((CharSequence) "Device Info");
        registerReceiver();
        this.mButton = (AppCompatButton) findViewById(R.id.button_request);
        this.mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Application.getCoreService().sendSppMessage(new MsgDebugSerialNumber());
                Application.getCoreService().sendSppMessage(new MsgDebugData());
                VerificationDeviceInfoActivity.this.mButton.setAlpha(0.4f);
                VerificationDeviceInfoActivity.this.mButton.setEnabled(false);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        super.onDestroy();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_MSG_ID_DEBUG_GET_ALL_DATA);
        registerReceiver(this.mReceiver, intentFilter);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
