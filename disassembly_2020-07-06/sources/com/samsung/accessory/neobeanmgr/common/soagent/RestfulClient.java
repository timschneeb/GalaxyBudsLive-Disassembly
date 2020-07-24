package com.samsung.accessory.neobeanmgr.common.soagent;

import android.util.Log;
import com.accessorydm.tp.urlconnect.HttpNetworkInterface;
import com.samsung.accessory.neobeanmgr.Application;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

public abstract class RestfulClient {
    public static final int RESPONSE_FAIL = 2;
    public static final int RESPONSE_RETRY = 3;
    public static final int RESPONSE_SUCCESS = 1;
    private static final String TAG = (Application.TAG_ + RestfulClient.class.getSimpleName());
    private JSONObject mBody;
    private String mMethod;
    private String mType;
    private String mUrl;

    public abstract String getAuthorization();

    public abstract String getID();

    public abstract void onError(int i, HttpsURLConnection httpsURLConnection) throws XmlPullParserException, IOException;

    public boolean onReponseHeader(HttpsURLConnection httpsURLConnection) {
        return true;
    }

    public abstract boolean onResult(HttpsURLConnection httpsURLConnection) throws XmlPullParserException, IOException;

    public void onResultBlock() {
    }

    public RestfulClient(String str, String str2, JSONObject jSONObject, String str3) {
        this.mUrl = str;
        this.mMethod = str2;
        this.mBody = jSONObject;
        this.mType = str3;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getMethod() {
        return this.mMethod;
    }

    public JSONObject getBody() {
        return this.mBody;
    }

    public String getType() {
        return this.mType;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: type inference failed for: r1v12 */
    /* JADX WARNING: type inference failed for: r1v13 */
    /* JADX WARNING: type inference failed for: r1v14 */
    /* JADX WARNING: type inference failed for: r1v15 */
    /* JADX WARNING: type inference failed for: r1v16 */
    /* JADX WARNING: type inference failed for: r1v24 */
    /* JADX WARNING: type inference failed for: r1v25 */
    /* JADX WARNING: type inference failed for: r1v26 */
    /* JADX WARNING: type inference failed for: r1v27 */
    /* JADX WARNING: type inference failed for: r1v28 */
    /* JADX WARNING: type inference failed for: r1v29 */
    /* JADX WARNING: type inference failed for: r1v30 */
    /* JADX WARNING: type inference failed for: r1v31 */
    /* JADX WARNING: type inference failed for: r1v32 */
    /* JADX WARNING: type inference failed for: r1v33 */
    /* JADX WARNING: type inference failed for: r1v34 */
    /* JADX WARNING: type inference failed for: r1v35 */
    /* JADX WARNING: type inference failed for: r1v36 */
    /* JADX WARNING: type inference failed for: r1v37 */
    /* JADX WARNING: type inference failed for: r1v38 */
    /* JADX WARNING: type inference failed for: r1v39 */
    /* JADX WARNING: type inference failed for: r1v40 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01ac  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01b6 A[SYNTHETIC, Splitter:B:110:0x01b6] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0169  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0173 A[SYNTHETIC, Splitter:B:79:0x0173] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x017e  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0188 A[SYNTHETIC, Splitter:B:89:0x0188] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x019d A[SYNTHETIC, Splitter:B:99:0x019d] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:73:0x0164=Splitter:B:73:0x0164, B:83:0x0179=Splitter:B:83:0x0179, B:93:0x018e=Splitter:B:93:0x018e} */
    public int execute() {
        HttpsURLConnection httpsURLConnection;
        OutputStream outputStream;
        HttpsURLConnection httpsURLConnection2;
        OutputStream outputStream2;
        OutputStream outputStream3;
        ? r1 = 0;
        try {
            httpsURLConnection = (HttpsURLConnection) new URL(getUrl()).openConnection();
            try {
                httpsURLConnection.setRequestMethod(getMethod());
                httpsURLConnection.setRequestProperty(HttpNetworkInterface.XTP_HTTP_CONTENT_TYPE, "application/json");
                httpsURLConnection.setRequestProperty(HttpNetworkInterface.XTP_HTTP_ACCEPT, "application/xml");
                httpsURLConnection.setRequestProperty("Authorization", getAuthorization());
                httpsURLConnection.setConnectTimeout(30000);
                httpsURLConnection.setReadTimeout(30000);
                httpsURLConnection.setDoOutput(true);
                OutputStream outputStream4 = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream4, HttpNetworkInterface.XTP_HTTP_UTF8));
                bufferedWriter.write(getBody().toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                int responseCode = httpsURLConnection.getResponseCode();
                Log.d(TAG, "responseCode: " + responseCode);
                if (responseCode == 200) {
                    if (!onReponseHeader(httpsURLConnection)) {
                        Log.d(TAG, "Header Fail, But response OK.");
                    }
                    onResult(httpsURLConnection);
                    Log.d(TAG, getBody().toString());
                    if (httpsURLConnection != null) {
                        httpsURLConnection.disconnect();
                    }
                    Log.d(TAG, "urlConnection.disconnect()");
                    if (outputStream4 != null) {
                        try {
                            outputStream4.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return 1;
                } else if (responseCode == 403) {
                    r1 = outputStream4;
                    r1 = outputStream4;
                    r1 = outputStream4;
                    r1 = outputStream4;
                    onResultBlock();
                    Log.d(TAG, getBody().toString());
                    r1 = outputStream4;
                    r1 = outputStream4;
                    r1 = outputStream4;
                    r1 = outputStream4;
                    if (httpsURLConnection != null) {
                        httpsURLConnection.disconnect();
                    }
                    Log.d(TAG, "urlConnection.disconnect()");
                    if (outputStream4 != null) {
                        try {
                            outputStream4.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    return 2;
                } else if (responseCode == 401) {
                    r1 = outputStream4;
                    r1 = outputStream4;
                    r1 = outputStream4;
                    r1 = outputStream4;
                    Log.d(TAG, "Get TOKEN from first HTTP_UNAUTHORIZED");
                    if (!onReponseHeader(httpsURLConnection)) {
                        Log.d(TAG, "No TOKEN!");
                    }
                    if (httpsURLConnection != null) {
                        httpsURLConnection.disconnect();
                    }
                    Log.d(TAG, "urlConnection.disconnect()");
                    if (outputStream4 != null) {
                        try {
                            outputStream4.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    return 3;
                } else {
                    r1 = outputStream4;
                    r1 = outputStream4;
                    r1 = outputStream4;
                    r1 = outputStream4;
                    Log.d(TAG, "response Fail. responseCode is :" + responseCode);
                    Log.d(TAG, getBody().toString());
                    if (!onReponseHeader(httpsURLConnection)) {
                        Log.d(TAG, "Header Fail");
                    }
                    onError(responseCode, httpsURLConnection);
                    if (httpsURLConnection != null) {
                        httpsURLConnection.disconnect();
                    }
                    Log.d(TAG, "urlConnection.disconnect()");
                    if (outputStream4 != null) {
                        try {
                            outputStream4.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    return 2;
                }
            } catch (MalformedURLException e5) {
                e = e5;
                HttpsURLConnection httpsURLConnection3 = httpsURLConnection;
                outputStream2 = r1;
                r1 = httpsURLConnection3;
                httpsURLConnection2 = r1;
                e.printStackTrace();
                httpsURLConnection2 = r1;
                if (r1 != 0) {
                }
                Log.d(TAG, "urlConnection.disconnect()");
                if (outputStream != null) {
                }
                return 2;
            } catch (IOException e6) {
                e = e6;
                HttpsURLConnection httpsURLConnection4 = httpsURLConnection;
                outputStream3 = r1;
                r1 = httpsURLConnection4;
                httpsURLConnection2 = r1;
                e.printStackTrace();
                httpsURLConnection2 = r1;
                if (r1 != 0) {
                }
                Log.d(TAG, "urlConnection.disconnect()");
                if (outputStream != null) {
                }
                return 2;
            } catch (XmlPullParserException e7) {
                e = e7;
                HttpsURLConnection httpsURLConnection5 = httpsURLConnection;
                outputStream = r1;
                r1 = httpsURLConnection5;
                try {
                    httpsURLConnection2 = r1;
                    e.printStackTrace();
                    httpsURLConnection2 = r1;
                    if (r1 != 0) {
                    }
                    Log.d(TAG, "urlConnection.disconnect()");
                    if (outputStream != null) {
                    }
                    return 2;
                } catch (Throwable th) {
                    th = th;
                    OutputStream outputStream5 = outputStream;
                    httpsURLConnection = httpsURLConnection2;
                    r1 = outputStream5;
                    if (httpsURLConnection != null) {
                    }
                    Log.d(TAG, "urlConnection.disconnect()");
                    if (r1 != 0) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                r1 = r1;
                if (httpsURLConnection != null) {
                }
                Log.d(TAG, "urlConnection.disconnect()");
                if (r1 != 0) {
                }
                throw th;
            }
        } catch (MalformedURLException e8) {
            e = e8;
            outputStream2 = null;
            httpsURLConnection2 = r1;
            e.printStackTrace();
            httpsURLConnection2 = r1;
            if (r1 != 0) {
                r1.disconnect();
            }
            Log.d(TAG, "urlConnection.disconnect()");
            if (outputStream != null) {
                outputStream.close();
            }
            return 2;
        } catch (IOException e9) {
            e = e9;
            outputStream3 = null;
            httpsURLConnection2 = r1;
            e.printStackTrace();
            httpsURLConnection2 = r1;
            if (r1 != 0) {
                r1.disconnect();
            }
            Log.d(TAG, "urlConnection.disconnect()");
            if (outputStream != null) {
                outputStream.close();
            }
            return 2;
        } catch (XmlPullParserException e10) {
            e = e10;
            outputStream = null;
            httpsURLConnection2 = r1;
            e.printStackTrace();
            httpsURLConnection2 = r1;
            if (r1 != 0) {
                r1.disconnect();
            }
            Log.d(TAG, "urlConnection.disconnect()");
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e11) {
                    e11.printStackTrace();
                }
            }
            return 2;
        } catch (Throwable th3) {
            th = th3;
            httpsURLConnection = null;
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            Log.d(TAG, "urlConnection.disconnect()");
            if (r1 != 0) {
                try {
                    r1.close();
                } catch (IOException e12) {
                    e12.printStackTrace();
                }
            }
            throw th;
        }
    }

    public byte[] getSHADigest(String str) {
        try {
            byte[] bytes = str.getBytes(Charset.defaultCharset());
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.reset();
            return instance.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
