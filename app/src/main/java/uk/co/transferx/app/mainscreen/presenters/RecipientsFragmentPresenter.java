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
import uk.co.transferx.app.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragmentPresenter extends BasePresenter<RecipientsFragmentPresenter.RecipientsFragmentUI> {

    private boolean isShoulRefresh;
    private final RecipientRepository recipientRepository;
    private Disposable disposable;


    @Inject
    public RecipientsFragmentPresenter(final RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }


    public void setShouldRefresh(boolean isShouldRefresh) {
        this.isShoulRefresh = isShouldRefresh;
    }


    @Override
    public void attachUI(RecipientsFragmentUI ui) {
        super.attachUI(ui);
        if (isShoulRefresh) {
            disposable = recipientRepository.refreshRecipients()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (ui != null) {
                            ui.setRecipients(result);
                        }
                    }, this::handleError);
            isShoulRefresh = false;
            return;
        }
        disposable = recipientRepository.getRecipients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (ui != null) {
                        ui.setRecipients(result);
                    }
                }, this::handleError);
        isShoulRefresh = false;
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
