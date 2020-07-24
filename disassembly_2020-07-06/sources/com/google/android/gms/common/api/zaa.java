package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult;

final class zaa implements PendingResult.StatusListener {
    private final /* synthetic */ Batch zabd;

    zaa(Batch batch) {
        this.zabd = batch;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0067, code lost:
        return;
     */
    public final void onComplete(Status status) {
        Status status2;
        synchronized (this.zabd.mLock) {
            if (!this.zabd.isCanceled()) {
                if (status.isCanceled()) {
                    boolean unused = this.zabd.zabb = true;
                } else if (!status.isSuccess()) {
                    boolean unused2 = this.zabd.zaba = true;
                }
                Batch.zab(this.zabd);
                if (this.zabd.zaaz == 0) {
                    if (this.zabd.zabb) {
                        zaa.super.cancel();
                    } else {
                        if (this.zabd.zaba) {
                            status2 = new Status(13);
                        } else {
                            status2 = Status.RESULT_SUCCESS;
                        }
                        this.zabd.setResult(new BatchResult(status2, this.zabd.zabc));
                    }
                }
            }
        }
    }
}
