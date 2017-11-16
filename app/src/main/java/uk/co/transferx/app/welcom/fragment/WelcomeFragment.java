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
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.welcom.presenter.WelcomeFragmentPresenter;

/**
 * Created by smilevkiy on 13.11.17.
 */

public class WelcomeFragment extends BaseFragment implements WelcomeFragmentPresenter.WelcomeUI {


    @Override
    public String tagName() {
        return WelcomeFragment.class.getSimpleName();
    }

    public WelcomeFragment() {
    }

    @Inject
    WelcomeFragmentPresenter presenter;

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
        view.findViewById(R.id.sign_up).setOnClickListener(v -> SignUpActivity.startSignUp(getActivity()));
        return view;
    }
}
