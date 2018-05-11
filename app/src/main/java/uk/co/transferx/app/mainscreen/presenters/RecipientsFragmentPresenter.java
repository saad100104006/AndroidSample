package uk.co.transferx.app.mainscreen.presenters;

import android.util.Log;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragmentPresenter extends BasePresenter<RecipientsFragmentPresenter.RecipientsFragmentUI> {

    private boolean isShouldRefresh;
    private final RecipientRepository recipientRepository;
    private List<RecipientDto> recipientDtoList = new ArrayList<>();
    private final RecipientsApi recipientsApi;
    private final TokenManager tokenManager;
    private Disposable disposable;
    private Disposable deleteDisposable;
    private boolean isReqested;

    @Inject
    public RecipientsFragmentPresenter(final RecipientRepository recipientRepository,
                                       final RecipientsApi recipientsApi,
                                       final TokenManager tokenManager) {
        this.recipientRepository = recipientRepository;
        this.recipientsApi = recipientsApi;
        this.tokenManager = tokenManager;
    }

    public void setShouldRefresh(boolean isShouldRefresh) {
        this.isShouldRefresh = isShouldRefresh;
    }

    @Override
    public void attachUI(RecipientsFragmentUI ui) {
        super.attachUI(ui);
        if (isShouldRefresh()) {
            refreshData();
            return;
        }
        if (ui != null) {
            ui.setRecipients(recipientDtoList);
        }
    }

    public void refreshIfNeeded() {
        if (isShouldRefresh()) {
            refreshData();
        }
    }

    private void refreshData() {
        if (isReqested) {
            return;
        }
        isShouldRefresh = false;
        recipientDtoList.clear();
        isReqested = true;
        disposable = recipientRepository.getRecipients()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    recipientDtoList = result;
                    isReqested = false;
                    if (ui != null) {
                        ui.setRecipients(recipientDtoList);
                    }
                }, this::handleError);
    }

    private boolean isShouldRefresh() {
        Log.d("Serge", "listIsEmpty " + recipientDtoList.isEmpty() + " isShouldRefresh " + isShouldRefresh);
        return recipientDtoList.isEmpty() || isShouldRefresh;
    }

    private void handleError(Throwable throwable) {
        isReqested = false;
    }

    public void deleteRecipient(final RecipientDto recipientDto) {
        deleteDisposable = recipientsApi.deleteRecipient(tokenManager.getToken(), recipientDto.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.code() == HttpURLConnection.HTTP_OK && ui != null) {
                        ui.deleteRecipient(recipientDto);
                    }
                });

    }

    public void addRecipient() {
        if (ui != null) {
            ui.addRecipient();
        }
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null) {
            disposable.dispose();
        }
        if (deleteDisposable != null) {
            deleteDisposable.dispose();
        }
    }


    public interface RecipientsFragmentUI extends UI {

        void setFavoriteRecipients(List<RecipientDto> recipientDtos);

        void setRecipients(List<RecipientDto> recipientDtos);

        void addToFavorite(RecipientDto recipientDto);

        void updateFavoriteRecipients();

        void showError();

        void addRecipient();

        void deleteRecipient(RecipientDto recipientDto);

    }
}
