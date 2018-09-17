package uk.co.transferx.app.ui.customview;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import uk.co.transferx.app.R;

import static android.app.Activity.RESULT_OK;
import static uk.co.transferx.app.util.Constants.DELETE;
import static uk.co.transferx.app.util.Constants.EMPTY;

public class ConfirmationDialogFragment extends DialogFragment {

    public static final String MESSAGE = "message";
    public static final String ADDITIONAL_DATA = "additional_data";
    public static final String POSITION = "position";
    private String message;
    private int position;
    private String id;

    public interface CallBackInterfaceDialog {
        void onSucces();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            message = bundle.getString(MESSAGE, EMPTY);
            position = bundle.getInt(POSITION, -1);
            id = bundle.getString(ADDITIONAL_DATA, null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirmation_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.message)).setText(message);
        view.findViewById(R.id.button_no).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.button_yes).setOnClickListener(v -> clickOkButton());
    }

    private void clickOkButton() {
        Fragment fragment = getTargetFragment();
        Intent intent;
        if (fragment != null) {
            intent = new Intent();
            intent.putExtra(ADDITIONAL_DATA, id);
            intent.putExtra(POSITION, position);
            fragment.onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        } else {
            intent = new Intent(DELETE);
            intent.putExtra(POSITION, position);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                    intent);
        }

        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
