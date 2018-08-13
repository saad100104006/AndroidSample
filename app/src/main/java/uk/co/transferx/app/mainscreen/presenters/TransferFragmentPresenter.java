package uk.co.transferx.app.mainscreen.presenters;

import android.content.SharedPreferences;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import uk.co.transferx.app.pojo.TransactionCreate;
import uk.co.transferx.app.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static java.net.HttpURLConnection.HTTP_OK;
import static uk.co.transferx.app.util.Util.formattedDateForSend;

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
    private RecipientDto recipient;
    private Card card;
    private int amount;
    private final CardsApi cardsApi;
    private String message;
    private boolean shouldRepeat;

    @Inject
    public TransferFragmentPresenter(final TransactionApi transactionApi,
                                     final TokenManager tokenManager,
                                     final RecipientRepository recipientRepository,
                                     final CardsApi cardsApi,
                                     final SharedPreferences sharedPreferences) {
        super(sharedPreferences);
        this.transactionApi = transactionApi;
        this.tokenManager = tokenManager;
        this.recipientRepository = recipientRepository;
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
                            setChoosetRecipient();

                        }
                    }, throwable -> Log.e(TransferFragmentPresenter.class.getSimpleName(), "Error ", throwable)));
            return;
        }
        this.ui.setRecipients(recipientDtos);
    }

    private void setChoosetRecipient() {
        if (recipient == null) {
            return;
        }
        ui.showChoosenRecipient(recipient, recipientDtos.indexOf(recipient));

    }

    private boolean isDataValid() {
        return recipient != null && card != null && amount > 0;
    }


    public void setCard(Card card) {
        this.card = card;
        if (ui != null) {
            ui.setButtonEnabled(isDataValid());
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void fetchRate() {
        Log.d("Serge", "currencyTo " + currencyTo + " currency from " + currencyFrom);
        if (currencyTo.equals(currencyFrom)) {
            return;
        }
        Disposable disposable = tokenManager.getToken()
                .flatMap(token -> transactionApi.getRats(token.getAccessToken(), currencyFrom, currencyTo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HTTP_OK && ui != null) {
                        rates = new BigDecimal(resp.body().getRates().get(0).getRate()).setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
                        Log.d("Serge", "rate " + rates.toPlainString());
                        ui.showRates(String.format("%s %s = %s %s", "1", currencyFrom, rates.toPlainString(), currencyTo));
                        calculateValue();
                    }
                }, this::globalErrorHandler);
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

    public void sendLaterClicked() {
        if (ui != null) {
            ui.goToScheduleScreen(new TransactionCreate(
                    recipient.getId(),
                    calculatedValue == null ? BigDecimal.ZERO.intValue() : calculatedValue.intValue(),
                    currencyTo,
                    currencyFrom,
                    card.getId(),
                    message,
                    false,
                    amount,
                    shouldRepeat,
                    null,
                    null,
                    null,
                    card,
                    recipient
            ));
        }
    }

    private void setCards() {
        compositeDisposable.add(tokenManager.getToken()
                .flatMap(token -> cardsApi.getCards(token.getAccessToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cards ->
                        {
                            if (ui != null) {
                                ui.setCardToSpinner(cards.getCards());
                            }
                        }
                        , throwable -> Log.e(TransferFragmentPresenter.class.getSimpleName(), "error ", throwable)));
    }

    public void setValueToSend(String value) {
        valueToSend = new BigDecimal(value).setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
        if (ui != null) {
            amount = valueToSend.intValue();
            ui.setButtonEnabled(isDataValid());
        }
        calculateValue();
    }

    public void setRepeatStatus(boolean shouldRepeat) {
        this.shouldRepeat = shouldRepeat;
    }

    private void calculateValue() {
        if (isCalculate()) {
            calculatedValue = valueToSend.multiply(rates).setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
        }
        if (ui != null && calculatedValue != null) {
            ui.setCalculatedValueForTransfer(calculatedValue.toPlainString());
        }
    }

    public void chooseRecipientForTransfer(RecipientDto recipientDto) {
        recipient = recipientDto;
    }


    private boolean isCalculate() {
        return rates != null && valueToSend != null;
    }

    public void setRecipient(RecipientDto recipient) {
        recipientDto = recipient;
    }

    public void goToNextScreen() {
        if (ui != null) {
            TransactionCreate transactionCreate = new TransactionCreate(
                    recipient.getId(),
                    calculatedValue == null ? BigDecimal.ZERO.intValue() : calculatedValue.intValue(),
                    currencyTo,
                    currencyFrom,
                    card.getId(),
                    message,
                    true,
                    amount,
                    shouldRepeat,
                    formattedDateForSend(new Date()),
                    null,
                    null,
                    card,
                    recipient
            );
            if (shouldRepeat) {
                ui.sendWithRepeat(transactionCreate);
                return;
            }
            ui.sendNowClick(transactionCreate);
        }
    }


    public interface SendFragmentUI extends UI {

        void showRates(String rates);

        void showDialogRecipients();

        void showChoosenRecipient(RecipientDto recipientDto, int position);

        void setCalculatedValueForTransfer(String value);

        void setRecipients(List<RecipientDto> recipients);

        void setCardToSpinner(List<Card> cards);

        void setButtonEnabled(boolean isEnabled);

        void sendNowClick(TransactionCreate transactionCreate);

        void sendWithRepeat(TransactionCreate transactionCreate);

        void goToScheduleScreen(TransactionCreate transactionCreate);
    }
}
