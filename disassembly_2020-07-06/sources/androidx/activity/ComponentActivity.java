package androidx.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.bundle.BundleSavedStateRegistry;
import androidx.savedstate.bundle.BundleSavedStateRegistryOwner;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComponentActivity extends androidx.core.app.ComponentActivity implements LifecycleOwner, ViewModelStoreOwner, BundleSavedStateRegistryOwner {
    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    final CopyOnWriteArrayList<LifecycleAwareOnBackPressedCallback> mOnBackPressedCallbacks = new CopyOnWriteArrayList<>();
    private final BundleSavedStateRegistry mSavedStateRegistry = new BundleSavedStateRegistry();
    private ViewModelStore mViewModelStore;

    @Deprecated
    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    static final class NonConfigurationInstances {
        Object custom;
        ViewModelStore viewModelStore;

        NonConfigurationInstances() {
        }
    }

    public ComponentActivity() {
        if (getLifecycle() != null) {
            if (Build.VERSION.SDK_INT >= 19) {
                getLifecycle().addObserver(new GenericLifecycleObserver() {
                    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                        if (event == Lifecycle.Event.ON_STOP) {
                            Window window = ComponentActivity.this.getWindow();
                            View peekDecorView = window != null ? window.peekDecorView() : null;
                            if (peekDecorView != null) {
                                peekDecorView.cancelPendingInputEvents();
                            }
                        }
                    }
                });
            }
            getLifecycle().addObserver(new GenericLifecycleObserver() {
                public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY && !ComponentActivity.this.isChangingConfigurations()) {
                        ComponentActivity.this.getViewModelStore().clear();
                    }
                }
            });
            if (19 <= Build.VERSION.SDK_INT && Build.VERSION.SDK_INT <= 23) {
                getLifecycle().addObserver(new ImmLeaksCleaner(this));
                return;
            }
            return;
        }
        throw new IllegalStateException("getLifecycle() returned null in ComponentActivity's constructor. Please make sure you are lazily constructing your Lifecycle in the first call to getLifecycle() rather than relying on field initialization.");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSavedStateRegistry.performRestore(bundle);
        ReportFragment.injectIfNeededIn(this);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            ((LifecycleRegistry) lifecycle).markState(Lifecycle.State.CREATED);
        }
        super.onSaveInstanceState(bundle);
        this.mSavedStateRegistry.performSave(bundle);
    }

    public final Object onRetainNonConfigurationInstance() {
        NonConfigurationInstances nonConfigurationInstances;
        Object onRetainCustomNonConfigurationInstance = onRetainCustomNonConfigurationInstance();
        ViewModelStore viewModelStore = this.mViewModelStore;
        if (viewModelStore == null && (nonConfigurationInstances = (NonConfigurationInstances) getLastNonConfigurationInstance()) != null) {
            viewModelStore = nonConfigurationInstances.viewModelStore;
        }
        if (viewModelStore == null && onRetainCustomNonConfigurationInstance == null) {
            return null;
        }
        NonConfigurationInstances nonConfigurationInstances2 = new NonConfigurationInstances();
        nonConfigurationInstances2.custom = onRetainCustomNonConfigurationInstance;
        nonConfigurationInstances2.viewModelStore = viewModelStore;
        return nonConfigurationInstances2;
    }

    @Deprecated
    public Object getLastCustomNonConfigurationInstance() {
        NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances) getLastNonConfigurationInstance();
        if (nonConfigurationInstances != null) {
            return nonConfigurationInstances.custom;
        }
        return null;
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    public ViewModelStore getViewModelStore() {
        if (getApplication() != null) {
            if (this.mViewModelStore == null) {
                NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances) getLastNonConfigurationInstance();
                if (nonConfigurationInstances != null) {
                    this.mViewModelStore = nonConfigurationInstances.viewModelStore;
                }
                if (this.mViewModelStore == null) {
                    this.mViewModelStore = new ViewModelStore();
                }
            }
            return this.mViewModelStore;
        }
        throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
    }

    public void onBackPressed() {
        Iterator<LifecycleAwareOnBackPressedCallback> it = this.mOnBackPressedCallbacks.iterator();
        while (it.hasNext()) {
            if (it.next().handleOnBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    public void addOnBackPressedCallback(OnBackPressedCallback onBackPressedCallback) {
        addOnBackPressedCallback(this, onBackPressedCallback);
    }

    public void addOnBackPressedCallback(LifecycleOwner lifecycleOwner, OnBackPressedCallback onBackPressedCallback) {
        Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() != Lifecycle.State.DESTROYED) {
            this.mOnBackPressedCallbacks.add(0, new LifecycleAwareOnBackPressedCallback(lifecycle, onBackPressedCallback));
        }
    }

    public void removeOnBackPressedCallback(OnBackPressedCallback onBackPressedCallback) {
        LifecycleAwareOnBackPressedCallback lifecycleAwareOnBackPressedCallback;
        Iterator<LifecycleAwareOnBackPressedCallback> it = this.mOnBackPressedCallbacks.iterator();
        while (true) {
            if (!it.hasNext()) {
                lifecycleAwareOnBackPressedCallback = null;
                break;
            }
            lifecycleAwareOnBackPressedCallback = it.next();
            if (lifecycleAwareOnBackPressedCallback.getOnBackPressedCallback().equals(onBackPressedCallback)) {
                break;
            }
        }
        if (lifecycleAwareOnBackPressedCallback != null) {
            lifecycleAwareOnBackPressedCallback.onRemoved();
            this.mOnBackPressedCallbacks.remove(lifecycleAwareOnBackPressedCallback);
        }
    }

    public final SavedStateRegistry<Bundle> getBundleSavedStateRegistry() {
        return this.mSavedStateRegistry;
    }

    private class LifecycleAwareOnBackPressedCallback implements OnBackPressedCallback, GenericLifecycleObserver {
        private final Lifecycle mLifecycle;
        private final OnBackPressedCallback mOnBackPressedCallback;

        LifecycleAwareOnBackPressedCallback(Lifecycle lifecycle, OnBackPressedCallback onBackPressedCallback) {
            this.mLifecycle = lifecycle;
            this.mOnBackPressedCallback = onBackPressedCallback;
            this.mLifecycle.addObserver(this);
        }

        /* access modifiers changed from: package-private */
        public Lifecycle getLifecycle() {
            return this.mLifecycle;
        }

        /* access modifiers changed from: package-private */
        public OnBackPressedCallback getOnBackPressedCallback() {
            return this.mOnBackPressedCallback;
        }

        public boolean handleOnBackPressed() {
            if (this.mLifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                return this.mOnBackPressedCallback.handleOnBackPressed();
            }
            return false;
        }

        public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                synchronized (ComponentActivity.this.mOnBackPressedCallbacks) {
                    this.mLifecycle.removeObserver(this);
                    ComponentActivity.this.mOnBackPressedCallbacks.remove(this);
                }
            }
        }

        public void onRemoved() {
            this.mLifecycle.removeObserver(this);
        }
    }
}
