package uk.co.transferx.app.welcom;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.welcom.fragment.WelcomeFragment;

/**
 * Created by smilevkiy on 13.11.17.
 */

public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_activity);
        final WelcomeFragment welcomeFragment = new WelcomeFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, welcomeFragment, welcomeFragment.getTag());
        ft.commit();
    }
}
