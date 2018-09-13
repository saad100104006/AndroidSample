package uk.co.transferx.app.ui.welcom;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import uk.co.transferx.app.ui.base.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.ui.welcom.fragment.WelcomeFragment;

/**
 * Created by smilevkiy on 13.11.17.
 */

public class WelcomeActivity extends BaseActivity {

    public static void startWelcomeActivity(Activity activity) {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

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
