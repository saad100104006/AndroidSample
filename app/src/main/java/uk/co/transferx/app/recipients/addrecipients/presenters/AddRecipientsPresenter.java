package uk.co.transferx.app.recipients.addrecipients.presenters;

import java.net.HttpURLConnection;

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
    private RecipientDto recipientDto;

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
                        recipientRepository.addUser(new RecipientDto(resp.body().getId(), firstName, lastName, null, country, phone));
                        ui.userActionPerformed();
                    }
                }, this::handleError);
    }

    public void refreshUserData() {
        disposable = recipientsApi.updateRecipient(tokenManager.getToken(),
                recipientDto.getId(),
                new Recipient.Builder()
                        .firstname(recipientDto.getFirstName())
                        .lastname(recipientDto.getLastName())
                        .country(recipientDto.getCountry())
                        .phone(recipientDto.getPhone())
                        .build())
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
        disposable = recipientsApi.deleteRecipient(tokenManager.getToken(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.code() == HttpURLConnection.HTTP_OK && ui != null) {
                        recipientRepository.deleteRecipient(id);
                        ui.userActionPerformed();
                    }
                });
    }

    public void sendTransfer(){
        if(ui != null){
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

        void showError();

        void setData(RecipientDto recipientDto);

        void setEnabledButton(boolean enabled);

        void sendTransfer(RecipientDto recipientDto);
    }
}
