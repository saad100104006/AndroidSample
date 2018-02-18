package uk.co.transferx.app.mainscreen.presenters;

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
import uk.co.transferx.app.api.TransactionApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.dto.TransactionDto;
import uk.co.transferx.app.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by sergey on 13/02/2018.
 */

public class ActivityFragmentPresenter extends BasePresenter<ActivityFragmentPresenter.ActivityFragmentUI> {

    private final RecipientRepository recipientRepository;
    private final TransactionApi transactionApi;
    private final TokenManager tokenManager;
    private CompositeDisposable compositeDisposable;
    private List<RecipientDto> recipientDtos;
    private List<TransactionDto> transactionDtos = new ArrayList<>();

    @Inject
    public ActivityFragmentPresenter(final RecipientRepository recipientRepository, final TransactionApi transactionApi, final TokenManager tokenManager) {
        this.recipientRepository = recipientRepository;
        this.transactionApi = transactionApi;
        this.tokenManager = tokenManager;
    }

    @Override
    public void attachUI(ActivityFragmentUI ui) {
        super.attachUI(ui);
        if (transactionDtos.isEmpty()) {
            loadData();
            return;
        }
        this.ui.setData(transactionDtos);

    }

    @Override
    public void detachUI() {
        super.detachUI();
        compositeDisposable.dispose();
        compositeDisposable = null;
    }

    public void loadData() {

        compositeDisposable = new CompositeDisposable();
        final Disposable dis = recipientRepository.getRecipients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipients -> {
                    recipientDtos = recipients;
                    fetchHistory();
                });
        compositeDisposable.add(dis);

    }

    private void fetchHistory() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        final Disposable dis = transactionApi.getHistory(tokenManager.getToken())
                .flatMap(trans -> Observable.fromIterable(trans.body()))
                .map(history -> new TransactionDto(history, recipientDtos))
                .doOnNext(trans -> {
                    if (!transactionDtos.contains(trans)) {
                        transactionDtos.add(trans);
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactionDtos -> {
                    if (ui != null) {
                        ui.setData(transactionDtos);
                    }
                }, throwable -> ui.setError());
        compositeDisposable.add(dis);

    }

    public interface ActivityFragmentUI extends UI {

        void setData(List<TransactionDto> transactionDtos);

        void setError();

    }
}
