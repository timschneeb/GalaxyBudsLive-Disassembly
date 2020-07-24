package androidx.savedstate;

import androidx.arch.core.internal.SafeIterableMap;
import androidx.savedstate.SavedStateRegistry;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSavedStateRegistry<S> implements SavedStateRegistry<S> {
    private SafeIterableMap<String, SavedStateRegistry.SavedStateProvider<S>> mComponents = new SafeIterableMap<>();
    private boolean mRestored;
    private Map<String, S> mSavedState;

    public final S consumeRestoredStateForKey(String str) {
        if (this.mRestored) {
            Map<String, S> map = this.mSavedState;
            if (map == null) {
                return null;
            }
            S remove = map.remove(str);
            if (!this.mSavedState.isEmpty()) {
                return remove;
            }
            this.mSavedState = null;
            return remove;
        }
        throw new IllegalStateException("You can consumeRestoredStateForKey only after super.onCreate of corresponding component");
    }

    public final void registerSavedStateProvider(String str, SavedStateRegistry.SavedStateProvider<S> savedStateProvider) {
        if (this.mComponents.putIfAbsent(str, savedStateProvider) != null) {
            throw new IllegalArgumentException("SavedStateProvider with the given key is already registered");
        }
    }

    public final void unregisterSavedStateProvider(String str) {
        this.mComponents.remove(str);
    }

    public final boolean isRestored() {
        return this.mRestored;
    }

    /* access modifiers changed from: protected */
    public final void restoreSavedState(Map<String, S> map) {
        if (map != null) {
            this.mSavedState = new HashMap(map);
        }
        this.mRestored = true;
    }

    /* access modifiers changed from: protected */
    public final Map<String, S> saveState() {
        HashMap hashMap = new HashMap();
        Map<String, S> map = this.mSavedState;
        if (map != null) {
            hashMap.putAll(map);
        }
        SafeIterableMap<K, V>.IteratorWithAdditions iteratorWithAdditions = this.mComponents.iteratorWithAdditions();
        while (iteratorWithAdditions.hasNext()) {
            Map.Entry entry = (Map.Entry) iteratorWithAdditions.next();
            hashMap.put(entry.getKey(), ((SavedStateRegistry.SavedStateProvider) entry.getValue()).saveState());
        }
        return hashMap;
    }
}
