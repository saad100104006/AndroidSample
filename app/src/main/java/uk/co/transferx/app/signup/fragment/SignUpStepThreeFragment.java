package uk.co.transferx.app.signup.fragment;

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

import com.chaos.view.PinView;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.mainscreen.MainActivity;
import uk.co.transferx.app.signup.presenters.SignUpStepThreePresenter;
import uk.co.transferx.app.tutorial.TutorialActivity;

import static uk.co.transferx.app.util.Constants.EMAIL;
import static uk.co.transferx.app.util.Constants.EMPTY;
import static uk.co.transferx.app.util.Constants.PASSWORD;
import static uk.co.transferx.app.util.Constants.U_NAME;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepThreeFragment extends BaseFragment implements SignUpStepThreePresenter.SignUpStepThreeUI {

    @Inject
    SignUpStepThreePresenter presenter;
    private PinView firstPinView, secondPinView;
    private TextView firsLabelPin, secondLabelPin;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setStatusOfError(firstPinView, firsLabelPin, R.color.black);
            setStatusOfError(secondPinView, secondLabelPin, R.color.black);
            setLinesColor(R.color.black);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
        view.findViewById(R.id.next).setOnClickListener(v -> presenter.validatePin(firstPinView.getText().toString(), secondPinView.getText().toString()));
        firstPinView.setAnimationEnable(true);
        secondPinView.setAnimationEnable(true);
    }

    @Override
    public void showErrorPin() {
        setStatusOfError(firstPinView, firsLabelPin, R.color.red);
        setStatusOfError(secondPinView, secondLabelPin, R.color.red);
        setLinesColor(R.color.red);
    }

    @Override
    public void goToMainScreen() {
        Activity activity = getActivity();
        if (activity != null) {
            TutorialActivity.startTutorialActivity(activity);
            activity.finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
        firstPinView.addTextChangedListener(textWatcher);
        secondPinView.addTextChangedListener(textWatcher);
    }

    private void setLinesColor(@ColorRes int color) {
        firstPinView.setLineColor(ContextCompat.getColor(firsLabelPin.getContext(), color));
        secondPinView.setLineColor(ContextCompat.getColor(firsLabelPin.getContext(), color));
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        firstPinView.removeTextChangedListener(textWatcher);
        secondPinView.removeTextChangedListener(textWatcher);
        firstPinView.setText(EMPTY);
        secondPinView.setText(EMPTY);
        super.onPause();
    }

    @Override
    public void showErrorFromBackend() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
