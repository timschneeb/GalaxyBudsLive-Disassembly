package com.samsung.context.sdk.samsunganalytics.internal.terms;

import android.net.Uri;
import android.text.TextUtils;
import com.accessorydm.tp.urlconnect.HttpNetworkInterface;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.context.sdk.samsunganalytics.internal.connection.API;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Validation;
import com.samsung.context.sdk.samsunganalytics.internal.security.CertificateManager;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterTask implements AsyncTaskClient {
    private static final String TAG = "RegisterTask";
    private final API api = API.DATA_DELETE;
    private AsyncTaskCallback callback;
    private HttpsURLConnection conn = null;
    private String deviceID = "";
    private long timestamp;
    private String trid = "";

    public RegisterTask(String str, String str2, long j) {
        this.trid = str;
        this.deviceID = str2;
        this.timestamp = j;
    }

    public RegisterTask(String str, String str2, long j, AsyncTaskCallback asyncTaskCallback) {
        this.trid = str;
        this.deviceID = str2;
        this.timestamp = j;
        this.callback = asyncTaskCallback;
    }

    private String makeRequestBody() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("tid", this.trid);
            jSONObject.put("lid", this.deviceID);
            jSONObject.put("ts", this.timestamp);
        } catch (JSONException unused) {
        }
        return jSONObject.toString();
    }

    public void run() {
        try {
            Uri.Builder buildUpon = Uri.parse(this.api.getUrl()).buildUpon();
            String format = SimpleDateFormat.getTimeInstance(2).format(new Date());
            Uri.Builder appendQueryParameter = buildUpon.appendQueryParameter("ts", format);
            appendQueryParameter.appendQueryParameter("hc", Validation.sha256(format + Validation.SALT));
            this.conn = (HttpsURLConnection) new URL(buildUpon.build().toString()).openConnection();
            this.conn.setSSLSocketFactory(CertificateManager.getInstance().getSSLContext().getSocketFactory());
            this.conn.setRequestMethod(this.api.getMethod());
            this.conn.setConnectTimeout(3000);
            this.conn.setRequestProperty(HttpNetworkInterface.XTP_HTTP_CONTENT_TYPE, "application/json");
            String makeRequestBody = makeRequestBody();
            if (!TextUtils.isEmpty(makeRequestBody)) {
                this.conn.setDoOutput(true);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(this.conn.getOutputStream());
                bufferedOutputStream.write(makeRequestBody.getBytes());
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
        } catch (Exception unused) {
        }
    }

    public int onFinish() {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        try {
            int responseCode = this.conn.getResponseCode();
            if (responseCode >= 400) {
                bufferedReader = new BufferedReader(new InputStreamReader(this.conn.getErrorStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            }
            bufferedReader2 = bufferedReader;
            String string = new JSONObject(bufferedReader2.readLine()).getString("rc");
            if (responseCode == 200) {
                if (string.equalsIgnoreCase(SA.Event.UP_BUTTON)) {
                    Debug.LogENG("Success : " + responseCode + " " + string);
                    callback(responseCode, string);
                    cleanUp(bufferedReader2);
                    return 0;
                }
            }
            Debug.LogENG("Fail : " + responseCode + " " + string);
            callback(responseCode, string);
        } catch (Exception unused) {
            callback(0, "");
        } catch (Throwable th) {
            cleanUp(bufferedReader2);
            throw th;
        }
        cleanUp(bufferedReader2);
        return 0;
    }

    private void callback(int i, String str) {
        if (this.callback != null) {
            if (i != 200 || !str.equalsIgnoreCase(SA.Event.UP_BUTTON)) {
                this.callback.onFail(i, str, "", "");
            } else {
                this.callback.onSuccess(0, "", "", "");
            }
        }
    }

    private void cleanUp(BufferedReader bufferedReader) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException unused) {
                return;
            }
        }
        if (this.conn != null) {
            this.conn.disconnect();
        }
    }
}
