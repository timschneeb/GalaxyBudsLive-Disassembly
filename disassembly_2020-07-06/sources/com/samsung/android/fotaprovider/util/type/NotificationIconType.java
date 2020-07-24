package com.samsung.android.fotaprovider.util.type;

import com.sec.android.fotaprovider.R;

public enum NotificationIconType {
    WATCH {
        public int getIndicatorCompletion() {
            return R.drawable.ic_stat_notify_watch_completion;
        }

        public int getIndicatorDmSession() {
            return R.drawable.ic_stat_notify_watch_session;
        }

        public int getIndicatorPostpone() {
            return R.drawable.ic_stat_notify_watch_postpone;
        }
    },
    FIT2 {
        public int getIndicatorCompletion() {
            return R.drawable.ic_stat_notify_band_completion;
        }

        public int getIndicatorDmSession() {
            return R.drawable.ic_stat_notify_band_session;
        }

        public int getIndicatorPostpone() {
            return R.drawable.ic_stat_notify_band_postopone;
        }
    },
    EARBUDS {
        public int getIndicatorCompletion() {
            return R.drawable.ic_stat_notify_bean_completion;
        }

        public int getIndicatorDmSession() {
            return R.drawable.ic_stat_notify_bean_session;
        }

        public int getIndicatorPostpone() {
            return R.drawable.ic_stat_notify_bean_postpone;
        }
    };

    public abstract int getIndicatorCompletion();

    public abstract int getIndicatorDmSession();

    public abstract int getIndicatorPostpone();
}
