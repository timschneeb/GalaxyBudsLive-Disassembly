package com.google.android.gms.internal.firebase_messaging;

import java.io.PrintStream;

public final class zzn {
    private static final zzm zzk;
    private static final int zzl;

    static final class zza extends zzm {
        zza() {
        }

        public final void zza(Throwable th, Throwable th2) {
        }
    }

    public static void zza(Throwable th, Throwable th2) {
        zzk.zza(th, th2);
    }

    private static Integer zzb() {
        try {
            return (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get((Object) null);
        } catch (Exception e) {
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(System.err);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0068  */
    static {
        zzm zzm;
        Integer num;
        int i = 1;
        try {
            num = zzb();
            if (num != null) {
                try {
                    if (num.intValue() >= 19) {
                        zzm = new zzr();
                        zzk = zzm;
                        if (num != null) {
                            i = num.intValue();
                        }
                        zzl = i;
                    }
                } catch (Throwable th) {
                    th = th;
                    PrintStream printStream = System.err;
                    String name = zza.class.getName();
                    StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 133);
                    sb.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
                    sb.append(name);
                    sb.append("will be used. The error is: ");
                    printStream.println(sb.toString());
                    th.printStackTrace(System.err);
                    zzm = new zza();
                    zzk = zzm;
                    if (num != null) {
                    }
                    zzl = i;
                }
            }
            if (!Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic")) {
                zzm = new zzq();
            } else {
                zzm = new zza();
            }
        } catch (Throwable th2) {
            th = th2;
            num = null;
            PrintStream printStream2 = System.err;
            String name2 = zza.class.getName();
            StringBuilder sb2 = new StringBuilder(String.valueOf(name2).length() + 133);
            sb2.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
            sb2.append(name2);
            sb2.append("will be used. The error is: ");
            printStream2.println(sb2.toString());
            th.printStackTrace(System.err);
            zzm = new zza();
            zzk = zzm;
            if (num != null) {
            }
            zzl = i;
        }
        zzk = zzm;
        if (num != null) {
        }
        zzl = i;
    }
}
