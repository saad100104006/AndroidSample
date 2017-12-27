package uk.co.transferx.app.signin.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by sergey on 27/12/2017.
 */

public class SignInEmailPresenter extends BasePresenter<SignInEmailPresenter.SignInEmailUI> {



    @Inject
    public SignInEmailPresenter() {
    }

    public interface SignInEmailUI extends UI {

    }
}
