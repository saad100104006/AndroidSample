package uk.co.transferx.app.ui.signup.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.view.PinView;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.ui.mainscreen.MainActivity;
import uk.co.transferx.app.ui.signup.presenters.SignUpStepThreePresenter;
import uk.co.transferx.app.ui.tutorial.TutorialActivity;
import uk.co.transferx.app.ui.welcom.WelcomeActivity;

import static uk.co.transferx.app.util.Constants.EMAIL;
import static uk.co.transferx.app.util.Constants.EMPTY;
import static uk.co.transferx.app.util.Constants.PASSWORD;
import static uk.co.transferx.app.util.Constants.PIN_SHOULD_BE_INPUT;
import static uk.co.transferx.app.util.Constants.U_NAME;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepThreeFragment extends BaseFragment implements SignUpStepThreePresenter.SignUpStepThreeUI {

    @Inject
    SignUpStepThreePresenter presenter;
    private PinView firstPinView, secondPinView;
    private TextView firsLabelPin, secondLabelPin;
    private ImageView backButton;
    private TextWatcher firstPinWatcher, secondPinWatcher;
    private boolean reEnterPin;

    @Override
    public String tagName() {
        return SignUpStepThreeFragment.class.getSimpleName();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            presenter.setCredential(bundle.getString(U_NAME), bundle.getString(EMAIL), bundle.getString(PASSWORD));
            reEnterPin = bundle.getBoolean(PIN_SHOULD_BE_INPUT, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_step_three_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstPinView = view.findViewById(R.id.first_pin);
        secondPinView = view.findViewById(R.id.second_pin);
        firsLabelPin = view.findViewById(R.id.first_label_pin);
        secondLabelPin = view.findViewById(R.id.second_label_pin);
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            hideKeyboard(backButton);
            getActivity().onBackPressed();
        });
        buttonNext = view.findViewById(R.id.sign_in);
        buttonNext.setText(reEnterPin ? R.string.sign_in : R.string.register);
        buttonNext.setOnClickListener(v -> presenter.validatePin());
        firstPinView.setAnimationEnable(true);
        secondPinView.setAnimationEnable(true);
    }

    @Override
    public void showErrorPin() {
        setStatusOfError(firstPinView, firsLabelPin, R.color.red);
        setStatusOfError(secondPinView, secondLabelPin, R.color.red);
        setLinesColor(R.color.red);
    }

    private void resetErrorPin() {
        setStatusOfError(firstPinView, firsLabelPin, R.color.black);
        setStatusOfError(secondPinView, secondLabelPin, R.color.black);
        setLinesColor(R.color.black);
    }

    @Override
    public void goToMainScreen() {
        Activity activity = getActivity();
        if (activity != null) {
            if (reEnterPin) {
                MainActivity.startMainActivity(activity);
                activity.finish();
                return;
            }
//            TutorialActivity.startTutorialActivity(activity);
            activity.finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
        firstPinView.addTextChangedListener(firstPinWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetErrorPin();
                presenter.setFirstPin(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        secondPinView.addTextChangedListener(secondPinWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetErrorPin();
                presenter.setSecondPin(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setLinesColor(@ColorRes int color) {
        firstPinView.setLineColor(ContextCompat.getColor(firsLabelPin.getContext(), color));
        secondPinView.setLineColor(ContextCompat.getColor(firsLabelPin.getContext(), color));
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        firstPinView.removeTextChangedListener(firstPinWatcher);
        secondPinView.removeTextChangedListener(secondPinWatcher);
        firstPinView.setText(EMPTY);
        secondPinView.setText(EMPTY);
        super.onPause();
    }

    @Override
    public void showErrorFromBackend() {

    }

    @Override
    public void goToWelcome() {
        WelcomeActivity.startWelcomeActivity(getActivity());
    }

    @Override
    public void setButtonEnabled(boolean isEnabled) {
        setButtonStatus(isEnabled);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
