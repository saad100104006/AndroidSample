package uk.co.transferx.app.welcom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private TextView emailLabel, passwordLabel, emailError, passwordError;
    private final TextWatcher firstTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //   setStatusOfError(firstInput, emailLabel, R.color.black);
            //   emailError.setVisibility(View.GONE);
            presenter.validateEmail(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher secondTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //   setStatusOfError(secondInput, passwordLabel, R.color.black);
            //   passwordError.setVisibility(View.GONE);
            presenter.validatePassword(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TransferXApplication) (getActivity().getApplication())).getAppComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        firstInput.addTextChangedListener(firstTextWatcher);
        secondInput.addTextChangedListener(secondTextWatcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        firstInput.removeTextChangedListener(firstTextWatcher);
        secondInput.removeTextChangedListener(secondTextWatcher);
        super.onPause();

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
        emailLabel = view.findViewById(R.id.email_label);
        passwordLabel = view.findViewById(R.id.password_label);
        emailError = view.findViewById(R.id.email_error);
        passwordError = view.findViewById(R.id.password_error);
        view.findViewById(R.id.create_account).setOnClickListener(v -> presenter.signUpClicked());
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        view.findViewById(R.id.forgot_password).setOnClickListener(v -> presenter.recoverPasswordClicked());
        buttonNext = view.findViewById(R.id.sign_in);
        buttonNext.setOnClickListener(v -> presenter.signInClicked());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void goToSignUp() {
        SignUpActivity.startSignUp(getActivity());
    }

    @Override
    public void goToPinView() {
        SignUpActivity.startSignUp(getActivity(), 2);
    }

    @Override
    public void goRecoverPassword() {
        startActivity(new Intent(getContext(), RecoverPasswordActivity.class));
    }

    @Override
    public void showWrongPassword() {
        Snackbar.make(coordinatorLayout, getColoredString(getString(R.string.wrong_username_or_password)), Snackbar.LENGTH_LONG).show();
    }

    private SpannableStringBuilder getColoredString(String message) {
        SpannableStringBuilder ssb = new SpannableStringBuilder()
                .append(message);
        ssb.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.red)),
                0,
                message.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @Override
    public void goToMainScreen() {
        Activity activity = getActivity();
        if (activity != null) {
            MainActivity.startMainActivity(activity);
            activity.finish();
        }
    }

    @Override
    public void showUserNotFound() {
        Snackbar.make(coordinatorLayout, getColoredString(getString(R.string.user_not_found)), Snackbar.LENGTH_LONG).show();
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
        setStatusOfError(firstInput, emailLabel, R.color.red);
        emailError.setText(R.string.email_error);
        emailError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPasswordError() {
        setStatusOfError(secondInput, passwordLabel, R.color.red);
        passwordError.setText(R.string.password_error);
        passwordError.setVisibility(View.VISIBLE);
    }

    @Override
    public void changeButtonState(boolean isEnabled) {
        setButtonStatus(isEnabled);
    }
}
