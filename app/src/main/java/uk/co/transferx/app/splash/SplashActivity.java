package uk.co.transferx.app.splash;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.splash.presenter.SplashPresenter;
import uk.co.transferx.app.welcom.WelcomeActivity;

/**
 * Created by sergey on 19.11.17.
 */

public class SplashActivity extends BaseActivity implements SplashPresenter.SplashUI {

    @Inject
    SplashPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;

    public final static String INITIAL_TOKEN = "initial_token";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TransferXApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.splash_activity_layout);

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
    public void goToWelcomeScreen(String token) {
        sharedPreferences.edit().putString(INITIAL_TOKEN, token).apply();
        WelcomeActivity.startWelcomeActivity(this);
        finish();
    }


}
