package com.accessorydm.ui.dialog.model;

import com.accessorydm.ui.dialog.model.XUIDialogModel;
import com.sec.android.fotaprovider.R;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class AccessorySysscopeScanningModel extends XUIDialogModel.ProgressWithNoButtons {
    private static String title() {
        return null;
    }

    public AccessorySysscopeScanningModel() {
        super(title(), message());
    }

    private static String message() {
        return getString(R.string.STR_DM_CHECKING_UPDATE);
    }

    public void preExecute() {
        Executors.newSingleThreadScheduledExecutor().schedule($$Lambda$AccessorySysscopeScanningModel$HmFbkioRss7J9Am7GIUEEEHj04.INSTANCE, 10, TimeUnit.SECONDS);
    }
}
