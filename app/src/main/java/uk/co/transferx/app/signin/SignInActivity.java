package uk.co.transferx.app.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.signin.fragment.SignInEmailFragment;
import uk.co.transferx.app.signin.fragment.SignInPinFragment;

/**
 * Created by sergey on 19.11.17.
 */

public class SignInActivity extends BaseActivity {

    private final static String TYPE_SIGNIN = "type_signin";


    public static void starSignInActivity(Activity activity) {

        Intent intent = new Intent(activity, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        //   activity.finish();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity_layout);
        BaseFragment signInFragment = new SignInPinFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, signInFragment, signInFragment.getTag());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
    }
}
