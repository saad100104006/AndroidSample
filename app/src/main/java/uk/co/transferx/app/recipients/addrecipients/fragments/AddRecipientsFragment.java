package uk.co.transferx.app.recipients.addrecipients.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.recipients.BaseRecipientFragment;
import uk.co.transferx.app.recipients.addrecipients.presenters.AddRecipientsPresenter;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsFragment extends BaseRecipientFragment implements AddRecipientsPresenter.AddRecipientsUI {


    @Inject
    AddRecipientsPresenter presenter;


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
        super.onCreateView(inflater, container, savedInstanceState);
        final Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setVisibility(View.VISIBLE);
        saveButton.setOnClickListener(v -> presenter.validateInput(firstName.getEditText().getText().toString(), lastName.getEditText().getText().toString(),
                country.getEditText().getText().toString(), phone.getEditText().getText().toString()));

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
        getActivity().setResult(Activity.RESULT_OK);
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
