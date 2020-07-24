package com.samsung.accessory.neobeanmgr.core.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.core.appwidget.base.WidgetBaseProvider;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetConstants;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetInfo;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetInfoManager;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetSizeManager;
import com.samsung.accessory.neobeanmgr.core.appwidget.util.WidgetUtil;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;

public class WidgetMasterProvider extends WidgetBaseProvider {
    private static final String TAG = (Application.TAG_ + WidgetMasterProvider.class.getSimpleName());

    /* JADX WARNING: Can't fix incorrect switch cases order */
    public void onReceive(Context context, Intent intent) {
        char c;
        super.onReceive(context, intent);
        String action = intent.getAction();
        switch (action.hashCode()) {
            case -1113631604:
                if (action.equals(WidgetConstants.WIDGET_ACTION_UPDATE_NOISE_REDUCTION)) {
                    c = 2;
                    break;
                }
            case -689938766:
                if (action.equals("android.appwidget.action.APPWIDGET_UPDATE_OPTIONS")) {
                    c = 0;
                    break;
                }
            case -539250705:
                if (action.equals(WidgetConstants.WIDGET_ACTION_DISCONNECTED)) {
                    c = 4;
                    break;
                }
            case 1619576947:
                if (action.equals("android.appwidget.action.APPWIDGET_UPDATE")) {
                    c = 1;
                    break;
                }
            case 2088479302:
                if (action.equals(WidgetConstants.WIDGET_ACTION_UPDATE_LOCK_TOUCHPAD)) {
                    c = 3;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            Log.d(TAG, "onReceive : ACTION_APPWIDGET_OPTIONS_CHANGED");
            int i = intent.getExtras().getInt("appWidgetId");
            if (i != 0) {
                updateUI(context, i);
            }
        } else if (c == 1) {
            updateUI(context);
        } else if (c == 2) {
            Log.d(TAG, "onReceive : WIDGET_ACTION_UPDATE_NOISE_REDUCTION");
            if (WidgetUtil.isConnected(context)) {
                setNoiseReduction(context, !WidgetUtil.getNoiseReductionEnabled(context));
            } else {
                updateUI(context);
            }
        } else if (c == 3) {
            Log.d(TAG, "onReceive : WIDGET_ACTION_UPDATE_LOCK_TOUCHPAD");
            if (WidgetUtil.isConnected(context)) {
                setTouchpadLock(context, !WidgetUtil.getTouchpadLockEnabled(context));
            } else {
                updateUI(context);
            }
        } else if (c == 4) {
            Log.d(TAG, "onReceive : WIDGET_ACTION_DISCONNECTED");
            WidgetUtil.showSingleToast(context, R.string.disconnec_text);
        }
    }

    private void setNoiseReduction(Context context, boolean z) {
        WidgetUtil.setNoiseReduction(context, z);
        SamsungAnalyticsUtil.sendEvent(SA.Event.WIDGET_ANC, SA.Screen.QUICK_CONTROL_WIDGET, z ? "b" : "a");
    }

    private void setTouchpadLock(Context context, boolean z) {
        WidgetUtil.setTouchpadLock(context, z);
        SamsungAnalyticsUtil.sendEvent(SA.Event.WIDGET_LOCK_TOUCHPAD, SA.Screen.QUICK_CONTROL_WIDGET, z ? "b" : "a");
    }

    public RemoteViews getRemoteView(Context context, int i) {
        int i2;
        Context context2 = context;
        int i3 = i;
        WidgetInfo widgetInfo = new WidgetInfoManager(context2, getClass()).getWidgetInfo(i3);
        boolean z = context.getResources().getConfiguration().orientation == 1;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_view_master);
        int widgetBgColor = WidgetUtil.getWidgetBgColor(context2, widgetInfo);
        if (WidgetUtil.getWidgetColor(context2, widgetInfo) != -16777216) {
            i2 = context.getResources().getColor(R.color.widget_title_color_style_white);
        } else {
            i2 = context.getResources().getColor(R.color.widget_title_color_style_black);
        }
        remoteViews.setTextViewText(R.id.widget_text_device_bt_name, WidgetUtil.getDeviceAliasName(context));
        remoteViews.setInt(R.id.widget_text_device_bt_name, "setTextColor", i2);
        remoteViews.setInt(R.id.widget_background, "setColorFilter", widgetBgColor);
        remoteViews.setInt(R.id.widget_background, "setImageAlpha", 255 - ((widgetInfo.alpha * 255) / 100));
        boolean isConnected = WidgetUtil.isConnected(context);
        int i4 = R.drawable.widget_noise_reduction_off;
        if (isConnected) {
            if (WidgetUtil.getNoiseReductionEnabled(context)) {
                i4 = R.drawable.widget_noise_reduction_on;
            }
            remoteViews.setImageViewResource(R.id.image_widget_master_noise_reduction, i4);
            remoteViews.setImageViewResource(R.id.image_widget_master_touchpad_lock, WidgetUtil.getTouchpadLockEnabled(context) ? R.drawable.widget_lock_touchpad_on : R.drawable.widget_lock_touchpad_off);
            remoteViews.setInt(R.id.image_widget_master_noise_reduction, "setAlpha", 255);
            remoteViews.setOnClickPendingIntent(R.id.switch_widget_master_noise_reduction, getPendingIntent(context2, WidgetConstants.WIDGET_ACTION_UPDATE_NOISE_REDUCTION));
            remoteViews.setInt(R.id.image_widget_master_touchpad_lock, "setAlpha", 255);
            remoteViews.setOnClickPendingIntent(R.id.switch_widget_master_touchpad_lock, getPendingIntent(context2, WidgetConstants.WIDGET_ACTION_UPDATE_LOCK_TOUCHPAD));
            remoteViews.setInt(R.id.text_widget_master_noise_reduction, "setTextColor", i2);
            remoteViews.setInt(R.id.text_widget_master_touchpad_lock, "setTextColor", i2);
            StringBuilder sb = new StringBuilder();
            sb.append(context2.getString(R.string.settings_noise_reduction_title));
            sb.append(", ");
            sb.append(context2.getString(WidgetUtil.getNoiseReductionEnabled(context) ? R.string.va_on : R.string.va_off));
            remoteViews.setContentDescription(R.id.image_widget_master_noise_reduction, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(context2.getString(R.string.settings_touchpad_menu1));
            sb2.append(", ");
            sb2.append(context2.getString(WidgetUtil.getTouchpadLockEnabled(context) ? R.string.va_on : R.string.va_off));
            remoteViews.setContentDescription(R.id.image_widget_master_touchpad_lock, sb2.toString());
        } else {
            remoteViews.setImageViewResource(R.id.image_widget_master_noise_reduction, R.drawable.widget_noise_reduction_off);
            remoteViews.setImageViewResource(R.id.image_widget_master_touchpad_lock, R.drawable.widget_lock_touchpad_off);
            remoteViews.setOnClickPendingIntent(R.id.switch_widget_master_noise_reduction, getPendingIntent(context2, WidgetConstants.WIDGET_ACTION_DISCONNECTED));
            remoteViews.setOnClickPendingIntent(R.id.switch_widget_master_touchpad_lock, getPendingIntent(context2, WidgetConstants.WIDGET_ACTION_DISCONNECTED));
            remoteViews.setInt(R.id.image_widget_master_noise_reduction, "setAlpha", 102);
            remoteViews.setInt(R.id.image_widget_master_touchpad_lock, "setAlpha", 102);
            remoteViews.setInt(R.id.text_widget_master_noise_reduction, "setTextColor", WidgetUtil.makeAlphaColor(i2, 102));
            remoteViews.setInt(R.id.text_widget_master_touchpad_lock, "setTextColor", WidgetUtil.makeAlphaColor(i2, 102));
            remoteViews.setContentDescription(R.id.image_widget_master_noise_reduction, context2.getString(R.string.settings_noise_reduction_title) + ", " + context2.getString(R.string.va_off));
            remoteViews.setContentDescription(R.id.image_widget_master_touchpad_lock, context2.getString(R.string.settings_touchpad_menu1) + ", " + context2.getString(R.string.va_off));
        }
        Bundle appWidgetOptions = AppWidgetManager.getInstance(context).getAppWidgetOptions(i3);
        if (z) {
            WidgetSizeManager widgetSizeManager = new WidgetSizeManager(1, (float) appWidgetOptions.getInt("appWidgetMinWidth"), (float) appWidgetOptions.getInt("appWidgetMaxHeight"));
            float ratio = widgetSizeManager.getRatio();
            remoteViews.setTextViewTextSize(R.id.widget_text_device_bt_name, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portrait_device_bt_name_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_master_noise_reduction, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portrait_master_title_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_master_touchpad_lock, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portrait_master_title_text_size)) * ratio);
            remoteViews.setViewPadding(R.id.layout_widget_master_container, (int) widgetSizeManager.getPaddingLeftPixel(), (int) widgetSizeManager.getPaddingTopPixel(), (int) widgetSizeManager.getPaddingLeftPixel(), (int) widgetSizeManager.getPaddingTopPixel());
        } else {
            WidgetSizeManager widgetSizeManager2 = new WidgetSizeManager(4, (float) appWidgetOptions.getInt("appWidgetMaxWidth"), (float) appWidgetOptions.getInt("appWidgetMinHeight"));
            float ratio2 = widgetSizeManager2.getRatio();
            remoteViews.setTextViewTextSize(R.id.widget_text_device_bt_name, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_device_bt_name_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_master_noise_reduction, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_master_title_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_master_touchpad_lock, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_master_title_text_size)) * ratio2);
            remoteViews.setViewPadding(R.id.layout_widget_master_container, 0, (int) widgetSizeManager2.getPaddingTopPixel(), 0, (int) widgetSizeManager2.getPaddingTopPixel());
        }
        return remoteViews;
    }
}
