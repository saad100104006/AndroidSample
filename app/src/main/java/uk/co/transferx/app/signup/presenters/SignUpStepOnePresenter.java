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

    private final SignUpApi signUpApi;
    private String initialToken;

    private Disposable disposable;

    @Inject
    public SignUpStepOnePresenter(SignUpApi signUpApi) {
        this.signUpApi = signUpApi;
    }


    @Override
    public void attachUI(SignUpStepOneUI ui) {
        super.attachUI(ui);
        if (initialToken == null) {
            disposable = signUpApi.getInitialToken()
                    .map(Response::body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bod -> initialToken = bod.string());
        }
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    public void validateAndGoNext(String firstName, String lastName) {
        if (initialToken == null) {
            ui.showError();
            return;
        }
        if (Util.isNullorEmpty(firstName)) {
            ui.showNameError();
            return;
        }
        if (Util.isNullorEmpty(lastName)) {
            ui.showLastNameError();
            return;
        }
        ui.goToNextStep(firstName, lastName, initialToken);

    }

    public interface SignUpStepOneUI extends UI {
        void goToNextStep(String firstName, String lastName, String token);

        void showError();

        void showNameError();

        void showLastNameError();
    }
}
