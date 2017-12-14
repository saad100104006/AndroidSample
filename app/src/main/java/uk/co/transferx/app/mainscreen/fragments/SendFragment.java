package uk.co.transferx.app.mainscreen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;

/**
 * Created by sergey on 14.12.17.
 */

public class SendFragment extends BaseFragment {
    @Override
    public String tagName() {
        return SendFragment.class.getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.send_fragment_layout, container, false);
        return view;

    }
}
