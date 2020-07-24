package com.samsung.accessory.neobeanmgr.module.aboutmenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.common.ui.OnSingleClickListener;
import com.samsung.accessory.neobeanmgr.common.util.BluetoothUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;
import com.samsung.accessory.neobeanmgr.module.mainmenu.GeneralActivity;
import com.samsung.android.sdk.mobileservice.social.group.provider.GroupInvitationContract;

public class AboutEarbudsActivity extends PermissionCheckActivity {
    private static final int LEFT = 0;
    private static final int RENAME_MAX_INPUT_LENGTH = 62;
    private static final int RIGHT = 1;
    private static final String TAG = "NeoBean_AboutEarbudsActivity";
    /* access modifiers changed from: private */
    public Button button;
    private TextView deviceName;
    private AppCompatButton editButton;
    public InputFilter inputFilter = new InputFilter() {
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if ("\n".equals(charSequence)) {
                return "";
            }
            if (!Util.hasEmoji(charSequence)) {
                return charSequence;
            }
            Toast.makeText(Application.getContext(), AboutEarbudsActivity.this.getString(R.string.about_earbuds_edit_invalid_character), 0).show();
            return spanned.subSequence(i3, i4);
        }
    };
    /* access modifiers changed from: private */
    public boolean invalidInputFlag = false;
    /* access modifiers changed from: private */
    public boolean isEnteredName = false;
    private TextView leftSerialNumber;
    public InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(62) {
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
            if (filter != null) {
                boolean unused = AboutEarbudsActivity.this.invalidInputFlag = true;
                Log.d(AboutEarbudsActivity.TAG, "lengthFilter : invalidInputFlag" + AboutEarbudsActivity.this.invalidInputFlag);
            }
            return filter;
        }
    };
    private View mLabelL;
    private View mLabelR;
    private View mLayoutLeftSerialNumber;
    private View mLayoutRightSerialNumber;
    /* access modifiers changed from: private */
    public String mMessage = GroupInvitationContract.Invitation.MESSAGE;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Can't fix incorrect switch cases order */
        public void onReceive(Context context, Intent intent) {
            char c;
            Log.d(AboutEarbudsActivity.TAG, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            switch (action.hashCode()) {
                case -2136954580:
                    if (action.equals(CoreService.ACTION_MSG_ID_DEBUG_SERIAL_NUMBER)) {
                        c = 3;
                        break;
                    }
                case -2043421558:
                    if (action.equals(CoreService.ACTION_MSG_ID_STATUS_UPDATED)) {
                        c = 2;
                        break;
                    }
                case -1354974214:
                    if (action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) {
                        c = 1;
                        break;
                    }
                case -415576694:
                    if (action.equals(CoreService.ACTION_DEVICE_CONNECTED)) {
                        c = 0;
                        break;
                    }
                case -46786983:
                    if (action.equals(CoreService.ACTION_MSG_ID_DEBUG_GET_ALL_DATA)) {
                        c = 4;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            if (c == 0 || c == 1 || c == 2 || c == 3 || c == 4) {
                AboutEarbudsActivity.this.updateUI();
            }
        }
    };
    private TextView rightSerialNumber;
    private TextView sw_version;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate()");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about_earbuds);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initView();
        registerReceiver();
        findViewById(R.id.menu_legal_information).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.LEGAL_INFORMATION, SA.Screen.ABOUT_EARBUDS);
                AboutEarbudsActivity aboutEarbudsActivity = AboutEarbudsActivity.this;
                aboutEarbudsActivity.startActivity(new Intent(aboutEarbudsActivity, LegalnformationActivity.class));
            }
        });
        findViewById(R.id.menu_battery_information).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.BATTERY_INFORMATION, SA.Screen.ABOUT_EARBUDS);
                AboutEarbudsActivity aboutEarbudsActivity = AboutEarbudsActivity.this;
                aboutEarbudsActivity.startActivity(new Intent(aboutEarbudsActivity, BatteryInformationActivity.class));
            }
        });
        findViewById(R.id.menu_reset_earbuds).setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.ELSE_RESET_EARBUDS, SA.Screen.ABOUT_EARBUDS);
                AboutEarbudsActivity aboutEarbudsActivity = AboutEarbudsActivity.this;
                aboutEarbudsActivity.startActivity(new Intent(aboutEarbudsActivity, GeneralActivity.class));
            }
        });
        updateResetMenu();
        updateEditDeviceName();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        SamsungAnalyticsUtil.sendPage(SA.Screen.ABOUT_EARBUDS);
        updateUI();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        super.onDestroy();
    }

    private void initView() {
        this.deviceName = (TextView) findViewById(R.id.device_name);
        this.editButton = (AppCompatButton) findViewById(R.id.edit_button);
        this.leftSerialNumber = (TextView) findViewById(R.id.left_serial_number);
        this.rightSerialNumber = (TextView) findViewById(R.id.right_serial_number);
        this.mLabelL = findViewById(R.id.label_left);
        this.mLabelR = findViewById(R.id.label_right);
        this.mLayoutLeftSerialNumber = findViewById(R.id.layout_left_serial_number);
        this.mLayoutRightSerialNumber = findViewById(R.id.layout_right_serial_number);
        this.sw_version = (TextView) findViewById(R.id.menu_sw_version);
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        Log.d(TAG, "updateUI()");
        updateEditDeviceName();
        updateResetMenu();
        updateDeviceName();
        updateSerialNumber();
        updateSWVersion();
    }

    private void updateEditDeviceName() {
        this.editButton = (AppCompatButton) findViewById(R.id.edit_button);
        if (!Util.isSamsungDevice() || !Application.getCoreService().isConnected()) {
            this.editButton.setVisibility(4);
        } else {
            this.editButton.setVisibility(0);
        }
        this.editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.DEVICE_NAME_EDIT, SA.Screen.ABOUT_EARBUDS);
                AboutEarbudsActivity.this.editDialog();
            }
        });
    }

    /* access modifiers changed from: private */
    public void editDialog() {
        Log.d(TAG, "editDialog");
        this.mMessage = BluetoothUtil.getAliasName(UhmFwUtil.getLastLaunchDeviceId());
        View inflate = View.inflate(this, R.layout.edit_text_rename, (ViewGroup) null);
        final TextView textView = (TextView) inflate.findViewById(R.id.text_invalid_reason);
        textView.setPaintFlags(textView.getPaintFlags() | 8);
        Application.getContext();
        ((InputMethodManager) getSystemService("input_method")).toggleSoftInput(2, 1);
        final EditText editText = (EditText) inflate.findViewById(R.id.editText_device_name);
        editText.setText(this.mMessage);
        editText.requestFocus();
        editText.selectAll();
        boolean z = false;
        editText.setFilters(new InputFilter[]{this.inputFilter, this.lengthFilter});
        editText.setPrivateImeOptions("inputType=PredictionOff;disableEmoticonInput=true;disableImage=true;disableSticker=true;disableGifKeyboard=true");
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                int length = editText.getText().toString().length();
                boolean unused = AboutEarbudsActivity.this.isEnteredName = length > 0;
                AboutEarbudsActivity.this.button.setEnabled(AboutEarbudsActivity.this.isEnteredName);
                if (AboutEarbudsActivity.this.invalidInputFlag || length >= 62) {
                    boolean unused2 = AboutEarbudsActivity.this.invalidInputFlag = false;
                    textView.setText(AboutEarbudsActivity.this.getResources().getQuantityString(R.plurals.about_earbuds_edit_invalid_length, 62, new Object[]{62}));
                    textView.setVisibility(0);
                    return;
                }
                editText.getBackground().clearColorFilter();
                textView.setVisibility(4);
            }
        });
        if (editText.getText().toString().length() > 0) {
            z = true;
        }
        this.isEnteredName = z;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) getString(R.string.about_earbuds_edit_title));
        builder.setView(inflate);
        builder.setNegativeButton((CharSequence) getApplicationContext().getString(R.string.cancel), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton((int) R.string.about_earbuds_edit_rename, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String unused = AboutEarbudsActivity.this.mMessage = editText.getText().toString();
                BluetoothUtil.setAliasName(UhmFwUtil.getLastLaunchDeviceId(), AboutEarbudsActivity.this.mMessage);
                AboutEarbudsActivity.this.updateDeviceName();
                dialogInterface.cancel();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                AboutEarbudsActivity.this.hideKeyboard();
            }
        });
        builder.setCancelable(true);
        this.button = builder.show().getButton(-1);
        this.button.setEnabled(this.isEnteredName);
    }

    /* access modifiers changed from: private */
    public void hideKeyboard() {
        Application.getContext();
        ((InputMethodManager) getSystemService("input_method")).toggleSoftInput(1, 0);
    }

    private void updateResetMenu() {
        if (Application.getCoreService().isConnected()) {
            findViewById(R.id.menu_reset_earbuds_title).setEnabled(true);
            findViewById(R.id.menu_reset_earbuds).setEnabled(true);
            return;
        }
        findViewById(R.id.menu_reset_earbuds_title).setEnabled(false);
        findViewById(R.id.menu_reset_earbuds).setEnabled(false);
    }

    /* access modifiers changed from: private */
    public void updateDeviceName() {
        String str;
        String lastLaunchDeviceId = UhmFwUtil.getLastLaunchDeviceId();
        if (lastLaunchDeviceId != null) {
            str = BluetoothUtil.getAliasName(lastLaunchDeviceId);
            if (str == null) {
                str = Application.getUhmDatabase().getDeviceName(lastLaunchDeviceId);
            }
        } else {
            str = null;
        }
        Log.d(TAG, "updateDeviceName() : name = " + str);
        if (str == null) {
            str = getString(R.string.app_name);
        }
        setDeviceNameWithLogoImage(str);
    }

    private void setDeviceNameWithLogoImage(String str) {
        Log.d(TAG, "setDeviceNameWithLogoImage() : " + str);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        setLogoImageSpan(spannableStringBuilder, str, "Galaxy");
        setLogoImageSpan(spannableStringBuilder, str, "Buds");
        setLogoImageSpan(spannableStringBuilder, str, "Live");
        this.deviceName.setText(spannableStringBuilder);
    }

    private void setLogoImageSpan(SpannableStringBuilder spannableStringBuilder, String str, String str2) {
        int indexOf = str.indexOf("Galaxy Buds Live");
        if (indexOf >= 0) {
            float textSize = this.deviceName.getTextSize();
            int indexOf2 = str.indexOf(str2, indexOf);
            if (indexOf2 >= 0) {
                char c = 65535;
                int hashCode = str2.hashCode();
                if (hashCode != 2081858) {
                    if (hashCode != 2368780) {
                        if (hashCode == 2125565744 && str2.equals("Galaxy")) {
                            c = 0;
                        }
                    } else if (str2.equals("Live")) {
                        c = 2;
                    }
                } else if (str2.equals("Buds")) {
                    c = 1;
                }
                int i = c != 0 ? c != 1 ? c != 2 ? 0 : R.drawable.logo_word_03_live : R.drawable.logo_word_02_buds : R.drawable.logo_word_01_galaxy;
                if (i != 0) {
                    Drawable newDrawable = getResources().getDrawable(i).getConstantState().newDrawable();
                    newDrawable.setBounds(0, 0, (int) ((((float) newDrawable.getIntrinsicWidth()) * textSize) / ((float) newDrawable.getIntrinsicHeight())), (int) textSize);
                    spannableStringBuilder.setSpan(new ImageSpan(newDrawable), indexOf2, str2.length() + indexOf2, 33);
                }
            }
        }
    }

    private void updateSerialNumber() {
        if (!Application.getCoreService().isConnected()) {
            setVisibilitySerialNumber(0, 8);
            setVisibilitySerialNumber(1, 8);
            return;
        }
        if (Application.getCoreService().getEarBudsInfo().batteryL > 0) {
            setVisibilitySerialNumber(0, 0);
        } else {
            setVisibilitySerialNumber(0, 8);
        }
        if (Application.getCoreService().getEarBudsInfo().batteryR > 0) {
            setVisibilitySerialNumber(1, 0);
        } else {
            setVisibilitySerialNumber(1, 8);
        }
    }

    private void setVisibilitySerialNumber(int i, int i2) {
        TextView textView = i == 0 ? this.leftSerialNumber : this.rightSerialNumber;
        View view = i == 0 ? this.mLabelL : this.mLabelR;
        View view2 = i == 0 ? this.mLayoutLeftSerialNumber : this.mLayoutRightSerialNumber;
        if (i2 == 0) {
            EarBudsInfo earBudsInfo = Application.getCoreService().getEarBudsInfo();
            textView.setText(i == 0 ? earBudsInfo.serialNumber_left : earBudsInfo.serialNumber_right);
        }
        view2.setVisibility(i2);
        view.setVisibility(i2);
    }

    private void updateSWVersion() {
        String str = Application.getCoreService().getEarBudsInfo().deviceSWVer;
        if (str == null) {
            str = "";
        }
        if (Application.getCoreService().getEarBudsFotaInfo().isFotaDM != 0) {
            str = str + ".DM";
        }
        this.sw_version.setText(String.format("%s : %s", new Object[]{getString(R.string.software_version), str}));
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_DEVICE_CONNECTED);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_DEBUG_SERIAL_NUMBER);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_DEBUG_GET_ALL_DATA);
        registerReceiver(this.mReceiver, intentFilter);
    }

    public boolean onSupportNavigateUp() {
        SamsungAnalyticsUtil.sendEvent(SA.Event.UP_BUTTON, SA.Screen.ABOUT_EARBUDS);
        finish();
        return true;
    }
}
