package uk.co.transferx.app.recoverpass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;

/**
 * Created by sergey on 23.11.17.
 */

public class RecoverPasswordFragment extends BaseFragment {

    @Override
    public String tagName() {
        return RecoverPasswordFragment.class.getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recover_password_fragment_layout, container, false);
        view.findViewById(R.id.send_mail_button).setOnClickListener(v -> Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show());
        return view;
    }


}
