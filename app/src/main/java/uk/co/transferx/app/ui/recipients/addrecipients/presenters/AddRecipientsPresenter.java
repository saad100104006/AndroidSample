package uk.co.transferx.app.ui.recipients.addrecipients.presenters;

import android.content.SharedPreferences;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;
import uk.co.transferx.app.data.remote.RecipientsApi;
import uk.co.transferx.app.data.dto.RecipientDto;
import uk.co.transferx.app.data.pojo.Recipient;
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager;
import uk.co.transferx.app.util.Util;

import static uk.co.transferx.app.util.Constants.FIRST_RECIPIENT_ADDED_MODE;
import static uk.co.transferx.app.util.Constants.FIRST_START_APP;
import static uk.co.transferx.app.util.Constants.RECIPIENT_ADDED_MODE;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsPresenter extends BasePresenter<AddRecipientsPresenter.AddRecipientsUI> {

    private final RecipientsApi recipientsApi;
    private RecipientDto recipientDto;

    private Disposable disposable;
    private final TokenManager tokenManager;
    private String firstName, lastName, country, phone;
    private final RecipientRepository recipientRepository;

    @Inject
    public AddRecipientsPresenter(final RecipientsApi recipientsApi,
                                  final TokenManager tokenManager,
                                  final RecipientRepository recipientRepository,
                                  final SharedPreferences sharedPreferences) {
        super(sharedPreferences);
        this.recipientsApi = recipientsApi;
        this.tokenManager = tokenManager;
        this.recipientRepository = recipientRepository;
    }

    public void saveUserToApi() {
        disposable = tokenManager.getToken()
                .flatMap(token -> recipientsApi.addRecipient(token.getAccessToken(), new Recipient.Builder()
                        .firstname(firstName)
                        .lastname(lastName)
                        .country(country)
                        .phone(phone)
                        .build()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        recipientRepository.addUser(new RecipientDto(resp.body().getId(), firstName, lastName, null, country, phone));
//                        ui.userActionPerformed();
                        Boolean firstRecipientAdded = sharedPreferences.getBoolean(FIRST_START_APP, true);
                        if (firstRecipientAdded) {
                            this.ui.goToConfirmationScreen(FIRST_RECIPIENT_ADDED_MODE);
                            sharedPreferences.edit().putBoolean(FIRST_START_APP, false).apply();
                        } else this.ui.goToConfirmationScreen(RECIPIENT_ADDED_MODE);
                    }
                }, this::handleError);
    }

    public void refreshUserData() {
        disposable = tokenManager.getToken()
                .flatMap(token -> recipientsApi.updateRecipient(token.getAccessToken(),
                        recipientDto.getId(),
                        new Recipient.Builder()
                                .firstname(recipientDto.getFirstName())
                                .lastname(recipientDto.getLastName())
                                .country(recipientDto.getCountry())
                                .phone(recipientDto.getPhone())
                                .build()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpURLConnection.HTTP_OK && ui != null) {
                        ui.userActionPerformed();
                        recipientRepository.upDateUser(recipientDto);
                        return;
                    }
                    handleError(new Throwable(resp.errorBody().string()));
                }, this::handleError);
    }

    public RecipientDto getRecipient() {
        return recipientDto;
    }

    public void deleteRecipient(final String id) {
        if (id == null) {
            handleError(new Throwable("Error id is null"));
            return;
        }
        disposable = tokenManager.getToken()
                .flatMap(token -> recipientsApi.deleteRecipient(token.getAccessToken(), id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.code() == HttpURLConnection.HTTP_OK && ui != null) {
                        recipientRepository.deleteRecipient(id);
                        ui.userActionPerformed();
                    }
                });
    }

    public void sendTransfer() {
        if (ui != null) {
            ui.sendTransfer(recipientDto);
        }
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        if (recipientDto != null) {
            recipientDto.setFirstName(firstName);
        }
        validateInput();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        if (recipientDto != null) {
            recipientDto.setLastName(lastName);
        }
        validateInput();
    }

    public void setCountry(String country) {
        this.country = country;
        if (recipientDto != null) {
            recipientDto.setCountry(country);
        }
        validateInput();
    }

    public void setPhone(String phone) {
        this.phone = phone;
        if (recipientDto != null) {
            recipientDto.setPhone(phone);
        }
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

    public void setRecipient(RecipientDto recipient) {
        if (recipient == null) {
            return;
        }
        this.recipientDto = recipient;
        firstName = recipientDto.getFirstName();
        lastName = recipientDto.getLastName();
        phone = recipientDto.getPhone();
        country = recipientDto.getCountry();
    }

    @Override
    public void attachUI(AddRecipientsUI ui) {
        super.attachUI(ui);
        if (recipientDto != null) {
            this.ui.setData(recipientDto);
        }
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

        void userActionPerformed();

        void goToConfirmationScreen(int MODE);

        void showError();

        void setData(RecipientDto recipientDto);

        void setEnabledButton(boolean enabled);

        void sendTransfer(RecipientDto recipientDto);
    }
}
