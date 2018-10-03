package uk.co.transferx.app.ui.signinpin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import uk.co.transferx.app.ui.base.BaseActivity;
import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.ui.signinpin.fragment.SignInPinFragment;

/**
 * Created by sergey on 19.11.17.
 */

public class SignInPinActivity extends BaseActivity {

    private final static String TYPE_SIGNIN = "type_signin";


    public static void starSignInActivity(Activity activity) {

        Intent intent = new Intent(activity, SignInPinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        //   activity.finish();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signpin_activity_layout);
        BaseFragment signInFragment = new SignInPinFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, signInFragment, signInFragment.getTag());
        ft.commit();
    }




    @Override
    public void onBackPressed() {
    }
}
