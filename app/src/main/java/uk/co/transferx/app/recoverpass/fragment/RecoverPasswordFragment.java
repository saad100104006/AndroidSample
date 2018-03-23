package uk.co.transferx.app.recoverpass.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.recoverpass.RecoverPasswordActivity;
import uk.co.transferx.app.recoverpass.presenter.RecoverPasswordPresenter;

/**
 * Created by sergey on 23.11.17.
 */

public class RecoverPasswordFragment extends BaseFragment implements RecoverPasswordPresenter.RecoverPasswordUI {

    @Inject
    RecoverPasswordPresenter presenter;

    private TextInputEditText textInputEditText;
    private TextView label, firstError;

    @Override
    public String tagName() {
        return RecoverPasswordFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TransferXApplication) (getActivity().getApplication())).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recover_password_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textInputEditText = view.findViewById(R.id.first_input);
        label = view.findViewById(R.id.email_label);
        firstError = view.findViewById(R.id.email_error);
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setStatusOfError(textInputEditText, label, R.color.black);
                firstError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.findViewById(R.id.send_mail_button).setOnClickListener(v -> presenter.sendEmail(textInputEditText.getText().toString()));

    }

    @Override
    public void showValidateError() {
        setStatusOfError(textInputEditText, label, R.color.red);
        firstError.setText(R.string.email_error);
        firstError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    public void successGoBack() {
        hideKeyboard(textInputEditText);
        ((RecoverPasswordActivity)getActivity()).goSuccess();
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        super.onPause();
    }
}
