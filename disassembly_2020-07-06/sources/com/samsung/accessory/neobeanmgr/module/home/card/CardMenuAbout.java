package com.samsung.accessory.neobeanmgr.module.home.card;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.core.fota.util.FotaUtil;
import com.samsung.accessory.neobeanmgr.module.home.card.Card;

public class CardMenuAbout extends Card {
    private static final String TAG = "NeoBean_CardMenuAbout";
    private ItemViewHolder mItemViewHolder;
    private View.OnClickListener mOnClickAboutEarBuds;
    private View.OnClickListener mOnClickEarbudsSoftwareUpdate;
    private View.OnClickListener mOnClickTipsAndUserManual;

    public CardMenuAbout(View.OnClickListener onClickListener, View.OnClickListener onClickListener2, View.OnClickListener onClickListener3) {
        super(3);
        this.mOnClickEarbudsSoftwareUpdate = onClickListener;
        this.mOnClickTipsAndUserManual = onClickListener2;
        this.mOnClickAboutEarBuds = onClickListener3;
    }

    public void onBindItemViewHolder(Card.ItemViewHolder itemViewHolder) {
        this.mItemViewHolder = (ItemViewHolder) itemViewHolder;
        updateUI();
    }

    public void updateUI() {
        Log.d(TAG, "updateUI()");
        if (this.mItemViewHolder != null) {
            if (!Application.getCoreService().isConnected()) {
                UiUtil.setEnabledWithChildren(this.mItemViewHolder.earbudsSoftwareUpdate, false);
                UiUtil.setDimColorFilter(this.mItemViewHolder.imageSwUpdate);
            } else {
                UiUtil.setEnabledWithChildren(this.mItemViewHolder.earbudsSoftwareUpdate, true);
                this.mItemViewHolder.imageSwUpdate.clearColorFilter();
            }
            this.mItemViewHolder.earbudsSoftwareUpdate.setOnClickListener(this.mOnClickEarbudsSoftwareUpdate);
            this.mItemViewHolder.tipsAndUserManual.setOnClickListener(this.mOnClickTipsAndUserManual);
            this.mItemViewHolder.aboutEarBuds.setOnClickListener(this.mOnClickAboutEarBuds);
            if (FotaUtil.getCheckFotaUpdate()) {
                this.mItemViewHolder.textFotaBadge.setVisibility(0);
            } else {
                this.mItemViewHolder.textFotaBadge.setVisibility(4);
            }
        }
    }

    static Card.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_menu_about, viewGroup, false));
    }

    static class ItemViewHolder extends Card.ItemViewHolder {
        View aboutEarBuds;
        View earbudsSoftwareUpdate;
        ImageView imageSwUpdate;
        TextView textFotaBadge;
        View tipsAndUserManual;

        ItemViewHolder(View view) {
            super(view);
            this.imageSwUpdate = (ImageView) view.findViewById(R.id.menu_list_earbuds_software_update_icon);
            this.earbudsSoftwareUpdate = view.findViewById(R.id.layout_earbuds_software_update);
            this.tipsAndUserManual = view.findViewById(R.id.layout_tips_and_user_manual);
            this.aboutEarBuds = view.findViewById(R.id.layout_about_earbuds);
            this.textFotaBadge = (TextView) view.findViewById(R.id.text_badge_fota);
        }
    }
}
