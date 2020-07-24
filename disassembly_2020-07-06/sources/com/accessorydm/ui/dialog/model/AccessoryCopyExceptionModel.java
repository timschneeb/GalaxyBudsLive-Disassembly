package com.accessorydm.ui.dialog.model;

import com.accessorydm.filetransfer.XDMFileTransferManager;
import com.accessorydm.ui.dialog.model.XUIDialogModel;
import com.accessorydm.ui.dialog.model.buttonstrategy.ButtonStrategy;
import com.samsung.android.fotaprovider.util.type.DeviceType;

public final class AccessoryCopyExceptionModel extends XUIDialogModel.Base {

    public enum State {
        FAILED,
        RETRY_LATER,
        RETRY_CONFIRM
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AccessoryCopyExceptionModel(State state) {
        super(title(), message(state), ButtonStrategy.Neutral.NONE, state == State.RETRY_CONFIRM ? new ButtonStrategy.StubCancel() : ButtonStrategy.Negative.NONE, state == State.RETRY_CONFIRM ? new RetryPositiveButtonStrategy() : new ButtonStrategy.StubOk());
    }

    private static String title() {
        return getString(DeviceType.get().getTextType().getCopyRetryTitleId());
    }

    /* renamed from: com.accessorydm.ui.dialog.model.AccessoryCopyExceptionModel$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$accessorydm$ui$dialog$model$AccessoryCopyExceptionModel$State = new int[State.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            $SwitchMap$com$accessorydm$ui$dialog$model$AccessoryCopyExceptionModel$State[State.RETRY_LATER.ordinal()] = 1;
            $SwitchMap$com$accessorydm$ui$dialog$model$AccessoryCopyExceptionModel$State[State.RETRY_CONFIRM.ordinal()] = 2;
            try {
                $SwitchMap$com$accessorydm$ui$dialog$model$AccessoryCopyExceptionModel$State[State.FAILED.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private static String message(State state) {
        int i = AnonymousClass1.$SwitchMap$com$accessorydm$ui$dialog$model$AccessoryCopyExceptionModel$State[state.ordinal()];
        if (i == 1) {
            return getString(DeviceType.get().getTextType().getCopyRetryLaterMessageId());
        }
        if (i != 2) {
            return getString(DeviceType.get().getTextType().getCopyFailedMessageId());
        }
        return getString(DeviceType.get().getTextType().getCopyRetryMessageId());
    }

    private static class RetryPositiveButtonStrategy extends ButtonStrategy.Positive {
        RetryPositiveButtonStrategy() {
            super(XUIDialogModel.Base.getString(DeviceType.get().getTextType().getCopyRetryPositiveButtonId()));
        }

        /* access modifiers changed from: protected */
        public void doOnClick() {
            XDMFileTransferManager.checkDeviceInfo();
        }
    }
}
