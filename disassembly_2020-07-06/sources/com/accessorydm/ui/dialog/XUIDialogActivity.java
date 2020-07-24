package com.accessorydm.ui.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import com.accessorydm.interfaces.XDMDefInterface;
import com.accessorydm.ui.UIManager;
import com.samsung.android.fotaprovider.log.Log;

public class XUIDialogActivity extends Activity {
    private static Activity m_DialogActivity;
    private static int m_DialogId;
    private boolean bIsDialog = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UIManager.getInstance().put(this);
        m_DialogActivity = this;
        m_DialogId = Integer.valueOf(getIntent().getAction()).intValue();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.bIsDialog = false;
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        Log.I("");
        int intValue = Integer.valueOf(intent.getAction()).intValue();
        int i = m_DialogId;
        if (i == intValue) {
            this.bIsDialog = true;
        } else {
            if (i > 0) {
                dismissDialogFragment(i);
            }
            m_DialogId = intValue;
        }
        super.onNewIntent(intent);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.I("");
        try {
            if (getFragmentManager().findFragmentById(m_DialogId) == null && !this.bIsDialog) {
                showDialogFragment(m_DialogId);
            }
        } catch (Exception e) {
            Log.E(e.toString());
        }
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onUserLeaveHint() {
        xuiRemoveDialog();
        super.onUserLeaveHint();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.I("");
        UIManager.getInstance().remove(this);
        super.onDestroy();
    }

    private void showDialogFragment(int i) {
        Log.I("Show dialog id : " + XUIDialog.valueOf(i));
        XUIDialogFragment newInstance = XUIDialogFragment.newInstance(i);
        FragmentManager fragmentManager = getFragmentManager();
        newInstance.show(fragmentManager, XDMDefInterface.XDM_DIALOG_TAG + i);
    }

    private void dismissDialogFragment(int i) {
        if (getFragmentManager() != null) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment findFragmentByTag = fragmentManager.findFragmentByTag(XDMDefInterface.XDM_DIALOG_TAG + i);
            if (findFragmentByTag != null) {
                ((DialogFragment) findFragmentByTag).dismiss();
            }
        }
    }

    public static void xuiRemoveDialog() {
        try {
            if (m_DialogActivity != null) {
                m_DialogId = 0;
                Log.I("DialogActivity Remove and reset mDialogId");
                m_DialogActivity.finish();
            }
        } catch (Exception e) {
            Log.E(e.toString());
        }
    }

    public static boolean xuiCheckDialog(int i) {
        if (m_DialogActivity == null || m_DialogId != i) {
            return false;
        }
        Log.I("XUIDialogActivity show dialog : " + i);
        return true;
    }
}
