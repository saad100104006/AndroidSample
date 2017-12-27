package uk.co.transferx.app.settings.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;

/**
 * Created by sergey on 22/12/2017.
 */

public class SettingsFragment extends BaseFragment {


    private View view;

    @Override
    public String tagName() {
        return SettingsFragment.class.getSimpleName();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.settings_fragment_layout, container, false);


        }
        return view;

    }




}
