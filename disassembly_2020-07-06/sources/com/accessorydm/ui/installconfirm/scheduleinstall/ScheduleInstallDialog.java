package com.accessorydm.ui.installconfirm.scheduleinstall;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TimePicker;
import com.accessorydm.ui.installconfirm.scheduleinstall.ScheduleInstallContract;
import com.samsung.android.fotaprovider.log.Log;

public class ScheduleInstallDialog extends DialogFragment implements ScheduleInstallContract.View {
    public static final String TAG = "DIALOG_FOR_SCHEDULE_INSTALL";
    private ScheduleInstallContract.Presenter presenter;

    public void onCreate(Bundle bundle) {
        Log.I("");
        this.presenter = new ScheduleInstallPresenter(this, ScheduleInstallModel.getInstance());
        super.onCreate(bundle);
        this.presenter.onCreate(getActivity());
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Log.I("");
        return this.presenter.onCreateDialog(getActivity());
    }

    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        Log.I("");
        if (getActivity() != null) {
            this.presenter.onTimeSet(timePicker.getContext(), i, i2);
            getActivity().finish();
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
        Log.I("");
        super.onCancel(dialogInterface);
    }
}
