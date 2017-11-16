package uk.co.transferx.app.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Slide;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.Gravity;

import com.rd.PageIndicatorView;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.signup.fragment.SignUpStepOneFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepTreeFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepTwoFragment;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpActivity extends BaseActivity {

    private final SparseArray<BaseFragment> sparseArray = new SparseArray<>(3);

    public static void startSignUp(Activity activity) {
        activity.startActivity(new Intent(activity, SignUpActivity.class));
    }

    private PageIndicatorView pageIndicatorView;
    private static int currentFragment = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_layout);
        pageIndicatorView = findViewById(R.id.page_indicator);
        sparseArray.put(0, new SignUpStepOneFragment());
        sparseArray.put(1, new SignUpStepTwoFragment());
        sparseArray.put(2, new SignUpStepTreeFragment());
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, sparseArray.get(0), sparseArray.get(0).getTag());
        ft.commit();
        currentFragment = 0;
        pageIndicatorView.setSelection(0);
    }


    public void showNextOrPriviosFragment(int nextView) {
        Slide slideTransition = new Slide(nextView > currentFragment ? Gravity.END : Gravity.START);
        slideTransition.setDuration(500);
        BaseFragment fragment = sparseArray.get(nextView);
        fragment.setEnterTransition(slideTransition);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, fragment.getTag());
        ft.commit();
        currentFragment = nextView;
        pageIndicatorView.setSelection(nextView);
    }


    @Override
    public void onBackPressed() {
        switch (currentFragment) {
            case 0:
                super.onBackPressed();
                break;
            case 1:
                showNextOrPriviosFragment(0);
                break;
            case 2:
                showNextOrPriviosFragment(1);
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

}
