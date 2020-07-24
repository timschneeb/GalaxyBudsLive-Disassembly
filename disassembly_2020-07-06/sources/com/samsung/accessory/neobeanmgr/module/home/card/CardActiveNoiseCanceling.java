package com.samsung.accessory.neobeanmgr.module.home.card;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.RecycleAnimationSwitchCompat;
import com.samsung.accessory.neobeanmgr.common.ui.SingleToast;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetNoiseReduction;
import com.samsung.accessory.neobeanmgr.module.home.card.Card;

public class CardActiveNoiseCanceling extends Card {
    private static final String TAG = "NeoBean_CardANC";
    /* access modifiers changed from: private */
    public ItemViewHolder mItemViewHolder;
    /* access modifiers changed from: private */
    public CoreService service;

    public CardActiveNoiseCanceling() {
        super(4);
    }

    public void onBindItemViewHolder(Card.ItemViewHolder itemViewHolder) {
        this.mItemViewHolder = (ItemViewHolder) itemViewHolder;
        updateUI();
    }

    public void updateUI() {
        Log.d(TAG, "updateUI()");
        if (this.mItemViewHolder != null) {
            this.service = Application.getCoreService();
            UiUtil.setEnabledWithChildren(this.mItemViewHolder.itemView, this.service.isConnected());
            unregisterListener();
            initView();
            if (this.service.isConnected()) {
                registerListener();
                setConnectedCard();
            } else {
                setDisconnectedCard();
            }
            updateVoiceAssistant();
        }
    }

    static Card.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_active_noise_canceling, viewGroup, false));
    }

    static class ItemViewHolder extends Card.ItemViewHolder {
        ConstraintLayout ancLayout;
        ImageView imageView;
        RecycleAnimationSwitchCompat switchCompat;
        TextView textDescription;

        ItemViewHolder(View view) {
            super(view);
            this.textDescription = (TextView) view.findViewById(R.id.text_active_noise_canceling_description);
            this.switchCompat = (RecycleAnimationSwitchCompat) view.findViewById(R.id.switch_active_noise_canceling);
            this.ancLayout = (ConstraintLayout) view.findViewById(R.id.layout_active_noise_canceling);
            this.imageView = (ImageView) view.findViewById(R.id.anc_icon);
        }
    }

    private void initView() {
        this.mItemViewHolder.switchCompat.setChecked(this.service.getEarBudsInfo().noiseReduction);
    }

    private void registerListener() {
        this.mItemViewHolder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SamsungAnalyticsUtil.sendEvent(SA.Event.ANC_SWITCH, SA.Screen.HOME);
                CardActiveNoiseCanceling.this.service.getEarBudsInfo().noiseReduction = z;
                CardActiveNoiseCanceling.this.service.sendSppMessage(new MsgSetNoiseReduction(z));
                CardActiveNoiseCanceling.this.setAncView();
                if (!z) {
                    return;
                }
                if (!CardActiveNoiseCanceling.this.service.getEarBudsInfo().wearingL || !CardActiveNoiseCanceling.this.service.getEarBudsInfo().wearingR) {
                    CardActiveNoiseCanceling.this.showBothWearingToast();
                }
            }
        });
        this.mItemViewHolder.ancLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CardActiveNoiseCanceling.this.mItemViewHolder.switchCompat.performClick();
            }
        });
    }

    private void unregisterListener() {
        this.mItemViewHolder.switchCompat.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        this.mItemViewHolder.ancLayout.setOnClickListener((View.OnClickListener) null);
    }

    private void setConnectedCard() {
        setAncView();
    }

    private void setDisconnectedCard() {
        this.mItemViewHolder.textDescription.setVisibility(8);
        UiUtil.setDimColorFilter(this.mItemViewHolder.imageView);
    }

    private void updateVoiceAssistant() {
        if (Util.isTalkBackEnabled()) {
            this.mItemViewHolder.switchCompat.setFocusable(false);
            this.mItemViewHolder.switchCompat.setClickable(false);
            if (this.service.isConnected()) {
                setConnectedVoiceAssistant();
            } else {
                setDisconnectedVoiceAssistant();
            }
        } else {
            this.mItemViewHolder.switchCompat.setFocusable(true);
            this.mItemViewHolder.switchCompat.setClickable(true);
        }
    }

    /* access modifiers changed from: private */
    public void setAncView() {
        Log.d(TAG, "setAncView() : ");
        UiUtil.setEnabledWithChildren(this.mItemViewHolder.itemView, true);
        this.mItemViewHolder.textDescription.setVisibility(8);
        this.mItemViewHolder.imageView.clearColorFilter();
    }

    /* access modifiers changed from: private */
    public void showBothWearingToast() {
        SingleToast.show(Application.getContext(), Application.getContext().getString(R.string.settings_anc_toast_both_wearing), 1);
    }

    private void setConnectedVoiceAssistant() {
        this.mItemViewHolder.itemView.findViewById(R.id.layout_active_noise_canceling).setImportantForAccessibility(1);
        this.mItemViewHolder.itemView.setImportantForAccessibility(2);
    }

    private void setDisconnectedVoiceAssistant() {
        this.mItemViewHolder.itemView.findViewById(R.id.layout_active_noise_canceling).setImportantForAccessibility(4);
        this.mItemViewHolder.itemView.setImportantForAccessibility(1);
    }
}
