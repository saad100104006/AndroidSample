package uk.co.transferx.app.recipients.detailsrecipient.presenter;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by sergey on 10/01/2018.
 */

public class RecipientDetailsPresenter extends BasePresenter<RecipientDetailsPresenter.RecipientDetailsUI> {

    private RecipientDto recipientDto;

    private Disposable disposable;

    private boolean isInitialized;


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
        if (!isInitialized) {
            ui.setData(recipientDto);
            isInitialized = true;
        }
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void deleteRecipient() {
        if (recipientDto != null) {
            disposable = recipientsApi.deleteRecipient(tokenManager.getToken(), recipientDto.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                            ui.goBackToList();
                        } else if (ui != null) {
                            ui.showError();
                        }
                    });
        }
    }

    public void setData(RecipientDto recipientDto) {
        this.recipientDto = recipientDto;
    }

    public interface RecipientDetailsUI extends UI {

        void goBackToList();

        void showError();

        void setData(RecipientDto recipientDto);

    }
}
