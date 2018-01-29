package uk.co.transferx.app.settings.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;

/**
 * Created by sergey on 26/01/2018.
 */

public class NotificationSettingsActivity extends BaseActivity implements NotificationSettingsPresenter.NotificationSettingsUI {

    @Inject
    NotificationSettingsPresenter presenter;

    private SwitchCompat newsSwitch, alertSwitch, notificationSwitch, paymentsSwitch;


    public static void startNotificationSettingsActivity(final Context context) {
        context.startActivity(new Intent(context, NotificationSettingsActivity.class));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TransferXApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.notification_settings_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        newsSwitch = findViewById(R.id.news_switch);
        alertSwitch = findViewById(R.id.alerts_switch);
        notificationSwitch = findViewById(R.id.all_notifications);
        paymentsSwitch = findViewById(R.id.payments_switch);
        newsSwitch.setChecked(presenter.isNewsSubscribed());
        alertSwitch.setChecked(presenter.isAlertSubscribed());
        paymentsSwitch.setChecked(presenter.isPaymentsSubscribed());
        notificationSwitch.setChecked(presenter.isNotificationSubscribed());
        newsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.setNewsSubscribtion(isChecked));
        alertSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.setAlertSubscribtion(isChecked));
        paymentsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.setPaymentsSubscribtion(isChecked));
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.setNotificationSubscribtion(isChecked));
        toolbar.setTitle(R.string.app_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    protected void onPause() {
        presenter.detachUI();
        super.onPause();
    }

    @Override
    public void setNews(boolean subscribed) {
        newsSwitch.setChecked(subscribed);
    }

    @Override
    public void setAlerts(boolean subscribed) {
        alertSwitch.setChecked(subscribed);

    }

    @Override
    public void setPayments(boolean subscribed) {
        paymentsSwitch.setChecked(subscribed);
    }

    @Override
    public void setNotificationOn() {
        notificationSwitch.setOnCheckedChangeListener(null);
        notificationSwitch.setChecked(true);
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.setNotificationSubscribtion(isChecked));
    }
}
