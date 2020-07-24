package com.samsung.android.fotaagent.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.accessorydm.interfaces.XCommonInterface;
import com.samsung.android.fotaagent.ui.DialogActivity;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.GeneralUtil;
import com.samsung.android.fotaprovider.util.OperatorUtil;
import com.samsung.android.fotaprovider.util.type.DeviceType;
import com.sec.android.fotaprovider.R;

public class DialogActivity extends Activity {
    public static final int UI_CONNECTING_CONSUMER = 0;
    public static final int UI_CONNECTION_FAILED = 10;
    public static final int UI_REGISTERING_DEVICE = 20;
    public static final int UI_REGISTRATION_FAILED = 30;
    public static final int UI_REGISTRATION_SUCCESS = 40;
    private static UiHandler mUiHandler;
    /* access modifiers changed from: private */
    public AlertDialog mAlertDialog = null;
    private Toast mToast = null;

    public class UiHandler extends Handler {
        public UiHandler() {
        }

        public void handleMessage(Message message) {
            if (!GeneralUtil.isIdleScreen((ActivityManager) DialogActivity.this.getSystemService("activity"))) {
                DialogActivity.this.finish();
                return;
            }
            int i = message.what;
            if (i == 0) {
                Log.I("UI State: connecting socket dialog");
            } else if (i == 10) {
                Log.I("UI State: socket connection failed dialog");
                DialogActivity.this.showError(DeviceType.get().getTextType().getConnectionFailedMessageId());
            } else if (i == 20) {
                Log.I("UI State: registering device dialog");
                DialogActivity.this.showToast(R.string.STR_DM_CONNECTING_SERVER);
            } else if (i == 30) {
                Log.I("UI State: registration failed dialog");
                int i2 = message.arg1;
                if (i2 == 400) {
                    DialogActivity.this.showError(R.string.STR_DM_UNABLE_NETWORK);
                } else if (i2 == 410) {
                    DialogActivity.this.showError(R.string.STR_ROAMING_WIFI_DISCONNECTED);
                } else {
                    DialogActivity.this.showError(R.string.STR_REGISTRATION_FAILED);
                }
            } else if (i == 40) {
                Log.I("UI State: registration success toast");
                DialogActivity.this.showToast(R.string.STR_REGISTRATION_SUCCESS);
                DialogActivity.this.finish();
            }
            super.handleMessage(message);
        }

        public void showDialog(int i) {
            showDialog(i, 0);
        }

        public void showDialog(int i, int i2) {
            Message message = new Message();
            message.what = i;
            message.arg1 = i2;
            sendMessage(message);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mUiHandler = new UiHandler();
        setFinishOnTouchOutside(false);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        try {
            if (this.mAlertDialog != null && this.mAlertDialog.isShowing()) {
                this.mAlertDialog.dismiss();
            }
        } catch (Exception e) {
            Log.E("Exception : " + e.toString());
        } catch (Throwable th) {
            this.mAlertDialog = null;
            mUiHandler = null;
            throw th;
        }
        this.mAlertDialog = null;
        mUiHandler = null;
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        finish();
    }

    public static UiHandler getUIHandler() {
        return mUiHandler;
    }

    /* access modifiers changed from: private */
    public void showError(int i) {
        try {
            FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
            Fragment findFragmentByTag = getFragmentManager().findFragmentByTag("dialog");
            if (findFragmentByTag != null) {
                beginTransaction.remove(findFragmentByTag);
            }
            beginTransaction.addToBackStack((String) null);
            MyAlertDialogFragment.newInstance(i).show(beginTransaction, "dialog");
        } catch (Exception e) {
            Log.E("Exception : " + e.toString());
            finish();
        }
    }

    public static class MyAlertDialogFragment extends DialogFragment {
        public static MyAlertDialogFragment newInstance(int i) {
            MyAlertDialogFragment myAlertDialogFragment = new MyAlertDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("msg", i);
            myAlertDialogFragment.setArguments(bundle);
            return myAlertDialogFragment;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            int i = getArguments().getInt("msg");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.FotaProviderTheme_Dialog);
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View inflate = layoutInflater.inflate(R.layout.dialog_title, (ViewGroup) null);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_dialog_title);
            View inflate2 = layoutInflater.inflate(R.layout.dialog_body, (ViewGroup) null);
            TextView textView2 = (TextView) inflate2.findViewById(R.id.tv_dialog_body);
            if (i != DeviceType.get().getTextType().getConnectionFailedMessageId()) {
                if (i == R.string.STR_ROAMING_WIFI_DISCONNECTED) {
                    textView.setText(R.string.STR_ROAMING_WIFI_DISCONNECTED_TITLE);
                } else {
                    textView.setText(DeviceType.get().getTextType().getTitleId());
                }
                builder.setCustomTitle(inflate);
            }
            textView2.setText(OperatorUtil.replaceToWLAN(i));
            builder.setView(inflate2).setOnKeyListener(new DialogInterface.OnKeyListener() {
                public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return DialogActivity.MyAlertDialogFragment.this.lambda$onCreateDialog$0$DialogActivity$MyAlertDialogFragment(dialogInterface, i, keyEvent);
                }
            });
            if (i == R.string.STR_ROAMING_WIFI_DISCONNECTED) {
                builder.setPositiveButton(R.string.STR_BTN_OK, new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DialogActivity.MyAlertDialogFragment.this.lambda$onCreateDialog$1$DialogActivity$MyAlertDialogFragment(dialogInterface, i);
                    }
                });
                builder.setNegativeButton(R.string.STR_BTN_CANCEL, new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DialogActivity.MyAlertDialogFragment.this.lambda$onCreateDialog$2$DialogActivity$MyAlertDialogFragment(dialogInterface, i);
                    }
                });
            } else {
                builder.setPositiveButton(R.string.STR_BTN_OK, new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DialogActivity.MyAlertDialogFragment.this.lambda$onCreateDialog$3$DialogActivity$MyAlertDialogFragment(dialogInterface, i);
                    }
                });
            }
            DialogActivity dialogActivity = (DialogActivity) getActivity();
            AlertDialog unused = dialogActivity.mAlertDialog = builder.create();
            dialogActivity.mAlertDialog.setCanceledOnTouchOutside(false);
            return dialogActivity.mAlertDialog;
        }

        public /* synthetic */ boolean lambda$onCreateDialog$0$DialogActivity$MyAlertDialogFragment(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            if (i != 4) {
                return false;
            }
            getActivity().finish();
            return true;
        }

        public /* synthetic */ void lambda$onCreateDialog$1$DialogActivity$MyAlertDialogFragment(DialogInterface dialogInterface, int i) {
            showWiFiSetting();
            getActivity().finish();
        }

        public /* synthetic */ void lambda$onCreateDialog$2$DialogActivity$MyAlertDialogFragment(DialogInterface dialogInterface, int i) {
            getActivity().finish();
        }

        public /* synthetic */ void lambda$onCreateDialog$3$DialogActivity$MyAlertDialogFragment(DialogInterface dialogInterface, int i) {
            getActivity().finish();
        }

        private void showWiFiSetting() {
            Log.I("show WiFi setting");
            Intent intent = new Intent(XCommonInterface.XCOMMON_INTENT_WIFI_SETTING);
            intent.setFlags(67108864);
            startActivity(intent);
        }
    }

    /* access modifiers changed from: protected */
    public void showToast(int i) {
        Toast toast = this.mToast;
        if (toast == null) {
            this.mToast = Toast.makeText(getApplicationContext(), getString(i), 0);
        } else {
            toast.setText(i);
        }
        this.mToast.show();
    }
}
