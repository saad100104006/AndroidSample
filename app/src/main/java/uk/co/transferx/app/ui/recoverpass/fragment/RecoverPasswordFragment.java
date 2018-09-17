package uk.co.transferx.app.ui.recoverpass.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.ui.recoverpass.RecoverPasswordActivity;
import uk.co.transferx.app.ui.recoverpass.presenter.RecoverPasswordPresenter;

/**
 * Created by sergey on 23.11.17.
 */

public class RecoverPasswordFragment extends BaseFragment implements RecoverPasswordPresenter.RecoverPasswordUI {

    @Inject
    RecoverPasswordPresenter presenter;

    private TextInputEditText textInputEditText;
    private TextView label, firstError;
    private Button sendEmail;

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
        textInputEditText = view.findViewById(R.id.mailInput);
        label = view.findViewById(R.id.mailLabel);
        firstError = view.findViewById(R.id.mailError);
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // setStatusOfError(textInputEditText, label, R.color.black);
               // firstError.setVisibility(View.GONE);
                presenter.validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendEmail = view.findViewById(R.id.send_mail_button);
        sendEmail.setOnClickListener(v -> presenter.sendEmail());
        view.findViewById(R.id.back_button).setOnClickListener(v -> presenter.goBack());
    }

    @Override
    public void unlockButton() {
        sendEmail.setEnabled(true);
        sendEmail.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oval_button_black));
        sendEmail.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
      //  setStatusOfError(textInputEditText, label, R.color.red);
      //  firstError.setText(R.string.email_error);
      //  firstError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    public void successGoBack() {
        hideKeyboard(textInputEditText);
        ((RecoverPasswordActivity) getActivity()).goSuccess();
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        super.onPause();
    }

    @Override
    public void lockButton() {
        sendEmail.setEnabled(false);
        sendEmail.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oval_button_gray));
    }

    @Override
    public void goBackToMain() {
        getActivity().finish();
    }

    @Override
    public void error() {
        Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToWelcome() {
        // no op
    }
}
