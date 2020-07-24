package com.samsung.accessory.neobeanmgr.module.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.samsung.accessory.fotaprovider.FotaProviderEventHandler;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.BuildConfig;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.soagent.SOAgentServiceUtil;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.common.ui.OnSingleClickListener;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.util.BluetoothUtil;
import com.samsung.accessory.neobeanmgr.common.util.ResponseCallback;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.bluetooth.BluetoothManagerEnabler;
import com.samsung.accessory.neobeanmgr.core.fota.util.FotaUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.uhmdb.UhmDatabase;
import com.samsung.accessory.neobeanmgr.module.aboutmenu.AboutEarbudsActivity;
import com.samsung.accessory.neobeanmgr.module.aboutmenu.LabsActivity;
import com.samsung.accessory.neobeanmgr.module.base.PermissionCheckActivity;
import com.samsung.accessory.neobeanmgr.module.home.card.Card;
import com.samsung.accessory.neobeanmgr.module.home.card.CardActiveNoiseCanceling;
import com.samsung.accessory.neobeanmgr.module.home.card.CardCleaning;
import com.samsung.accessory.neobeanmgr.module.home.card.CardEarbuds;
import com.samsung.accessory.neobeanmgr.module.home.card.CardEqualizer;
import com.samsung.accessory.neobeanmgr.module.home.card.CardFota;
import com.samsung.accessory.neobeanmgr.module.home.card.CardMenuAbout;
import com.samsung.accessory.neobeanmgr.module.home.card.CardMenuMain;
import com.samsung.accessory.neobeanmgr.module.home.card.CardOobeTips;
import com.samsung.accessory.neobeanmgr.module.home.card.CardSeamlessConnection;
import com.samsung.accessory.neobeanmgr.module.home.drawer.Drawer;
import com.samsung.accessory.neobeanmgr.module.mainmenu.AdvancedActivity;
import com.samsung.accessory.neobeanmgr.module.mainmenu.FindMyEarbudsActivity;
import com.samsung.accessory.neobeanmgr.module.mainmenu.GeneralActivity;
import com.samsung.accessory.neobeanmgr.module.mainmenu.TouchpadActivity;
import com.samsung.accessory.neobeanmgr.module.notification.NewNotificationActivity;
import com.samsung.accessory.neobeanmgr.module.softwareupdate.SoftwareUpdateActivity;
import com.samsung.accessory.neobeanmgr.module.tipsmanual.TipsAndUserManualActivity;
import com.samsung.android.fotaagent.update.UpdateInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class HomeActivity extends PermissionCheckActivity implements Card.CardOwnerActivity, Drawer.DrawerOwnerActivity {
    private static final int DAYS_30 = 30;
    private static final int DRAWER_GRAVITY = 8388611;
    public static final String EXTRA_AUTO_CONNECT = "HomeActivity.extra.AUTO_CONNECT";
    public static final String EXTRA_FROM_SETUPWIZARD = "HomeActivity.extra.FROM_SETUPWIZARD";
    private static final int MAX_CANCEL_COUNT = 3;
    private static final int MAX_SHOWING_COUNT = 5;
    private static final String TAG = "NeoBean_HomeActivity";
    private final int COLOR_BLACK = Application.getContext().getResources().getColor(R.color.color_black);
    private ArrayList<Card> mCards;
    private Drawer mDrawer;
    /* access modifiers changed from: private */
    public DrawerLayout mDrawerLayout;
    private Dialog mForegroundDialog;
    private View mGradationBg;
    /* access modifiers changed from: private */
    public View mLayoutHome;
    /* access modifiers changed from: private */
    public ProgressBar mProgressBar;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Can't fix incorrect switch cases order */
        public void onReceive(Context context, Intent intent) {
            char c;
            Log.d(HomeActivity.TAG, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            switch (action.hashCode()) {
                case -2136954580:
                    if (action.equals(CoreService.ACTION_MSG_ID_DEBUG_SERIAL_NUMBER)) {
                        c = 13;
                        break;
                    }
                case -2043421558:
                    if (action.equals(CoreService.ACTION_MSG_ID_STATUS_UPDATED)) {
                        c = 5;
                        break;
                    }
                case -1856324259:
                    if (action.equals(CoreService.ACTION_DEVICE_EXTENDED_STATUS_READY)) {
                        c = 10;
                        break;
                    }
                case -1354974214:
                    if (action.equals(CoreService.ACTION_DEVICE_DISCONNECTED)) {
                        c = 3;
                        break;
                    }
                case -1314239911:
                    if (action.equals(CoreService.ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED)) {
                        c = 12;
                        break;
                    }
                case -1197934452:
                    if (action.equals(CoreService.ACTION_MSG_ID_CALL_STATE)) {
                        c = 8;
                        break;
                    }
                case -1055987649:
                    if (action.equals(CoreService.ACTION_MSG_ID_EQUALIZER_TYPE_UPDATED)) {
                        c = 7;
                        break;
                    }
                case -1055608668:
                    if (action.equals(CoreService.ACTION_MSG_ID_FOTA_EMERGENCY)) {
                        c = 15;
                        break;
                    }
                case -415576694:
                    if (action.equals(CoreService.ACTION_DEVICE_CONNECTED)) {
                        c = 2;
                        break;
                    }
                case -145626792:
                    if (action.equals(CoreService.ACTION_MSG_ID_EXTENDED_STATUS_UPDATED)) {
                        c = 6;
                        break;
                    }
                case -117388702:
                    if (action.equals(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT)) {
                        c = 14;
                        break;
                    }
                case -46786983:
                    if (action.equals(CoreService.ACTION_MSG_ID_DEBUG_GET_ALL_DATA)) {
                        c = 4;
                        break;
                    }
                case 2028599:
                    if (action.equals(CoreService.ACTION_DEVICE_CONNECTING)) {
                        c = 0;
                        break;
                    }
                case 945476551:
                    if (action.equals(CoreService.ACTION_DEVICE_DISCONNECTING)) {
                        c = 1;
                        break;
                    }
                case 1333557313:
                    if (action.equals(CoreService.ACTION_MSG_FOTA_CHECK_UPDATE)) {
                        c = 16;
                        break;
                    }
                case 1791209090:
                    if (action.equals(CoreService.ACTION_MSG_ID_SCO_STATE_UPDATED)) {
                        c = 9;
                        break;
                    }
                case 1936469230:
                    if (action.equals(CoreService.ACTION_MSG_ID_NOISE_REDUCTION_UPDATED)) {
                        c = 11;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    HomeActivity.this.mProgressBar.setVisibility(0);
                    HomeActivity.this.updateUI();
                    return;
                case 1:
                    HomeActivity.this.mProgressBar.setVisibility(0);
                    HomeActivity.this.hideTipCard();
                    return;
                case 2:
                case 3:
                case 4:
                    HomeActivity.this.updateTipCard();
                    HomeActivity.this.mProgressBar.setVisibility(8);
                    HomeActivity.this.updateUI();
                    return;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    HomeActivity.this.updateUI();
                    return;
                case 13:
                    HomeActivity.this.checkSellOutInfoUpdate();
                    return;
                case 14:
                    int intExtra = intent.getIntExtra(FotaUtil.FOTA_RESULT, 0);
                    Log.d(HomeActivity.TAG, "ACTION_FOTA_PROGRESS_COPY_RESULT : " + intExtra);
                    if (intExtra == 1) {
                        Log.d(HomeActivity.TAG, "ACTION_FOTA_UPDATE_DONE");
                        FotaUtil.setLastDoneTime(Calendar.getInstance().getTimeInMillis());
                        HomeActivity.this.dialogFOTADone();
                        return;
                    }
                    return;
                case 15:
                    Log.d(HomeActivity.TAG, "ACTION_MSG_ID_FOTA_EMERGENCY");
                    HomeActivity.this.emergencyFotaDialog();
                    return;
                case 16:
                    HomeActivity.this.updateTipCard();
                    return;
                default:
                    return;
            }
        }
    };
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private View mTextBadgeOnDrawerButton;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate() : versionCode=2020070651");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_home);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.mTextBadgeOnDrawerButton = findViewById(R.id.text_badge_notification);
        findViewById(R.id.image_drawer_button).setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                HomeActivity.this.openDrawer();
            }
        });
        setTitle();
        this.mGradationBg = findViewById(R.id.view_gradation_bg);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);
        this.mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            public void onDrawerSlide(View view, float f) {
                Log.v(HomeActivity.TAG, "onDrawerSlide() : " + f);
                float width = ((float) view.getWidth()) * f;
                View access$000 = HomeActivity.this.mLayoutHome;
                if (UiUtil.isLayoutRtl(HomeActivity.this.mLayoutHome)) {
                    width = -width;
                }
                access$000.setTranslationX(width);
            }

            public void onDrawerOpened(View view) {
                Log.d(HomeActivity.TAG, "onDrawerOpened()");
            }

            public void onDrawerClosed(View view) {
                Log.d(HomeActivity.TAG, "onDrawerClosed()");
            }

            public void onDrawerStateChanged(int i) {
                Log.v(HomeActivity.TAG, "onDrawerStateChanged() : " + i);
            }
        });
        this.mLayoutHome = findViewById(R.id.layout_home);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        AnonymousClass3 r12 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.UPDATE_EARBUDS_SOFTWARE, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, SoftwareUpdateActivity.class));
            }
        };
        AnonymousClass4 r0 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.TIPS_AND_USER_MANUAL, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, TipsAndUserManualActivity.class));
            }
        };
        AnonymousClass5 r5 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.LABS, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, LabsActivity.class));
            }
        };
        AnonymousClass6 r8 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.ABOUT_EARBUDS, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, AboutEarbudsActivity.class));
            }
        };
        AnonymousClass7 r3 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.NOTIFICATIONS, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, NewNotificationActivity.class));
            }
        };
        AnonymousClass8 r2 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.TOUCHPAD, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, TouchpadActivity.class));
            }
        };
        AnonymousClass9 r4 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.ADVANCED, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, AdvancedActivity.class));
            }
        };
        AnonymousClass10 r6 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.FIND_MY_EARBUDS, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, FindMyEarbudsActivity.class));
            }
        };
        AnonymousClass11 r7 = new OnSingleClickListener() {
            public void onSingleClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.GENERAL, SA.Screen.HOME);
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, GeneralActivity.class));
            }
        };
        this.mCards = new ArrayList<>();
        this.mRecyclerViewAdapter = new RecyclerViewAdapter(this.mCards);
        this.mCards.add(new CardEarbuds(this));
        this.mCards.add(new CardActiveNoiseCanceling());
        this.mCards.add(new CardEqualizer());
        this.mCards.add(new CardMenuMain(r2, r3, r4, r5, r6, r7));
        this.mCards.add(new CardMenuAbout(r12, r0, r8));
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setAdapter(this.mRecyclerViewAdapter);
        this.mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.card_between_space)));
        this.mDrawer = new Drawer(this);
        this.mProgressBar = (ProgressBar) findViewById(R.id.progress_connecting);
        registerReceiver();
        handleIntent(getIntent());
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(300);
        defaultItemAnimator.setRemoveDuration(300);
        this.mRecyclerView.setItemAnimator(defaultItemAnimator);
        checkSellOutInfoUpdate();
        updateTipCard();
        initCardShowTime();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            Log.e(TAG, "handleIntent() : intent == null");
        } else if ((intent.getBooleanExtra(EXTRA_AUTO_CONNECT, false) || !Preferences.getBoolean(PreferenceKey.HOME_DISCONNECTED_BY_USER, false)) && !Application.getCoreService().isConnected(UhmFwUtil.getLastLaunchDeviceId())) {
            Log.d(TAG, "handleIntent() : AUTO_CONNECT");
            new Handler().post(new Runnable() {
                public void run() {
                    HomeActivity.this.requestConnectToDevice();
                }
            });
        }
    }

    private void setTitle() {
        String deviceName = BluetoothUtil.getDeviceName(UhmFwUtil.getLastLaunchDeviceId());
        String aliasName = BluetoothUtil.getAliasName(UhmFwUtil.getLastLaunchDeviceId());
        Log.i(TAG, "originalBTName=" + deviceName + ", aliasName=" + aliasName);
        if (aliasName == null || aliasName.equals(deviceName) || aliasName.equals("Galaxy Buds Live") || aliasName.equals(Application.DEVICE_NAME_COMPAT)) {
            findViewById(R.id.image_app_name).setVisibility(0);
            findViewById(R.id.text_app_name).setVisibility(8);
            findViewById(R.id.focus_view_title).setContentDescription(getString(R.string.app_name));
            return;
        }
        findViewById(R.id.image_app_name).setVisibility(8);
        findViewById(R.id.text_app_name).setVisibility(0);
        ((AppCompatTextView) findViewById(R.id.text_app_name)).setText(aliasName);
        findViewById(R.id.focus_view_title).setContentDescription(aliasName);
    }

    /* access modifiers changed from: private */
    public void updateTipCard() {
        if (Application.getCoreService().isConnected()) {
            insertTipCard();
        } else {
            hideTipCard();
        }
    }

    public void removeTipCard() {
        Log.d(TAG, "removeCard()::" + this.mCards.get(1).getType());
        this.mRecyclerView.getRecycledViewPool().clear();
        this.mRecyclerViewAdapter.removeItem(1);
        updateTipCard();
    }

    private int getCurTipCard() {
        int type = this.mCards.get(1).getType();
        switch (type) {
            case 101:
            case 102:
            case 103:
            case 105:
                return type;
            default:
                return 100;
        }
    }

    public void insertTipCard() {
        int selectNextTipCard = selectNextTipCard();
        Log.d(TAG, "insertTipCard():: nextCard=" + selectNextTipCard + ", curCard=" + getCurTipCard());
        if (isShowingTipCard() && selectNextTipCard != getCurTipCard()) {
            hideTipCard();
        }
        if (!isShowingTipCard()) {
            switch (selectNextTipCard) {
                case 101:
                    this.mRecyclerViewAdapter.insertItem(1, new CardFota(this));
                    return;
                case 102:
                    this.mRecyclerViewAdapter.insertItem(1, new CardOobeTips(this));
                    return;
                case 103:
                    this.mRecyclerViewAdapter.insertItem(1, new CardCleaning(this));
                    return;
                case 105:
                    this.mRecyclerViewAdapter.insertItem(1, new CardSeamlessConnection(this));
                    return;
                default:
                    return;
            }
        }
    }

    private int selectNextTipCard() {
        if (FotaUtil.getCheckFotaUpdate() && Preferences.getBoolean(PreferenceKey.FOTA_CARD_SHOW_AGAIN, true, UhmFwUtil.getLastLaunchDeviceId())) {
            return 101;
        }
        if (getIntent().getBooleanExtra(EXTRA_FROM_SETUPWIZARD, false) && Preferences.getBoolean(PreferenceKey.OOBE_CARD_SHOW_AGAIN, true, UhmFwUtil.getLastLaunchDeviceId())) {
            return 102;
        }
        if (Preferences.getInt(PreferenceKey.CLEANING_CARD_SHOW_COUNT, 0) < 4) {
            long j = Preferences.getLong(PreferenceKey.CLEANING_CARD_SHOW_TIME, 0);
            long currentTimeMillis = System.currentTimeMillis();
            Log.d(TAG, "CardCleaning : showTime=" + UiUtil.MillisToString(j) + ", curTime=" + UiUtil.MillisToString(currentTimeMillis));
            if (currentTimeMillis >= j) {
                return 103;
            }
        }
        return (!CardSeamlessConnection.isSupported() || !Preferences.getBoolean(PreferenceKey.SEAMLESS_CONNECTION_CARD_SHOW_AGAIN, true, Preferences.MODE_MANAGER)) ? 100 : 105;
    }

    /* access modifiers changed from: private */
    public void hideTipCard() {
        Log.d(TAG, "hideTipCard() : " + getCurTipCard());
        if (isShowingTipCard()) {
            this.mRecyclerViewAdapter.removeItem(1);
        }
    }

    private boolean isShowingTipCard() {
        return getCurTipCard() != 100;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        SamsungAnalyticsUtil.sendPage(SA.Screen.HOME);
        checkShowUpdateBadge();
        updateTipCard();
        updateUI();
        setTitle();
        this.mDrawer.updateProfileImage();
        Application.getUhmDatabase().postCleanUpUnpairedDevices();
        Application.getUhmDatabase().postUpdatePluginDeviceName();
        Application.getUhmDatabase().postUpdateDeviceConnectionState();
        Application.getUhmDatabase().postUpdateLastLaunchDevice();
        closeDrawerDirectly();
        if (FotaUtil.getEmergencyFOTAIsRunning()) {
            emergencyFotaDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        this.mDrawer.destroy();
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        Iterator<Card> it = this.mCards.iterator();
        while (it.hasNext()) {
            it.next().updateUI();
        }
        updateDrawerBadge();
        findViewById(R.id.layout_drawer).setImportantForAccessibility(2);
        findViewById(R.id.layout_coordinator).setImportantForAccessibility(2);
    }

    private void updateBadgeOnDrawerButton() {
        int i = 0;
        boolean z = Preferences.getBoolean(PreferenceKey.EXISTED_NEW_VERSION_PLUGIN, false, UhmFwUtil.getLastLaunchDeviceId());
        View view = this.mTextBadgeOnDrawerButton;
        if (!z) {
            i = 8;
        }
        view.setVisibility(i);
    }

    private void updateDrawerBadge() {
        Log.d(TAG, "updateDrawerBadge()");
        updateBadgeOnDrawerButton();
        this.mDrawer.updateGalaxyWearableBadge();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_MSG_ID_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_EXTENDED_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_DEVICE_EXTENDED_STATUS_READY);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_DEBUG_GET_ALL_DATA);
        intentFilter.addAction(CoreService.ACTION_DEVICE_CONNECTING);
        intentFilter.addAction(CoreService.ACTION_DEVICE_CONNECTED);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTING);
        intentFilter.addAction(CoreService.ACTION_DEVICE_DISCONNECTED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_DEBUG_SERIAL_NUMBER);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_EQUALIZER_TYPE_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_CALL_STATE);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_SCO_STATE_UPDATED);
        intentFilter.addAction(FotaUtil.ACTION_FOTA_PROGRESS_COPY_RESULT);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_FOTA_EMERGENCY);
        intentFilter.addAction(CoreService.ACTION_MSG_FOTA_CHECK_UPDATE);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_NOISE_REDUCTION_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED);
        registerReceiver(this.mReceiver, intentFilter);
    }

    public void requestConnectToDevice() {
        Log.d(TAG, "requestConnectToDevice()");
        if (Util.isEmulator()) {
            Application.getCoreService().emulateConnected();
        } else if (BluetoothUtil.getAdapter() == null) {
            Log.e(TAG, "requestConnectToDevice() : BluetoothUtil.getAdapter() == null");
        } else if (Application.getBluetoothManager().isReady()) {
            Application.getCoreService().connectToDevice();
        } else if (!BluetoothUtil.isAdapterOn()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage((int) R.string.turn_on_bluetooth_q);
            builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    new BluetoothManagerEnabler(new ResponseCallback() {
                        public void onResponse(String str) {
                            if (str == null) {
                                Application.getCoreService().connectToDevice();
                                return;
                            }
                            HomeActivity homeActivity = HomeActivity.this;
                            Toast.makeText(homeActivity, "Error: " + str, 1).show();
                        }
                    }).execute();
                }
            });
            builder.setNegativeButton(17039360, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        } else {
            Log.e(TAG, "requestConnectToDevice() : BluetoothManager is NOT ready");
            Application.getBluetoothManager().rebindProfiles();
        }
    }

    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        if (isDrawerOpen()) {
            closeDrawer((Runnable) null);
        } else {
            super.onBackPressed();
        }
    }

    class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int mBottom;

        public VerticalSpaceItemDecoration(int i) {
            this.mBottom = i;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            if (recyclerView.getChildAdapterPosition(view) < recyclerView.getAdapter().getItemCount() - 1) {
                rect.bottom = this.mBottom;
            }
        }
    }

    public static void startSamsungMembers(Activity activity) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("voc://view/contactUs"));
        intent.putExtra("packageName", "com.samsung.accessory.fridaymgr");
        intent.putExtra("appId", "q5pb6l4o1v");
        intent.putExtra("appName", "Galaxy_Earbuds");
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Log.e(TAG, "can not find activity, intent[" + intent + "]");
        }
    }

    public void startSamsungMembers() {
        startSamsungMembers(this);
    }

    public boolean isDrawerOpen() {
        return this.mDrawerLayout.isDrawerOpen(8388611);
    }

    public void openDrawer() {
        Log.d(TAG, "openDrawer()");
        if (!isDrawerOpen()) {
            this.mDrawerLayout.openDrawer(8388611);
        }
    }

    public void closeDrawer(final Runnable runnable) {
        Log.d(TAG, "closeDrawer()");
        if (isDrawerOpen()) {
            if (runnable != null) {
                final Handler handler = new Handler();
                final AnonymousClass16 r1 = new DrawerLayout.DrawerListener() {
                    public void onDrawerOpened(View view) {
                    }

                    public void onDrawerSlide(View view, float f) {
                    }

                    public void onDrawerStateChanged(int i) {
                    }

                    public void onDrawerClosed(View view) {
                        Log.d(HomeActivity.TAG, "onDrawerClosed() : afterClose.run()");
                        handler.removeCallbacksAndMessages((Object) null);
                        HomeActivity.this.mDrawerLayout.removeDrawerListener(this);
                        runnable.run();
                    }
                };
                this.mDrawerLayout.addDrawerListener(r1);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.w(HomeActivity.TAG, "closeDrawer() : timeoverHandler.run()");
                        HomeActivity.this.mDrawerLayout.removeDrawerListener(r1);
                    }
                }, UpdateInterface.HOLDING_AFTER_BT_CONNECTED);
            }
            this.mDrawerLayout.closeDrawer(8388611);
        }
    }

    public void closeDrawerDirectly() {
        Log.d(TAG, "closeDrawerDirectly()");
        if (isDrawerOpen()) {
            this.mDrawerLayout.closeDrawer(8388611, false);
        }
    }

    /* access modifiers changed from: private */
    public void checkSellOutInfoUpdate() {
        Log.d(TAG, "checkSellOutInfoUpdate()");
        EarBudsInfo earBudsInfo = Application.getCoreService().getEarBudsInfo();
        SOAgentServiceUtil.checkSellOutInfoUpdate(earBudsInfo.serialNumber_left, earBudsInfo.serialNumber_right, earBudsInfo.address);
    }

    /* access modifiers changed from: private */
    public void dialogFOTADone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((int) R.string.keep_the_case_open);
        builder.setMessage((int) R.string.install_now_notice_content);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    /* access modifiers changed from: private */
    public void emergencyFotaDialog() {
        Log.d(TAG, "emergencyFotaDialog");
        Dialog dialog = this.mForegroundDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mForegroundDialog.dismiss();
            this.mForegroundDialog = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((int) R.string.fota_miss_match_version_title);
        builder.setMessage((int) R.string.fota_miss_match_version_content);
        builder.setPositiveButton((int) R.string.update, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                HomeActivity.this.emergencyFota();
            }
        });
        builder.setNegativeButton((int) R.string.later, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        this.mForegroundDialog = builder.create();
        this.mForegroundDialog.show();
    }

    /* access modifiers changed from: private */
    public void emergencyFota() {
        Log.d(TAG, "emergencyFota");
        FotaProviderEventHandler.softwareUpdate(Application.getContext());
    }

    private void checkShowUpdateBadge() {
        int queryAppUpdateCancelCount = UhmDatabase.queryAppUpdateCancelCount(BuildConfig.APPLICATION_ID);
        if (queryAppUpdateCancelCount < 32767) {
            int i = Preferences.getInt(PreferenceKey.PREV_UPDATE_CANCEL_COUNT, 0, UhmFwUtil.getLastLaunchDeviceId());
            if (queryAppUpdateCancelCount == 0) {
                Preferences.putBoolean(PreferenceKey.EXISTED_NEW_VERSION_PLUGIN, false, UhmFwUtil.getLastLaunchDeviceId());
            } else if (i < queryAppUpdateCancelCount) {
                Preferences.putBoolean(PreferenceKey.EXISTED_NEW_VERSION_PLUGIN, true, UhmFwUtil.getLastLaunchDeviceId());
            }
            Preferences.putInt(PreferenceKey.PREV_UPDATE_CANCEL_COUNT, Integer.valueOf(queryAppUpdateCancelCount), UhmFwUtil.getLastLaunchDeviceId());
        }
        updateDrawerBadge();
    }

    public void setBgGradationColor(int i) {
        Log.d(TAG, "setBgGradationColor() : " + i);
        Drawable background = this.mGradationBg.getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColors(new int[]{i, this.COLOR_BLACK});
        }
    }

    private void initCardShowTime() {
        Log.d(TAG, "initCardShowTime()");
        long j = 0;
        if (Preferences.getLong(PreferenceKey.CLEANING_CARD_SHOW_TIME, 0) == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            if (!Application.DEBUG_MODE) {
                j = Card.MILLIS_A_WEEK;
            }
            long j2 = currentTimeMillis + j;
            Log.d(TAG, "CardCleaning : nextShowTime=" + UiUtil.MillisToString(j2));
            Preferences.putLong(PreferenceKey.CLEANING_CARD_SHOW_TIME, Long.valueOf(j2));
        }
    }
}
