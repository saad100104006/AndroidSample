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

    @Inject
    public RecoverPasswordPresenter(final TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }


    public void sendEmail(String email){
        if (!Util.validateEmail(email)) {
            ui.showValidateError();
            return;
        }
         ui.successGoBack();
    }

    public interface RecoverPasswordUI extends UI{

        void showValidateError();

        void successGoBack();

    }
}
