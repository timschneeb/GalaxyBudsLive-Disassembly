package com.samsung.accessory.neobeanmgr.module.setupwizard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.ui.OnSingleClickListener;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;
import com.samsung.accessory.neobeanmgr.module.home.HomeActivity;

public class TermsAndConditionsActivity extends PermissionCheckActivity {
    private static final String TAG = "NeoBean_TermsAndConditionsActivity";
    /* access modifiers changed from: private */
    public CheckBox mCheckBoxReport;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate()");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_terms_and_conditions);
        this.mCheckBoxReport = (CheckBox) findViewById(R.id.checkbox_report);
        findViewById(R.id.layout_checkbox_report).setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                TermsAndConditionsActivity.this.mCheckBoxReport.toggle();
            }
        });
        initTitle();
        findViewById(R.id.button_agree).setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                TermsAndConditionsActivity.this.onClickButtonAgree();
            }
        });
        initLayout();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        updateUI();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    private void initTitle() {
        String string = getString(R.string.congrats_on_your_new, new Object[]{getString(R.string.app_name)});
        ((TextView) findViewById(R.id.text_full_title)).setText(string);
        setTitle(string);
    }

    private void initText() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (Util.isGDPRCountry(Util.getCountryIso())) {
            LinkString linkString = new LinkString(getString(R.string.check_our_privacy_gdpr), 1);
            linkString.addLinkSpan(0, new StyleSpan(1));
            LinkString linkString2 = new LinkString(getString(R.string.by_continuing_gdpr), 1);
            linkString2.addLinkSpan(0, new StyleSpan(1));
            if (!Util.isTalkBackEnabled()) {
                linkString.addLinkSpan(0, new ClickableSpan() {
                    public void onClick(View view) {
                        TermsAndConditionsActivity.this.onClickPrivacyPolicy();
                    }
                });
                linkString2.addLinkSpan(0, new ClickableSpan() {
                    public void onClick(View view) {
                        TermsAndConditionsActivity.this.onClickEULA();
                    }
                });
            }
            spannableStringBuilder.append(linkString.toCharSequence()).append("\n\n").append(linkString2.toCharSequence());
        } else {
            LinkString linkString3 = new LinkString(getString(R.string.by_continuing), 2);
            linkString3.addLinkSpan(0, new StyleSpan(1));
            linkString3.addLinkSpan(1, new StyleSpan(1));
            if (!Util.isTalkBackEnabled()) {
                linkString3.addLinkSpan(0, new ClickableSpan() {
                    public void onClick(View view) {
                        TermsAndConditionsActivity.this.onClickEULA();
                    }
                });
                linkString3.addLinkSpan(1, new ClickableSpan() {
                    public void onClick(View view) {
                        TermsAndConditionsActivity.this.onClickPrivacyPolicy();
                    }
                });
            }
            spannableStringBuilder.append(linkString3.toCharSequence());
        }
        TextView textView = (TextView) findViewById(R.id.text_description1);
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(Util.isTalkBackEnabled() ? null : LinkMovementMethod.getInstance());
        if (!Util.isTalkBackEnabled()) {
            findViewById(R.id.layout_accessibility_links_01).setVisibility(8);
        } else {
            TextView textView2 = (TextView) findViewById(R.id.text_link_privacy_policy);
            TextView textView3 = (TextView) findViewById(R.id.text_link_eula);
            textView2.setPaintFlags(textView2.getPaintFlags() | 8);
            textView3.setPaintFlags(textView3.getPaintFlags() | 8);
            textView2.setOnClickListener(new OnSingleClickListener() {
                public void onSingleClick(View view) {
                    TermsAndConditionsActivity.this.onClickPrivacyPolicy();
                }
            });
            textView3.setOnClickListener(new OnSingleClickListener() {
                public void onSingleClick(View view) {
                    TermsAndConditionsActivity.this.onClickEULA();
                }
            });
            findViewById(R.id.layout_accessibility_links_01).setVisibility(0);
        }
        LinkString linkString4 = new LinkString(getString(R.string.you_can_also_check_the_required_permissions), 1);
        linkString4.addLinkSpan(0, new StyleSpan(1));
        if (!Util.isTalkBackEnabled()) {
            linkString4.addLinkSpan(0, new ClickableSpan() {
                public void onClick(View view) {
                    TermsAndConditionsActivity.this.onClickPermissions();
                }
            });
        }
        TextView textView4 = (TextView) findViewById(R.id.text_description2);
        textView4.setText(linkString4.toCharSequence());
        if (!Util.isTalkBackEnabled()) {
            textView4.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            textView4.setMovementMethod((MovementMethod) null);
        }
        TextView textView5 = (TextView) findViewById(R.id.text_link_permissions);
        if (!Util.isTalkBackEnabled()) {
            textView5.setVisibility(8);
        } else {
            textView5.setVisibility(0);
            textView5.setPaintFlags(textView5.getPaintFlags() | 8);
            textView5.setOnClickListener(new OnSingleClickListener() {
                public void onSingleClick(View view) {
                    TermsAndConditionsActivity.this.onClickPermissions();
                }
            });
        }
        String string = getString(R.string.report_diagnostic_info);
        String string2 = getString(R.string.optional, new Object[]{string});
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(string2);
        int indexOf = string2.indexOf(string);
        int length = string.length() + indexOf;
        spannableStringBuilder2.setSpan(new StyleSpan(1), indexOf, length, 33);
        if (!Util.isTalkBackEnabled()) {
            spannableStringBuilder2.setSpan(new ClickableSpan() {
                public void onClick(View view) {
                    TermsAndConditionsActivity.this.onClickReportDiagnosticInfo();
                }
            }, indexOf, length, 33);
        } else {
            spannableStringBuilder2.setSpan(new UnderlineSpan(), indexOf, length, 33);
        }
        TextView textView6 = (TextView) findViewById(R.id.text_diagnostic_info);
        textView6.setText(spannableStringBuilder2);
        if (!Util.isTalkBackEnabled()) {
            textView6.setMovementMethod(LinkMovementMethod.getInstance());
            return;
        }
        textView6.setMovementMethod((MovementMethod) null);
        textView6.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                TermsAndConditionsActivity.this.onClickReportDiagnosticInfo();
            }
        });
    }

    private void initLayout() {
        Log.d(TAG, "initLayout()");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_accessibility_links_01);
        if (Util.isGDPRCountry(Util.getCountryIso())) {
            View[] viewArr = new View[linearLayout.getChildCount()];
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                viewArr[i] = linearLayout.getChildAt(i);
            }
            linearLayout.removeAllViews();
            for (int length = viewArr.length - 1; length >= 0; length--) {
                linearLayout.addView(viewArr[length]);
            }
        }
    }

    private void updateUI() {
        Log.d(TAG, "updateUI()");
        initText();
        if (Preferences.getBoolean(PreferenceKey.SETUP_WIZARD_REPORT_DIAGNOSTIC_INFO, false)) {
            this.mCheckBoxReport.setChecked(true);
        }
    }

    /* access modifiers changed from: private */
    public void onClickPrivacyPolicy() {
        Log.d(TAG, "onClickPrivacyPolicy()");
        if ("kr".equalsIgnoreCase(Util.getCountryIso())) {
            PrivacyNotice.startOnlinePage(this);
        } else {
            startActivity(new Intent(this, NoticePrivacyPolicyActivity.class));
        }
    }

    /* access modifiers changed from: private */
    public void onClickEULA() {
        Log.d(TAG, "onClickEULA()");
        startActivity(new Intent(this, NoticeEULAActivity.class));
    }

    /* access modifiers changed from: private */
    public void onClickPermissions() {
        Log.d(TAG, "onClickPermissions()");
        startActivity(new Intent(this, PermissionsActivity.class));
    }

    /* access modifiers changed from: private */
    public void onClickReportDiagnosticInfo() {
        Log.d(TAG, "onClickReportDiagnosticInfo()");
        startActivity(new Intent(this, NoticeDiagnosticInfoActivity.class));
    }

    /* access modifiers changed from: private */
    public void onClickButtonAgree() {
        Log.d(TAG, "onClickButtonAgree()");
        SamsungAnalyticsUtil.setReportDiagnosticInfo(this.mCheckBoxReport.isChecked());
        SamsungAnalyticsUtil.sendPage(SA.Screen.TERMS_AND_CONDITIONS);
        SamsungAnalyticsUtil.sendEvent(SA.Event.T_AND_C_AGREE, SA.Screen.TERMS_AND_CONDITIONS, this.mCheckBoxReport.isChecked() ? "1" : "0");
        startActivityForResult(new Intent(this, MoreUsefulFeaturesActivity.class), 0);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Log.d(TAG, "onActivityResult() : requestCode=" + i + ", resultCode=" + i2);
        if (i2 == -1) {
            setResult(-1);
            startHomeActivity();
            finish();
        }
    }

    private void startHomeActivity() {
        Log.d(TAG, "startHomeActivity()");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.EXTRA_AUTO_CONNECT, true);
        intent.putExtra(HomeActivity.EXTRA_FROM_SETUPWIZARD, true);
        startActivity(intent);
    }

    class LinkString {
        private final SpannableString mSpannableString;

        public LinkString(String str, int i) {
            Object[] objArr = new Object[(i * 2)];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                objArr[i2] = i2 % 2 == 0 ? "<u>" : "</u>";
            }
            this.mSpannableString = new SpannableString(Html.fromHtml(String.format(str, objArr)));
        }

        public void addLinkSpan(int i, Object obj) {
            SpannableString spannableString = this.mSpannableString;
            Object[] spans = spannableString.getSpans(0, spannableString.length(), Object.class);
            if (spans != null && i < spans.length) {
                Object obj2 = spans[i];
                SpannableString spannableString2 = this.mSpannableString;
                spannableString2.setSpan(obj, spannableString2.getSpanStart(obj2), this.mSpannableString.getSpanEnd(obj2), 33);
            }
        }

        public CharSequence toCharSequence() {
            return this.mSpannableString;
        }
    }
}
