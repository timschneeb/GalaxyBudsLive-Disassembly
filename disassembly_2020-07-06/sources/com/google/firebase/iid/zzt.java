package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.io.IOException;

final class zzt implements Continuation<Bundle, String> {
    private final /* synthetic */ zzs zzbs;

    zzt(zzs zzs) {
        this.zzbs = zzs;
    }

    public final /* synthetic */ Object then(Task task) throws Exception {
        return zzs.zza((Bundle) task.getResult(IOException.class));
    }
}
