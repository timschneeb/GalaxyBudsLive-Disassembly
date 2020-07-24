package com.samsung.accessory.neobeanmgr.module.home.card;

import android.animation.LayoutTransition;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.ui.BatteryImageView;
import com.samsung.accessory.neobeanmgr.common.ui.OnSingleClickListener;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.module.home.card.Card;

public class CardEarbuds extends Card {
    private static final int COMMON_BATTERY_RANGE = 15;
    private static final int EARBUD_COLOR_BLACK = 260;
    private static final int EARBUD_COLOR_BLUE = 258;
    private static final int EARBUD_COLOR_DEEP_BLUE = 264;
    private static final int EARBUD_COLOR_NULL = -1;
    private static final int EARBUD_COLOR_PINK = 259;
    private static final int EARBUD_COLOR_RED = 263;
    private static final int EARBUD_COLOR_THOM_BROWNE = 262;
    private static final int EARBUD_COLOR_WHITE = 261;
    private static final int EARBUD_PLACEMENT_IN_OPEN_CASE = 3;
    private static final String TAG = "NeoBean_CardEarbuds";
    private EarBudsInfo info;
    /* access modifiers changed from: private */
    public Activity mActivity;
    private int mCurBetweenMarginDimenId = 0;
    private boolean mFirstUpdateUI = false;
    private ItemViewHolder mItemViewHolder;

    public CardEarbuds(Activity activity) {
        super(0);
        this.mActivity = activity;
    }

