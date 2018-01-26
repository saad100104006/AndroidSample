package uk.co.transferx.app.settings.fragment;

import android.os.Bundle;
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
import uk.co.transferx.app.welcom.WelcomeActivity;

/**
 * Created by sergey on 22/12/2017.
 */

public class SettingsFragment extends BaseFragment implements SettingsFragmentPresenter.SettingsFragmentUI {


    private View view;


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
        if (view == null) {
            view = inflater.inflate(R.layout.settings_fragment_layout, container, false);
            view.findViewById(R.id.logout).setOnClickListener(view -> presenter.logOut());
            view.findViewById(R.id.app_settings).setOnClickListener(view -> presenter.clickAppSettings());

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
    public void goToWelcome() {
        WelcomeActivity.startWelcomeActivity(getActivity());
    }

    @Override
    public void goAppSettings() {
        NotificationSettingsActivity.startNotificationSettingsActivity(getContext());
    }
}
