package androidx.savedstate.bundle;

import android.os.Bundle;
import androidx.savedstate.SavedStateRegistry;

public interface BundleSavedStateRegistryOwner {
    SavedStateRegistry<Bundle> getBundleSavedStateRegistry();
}
