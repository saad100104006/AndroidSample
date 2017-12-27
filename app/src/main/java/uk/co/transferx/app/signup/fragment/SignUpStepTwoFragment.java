package uk.co.transferx.app.signup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.signup.presenters.SignUpStepTwoPresenter;

import static uk.co.transferx.app.signup.fragment.SignUpStepOneFragment.FIRST_NAME;
import static uk.co.transferx.app.signup.fragment.SignUpStepOneFragment.LAST_NAME;
import static uk.co.transferx.app.signup.fragment.SignUpStepOneFragment.TOKEN;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepTwoFragment extends BaseFragment implements SignUpStepTwoPresenter.SignUpStepTwoUI {

    @Inject
    SignUpStepTwoPresenter presenter;

    private TextInputLayout secondInputLayout, firstInputLayout;

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
            presenter.setNamesAndToken(bundle.getString(FIRST_NAME), bundle.getString(LAST_NAME), bundle.getString(TOKEN));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_step_fragment_layout, container, false);
        firstInputLayout = view.findViewById(R.id.first_layout);
        secondInputLayout = view.findViewById(R.id.second_layout);
        ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.title_two));
        firstInputLayout.setHint(getString(R.string.email));
        firstInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        secondInputLayout.setHint(getString(R.string.password));
        final EditText passwordEditText = secondInputLayout.getEditText();
        final EditText emailEditText = firstInputLayout.getEditText();
        passwordEditText.addTextChangedListener(new TextWatcher() {
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
        emailEditText.addTextChangedListener(new TextWatcher() {
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
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        secondInputLayout.setErrorEnabled(true);
        view.findViewById(R.id.next_step).setOnClickListener(v -> presenter.validateInput(passwordEditText.getText().toString(), emailEditText.getText().toString()));
        return view;
    }


    @Override
    public void showErrorEmail(String message) {

        firstInputLayout.setError(message == null ? getString(R.string.email_error) : message);
    }

    @Override
    public void showErrorPassword(String message) {
        secondInputLayout.setError(message == null ? getString(R.string.password_error) : message);
    }

    @Override
    public void goToNextView() {
        ((SignUpActivity) getActivity()).showNextOrPreviousFragment(2, null);
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
