package uk.co.transferx.app;


import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

/**
 * Created by smilevkiy on 13.11.17.
 */

public abstract class BaseFragment extends Fragment {

    public abstract String tagName();

    protected void setStatusOfError(final TextInputEditText textInputEditText, final TextView label, @ColorRes int color) {
        textInputEditText.getBackground().mutate().setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
        textInputEditText.setTextColor(getResources().getColor(color));
        label.setTextColor(ContextCompat.getColor(label.getContext(), color));
        for (Drawable drawable : textInputEditText.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textInputEditText.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

}
