package com.accessorydm.ui.progress;

import com.accessorydm.ui.progress.XUIProgressContract;

public class XUICopyProgressPresenter extends XUIProgressPresenter {
    XUICopyProgressPresenter(XUIProgressContract.View view, XUIProgressModel xUIProgressModel) {
        super(view, xUIProgressModel);
        xUIProgressModel.setProgressMode(2);
    }

    public void onCreate() {
        super.onCreate();
        this.view.getTopContentView().setIndeterminateProgressbar(true);
    }
}
