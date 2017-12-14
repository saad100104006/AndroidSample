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


    @Override
    public void attachUI(SignUpStepThreeUI ui) {
        super.attachUI(ui);
        if (firstPin != null && secondPin != null) {
            validatePin(secondPin);
            secondPin = null;
        }

    }

    public void pinFirstEntered(int[] firstPin) {
        this.firstPin = firstPin;
    }

    public void setSecondPin(int[] secondPin) {
        validatePin(secondPin);
    }

    private void validatePin(int[] secondPin) {
        if (ui == null) {
            this.secondPin = secondPin;
            return;
        }
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
