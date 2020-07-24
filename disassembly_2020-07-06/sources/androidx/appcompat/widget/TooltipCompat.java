package androidx.appcompat.widget;

import android.os.Build;
import android.view.View;

public class TooltipCompat {
    private static final ViewCompatImpl IMPL;

    private interface ViewCompatImpl {
        void seslSetTooltipForceActionBarPosX(boolean z);

        void seslSetTooltipForceBelow(boolean z);

        void seslSetTooltipNull(boolean z);

        void seslSetTooltipPosition(int i, int i2, int i3);

        void setTooltipText(View view, CharSequence charSequence);
    }

    private static class BaseViewCompatImpl implements ViewCompatImpl {
        private BaseViewCompatImpl() {
        }

        public void setTooltipText(View view, CharSequence charSequence) {
            TooltipCompatHandler.setTooltipText(view, charSequence);
        }

        public void seslSetTooltipPosition(int i, int i2, int i3) {
            TooltipCompatHandler.seslSetTooltipPosition(i, i2, i3);
        }

        public void seslSetTooltipNull(boolean z) {
            TooltipCompatHandler.seslSetTooltipNull(z);
        }

        public void seslSetTooltipForceBelow(boolean z) {
            TooltipCompatHandler.seslSetTooltipForceBelow(z);
        }

        public void seslSetTooltipForceActionBarPosX(boolean z) {
            TooltipCompatHandler.seslSetTooltipForceActionBarPosX(z);
        }
    }

    private static class Api26ViewCompatImpl implements ViewCompatImpl {
        private Api26ViewCompatImpl() {
        }

        public void setTooltipText(View view, CharSequence charSequence) {
            TooltipCompatHandler.setTooltipText(view, charSequence);
        }

        public void seslSetTooltipPosition(int i, int i2, int i3) {
            TooltipCompatHandler.seslSetTooltipPosition(i, i2, i3);
        }

        public void seslSetTooltipNull(boolean z) {
            TooltipCompatHandler.seslSetTooltipNull(z);
        }

        public void seslSetTooltipForceBelow(boolean z) {
            TooltipCompatHandler.seslSetTooltipForceBelow(z);
        }

        public void seslSetTooltipForceActionBarPosX(boolean z) {
            TooltipCompatHandler.seslSetTooltipForceActionBarPosX(z);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 26) {
            IMPL = new Api26ViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static void seslSetTooltipForceBelow(boolean z) {
        IMPL.seslSetTooltipForceBelow(z);
    }

    public static void seslSetTooltipForceActionBarPosX(boolean z) {
        IMPL.seslSetTooltipForceActionBarPosX(z);
    }

    public static void setTooltipText(View view, CharSequence charSequence) {
        IMPL.setTooltipText(view, charSequence);
    }

    public static void seslSetTooltipPosition(int i, int i2, int i3) {
        IMPL.seslSetTooltipPosition(i, i2, i3);
    }

    public static void seslSetTooltipNull(boolean z) {
        IMPL.seslSetTooltipNull(z);
    }

    private TooltipCompat() {
    }
}
