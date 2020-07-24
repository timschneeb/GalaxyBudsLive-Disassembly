package com.samsung.accessory.neobeanmgr.module.tipsmanual;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.LayoutResourceFragment;
import com.samsung.accessory.neobeanmgr.common.ui.UiUtil;
import com.samsung.accessory.neobeanmgr.common.ui.VIView;
import com.samsung.accessory.neobeanmgr.module.tipsmanual.TipsAndUserManualActivity;

public class TipsPairingFragment extends LayoutResourceFragment implements TipsAndUserManualActivity.OnSelectedTipsFragment {
    private VIView mVIView;
    private final int quantity = 3;

    public TipsPairingFragment(int i) {
        super(i);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((TextView) view.findViewById(R.id.tips_pair_with_a_phone_or_tablet_desc)).setText(getResources().getQuantityString(R.plurals.tips_pair_with_a_phone_or_tablet_desc, 3, new Object[]{3}));
        this.mVIView = (VIView) view.findViewById(R.id.vi_view);
        this.mVIView.stop();
        if (getView() != null) {
            getView().setContentDescription(UiUtil.getAllTextWithChildView(getView()));
        }
    }

    public void onSelected(boolean z) {
        VIView vIView = this.mVIView;
        if (vIView != null) {
            if (z) {
                vIView.start();
            } else {
                vIView.reset();
            }
        }
    }
}
