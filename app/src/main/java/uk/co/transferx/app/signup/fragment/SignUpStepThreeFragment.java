package uk.co.transferx.app.signup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.signin.SignInActivity;
import uk.co.transferx.app.signin.SignInType;
import uk.co.transferx.app.signup.presenters.SignUpStepThreePresenter;
import uk.co.transferx.app.view.pinview.PinEditView;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepThreeFragment extends BaseFragment implements SignUpStepThreePresenter.SignUpStepThreeUI {

    @Inject
    SignUpStepThreePresenter presenter;

    private PinEditView firstPinEditView, secondPinEditView;


    @Override
    public String tagName() {
        return SignUpStepThreeFragment.class.getSimpleName();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_step_three_fragment_layout, container, false);
        Button nextButton = view.findViewById(R.id.next_step);
        firstPinEditView = view.findViewById(R.id.pin_first);
        secondPinEditView = view.findViewById(R.id.pin_second);
        firstPinEditView.setPinViewListener(pin -> presenter.pinFirstEntered(pin));
        secondPinEditView.setPinViewListener(pin -> presenter.setSecondPin(pin));
        nextButton.setText(R.string.sign_me_up);
        nextButton.setOnClickListener(v -> SignInActivity.starSignInActivity(getActivity(), SignInType.PIN));
        return view;
    }

    @Override
    public void showErrorPin() {
        secondPinEditView.setError();
    }

    @Override
    public void nextStep(int[] validPin) {
        StringBuilder sb = new StringBuilder();
        for (int p : validPin) {
            sb.append(" ");
            sb.append(p);
            sb.append(" ");
        }

        Log.d("Sergey", "pin [" + sb.toString() + "]");
        SignInActivity.starSignInActivity(getActivity(), SignInType.EMAIL);
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
    public void onDestroyView() {
        firstPinEditView.setPinViewListener(null);
        secondPinEditView.setPinViewListener(null);
        super.onDestroyView();
    }
}
