package uk.co.transferx.app.signup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import com.rd.PageIndicatorView;

import javax.inject.Inject;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.pojo.Recipient;
import uk.co.transferx.app.signup.fragment.SignUpStepOneFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepThreeFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepTwoFragment;
import uk.co.transferx.app.welcom.WelcomeActivity;

import static uk.co.transferx.app.util.Constants.CREDENTIAL;
import static uk.co.transferx.app.util.Constants.EMAIL;
import static uk.co.transferx.app.util.Constants.PASSWORD;
import static uk.co.transferx.app.util.Constants.PIN_SHOULD_BE_INPUT;
import static uk.co.transferx.app.util.Constants.U_NAME;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpActivity extends BaseActivity {

    public static void startSignUp(Activity activity, Bundle bundle) {
        final Intent intent = new Intent(activity, SignUpActivity.class);
        if (bundle != null) {
            intent.putExtra(CREDENTIAL, bundle);
        }
        activity.startActivity(intent);
        activity.finish();
    }

    public static void startSignUp(Activity activity, int fragmentNumber, final String email, final String password) {
        final Bundle bundle = new Bundle();
        bundle.putString(EMAIL, email);
        bundle.putString(PASSWORD, password);
        currentFragment = fragmentNumber;
        startSignUp(activity, bundle);

    }

    @Inject
    SharedPreferences sharedPreferences;
    private final SparseArray<BaseFragment> sparseArray = new SparseArray<>(3);
    private PageIndicatorView pageIndicatorView;
    private static int currentFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TransferXApplication) (getApplication())).getAppComponent().inject(this);
        final Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra(CREDENTIAL);
        setContentView(R.layout.signup_activity_layout);
        pageIndicatorView = findViewById(R.id.page_indicator);
        sparseArray.put(0, new SignUpStepOneFragment());
        sparseArray.put(1, new SignUpStepTwoFragment());
        sparseArray.put(2, new SignUpStepThreeFragment());
        final BaseFragment fragment = sparseArray.get(currentFragment);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, sparseArray.get(currentFragment).getTag());
        ft.commit();
        pageIndicatorView.setSelection(currentFragment);
    }

    @Override
    protected void onDestroy() {
        currentFragment = 0;
        sparseArray.clear();
        super.onDestroy();
    }

    public void showNextOrPreviousFragment(int nextView, Bundle bundle) {
        BaseFragment fragment = sparseArray.get(nextView);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        replaceFragment(fragment, currentFragment - nextView, R.id.container);
        currentFragment = nextView;
        pageIndicatorView.setSelection(nextView);
    }

    @Override
    public void onBackPressed() {
        if (sharedPreferences.getBoolean(PIN_SHOULD_BE_INPUT, false)) {
            return;
        }
        switch (currentFragment) {
            case 0:
                startActivity(new Intent(SignUpActivity.this, WelcomeActivity.class));
                finish();
                break;
            case 1:
                showNextOrPreviousFragment(0, null);
                break;
            case 2:
                showNextOrPreviousFragment(1, null);
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

}
