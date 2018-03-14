package uk.co.transferx.app.welcom.fragment;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.mainscreen.MainActivity;
import uk.co.transferx.app.recoverpass.RecoverPasswordActivity;
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

    private CoordinatorLayout coordinatorLayout;
    private TextInputEditText firstInput;
    private TextInputEditText secondInput;
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
        return inflater.inflate(R.layout.on_boarding_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firstInput = view.findViewById(R.id.first_input);
        secondInput = view.findViewById(R.id.second_input);
        firstInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstInput.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        secondInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                secondInput.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.findViewById(R.id.create_account).setOnClickListener(v -> presenter.signUpClicked());
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        view.findViewById(R.id.forgot_password).setOnClickListener(v -> presenter.recoverPasswordClicked());
        view.findViewById(R.id.next).setOnClickListener(v -> presenter.validateInput(firstInput.getText().toString(), secondInput.getText().toString()));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void goToSignUp() {
        SignUpActivity.startSignUp(getActivity());
    }

    @Override
    public void goRecoverPassword() {
        RecoverPasswordActivity.starRecoverPasswordActivity(getActivity());
    }

    @Override
    public void showWrongPassword() {
        Snackbar.make(coordinatorLayout, getString(R.string.wrong_username_or_password), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void goToMainScreen() {
        Activity activity = getActivity();
        if (activity != null) {
            MainActivity.startMainActivity(getActivity());
            activity.finish();
        }
    }

    @Override
    public void showUserNotFound() {
        Snackbar.make(coordinatorLayout, getString(R.string.user_not_found), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showConnectionError() {
        snackbar = Snackbar.make(coordinatorLayout, getString(R.string.connection_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.try_again), v -> {
                    presenter.refreshToken();
                    snackbar.dismiss();
                });
        snackbar.show();
    }

    @Override
    public void showEmailError() {
        firstInput.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        firstInput.setTextColor(getResources().getColor(R.color.red));
        Snackbar.make(coordinatorLayout, getString(R.string.email_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showPasswordError() {
        secondInput.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        secondInput.setTextColor(getResources().getColor(R.color.red));
        Snackbar.make(coordinatorLayout, getString(R.string.password_error), Snackbar.LENGTH_LONG).show();
    }
}
