package uk.co.transferx.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import uk.co.transferx.app.signin.SignInActivity;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.tokenmanager.TokenRepository;

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
     /*   if (isShouldStartPinCheck()) {
            SignInActivity.starSignInActivity(this);
        } */
    }

    private boolean isShouldStartPinCheck() {
        return sharedPreferences.getBoolean(PIN_REQUIRED, false) && !(this instanceof SignInActivity) &&
                !tokenRepository.getToken().getAccessToken().isEmpty() && sharedPreferences.getBoolean(LOGGED_IN_STATUS,false) &&
                !(this instanceof SignUpActivity);
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
}
