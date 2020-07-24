package androidx.core.content.pm;

import androidx.concurrent.futures.ResolvableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;

public class ShortcutInfoCompatSaver {
    public ListenableFuture<Void> addShortcuts(List<ShortcutInfoCompat> list) {
        ResolvableFuture create = ResolvableFuture.create();
        create.set((Object) null);
        return create;
    }

    public ListenableFuture<Void> removeShortcuts(List<String> list) {
        ResolvableFuture create = ResolvableFuture.create();
        create.set((Object) null);
        return create;
    }

    public ListenableFuture<Void> removeAllShortcuts() {
        ResolvableFuture create = ResolvableFuture.create();
        create.set((Object) null);
        return create;
    }

    public List<ShortcutInfoCompat> getShortcuts() throws Exception {
        return new ArrayList();
    }
}
