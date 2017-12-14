package uk.co.transferx.app.signup.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by sergey on 08.12.17.
 */

public class SignUpStepTwoPresenter extends BasePresenter<SignUpStepTwoPresenter.SignUpStepTwoUI> {

    private static final String VALIDATE_PATTERN_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d+)[0-9a-zA-Z!@#$%]{8,}$";
    private static final String VALIDATE_PATTERN_EMAIL = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

    @Inject
    public SignUpStepTwoPresenter() {
    }

    public void validateInput(String password, String email) {
        if (!email.matches(VALIDATE_PATTERN_EMAIL)) {
            ui.showErrorEmail();
            return;
        }
        if (!password.matches(VALIDATE_PATTERN_PASSWORD)) {
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
