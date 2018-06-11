package uk.co.transferx.app.mainscreen.presenters;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.CardsApi;
import uk.co.transferx.app.api.TransactionApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.pojo.Card;
import uk.co.transferx.app.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static java.net.HttpURLConnection.HTTP_OK;
import static uk.co.transferx.app.util.Constants.CARDS;
import static uk.co.transferx.app.util.Constants.EMPTY;

/**
 * Created by sergey on 15.12.17.
 */

public class TransferFragmentPresenter extends BasePresenter<TransferFragmentPresenter.SendFragmentUI> {

    private RecipientDto recipientDto;
    private final TransactionApi transactionApi;
    private final TokenManager tokenManager;
    private BigDecimal rates;
    private String currencyFrom = "GBP";
    private String currencyTo = "UGX";
    private BigDecimal valueToSend;
    private BigDecimal calculatedValue;
    private final static int SCALE_VALUE = 4;
    private final RecipientRepository recipientRepository;
    private List<RecipientDto> recipientDtos = new ArrayList<>();
    private CompositeDisposable compositeDisposable;
    private final SharedPreferences sharedPreferences;
    private List<String> cards;
    private final CardsApi cardsApi;

    @Inject
    public TransferFragmentPresenter(final TransactionApi transactionApi, final TokenManager tokenManager, final RecipientRepository recipientRepository,
                                     final SharedPreferences sharedPreferences, final CardsApi cardsApi) {
        this.transactionApi = transactionApi;
        this.tokenManager = tokenManager;
        this.recipientRepository = recipientRepository;
        this.sharedPreferences = sharedPreferences;
        compositeDisposable = new CompositeDisposable();
        this.cardsApi = cardsApi;
    }


    @Override
    public void attachUI(SendFragmentUI ui) {
        super.attachUI(ui);
        if (compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        setCards();
        if (rates == null && isShouldFetch()) {
            fetchRate();
        }
        if (recipientDtos.isEmpty()) {
            compositeDisposable.add(recipientRepository.getRecipients()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(recip -> {
                        recipientDtos.addAll(recip);
                        if (ui != null) {
                            ui.setRecipients(recipientDtos);
                        }
                    }, throwable -> Log.e(TransferFragmentPresenter.class.getSimpleName(), "Error ", throwable)));
            return;
        }
        ui.setRecipients(recipientDtos);
    }

    private void fetchRate() {
        Log.d("Serge", "currencyTo " + currencyTo + " currency from " + currencyFrom);
        if (currencyTo.equals(currencyFrom)) {
            return;
        }
        Disposable disposable = transactionApi.getRats(tokenManager.getToken(), currencyFrom, currencyTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HTTP_OK && ui != null) {
                        rates = new BigDecimal(resp.body().getRates().get(0).getRate()).setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
                        Log.d("Serge", "rate " + rates.toPlainString());
                        ui.showRates(String.format("%s %s = %s %s", "1", currencyFrom, rates.toPlainString(), currencyTo));
                        calculateValue();
                    }
                }, throwable -> Log.e(TransferFragmentPresenter.class.getSimpleName(), "Error ", throwable));
        compositeDisposable.add(disposable);
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

    }

    private boolean isShouldFetch() {
        return ui != null && currencyFrom != null && currencyTo != null;

    }

    public void setCurrencyFrom(String currencyFrom) {
        if (currencyFrom.equals(this.currencyFrom)) {
            return;
        }
        this.currencyFrom = currencyFrom;
        if (isShouldFetch()) {
            fetchRate();
        }
    }

    public void setCurrencyTo(String currencyTo) {
        if (currencyTo.equals(this.currencyTo)) {
            return;
        }
        this.currencyTo = currencyTo;
        if (isShouldFetch()) {
            fetchRate();
        }
    }

    private void setCards() {
        compositeDisposable.add(cardsApi.getCards(tokenManager.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cards ->
                        {
                            if (ui != null) {
                                ui.setCardToSpinner(cards.getCards());
                            }
                        }
                ));
    }

    public void setValueToSend(String value) {
        valueToSend = new BigDecimal(value).setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
        calculateValue();
    }

    private void calculateValue() {
        if (isCalculate()) {
            calculatedValue = valueToSend.multiply(rates).setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
        }
        if (ui != null && calculatedValue != null) {
            ui.setCalculatedValueForTransfer(calculatedValue.toPlainString());
        }

    }

    private boolean isCalculate() {
        return rates != null && valueToSend != null;
    }

    public void setRecipient(RecipientDto recipient) {
        recipientDto = recipient;
        if (ui != null) {
            ui.showChoosenRecipient(recipientDto);
        }
    }


    public interface SendFragmentUI extends UI {

        void showRates(String rates);

        void showDialogRecipients();

        void showChoosenRecipient(RecipientDto recipientDto);

        void setCalculatedValueForTransfer(String value);

        void setRecipients(List<RecipientDto> recipients);

        void setCardToSpinner(List<Card> cards);
    }
}
