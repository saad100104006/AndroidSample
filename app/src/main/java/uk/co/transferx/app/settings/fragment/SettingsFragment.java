package uk.co.transferx.app.settings.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.settings.notification.NotificationSettingsActivity;
import uk.co.transferx.app.settings.presenter.SettingsFragmentPresenter;
import uk.co.transferx.app.settings.profile.ProfileActivity;
import uk.co.transferx.app.settings.support.SupportActivity;
import uk.co.transferx.app.welcom.WelcomeActivity;

/**
 * Created by sergey on 22/12/2017.
 */

public class SettingsFragment extends BaseFragment implements SettingsFragmentPresenter.SettingsFragmentUI {

    @Inject
    SettingsFragmentPresenter presenter;

    @Override
    public String tagName() {
        return SettingsFragment.class.getSimpleName();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.logout).setOnClickListener(v -> presenter.logOut());
        view.findViewById(R.id.notification).setOnClickListener(v -> presenter.clickNotification());
        view.findViewById(R.id.profile).setOnClickListener(v -> presenter.clickProfile());
        view.findViewById(R.id.support).setOnClickListener(v -> presenter.supportClicked());
        super.onViewCreated(view, savedInstanceState);
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
    public void goToWelcome() {
        WelcomeActivity.startWelcomeActivity(getActivity());
    }

    @Override
    public void goAppSettings() {
        NotificationSettingsActivity.startNotificationSettingsActivity(getContext());
    }

    @Override
    public void goToProfile() {
        ProfileActivity.startProfileActivity(getContext());
    }

    @Override
    public void goToSupport() {
        startActivity(new Intent(getContext(), SupportActivity.class));
    }
}
