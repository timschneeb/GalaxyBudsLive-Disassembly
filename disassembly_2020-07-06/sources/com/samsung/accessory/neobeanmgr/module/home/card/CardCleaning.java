package com.samsung.accessory.neobeanmgr.module.home.card;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.module.home.card.Card;

public class CardCleaning extends Card implements View.OnClickListener {
    public static final int MAX_SHOW_COUNT = 4;
    private static final String TAG = "NeoBean_CardCleaning";
    private Activity mActivity;
    private ItemViewHolder mItemViewHolder;

    public CardCleaning(Activity activity) {
        super(103);
        this.mActivity = activity;
    }

    public void onBindItemViewHolder(Card.ItemViewHolder itemViewHolder) {
        this.mItemViewHolder = (ItemViewHolder) itemViewHolder;
        updateUI();
    }

    public void updateUI() {
        ItemViewHolder itemViewHolder = this.mItemViewHolder;
        if (itemViewHolder != null) {
            itemViewHolder.textDescription.setText(R.string.clean_ear_wax_card_desc);
            this.mItemViewHolder.layoutInlineCue.setContentDescription(this.mItemViewHolder.textDescription.getText());
            this.mItemViewHolder.imageCancel.setOnClickListener(this);
            this.mItemViewHolder.textAction.setVisibility(8);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.image_cancel) {
            Log.d(TAG, "Click Cleaning Tip card cancel image.");
            SamsungAnalyticsUtil.sendEvent(SA.Event.TIPS_CLOSE, SA.Screen.HOME);
            int i = Preferences.getInt(PreferenceKey.CLEANING_CARD_SHOW_COUNT, 0) + 1;
            Preferences.putInt(PreferenceKey.CLEANING_CARD_SHOW_COUNT, Integer.valueOf(i));
            long currentTimeMillis = System.currentTimeMillis() + Card.MILLIS_A_MONTH;
            Preferences.putLong(PreferenceKey.CLEANING_CARD_SHOW_TIME, Long.valueOf(currentTimeMillis));
            Log.d(TAG, "CardCleaning : showCount=" + i + ", nextShowTime=" + UiUtil.MillisToString(currentTimeMillis));
            ((Card.CardOwnerActivity) this.mActivity).removeTipCard();
        }
    }

    static Card.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_inline_cue, viewGroup, false));
    }

    static class ItemViewHolder extends Card.ItemViewHolder {
        ImageView imageCancel;
        View layoutInlineCue;
        TextView textAction;
        TextView textDescription;

        ItemViewHolder(View view) {
            super(view);
            this.imageCancel = (ImageView) view.findViewById(R.id.image_cancel);
            this.textDescription = (TextView) view.findViewById(R.id.text_description);
            this.textAction = (TextView) view.findViewById(R.id.text_action);
            this.layoutInlineCue = view.findViewById(R.id.layout_inline_cue);
        }
    }
}
