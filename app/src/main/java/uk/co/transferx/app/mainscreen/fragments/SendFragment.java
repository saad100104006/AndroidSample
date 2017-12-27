package uk.co.transferx.app.mainscreen.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.mainscreen.presenters.SendFragmentPresenter;

/**
 * Created by sergey on 14.12.17.
 */

public class SendFragment extends BaseFragment implements SendFragmentPresenter.SendFragmentUI {

    private static final String CURRENCY_PICKER = "currency_picker";
    private View view;
    private final static String INITIAL_VALUE = "50";

    @Inject
    SendFragmentPresenter sendFragmentPresenter;


    @Override
    public String tagName() {
        return SendFragment.class.getSimpleName();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.send_fragment_layout, container, false);
           // AppCompatSpinner chooseDelivery = view.findViewById(R.id.delivery_method);
            view.findViewById(R.id.send_container).setOnClickListener(v -> hideKeyboard(v));
            final CardView recipient = view.findViewById(R.id.recipient_container);
            final ImageView countryFlagFirst = view.findViewById(R.id.country_flag_first);
            final ImageView countryFlagSecond = view.findViewById(R.id.country_flag_second);
            final TextView currencyCodeFirst = view.findViewById(R.id.first_currency);
            final TextView currencyCodeSecond = view.findViewById(R.id.second_currency);
            final EditText currencyAmountSender = view.findViewById(R.id.amount_sender);
            final EditText currencyAmountRecipient = view.findViewById(R.id.amount_recipient);
            recipient.setOnClickListener(view -> Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show());
            setUpInitialValue(currencyCodeFirst, countryFlagFirst, "GBP");
            setUpInitialValue(currencyCodeSecond, countryFlagSecond, "UGX");
            currencyAmountSender.setText(INITIAL_VALUE);
          //  currencyCodeFirst.setOnClickListener(v -> showCurrencyPicker((TextView) v, countryFlagFirst));
          //  currencyCodeSecond.setOnClickListener(v -> showCurrencyPicker((TextView) v, countryFlagSecond));
          //  ArrayAdapter<String> deliveryMethodAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, getResources().getStringArray(R.array.delivery_method));
          //  deliveryMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          //  chooseDelivery.setAdapter(deliveryMethodAdapter);
        }
        return view;

    }

    private void hideKeyboard(View view) {
        if(view instanceof EditText){
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setUpInitialValue(final TextView codeText, final ImageView flag, String currencyName) {
        ExtendedCurrency currency = ExtendedCurrency.getCurrencyByISO(currencyName);
        codeText.setText(currency.getCode());
        flag.setImageDrawable(ContextCompat.getDrawable(flag.getContext(), currency.getFlag()));
    }

    private void showCurrencyPicker(final TextView textView, final ImageView imageView) {
        final CurrencyPicker currencyPicker = CurrencyPicker.newInstance(getString(R.string.select_currency_title));
        currencyPicker.setListener((name, code, symbol, flagDrawableResID) -> {
            textView.setText(code);
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), flagDrawableResID));
            currencyPicker.dismiss();
        });
        currencyPicker.show(getActivity().getSupportFragmentManager(), CURRENCY_PICKER);
    }
}
