package uk.co.transferx.app.welcom.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.signin.SignInActivity;
import uk.co.transferx.app.signin.SignInType;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.welcom.presenter.WelcomeFragmentPresenter;

import static uk.co.transferx.app.splash.SplashActivity.INITIAL_TOKEN;

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

    @Inject
    SharedPreferences sharedPreferences;

    private CoordinatorLayout coordinatorLayout;

    private Snackbar snackbar;

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
        view.findViewById(R.id.sign_up).setOnClickListener(v -> presenter.signUpClicked());
        view.findViewById(R.id.sign_in).setOnClickListener(v -> presenter.signInClicked());
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        return view;
    }

    @Override
    public void checkToken() {
        presenter.setTokenStatus(sharedPreferences.getString(INITIAL_TOKEN, null) != null);
    }

    @Override
    public void goToSignUp() {
        SignUpActivity.startSignUp(getActivity());
    }

    @Override
    public void goToSignIn() {
        SignInActivity.starSignInActivity(getActivity(), SignInType.EMAIL);
    }


    @Override
    public void noTokenError() {
        snackbar = Snackbar.make(coordinatorLayout, "Something wrong with connections", Snackbar.LENGTH_INDEFINITE)
                .setAction("Try again", v -> {
                    presenter.refreshToken();
                    snackbar.dismiss();
                });
        snackbar.show();
    }

    @Override
    public void upDateToken(String token) {
        sharedPreferences.edit().putString(INITIAL_TOKEN, token).apply();
    }
}
