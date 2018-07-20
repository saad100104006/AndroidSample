package uk.co.transferx.app.mainscreen.presenters;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.TransactionApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.dto.TransactionDto;
import uk.co.transferx.app.pojo.Transaction;
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
    private List<Transaction> transactionDtos = new ArrayList<>();

    @Inject
    public ActivityFragmentPresenter(final RecipientRepository recipientRepository, final TransactionApi transactionApi, final TokenManager tokenManager) {
        this.recipientRepository = recipientRepository;
        this.transactionApi = transactionApi;
        this.tokenManager = tokenManager;
        //    generateMockData();
    }

   /* private void generateMockData() {
        transactionDtos.add(new TransactionDto(0, " ", "Ahmet Shakir", "GBP", new BigDecimal("50"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Tim Hardon", "GBP", new BigDecimal("100"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Steve Jobs", "GBP", new BigDecimal("1000"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Tom Bibik", "GBP", new BigDecimal("430"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Leonid Warik", "GBP", new BigDecimal("40"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Sergey Milewski", "GBP", new BigDecimal("50"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Kukumber Bibik", "GBP", new BigDecimal("430"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Kuropatka Warik", "GBP", new BigDecimal("40"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Piatka Bibik", "GBP", new BigDecimal("430"), 0, 0, ""));
        transactionDtos.add(new TransactionDto(0, " ", "Kakosik Warik", "GBP", new BigDecimal("40"), 0, 0, ""));
    } */

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
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public void loadData() {

        compositeDisposable = new CompositeDisposable();
        final Disposable dis = recipientRepository.getRecipients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipients -> {
                    recipientDtos = recipients;
                    fetchHistory();
                }, this::handleError);
        compositeDisposable.add(dis);

    }

    private void fetchHistory() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        final Disposable historyDis = tokenManager.getToken()
                .flatMap(token -> transactionApi.getHistory(token.getAccessToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if(resp.code() == HttpsURLConnection.HTTP_OK && ui != null){
                        ui.setData(resp.body().getTransactions());
                        return;
                    }
                    handleError(new Throwable("Error " + resp.code()));

                });
        compositeDisposable.add(historyDis);
    }

    private void handleError(Throwable throwable) {
        Log.e(ActivityFragmentPresenter.class.getSimpleName(), "error", throwable);
        if (ui != null) {
            ui.setError();
        }
    }

    public interface ActivityFragmentUI extends UI {

        void setData(List<Transaction> transactions);

        void setError();

    }
}
