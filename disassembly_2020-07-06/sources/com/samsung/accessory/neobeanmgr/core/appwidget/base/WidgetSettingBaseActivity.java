package com.samsung.accessory.neobeanmgr.core.appwidget.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SeslSeekBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetInfo;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetInfoManager;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetUtil;

public abstract class WidgetSettingBaseActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public SeslSeekBar mBackgroundAlphaSeekBar;
    /* access modifiers changed from: private */
    public TextView mBackgroundAlphaTextView;
    private LinearLayout mWidgetDarkModeLayout;
    /* access modifiers changed from: private */
    public SwitchCompat mWidgetDarkModeSwitch;
    private int mWidgetId;
    /* access modifiers changed from: private */
    public WidgetInfo mWidgetInfo;
    private FrameLayout mWidgetPreview;
    /* access modifiers changed from: private */
    public RadioButton mWidgetStyleRadioBlack;
    private RadioGroup mWidgetStyleRadioGroup;
    /* access modifiers changed from: private */
    public RadioButton mWidgetStyleRadioWhite;

    public abstract Class getProviderClass();

    public abstract void onUpdatedAlpha(int i);

    public abstract void onUpdatedColor(int i);

    public abstract void onUpdatedDarkMode(boolean z);

    public abstract void updateWidget(int i);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_widget_setting);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        init();
        initView();
        initListener();
    }

    private void init() {
        this.mWidgetPreview = (FrameLayout) findViewById(R.id.view_widget_preview);
        this.mWidgetStyleRadioGroup = (RadioGroup) findViewById(R.id.radio_group_widget_background_color_type);
        this.mWidgetStyleRadioWhite = (RadioButton) findViewById(R.id.radio_widget_setting_background_color_white);
        this.mWidgetStyleRadioBlack = (RadioButton) findViewById(R.id.radio_widget_setting_background_color_black);
        this.mBackgroundAlphaTextView = (TextView) findViewById(R.id.text_widget_background_alpha);
        this.mBackgroundAlphaSeekBar = (SeslSeekBar) findViewById(R.id.seekbar_widget_background_alpha);
        this.mWidgetDarkModeLayout = (LinearLayout) findViewById(R.id.layout_widget_base_dark_mode);
        this.mWidgetDarkModeSwitch = (SwitchCompat) findViewById(R.id.switch_widget_base_dark_mode);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mWidgetId = extras.getInt("appWidgetId", 0);
        }
        this.mWidgetInfo = new WidgetInfoManager(this, getProviderClass()).getWidgetInfo(this.mWidgetId);
    }

    private void initListener() {
        this.mBackgroundAlphaSeekBar.setOnSeekBarChangeListener(new SeslSeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeslSeekBar seslSeekBar) {
            }

            public void onStopTrackingTouch(SeslSeekBar seslSeekBar) {
            }

            public void onProgressChanged(SeslSeekBar seslSeekBar, int i, boolean z) {
                WidgetSettingBaseActivity.this.mWidgetInfo.alpha = i;
                WidgetSettingBaseActivity.this.mBackgroundAlphaTextView.setText(i + "%");
                WidgetSettingBaseActivity.this.onUpdatedAlpha(i);
            }
        });
        this.mWidgetStyleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_widget_setting_background_color_black:
                        WidgetSettingBaseActivity.this.mWidgetInfo.color = -16777216;
                        WidgetSettingBaseActivity.this.onUpdatedColor(-16777216);
                        return;
                    case R.id.radio_widget_setting_background_color_white:
                        WidgetSettingBaseActivity.this.mWidgetInfo.color = -1;
                        WidgetSettingBaseActivity.this.onUpdatedColor(-1);
                        return;
                    default:
                        return;
                }
            }
        });
        this.mWidgetDarkModeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WidgetSettingBaseActivity.this.mWidgetDarkModeSwitch.performClick();
            }
        });
        this.mWidgetDarkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                WidgetSettingBaseActivity.this.mWidgetInfo.darkmode = z;
                if (WidgetUtil.isDeviceDarkMode(WidgetSettingBaseActivity.this.getApplicationContext())) {
                    WidgetSettingBaseActivity.this.mWidgetStyleRadioWhite.setEnabled(!z);
                    WidgetSettingBaseActivity.this.mWidgetStyleRadioBlack.setEnabled(!z);
                    WidgetSettingBaseActivity.this.mBackgroundAlphaSeekBar.setEnabled(!z);
                    WidgetSettingBaseActivity.this.mBackgroundAlphaSeekBar.setAlpha(!z ? 1.0f : 0.4f);
                    WidgetSettingBaseActivity.this.onUpdatedDarkMode(z);
                }
            }
        });
    }

    private void initView() {
        if (this.mWidgetInfo.color == -1) {
            this.mWidgetStyleRadioWhite.setChecked(true);
            this.mWidgetStyleRadioBlack.setChecked(false);
        } else {
            this.mWidgetStyleRadioWhite.setChecked(false);
            this.mWidgetStyleRadioBlack.setChecked(true);
        }
        this.mBackgroundAlphaTextView.setText(this.mWidgetInfo.alpha + "%");
        this.mBackgroundAlphaSeekBar.setProgress(this.mWidgetInfo.alpha);
        this.mWidgetDarkModeSwitch.setChecked(this.mWidgetInfo.darkmode);
        if (WidgetUtil.isDeviceDarkMode(getApplicationContext())) {
            this.mWidgetStyleRadioWhite.setEnabled(!this.mWidgetInfo.darkmode);
            this.mWidgetStyleRadioBlack.setEnabled(!this.mWidgetInfo.darkmode);
            this.mBackgroundAlphaSeekBar.setEnabled(!this.mWidgetInfo.darkmode);
            this.mBackgroundAlphaSeekBar.setAlpha(!this.mWidgetInfo.darkmode ? 1.0f : 0.4f);
        }
        updateConfiguration(getResources().getConfiguration());
    }

    private void updateConfiguration(Configuration configuration) {
        if (configuration.orientation == 1) {
            ((LinearLayout) findViewById(R.id.layout_widget_base_container)).setOrientation(1);
        } else {
            ((LinearLayout) findViewById(R.id.layout_widget_base_container)).setOrientation(0);
        }
    }

    /* access modifiers changed from: protected */
    public WidgetInfo getWidgetInfo() {
        return this.mWidgetInfo;
    }

    /* access modifiers changed from: protected */
    public FrameLayout getWidgetPreview() {
        return this.mWidgetPreview;
    }

    /* access modifiers changed from: protected */
    public void setWidgetPreview(int i) {
        LayoutInflater.from(this).inflate(i, this.mWidgetPreview);
    }

    /* access modifiers changed from: protected */
    public void setPreviewWidth(int i) {
        ViewGroup.LayoutParams layoutParams = this.mWidgetPreview.getLayoutParams();
        layoutParams.width = i;
        this.mWidgetPreview.setLayoutParams(layoutParams);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void finish() {
        saveWidgetInfo();
        updateWidget(this.mWidgetId);
        super.finish();
    }

    private void saveWidgetInfo() {
        new WidgetInfoManager(this, getProviderClass()).setWidgetInfo(this.mWidgetId, this.mWidgetInfo);
        Intent intent = new Intent();
        intent.putExtra("appWidgetId", this.mWidgetId);
        setResult(-1, intent);
    }
}
