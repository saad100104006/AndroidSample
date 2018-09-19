package uk.co.transferx.app.ui.signup.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 08.12.17.
 */

public class SignUpStepTwoPresenter extends BasePresenter<SignUpStepTwoPresenter.SignUpStepTwoUI> {


    private String uname;
    private String email, password;

    @Inject
    public SignUpStepTwoPresenter() {

    }

    public void goToNextStep() {
        if (ui != null) {
            ui.goToNextView(uname, email, password);
        }
    }

    public void setEmail(String email) {
        this.email = email;
        validateInputData();
    }

    public void setPassword(String password) {
        this.password = password;
        validateInputData();
    }

    private void validateInputData() {
        if (ui != null) {
            ui.setStateButton(isInputCorrect());
        }
    }

    private boolean isInputCorrect() {
        return Util.validatePassword(password) && Util.validateEmail(email);
    }

    public void setName(String uname) {
        this.uname = uname;

    }

    public interface SignUpStepTwoUI extends UI {

        void goToNextView(String uname, String email, String password);

        void showErrorPassword();

        void showErrorEmail();

        void setStateButton(boolean isEnabled);

    }
}
