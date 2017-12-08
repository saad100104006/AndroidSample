package uk.co.transferx.app.view.pinview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import uk.co.transferx.app.R;

/**
 * Created by smilevkiy on 21.11.17.
 */

public class PinEditView extends FrameLayout implements TextWatcher, View.OnFocusChangeListener, View.OnClickListener, CustomEditPinText.OnKeyBackSpaceListener {

    public final static int PIN_MIN_LENGTH = 4;
    public final static int PIN_MAX_LENGTH = 8;
    private final static int HIDE_DELAY = 100;
    private SingleCharView currentText;
    private PinViewListener pinViewListener;
    private LinearLayout containerPin;
    boolean isShouldHideKeyboard;

    public interface PinViewListener {
        void pinEntered(int[] pin);
    }

    Handler handler = new Handler();
    Runnable mHideRunnable = this::hideChar;

    private List<SingleCharView> listOfChars = new ArrayList<>(8);
    private boolean isShouldHandled = true;
    private int[] pinArray;

    public PinEditView(Context context) {
        super(context);
        init(context, null);
    }

    public PinEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PinEditView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PinEditView, 0, 0);
            isShouldHideKeyboard = typedArray.getBoolean(R.styleable.PinEditView_is_should_hide_keyboard, false);
            typedArray.recycle();
        }
        setSaveEnabled(true);
        ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pin_view_layout, this);
        containerPin = new LinearLayout(context);
        containerPin.setId(View.generateViewId());
        view.addView(containerPin);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, getResources().getDisplayMetrics()), 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, getResources().getDisplayMetrics()), 0);
        containerPin.removeAllViews();
        for (int i = 0; i < PIN_MIN_LENGTH; i++) {
            final SingleCharView secreteChar = new SingleCharView(getContext());
            if (isFocusable() && i == 0) {
                handler.post(() -> secreteChar.getEditChar().requestFocus());
            }
            secreteChar.setId(View.generateViewId());
            secreteChar.setTag(i);
            secreteChar.getEditChar().setFocusable(true);
            secreteChar.addTextChangedListener(this);
            secreteChar.setOnFocusChangeListener(this);
            secreteChar.setOnClickListener(this);
            secreteChar.setOnKeyBackSpaceListener(this);
            listOfChars.add(secreteChar);
            containerPin.addView(secreteChar, lp);
        }
        pinArray = new int[PIN_MIN_LENGTH];
        cleanPinArray();
        currentText = listOfChars.get(0);
    }


    private void cleanPinArray() {
        for (int i = 0; i < pinArray.length; i++) {
            pinArray[i] = -1;
        }
    }

    private void savePin(int index, short number) {
        pinArray[index] = number;
        checkPinAndHideKeyboard();
    }

    public void setPinViewListener(PinViewListener pinViewListener) {
        this.pinViewListener = pinViewListener;
    }

    private void checkPinAndHideKeyboard() {
        for (int p : pinArray) {
            if (p == -1) {
                return;
            }
        }
        if (isShouldHideKeyboard) {
            hideKeyboard();
        }
        if (pinViewListener != null) {
            pinViewListener.pinEntered(pinArray);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(currentText.getEditChar().getWindowToken(), 0);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        PinEditSaveState pinEditSaveState = new PinEditSaveState(superState);
        pinEditSaveState.pinValues = pinArray;
        return pinEditSaveState;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        PinEditSaveState pinEditSaveState = (PinEditSaveState) state;
        super.onRestoreInstanceState(pinEditSaveState.getSuperState());
        pinArray = pinEditSaveState.pinValues;
        boolean isFilledPinView = true;
        for (int i = 0; i < PIN_MIN_LENGTH; i++) {
            int v = pinArray[i];
            final SingleCharView singleCharView = (SingleCharView) containerPin.getChildAt(i);
            if (v != -1) {
                singleCharView.setSecurity();
                currentText = singleCharView;
            } else {
                isFilledPinView = false;
                singleCharView.disableSecurity();

            }
        }
        if (pinViewListener != null && isFilledPinView) {
            pinViewListener.pinEntered(pinArray);
            return;
        }

        if (currentText != null && !isPinArrayEmpty()) {
            final int position = (int) (currentText.getTag());
            handler.post(() -> listOfChars.get(position + 1).getEditChar().requestFocus());
        }
    }

    private void removePinValue(int index) {
        pinArray[index] = -1;
    }

    public void hideChar() {
        savePin(((int) currentText.getTag()), Short.valueOf(currentText.getEditChar().getText().toString().replaceAll("\\s+", "")));
        currentText.setSecurity();
        if ((int) currentText.getTag() == (listOfChars.size() - 1) && isShouldHideKeyboard) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentText.getEditChar().getWindowToken(), 0);
        }
        isShouldHandled = true;
    }

    @Override
    public void onBackSpaceEvent() {
        int currentNumber = (int) currentText.getTag();
        if (isLastWrongNumber(currentNumber)) {
            resetError();
            disableSecurityAndRemoveValue(currentNumber);
            return;
        }
        if (currentNumber > 0) {
            currentText.getEditChar().clearFocus();
            currentText = listOfChars.get(currentNumber - 1);
            disableSecurityAndRemoveValue(currentNumber - 1);
            handler.post(() -> currentText.getEditChar().requestFocus());
        } else {
            handler.post(() -> currentText.getEditChar().requestFocus());
        }
    }

    private boolean isLastWrongNumber(int currentNumber) {
        return currentNumber == pinArray.length - 1 && !currentText.getEditChar().hasFocus();
    }

    private void refillInput() {
        int current = (int) currentText.getTag();
        handler.post(() -> currentText.getEditChar().requestFocus());
        currentText.getEditChar().getText().clear();
        disableSecurityAndRemoveValue(current);
        resetError();
        showKeyboard();
    }

    public void setError() {
        for (SingleCharView singleCharView : listOfChars) {
            singleCharView.setError();
        }
        handler.postDelayed(() -> listOfChars.get(listOfChars.size() - 1).setError(), HIDE_DELAY);
    }


    public void resetError() {
        for (int i = 0; i < listOfChars.size() - 1; i++) {
            listOfChars.get(i).setSecurity();
        }
    }

    @Override
    public void onClick(View view) {
        if (isNotLast() && isShouldHandled) {
            showKeyboard();
        }
        if (isLastAndEmpty() && isShouldHandled) {
            showKeyboard();
        }
        if (isShouldBeReInputted() && isShouldHandled) {
            refillInput();
        }

    }


    private boolean isShouldBeReInputted() {
        boolean result = true;
        for (int pin : pinArray) {
            if (pin == -1) {
                result = false;
            }
        }
        return result;
    }

    private boolean isPinArrayEmpty() {
        for (int number : pinArray) {
            if (number != -1) {
                return false;
            }
        }
        return true;
    }

    private boolean isNotLast() {
        return currentText != null && (((int) currentText.getTag()) != (listOfChars.size() - 1));
    }

    private boolean isLastAndEmpty() {
        return currentText != null && (((int) currentText.getTag()) == (listOfChars.size() - 1) && TextUtils.isEmpty(currentText.getEditChar().getText()));
    }

    private void disableSecurityAndRemoveValue(int charPosition) {
        listOfChars.get(charPosition).disableSecurity();
        removePinValue(charPosition);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(currentText.getEditChar(), InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            handler.postDelayed(() -> currentText = listOfChars.get((int) view.getTag()), HIDE_DELAY);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (currentText != null && !TextUtils.isEmpty(charSequence.toString().replaceAll("\\s+", ""))) {
            isShouldHandled = false;
            handler.postDelayed(mHideRunnable, HIDE_DELAY);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
