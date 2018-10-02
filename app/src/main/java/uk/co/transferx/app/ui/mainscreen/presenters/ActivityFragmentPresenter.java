package uk.co.transferx.app.ui.mainscreen.presenters;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;
import uk.co.transferx.app.data.remote.TransactionApi;
import uk.co.transferx.app.data.dto.RecipientDto;
import uk.co.transferx.app.data.pojo.Card;
import uk.co.transferx.app.data.pojo.Transaction;
import uk.co.transferx.app.data.pojo.TransactionCreate;
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager;

import static uk.co.transferx.app.util.Constants.EMPTY;

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
    public ActivityFragmentPresenter(final RecipientRepository recipientRepository,
                                     final TransactionApi transactionApi,
                                     final TokenManager tokenManager,
                                     final SharedPreferences sharedPreferences) {
        super(sharedPreferences);
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
                }, this::globalErrorHandler);
        compositeDisposable.add(dis);

    }

    public void goToReceipt(Transaction transaction) {

        if (ui != null) {
            ui.goToRecieptScreen(new TransactionCreate(EMPTY, Integer.valueOf(transaction.getAmount()),
                    transaction.getCurrency(), transaction.getCurrency(), EMPTY, transaction.getMessage(),
                    true, null, transaction.getRepeat(), transaction.getStartTime(), transaction.getEndTime(),
                    transaction.getFrequency(), new Card(EMPTY, transaction.getMeta().getCardInfo().getName(),
                    transaction.getMeta().getCardInfo().getNumber(), transaction.getMeta().getCardInfo().getType(),
                    transaction.getMeta().getCardInfo().getExpDate(), null),
                    new RecipientDto(null, transaction.getMeta().getRecipientInfo().getFirstName(),
                            transaction.getMeta().getRecipientInfo().getLastName(),
                            transaction.getMeta().getRecipientInfo().getImgUrl(),
                            transaction.getMeta().getRecipientInfo().getCountry(),
                            transaction.getMeta().getRecipientInfo().getPhone()),
                    transaction.getId(),
                    transaction.getStatus()
            ));
        }
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
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        ui.setData(resp.body().getTransactions());
                        return;
                    }
                    globalErrorHandler(resp.code());

                });
        compositeDisposable.add(historyDis);
    }

    private void fetchRecurrent() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        final Disposable historyDis = tokenManager.getToken()
                .flatMap(token -> transactionApi.getHistory(token.getAccessToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        ui.setData(resp.body().getTransactions());
                        return;
                    }
                    globalErrorHandler(resp.code());

                });
        compositeDisposable.add(historyDis);
    }

    @Override
    protected void globalErrorHandler(Throwable throwable) {
        super.globalErrorHandler(throwable);
    }

    public interface ActivityFragmentUI extends UI {

        void setData(List<Transaction> transactions);

        void setError();

        void goToRecieptScreen(TransactionCreate transaction);

    }
}
