package uk.co.transferx.app.mainscreen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;

/**
 * Created by sergey on 17.12.17.
 */

public class ActivityFragment extends BaseFragment {

    private View view;

    @Override
    public String tagName() {
        return ActivityFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_fragment_layout, container, false);
        }
        return view;

    }
}
