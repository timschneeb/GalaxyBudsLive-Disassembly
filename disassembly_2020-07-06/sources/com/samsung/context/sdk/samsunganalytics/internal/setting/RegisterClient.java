package com.samsung.context.sdk.samsunganalytics.internal.setting;

import android.content.Context;
import android.content.SharedPreferences;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegisterClient implements AsyncTaskClient {
    private Context context;
    private Map<String, Set<String>> map;

    public int onFinish() {
        return 0;
    }

    public RegisterClient(Context context2, Map<String, Set<String>> map2) {
        this.context = context2;
        this.map = map2;
    }

    public void run() {
        SharedPreferences preferences = Preferences.getPreferences(this.context);
        for (String remove : preferences.getStringSet(Preferences.APP_PREF_NAMES, new HashSet())) {
            preferences.edit().remove(remove).apply();
        }
        preferences.edit().remove(Preferences.APP_PREF_NAMES).apply();
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        for (Map.Entry next : this.map.entrySet()) {
            String str = (String) next.getKey();
            hashSet.add(str);
            preferences.edit().putStringSet(str, (Set) next.getValue()).apply();
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(str, 0);
            for (String str2 : (Set) next.getValue()) {
                if (!sharedPreferences.contains(str2)) {
                    hashSet2.add(str2);
                }
            }
        }
        preferences.edit().putStringSet(Preferences.APP_PREF_NAMES, hashSet).apply();
        if (!hashSet2.isEmpty()) {
            Debug.LogENG("Keys not found " + hashSet2);
        }
    }
}
