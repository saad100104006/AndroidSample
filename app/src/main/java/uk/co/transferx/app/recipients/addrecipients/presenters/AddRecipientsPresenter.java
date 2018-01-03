package uk.co.transferx.app.recipients.addrecipients.presenters;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.pojo.Recipient;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsPresenter extends BasePresenter<AddRecipientsPresenter.AddRecipientsUI> {

    private final RecipientsApi recipientsApi;

    private Disposable disposable;

    @Inject
    public AddRecipientsPresenter(final RecipientsApi recipientsApi) {
        this.recipientsApi = recipientsApi;
    }

    public void saveUserToApi(String firstName, String lastName, String country, String phone, String token) {
        disposable = recipientsApi.addRecipient(token, new Recipient.Builder()
                .firstname(firstName)
                .lastname(lastName)
                .country(country)
                .phone(phone)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        ui.userWasAdded();
                    }
                }, this::handleError);
    }

    @Override
    public void attachUI(AddRecipientsUI ui) {
        super.attachUI(ui);
    }

    @Override
    public void detachUI() {
        super.detachUI();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void handleError(Throwable throwable){
        ui.showError();
    }

    public interface AddRecipientsUI extends UI {

        void userWasAdded();

        void showError();
    }
}
