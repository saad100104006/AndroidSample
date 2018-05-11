package uk.co.transferx.app.recipients.addrecipients.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.recipients.addrecipients.presenters.AddRecipientsPresenter;
import uk.co.transferx.app.view.CustomSpinner;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsFragment extends BaseFragment implements AddRecipientsPresenter.AddRecipientsUI {

    @Inject
    AddRecipientsPresenter presenter;
    private TextInputEditText firstName, lastName, phoneNumber;
    private CustomSpinner countrySpinner;
    private CompositeDisposable compositeDisposable;

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
        return inflater.inflate(R.layout.add_recipient_fragment_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        buttonNext = view.findViewById(R.id.add_recipient_button);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        phoneNumber = view.findViewById(R.id.phoneInput);
        buttonNext.setOnClickListener(v -> presenter.saveUserToApi());
        countrySpinner.setOnItemSelectedListener((position, object) -> {
            presenter.setCountry(object.toString());
        });
        countrySpinner.setDataWithHintItem(getResources().getStringArray(R.array.countries), getString(R.string.choose_country));
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

    @Override
    public void onPause() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
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
    public void setEnabledButton(boolean enabled) {
        setButtonStatus(enabled);
    }
}
