package uk.co.transferx.app.mainscreen.presenters;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.recipientsrepository.RecipientRepository;

/**
 * Created by sergey on 15/01/2018.
 */

public class RecipientDialogFragmentPresenter extends BasePresenter<RecipientDialogFragmentPresenter.RecipientDialogFragmentUI> {

    private final RecipientRepository recipientRepository;
    private CompositeDisposable compositeDisposable;

    @Inject
    public RecipientDialogFragmentPresenter(final RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }


    @Override
    public void attachUI(RecipientDialogFragmentUI ui) {
        super.attachUI(ui);
        compositeDisposable = new CompositeDisposable();
        Disposable disposable = recipientRepository.getRecipients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (ui != null) {
                        ui.setRecipients(result);
                    }
                }, this::handleError);
        compositeDisposable.add(disposable);

    }

    public void searchRecipient(final String search) {
        Disposable searchDisposable = recipientRepository.getRecipients()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .filter(recipient -> recipient.getName().toLowerCase().contains(search.toLowerCase()))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (ui != null) {
                        ui.setRecipients(result);
                    }
                });
        compositeDisposable.add(searchDisposable);

    }

    private void handleError(Throwable throwable) {

    }


    @Override
    public void detachUI() {
        super.detachUI();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public interface RecipientDialogFragmentUI extends UI {

        void setRecipients(List<RecipientDto> recipients);
    }
}
