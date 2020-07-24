package com.samsung.accessory.neobeanmgr.module.mainmenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.viewpager.widget.ViewPager;
import com.accessorydm.eng.core.XDMWbxml;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.uhm.BaseContentProvider;
import com.samsung.accessory.neobeanmgr.common.uhm.UhmFwUtil;
import com.samsung.accessory.neobeanmgr.common.ui.DropdownListPopup;
import com.samsung.accessory.neobeanmgr.common.ui.PageIndicatorView;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.util.SecurityUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLockTouchpad;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetTouchpadOption;
import com.samsung.accessory.neobeanmgr.module.base.ConnectionActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TouchpadActivity extends ConnectionActivity {
    public static final int DEFAULT_TOUCHPAD_OPTION = 2;
    private static final int LEFT_OPTION = 0;
    public static final int OPTION_BIXBY = 1;
    public static final int OPTION_NOISE_REDUCTION = 2;
    public static final int OPTION_OTHERS_LEFT = 5;
    public static final int OPTION_OTHERS_RIGHT = 6;
    public static final int OPTION_SPOTIFY = 4;
    public static final int OPTION_VOLUME = 3;
    private static final int RIGHT_OPTION = 1;
    /* access modifiers changed from: private */
    public static final String TAG = (Application.TAG_ + TouchpadActivity.class.getSimpleName());
    private TouchpadViewPagerAdapter adapter = null;
    private ArrayList<HashMap<String, String>> appListForA2A = new ArrayList<>();
    private Context context;
    /* access modifiers changed from: private */
    public boolean isReceivedMesseage = false;
    private final BroadcastReceiver mAppToAppPackageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Application.getCoreService().getConnectionState() == 2) {
                if (intent.getData() == null || !intent.getData().getSchemeSpecificPart().equals(Application.getContext().getPackageName())) {
                    TouchpadActivity.this.checkApp2App();
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public int mClickedSide;
    /* access modifiers changed from: private */
    public DropdownListPopup mCurDropdownPopup;
    /* access modifiers changed from: private */
    public EarBudsInfo mEarbudsInfo;
    private RelativeLayout mLeftOptionLayout;
    private TextView mLeftOptionTxt;
    /* access modifiers changed from: private */
    public SwitchCompat mLockTouchpadSwitch;
    /* access modifiers changed from: private */
    public PageIndicatorView mPageIndicatorView;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0067  */
        public void onReceive(Context context, Intent intent) {
            char c;
            String access$300 = TouchpadActivity.TAG;
            Log.d(access$300, "onReceive() : " + intent.getAction());
            String action = intent.getAction();
            int hashCode = action.hashCode();
            if (hashCode != -2043421558) {
                if (hashCode == -1314239911 && action.equals(CoreService.ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED)) {
                    c = 1;
                    if (c == 0) {
                        TouchpadActivity.this.updateTouchpadUI();
                        return;
                    } else if (c == 1) {
                        boolean unused = TouchpadActivity.this.isReceivedMesseage = true;
                        TouchpadActivity.this.mLockTouchpadSwitch.setChecked(TouchpadActivity.this.mEarbudsInfo.touchpadLocked);
                        boolean unused2 = TouchpadActivity.this.isReceivedMesseage = false;
                        return;
                    } else {
                        return;
                    }
                }
            } else if (action.equals(CoreService.ACTION_MSG_ID_STATUS_UPDATED)) {
                c = 0;
                if (c == 0) {
                }
            }
            c = 65535;
            if (c == 0) {
            }
        }
    };
    private RelativeLayout mRightOptionLayout;
    private TextView mRightOptionTxt;
    private ViewPager.OnPageChangeListener setViewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
            String access$300 = TouchpadActivity.TAG;
            Log.d(access$300, "position::" + i);
            TouchpadActivity.this.mPageIndicatorView.setPageSelect(i);
            TouchpadActivity.this.touchpadTipsDescriptionAdapter.onPageSelected(i);
            UiUtil.awakeScrollbarWidthChildView(TouchpadActivity.this.viewPager.getChildAt(i));
        }
    };
    FrameLayout touchpadTipsDescFrameLayout;
    TouchpadTipsDescriptionAdapter touchpadTipsDescriptionAdapter;
    ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate((Bundle) null);
        this.context = this;
        setContentView((int) R.layout.activity_touchpad);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.mEarbudsInfo = Application.getCoreService().getEarBudsInfo();
        checkApp2App();
        initTipsCard();
        initLockTouchpad();
        initToucpadOption();
        updateTouchpadUI();
        registerReceiver();
    }

    private void initTipsCard() {
        this.viewPager = (ViewPager) findViewById(R.id.touchpad_viewpager);
        this.adapter = new TouchpadViewPagerAdapter(getSupportFragmentManager());
        this.viewPager.setOffscreenPageLimit(4);
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.addOnPageChangeListener(this.setViewPagerOnPageChangeListener);
        this.mPageIndicatorView = (PageIndicatorView) findViewById(R.id.page_indicator);
        this.mPageIndicatorView.setPageMax(this.adapter.getCount());
        this.touchpadTipsDescFrameLayout = (FrameLayout) findViewById(R.id.tips_desc_layout);
        this.touchpadTipsDescriptionAdapter = new TouchpadTipsDescriptionAdapter(this, this.touchpadTipsDescFrameLayout);
        this.viewPager.setCurrentItem(UiUtil.rtlCompatIndex(0, this.adapter.getCount()));
        String str = TAG;
        Log.d(str, "init():: touchpad Locked=" + this.mEarbudsInfo.touchpadLocked + ", touchpad left option=, " + this.mEarbudsInfo.touchpadOptionLeft + ", touchpad right option=" + this.mEarbudsInfo.touchpadOptionRight + ", volume control=" + this.mEarbudsInfo.outsideDoubleTap);
    }

    private void initLockTouchpad() {
        this.mLockTouchpadSwitch = (SwitchCompat) findViewById(R.id.switch_lock_touchpad);
        this.mLockTouchpadSwitch.setChecked(this.mEarbudsInfo.touchpadLocked);
        this.mLockTouchpadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!TouchpadActivity.this.isReceivedMesseage) {
                    TouchpadActivity.this.mEarbudsInfo.touchpadLocked = z;
                    Application.getCoreService().sendSppMessage(new MsgLockTouchpad(z));
                }
            }
        });
        ((LinearLayout) findViewById(R.id.layout_lock_touchpad_)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.LOCK_TOUCHPAD, "260", TouchpadActivity.this.mLockTouchpadSwitch.isChecked() ? "a" : "b");
                TouchpadActivity.this.mLockTouchpadSwitch.setChecked(!TouchpadActivity.this.mLockTouchpadSwitch.isChecked());
            }
        });
    }

    private void initToucpadOption() {
        this.mLeftOptionLayout = (RelativeLayout) findViewById(R.id.left_option_layout);
        this.mLeftOptionTxt = (TextView) findViewById(R.id.left_option_sub_txt);
        this.mLeftOptionTxt.setText(getDefaultLeftOptionValueText());
        this.mLeftOptionLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TouchpadActivity.TAG, "onClick()::left option");
                SamsungAnalyticsUtil.sendEvent(SA.Event.TAP_AND_HOLD_TOUCHPAD_LEFT, "260");
                TouchpadActivity.this.setPopupWindow(view, 0);
            }
        });
        this.mRightOptionLayout = (RelativeLayout) findViewById(R.id.right_option_layout);
        this.mRightOptionTxt = (TextView) findViewById(R.id.right_option_sub_txt);
        this.mRightOptionTxt.setText(getDefaultRightOptionValueText());
        this.mRightOptionLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TouchpadActivity.TAG, "onClick()::right option");
                SamsungAnalyticsUtil.sendEvent(SA.Event.TAP_AND_HOLD_TOUCHPAD_RIGHT, "260");
                TouchpadActivity.this.setPopupWindow(view, 1);
            }
        });
    }

    private String getDefaultLeftOptionValueText() {
        int i = this.mEarbudsInfo.touchpadOptionLeft;
        if (i == 1) {
            return this.context.getString(setVoiceRecognitionText());
        }
        if (i == 2) {
            return this.context.getString(R.string.settings_noise_reduction_title);
        }
        if (i == 3) {
            return this.context.getString(R.string.settings_touchpad_popup_txt3_left);
        }
        if (i != 4) {
            if (i != 5) {
                return setLeftDefaultOption();
            }
            return getOptionValueText(0);
        } else if (isReadySpotify()) {
            return this.context.getString(R.string.settings_touchpad_popup_txt4);
        } else {
            return setLeftDefaultOption();
        }
    }

    private String getDefaultRightOptionValueText() {
        int i = this.mEarbudsInfo.touchpadOptionRight;
        if (i == 1) {
            return this.context.getString(setVoiceRecognitionText());
        }
        if (i == 2) {
            return this.context.getString(R.string.settings_noise_reduction_title);
        }
        if (i == 3) {
            return this.context.getString(R.string.settings_touchpad_popup_txt3_right);
        }
        if (i != 4) {
            if (i != 6) {
                return setRightDefaultOption();
            }
            return getOptionValueText(1);
        } else if (isReadySpotify()) {
            return this.context.getString(R.string.settings_touchpad_popup_txt4);
        } else {
            return setRightDefaultOption();
        }
    }

    /* access modifiers changed from: private */
    public void setPopupWindow(View view, int i) {
        this.mClickedSide = i;
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.context.getString(R.string.settings_noise_reduction_title));
        arrayList.add(this.context.getString(setVoiceRecognitionText()));
        arrayList.add(this.context.getString(this.mClickedSide == 0 ? R.string.settings_touchpad_popup_txt3_left : R.string.settings_touchpad_popup_txt3_right));
        if (isReadySpotify()) {
            arrayList.add(this.context.getString(R.string.settings_touchpad_popup_txt4));
        }
        if (this.appListForA2A.size() > 0) {
            for (int i2 = 0; i2 < this.appListForA2A.size(); i2++) {
                String str = TAG;
                Log.i(str, "setPopupWindow()::" + ((String) this.appListForA2A.get(i2).get("menu_name")));
                String str2 = TAG;
                Log.i(str2, "setPopupWindow()::" + ((String) this.appListForA2A.get(i2).get(BaseContentProvider.PACKAGE_NAME)));
                arrayList.add((String) this.appListForA2A.get(i2).get("menu_name"));
            }
        }
        String[] strArr = (String[]) arrayList.toArray(new String[0]);
        int i3 = this.mClickedSide == 0 ? this.mEarbudsInfo.touchpadOptionLeft : this.mEarbudsInfo.touchpadOptionRight;
        if (i3 > 4) {
            i3 = getSelectedItem(this.mClickedSide);
        } else if (i3 == 1) {
            i3 = 2;
        } else if (i3 == 2) {
            i3 = 1;
        }
        this.mCurDropdownPopup = new DropdownListPopup((Context) this, view, strArr, Integer.valueOf(i3 - 1));
        this.mCurDropdownPopup.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                DropdownListPopup unused = TouchpadActivity.this.mCurDropdownPopup = null;
            }
        });
        this.mCurDropdownPopup.setOnItemClickListener(new DropdownListPopup.OnItemClickListener() {
            public void onItemClick(DropdownListPopup dropdownListPopup, View view, int i, long j) {
                if (TouchpadActivity.this.mClickedSide == 0) {
                    TouchpadActivity.this.onDefaultLeftOptionPopupClickListener(i + 1);
                } else {
                    TouchpadActivity.this.onDefaultRightOptionPopupClickListener(i + 1);
                }
            }
        });
        this.mCurDropdownPopup.show();
    }

    /* access modifiers changed from: private */
    public void onDefaultLeftOptionPopupClickListener(int i) {
        String str = TAG;
        Log.d(str, "onLeftOptionPopupClickListener():: " + i);
        if (i == 1) {
            if (isSetVolumeUpDown()) {
                isSetVolumeControl(0);
            }
            this.mEarbudsInfo.touchpadOptionLeft = 2;
            this.mLeftOptionTxt.setText(R.string.settings_noise_reduction_title);
        } else if (i == 2) {
            if (isSetVolumeUpDown()) {
                isSetVolumeControl(0);
            }
            this.mEarbudsInfo.touchpadOptionLeft = 1;
            this.mLeftOptionTxt.setText(setVoiceRecognitionText());
        } else if (i == 3) {
            if (!isSetVolumeUpDown()) {
                toastVolumeAutomatically(0);
            }
            EarBudsInfo earBudsInfo = this.mEarbudsInfo;
            earBudsInfo.touchpadOptionLeft = 3;
            earBudsInfo.touchpadOptionRight = 3;
            this.mLeftOptionTxt.setText(R.string.settings_touchpad_popup_txt3_left);
            this.mRightOptionTxt.setText(R.string.settings_touchpad_popup_txt3_right);
        } else if (i != 4) {
            onLeftOptionPopupClickListener(i);
        } else if (isReadySpotify()) {
            if (isSetVolumeUpDown()) {
                isSetVolumeControl(0);
            }
            this.mEarbudsInfo.touchpadOptionLeft = 4;
            this.mLeftOptionTxt.setText(R.string.settings_touchpad_popup_txt4);
        } else {
            onLeftOptionPopupClickListener(i);
        }
        Application.getCoreService().sendSppMessage(new MsgSetTouchpadOption((byte) this.mEarbudsInfo.touchpadOptionLeft, (byte) this.mEarbudsInfo.touchpadOptionRight));
        DropdownListPopup dropdownListPopup = this.mCurDropdownPopup;
        if (dropdownListPopup != null) {
            dropdownListPopup.dismiss();
            this.mCurDropdownPopup = null;
        }
    }

    private void onLeftOptionPopupClickListener(int i) {
        String str = TAG;
        Log.d(str, "onLeftOptionPopupClickListener():: " + i);
        if (isSetVolumeUpDown()) {
            isSetVolumeControl(0);
        }
        this.mEarbudsInfo.touchpadOptionLeft = 5;
        this.mLeftOptionTxt.setText(getOnClickOptionValueText(0, i));
    }

    /* access modifiers changed from: private */
    public void onDefaultRightOptionPopupClickListener(int i) {
        String str = TAG;
        Log.d(str, "onOptionPopupClickListener():: " + i);
        if (i == 1) {
            if (isSetVolumeUpDown()) {
                isSetVolumeControl(1);
            }
            this.mEarbudsInfo.touchpadOptionRight = 2;
            this.mRightOptionTxt.setText(R.string.settings_noise_reduction_title);
        } else if (i == 2) {
            if (isSetVolumeUpDown()) {
                isSetVolumeControl(1);
            }
            this.mEarbudsInfo.touchpadOptionRight = 1;
            this.mRightOptionTxt.setText(setVoiceRecognitionText());
        } else if (i == 3) {
            if (!isSetVolumeUpDown()) {
                toastVolumeAutomatically(1);
            }
            EarBudsInfo earBudsInfo = this.mEarbudsInfo;
            earBudsInfo.touchpadOptionLeft = 3;
            earBudsInfo.touchpadOptionRight = 3;
            this.mLeftOptionTxt.setText(R.string.settings_touchpad_popup_txt3_left);
            this.mRightOptionTxt.setText(R.string.settings_touchpad_popup_txt3_right);
        } else if (i != 4) {
            onRightOptionPopupClickListener(i);
        } else if (isReadySpotify()) {
            if (isSetVolumeUpDown()) {
                isSetVolumeControl(1);
            }
            this.mEarbudsInfo.touchpadOptionRight = 4;
            this.mRightOptionTxt.setText(R.string.settings_touchpad_popup_txt4);
        } else {
            onRightOptionPopupClickListener(i);
        }
        Application.getCoreService().sendSppMessage(new MsgSetTouchpadOption((byte) this.mEarbudsInfo.touchpadOptionLeft, (byte) this.mEarbudsInfo.touchpadOptionRight));
        DropdownListPopup dropdownListPopup = this.mCurDropdownPopup;
        if (dropdownListPopup != null) {
            dropdownListPopup.dismiss();
            this.mCurDropdownPopup = null;
        }
    }

    private void onRightOptionPopupClickListener(int i) {
        String str = TAG;
        Log.d(str, "onOptionPopupClickListener():: " + i);
        if (isSetVolumeUpDown()) {
            isSetVolumeControl(1);
        }
        this.mEarbudsInfo.touchpadOptionRight = 6;
        this.mRightOptionTxt.setText(getOnClickOptionValueText(1, i));
    }

    private void isSetVolumeControl(int i) {
        if (i == 0) {
            this.mEarbudsInfo.touchpadOptionRight = 2;
            this.mRightOptionTxt.setText(R.string.settings_noise_reduction_title);
            toastRightSideFromVolumetoDefault();
            return;
        }
        this.mEarbudsInfo.touchpadOptionLeft = 2;
        this.mLeftOptionTxt.setText(R.string.settings_noise_reduction_title);
        toastLeftSideFromVolumetoDefault();
    }

    private void saveOtherOptionPackageName(int i, String str) {
        Preferences.putString(i == 0 ? PreferenceKey.LEFT_OTHER_OPTION_PACKAGE_NAME : PreferenceKey.RIGHT_OTHER_OPTION_PACKAGE_NAME, str, UhmFwUtil.getLastLaunchDeviceId());
    }

    private void toastRightSideFromVolumetoDefault() {
        Context context2 = this.context;
        Toast.makeText(context2, context2.getString(R.string.settings_touchpad_option_toast_set_right_anc), 1).show();
    }

    private void toastLeftSideFromVolumetoDefault() {
        Context context2 = this.context;
        Toast.makeText(context2, context2.getString(R.string.settings_touchpad_option_toast_set_left_anc), 1).show();
    }

    private void toastVolumeAutomatically(int i) {
        if (i == 0) {
            Context context2 = this.context;
            Toast.makeText(context2, context2.getString(R.string.settings_touchpad_option_toast_set_right_volume_up), 1).show();
            return;
        }
        Context context3 = this.context;
        Toast.makeText(context3, context3.getString(R.string.settings_touchpad_option_toast_set_left_volume_down), 1).show();
    }

    private void updateTouchpadOptionText() {
        this.mLeftOptionTxt.setText(getDefaultLeftOptionValueText());
        this.mRightOptionTxt.setText(getDefaultRightOptionValueText());
    }

    private int setVoiceRecognitionText() {
        String str = TAG;
        Log.i(str, "setVoiceRecognitionText()::" + getPreferredAppInfo());
        return getPreferredAppInfo() == 1 ? R.string.settings_touchpad_popup_txt1_bixby : R.string.settings_touchpad_popup_txt1_normal;
    }

    private int getPreferredAppInfo() {
        Log.d(TAG, "getPreferredAppInfo()");
        ResolveInfo resolveActivity = this.context.getPackageManager().resolveActivity(new Intent("android.intent.action.VOICE_COMMAND"), 0);
        if (resolveActivity == null) {
            return 0;
        }
        String str = TAG;
        Log.d(str, "ResolveInfo : : info.activityInfo.packageName : " + resolveActivity.activityInfo.packageName);
        if (resolveActivity.activityInfo.packageName.equalsIgnoreCase("com.samsung.android.bixby.agent")) {
            return 1;
        }
        return 0;
    }

    private boolean isSetVolumeUpDown() {
        return this.mEarbudsInfo.touchpadOptionLeft == 3 && this.mEarbudsInfo.touchpadOptionRight == 3;
    }

    /* access modifiers changed from: private */
    public void updateTouchpadUI() {
        if (this.mEarbudsInfo.batteryL > 0) {
            UiUtil.setEnabledWithChildren(this.mLeftOptionLayout, true);
        } else {
            UiUtil.setEnabledWithChildren(this.mLeftOptionLayout, false);
        }
        if (this.mEarbudsInfo.batteryR > 0) {
            UiUtil.setEnabledWithChildren(this.mRightOptionLayout, true);
        } else {
            UiUtil.setEnabledWithChildren(this.mRightOptionLayout, false);
        }
    }

    /* access modifiers changed from: private */
    public void checkApp2App() {
        List<ResolveInfo> queryBroadcastReceivers = getPackageManager().queryBroadcastReceivers(new Intent(Util.SEND_PUI_EVENT), XDMWbxml.WBXML_EXT_0);
        this.appListForA2A = new ArrayList<>();
        String str = TAG;
        Log.d(str, "receivers.size: " + queryBroadcastReceivers.size());
        for (ResolveInfo resolveInfo : queryBroadcastReceivers) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            Bundle bundle = activityInfo.metaData;
            if (!(activityInfo == null || bundle == null)) {
                String str2 = activityInfo.packageName;
                String string = bundle.getString("menu_name");
                String string2 = bundle.getString("description");
                String string3 = bundle.getString("autho_key");
                if (!(string == null || string2 == null || !checkAuthorization(str2, string3))) {
                    String str3 = TAG;
                    Log.d(str3, "menuName : " + string);
                    String str4 = TAG;
                    Log.d(str4, "description :" + string2);
                    HashMap hashMap = new HashMap();
                    hashMap.put(BaseContentProvider.PACKAGE_NAME, str2);
                    hashMap.put("menu_name", string);
                    hashMap.put("description", string2);
                    this.appListForA2A.add(hashMap);
                }
            }
        }
    }

    private boolean checkAuthorization(String str, String str2) {
        boolean z;
        try {
            z = SecurityUtil.verify(str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            z = false;
        }
        String str3 = TAG;
        Log.d(str3, "packageName : " + str);
        String str4 = TAG;
        Log.d(str4, "decryptValue : " + z);
        return z;
    }

    private String setLeftDefaultOption() {
        this.mEarbudsInfo.touchpadOptionLeft = 2;
        Application.getCoreService().sendSppMessage(new MsgSetTouchpadOption((byte) this.mEarbudsInfo.touchpadOptionLeft, (byte) this.mEarbudsInfo.touchpadOptionRight));
        return this.context.getString(R.string.settings_noise_reduction_title);
    }

    private String setRightDefaultOption() {
        this.mEarbudsInfo.touchpadOptionRight = 2;
        Application.getCoreService().sendSppMessage(new MsgSetTouchpadOption((byte) this.mEarbudsInfo.touchpadOptionLeft, (byte) this.mEarbudsInfo.touchpadOptionRight));
        return this.context.getString(R.string.settings_noise_reduction_title);
    }

    private String getOnClickOptionValueText(int i, int i2) {
        if (this.appListForA2A.size() > 0) {
            int i3 = i2 - (isReadySpotify() ? 5 : 4);
            if (((String) this.appListForA2A.get(i3).get("menu_name")) != null) {
                saveOtherOptionPackageName(i, (String) this.appListForA2A.get(i3).get(BaseContentProvider.PACKAGE_NAME));
                return (String) this.appListForA2A.get(i3).get("menu_name");
            }
        }
        if (i == 0) {
            return setLeftDefaultOption();
        }
        return setRightDefaultOption();
    }

    private String getOptionValueText(int i) {
        String string = Preferences.getString(i == 0 ? PreferenceKey.LEFT_OTHER_OPTION_PACKAGE_NAME : PreferenceKey.RIGHT_OTHER_OPTION_PACKAGE_NAME, "", UhmFwUtil.getLastLaunchDeviceId());
        if (this.appListForA2A.size() > 0) {
            for (int i2 = 0; i2 < this.appListForA2A.size(); i2++) {
                if (string.equals((String) this.appListForA2A.get(i2).get(BaseContentProvider.PACKAGE_NAME)) && ((String) this.appListForA2A.get(i2).get("menu_name")) != null) {
                    return (String) this.appListForA2A.get(i2).get("menu_name");
                }
            }
        }
        if (i == 0) {
            return setLeftDefaultOption();
        }
        return setRightDefaultOption();
    }

    private int getSelectedItem(int i) {
        int i2 = isReadySpotify() ? 5 : 4;
        String string = Preferences.getString(i == 0 ? PreferenceKey.LEFT_OTHER_OPTION_PACKAGE_NAME : PreferenceKey.RIGHT_OTHER_OPTION_PACKAGE_NAME, "", UhmFwUtil.getLastLaunchDeviceId());
        if (this.appListForA2A.size() > 0) {
            for (int i3 = 0; i3 < this.appListForA2A.size(); i3++) {
                if (string.equals((String) this.appListForA2A.get(i3).get(BaseContentProvider.PACKAGE_NAME)) && ((String) this.appListForA2A.get(i3).get("menu_name")) != null) {
                    return i3 + i2;
                }
            }
        }
        if (i == 0) {
            this.mEarbudsInfo.touchpadOptionLeft = 2;
        } else {
            this.mEarbudsInfo.touchpadOptionRight = 2;
        }
        Application.getCoreService().sendSppMessage(new MsgSetTouchpadOption((byte) this.mEarbudsInfo.touchpadOptionLeft, (byte) this.mEarbudsInfo.touchpadOptionRight));
        return 2;
    }

    private void sendBroadcast(String str, String str2, String str3, String str4) {
        Intent intent = new Intent(str);
        intent.setPackage(str2);
        intent.putExtra("menu_name", str3);
        intent.putExtra("description", str4);
        sendBroadcast(intent);
    }

    private void registerAppToAppPackageReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        registerReceiver(this.mAppToAppPackageReceiver, intentFilter);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        updateTouchpadOptionText();
        updateTouchpadUI();
        this.mLockTouchpadSwitch.setChecked(this.mEarbudsInfo.touchpadLocked);
        super.onResume();
        updateVoiceAssistant();
        SamsungAnalyticsUtil.sendPage("260");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        unregisterReceiver(this.mAppToAppPackageReceiver);
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Log.d(TAG, "onConfigurationChanged()");
        DropdownListPopup dropdownListPopup = this.mCurDropdownPopup;
        if (dropdownListPopup != null) {
            dropdownListPopup.dismiss();
            this.mCurDropdownPopup = null;
        }
    }

    public boolean onSupportNavigateUp() {
        SamsungAnalyticsUtil.sendEvent(SA.Event.UP_BUTTON, "260");
        onBackPressed();
        return true;
    }

    private void updateVoiceAssistant() {
        if (Util.isTalkBackEnabled()) {
            this.mLockTouchpadSwitch.setFocusable(false);
            this.mLockTouchpadSwitch.setClickable(false);
            return;
        }
        this.mLockTouchpadSwitch.setFocusable(true);
        this.mLockTouchpadSwitch.setClickable(true);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CoreService.ACTION_MSG_ID_STATUS_UPDATED);
        intentFilter.addAction(CoreService.ACTION_MSG_ID_LOCK_TOUCHPAD_UPDATED);
        registerReceiver(this.mReceiver, intentFilter);
        registerAppToAppPackageReceiver();
    }

    public static boolean isReadySpotify() {
        boolean z = false;
        Long l = null;
        try {
            PackageInfo packageInfo = Application.getContext().getPackageManager().getPackageInfo(Util.SPOTIFY, 0);
            if (packageInfo != null) {
                l = Long.valueOf(PackageInfoCompat.getLongVersionCode(packageInfo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (l != null && l.longValue() >= 54789462) {
            z = true;
        }
        String str = TAG;
        Log.d(str, "isReadySpotify() : " + z + " (" + l + ")");
        return z;
    }
}
