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
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.signup.presenters.SignUpStepOnePresenter;

import static uk.co.transferx.app.util.Constants.U_NAME;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepOneFragment extends BaseFragment implements SignUpStepOnePresenter.SignUpStepOneUI {

    @Inject
    SignUpStepOnePresenter presenter;
    private TextInputEditText firstInput, secondInput;
    private TextView firstLabel, secondLabel;

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
        firstLabel.setText(R.string.first_name);
        secondLabel.setText(R.string.last_name);
        firstInput.setHint(R.string.first_name_hint);
        secondInput.setHint(R.string.last_name_hint);
        firstInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        secondInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        firstInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name, 0, 0, 0);
        secondInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name, 0, 0, 0);
        firstInput.addTextChangedListener(new TextWatcher() {
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
        secondInput.addTextChangedListener(new TextWatcher() {
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
        view.findViewById(R.id.next).setOnClickListener(v -> presenter.validateAndGoNext(firstInput.getText().toString(), secondInput.getText().toString()));
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

    @Override
    public void goToNextStep(String uname) {
        Bundle bundle = new Bundle();
        bundle.putString(U_NAME, uname);
        ((SignUpActivity) getActivity()).showNextOrPreviousFragment(1, bundle);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "Something wrong with connection, please try again later", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNameError() {
        setStatusOfError(firstInput, firstLabel, R.color.red);
    }

    @Override
    public void showLastNameError() {
        setStatusOfError(secondInput, secondLabel, R.color.red);
    }
}
