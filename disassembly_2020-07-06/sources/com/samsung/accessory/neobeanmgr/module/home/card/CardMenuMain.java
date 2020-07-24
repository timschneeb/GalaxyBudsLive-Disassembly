package com.samsung.accessory.neobeanmgr.module.home.card;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.core.gamemode.GameModeManager;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.module.home.card.Card;

public class CardMenuMain extends Card {
    private static final String TAG = "NeoBean_CardMenuMain";
    private ItemViewHolder mItemViewHolder;
    private View.OnClickListener mOnClickAdvanced;
    private View.OnClickListener mOnClickFindMyGear;
    private View.OnClickListener mOnClickGeneral;
    private View.OnClickListener mOnClickLabs;
    private View.OnClickListener mOnClickNotification;
    private View.OnClickListener mOnClickTouchpad;

    public CardMenuMain(View.OnClickListener onClickListener, View.OnClickListener onClickListener2, View.OnClickListener onClickListener3, View.OnClickListener onClickListener4, View.OnClickListener onClickListener5, View.OnClickListener onClickListener6) {
        super(2);
        this.mOnClickNotification = onClickListener2;
        this.mOnClickTouchpad = onClickListener;
        this.mOnClickAdvanced = onClickListener3;
        this.mOnClickLabs = onClickListener4;
        this.mOnClickFindMyGear = onClickListener5;
        this.mOnClickGeneral = onClickListener6;
    }

    public void onBindItemViewHolder(Card.ItemViewHolder itemViewHolder) {
        this.mItemViewHolder = (ItemViewHolder) itemViewHolder;
        updateUI();
    }

    public void updateUI() {
        String str;
        Log.d(TAG, "updateUI()");
        if (this.mItemViewHolder != null) {
            hideAdvancedMenu();
            hideLabsMenu();
            ItemViewHolder itemViewHolder = this.mItemViewHolder;
            itemViewHolder.notification.setOnClickListener(this.mOnClickNotification);
            itemViewHolder.touchpad.setOnClickListener(this.mOnClickTouchpad);
            itemViewHolder.advanced.setOnClickListener(this.mOnClickAdvanced);
            itemViewHolder.labs.setOnClickListener(this.mOnClickLabs);
            itemViewHolder.findMyGear.setOnClickListener(this.mOnClickFindMyGear);
            itemViewHolder.general.setOnClickListener(this.mOnClickGeneral);
            Context context = itemViewHolder.itemView.getContext();
            itemViewHolder.textTouchpadDesc.setText(context.getString(R.string.settings_touchpad_menu1) + ", " + context.getString(R.string.settings_touchpad_option_menu));
            String str2 = "";
            if (Application.getAomManager().checkEnabledBixby()) {
                str = context.getString(R.string.settings_voice_wakeup_title);
                if (Application.getCoreService().getEarBudsInfo().extendedRevision >= 3) {
                    str = str + ", " + context.getString(R.string.settings_seamless_connection);
                }
            } else {
                str = Application.getCoreService().getEarBudsInfo().extendedRevision >= 3 ? context.getString(R.string.settings_seamless_connection) : str2;
            }
            if (GameModeManager.isSupportDevice()) {
                str2 = context.getString(R.string.settings_game_mode_title);
                if (Application.getCoreService().getEarBudsInfo().extendedRevision >= 5) {
                    str2 = str2 + ", " + context.getString(R.string.relieve_pressure_ambient_sound_title);
                }
            } else if (Application.getCoreService().getEarBudsInfo().extendedRevision >= 5) {
                str2 = context.getString(R.string.relieve_pressure_ambient_sound_title);
            }
            itemViewHolder.textAdvancedDescription.setText(str);
            itemViewHolder.textLabsDesc.setText(str2);
            CoreService coreService = Application.getCoreService();
            if (coreService == null || !coreService.isConnected()) {
                UiUtil.setEnabledWithChildren(this.mItemViewHolder.itemView, false);
                UiUtil.setDimColorFilter(itemViewHolder.imageNotiIcon);
                UiUtil.setDimColorFilter(itemViewHolder.imageTouchpadIcon);
                UiUtil.setDimColorFilter(itemViewHolder.imageAdvancedIcon);
                UiUtil.setDimColorFilter(itemViewHolder.imageLabsIcon);
                UiUtil.setDimColorFilter(itemViewHolder.imageFindMyEarbudIcon);
                UiUtil.setDimColorFilter(itemViewHolder.imageGeneralIcon);
                return;
            }
            UiUtil.setEnabledWithChildren(this.mItemViewHolder.itemView, true);
            itemViewHolder.imageNotiIcon.clearColorFilter();
            itemViewHolder.imageTouchpadIcon.clearColorFilter();
            itemViewHolder.imageAdvancedIcon.clearColorFilter();
            itemViewHolder.imageLabsIcon.clearColorFilter();
            itemViewHolder.imageFindMyEarbudIcon.clearColorFilter();
            itemViewHolder.imageGeneralIcon.clearColorFilter();
        }
    }

