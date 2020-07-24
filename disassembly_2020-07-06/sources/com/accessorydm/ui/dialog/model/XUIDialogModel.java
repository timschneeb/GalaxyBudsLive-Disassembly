package com.accessorydm.ui.dialog.model;

import android.content.Context;
import com.accessorydm.XDMDmUtils;
import com.accessorydm.ui.dialog.model.buttonstrategy.ButtonStrategy;
import com.sec.android.fotaprovider.R;

public interface XUIDialogModel {
    int[] getBlockKeyEvents();

    int getBodyLayout();

    int getGravity();

    String getMessage();

    ButtonStrategy.Negative getNegativeButtonStrategy();

    ButtonStrategy.Neutral getNeutralButtonStrategy();

    ButtonStrategy.Positive getPositiveButtonStrategy();

    String getTitle();

    boolean isCancelable();

    void preExecute();

    public static class Base implements XUIDialogModel {
        private final String message;
        private final ButtonStrategy.Negative negativeButtonStrategy;
        private final ButtonStrategy.Neutral neutralButtonStrategy;
        private final ButtonStrategy.Positive positiveButtonStrategy;
        private final String title;

        public int[] getBlockKeyEvents() {
            return null;
        }

        public int getGravity() {
            return 80;
        }

        public boolean isCancelable() {
            return true;
        }

        public void preExecute() {
        }

        Base(String str, String str2, ButtonStrategy.Neutral neutral, ButtonStrategy.Negative negative, ButtonStrategy.Positive positive) {
            this.title = str;
            this.message = str2;
            this.neutralButtonStrategy = neutral;
            this.negativeButtonStrategy = negative;
            this.positiveButtonStrategy = positive;
        }

        public String getTitle() {
            return this.title;
        }

        public String getMessage() {
            return this.message;
        }

        public ButtonStrategy.Neutral getNeutralButtonStrategy() {
            return this.neutralButtonStrategy;
        }

        public ButtonStrategy.Negative getNegativeButtonStrategy() {
            return this.negativeButtonStrategy;
        }

        public ButtonStrategy.Positive getPositiveButtonStrategy() {
            return this.positiveButtonStrategy;
        }

        public int getBodyLayout() {
            return R.layout.dialog_body;
        }

        static String getString(int i) {
            if (i == -1) {
                return null;
            }
            return getContext().getString(i);
        }

        private static Context getContext() {
            return XDMDmUtils.getContext();
        }
    }

    public static class StubOk extends Base {
        public StubOk(String str, String str2) {
            super(str, str2, ButtonStrategy.Neutral.NONE, ButtonStrategy.Negative.NONE, new ButtonStrategy.StubOk());
        }
    }

    public static class ProgressWithNoButtons extends Base {
        public int[] getBlockKeyEvents() {
            return new int[]{4};
        }

        public int getGravity() {
            return 17;
        }

        public boolean isCancelable() {
            return false;
        }

        public ProgressWithNoButtons(String str, String str2) {
            super(str, str2, ButtonStrategy.Neutral.NONE, ButtonStrategy.Negative.NONE, ButtonStrategy.Positive.NONE);
        }

        public int getBodyLayout() {
            return R.layout.progress_circle_dialog;
        }
    }
}
