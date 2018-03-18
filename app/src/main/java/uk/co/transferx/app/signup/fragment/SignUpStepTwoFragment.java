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
import android.widget.TextView;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.signup.presenters.SignUpStepTwoPresenter;

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
    private TextView firstLabel, secondLabel;
    private TextWatcher firstTextWatcher, secondTextWatcher;

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
        firstLabel.setText(R.string.email_address);
        secondLabel.setText(R.string.password);
        firstInput.setHint(R.string.enter_your_email_address);
        secondInput.setHint(R.string.enter_your_password);
        firstInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        secondInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        firstInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_address, 0, 0, 0);
        secondInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        view.findViewById(R.id.next).setOnClickListener(v -> presenter.validateInput(secondInput.getText().toString(), firstInput.getText().toString()));
    }

    @Override
    public void showErrorEmail() {
        setStatusOfError(firstInput, firstLabel, R.color.red);
    }

    @Override
    public void showErrorPassword() {
        setStatusOfError(secondInput, secondLabel, R.color.red);
    }

    @Override
    public void goToNextView(String uname, String email, String password) {
        Bundle bundle = new Bundle();
        bundle.putString(U_NAME, uname);
        bundle.putString(EMAIL, email);
        bundle.putString(PASSWORD, password);
        ((SignUpActivity) getActivity()).showNextOrPreviousFragment(2, bundle);
    }


    @Override
    public void onResume() {
        super.onResume();
        firstInput.addTextChangedListener(firstTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setStatusOfError(firstInput, firstLabel, R.color.black);
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
                setStatusOfError(secondInput, secondLabel, R.color.black);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        presenter.attachUI(this);
    }

    @Override
    public void onPause() {
        firstInput.removeTextChangedListener(firstTextWatcher);
        secondInput.removeTextChangedListener(secondTextWatcher);
        presenter.detachUI();
        super.onPause();
    }
}
