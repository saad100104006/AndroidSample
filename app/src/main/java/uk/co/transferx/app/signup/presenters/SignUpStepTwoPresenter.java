package uk.co.transferx.app.signup.presenters;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.crypto.CryptoManager;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 08.12.17.
 */

public class SignUpStepTwoPresenter extends BasePresenter<SignUpStepTwoPresenter.SignUpStepTwoUI> {


    private String uname;

    @Inject
    public SignUpStepTwoPresenter() {

    }
    public void validateInput(String password, String email) {
        if (!Util.validateEmail(email) && ui != null) {
            ui.showErrorEmail();
            return;
        }
        if (!Util.validatePassword(password) && ui != null) {
            ui.showErrorPassword();
            return;
        }
        if (ui != null) {
            ui.goToNextView(uname, email, password);
        }
    }

    public void setName(String uname) {
        this.uname = uname;

    }

    public interface SignUpStepTwoUI extends UI {

        void goToNextView(String uname, String email, String password);

        void showErrorPassword();

        void showErrorEmail();

    }
}
