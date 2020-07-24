package com.samsung.accessory.neobeanmgr.module.setupwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.samsung.accessory.neobeanmgr.R;

public class HowToWearFragment extends Fragment {
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.layout_more_useful_features_01, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.make_sure_you_put));
        sb.append("\n\n");
        sb.append(getString(R.string.a_correct_fit_will_give_you));
        ((TextView) view.findViewById(R.id.text_description)).setText(sb);
    }
}
