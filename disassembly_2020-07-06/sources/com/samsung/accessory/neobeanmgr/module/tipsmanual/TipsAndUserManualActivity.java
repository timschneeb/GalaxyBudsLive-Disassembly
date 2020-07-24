package com.samsung.accessory.neobeanmgr.module.tipsmanual;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.PageIndicatorView;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;

public class TipsAndUserManualActivity extends PermissionCheckActivity {
    private static final String ONLINE_USER_MANUAL_URL = "http://www.samsung.com/m-manual/mod/SM-R180";
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + TipsAndUserManualActivity.class.getSimpleName());
    PageIndicatorView mPageIndicatorView;
    /* access modifiers changed from: private */
    public int prevPosition = 0;
    private ViewPager.OnPageChangeListener setViewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
            String access$000 = TipsAndUserManualActivity.TAG;
            Log.d(access$000, "onPageSelected() prevPosition : " + TipsAndUserManualActivity.this.prevPosition + ", position  : " + i);
            TipsAndUserManualActivity.this.setNavigationFocus(i);
            UiUtil.awakeScrollbarWidthChildView(TipsAndUserManualActivity.this.viewPager.getChildAt(i));
            if (TipsAndUserManualActivity.this.tipsAdapter.getItem(TipsAndUserManualActivity.this.prevPosition) instanceof OnSelectedTipsFragment) {
                ((OnSelectedTipsFragment) TipsAndUserManualActivity.this.tipsAdapter.getItem(TipsAndUserManualActivity.this.prevPosition)).onSelected(false);
            }
            if (TipsAndUserManualActivity.this.tipsAdapter.getItem(i) instanceof OnSelectedTipsFragment) {
                ((OnSelectedTipsFragment) TipsAndUserManualActivity.this.tipsAdapter.getItem(i)).onSelected(true);
            }
            int unused = TipsAndUserManualActivity.this.prevPosition = i;
        }
    };
    TipsAdapter tipsAdapter;
    TextView userManual;
    ViewPager viewPager;

    interface OnSelectedTipsFragment {
        void onSelected(boolean z);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.i(TAG, "onCreate()");
        super.onCreate((Bundle) null);
        setContentView((int) R.layout.activity_tips_and_user_manual);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.tipsAdapter = new TipsAdapter(getSupportFragmentManager());
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.viewPager.setOffscreenPageLimit(5);
        this.viewPager.setAdapter(this.tipsAdapter);
        this.viewPager.addOnPageChangeListener(this.setViewPagerOnPageChangeListener);
        this.mPageIndicatorView = (PageIndicatorView) findViewById(R.id.page_indicator);
        this.mPageIndicatorView.setPageMax(this.tipsAdapter.getCount());
        this.userManual = (TextView) findViewById(R.id.user_manual);
        this.userManual.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.USER_MANUAL, SA.Screen.TIPS_AND_USER_MANUAL);
                TipsAndUserManualActivity.startUserManual(TipsAndUserManualActivity.this);
            }
        });
        this.viewPager.setCurrentItem(UiUtil.rtlCompatIndex(0, this.tipsAdapter.getCount()));
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        SamsungAnalyticsUtil.sendPage(SA.Screen.TIPS_AND_USER_MANUAL);
    }

    public boolean onSupportNavigateUp() {
        SamsungAnalyticsUtil.sendEvent(SA.Event.UP_BUTTON, SA.Screen.TIPS_AND_USER_MANUAL);
        onBackPressed();
        return true;
    }

    /* access modifiers changed from: private */
    public void setNavigationFocus(int i) {
        this.mPageIndicatorView.setPageSelect(i);
    }

    public static void startUserManual(Activity activity) {
        Log.d(TAG, "startUserManual()");
        if (Util.getActiveNetworkInfo() >= 0) {
            UiUtil.startWebBrowser(activity, ONLINE_USER_MANUAL_URL);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle((CharSequence) activity.getString(R.string.no_network_connect));
        builder.setMessage((CharSequence) activity.getString(Util.isChinaModel() ? R.string.no_network_connect_description_chn : R.string.no_network_connect_description));
        builder.setPositiveButton((CharSequence) activity.getString(R.string.ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
