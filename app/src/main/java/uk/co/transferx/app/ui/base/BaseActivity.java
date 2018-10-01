package uk.co.transferx.app.ui.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import javax.inject.Inject;

import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.ui.recoverpass.RecoverPasswordActivity;
import uk.co.transferx.app.ui.signinpin.SignInPinActivity;
import uk.co.transferx.app.ui.signup.SignUpActivity;
import uk.co.transferx.app.ui.splash.SplashActivity;
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager;
import uk.co.transferx.app.data.repository.tokenmanager.TokenRepository;

import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;
import static uk.co.transferx.app.util.Constants.PIN_REQUIRED;

/**
 * Created by smilevkiy on 15.11.17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;
    protected int container;
    @Inject
    TokenManager tokenManager;
    @Inject
    TokenRepository tokenRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TransferXApplication) (getApplication())).getAppComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* TBDD
        if (isShouldStartPinCheck()) {

            SignInPinActivity.starSignInActivity(this);
        }
         */
    }

    private boolean isShouldStartPinCheck() {
        return sharedPreferences.getBoolean(PIN_REQUIRED, false) && !(this instanceof SignInPinActivity) &&
                tokenRepository.getToken().getRefreshToken() != null && sharedPreferences.getBoolean(LOGGED_IN_STATUS, false) &&
                !(this instanceof SignUpActivity) && !(this instanceof SplashActivity) && !(this instanceof RecoverPasswordActivity);
    }

    protected void replaceFragment(BaseFragment baseFragment, int current, int container) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (current != 0) {
            int animationEnter = (current > 0) ? R.animator.slide_right_enter : R.animator.slide_left_enter;
            int animationExit = (current > 0) ? R.animator.slide_right_exit : R.animator.slide_left_exit;
            fragmentTransaction.setCustomAnimations(animationEnter, animationExit, 0, 0);
        }
        fragmentTransaction.replace(container, baseFragment, baseFragment.getTag()).commit();
    }

    protected void setButtonStatus(Button button, boolean isEnabled) {
        button.setEnabled(isEnabled);
        button.setBackground(isEnabled ?
                ContextCompat.getDrawable(this, R.drawable.oval_button_black) :
                ContextCompat.getDrawable(this, R.drawable.oval_button_gray));
    }
}
