package uk.co.transferx.app.signup.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 08.12.17.
 */

public class SignUpStepTwoPresenter extends BasePresenter<SignUpStepTwoPresenter.SignUpStepTwoUI> {


    @Inject
    public SignUpStepTwoPresenter() {
    }

    public void validateInput(String password, String email) {
        if (!Util.validateEmail(email)) {
            ui.showErrorEmail();
            return;
        }
        if (!Util.validatePassword(password)) {
            ui.showErrorPassword();
            return;
        }
        ui.goToNextView();


    }

    public interface SignUpStepTwoUI extends UI {

        void goToNextView();

        void showErrorPassword();

        void showErrorEmail();

    }
}
