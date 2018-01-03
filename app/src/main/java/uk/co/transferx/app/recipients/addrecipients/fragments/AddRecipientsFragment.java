package uk.co.transferx.app.recipients.addrecipients.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.recipients.addrecipients.presenters.AddRecipientsPresenter;

import static uk.co.transferx.app.splash.SplashActivity.INITIAL_TOKEN;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsFragment extends BaseFragment implements AddRecipientsPresenter.AddRecipientsUI {

    private View view;

    @Inject
    AddRecipientsPresenter presenter;

    @Inject
    SharedPreferences sharedPreferences;

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
            final TextInputLayout firstName = view.findViewById(R.id.first_name);
            final TextInputLayout lastName = view.findViewById(R.id.last_name);
            final TextInputLayout country = view.findViewById(R.id.country);
            final TextInputLayout phone = view.findViewById(R.id.phone);
            view.findViewById(R.id.save_button).setOnClickListener(v -> presenter.saveUserToApi(firstName.getEditText().getText().toString(), lastName.getEditText().getText().toString(),
                    country.getEditText().getText().toString(), phone.getEditText().getText().toString(), sharedPreferences.getString(INITIAL_TOKEN, null)));

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
        getActivity().finish();
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "Upss shit happens", Toast.LENGTH_SHORT).show();
    }
}
