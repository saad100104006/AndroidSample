package uk.co.transferx.app;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by smilevkiy on 15.11.17.
 */

public abstract class BaseActivity extends AppCompatActivity {


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
