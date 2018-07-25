package uk.co.transferx.app.signup.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.signup.presenters.SignUpStepTwoPresenter;
import uk.co.transferx.app.welcom.WelcomeActivity;

import static uk.co.transferx.app.util.Constants.EMAIL;
import static uk.co.transferx.app.util.Constants.PASSWORD;
import static uk.co.transferx.app.util.Constants.U_NAME;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepTwoFragment extends BaseFragment implements SignUpStepTwoPresenter.SignUpStepTwoUI {

    @Inject
    SignUpStepTwoPresenter presenter;
    private TextInputEditText firstInput, secondInput;
    private TextView firstLabel, secondLabel, firstError, secondError;
    private TextWatcher firstTextWatcher, secondTextWatcher;
    private ImageView backButton;

    @Override
    public String tagName() {
        return SignUpStepTwoFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            presenter.setName(bundle.getString(U_NAME));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_step_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.title_two));
        firstInput = view.findViewById(R.id.first_input);
        secondInput = view.findViewById(R.id.second_input);
        firstLabel = view.findViewById(R.id.first_input_label);
        secondLabel = view.findViewById(R.id.second_input_label);
        firstError = view.findViewById(R.id.first_input_error);
        secondError = view.findViewById(R.id.second_input_error);
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            hideKeyboard(backButton);
            getActivity().onBackPressed();
        });
        firstLabel.setText(R.string.email_address);
        secondLabel.setText(R.string.password);
        firstInput.setHint(R.string.enter_your_email_address);
        secondInput.setHint(R.string.enter_your_password);
        firstInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        secondInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        firstInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_address, 0, 0, 0);
        secondInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        buttonNext = view.findViewById(R.id.sign_in);
        buttonNext.setOnClickListener(v -> presenter.goToNextStep());
        if (savedInstanceState != null) {
            firstInput.setText(savedInstanceState.getString(EMAIL));
            secondInput.setText(savedInstanceState.getString(PASSWORD));
        }
    }

    @Override
    public void showErrorEmail() {
        setStatusOfError(firstInput, firstLabel, R.color.red);
        firstError.setText(R.string.email_error);
        firstError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorPassword() {
        setStatusOfError(secondInput, secondLabel, R.color.red);
        secondError.setText(R.string.password_error);
        secondError.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToNextView(String uname, String email, String password) {
        hideKeyboard(secondInput);
        Bundle bundle = new Bundle();
        bundle.putString(U_NAME, uname);
        bundle.putString(EMAIL, email);
        bundle.putString(PASSWORD, password);
        ((SignUpActivity) getActivity()).showNextOrPreviousFragment(2, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
        presenter.setEmail(firstInput.getText().toString());
        presenter.setPassword(secondInput.getText().toString());
        firstInput.addTextChangedListener(firstTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.setEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        secondInput.addTextChangedListener(secondTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.setPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        firstInput.removeTextChangedListener(firstTextWatcher);
        secondInput.removeTextChangedListener(secondTextWatcher);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(EMAIL, firstInput.getText().toString());
        outState.putString(PASSWORD, secondInput.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setStateButton(boolean isEnabled) {
        setButtonStatus(isEnabled);
    }

    @Override
    public void goToWelcome() {
       //no op
    }
}
