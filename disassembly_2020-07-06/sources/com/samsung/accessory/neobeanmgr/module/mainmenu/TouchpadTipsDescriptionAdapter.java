package com.samsung.accessory.neobeanmgr.module.mainmenu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import java.util.ArrayList;

public class TouchpadTipsDescriptionAdapter {
    private static final String TAG = (Application.TAG_ + TouchpadTipsDescriptionAdapter.class.getSimpleName());
    private Context mContext;
    private ViewGroup mParentView;
    private View mTipsDescViewDoubleTap;
    private View mTipsDescViewTap;
    private View mTipsDescViewTouchAndHold;
    private View mTipsDescViewTripleTap;
    private ArrayList<View> mTipsDescriptionViewList = new ArrayList<>();

    public TouchpadTipsDescriptionAdapter(Context context, ViewGroup viewGroup) {
        this.mParentView = viewGroup;
        this.mContext = context;
        init();
        this.mParentView.addView(this.mTipsDescriptionViewList.get(0));
    }

    private void init() {
        this.mTipsDescViewTap = LayoutInflater.from(this.mContext).inflate(R.layout.touchpad_tips_desc_0, (ViewGroup) null);
        this.mTipsDescViewDoubleTap = LayoutInflater.from(this.mContext).inflate(R.layout.touchpad_tips_desc_1, (ViewGroup) null);
        this.mTipsDescViewTripleTap = LayoutInflater.from(this.mContext).inflate(R.layout.touchpad_tips_desc_2, (ViewGroup) null);
        this.mTipsDescViewTouchAndHold = LayoutInflater.from(this.mContext).inflate(R.layout.touchpad_tips_desc_3, (ViewGroup) null);
        initTipsDescriptionViewList();
    }

    private void initTipsDescriptionViewList() {
        this.mTipsDescriptionViewList.add(this.mTipsDescViewTap);
        this.mTipsDescriptionViewList.add(this.mTipsDescViewDoubleTap);
        this.mTipsDescriptionViewList.add(this.mTipsDescViewTripleTap);
        this.mTipsDescriptionViewList.add(this.mTipsDescViewTouchAndHold);
    }

    public void onPageSelected(int i) {
        this.mParentView.removeAllViews();
        try {
            this.mParentView.addView(this.mTipsDescriptionViewList.get(i));
            UiUtil.awakeScrollbarWidthChildView(this.mParentView);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
