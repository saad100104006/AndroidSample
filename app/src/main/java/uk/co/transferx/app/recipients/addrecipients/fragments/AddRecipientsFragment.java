package uk.co.transferx.app.recipients.addrecipients.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.recipients.addrecipients.Mode;
import uk.co.transferx.app.recipients.addrecipients.presenters.AddRecipientsPresenter;
import uk.co.transferx.app.view.ConfirmationDialogFragment;
import uk.co.transferx.app.view.CustomSpinner;
import uk.co.transferx.app.welcom.WelcomeActivity;

import static android.view.Gravity.CENTER;
import static uk.co.transferx.app.mainscreen.fragments.RecipientsFragment.DELETE_USER;
import static uk.co.transferx.app.util.Constants.MODE;
import static uk.co.transferx.app.util.Constants.RECIPIENT;
import static uk.co.transferx.app.view.ConfirmationDialogFragment.ADDITIONAL_DATA;
import static uk.co.transferx.app.view.ConfirmationDialogFragment.MESSAGE;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsFragment extends BaseFragment implements AddRecipientsPresenter.AddRecipientsUI {

    @Inject
    AddRecipientsPresenter presenter;
    private TextInputEditText firstName, lastName, phoneNumber;
    private CustomSpinner countrySpinner;
    private TextView titleText;
    private CompositeDisposable compositeDisposable;
    private Mode mode;
    private Button newTransfer, deleteRecipient;
    private boolean shouldButtonDisabled = true;

    @Override
    public String tagName() {
        return AddRecipientsFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null && bundle.getInt(MODE) != -1) {
            mode = Mode.values()[bundle.getInt(MODE)];
            presenter.setRecipient(bundle.getParcelable(RECIPIENT));
        } else {
            mode = Mode.NONE;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.add_recipient_fragment_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        buttonNext = view.findViewById(R.id.add_recipient_button);
        titleText = view.findViewById(R.id.title_recipient);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        phoneNumber = view.findViewById(R.id.phoneInput);
        newTransfer = view.findViewById(R.id.transfer_button);
        deleteRecipient = view.findViewById(R.id.delete_recipient);
        view.findViewById(R.id.button_back).setOnClickListener(v -> getActivity().onBackPressed());
        countrySpinner.setOnItemSelectedListener((position, object) -> presenter.setCountry(object.toString()));
        countrySpinner.setDataWithHintItem(getResources().getStringArray(R.array.countries), getString(R.string.choose_country));
        if (mode == Mode.EDIT) {
            setUpView();
            return;
        }
        shouldButtonDisabled = false;
        buttonNext.setOnClickListener(v -> presenter.saveUserToApi());
    }

    private void setUpView() {
        titleText.setGravity(CENTER);
        setEnabled(false);
        setButtonStatus(true);
        buttonNext.setText(R.string.edit);
        newTransfer.setVisibility(View.VISIBLE);
        deleteRecipient.setVisibility(View.VISIBLE);
        buttonNext.setOnClickListener(v -> setEditMode());
        deleteRecipient.setOnClickListener(v -> showDialogConfirmation(presenter.getRecipient()));
        newTransfer.setOnClickListener(v -> presenter.sendTransfer());
    }

    private void setEditMode() {
        shouldButtonDisabled = false;
        setEnabled(true);
        newTransfer.setVisibility(View.GONE);
        deleteRecipient.setVisibility(View.GONE);
        buttonNext.setText(R.string.save_changes);
        setButtonStatus(false);
        buttonNext.setOnClickListener(v -> presenter.refreshUserData());
    }

    private void setEnabled(boolean enabled) {
        countrySpinner.setEnabled(enabled);
        firstName.setEnabled(enabled);
        lastName.setEnabled(enabled);
        phoneNumber.setEnabled(enabled);
    }

    @Override
    public void onResume() {
        super.onResume();
        compositeDisposable = new CompositeDisposable();
        presenter.attachUI(this);
        compositeDisposable.add(RxTextView.textChanges(firstName)
                .subscribe(first -> presenter.setFirstName(first.toString())));
        compositeDisposable.add(RxTextView.textChanges(lastName)
                .subscribe(last -> presenter.setLastName(last.toString())));
        compositeDisposable.add(RxTextView.textChanges(phoneNumber)
                .subscribe(phone -> presenter.setPhone(phone.toString())));

    }

    private void showDialogConfirmation(RecipientDto recipientDto) {
        ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, getString(R.string.delete_user_message, recipientDto.getFullName()));
        bundle.putString(ADDITIONAL_DATA, recipientDto.getId());
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, DELETE_USER);
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(), "TAG");
    }

    @Override
    public void onPause() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        presenter.detachUI();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_USER) {
            final String id = data.getStringExtra(ADDITIONAL_DATA);
            presenter.deleteRecipient(id);
        }
    }

    @Override
    public void userActionPerformed() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "Upss something went wrong", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setEnabledButton(boolean enabled) {
        if (shouldButtonDisabled) {
            return;
        }
        setButtonStatus(enabled);
    }

    private int getItemPosition(String country) {
        String countries[] = getResources().getStringArray(R.array.countries);
        for (int i = 0; i < countries.length; i++) {
            if (countries[i].equals(country)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setData(RecipientDto recipientDto) {
        titleText.setText(recipientDto.getFullName());
        firstName.setText(recipientDto.getFirstName());
        lastName.setText(recipientDto.getLastName());
        phoneNumber.setText(recipientDto.getPhone());
        int position = getItemPosition(recipientDto.getCountry());
        countrySpinner.getAdapter().setItemSelected(position);
        countrySpinner.setSelection(position);
    }

    @Override
    public void sendTransfer(RecipientDto recipientDto) {
        final Intent intent = new Intent();
        intent.putExtra(RECIPIENT, recipientDto);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void goToWelcome() {
        WelcomeActivity.startWelcomeActivity(getActivity());
    }
}
