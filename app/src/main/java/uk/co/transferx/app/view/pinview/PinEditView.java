package uk.co.transferx.app.view.pinview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
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

public class PinEditView extends FrameLayout implements TextWatcher, View.OnFocusChangeListener, View.OnClickListener, CustomEditPinText.OnKeyKodEventListener {

    public final static int PIN_MIN_LENGTH = 4;
    public final static int PIN_MAX_LENGTH = 8;
    private final static int HIDE_DELAY = 400;
    private SingleCharView currentText;

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = this::hideChar;

    private List<SingleCharView> listOfChars = new ArrayList<>(8);
    private LinearLayout containerPin;
    private boolean isShoulHandled = true;

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


    public void hideChar() {
        currentText.setVisibility(View.INVISIBLE);
        currentText.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.circle));
        if ((int) currentText.getTag() == (listOfChars.size() - 1)) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentText.getEditChar().getWindowToken(), 0);
        }
        isShoulHandled = true;

    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.pin_view_layout, this);
        if (attrs != null) {

        }

        containerPin = findViewById(R.id.pin_container);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics()), 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics()), 0);
        containerPin.removeAllViews();
        for (int i = 0; i < PIN_MIN_LENGTH; i++) {
            SingleCharView secreteChar = new SingleCharView(getContext());
            secreteChar.setTag(i);
            secreteChar.addTextChangedListener(this);
            secreteChar.setOnFocusChangeListener(this);
            secreteChar.setOnClickListener(this);
            secreteChar.setOnKeyCodEventListener(this);
            listOfChars.add(secreteChar);
            containerPin.addView(secreteChar, lp);
        }

    }

    @Override
    public void onKeyKodEvent(int keyCode, KeyEvent event) {
        int current = (int) currentText.getTag();
        if (current > 0) {
            new Handler().post(() -> currentText.getEditChar().clearFocus());
            currentText = listOfChars.get(current - 1);
            new Handler().post(() -> currentText.getEditChar().requestFocus());
            disableSecurity();
        }
    }

    @Override
    public void onClick(View view) {
        if (isNotLast() && isShoulHandled) {
            disableSecurity();
            showKeyboard();
        }
        if (isLastAndEmpty() && isShoulHandled) {
            disableSecurity();
            showKeyboard();
        }
    }

    private boolean isNotLast() {
        return currentText != null && (((int) currentText.getTag()) != (listOfChars.size() - 1));
    }

    private boolean isLastAndEmpty() {
        return currentText != null && (((int) currentText.getTag()) == (listOfChars.size() - 1) && TextUtils.isEmpty(currentText.getEditChar().getText()));
    }

    private void disableSecurity() {
        currentText.disableSecurity();
        currentText.setVisibility(VISIBLE);

    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(currentText.getEditChar(), InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            mHideHandler.postDelayed(() -> currentText = listOfChars.get((int) view.getTag()), HIDE_DELAY);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (currentText != null && !TextUtils.isEmpty(charSequence)) {
            isShoulHandled = false;
            mHideHandler.postDelayed(mHideRunnable, HIDE_DELAY);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
