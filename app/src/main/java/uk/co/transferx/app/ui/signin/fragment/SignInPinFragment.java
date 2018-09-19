package uk.co.transferx.app.ui.signin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.ui.mainscreen.MainActivity;
import uk.co.transferx.app.ui.signin.presenter.SignInPinPresenter;
import uk.co.transferx.app.ui.welcom.WelcomeActivity;

/**
 * Created by sergey on 19.11.17.
 */

public class SignInPinFragment extends BaseFragment implements SignInPinPresenter.SignInPinUI {

    private PinView pinView;
    private TextView label;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setStatusOfError(pinView, label, R.color.black);
            setLinesColor(R.color.black);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public String tagName() {
        return SignInPinFragment.class.getSimpleName();
    }

    @Inject
    SignInPinPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signin_pin_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pinView = view.findViewById(R.id.first_pin);
        pinView.setAnimationEnable(true);
        ((TextView) view.findViewById(R.id.title)).setText(R.string.enter_your_pin);
        label = view.findViewById(R.id.first_label_pin);
        view.findViewById(R.id.sign_in).setOnClickListener(v -> presenter.checkPingAndLogIn(pinView.getText().toString()));
        view.findViewById(R.id.forgot_pin).setOnClickListener(v -> presenter.resetPassword());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
        pinView.addTextChangedListener(textWatcher);
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        super.onPause();
        pinView.removeTextChangedListener(textWatcher);
    }

    @Override
    public void goToMainScree() {
        Activity activity = getActivity();
        if (activity != null) {
            MainActivity.startMainActivity(activity);
            activity.finish();
        }
    }

    @Override
    public void pinChecked() {
        hideKeyboard(pinView);
        getActivity().finish();
    }

    private void setLinesColor(@ColorRes int color) {
        pinView.setLineColor(ContextCompat.getColor(pinView.getContext(), color));
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), "Somthing went wrong ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToWelcomeScreen() {
        hideKeyboard(pinView);
        WelcomeActivity.startWelcomeActivity(getActivity());
    }

    @Override
    public void showErrorPin() {
        setStatusOfError(pinView, label, R.color.red);
        setLinesColor(R.color.red);
    }

    @Override
    public void goToWelcome() {
        //no op
    }
}
