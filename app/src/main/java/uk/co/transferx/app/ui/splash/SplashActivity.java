package uk.co.transferx.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.ui.landing.LandingActivity;
import uk.co.transferx.app.ui.signin.SignInActivity;
import uk.co.transferx.app.ui.splash.presenter.SplashPresenter;
import uk.co.transferx.app.ui.welcom.WelcomeActivity;

/**
 * Created by sergey on 19.11.17.
 */

public class SplashActivity extends BaseActivity implements SplashPresenter.SplashUI {

    @Inject
    SplashPresenter presenter;

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
    public void goToWelcomeScreen() {
        // TEMPORARY LINK TO NEW LANDING ACTIVITY
        // TODO - REFACTOR LINK WHEN LINKING IS PERFORMED
        Intent intent = new Intent(this, LandingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToPinScreen() {
        SignInActivity.starSignInActivity(SplashActivity.this);
        finish();
    }

    @Override
    public void goToWelcome() {
        //no op
    }
}
