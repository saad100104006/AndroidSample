package uk.co.transferx.app.ui.tutorial.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.ui.homescreen.MainActivity;
import uk.co.transferx.app.ui.mainscreen.MainActivityOld;
import uk.co.transferx.app.ui.recipients.addrecipients.AddRecipientsActivity;
import uk.co.transferx.app.ui.recipients.addrecipients.Mode;
import uk.co.transferx.app.ui.settings.profile.wallet.AddCardActivity;
import uk.co.transferx.app.ui.settings.profile.wallet.CardMode;
import uk.co.transferx.app.ui.tutorial.TutorialActivity;

import static uk.co.transferx.app.util.Constants.BUTTON_TEXT;
import static uk.co.transferx.app.util.Constants.DESCRIPTION_ONE;
import static uk.co.transferx.app.util.Constants.DESCRIPTION_TWO;
import static uk.co.transferx.app.util.Constants.LAYOUT;
import static uk.co.transferx.app.util.Constants.MODE;
import static uk.co.transferx.app.util.Constants.TUTORIAL_IMG;

/**
 * Created by sergey on 21/03/2018.
 */

public class TutorialFragment extends BaseFragment {

    @Override
    public String tagName() {
        return TutorialFragment.class.getSimpleName();
    }

    @DrawableRes
    private int image = -1;
    @LayoutRes
    private int layout;
    @StringRes
    private int descriptionOne, descriptionTwo, buttonText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            layout = bundle.getInt(LAYOUT);
            image = bundle.getInt(TUTORIAL_IMG);
            descriptionOne = bundle.getInt(DESCRIPTION_ONE);
            descriptionTwo = bundle.getInt(DESCRIPTION_TWO);
            buttonText = bundle.getInt(BUTTON_TEXT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (layout == R.layout.tutorial_fragment_layout) {
            ((ImageView) view.findViewById(R.id.tutorial_img)).setImageDrawable(ContextCompat.getDrawable(getContext(), image));
            ((TextView) view.findViewById(R.id.title)).setText(descriptionOne);
            ((TextView) view.findViewById(R.id.description)).setText(descriptionTwo);
            return;
        }
        if (layout == R.layout.tutorial_fragment_layout_last) {
            ((ImageView) view.findViewById(R.id.tutorial_img)).setImageDrawable(ContextCompat.getDrawable(getContext(), image));
            ((TextView) view.findViewById(R.id.title)).setText(descriptionOne);
            ((TextView) view.findViewById(R.id.description)).setText(descriptionTwo);
            ((Button) view.findViewById(R.id.action_button)).setText(buttonText);
            if (buttonText == R.string.add_recipientss) {
                view.findViewById(R.id.action_button).setOnClickListener(v -> {
                    final Intent intent = new Intent(getContext(), AddRecipientsActivity.class);
                    intent.putExtra(MODE, Mode.ADD.ordinal());
                    startActivity(intent);
                });
                view.findViewById(R.id.skip).setOnClickListener(v -> ((TutorialActivity) getActivity()).skipStep());
                return;
            }
            view.findViewById(R.id.action_button).setOnClickListener(v -> {
                final Intent intent = new Intent(getContext(), AddCardActivity.class);
                intent.putExtra(MODE, CardMode.ADD.ordinal());
                startActivity(intent);
            });
            view.findViewById(R.id.skip).setOnClickListener(v -> MainActivity.Companion.startMainActivity(getActivity()));

        }
    }
}
