package uk.co.transferx.app.welcom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.welcom.presenter.WelcomePresenter;

/**
 * Created by smilevkiy on 13.11.17.
 */

public class WelcomeFragment extends BaseFragment implements WelcomePresenter.WelcomeUI {


    public final static String TAG = WelcomeFragment.class.getSimpleName();

    public WelcomeFragment() {
    }

    @Inject
    WelcomePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TransferXApplication) (getActivity().getApplication())).getAppComponent().inject(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        presenter.detachUI();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.on_boarding_fragment_layout, container, false);
        return view;
    }
}
