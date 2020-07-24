package com.samsung.accessory.neobeanmgr.core.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.core.appwidget.base.WidgetSettingBaseActivity;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WallpaperColorManager;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetUtil;

public class WidgetSettingMasterActivity extends WidgetSettingBaseActivity {
    private static final String TAG = (Application.TAG_ + WidgetSettingMasterActivity.class.getSimpleName());
    private ImageView mNoiseReductionImage;
    private TextView mNoiseReductionText;
    private TextView mTitleText;
    private ImageView mTouchpadLockImage;
    private TextView mTouchpadLockText;
    private ImageView mWidgetBackground;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate()");
        setWidgetPreview(R.layout.widget_view_master);
        WallpaperColorManager.initWallpaperColor(this);
        init();
        initView();
    }

    private void init() {
        this.mWidgetBackground = (ImageView) getWidgetPreview().findViewById(R.id.widget_background);
        this.mTitleText = (TextView) getWidgetPreview().findViewById(R.id.widget_text_device_bt_name);
        this.mNoiseReductionText = (TextView) getWidgetPreview().findViewById(R.id.text_widget_master_noise_reduction);
        this.mTouchpadLockText = (TextView) getWidgetPreview().findViewById(R.id.text_widget_master_touchpad_lock);
        this.mNoiseReductionImage = (ImageView) getWidgetPreview().findViewById(R.id.image_widget_master_noise_reduction);
        this.mTouchpadLockImage = (ImageView) getWidgetPreview().findViewById(R.id.image_widget_master_touchpad_lock);
    }

    private void initView() {
        this.mTitleText.setText(WidgetUtil.getDeviceAliasName(getApplicationContext()));
        boolean isConnected = Application.getCoreService().isConnected();
        int i = R.drawable.widget_lock_touchpad_off;
        int i2 = R.drawable.widget_noise_reduction_off;
        if (isConnected) {
            ImageView imageView = this.mNoiseReductionImage;
            Resources resources = getResources();
            if (WidgetUtil.getNoiseReductionEnabled(this)) {
                i2 = R.drawable.widget_noise_reduction_on;
            }
            imageView.setImageDrawable(resources.getDrawable(i2));
            ImageView imageView2 = this.mTouchpadLockImage;
            Resources resources2 = getResources();
            if (WidgetUtil.getTouchpadLockEnabled(this)) {
                i = R.drawable.widget_lock_touchpad_on;
            }
            imageView2.setImageDrawable(resources2.getDrawable(i));
            this.mNoiseReductionImage.setAlpha(1.0f);
            this.mTouchpadLockImage.setAlpha(1.0f);
            this.mNoiseReductionText.setAlpha(1.0f);
            this.mTouchpadLockText.setAlpha(1.0f);
        } else {
            this.mNoiseReductionImage.setImageDrawable(getResources().getDrawable(R.drawable.widget_noise_reduction_off));
            this.mTouchpadLockImage.setImageDrawable(getResources().getDrawable(R.drawable.widget_lock_touchpad_off));
            this.mNoiseReductionImage.setAlpha(0.4f);
            this.mTouchpadLockImage.setAlpha(0.4f);
            this.mNoiseReductionText.setAlpha(0.4f);
            this.mTouchpadLockText.setAlpha(0.4f);
        }
        onUpdatedAlpha(getWidgetInfo().alpha);
        onUpdatedColor(getWidgetInfo().color);
        onUpdatedDarkMode(getWidgetInfo().darkmode);
    }

    public Class getProviderClass() {
        return WidgetMasterProvider.class;
    }

    public void onUpdatedAlpha(int i) {
        this.mWidgetBackground.setImageAlpha(255 - ((i * 255) / 100));
        onUpdatedColor(getWidgetInfo().color);
    }

    public void onUpdatedColor(int i) {
        int widgetColor = WidgetUtil.getWidgetColor(this, getWidgetInfo());
        this.mWidgetBackground.setColorFilter(i);
        if (widgetColor == -16777216) {
            this.mTitleText.setTextColor(getResources().getColor(R.color.title_text_normal_color));
            this.mNoiseReductionText.setTextColor(getResources().getColor(R.color.title_text_normal_color));
            this.mTouchpadLockText.setTextColor(getResources().getColor(R.color.title_text_normal_color));
        } else if (widgetColor == -1) {
            this.mTitleText.setTextColor(getResources().getColor(R.color.color_black));
            this.mNoiseReductionText.setTextColor(getResources().getColor(R.color.color_black));
            this.mTouchpadLockText.setTextColor(getResources().getColor(R.color.color_black));
        }
    }

    public void onUpdatedDarkMode(boolean z) {
        int i;
        if (WidgetUtil.isDeviceDarkMode(getApplicationContext())) {
            if (z) {
                i = -16777216;
            } else {
                i = getWidgetInfo().color;
            }
            onUpdatedColor(i);
        }
    }

    public void updateWidget(int i) {
        AppWidgetManager.getInstance(this).updateAppWidget(i, new WidgetMasterProvider().getRemoteView(this, i));
    }
}
