package uk.co.transferx.app.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.content.res.AppCompatResources;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    public static void startSignUp(Activity activity) {
        activity.startActivity(new Intent(activity, SignUpActivity.class));
    }

    private static final int DURATION = 500;
    private final SparseArray<BaseFragment> sparseArray = new SparseArray<>(3);
    private PageIndicatorView pageIndicatorView;
    private TextView steps;
    private ImageView arrowBack;
    private static int currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_layout);
        pageIndicatorView = findViewById(R.id.page_indicator);
        steps = findViewById(R.id.steps);
        arrowBack = findViewById(R.id.arrow_back);
        arrowBack.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.abc_ic_ab_back_material));
        arrowBack.setOnClickListener(view -> onBackPressed());
        steps.setText(getString(R.string.steps, currentFragment + 1));
        sparseArray.put(0, new SignUpStepOneFragment());
        sparseArray.put(1, new SignUpStepTwoFragment());
        sparseArray.put(2, new SignUpStepTreeFragment());
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, sparseArray.get(currentFragment), sparseArray.get(currentFragment).getTag());
        ft.commit();
        pageIndicatorView.setSelection(currentFragment);
    }


    public void showNextOrPreviousFragment(int nextView) {
        Slide slideTransition = new Slide(nextView > currentFragment ? Gravity.END : Gravity.START);
        slideTransition.setDuration(DURATION);
        slideTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                arrowBack.setVisibility(currentFragment > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionPause(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionResume(@NonNull Transition transition) {

            }
        });
        BaseFragment fragment = sparseArray.get(nextView);
        fragment.setEnterTransition(slideTransition);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, fragment.getTag());
        ft.commit();
        currentFragment = nextView;
        pageIndicatorView.setSelection(nextView);
        steps.setText(getString(R.string.steps, currentFragment + 1));
    }


    @Override
    public void onBackPressed() {
        switch (currentFragment) {
            case 0:
                super.onBackPressed();
                break;
            case 1:
                showNextOrPreviousFragment(0);
                break;
            case 2:
                showNextOrPreviousFragment(1);
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

}
