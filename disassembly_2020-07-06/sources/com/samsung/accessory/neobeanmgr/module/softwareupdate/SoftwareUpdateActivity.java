package com.samsung.accessory.neobeanmgr.module.softwareupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.samsung.accessory.fotaprovider.FotaProviderEventHandler;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.OnSingleClickListener;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.fota.util.FotaUtil;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SoftwareUpdateActivity extends ConnectionActivity {
    private static final String TAG = "NeoBean_SoftwardUpdateActivity";
    private LinearLayout lastUpdateLayout;
    private TextView mBadgeForNotification;
    private LinearLayout mDownloadUpdatesManuallyLayout;
    private TextView mLastCheckedDayTextView;
    private TextView mLastUpdateContent;
    private TextView mLastUpdateText;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(SoftwareUpdateActivity.TAG, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            if (((action.hashCode() == -117388702 && action.equals(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT)) ? (char) 0 : 65535) == 0) {
                int intExtra = intent.getIntExtra(FotaUtil.FOTA_RESULT, 0);
                Log.d(SoftwareUpdateActivity.TAG, "ACTION_FOTA_PROGRESS_COPY_RESULT : " + intExtra);
                if (intExtra == 1) {
                    Log.d(SoftwareUpdateActivity.TAG, "ACTION_FOTA_UPDATE_DONE");
                    SoftwareUpdateActivity.this.finish();
                }
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "SoftwardUpdateActivity");
        setContentView((int) R.layout.activity_software_update);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Log.d(TAG, "onCreate()");
        this.mDownloadUpdatesManuallyLayout = (LinearLayout) findViewById(R.id.download_updates_manually_layout);
        this.mLastCheckedDayTextView = (TextView) findViewById(R.id.download_update_last_checked_on_content);
        this.lastUpdateLayout = (LinearLayout) findViewById(R.id.last_update_layout);
        this.mLastUpdateText = (TextView) findViewById(R.id.last_update_text);
        this.mLastUpdateContent = (TextView) findViewById(R.id.last_update_content);
        registerReceiver();
        this.mDownloadUpdatesManuallyLayout.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                Log.d(SoftwareUpdateActivity.TAG, "mDownloadUpdatesManuallyLayout : click");
                SamsungAnalyticsUtil.sendEvent(SA.Event.DOWNLOAD_AND_INSTALL, SA.Screen.UPDATE_EARBUDS_SOFTWARE);
                FotaProviderEventHandler.softwareUpdate(Application.getContext());
                Log.d(SoftwareUpdateActivity.TAG, "send broadcast : SOFTWARE_UPDATE");
            }
        });
        this.lastUpdateLayout.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.LAST_UPDATE, SA.Screen.UPDATE_EARBUDS_SOFTWARE);
                FotaProviderEventHandler.lastUpdate(Application.getContext());
                Log.d(SoftwareUpdateActivity.TAG, "FotaProviderEventHandler : LAST_UPDATE");
            }
        });
        this.mBadgeForNotification = (TextView) findViewById(R.id.badge_notification);
        setBadgeForNotification();
    }

    public boolean onSupportNavigateUp() {
        SamsungAnalyticsUtil.sendEvent(SA.Event.UP_BUTTON, SA.Screen.UPDATE_EARBUDS_SOFTWARE);
        finish();
        return true;
    }

    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        setBadgeForNotification();
        updateLastVersionCheckTimeText();
        updateLastUpdateTimeText();
        SamsungAnalyticsUtil.sendPage(SA.Screen.UPDATE_EARBUDS_SOFTWARE);
    }

    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        unregisterReceiver(this.mReceiver);
    }

    private void updateLastVersionCheckTimeText() {
        long lastSWVersionCheckTime = FotaUtil.getLastSWVersionCheckTime();
        if (lastSWVersionCheckTime > 0) {
            String format = DateFormat.getDateInstance(1).format(new Date(lastSWVersionCheckTime));
            this.mLastCheckedDayTextView.setText(getString(R.string.last_checked) + " " + format);
            return;
        }
        this.mLastCheckedDayTextView.setText(getString(R.string.download_update_manually_content1));
    }

    private void updateLastUpdateTimeText() {
        long lastDoneTime = FotaUtil.getLastDoneTime();
        if (lastDoneTime > 0) {
            this.mLastUpdateContent.setText(getString(R.string.last_update_body, new Object[]{DateFormat.getDateInstance(1).format(new Date(lastDoneTime)), new SimpleDateFormat("h:mm a").format(Long.valueOf(lastDoneTime))}));
            this.mLastUpdateText.setEnabled(true);
            this.mLastUpdateContent.setEnabled(true);
            this.lastUpdateLayout.setEnabled(true);
            this.lastUpdateLayout.setClickable(true);
            return;
        }
        this.mLastUpdateContent.setText(R.string.fota_update_no_info_content);
        this.mLastUpdateText.setEnabled(false);
        this.mLastUpdateContent.setEnabled(false);
        this.lastUpdateLayout.setEnabled(false);
        this.lastUpdateLayout.setClickable(false);
    }

    private void setBadgeForNotification() {
        if (FotaUtil.getCheckFotaUpdate()) {
            this.mBadgeForNotification.setVisibility(0);
        } else {
            this.mBadgeForNotification.setVisibility(4);
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT);
        registerReceiver(this.mReceiver, intentFilter);
    }
}
