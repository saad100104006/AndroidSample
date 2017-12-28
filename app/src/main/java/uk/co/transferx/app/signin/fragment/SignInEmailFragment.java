package uk.co.transferx.app.signin.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.mainscreen.MainActivity;
import uk.co.transferx.app.recoverpass.RecoverPasswordActivity;
import uk.co.transferx.app.signin.presenters.SignInEmailPresenter;

import static uk.co.transferx.app.splash.SplashActivity.INITIAL_TOKEN;

/**
 * Created by sergey on 19.11.17.
 */

public class SignInEmailFragment extends BaseFragment implements SignInEmailPresenter.SignInEmailUI {

    @Inject
    SignInEmailPresenter presenter;

    @Inject
    SharedPreferences sharedPreferences;

    private TextInputLayout firstInputLayout;
    private TextInputLayout secondInputLayout;


    @Override
    public String tagName() {
        return SignInEmailFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_step_fragment_layout, container, false);
        firstInputLayout = view.findViewById(R.id.first_layout);
        secondInputLayout = view.findViewById(R.id.second_layout);
        final EditText email = firstInputLayout.getEditText();
        final EditText password = secondInputLayout.getEditText();
        ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.one_step_away));
        firstInputLayout.setHint(getString(R.string.email));
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        secondInputLayout.setHint(getString(R.string.password));
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        firstInputLayout.setErrorEnabled(true);
        secondInputLayout.setErrorEnabled(true);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                firstInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                secondInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button buttonSignIn = view.findViewById(R.id.next_step);
        buttonSignIn.setText(R.string.sign_in);
        TextView forgotPass = view.findViewById(R.id.terms);
        String forgotPassword = getString(R.string.forgot_password) + "?";
        forgotPass.setText(forgotPassword.toUpperCase());
        forgotPass.setOnClickListener(v -> RecoverPasswordActivity.starRecoverPasswordActivity(getActivity()));
        buttonSignIn.setOnClickListener(v -> {
            String token = sharedPreferences.getString(INITIAL_TOKEN, null);
            if (token != null) {
                presenter.validateInput(email.getText().toString(), password.getText().toString(), token);
                return;
            }
            showError();
        });
        return view;
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
    public void showError() {
        Toast.makeText(getContext(), "Something went wrong try again later", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidationErrorEmail() {
        firstInputLayout.setError(getString(R.string.email_error));
    }

    @Override
    public void showValidationErrorPassword() {
        secondInputLayout.setError(getString(R.string.password_error));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        super.onPause();
    }
}
