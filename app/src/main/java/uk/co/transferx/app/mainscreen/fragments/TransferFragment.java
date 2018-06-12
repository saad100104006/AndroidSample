package uk.co.transferx.app.mainscreen.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.mainscreen.presenters.TransferFragmentPresenter;
import uk.co.transferx.app.mainscreen.schedule.ScheduleActivity;
import uk.co.transferx.app.pojo.Card;
import uk.co.transferx.app.pojo.TransactionCreate;
import uk.co.transferx.app.transfersummary.TransferSummaryActivity;
import uk.co.transferx.app.view.CustomSpinner;

import static uk.co.transferx.app.util.Constants.EMPTY;
import static uk.co.transferx.app.util.Constants.TRANSACTION;

/**
 * Created by sergey on 14.12.17.
 */

public class TransferFragment extends BaseFragment implements TransferFragmentPresenter.SendFragmentUI {

    private static final String CURRENCY_PICKER = "currency_picker";
    private View view;
    private final static String INITIAL_VALUE = "50";
    public final static int REQUEST_RECIPIENT = 321;
    public static final String RECIPIENT = "recipient";
    private ImageView photo;
    private TextView name, rate, country, currencyCodeFirst, currencyCodeSecond, calculatedValue;
    private RecipientDialogFragment recipientDialogFragment;
    private final static String GBP = "GBP";
    private final static String UGX = "UGX";
    private List<ExtendedCurrency> currencyList;
    private EditText sendInput, messageInput;
    private Disposable disposable;
    private Disposable messageDisposable;
    private CustomSpinner recipientSpinner, paymentMethod;
    private Pattern pattern = Pattern.compile("^(\\d+\\.)?\\d+$");
    private Button sendNowButton, sendLaterButton;

    @Inject
    TransferFragmentPresenter presenter;


    @Override
    public String tagName() {
        return TransferFragment.class.getSimpleName();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    /*    if (savedInstanceState != null && view != null) {
            presenter.setCurrencyFrom(currencyCodeFirst.getText().toString());
            presenter.setCurrencyTo(currencyCodeSecond.getText().toString());
        } */
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
        disposable = RxTextView.textChanges(sendInput)
                .debounce(300L, TimeUnit.MILLISECONDS)
                .filter(val -> !EMPTY.equals(val.toString()))
                .filter(value -> pattern.matcher(value.toString()).matches())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sequence -> presenter.setValueToSend(sequence.toString()));
        messageDisposable = RxTextView.textChanges(messageInput)
                .debounce(100L, TimeUnit.MILLISECONDS)
                .filter(val -> !EMPTY.equals(val.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sequence -> presenter.setMessage(sequence.toString()));
    }


