package uk.co.transferx.app.signin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.MainActivity;
import uk.co.transferx.app.recoverpass.RecoverPasswordActivity;

/**
 * Created by sergey on 19.11.17.
 */

public class SignInEmailFragment extends BaseFragment {


    @Override
    public String tagName() {
        return SignInEmailFragment.class.getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_step_fragment_layout, container, false);
        TextInputLayout firstInputLayout = view.findViewById(R.id.first_layout);
        TextInputLayout secondInputLayout = view.findViewById(R.id.second_layout);
        ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.one_step_away));
        firstInputLayout.setHint(getString(R.string.email));
        firstInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        secondInputLayout.setHint(getString(R.string.password));
        secondInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Button buttonSignIn = view.findViewById(R.id.next_step);
        buttonSignIn.setText(R.string.sign_in);
        TextView forgotPass = view.findViewById(R.id.terms);
        String forgotPassword = getString(R.string.forgot_password) + "?";
        forgotPass.setText(forgotPassword.toUpperCase());
        forgotPass.setOnClickListener(v -> RecoverPasswordActivity.starRecoverPasswordActivity(getActivity()));
        buttonSignIn.setOnClickListener(v -> MainActivity.startMainActivity(getActivity()));
        return view;
    }


}
