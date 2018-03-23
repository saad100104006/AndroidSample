package uk.co.transferx.app.tutorial.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.MainActivity;

import static uk.co.transferx.app.util.Constants.DESCRIPTION_ONE;
import static uk.co.transferx.app.util.Constants.DESCRIPTION_TWO;
import static uk.co.transferx.app.util.Constants.LAYOUT;
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
    private int descriptionOne, descriptionTwo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            layout = bundle.getInt(LAYOUT);
            image = bundle.getInt(TUTORIAL_IMG);
            descriptionOne = bundle.getInt(DESCRIPTION_ONE);
            descriptionTwo = bundle.getInt(DESCRIPTION_TWO);
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
            ((TextView) view.findViewById(R.id.description_one)).setText(descriptionOne);
            ((TextView) view.findViewById(R.id.description_two)).setText(descriptionTwo);
            return;
        }
        view.findViewById(R.id.get_started).setOnClickListener(v -> MainActivity.startMainActivity(getActivity()));
    }
}
