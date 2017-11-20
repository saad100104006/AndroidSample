package uk.co.transferx.app.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.TextView;

import uk.co.transferx.app.R;

/**
 * Created by sergey on 19.11.17.
 */

public class PinView extends SingleCharEditTextView {

    public final static int PIN_MIN_LENGTH = 4;
    public final static int PIN_MAX_LENGTH = 8;

    private final static int HIDE_DELAY = 1000;

    TextView hint;

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = () -> hideChars(textLength);

    public PinView(Context context) {
        super(context);
        init(context, null);
    }

    public PinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        hint = findViewById(R.id.hint);
    }

    @Override
    public void setColor(@ColorRes int color) {
        super.setColor(color);
        hint.setTextColor(getResources().getColor(color));
    }

    @Override
    public void addChar(char character) {
        super.addChar(character);
        hideChars(textLength - 1);
        mHideHandler.postDelayed(mHideRunnable, HIDE_DELAY);
    }

    private void hideChars(int index) {
        for (int i = 0; i < index; i++) {
            charViewList.get(i).hideChar();
        }
    }

    @Override
    public void removeChar() {
        super.removeChar();
        CharView charView = charViewList.get(textLength);
        charView.showChar();
    }

    public void removeAllChars() {
        for (int i = 0; i < charViewList.size(); i++) {
            removeChar();
        }
    }

    public void resetColors() {
        resetColors(R.color.white, R.color.colorAccent);
    }

    public void resetColors(@ColorRes int textColor, @ColorRes int underlineColor) {
        setTextColor(textColor);
    }

    @Override
    public void setCharLength(@IntRange(from = PIN_MIN_LENGTH, to = PIN_MAX_LENGTH) int length) {
        super.setCharLength(length);
    }

    public void clearPin() {
        for (int i = 0; i < text.length; i++) {
            text[i] = 0;
        }
        textLength = 0;
    }

    public void setHint(@StringRes int hintMessage) {
        hint.setVisibility(VISIBLE);
        hint.setText(hintMessage);
    }

}
