package com.accessorydm.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.accessorydm.ui.dialog.XUIDialogContract;
import com.accessorydm.ui.dialog.model.buttonstrategy.ButtonStrategy;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.sdk.mobileservice.social.group.provider.GroupMemberContract;
import com.sec.android.fotaprovider.R;

public class XUIDialogFragment extends DialogFragment implements XUIDialogContract.View {
    private XUIDialogContract.Presenter presenter;

    static /* synthetic */ boolean lambda$blockKeyEvents$1(int i, DialogInterface dialogInterface, int i2, KeyEvent keyEvent) {
        return i2 == i;
    }

    public static XUIDialogFragment newInstance(int i) {
        XUIDialogFragment xUIDialogFragment = new XUIDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GroupMemberContract.GroupMember.ID, i);
        xUIDialogFragment.setArguments(bundle);
        return xUIDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Log.I("");
        return new AlertDialog.Builder(getActivity(), R.style.FotaProviderTheme_Dialog).create();
    }

    public void onActivityCreated(Bundle bundle) {
        Log.I("");
        this.presenter = new XUIDialogPresenter(this, XUIDialog.valueOf(getArguments().getInt(GroupMemberContract.GroupMember.ID)));
        this.presenter.onCreate();
        super.onActivityCreated(bundle);
    }

    public void setDialogTitle(String str) {
        if (str != null) {
            View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_title, (ViewGroup) null);
            ((TextView) inflate.findViewById(R.id.tv_dialog_title)).setText(str);
            ((AlertDialog) getDialog()).setCustomTitle(inflate);
        }
    }

    public void setDialogBody(int i, String str) {
        if (str != null) {
            View inflate = getActivity().getLayoutInflater().inflate(i, (ViewGroup) null);
            ((TextView) inflate.findViewById(R.id.tv_dialog_body)).setText(str);
            ((AlertDialog) getDialog()).setView(inflate);
        }
    }

    public void setButton(ButtonStrategy buttonStrategy) {
        if (buttonStrategy != ButtonStrategy.NONE) {
            ((AlertDialog) getDialog()).setButton(buttonStrategy.getId(), buttonStrategy.getText(), new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ButtonStrategy.this.onClick();
                }
            });
        }
    }

    public void setGravity(int i) {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setGravity(i);
        }
    }

    public void blockKeyEvents(int[] iArr) {
        if (iArr != null) {
            for (int r2 : iArr) {
                getDialog().setOnKeyListener(new DialogInterface.OnKeyListener(r2) {
                    private final /* synthetic */ int f$0;

                    {
                        this.f$0 = r1;
                    }

                    public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        return XUIDialogFragment.lambda$blockKeyEvents$1(this.f$0, dialogInterface, i, keyEvent);
                    }
                });
            }
        }
    }

    public void setCancelable(boolean z) {
        getDialog().setCanceledOnTouchOutside(z);
        getDialog().setCancelable(z);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        Log.I("");
        XUIDialogContract.Presenter presenter2 = this.presenter;
        if (presenter2 != null) {
            presenter2.onDismiss();
        }
        finish();
        super.onDismiss(dialogInterface);
    }

    public void onCancel(DialogInterface dialogInterface) {
        Log.I("");
        super.onCancel(dialogInterface);
    }

    private void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
