package uk.co.transferx.app.recipients;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;

/**
 * Created by sergey on 10/01/2018.
 */

public abstract class BaseRecipientFragment extends BaseFragment {

    protected View view;

    protected TextInputLayout firstName, lastName, country, phone;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            firstName.setError(null);
            lastName.setError(null);
            country.setError(null);
            phone.setError(null);
            firstName.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            lastName.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            country.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            phone.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.add_recipient_fragment_layout, container, false);
            firstName = view.findViewById(R.id.first_name);
            lastName = view.findViewById(R.id.last_name);
            country = view.findViewById(R.id.country);
            phone = view.findViewById(R.id.phone);
            firstName.getEditText().addTextChangedListener(textWatcher);
            lastName.getEditText().addTextChangedListener(textWatcher);
            country.getEditText().addTextChangedListener(textWatcher);
            phone.getEditText().addTextChangedListener(textWatcher);

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        firstName.getEditText().addTextChangedListener(textWatcher);
        lastName.getEditText().addTextChangedListener(textWatcher);
        country.getEditText().addTextChangedListener(textWatcher);
        phone.getEditText().addTextChangedListener(textWatcher);
    }

    @Override
    public void onPause() {
        firstName.getEditText().removeTextChangedListener(textWatcher);
        lastName.getEditText().removeTextChangedListener(textWatcher);
        country.getEditText().removeTextChangedListener(textWatcher);
        phone.getEditText().removeTextChangedListener(textWatcher);
        super.onPause();

    }
}
