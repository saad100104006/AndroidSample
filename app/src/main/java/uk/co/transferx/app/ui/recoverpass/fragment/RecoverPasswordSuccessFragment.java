package uk.co.transferx.app.ui.recoverpass.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;

/**
 * Created by sergey on 19/03/2018.
 */

public class RecoverPasswordSuccessFragment extends BaseFragment {
    @Override
    public String tagName() {
        return RecoverPasswordSuccessFragment.class.getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recover_password_success_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.sign_in).setOnClickListener(v -> getActivity().finish());
        view.findViewById(R.id.close).setOnClickListener(v -> getActivity().finish());
    }
}
