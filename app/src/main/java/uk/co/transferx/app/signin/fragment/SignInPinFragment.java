package uk.co.transferx.app.signin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;

/**
 * Created by sergey on 19.11.17.
 */

public class SignInPinFragment extends BaseFragment {
    @Override
    public String tagName() {
        return SignInPinFragment.class.getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.signin_pin_fragment, container, false);
        view.findViewById(R.id.next_step).setOnClickListener(v -> Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show());
        return view;
    }


}
