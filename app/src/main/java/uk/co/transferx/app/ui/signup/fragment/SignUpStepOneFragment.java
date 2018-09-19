package uk.co.transferx.app.ui.signup.fragment;

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
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.ui.signup.SignUpActivity;
import uk.co.transferx.app.ui.signup.presenters.SignUpStepOnePresenter;

import static uk.co.transferx.app.util.Constants.U_NAME;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepOneFragment extends BaseFragment implements SignUpStepOnePresenter.SignUpStepOneUI {

    private final static String FIRST_NAME = "first_name";
    private final static String LAST_NAME = "last_name";

    @Inject
    SignUpStepOnePresenter presenter;
    private TextInputEditText firstInput, secondInput;
    private TextView firstLabel, secondLabel, firstError, secondError;
    private TextWatcher firstTextWatcher, secondTextWatcher;
    private ImageView backButton;

    @Override
    public String tagName() {
        return SignUpStepOneFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_step_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        firstLabel.setText(R.string.first_name);
        secondLabel.setText(R.string.last_name);
        firstInput.setHint(R.string.first_name_hint);
        secondInput.setHint(R.string.last_name_hint);
        firstInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        secondInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        firstInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name, 0, 0, 0);
        secondInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name, 0, 0, 0);
        buttonNext = view.findViewById(R.id.sign_in);
        buttonNext.setOnClickListener(v -> presenter.goToNextStep());
        if (savedInstanceState != null) {
            firstInput.setText(savedInstanceState.getString(FIRST_NAME));
            secondInput.setText(savedInstanceState.getString(LAST_NAME));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
        presenter.setFirstName(firstInput.getText().toString());
        presenter.setLastName(secondInput.getText().toString());
        firstInput.addTextChangedListener(firstTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.setFirstName(charSequence.toString());
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
                presenter.setLastName(charSequence.toString());
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
        outState.putString(FIRST_NAME, firstInput.getText().toString());
        outState.putString(LAST_NAME, secondInput.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void goToNextStep(String uname) {
        Bundle bundle = new Bundle();
        bundle.putString(U_NAME, uname);
        ((SignUpActivity) getActivity()).showNextOrPreviousFragment(1, bundle);
    }

    @Override
    public void setButton(boolean isEnabled) {
        setButtonStatus(isEnabled);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "Something wrong with connection, please try again later", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNameError() {
        setStatusOfError(firstInput, firstLabel, R.color.red);
        firstError.setText(R.string.first_name_error);
        firstError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLastNameError() {
        setStatusOfError(secondInput, secondLabel, R.color.red);
        secondError.setText(R.string.last_name_error);
        secondError.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToWelcome() {
        //no op
    }
}
