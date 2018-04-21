package uk.co.transferx.app.signup.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.util.Util;

import static uk.co.transferx.app.util.Constants.UNDERSCORE;

/**
 * Created by sergey on 27/12/2017.
 */

public class SignUpStepOnePresenter extends BasePresenter<SignUpStepOnePresenter.SignUpStepOneUI> {

    private String firstName, lastName;


    @Inject
    public SignUpStepOnePresenter() {
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        isInputDataValid();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        isInputDataValid();
    }

    public void goToNextStep() {
        if (ui != null) {
            String sb = firstName + UNDERSCORE + lastName;
            ui.goToNextStep(sb);
        }
    }

    private void isInputDataValid() {
        if (ui != null) {
            ui.setButton(Util.validateName(firstName) && Util.validateName(lastName));
        }
    }

    public interface SignUpStepOneUI extends UI {
        void goToNextStep(String uname);

        void showError();

        void setButton(boolean isEnabled);

        void showNameError();

        void showLastNameError();
    }
}
