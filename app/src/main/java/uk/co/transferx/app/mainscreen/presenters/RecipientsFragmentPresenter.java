package uk.co.transferx.app.mainscreen.presenters;

import android.content.SharedPreferences;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
    private final SharedPreferences sharedPreferences;
    private CompositeDisposable compositeDisposable;
    private List<RecipientDto> favoriteListRecipients = new ArrayList<>(3);
    private List<RecipientDto> recipientDtoList = new ArrayList<>();
    private final RecipientsApi recipientsApi;
    private final TokenManager tokenManager;

    @Inject
    public RecipientsFragmentPresenter(final RecipientRepository recipientRepository,
                                       final SharedPreferences sharedPreferences,
                                       final RecipientsApi recipientsApi,
                                       final TokenManager tokenManager) {
        this.recipientRepository = recipientRepository;
        this.sharedPreferences = sharedPreferences;
        this.recipientsApi = recipientsApi;
        this.tokenManager = tokenManager;
    }

    public void setShouldRefresh(boolean isShouldRefresh) {
        this.isShouldRefresh = isShouldRefresh;
    }

    @Override
    public void attachUI(RecipientsFragmentUI ui) {
        super.attachUI(ui);
        compositeDisposable = new CompositeDisposable();
        if (isShouldRefresh) {
            recipientDtoList.clear();
            favoriteListRecipients.clear();
            final Disposable disposable = recipientRepository.refreshRecipients()
                    .toObservable()
                    .flatMap(Observable::fromIterable)
                    .doOnNext(res -> {
                        if (sharedPreferences.getBoolean(res.getId(), false)) {
                            favoriteListRecipients.add(res);
                        }
                    })
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        recipientDtoList = result;
                        if (ui != null) {
                            ui.setRecipients(recipientDtoList);
                            ui.setFavoriteRecipients(favoriteListRecipients);
                        }
                    }, this::handleError);
            isShouldRefresh = false;
            compositeDisposable.add(disposable);
            return;
        }
        if (recipientDtoList.isEmpty()) {
            favoriteListRecipients.clear();
            final Disposable recipientDisposable = recipientRepository.getRecipients()
                    .toObservable()
                    .flatMap(Observable::fromIterable)
                    .doOnNext(res -> {
                        if (sharedPreferences.getBoolean(res.getId(), false)) {
                            favoriteListRecipients.add(res);
                        }
                    })
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (ui != null) {
                            ui.setRecipients(result);
                            ui.setFavoriteRecipients(favoriteListRecipients);
                        }
                    }, this::handleError);
            isShouldRefresh = false;
            compositeDisposable.add(recipientDisposable);
        }
    }

    private void handleError(Throwable throwable) {

    }

    public void deleteRecipient(final RecipientDto recipientDto) {
        final Disposable disposable = recipientsApi.deleteRecipient(tokenManager.getToken(), recipientDto.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.code() == HttpURLConnection.HTTP_OK && ui != null) {
                        ui.deleteRecipient(recipientDto);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void addRecipient() {
        if (ui != null) {
            ui.addRecipient();
        }
    }

    public void putToFavorite(RecipientDto recipientDto) {
        if (sharedPreferences.getBoolean(recipientDto.getId(), false)) {
            return;
        }
        if (favoriteListRecipients.size() == 3) {
            sharedPreferences.edit().remove(favoriteListRecipients.get(0).getId()).apply();
            favoriteListRecipients.remove(0);
        }
        sharedPreferences.edit().putBoolean(recipientDto.getId(), true).apply();
        favoriteListRecipients.add(recipientDto);
        if (ui != null) {
            ui.updateFavoriteRecipients();
        }

    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
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
