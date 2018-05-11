package uk.co.transferx.app.recipients.addrecipients.presenters;

import android.util.Log;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.pojo.Recipient;
import uk.co.transferx.app.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsPresenter extends BasePresenter<AddRecipientsPresenter.AddRecipientsUI> {

    private final RecipientsApi recipientsApi;

    private Disposable disposable;
    private final TokenManager tokenManager;
    private String firstName, lastName, country, phone;
    private final RecipientRepository recipientRepository;

    @Inject
    public AddRecipientsPresenter(final RecipientsApi recipientsApi,
                                  final TokenManager tokenManager,
                                  final RecipientRepository recipientRepository) {
        this.recipientsApi = recipientsApi;
        this.tokenManager = tokenManager;
        this.recipientRepository = recipientRepository;
    }

    public void saveUserToApi() {
        disposable = recipientsApi.addRecipient(tokenManager.getToken(), new Recipient.Builder()
                .firstname(firstName)
                .lastname(lastName)
                .country(country)
                .phone(phone)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        recipientRepository.addUser(new RecipientDto(resp.body().getId(),firstName, lastName, null, country, phone));
                        ui.userWasAdded();
                    }
                }, this::handleError);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        validateInput();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        validateInput();
    }

    public void setCountry(String country) {
        this.country = country;
        validateInput();
    }

    public void setPhone(String phone) {
        this.phone = phone;
        validateInput();
    }

    private void validateInput() {
        if (ui != null) {
            ui.setEnabledButton(isInputValid());
        }
    }

    private boolean isInputValid() {
        return Util.validateName(firstName) && Util.validateName(lastName)
                && Util.validateName(country) && Util.validatePhone(phone);
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

    private void handleError(Throwable throwable) {
        ui.showError();
    }

    public interface AddRecipientsUI extends UI {

        void userWasAdded();

        void showError();

        void setEnabledButton(boolean enabled);
    }
}
