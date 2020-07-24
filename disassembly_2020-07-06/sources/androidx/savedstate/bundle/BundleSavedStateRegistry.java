package androidx.savedstate.bundle;

import android.os.Bundle;
import androidx.savedstate.AbstractSavedStateRegistry;
import java.util.HashMap;
import java.util.Map;

public final class BundleSavedStateRegistry extends AbstractSavedStateRegistry<Bundle> {
    private static final String SAVED_COMPONENTS_KEY = "androidx.lifecycle.BundlableSavedStateRegistry.key";

    public void performRestore(Bundle bundle) {
        Bundle bundle2 = bundle != null ? bundle.getBundle(SAVED_COMPONENTS_KEY) : null;
        if (bundle2 == null || bundle2.isEmpty()) {
            restoreSavedState((Map) null);
            return;
        }
        HashMap hashMap = new HashMap();
        for (String str : bundle2.keySet()) {
            hashMap.put(str, bundle2.getBundle(str));
        }
        restoreSavedState(hashMap);
    }

    public void performSave(Bundle bundle) {
        Map saveState = saveState();
        Bundle bundle2 = new Bundle();
        for (Map.Entry entry : saveState.entrySet()) {
            bundle2.putBundle((String) entry.getKey(), (Bundle) entry.getValue());
        }
        bundle.putBundle(SAVED_COMPONENTS_KEY, bundle2);
    }
}
