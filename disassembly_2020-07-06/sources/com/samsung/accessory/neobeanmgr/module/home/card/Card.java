package com.samsung.accessory.neobeanmgr.module.home.card;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

public abstract class Card {
    public static final long MILLIS_A_MONTH = 2592000000L;
    public static final long MILLIS_A_WEEK = 604800000;
    static final int TYPE_ACTIVE_NOISE_CANCELING = 4;
    static final int TYPE_EARBUDS = 0;
    static final int TYPE_EQUALIZER = 1;
    static final int TYPE_MENU_ABOUT = 3;
    static final int TYPE_MENU_MAIN = 2;
    public static final int TYPE_NONE = 100;
    public static final int TYPE_TIP_CLEANING = 103;
    public static final int TYPE_TIP_FOTA = 101;
    public static final int TYPE_TIP_OOBE = 102;
    public static final int TYPE_TIP_SEAMLESS_CONNECTION = 105;
    private int mType;

    public interface CardOwnerActivity {
        void removeTipCard();

        void requestConnectToDevice();

        void setBgGradationColor(int i);
    }

    public abstract void onBindItemViewHolder(ItemViewHolder itemViewHolder);

    public abstract void updateUI();

    Card(int i) {
        this.mType = i;
    }

    public int getType() {
        return this.mType;
    }

    public static ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return CardEarbuds.onCreateViewHolder(viewGroup);
        }
        if (i == 1) {
            return CardEqualizer.onCreateViewHolder(viewGroup);
        }
        if (i == 2) {
            return CardMenuMain.onCreateViewHolder(viewGroup);
        }
        if (i == 3) {
            return CardMenuAbout.onCreateViewHolder(viewGroup);
        }
        if (i == 4) {
            return CardActiveNoiseCanceling.onCreateViewHolder(viewGroup);
        }
        if (i == 105) {
            return CardSeamlessConnection.onCreateViewHolder(viewGroup);
        }
        switch (i) {
            case 101:
                return CardFota.onCreateViewHolder(viewGroup);
            case 102:
                return CardOobeTips.onCreateViewHolder(viewGroup);
            case 103:
                return CardCleaning.onCreateViewHolder(viewGroup);
            default:
                return null;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(View view) {
            super(view);
        }
    }
}
