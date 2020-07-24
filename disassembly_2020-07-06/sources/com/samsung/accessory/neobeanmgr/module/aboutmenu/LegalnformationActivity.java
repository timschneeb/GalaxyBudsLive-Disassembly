package com.samsung.accessory.neobeanmgr.module.aboutmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;
import com.samsung.accessory.neobeanmgr.module.setupwizard.AssetString;
import com.samsung.accessory.neobeanmgr.module.setupwizard.NoticeDiagnosticInfoActivity;
import com.samsung.accessory.neobeanmgr.module.setupwizard.PrivacyNotice;

public class LegalnformationActivity extends PermissionCheckActivity {
    private static final String TAG = "NeoBean_LegalnformationActivity";
    /* access modifiers changed from: private */
    public Button button;
    /* access modifiers changed from: private */
    public boolean isCheckedAgree;
    private Context mContext;
    LinearLayout reportDiagnosticInfoLayout;
    SwitchCompat reportDiagnosticInfoSwitch;
    private String samsungLegalMessage;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        setContentView((int) R.layout.activity_legal_information);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((CharSequence) this.mContext.getString(R.string.about_earbuds_legal_information));
        prepareMessage();
        this.reportDiagnosticInfoSwitch = (SwitchCompat) findViewById(R.id.switch_report_diagnostic_info);
        this.reportDiagnosticInfoLayout = (LinearLayout) findViewById(R.id.layout_report_diagnostic_info_switch);
        this.reportDiagnosticInfoSwitch.setChecked(Preferences.getBoolean(PreferenceKey.SETUP_WIZARD_REPORT_DIAGNOSTIC_INFO, false));
        findViewById(R.id.menu_samsung_legal).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LegalnformationActivity.this.alertDialog();
            }
        });
        findViewById(R.id.menu_privacy_policy).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ("kr".equalsIgnoreCase(Util.getCountryIso())) {
                    PrivacyNotice.startOnlinePage(LegalnformationActivity.this);
                } else {
                    LegalnformationActivity.this.showPrivacyNoticeDialog();
                }
            }
        });
        this.reportDiagnosticInfoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Preferences.getBoolean(PreferenceKey.SETUP_WIZARD_REPORT_DIAGNOSTIC_INFO, false)) {
                    if (!z) {
                        SamsungAnalyticsUtil.setReportDiagnosticInfo(false);
                    }
                } else if (z) {
                    LegalnformationActivity.this.reportDiagnosticInfoSwitch.setChecked(false);
                    LegalnformationActivity.this.diagnosticDialog();
                }
            }
        });
        this.reportDiagnosticInfoLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LegalnformationActivity.this.reportDiagnosticInfoSwitch.setChecked(!LegalnformationActivity.this.reportDiagnosticInfoSwitch.isChecked());
            }
        });
    }

    /* access modifiers changed from: private */
    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) this.mContext.getString(R.string.legal_info_samsung_legal));
        builder.setMessage((CharSequence) this.samsungLegalMessage);
        builder.setPositiveButton((int) R.string.ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setCancelable(true);
        builder.show();
    }

    /* access modifiers changed from: private */
    public void showPrivacyNoticeDialog() {
        Log.d(TAG, "showPrivacyNoticeDialog()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) this.mContext.getString(R.string.privacy_policy));
        builder.setMessage((CharSequence) PrivacyNotice.getString());
        builder.setCancelable(true);
        builder.setPositiveButton((int) R.string.ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(LegalnformationActivity.TAG, "showPrivacyNoticeDialog() : OK");
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void prepareMessage() {
        this.samsungLegalMessage = AssetString.getStringEULA();
    }

    /* access modifiers changed from: private */
    public void diagnosticDialog() {
        String string = getString(R.string.optional, new Object[]{getString(R.string.report_diagnostic_info_agree_description)});
        this.isCheckedAgree = Preferences.getBoolean(PreferenceKey.SETUP_WIZARD_REPORT_DIAGNOSTIC_INFO, false);
        ScrollView scrollView = (ScrollView) View.inflate(this, R.layout.check_box_diagnostic_info, (ViewGroup) null);
        CheckBox checkBox = (CheckBox) scrollView.findViewById(R.id.checkbox_agree);
        checkBox.setText(string);
        checkBox.setChecked(this.isCheckedAgree);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                boolean unused = LegalnformationActivity.this.isCheckedAgree = z;
                if (LegalnformationActivity.this.isCheckedAgree) {
                    LegalnformationActivity.this.button.setEnabled(true);
                } else {
                    LegalnformationActivity.this.button.setEnabled(false);
                }
            }
        });
        TextView textView = (TextView) scrollView.findViewById(R.id.text_diagnostic_data);
        textView.setPaintFlags(textView.getPaintFlags() | 8);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LegalnformationActivity.this.onClickReportDiagnosticInfo();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) getApplicationContext().getString(R.string.send_diagnostic_data));
        builder.setCancelable(false);
        builder.setView((View) scrollView);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                dialogInterface.dismiss();
                return true;
            }
        });
        builder.setNegativeButton((CharSequence) getApplicationContext().getString(R.string.cancel), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton((CharSequence) getApplicationContext().getString(R.string.ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                SamsungAnalyticsUtil.setReportDiagnosticInfo(LegalnformationActivity.this.isCheckedAgree);
                if (LegalnformationActivity.this.isCheckedAgree) {
                    LegalnformationActivity.this.reportDiagnosticInfoSwitch.setChecked(true);
                }
            }
        });
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                Button unused = LegalnformationActivity.this.button = create.getButton(-1);
                if (LegalnformationActivity.this.button == null) {
                    return;
                }
                if (LegalnformationActivity.this.isCheckedAgree) {
                    LegalnformationActivity.this.button.setEnabled(true);
                } else {
                    LegalnformationActivity.this.button.setEnabled(false);
                }
            }
        });
        create.show();
    }

    /* access modifiers changed from: private */
    public void onClickReportDiagnosticInfo() {
        Log.d(TAG, "onClickReportDiagnosticInfo()");
        startActivity(new Intent(this, NoticeDiagnosticInfoActivity.class));
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
