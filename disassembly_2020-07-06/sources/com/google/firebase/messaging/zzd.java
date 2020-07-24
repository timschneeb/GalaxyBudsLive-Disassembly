package com.google.firebase.messaging;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_messaging.zzj;
import com.google.android.gms.internal.firebase_messaging.zzk;
import com.google.android.gms.internal.firebase_messaging.zzn;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

final class zzd implements Closeable {
    private final URL url;
    private Task<Bitmap> zzea;
    private volatile InputStream zzeb;

    public static zzd zzo(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new zzd(new URL(str));
        } catch (MalformedURLException unused) {
            String valueOf = String.valueOf(str);
            Log.w("FirebaseMessaging", valueOf.length() != 0 ? "Not downloading image, bad URL: ".concat(valueOf) : new String("Not downloading image, bad URL: "));
            return null;
        }
    }

    private zzd(URL url2) {
        this.url = url2;
    }

    public final void zza(Executor executor) {
        this.zzea = Tasks.call(executor, new zze(this));
    }

    public final Task<Bitmap> getTask() {
        return (Task) Preconditions.checkNotNull(this.zzea);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00a0, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        zza(r3, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a4, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a7, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a8, code lost:
        if (r0 != null) goto L_0x00aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        zza(r2, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00ad, code lost:
        throw r3;
     */
    public final Bitmap zzat() throws IOException {
        String valueOf = String.valueOf(this.url);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 22);
        sb.append("Starting download of: ");
        sb.append(valueOf);
        Log.i("FirebaseMessaging", sb.toString());
        try {
            InputStream inputStream = this.url.openConnection().getInputStream();
            InputStream zza = zzj.zza(inputStream, 1048576);
            this.zzeb = inputStream;
            Bitmap decodeStream = BitmapFactory.decodeStream(zza);
            if (decodeStream != null) {
                if (Log.isLoggable("FirebaseMessaging", 3)) {
                    String valueOf2 = String.valueOf(this.url);
                    StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 31);
                    sb2.append("Successfully downloaded image: ");
                    sb2.append(valueOf2);
                    Log.d("FirebaseMessaging", sb2.toString());
                }
                zza((Throwable) null, zza);
                if (inputStream != null) {
                    zza((Throwable) null, inputStream);
                }
                return decodeStream;
            }
            String valueOf3 = String.valueOf(this.url);
            StringBuilder sb3 = new StringBuilder(String.valueOf(valueOf3).length() + 24);
            sb3.append("Failed to decode image: ");
            sb3.append(valueOf3);
            String sb4 = sb3.toString();
            Log.w("FirebaseMessaging", sb4);
            throw new IOException(sb4);
        } catch (IOException e) {
            String valueOf4 = String.valueOf(this.url);
            StringBuilder sb5 = new StringBuilder(String.valueOf(valueOf4).length() + 26);
            sb5.append("Failed to download image: ");
            sb5.append(valueOf4);
            Log.w("FirebaseMessaging", sb5.toString());
            throw e;
        }
    }

    public final void close() {
        zzk.zza(this.zzeb);
    }

    private static /* synthetic */ void zza(Throwable th, InputStream inputStream) {
        if (th != null) {
            try {
                inputStream.close();
            } catch (Throwable th2) {
                zzn.zza(th, th2);
            }
        } else {
            inputStream.close();
        }
    }
}
