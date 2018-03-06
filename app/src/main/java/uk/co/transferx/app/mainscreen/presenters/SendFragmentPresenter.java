package uk.co.transferx.app.mainscreen.presenters;

import java.math.BigDecimal;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.TransactionApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by sergey on 15.12.17.
 */

public class SendFragmentPresenter extends BasePresenter<SendFragmentPresenter.SendFragmentUI> {

    private RecipientDto recipientDto;
    private final TransactionApi transactionApi;
    private final TokenManager tokenManager;
    private BigDecimal rates;
    private Disposable disposable;
    private String currencyFrom;
    private String currencyTo;
    private BigDecimal valueToSend;
    private BigDecimal calculatedValue;
    private final static int SCALE_VALUE = 4;

    @Inject
    public SendFragmentPresenter(final TransactionApi transactionApi, final TokenManager tokenManager) {
        this.transactionApi = transactionApi;
        this.tokenManager = tokenManager;
    }


    @Override
    public void attachUI(SendFragmentUI ui) {
        super.attachUI(ui);
        if (rates == null && isShouldFetch()) {
            fetchRate();
        }
    }

    private void fetchRate() {
        if (currencyTo.equals(currencyFrom)) {
            return;
        }
        disposable = transactionApi.getRatest(tokenManager.getToken(), currencyFrom, currencyTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HTTP_OK && ui != null) {
                        rates = new BigDecimal(resp.body()).setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP);
                        ui.showRates(rates.toPlainString());
                        calculateValue();
                    }
                });
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null) {
            disposable.dispose();
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

    public void clickMainRecipient() {
        if (ui != null) {
            ui.showDialogRecipients();
        }
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
    }
}
