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




    @Inject
    public SignUpStepOnePresenter() {
    }


    public void validateAndGoNext(String firstName, String lastName) {
        if (Util.isNullorEmpty(firstName) && ui != null) {
            ui.showNameError();
            return;
        }
        if (Util.isNullorEmpty(lastName) && ui != null) {
            ui.showLastNameError();
            return;
        }
        String sb = firstName + UNDERSCORE + lastName;
        ui.goToNextStep(sb);

    }

    public interface SignUpStepOneUI extends UI {
        void goToNextStep(String uname);

        void showError();

        void showNameError();

        void showLastNameError();
    }
}
