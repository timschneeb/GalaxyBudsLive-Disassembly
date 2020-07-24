package androidx.savedstate;

public interface SavedStateRegistry<S> {

    public interface SavedStateProvider<S> {
        S saveState();
    }

    S consumeRestoredStateForKey(String str);

    boolean isRestored();

    void registerSavedStateProvider(String str, SavedStateProvider<S> savedStateProvider);

    void unregisterSavedStateProvider(String str);
}
