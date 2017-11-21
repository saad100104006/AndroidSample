package uk.co.transferx.app.signup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.signin.SignInActivity;
import uk.co.transferx.app.signin.SignInType;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepTreeFragment extends BaseFragment {

    private static final int MAX_PIN_LIGHT = 4;


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
        secondInputLayout.setHint(getString(R.string.confirm_pin));
        setPinSettings(firstInputLayout.getEditText());
        setPinSettings(secondInputLayout.getEditText());
        Button nextButton = view.findViewById(R.id.next_step);
        nextButton.setText(R.string.sign_me_up);
        nextButton.setOnClickListener(view1 -> SignInActivity.starSignInActivity(getActivity(), SignInType.PIN));
        return view;
    }

    private void setPinSettings(EditText editText) {
        if (editText != null) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(MAX_PIN_LIGHT);
            editText.setFilters(filterArray);
        }
    }


}
