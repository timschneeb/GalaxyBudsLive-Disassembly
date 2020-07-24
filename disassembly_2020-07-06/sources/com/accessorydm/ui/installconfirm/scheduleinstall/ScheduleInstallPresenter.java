package com.accessorydm.ui.installconfirm.scheduleinstall;

import android.app.Dialog;
import android.app.SpinnerTimePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import com.accessorydm.ui.handler.XDMToastHandler;
import com.accessorydm.ui.installconfirm.InstallCountdown;
import com.accessorydm.ui.installconfirm.scheduleinstall.ScheduleInstallContract;
import com.samsung.accessory.fotaprovider.AccessoryController;
import com.samsung.accessory.fotaprovider.controller.NotificationController;
import com.samsung.android.fotaprovider.log.Log;
import com.sec.android.fotaprovider.R;

public class ScheduleInstallPresenter implements ScheduleInstallContract.Presenter {
    private ScheduleInstallModel model;
    private ScheduleInstallContract.View view;

    ScheduleInstallPresenter(ScheduleInstallContract.View view2, ScheduleInstallModel scheduleInstallModel) {
        this.view = view2;
        this.model = scheduleInstallModel;
    }

    public void onCreate(Context context) {
        Log.I("");
    }

    /* JADX WARNING: type inference failed for: r1v2, types: [android.app.TimePickerDialog, android.app.Dialog] */
    /* JADX WARNING: type inference failed for: r10v1, types: [android.app.TimePickerDialog] */
    /* JADX WARNING: type inference failed for: r3v1, types: [android.app.SpinnerTimePickerDialog] */
    /* JADX WARNING: Multi-variable type inference failed */
    public Dialog onCreateDialog(Context context) {
        ? r1;
        Log.I("");
        if (Build.VERSION.SDK_INT == 24) {
            r1 = new SpinnerTimePickerDialog(context, R.style.FotaProviderTheme_Dialog_TimePicker, this.view, this.model.getDefaultHour(), this.model.getDefaultMinute(), DateFormat.is24HourFormat(context));
        } else {
            r1 = new TimePickerDialog(context, R.style.FotaProviderTheme_Dialog_TimePicker, this.view, this.model.getDefaultHour(), this.model.getDefaultMinute(), DateFormat.is24HourFormat(context));
        }
        r1.setTitle(this.model.getTimePickerTitle());
        return r1;
    }

    public void onTimeSet(Context context, int i, int i2) {
        Log.I("");
        InstallCountdown.getInstance().stop();
        AccessoryController.getInstance().getNotificationController().removeAccessoryNotification(new NotificationController.NotificationCallback(i, i2) {
            private final /* synthetic */ int f$1;
            private final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onResponse() {
                ScheduleInstallPresenter.this.lambda$onTimeSet$0$ScheduleInstallPresenter(this.f$1, this.f$2);
            }
        });
    }

    public /* synthetic */ void lambda$onTimeSet$0$ScheduleInstallPresenter(int i, int i2) {
        Log.I("");
        this.model.scheduleInstallByTimePicker(i, i2);
        XDMToastHandler.xdmShowToast(this.model.getSetTimeToastText(), 0);
    }
}
