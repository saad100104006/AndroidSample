package uk.co.transferx.app.signup.presenters;

import android.util.Log;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.pojo.UserRequest;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 08.12.17.
 */

public class SignUpStepTwoPresenter extends BasePresenter<SignUpStepTwoPresenter.SignUpStepTwoUI> {


    private String uname;
    private String token;
    private final SignUpApi signUpApi;
    private Disposable disposable;
    private final static String EMAIL = "Email";

    @Inject
    public SignUpStepTwoPresenter(SignUpApi signUpApi) {
        this.signUpApi = signUpApi;
    }

    public void validateInput(String password, String email) {
        if (!Util.validateEmail(email)) {
            ui.showErrorEmail(null);
            return;
        }
        if (!Util.validatePassword(password)) {
            ui.showErrorPassword(null);
            return;
        }
        sendInfoToBackend(email, password);

    }

    public void setNamesAndToken(String uname, String token) {
        this.uname = uname;
        this.token = token;
        Log.d("Sergey", uname);
        Log.d("Sergey", "token " + token);

    }

    @Override
    public void attachUI(SignUpStepTwoUI ui) {
        super.attachUI(ui);
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void sendInfoToBackend(String email, String password) {
        UserRequest.Builder request = new UserRequest.Builder();

        disposable = signUpApi.registerUser(token, request.uname(uname).email(email).upass(password).upassConfirmation(password).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        ui.goToNextView();
                        return;
                    }
                    if (resp.code() == HttpsURLConnection.HTTP_BAD_REQUEST && ui != null) {
                        String message = resp.errorBody().string();
                        if (message != null && message.contains(EMAIL)) {
                            ui.showErrorEmail(message);
                        }
                    }

                }, this::handleErrorFromBackend);


    }

    private void handleErrorFromBackend(Throwable throwable) {
        Log.d("Sergey", throwable.getMessage());
    }

    public interface SignUpStepTwoUI extends UI {

        void goToNextView();

        void showErrorPassword(String message);

        void showErrorEmail(String message);

    }
}
