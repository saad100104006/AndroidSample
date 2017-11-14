package uk.co.transferx.app.welcom;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import uk.co.transferx.app.R;
import uk.co.transferx.app.welcom.fragment.WelcomeFragment;

/**
 * Created by smilevkiy on 13.11.17.
 */

public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WelcomeFragment welcomeFragment = new WelcomeFragment();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, welcomeFragment, WelcomeFragment.TAG);
        ft.commit();
    }
}
