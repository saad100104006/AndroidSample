package uk.co.transferx.app.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.signin.SignInActivity;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.splash.presenter.SplashPresenter;

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
    public void goToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    @Override
    public void goToSignIn() {

        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}
