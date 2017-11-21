package uk.co.transferx.app.view.pinview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.transferx.app.R;
import uk.co.transferx.app.view.pinview.CharView;

/**
 * Created by sergey on 19.11.17.
 */

public class SingleCharEditTextView extends FrameLayout {

    public interface OnFilledListener {
        void onFilled(char[] text);
    }

    public final static int MIN_LENGTH = 4;
    public final static int MAX_LENGTH = 8;


    protected LinearLayout charHolderView;
    protected List<CharView> charViewList;
    protected int charLength = MIN_LENGTH;
    protected char[] text = new char[MAX_LENGTH];
    protected int textLength = 0;

    private OnFilledListener onFilledListener;
    private float textSize = 0;
    private int charWidth = 0;

    protected void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.single_char_edit_text, this);
        charHolderView = findViewById(R.id.pin_char_view_holder);
        updateView(attrs);
    }

    public SingleCharEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SingleCharEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SingleCharEditTextView(Context context) {
        super(context);
        init(context, null);
    }

    public void updateView(AttributeSet attrs) {
        reset();

        if (attrs != null) {
//            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.single_char_items);
//            textSize = typedArray.getDimensionPixelSize(R.styleable.single_char_items_char_font_size, R.dimen.text_size_very_small);
//            charWidth = typedArray.getDimensionPixelSize(R.styleable.single_char_items_char_width, R.dimen.char_size_pesel);
//            typedArray.recycle();
        }
        for (int i = 0; i <= getMaxCharLength() - 1; i++) {
            CharView pinCharView = new CharView(getContext());

            if (textSize > 0) {
                pinCharView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                pinCharView.setCharWidth(charWidth);
            }

            charViewList.add(pinCharView);
            charHolderView.addView(pinCharView);
        }
    }

    public void reset() {
        charHolderView.removeAllViews();
        charViewList = new ArrayList<>();
        text = new char[MAX_LENGTH];
    }

    public void addChar(char character) {
        if (textLength < getMaxCharLength()) {
            text[textLength] = character;
            charViewList.get(textLength).setText(character + "");
            textLength++;
            if (textLength == getMaxCharLength() && onFilledListener != null) {
                onFilledListener.onFilled(getText());
            }
        }
    }

    public char[] getText() {
        return Arrays.copyOf(text, textLength);
    }

    public void removeChar() {
        if (textLength > 0) {
            text[--textLength] = 0;
            charViewList.get(textLength).setText("");
            charViewList.get(textLength).setSecretColor(android.R.color.transparent);
            charViewList.get(textLength).applySecretColor();
        }
    }

    public void setCharLength(@IntRange(from = 1, to = MAX_LENGTH) int length) {
        charLength = length;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        View view = (View) getParent();
        float scale = displayMetrics.density;
        int widthDisplay = (int) (displayMetrics.widthPixels / displayMetrics.density);
        int lengthAllElementsInDp = ((int) (charWidth / scale)) * length;
        int paddingLeftDp = (int) (view.getPaddingLeft() / scale);
        int paddingRightDp = (int) (view.getPaddingRight() / scale);
        int widthElementsWithPadding = lengthAllElementsInDp + paddingLeftDp + paddingRightDp;
        if (widthElementsWithPadding > widthDisplay) {
            charWidth = (int) (((float) charWidth) * (float) widthDisplay / (float) widthElementsWithPadding);
        }
        updateView(null);
    }

    public int getMaxCharLength() {
        return charLength;
    }

    public int getCharLength() {
        return textLength;
    }

    public void setColor(@ColorRes int color) {
        for (CharView pinCharView : charViewList) {
            pinCharView.setColor(color);
            if (TextUtils.isEmpty(pinCharView.getText())) {
                pinCharView.setSecretColor(android.R.color.transparent);
            } else {
                pinCharView.setSecretColor(color);
            }
        }
    }

    public void setTextColor(@ColorRes int color) {
        for (CharView pinCharView : charViewList) {
            pinCharView.setTextColor(color);
        }
    }

    public void resetState() {
        for (CharView charView : charViewList) {
        }
    }

    public void setOnFilledListener(OnFilledListener onFilledListener) {
        this.onFilledListener = onFilledListener;
    }

    public void setSecretColor(@ColorRes int color) {
        for (CharView pinCharView : charViewList) {
            pinCharView.setSecretColor(color);
        }
    }

    public void applySecretColorForNotEmptyChars() {
        for (CharView pinCharView : charViewList) {
            if (!TextUtils.isEmpty(pinCharView.getText())) {
                pinCharView.applySecretColor();
            }
        }
    }
}