    public void onBindItemViewHolder(Card.ItemViewHolder itemViewHolder) {
        Log.d(TAG, "onBindItemViewHolder() : " + itemViewHolder);
        this.mItemViewHolder = (ItemViewHolder) itemViewHolder;
        this.mFirstUpdateUI = true;
        updateUI();
        this.mFirstUpdateUI = false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x03c8  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x03cc  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x03d9  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0411  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0420  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x044b  */
    public void updateUI() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        Log.d(TAG, "updateUI()");
        if (this.mItemViewHolder == null) {
            Log.e(TAG, "updateUI() : mItemViewHolder == null");
            return;
        }
        CoreService coreService = Application.getCoreService();
        this.info = coreService.getEarBudsInfo();
        Log.i(TAG, "[Earbuds Info]");
        Log.i(TAG, ":: Battery L= " + this.info.batteryL);
        Log.i(TAG, ":: Battery R= " + this.info.batteryR);
        Log.i(TAG, ":: Battery I= " + this.info.batteryI);
        Log.i(TAG, ":: Battery Cradle= " + this.info.batteryCase);
        Log.i(TAG, ":: isCoupled=" + this.info.coupled);
        Log.i(TAG, ":: placementL=" + this.info.placementL);
        Log.i(TAG, ":: placementR=" + this.info.placementR);
        Log.i(TAG, ":: device color=" + this.info.deviceColor);
        String str = "";
        boolean z = true;
        if (!coreService.isConnected()) {
            UiUtil.setEnabledWithChildren(this.mItemViewHolder.itemView, false);
            this.mItemViewHolder.frameImageGearLeft.setVisibility(0);
            this.mItemViewHolder.batteryLeft.setVisibility(4);
            this.mItemViewHolder.batteryLeft.setBatteryValue(-1);
            this.mItemViewHolder.textBatteryLeft.setVisibility(4);
            this.mItemViewHolder.textBatteryLeft.setText(str);
            UiUtil.setAnimateAlpha(this.mItemViewHolder.imageGearLeft, 0.4f, this.mFirstUpdateUI);
            setEarbudsBetweenMargin(R.dimen.card_earbuds_between_normal);
            this.mItemViewHolder.frameImageGearRight.setVisibility(0);
            this.mItemViewHolder.batteryRight.setVisibility(4);
            this.mItemViewHolder.batteryRight.setBatteryValue(-1);
            this.mItemViewHolder.textBatteryRight.setVisibility(4);
            this.mItemViewHolder.textBatteryRight.setText(str);
            UiUtil.setAnimateAlpha(this.mItemViewHolder.imageGearRight, 0.4f, this.mFirstUpdateUI);
            this.mItemViewHolder.batteryCommon.setVisibility(4);
            this.mItemViewHolder.textBatteryCommon.setVisibility(4);
            this.mItemViewHolder.frameImageCradle.setVisibility(0);
            this.mItemViewHolder.animateLayoutCradleBattery.setVisibility(8);
            this.mItemViewHolder.batteryCradle.setVisibility(8);
            this.mItemViewHolder.batteryCradle.setBatteryValue(-1);
            this.mItemViewHolder.textBatteryCradle.setVisibility(8);
            this.mItemViewHolder.textBatteryCradle.setText(str);
            UiUtil.setAnimateAlpha(this.mItemViewHolder.imageCradle, 0.4f, this.mFirstUpdateUI);
            this.mItemViewHolder.layoutTouchpad.setVisibility(8);
            this.mItemViewHolder.layoutDisconnect.setVisibility(0);
            this.mItemViewHolder.buttonConnect.setEnabled(true);
            this.mItemViewHolder.focusViewRemainingBattery.setVisibility(8);
            this.mItemViewHolder.focusViewRemainingBattery.setContentDescription(str);
            this.mItemViewHolder.focusViewCaseBattery.setVisibility(8);
            this.mItemViewHolder.focusViewCaseBattery.setContentDescription(str);
            this.mItemViewHolder.focusViewEarBudsStatus.setVisibility(0);
        } else {
            UiUtil.setEnabledWithChildren(this.mItemViewHolder.itemView, true);
            if (!coreService.isExtendedStatusReady() || !this.info.touchpadLocked) {
                this.mItemViewHolder.layoutTouchpad.setVisibility(8);
            } else {
                this.mItemViewHolder.layoutTouchpad.setVisibility(0);
            }
            this.mItemViewHolder.layoutDisconnect.setVisibility(8);
            this.mItemViewHolder.buttonConnect.setEnabled(false);
            Integer commonBatteryValue = getCommonBatteryValue(this.info);
            int i6 = coreService.isExtendedStatusReady() ? 0 : 4;
            if (!coreService.isExtendedStatusReady() || this.info.batteryL <= 0) {
                UiUtil.setAnimateAlpha(this.mItemViewHolder.imageGearLeft, 0.4f, this.mFirstUpdateUI);
                this.mItemViewHolder.textBatteryLeft.setText(str);
                this.mItemViewHolder.batteryLeft.setBatteryValue(0);
                i = 4;
            } else {
                UiUtil.setAnimateAlpha(this.mItemViewHolder.imageGearLeft, 1.0f, this.mFirstUpdateUI);
                TextView textView = this.mItemViewHolder.textBatteryLeft;
                textView.setText(this.info.batteryL + "%");
                if (commonBatteryValue == null) {
                    this.mItemViewHolder.batteryLeft.setBatteryValue(this.info.batteryL);
                }
                i = 0;
            }
            int i7 = coreService.isExtendedStatusReady() ? 0 : 4;
            if (!coreService.isExtendedStatusReady() || this.info.batteryR <= 0) {
                UiUtil.setAnimateAlpha(this.mItemViewHolder.imageGearRight, 0.4f, this.mFirstUpdateUI);
                this.mItemViewHolder.textBatteryRight.setText(str);
                this.mItemViewHolder.batteryRight.setBatteryValue(0);
                i2 = 4;
            } else {
                UiUtil.setAnimateAlpha(this.mItemViewHolder.imageGearRight, 1.0f, this.mFirstUpdateUI);
                TextView textView2 = this.mItemViewHolder.textBatteryRight;
                textView2.setText(this.info.batteryR + "%");
                if (commonBatteryValue == null) {
                    this.mItemViewHolder.batteryRight.setBatteryValue(this.info.batteryR);
                }
                i2 = 0;
            }
            if (!coreService.isExtendedStatusReady()) {
                this.mItemViewHolder.animateLayoutCradleBattery.setVisibility(8);
                this.mItemViewHolder.batteryCradle.setVisibility(8);
                this.mItemViewHolder.textBatteryCradle.setVisibility(8);
                UiUtil.setAnimateAlpha(this.mItemViewHolder.imageCradle, 0.4f, this.mFirstUpdateUI);
                this.mItemViewHolder.focusViewCaseBattery.setVisibility(8);
            } else if (this.info.placementL >= 3 || this.info.placementR >= 3) {
                this.mItemViewHolder.batteryCradle.setBatteryValue(this.info.batteryCase);
                TextView textView3 = this.mItemViewHolder.textBatteryCradle;
                textView3.setText(this.info.batteryCase + "%");
                this.mItemViewHolder.animateLayoutCradleBattery.setVisibility(0);
                this.mItemViewHolder.batteryCradle.setVisibility(0);
                this.mItemViewHolder.textBatteryCradle.setVisibility(0);
                UiUtil.setAnimateAlpha(this.mItemViewHolder.imageCradle, 1.0f, this.mFirstUpdateUI);
                String format = String.format(this.mItemViewHolder.itemView.getContext().getString(R.string.case_d_percent), new Object[]{Integer.valueOf(this.info.batteryCase)});
                this.mItemViewHolder.focusViewCaseBattery.setVisibility(0);
                this.mItemViewHolder.focusViewCaseBattery.setContentDescription(format);
            } else {
                this.mItemViewHolder.animateLayoutCradleBattery.setVisibility(8);
                this.mItemViewHolder.batteryCradle.setVisibility(8);
                this.mItemViewHolder.textBatteryCradle.setVisibility(8);
                this.mItemViewHolder.focusViewCaseBattery.setVisibility(8);
                i3 = 8;
                if (coreService.isExtendedStatusReady() || commonBatteryValue == null) {
                    i4 = 4;
                } else {
                    this.mItemViewHolder.batteryCommon.setBatteryValue(commonBatteryValue.intValue());
                    TextView textView4 = this.mItemViewHolder.textBatteryCommon;
                    textView4.setText(commonBatteryValue + "%");
                    i4 = 0;
                    i2 = 4;
                    i6 = 4;
                    i = 4;
                    i7 = 4;
                }
                i5 = i4 != 0 ? R.dimen.card_earbuds_between_common_battery : i3 != 0 ? R.dimen.card_earbuds_between_no_cradle : R.dimen.card_earbuds_between_normal;
                if (this.mCurBetweenMarginDimenId != i5) {
                    toggleViewMarginBetweenChildVisibility();
                }
                this.mItemViewHolder.frameImageCradle.setVisibility(i3);
                this.mItemViewHolder.batteryLeft.setVisibility(i6);
                this.mItemViewHolder.textBatteryLeft.setVisibility(i);
                this.mItemViewHolder.batteryRight.setVisibility(i7);
                this.mItemViewHolder.textBatteryRight.setVisibility(i2);
                this.mItemViewHolder.batteryCommon.setVisibility(i4);
                this.mItemViewHolder.textBatteryCommon.setVisibility(i4);
                if (this.mCurBetweenMarginDimenId != i5) {
                    setEarbudsBetweenMargin(i5);
                }
                if (this.info.batteryL > 0 && this.info.batteryR > 0) {
                    str = String.format(this.mItemViewHolder.itemView.getContext().getString(R.string.remaining_battery_both), new Object[]{Integer.valueOf(this.info.batteryL), Integer.valueOf(this.info.batteryR)});
                } else if (this.info.batteryL > 0) {
                    str = String.format(this.mItemViewHolder.itemView.getContext().getString(R.string.remaining_battery_left_only), new Object[]{Integer.valueOf(this.info.batteryL)});
                } else if (this.info.batteryR > 0) {
                    str = String.format(this.mItemViewHolder.itemView.getContext().getString(R.string.remaining_battery_right_only), new Object[]{Integer.valueOf(this.info.batteryR)});
                }
                this.mItemViewHolder.focusViewRemainingBattery.setVisibility(0);
                this.mItemViewHolder.focusViewRemainingBattery.setContentDescription(str);
                this.mItemViewHolder.focusViewEarBudsStatus.setVisibility(8);
            }
            i3 = 0;
            if (coreService.isExtendedStatusReady()) {
            }
            i4 = 4;
            if (i4 != 0) {
            }
            if (this.mCurBetweenMarginDimenId != i5) {
            }
            this.mItemViewHolder.frameImageCradle.setVisibility(i3);
            this.mItemViewHolder.batteryLeft.setVisibility(i6);
            this.mItemViewHolder.textBatteryLeft.setVisibility(i);
            this.mItemViewHolder.batteryRight.setVisibility(i7);
            this.mItemViewHolder.textBatteryRight.setVisibility(i2);
            this.mItemViewHolder.batteryCommon.setVisibility(i4);
            this.mItemViewHolder.textBatteryCommon.setVisibility(i4);
            if (this.mCurBetweenMarginDimenId != i5) {
            }
            if (this.info.batteryL > 0 || this.info.batteryR > 0) {
            }
            this.mItemViewHolder.focusViewRemainingBattery.setVisibility(0);
            this.mItemViewHolder.focusViewRemainingBattery.setContentDescription(str);
            this.mItemViewHolder.focusViewEarBudsStatus.setVisibility(8);
        }
        setDeviceImage();
        this.mItemViewHolder.buttonConnect.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                ((Card.CardOwnerActivity) CardEarbuds.this.mActivity).requestConnectToDevice();
            }
        });
        Button button = this.mItemViewHolder.buttonConnect;
        if (coreService.getConnectionState() == 1) {
            z = false;
        }
        button.setEnabled(z);
    }

    private void setEarbudsBetweenMargin(int i) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mItemViewHolder.viewMarginBetween.getLayoutParams();
        layoutParams.width = (int) this.mActivity.getResources().getDimension(i);
        this.mItemViewHolder.viewMarginBetween.setLayoutParams(layoutParams);
        this.mCurBetweenMarginDimenId = i;
    }

    private void toggleViewMarginBetweenChildVisibility() {
        this.mItemViewHolder.viewMarginBetweenChild.setVisibility(this.mItemViewHolder.viewMarginBetweenChild.getVisibility() == 0 ? 8 : 0);
    }

    private Integer getCommonBatteryValue(EarBudsInfo earBudsInfo) {
        if (earBudsInfo.batteryI == -1) {
            return null;
        }
        return Integer.valueOf(earBudsInfo.batteryI);
    }

    private void setDeviceImage() {
        int earBudsColorCode = getEarBudsColorCode();
        setDeviceImages(Integer.valueOf(earBudsColorCode), this.mItemViewHolder.imageGearLeft, this.mItemViewHolder.imageGearRight, this.mItemViewHolder.imageCradle);
        setActivityBgGradationColor(earBudsColorCode);
    }

    public static int getEarBudsColorCode() {
        CoreService coreService = Application.getCoreService();
        int i = Preferences.getInt(PreferenceKey.LAST_DEVICE_COLOR, -1);
        if (coreService.isConnected() && coreService.isExtendedStatusReady()) {
            i = coreService.getEarBudsInfo().deviceColor;
            Preferences.putInt(PreferenceKey.LAST_DEVICE_COLOR, Integer.valueOf(i));
        }
        Log.d(TAG, "getEarBudsColorCode() : " + i);
        return i;
    }

    public static void setDeviceImages(Integer num, ImageView imageView, ImageView imageView2, ImageView imageView3) {
        Log.d(TAG, "setDeviceImages() : " + num);
        if (num == null) {
            num = Integer.valueOf(getEarBudsColorCode());
        }
        Util.isJapanModel();
        num.intValue();
        if (imageView != null) {
            imageView.setImageResource(R.drawable.gw_buds_kv_left_black);
        }
        if (imageView2 != null) {
            imageView2.setImageResource(R.drawable.gw_buds_kv_right_black);
        }
        if (imageView3 != null) {
            imageView3.setImageResource(R.drawable.gw_buds_kv_credle_black);
        }
    }

    private void setActivityBgGradationColor(int i) {
        ((Card.CardOwnerActivity) this.mActivity).setBgGradationColor(Application.getContext().getResources().getColor(R.color.home_gradation_default));
    }

    static Card.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_earbuds, viewGroup, false));
    }

    static class ItemViewHolder extends Card.ItemViewHolder {
        View animateLayoutCradleBattery;
        BatteryImageView batteryCommon;
        BatteryImageView batteryCradle;
        BatteryImageView batteryLeft;
        BatteryImageView batteryRight;
        Button buttonConnect;
        View focusViewCaseBattery;
        View focusViewEarBudsStatus;
        View focusViewRemainingBattery;
        View frameImageCradle;
        View frameImageGearLeft;
        View frameImageGearRight;
        ImageView imageCradle;
        ImageView imageGearLeft;
        ImageView imageGearRight;
        LinearLayout layoutDisconnect;
        View layoutTouchpad;
        TextView textBatteryCommon;
        TextView textBatteryCradle;
        TextView textBatteryLeft;
        TextView textBatteryRight;
        TextView textDisconnect;
        View viewMarginBetween;
        View viewMarginBetweenChild;

        ItemViewHolder(View view) {
            super(view);
            this.textBatteryLeft = (TextView) view.findViewById(R.id.text_left_battery);
            this.textBatteryRight = (TextView) view.findViewById(R.id.text_right_battery);
            this.textBatteryCommon = (TextView) view.findViewById(R.id.text_common_battery);
            this.textBatteryCradle = (TextView) view.findViewById(R.id.text_cradle_battery);
            this.frameImageGearLeft = view.findViewById(R.id.frame_image_bud_left);
            this.imageGearLeft = (ImageView) view.findViewById(R.id.image_bud_left);
            this.viewMarginBetween = view.findViewById(R.id.view_margin_between);
            this.viewMarginBetweenChild = view.findViewById(R.id.view_margin_between_child);
            this.frameImageGearRight = view.findViewById(R.id.frame_image_bud_right);
            this.imageGearRight = (ImageView) view.findViewById(R.id.image_bud_right);
            this.frameImageCradle = view.findViewById(R.id.frame_image_cradle);
            this.imageCradle = (ImageView) view.findViewById(R.id.image_cradle);
            this.batteryLeft = (BatteryImageView) view.findViewById(R.id.image_left_battery);
            this.batteryRight = (BatteryImageView) view.findViewById(R.id.image_right_battery);
            this.batteryCommon = (BatteryImageView) view.findViewById(R.id.image_common_battery);
            this.batteryCradle = (BatteryImageView) view.findViewById(R.id.image_cradle_battery);
            this.textDisconnect = (TextView) view.findViewById(R.id.text_disconnect);
            this.buttonConnect = (Button) view.findViewById(R.id.button_connect);
            this.layoutDisconnect = (LinearLayout) view.findViewById(R.id.disconnect_layout);
            this.layoutTouchpad = view.findViewById(R.id.layout_touchpad);
            this.focusViewRemainingBattery = view.findViewById(R.id.focus_view_remaining_battery);
            this.focusViewCaseBattery = view.findViewById(R.id.focus_view_case_battery);
            this.focusViewEarBudsStatus = view.findViewById(R.id.focus_view_earbuds_status);
            this.animateLayoutCradleBattery = view.findViewById(R.id.animate_layout_cradle_battery);
            ((ViewGroup) view).setLayoutTransition(new BaseLayoutTransition());
            ((ViewGroup) view.findViewById(R.id.animate_layout_disconnect)).setLayoutTransition(new BaseLayoutTransition());
            ((ViewGroup) view.findViewById(R.id.animate_layout_touchpad)).setLayoutTransition(new BaseLayoutTransition());
            ((ViewGroup) view.findViewById(R.id.animate_layout_left_battery)).setLayoutTransition(new BaseLayoutTransition());
            ((ViewGroup) view.findViewById(R.id.animate_layout_right_battery)).setLayoutTransition(new BaseLayoutTransition());
            ((ViewGroup) view.findViewById(R.id.animate_layout_common_battery)).setLayoutTransition(new BaseLayoutTransition());
            ((ViewGroup) view.findViewById(R.id.animate_layout_cradle_battery)).setLayoutTransition(new BaseLayoutTransition());
        }
    }

    static Interpolator SineOut60Interpolator() {
        return PathInterpolatorCompat.create(0.17f, 0.17f, 0.4f, 1.0f);
    }

    static Interpolator SineInOut70Interpolator() {
        return PathInterpolatorCompat.create(0.33f, 0.0f, 0.3f, 1.0f);
    }

    static class DelayLinearInterpolator implements TimeInterpolator {
        private float mDelay;

        DelayLinearInterpolator(float f) {
            this.mDelay = f;
        }

        public float getInterpolation(float f) {
            float f2 = this.mDelay;
            float f3 = (f - f2) / (1.0f - f2);
            if (f3 > 0.0f) {
                return f3;
            }
            return 0.0f;
        }
    }

    private static class BaseLayoutTransition extends TjLayoutTransition {
        BaseLayoutTransition() {
            setDuration(0, 600);
            setInterpolator(0, CardEarbuds.SineInOut70Interpolator());
            setDuration(1, 600);
            setInterpolator(1, CardEarbuds.SineInOut70Interpolator());
            setDuration(2, 600);
            setInterpolator(2, new DelayLinearInterpolator(0.5f));
            setDuration(3, 300);
            setInterpolator(3, new LinearInterpolator());
        }
    }

    static class TjLayoutTransition extends LayoutTransition {
        private static final boolean ENABLE_ANIMATION_LOG = false;
        private static final String[] TRANSITION_TYPE_NAME = {"CHANGE_APPEARING", "CHANGE_DISAPPEARING", "APPEARING", "DISAPPEARING", "CHANGING"};

        TjLayoutTransition() {
            for (int i = 0; i <= 4; i++) {
                setAnimator(i, getAnimator(i).clone());
            }
        }
    }
}
