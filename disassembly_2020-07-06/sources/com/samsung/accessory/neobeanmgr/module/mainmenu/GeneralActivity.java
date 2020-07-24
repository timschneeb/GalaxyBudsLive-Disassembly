package com.samsung.accessory.neobeanmgr.module.mainmenu;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.uhm.DeviceRegistryData;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.common.ui.OnSingleClickListener;
import com.samsung.accessory.neobeanmgr.common.util.BluetoothUtil;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationManager;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgReset;
import com.samsung.accessory.neobeanmgr.core.uhmdb.UhmDatabase;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;
import com.samsung.accessory.neobeanmgr.module.tipsmanual.TipsAndUserManualActivity;

public class GeneralActivity extends PermissionCheckActivity {
    private static final int MIN_BATTERY_GAUGE = 15;
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + GeneralActivity.class.getSimpleName());
    /* access modifiers changed from: private */
    public boolean isWorkingResetProcess;
    /* access modifiers changed from: private */
    public Handler mProgressDialogTimer = new Handler();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0059  */
        /* JADX WARNING: Removed duplicated region for block: B:41:0x011a  */
        public void onReceive(Context context, Intent intent) {
            char c;
            String access$200 = GeneralActivity.TAG;
            Log.d(access$200, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            int hashCode = action.hashCode();
            if (hashCode != -1926486170) {
                if (hashCode != -1354974214) {
                    if (hashCode == 1217051251 && action.equals(CoreService.ACTION_MSG_ID_RESET)) {
                        c = 0;
                        if (c == 0) {
                            if (c != 1) {
                                if (c == 2) {
                                    if (GeneralActivity.this.isWorkingResetProcess) {
                                        BluetoothDevice bondedDevice = BluetoothUtil.getBondedDevice(UhmFwUtil.getLastLaunchDeviceId());
                                        if (bondedDevice != null) {
                                            GeneralActivity.this.unpairDevice(bondedDevice);
                                        } else {
                                            String access$2002 = GeneralActivity.TAG;
                                            Log.d(access$2002, "resetDevice- connected A2DP device : " + bondedDevice);
                                        }
                                        Preferences.clear(UhmFwUtil.getLastLaunchDeviceId());
                                        return;
                                    }
                                    Log.w(GeneralActivity.TAG, "CoreService.ACTION_DEVICE_DISCONNECTED -> finish()");
                                    GeneralActivity.this.finish();
                                    return;
                                }
                                return;
                            } else if (GeneralActivity.this.isWorkingResetProcess && Application.getUhmDatabase().getDevice(UhmFwUtil.getLastLaunchDeviceId()) == null) {
                                GeneralActivity.this.progressDialog.cancel();
                                GeneralActivity.this.mProgressDialogTimer.removeCallbacksAndMessages((Object) null);
                                Toast.makeText(Application.getContext(), GeneralActivity.this.getString(R.string.earbuds_reset), 0).show();
                                GeneralActivity.this.finishAffinity();
                                DeviceRegistryData access$700 = GeneralActivity.this.getLastLaunchedDevice();
                                if (access$700 == null) {
                                    access$700 = GeneralActivity.this.getDeviceByStatus(2);
                                }
                                if (access$700 == null) {
                                    access$700 = GeneralActivity.this.getDeviceByStatus(1);
                                }
                                if (access$700 == null) {
                                    UhmFwUtil.startSetupWizardWelcomeActivity(GeneralActivity.this);
                                } else {
                                    UhmFwUtil.handlePluginLaunch(GeneralActivity.this, UhmFwUtil.getLastLaunchDeviceId(), access$700.deviceId, access$700.deviceName);
                                }
                                boolean unused = GeneralActivity.this.isWorkingResetProcess = false;
                                return;
                            } else {
                                return;
                            }
                        } else if (!Application.getCoreService().getEarBudsInfo().resultOfReset) {
                            boolean unused2 = GeneralActivity.this.isWorkingResetProcess = false;
                            GeneralActivity.this.progressDialog.cancel();
                            GeneralActivity.this.mProgressDialogTimer.removeCallbacksAndMessages((Object) null);
                            GeneralActivity.this.resetFailDialog();
                            return;
                        } else if (NotificationManager.hasInstance()) {
                            NotificationManager.getInstance(Application.getContext()).destroy();
                            return;
                        } else {
                            return;
                        }
                    }
                } else if (action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) {
                    c = 2;
                    if (c == 0) {
                    }
                }
            } else if (action.equals(UhmDatabase.ACTION_DB_UPDATED)) {
                c = 1;
                if (c == 0) {
                }
            }
            c = 65535;
            if (c == 0) {
            }
        }
    };
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_general);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((CharSequence) getString(R.string.general));
        findViewById(R.id.text_user_manaual).setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.ELSE_USER_MANUAL, SA.Screen.GENERAL);
                TipsAndUserManualActivity.startUserManual(GeneralActivity.this);
            }
        });
        findViewById(R.id.reset_earbuds).setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.RESET_EARBUDS, SA.Screen.GENERAL);
                GeneralActivity.this.alertDialog(Application.getCoreService().getEarBudsInfo());
            }
        });
    }

    /* access modifiers changed from: private */
    public void alertDialog(EarBudsInfo earBudsInfo) {
        Integer valueOf = earBudsInfo.batteryI == -1 ? null : Integer.valueOf(earBudsInfo.batteryI);
        if ((valueOf != null || (earBudsInfo.batteryL < 15 && earBudsInfo.batteryR < 15)) && (valueOf == null || valueOf.intValue() < 15)) {
            batteryLowDialog();
        } else {
            resetDialog();
        }
    }

    private void resetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) getApplicationContext().getString(R.string.reset_earbuds_dialog_title));
        builder.setMessage((CharSequence) getApplicationContext().getString(R.string.reset_earbuds_dialog_message));
        builder.setNegativeButton((CharSequence) getApplicationContext().getString(R.string.cancel), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton((CharSequence) getApplicationContext().getString(R.string.button_reset), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Application.getCoreService().sendSppMessage(new MsgReset());
                GeneralActivity.this.resetProgressDialog();
            }
        });
        builder.show();
    }

    private void batteryLowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) getApplicationContext().getString(R.string.battery_low_dialog_title));
        builder.setMessage((CharSequence) getApplicationContext().getString(R.string.battery_low_dialog_message, new Object[]{15}));
        builder.setPositiveButton((CharSequence) getApplicationContext().getString(R.string.ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    /* access modifiers changed from: private */
    public void resetFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) getApplicationContext().getString(R.string.reset_fail_dialog_title));
        builder.setMessage((CharSequence) getApplicationContext().getString(R.string.reset_fail_dialog_message));
        builder.setPositiveButton((CharSequence) getApplicationContext().getString(R.string.ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    /* access modifiers changed from: private */
    public void resetProgressDialog() {
        Log.d(TAG, "resetProgressDialog");
        this.isWorkingResetProcess = true;
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage(getApplicationContext().getString(R.string.reset_progress_dialog_message));
        this.progressDialog.setCancelable(false);
        this.progressDialog.setProgressStyle(16973855);
        this.progressDialog.show();
        this.mProgressDialogTimer.postDelayed(new Runnable() {
            public void run() {
                Log.i(GeneralActivity.TAG, "resetProgressDialogTimeOut run");
                boolean unused = GeneralActivity.this.isWorkingResetProcess = false;
                GeneralActivity.this.progressDialog.cancel();
            }
        }, 30000);
    }

    /* access modifiers changed from: private */
    public void unpairDevice(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "unpairDevice");
        try {
            bluetoothDevice.getClass().getMethod("removeBond", (Class[]) null).invoke(bluetoothDevice, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_MSG_ID_RESET);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        intentFilter.addAction(UhmDatabase.ACTION_DB_UPDATED);
        registerReceiver(this.mReceiver, intentFilter);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null) {
            progressDialog2.cancel();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        SamsungAnalyticsUtil.sendPage(SA.Screen.GENERAL);
        registerReceiver();
        super.onResume();
        if (!Application.getCoreService().isConnected()) {
            Log.w(TAG, "isConnected() == false -> finish()");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        unregisterReceiver(this.mReceiver);
    }

    public boolean onSupportNavigateUp() {
        SamsungAnalyticsUtil.sendEvent(SA.Event.UP_BUTTON, SA.Screen.GENERAL);
        finish();
        return true;
    }

    /* access modifiers changed from: private */
    public DeviceRegistryData getDeviceByStatus(int i) {
        for (DeviceRegistryData next : Application.getUhmDatabase().getDeviceList()) {
            if (next.connected.intValue() == i) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public DeviceRegistryData getLastLaunchedDevice() {
        for (DeviceRegistryData next : Application.getUhmDatabase().getDeviceList()) {
            if (next.lastLaunch.intValue() == 1) {
                return next;
            }
        }
        return null;
    }
}
