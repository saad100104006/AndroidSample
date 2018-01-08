package uk.co.transferx.app.recipients.addrecipients.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.recipients.addrecipients.presenters.AddRecipientsPresenter;

import static uk.co.transferx.app.mainscreen.fragments.RecipientsFragment.IS_SHOULD_REFRESH;
import static uk.co.transferx.app.recipients.addrecipients.AddRecipientsActivity.ADD_RECIPIENT;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsFragment extends BaseFragment implements AddRecipientsPresenter.AddRecipientsUI {

    private View view;

    @Inject
    AddRecipientsPresenter presenter;

    private TextInputLayout firstName, lastName, country, phone;

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
            firstName.getEditText().setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            lastName.getEditText().setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            country.getEditText().setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            phone.getEditText().setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public String tagName() {
        return AddRecipientsFragment.class.getSimpleName();
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
            view = inflater.inflate(R.layout.add_recipient_fragment_layout, container, false);
            firstName = view.findViewById(R.id.first_name);
            lastName = view.findViewById(R.id.last_name);
            country = view.findViewById(R.id.country);
            phone = view.findViewById(R.id.phone);
            firstName.getEditText().addTextChangedListener(textWatcher);
            lastName.getEditText().addTextChangedListener(textWatcher);
            country.getEditText().addTextChangedListener(textWatcher);
            phone.getEditText().addTextChangedListener(textWatcher);
            view.findViewById(R.id.save_button).setOnClickListener(v -> presenter.validateInput(firstName.getEditText().getText().toString(), lastName.getEditText().getText().toString(),
                    country.getEditText().getText().toString(), phone.getEditText().getText().toString()));

        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        super.onPause();
    }

    @Override
    public void userWasAdded() {
        final Intent intent = new Intent();
        intent.putExtra(IS_SHOULD_REFRESH, true);
        getActivity().setResult(ADD_RECIPIENT, intent);
        getActivity().finish();
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "Upss something went wrong", Toast.LENGTH_LONG).show();
    }


    @Override
    public void nameError() {
        firstName.setError(" ");
        firstName.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        Toast.makeText(getContext(), getString(R.string.first_name_error), Toast.LENGTH_LONG).show();

    }

    @Override
    public void lastNameError() {
        lastName.setError(" ");
        lastName.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        Toast.makeText(getContext(), getString(R.string.last_name_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void countryError() {
        country.setError(" ");
        country.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        Toast.makeText(getContext(), getString(R.string.country_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void phoneError() {
        phone.setError(" ");
        phone.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        Toast.makeText(getContext(), getString(R.string.phone_error), Toast.LENGTH_LONG).show();
    }
}
