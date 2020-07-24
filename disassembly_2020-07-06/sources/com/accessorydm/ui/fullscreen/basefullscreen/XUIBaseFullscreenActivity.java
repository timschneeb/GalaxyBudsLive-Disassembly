package com.accessorydm.ui.fullscreen.basefullscreen;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewStub;
import com.accessorydm.ui.UIManager;
import com.accessorydm.ui.fullscreen.basefullscreen.XUIBaseFullscreenContract;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.fotaprovider.util.UiUtil;
import com.sec.android.fotaprovider.R;

public abstract class XUIBaseFullscreenActivity extends Activity implements XUIBaseFullscreenContract.View {
    private ActionBar actionBar = null;

    /* access modifiers changed from: protected */
    public abstract XUIBaseFullscreenContract.Presenter getPresenter();

    public abstract void xuiGenerateBottomLayout(ViewStub viewStub);

    public abstract void xuiGenerateMiddleContentLayout(ViewStub viewStub);

    public abstract void xuiGenerateTopContentLayout(ViewStub viewStub);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.I("");
        super.onCreate(bundle);
        UiUtil.setOrientationFor(this);
        UIManager.getInstance().put(this);
        setContentView(R.layout.base_activity);
        this.actionBar = getActionBar();
        xuiGenerateTopContentLayout((ViewStub) findViewById(R.id.viewstub_top_content));
        xuiGenerateMiddleContentLayout((ViewStub) findViewById(R.id.viewstub_middle_content));
        xuiGenerateBottomLayout((ViewStub) findViewById(R.id.viewstub_bottom_content));
    }

    public void xuiSetActionBarTitleText(String str) {
        ActionBar actionBar2 = this.actionBar;
        if (actionBar2 != null) {
            actionBar2.setTitle(str);
        }
    }

    public void xuiOnBackPressed() {
        onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        Log.D("AsUp pressed");
        return getPresenter() != null && getPresenter().onOptionsItemSelected(menuItem.getItemId());
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        Log.D("BackKey pressed");
        return getPresenter() != null && getPresenter().onKeyDown(i);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
        UIManager.getInstance().remove(this);
        super.onDestroy();
    }
}
