package uk.co.transferx.app.recoverpass.presenter;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 15/03/2018.
 */

public class RecoverPasswordPresenter extends BasePresenter<RecoverPasswordPresenter.RecoverPasswordUI> {

    private final TokenManager tokenManager;
    private String validEmail;

    @Inject
    public RecoverPasswordPresenter(final TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public void validateEmail(String email){
        if (!Util.validateEmail(email)) {
            ui.lockButton();
            return;
        }
        validEmail = email;
        ui.unlockButton();
    }

    public void sendEmail(){
        ui.successGoBack();
    }

    public void goBack(){
        ui.goBackToMain();
    }

    public interface RecoverPasswordUI extends UI{

        void unlockButton();

        void successGoBack();

        void lockButton();

        void goBackToMain();

    }
}