    @Override
    public void onPause() {
        presenter.detachUI();
        if (disposable != null) {
            disposable.dispose();
        }
        if (messageDisposable != null) {
            messageDisposable.dispose();
        }
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      /*  if (view == null) {
            view = inflater.inflate(R.layout.transfer_fragment_layout, container, false);
            // AppCompatSpinner chooseDelivery = view.findViewById(R.id.delivery_method);
            view.findViewById(R.id.send_container).setOnClickListener(v -> {
                if (v instanceof EditText) {
                    return;
                }
                hideKeyboard(v);}
            );
            final CardView recipient = view.findViewById(R.id.recipient_container);
            final ImageView countryFlagFirst = view.findViewById(R.id.country_flag_first);
            final ImageView countryFlagSecond = view.findViewById(R.id.country_flag_second);
            currencyCodeFirst = view.findViewById(R.id.first_currency);
            currencyCodeSecond = view.findViewById(R.id.second_currency);
            currencyAmountSender = view.findViewById(R.id.amount_sender);
            currencyAmountRecipient = view.findViewById(R.id.amount_recipient);
            photo = view.findViewById(R.id.photo);
            name = view.findViewById(R.id.name);
            country = view.findViewById(R.id.country);
            rate = view.findViewById(R.id.rate_value);
            recipient.setOnClickListener(view -> presenter.clickMainRecipient());
            setUpInitialValue(currencyCodeFirst, countryFlagFirst, GBP);
            presenter.setCurrencyFrom(GBP);
            setUpInitialValue(currencyCodeSecond, countryFlagSecond, UGX);
            presenter.setCurrencyTo(UGX);
            currencyAmountSender.setText(INITIAL_VALUE);
            presenter.setValueToSend(INITIAL_VALUE);
            currencyCodeFirst.setOnClickListener(v -> showCurrencyPicker((TextView) v, countryFlagFirst));
            currencyCodeSecond.setOnClickListener(v -> showCurrencyPicker((TextView) v, countryFlagSecond));
            //  ArrayAdapter<String> deliveryMethodAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, getResources().getStringArray(R.array.delivery_method));
            //  deliveryMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //  chooseDelivery.setAdapter(deliveryMethodAdapter);
        } */
        return inflater.inflate(R.layout.transfer_fragment_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rate = view.findViewById(R.id.exchange_rate);
        messageInput = view.findViewById(R.id.messageInput);
        sendInput = view.findViewById(R.id.sendInput);
        calculatedValue = view.findViewById(R.id.receiveInput);
        paymentMethod = view.findViewById(R.id.paymentCard);
        sendNowButton = view.findViewById(R.id.sendNow);
        sendLaterButton = view.findViewById(R.id.sendLater);
        sendNowButton.setOnClickListener(v -> presenter.goToTrasferSummary());
        sendLaterButton.setOnClickListener(v -> startActivity(new Intent(getContext(), ScheduleActivity.class)));
        paymentMethod.setDataWithHintItem(getResources().getStringArray(R.array.payment_method), getString(R.string.select_a_payment_method));
        paymentMethod.setOnItemSelectedListener((position, object) -> {
            presenter.setCard((Card) object);
        });
        recipientSpinner = view.findViewById(R.id.SendToRecipient);
        recipientSpinner.setOnItemSelectedListener((position, object) -> {
            presenter.chooseRecipientForTransfer((RecipientDto) object);
        });
    }

    @Override
    public void setRecipients(List<RecipientDto> recipients) {
        if (getActivity() != null && isAdded()) {
            recipientSpinner.setDataWithHintItem(recipients.toArray(), getString(R.string.recipient));
        }
    }

    @Override
    public void showDialogRecipients() {
        recipientDialogFragment = RecipientDialogFragment.newInstance();
        recipientDialogFragment.setTargetFragment(this, REQUEST_RECIPIENT);
        recipientDialogFragment.show(getFragmentManager(), RecipientDialogFragment.class.getSimpleName());
    }

    @Override
    public void showChoosenRecipient(RecipientDto recipientDto) {
        //  GlideApp.with(this)
        //           .load(recipientDto.getImgUrl())
        //           .placeholder(R.drawable.placeholder)
        //            .into(photo);
        name.setText(recipientDto.getFullName());
        country.setText(recipientDto.getCountry());
    }

    private void setUpInitialValue(final TextView codeText, final ImageView flag, String currencyName) {
        ExtendedCurrency currency = ExtendedCurrency.getCurrencyByISO(currencyName);
        codeText.setText(currency.getCode());
        flag.setImageDrawable(ContextCompat.getDrawable(flag.getContext(), currency.getFlag()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECIPIENT && resultCode == Activity.RESULT_OK) {
            if (recipientDialogFragment != null) {
                recipientDialogFragment.dismiss();
            }
            presenter.setRecipient(data.getParcelableExtra(RECIPIENT));
        }
    }

    private void showCurrencyPicker(final TextView textView, final ImageView imageView) {
        final CurrencyPicker currencyPicker = CurrencyPicker.newInstance(getString(R.string.select_currency_title));
        if (currencyList == null) {
            currencyList = new ArrayList<>(2);
            currencyList.add(ExtendedCurrency.getCurrencyByISO(GBP));
            currencyList.add(ExtendedCurrency.getCurrencyByISO(UGX));
        }
        currencyPicker.setCurrenciesList(currencyList);
        currencyPicker.setListener((name, code, symbol, flagDrawableResID) -> {
            if (textView.equals(currencyCodeFirst)) {
                presenter.setCurrencyFrom(code);
            } else {
                presenter.setCurrencyTo(code);
            }
            textView.setText(code);
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), flagDrawableResID));
            currencyPicker.dismiss();
        });
        currencyPicker.show(getActivity().getSupportFragmentManager(), CURRENCY_PICKER);
    }

    @Override
    public void setCardToSpinner(List<Card> cards) {
        paymentMethod.setDataWithHintItem(cards.toArray(), getString(R.string.select_a_payment_method));
    }

    @Override
    public void showRates(String rates) {
        rate.setText(rates);
    }

    @Override
    public void setCalculatedValueForTransfer(String value) {
        calculatedValue.setText(value);
    }

    @Override
    public void setButtonEnabled(boolean isEnabled) {
        sendNowButton.setEnabled(isEnabled);
        sendNowButton.setBackground(isEnabled ?
                ContextCompat.getDrawable(getContext(), R.drawable.oval_button_white) :
                ContextCompat.getDrawable(getContext(), R.drawable.oval_button_gray));
    }

    @Override
    public void sendNowClick(TransactionCreate transactionCreate) {
        final Intent intent = new Intent(getContext(), TransferSummaryActivity.class);
        intent.putExtra(TRANSACTION, transactionCreate);
        startActivity(intent);
    }
}
