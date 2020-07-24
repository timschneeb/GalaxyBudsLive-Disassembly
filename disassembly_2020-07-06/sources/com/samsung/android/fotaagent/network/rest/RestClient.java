package com.samsung.android.fotaagent.network.rest;

import com.accessorydm.tp.urlconnect.HttpNetworkInterface;
import com.samsung.android.fotaprovider.log.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class RestClient {
    /* JADX WARNING: Removed duplicated region for block: B:29:0x016d  */
    public RestResponse execute(RestRequest restRequest) {
        HttpURLConnection httpURLConnection;
        HttpURLConnection httpURLConnection2 = null;
        try {
            Log.I("[[ =================================================================");
            URL url = new URL(restRequest.getUri());
            Log.I(">> ------------------------------------------------------------- >>");
            Log.I(">> Request: " + url.getPath() + " >>");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                httpURLConnection.setRequestMethod(restRequest.getMethod());
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setDoInput(true);
                Log.H(">> Headers:");
                for (Map.Entry next : restRequest.getHeaders().entrySet()) {
                    Log.H(((String) next.getKey()) + "=" + ((String) next.getValue()));
                    httpURLConnection.setRequestProperty((String) next.getKey(), (String) next.getValue());
                }
                Log.H(">> Method: " + restRequest.getMethod());
                Log.H(">> Url: " + restRequest.getUri());
                Log.H(">> Body: " + restRequest.getBody());
                if (restRequest.hasBody()) {
                    httpURLConnection.setDoOutput(true);
                    sendStream(httpURLConnection, restRequest.getBody());
                } else {
                    httpURLConnection.connect();
                }
                Log.I("<< ------------------------------------------------------------- <<");
                Log.I("<< Response: " + url.getPath() + " <<");
                StringBuilder sb = new StringBuilder();
                sb.append("<< Http Code: ");
                sb.append(httpURLConnection.getResponseCode());
                Log.I(sb.toString());
                int responseCode = httpURLConnection.getResponseCode();
                RestResponse restResponse = new RestResponse(responseCode, receiveStream(responseCode, httpURLConnection), httpURLConnection.getHeaderFields());
                if (!restResponse.isSuccess()) {
                    logcatHeaders(restResponse.getHeaders());
                    restResponse.setError();
                }
                Log.I("================================================================= ]]");
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return restResponse;
            } catch (RuntimeException e) {
                e = e;
                httpURLConnection2 = httpURLConnection;
                try {
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    httpURLConnection = httpURLConnection2;
                }
            } catch (Exception e2) {
                e = e2;
                try {
                    Log.printStackTrace(e);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            }
        } catch (RuntimeException e3) {
            e = e3;
            throw e;
        } catch (Exception e4) {
            e = e4;
            httpURLConnection = null;
            Log.printStackTrace(e);
            if (httpURLConnection != null) {
            }
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0021, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0026, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r5.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002a, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x002d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x002e, code lost:
        if (r4 != null) goto L_0x0030;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0038, code lost:
        throw r0;
     */
    private void sendStream(HttpURLConnection httpURLConnection, String str) {
        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, HttpNetworkInterface.XTP_HTTP_UTF8));
            bufferedWriter.write(str);
            bufferedWriter.flush();
            bufferedWriter.close();
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
        } catch (Throwable th) {
            r5.addSuppressed(th);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0037, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x003c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r0.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0040, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0043, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0044, code lost:
        if (r3 != null) goto L_0x0046;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x004e, code lost:
        throw r0;
     */
    private String receiveStream(int i, HttpURLConnection httpURLConnection) {
        InputStream inputStream;
        if (200 == i) {
            try {
                inputStream = httpURLConnection.getInputStream();
            } catch (Exception e) {
                Log.printStackTrace(e);
                return null;
            } catch (Throwable th) {
                r4.addSuppressed(th);
            }
        } else {
            inputStream = httpURLConnection.getErrorStream();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, HttpNetworkInterface.XTP_HTTP_UTF8));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            sb.append(readLine);
        }
        String sb2 = sb.toString();
        bufferedReader.close();
        if (inputStream != null) {
            inputStream.close();
        }
        return sb2;
    }

    private void logcatHeaders(Map<String, List<String>> map) {
        String str;
        Log.H("<< Headers:");
        for (Map.Entry next : map.entrySet()) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < ((List) next.getValue()).size(); i++) {
                if (i == 0) {
                    str = "";
                } else {
                    str = ";" + ((String) ((List) next.getValue()).get(i));
                }
                stringBuffer.append(str);
            }
            Log.H(((String) next.getKey()) + "=" + stringBuffer.toString());
        }
    }
}
