package uk.co.transferx.app.signup.presenters;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 27/12/2017.
 */

public class SignUpStepOnePresenter extends BasePresenter<SignUpStepOnePresenter.SignUpStepOneUI> {

    private final char UNDERSCORE = '_';


    @Inject
    public SignUpStepOnePresenter() {
    }


    public void validateAndGoNext(String firstName, String lastName) {
        if (Util.isNullorEmpty(firstName)) {
            ui.showNameError();
            return;
        }
        if (Util.isNullorEmpty(lastName)) {
            ui.showLastNameError();
            return;
        }
        String sb = firstName + UNDERSCORE +lastName;
        ui.goToNextStep(sb);

    }

    public interface SignUpStepOneUI extends UI {
        void goToNextStep(String uname);

        void showError();

        void showNameError();

        void showLastNameError();
    }
}
