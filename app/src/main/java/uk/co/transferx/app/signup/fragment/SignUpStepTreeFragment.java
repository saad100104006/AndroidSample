package uk.co.transferx.app.signup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepTreeFragment extends BaseFragment {

    @Override
    public String tagName() {
        return SignUpStepTreeFragment.class.getSimpleName();
    }

    private TextInputLayout firstInputLayout;
    private TextInputLayout secondInputLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_step_fragment_layout, container, false);
        firstInputLayout = view.findViewById(R.id.first_layout);
        secondInputLayout = view.findViewById(R.id.second_layout);
        ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.title_three));
        firstInputLayout.setHint(getString(R.string.pin));
        firstInputLayout.getEditText().setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        secondInputLayout.setHint(getString(R.string.confirm_pin));
        secondInputLayout.getEditText().setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        Button nextButton = view.findViewById(R.id.next_step);
        nextButton.setText(R.string.sign_me_up);
        return view;
    }


}