    static Card.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_menu_main, viewGroup, false));
    }

    static class ItemViewHolder extends Card.ItemViewHolder {
        View advanced;
        View dividerAdvanced;
        View dividerLabs;
        View findMyGear;
        View general;
        ImageView imageAdvancedIcon;
        ImageView imageFindMyEarbudIcon;
        ImageView imageGeneralIcon;
        ImageView imageLabsIcon;
        ImageView imageNotiIcon;
        ImageView imageTouchpadIcon;
        View labs;
        View notification;
        TextView textAdvancedDescription;
        TextView textLabsDesc;
        TextView textTouchpadDesc;
        View touchpad;

        ItemViewHolder(View view) {
            super(view);
            this.notification = view.findViewById(R.id.layout_notification);
            this.touchpad = view.findViewById(R.id.layout_touchpad);
            this.advanced = view.findViewById(R.id.layout_advanced);
            this.labs = view.findViewById(R.id.layout_labs);
            this.findMyGear = view.findViewById(R.id.layout_find_my_gear);
            this.general = view.findViewById(R.id.layout_general);
            this.dividerAdvanced = view.findViewById(R.id.divider_advanced);
            this.dividerLabs = view.findViewById(R.id.divider_labs);
            this.imageNotiIcon = (ImageView) view.findViewById(R.id.menu_list_notification_icon);
            this.imageTouchpadIcon = (ImageView) view.findViewById(R.id.menu_list_touchpad_icon);
            this.imageAdvancedIcon = (ImageView) view.findViewById(R.id.menu_list_advanced_icon);
            this.imageLabsIcon = (ImageView) view.findViewById(R.id.menu_list_labs_icon);
            this.imageFindMyEarbudIcon = (ImageView) view.findViewById(R.id.menu_list_find_my_gear_icon);
            this.imageGeneralIcon = (ImageView) view.findViewById(R.id.menu_list_general_icon);
            this.textTouchpadDesc = (TextView) view.findViewById(R.id.menu_list_touchpad_sub_txt);
            this.textAdvancedDescription = (TextView) view.findViewById(R.id.menu_list_advanced_sub_txt);
            this.textLabsDesc = (TextView) view.findViewById(R.id.menu_list_labs_sub_txt);
        }
    }

    private void hideAdvancedMenu() {
        if (Application.getAomManager().checkEnabledBixby() || Application.getCoreService().getEarBudsInfo().extendedRevision >= 3) {
            this.mItemViewHolder.advanced.setVisibility(0);
            this.mItemViewHolder.dividerAdvanced.setVisibility(0);
            return;
        }
        this.mItemViewHolder.advanced.setVisibility(8);
        this.mItemViewHolder.dividerAdvanced.setVisibility(8);
    }

    private void hideLabsMenu() {
        if (GameModeManager.isSupportDevice() || Application.getCoreService().getEarBudsInfo().extendedRevision >= 5) {
            this.mItemViewHolder.labs.setVisibility(0);
            this.mItemViewHolder.dividerLabs.setVisibility(0);
            return;
        }
        this.mItemViewHolder.labs.setVisibility(8);
        this.mItemViewHolder.dividerLabs.setVisibility(8);
    }
}
