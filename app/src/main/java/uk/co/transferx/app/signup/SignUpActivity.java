package uk.co.transferx.app.signup;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.signup.fragment.SignUpStepOneFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepTreeFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepTwoFragment;
import uk.co.transferx.app.welcom.fragment.WelcomeFragment;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpActivity extends BaseActivity {

    private final SparseArray<Fragment> sparseArray = new SparseArray<>(3);

    public static void startSignUp(Activity activity) {
        activity.startActivity(new Intent(activity, SignUpActivity.class));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_layout);
        sparseArray.put(0, new SignUpStepOneFragment());
        sparseArray.put(1, new SignUpStepTwoFragment());
        sparseArray.put(2, new SignUpStepTreeFragment());
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, sparseArray.get(0), WelcomeFragment.TAG);
        ft.commit();
    }


}
