package uk.co.transferx.app.ui.mainscreen.presenters;

import android.content.SharedPreferences;
import android.util.Log;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;
import uk.co.transferx.app.data.dto.RecipientDto;
import uk.co.transferx.app.data.pojo.Card;
import uk.co.transferx.app.data.pojo.TransactionCreate;
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.data.repository.CardsRepository;
import uk.co.transferx.app.data.repository.ExchangeRateRepository;

import static uk.co.transferx.app.util.Util.formattedDateForSend;

/**
 * Created by sergey on 15.12.17.
 */

public class TransferFragmentPresenter extends BasePresenter<TransferFragmentPresenter.SendFragmentUI> {

    private RecipientDto recipientDto;
    private BigDecimal rates;
    private String currencyFrom = "GBP";
    private String currencyTo = "UGX";
    private BigDecimal valueToSend;
    private BigDecimal calculatedValue;
    private final static int SCALE_VALUE = 4;
    private final RecipientRepository recipientRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private CompositeDisposable compositeDisposable;
    private RecipientDto recipient;
    private Card card;
    private int amount;
    private String message;
    private boolean shouldRepeat;
    private final CardsRepository cardsRepository;
    private boolean isInitialized;

    @Inject
    public TransferFragmentPresenter(final RecipientRepository recipientRepository,
                                     final SharedPreferences sharedPreferences,
                                     final ExchangeRateRepository exchangeRateRepository,
                                     final CardsRepository cardsRepository) {
        super(sharedPreferences);
        this.recipientRepository = recipientRepository;
        compositeDisposable = new CompositeDisposable();
        this.exchangeRateRepository = exchangeRateRepository;
        this.cardsRepository = cardsRepository;
    }

    public void setPreviosStateCard(Card card) {
        if (card != null) {
            this.card = card;
        }
    }

    public void setPreviosStateRecipient(RecipientDto recipient) {
        if (recipient != null) {
            this.recipient = recipient;
        }
    }

    @Override
    public void attachUI(SendFragmentUI ui) {
        super.attachUI(ui);
        if (compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        if (!isInitialized || cardsRepository.shouldRefresh()) {
            setCards();
        }
        if (rates == null && isShouldFetch()) {
            fetchRate();
        }
        if (!isInitialized || recipientRepository.isShouldRefresh()) {
            compositeDisposable.add(recipientRepository.getRecipients()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(recip -> {
                        if (ui != null) {
                            ui.setRecipients(recip);
                        }
                    }, throwable -> Log.e(TransferFragmentPresenter.class.getSimpleName(), "Error ", throwable)));
        }
        this.ui.setButtonEnabled(isDataValid());

    }

    public void setInitialization(boolean isInitialized) {
        this.isInitialized = isInitialized;
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
        if (currencyTo.equals(currencyFrom)) {
            return;
        }
        Disposable disposable = exchangeRateRepository.getRates(currencyFrom, currencyTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (ui != null && !resp.getRates().isEmpty()) {
                        rates = new BigDecimal(resp.getRates().get(0).getRate()).setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
                        ui.showRates(String.format("%s %s = %s %s", "1", currencyFrom, rates.toPlainString(), currencyTo));
                        calculateValue();
                        return;
                    }
                    if(ui != null){
                        ui.showErrorRates();
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
                    recipient,
                    null,
                    null
            ));
        }
    }

    private void setCards() {
        compositeDisposable.add(cardsRepository.getCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cards ->
                        {
                            if (ui != null) {
                                ui.setCardToSpinner(cards);
                            }
                        }
                        , Timber::e));
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
                    recipient,
                    null,
                    null
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

        void showErrorRates();
    }
}
