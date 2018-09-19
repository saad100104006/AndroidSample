package uk.co.transferx.app.ui.mainscreen.presenters;

import android.content.SharedPreferences;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;
import uk.co.transferx.app.data.remote.RecipientsApi;
import uk.co.transferx.app.data.dto.RecipientDto;
import uk.co.transferx.app.ui.recipients.addrecipients.Mode;
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager;

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
                                       final TokenManager tokenManager,
                                       final SharedPreferences sharedPreferences) {
        super(sharedPreferences);
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
        refreshData();
    }

    public void refreshIfNeeded() {

    }

    private void refreshData() {
        disposable = recipientRepository.getRecipients()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    recipientDtoList = result;
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
        globalErrorHandler(throwable);
        isReqested = false;
    }

    public void deleteRecipient(final RecipientDto recipientDto) {
        deleteDisposable = tokenManager.getToken()
                .flatMap(token -> recipientsApi.deleteRecipient(token.getAccessToken(), recipientDto.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.code() == HttpURLConnection.HTTP_OK && ui != null) {
                        ui.deleteRecipient(recipientDto);
                        recipientRepository.deleteRecipient(recipientDto);
                    }
                }, this::handleError);

    }

    public void addRecipient(Mode mode, RecipientDto recipientDto) {
        if (ui != null) {
            ui.addRecipient(mode, recipientDto);
        }
    }

    public void editRecipient(Mode mode, int position) {
        if (ui != null) {
            ui.addRecipient(mode, recipientDtoList.get(position));
        }
    }

    public void goToTransfer(int position) {
        if (ui != null) {
            ui.goToTransfer(recipientDtoList.get(position));
        }
    }

    public void goToTransfer(RecipientDto recipientDto) {
        if (ui != null) {
            ui.goToTransfer(recipientDto);
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

        void goToTransfer(RecipientDto recipientDto);

        void showError();

        void addRecipient(Mode mode, RecipientDto recipientDto);

        void deleteRecipient(RecipientDto recipientDto);

    }
}
