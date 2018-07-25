package uk.co.transferx.app.recipients.detailsrecipient.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.recipients.BaseRecipientFragment;
import uk.co.transferx.app.recipients.detailsrecipient.presenter.RecipientDetailsPresenter;
import uk.co.transferx.app.welcom.WelcomeActivity;

import static uk.co.transferx.app.recipients.detailsrecipient.RecipientDetailsActivity.RECIPIENT;

/**
 * Created by sergey on 10/01/2018.
 */

public class RecipientDetailsFragment extends BaseRecipientFragment implements RecipientDetailsPresenter.RecipientDetailsUI {

    @Inject
    RecipientDetailsPresenter presenter;


    @Override
    public String tagName() {
        return RecipientDetailsFragment.class.getSimpleName();
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
//        view.findViewById(R.id.edit_cont).setVisibility(View.VISIBLE);
//        ((TextView) view.findViewById(R.id.recipient_sub_title)).setText(R.string.edit_recipient);
//        view.findViewById(R.id.delete_recipient).setOnClickListener(v -> presenter.deleteRecipient());
//        view.findViewById(R.id.save_recipient).setOnClickListener(v -> presenter.validateInput(firstName.getEditText().getText().toString(),
//                lastName.getEditText().getText().toString(), country.getEditText().getText().toString(), phone.getEditText().getText().toString()));
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            presenter.setData(intent.getParcelableExtra(RECIPIENT));
        }
    }

    @Override
    public void goBackToListWithRefresh() {
        Activity activity = getActivity();
        activity.setResult(Activity.RESULT_OK);
        activity.finish();

    }

    @Override
    public void setData(RecipientDto recipientDto) {
        firstName.getEditText().setText(recipientDto.getFirstName());
        lastName.getEditText().setText(recipientDto.getLastName());
        country.getEditText().setText(recipientDto.getCountry());
        phone.getEditText().setText(recipientDto.getPhone());
    }

    @Override
    public void showErrorDelete() {
        Toast.makeText(getContext(), "Something went wrong, user wasn't deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {

        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    public void onPause() {
        hideKeyboard();
        presenter.detachUI();
        super.onPause();
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

    @Override
    public void showErrorSave() {
        Toast.makeText(getContext(), "Something went wrong, user wasn't updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goBackToList() {
        Activity activity = getActivity();
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    @Override
    public void goToWelcome() {
        WelcomeActivity.startWelcomeActivity(getActivity());
    }
}
