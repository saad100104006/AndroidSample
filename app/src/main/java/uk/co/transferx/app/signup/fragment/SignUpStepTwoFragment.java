package uk.co.transferx.app.signup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.signup.SignUpActivity;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepTwoFragment extends BaseFragment {

    @Override
    public String tagName() {
        return SignUpStepTwoFragment.class.getSimpleName();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_step_two_fragment_layout, container, false);
        view.findViewById(R.id.next_step).setOnClickListener(v -> ((SignUpActivity) getActivity()).showNextOrPreviousFragment(2));
        return view;
    }


}
