package uk.co.transferx.app.signin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.MainActivity;
import uk.co.transferx.app.recoverpass.RecoverPasswordActivity;
import uk.co.transferx.app.view.pinview.PinEditView;

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
        ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.one_step_away));
        TextView forgotPass = view.findViewById(R.id.terms);
        String forgotPassword = getString(R.string.forgot_password) + "?";
        PinEditView pinEditView = view.findViewById(R.id.pin_input);
        pinEditView.setPinViewListener(pin -> {
            Activity activity = getActivity();
            if (activity != null) {
                MainActivity.startMainActivity(activity);
                activity.finish();

            }
        });
        forgotPass.setText(forgotPassword.toUpperCase());
        forgotPass.setOnClickListener(v -> RecoverPasswordActivity.starRecoverPasswordActivity(getActivity()));
        view.findViewById(R.id.next_step).setOnClickListener(v -> Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show());
        return view;
    }


}
