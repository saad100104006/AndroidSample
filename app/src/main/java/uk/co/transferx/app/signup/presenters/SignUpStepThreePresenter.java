package uk.co.transferx.app.signup.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by sergey on 06.12.17.
 */

public class SignUpStepThreePresenter extends BasePresenter<SignUpStepThreePresenter.SignUpStepThreeUI> {

    private int[] firstPin;
    private int[] secondPin;

    @Inject
    public SignUpStepThreePresenter() {
    }


    public void pinFirstEntered(int[] firstPin) {
        this.firstPin = firstPin;
    }

    public void setSecondPin(int[] secondPin) {
        validatePin(secondPin);
    }

    private void validatePin(int[] secondPin) {
        for (int i = 0; i < firstPin.length; i++) {
            if (firstPin[i] != secondPin[i]) {
                ui.showErrorPin();
                return;
            }
        }
        ui.nextStep(secondPin);

    }


    public interface SignUpStepThreeUI extends UI {

        void showErrorPin();

        void nextStep(int[] validPin);
    }
}
