package uk.co.transferx.app.mainscreen.presenters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.dto.RecipientDto;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragmentPresenter extends BasePresenter<RecipientsFragmentPresenter.RecipientsFragmentUI> {

    private boolean isInitialized;
    private final RecipientsApi recipientsApi;
    private Disposable disposable;
    private List<RecipientDto> recipientDtos;
    private String token;


    @Inject
    public RecipientsFragmentPresenter(final RecipientsApi recipientsApi) {
        this.recipientsApi = recipientsApi;
    }


    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void attachUI(RecipientsFragmentUI ui) {
        super.attachUI(ui);
        if (!isInitialized && token != null) {
            disposable = recipientsApi.getRecipients(token)
                    .flatMap(resp -> Observable.fromIterable(resp.body()))
                    .map(RecipientDto::new)
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (ui != null) {
                            ui.setFavoriteRecipients(result);
                        }
                    }, this::handleError);
            isInitialized = true;
        }
    }

    private void handleError(Throwable throwable) {

    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    public interface RecipientsFragmentUI extends UI {

        void setFavoriteRecipients(List<RecipientDto> recipientDtos);

        void showError();

    }
}
