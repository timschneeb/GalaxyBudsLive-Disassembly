package androidx.core.app;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.os.Build;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;

public final class RemoteActionCompat {
    private static final String EXTRA_ACTION_INTENT = "action";
    private static final String EXTRA_CONTENT_DESCRIPTION = "desc";
    private static final String EXTRA_ENABLED = "enabled";
    private static final String EXTRA_ICON = "icon";
    private static final String EXTRA_SHOULD_SHOW_ICON = "showicon";
    private static final String EXTRA_TITLE = "title";
    private final PendingIntent mActionIntent;
    private final CharSequence mContentDescription;
    private boolean mEnabled;
    private final IconCompat mIcon;
    private boolean mShouldShowIcon;
    private final CharSequence mTitle;

    public RemoteActionCompat(IconCompat iconCompat, CharSequence charSequence, CharSequence charSequence2, PendingIntent pendingIntent) {
        this.mIcon = (IconCompat) Preconditions.checkNotNull(iconCompat);
        this.mTitle = (CharSequence) Preconditions.checkNotNull(charSequence);
        this.mContentDescription = (CharSequence) Preconditions.checkNotNull(charSequence2);
        this.mActionIntent = (PendingIntent) Preconditions.checkNotNull(pendingIntent);
        this.mEnabled = true;
        this.mShouldShowIcon = true;
    }

    public RemoteActionCompat(RemoteActionCompat remoteActionCompat) {
        Preconditions.checkNotNull(remoteActionCompat);
        this.mIcon = remoteActionCompat.mIcon;
        this.mTitle = remoteActionCompat.mTitle;
        this.mContentDescription = remoteActionCompat.mContentDescription;
        this.mActionIntent = remoteActionCompat.mActionIntent;
        this.mEnabled = remoteActionCompat.mEnabled;
        this.mShouldShowIcon = remoteActionCompat.mShouldShowIcon;
    }

    public static RemoteActionCompat createFromRemoteAction(RemoteAction remoteAction) {
        Preconditions.checkNotNull(remoteAction);
        RemoteActionCompat remoteActionCompat = new RemoteActionCompat(IconCompat.createFromIcon(remoteAction.getIcon()), remoteAction.getTitle(), remoteAction.getContentDescription(), remoteAction.getActionIntent());
        remoteActionCompat.setEnabled(remoteAction.isEnabled());
        if (Build.VERSION.SDK_INT >= 28) {
            remoteActionCompat.setShouldShowIcon(remoteAction.shouldShowIcon());
        }
        return remoteActionCompat;
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setShouldShowIcon(boolean z) {
        this.mShouldShowIcon = z;
    }

    public boolean shouldShowIcon() {
        return this.mShouldShowIcon;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public PendingIntent getActionIntent() {
        return this.mActionIntent;
    }

    public RemoteAction toRemoteAction() {
        RemoteAction remoteAction = new RemoteAction(this.mIcon.toIcon(), this.mTitle, this.mContentDescription, this.mActionIntent);
        remoteAction.setEnabled(isEnabled());
        if (Build.VERSION.SDK_INT >= 28) {
            remoteAction.setShouldShowIcon(shouldShowIcon());
        }
        return remoteAction;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putBundle(EXTRA_ICON, this.mIcon.toBundle());
        bundle.putCharSequence("title", this.mTitle);
        bundle.putCharSequence(EXTRA_CONTENT_DESCRIPTION, this.mContentDescription);
        bundle.putParcelable(EXTRA_ACTION_INTENT, this.mActionIntent);
        bundle.putBoolean(EXTRA_ENABLED, this.mEnabled);
        bundle.putBoolean(EXTRA_SHOULD_SHOW_ICON, this.mShouldShowIcon);
        return bundle;
    }

    public static RemoteActionCompat createFromBundle(Bundle bundle) {
        RemoteActionCompat remoteActionCompat = new RemoteActionCompat(IconCompat.createFromBundle(bundle.getBundle(EXTRA_ICON)), bundle.getCharSequence("title"), bundle.getCharSequence(EXTRA_CONTENT_DESCRIPTION), (PendingIntent) bundle.getParcelable(EXTRA_ACTION_INTENT));
        remoteActionCompat.setEnabled(bundle.getBoolean(EXTRA_ENABLED));
        remoteActionCompat.setShouldShowIcon(bundle.getBoolean(EXTRA_SHOULD_SHOW_ICON));
        return remoteActionCompat;
    }
}
