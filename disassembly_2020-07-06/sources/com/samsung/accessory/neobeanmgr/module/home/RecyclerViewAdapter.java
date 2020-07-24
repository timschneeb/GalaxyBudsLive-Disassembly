package com.samsung.accessory.neobeanmgr.module.home;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.samsung.accessory.neobeanmgr.module.home.card.Card;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<Card.ItemViewHolder> {
    private static final String TAG = "NeoBean_HomeRecyclerViewAdapter";
    private ArrayList<Card> mItems;

    public RecyclerViewAdapter(ArrayList<Card> arrayList) {
        this.mItems = arrayList;
    }

    public Card.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return Card.onCreateViewHolder(viewGroup, i);
    }

    public void onBindViewHolder(Card.ItemViewHolder itemViewHolder, int i) {
        this.mItems.get(i).onBindItemViewHolder(itemViewHolder);
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    public int getItemViewType(int i) {
        return this.mItems.get(i).getType();
    }

    public void removeItem(int i) {
        if (this.mItems.get(i) != null) {
            this.mItems.remove(i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i, this.mItems.size());
        }
    }

    public void insertItem(int i, Card card) {
        if (!this.mItems.contains(card)) {
            this.mItems.add(i, card);
            notifyItemInserted(i);
        }
    }
}
