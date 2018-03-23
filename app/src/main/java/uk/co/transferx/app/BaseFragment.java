package uk.co.transferx.app;


import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by smilevkiy on 13.11.17.
 */

public abstract class BaseFragment extends Fragment {

    public abstract String tagName();

    protected void setStatusOfError(final EditText textInputEditText, final TextView label, @ColorRes int color) {
        Drawable draw = textInputEditText.getBackground();
        if (draw != null) {
            draw.mutate().setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
        }
        textInputEditText.setTextColor(getResources().getColor(color));
        label.setTextColor(ContextCompat.getColor(label.getContext(), color));
        for (Drawable drawable : textInputEditText.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textInputEditText.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    protected void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
