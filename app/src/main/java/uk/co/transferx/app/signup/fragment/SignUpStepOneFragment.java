package uk.co.transferx.app.signup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.signup.SignUpActivity;

/**
 * Created by smilevkiy on 15.11.17.
 */

public class SignUpStepOneFragment extends BaseFragment {


    @Override
    public String tagName() {
        return SignUpStepOneFragment.class.getSimpleName();
    }

    private TextInputLayout firstInputLayout;
    private TextInputLayout secondInputLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_step_fragment_layout, container, false);
        firstInputLayout = view.findViewById(R.id.first_layout);
        secondInputLayout = view.findViewById(R.id.second_layout);
        ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.title_one));
        firstInputLayout.setHint(getString(R.string.first_name));
        firstInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        secondInputLayout.setHint(getString(R.string.last_name));
        secondInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        view.findViewById(R.id.next_step).setOnClickListener(v -> ((SignUpActivity) getActivity()).showNextOrPriviosFragment(1));
        return view;
    }

}
