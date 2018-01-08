package uk.co.transferx.app.mainscreen.presenters;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragmentPresenter extends BasePresenter<RecipientsFragmentPresenter.RecipientsFragmentUI> {

    private boolean isInitialized;
    private boolean isShoulRefresh;
    private final RecipientsApi recipientsApi;
    private final TokenManager tokenManager;
    private Disposable disposable;
    private List<RecipientDto> recipientDtos;



    @Inject
    public RecipientsFragmentPresenter(final RecipientsApi recipientsApi, TokenManager tokenManager) {
        this.recipientsApi = recipientsApi;
        this.tokenManager = tokenManager;
    }


    public void setShouldRefresh(boolean isShouldRefresh){
        this.isShoulRefresh = isShouldRefresh;
    }



    @Override
    public void attachUI(RecipientsFragmentUI ui) {
        super.attachUI(ui);
        if (!isInitialized || isShoulRefresh) {
            disposable = recipientsApi.getRecipients(tokenManager.getToken())
                    .flatMap(resp -> Observable.fromIterable(resp.body()))
                    .map(RecipientDto::new)
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (ui != null) {
                            ui.setRecipients(result);
                        }
                    }, this::handleError);
            isInitialized = true;
            isShoulRefresh = false;
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

        void setRecipients(List<RecipientDto> recipientDtos);

        void showError();

    }
}
