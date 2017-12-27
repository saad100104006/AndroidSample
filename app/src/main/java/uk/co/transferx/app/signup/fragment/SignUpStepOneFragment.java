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
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.signup.presenters.SignUpStepOnePresenter;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepOneFragment extends BaseFragment implements SignUpStepOnePresenter.SignUpStepOneUI {


    @Inject
    SignUpStepOnePresenter presenter;

    private TextInputLayout firstInputLayout;
    private TextInputLayout secondInputLayout;
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String TOKEN = "token";


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

        View view = inflater.inflate(R.layout.sign_up_step_fragment_layout, container, false);
        firstInputLayout = view.findViewById(R.id.first_layout);
        secondInputLayout = view.findViewById(R.id.second_layout);
        ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.title_one));
        firstInputLayout.setHint(getString(R.string.first_name));
        secondInputLayout.setHint(getString(R.string.last_name));
        final EditText firstName = firstInputLayout.getEditText();
        final EditText lastName = secondInputLayout.getEditText();
        firstName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        lastName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        firstInputLayout.setErrorEnabled(true);
        secondInputLayout.setErrorEnabled(true);
        firstName.addTextChangedListener(new TextWatcher() {
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
        lastName.addTextChangedListener(new TextWatcher() {
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

        view.findViewById(R.id.next_step).setOnClickListener(v -> presenter.validateAndGoNext(firstInputLayout.getEditText().getText().toString(), secondInputLayout.getEditText().getText().toString()));
        return view;
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
    public void goToNextStep(String firstName, String lastName, String token) {
        Bundle bundle = new Bundle();
        bundle.putString(FIRST_NAME, firstName);
        bundle.putString(LAST_NAME, lastName);
        bundle.putString(TOKEN, token);
        ((SignUpActivity) getActivity()).showNextOrPreviousFragment(1, bundle);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "Something wrong with connection, please try again later", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNameError() {
        firstInputLayout.setError("error");
    }

    @Override
    public void showLastNameError() {
        secondInputLayout.setError("error");
    }
}
