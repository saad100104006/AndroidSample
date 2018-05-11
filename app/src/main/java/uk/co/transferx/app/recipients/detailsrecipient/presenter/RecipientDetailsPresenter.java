package uk.co.transferx.app.recipients.detailsrecipient.presenter;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.pojo.Recipient;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 10/01/2018.
 */

public class RecipientDetailsPresenter extends BasePresenter<RecipientDetailsPresenter.RecipientDetailsUI> {

    private RecipientDto recipientDto;
    private boolean isInitialized;
    private CompositeDisposable compositeDisposable;
    private final RecipientsApi recipientsApi;
    private final TokenManager tokenManager;


    @Inject
    public RecipientDetailsPresenter(final RecipientsApi recipientsApi, final TokenManager tokenManager) {
        this.recipientsApi = recipientsApi;
        this.tokenManager = tokenManager;

    }


    @Override
    public void attachUI(RecipientDetailsUI ui) {
        super.attachUI(ui);
        compositeDisposable = new CompositeDisposable();
        if (!isInitialized) {
            ui.setData(recipientDto);
            isInitialized = true;
        }
    }

    @Override
    public void detachUI() {
        super.detachUI();
        compositeDisposable.dispose();
    }

    public void deleteRecipient() {
        if (recipientDto != null) {
            Disposable disposable = recipientsApi.deleteRecipient(tokenManager.getToken(), recipientDto.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                            ui.goBackToListWithRefresh();
                        } else if (ui != null) {
                            ui.showErrorDelete();
                        }
                    });
            compositeDisposable.add(disposable);
        }
    }

    private void saveRecipient(RecipientDto recipientDtoChanged) {
        if (recipientDto.equals(recipientDtoChanged)) {
            ui.goBackToList();
            return;
        }
        Disposable updateDisposable = recipientsApi.updateRecipient(tokenManager.getToken(), recipientDto.getId(), new Recipient.Builder()
                .firstname(recipientDtoChanged.getFirstName())
                .lastname(recipientDtoChanged.getLastName())
                .country(recipientDtoChanged.getCountry())
                .phone(recipientDtoChanged.getPhone())
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        ui.goBackToListWithRefresh();
                    }
                }, throwable -> {
                    if (ui != null) {
                        ui.showErrorSave();
                    }
                });
        compositeDisposable.add(updateDisposable);
    }

    public void validateInput(String firstName, String lastName, String country, String phone) {
        if (!Util.validateName(firstName)) {
            ui.nameError();
            return;
        }

        if (!Util.validateName(lastName)) {
            ui.lastNameError();
            return;
        }

        if (!Util.validateName(country)) {
            ui.countryError();
            return;
        }
        if (!Util.validatePhone(phone)) {
            ui.phoneError();
            return;
        }
     //   RecipientDto recipientDtoChanged = new RecipientDto(recipientDto.getId(), firstName + " " + lastName, recipientDto.getImgUrl(), country, phone);
     //   saveRecipient(recipientDtoChanged);

    }

    public void setData(RecipientDto recipientDto) {
        this.recipientDto = recipientDto;
    }

    public interface RecipientDetailsUI extends UI {

        void goBackToListWithRefresh();

        void goBackToList();

        void showErrorDelete();

        void showErrorSave();

        void setData(RecipientDto recipientDto);

        void nameError();

        void lastNameError();

        void countryError();

        void phoneError();

    }
}
