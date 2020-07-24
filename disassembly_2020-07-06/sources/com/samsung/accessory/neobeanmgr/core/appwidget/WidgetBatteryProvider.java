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

public class WidgetBatteryProvider extends WidgetBaseProvider {
    private static final String TAG = (Application.TAG_ + WidgetBatteryProvider.class.getSimpleName());

    /* JADX WARNING: Can't fix incorrect switch cases order */
    public void onReceive(Context context, Intent intent) {
        char c;
        super.onReceive(context, intent);
        String action = intent.getAction();
        switch (action.hashCode()) {
            case -689938766:
                if (action.equals("android.appwidget.action.APPWIDGET_UPDATE_OPTIONS")) {
                    c = 0;
                    break;
                }
            case -539250705:
                if (action.equals(WidgetConstants.WIDGET_ACTION_DISCONNECTED)) {
                    c = 2;
                    break;
                }
            case 762897642:
                if (action.equals(WidgetConstants.WIDGET_ACTION_START_LAUNCH_ACTIVITY)) {
                    c = 3;
                    break;
                }
            case 1619576947:
                if (action.equals("android.appwidget.action.APPWIDGET_UPDATE")) {
                    c = 1;
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
            Log.d(TAG, "onReceive : WIDGET_ACTION_DISCONNECTED");
            WidgetUtil.showSingleToast(context, R.string.disconnec_text);
            SamsungAnalyticsUtil.sendEvent("6672", SA.Screen.BATTERY_WIDGET);
        } else if (c == 3) {
            Log.d(TAG, "onReceive : WIDGET_ACTION_START_LAUNCH_ACTIVITY");
            WidgetUtil.startActivity(context);
            updateUI(context);
            SamsungAnalyticsUtil.sendEvent("6672", SA.Screen.BATTERY_WIDGET);
        }
    }

    /* access modifiers changed from: protected */
    public RemoteViews getRemoteView(Context context, int i) {
        int i2;
        int i3;
        String str;
        String str2;
        Context context2 = context;
        int i4 = i;
        WidgetInfo widgetInfo = new WidgetInfoManager(context2, getClass()).getWidgetInfo(i4);
        boolean z = context.getResources().getConfiguration().orientation == 1;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_view_battery);
        int widgetBgColor = WidgetUtil.getWidgetBgColor(context2, widgetInfo);
        if (WidgetUtil.getWidgetColor(context2, widgetInfo) != -16777216) {
            i3 = context.getResources().getColor(R.color.widget_title_color_style_white);
            i2 = context.getResources().getColor(R.color.widget_device_name_color_style_white);
            remoteViews.setImageViewResource(R.id.image_widget_device_left, R.drawable.widget_buds_left_white);
            remoteViews.setImageViewResource(R.id.image_widget_device_right, R.drawable.widget_buds_right_white);
            remoteViews.setImageViewResource(R.id.image_widget_common_left, R.drawable.widget_buds_left_white);
            remoteViews.setImageViewResource(R.id.image_widget_common_right, R.drawable.widget_buds_right_white);
            remoteViews.setImageViewResource(R.id.image_widget_cradle, R.drawable.widget_buds_cradle_white);
        } else {
            i3 = context.getResources().getColor(R.color.widget_title_color_style_black);
            i2 = context.getResources().getColor(R.color.widget_device_name_color_style_black);
            remoteViews.setImageViewResource(R.id.image_widget_device_left, R.drawable.widget_buds_left_black);
            remoteViews.setImageViewResource(R.id.image_widget_device_right, R.drawable.widget_buds_right_black);
            remoteViews.setImageViewResource(R.id.image_widget_common_left, R.drawable.widget_buds_left_black);
            remoteViews.setImageViewResource(R.id.image_widget_common_right, R.drawable.widget_buds_right_black);
            remoteViews.setImageViewResource(R.id.image_widget_cradle, R.drawable.widget_buds_cradle_black);
        }
        remoteViews.setTextViewText(R.id.widget_text_device_bt_name, WidgetUtil.getDeviceAliasName(context));
        remoteViews.setInt(R.id.widget_text_device_bt_name, "setTextColor", i3);
        remoteViews.setInt(R.id.text_widget_device_left, "setTextColor", i2);
        remoteViews.setInt(R.id.text_widget_device_right, "setTextColor", i2);
        remoteViews.setInt(R.id.text_widget_common, "setTextColor", i2);
        remoteViews.setInt(R.id.text_widget_battery_gauge_left, "setTextColor", i3);
        remoteViews.setInt(R.id.text_widget_battery_gauge_right, "setTextColor", i3);
        remoteViews.setInt(R.id.text_widget_battery_gauge_common, "setTextColor", i3);
        remoteViews.setInt(R.id.text_widget_battery_gauge_cradle, "setTextColor", i3);
        remoteViews.setInt(R.id.widget_background, "setColorFilter", widgetBgColor);
        remoteViews.setInt(R.id.widget_background, "setImageAlpha", 255 - ((widgetInfo.alpha * 255) / 100));
        boolean isConnectedLeftDevice = WidgetUtil.isConnectedLeftDevice(context);
        boolean isConnectedRightDevice = WidgetUtil.isConnectedRightDevice(context);
        boolean isConnectedCradle = WidgetUtil.isConnectedCradle(context);
        boolean isCommonBattery = WidgetUtil.isCommonBattery(context);
        if (WidgetUtil.isConnected(context)) {
            remoteViews.setTextViewText(R.id.text_widget_battery_gauge_left, WidgetUtil.getBatteryGaugeLeft(context) + "%");
            remoteViews.setTextViewText(R.id.text_widget_battery_gauge_right, WidgetUtil.getBatteryGaugeRight(context) + "%");
            remoteViews.setTextViewText(R.id.text_widget_battery_gauge_common, WidgetUtil.getBatteryGaugeCommon(context) + "%");
            remoteViews.setTextViewText(R.id.text_widget_battery_gauge_cradle, WidgetUtil.getBatteryGaugeCradle(context) + "%");
            remoteViews.setOnClickPendingIntent(R.id.layout_widget_battery_container, getPendingIntent(context2, WidgetConstants.WIDGET_ACTION_START_LAUNCH_ACTIVITY));
            if (isCommonBattery) {
                remoteViews.setContentDescription(R.id.layout_widget_battery_common, context2.getString(R.string.remaining_battery) + String.format(context2.getString(R.string.d_percent), new Object[]{Integer.valueOf(WidgetUtil.getBatteryGaugeCommon(context))}));
            } else {
                if (WidgetUtil.isConnectedLeftDevice(context) && WidgetUtil.isConnectedRightDevice(context)) {
                    str2 = String.format(context2.getString(R.string.remaining_battery_both), new Object[]{Integer.valueOf(WidgetUtil.getBatteryGaugeLeft(context)), Integer.valueOf(WidgetUtil.getBatteryGaugeRight(context))});
                } else if (WidgetUtil.isConnectedLeftDevice(context)) {
                    str2 = String.format(context2.getString(R.string.remaining_battery_left_only), new Object[]{Integer.valueOf(WidgetUtil.getBatteryGaugeLeft(context))});
                } else {
                    str2 = WidgetUtil.isConnectedRightDevice(context) ? String.format(context2.getString(R.string.remaining_battery_right_only), new Object[]{Integer.valueOf(WidgetUtil.getBatteryGaugeRight(context))}) : "";
                }
                remoteViews.setContentDescription(R.id.layout_widget_battery_device, str2);
            }
            if (isConnectedCradle) {
                str = context2.getString(R.string.case_d_percent, new Object[]{Integer.valueOf(WidgetUtil.getBatteryGaugeCradle(context))});
            } else {
                str = context2.getString(R.string.widget_cradle) + ", " + context2.getString(R.string.va_disabled);
            }
            remoteViews.setContentDescription(R.id.layout_widget_cradle, str);
        } else {
            remoteViews.setOnClickPendingIntent(R.id.layout_widget_battery_container, getPendingIntent(context2, WidgetConstants.WIDGET_ACTION_DISCONNECTED));
            remoteViews.setContentDescription(R.id.layout_widget_battery_device, context2.getString(R.string.earbuds) + ", " + context2.getString(R.string.va_disabled));
            remoteViews.setContentDescription(R.id.layout_widget_cradle, context2.getString(R.string.widget_cradle) + ", " + context2.getString(R.string.va_disabled));
        }
        int i5 = 102;
        remoteViews.setInt(R.id.image_widget_device_left, "setAlpha", isConnectedLeftDevice ? 255 : 102);
        remoteViews.setInt(R.id.image_widget_device_right, "setAlpha", isConnectedRightDevice ? 255 : 102);
        remoteViews.setInt(R.id.image_widget_cradle, "setAlpha", isConnectedCradle ? 255 : 102);
        remoteViews.setInt(R.id.text_widget_device_left, "setTextColor", WidgetUtil.makeAlphaColor(i2, isConnectedLeftDevice ? 255 : 102));
        remoteViews.setInt(R.id.text_widget_device_right, "setTextColor", WidgetUtil.makeAlphaColor(i2, isConnectedRightDevice ? 255 : 102));
        if (isConnectedCradle) {
            i5 = 255;
        }
        remoteViews.setInt(R.id.text_widget_cradle, "setTextColor", WidgetUtil.makeAlphaColor(i2, i5));
        remoteViews.setViewVisibility(R.id.text_widget_battery_gauge_left, isConnectedLeftDevice ? 0 : 4);
        remoteViews.setViewVisibility(R.id.text_widget_battery_gauge_right, isConnectedRightDevice ? 0 : 4);
        remoteViews.setViewVisibility(R.id.text_widget_battery_gauge_cradle, isConnectedCradle ? 0 : 4);
        remoteViews.setViewVisibility(R.id.layout_widget_battery_common, isCommonBattery ? 0 : 8);
        remoteViews.setViewVisibility(R.id.layout_widget_battery_device, isCommonBattery ? 8 : 0);
        Bundle appWidgetOptions = AppWidgetManager.getInstance(context).getAppWidgetOptions(i4);
        if (z) {
            WidgetSizeManager widgetSizeManager = new WidgetSizeManager(1, (float) appWidgetOptions.getInt("appWidgetMinWidth"), (float) appWidgetOptions.getInt("appWidgetMaxHeight"));
            float ratio = widgetSizeManager.getRatio();
            remoteViews.setTextViewTextSize(R.id.widget_text_device_bt_name, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portrait_device_bt_name_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_device_left, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portait_battery_device_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_device_right, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portait_battery_device_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_battery_gauge_left, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portait_battery_gauge_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_battery_gauge_right, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portait_battery_gauge_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_common, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portait_battery_device_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_battery_gauge_common, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portait_battery_gauge_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_cradle, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portait_battery_device_text_size)) * ratio);
            remoteViews.setTextViewTextSize(R.id.text_widget_battery_gauge_cradle, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_portait_battery_gauge_text_size)) * ratio);
            remoteViews.setViewPadding(R.id.layout_widget_battery_container, (int) widgetSizeManager.getPaddingLeftPixel(), (int) widgetSizeManager.getPaddingTopPixel(), (int) widgetSizeManager.getPaddingLeftPixel(), (int) widgetSizeManager.getPaddingTopPixel());
        } else {
            WidgetSizeManager widgetSizeManager2 = new WidgetSizeManager(4, (float) appWidgetOptions.getInt("appWidgetMaxWidth"), (float) appWidgetOptions.getInt("appWidgetMinHeight"));
            float ratio2 = widgetSizeManager2.getRatio();
            remoteViews.setTextViewTextSize(R.id.widget_text_device_bt_name, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_device_bt_name_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_device_left, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_battery_device_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_device_right, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_battery_device_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_battery_gauge_left, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_battery_gauge_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_battery_gauge_right, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_battery_gauge_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_common, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_battery_device_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_battery_gauge_common, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_battery_gauge_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_cradle, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_battery_device_text_size)) * ratio2);
            remoteViews.setTextViewTextSize(R.id.text_widget_battery_gauge_cradle, 0, ((float) context.getResources().getDimensionPixelSize(R.dimen.widget_landscape_battery_gauge_text_size)) * ratio2);
            remoteViews.setViewPadding(R.id.layout_widget_battery_container, 0, (int) widgetSizeManager2.getPaddingTopPixel(), 0, (int) widgetSizeManager2.getPaddingTopPixel());
        }
        return remoteViews;
    }
}
