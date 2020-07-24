package com.samsung.accessory.neobeanmgr.module.setupwizard;

import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

public class AssetString {
    private static final String TAG = "NeoBean_AssetString";

    public static String getStringEULA() {
        Log.d(TAG, "getStringEULA()");
        String string = getString("EULA");
        if (!Util.isJapanModel()) {
            string = string.replaceAll("Samsung Account", "Galaxy account");
        }
        Log.d(TAG, "getStringEULA()_end");
        return string;
    }

    public static String getStringRDI() {
        Log.d(TAG, "getStringRDI()");
        String string = getString("RDI");
        Log.d(TAG, "getStringRDI()_end");
        return string;
    }

    public static String getString(String str) {
        String str2;
        Locale[] localeArr;
        String str3 = str;
        Log.d(TAG, "getString() : " + str3);
        try {
            String[] list = Application.getContext().getAssets().list(str3);
            if (list != null) {
                Configuration configuration = Application.getContext().getResources().getConfiguration();
                if (Build.VERSION.SDK_INT >= 24) {
                    localeArr = new Locale[configuration.getLocales().size()];
                    for (int i = 0; i < localeArr.length; i++) {
                        localeArr[i] = configuration.getLocales().get(0);
                    }
                } else {
                    localeArr = new Locale[]{configuration.locale};
                }
                String str4 = null;
                for (int i2 = 0; i2 < localeArr.length && str4 == null; i2++) {
                    Locale locale = localeArr[i2];
                    if (locale != null) {
                        String language = locale.getLanguage();
                        String country = locale.getCountry();
                        Log.d(TAG, "getString() : " + str3 + " : language=" + language + ", country=" + country);
                        if (TextUtils.isEmpty(str4)) {
                            if (!TextUtils.isEmpty(language) && !TextUtils.isEmpty(country)) {
                                String str5 = language + "-r" + country + ".txt";
                                if (listContains(list, str5)) {
                                    str4 = str5;
                                }
                                Log.d(TAG, "getString() : " + str3 + " : key=" + str5 + ", filename=" + str4);
                            }
                        }
                        if (TextUtils.isEmpty(str4) && !TextUtils.isEmpty(language)) {
                            String str6 = language + ".txt";
                            if (listContains(list, str6)) {
                                str4 = str6;
                            }
                            Log.d(TAG, "getString() : " + str3 + " : key=" + str6 + ", filename=" + str4);
                        }
                        if (TextUtils.isEmpty(str4) && !TextUtils.isEmpty(language)) {
                            String str7 = str4;
                            for (String str8 : list) {
                                if (str8 != null && str8.startsWith(language)) {
                                    str7 = str8;
                                }
                            }
                            Log.d(TAG, "getString() : " + str3 + " : language=" + language + ", filename=" + str7);
                            str4 = str7;
                        }
                    }
                }
                if (str4 == null) {
                    str4 = "en.txt";
                }
                str2 = getStringFromPath(str3 + "/" + str4);
            } else {
                str2 = null;
            }
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getString() : " + str3 + " : Exception : " + e);
            return null;
        }
    }

    private static String getStringFromPath(String str) {
        Log.d(TAG, "getStringFromFile() : " + str);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(Application.getContext().getAssets().open(str)));
            while (true) {
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                    sb.append("\n");
                } catch (Exception e) {
                    BufferedReader bufferedReader3 = bufferedReader2;
                    e = e;
                    bufferedReader = bufferedReader3;
                    Log.e(TAG, "getStringFromFile() : exception : " + e);
                    Util.safeClose(bufferedReader);
                    Log.d(TAG, "getStringFromFile()_end : " + str);
                    return sb.toString();
                }
            }
            bufferedReader = bufferedReader2;
        } catch (Exception e2) {
            e = e2;
            Log.e(TAG, "getStringFromFile() : exception : " + e);
            Util.safeClose(bufferedReader);
            Log.d(TAG, "getStringFromFile()_end : " + str);
            return sb.toString();
        }
        Util.safeClose(bufferedReader);
        Log.d(TAG, "getStringFromFile()_end : " + str);
        return sb.toString();
    }

    private static boolean listContains(String[] strArr, String str) {
        for (String equals : strArr) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }
}
