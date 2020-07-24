package com.samsung.context.sdk.samsunganalytics.internal.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettingReader {
    private static final int STATUS_LOG_BODY_LENGTH_LIMIT = 512;
    private final String THREE_DEPTH_ENTITY_DELIMETER = Delimiter.Depth.THREE_DEPTH.getCollectionDLM();
    private final String TWO_DEPTH_DELIMETER = Delimiter.Depth.TWO_DEPTH.getKeyValueDLM();
    private final String TWO_DEPTH_ENTITY_DELIMETER = Delimiter.Depth.TWO_DEPTH.getCollectionDLM();
    private Set<String> appPrefNames;
    private Context context;

    public SettingReader(Context context2) {
        this.context = context2;
        this.appPrefNames = Preferences.getPreferences(context2).getStringSet(Preferences.APP_PREF_NAMES, new HashSet());
    }

    private SharedPreferences getPreference(String str) {
        return this.context.getSharedPreferences(str, 0);
    }

    private Set<String> getKeySet(String str) {
        return Preferences.getPreferences(this.context).getStringSet(str, new HashSet());
    }

    private List<String> readFromApp() {
        String str;
        if (this.appPrefNames.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        String str2 = "";
        for (String next : this.appPrefNames) {
            SharedPreferences preference = getPreference(next);
            Set<String> keySet = getKeySet(next);
            for (Map.Entry next2 : preference.getAll().entrySet()) {
                if (keySet.contains(next2.getKey())) {
                    Class<?> cls = next2.getValue().getClass();
                    if (cls.equals(Integer.class) || cls.equals(Float.class) || cls.equals(Long.class) || cls.equals(String.class) || cls.equals(Boolean.class)) {
                        str = "" + ((String) next2.getKey()) + this.TWO_DEPTH_DELIMETER + next2.getValue();
                    } else {
                        String str3 = "" + ((String) next2.getKey()) + this.TWO_DEPTH_DELIMETER;
                        String str4 = null;
                        for (String str5 : (Set) next2.getValue()) {
                            if (!TextUtils.isEmpty(str4)) {
                                str4 = str4 + this.THREE_DEPTH_ENTITY_DELIMETER;
                            }
                            str4 = str4 + str5;
                        }
                        str = str3 + str4;
                    }
                    if (str2.length() + str.length() > 512) {
                        arrayList.add(str2);
                        str2 = "";
                    } else if (!TextUtils.isEmpty(str2)) {
                        str2 = str2 + this.TWO_DEPTH_ENTITY_DELIMETER;
                    }
                    str2 = str2 + str;
                }
            }
        }
        if (str2.length() != 0) {
            arrayList.add(str2);
        }
        return arrayList;
    }

    public List<String> read() {
        List<String> readFromApp = readFromApp();
        Map<String, ?> all = getPreference(Preferences.SETTING_PREF_NAME).getAll();
        if (all != null && !all.isEmpty()) {
            readFromApp.add(new Delimiter().makeDelimiterString(all, Delimiter.Depth.TWO_DEPTH));
        }
        return readFromApp;
    }
}
